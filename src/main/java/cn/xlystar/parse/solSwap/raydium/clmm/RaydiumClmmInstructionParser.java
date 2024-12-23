package cn.xlystar.parse.solSwap.raydium.clmm;

import org.bitcoinj.core.Base58;
import org.bouncycastle.util.encoders.Hex;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
            ByteBuffer buffer = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN);
            byte[] discriminatorBytes = new byte[8];
            buffer.get(discriminatorBytes);
            String discriminator = Hex.toHexString(discriminatorBytes);
            RaydiumClmmInstruction instructionType = RaydiumClmmInstruction.fromValue(discriminator);
            result.put("type", instructionType.name());

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
                    info = parseCreateAmmConfig(buffer, accounts);
                    info.put("type", "createAmmConfig");
                    return info;
                case UPDATE_AMM_CONFIG: // 1
                    info = parseUpdateAmmConfig(buffer, accounts);
                    info.put("type", "updateAmmConfig");
                    return info;

                // === 池操作 (2-3) ===
                case CREATE_POOL: // 2
                    info = parseCreatePool(buffer, accounts);
                    info.put("type", "createPool");
                    return info;
                case UPDATE_POOL_STATUS: // 3
                    info = parseUpdatePoolStatus(buffer, accounts);
                    info.put("type", "updatePoolStatus");
                    return info;

                // === 操作账户管理 (4-5) ===
                case CREATE_OPERATION_ACCOUNT: // 4
                    info = parseCreateOperationAccount(buffer, accounts);
                    info.put("type", "createOperationAccount");
                    return info;
                case UPDATE_OPERATION_ACCOUNT: // 5
                    info = parseUpdateOperationAccount(buffer, accounts);
                    info.put("type", "updateOperationAccount");
                    return info;

                // === 奖励管理 (6-10) ===
                case TRANSFER_REWARD_OWNER: // 6
                    info = parseTransferRewardOwner(buffer, accounts);
                    info.put("type", "transferRewardOwner");
                    return info;
                case INITIALIZE_REWARD: // 7
                    info = parseInitializeReward(buffer, accounts);
                    info.put("type", "initializeReward");
                    return info;
                case COLLECT_REMAINING_REWARDS: // 8
                    info = parseCollectRemainingRewards(buffer, accounts);
                    info.put("type", "collectRemainingRewards");
                    return info;
                case UPDATE_REWARD_INFOS: // 9
                    info = parseUpdateRewardInfos(buffer, accounts);
                    info.put("type", "updateRewardInfos");
                    return info;
                case SET_REWARD_PARAMS: // 10
                    info = parseSetRewardParams(buffer, accounts);
                    info.put("type", "setRewardParams");
                    return info;

                // === 费用管理 (11-12) ===
                case COLLECT_PROTOCOL_FEE: // 11
                    info = parseCollectProtocolFee(buffer, accounts);
                    info.put("type", "collectProtocolFee");
                    return info;
                case COLLECT_FUND_FEE: // 12
                    info = parseCollectFundFee(buffer, accounts);
                    info.put("type", "collectFundFee");
                    return info;

                // === 头寸操作 (13-16) ===
                case OPEN_POSITION: // 13
                    info = parseOpenPosition(buffer, accounts);
                    info.put("type", "openPosition");
                    return info;
                case OPEN_POSITION_V2: // 14
                    info = parseOpenPositionV2(buffer, accounts);
                    info.put("type", "openPositionV2");
                    return info;
                case OPEN_POSITION_WITH_TOKEN22_NFT: // 15
                    info = parseOpenPositionWithToken22Nft(buffer, accounts);
                    info.put("type", "openPositionWithToken22Nft");
                    return info;
                case CLOSE_POSITION: // 16
                    info = parseClosePosition(buffer, accounts);
                    info.put("type", "closePosition");
                    return info;

                // === 流动性操作 (17-20) ===
                case INCREASE_LIQUIDITY: // 17
                    info = parseIncreaseLiquidity(buffer, accounts);
                    info.put("type", "increaseLiquidity");
                    return info;
                case INCREASE_LIQUIDITY_V2: // 18
                    info = parseIncreaseLiquidityV2(buffer, accounts);
                    info.put("type", "increaseLiquidityV2");
                    return info;
                case DECREASE_LIQUIDITY: // 19
                    info = parseDecreaseLiquidity(buffer, accounts);
                    info.put("type", "decreaseLiquidity");
                    return info;
                case DECREASE_LIQUIDITY_V2: // 20
                    info = parseDecreaseLiquidityV2(buffer, accounts);
                    info.put("type", "decreaseLiquidityV2");
                    return info;

                // === 交易操作 (21-23) ===
                case SWAP: // 21
                    info = parseSwap(buffer, accounts);
                    info.put("type", "swap");
                    return info;
                case SWAP_V2: // 22
                    info = parseSwapV2(buffer, accounts);
                    info.put("type", "swapV2");
                    return info;
                case SWAP_ROUTER_BASE_IN: // 23
                    info = parseSwapRouterBaseIn(buffer, accounts);
                    info.put("type", "swapRouterBaseIn");
                    return info;

                default:
                    info.put("error", "Unknown instruction type: " + instruction.name());
                    info.put("type", "unknown");
                    return info;
            }
        } catch (Exception e) {
            info.put("error", "Failed to parse " + instruction.name() + " parameters: " + e.getMessage());
            info.put("type", "error");
            return info;
        }
    }
    private static Map<String, Object> parseCreateAmmConfig(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        // 存储解析的字段
        info.put("index", Short.toUnsignedInt(buffer.getShort()));
        info.put("tick_spacing", Short.toUnsignedInt(buffer.getShort()));
        info.put("trade_fee_rate", Integer.toUnsignedString(buffer.getInt()));
        info.put("protocol_fee_rate", Integer.toUnsignedString(buffer.getInt()));
        info.put("fund_fee_rate", Integer.toUnsignedString(buffer.getInt()));

        // 账户信息
        info.put("owner", accounts[0]); // 协议拥有者
        info.put("amm_config", accounts[1]); // AMM 配置账户
        info.put("system_program", accounts[2]); // 系统程序账户

        return info;
    }

    private static Map<String, Object> parseUpdateAmmConfig(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        // 设置返回信息
        info.put("param", Byte.toUnsignedInt(buffer.get()));
        info.put("value", Integer.toUnsignedString(buffer.getInt()));

        // 解析账户信息
        info.put("owner", accounts[0]); // Owner account
        info.put("amm_config", accounts[1]); // AMM config account

        // 处理剩余账户（如果有）
        if (accounts.length > 2) {
            info.put("remaining_account", accounts[2]); // 剩余账户
        }
        return info;
    }

    private static Map<String, Object> parseCreatePool(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        String low = Long.toUnsignedString(buffer.getLong());  // 读取低位
        String high = Long.toUnsignedString(buffer.getLong()); // 读取高位
        BigInteger sqrtPriceLimitX64 = new BigInteger(high).shiftLeft(64).or(new BigInteger(low));
        info.put("sqrt_price_x64", sqrtPriceLimitX64);
        info.put("open_time", "0");
        if (buffer.limit() - buffer.position() >= 8) {
            info.put("open_time", Long.toUnsignedString(buffer.getLong()));
        }

        // 账户信息
        info.put("pool_creator", accounts[0]); // 创建池的账户
        info.put("amm_config", accounts[1]); // AMM 配置账户
        info.put("pool_state", accounts[2]); // 池状态账户
        info.put("token_mint_0", accounts[3]); // Token 0 mint 账户
        info.put("token_mint_1", accounts[4]); // Token 1 mint 账户
        info.put("token_vault_0", accounts[5]); // Token 0 vault 账户
        info.put("token_vault_1", accounts[6]); // Token 1 vault 账户
        info.put("observation_state", accounts[7]); // 观察状态账户
        info.put("tick_array_bitmap", accounts[8]); // Tick 数组位图账户
        info.put("token_program_0", accounts[9]); // Token 程序 0
        info.put("token_program_1", accounts[10]); // Token 程序 1
        info.put("system_program", accounts[11]); // 系统程序账户
        info.put("rent", accounts[12]); // 租金账户
        return info;
    }

    private static Map<String, Object> parseSwap(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 存储解析的字段
        info.put("amount", Long.toUnsignedString(buffer.getLong()));
        info.put("other_amount_threshold", Long.toUnsignedString(buffer.getLong()));

        String low = Long.toUnsignedString(buffer.getLong());  // 读取低位
        String high = Long.toUnsignedString(buffer.getLong()); // 读取高位
        BigInteger sqrtPriceLimitX64 = new BigInteger(high).shiftLeft(64).or(new BigInteger(low));
        info.put("sqrt_price_limit_x64", sqrtPriceLimitX64);
        info.put("is_base_input", Long.toUnsignedString(buffer.get()));

        // 账户信息
        info.put("payer", accounts[0]); // 用户执行交换的账户
        info.put("amm_config", accounts[1]); // AMM 配置账户
        info.put("pool_state", accounts[2]); // 池状态账户
        info.put("input_token_account", accounts[3]); // 输入代币账户
        info.put("output_token_account", accounts[4]); // 输出代币账户
        info.put("input_vault", accounts[5]); // 输入金库账户
        info.put("output_vault", accounts[6]); // 输出金库账户
        info.put("observation_state", accounts[7]); // 观察状态账户
        info.put("token_program", accounts[8]); // 代币程序账户
        info.put("tick_array", accounts[9]); // Tick 数组账户

        return info;
    }

    private static Map<String, Object> parseSwapV2(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        // 存储解析的字段
        info.put("amount", Long.toUnsignedString(buffer.getLong()));
        info.put("other_amount_threshold", Long.toUnsignedString(buffer.getLong()));
        String low = Long.toUnsignedString(buffer.getLong());  // 读取低位
        String high = Long.toUnsignedString(buffer.getLong()); // 读取高位
        BigInteger sqrtPriceLimitX64 = new BigInteger(high).shiftLeft(64).or(new BigInteger(low));
        info.put("sqrt_price_limit_x64", sqrtPriceLimitX64);
        info.put("is_base_input", Long.toUnsignedString(buffer.get()));

        // 账户信息
        info.put("payer", accounts[0]); // 用户执行交换的账户
        info.put("amm_config", accounts[1]); // AMM 配置账户
        info.put("pool_state", accounts[2]); // 池状态账户
        info.put("input_token_account", accounts[3]); // 输入代币账户
        info.put("output_token_account", accounts[4]); // 输出代币账户
        info.put("input_vault", accounts[5]); // 输入金库账户
        info.put("output_vault", accounts[6]); // 输出金库账户
        info.put("observation_state", accounts[7]); // 观察状态账户
        info.put("token_program", accounts[8]); // 代币程序账户
        info.put("token_program_2022", accounts[9]); // 2022 代币程序账户
        info.put("memo_program", accounts[10]); // Memo 程序账户
        info.put("input_vault_mint", accounts[11]); // 输入金库的 mint 账户
        info.put("output_vault_mint", accounts[12]); // 输出金库的 mint 账户
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


    private static Map<String, Object> parseUpdatePoolStatus(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        info.put("status", Byte.toUnsignedInt(buffer.get()));

        // 账户信息
        info.put("authority", accounts[0]); // 权限账户
        info.put("pool_state", accounts[1]); // 池状态账户
        return info;
    }

    private static Map<String, Object> parseCreateOperationAccount(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 存储账户信息
        info.put("owner", accounts[0]); // 操作账户拥有者
        info.put("operation_state", accounts[1]); // 操作状态账户
        info.put("system_program", accounts[2]); // 系统程序账户
        return info;
    }

    private static Map<String, Object> parseUpdateOperationAccount(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        // 解析输入参数
        int param = Byte.toUnsignedInt(buffer.get()); // 读取参数
        long keysCount = Integer.toUnsignedLong(buffer.getInt()); // 读取 keys 的数量
        List<String> keys = new ArrayList<>();

        // 读取 keys
        for (int i = 0; i < keysCount; i++) {
            byte[] keyBytes = new byte[32]; // 假设 Pubkey 是 32 字节
            buffer.get(keyBytes);
            keys.add(Base58.encode(keyBytes)); // 假设有一个 Pubkey 类来处理字节数组
        }

        // 存储解析的字段
        info.put("param", param);
        info.put("keys", keys);

        // 账户信息
        info.put("owner", accounts[0]); // 操作账户拥有者
        info.put("operation_state", accounts[1]); // 操作状态账户
        info.put("system_program", accounts[2]); // 系统程序账户
        return info;
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
        // 存储解析的字段
        info.put("open_time", Long.toUnsignedString(buffer.getLong()));
        info.put("end_time", Long.toUnsignedString(buffer.getLong()));
        String low = Long.toUnsignedString(buffer.getLong());  // 读取低位
        String high = Long.toUnsignedString(buffer.getLong()); // 读取高位
        BigInteger emissionsPerSecondX64 = new BigInteger(high).shiftLeft(64).or(new BigInteger(low));
        info.put("emissions_per_second_x64", emissionsPerSecondX64);

        // 账户信息
        info.put("reward_funder", accounts[0]); // 奖励资金提供者
        info.put("funder_token_account", accounts[1]); // 资金提供者的奖励代币账户
        info.put("amm_config", accounts[2]); // AMM 配置账户
        info.put("pool_state", accounts[3]); // 池状态账户
        info.put("operation_state", accounts[4]); // 操作状态账户
        info.put("reward_token_mint", accounts[5]); // 奖励代币 mint
        info.put("reward_token_vault", accounts[6]); // 奖励代币金库
        info.put("reward_token_program", accounts[7]); // 奖励代币程序账户
        info.put("system_program", accounts[8]); // 系统程序账户
        info.put("rent", accounts[9]); // 租金账户
        return info;
    }

    private static Map<String, Object> parseOpenPosition(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        info.put("tick_lower_index", Long.toUnsignedString(buffer.getInt()));
        info.put("tick_upper_index", Long.toUnsignedString(buffer.getInt()));
        info.put("tick_array_lower_start_index", Integer.toUnsignedString(buffer.getInt()));
        info.put("tick_array_upper_start_index", Integer.toUnsignedString(buffer.getInt()));

        String low = Long.toUnsignedString(buffer.getLong());  // 读取低位
        String high = Long.toUnsignedString(buffer.getLong()); // 读取高位
        BigInteger liquidity = new BigInteger(high).shiftLeft(64).or(new BigInteger(low));
        info.put("liquidity", liquidity.toString());
        info.put("amount_0_max", Long.toUnsignedString(buffer.getLong()));
        info.put("amount_1_max", Long.toUnsignedString(buffer.getLong()));

//        info.put("with_metadata", Byte.toUnsignedInt(buffer.get()));
//        if (buffer.limit() - buffer.position() < 2) {
//            info.put("base_flag", 0);
//        } else {
//            buffer.get();
//            info.put("base_flag", Byte.toUnsignedInt(buffer.get()));
//        }

        // 账户信息
        info.put("payer", accounts[0]); // 支付者
        info.put("position_nft_owner", accounts[1]); // 位置 NFT 拥有者
        info.put("position_nft_mint", accounts[2]); // 位置 NFT mint 账户
        info.put("position_nft_account", accounts[3]); // 位置 NFT 账户
        info.put("metadata_account", accounts[4]); // 元数据账户
        info.put("pool_state", accounts[5]); // 池状态账户
        info.put("protocol_position", accounts[6]); // 协议位置账户
        info.put("tick_array_lower", accounts[7]); // 下限 tick 数组账户
        info.put("tick_array_upper", accounts[8]); // 上限 tick 数组账户
        info.put("personal_position", accounts[9]); // 个人位置状态账户
        info.put("token_account_0", accounts[10]); // 代币0账户
        info.put("token_account_1", accounts[11]); // 代币1账户
        info.put("token_vault_0", accounts[12]); // 代币0金库账户
        info.put("token_vault_1", accounts[13]); // 代币1金库账户
        info.put("rent", accounts[14]); // 租金账户
        info.put("system_program", accounts[15]); // 系统程序账户
        info.put("token_program", accounts[16]); // 代币程序账户
        info.put("associated_token_program", accounts[17]); // 关联代币程序账户
        info.put("metadata_program", accounts[18]); // 元数据程序账户

        return info;
    }

    private static Map<String, Object> parseIncreaseLiquidity(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        // 存储解析的字段
        String low = Long.toUnsignedString(buffer.getLong());  // 读取低位
        String high = Long.toUnsignedString(buffer.getLong()); // 读取高位
        BigInteger liquidity = new BigInteger(high).shiftLeft(64).or(new BigInteger(low));
        info.put("liquidity", liquidity.toString());
        info.put("amount_0_max", Long.toUnsignedString(buffer.getLong()));
        info.put("amount_1_max", Long.toUnsignedString(buffer.getLong()));
        if (buffer.limit() - buffer.position() < 2) {
            info.put("base_flag", 0);
        } else {
            buffer.get();
            info.put("base_flag", Byte.toUnsignedInt(buffer.get()));
        }

        // 账户信息
        info.put("nft_owner", accounts[0]); // NFT 拥有者
        info.put("nft_account", accounts[1]); // NFT 账户
        info.put("pool_state", accounts[2]); // 池状态账户
        info.put("protocol_position", accounts[3]); // 协议位置账户
        info.put("personal_position", accounts[4]); // 个人位置状态账户
        info.put("tick_array_lower", accounts[5]); // 下限 tick 数组账户
        info.put("tick_array_upper", accounts[6]); // 上限 tick 数组账户
        info.put("token_account_0", accounts[7]); // 代币0账户
        info.put("token_account_1", accounts[8]); // 代币1账户
        info.put("token_vault_0", accounts[9]); // 代币0金库账户
        info.put("token_vault_1", accounts[10]); // 代币1金库账户
        info.put("token_program", accounts[11]); // 代币程序账户

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
        // 存储解析的字段
        String low = Long.toUnsignedString(buffer.getLong());  // 读取低位
        String high = Long.toUnsignedString(buffer.getLong()); // 读取高位
        BigInteger liquidity = new BigInteger(high).shiftLeft(64).or(new BigInteger(low));
        info.put("liquidity", liquidity.toString());
        info.put("amount_0_min", Long.toUnsignedString(buffer.getLong()));
        info.put("amount_1_min", Long.toUnsignedString(buffer.getLong()));

        // 账户信息
        info.put("nft_owner", accounts[0]); // NFT 拥有者
        info.put("nft_account", accounts[1]); // NFT 账户
        info.put("personal_position", accounts[2]); // 个人位置状态账户
        info.put("pool_state", accounts[3]); // 池状态账户
        info.put("protocol_position", accounts[4]); // 协议位置账户
        info.put("token_vault_0", accounts[5]); // 代币0金库账户
        info.put("token_vault_1", accounts[6]); // 代币1金库账户
        info.put("tick_array_lower", accounts[7]); // 下限 tick 数组账户
        info.put("tick_array_upper", accounts[8]); // 上限 tick 数组账户
        info.put("recipient_token_account_0", accounts[9]); // 接收代币0的账户
        info.put("recipient_token_account_1", accounts[10]); // 接收代币1的账户
        info.put("token_program", accounts[11]); // 代币程序账户
        info.put("vault_0_mint", accounts[12]); // 代币0金库mint账户
        info.put("vault_1_mint", accounts[13]); // 代币1金库mint账户

        return info;
    }

    private static Map<String, Object> parseSwapRouterBaseIn(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        // 存储解析的字段
        info.put("amount_in", Long.toUnsignedString(buffer.getLong()));
        info.put("amount_out_minimum", Long.toUnsignedString(buffer.getLong()));

        // 账户信息
        info.put("payer", accounts[0]); // 用户
        info.put("input_token_account", accounts[1]); // 输入代币账户
        info.put("input_token_mint", accounts[2]); // 输入代币的 mint
        info.put("token_program", accounts[3]); // SPL 代币程序
        info.put("token_program_2022", accounts[4]); // SPL 2022 代币程序
        info.put("memo_program", accounts[5]); // memo 程序账户

        // 其他账户信息
        for (int i = 6; i < accounts.length; i++) {
            info.put("remaining_account_" + (i - 6), accounts[i]); // 剩余账户
        }
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

        // 存储解析的字段
        info.put("reward_index", Byte.toUnsignedInt(buffer.get()));
        String low = Long.toUnsignedString(buffer.getLong());  // 读取低位
        String high = Long.toUnsignedString(buffer.getLong()); // 读取高位
        BigInteger emissionsPerSecondX64 = new BigInteger(high).shiftLeft(64).or(new BigInteger(low));
        info.put("emissions_per_second_x64", emissionsPerSecondX64);
        info.put("open_time", Long.toUnsignedString(buffer.getLong()));
        info.put("end_time", Long.toUnsignedString(buffer.getLong()));

        // 账户信息
        info.put("authority", accounts[0]); // 权限账户
        info.put("amm_config", accounts[1]); // AMM 配置账户
        info.put("pool_state", accounts[2]); // 池状态账户
        info.put("operation_state", accounts[3]); // 操作状态账户
        info.put("token_program", accounts[4]); // 代币程序账户
        info.put("token_program_2022", accounts[5]); // 2022 代币程序账户accounts", parseSetRewardParamsAccounts(accounts));

        return info;
    }

    private static Map<String, Object> parseCollectProtocolFee(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        // 存储解析的字段
        info.put("amount_0_requested", Long.toUnsignedString(buffer.getLong()));
        info.put("amount_1_requested", Long.toUnsignedString(buffer.getLong()));

        // 账户信息
        info.put("owner", accounts[0]); // 权限账户
        info.put("pool_state", accounts[1]); // 池状态账户
        info.put("amm_config", accounts[2]); // AMM 配置账户
        info.put("token_vault_0", accounts[3]); // token_0 金库账户
        info.put("token_vault_1", accounts[4]); // token_1 金库账户
        info.put("vault_0_mint", accounts[5]); // token_0 mint 账户
        info.put("vault_1_mint", accounts[6]); // token_1 mint 账户
        info.put("recipient_token_account_0", accounts[7]); // 接收 token_0 的账户
        info.put("recipient_token_account_1", accounts[8]); // 接收 token_1 的账户
        info.put("token_program", accounts[9]); // SPL 代币程序账户
        info.put("token_program_2022", accounts[10]); // SPL 2022 代币程序账户s(accounts));

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
        // 存储解析的字段
        String low = Long.toUnsignedString(buffer.getLong());  // 读取低位
        String high = Long.toUnsignedString(buffer.getLong()); // 读取高位
        BigInteger liquidity = new BigInteger(high).shiftLeft(64).or(new BigInteger(low));
        info.put("liquidity", liquidity.toString());
        info.put("amount_0_min", Long.toUnsignedString(buffer.getLong()));
        info.put("amount_1_min", Long.toUnsignedString(buffer.getLong()));

        // 账户信息
        info.put("nft_owner", accounts[0]); // NFT 拥有者
        info.put("nft_account", accounts[1]); // NFT 账户
        info.put("personal_position", accounts[2]); // 个人位置状态账户
        info.put("pool_state", accounts[3]); // 池状态账户
        info.put("protocol_position", accounts[4]); // 协议位置账户
        info.put("token_vault_0", accounts[5]); // 代币0金库账户
        info.put("token_vault_1", accounts[6]); // 代币1金库账户
        info.put("tick_array_lower", accounts[7]); // 下限 tick 数组账户
        info.put("tick_array_upper", accounts[8]); // 上限 tick 数组账户
        info.put("recipient_token_account_0", accounts[9]); // 接收代币0的账户
        info.put("recipient_token_account_1", accounts[10]); // 接收代币1的账户
        info.put("token_program", accounts[11]); // 代币程序账户
        info.put("token_program_2022", accounts[12]); // 代币程序2022账户
        info.put("memo_program", accounts[13]); // memo 程序账户
        info.put("vault_0_mint", accounts[14]); // 代币0金库mint账户
        info.put("vault_1_mint", accounts[15]); // 代币1金库mint账户

        return info;
    }

    private static Map<String, Object> parseIncreaseLiquidityV2(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        // 存储解析的字段
        String low = Long.toUnsignedString(buffer.getLong());  // 读取低位
        String high = Long.toUnsignedString(buffer.getLong()); // 读取高位
        BigInteger liquidity = new BigInteger(high).shiftLeft(64).or(new BigInteger(low));
        info.put("liquidity", liquidity.toString());
        info.put("amount_0_max", Long.toUnsignedString(buffer.getLong()));
        info.put("amount_1_max", Long.toUnsignedString(buffer.getLong()));
        if (buffer.limit() - buffer.position() < 2) {
            info.put("base_flag", 0);
        } else {
            buffer.get();
            info.put("base_flag", Byte.toUnsignedInt(buffer.get()));
        }

        // 账户信息
        info.put("nft_owner", accounts[0]); // NFT 拥有者
        info.put("nft_account", accounts[1]); // NFT 账户
        info.put("pool_state", accounts[2]); // 池状态账户
        info.put("protocol_position", accounts[3]); // 协议位置账户
        info.put("personal_position", accounts[4]); // 个人位置状态账户
        info.put("tick_array_lower", accounts[5]); // 下限 tick 数组账户
        info.put("tick_array_upper", accounts[6]); // 上限 tick 数组账户
        info.put("token_account_0", accounts[7]); // 代币0账户
        info.put("token_account_1", accounts[8]); // 代币1账户
        info.put("token_vault_0", accounts[9]); // 代币0金库账户
        info.put("token_vault_1", accounts[10]); // 代币1金库账户
        info.put("token_program", accounts[11]); // 代币程序账户
        info.put("token_program_2022", accounts[12]); // 代币程序2022账户
        info.put("vault_0_mint", accounts[13]); // 代币0金库mint账户
        info.put("vault_1_mint", accounts[14]); // 代币1金库mint账户
        return info;
    }

    private static Map<String, Object> parseOpenPositionV2(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        info.put("tick_lower_index", Long.toUnsignedString(buffer.getInt()));
        info.put("tick_upper_index", Long.toUnsignedString(buffer.getInt()));
        info.put("tick_array_lower_start_index", Integer.toUnsignedString(buffer.getInt()));
        info.put("tick_array_upper_start_index", Integer.toUnsignedString(buffer.getInt()));

        String low = Long.toUnsignedString(buffer.getLong());  // 读取低位
        String high = Long.toUnsignedString(buffer.getLong()); // 读取高位
        BigInteger liquidity = new BigInteger(high).shiftLeft(64).or(new BigInteger(low));
        info.put("liquidity", liquidity.toString());
        info.put("amount_0_max", Long.toUnsignedString(buffer.getLong()));
        info.put("amount_1_max", Long.toUnsignedString(buffer.getLong()));

        info.put("with_metadata", Byte.toUnsignedInt(buffer.get()));
        if (buffer.limit() - buffer.position() < 2) {
            info.put("base_flag", 0);
        } else {
            buffer.get();
            info.put("base_flag", Byte.toUnsignedInt(buffer.get()));
        }

        info.put("payer", accounts[0]); // 支付者
        info.put("position_nft_owner", accounts[1]); // 位置 NFT 拥有者
        info.put("position_nft_mint", accounts[2]); // 位置 NFT mint 账户
        info.put("position_nft_account", accounts[3]); // 位置 NFT 账户
        info.put("metadata_account", accounts[4]); // 元数据账户
        info.put("pool_state", accounts[5]); // 池状态账户
        info.put("protocol_position", accounts[6]); // 协议位置账户
        info.put("tick_array_lower", accounts[7]); // 下限 tick 数组账户
        info.put("tick_array_upper", accounts[8]); // 上限 tick 数组账户
        info.put("personal_position", accounts[9]); // 个人位置状态账户
        info.put("token_account_0", accounts[10]); // 代币0账户
        info.put("token_account_1", accounts[11]); // 代币1账户
        info.put("token_vault_0", accounts[12]); // 代币0金库账户
        info.put("token_vault_1", accounts[13]); // 代币1金库账户
        info.put("rent", accounts[14]); // 租金账户
        info.put("system_program", accounts[15]); // 系统程序账户
        info.put("token_program", accounts[16]); // 代币程序账户
        info.put("associated_token_program", accounts[17]); // 关联代币程序账户
        info.put("metadata_program", accounts[18]); // 元数据程序账户
        info.put("token_program_2022", accounts[19]); // 代币程序2022账户
        info.put("vault_0_mint", accounts[20]); // 代币0金库mint账户
        info.put("vault_1_mint", accounts[21]); // 代币1金库mint账户
        return info;
    }

    private static Map<String, Object> parseOpenPositionWithToken22Nft(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        info.put("tick_lower_index", Long.toUnsignedString(buffer.getInt()));
        info.put("tick_upper_index", Long.toUnsignedString(buffer.getInt()));
        info.put("tick_array_lower_start_index", Integer.toUnsignedString(buffer.getInt()));
        info.put("tick_array_upper_start_index", Integer.toUnsignedString(buffer.getInt()));

        String low = Long.toUnsignedString(buffer.getLong());  // 读取低位
        String high = Long.toUnsignedString(buffer.getLong()); // 读取高位
        BigInteger liquidity = new BigInteger(high).shiftLeft(64).or(new BigInteger(low));
        info.put("liquidity", liquidity.toString());
        info.put("amount_0_max", Long.toUnsignedString(buffer.getLong()));
        info.put("amount_1_max", Long.toUnsignedString(buffer.getLong()));

        info.put("with_metadata", Byte.toUnsignedInt(buffer.get()));
        if (buffer.limit() - buffer.position() < 2) {
            info.put("base_flag", 0);
        } else {
            buffer.get();
            info.put("base_flag", Byte.toUnsignedInt(buffer.get()));
        }

        // 账户信息
        info.put("payer", accounts[0]); // 支付者
        info.put("position_nft_owner", accounts[1]); // 位置 NFT 拥有者
        info.put("position_nft_mint", accounts[2]); // 位置 NFT mint
        info.put("position_nft_account", accounts[3]); // 位置 NFT 账户
        info.put("pool_state", accounts[4]); // 池状态账户
        info.put("protocol_position", accounts[5]); // 协议位置账户
        info.put("tick_array_lower", accounts[6]); // 下限 tick 数组账户
        info.put("tick_array_upper", accounts[7]); // 上限 tick 数组账户
        info.put("personal_position", accounts[8]); // 个人位置账户
        info.put("token_account_0", accounts[9]); // token_0 账户
        info.put("token_account_1", accounts[10]); // token_1 账户
        info.put("token_vault_0", accounts[11]); // token_0 金库账户
        info.put("token_vault_1", accounts[12]); // token_1 金库账户
        info.put("rent", accounts[13]); // 租金账户
        info.put("system_program", accounts[14]); // 系统程序账户
        info.put("token_program", accounts[15]); // 代币程序账户
        info.put("associated_token_program", accounts[16]); // 关联代币程序账户
        info.put("token_program_2022", accounts[17]); // 2022 代币程序账户
        info.put("vault_0_mint", accounts[18]); // token vault 0 mint
        info.put("vault_1_mint", accounts[19]); // token vault 1 mint
        return info;
    }

    private static Map<String, Object> parseClosePosition(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 账户信息
        info.put("nft_owner", accounts[0]); // NFT 拥有者
        info.put("position_nft_mint", accounts[1]); // 位置 NFT mint 账户
        info.put("position_nft_account", accounts[2]); // 位置 NFT 账户
        info.put("personal_position", accounts[3]); // 个人位置状态账户
        info.put("system_program", accounts[4]); // 系统程序账户
        info.put("token_program", accounts[5]); // 代币程序账户
        return info;
    }

    private static Map<String, Object> parseCollectFundFee(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        info.put("amount_0_requested", Long.toUnsignedString(buffer.getLong()));
        info.put("amount_1_requested", Long.toUnsignedString(buffer.getLong()));

        // 账户信息
        info.put("owner", accounts[0]); // 权限账户
        info.put("pool_state", accounts[1]); // 池状态账户
        info.put("amm_config", accounts[2]); // AMM 配置账户
        info.put("token_vault_0", accounts[3]); // token_0 金库账户
        info.put("token_vault_1", accounts[4]); // token_1 金库账户
        info.put("vault_0_mint", accounts[5]); // token_0 mint 账户
        info.put("vault_1_mint", accounts[6]); // token_1 mint 账户
        info.put("recipient_token_account_0", accounts[7]); // 接收 token_0 的账户
        info.put("recipient_token_account_1", accounts[8]); // 接收 token_1 的账户
        info.put("token_program", accounts[9]); // SPL 代币程序账户
        info.put("token_program_2022", accounts[10]); // SPL 2022 代币程序账户s(accounts));

        return info;
    }

    private static Map<String, Object> parseUpdateRewardInfos(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        info.put("pool_state", accounts[0]); // 流动性池状态账户
        return info;
    }

    private static Map<String, Object> parseTransferRewardOwner(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        byte[] newOwnerBytes = new byte[32]; // 假设 Pubkey 是 32 字节
        buffer.get(newOwnerBytes); // 读取新的所有者地址

        // 存储解析的字段
        info.put("new_owner", Base58.encode(newOwnerBytes));

        // 账户信息
        info.put("authority", accounts[0]); // 权限账户
        info.put("pool_state", accounts[1]); // 池状态账户
        return info;
    }

    private static Map<String, Object> parseCollectRemainingRewards(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        // 存储解析的字段
        info.put("reward_index", Byte.toUnsignedInt(buffer.get()));

        // 账户信息
        info.put("reward_funder", accounts[0]); // 奖励资金提供者
        info.put("funder_token_account", accounts[1]); // 资金提供者的奖励代币账户
        info.put("pool_state", accounts[2]); // 池状态账户
        info.put("reward_token_vault", accounts[3]); // 奖励代币金库
        info.put("reward_vault_mint", accounts[4]); // 奖励代币金库的 mint
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