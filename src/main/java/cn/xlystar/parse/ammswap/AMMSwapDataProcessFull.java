package cn.xlystar.parse.ammswap;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import cn.xlystar.entity.TransferEvent;
import cn.xlystar.entity.UniswapEvent;
import cn.xlystar.helpers.ChainConfig;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AMMSwapDataProcessFull {

    /**
     * 格式化最后的返回值
     */
    public static List<Map<String, String>> parseFullUniswap(ChainConfig conf, String logs, String hash, List<TransferEvent> tftxs) throws IOException {
        List<Map<String, String>> lists = new ArrayList<>();
        List<UniswapEvent> uniswapEvents = parseAllUniSwapLogs(conf, logs, hash, tftxs);
        uniswapEvents.forEach(t -> {
                    // 非 eth 币对处理
                    if (t.getTokenIn() != null && t.getTokenOut() != null && !t.getTokenIn().equals(conf.getWCoinAddress()) && !t.getTokenOut().equals(conf.getWCoinAddress())) {
                        t.setAmountOut(BigInteger.ZERO);
                        t.setTokenOut(conf.getWCoinAddress());
                    }
                    HashMap<String, String> map = new HashMap<>();
                    map.put("caller", t.getSender());
                    map.put("methodId", "others");
                    map.put("to", t.getTo());
                    map.put("amountIn", t.getAmountIn() == null ? null : t.getAmountIn().toString());
                    map.put("amountOut", t.getAmountOut() == null ? null : t.getAmountOut().toString());
                    map.put("tokenIn", t.getTokenIn());
                    map.put("tokenOut", t.getTokenOut());
                    map.put("pair", t.getPair().toString());
                    map.put("logIndex", t.getLogIndex() == null ? null : t.getLogIndex().toString());
                    map.put("fromMergedTransferEvent", t.getFromMergedTransferEvent() == null ? null : t.getFromMergedTransferEvent().toString());
                    map.put("toMergedTransferEvent", t.getToMergedTransferEvent() == null ? null : t.getToMergedTransferEvent().toString());
                    map.put("connectedPools",  JSONObject.parseObject(JSON.toJSONString(t.getConnectedPools()), List.class).toString());
                    map.put("protocol", conf.getProtocol());
                    map.put("version", t.getVersion());
                    map.put("errorMsg", t.getErrorMsg());
                    map.put("chain", conf.getChainId());
                    if (conf.getWCoinAddress().equals(t.getTokenIn())) {
                        map.put("eventType", "buy");
                    } else {
                        map.put("eventType", "sell");
                    }
                    lists.add(map);
                }
        );
        return lists;
    }

    /**
     * 解析所有的swap
     */
    public static List<UniswapEvent> parseAllUniSwapLogs(ChainConfig conf, String logs, String hash, List<TransferEvent> validInternalTxs) throws IOException {
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
        List<UniswapEvent> fullUniswapEvents = getUniswapEvents(conf, transferEvents, txLog, hash);

        // 5、循环遍历swapEvents， 从transferEvents找到每一个swapEvent的最开始的入地址和最终的转出地址
        fullUniswapEvents.forEach(ut -> {
            TransferEvent _tmpPreTf = null;
            try {
                _tmpPreTf = TransferEvent.findPreTx(transferEvents, ut.getAmountIn(), ut.getSender(), ut.getTo(), ut.getTokenIn(), ut);
                if (_tmpPreTf == null || _tmpPreTf.getSender() == null) {
                    log.debug("******* ❌  not fond any pre transfer to merge \n");
                    ut.setErrorMsg(ut.getErrorMsg() + " | " + "multity from :" + hash);
                    return;
                }
                ut.setSender(_tmpPreTf.getSender());

                TransferEvent _tmpAftTf = TransferEvent.findAfterTx(transferEvents, ut.getAmountOut(), ut.getSender(), ut.getTo(), ut.getTokenOut(), ut);
                if (_tmpAftTf == null || _tmpAftTf.getSender().equalsIgnoreCase("")) {
                    log.debug("******* ❌  not fond any after transfer to merge \n");
                    ut.setErrorMsg(ut.getErrorMsg() + " | " + "multity to :" + hash);
                    return;
                }
                ut.setAmountOut(_tmpAftTf.getAmount());
                ut.setTo(_tmpAftTf.getReceiver());
            } catch (InvocationTargetException | IllegalAccessException e) {
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
        return fullUniswapEvents;
    }

    public static List<UniswapEvent> getUniswapEvents(ChainConfig conf, List<TransferEvent> transferEvents, JsonNode txLog, String hash) {
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

        // 3、将构建好的所有 swapEven t的首尾串联，串联规则：前一个swap的receiver = 后一个swap的sender
        uniswapEvents = UniswapEvent.merge(uniswapEvents);
        log.debug("******* 所有 Swap 首尾相连后为： {} 条", uniswapEvents.size());
        return uniswapEvents;
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

