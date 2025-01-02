package cn.xlystar.mc.udf;

import cn.xlystar.entity.TransferEvent;
import cn.xlystar.entity.UniswapEvent;
import cn.xlystar.helpers.ChainConfig;
import cn.xlystar.helpers.ConfigHelper;
import cn.xlystar.parse.ammswap.AMMSwapDataProcess;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.odps.udf.UDF;
import org.apache.commons.collections.CollectionUtils;

import java.io.IOException;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 计算引擎：maxCompute
 * sol_amm_parse()
 * data: 要解析的数据
 */
public class SolanaParse extends UDF {
    public SolanaParse() {
    }

    public String evaluate(String originSender, String chainId, String protocol, String hash, List<String> swapEvents, List<String> transferEvents) throws IOException {
        ChainConfig conf = new ConfigHelper().getConfig(chainId, protocol);
        List<Map<String, String>> maps = new ArrayList<>();
        String res = "";

        List<UniswapEvent> swapEventLists = new ArrayList<>();
        List<TransferEvent> transferEventLists = new ArrayList<>();
        try {
            if (!CollectionUtils.isEmpty(swapEvents)) swapEventLists = swapEvents.stream().map(t -> {
                JSONObject json = JSONObject.parseObject(t);
                return UniswapEvent.builder()
                        .to(json.getString("to"))
                        .sender(json.getString("sender"))
                        .tokenIn(json.getString("token_in"))
                        .tokenOut(json.getString("token_out"))
                        .amountIn(new BigInteger(json.getString("amount_in")))
                        .amountOut(new BigInteger(json.getString("amount_out")))
                        .logIndex(json.getBigInteger("log_index"))
                        .contractAddress(json.getString("contract_address"))
                        .version(json.getString("version"))
                        .build();
            }).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(transferEvents)) transferEventLists = transferEvents.stream().map(t -> {
                JSONObject json = JSONObject.parseObject(t);
                return TransferEvent.builder()
                        .sender(json.getString("sender"))
                        .receiver(json.getString("receiver"))
                        .blockTime(json.getInteger("block_time"))
                        .amount(json.getBigInteger("amount"))
                        .logIndex(json.getBigInteger("log_index"))
                        .contractAddress(json.getString("contract_address"))
                        .assetType(json.getString("asset_type"))
                        .origin(json.getString("origin"))
                        .build();
            }).collect(Collectors.toList());
            maps = AMMSwapDataProcess.decodeSwap(conf,
                    originSender,
                    hash,
                    swapEventLists,
                    transferEventLists
            );
            res = JSON.toJSONString(maps);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(String.format("swapEvents:%s, transferEvents:%s, hash:%s, stack:%s, msg:%s", swapEventLists, transferEventLists, hash, Arrays.toString(e.getStackTrace()), e.getLocalizedMessage()));
        } catch (StackOverflowError | OutOfMemoryError e) {
            e.printStackTrace();
            maps = new ArrayList<>();
            HashMap<String, String> errorSwap = new HashMap<>();
            errorSwap.put("errorMsg", "oom");
            errorSwap.put("chain", chainId);
            errorSwap.put("protocol", protocol);
            maps.add(errorSwap);
            throw new RuntimeException(String.format("swapEvents:%s, transferEvents:%s, hash:%s", swapEvents, transferEvents, hash));
        }
        return res;
    }


}