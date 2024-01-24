package cn.xlystar.parse.pancake;

import cn.xlystar.entity.TransferEvent;
import cn.xlystar.entity.UniswapEvent;
import cn.xlystar.entity.UniswapV2Event;
import cn.xlystar.entity.UniswapV3Event;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.math.BigInteger;
import java.util.*;

public class PancakeSwapDataProcessFull {

    /**
     * 格式化最后的返回值
     */
    public static List<Map<String, String>> parseFullUniswap(String log, String hash, List<TransferEvent> tftxs) throws IOException {
        List<Map<String, String>> lists = new ArrayList<>();
        List<UniswapEvent> uniswapEvents = parseAllUniSwapLogs(log, hash, tftxs);
        uniswapEvents.forEach(t -> {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("caller", t.getSender());
                    map.put("methodId", "others");
                    map.put("to", t.getTo());
                    map.put("amountIn", t.getAmountIn() == null ? null : t.getAmountIn().toString());
                    map.put("amountOut", t.getAmountOut() == null ? null : t.getAmountOut().toString());
                    map.put("tokenIn", t.getTokenIn());
                    map.put("tokenOut", t.getTokenOut());
                    map.put("protocol", "pancake");
                    map.put("version", t.getVersion());
                    map.put("errorMsg", t.getErrorMsg());
                    map.put("chain", "56");
                    if (PancakeSwapDataProcess.WBNB.equals(t.getTokenIn())) {
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
    public static List<UniswapEvent> parseAllUniSwapLogs(String log, String hash, List<TransferEvent> validInternalTxs) throws IOException {
        if (log.isEmpty()) {
            return new ArrayList<>();
        }

        List<Object> lists = new ArrayList<>();
        List<UniswapEvent> uniswapEvents = new ArrayList<>();

        // 1、将log string转为对象
        JsonNode txLog = Log.parseLogsFromString(log);

        // 2、从log对象中解析transfer、swapv2和swapv3事件
        List<TransferEvent> transferEvents = Log.findTransfer(txLog);
        List<UniswapV2Event> uniswapV2Events = Log.findSwapV2(txLog);
        List<UniswapV3Event> uniswapV3Events = Log.findSwapV3(txLog);

        // 3、将有效的内部交易添加到transfer事件中
        validInternalTxs.forEach(tx -> {
            transferEvents.add(tx);
        });

        // 4、统计所有transfer的token和数量， 并且找到有价值的地址
        Map<String, Map<String, BigInteger>> balances = TransferEvent.calculateBalances(transferEvents);

        // 记录所有 token 地址和 池子地址
        Map<String, String> tokenOrPool = new HashMap<>();
        transferEvents.forEach(t -> tokenOrPool.put(t.getContractAddress(), "token"));

        // 5、筛选有价值的地址，拥有的token数>2并且token余额有正有负（有进有出）
        List<String> addrs = TransferEvent.validAddrs(balances);

        // 6、 将v2和v3构建为标准的swapEvent事件，构建完成以后并且删除构建中使用的transferEvent
        // 6.1 构建v2
        Result parseMapResultv2 = Log.parseUniswapV2ToUniswapEvent(transferEvents, uniswapV2Events, hash);
        uniswapEvents = parseMapResultv2.getUniswapEvents();
        // 6.2 删除已经使用的transferEvents
        transferEvents.removeAll(parseMapResultv2.getTransferEvents());
        // 6.3 构建v3
        Result parseMapResultv3 = Log.parseUniswapV3ToUniswapEvent(transferEvents, uniswapV3Events, hash);
        uniswapEvents.addAll(parseMapResultv3.getUniswapEvents());
        // 6.4 删除已经使用的transferEvents
        transferEvents.removeAll(parseMapResultv3.getTransferEvents());
        uniswapEvents.forEach(t -> tokenOrPool.put(t.getContractAddress(), "pool"));

        // 7、将构建好的所有swapEvent的首尾串联，串联规则：前一个swap的receiver = 后一个swap的sender
        List<UniswapEvent> fullUniswapEvents = UniswapEvent.parseFullUniswapEvents(uniswapEvents);

        // 8、循环遍历swapEvents， 从transferEvents找到每一个swapEvent的最开始的入地址和最终的转出地址
        fullUniswapEvents.forEach(ut -> {
            TransferEvent _tmpPreTf = TransferEvent.findPreTx(transferEvents, ut.getAmountIn(), ut.getSender(), ut.getTo(), ut.getTokenIn());
            if (_tmpPreTf == null || _tmpPreTf.getSender() == null) {
                ut.setErrorMsg(ut.getErrorMsg() + " | " + "multity to :" + hash);
                return;
            }
            ut.setSender(_tmpPreTf.getSender());

            TransferEvent _tmpAftTf = TransferEvent.findAfterTx(transferEvents, ut.getAmountOut(), ut.getSender(), ut.getTo(), ut.getTokenOut());
            if (_tmpAftTf == null || _tmpAftTf.getReceiver() == null) {
                ut.setErrorMsg(ut.getErrorMsg() + " | " + "multity from :" + hash);
                return;
            }
            ut.setTo(_tmpAftTf.getReceiver());
        });

        // 9、swap事件和有价值地址进行交叉判断，只保留有价值的地址的swap
        List<UniswapEvent> validUniswapEvents = new ArrayList<>();
        // 9.1 swap事件是否在期望的地址中
        fullUniswapEvents.forEach(e -> {
            if (e.getErrorMsg() != null
                    || (e.getSender().equalsIgnoreCase(e.getTo()) &&
                    addrs.contains(e.getSender().toLowerCase()))

            ) {
                validUniswapEvents.add(e);
            }
        });

        // 10、将最终的 swap 关联上的 transfer 去掉 | 池子、token的 transfer 去除。保留没有使用的 transfer, 将这些 transfer 封装为 uniswap
        tokenOrPool.forEach((address, value) -> {
            for (int i = 0, len = transferEvents.size(); i < len; i++) {
                TransferEvent transferEvent = transferEvents.get(i);
                if (address.equals(transferEvent.getReceiver()) ||
                        address.equals(transferEvent.getSender())
                ) {
                    transferEvents.remove(i);
                    return;
                }
            }
        });
        validUniswapEvents.forEach(uniswapEvent -> {
            if (uniswapEvent.getErrorMsg() != null) {
                return;
            }
            TransferEvent transferIn = TransferEvent.builder()
                    .sender(uniswapEvent.getSender())
                    .amount(uniswapEvent.getAmountIn())
                    .contractAddress(uniswapEvent.getTokenIn())
                    .build();

            for (int i = 0, len = transferEvents.size(); i < len; i++) {
                TransferEvent transferEvent = transferEvents.get(i);
                if ("internal".equals(transferEvent.getOrigin()) || transferEvent.getAmount().equals(BigInteger.ZERO)) {
                    transferEvents.remove(i);
                    len--;
                    i--;
                    continue;
                }

                if (transferIn.getSender().equals(transferEvent.getSender()) &&
                        transferIn.getAmount().equals(transferEvent.getAmount()) &&
                        transferIn.getContractAddress().equals(transferEvent.getContractAddress())
                ) {
                    transferEvents.remove(i);
                    return;
                }
            }

            TransferEvent transferOut = TransferEvent.builder()
                    .receiver(uniswapEvent.getTo())
                    .amount(uniswapEvent.getAmountOut())
                    .contractAddress(uniswapEvent.getTokenOut())
                    .build();
            for (int i = 0, len = transferEvents.size(); i < len; i++) {
                TransferEvent transferEvent = transferEvents.get(i);
                if (transferEvent.getAmount().equals(BigInteger.ZERO)) {
                    transferEvents.remove(i);
                    len--;
                    i--;
                    continue;
                }
                if (transferOut.getReceiver().equals(transferEvent.getReceiver()) &&
                        transferOut.getAmount().equals(transferEvent.getAmount()) &&
                        transferOut.getContractAddress().equals(transferEvent.getContractAddress())
                ) {
                    transferEvents.remove(i);
                    return;
                }
            }

        });
        Map<String, Map<String, BigInteger>> finalTransfer = TransferEvent.calculateBalances(transferEvents);
        List<TransferEvent> groupTransfer = groupTransfer(finalTransfer);

        transferToUniswapSell(groupTransfer, validUniswapEvents);

        return validUniswapEvents;
    }

    private static List<TransferEvent> groupTransfer(Map<String, Map<String, BigInteger>> finalTransfer) {
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

    private static void transferToUniswapSell(List<TransferEvent> transferLog, List<UniswapEvent> eventLists) {
        transferLog.forEach(t -> {
            // transferOut 事件构造成 Uniswap Sell 操作
            if (t.getAmount().equals(BigInteger.ZERO)) return;
            // from是0地址，表示增加总供应，实际并没有转账，所以过滤
            if (t.getSender().equals(PancakeSwapDataProcess.ZEROADDR)) return;
            if (PancakeSwapDataProcess.WBNB.equals(t.getContractAddress())) return;
            UniswapEvent build = UniswapEvent.builder()
                    .protocol("transferOut")
                    .version("transferOut")
                    .amountIn(t.getAmount())
                    .amountOut(new BigInteger("0"))
                    .tokenIn(t.getContractAddress())
                    .tokenOut(PancakeSwapDataProcess.WBNB)
                    .sender(t.getSender())
                    .to(t.getSender())
                    .build();
            eventLists.add(build);
        });
    }
}

