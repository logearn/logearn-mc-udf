package cn.xlystar.parse.ammswap;

import cn.xlystar.entity.LiquidityEvent;
import cn.xlystar.entity.TransferEvent;
import cn.xlystar.helpers.ChainConfig;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Slf4j
public class AMMLiquidityDataProcess {

    /**
     * 格式化最后的返回值
     */
    public static List<LiquidityEvent> parseFullLiquidity(String sender, ChainConfig conf, String logs, String internalTxs, String hash) throws IOException {
        // 从内部交易中找到有效的交易，即：value>0的交易，且type = call
        List<TransferEvent> validInternalTxs = InternalTx.parseTx(internalTxs, conf.getWCoinAddress());
        log.debug("******* Log 中找到符合条件(value > 0 且 callType == call)的 InnelTx： {} 条", validInternalTxs.size());
        return parseAllLiquidityLogs(sender, conf, logs, hash, validInternalTxs);
    }

    /**
     * 解析所有的swap
     */
    public static List<LiquidityEvent> parseAllLiquidityLogs(String originSender, ChainConfig conf, String logs, String hash, List<TransferEvent> validInternalTxs) throws IOException {
        if (logs.isEmpty()) {
            return new ArrayList<>();
        }

        // 1、将log string转为对象
        JsonNode txLog = Log.parseLogsFromString(logs);

        // 2、从log对象中解析 transfer 事件
        List<TransferEvent> transferEvents = Log.findTransfer(txLog);
        log.debug("******* Log 中找到符合条件 TransferEvent： {} 条", transferEvents.size());
        transferEvents.sort(Comparator.comparing(TransferEvent::getLogIndex));

        // 3、获取 流动性 事件, 且删除构建中使用的 transferEvent
        return getLiquidityEvents(conf, transferEvents, txLog, originSender, hash);
    }

    public static List<LiquidityEvent> getLiquidityEvents(ChainConfig conf, List<TransferEvent> transferEvents, JsonNode txLog, String originSender, String hash) {
        // 1、找到所有的流动性事件
        List<LiquidityEvent> liquidityEvents = Log.findLiquidityEvents(originSender, conf.getProtocol(), txLog);
        log.debug("******* log 中 找到 liquidityEvents：{} 条", liquidityEvents.size());
        Collections.sort(liquidityEvents, Comparator.comparing(LiquidityEvent::getLogIndex));

        // 2、 结合 TransferEvent 给流动性事件补边，构建完成以后并且删除构建中使用的 TransferEvent
        liquidityEvents = Log.fillLiquidityEventWithTransferEvent(transferEvents, liquidityEvents, hash);
        log.debug("******* 将 {} 条 liquidityEvents 转化成标准 liquidityEvents，共消费 {} 条 transferEvents", liquidityEvents.size(), transferEvents.size());
        log.debug("******* 当前 liquidityEvents: {}, 条， 还剩余 transferEvents： {} 条", liquidityEvents.size(), transferEvents.size());
        return liquidityEvents;
    }



}

