package cn.xlystar.mc.udf;

import cn.xlystar.entity.TransferEvent;
import cn.xlystar.entity.UniswapEvent;
import cn.xlystar.helpers.ChainConfig;
import cn.xlystar.helpers.ConfigHelper;
import cn.xlystar.parse.ammswap.AMMSwapDataProcess;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
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

    public String evaluate(String originSender, String chainId, String protocol, String hash, List<String> swapEvents, List<String> transferEvents, String price) throws IOException {
        ChainConfig conf = new ConfigHelper().getConfig(chainId, protocol);
        List<Map<String, String>> maps = new ArrayList<>();
        String res = "";

        List<UniswapEvent> swapEventLists = new ArrayList<>();
        List<TransferEvent> finalEventLists = new ArrayList<>();
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
            if (!CollectionUtils.isEmpty(transferEvents)) {
                List<TransferEvent> transferEventLists = transferEvents.stream().map(t -> {
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

                swapEventLists.forEach(t -> {
                    boolean isInTransfer = false;
                    boolean isOutTransfer = false;
                    for (int i = 0; i < transferEventLists.size(); i++) {
                        TransferEvent transferEvent = transferEventLists.get(i);
                        if (transferEvent.getSender().equals(t.getSender())
                                && transferEvent.getLogIndex().divide(new BigInteger("1000")).equals(t.getLogIndex().divide(new BigInteger("1000"))) // outer instruction
                                && transferEvent.getContractAddress().equals(t.getTokenIn())
                                && transferEvent.getAmount().equals(t.getAmountIn())
                                && !isInTransfer
                        ) {
                            transferEventLists.remove(transferEvent);
                            isInTransfer = true;
                            continue;
                        }

                        if (transferEvent.getReceiver().equals(t.getTo())
                                && transferEvent.getLogIndex().divide(new BigInteger("1000")).equals(t.getLogIndex().divide(new BigInteger("1000"))) // outer instruction
                                && transferEvent.getContractAddress().equals(t.getTokenOut())
                                && transferEvent.getAmount().equals(t.getAmountOut())
                                && !isOutTransfer
                        ) {
                            transferEventLists.remove(transferEvent);
                            isOutTransfer = true;
                        }
                    }
                });
                finalEventLists = transferEventLists;
            }

            maps = AMMSwapDataProcess.decodeSwap(conf,
                    originSender,
                    hash,
                    swapEventLists,
                    finalEventLists,
                    price
            );
            res = JSON.toJSONString(maps);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(String.format("swapEvents:%s, transferEvents:%s, hash:%s, stack:%s, msg:%s", swapEventLists, finalEventLists, hash, Arrays.toString(e.getStackTrace()), e.getLocalizedMessage()));
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

    public static void main(String[] args) throws IOException {
        SolanaParse solanaParse = new SolanaParse();
        String originSender = "DjE156BsjKqti8fyqW94DbNtippAVcox7zPs2ybp5V43";
        String hash = "4y8q63rALSWZY5AyFQuQPZThTSnatUikPpiyrFAxMzCtRXPMrQGkaQHria19maLHgU1xNrLNJfm2cqp2Z1PguKn3";
        String swapList = "[{\"sender\":\"4xDsmeTWPNjgSVSS1VTfzFq3iHZhp77ffPkAmkZkdu71\",\"to\":\"4xDsmeTWPNjgSVSS1VTfzFq3iHZhp77ffPkAmkZkdu71\",\"token_in\":\"G3EDZoS49NRVKP8X1HggHZJueJeR8d2izUHeXdV3pump\",\"token_out\":\"So11111111111111111111111111111111111111112\",\"amount_in\":\"291475571661\",\"amount_out\":\"942604977\",\"log_index\":2001,\"contract_address\":\"9kmoptBojP24rAUwRGh5M3oH3FhtnAJE7ywcDhypd88x\",\"version\":\"raydium\"}, {\"sender\":\"4xDsmeTWPNjgSVSS1VTfzFq3iHZhp77ffPkAmkZkdu71\",\"to\":\"4xDsmeTWPNjgSVSS1VTfzFq3iHZhp77ffPkAmkZkdu71\",\"token_in\":\"So11111111111111111111111111111111111111112\",\"token_out\":\"AUdUEc98MGfEHJiJfCgMaW8gKdcfNDio8BFzGKBwjztC\",\"amount_in\":\"942604977\",\"amount_out\":\"19740855414033\",\"log_index\":2005,\"contract_address\":\"68vHUSKj8LZSWnvvjtSmgNMoiNwR7vS8t8t1DTaA1f53\",\"version\":\"raydium\"}]";
        String transferList = "[{\"sender\":\"4xDsmeTWPNjgSVSS1VTfzFq3iHZhp77ffPkAmkZkdu71\",\"receiver\":\"5Q544fKrFoe6tsEbD7S8EmxGTJYAKtTVhAW5Q5pge4j1\",\"block_time\":1737904990,\"amount\":\"291475571661\",\"log_index\":2002,\"contract_address\":\"G3EDZoS49NRVKP8X1HggHZJueJeR8d2izUHeXdV3pump\",\"asset_type\":\"token\",\"origin\":\"log\"}, {\"sender\":\"4xDsmeTWPNjgSVSS1VTfzFq3iHZhp77ffPkAmkZkdu71\",\"receiver\":\"CapuXNQoDviLvU1PxFiizLgPNQCxrsag1uMeyk6zLVps\",\"block_time\":1737904990,\"amount\":\"19740855414\",\"log_index\":2009,\"contract_address\":\"AUdUEc98MGfEHJiJfCgMaW8gKdcfNDio8BFzGKBwjztC\",\"asset_type\":\"token\",\"origin\":\"log\"}, {\"sender\":\"4xDsmeTWPNjgSVSS1VTfzFq3iHZhp77ffPkAmkZkdu71\",\"receiver\":\"DjE156BsjKqti8fyqW94DbNtippAVcox7zPs2ybp5V43\",\"block_time\":1737904990,\"amount\":\"19721114558619\",\"log_index\":2011,\"contract_address\":\"AUdUEc98MGfEHJiJfCgMaW8gKdcfNDio8BFzGKBwjztC\",\"asset_type\":\"token\",\"origin\":\"log\"}, {\"sender\":\"4xDsmeTWPNjgSVSS1VTfzFq3iHZhp77ffPkAmkZkdu71\",\"receiver\":\"GpMZbSM2GgvTKHJirzeGfMFoaZ8UR2X7F4v8vHTvxFbL\",\"block_time\":1737904990,\"amount\":\"942604977\",\"log_index\":2006,\"contract_address\":\"So11111111111111111111111111111111111111112\",\"asset_type\":\"token\",\"origin\":\"log\"}, {\"sender\":\"5Q544fKrFoe6tsEbD7S8EmxGTJYAKtTVhAW5Q5pge4j1\",\"receiver\":\"4xDsmeTWPNjgSVSS1VTfzFq3iHZhp77ffPkAmkZkdu71\",\"block_time\":1737904990,\"amount\":\"942604977\",\"log_index\":2003,\"contract_address\":\"So11111111111111111111111111111111111111112\",\"asset_type\":\"token\",\"origin\":\"log\"}, {\"sender\":\"DjE156BsjKqti8fyqW94DbNtippAVcox7zPs2ybp5V43\",\"receiver\":\"4xDsmeTWPNjgSVSS1VTfzFq3iHZhp77ffPkAmkZkdu71\",\"block_time\":1737904990,\"amount\":\"291475571661\",\"log_index\":2000,\"contract_address\":\"G3EDZoS49NRVKP8X1HggHZJueJeR8d2izUHeXdV3pump\",\"asset_type\":\"token\",\"origin\":\"log\"}, {\"sender\":\"GpMZbSM2GgvTKHJirzeGfMFoaZ8UR2X7F4v8vHTvxFbL\",\"receiver\":\"4xDsmeTWPNjgSVSS1VTfzFq3iHZhp77ffPkAmkZkdu71\",\"block_time\":1737904990,\"amount\":\"19740855414033\",\"log_index\":2007,\"contract_address\":\"AUdUEc98MGfEHJiJfCgMaW8gKdcfNDio8BFzGKBwjztC\",\"asset_type\":\"token\",\"origin\":\"log\"}]";
        String price =  "255";
        String evaluate = solanaParse.evaluate(originSender, "3", "swap", hash, JSONArray.parseArray(swapList, String.class),JSONArray.parseArray(transferList, String.class), price);
        System.out.println(evaluate);
    }


}