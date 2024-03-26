package cn.xlystar.parse.ammswap;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import cn.xlystar.entity.TransferEvent;
import cn.xlystar.helpers.ChainConfig;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AMMSwapDataProcess {
    public static String ZEROADDR = "0x0000000000000000000000000000000000000000".toLowerCase();

    public static List<Map<String, String>> decodeInputData(ChainConfig conf, String inputData, String from, String to, String value, String logs, String internalTxs, String hash) throws IOException {

        // 从内部交易中找到有效的交易，即：value>0的交易，且type = call
        List<TransferEvent> validInternalTxs = InternalTx.parseTx(internalTxs, conf.getWCoinAddress());
        log.debug("******* Log 中找到符合条件(value > 0 且 callType == call)的 InnelTx： {} 条", validInternalTxs.size());

        // 从logs中解析swap
        List<Map<String, String>> maps = AMMSwapDataProcessFull.parseFullUniswap(conf, logs, hash, validInternalTxs);
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

    public static List<Map<String, String>> decodeInputData(ChainConfig conf, String logs, String internalTxs, String hash) throws IOException {
        return decodeInputData(conf, "", "", "", "", logs, internalTxs, hash);
    }

}

