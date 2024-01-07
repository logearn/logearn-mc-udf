package cn.xlystar.parse;

import cn.xlystar.entity.Tx;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.math.BigInteger;
import java.util.*;

public class PancakeSwapDataProcess {
    private static String ROUTE_V2 = "0x10ED43C718714eb63d5aA57B78B54704E256024E".toLowerCase();
    private static String ROUTE_V3 = "0x13f4EA83D0bd40E75C8222255bc855a974568Dd4".toLowerCase();

    public static List<Map<String, String>> decodeInputData(String inputData, String from, String to, String value, String logs, String internalTxs) throws IOException {
        if (inputData.length() < 10) {
            return Collections.emptyList();
        }
        List<Tx> lists = new ArrayList<>();
        if (!internalTxs.isEmpty()) {
            lists = parseTrace(internalTxs);
        }

        List<Map<String, String>> maps = new ArrayList<>();
        try {
            maps = decodeInputData(inputData, from, to, value, logs, lists);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(logs);
        }
        return maps;
    }


    public static List<Map<String, String>> decodeInputData(String inputData, String caller, String to, String value, String logs, List<Tx> internalTxs) throws IOException {

        // 使用 ArrayList 来存储多个 交易
        List<Map<String, String>> resList = new ArrayList<>();

        // 截取 method
        String methodId = inputData.substring(0, 10);

        // 解析 V2
        if (to.toLowerCase().equals(ROUTE_V2)) {
            if (
                    "0x5c11d795".equals(methodId) || "0x38ed1739".equals(methodId)
                            || "0x7ff36ab5".equals(methodId) || "0xb6f9de95".equals(methodId)
                            || "0xfb3bdb41".equals(methodId) || "0x8803dbee".equals(methodId)
                            || "0x4a25d94a".equals(methodId) || "0x791ac947".equals(methodId)
                            || "0x18cbafe5".equals(methodId)
            ) {
                resList.add(PancakeSwapDataProcessV2.decodeInputData(inputData, caller, value));
            }
        } else if (to.toLowerCase().equals(ROUTE_V3)) {
            if (
                    "0x09b81346".equals(methodId) || "0xb858183f".equals(methodId)
                            || "0x04e45aaf".equals(methodId) || "0x5023b4df".equals(methodId)
                            || "0x42712a67".equals(methodId)
            ) {
                resList.add(PancakeSwapDataProcessV3.decodeInputData(inputData, caller));
            }
        } else {

            List<Map<String, String>> lists = PancakeSwapDataProcessFull.parseFullUniswap(logs);
            List<Map<String, String>> lists2 = new ArrayList<>(lists);
            List<Map<String, String>> _tmpList = new ArrayList<>();
            lists.forEach(t -> {
                        Map<String, String> _tmpt = new HashMap<>(t);
                        // 向前找最早1个swap事件
                        Map<String, String> _tpre = findPreEvent(lists2, t);
                        _tmpt.put("tokenIn", _tpre.get("tokenIn"));
                        _tmpt.put("amountIn", _tpre.get("amountIn"));
                        _tmpt.put("caller", _tpre.get("caller"));

                        // 向后找最后一个swap事件
                        Map<String, String> _taft = findAfterEvent(lists2, t);
                        _tmpt.put("tokenOut", _taft.get("tokenOut"));
                        _tmpt.put("amountOut", _taft.get("amountOut"));
                        _tmpt.put("to", _taft.get("to"));
                        _tmpList.add(_tmpt);
                    }
            );

            // 过滤重复event事件
            Set<String> seen = new HashSet<>();
            List<Map<String, String>> _uniqueList = new ArrayList<>();

            for (Map<String, String> map : _tmpList) {
                String key = map.get("tokenIn") + map.get("tokenOut") + map.get("amountIn") + map.get("amountOut");
                if (!seen.contains(key)) {
                    seen.add(key);
                    _uniqueList.add(map);
                }
            }

            List<Map<String, String>> finalResList;
            int count = 1;
            if (internalTxs.size() > 0) {
                Iterator<Tx> iterator = internalTxs.iterator();
                while (iterator.hasNext()) {
                    Tx elem = iterator.next();
                    if (elem.getValue().compareTo(BigInteger.ZERO) == 0) {
                        iterator.remove(); // 移除value==0
                    }
                }

                finalResList = resList;
                _uniqueList.forEach(t -> {
                            // 找到最早一个转账tx
                            if (t.get("tokenIn").equals("0xbb4cdb9cbd36b01bd1cbaebf2de08d9173bc095c")) {
                                Tx _tx = findPreTx(internalTxs, new BigInteger(t.get("amountOut")), t.get("caller"), t.get("to"), count);
                                t.put("caller", _tx.getFrom());
                            }
                            // 找到最后一个转账tx
                            if (t.get("tokenOut").equals("0xbb4cdb9cbd36b01bd1cbaebf2de08d9173bc095c")) {

                                Tx _tx = findAfterTx(internalTxs, new BigInteger(t.get("amountOut")), t.get("caller"), t.get("to"), count);
                                t.put("to", _tx.getTo());
                            }
                            if(t.get("caller").toLowerCase().equals(t.get("to").toLowerCase())){
                                finalResList.add(t);
                            }
                        }
                );
            } else {
                finalResList = _uniqueList;
            }
            resList = finalResList;
        }

        return resList;
    }

