package cn.xlystar.parse.ammswap;

import cn.xlystar.entity.TransferEvent;
import cn.xlystar.entity.UniswapEvent;
import cn.xlystar.helpers.ChainConfig;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class AMMSwapDataProcessFull {

    /**
     * 格式化最后的返回值
     */
    public static List<Map<String, String>> parseFullUniswap(String sender, ChainConfig conf, String logs, String hash, List<TransferEvent> tftxs) throws IOException {
        List<Map<String, String>> lists = new ArrayList<>();
        List<UniswapEvent> uniswapEvents = parseAllUniSwapLogs(sender, conf, logs, hash, tftxs);
        uniswapEvents.forEach(t -> {
                    // 非 eth 币对处理
                    if (t.getTokenIn() != null && t.getTokenOut() != null && !t.getTokenIn().equals(conf.getWCoinAddress()) && !t.getTokenOut().equals(conf.getWCoinAddress())) {
                        // 找到第一个 ETH 的池子
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
                                    lists.add(swapResultStruct(conf, ethSellUniswap.build()));

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

                                    lists.add(swapResultStruct(conf, ethBuyUniswap.build()));
                                    return;
                                }

                            }
                        }
                        // 不存在 eth 的池子
                        t.setAmountOut(BigInteger.ZERO);
                        t.setTokenOut(conf.getWCoinAddress());
                    }

                    lists.add(swapResultStruct(conf, t));
                }
        );
        return lists;
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
        map.put("raw_swap_log", JSONObject.parseObject(JSON.toJSONString(swap.getRawSwapLog()), List.class).toString());
        map.put("protocol", conf.getProtocol());
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
        // 4、获取 swap 事件, 且删除构建中使用的transferEvent
        Result resultEvent = getUniswapEvents(conf, transferEvents, txLog, hash);
        // 3、将构建好的所有 swapEven t的首尾串联，串联规则：前一个swap的receiver = 后一个swap的sender
        List<UniswapEvent> fullUniswapEvents = UniswapEvent.merge(resultEvent.getUniswapEvents());
        log.debug("******* 所有 Swap 首尾相连后为： {} 条", fullUniswapEvents.size());
        // 5、循环遍历swapEvents， 从transferEvents找到每一个swapEvent的最开始的入地址和最终的转出地址
        fullUniswapEvents.forEach(ut -> {
            try {
                TransferEvent _tmpPreTf = TransferEvent.findPreTx(originSender, transferEvents, resultEvent.getPoolAddressLists(), ut.getAmountIn(), ut.getSender(), ut.getTo(), ut.getTokenIn(), ut);
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
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });

        log.debug("******* 所有 Swap 收尾再各自向前后链接 TransferEvent后，当前还剩余 transferEvents： {} 条", transferEvents.size());

        // 10、将最终的 swap 关联上的 transfer 去掉 | 池子、token的 transfer 去除。保留没有使用的 transfer, 将这些 transfer 封装为 uniswap
        Map<String, Map<String, BigInteger>> finalTransfer = TransferEvent.calculateBalances(transferEvents);
        List<TransferEvent> getFinalTransferOutEvent = getTransferOutEvent(finalTransfer);
        transferToUniswapSell(conf, getFinalTransferOutEvent, fullUniswapEvents);
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

        log.debug("******* log 中 找到 uniswapV2Events：{} 条，uniswapV3Events： {} 条。uniswapMMEvents: {} 条，uniswapStableCoinEvents： {} 条\n", uniswapV2Events.size(), uniswapV3Events.size(), uniswapMMEvents.size(), uniswapStableCoinEvents.size());

        uniswapEvents.addAll(uniswapV2Events);
        uniswapEvents.addAll(uniswapV3Events);
        uniswapEvents.addAll(uniswapMMEvents);
        uniswapEvents.addAll(uniswapStableCoinEvents);
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
            if (t.getSender().equals(AMMSwapDataProcess.ZEROADDR)) return;
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
                    .to(t.getSender())
                    .build();
            eventLists.add(build);
            count.getAndIncrement();
        });
        log.debug("******* 将 {} 条 transferOut 中的转化为 Swap 事件", count.get());
    }
}

