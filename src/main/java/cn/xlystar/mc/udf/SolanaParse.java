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
        String originSender = "J9mMr2nzTrKvMBVPr3RN8Ey5wkhd8g3iuruSPBnUgJQm";
        String hash = "4y8q63rALSWZY5AyFQuQPZThTSnatUikPpiyrFAxMzCtRXPMrQGkaQHria19maLHgU1xNrLNJfm2cqp2Z1PguKn3";
        String swapList = "[{\"sender\":\"J9mMr2nzTrKvMBVPr3RN8Ey5wkhd8g3iuruSPBnUgJQm\",\"to\":\"J9mMr2nzTrKvMBVPr3RN8Ey5wkhd8g3iuruSPBnUgJQm\",\"token_in\":\"So11111111111111111111111111111111111111112\",\"token_out\":\"6p6xgHyF7AeE6TZkSmFsko444wqoP15icUSqi2jfGiPN\",\"amount_in\":\"3194672\",\"amount_out\":\"292086\",\"log_index\":2004,\"contract_address\":\"A8dZPP72tn7HAHe4h3ocjE8U3ePFGRsnbugeraqVAntY\",\"version\":\"meteoral\"}]";
        String transferList = "[{\"sender\":\"3AbG3ZA19fJKjTSTMTCz7j2bodPagXog4PwTBi8H7UA4\",\"receiver\":\"J9mMr2nzTrKvMBVPr3RN8Ey5wkhd8g3iuruSPBnUgJQm\",\"block_time\":1739202400,\"amount\":\"4677377\",\"log_index\":2011,\"contract_address\":\"EPjFWdd5AufqSSqeM2qN1xzybapC8G4wEGGkZwyTDt1v\",\"asset_type\":\"token\",\"origin\":\"log\"}, {\"sender\":\"A8dZPP72tn7HAHe4h3ocjE8U3ePFGRsnbugeraqVAntY\",\"receiver\":\"J9mMr2nzTrKvMBVPr3RN8Ey5wkhd8g3iuruSPBnUgJQm\",\"block_time\":1739202400,\"amount\":\"292086\",\"log_index\":2006,\"contract_address\":\"6p6xgHyF7AeE6TZkSmFsko444wqoP15icUSqi2jfGiPN\",\"asset_type\":\"token\",\"origin\":\"log\"}, {\"sender\":\"AF11KSdsZYTqPBuNJwsF3CJZA8sbMJfdgcTY6ABWsJNJ\",\"receiver\":\"J9mMr2nzTrKvMBVPr3RN8Ey5wkhd8g3iuruSPBnUgJQm\",\"block_time\":1739202400,\"amount\":\"3194672\",\"log_index\":2002,\"contract_address\":\"FUAfBo2jgks6gB4Z4LfZkqSZgzNucisEHqnNebaRxM1P\",\"asset_type\":\"token\",\"origin\":\"log\"}, {\"sender\":\"J9mMr2nzTrKvMBVPr3RN8Ey5wkhd8g3iuruSPBnUgJQm\",\"receiver\":\"3AbG3ZA19fJKjTSTMTCz7j2bodPagXog4PwTBi8H7UA4\",\"block_time\":1739202400,\"amount\":\"292086\",\"log_index\":2010,\"contract_address\":\"6p6xgHyF7AeE6TZkSmFsko444wqoP15icUSqi2jfGiPN\",\"asset_type\":\"token\",\"origin\":\"log\"}, {\"sender\":\"J9mMr2nzTrKvMBVPr3RN8Ey5wkhd8g3iuruSPBnUgJQm\",\"receiver\":\"3ZuFZNPktr4VA6BLrNmT9CWYE3k7mu8tjpLcUhpBrHSg\",\"block_time\":1739202400,\"amount\":\"4674607\",\"log_index\":2001,\"contract_address\":\"EPjFWdd5AufqSSqeM2qN1xzybapC8G4wEGGkZwyTDt1v\",\"asset_type\":\"token\",\"origin\":\"log\"}, {\"sender\":\"J9mMr2nzTrKvMBVPr3RN8Ey5wkhd8g3iuruSPBnUgJQm\",\"receiver\":\"45ruCyfdRkWpRNGEqWzjCiXRHkZs8WXCLQ67Pnpye7Hp\",\"block_time\":1739202400,\"amount\":\"3194672\",\"log_index\":2005,\"contract_address\":\"So11111111111111111111111111111111111111112\",\"asset_type\":\"token\",\"origin\":\"log\"}, {\"sender\":\"J9mMr2nzTrKvMBVPr3RN8Ey5wkhd8g3iuruSPBnUgJQm\",\"receiver\":\"AtfwFfmzS5BQr5fe2cfSSm44XG1Ruqh9BvtXoXThGbd6\",\"block_time\":1739202400,\"amount\":\"87\",\"log_index\":3000,\"contract_address\":\"EPjFWdd5AufqSSqeM2qN1xzybapC8G4wEGGkZwyTDt1v\",\"asset_type\":\"token\",\"origin\":\"log\"}]";
        String price =  "255";
        String evaluate = solanaParse.evaluate(originSender, "3", "swap", hash, JSONArray.parseArray(swapList, String.class),JSONArray.parseArray(transferList, String.class), price);
        System.out.println(evaluate);
    }


}