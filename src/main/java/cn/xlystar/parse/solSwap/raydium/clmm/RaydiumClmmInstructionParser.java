package cn.xlystar.parse.solSwap.raydium.clmm;

import org.bitcoinj.core.Base58;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class RaydiumClmmInstructionParser {

    private static final String PROGRAM_ID = "CAMMCzo5YL8w4VFF8KVHrK22GGUsp5VTaW7grrKgrWqK";

    public static Map<String, Object> parseInstruction(byte[] data, String[] accounts) {
        Map<String, Object> result = new HashMap<>();

        if (data == null || data.length == 0) {
            result.put("error", "Invalid instruction data");
            return result;
        }

        try {
            int discriminator = data[0] & 0xFF;
            RaydiumClmmInstruction instructionType = RaydiumClmmInstruction.fromValue(discriminator);
            result.put("type", instructionType.name());

            ByteBuffer buffer = ByteBuffer.wrap(data, 1, data.length - 1);
            Map<String, Object> info = parseInstructionInfo(instructionType, buffer, accounts);
            result.put("info", info);

        } catch (Exception e) {
            result.put("error", "Failed to parse instruction: " + e.getMessage());
        }

        return result;
    }

    private static Map<String, Object> parseInstructionInfo(RaydiumClmmInstruction instruction, ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        try {
            switch (instruction) {
                // === AMM配置操作 (0-1) ===
                case CREATE_AMM_CONFIG: // 0
                    return parseCreateAmmConfig(buffer, accounts);
                case UPDATE_AMM_CONFIG: // 1
                    return parseUpdateAmmConfig(buffer, accounts);

                // === 池操作 (2-3) ===
                case CREATE_POOL: // 2
                    return parseCreatePool(buffer, accounts);
                case UPDATE_POOL_STATUS: // 3
                    return parseUpdatePoolStatus(buffer, accounts);

                // === 操作账户管理 (4-5) ===
                case CREATE_OPERATION_ACCOUNT: // 4
                    return parseCreateOperationAccount(buffer, accounts);
                case UPDATE_OPERATION_ACCOUNT: // 5
                    return parseUpdateOperationAccount(buffer, accounts);

                // === 奖励管理 (6-10) ===
                case TRANSFER_REWARD_OWNER: // 6
                    return parseTransferRewardOwner(buffer, accounts);
                case INITIALIZE_REWARD: // 7
                    return parseInitializeReward(buffer, accounts);
                case COLLECT_REMAINING_REWARDS: // 8
                    return parseCollectRemainingRewards(buffer, accounts);
                case UPDATE_REWARD_INFOS: // 9
                    return parseUpdateRewardInfos(buffer, accounts);
                case SET_REWARD_PARAMS: // 10
                    return parseSetRewardParams(buffer, accounts);

                // === 费用管理 (11-12) ===
                case COLLECT_PROTOCOL_FEE: // 11
                    return parseCollectProtocolFee(buffer, accounts);
                case COLLECT_FUND_FEE: // 12
                    return parseCollectFundFee(buffer, accounts);

                // === 头寸操作 (13-16) ===
                case OPEN_POSITION: // 13
                    return parseOpenPosition(buffer, accounts);
                case OPEN_POSITION_V2: // 14
                    return parseOpenPositionV2(buffer, accounts);
                case OPEN_POSITION_WITH_TOKEN22_NFT: // 15
                    return parseOpenPositionWithToken22Nft(buffer, accounts);
                case CLOSE_POSITION: // 16
                    return parseClosePosition(buffer, accounts);

                // === 流动性操作 (17-20) ===
                case INCREASE_LIQUIDITY: // 17
                    return parseIncreaseLiquidity(buffer, accounts);
                case INCREASE_LIQUIDITY_V2: // 18
                    return parseIncreaseLiquidityV2(buffer, accounts);
                case DECREASE_LIQUIDITY: // 19
                    return parseDecreaseLiquidity(buffer, accounts);
                case DECREASE_LIQUIDITY_V2: // 20
                    return parseDecreaseLiquidityV2(buffer, accounts);

                // === 交易操作 (21-23) ===
                case SWAP: // 21
                    return parseSwap(buffer, accounts);
                case SWAP_V2: // 22
                    return parseSwapV2(buffer, accounts);
                case SWAP_ROUTER_BASE_IN: // 23
                    return parseSwapRouterBaseIn(buffer, accounts);

                default:
                    info.put("error", "Unknown instruction type: " + instruction.name());
                    return info;
            }
        } catch (Exception e) {
            info.put("error", "Failed to parse " + instruction.name() + " parameters: " + e.getMessage());
            return info;
        }
    }

    private static Map<String, Object> parseCreateAmmConfig(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        int index = buffer.getShort() & 0xFFFF;
        int tickSpacing = buffer.getShort() & 0xFFFF;
        int tradeFeeRate = buffer.getInt();
        int protocolFeeRate = buffer.getInt();
        int fundFeeRate = buffer.getInt();

        info.put("index", index);
        info.put("tickSpacing", tickSpacing);
        info.put("tradeFeeRate", tradeFeeRate);
        info.put("protocolFeeRate", protocolFeeRate);
        info.put("fundFeeRate", fundFeeRate);
        info.put("accounts", parseCreateAmmConfigAccounts(accounts));

        return info;
    }

    private static Map<String, Object> parseCreatePool(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        long sqrtPriceX64High = buffer.getLong();
        long sqrtPriceX64Low = buffer.getLong();
        long openTime = buffer.getLong();

        info.put("sqrtPriceX64", (sqrtPriceX64High << 64) | sqrtPriceX64Low);
        info.put("openTime", openTime);
        info.put("accounts", parseCreatePoolAccounts(accounts));

        return info;
    }

    private static Map<String, Object> parseSwap(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        long amount = buffer.getLong();
        long otherAmountThreshold = buffer.getLong();
        long sqrtPriceLimitX64High = buffer.getLong();
        long sqrtPriceLimitX64Low = buffer.getLong();
        boolean isBaseInput = buffer.get() != 0;

        info.put("amount", amount);
        info.put("otherAmountThreshold", otherAmountThreshold);
        info.put("sqrtPriceLimitX64", (sqrtPriceLimitX64High << 64) | sqrtPriceLimitX64Low);
        info.put("isBaseInput", isBaseInput);
        info.put("accounts", parseSwapAccounts(accounts));

        return info;
    }

    private static Map<String, Object> parseSwapV2(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        long amount = buffer.getLong();
        long otherAmountThreshold = buffer.getLong();
        long sqrtPriceLimitX64High = buffer.getLong();
        long sqrtPriceLimitX64Low = buffer.getLong();
        boolean isBaseInput = buffer.get() != 0;

        info.put("amount", amount);
        info.put("otherAmountThreshold", otherAmountThreshold);
        info.put("sqrtPriceLimitX64", (sqrtPriceLimitX64High << 64) | sqrtPriceLimitX64Low);
        info.put("isBaseInput", isBaseInput);
        info.put("accounts", parseSwapV2Accounts(accounts));

        return info;
    }

    private static Map<String, String> parseSwapV2Accounts(String[] accounts) {
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 13) {
            accountMap.put("payer", accounts[0]);
            accountMap.put("ammConfig", accounts[1]);
            accountMap.put("pool", accounts[2]);
            accountMap.put("tokenA", accounts[3]);
            accountMap.put("tokenB", accounts[4]);
            accountMap.put("tokenOwnerAccountA", accounts[5]);
            accountMap.put("tokenOwnerAccountB", accounts[6]);
            accountMap.put("tokenVaultA", accounts[7]);
            accountMap.put("tokenVaultB", accounts[8]);
            accountMap.put("tickArray0", accounts[9]);
            accountMap.put("tickArray1", accounts[10]);
            accountMap.put("tickArray2", accounts[11]);
            accountMap.put("oracle", accounts[12]);
        }
        return accountMap;
    }

    // 账户解析辅助方法
    private static Map<String, String> parseCreateAmmConfigAccounts(String[] accounts) {
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 3) {
            accountMap.put("owner", accounts[0]);
            accountMap.put("ammConfig", accounts[1]);
            accountMap.put("systemProgram", accounts[2]);
        }
        return accountMap;
    }

    private static Map<String, String> parseCreatePoolAccounts(String[] accounts) {
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 8) {
            accountMap.put("poolCreator", accounts[0]);
            accountMap.put("ammConfig", accounts[1]);
            accountMap.put("pool", accounts[2]);
            accountMap.put("tokenMintA", accounts[3]);
            accountMap.put("tokenMintB", accounts[4]);
            accountMap.put("tokenVaultA", accounts[5]);
            accountMap.put("tokenVaultB", accounts[6]);
            accountMap.put("systemProgram", accounts[7]);
        }
        return accountMap;
    }

    private static Map<String, String> parseSwapAccounts(String[] accounts) {
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 12) {
            accountMap.put("payer", accounts[0]);
            accountMap.put("ammConfig", accounts[1]);
            accountMap.put("pool", accounts[2]);
            accountMap.put("tokenA", accounts[3]);
            accountMap.put("tokenB", accounts[4]);
            accountMap.put("tokenOwnerAccountA", accounts[5]);
            accountMap.put("tokenOwnerAccountB", accounts[6]);
            accountMap.put("tokenVaultA", accounts[7]);
            accountMap.put("tokenVaultB", accounts[8]);
            accountMap.put("tickArray", accounts[9]);
            accountMap.put("oracle", accounts[10]);
            accountMap.put("tokenProgram", accounts[11]);
        }
        return accountMap;
    }
    private static Map<String, Object> parseUpdateAmmConfig(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        int param = buffer.get() & 0xFF;
        int value = buffer.getInt();

        info.put("param", param);
        info.put("value", value);
        info.put("accounts", parseUpdateAmmConfigAccounts(accounts));

        return info;
    }

    private static Map<String, Object> parseUpdatePoolStatus(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        int status = buffer.get() & 0xFF;

        info.put("status", status);
        info.put("accounts", parseUpdatePoolStatusAccounts(accounts));

        return info;
    }

    private static Map<String, Object> parseCreateOperationAccount(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        info.put("accounts", parseCreateOperationAccountAccounts(accounts));
        return info;
    }

    private static Map<String, Object> parseUpdateOperationAccount(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        int param = buffer.get() & 0xFF;
        int keysLength = buffer.getInt();
        String[] keys = new String[keysLength];
        for (int i = 0; i < keysLength; i++) {
            byte[] key = new byte[32];
            buffer.get(key);
            keys[i] = Base58.encode(key);
        }

        info.put("param", param);
        info.put("keys", keys);
        info.put("accounts", parseUpdateOperationAccountAccounts(accounts));

        return info;
    }
    private static Map<String, String> parseCreateOperationAccountAccounts(String[] accounts) {
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 4) {
            accountMap.put("admin", accounts[0]);
            accountMap.put("operationState", accounts[1]);
            accountMap.put("systemProgram", accounts[2]);
            accountMap.put("rent", accounts[3]);
        }
        return accountMap;
    }

    private static Map<String, String> parseUpdateOperationAccountAccounts(String[] accounts) {
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 2) {
            accountMap.put("admin", accounts[0]);
            accountMap.put("operationState", accounts[1]);
        }
        return accountMap;
    }

    private static Map<String, Object> parseInitializeReward(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        int rewardIndex = buffer.get() & 0xFF;
        long openTime = buffer.getLong();
        long endTime = buffer.getLong();
        long emissionsPerSecondX64High = buffer.getLong();
        long emissionsPerSecondX64Low = buffer.getLong();

        info.put("rewardIndex", rewardIndex);
        info.put("openTime", openTime);
        info.put("endTime", endTime);
        info.put("emissionsPerSecondX64", (emissionsPerSecondX64High << 64) | emissionsPerSecondX64Low);
        info.put("accounts", parseInitializeRewardAccounts(accounts));

        return info;
    }

    private static Map<String, Object> parseOpenPosition(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        int tickLowerIndex = buffer.getInt();
        int tickUpperIndex = buffer.getInt();
        int tickArrayLowerStartIndex = buffer.getInt();
        int tickArrayUpperStartIndex = buffer.getInt();
        long liquidityHigh = buffer.getLong();
        long liquidityLow = buffer.getLong();
        long amount0Max = buffer.getLong();
        long amount1Max = buffer.getLong();

        info.put("tickLowerIndex", tickLowerIndex);
        info.put("tickUpperIndex", tickUpperIndex);
        info.put("tickArrayLowerStartIndex", tickArrayLowerStartIndex);
        info.put("tickArrayUpperStartIndex", tickArrayUpperStartIndex);
        info.put("liquidity", (liquidityHigh << 64) | liquidityLow);
        info.put("amount0Max", amount0Max);
        info.put("amount1Max", amount1Max);
        info.put("accounts", parseOpenPositionAccounts(accounts));

        return info;
    }

    private static Map<String, Object> parseIncreaseLiquidity(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        long liquidityHigh = buffer.getLong();
        long liquidityLow = buffer.getLong();
        long amount0Max = buffer.getLong();
        long amount1Max = buffer.getLong();

        info.put("liquidity", (liquidityHigh << 64) | liquidityLow);
        info.put("amount0Max", amount0Max);
        info.put("amount1Max", amount1Max);
        info.put("accounts", parseIncreaseLiquidityAccounts(accounts));

        return info;
    }

    // 账户解析辅助方法继续
    private static Map<String, String> parseUpdateAmmConfigAccounts(String[] accounts) {
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 2) {
            accountMap.put("owner", accounts[0]);
            accountMap.put("ammConfig", accounts[1]);
        }
        return accountMap;
    }

    private static Map<String, String> parseUpdatePoolStatusAccounts(String[] accounts) {
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 3) {
            accountMap.put("owner", accounts[0]);
            accountMap.put("pool", accounts[1]);
            accountMap.put("ammConfig", accounts[2]);
        }
        return accountMap;
    }

    private static Map<String, String> parseInitializeRewardAccounts(String[] accounts) {
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 7) {
            accountMap.put("rewardFunder", accounts[0]);
            accountMap.put("pool", accounts[1]);
            accountMap.put("rewardTokenMint", accounts[2]);
            accountMap.put("rewardTokenVault", accounts[3]);
            accountMap.put("tokenProgram", accounts[4]);
            accountMap.put("systemProgram", accounts[5]);
            accountMap.put("rent", accounts[6]);
        }
        return accountMap;
    }

    private static Map<String, String> parseOpenPositionAccounts(String[] accounts) {
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 14) {
            accountMap.put("payer", accounts[0]);
            accountMap.put("pool", accounts[1]);
            accountMap.put("position", accounts[2]);
            accountMap.put("positionNftMint", accounts[3]);
            accountMap.put("positionNftAccount", accounts[4]);
            accountMap.put("tokenAccount0", accounts[5]);
            accountMap.put("tokenAccount1", accounts[6]);
            accountMap.put("tokenVault0", accounts[7]);
            accountMap.put("tokenVault1", accounts[8]);
            accountMap.put("tickArrayLower", accounts[9]);
            accountMap.put("tickArrayUpper", accounts[10]);
            accountMap.put("tokenProgram", accounts[11]);
            accountMap.put("systemProgram", accounts[12]);
            accountMap.put("rent", accounts[13]);
        }
        return accountMap;
    }

    private static Map<String, String> parseIncreaseLiquidityAccounts(String[] accounts) {
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 11) {
            accountMap.put("payer", accounts[0]);
            accountMap.put("position", accounts[1]);
            accountMap.put("pool", accounts[2]);
            accountMap.put("tokenAccount0", accounts[3]);
            accountMap.put("tokenAccount1", accounts[4]);
            accountMap.put("tokenVault0", accounts[5]);
            accountMap.put("tokenVault1", accounts[6]);
            accountMap.put("tickArrayLower", accounts[7]);
            accountMap.put("tickArrayUpper", accounts[8]);
            accountMap.put("tokenProgram", accounts[9]);
            accountMap.put("owner", accounts[10]);
        }
        return accountMap;
    }

    private static Map<String, Object> parseDecreaseLiquidity(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        long liquidityHigh = buffer.getLong();
        long liquidityLow = buffer.getLong();
        long amount0Min = buffer.getLong();
        long amount1Min = buffer.getLong();

        info.put("liquidity", (liquidityHigh << 64) | liquidityLow);
        info.put("amount0Min", amount0Min);
        info.put("amount1Min", amount1Min);
        info.put("accounts", parseDecreaseLiquidityAccounts(accounts));

        return info;
    }

    private static Map<String, Object> parseSwapRouterBaseIn(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        long amountIn = buffer.getLong();
        long amountOutMinimum = buffer.getLong();

        info.put("amountIn", amountIn);
        info.put("amountOutMinimum", amountOutMinimum);
        info.put("accounts", parseSwapRouterBaseInAccounts(accounts));

        return info;
    }

    private static Map<String, Object> parseCollectFee(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        long amount0Requested = buffer.getLong();
        long amount1Requested = buffer.getLong();

        info.put("amount0Requested", amount0Requested);
        info.put("amount1Requested", amount1Requested);
        info.put("accounts", parseCollectFeeAccounts(accounts));

        return info;
    }

    private static Map<String, Object> parseCollectReward(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        int rewardIndex = buffer.get() & 0xFF;

        info.put("rewardIndex", rewardIndex);
        info.put("accounts", parseCollectRewardAccounts(accounts));

        return info;
    }

    private static Map<String, Object> parseSetRewardParams(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        int rewardIndex = buffer.get() & 0xFF;
        long emissionsPerSecondX64High = buffer.getLong();
        long emissionsPerSecondX64Low = buffer.getLong();
        long openTime = buffer.getLong();
        long endTime = buffer.getLong();

        info.put("rewardIndex", rewardIndex);
        info.put("emissionsPerSecondX64", (emissionsPerSecondX64High << 64) | emissionsPerSecondX64Low);
        info.put("openTime", openTime);
        info.put("endTime", endTime);
        info.put("accounts", parseSetRewardParamsAccounts(accounts));

        return info;
    }

    private static Map<String, Object> parseCollectProtocolFee(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        long amount0Requested = buffer.getLong();
        long amount1Requested = buffer.getLong();

        info.put("amount0Requested", amount0Requested);
        info.put("amount1Requested", amount1Requested);
        info.put("accounts", parseCollectProtocolFeeAccounts(accounts));

        return info;
    }

    // 账户解析辅助方法
    private static Map<String, String> parseDecreaseLiquidityAccounts(String[] accounts) {
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 11) {
            accountMap.put("owner", accounts[0]);
            accountMap.put("position", accounts[1]);
            accountMap.put("pool", accounts[2]);
            accountMap.put("tokenAccount0", accounts[3]);
            accountMap.put("tokenAccount1", accounts[4]);
            accountMap.put("tokenVault0", accounts[5]);
            accountMap.put("tokenVault1", accounts[6]);
            accountMap.put("tickArrayLower", accounts[7]);
            accountMap.put("tickArrayUpper", accounts[8]);
            accountMap.put("tokenProgram", accounts[9]);
            accountMap.put("positionNftAccount", accounts[10]);
        }
        return accountMap;
    }

    private static Map<String, String> parseSwapRouterBaseInAccounts(String[] accounts) {
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 13) {
            accountMap.put("payer", accounts[0]);
            accountMap.put("inputTokenAccount", accounts[1]);
            accountMap.put("outputTokenAccount", accounts[2]);
            accountMap.put("inputVault", accounts[3]);
            accountMap.put("outputVault", accounts[4]);
            accountMap.put("pool", accounts[5]);
            accountMap.put("ammConfig", accounts[6]);
            accountMap.put("tokenProgram", accounts[7]);
            accountMap.put("tickArray0", accounts[8]);
            accountMap.put("tickArray1", accounts[9]);
            accountMap.put("tickArray2", accounts[10]);
            accountMap.put("oracle", accounts[11]);
            accountMap.put("systemProgram", accounts[12]);
        }
        return accountMap;
    }

    private static Map<String, String> parseCollectFeeAccounts(String[] accounts) {
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 8) {
            accountMap.put("owner", accounts[0]);
            accountMap.put("position", accounts[1]);
            accountMap.put("pool", accounts[2]);
            accountMap.put("tokenAccount0", accounts[3]);
            accountMap.put("tokenAccount1", accounts[4]);
            accountMap.put("tokenVault0", accounts[5]);
            accountMap.put("tokenVault1", accounts[6]);
            accountMap.put("tokenProgram", accounts[7]);
        }
        return accountMap;
    }

    private static Map<String, String> parseCollectRewardAccounts(String[] accounts) {
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 7) {
            accountMap.put("owner", accounts[0]);
            accountMap.put("position", accounts[1]);
            accountMap.put("pool", accounts[2]);
            accountMap.put("rewardTokenVault", accounts[3]);
            accountMap.put("rewardTokenAccount", accounts[4]);
            accountMap.put("tokenProgram", accounts[5]);
            accountMap.put("positionNftAccount", accounts[6]);
        }
        return accountMap;
    }

    private static Map<String, String> parseSetRewardParamsAccounts(String[] accounts) {
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 4) {
            accountMap.put("rewardFunder", accounts[0]);
            accountMap.put("rewardTokenVault", accounts[1]);
            accountMap.put("pool", accounts[2]);
            accountMap.put("tokenProgram", accounts[3]);
        }
        return accountMap;
    }

    private static Map<String, String> parseCollectProtocolFeeAccounts(String[] accounts) {
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 7) {
            accountMap.put("owner", accounts[0]);
            accountMap.put("pool", accounts[1]);
            accountMap.put("tokenVault0", accounts[2]);
            accountMap.put("tokenVault1", accounts[3]);
            accountMap.put("recipientTokenAccount0", accounts[4]);
            accountMap.put("recipientTokenAccount1", accounts[5]);
            accountMap.put("tokenProgram", accounts[6]);
        }
        return accountMap;
    }


    private static Map<String, Object> parseDecreaseLiquidityV2(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        long liquidityHigh = buffer.getLong();
        long liquidityLow = buffer.getLong();
        long amount0Min = buffer.getLong();
        long amount1Min = buffer.getLong();

        info.put("liquidity", (liquidityHigh << 64) | liquidityLow);
        info.put("amount0Min", amount0Min);
        info.put("amount1Min", amount1Min);
        info.put("accounts", parseDecreaseLiquidityV2Accounts(accounts));

        return info;
    }

    private static Map<String, Object> parseIncreaseLiquidityV2(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        long liquidityHigh = buffer.getLong();
        long liquidityLow = buffer.getLong();
        long amount0Max = buffer.getLong();
        long amount1Max = buffer.getLong();
        boolean hasBaseFlag = buffer.get() != 0;
        boolean baseFlag = hasBaseFlag ? buffer.get() != 0 : false;

        info.put("liquidity", (liquidityHigh << 64) | liquidityLow);
        info.put("amount0Max", amount0Max);
        info.put("amount1Max", amount1Max);
        if (hasBaseFlag) {
            info.put("baseFlag", baseFlag);
        }
        info.put("accounts", parseIncreaseLiquidityV2Accounts(accounts));

        return info;
    }

    private static Map<String, Object> parseOpenPositionV2(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        int tickLowerIndex = buffer.getInt();
        int tickUpperIndex = buffer.getInt();
        int tickArrayLowerStartIndex = buffer.getInt();
        int tickArrayUpperStartIndex = buffer.getInt();
        long liquidityHigh = buffer.getLong();
        long liquidityLow = buffer.getLong();
        long amount0Max = buffer.getLong();
        long amount1Max = buffer.getLong();
        boolean withMetadata = buffer.get() != 0;
        boolean hasBaseFlag = buffer.get() != 0;
        boolean baseFlag = hasBaseFlag ? buffer.get() != 0 : false;

        info.put("tickLowerIndex", tickLowerIndex);
        info.put("tickUpperIndex", tickUpperIndex);
        info.put("tickArrayLowerStartIndex", tickArrayLowerStartIndex);
        info.put("tickArrayUpperStartIndex", tickArrayUpperStartIndex);
        info.put("liquidity", (liquidityHigh << 64) | liquidityLow);
        info.put("amount0Max", amount0Max);
        info.put("amount1Max", amount1Max);
        info.put("withMetadata", withMetadata);
        if (hasBaseFlag) {
            info.put("baseFlag", baseFlag);
        }
        info.put("accounts", parseOpenPositionV2Accounts(accounts));

        return info;
    }

    private static Map<String, Object> parseOpenPositionWithToken22Nft(ByteBuffer buffer, String[] accounts) {
        // 与 OpenPositionV2 相同的参数结构
        return parseOpenPositionV2(buffer, accounts);
    }

    private static Map<String, Object> parseClosePosition(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        info.put("accounts", parseClosePositionAccounts(accounts));
        return info;
    }

    private static Map<String, Object> parseCollectFundFee(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        long amount0Requested = buffer.getLong();
        long amount1Requested = buffer.getLong();

        info.put("amount0Requested", amount0Requested);
        info.put("amount1Requested", amount1Requested);
        info.put("accounts", parseCollectFundFeeAccounts(accounts));

        return info;
    }

    private static Map<String, Object> parseUpdateRewardInfos(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        info.put("accounts", parseUpdateRewardInfosAccounts(accounts));
        return info;
    }

    private static Map<String, Object> parseTransferRewardOwner(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        byte[] newOwner = new byte[32];
        buffer.get(newOwner);

        info.put("newOwner", Base58.encode(newOwner));
        info.put("accounts", parseTransferRewardOwnerAccounts(accounts));
        return info;
    }

    private static Map<String, Object> parseCollectRemainingRewards(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        int rewardIndex = buffer.get() & 0xFF;

        info.put("rewardIndex", rewardIndex);
        info.put("accounts", parseCollectRemainingRewardsAccounts(accounts));
        return info;
    }

    // 对应的账户解析方法
    private static Map<String, String> parseDecreaseLiquidityV2Accounts(String[] accounts) {
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 12) {
            accountMap.put("owner", accounts[0]);
            accountMap.put("position", accounts[1]);
            accountMap.put("pool", accounts[2]);
            accountMap.put("tokenAccount0", accounts[3]);
            accountMap.put("tokenAccount1", accounts[4]);
            accountMap.put("tokenVault0", accounts[5]);
            accountMap.put("tokenVault1", accounts[6]);
            accountMap.put("tickArrayLower", accounts[7]);
            accountMap.put("tickArrayUpper", accounts[8]);
            accountMap.put("tokenProgram", accounts[9]);
            accountMap.put("tokenProgram2022", accounts[10]);
            accountMap.put("positionNftAccount", accounts[11]);
        }
        return accountMap;
    }

    private static Map<String, String> parseIncreaseLiquidityV2Accounts(String[] accounts) {
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 12) {
            accountMap.put("payer", accounts[0]);
            accountMap.put("position", accounts[1]);
            accountMap.put("pool", accounts[2]);
            accountMap.put("tokenAccount0", accounts[3]);
            accountMap.put("tokenAccount1", accounts[4]);
            accountMap.put("tokenVault0", accounts[5]);
            accountMap.put("tokenVault1", accounts[6]);
            accountMap.put("tickArrayLower", accounts[7]);
            accountMap.put("tickArrayUpper", accounts[8]);
            accountMap.put("tokenProgram", accounts[9]);
            accountMap.put("tokenProgram2022", accounts[10]);
            accountMap.put("owner", accounts[11]);
        }
        return accountMap;
    }

    private static Map<String, String> parseOpenPositionV2Accounts(String[] accounts) {
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 15) {
            accountMap.put("payer", accounts[0]);
            accountMap.put("pool", accounts[1]);
            accountMap.put("position", accounts[2]);
            accountMap.put("positionNftMint", accounts[3]);
            accountMap.put("positionNftAccount", accounts[4]);
            accountMap.put("tokenAccount0", accounts[5]);
            accountMap.put("tokenAccount1", accounts[6]);
            accountMap.put("tokenVault0", accounts[7]);
            accountMap.put("tokenVault1", accounts[8]);
            accountMap.put("tickArrayLower", accounts[9]);
            accountMap.put("tickArrayUpper", accounts[10]);
            accountMap.put("tokenProgram", accounts[11]);
            accountMap.put("tokenProgram2022", accounts[12]);
            accountMap.put("systemProgram", accounts[13]);
            accountMap.put("rent", accounts[14]);
        }
        return accountMap;
    }

    private static Map<String, String> parseClosePositionAccounts(String[] accounts) {
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 7) {
            accountMap.put("nftOwner", accounts[0]);
            accountMap.put("position", accounts[1]);
            accountMap.put("positionNftMint", accounts[2]);
            accountMap.put("positionNftAccount", accounts[3]);
            accountMap.put("tokenProgram", accounts[4]);
            accountMap.put("tokenProgram2022", accounts[5]);
            accountMap.put("systemProgram", accounts[6]);
        }
        return accountMap;
    }

    private static Map<String, String> parseCollectFundFeeAccounts(String[] accounts) {
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 7) {
            accountMap.put("owner", accounts[0]);
            accountMap.put("pool", accounts[1]);
            accountMap.put("tokenVault0", accounts[2]);
            accountMap.put("tokenVault1", accounts[3]);
            accountMap.put("recipientTokenAccount0", accounts[4]);
            accountMap.put("recipientTokenAccount1", accounts[5]);
            accountMap.put("tokenProgram", accounts[6]);
        }
        return accountMap;
    }

    private static Map<String, String> parseUpdateRewardInfosAccounts(String[] accounts) {
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 1) {
            accountMap.put("pool", accounts[0]);
        }
        return accountMap;
    }

    private static Map<String, String> parseTransferRewardOwnerAccounts(String[] accounts) {
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 3) {
            accountMap.put("oldOwner", accounts[0]);
            accountMap.put("newOwner", accounts[1]);
            accountMap.put("pool", accounts[2]);
        }
        return accountMap;
    }

    private static Map<String, String> parseCollectRemainingRewardsAccounts(String[] accounts) {
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 5) {
            accountMap.put("rewardFunder", accounts[0]);
            accountMap.put("pool", accounts[1]);
            accountMap.put("rewardVault", accounts[2]);
            accountMap.put("rewardTokenAccount", accounts[3]);
            accountMap.put("tokenProgram", accounts[4]);
        }
        return accountMap;
    }
} 