    static List<Tx> parseTrace(String trace) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode txTrace = objectMapper.readTree(trace);

        // 2、读取每个字段
        // String transactionHash = txLog.get("transactionHash").asText();
        ArrayList<Tx> list = new ArrayList<>();
        if (txTrace.isArray()) {
            for (JsonNode tmp : txTrace) {
                JsonNode action = tmp.get("action");
                if (action == null) {
                    continue;
                }
                JsonNode value = action.get("value");
                if (value == null) continue;
                JsonNode from = action.get("from");
                if (from == null) continue;
                JsonNode to = action.get("to");
                if (to == null) continue;
                Tx tx = new Tx(new BigInteger(value.asText().substring(2), 16), from.asText(), to.asText());
                list.add(tx);
            }
        }
        return list;
    }

    public static Tx findAfterTx(List<Tx> internalTxs, BigInteger value, String from, String to, int count) {
        if (internalTxs.isEmpty()) {
            return new Tx(value, from, to);
        }
        Iterator<Tx> iterator = internalTxs.iterator();
        while (iterator.hasNext()) {
            Tx elem = iterator.next();
            if (elem.getValue().compareTo(value) == 0 && elem.getFrom().toLowerCase().equals(to.toLowerCase())) {
                iterator.remove(); // 移除匹配到的元素
                return findAfterTx(internalTxs, elem.getValue(), elem.getFrom(), elem.getTo(), ++count);
            }
        }
        return new Tx(value, from, to); // 或者返回null，如果你希望在未找到时返回空
    }

    public static Tx findPreTx(List<Tx> internalTxs, BigInteger value, String from, String to, int count) {
        if (internalTxs.isEmpty()) {
            return new Tx(value, from, to);
        }
        Iterator<Tx> iterator = internalTxs.iterator();
        while (iterator.hasNext()) {
            Tx elem = iterator.next();
            if (elem.getValue().compareTo(value) == 0 && elem.getTo().toLowerCase().equals(from.toLowerCase())) {
                iterator.remove(); // 移除匹配到的元素
                return findPreTx(internalTxs, elem.getValue(), elem.getFrom(), elem.getTo(), ++count);
            }
        }
        return new Tx(value, from, to); // 或者返回null，如果你希望在未找到时返回空
    }

    public static Map<String, String> findPreEvent(List<Map<String, String>> events, Map<String, String> target) {
        if (events.isEmpty()) {
            return target;
        }
        Iterator<Map<String, String>> iterator = events.iterator();
        while (iterator.hasNext()) {
            Map<String, String> elem = iterator.next();
            if (elem.get("tokenOut").toLowerCase().equals(target.get("tokenIn")) && elem.get("amountOut").equals(target.get("amountIn"))) {
                iterator.remove(); // 移除匹配到的元素
                return findPreEvent(events, elem);
            }
        }
        return target; // 或者返回null，如果你希望在未找到时返回空
    }

    public static Map<String, String> findAfterEvent(List<Map<String, String>> events, Map<String, String> target) {
        if (events.isEmpty()) {
            return target;
        }
        Iterator<Map<String, String>> iterator = events.iterator();
        while (iterator.hasNext()) {
            Map<String, String> elem = iterator.next();
            if (elem.get("tokenIn").toLowerCase().equals(target.get("tokenOut")) && elem.get("amountIn").equals(target.get("amountOut"))) {
                iterator.remove(); // 移除匹配到的元素
                return findAfterEvent(events, elem);
            }
        }
        return target; // 或者返回null，如果你希望在未找到时返回空
    }

    public static void main(String[] args) throws IOException {
        // https://bscscan.com/tx/0x7fe85ab2a31b16a69e0aeebab46ea9043ec6d7e906496e0d97f28971cb6cc7a4
        String inputData = "0x04e45aaf0000000000000000000000002bf83d080d8bc4715984e75e5b3d149805d1175100000000000000000000000055d398326f99059ff775485246999027b319795500000000000000000000000000000000000000000000000000000000000009c4000000000000000000000000f52ebea7b851e452ece018fbd753bd6cb5387e36000000000000000000000000000000000000000000000028a857425466f80000000000000000000000000000000000000000000000000003bd7ba93df4f150000000000000000000000000000000000000000000000000000000000000000000";
        List<Map<String, String>> maps = decodeInputData(inputData, "0xF52ebEA7b851E452ecE018FbD753bd6cB5387E36", "0x13f4EA83D0bd40E75C8222255bc855a974568Dd4", "", "", "{}");
        System.out.println(maps);
        // https://bscscan.com/tx/0x0ca49e35d8f841bc0de8440449ea040a79d2b2facacfe2a0e003d8fc321258ac
        String inputData2 = "0x5ae401dc000000000000000000000000000000000000000000000000000000006594274a0000000000000000000000000000000000000000000000000000000000000040000000000000000000000000000000000000000000000000000000000000000100000000000000000000000000000000000000000000000000000000000000200000000000000000000000000000000000000000000000000000000000000104472b43f3000000000000000000000000000000000000000000000000458e86a646ca9afb00000000000000000000000000000000000000000000033b9a9589641200e76a0000000000000000000000000000000000000000000000000000000000000080000000000000000000000000c394183613aa58d3476f66f226c6856f1993554c000000000000000000000000000000000000000000000000000000000000000300000000000000000000000055d398326f99059ff775485246999027b3197955000000000000000000000000bb4cdb9cbd36b01bd1cbaebf2de08d9173bc095c0000000000000000000000009851feb263dd6e559c2b934f2873401cfb09ecb500000000000000000000000000000000000000000000000000000000";
        String logs = "{\n" +
                "                \"transactionHash\": \"0x567f696f8d0ea7ee7cb0136c28092cfae31e036c059768adfdf640cd2892403a\",\n" +
                "                \"blockHash\": \"0x68c1afd1897e6f44620ab65c11ed18186d930f7f28d858065e19a67a211468e2\",\n" +
                "                \"blockNumber\": \"0x11a08c5\",\n" +
                "                \"logs\": [\n" +
                "                    {\n" +
                "                        \"transactionHash\": \"0x567f696f8d0ea7ee7cb0136c28092cfae31e036c059768adfdf640cd2892403a\",\n" +
                "                        \"address\": \"0x55d398326f99059ff775485246999027b3197955\",\n" +
                "                        \"blockHash\": \"0x68c1afd1897e6f44620ab65c11ed18186d930f7f28d858065e19a67a211468e2\",\n" +
                "                        \"blockNumber\": \"0x11a08c5\",\n" +
                "                        \"data\": \"0x000000000000000000000000000000000000000000000000458e86a646ca9afb\",\n" +
                "                        \"logIndex\": \"0x87\",\n" +
                "                        \"removed\": false,\n" +
                "                        \"topics\": [\n" +
                "                            \"0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef\",\n" +
                "                            \"0x000000000000000000000000C394183613aA58d3476F66f226c6856F1993554c\",\n" +
                "                            \"0x00000000000000000000000016b9a82891338f9bA80E2D6970FddA79D1eb0daE\"\n" +
                "                        ],\n" +
                "                        \"transactionIndex\": \"0x24\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"transactionHash\": \"0x567f696f8d0ea7ee7cb0136c28092cfae31e036c059768adfdf640cd2892403a\",\n" +
                "                        \"address\": \"0xbb4cdb9cbd36b01bd1cbaebf2de08d9173bc095c\",\n" +
                "                        \"blockHash\": \"0x68c1afd1897e6f44620ab65c11ed18186d930f7f28d858065e19a67a211468e2\",\n" +
                "                        \"blockNumber\": \"0x11a08c5\",\n" +
                "                        \"data\": \"0x00000000000000000000000000000000000000000000000000388fa13f40d441\",\n" +
                "                        \"logIndex\": \"0x88\",\n" +
                "                        \"removed\": false,\n" +
                "                        \"topics\": [\n" +
                "                            \"0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef\",\n" +
                "                            \"0x00000000000000000000000016b9a82891338f9bA80E2D6970FddA79D1eb0daE\",\n" +
                "                            \"0x00000000000000000000000028F6F5bB7f79a2F1C43392Afc95bFf74910dfBaA\"\n" +
                "                        ],\n" +
                "                        \"transactionIndex\": \"0x24\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"transactionHash\": \"0x567f696f8d0ea7ee7cb0136c28092cfae31e036c059768adfdf640cd2892403a\",\n" +
                "                        \"address\": \"0x9851feb263dd6e559c2b934f2873401cfb09ecb5\",\n" +
                "                        \"blockHash\": \"0x68c1afd1897e6f44620ab65c11ed18186d930f7f28d858065e19a67a211468e2\",\n" +
                "                        \"blockNumber\": \"0x11a08c5\",\n" +
                "                        \"data\": \"0x000000000000000000000000000000000000000000000341084964ef4345f5cb\",\n" +
                "                        \"logIndex\": \"0x88\",\n" +
                "                        \"removed\": false,\n" +
                "                        \"topics\": [\n" +
                "                            \"0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef\",\n" +
                "                            \"0x00000000000000000000000028F6F5bB7f79a2F1C43392Afc95bFf74910dfBaA\",\n" +
                "                            \"0x000000000000000000000000C394183613aA58d3476F66f226c6856F1993554c\"\n" +
                "                        ],\n" +
                "                        \"transactionIndex\": \"0x24\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"transactionHash\": \"0x567f696f8d0ea7ee7cb0136c28092cfae31e036c059768adfdf640cd2892403a\",\n" +
                "                        \"address\": \"0x16b9a82891338f9ba80e2d6970fdda79d1eb0dae\",\n" +
                "                        \"blockHash\": \"0x68c1afd1897e6f44620ab65c11ed18186d930f7f28d858065e19a67a211468e2\",\n" +
                "                        \"blockNumber\": \"0x11a08c5\",\n" +
                "                        \"data\": \"0x000000000000000000000000000000000000000000000000458e86a646ca9afb0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000388fa13f40d441\",\n" +
                "                        \"logIndex\": \"0x8a\",\n" +
                "                        \"removed\": false,\n" +
                "                        \"topics\": [\n" +
                "                            \"0xd78ad95fa46c994b6551d0da85fc275fe613ce37657fb8d5e3d130840159d822\",\n" +
                "                            \"0x00000000000000000000000013f4EA83D0bd40E75C8222255bc855a974568Dd4\",\n" +
                "                            \"0x00000000000000000000000028F6F5bB7f79a2F1C43392Afc95bFf74910dfBaA\"\n" +
                "                        ],\n" +
                "                        \"transactionIndex\": \"0x24\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"transactionHash\": \"0x567f696f8d0ea7ee7cb0136c28092cfae31e036c059768adfdf640cd2892403a\",\n" +
                "                        \"address\": \"0x28f6f5bb7f79a2f1c43392afc95bff74910dfbaa\",\n" +
                "                        \"blockHash\": \"0x68c1afd1897e6f44620ab65c11ed18186d930f7f28d858065e19a67a211468e2\",\n" +
                "                        \"blockNumber\": \"0x11a08c5\",\n" +
                "                        \"data\": \"0x000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000388fa13f40d441000000000000000000000000000000000000000000000341084964ef4345f5cb0000000000000000000000000000000000000000000000000000000000000000\",\n" +
                "                        \"logIndex\": \"0x8a\",\n" +
                "                        \"removed\": false,\n" +
                "                        \"topics\": [\n" +
                "                            \"0xd78ad95fa46c994b6551d0da85fc275fe613ce37657fb8d5e3d130840159d822\",\n" +
                "                            \"0x00000000000000000000000013f4EA83D0bd40E75C8222255bc855a974568Dd4\",\n" +
                "                            \"0x000000000000000000000000C394183613aA58d3476F66f226c6856F1993554c\"\n" +
                "                        ],\n" +
                "                        \"transactionIndex\": \"0x24\"\n" +
                "                    }\n" +
                "                ],\n" +
                "                \"contractAddress\": null,\n" +
                "                \"effectiveGasPrice\": \"0x52d4e6340\",\n" +
                "                \"cumulativeGasUsed\": \"0x5cd998\",\n" +
                "                \"from\": \"0x29bbc2b5aff41a2143f7d28fe6944453178f1473\",\n" +
                "                \"gasUsed\": \"0x1a7c0\",\n" +
                "                \"logsBloom\": \"0x00200000000000000000000c80000000000000000000000000010000000000040000000000000020000000000000000002000000080000000800000010000000000000000000020000000008000000200000000000000000000000000000000000000000000000000000000000000000000000000000000000000010000000000000000000000000104000000000000000000000000000080000004040000000000000000000000000000000000000000000000000000000000000000000000000000002000000000000000000008000000000000000001000002000000020004000200000000000000000000000000000000000000000000000000000000000\",\n" +
                "                \"status\": \"0x1\",\n" +
                "                \"to\": \"0x7a250d5630b4cf539739df2c5dacb4c659f2488d\",\n" +
                "                \"transactionIndex\": \"0x24\",\n" +
                "                \"type\": \"0x0\"\n" +
                "            }";
        List<Map<String, String>> maps2 = decodeInputData(inputData2, "0xC394183613aA58d3476F66f226c6856F1993554c", "0x13f4EA83D0bd40E75C8222255bc855a974568Dd4", "", logs, "");
        System.out.println(maps2);

        // https://bscscan.com/tx/0x46dfffb3a977a050d693d4a0fba485c01d3589670b3b1b265e3de9ae9d3e7af4
        String inputData3 = "0xb858183f00000000000000000000000000000000000000000000000000000000000000200000000000000000000000000000000000000000000000000000000000000080000000000000000000000000d24ac9bb506a9234778b1bbe0ee8883fa39de29400000000000000000000000000000000000000000000005150ae84a8cdf000000000000000000000000000000000000000000000000000077af8e3c0cb290000000000000000000000000000000000000000000000000000000000000000002b2bf83d080d8bc4715984e75e5b3d149805d117510009c455d398326f99059ff775485246999027b3197955000000000000000000000000000000000000000000";
        List<Map<String, String>> maps3 = decodeInputData(inputData3, "0xD24Ac9Bb506a9234778b1bbE0Ee8883FA39de294", "0x13f4EA83D0bd40E75C8222255bc855a974568Dd4", "", "", "");
        System.out.println(maps3);

        // https://bscscan.com/tx/0xd125efda64076e98c1855416551fbf608cb74b5ab435c6a6e11c2991a69ce978
        String inputData4 = "0x472b43f300000000000000000000000000000000000000000000000f5606b321ae020000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000800000000000000000000000009b76e4fc04b1856a8d3a028fbadbb024f8704c4a000000000000000000000000000000000000000000000000000000000000000200000000000000000000000055d398326f99059ff775485246999027b31979550000000000000000000000001f1c90aeb2fd13ea972f0a71e35c0753848e3db0";
        String logs4 = "{\n" +
                "                \"transactionHash\": \"0x567f696f8d0ea7ee7cb0136c28092cfae31e036c059768adfdf640cd2892403a\",\n" +
                "                \"blockHash\": \"0x68c1afd1897e6f44620ab65c11ed18186d930f7f28d858065e19a67a211468e2\",\n" +
                "                \"blockNumber\": \"0x11a08c5\",\n" +
                "                \"logs\": [\n" +
                "                    {\n" +
                "                        \"transactionHash\": \"0x567f696f8d0ea7ee7cb0136c28092cfae31e036c059768adfdf640cd2892403a\",\n" +
                "                        \"address\": \"0x55d398326f99059ff775485246999027b3197955\",\n" +
                "                        \"blockHash\": \"0x68c1afd1897e6f44620ab65c11ed18186d930f7f28d858065e19a67a211468e2\",\n" +
                "                        \"blockNumber\": \"0x11a08c5\",\n" +
                "                        \"data\": \"0x00000000000000000000000000000000000000000000000f5606b321ae020000\",\n" +
                "                        \"logIndex\": \"0x87\",\n" +
                "                        \"removed\": false,\n" +
                "                        \"topics\": [\n" +
                "                            \"0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef\",\n" +
                "                            \"0x0000000000000000000000009B76e4fc04B1856A8D3A028fBAdBB024F8704C4A\",\n" +
                "                            \"0x000000000000000000000000fE155cBBB949e3594146DdceE3b3Fae4B6321aA7\"\n" +
                "                        ],\n" +
                "                        \"transactionIndex\": \"0x24\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"transactionHash\": \"0x567f696f8d0ea7ee7cb0136c28092cfae31e036c059768adfdf640cd2892403a\",\n" +
                "                        \"address\": \"0x1f1c90aeb2fd13ea972f0a71e35c0753848e3db0\",\n" +
                "                        \"blockHash\": \"0x68c1afd1897e6f44620ab65c11ed18186d930f7f28d858065e19a67a211468e2\",\n" +
                "                        \"blockNumber\": \"0x11a08c5\",\n" +
                "                        \"data\": \"0x000000000000000000000000000000000000000000000000f5505d821a0d01b4\",\n" +
                "                        \"logIndex\": \"0x88\",\n" +
                "                        \"removed\": false,\n" +
                "                        \"topics\": [\n" +
                "                            \"0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef\",\n" +
                "                            \"0x000000000000000000000000fE155cBBB949e3594146DdceE3b3Fae4B6321aA7\",\n" +
                "                            \"0x0000000000000000000000009B76e4fc04B1856A8D3A028fBAdBB024F8704C4A\"\n" +
                "                        ],\n" +
                "                        \"transactionIndex\": \"0x24\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"transactionHash\": \"0x567f696f8d0ea7ee7cb0136c28092cfae31e036c059768adfdf640cd2892403a\",\n" +
                "                        \"address\": \"0xfe155cbbb949e3594146ddcee3b3fae4b6321aa7\",\n" +
                "                        \"blockHash\": \"0x68c1afd1897e6f44620ab65c11ed18186d930f7f28d858065e19a67a211468e2\",\n" +
                "                        \"blockNumber\": \"0x11a08c5\",\n" +
                "                        \"data\": \"0x000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000f5606b321ae020000000000000000000000000000000000000000000000000000f5505d821a0d01b40000000000000000000000000000000000000000000000000000000000000000\",\n" +
                "                        \"logIndex\": \"0x8a\",\n" +
                "                        \"removed\": false,\n" +
                "                        \"topics\": [\n" +
                "                            \"0xd78ad95fa46c994b6551d0da85fc275fe613ce37657fb8d5e3d130840159d822\",\n" +
                "                            \"0x00000000000000000000000013f4EA83D0bd40E75C8222255bc855a974568Dd4\",\n" +
                "                            \"0x0000000000000000000000009B76e4fc04B1856A8D3A028fBAdBB024F8704C4A\"\n" +
                "                        ],\n" +
                "                        \"transactionIndex\": \"0x24\"\n" +
                "                    }\n" +
                "                ],\n" +
                "                \"contractAddress\": null,\n" +
                "                \"effectiveGasPrice\": \"0x52d4e6340\",\n" +
                "                \"cumulativeGasUsed\": \"0x5cd998\",\n" +
                "                \"from\": \"0x29bbc2b5aff41a2143f7d28fe6944453178f1473\",\n" +
                "                \"gasUsed\": \"0x1a7c0\",\n" +
                "                \"logsBloom\": \"0x00200000000000000000000c80000000000000000000000000010000000000040000000000000020000000000000000002000000080000000800000010000000000000000000020000000008000000200000000000000000000000000000000000000000000000000000000000000000000000000000000000000010000000000000000000000000104000000000000000000000000000080000004040000000000000000000000000000000000000000000000000000000000000000000000000000002000000000000000000008000000000000000001000002000000020004000200000000000000000000000000000000000000000000000000000000000\",\n" +
                "                \"status\": \"0x1\",\n" +
                "                \"to\": \"0x7a250d5630b4cf539739df2c5dacb4c659f2488d\",\n" +
                "                \"transactionIndex\": \"0x24\",\n" +
                "                \"type\": \"0x0\"\n" +
                "            }";
        List<Map<String, String>> maps4 = decodeInputData(inputData4, "0x9B76e4fc04B1856A8D3A028fBAdBB024F8704C4A", "0x13f4EA83D0bd40E75C8222255bc855a974568Dd4", "", logs4, "");
        System.out.println(maps4);

    }

}
