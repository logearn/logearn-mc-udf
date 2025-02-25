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
                    Iterator<TransferEvent> iterator = transferEventLists.iterator();
                    while (iterator.hasNext()) {
                        TransferEvent transferEvent = iterator.next();

                        if (transferEvent.getSender().equals(t.getSender())
                                && transferEvent.getLogIndex().divide(new BigInteger("1000")).equals(t.getLogIndex().divide(new BigInteger("1000"))) // outer instruction
                                && transferEvent.getContractAddress().equals(t.getTokenIn())
                                && transferEvent.getAmount().equals(t.getAmountIn())
                                && !isInTransfer
                        ) {
                            iterator.remove(); // 使用迭代器安全地移除元素
                            isInTransfer = true;
                            continue;
                        }

                        if (transferEvent.getReceiver().equals(t.getTo())
                                && transferEvent.getLogIndex().divide(new BigInteger("1000")).equals(t.getLogIndex().divide(new BigInteger("1000"))) // outer instruction
                                && transferEvent.getContractAddress().equals(t.getTokenOut())
                                && transferEvent.getAmount().equals(t.getAmountOut())
                                && !isOutTransfer
                        ) {
                            iterator.remove(); // 使用迭代器安全地移除元素
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
            throw new RuntimeException(String.format("price:%s,swapEvents:%s, transferEvents:%s, hash:%s, stack:%s, msg:%s", price, swapEventLists, finalEventLists, hash, Arrays.toString(e.getStackTrace()), e.getLocalizedMessage()));
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
        String originSender = "8w15mVWttbKurtphm4z7YfTAQqdx79s7M3U45gCuMotS";
        String hash = "8w15mVWttbKurtphm4z7YfTAQqdx79s7M3U45gCuMotS";
        String swapList = "[{\"sender\":\"2QdgkUTBsPJAQ8gcUUAWF3J2EwLap7rHJxQ5CyRs8s6E\",\"to\":\"2QdgkUTBsPJAQ8gcUUAWF3J2EwLap7rHJxQ5CyRs8s6E\",\"token_in\":\"So11111111111111111111111111111111111111112\",\"token_out\":\"43YakhC3TcSuTgSXnxFgw8uKL8VkuLuFa4M6Bninpump\",\"amount_in\":\"2500000000\",\"amount_out\":\"1216146905549\",\"log_index\":1005,\"contract_address\":\"CaysL4cjU1BuB9ECvhQ4yNQBVt7eug3GcZjndcJdf5JU\",\"version\":\"raydium\"}]";
        String transferList = "[{\"sender\":\"2QdgkUTBsPJAQ8gcUUAWF3J2EwLap7rHJxQ5CyRs8s6E\",\"receiver\":\"5Q544fKrFoe6tsEbD7S8EmxGTJYAKtTVhAW5Q5pge4j1\",\"block_time\":1739613966,\"amount\":\"2500000000\",\"log_index\":1006,\"contract_address\":\"So11111111111111111111111111111111111111112\",\"asset_type\":\"token\",\"origin\":\"log\"}, {\"sender\":\"5Q544fKrFoe6tsEbD7S8EmxGTJYAKtTVhAW5Q5pge4j1\",\"receiver\":\"2QdgkUTBsPJAQ8gcUUAWF3J2EwLap7rHJxQ5CyRs8s6E\",\"block_time\":1739613966,\"amount\":\"1216146905549\",\"log_index\":1007,\"contract_address\":\"43YakhC3TcSuTgSXnxFgw8uKL8VkuLuFa4M6Bninpump\",\"asset_type\":\"token\",\"origin\":\"log\"}]";
        String price =  "255";
        String evaluate = solanaParse.evaluate(originSender, "3", "swap", hash, JSONArray.parseArray(swapList, String.class),JSONArray.parseArray(transferList, String.class), price);
        System.out.println(evaluate);
    }


}