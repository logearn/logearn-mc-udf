package cn.xlystar.parse.ammswap;

import cn.xlystar.entity.TransferEvent;
import cn.xlystar.entity.UniswapV2Event;
import cn.xlystar.entity.UniswapV3Event;
import cn.xlystar.helpers.ChainConfig;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.*;

public class AddressTagProcess {

    public static Map<String, Set<String>> getAddressTagList(ChainConfig conf, String log, String internalTxs, String hash) throws IOException {
        Map<String,Set<String>> addressTagList = new HashMap<>();
        if (log.isEmpty()) {
            return addressTagList;
        }

        // 1、将log string转为对象
        JsonNode txLog = Log.parseLogsFromString(log);

        List<TransferEvent> validInternalTxs = InternalTx.parseTx(internalTxs, conf.getWCoinAddress());

        // 2、从log对象中解析 transfer 事件
        List<TransferEvent> transferEvents = Log.findTransfer(txLog);

        // 3、将有效的内部交易添加到transfer事件
        transferEvents.addAll(validInternalTxs);
        System.out.printf("******* 将有效的内部交易添加到transfer事件中， 当前 总共 transferEvents： %s 条。\n", transferEvents.size());

        // 4、获取 swap 事件
        List<UniswapV2Event> uniswapV2Events = Log.findSwapV2(conf.getProtocol(), txLog);
        List<UniswapV3Event> uniswapV3Events = Log.findSwapV3(conf.getProtocol(), txLog);

        // 3、记录所有 token 地址和 池子地址
        addressTagList.put("contract", new HashSet<>());
        addressTagList.put("token", new HashSet<>());
        addressTagList.put("pool_uniswap_v2", new HashSet<>());
        addressTagList.put("pool_uniswap_v3", new HashSet<>());
        // 4、给 地址 打 contract 标识
        addressTagList.get("contract").addAll(Log.getContractAddress(txLog));
        addressTagList.get("contract").addAll(InternalTx.getContractAddress(internalTxs));

        // 5、给 地址 打 token 标识
        transferEvents.forEach(t -> addressTagList.get("token").add(t.getContractAddress()));

        // 6、 将v2和v3构建为标准的swapEvent事件
        // 6.1 构建v2, 给地址打 pool_uniswap_v2 标识
        Result parseMapResultv2 = Log.parseUniswapV2ToUniswapEvent(transferEvents, uniswapV2Events, hash);
        parseMapResultv2.getUniswapEvents().forEach(v2Pool -> addressTagList.get("pool_uniswap_v2").add(v2Pool.getContractAddress()));
        System.out.printf("******* 将 %s 条 v2swap 转化成标准 Swap，共消费 %s 条 transferEvents\n", parseMapResultv2.getUniswapEvents().size(), parseMapResultv2.getTransferEvents().size());

        // 6.2 构建v3, 给地址打 pool_uniswap_v3 标识
        Result parseMapResultv3 = Log.parseUniswapV3ToUniswapEvent(transferEvents, uniswapV3Events, hash);
        parseMapResultv3.getUniswapEvents().forEach(v3Pool -> addressTagList.get("pool_uniswap_v3").add(v3Pool.getContractAddress()));
        System.out.printf("******* 将 %s 条 v3swap 转化成标准 Swap，共消费 %s 条 transferEvents\n", parseMapResultv3.getUniswapEvents().size(), parseMapResultv3.getTransferEvents().size());

        return addressTagList;
    }


}
