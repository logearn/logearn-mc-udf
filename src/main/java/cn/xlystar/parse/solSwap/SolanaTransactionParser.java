package cn.xlystar.parse.solSwap;

import cn.xlystar.entity.PumpFunTokenPool;
import cn.xlystar.entity.TransferEvent;
import cn.xlystar.entity.UniswapEvent;
import cn.xlystar.parse.ammswap.AMMSwapDataProcessFull;
import cn.xlystar.parse.solSwap.boop.BoopInstructionParser;
import cn.xlystar.parse.solSwap.meteora.almm.MeteoraAlmmInstructionParser;
import cn.xlystar.parse.solSwap.meteora.dbc.MeteoraDbcInstructionParser;
import cn.xlystar.parse.solSwap.meteora.dlmm.MeteoraDlmmInstructionParser;
import cn.xlystar.parse.solSwap.meteora.dlmm_v2.MeteoraDlmmV2InstructionParser;
import cn.xlystar.parse.solSwap.moonshot.MoonshotInstructionParser;
import cn.xlystar.parse.solSwap.pump.PumpDotFunInstructionParser;
import cn.xlystar.parse.solSwap.pump_swap.PumpSwapInstructionParser;
import cn.xlystar.parse.solSwap.raydium.amm_v4.RaydiumAmmInstructionParser;
import cn.xlystar.parse.solSwap.raydium.clmm.RaydiumClmmInstructionParser;
import cn.xlystar.parse.solSwap.raydium.cpmm.RaydiumCpmmInstructionParser;
import cn.xlystar.parse.solSwap.raydium.launch.RaydiumLaunchInstructionParser;
import cn.xlystar.parse.solSwap.whirlpool.WhirlpoolInstructionParser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class SolanaTransactionParser {

    public static void processTransactions(String blockTime,
                                           List<String> accountKeys, List<String> logMessages,
                                           List<String> writableAddresses, List<String> readonlyAddresses,
                                           List<Map<String, Object>> postTokenBalances, List<Map<String, Object>> preTokenBalances,
                                           List<String> postBalances, List<String> preBalances,
                                           List<Map<String, Object>> innerInstructions, List<Map<String, Object>> outerInstructions,

                                           List<TransferEvent> allTransferEvents,
                                           List<UniswapEvent> allSwapEvents,
                                           List<UniswapEvent> allAggregatorSwapEvents,

                                           Map<String, Map<String, Object>> allCreatePDA,
                                           Map<String, Map<String, Object>> allClosePDA,
                                           List<PumpFunTokenPool> allPool,
                                           List<TransferEvent> burnTransferEvents,
                                           List<TransferEvent> solTxTransferEvents,
                                           List<PumpFunTokenPool> pumpPoolLiquidityEvents,
                                           List<TransferEvent> txTransferOwnerEvents,
                                           List<AMMSwapDataProcessFull.TokenBalance> postTokenBalanceLists,
                                           List<AMMSwapDataProcessFull.TokenBalance> preTokenBalanceLists
    ) {

        try {
            List<String> mergedKeys = new ArrayList<>(accountKeys);
            mergedKeys.addAll(writableAddresses);
            mergedKeys.addAll(readonlyAddresses);

            if (CollectionUtils.isEmpty(outerInstructions)) return;
            List<Map<String, Object>> instructions = processInstructions(outerInstructions, innerInstructions, mergedKeys);

            // 处理事件
            List<Object> allEvents = processInstructionEvents(instructions);

            postTokenBalanceLists.addAll(processTokenBalance(postTokenBalances, mergedKeys));
            preTokenBalanceLists.addAll(processTokenBalance(preTokenBalances, mergedKeys));
            if (((List<TransferEvent>) allEvents.get(0)).size() > 0)
                allTransferEvents.addAll((List<TransferEvent>) allEvents.get(0));
            if (((List<UniswapEvent>) allEvents.get(1)).size() > 0)
                allSwapEvents.addAll((List<UniswapEvent>) allEvents.get(1));
            if (((Map<String, Map<String, Object>>) allEvents.get(2)).size() > 0)
                allCreatePDA.putAll((Map<String, Map<String, Object>>) allEvents.get(2));
            if (((Map<String, Map<String, Object>>) allEvents.get(3)).size() > 0)
                allClosePDA.putAll((Map<String, Map<String, Object>>) allEvents.get(3));
            if (((List<PumpFunTokenPool>) allEvents.get(4)).size() > 0)
                allPool.addAll((List<PumpFunTokenPool>) allEvents.get(4));
            if (((List<TransferEvent>) allEvents.get(5)).size() > 0)
                burnTransferEvents.addAll((List<TransferEvent>) allEvents.get(5));
            if (((List<TransferEvent>) allEvents.get(6)).size() > 0)
                solTxTransferEvents.addAll((List<TransferEvent>) allEvents.get(6));
            if (((List<PumpFunTokenPool>) allEvents.get(7)).size() > 0)
                pumpPoolLiquidityEvents.addAll((List<PumpFunTokenPool>) allEvents.get(7));
            if (((List<UniswapEvent>) allEvents.get(8)).size() > 0)
                allAggregatorSwapEvents.addAll((List<UniswapEvent>) allEvents.get(8));
            if (((List<TransferEvent>) allEvents.get(9)).size() > 0)
                txTransferOwnerEvents.addAll((List<TransferEvent>) allEvents.get(9));

            Map<String, Map<String, Object>> pdaInfo = processATokenAccount(blockTime, preTokenBalances, preBalances, postTokenBalances, postBalances, mergedKeys);
            if (!MapUtils.isEmpty(pdaInfo)) allCreatePDA.putAll(pdaInfo);
        } catch (Exception e) {
            log.error("Error processing transaction {}", e.getMessage(), e);
        }

    }

    private static List<AMMSwapDataProcessFull.TokenBalance> processTokenBalance(List<Map<String, Object>> tokenBalances, List<String> accountKeys) {
        if (CollectionUtils.isEmpty(tokenBalances)) return Collections.emptyList();
        return tokenBalances.stream().map(balance -> {
            return AMMSwapDataProcessFull.TokenBalance.builder()
                    .account(accountKeys.get((Integer) balance.get("accountIndex")))
                    .mint(balance.get("mint").toString())
                    .owner(balance.get("owner").toString())
                    .programId(balance.get("programId").toString())
                    .uiTokenAmount(
                            AMMSwapDataProcessFull.UiTokenAmount.builder()
                                    .amount(((Map<String, Object>) balance.get("uiTokenAmount")).get("amount").toString())
                                    .decimals(Integer.parseInt(((Map<String, Object>) balance.get("uiTokenAmount")).get("decimals").toString()))
                                    .build())
                    .build();
        }).collect(Collectors.toList());
    }

    /**
     * 处理Transfer事件
     */
    public static void processTransferEvents(String hash, Integer blockTime, List<TransferEvent> txTransferEvents,
                                             Map<String, Map<String, Object>> accountInfoMap) {
        if (CollectionUtils.isEmpty(txTransferEvents)) return;
        Iterator<TransferEvent> iterator = txTransferEvents.iterator();
        while (iterator.hasNext()) {
            TransferEvent event = iterator.next();


            event.setBlockTime(blockTime);
            event.setTxHash(hash);
            // sol transfer
            if (event.getEventType().equals("sol_transfer")) {
                continue;
            }
            // 检查接收账户是否有效 burn
            if (event.getEventType().equals("burn")) {
                if (event.getContractAddress() != null) continue;
                if (!accountInfoMap.containsKey(event.getSenderOrigin())) {
                    iterator.remove();
                    continue;
                }
                Map<String, Object> accountInfo = accountInfoMap.get(event.getSenderOrigin());
                event.setContractAddress(accountInfo.get("mint").toString());
                continue;
            }


            // 检查接收账户是否有效 正常transfer
            String receiver = event.getReceiverOrigin();
            if (!event.getReceiverOrigin().equals(event.getReceiver()) && (!accountInfoMap.containsKey(receiver) || !accountInfoMap.get(receiver).containsKey("mint"))) {
                String senderOrigin = event.getSenderOrigin();
                if (!accountInfoMap.containsKey(senderOrigin)) {
                    iterator.remove();
                } else {
                    Map<String, Object> accountInfo = accountInfoMap.get(senderOrigin);
                    event.setContractAddress(accountInfo.get("mint").toString());
                    event.setReceiver(accountInfo.get("owner").toString());
                }
                continue;
            }
            // 更新合约地址（mint）
            Map<String, Object> accountInfo = accountInfoMap.get(receiver);
            event.setContractAddress(accountInfo.get("mint").toString());

            // 设置owner信息
            if (accountInfo.containsKey("owner")) {
                event.setReceiver(accountInfo.get("owner").toString());
            }

        }
    }

    public static List<Map<String, Object>> processInstructions(List<Map<String, Object>> outerInstructions, List<Map<String, Object>> innerInstructions, List<String> accountKeys) {
        // 处理内部指令和外部指令
        List<Map<String, Object>> allInstructions = new ArrayList<>();
        for (int i = 0; i < outerInstructions.size(); i++) {
            Map<String, Object> outerInstru = outerInstructions.get(i);
            createInstructionMap(allInstructions, outerInstru, accountKeys, false, i + 1, null);
        }

        // 处理内部指令
        if (!CollectionUtils.isEmpty(innerInstructions)) {
            for (int i = 0; i < innerInstructions.size(); i++) {
                Map<String, Object> innerInstru = innerInstructions.get(i);
                List<Map<String, Object>> instructions = (List<Map<String, Object>>) innerInstru.get("instructions");
                Integer outIndex = (Integer) innerInstru.get("index");
                for (int j = 0; j < instructions.size(); j++) {
                    createInstructionMap(allInstructions, instructions.get(j), accountKeys, true, outIndex + 1, j + 1);
                }
            }
        }
        return allInstructions;
    }


    public static void createInstructionMap(List<Map<String, Object>> allInstructions, Map<String, Object> instru, List<String> accountKeys, boolean isInner, Integer outerIndex, Integer innerIndex) {
        Map<String, Object> instructionMap = new HashMap<>();
        String executeProgramId = accountKeys.get((Integer) instru.get("programIdIndex"));
        if (SolInstructionParserFactory.getParser(executeProgramId) == null) return;

        instructionMap.put("is_inner", isInner);
        instructionMap.put("outer_instruction_index", outerIndex);
        instructionMap.put("inner_instruction_index", innerIndex);
        instructionMap.put("execute_program_id", executeProgramId);
        instructionMap.put("data", instru.get("data"));

        List<Integer> accounts = (List<Integer>) instru.get("accounts");
        if (CollectionUtils.isEmpty(accounts)) instructionMap.put("accounts", new ArrayList<>());
        else {
            instructionMap.put("accounts", accounts.stream().map(index -> accountKeys.get(index)).collect(Collectors.toList()));
        }
        allInstructions.add(instructionMap);
    }

    public static boolean validateSwapEvent(UniswapEvent event,
                                            TransferEvent transfer1,
                                            TransferEvent transfer2,
                                            Map<String, Map<String, Object>> vaultLists) {
        if (transfer1 == null || transfer2 == null) {
            return false;
        }

        String vaultIn = event.getVaultIn();
        String vaultOut = event.getVaultOut();

        if (!vaultLists.containsKey(vaultIn) || !vaultLists.containsKey(vaultOut) || event.getSender() == null || event.getTo() == null) {
            return false;
        }

        return true;
    }

    /**
     * 处理有效的Swap事件
     */
    public static void processValidSwapEvent(UniswapEvent event,
                                             TransferEvent transfer1,
                                             TransferEvent transfer2,
                                             Map<String, Map<String, Object>> vaultLists) {
        if (isRaydiumAmmV4(event)) {
            String vaultIn = event.getVaultIn();
            String vaultOut = event.getVaultOut();
            boolean swapIn = (transfer1.getInnerIndex() < transfer2.getInnerIndex() && event.getVaultIn().equals(transfer1.getReceiverOrigin()))
                    || (transfer1.getInnerIndex() > transfer2.getInnerIndex() && event.getVaultIn().equals(transfer2.getReceiverOrigin()));
            if (!swapIn) {
                event.setVaultIn(vaultOut);
                event.setVaultOut(vaultIn);
            }
        } else if (isWhirlPool(event)) {
            String vaultIn = event.getVaultIn();
            String vaultOut = event.getVaultOut();
            String receiver = event.getTo();
            String sender = event.getSender();
            boolean swapIn = (transfer1.getInnerIndex() < transfer2.getInnerIndex() && event.getVaultIn().equals(transfer1.getReceiverOrigin()))
                    || (transfer1.getInnerIndex() > transfer2.getInnerIndex() && event.getVaultIn().equals(transfer2.getReceiverOrigin()));
            if (!swapIn) {
                event.setTo(sender);
                event.setSender(receiver);
                event.setVaultIn(vaultOut);
                event.setVaultOut(vaultIn);
            }
        } else if (isMeteoralDlmm(event) || isMeteoralDbc(event)) {
            String vaultIn = event.getVaultIn();
            String vaultOut = event.getVaultOut();
            String receiver = event.getTo();
            String sender = event.getSender();
            boolean swapIn = (transfer1.getInnerIndex() < transfer2.getInnerIndex() && event.getVaultIn().equals(transfer1.getReceiverOrigin()))
                    || (transfer1.getInnerIndex() > transfer2.getInnerIndex() && event.getVaultIn().equals(transfer2.getReceiverOrigin()));
            if (!swapIn) {
                event.setTo(sender);
                event.setSender(receiver);
                event.setVaultIn(vaultOut);
                event.setVaultOut(vaultIn);
            }
        } else if (isMeteoralDlmmV2(event)) {
            boolean swapIn = (transfer1.getInnerIndex() < transfer2.getInnerIndex() && event.getVaultOut().equals(transfer1.getReceiverOrigin()))
                    || (transfer1.getInnerIndex() > transfer2.getInnerIndex() && event.getVaultOut().equals(transfer2.getReceiverOrigin()));

            if (swapIn) {
                event.setAmountIn(transfer1.getAmount());
                event.setAmountOut(transfer2.getAmount());
            }
        }
        // 设置token信息
        event.setTokenIn(vaultLists.get(event.getVaultIn()).get("mint").toString());
        event.setTokenOut(vaultLists.get(event.getVaultOut()).get("mint").toString());

        // 根据SQL逻辑设置买卖方向和金额
        if (transfer1.getReceiverOrigin().equals(event.getVaultIn())) {
            // 第一笔transfer是买入
            event.setAmountIn(transfer1.getAmount());
            event.setAmountOut(transfer2.getAmount());
            event.setSender(transfer1.getSender());
            event.setTo(transfer2.getReceiver());
        } else {
            // 第二笔transfer是买入
            event.setAmountIn(transfer2.getAmount());
            event.setAmountOut(transfer1.getAmount());
            event.setSender(transfer2.getSender());
            event.setTo(transfer1.getReceiver());
        }
    }

    private static boolean isMeteoralDbc(UniswapEvent event) {
        return MeteoraDbcInstructionParser.PROGRAM_ID.equals(event.getProgramId());
    }

    private static boolean isMeteoralDlmmV2(UniswapEvent event) {
        return MeteoraDlmmV2InstructionParser.PROGRAM_ID.equals(event.getProgramId());
    }

    public static Pair<TransferEvent, TransferEvent> findMeteoraDlmmTransfers(
            List<TransferEvent> transfers,
            UniswapEvent swapEvent) {

        // 找到所有可能的transfer1
        List<TransferEvent> possibleTransfer1s = transfers.stream()
                .filter(tr -> {
                    // 基本条件匹配
                    boolean outerMatch = tr.getOuterIndex() == swapEvent.getOuterIndex();

                    // inner_instruction_index匹配
                    boolean innerMatch;
                    if (swapEvent.getInnerIndex() == 0) {  // is null
                        innerMatch = tr.getInnerIndex() == 1 ||
                                tr.getInnerIndex() == 2;
                    } else {
                        innerMatch = tr.getInnerIndex() == swapEvent.getInnerIndex() + 1 ||
                                tr.getInnerIndex() == swapEvent.getInnerIndex() + 2;
                    }

                    return outerMatch && innerMatch;
                })
                .collect(Collectors.toList());

        // 找到所有可能的transfer2
        List<TransferEvent> possibleTransfer2s = transfers.stream()
                .filter(tr -> {
                    boolean outerMatch = tr.getOuterIndex() == swapEvent.getOuterIndex();

                    boolean innerMatch;
                    if (swapEvent.getInnerIndex() == 0) {  // is null
                        innerMatch = tr.getInnerIndex() == 2 ||
                                tr.getInnerIndex() == 3;
                    } else {
                        innerMatch = tr.getInnerIndex() == swapEvent.getInnerIndex() + 2 ||
                                tr.getInnerIndex() == swapEvent.getInnerIndex() + 3;
                    }

                    return outerMatch && innerMatch;
                })
                .collect(Collectors.toList());

        // 遍历所有可能的组合，找到符合条件的pair
        for (TransferEvent tr1 : possibleTransfer1s) {
            for (TransferEvent tr2 : possibleTransfer2s) {
                // 检查token mint不相等
                if (tr1.getContractAddress().equals(tr2.getContractAddress())
                        || tr1.getSenderOrigin().equals(tr1.getReceiverOrigin())
                        || tr2.getSenderOrigin().equals(tr2.getReceiverOrigin())
                ) {
                    continue;
                }

                // 检查转账方向匹配
                boolean matchPattern1 =
                        tr2.getSenderOrigin().equals(swapEvent.getSender()) &&
                                tr1.getReceiverOrigin().equals(swapEvent.getTo());

                boolean matchPattern2 =
                        tr1.getSenderOrigin().equals(swapEvent.getSender()) &&
                                tr2.getReceiverOrigin().equals(swapEvent.getTo());

                if (matchPattern1 || matchPattern2) {
                    return Pair.of(tr1, tr2);
                }
            }
        }

        return null;
    }

    /**
     * 确定使用哪个匹配规则
     */
    private static SolDexTransferMatchRule determineMatchRule(UniswapEvent event) {
        String programId = event.getProgramId();
        String[] p = programId.split("_");
        switch (p[0]) {
            case RaydiumClmmInstructionParser.PROGRAM_ID:
                return SolDexTransferMatchRule.RAYDIUM_CLMM;
            case PumpSwapInstructionParser.PROGRAM_ID:
                return SolDexTransferMatchRule.PUPMFUN_AMM;
            case RaydiumCpmmInstructionParser.PROGRAM_ID:
                // CPMM的规则在处理时动态决定
                return SolDexTransferMatchRule.RAYDIUM_CPMM_V1;
            case RaydiumAmmInstructionParser.PROGRAM_ID:
                // CPMM的规则在处理时动态决定
                return SolDexTransferMatchRule.RAYDIUM_AMM_V4;
            case RaydiumLaunchInstructionParser.PROGRAM_ID:
                // CPMM的规则在处理时动态决定
                return SolDexTransferMatchRule.RAYDIUM_LAUNCH;
            case MeteoraDlmmInstructionParser.PROGRAM_ID:
                // CPMM的规则在处理时动态决定
                return SolDexTransferMatchRule.METEORA_DLMM;
            case MeteoraDlmmV2InstructionParser.PROGRAM_ID:
                // CPMM的规则在处理时动态决定
                return SolDexTransferMatchRule.METEORA_DLMM_V2;
            case MeteoraDbcInstructionParser.PROGRAM_ID:
                // CPMM的规则在处理时动态决定
                return SolDexTransferMatchRule.METEORA_DBC;
            case MoonshotInstructionParser.PROGRAM_ID:
                // CPMM的规则在处理时动态决定
                return SolDexTransferMatchRule.MOOTSHOT;
            case BoopInstructionParser.PROGRAM_ID:
                // CPMM的规则在处理时动态决定
                return SolDexTransferMatchRule.BOOPFUN;
            case MeteoraAlmmInstructionParser.PROGRAM_ID:
                // CPMM的规则在处理时动态决定
                return SolDexTransferMatchRule.METEORA_ALMM;
            case PumpDotFunInstructionParser.PROGRAM_ID:
                // CPMM的规则在处理时动态决定
                return SolDexTransferMatchRule.PUPMFUN;
            case WhirlpoolInstructionParser.PROGRAM_ID:
                return SolDexTransferMatchRule.ORCA_WHIRLPOOL;
            case "phoenix":
                return SolDexTransferMatchRule.PHOENIX;
            default:
                return null;
        }
    }


    /**
     * 查找CPMM的transfer pairs
     */
    public static Pair<TransferEvent, TransferEvent> findCPMMTransfers(
            List<TransferEvent> transfers,
            UniswapEvent swapEvent) {

        // 找到所有可能的transfer1
        List<TransferEvent> possibleTransfer1s = transfers.stream()
                .filter(tr -> {
                    // 基本条件匹配
                    boolean outerMatch = tr.getOuterIndex() == swapEvent.getOuterIndex();

                    // inner_instruction_index匹配
                    boolean innerMatch;
                    if (swapEvent.getInnerIndex() == 0) {  // is null
                        innerMatch = tr.getInnerIndex() == 1 ||
                                tr.getInnerIndex() == 2;
                    } else {
                        innerMatch = tr.getInnerIndex() == swapEvent.getInnerIndex() + 1 ||
                                tr.getInnerIndex() == swapEvent.getInnerIndex() + 2;
                    }

                    return outerMatch && innerMatch;
                })
                .collect(Collectors.toList());

        // 找到所有可能的transfer2
        List<TransferEvent> possibleTransfer2s = transfers.stream()
                .filter(tr -> {
                    boolean outerMatch = tr.getOuterIndex() == swapEvent.getOuterIndex();

                    boolean innerMatch;
                    if (swapEvent.getInnerIndex() == 0) {  // is null
                        innerMatch = tr.getInnerIndex() == 2 ||
                                tr.getInnerIndex() == 3;
                    } else {
                        innerMatch = tr.getInnerIndex() == swapEvent.getInnerIndex() + 2 ||
                                tr.getInnerIndex() == swapEvent.getInnerIndex() + 3;
                    }

                    return outerMatch && innerMatch;
                })
                .collect(Collectors.toList());

        // 遍历所有可能的组合，找到符合条件的pair
        for (TransferEvent tr1 : possibleTransfer1s) {
            for (TransferEvent tr2 : possibleTransfer2s) {
                // 检查token mint不相等
                if (tr1.getContractAddress().equals(tr2.getContractAddress())) {
                    continue;
                }

                // 检查转账方向匹配
                boolean matchPattern1 =
                        tr2.getSenderOrigin().equals(swapEvent.getSender()) &&
                                tr1.getReceiverOrigin().equals(swapEvent.getTo()) &&
                                tr1.getSenderOrigin().equals(swapEvent.getVaultOut()) &&
                                tr2.getReceiverOrigin().equals(swapEvent.getVaultIn());

                boolean matchPattern2 =
                        tr1.getSenderOrigin().equals(swapEvent.getSender()) &&
                                tr2.getReceiverOrigin().equals(swapEvent.getTo()) &&
                                tr2.getSenderOrigin().equals(swapEvent.getVaultOut()) &&
                                tr1.getReceiverOrigin().equals(swapEvent.getVaultIn());

                if (matchPattern1 || matchPattern2) {
                    return Pair.of(tr1, tr2);
                }
            }
        }

        return null;
    }


    /**
     * 判断是否是CPMM协议
     */
    public static boolean isCPMM(UniswapEvent event) {
        return RaydiumCpmmInstructionParser.PROGRAM_ID.equals(event.getProgramId());
    }

    /**
     * 判断是否是CPMM协议
     */
    public static boolean isPumpFun(UniswapEvent event) {
        return PumpDotFunInstructionParser.PROGRAM_ID.equals(event.getProgramId());
    }

    public static List<Object> processInstructionEvents(List<Map<String, Object>> instructions) {
        List<TransferEvent> txTransferEvents = new ArrayList<>();
        List<TransferEvent> txTransferOwnerEvents = new ArrayList<>();
        List<TransferEvent> burnTransferEvents = new ArrayList<>();
        List<TransferEvent> solTxTransferEvents = new ArrayList<>();
        List<PumpFunTokenPool> pumpPoolLiquidityEvents = new ArrayList<>();
        List<UniswapEvent> txSwapEvents = new ArrayList<>();
        List<UniswapEvent> txAggregatorSwapEvents = new ArrayList<>();
        List<PumpFunTokenPool> poolEvents = new ArrayList<>();
        Map<String, Map<String, Object>> createPDA = new HashMap<>();
        Map<String, Map<String, Object>> closePDA = new HashMap<>();

        instructions.forEach(t -> {
            String executeProgramId = (String) t.get("execute_program_id");
            int outerInstructionIndex = t.get("outer_instruction_index") == null ? 0 : Integer.parseInt(t.get("outer_instruction_index").toString());
            int innerInstructionIndex = t.get("inner_instruction_index") == null ? 0 : Integer.parseInt(t.get("inner_instruction_index").toString());
            int logIndex = outerInstructionIndex * 10000 + innerInstructionIndex;
            List<String> accounts = (List<String>) t.get("accounts");
            String data = (String) t.get("data");

            Map<String, Object> event = SolInstructionProcessor.processInstructionRt(executeProgramId, accounts.toArray(new String[0]), data);

            if (event == null || !event.containsKey("instruction_type")) return;

            switch (event.get("instruction_type").toString()) {
                case "transfer":
                    txTransferEvents.add(TransferEvent.builder()
                            .sender((String) event.get("sender_owner"))
                            .senderOrigin((String) event.get("sender"))
                            .receiverOrigin((String) event.get("receiver"))
                            .receiver((String) event.get("receiver_owner"))
                            .amount(new BigInteger(event.get("amount").toString()))
                            .contractAddress((String) event.get("mint"))
                            .outerIndex(outerInstructionIndex)
                            .innerIndex(innerInstructionIndex)
                            .logIndex(new BigInteger(String.valueOf(logIndex)))
                            .eventType("transfer")
                            .build());
                    break;
                case "transfer_owner":
                    txTransferOwnerEvents.add(TransferEvent.builder()
                            .sender((String) event.get("authority"))
                            .senderOrigin((String) event.get("account"))
                            .receiverOrigin((String) event.get("account"))
                            .receiver((String) event.get("newAuthority"))
//                            .amount(event.containsKey("amount") ? new BigInteger(event.get("amount").toString()) : BigInteger.ZERO)
//                            .contractAddress(event.containsKey("mint") ? event.get("mint").toString() : null)
                            .outerIndex(outerInstructionIndex)
                            .innerIndex(innerInstructionIndex)
                            .logIndex(new BigInteger(String.valueOf(logIndex)))
                            .eventType("transfer_owner")
                            .build());
                    break;
                case "sol_transfer":
                    solTxTransferEvents.add(TransferEvent.builder()
                            .sender((String) event.get("sender_owner"))
                            .senderOrigin((String) event.get("sender_owner"))
                            .receiver((String) event.get("receiver_owner"))
                            .receiverOrigin((String) event.get("receiver_owner"))
                            .amount(new BigInteger(event.get("amount").toString()))
                            .contractAddress((String) event.get("mint"))
                            .outerIndex(outerInstructionIndex)
                            .innerIndex(innerInstructionIndex)
                            .logIndex(new BigInteger(String.valueOf(logIndex)))
                            .eventType("sol_transfer")
                            .build());
                    break;
                case "burn":
                    burnTransferEvents.add(TransferEvent.builder()
                            .sender((String) event.get("sender_owner"))
                            .senderOrigin((String) event.get("sender"))
                            .receiver("0x0")
                            .receiverOrigin("0x0")
                            .amount(new BigInteger(event.get("amount").toString()))
                            .contractAddress((String) event.get("mint"))
                            .outerIndex(outerInstructionIndex)
                            .innerIndex(innerInstructionIndex)
                            .logIndex(new BigInteger(String.valueOf(logIndex)))
                            .eventType("burn")
                            .build());
                    break;
                case "dex_amm":
                    txSwapEvents.add(UniswapEvent.builder()
                            .contractAddress((String) event.get("pool_id"))
                            .sender((String) event.get("input_token_account"))
                            .to((String) event.get("output_token_account"))
                            .tokenIn((String) event.get("input_vault_mint"))
                            .tokenOut((String) event.get("output_vault_mint"))
                            .vaultIn((String) event.get("input_vault"))
                            .vaultOut((String) event.get("output_vault"))
                            .outerIndex(outerInstructionIndex)
                            .innerIndex(innerInstructionIndex)
                            .logIndex(new BigInteger(String.valueOf(logIndex)))
                            .programId(executeProgramId)
                            .build());
                    break;
                case "aggregator_amm":
                    txAggregatorSwapEvents.add(UniswapEvent.builder()
                            .sender((String) event.get("input_token_account"))
                            .to((String) event.get("output_token_account"))
                            .tokenIn((String) event.get("input_vault_mint"))
                            .tokenOut((String) event.get("output_vault_mint"))
                            .outerIndex(outerInstructionIndex)
                            .innerIndex(innerInstructionIndex)
                            .logIndex(new BigInteger(String.valueOf(logIndex)))
                            .programId(executeProgramId)
                            .build());
                    break;
                case "pump_amm_instruction":
                    txSwapEvents.add(UniswapEvent.builder()
                            .contractAddress((String) event.get("pool_id"))
                            .sender((String) event.get("input_token_account"))
                            .to((String) event.get("output_token_account"))
                            .oriSender((String) event.get("user"))
                            .vaultIn((String) event.get("input_vault"))
                            .vaultOut((String) event.get("output_vault"))
                            .tokenIn((String) event.get("input_vault_mint"))
                            .tokenOut((String) event.get("output_vault_mint"))
                            .amountIn((BigInteger) event.get("input_amount"))
                            .amountOut((BigInteger) event.get("output_amount"))
                            .outerIndex(outerInstructionIndex)
                            .innerIndex(innerInstructionIndex)
                            .logIndex(new BigInteger(String.valueOf(logIndex)))
                            .programId(executeProgramId)
                            .build());
                    Map<String, Object> pda = new HashMap<>();
                    String mint = !event.get("input_vault_mint").equals("So11111111111111111111111111111111111111112") ? event.get("input_vault_mint").toString() : event.get("output_vault_mint").toString();
                    pda.put("mint", mint);
                    pda.put("account", event.get("input_token_account"));
                    pda.put("owner", event.get("user"));
                    createPDA.put(event.get("input_token_account").toString(), pda);

                    pda = new HashMap<>();
                    pda.put("mint", mint);
                    pda.put("account", event.get("input_vault"));
                    pda.put("owner", event.get("pool_id"));
                    createPDA.put(event.get("input_vault").toString(), pda);
                    break;
                case "pump_amm_liquidity":
                    pumpPoolLiquidityEvents.add(
                            PumpFunTokenPool.builder()
                                    .tokenAddress((String) event.get("mint"))
                                    .amount0((String) event.get("real_token_reserves"))
                                    .token0((String) event.get("mint"))
                                    .amount1((String) event.get("real_sol_reserves"))
                                    .amountChange0((event.get("isBuy").toString().equals("1") ? "" : "-") + event.get("tokenAmount").toString())
                                    .amountChange1((event.get("isBuy").toString().equals("1") ? "-" : "") + event.get("solAmount").toString())
                                    .token1("So11111111111111111111111111111111111111112")
                                    .version(executeProgramId)
                                    .price(new BigDecimal(event.get("virtual_sol_reserves").toString()).divide(new BigDecimal(event.get("virtual_token_reserves").toString()), 9, RoundingMode.HALF_UP).toPlainString())
                                    .build());
                    break;
                case "pump_create_pool":
                    poolEvents.add(PumpFunTokenPool.builder()
                            .poolAddress((String) event.get("pool_id"))
                            .tokenAddress((String) event.get("vault_mint_0"))
                            .vault0((String) event.get("vault_0"))
                            .token0((String) event.get("vault_mint_0"))
                            .vault1((String) event.get("vault_1"))
                            .token1((String) event.get("vault_mint_1"))
                            .version(executeProgramId)
                            .initOwner((String) event.get("creator"))
                            .build());
                    break;
                case "pda_create_account":
                    createPDA.put(event.get("account").toString(), event);
                    break;
                case "pda_close_account":
                    closePDA.put(event.get("account").toString(), event);
                    break;
            }
        });

        List<Object> list = new ArrayList<>();
        list.add(txTransferEvents);
        list.add(txSwapEvents);
        list.add(createPDA);
        list.add(closePDA);
        list.add(poolEvents);
        list.add(burnTransferEvents);
        list.add(solTxTransferEvents);
        list.add(pumpPoolLiquidityEvents);
        list.add(txAggregatorSwapEvents);
        list.add(txTransferOwnerEvents);
        return list;
    }


    /**
     * 处理Swap事件
     */
    public static void processSwapEvents(List<UniswapEvent> txSwapEvents, List<TransferEvent> txTransferEvents,
                                         Map<String, Map<String, Object>> vaultInfoMap, List<PumpFunTokenPool> allPoolLiquidity, List<TransferEvent> solTransferEvents,
                                         List<String> messageList, List<UniswapEvent> aggregatorSwapEvents) {
        if (CollectionUtils.isEmpty(txTransferEvents)) return;
        Iterator<UniswapEvent> aggSwapIterator = aggregatorSwapEvents.iterator();
        List<UniswapEvent> txValidAggSwapEvents = new ArrayList<>();
        List<TransferEvent> aggSwapTransfers = new ArrayList<>();
        while (aggSwapIterator.hasNext()) {
            UniswapEvent aggSwap = aggSwapIterator.next();
            findAggregatorSwapTransfers(txValidAggSwapEvents, txTransferEvents, aggSwap, aggSwapTransfers);
        }

        Iterator<UniswapEvent> swapIterator = txSwapEvents.iterator();
        while (swapIterator.hasNext()) {
            UniswapEvent event = swapIterator.next();

            // 确定使用哪个匹配规则
            SolDexTransferMatchRule matchRule = determineMatchRule(event);
            if (matchRule == null) {
                swapIterator.remove();
                continue;
            }

            // 尝试找到匹配的transfers
            TransferEvent transfer1 = null;
            TransferEvent transfer2 = null;

            // 对于CPMM，尝试两种规则
            if (isCPMM(event)) {
                // 查找符合条件的transfer pair
                Pair<TransferEvent, TransferEvent> transferPair =
                        findCPMMTransfers(txTransferEvents, event);
                if (transferPair != null) {
                    transfer1 = transferPair.getKey();
                    transfer2 = transferPair.getValue();
                }
            } else if (isMeteoralDlmm(event)) {
                // 查找符合条件的transfer pair
                Pair<TransferEvent, TransferEvent> transferPair =
                        findMeteoraDlmmTransfers(txTransferEvents, event);
                if (transferPair != null) {
                    transfer1 = transferPair.getKey();
                    transfer2 = transferPair.getValue();
                }
            } else if (isMeteoralAlmm(event) || isMeteoralDlmmV2(event)) {
                // 查找符合条件的transfer pair
                processMeteoraAlmmEvent(event, txTransferEvents);
                continue;
            } else if (isPumpFun(event)) {
                // 其他协议使用确定的规则
                processPumpSwapEvent(event, txTransferEvents, allPoolLiquidity);
                if (event.getAmountOut() == null || event.getAmountIn() == null) swapIterator.remove();
                continue;
            } else if (isBoopFun(event)) {
                processBoopfunEvent(event, txTransferEvents, solTransferEvents);
                continue;
            } else if (isMoonshot(event)) {
                processMoonshotEvent(event, txTransferEvents, messageList);
                continue;
            } else {
                // 其他协议使用确定的规则
                transfer1 = findTransfer(txTransferEvents, event, matchRule.getTransfer1Offset());
                transfer2 = findTransfer(txTransferEvents, event, matchRule.getTransfer2Offset());
            }

            // 验证vault和transfers
            if (!validateSwapEvent(event, transfer1, transfer2, vaultInfoMap)) {
                swapIterator.remove();
                continue;
            }

            // 设置token信息和方向
            processValidSwapEvent(event, transfer1, transfer2, vaultInfoMap);
            txTransferEvents.remove(transfer1);
            txTransferEvents.remove(transfer2);
        }

        txTransferEvents.removeAll(aggSwapTransfers);
        txSwapEvents.addAll(txValidAggSwapEvents);
    }

    private static void findAggregatorSwapTransfers(List<UniswapEvent> txValidAggSwapEvents, List<TransferEvent> transfers, UniswapEvent swapEvent, List<TransferEvent> txTransferEvents) {
        List<TransferEvent> txFromList = new ArrayList<>();
        List<TransferEvent> txToList = new ArrayList<>();
        for (int i = 0; i < transfers.size(); i++) {
            TransferEvent tr = transfers.get(i);
            // 基本条件匹配
            boolean outerMatch = tr.getOuterIndex() == swapEvent.getOuterIndex();
            if (!outerMatch) continue;

            if ((swapEvent.getInnerIndex() == 0 && tr.getInnerIndex() >= 1)
                    || (swapEvent.getInnerIndex() != 0 && tr.getInnerIndex() > swapEvent.getInnerIndex())

            ) {
                if (tr.getSenderOrigin().equals(swapEvent.getSender())) {
                    txFromList.add(tr);
                } else if (tr.getReceiverOrigin().equals(swapEvent.getTo())) {
                    txToList.add(tr);
                }
            }
        }

        if (CollectionUtils.isEmpty(txFromList) || CollectionUtils.isEmpty(txToList)) return;

        txValidAggSwapEvents.add(swapEvent);
        txTransferEvents.addAll(txFromList);
        txTransferEvents.addAll(txToList);
        swapEvent.setAggregator(true);

        swapEvent.setAmountIn(txFromList.stream().map(TransferEvent::getAmount).reduce(BigInteger.ZERO, BigInteger::add));
        swapEvent.setAmountOut(txToList.stream().map(TransferEvent::getAmount).reduce(BigInteger.ZERO, BigInteger::add));

        swapEvent.setSender(txFromList.get(0).getSender());
        swapEvent.setTokenIn(txFromList.get(0).getContractAddress());
        swapEvent.setTo(txToList.get(0).getReceiver());
        swapEvent.setTokenOut(txToList.get(0).getContractAddress());
    }

    public static void processPumpSwapEvent(UniswapEvent event, List<TransferEvent> txTransferEvents, List<PumpFunTokenPool> allPoolLiquidity) {
        // sell
        try {
            if (event.getAmountOut() == null) {
                for (int i = 0; i < allPoolLiquidity.size(); i++) {
                    PumpFunTokenPool dimAMMTokenPoolInfo = allPoolLiquidity.get(i);
                    if (dimAMMTokenPoolInfo.getTokenAddress().equals(event.getTokenIn())
                            && new BigInteger(dimAMMTokenPoolInfo.getAmountChange0()).abs().equals(event.getAmountIn())
                            && new BigInteger(dimAMMTokenPoolInfo.getAmountChange1()).compareTo(BigInteger.ZERO) > 0
                    ) {
                        event.setAmountOut(new BigInteger(dimAMMTokenPoolInfo.getAmountChange1()).abs());
                        dimAMMTokenPoolInfo.setPoolAddress(event.getContractAddress());
                        break;
                    }
                }
            } else { // buy
                for (int i = 0; i < allPoolLiquidity.size(); i++) {
                    PumpFunTokenPool dimAMMTokenPoolInfo = allPoolLiquidity.get(i);
                    if (dimAMMTokenPoolInfo.getTokenAddress().equals(event.getTokenOut())
                            && new BigInteger(dimAMMTokenPoolInfo.getAmountChange0()).abs().equals(event.getAmountOut())
                            && new BigInteger(dimAMMTokenPoolInfo.getAmountChange1()).compareTo(BigInteger.ZERO) < 0
                    ) {
                        event.setAmountIn(new BigInteger(dimAMMTokenPoolInfo.getAmountChange1()).abs());
                        dimAMMTokenPoolInfo.setPoolAddress(event.getContractAddress());
                        break;
                    }
                }

            }

            List<TransferEvent> swapEventsTransfer = new ArrayList<>();
            for (int i = 0; i < txTransferEvents.size(); i++) {
                TransferEvent transferEvent = txTransferEvents.get(i);
                if (transferEvent.getContractAddress().equals(event.getTokenOut())
                        && transferEvent.getAmount().equals(event.getAmountOut())
                        && transferEvent.getOuterIndex() == event.getOuterIndex()
                ) {
                    swapEventsTransfer.add(transferEvent);
                    event.setSender(transferEvent.getReceiver());
                    event.setTo(transferEvent.getReceiver());
                    break;
                }
                if (transferEvent.getContractAddress().equals(event.getTokenIn())
                        && transferEvent.getAmount().equals(event.getAmountIn())
                        && transferEvent.getOuterIndex() == event.getOuterIndex()
                ) {
                    swapEventsTransfer.add(transferEvent);
                    event.setSender(transferEvent.getSender());
                    event.setTo(transferEvent.getSender());
                    break;
                }
            }
            if (event.getAmountOut() == null || event.getAmountIn() == null) return;
            // swap 0 的情况：https://solscan.io/tx/3nQ5QKSx31rKLcHpqgq63YuHURd3kuCz6Ae9UwDRgzpVLceAAYRQGRWbywUtzVFnDfb6tcinMP2f3YofHfMVTZyF

            txTransferEvents.removeAll(swapEventsTransfer);
            // 针对 A 给 B 买的情况： https://solscan.io/tx/4orfsnT7XjfZX9E6fK8oxZSXN59Nbo44eyASophn3UkCnp1fAgpBCJWnuENpFGJZq8fJabcwQuLQBsuR9jCoZThA?utm_source=https%3A%2F%2Flogearn.com
            // Sell 的 case 没有找到, 且识别了 Buy，暂不考虑 Sell 的情况。
            if (event.getTokenIn().equals("So11111111111111111111111111111111111111112"))
                event.setSender(event.getOriSender());
        } catch (Exception e) {
            System.out.println(event);
            e.printStackTrace();
        }
    }

    private static void processMoonshotEvent(UniswapEvent swapEvent, List<TransferEvent> transfers, List<String> message) {
        // 找到所有可能的transfer1
        boolean isBuy = swapEvent.getTokenIn().equals("So11111111111111111111111111111111111111112");
        for (int i = 0; i < transfers.size(); i++) {
            TransferEvent tr = transfers.get(i);
            // 基本条件匹配
            boolean outerMatch = tr.getOuterIndex() == swapEvent.getOuterIndex();
            if (!outerMatch) continue;

            // inner_instruction_index匹配
            if ((swapEvent.getInnerIndex() == 0 && tr.getInnerIndex() >= 1)
                    || (swapEvent.getInnerIndex() != 0 && tr.getInnerIndex() > swapEvent.getInnerIndex())

            ) {
                boolean buyPattern = isBuy && tr.getSender().equals(swapEvent.getVaultOut());
                boolean sellPattern = !isBuy && tr.getReceiver().equals(swapEvent.getVaultIn());
                if (!buyPattern && !sellPattern) continue;

                String solAmount = MoonshotInstructionParser.extractCollateralAmount(message);
                if (StringUtils.isEmpty(solAmount)) continue;
                if (buyPattern) {
                    transfers.remove(tr);
                    swapEvent.setAmountIn(new BigInteger(solAmount));
                    swapEvent.setTo(tr.getReceiver());
                    swapEvent.setAmountOut(tr.getAmount());
                } else {
                    transfers.remove(tr);
                    swapEvent.setSender(tr.getSender());
                    swapEvent.setAmountIn(tr.getAmount());
                    swapEvent.setAmountOut(new BigInteger(solAmount));
                }
            }
        }
    }

    private static void processMeteoraAlmmEvent(UniswapEvent swapEvent, List<TransferEvent> transfers) {
        // 找到所有可能的transfer1
        for (int i = 0; i < transfers.size(); i++) {
            TransferEvent tempT = transfers.get(i);
            for (int j = i + 1; j < transfers.size(); j++) {
                TransferEvent tempS = transfers.get(j);
                if (tempS.getOuterIndex() != tempT.getOuterIndex()) continue;
                if (tempS.getContractAddress().equals(tempT.getContractAddress())) continue;

                if (tempS.getInnerIndex() > tempT.getInnerIndex()
                        && tempT.getSenderOrigin().equals(swapEvent.getSender())
                        && tempS.getReceiverOrigin().equals(swapEvent.getTo())
                        && ((tempS.getSenderOrigin().equals(swapEvent.getVaultOut()) && tempT.getReceiverOrigin().equals(swapEvent.getVaultIn()))
                        || (tempS.getSenderOrigin().equals(swapEvent.getVaultIn()) && tempT.getReceiverOrigin().equals(swapEvent.getVaultOut()))
                )
                ) {
                    transfers.remove(tempT);
                    transfers.remove(tempS);
                    swapEvent.setSender(tempT.getSender());
                    swapEvent.setTokenIn(tempT.getContractAddress());
                    swapEvent.setAmountIn(tempT.getAmount());
                    swapEvent.setTo(tempS.getReceiver());
                    swapEvent.setTokenOut(tempS.getContractAddress());
                    swapEvent.setAmountOut(tempS.getAmount());
                    return;
                } else if (tempS.getInnerIndex() < tempT.getInnerIndex()
                        && tempS.getSenderOrigin().equals(swapEvent.getSender())
                        && tempT.getReceiverOrigin().equals(swapEvent.getTo())
                        && ((tempT.getSenderOrigin().equals(swapEvent.getVaultOut()) && tempS.getReceiverOrigin().equals(swapEvent.getVaultIn()))
                        || (tempT.getSenderOrigin().equals(swapEvent.getVaultIn()) && tempS.getReceiverOrigin().equals(swapEvent.getVaultOut()))
                )
                ) {
                    transfers.remove(tempT);
                    transfers.remove(tempS);
                    swapEvent.setSender(tempS.getSender());
                    swapEvent.setTokenIn(tempS.getContractAddress());
                    swapEvent.setAmountIn(tempS.getAmount());
                    swapEvent.setTo(tempT.getReceiver());
                    swapEvent.setTokenOut(tempT.getContractAddress());
                    swapEvent.setAmountOut(tempT.getAmount());
                    return;
                }
            }
        }

    }


    private static void processBoopfunEvent(UniswapEvent swapEvent, List<TransferEvent> transfers, List<TransferEvent> solTransferEvent) {
        // 找到所有可能的transfer1
        boolean isBuy = swapEvent.getTokenIn().equals("So11111111111111111111111111111111111111112");
        for (int i = 0; i < transfers.size(); i++) {
            TransferEvent tempT = transfers.get(i);
            for (int j = 0; j < solTransferEvent.size(); j++) {
                TransferEvent tempS = solTransferEvent.get(j);
                if (tempS.getOuterIndex() != tempT.getOuterIndex()) continue;
                if (tempS.getContractAddress().equals(tempT.getContractAddress())) continue;

                if (isBuy
                        && tempS.getInnerIndex() + 1 == tempT.getInnerIndex()
                        && tempT.getContractAddress().equals(swapEvent.getTokenOut())
                        && tempS.getSenderOrigin().equals(swapEvent.getSender())
                        && tempT.getReceiverOrigin().equals(swapEvent.getTo())
                        && tempT.getSenderOrigin().equals(swapEvent.getVaultOut())
                        && tempS.getReceiverOrigin().equals(swapEvent.getVaultIn())
                ) {
                    transfers.remove(tempT);
                    solTransferEvent.remove(tempS);
                    swapEvent.setTo(tempT.getReceiver());
                    swapEvent.setSender(tempS.getSender());
                    swapEvent.setAmountOut(tempT.getAmount());
                    swapEvent.setAmountIn(tempS.getAmount());
                    return;
                } else if (!isBuy
                        && tempS.getInnerIndex() == tempT.getInnerIndex() + 1
                        && tempT.getContractAddress().equals(swapEvent.getTokenIn())
                        && tempT.getSenderOrigin().equals(swapEvent.getSender())
                        && tempS.getReceiverOrigin().equals(swapEvent.getTo())
                        && tempS.getSenderOrigin().equals(swapEvent.getVaultOut())
                        && tempT.getReceiverOrigin().equals(swapEvent.getVaultIn())
                ) {
                    transfers.remove(tempT);
                    solTransferEvent.remove(tempS);
                    swapEvent.setSender(tempT.getSender());
                    swapEvent.setTo(tempS.getReceiver());
                    swapEvent.setAmountIn(tempT.getAmount());
                    swapEvent.setAmountOut(tempS.getAmount());
                    return;
                }
            }
        }

    }


    private static boolean isBoopFun(UniswapEvent event) {
        return BoopInstructionParser.PROGRAM_ID.equals(event.getProgramId());
    }

    private static boolean isMoonshot(UniswapEvent event) {
        return MoonshotInstructionParser.PROGRAM_ID.equals(event.getProgramId());
    }

    /**
     * 判断是否是CPMM协议
     */
    public static boolean isRaydiumAmmV4(UniswapEvent event) {
        return RaydiumAmmInstructionParser.PROGRAM_ID.equals(event.getProgramId());
    }

    public static boolean isWhirlPool(UniswapEvent event) {
        return WhirlpoolInstructionParser.PROGRAM_ID.equals(event.getProgramId());
    }

    public static boolean isMeteoralAlmm(UniswapEvent event) {
        return MeteoraAlmmInstructionParser.PROGRAM_ID.equals(event.getProgramId());
    }

    public static boolean isMeteoralDlmm(UniswapEvent event) {
        return MeteoraDlmmInstructionParser.PROGRAM_ID.equals(event.getProgramId());
    }

    public static Map<String, Map<String, Object>> processATokenAccount(String blockTime, List<Map<String, Object>> preTokenBalances, List<String> preBalances, List<Map<String, Object>> postTokenBalances, List<String> postBalances, List<String> accountKeys) {
        if (CollectionUtils.isEmpty(postTokenBalances)) {
            return Collections.emptyMap();
        }

        Map<String, Map<String, Object>> tokenAccountMap = new HashMap<>();

        // Sol
        for (int i = 0; i < accountKeys.size(); i++) {
            String solBalance = String.valueOf(postBalances.get(i));
            String account = accountKeys.get(i);
            // 构建账户信息映射
            Map<String, Object> accountInfo = new HashMap<>();
            accountInfo.put("account", account);
            accountInfo.put("mint", "So11111111111111111111111111111111111111112");
            accountInfo.put("owner", account);
            accountInfo.put("amount", solBalance);
            accountInfo.put("sol", solBalance);

            // 使用 mint 作为键存储账户信息
            tokenAccountMap.put(accountInfo.get("account").toString(), accountInfo);
        }

//        preTokenBalances.forEach(balance -> {
//            String solBalance = String.valueOf(preBalances.get((Integer) balance.get("accountIndex")));
//            // 构建账户信息映射
//            Map<String, Object> accountInfo = new HashMap<>();
//            accountInfo.put("account", accountKeys.get((Integer) balance.get("accountIndex")));
//            accountInfo.put("mint", balance.get("mint"));
//            accountInfo.put("owner", balance.get("owner"));
//            accountInfo.put("amount", ((Map<String, Object>) balance.get("uiTokenAmount")).get("amount"));
//            accountInfo.put("sol", solBalance);
//
//            // 使用 mint 作为键存储账户信息
//            tokenAccountMap.put(accountInfo.get("account").toString(), accountInfo);
//        });

        postTokenBalances.forEach(balance -> {
            String solBalance = String.valueOf(postBalances.get((Integer) balance.get("accountIndex")));
            // 构建账户信息映射
            Map<String, Object> accountInfo = new HashMap<>();
            accountInfo.put("account", accountKeys.get((Integer) balance.get("accountIndex")));
            accountInfo.put("mint", balance.get("mint"));
            accountInfo.put("owner", balance.get("owner"));
            accountInfo.put("amount", ((Map<String, Object>) balance.get("uiTokenAmount")).get("amount"));
            accountInfo.put("sol", solBalance);

            // 使用 mint 作为键存储账户信息
            tokenAccountMap.put(accountInfo.get("account").toString(), accountInfo);
        });

        preTokenBalances.forEach(balance -> {
            Optional<Map<String, Object>> first = postTokenBalances.stream().filter(t -> balance.get("accountIndex").equals(t.get("accountIndex"))).findFirst();
            if (first.isPresent()) return;
            Map<String, Object> accountInfo = new HashMap<>();
            accountInfo.put("account", accountKeys.get((Integer) balance.get("accountIndex")));
            accountInfo.put("mint", balance.get("mint"));
            accountInfo.put("owner", balance.get("owner"));
            accountInfo.put("sol", "0");
            accountInfo.put("amount", "0");
            accountInfo.put("sol_balance", "0");
            // 使用 mint 作为键存储账户信息
            tokenAccountMap.put(accountInfo.get("account").toString(), accountInfo);
        });

//        if (!CollectionUtils.isEmpty(tokenAccountMap)) {
//            CompletableFuture.runAsync(() -> {
//                List<DimAtokenAccount> atokenAccountLists = tokenAccountMap.entrySet().stream().map(account -> {
//                    return DimAtokenAccount.builder()
//                            .account(account.getKey())
//                            .owner(account.getValue().get("owner").toString())
//                            .mint(account.getValue().get("mint").toString())
//                            .amount(new BigInteger(account.getValue().get("amount").toString()))
//                            .lastTxTime(Integer.valueOf(blockTime))
//                            .earliestTxTime(Integer.valueOf(blockTime))
//                            .build();
//                }).collect(Collectors.toList());
//                dimAtokenAccountMapper.batchInsert(atokenAccountLists);
//            }, holoPersisThreadPool);
//        }
        return tokenAccountMap;
    }

    /**
     * 判断是否是CPMM协议
     */
    public static boolean isCLMM(UniswapEvent event) {
        return RaydiumClmmInstructionParser.PROGRAM_ID.equals(event.getProgramId());
    }

    /**
     * 根据outer_index和inner_index查找对应的transfer
     */
    public static TransferEvent findTransfer(List<TransferEvent> transfers,
                                             UniswapEvent swapEvent,
                                             int transferIndex) {
        return transfers.stream()
                .filter(transfer -> {
                    // 匹配outer_instruction_index
                    boolean outerMatch = transfer.getOuterIndex() == swapEvent.getOuterIndex();

                    // 匹配inner_instruction_index
                    boolean innerMatch;
                    if (swapEvent.getInnerIndex() == 0) {  // inner_instruction_index is null
                        innerMatch = transfer.getInnerIndex() == transferIndex;
                    } else {
                        innerMatch = transfer.getInnerIndex() == swapEvent.getInnerIndex() + transferIndex;
                    }

                    return outerMatch && innerMatch;
                })
                .findFirst()
                .orElse(null);
    }


}