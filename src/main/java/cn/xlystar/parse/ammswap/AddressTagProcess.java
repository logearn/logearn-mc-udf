package cn.xlystar.parse.ammswap;

import cn.xlystar.entity.TransferEvent;
import cn.xlystar.entity.UniswapEvent;
import cn.xlystar.helpers.ChainConfig;
import cn.xlystar.helpers.ConfigHelper;
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


    public static void main(String[] args) throws IOException {
        String logs = "{\"logs\":[{\"address\":\"0x02b15c47b4b516a22fd2d8b1fc662afb808a2169\",\"data\":\"0x00000000000000000000000000000000000000000000000001020ea2b41ae2900000000000000000000000000000000000000000000000000030d3948349aa04\",\"logindex\":\"0xe3\",\"topics\":[\"0x34fcbac0073d7c3d388e51312faf357774904998eeb8fca628b9e6f65ee1cbf7\",\"0x0000000000000000000000009a1edddb86937ef80112160814b94a8bd1811ba1\"]}]}";
        String interTx = "[{\"action\":{\"from\":\"0xdef1c0ded9bec7f1a1670819833240f027b25eff\",\"to\":\"0x67f4c72a50f8df6487720261e188f2abe83f57d7\",\"input\":\"0x23b872dd0000000000000000000000004b6cba984d126182443afe8e578566683a32482f000000000000000000000000a7fd8ff8f4cada298286d3006ee8f9c11e2ff84e000000000000000000000000000000000000000000000000000000081a5fde55\",\"value\":\"0x0\",\"calltype\":\"call\"}},{\"action\":{\"from\":\"0xa7fd8ff8f4cada298286d3006ee8f9c11e2ff84e\",\"to\":\"0xc02aaa39b223fe8d0a0e5c4f27ead9083c756cc2\",\"input\":\"0xa9059cbb000000000000000000000000def1c0ded9bec7f1a1670819833240f027b25eff0000000000000000000000000000000000000000000000001666d8e832e0188a\",\"value\":\"0x0\",\"calltype\":\"call\"}},{\"action\":{\"from\":\"0xdef1c0ded9bec7f1a1670819833240f027b25eff\",\"to\":\"0x4b6cba984d126182443afe8e578566683a32482f\",\"input\":\"0x\",\"value\":\"0x1666d8e832e0188a\",\"calltype\":\"call\"}},{\"action\":{\"from\":\"0x4b6cba984d126182443afe8e578566683a32482f\",\"to\":\"0xdef1c0ded9bec7f1a1670819833240f027b25eff\",\"input\":\"0xd9627aa40000000000000000000000000000000000000000000000000000000000000080000000000000000000000000000000000000000000000000000000081a5fde5500000000000000000000000000000000000000000000000015bacd47a1fd36850000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000200000000000000000000000067f4c72a50f8df6487720261e188f2abe83f57d7000000000000000000000000eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee869584cd000000000000000000000000382ffce2287252f930e1c8dc9328dac5bf282ba100000000000000000000000000000000a1bad7d5f1df2e15290ee1218428336c\",\"value\":\"0x0\",\"calltype\":\"call\"}},{\"action\":{\"from\":\"0xdef1c0ded9bec7f1a1670819833240f027b25eff\",\"to\":\"0xc02aaa39b223fe8d0a0e5c4f27ead9083c756cc2\",\"input\":\"0x2e1a7d4d0000000000000000000000000000000000000000000000001666d8e832e0188a\",\"value\":\"0x0\",\"calltype\":\"call\"}},{\"action\":{\"from\":\"0xdef1c0ded9bec7f1a1670819833240f027b25eff\",\"to\":\"0xf9b30557afcf76ea82c04015d80057fa2147dfa9\",\"input\":\"0xd9627aa40000000000000000000000000000000000000000000000000000000000000080000000000000000000000000000000000000000000000000000000081a5fde5500000000000000000000000000000000000000000000000015bacd47a1fd36850000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000200000000000000000000000067f4c72a50f8df6487720261e188f2abe83f57d7000000000000000000000000eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee869584cd000000000000000000000000382ffce2287252f930e1c8dc9328dac5bf282ba100000000000000000000000000000000a1bad7d5f1df2e15290ee1218428336c\",\"value\":\"0x0\",\"calltype\":\"delegatecall\"}},{\"action\":{\"from\":\"0xdef1c0ded9bec7f1a1670819833240f027b25eff\",\"to\":\"0xa7fd8ff8f4cada298286d3006ee8f9c11e2ff84e\",\"input\":\"0x0902f1ac\",\"value\":\"0x0\",\"calltype\":\"staticcall\"}},{\"action\":{\"from\":\"0xa7fd8ff8f4cada298286d3006ee8f9c11e2ff84e\",\"to\":\"0x67f4c72a50f8df6487720261e188f2abe83f57d7\",\"input\":\"0x70a08231000000000000000000000000a7fd8ff8f4cada298286d3006ee8f9c11e2ff84e\",\"value\":\"0x0\",\"calltype\":\"staticcall\"}},{\"action\":{\"from\":\"0xc02aaa39b223fe8d0a0e5c4f27ead9083c756cc2\",\"to\":\"0xdef1c0ded9bec7f1a1670819833240f027b25eff\",\"input\":\"0x\",\"value\":\"0x1666d8e832e0188a\",\"calltype\":\"call\"}},{\"action\":{\"from\":\"0xdef1c0ded9bec7f1a1670819833240f027b25eff\",\"to\":\"0xa7fd8ff8f4cada298286d3006ee8f9c11e2ff84e\",\"input\":\"0x022c0d9f00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000001666d8e832e0188a000000000000000000000000def1c0ded9bec7f1a1670819833240f027b25eff00000000000000000000000000000000000000000000000000000000000000800000000000000000000000000000000000000000000000000000000000000000\",\"value\":\"0x0\",\"calltype\":\"call\"}},{\"action\":{\"from\":\"0xa7fd8ff8f4cada298286d3006ee8f9c11e2ff84e\",\"to\":\"0xc02aaa39b223fe8d0a0e5c4f27ead9083c756cc2\",\"input\":\"0x70a08231000000000000000000000000a7fd8ff8f4cada298286d3006ee8f9c11e2ff84e\",\"value\":\"0x0\",\"calltype\":\"staticcall\"}}]";

        System.out.println();

        String hash = "0xa1a3f100f08d6eb1c723e682a95ef0625ee52dd0e01bd50b2cd247813e91d398";
        ConfigHelper configHelper = new ConfigHelper();
        ChainConfig config = configHelper.getConfig("1", "uniswap");
        System.out.println(getAddressTagList(config, logs, interTx, hash));
    }


}
