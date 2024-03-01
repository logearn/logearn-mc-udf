package cn.xlystar.parse.ammswap;

import cn.xlystar.entity.TransferEvent;
import cn.xlystar.helpers.ChainConfig;
import cn.xlystar.helpers.ConfigHelper;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class AMMSwapDataProcess {
    public static String ZEROADDR = "0x0000000000000000000000000000000000000000".toLowerCase();

    public static List<Map<String, String>> decodeInputData(ChainConfig conf, String inputData, String from, String to, String value, String logs, String internalTxs, String hash) throws IOException {
        // 从内部交易中找到有效的交易，即：value>0的交易，且type = call
        List<TransferEvent> validInternalTxs = InternalTx.parseTx(internalTxs, conf.getWCoinAddress());
        // 从logs中解析swap
        List<Map<String, String>> maps = AMMSwapDataProcessFull.parseFullUniswap(conf, logs, hash, validInternalTxs);
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

    public static List<Map<String, String>> decodeInputData(ChainConfig conf, String logs, String internalTxs, String hash) throws IOException {
        return decodeInputData(conf, "", "", "", "", logs, internalTxs, hash);
    }

    public static void main(String[] args) throws IOException {
        String logs = "{\"logs\":[{\"address\":\"0xc02aaa39b223fe8d0a0e5c4f27ead9083c756cc2\",\"data\":\"0x00000000000000000000000000000000000000000000000001d923f2514cfefe\",\"logindex\":\"0x8d\",\"topics\":[\"0xe1fffcc4923d04b559f4d29a8bfc6cda04eb5b0d3c460751c2402c5c5cc9109c\",\"0x0000000000000000000000007a250d5630b4cf539739df2c5dacb4c659f2488d\"]},{\"address\":\"0xc02aaa39b223fe8d0a0e5c4f27ead9083c756cc2\",\"data\":\"0x00000000000000000000000000000000000000000000000001d923f2514cfefe\",\"logindex\":\"0x8e\",\"topics\":[\"0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef\",\"0x0000000000000000000000007a250d5630b4cf539739df2c5dacb4c659f2488d\",\"0x0000000000000000000000000481146570c926aae7b5c961c192144fd64976db\"]},{\"address\":\"0x54b216f11abd2c16d02d8992c32dfbb8e1c131ff\",\"data\":\"0x0000000000000000000000000000000000000000000211654585005212800000\",\"logindex\":\"0x8f\",\"topics\":[\"0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef\",\"0x0000000000000000000000000481146570c926aae7b5c961c192144fd64976db\",\"0x00000000000000000000000054b216f11abd2c16d02d8992c32dfbb8e1c131ff\"]},{\"address\":\"0x54b216f11abd2c16d02d8992c32dfbb8e1c131ff\",\"data\":\"0x00000000000000000000000000000000000000000006342fd08f00f637800000\",\"logindex\":\"0x90\",\"topics\":[\"0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef\",\"0x0000000000000000000000000481146570c926aae7b5c961c192144fd64976db\",\"0x000000000000000000000000ff56a01f2dc20a824e742fdb08a80f7803f5b333\"]},{\"address\":\"0x0481146570c926aae7b5c961c192144fd64976db\",\"data\":\"0x000000000000000000000000000000000000000000df56b9541c229fce00000000000000000000000000000000000000000000000000000033999b76bc8e5b64\",\"logindex\":\"0x91\",\"topics\":[\"0x1c411e9a96e071241c2f21f7726b17ae89e3cab4c78be50e062b03a9fffbbad1\"]},{\"address\":\"0x0481146570c926aae7b5c961c192144fd64976db\",\"data\":\"0x000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000001d923f2514cfefe000000000000000000000000000000000000000000084595161401484a0000000000000000000000000000000000000000000000000000000000000000000000\",\"logindex\":\"0x92\",\"topics\":[\"0xd78ad95fa46c994b6551d0da85fc275fe613ce37657fb8d5e3d130840159d822\",\"0x0000000000000000000000007a250d5630b4cf539739df2c5dacb4c659f2488d\",\"0x000000000000000000000000ff56a01f2dc20a824e742fdb08a80f7803f5b333\"]}]}";
        String interTx = "[{\"action\":{\"from\":\"0x7a250d5630b4cf539739df2c5dacb4c659f2488d\",\"to\":\"0x0481146570c926aae7b5c961c192144fd64976db\",\"input\":\"0x022c0d9f000000000000000000000000000000000000000000084595161401484a0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000ff56a01f2dc20a824e742fdb08a80f7803f5b33300000000000000000000000000000000000000000000000000000000000000800000000000000000000000000000000000000000000000000000000000000000\",\"value\":\"0x0\",\"calltype\":\"call\"}},{\"action\":{\"from\":\"0x7a250d5630b4cf539739df2c5dacb4c659f2488d\",\"to\":\"0x0481146570c926aae7b5c961c192144fd64976db\",\"input\":\"0x0902f1ac\",\"value\":\"0x0\",\"calltype\":\"staticcall\"}},{\"action\":{\"from\":\"0x0481146570c926aae7b5c961c192144fd64976db\",\"to\":\"0xc02aaa39b223fe8d0a0e5c4f27ead9083c756cc2\",\"input\":\"0x70a082310000000000000000000000000481146570c926aae7b5c961c192144fd64976db\",\"value\":\"0x0\",\"calltype\":\"staticcall\"}},{\"action\":{\"from\":\"0x0481146570c926aae7b5c961c192144fd64976db\",\"to\":\"0x54b216f11abd2c16d02d8992c32dfbb8e1c131ff\",\"input\":\"0xa9059cbb000000000000000000000000ff56a01f2dc20a824e742fdb08a80f7803f5b333000000000000000000000000000000000000000000084595161401484a000000\",\"value\":\"0x0\",\"calltype\":\"call\"}},{\"action\":{\"from\":\"0x7a250d5630b4cf539739df2c5dacb4c659f2488d\",\"to\":\"0xc02aaa39b223fe8d0a0e5c4f27ead9083c756cc2\",\"input\":\"0xd0e30db0\",\"value\":\"0x1d923f2514cfefe\",\"calltype\":\"call\"}},{\"action\":{\"from\":\"0x7a250d5630b4cf539739df2c5dacb4c659f2488d\",\"to\":\"0xc02aaa39b223fe8d0a0e5c4f27ead9083c756cc2\",\"input\":\"0xa9059cbb0000000000000000000000000481146570c926aae7b5c961c192144fd64976db00000000000000000000000000000000000000000000000001d923f2514cfefe\",\"value\":\"0x0\",\"calltype\":\"call\"}},{\"action\":{\"from\":\"0x7a250d5630b4cf539739df2c5dacb4c659f2488d\",\"to\":\"0xff56a01f2dc20a824e742fdb08a80f7803f5b333\",\"input\":\"0x\",\"value\":\"0x1bf3fe4edb8d5ec\",\"calltype\":\"call\"}},{\"action\":{\"from\":\"0xff56a01f2dc20a824e742fdb08a80f7803f5b333\",\"to\":\"0x7a250d5630b4cf539739df2c5dacb4c659f2488d\",\"input\":\"0xfb3bdb41000000000000000000000000000000000000000000084595161401484a0000000000000000000000000000000000000000000000000000000000000000000080000000000000000000000000ff56a01f2dc20a824e742fdb08a80f7803f5b3330000000000000000000000000000000000000000000000000000000065bb52650000000000000000000000000000000000000000000000000000000000000002000000000000000000000000c02aaa39b223fe8d0a0e5c4f27ead9083c756cc200000000000000000000000054b216f11abd2c16d02d8992c32dfbb8e1c131ff\",\"value\":\"0x39863d73f05d4ea\",\"calltype\":\"call\"}},{\"action\":{\"from\":\"0x0481146570c926aae7b5c961c192144fd64976db\",\"to\":\"0x54b216f11abd2c16d02d8992c32dfbb8e1c131ff\",\"input\":\"0x70a082310000000000000000000000000481146570c926aae7b5c961c192144fd64976db\",\"value\":\"0x0\",\"calltype\":\"staticcall\"}}]";

        String hash = "0xa1a3f100f08d6eb1c723e682a95ef0625ee52dd0e01bd50b2cd247813e91d398";
        ConfigHelper configHelper = new ConfigHelper();
        ChainConfig config = configHelper.getConfig("1", "uniswap");
        System.out.println(decodeInputData(config, logs, interTx, hash));
    }
}

