package cn.xlystar.parse.uniswap;

import cn.xlystar.entity.TransferEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class UniSwapDataProcess {

    private static String ROUTE_V2 = "0x7a250d5630b4cf539739df2c5dacb4c659f2488d".toLowerCase();
    private static String ROUTE_V3 = "0xe592427a0aece92de3edee1f18e0157c05861564".toLowerCase();

    public static String WETH = "0xc02aaa39b223fe8d0a0e5c4f27ead9083c756cc2".toLowerCase();
    public static String ZEROADDR = "0x0000000000000000000000000000000000000000".toLowerCase();

    public static List<Map<String, String>> decodeInputData(String inputData, String from, String to, String value, String logs, String internalTxs, String hash) throws IOException {
        // 将内部交易的string转为json
        JsonNode txTrace = InternalTx.parseTrace(internalTxs);
        // 从内部交易中找到有效的交易，即：value>0的交易，且type = call
        List<TransferEvent> validInternalTxs = InternalTx.parseTx(txTrace, WETH);
        // 从logs中解析swap
        List<Map<String, String>> maps = UniSwapDataProcessFull.parseFullUniswap(logs, hash, validInternalTxs);
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

