package cn.xlystar.parse.ammswap;

import cn.xlystar.entity.TransferEvent;
import cn.xlystar.entity.UniswapEvent;
import cn.xlystar.helpers.ChainConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

public class AddressTagProcess {

    public static Map<String, Set<String>> getAddressTagList(ChainConfig conf, String log, String internalTxs, String hash) throws IOException {
        List<UniswapEvent> uniswapEvents = new ArrayList<>();

        Map<String, Set<String>> addressTagList = new HashMap<>();
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
        List<UniswapEvent> uniswapV2Events = Log.findSwapV2(conf.getProtocol(), txLog);
        List<UniswapEvent> uniswapV3Events = Log.findSwapV3(conf.getProtocol(), txLog);
        uniswapEvents.addAll(uniswapV2Events);
        uniswapEvents.addAll(uniswapV3Events);

        // 3、记录所有 token 地址和 池子地址
        addressTagList.put("contract", new HashSet<>()); // 合约地址
        addressTagList.put("erc20", new HashSet<>());  // erc420 类型资产
        addressTagList.put("erc404", new HashSet<>()); // erc404 类型资产
        addressTagList.put("pool_uniswap_v2", new HashSet<>()); // uniswap v2 池子
        addressTagList.put("pool_uniswap_v3", new HashSet<>()); // uniswap v3 池子
        addressTagList.put("1000_eth_trader", new HashSet<>()); // 1000eth 交易的 巨鲸
        addressTagList.put("500_eth_trader", new HashSet<>()); // 500eth 交易的 巨鲸
        addressTagList.put("100_eth_trader", new HashSet<>()); // 100eth 交易的 巨鲸
        addressTagList.put("whale_trader", new HashSet<>()); // 巨鲸
        addressTagList.put("arbitrage_trader", new HashSet<>()); // 套利合约
        // 4、给 地址 打 contract 标签
        addressTagList.get("contract").addAll(Log.getContractAddress(txLog));
        addressTagList.get("contract").addAll(InternalTx.getContractAddress(internalTxs));

        // 5、给 地址 打 erc20 标签, erc404 标签
        transferEvents.forEach(t -> {
            if (t.getAssetType().equals("erc20")) addressTagList.get("erc20").add(t.getContractAddress());
            if (t.getAssetType().equals("erc404")) addressTagList.get("erc404").add(t.getContractAddress());
        });

        // 6、 将v2和v3构建为标准的swapEvent事件
        // 6.1 构建v2, 给地址打 pool_uniswap_v2 标签
        Result parseMapResult = Log.fillSwapTokenInAndTokenOutWithTransferEvent(transferEvents, uniswapEvents, hash);
        parseMapResult.getUniswapEvents().forEach(u -> {
            String key = "pool_uniswap_" + u.getVersion();
            addressTagList.get(key).add(u.getContractAddress());
        });

        // 7、套利合约交易者 tag
        uniswapEvents = UniswapEvent.merge(uniswapEvents);
        uniswapEvents.forEach(uniswapEvent -> {
            if (StringUtils.isEmpty(uniswapEvent.getTokenIn()) || StringUtils.isEmpty(uniswapEvent.getTokenOut()))
                return;
            if (uniswapEvent.getTokenIn().equals(uniswapEvent.getTokenOut()))
                addressTagList.get("arbitrage_trader").add(uniswapEvent.getSender());
            String wCoinAddress = conf.getWCoinAddress();
            BigInteger amountOut = uniswapEvent.getAmountOut();
            BigInteger amountIn = uniswapEvent.getAmountIn();
            String tokenOut = uniswapEvent.getTokenOut();
            String tokenIn = uniswapEvent.getTokenIn();
            BigInteger wethAmount = tokenOut.equals(wCoinAddress) ? amountOut : (tokenIn.equals(wCoinAddress) ? amountIn : BigInteger.ZERO);
            if (wethAmount.compareTo(new BigInteger("1000").multiply(new BigDecimal("1e18").toBigInteger())) > 0) {
                addressTagList.get("1000_eth_trader").add(uniswapEvent.getSender());
                addressTagList.get("whale_trader").add(uniswapEvent.getSender());
            } else if (wethAmount.compareTo(new BigInteger("500").multiply(new BigDecimal("1e18").toBigInteger())) > 0) {
                addressTagList.get("500_eth_trader").add(uniswapEvent.getSender());
                addressTagList.get("whale_trader").add(uniswapEvent.getSender());
            } else if (wethAmount.compareTo(new BigInteger("100").multiply(new BigDecimal("1e18").toBigInteger())) > 0) {
                addressTagList.get("100_eth_trader").add(uniswapEvent.getSender());
                addressTagList.get("whale_trader").add(uniswapEvent.getSender());
            }
        });
        return addressTagList;
    }


}
