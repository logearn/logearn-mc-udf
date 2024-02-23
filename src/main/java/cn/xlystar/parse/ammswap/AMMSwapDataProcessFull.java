package cn.xlystar.parse.ammswap;

import java.io.IOException;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import cn.xlystar.entity.TransferEvent;
import cn.xlystar.helpers.ChainConfig;
import com.fasterxml.jackson.databind.JsonNode;

import cn.xlystar.entity.UniswapEvent;
import cn.xlystar.entity.UniswapV2Event;
import cn.xlystar.entity.UniswapV3Event;
import com.google.common.collect.Lists;

public class AMMSwapDataProcessFull {

    /**
     * 格式化最后的返回值
     */
    public static List<Map<String, String>> parseFullUniswap(ChainConfig conf, String log, String hash, List<TransferEvent> tftxs) throws IOException {
        List<Map<String, String>> lists = new ArrayList<>();
        List<UniswapEvent> uniswapEvents = parseAllUniSwapLogs(conf, log, hash, tftxs);
        uniswapEvents.forEach(t -> {
                    // 非 eth 币对处理
                    if (t.getErrorMsg() == null && t.getTokenIn() != null && t.getTokenOut() != null && !t.getTokenIn().equals(conf.getWCoinAddress()) && !t.getTokenOut().equals(conf.getWCoinAddress())) {
                        t.setAmountOut(BigInteger.ZERO);
                        t.setTokenOut(conf.getWCoinAddress());
                        t.setPair(Lists.newArrayList(t.getTokenIn(), t.getTokenOut()));
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
                    map.put("poolAddress", t.getConnectedPoolAddress() == null ? null : t.getConnectedPoolAddress().toString());
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
    public static List<UniswapEvent> parseAllUniSwapLogs(ChainConfig conf, String log, String hash, List<TransferEvent> validInternalTxs) throws IOException {
        if (log.isEmpty()) {
            return new ArrayList<>();
        }

        // 1、将log string转为对象
        JsonNode txLog = Log.parseLogsFromString(log);

        // 2、从log对象中解析 transfer 事件
        List<TransferEvent> transferEvents = Log.findTransfer(txLog);
        System.out.printf("******* Log 中找到符合条件 TransferEvent： %s 条\n", transferEvents.size());

        // 3、将有效的内部交易添加到transfer事件
        transferEvents.addAll(validInternalTxs);
        System.out.printf("******* InnerTx 和 Transfer 合并, 当前 总共 transferEvents： %s 条。\n", transferEvents.size());

        // 4、获取 swap 事件, 且删除构建中使用的transferEvent
        List<UniswapEvent> fullUniswapEvents = getUniswapEvents(conf, transferEvents, txLog, hash);

        // 5、循环遍历swapEvents， 从transferEvents找到每一个swapEvent的最开始的入地址和最终的转出地址
        fullUniswapEvents.forEach(ut -> {
            TransferEvent _tmpPreTf = TransferEvent.findPreTx(transferEvents, ut.getAmountIn(), ut.getSender(), ut.getTo(), ut.getTokenIn());
            if (_tmpPreTf == null || _tmpPreTf.getSender() == null) {
                ut.setErrorMsg(ut.getErrorMsg() + " | " + "multity to :" + hash);
                return;
            }
            ut.setSender(_tmpPreTf.getSender());

            TransferEvent _tmpAftTf = TransferEvent.findAfterTx(transferEvents, ut.getAmountOut(), ut.getSender(), ut.getTo(), ut.getTokenOut());
            if (_tmpAftTf == null || _tmpAftTf.getSender().equalsIgnoreCase("")) {
                ut.setErrorMsg(ut.getErrorMsg() + " | " + "multity from :" + hash);
                return;
            }
            ut.setTo(_tmpAftTf.getReceiver());
        });

//        // 9、swap事件和有价值地址进行交叉判断，只保留有价值的地址的swap
//        List<UniswapEvent> validUniswapEvents = new ArrayList<>();
//        fullUniswapEvents.forEach(e -> {
        // 这个逻辑放到 跟单服务里面判断，判断规则是： 跟单买的时候，必须是 from = to,  跟单卖的时候，无脑跟 from
//            if (e.getSender().equalsIgnoreCase(e.getTo()) && addrs.contains(e.getSender().toLowerCase())) {
//                validUniswapEvents.add(e);
//            }
//        });

        System.out.printf("******* 所有 Swap 收尾再各自向前后链接 TransferEvent后，当前还剩余 transferEvents： %s 条\n", transferEvents.size());
        // 10、将最终的 swap 关联上的 transfer 去掉 | 池子、token的 transfer 去除。保留没有使用的 transfer, 将这些 transfer 封装为 uniswap
        Map<String, Map<String, BigInteger>> finalTransfer = TransferEvent.calculateBalances(transferEvents);
        List<TransferEvent> getFinalTransferOutEvent = getTransferOutEvent(finalTransfer);
        transferToUniswapSell(conf, getFinalTransferOutEvent, fullUniswapEvents);
        System.out.printf("******* 最终有效 Swap： %s 条\n", fullUniswapEvents.size());
        return fullUniswapEvents;
    }

    public static List<UniswapEvent> getUniswapEvents(ChainConfig conf, List<TransferEvent> transferEvents, JsonNode txLog, String hash) {
        List<UniswapEvent> uniswapEvents = new ArrayList<>();

        // 1、解析 swap v2\v3
        List<UniswapV2Event> uniswapV2Events = Log.findSwapV2(conf.getProtocol(), txLog);
        List<UniswapV3Event> uniswapV3Events = Log.findSwapV3(conf.getProtocol(), txLog);
        System.out.printf("******* log 中 找到 uniswapV2Events：%s 条，uniswapV3Events： %s 条。\n", uniswapV2Events.size(), uniswapV3Events.size());

//        // 4、统计所有transfer的token和数量， 并且找到有价值的地址
//        Map<String, Map<String, BigInteger>> balances = TransferEvent.calculateBalances(transferEvents);

        // 5、筛选有价值的地址，拥有的token数>2并且token余额有正有负（有进有出）， 不需要这个判断
        //  List<String> addrs = TransferEvent.validAddrs(balances);

        // 2、 将v2和v3构建为标准的swapEvent事件，构建完成以后并且删除构建中使用的transferEvent
        // 2.1 构建v2
        Result parseMapResultv2 = Log.parseUniswapV2ToUniswapEvent(transferEvents, uniswapV2Events, hash);
        uniswapEvents = parseMapResultv2.getUniswapEvents();
        System.out.printf("******* 将 %s 条 v2swap 转化成标准 Swap，共消费 %s 条 transferEvents\n", parseMapResultv2.getUniswapEvents().size(), parseMapResultv2.getTransferEvents().size());

        // 2.2 构建v3
        Result parseMapResultv3 = Log.parseUniswapV3ToUniswapEvent(transferEvents, uniswapV3Events, hash);
        uniswapEvents.addAll(parseMapResultv3.getUniswapEvents());
        System.out.printf("******* 将 %s 条 v3swap 转化成标准 Swap，共消费 %s 条 transferEvents\n", parseMapResultv3.getUniswapEvents().size(), parseMapResultv3.getTransferEvents().size());

        System.out.printf("******* 当前还剩余 transferEvents： %s 条\n", transferEvents.size());

        // 3、将构建好的所有swapEvent的首尾串联，串联规则：前一个swap的receiver = 后一个swap的sender
        List<UniswapEvent> fullUniswapEvents = UniswapEvent.parseFullUniswapEvents(uniswapEvents);
        System.out.printf("******* 所有 Swap 首尾相连后为： %s 条\n", fullUniswapEvents.size());
        return fullUniswapEvents;
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
                    .connectedPoolAddress(new ArrayList<>())
                    .sender(t.getSender())
                    .to(t.getSender())
                    .build();
            eventLists.add(build);
            count.getAndIncrement();
        });
        System.out.printf("******* 将 %s 条 transferOut 中的转化为 Swap 事件 \n", count.get());
    }
}

