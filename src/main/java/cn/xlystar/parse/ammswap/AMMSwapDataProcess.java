package cn.xlystar.parse.ammswap;

import cn.xlystar.entity.PumpFunTokenPool;
import cn.xlystar.entity.TransferEvent;
import cn.xlystar.entity.UniswapEvent;
import cn.xlystar.helpers.ChainConfig;
import cn.xlystar.parse.solSwap.SolanaTransactionParser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;

import java.io.IOException;
import java.util.*;

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

    public static List<Map<String, String>> decodeSwap(ChainConfig conf, String originSender, String hash, List<UniswapEvent> swapEvents, List<TransferEvent> transferEvents, String price, List<TransferEvent> transferOwnerEvents,
                                                       List<AMMSwapDataProcessFull.TokenBalance> postTokenBalance, List<AMMSwapDataProcessFull.TokenBalance> preTokenBalance) {
        return AMMSwapDataProcessFull.parseSolanaSwap(conf, originSender, hash, swapEvents, transferEvents, price, transferOwnerEvents, postTokenBalance, preTokenBalance);
    }


    public static List<Map<String, String>> parseSolTx(ChainConfig conf, String chain,
                                                       String slot, String blockTime,
                                                       List<String> accountKeys, List<String> logMessages,
                                                       List<String> writableAddresses, List<String> readonlyAddresses, List<Map<String, Object>> postTokenBalances, List<Map<String, Object>> preTokenBalances, List<String> postBalances, List<String> preBalances, List<Map<String, Object>> innerInstructions, List<Map<String, Object>> outerInstructions, String hash, String price) throws Exception {

        return processAmm(conf, chain,
                slot, blockTime,
                accountKeys, logMessages,
                writableAddresses, readonlyAddresses,
                postTokenBalances, preTokenBalances,
                postBalances, preBalances,
                innerInstructions, outerInstructions,
                hash, price);
    }

    public static List<Map<String, String>> parsePdaBalance(String slot, String blockTime,
                                                            List<String> accountKeys, List<String> logMessages,
                                                            List<String> writableAddresses, List<String> readonlyAddresses, List<Map<String, Object>> postTokenBalances, List<Map<String, Object>> preTokenBalances, List<String> postBalances, List<String> preBalances, List<Map<String, Object>> innerInstructions, List<Map<String, Object>> outerInstructions) throws Exception {
        List<Map<String, String>> accountBalance = new ArrayList<>(9);
        try {
            List<AMMSwapDataProcessFull.TokenBalance> postTokenBalance = new ArrayList<>(8);
            List<AMMSwapDataProcessFull.TokenBalance> preTokenBalance = new ArrayList<>(8);
            List<TransferEvent> allTransferEvents = new ArrayList<>(8);
            List<TransferEvent> allTxTransferOwnerEvents = new ArrayList<>(4);
            List<TransferEvent> allSolTransferEvents = new ArrayList<>(8);
            List<TransferEvent> allBurnTransferEvents = new ArrayList<>(4);
            List<UniswapEvent> allSwapEvents = new ArrayList<>(4);
            List<UniswapEvent> allAggregatorSwapEvents = new ArrayList<>(4);
            Map<String, Map<String, Object>> allCreatePDA = new HashMap<>(8);
            Map<String, Map<String, Object>> allClosePDA = new HashMap<>(4);
            List<PumpFunTokenPool> allPool = new ArrayList<>(4);
            List<PumpFunTokenPool> allPoolLiquidity = new ArrayList<>(4);
            Map<String, Map<String, Object>> poolVaultAddress = new HashMap<>();
            // 处理单笔交易
            SolanaTransactionParser.processTransactions(
                    blockTime,
                    accountKeys, logMessages,
                    writableAddresses, readonlyAddresses,
                    postTokenBalances, preTokenBalances,
                    postBalances, preBalances,
                    innerInstructions, outerInstructions,

                    allTransferEvents,
                    allSwapEvents,
                    allAggregatorSwapEvents,
                    allCreatePDA,
                    allClosePDA,
                    allPool,
                    allBurnTransferEvents,
                    allSolTransferEvents,
                    allPoolLiquidity,
                    allTxTransferOwnerEvents,
                    postTokenBalance, preTokenBalance, poolVaultAddress);

            allCreatePDA.forEach((key, pda) -> {
                if (pda == null
                        || pda.get("mint") == null || pda.get("account") == null
                        || pda.get("sol") == null || pda.get("sol_balance") == null
                        || pda.get("owner") == null
                        || (!poolVaultAddress.containsKey(key) && pda.get("account").equals(pda.get("owner")))
                ) return;

                Map<String, String> map = new HashMap<>();
                map.put("owner", pda.get("owner").toString());
                map.put("account", key);
                map.put("mint", pda.get("mint").toString());
                map.put("solAmount", pda.get("sol").toString());
                map.put("tokenAmount", pda.get("amount").toString());
                map.put("solBalance", pda.get("sol_balance").toString());
                map.put("lastTxTime", blockTime);
                map.put("lastBlockId", slot);
                accountBalance.add(map);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return accountBalance;
    }

    public static List<Map<String, String>> processAmm(ChainConfig conf, String chain,
                                                       String slot, String blockTime,
                                                       List<String> accountKeys, List<String> logMessages,
                                                       List<String> writableAddresses, List<String> readonlyAddresses,
                                                       List<Map<String, Object>> postTokenBalances, List<Map<String, Object>> preTokenBalances,
                                                       List<String> postBalances, List<String> preBalances,
                                                       List<Map<String, Object>> innerInstructions, List<Map<String, Object>> outerInstructions,
                                                       String hash, String price) throws Exception {
        try {
            List<AMMSwapDataProcessFull.TokenBalance> postTokenBalance = new ArrayList<>(8);
            List<AMMSwapDataProcessFull.TokenBalance> preTokenBalance = new ArrayList<>(8);
            List<TransferEvent> allTransferEvents = new ArrayList<>(8);
            List<TransferEvent> allTxTransferOwnerEvents = new ArrayList<>(4);
            List<TransferEvent> allSolTransferEvents = new ArrayList<>(8);
            List<TransferEvent> allBurnTransferEvents = new ArrayList<>(4);
            List<UniswapEvent> allSwapEvents = new ArrayList<>(4);
            List<UniswapEvent> allAggregatorSwapEvents = new ArrayList<>(4);
            Map<String, Map<String, Object>> allCreatePDA = new HashMap<>(8);
            Map<String, Map<String, Object>> allClosePDA = new HashMap<>(4);
            List<PumpFunTokenPool> allPool = new ArrayList<>(4);
            List<PumpFunTokenPool> allPoolLiquidity = new ArrayList<>(4);
            Map<String, Map<String, Object>> poolVaultAddress = new HashMap<>();

            // 创建只包含目标交易的新数组
            String originSender = accountKeys.get(0);

            // 处理单笔交易
            SolanaTransactionParser.processTransactions(
                    blockTime,
                    accountKeys, logMessages,
                    writableAddresses, readonlyAddresses,
                    postTokenBalances, preTokenBalances,
                    postBalances, preBalances,
                    innerInstructions, outerInstructions,

                    allTransferEvents,
                    allSwapEvents,
                    allAggregatorSwapEvents,
                    allCreatePDA,
                    allClosePDA,
                    allPool,
                    allBurnTransferEvents,
                    allSolTransferEvents,
                    allPoolLiquidity,
                    allTxTransferOwnerEvents,
                    postTokenBalance, preTokenBalance,poolVaultAddress);

            // 处理 transfer 维度信息
            if (CollectionUtils.isEmpty(allTxTransferOwnerEvents)
                    && CollectionUtils.isEmpty(allTransferEvents)
                    && CollectionUtils.isEmpty(allBurnTransferEvents)
            ) return null;

            Map<String, Map<String, Object>> transferSenderPda = new HashMap<>();
            allTransferEvents.forEach(t -> {
                if (t.getContractAddress() == null) return;
                Map<String, Object> pda = new HashMap<>();
                pda.put("mint", t.getContractAddress());
                pda.put("account", t.getSenderOrigin());
                pda.put("owner", t.getSender());
                transferSenderPda.put(t.getSenderOrigin(), pda);
                if (allCreatePDA.containsKey(t.getSenderOrigin())) {
                    Map<String, Object> pdaNow = allCreatePDA.get(t.getSenderOrigin());
                    pdaNow.put("mint", t.getContractAddress());
                    pdaNow.put("owner", t.getSender());
                } else {
                    allCreatePDA.put(t.getSenderOrigin(), pda);
                }
            });
            processPDA(allTransferEvents, allCreatePDA, transferSenderPda);
            allTransferEvents.forEach(t -> {
                if (t.getReceiver() == null) return;
                Map<String, Object> pda = new HashMap<>();
                pda.put("mint", t.getContractAddress());
                pda.put("account", t.getReceiverOrigin());
                pda.put("owner", t.getReceiver());
                transferSenderPda.put(t.getReceiverOrigin(), pda);
                if (allCreatePDA.containsKey(t.getReceiverOrigin())) {
                    Map<String, Object> pdaNow = allCreatePDA.get(t.getReceiverOrigin());
                    pdaNow.put("mint", t.getContractAddress());
                    pdaNow.put("owner", t.getReceiver());
                } else {
                    allCreatePDA.put(t.getReceiverOrigin(), pda);
                }
            });
            processPDA(allTransferEvents, allCreatePDA, transferSenderPda);

//            Set<String> allReceivers = allTransferEvents.stream().map(TransferEvent::getReceiverOrigin).collect(Collectors.toSet());
//            allReceivers.addAll(allTransferEvents.stream().peek(t -> {
//                if (t.getContractAddress() != null && !allCreatePDA.containsKey(t.getSenderOrigin())) {
//                    Map<String, Object> pda = new HashMap<>();
//                    pda.put("mint", t.getContractAddress());
//                    pda.put("account", t.getSenderOrigin());
//                    pda.put("owner", t.getSender());
//                    allCreatePDA.put(t.getSenderOrigin(), pda);
//                }
//            }).map(TransferEvent::getSenderOrigin).collect(Collectors.toSet()));
//            allReceivers.addAll(allBurnTransferEvents.stream().peek(t -> {
//                if (t.getContractAddress() != null && !allCreatePDA.containsKey(t.getSenderOrigin())) {
//                    Map<String, Object> pda = new HashMap<>();
//                    pda.put("mint", t.getContractAddress());
//                    pda.put("account", t.getSenderOrigin());
//                    pda.put("owner", t.getSender());
//                    allCreatePDA.put(t.getSenderOrigin(), pda);
//                }
//            }).map(TransferEvent::getSenderOrigin).collect(Collectors.toSet()));

            // 3.2. 从数据库获取已存在的 aToken account
//            List<Map<String, Object>> receiversInfoFromDb = allReceivers.stream()
//                    .map(t -> {
//                        if (allCreatePDA.containsKey(t)) {
//                            return allCreatePDA.get(t);
//                        }
//                        return null;
//                    })
//                    .filter(Objects::nonNull)
//                    .collect(Collectors.toList());

            //3.3. 过滤出需要查询的地址（token accounts），且获取账户信息
//            Set<String> existingReceiversDb = receiversInfoFromDb.stream().map(entry -> entry.get("account").toString()).collect(Collectors.toSet());
//            allClosePDA.forEach((key, value) -> {
//                if (!existingReceiversDb.contains(key)) {
//                    existingReceiversDb.add(key);
//                    receiversInfoFromDb.add(value);
//                }
//            });
//            List<String> receiversToQuery = allReceivers.stream()
//                    .filter(r -> !existingReceiversDb.contains(r))
//                    .collect(Collectors.toList());
//            List<Map<String, Object>> pdaAccountInfo = receiversToQuery.size() == 0 ? new ArrayList<>() : ReactiveWrapper.wrapToFlux(
//                            receiversToQuery,
//                            true,
//                            SolanaWeb3::getAccountInfo
//                    ).filter(Objects::nonNull).parallel()
//                    .runOn(Schedulers.parallel())
//                    .sequential().collectList().block();
//            if (!CollectionUtils.isEmpty(pdaAccountInfo))
//                receiversInfoFromDb.addAll(pdaAccountInfo);

            // 3.4. 移除请求失败的 transfer
//            Map<String, Map<String, Object>> validReceivers = receiversInfoFromDb.stream()
//                    .collect(Collectors.toMap(
//                            t -> t.get("account").toString(),  // key是account
//                            t -> t,                            // value是完整的Map
//                            (existing, replacement) -> replacement // 如果有重复key，保留第一个
//                    ));
//            allSolTransferEvents.forEach(t -> {
//                validReceivers.put(t.getSenderOrigin(),
//                        new HashMap<String, Object>() {{
//                            put("account", t.getSenderOrigin());
//                            put("owner", t.getSender());
//                            put("mint", t.getContractAddress());
//                        }}
//                );
//            });

            if (!CollectionUtils.isEmpty(allBurnTransferEvents)) allTransferEvents.addAll(allBurnTransferEvents);
            if (CollectionUtils.isEmpty(allTransferEvents))
                if (CollectionUtils.isEmpty(allTxTransferOwnerEvents)) return null;
            SolanaTransactionParser.processTransferEvents(hash, Integer.valueOf(blockTime), allTransferEvents, allCreatePDA);
            if (!CollectionUtils.isEmpty(allSwapEvents) || !CollectionUtils.isEmpty(allAggregatorSwapEvents)) {
                SolanaTransactionParser.processSwapEvents(allSwapEvents, allTransferEvents, allCreatePDA, allPoolLiquidity, allSolTransferEvents, logMessages, allAggregatorSwapEvents);
            }
            // 串联 swap
            return new ArrayList<>(AMMSwapDataProcess.decodeSwap(conf, originSender, hash, allSwapEvents, allTransferEvents, price, allTxTransferOwnerEvents, postTokenBalance, preTokenBalance));
        } catch (Exception e) {
            e.printStackTrace();
            log.error("[processAmm] Error processing transaction {}: {}", hash, e.getMessage(), e);
        }
        return null;
    }

    public static void processPDA(List<TransferEvent> allTransferEvents, Map<String, Map<String, Object>> allCreatePDA, Map<String, Map<String, Object>> transferSenderPda) {
        allTransferEvents.forEach(t -> {
            if (allCreatePDA.containsKey(t.getSenderOrigin())) {
                Map<String, Object> sendrMap = allCreatePDA.get(t.getSenderOrigin());

                if (t.getSender() == null) {
                    if (!sendrMap.get("owner").equals(sendrMap.get("account"))) {
                        t.setSender(sendrMap.get("owner").toString());
                        t.setContractAddress(sendrMap.get("mint").toString());
                    } else if (transferSenderPda.containsKey(t.getSenderOrigin())) {
                        t.setSender(transferSenderPda.get(t.getSenderOrigin()).get("owner").toString());
                        t.setContractAddress(transferSenderPda.get(t.getSenderOrigin()).get("mint").toString());
                    }
                }
            }

            if (allCreatePDA.containsKey(t.getReceiverOrigin())) {
                if (t.getReceiver() == null) {
                    Map<String, Object> receiverMap = allCreatePDA.get(t.getReceiverOrigin());
                    if (!receiverMap.get("owner").equals(receiverMap.get("account"))) {
                        t.setReceiver(receiverMap.get("owner").toString());
                        t.setContractAddress(receiverMap.get("mint").toString());
                    } else if (transferSenderPda.containsKey(t.getReceiverOrigin())) {
                        t.setReceiver(transferSenderPda.get(t.getReceiverOrigin()).get("owner").toString());
                        t.setContractAddress(transferSenderPda.get(t.getReceiverOrigin()).get("mint").toString());
                    }
                }
            }
        });

    }
}

