package cn.xlystar.parse.ammswap;

import cn.xlystar.entity.UniswapEvent;
import cn.xlystar.entity.TransferEvent;
import cn.xlystar.helpers.ChainConfig;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class AMMSwapDataProcessFull {

    /**
     * 格式化最后的返回值
     */
    public static List<Map<String, String>> parseFullUniswap(String sender, ChainConfig conf, String logs, String hash, List<TransferEvent> tftxs, String price) throws IOException {
        // 解析
        List<UniswapEvent> uniswapEvents = parseAllUniSwapLogs(sender, conf, logs, hash, tftxs);
        // 后缀处理
        return processSwap(uniswapEvents, new ArrayList<>(), conf, price);
    }

    /**
     * 格式化最后的返回值
     */
    public static List<Map<String, String>> parseSolanaSwap(ChainConfig conf, String originSender, String hash, List<UniswapEvent> swapEvents, List<TransferEvent> transferEvents, String price) {
        // 解析 solana swap
        List<UniswapEvent> uniswapEvents = getSolanaSwapEvents(conf, originSender, hash, swapEvents, transferEvents);
        // 后缀处理
        return processSwap(uniswapEvents, new ArrayList<>(), conf, price);
    }

    private static List<Map<String, String>> processSwap(List<UniswapEvent> swapEvents, List<Map<String, String>> finalSwap, ChainConfig conf, final String price) {
        swapEvents.forEach(t -> {
                    // 非 wcoin 币对处理
                    if (!CollectionUtils.isEmpty(t.getRawSwapLog()) && !StringUtils.isEmpty(price)) {
                        if (conf.getChainId().equals("3")) {
                            t.getRawSwapLog().forEach(u -> convertToSol(conf, u, new BigDecimal(price)));
                        } else {
                            t.getRawSwapLog().forEach(u -> convertToNative(conf, u, new BigDecimal(price)));
                        }
                    }
                    if (t.getTokenIn() != null && t.getTokenOut() != null && !t.getTokenIn().equals(conf.getWCoinAddress()) && !t.getTokenOut().equals(conf.getWCoinAddress())) {
                        switch (conf.getChainId()) {
                            case "3":
                                if (convertToSol(conf, t, new BigDecimal(price))) break;
                            default:
                                List<UniswapEvent> connectedPools = t.getConnectedPools();
                                if (connectedPools != null && connectedPools.size() > 0) {
                                    UniswapEvent.UniswapEventBuilder ethBuyUniswap = null;
                                    UniswapEvent.UniswapEventBuilder ethSellUniswap = null;
                                    for (int i = 0; i < connectedPools.size(); i++) {
                                        UniswapEvent uniswapEvent = connectedPools.get(i);
                                        if (uniswapEvent.getTokenOut().equals(conf.getWCoinAddress())) {
                                            // sell
                                            ethSellUniswap = UniswapEvent.builder()
                                                    .sender(t.getSender())
                                                    .to(t.getSender())
                                                    .tokenIn(t.getTokenIn())
                                                    .amountIn(t.getAmountIn())
                                                    .tokenOut(conf.getWCoinAddress())
                                                    .amountOut(uniswapEvent.getAmountOut())
                                                    .pair(t.getPair())
                                                    .fromMergedTransferEvent(t.getFromMergedTransferEvent())
                                                    .toMergedTransferEvent(t.getToMergedTransferEvent())
                                                    .connectedPools(t.getConnectedPools())
                                                    .rawSwapLog(t.getRawSwapLog())
                                                    .version(t.getVersion())
                                                    .errorMsg(t.getErrorMsg());
                                            finalSwap.add(swapResultStruct(conf, ethSellUniswap.build()));

                                            // buy eth币对
                                            ethBuyUniswap = UniswapEvent.builder()
                                                    .sender(t.getSender())
                                                    .to(t.getTo())
                                                    .tokenIn(conf.getWCoinAddress())
                                                    .amountIn(uniswapEvent.getAmountOut())
                                                    .tokenOut(t.getTokenOut())
                                                    .amountOut(t.getAmountOut())
                                                    .pair(t.getPair())
                                                    .fromMergedTransferEvent(t.getFromMergedTransferEvent())
                                                    .toMergedTransferEvent(t.getToMergedTransferEvent())
                                                    .connectedPools(t.getConnectedPools())
                                                    .rawSwapLog(t.getRawSwapLog())
                                                    .version(t.getVersion())
                                                    .errorMsg(t.getErrorMsg());

                                            finalSwap.add(swapResultStruct(conf, ethBuyUniswap.build()));
                                            return;
                                        }

                                    }
                                }
                                // 不存在 coin 的池子
                                t.setAmountOut(BigInteger.ZERO);
                                t.setTokenOut(conf.getWCoinAddress());
                        }
                    }

                    finalSwap.add(swapResultStruct(conf, t));
                }
        );
        Set<Integer> logIndexRange = new HashSet<>();
        int minLogIndex = Integer.MAX_VALUE;
        for (int i = 0; i < finalSwap.size(); i++) {
            Map<String, String> entry = finalSwap.get(i);
            if (entry.get("logIndex") == null) continue;

            Integer logIndex = Integer.valueOf(entry.get("logIndex"));
            logIndexRange.add(logIndex);
            minLogIndex = Math.min(minLogIndex, logIndex);
        }

        // 设定初始的 logIndex 值，从 0 开始自增
        int nextLogIndex = 0;
        for (int i = 0; i < finalSwap.size(); i++) {
            Map<String, String> map = finalSwap.get(i);
            if (map.get("logIndex") == null) {
                // 找到不重复的 logIndex 值
                while (logIndexRange.contains(nextLogIndex)) {
                    nextLogIndex++;
                }
                // 给当前 map 设置 logIndex 值
                map.put("logIndex", String.valueOf(nextLogIndex));
                // 更新 logIndexRange
                logIndexRange.add(nextLogIndex);
                nextLogIndex++;
            }
        }

        // 删除有问题的 swap
        for (int i = 0, len = finalSwap.size(); i < len; i++) {
            Map<String, String> t = finalSwap.get(i);
            if (t.get("errorMsg") != null || t.get("tokenIn") == null || t.get("tokenOut") == null) {
                finalSwap.remove(i);
                len--;
                i--;
            }
        }
        return finalSwap;
    }

    private static boolean convertToNative(ChainConfig conf, UniswapEvent t, BigDecimal price) {
        String wcoin = conf.getChainConf().get("wcoin").asText();
        String wcoinDecimals = conf.getTokens().get(wcoin).get("scale").asText();
        String wcoinAddress = conf.getTokens().get(wcoin).get("address").asText();
        if (t.getTokenIn() == null || t.getTokenOut() == null || t.getTokenIn().equals(wcoinAddress) || t.getTokenOut().equals(wcoinAddress)) return false;
        JsonNode usdc = conf.getTokens().get("USDC");
        JsonNode usdt = conf.getTokens().get("USDT");
        JsonNode usd1 = conf.getTokens().get("USD1");
        boolean hasUsdc = false;
        boolean hasUsdt = false;
        boolean hasUsd1 = false;
        String usdcDecimals = "";
        String usdcAddress = "";
        String usdtDecimals = "";
        String usdtAddress = "";
        String usd1Decimals = "";
        String usd1Address = "";
        if (usdc != null) {
            usdcAddress = usdc.get("address").asText();
            usdcDecimals = usdc.get("scale").asText();
            hasUsdc = usdcAddress.equals(t.getTokenIn()) || usdcAddress.equals(t.getTokenOut());
        }
        if (usd1 != null) {
            usd1Address = usd1.get("address").asText();
            usd1Decimals = usd1.get("scale").asText();
            hasUsd1 = usd1Address.equals(t.getTokenIn()) || usd1Address.equals(t.getTokenOut());
        }
        if (usdt != null) {
            usdtAddress = usdt.get("address").asText();
            usdtDecimals = usdt.get("scale").asText();
            hasUsdt = usdtAddress.equals(t.getTokenIn()) || usdtAddress.equals(t.getTokenOut());
        }

        String tokenAddress = "";
        String decimals = "";

        if (hasUsdc) {
            tokenAddress = usdcAddress;
            decimals = usdcDecimals;
        } else if (hasUsdt) {
            tokenAddress = usdtAddress;
            decimals = usdtDecimals;
        } else if (hasUsd1) {
            tokenAddress = usd1Address;
            decimals = usd1Decimals;
        }

        if (StringUtils.isEmpty(tokenAddress)) return false;

        BigInteger tokenAmount = t.getTokenIn().equals(tokenAddress) ? t.getAmountIn() : t.getAmountOut();
        BigInteger wcoinAmount = new BigDecimal(tokenAmount).divide(price, 20, RoundingMode.HALF_UP).multiply(new BigDecimal(wcoinDecimals)).divide(new BigDecimal(decimals)).toBigInteger();
        if (t.getTokenIn().equals(tokenAddress)) {
            t.setTokenIn(conf.getWCoinAddress());
            t.setAmountIn(wcoinAmount);
        } else {
            t.setTokenOut(conf.getWCoinAddress());
            t.setAmountOut(wcoinAmount);
        }

        return true;
    }

    private static boolean convertToSol(ChainConfig conf, UniswapEvent t, BigDecimal price) {
        String wcoinDecimals = conf.getTokens().get(conf.getChainConf().get("wcoin").asText()).get("scale").asText();
        String usdcAddress = conf.getTokens().get("USDC").get("address").asText();
        String usdcDecimals = conf.getTokens().get("USDC").get("scale").asText();
        String usdtAddress = conf.getTokens().get("USDT").get("address").asText();
        String usdtDecimals = conf.getTokens().get("USDT").get("scale").asText();
        boolean hasUsdc = usdcAddress.equals(t.getTokenIn()) || usdcAddress.equals(t.getTokenOut());
        boolean hasUsdt = usdtAddress.equals(t.getTokenIn()) || usdtAddress.equals(t.getTokenOut());

        String tokenAddress = "";
        String decimals = "";

        if (hasUsdc) {
            tokenAddress = usdcAddress;
            decimals = usdcDecimals;
        } else if (hasUsdt) {
            tokenAddress = usdtAddress;
            decimals = usdtDecimals;
        }

        if (StringUtils.isEmpty(tokenAddress)) return false;

        BigInteger tokenAmount = t.getTokenIn().equals(tokenAddress) ? t.getAmountIn() : t.getAmountOut();
        BigInteger wcoinAmount = new BigDecimal(tokenAmount).divide(price, 20, RoundingMode.HALF_UP).multiply(new BigDecimal(wcoinDecimals)).divide(new BigDecimal(decimals)).toBigInteger();
        if (t.getTokenIn().equals(tokenAddress)) {
            t.setTokenIn(conf.getWCoinAddress());
            t.setAmountIn(wcoinAmount);
        } else {
            t.setTokenOut(conf.getWCoinAddress());
            t.setAmountOut(wcoinAmount);
        }

        return true;
    }

    private static HashMap<String, String> swapResultStruct(ChainConfig conf, UniswapEvent swap) {
        HashMap<String, String> map = new HashMap<>();
        map.put("caller", swap.getSender());
        map.put("methodId", "others");
        map.put("to", swap.getTo());
        map.put("amountIn", swap.getAmountIn() == null ? null : swap.getAmountIn().toString());
        map.put("amountOut", swap.getAmountOut() == null ? null : swap.getAmountOut().toString());
        map.put("tokenIn", swap.getTokenIn());
        map.put("tokenOut", swap.getTokenOut());
        map.put("pair", swap.getPair().toString());
        map.put("logIndex", swap.getLogIndex() == null ? null : swap.getLogIndex().toString());
        map.put("fromMergedTransferEvent", swap.getFromMergedTransferEvent() == null ? null : swap.getFromMergedTransferEvent().toString());
        map.put("toMergedTransferEvent", swap.getToMergedTransferEvent() == null ? null : swap.getToMergedTransferEvent().toString());
        map.put("connectedPools", JSONObject.parseObject(JSON.toJSONString(swap.getConnectedPools()), List.class).toString());
        map.put("protocol", conf.getProtocol());
        map.put("raw_swap_log", JSONObject.parseObject(JSON.toJSONString(swap.getRawSwapLog()), List.class).toString());
        map.put("version", swap.getVersion());
        map.put("errorMsg", swap.getErrorMsg());
        map.put("chain", conf.getChainId());
        if (conf.getWCoinAddress().equals(swap.getTokenIn())) {
            map.put("eventType", "buy");
        } else {
            map.put("eventType", "sell");
        }
        return map;
    }

    public static List<UniswapEvent> getSolanaSwapEvents(ChainConfig conf, String originSender, String hash, List<UniswapEvent> uniswapEvents, List<TransferEvent> transferEvents) {
        // 1、移除 swap 的 transfer event
//        List<TransferEvent> transferEvents = CollectionUtils.isEmpty(uniswapEvents) ? rawTransferEvents : rawTransferEvents.stream().filter(t -> {
//            for (int i = 0; i < uniswapEvents.size(); i++) {
//                if (uniswapEvents.get(i).getLogIndex().compareTo(t.getLogIndex()) == 0) return false;
//            }
//            return true;
//        }).collect(Collectors.toList());
        // 2、将构建好的所有 swapEvent 的首尾串联，串联规则：前一个swap的receiver = 后一个swap的sender
        List<UniswapEvent> rawSwapEvents = new ArrayList<>();
        uniswapEvents.forEach(t -> {
            try {
                UniswapEvent temp = UniswapEvent.builder().build();
                BeanUtils.copyProperties(temp, t);
                rawSwapEvents.add(temp);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        List<UniswapEvent> fullUniswapEvents = UniswapEvent.merge(uniswapEvents);
        log.debug("******* 所有 Swap 首尾相连后为： {} 条", fullUniswapEvents.size());

        // 2、循环遍历swapEvents， 从transferEvents找到每一个swapEvent的最开始的入地址和最终的转出地址
        fullUniswapEvents.forEach(ut -> {
            TransferEvent _tmpPreTf = null;
            try {
                _tmpPreTf = TransferEvent.findPreTx(originSender, transferEvents, null, ut.getAmountIn(), ut.getSender(), ut.getTo(), ut.getTokenIn(), ut);
                if (_tmpPreTf == null || _tmpPreTf.getSender() == null) {
                    log.debug("******* ❌  not fond any pre transfer to merge \n");
                    ut.setErrorMsg(ut.getErrorMsg() + " | " + "multity from :" + hash);
                    return;
                }
                ut.setSender(_tmpPreTf.getSender());

                TransferEvent _tmpAftTf = TransferEvent.findAfterTx(originSender, transferEvents, null, ut.getAmountOut(), ut.getSender(), ut.getTo(), ut.getTokenOut(), ut);
                if (_tmpAftTf == null || _tmpAftTf.getSender().equalsIgnoreCase("")) {
                    log.debug("******* ❌  not fond any after transfer to merge \n");
                    ut.setErrorMsg(ut.getErrorMsg() + " | " + "multity to :" + hash);
                    return;
                }
                ut.setAmountOut(_tmpAftTf.getAmount());
                ut.setTo(_tmpAftTf.getReceiver());
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        });
        // 3、将最终的 swap 关联上的 transfer 去掉 | 池子、token的 transfer 去除。保留没有使用的 transfer, 将这些 transfer 封装为 uniswap
        List<TransferEvent> getFinalTransferOutEvent = TransferEvent.calculateBalances(transferEvents);
        transferToUniswapSell(conf, getFinalTransferOutEvent, fullUniswapEvents);
        log.debug("******* 最终有效 Swap： {} 条", fullUniswapEvents.size());

        // 4、保存原始 swap
        fullUniswapEvents.forEach(t -> t.setSwapRawSwapLog(rawSwapEvents));
        return fullUniswapEvents;
    }


    /**
     * 解析所有的swap
     */
    public static List<UniswapEvent> parseAllUniSwapLogs(String originSender, ChainConfig conf, String logs, String hash, List<TransferEvent> validInternalTxs) throws IOException {
        if (logs.isEmpty()) {
            return new ArrayList<>();
        }

        // 1、将log string转为对象
        JsonNode txLog = Log.parseLogsFromString(logs);

        // 2、从log对象中解析 transfer 事件
        List<TransferEvent> transferEvents = Log.findTransfer(txLog);
        log.debug("******* Log 中找到符合条件 TransferEvent： {} 条", transferEvents.size());
        Collections.sort(transferEvents, Comparator.comparing(TransferEvent::getLogIndex));

        // 3、将有效的内部交易添加到transfer事件
        transferEvents.addAll(validInternalTxs);
        log.debug("******* InnerTx 和 Transfer 合并, 当前 总共 transferEvents： {} 条。", transferEvents.size());

        // 4、获取 swap 事件, 且删除构建中使用的transferEvent
        Result resultEvent = getUniswapEvents(conf, transferEvents, txLog, hash);
        // 3、将构建好的所有 swapEven t的首尾串联，串联规则：前一个swap的receiver = 后一个swap的sender
        List<UniswapEvent> fullUniswapEvents = UniswapEvent.merge(resultEvent.getUniswapEvents());
        log.debug("******* 所有 Swap 首尾相连后为： {} 条", fullUniswapEvents.size());

        // 5、循环遍历swapEvents， 从transferEvents找到每一个swapEvent的最开始的入地址和最终的转出地址
        fullUniswapEvents.forEach(ut -> {
            TransferEvent _tmpPreTf = null;
            try {
                _tmpPreTf = TransferEvent.findPreTx(originSender, transferEvents, resultEvent.getPoolAddressLists(), ut.getAmountIn(), ut.getSender(), ut.getTo(), ut.getTokenIn(), ut);
                if (_tmpPreTf == null || _tmpPreTf.getSender() == null) {
                    log.debug("******* ❌  not fond any pre transfer to merge \n");
                    ut.setErrorMsg(ut.getErrorMsg() + " | " + "multity from :" + hash);
                    return;
                }
                ut.setSender(_tmpPreTf.getSender());

                TransferEvent _tmpAftTf = TransferEvent.findAfterTx(originSender, transferEvents, resultEvent.getPoolAddressLists(), ut.getAmountOut(), ut.getSender(), ut.getTo(), ut.getTokenOut(), ut);
                if (_tmpAftTf == null || _tmpAftTf.getSender().equalsIgnoreCase("")) {
                    log.debug("******* ❌  not fond any after transfer to merge \n");
                    ut.setErrorMsg(ut.getErrorMsg() + " | " + "multity to :" + hash);
                    return;
                }
                ut.setAmountOut(_tmpAftTf.getAmount());
                ut.setTo(_tmpAftTf.getReceiver());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        log.debug("******* 所有 Swap 收尾再各自向前后链接 TransferEvent后，当前还剩余 transferEvents： {} 条", transferEvents.size());

        // 10、将最终的 swap 关联上的 transfer 去掉 | 池子、token的 transfer 去除。保留没有使用的 transfer, 将这些 transfer 封装为 uniswap
//        Map<String, Map<String, BigInteger>> finalTransfer = TransferEvent.calculateBalances(transferEvents);
//        List<TransferEvent> getFinalTransferOutEvent = getTransferOutEvent(finalTransfer);
        transferToUniswapSell(conf, transferEvents, fullUniswapEvents);
        log.debug("******* 最终有效 Swap： {} 条", fullUniswapEvents.size());


//        for (int i = 0; i < fullUniswapEvents.size(); i++) {
//            UniswapEvent ut = fullUniswapEvents.get(i);
////            if (ut.getTokenIn().equals("0xc02aaa39b223fe8d0a0e5c4f27ead9083c756cc2")) {
//            if (ut.getTokenIn().equals("0xbb4cdb9cbd36b01bd1cbaebf2de08d9173bc095c") && !ut.getTokenOut().equals("0xbb4cdb9cbd36b01bd1cbaebf2de08d9173bc095c") && !ut.getVersion().equals("transferOut")) {
//
//                BigDecimal t1 =  new BigDecimal(ut.getAmountIn()); //.divide(new BigDecimal("1e18"), 20, RoundingMode.HALF_UP);
//                BigDecimal t2 =  new BigDecimal(ut.getAmountOut()); //.divide(new BigDecimal("1e9"), 20, RoundingMode.HALF_UP);
//
//                log.info(ut.getTokenOut() + " 卖的价格： " + t1.divide(t2, 20, RoundingMode.HALF_UP));
//
//            } else if (!ut.getTokenIn().equals("0xbb4cdb9cbd36b01bd1cbaebf2de08d9173bc095c") && ut.getTokenOut().equals("0xbb4cdb9cbd36b01bd1cbaebf2de08d9173bc095c")) {
//                BigDecimal t1 =  new BigDecimal(ut.getAmountIn()); //.divide(new BigDecimal("1e9"), 20, RoundingMode.HALF_UP);
//                BigDecimal t2 =  new BigDecimal(ut.getAmountOut()); //.divide(new BigDecimal("1e18"), 20, RoundingMode.HALF_UP);
//
//                log.info(ut.getTokenIn() + " 买的价格： " + t2.divide(t1, 20, RoundingMode.HALF_UP));
//            } else {
//                log.info("非正常买卖，忽略价格！");
//            }
//        }
        fullUniswapEvents.forEach(t -> t.setRawSwapLog(resultEvent.getRawUniswapEvents()));
        return fullUniswapEvents;
    }

    public static Result getUniswapEvents(ChainConfig conf, List<TransferEvent> transferEvents, JsonNode txLog, String hash) {
        List<UniswapEvent> uniswapEvents = new ArrayList<>();

        // 1、解析 swap v2\v3
        List<UniswapEvent> uniswapV2Events = Log.findSwapV2(conf.getProtocol(), txLog);
        List<UniswapEvent> uniswapV3Events = Log.findSwapV3(conf.getProtocol(), txLog);
        List<UniswapEvent> uniswapMMEvents = Log.findSwapMM(conf.getProtocol(), txLog);
        List<UniswapEvent> uniswapStableCoinEvents = Log.findSwapStableCoin(conf.getProtocol(), txLog);
        List<UniswapEvent> fourMemeSwapV2 = Log.findFourMemeSwapV2(conf, txLog, transferEvents);
        List<UniswapEvent> fourMemeSwapV1 = Log.findFourMemeSwapV1(conf, txLog, transferEvents);

        log.debug("******* log 中 找到 uniswapV2Events：{} 条，uniswapV3Events： {} 条。uniswapMMEvents: {} 条，uniswapStableCoinEvents： {} 条\n", uniswapV2Events.size(), uniswapV3Events.size(), uniswapMMEvents.size(), uniswapStableCoinEvents.size());

        uniswapEvents.addAll(uniswapV2Events);
        uniswapEvents.addAll(uniswapV3Events);
        uniswapEvents.addAll(uniswapMMEvents);
        uniswapEvents.addAll(uniswapStableCoinEvents);
        uniswapEvents.addAll(fourMemeSwapV1);
        uniswapEvents.addAll(fourMemeSwapV2);
        Collections.sort(uniswapEvents, Comparator.comparing(UniswapEvent::getLogIndex));

        // 2、 结合 TransferEvent 将v2和v3构建为标准的 SwapEvent事件，构建完成以后并且删除构建中使用的 TransferEvent
        Result parseMapResult = Log.fillSwapTokenInAndTokenOutWithTransferEvent(transferEvents, uniswapEvents, hash);
        log.debug("******* 将 {} 条 swapEvent 转化成标准 Swap，共消费 {} 条 transferEvents", parseMapResult.getUniswapEvents().size(), parseMapResult.getTransferEvents().size());
        log.debug("******* 当前 Swap: {}, 条， 还剩余 transferEvents： {} 条", uniswapEvents.size(), transferEvents.size());
        return parseMapResult;
    }

    private static List<TransferEvent> getTransferOutEvent(Map<String, Map<String, BigInteger>> finalTransfer) {
        List<TransferEvent> list = new ArrayList<>();
        for (Map.Entry<String, Map<String, BigInteger>> entry : finalTransfer.entrySet()) {
            String address = entry.getKey();
            Map<String, BigInteger> tokens = entry.getValue();
            for (Map.Entry<String, BigInteger> tokenEntry : tokens.entrySet()) {
                if (tokenEntry.getValue().compareTo(BigInteger.ZERO) < 0) {
                    list.add(TransferEvent.builder()
                            .amount(tokenEntry.getValue().multiply(new BigInteger("-1")))
                            .sender(address)
                            .contractAddress(tokenEntry.getKey())
                            .build());
                }
            }
        }
        return list;
    }

    private static void transferToUniswapSell(ChainConfig conf, List<TransferEvent> transferLog, List<UniswapEvent> eventLists) {
        AtomicInteger count = new AtomicInteger();
        transferLog.forEach(t -> {
            // transferOut 事件构造成 Uniswap Sell 操作
            if (t.getAmount().equals(BigInteger.ZERO)) return;
            // from是0地址，表示增加总供应，实际并没有转账，所以过滤
//            if (t.getSender().equals(AMMSwapDataProcess.ZEROADDR)) return;
            if (conf.getWCoinAddress().equals(t.getContractAddress())) return;
            UniswapEvent build = UniswapEvent.builder()
                    .protocol("transferOut")
                    .version("transferOut")
                    .amountIn(t.getAmount())
                    .amountOut(new BigInteger("0"))
                    .tokenIn(t.getContractAddress())
                    .tokenOut(conf.getWCoinAddress())
                    .pair(Lists.newArrayList(t.getContractAddress(), conf.getWCoinAddress()))
                    .connectedPools(new ArrayList<>())
                    .sender(t.getSender())
                    .to(t.getReceiver())
                    .build();
            eventLists.add(build);
            count.getAndIncrement();
        });
        log.debug("******* 将 {} 条 transferOut 中的转化为 Swap 事件", count.get());
    }
}

