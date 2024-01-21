package cn.xlystar.parse;

import cn.xlystar.entity.TransferEvent;
import cn.xlystar.entity.Tx;
import cn.xlystar.parse.pancake.InternalTx;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.NullNode;

import java.io.IOException;
import java.math.BigInteger;
import java.util.*;


public class PancakeSwapDataProcess {
    private static String ROUTE_V2 = "0x10ED43C718714eb63d5aA57B78B54704E256024E".toLowerCase();
    private static String ROUTE_V3 = "0x13f4EA83D0bd40E75C8222255bc855a974568Dd4".toLowerCase();
    public static String WBNB = "0xbb4cdb9cbd36b01bd1cbaebf2de08d9173bc095c".toLowerCase();
    public static String ZEROADDR = "0x0000000000000000000000000000000000000000".toLowerCase();
    public static List<Map<String, String>> decodeInputData(String inputData, String from, String to, String value, String logs, String internalTxs, String hash) throws IOException {
        if (inputData.length() < 10) {
            return Collections.emptyList();
        }
        // 将内部交易的string转为json
        JsonNode txTrace = InternalTx.parseTrace(internalTxs);
        // 从内部交易中找到有效的交易，即：value>0的交易，且type = call
        List<TransferEvent> validInternalTxs = InternalTx.parseTx(txTrace, WBNB);
        // 从logs中解析swap
        List<Map<String, String>> maps = PancakeSwapDataProcessFull.parseFullUniswap(logs, hash, validInternalTxs);
        for (int i = 0, len = maps.size(); i < len; i++) {
            Map<String, String> t = maps.get(i);
            if (t.get("errorMsg") != null || t.get("tokenIn") == null || t.get("tokenOut") == null) {
                maps.remove(i);
                len--;
                i--;
            }
        }
        return maps;
    }

    public static List<Map<String, String>> decodeInputData(String logs, String internalTxs, String hash) throws IOException {
        return decodeInputData("", "", "", "", logs, internalTxs, hash);
    }


}
