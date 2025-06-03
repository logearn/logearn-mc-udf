package cn.xlystar.parse.ammswap;

import cn.xlystar.entity.TransferEvent;
import cn.xlystar.entity.UniswapEvent;
import cn.xlystar.helpers.ChainConfig;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
public class AMMSwapDataProcess {
    public static String ZEROADDR = "0x0000000000000000000000000000000000000000".toLowerCase();

    public static List<Map<String, String>> decodeInputData(ChainConfig conf, String inputData, String from, String to, String value, String logs, String internalTxs, String hash, String price) throws IOException {

        // 从内部交易中找到有效的交易，即：value>0的交易，且type = call
        List<TransferEvent> validInternalTxs = InternalTx.parseTx(internalTxs, conf.getWCoinAddress());
        log.debug("******* Log 中找到符合条件(value > 0 且 callType == call)的 InnelTx： {} 条", validInternalTxs.size());

        // 从logs中解析swap
        List<Map<String, String>> maps = AMMSwapDataProcessFull.parseFullUniswap(from, conf, logs, hash, validInternalTxs, price);
        return maps;
    }

    public static List<Map<String, String>> decodeSwap(ChainConfig conf, String originSender, String hash, List<UniswapEvent> swapEvents, List<TransferEvent> transferEvents, String price) {
        return AMMSwapDataProcessFull.parseSolanaSwap(conf, originSender, hash, swapEvents, transferEvents, price);
    }



}

