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

        // 将外部交易放到内部交易中
        if (!"0".equals(value)) {
            internalTxs.add(new Tx(new BigInteger(value), caller, to));
        }
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
                if (action == null){
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

    static List<TransferEvent> parseTx(List<Tx> txs) {
        List<TransferEvent> transfers = new ArrayList<>();
        txs.forEach(t -> {
            TransferEvent tevent = TransferEvent.builder().sender(t.getFrom())
                    .receiver(t.getTo())
                    .amount(t.getValue())
                    .contractAddress("0xbb4cdb9cbd36b01bd1cbaebf2de08d9173bc095c")
                    .build();
            transfers.add(tevent);
        });
        return transfers;
    }

    public static void main(String[] args) throws IOException {


    }

}
