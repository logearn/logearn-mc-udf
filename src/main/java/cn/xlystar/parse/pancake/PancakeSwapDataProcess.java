package cn.xlystar.parse.pancake;

import cn.xlystar.entity.TransferEvent;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.*;


public class PancakeSwapDataProcess {
    private static String ROUTE_V2 = "0x10ED43C718714eb63d5aA57B78B54704E256024E".toLowerCase();
    private static String ROUTE_V3 = "0x13f4EA83D0bd40E75C8222255bc855a974568Dd4".toLowerCase();
    public static String WBNB = "0xbb4cdb9cbd36b01bd1cbaebf2de08d9173bc095c".toLowerCase();
    public static String ZEROADDR = "0x0000000000000000000000000000000000000000".toLowerCase();
    public static List<Map<String, String>> decodeInputData(String inputData, String from, String to, String value, String logs, String internalTxs, String hash) throws IOException {
        // 将内部交易的string转为json
        JsonNode txTrace = InternalTx.parseTrace(internalTxs);
        // 从内部交易中找到有效的交易，即：value>0的交易，且type = call
        List<TransferEvent> validInternalTxs = InternalTx.parseTx(txTrace, WBNB);
        // 从logs中解析swap
        List<Map<String, String>> maps = PancakeSwapDataProcessFull.parseFullUniswap(logs, hash, validInternalTxs);
//        for (int i = 0, len = maps.size(); i < len; i++) {
//            Map<String, String> t = maps.get(i);
//            if (t.get("errorMsg") != null || t.get("tokenIn") == null || t.get("tokenOut") == null) {
//                maps.remove(i);
//                len--;
//                i--;
//            }
//        }
        return maps;
    }

    public static List<Map<String, String>> decodeInputData(String logs, String internalTxs, String hash) throws IOException {
        return decodeInputData("", "", "", "", logs, internalTxs, hash);
    }

    public static void main(String[] args) throws IOException {
        String logs = "{\n" +
                "  \"blockHash\": \"0xd6ac6807cc9a5c7f7376fc0165ccc5a13a6dba1f3b8274e115bcb0e480c58cef\",\n" +
                "  \"blockNumber\": \"0x218c445\",\n" +
                "  \"contractAddress\": null,\n" +
                "  \"cumulativeGasUsed\": \"0x138030\",\n" +
                "  \"effectiveGasPrice\": \"0xddada181\",\n" +
                "  \"from\": \"0x435ce5cf73cc615a341077f5c2b20c0ae53e22c2\",\n" +
                "  \"gasUsed\": \"0x23082\",\n" +
                "  \"logs\": [\n" +
                "    {\n" +
                "      \"address\": \"0xbb4cdb9cbd36b01bd1cbaebf2de08d9173bc095c\",\n" +
                "      \"topics\": [\n" +
                "        \"0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef\",\n" +
                "        \"0x000000000000000000000000d0e226f674bbf064f54ab47f42473ff80db98cba\",\n" +
                "        \"0x000000000000000000000000000000000d2e592685553cb51ab23eba8a6e1969\"\n" +
                "      ],\n" +
                "      \"data\": \"0x0000000000000000000000000000000000000000000000003ee0286cd2925efb\",\n" +
                "      \"blockNumber\": \"0x218c445\",\n" +
                "      \"transactionHash\": \"0x0008cc23082353d2319339342bd8b588a729c1e145cf00e11903e26e3d5fcbea\",\n" +
                "      \"transactionIndex\": \"0x12\",\n" +
                "      \"blockHash\": \"0xd6ac6807cc9a5c7f7376fc0165ccc5a13a6dba1f3b8274e115bcb0e480c58cef\",\n" +
                "      \"logIndex\": \"0x19\",\n" +
                "      \"removed\": false\n" +
                "    },\n" +
                "    {\n" +
                "      \"address\": \"0x2170ed0880ac9a755fd29b2688956bd959f933f8\",\n" +
                "      \"topics\": [\n" +
                "        \"0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef\",\n" +
                "        \"0x000000000000000000000000000000000d2e592685553cb51ab23eba8a6e1969\",\n" +
                "        \"0x000000000000000000000000d0e226f674bbf064f54ab47f42473ff80db98cba\"\n" +
                "      ],\n" +
                "      \"data\": \"0x000000000000000000000000000000000000000000000000076fd50c138a5600\",\n" +
                "      \"blockNumber\": \"0x218c445\",\n" +
                "      \"transactionHash\": \"0x0008cc23082353d2319339342bd8b588a729c1e145cf00e11903e26e3d5fcbea\",\n" +
                "      \"transactionIndex\": \"0x12\",\n" +
                "      \"blockHash\": \"0xd6ac6807cc9a5c7f7376fc0165ccc5a13a6dba1f3b8274e115bcb0e480c58cef\",\n" +
                "      \"logIndex\": \"0x1a\",\n" +
                "      \"removed\": false\n" +
                "    },\n" +
                "    {\n" +
                "      \"address\": \"0xd0e226f674bbf064f54ab47f42473ff80db98cba\",\n" +
                "      \"topics\": [\n" +
                "        \"0x19b47279256b2a23a1665c810c8d55a1758940ee09377d4f8d26497a3577dc83\",\n" +
                "        \"0x000000000000000000000000000000000d2e592685553cb51ab23eba8a6e1969\",\n" +
                "        \"0x000000000000000000000000000000000d2e592685553cb51ab23eba8a6e1969\"\n" +
                "      ],\n" +
                "      \"data\": \"0x000000000000000000000000000000000000000000000000076fd50c138a5600ffffffffffffffffffffffffffffffffffffffffffffffffc11fd7932d6da1050000000000000000000000000000000000000002e88879bb159c8d67a68062d4000000000000000000000000000000000000000000000652cb04e72c03e270630000000000000000000000000000000000000000000000000000000000005368000000000000000000000000000000000000000000000000000052dad2bb76d30000000000000000000000000000000000000000000000000000000000000000\",\n" +
                "      \"blockNumber\": \"0x218c445\",\n" +
                "      \"transactionHash\": \"0x0008cc23082353d2319339342bd8b588a729c1e145cf00e11903e26e3d5fcbea\",\n" +
                "      \"transactionIndex\": \"0x12\",\n" +
                "      \"blockHash\": \"0xd6ac6807cc9a5c7f7376fc0165ccc5a13a6dba1f3b8274e115bcb0e480c58cef\",\n" +
                "      \"logIndex\": \"0x1b\",\n" +
                "      \"removed\": false\n" +
                "    }\n" +
                "  ],\n" +
                "  \"logsBloom\": \"0x00000004000000000000000000000000000000000000000000000000000000000000000000000000000000200002000000000000000000000000000000000002040000000040000000000008000000000000002000000040000400000000000000000000000000000000000000000000000000010000000000000010000000000000000000000000000000000000000000040000000000000000000000000000000000004000000000000000000000000000000000000000000000000000000001000002000000000000000000000000000000001000000000000400000080200000000000000400000000000000000000000000000000000000000000000000\",\n" +
                "  \"status\": \"0x1\",\n" +
                "  \"to\": \"0x000000000d2e592685553cb51ab23eba8a6e1969\",\n" +
                "  \"transactionHash\": \"0x0008cc23082353d2319339342bd8b588a729c1e145cf00e11903e26e3d5fcbea\",\n" +
                "  \"transactionIndex\": \"0x12\",\n" +
                "  \"type\": \"0x0\"\n" +
                "}";
        System.out.println(decodeInputData(logs,"[]","0x0"));
    }


}
