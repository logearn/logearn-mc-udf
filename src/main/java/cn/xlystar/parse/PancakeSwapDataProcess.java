package cn.xlystar.parse;

import cn.xlystar.entity.TransferEvent;
import cn.xlystar.entity.Tx;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.math.BigInteger;
import java.util.*;


public class PancakeSwapDataProcess {
    private static String ROUTE_V2 = "0x10ED43C718714eb63d5aA57B78B54704E256024E".toLowerCase();
    private static String ROUTE_V3 = "0x13f4EA83D0bd40E75C8222255bc855a974568Dd4".toLowerCase();

    public static List<Map<String, String>> decodeInputData(String inputData, String from, String to, String value, String logs, String internalTxs, String hash) throws IOException {
        if (inputData.length() < 10) {
            return Collections.emptyList();
        }
        List<Tx> lists = new ArrayList<>();
        if (!internalTxs.isEmpty()) {
            lists = parseTrace(internalTxs);
        }

        List<Map<String, String>> maps = new ArrayList<>();
        try {
            maps = decodeInputData(inputData, from, to, value, logs, lists, hash);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(logs);
        }
        return maps;
    }


    public static List<Map<String, String>> decodeInputData(String inputData, String caller, String to, String value, String logs, List<Tx> internalTxs, String hash) throws IOException {

        // 使用 ArrayList 来存储多个 交易
        List<Map<String, String>> resList = new ArrayList<>();

        // 截取 method
        String methodId = inputData.substring(0, 10);
//
//        // 将外部交易放到内部交易中
//        if (!"0".equals(value)) {
//            internalTxs.add(new Tx(new BigInteger(value), caller.toLowerCase(), to.toLowerCase()));
//        }
        List<TransferEvent> tftxs = parseTx(internalTxs);
        resList = PancakeSwapDataProcessFull.parseFullUniswap(logs, hash, tftxs);

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
                JsonNode type = action.get("calltype");
                if (type == null || !"call".equals(type.asText())) continue;
                JsonNode value = action.get("value");
                if (value == null || new BigInteger(value.asText()).compareTo(BigInteger.ZERO) <= 0)
                    continue;
                JsonNode from = action.get("from");
                if (from == null) continue;
                JsonNode to = action.get("to");
                if (to == null) continue;
                Tx tx = new Tx(new BigInteger(value.asText()), from.asText(), to.asText());
                list.add(tx);
            }
        }
        return list;
    }

    static List<TransferEvent> parseTx(List<Tx> txs) {
        List<TransferEvent> transfers = new ArrayList<>();
        txs.forEach(t -> {
            TransferEvent tevent = TransferEvent.builder().sender(t.getFrom())
                    .receiver(t.getTo())
                    .amount(t.getValue())
                    .contractAddress("0xbb4cdb9cbd36b01bd1cbaebf2de08d9173bc095c")
                    .origin("internal")
                    .build();
            transfers.add(tevent);
        });
        return transfers;
    }

    public static void main(String[] args) throws IOException {
        String logs1 = "{\"logs\":[{\"address\":\"0xbb4cdb9cbd36b01bd1cbaebf2de08d9173bc095c\",\"data\":\"0x000000000000000000000000000000000000000000000000379bb9451efd8000\",\"topics\":[\"0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef\",\"0x00000000000000000000000013f4ea83d0bd40e75c8222255bc855a974568dd4\",\"0x00000000000000000000000047477fc619e17b9154cb6fe2a4b8226be906b6e5\"]},{\"address\":\"0x660ae046cf9297f0490be145b818b36e92f0e057\",\"data\":\"0x0000000000000000000000000000000000000000055b5a99e28f9f77b59c515f\",\"topics\":[\"0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef\",\"0x00000000000000000000000047477fc619e17b9154cb6fe2a4b8226be906b6e5\",\"0x0000000000000000000000009cc1ac48b207d766ef4ae81ec2978e0596845099\"]},{\"address\":\"0x47477fc619e17b9154cb6fe2a4b8226be906b6e5\",\"data\":\"0x0000000000000000000000000000000000000000667276492085e894143de8070000000000000000000000000000000000000000000000045c6e3cca66c81661\",\"topics\":[\"0x1c411e9a96e071241c2f21f7726b17ae89e3cab4c78be50e062b03a9fffbbad1\"]},{\"address\":\"0xbb4cdb9cbd36b01bd1cbaebf2de08d9173bc095c\",\"data\":\"0x000000000000000000000000000000000000000000000000379bb9451efd8000\",\"topics\":[\"0xe1fffcc4923d04b559f4d29a8bfc6cda04eb5b0d3c460751c2402c5c5cc9109c\",\"0x00000000000000000000000013f4ea83d0bd40e75c8222255bc855a974568dd4\"]},{\"address\":\"0x47477fc619e17b9154cb6fe2a4b8226be906b6e5\",\"data\":\"0x0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000379bb9451efd80000000000000000000000000000000000000000000055b5a99e28f9f77b59c515f0000000000000000000000000000000000000000000000000000000000000000\",\"topics\":[\"0xd78ad95fa46c994b6551d0da85fc275fe613ce37657fb8d5e3d130840159d822\",\"0x00000000000000000000000013f4ea83d0bd40e75c8222255bc855a974568dd4\",\"0x0000000000000000000000009cc1ac48b207d766ef4ae81ec2978e0596845099\"]}]}";

        String traces = "[{\"action\":{\"from\":\"0x13f4ea83d0bd40e75c8222255bc855a974568dd4\",\"to\":\"0xbb4cdb9cbd36b01bd1cbaebf2de08d9173bc095c\",\"value\":\"4007000000000000000\"}},{\"action\":{\"from\":\"0x13f4ea83d0bd40e75c8222255bc855a974568dd4\",\"to\":\"0x660ae046cf9297f0490be145b818b36e92f0e057\",\"value\":\"0\"}},{\"action\":{\"from\":\"0x13f4ea83d0bd40e75c8222255bc855a974568dd4\",\"to\":\"0x660ae046cf9297f0490be145b818b36e92f0e057\",\"value\":\"0\"}},{\"action\":{\"from\":\"0x13f4ea83d0bd40e75c8222255bc855a974568dd4\",\"to\":\"0xdaecee3c08e953bd5f89a5cc90ac560413d709e3\",\"value\":\"4007000000000000000\"}},{\"action\":{\"from\":\"0x13f4ea83d0bd40e75c8222255bc855a974568dd4\",\"to\":\"0xbb4cdb9cbd36b01bd1cbaebf2de08d9173bc095c\",\"value\":\"0\"}},{\"action\":{\"from\":\"0x13f4ea83d0bd40e75c8222255bc855a974568dd4\",\"to\":\"0xdaecee3c08e953bd5f89a5cc90ac560413d709e3\",\"value\":\"4007000000000000000\"}},{\"action\":{\"from\":\"0x13f4ea83d0bd40e75c8222255bc855a974568dd4\",\"to\":\"0x47477fc619e17b9154cb6fe2a4b8226be906b6e5\",\"value\":\"0\"}},{\"action\":{\"from\":\"0x47477fc619e17b9154cb6fe2a4b8226be906b6e5\",\"to\":\"0x660ae046cf9297f0490be145b818b36e92f0e057\",\"value\":\"0\"}},{\"action\":{\"from\":\"0x47477fc619e17b9154cb6fe2a4b8226be906b6e5\",\"to\":\"0xbb4cdb9cbd36b01bd1cbaebf2de08d9173bc095c\",\"value\":\"0\"}},{\"action\":{\"from\":\"0x47477fc619e17b9154cb6fe2a4b8226be906b6e5\",\"to\":\"0x660ae046cf9297f0490be145b818b36e92f0e057\",\"value\":\"0\"}},{\"action\":{\"from\":\"0x13f4ea83d0bd40e75c8222255bc855a974568dd4\",\"to\":\"0x47477fc619e17b9154cb6fe2a4b8226be906b6e5\",\"value\":\"0\"}},{\"action\":{\"from\":\"0x13f4ea83d0bd40e75c8222255bc855a974568dd4\",\"to\":\"0xdaecee3c08e953bd5f89a5cc90ac560413d709e3\",\"value\":\"4007000000000000000\"}},{\"action\":{\"from\":\"0x13f4ea83d0bd40e75c8222255bc855a974568dd4\",\"to\":\"0xbb4cdb9cbd36b01bd1cbaebf2de08d9173bc095c\",\"value\":\"0\"}},{\"action\":{\"from\":\"0x13f4ea83d0bd40e75c8222255bc855a974568dd4\",\"to\":\"0x13f4ea83d0bd40e75c8222255bc855a974568dd4\",\"value\":\"4007000000000000000\"}},{\"action\":{\"from\":\"0x9cc1ac48b207d766ef4ae81ec2978e0596845099\",\"to\":\"0x13f4ea83d0bd40e75c8222255bc855a974568dd4\",\"value\":\"4007000000000000000\"}}]";
        List<Map<String, String>> maps = decodeInputData("0x1112121221212", "0x9cc1ac48b207d766ef4ae81ec2978e0596845099", "0x13f4ea83d0bd40e75c8222255bc855a974568dd4", "4007000000000000000", logs1, traces, "0x5a2e4e5ba378a31524d9e486f4c21f4684ace8db614d75a996d7c673a7c67982");
        maps.forEach(System.out::println);
    }

}
