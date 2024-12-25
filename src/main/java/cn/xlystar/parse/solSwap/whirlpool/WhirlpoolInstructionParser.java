package cn.xlystar.parse.solSwap.whirlpool;

import cn.xlystar.parse.solSwap.InstructionParser;
import org.bitcoinj.core.Base58;
import org.bouncycastle.util.encoders.Hex;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class WhirlpoolInstructionParser extends InstructionParser {

    @Override
    public String getMethodId(ByteBuffer buffer) {
        byte[] discriminatorBytes = new byte[8];
        buffer.get(discriminatorBytes);
        return Hex.toHexString(discriminatorBytes);
    }

    @Override
    public Map<String, Object> matchInstruction(String methodId, ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info;
        switch (WhirlpoolInstruction.fromValue(methodId)) {
            case INITIALIZE_CONFIG: // 0
                info = parseInitializeConfig(buffer, accounts);
                break;
            case INITIALIZE_POOL: // 1
                info = parseInitializePool(buffer, accounts);
                break;
            case INITIALIZE_TICK_ARRAY: // 2
                info = parseInitializeTickArray(buffer, accounts);
                break;
            case INITIALIZE_FEE_TIER: // 3
                info = parseInitializeFeeTier(buffer, accounts);
                break;
            case INITIALIZE_REWARD: // 4
                info = parseInitializeReward(buffer, accounts);
                break;
            case SET_REWARD_EMISSIONS: // 5
                info = parseSetRewardEmissions(buffer, accounts);
                break;
            case OPEN_POSITION: // 6
                info = parseOpenPosition(buffer, accounts);
                break;
            case OPEN_POSITION_WITH_METADATA: // 7
                info = parseOpenPositionWithMetadata(buffer, accounts);
                break;
            case INCREASE_LIQUIDITY: // 8
                info = parseIncreaseLiquidity(buffer, accounts);
                break;
            case DECREASE_LIQUIDITY: // 9
                info = parseDecreaseLiquidity(buffer, accounts);
                break;
            case UPDATE_FEES_AND_REWARDS: // 10
                info = parseUpdateFeesAndRewards(buffer, accounts);
                break;
            case COLLECT_FEES: // 11
                info = parseCollectFees(buffer, accounts);
                break;
            case COLLECT_REWARD: // 12
                info = parseCollectReward(buffer, accounts);
                break;
            case COLLECT_PROTOCOL_FEES: // 13
                info = parseCollectProtocolFees(buffer, accounts);
                break;
            case SWAP: // 14
                info = parseSwap(buffer, accounts);
                break;
            case CLOSE_POSITION: // 15
                info = parseClosePosition(buffer, accounts);
                break;
            case SET_DEFAULT_FEE_RATE: // 16
                info = parseSetDefaultFeeRate(buffer, accounts);
                break;
            case SET_DEFAULT_PROTOCOL_FEE_RATE: // 17
                info = parseSetDefaultProtocolFeeRate(buffer, accounts);
                break;
            case SET_FEE_RATE: // 18
                info = parseSetFeeRate(buffer, accounts);
                break;
            case SET_PROTOCOL_FEE_RATE: // 19
                info = parseSetProtocolFeeRate(buffer, accounts);
                break;
            case SET_FEE_AUTHORITY: // 20
                info = parseSetFeeAuthority(buffer, accounts);
                break;
            case SET_COLLECT_PROTOCOL_FEES_AUTHORITY: // 21
                info = parseSetCollectProtocolFeesAuthority(buffer, accounts);
                break;
            case SET_REWARD_AUTHORITY: // 22
                info = parseSetRewardAuthority(buffer, accounts);
                break;
            case SET_REWARD_AUTHORITY_BY_SUPER_AUTHORITY: // 23
                info = parseSetRewardAuthorityBySuperAuthority(buffer, accounts);
                break;
            case SET_REWARD_EMISSIONS_SUPER_AUTHORITY: // 24
                info = parseSetRewardEmissionsSuperAuthority(buffer, accounts);
                break;
            case TWO_HOP_SWAP: // 25
                info = parseTwoHopSwap(buffer, accounts);
                break;
            case INITIALIZE_POSITION_BUNDLE: // 26
                info = parseInitializePositionBundle(buffer, accounts);
                break;
            case INITIALIZE_POSITION_BUNDLE_WITH_METADATA: // 27
                info = parseInitializePositionBundleWithMetadata(buffer, accounts);
                break;
            case DELETE_POSITION_BUNDLE: // 28
                info = parseDeletePositionBundle(buffer, accounts);
                break;
            case OPEN_BUNDLED_POSITION: // 29
                info = parseOpenBundledPosition(buffer, accounts);
                break;
            case CLOSE_BUNDLED_POSITION: // 30
                info = parseCloseBundledPosition(buffer, accounts);
                break;
            case OPEN_POSITION_WITH_TOKEN_EXTENSIONS: // 31
                info = parseOpenPositionWithTokenExtensions(buffer, accounts);
                break;
            case CLOSE_POSITION_WITH_TOKEN_EXTENSIONS: // 32
                info = parseClosePositionWithTokenExtensions(buffer, accounts);
                break;
            case COLLECT_FEES_V2: // 33
                info = parseCollectFeesV2(buffer, accounts);
                break;
            case COLLECT_PROTOCOL_FEES_V2: // 34
                info = parseCollectProtocolFeesV2(buffer, accounts);
                break;
            case COLLECT_REWARD_V2: // 35
                info = parseCollectRewardV2(buffer, accounts);
                break;
            case DECREASE_LIQUIDITY_V2: // 36
                info = parseDecreaseLiquidityV2(buffer, accounts);
                break;
            case INCREASE_LIQUIDITY_V2: // 37
                info = parseIncreaseLiquidityV2(buffer, accounts);
                break;
            case INITIALIZE_POOL_V2: // 38
                info = parseInitializePoolV2(buffer, accounts);
                break;
            case INITIALIZE_REWARD_V2: // 39
                info = parseInitializeRewardV2(buffer, accounts);
                break;
            case SET_REWARD_EMISSIONS_V2: // 40
                info = parseSetRewardEmissionsV2(buffer, accounts);
                break;
            case SWAP_V2: // 41
                info = parseSwapV2(buffer, accounts);
                break;
            case TWO_HOP_SWAP_V2: // 42
                info = parseTwoHopSwapV2(buffer, accounts);
                break;
            case INITIALIZE_CONFIG_EXTENSION: // 43
                info = parseInitializeConfigExtension(buffer, accounts);
                break;
            case SET_CONFIG_EXTENSION_AUTHORITY: // 44
                info = parseSetConfigExtensionAuthority(buffer, accounts);
                break;
            case SET_TOKEN_BADGE_AUTHORITY: // 45
                info = parseSetTokenBadgeAuthority(buffer, accounts);
                break;
            case INITIALIZE_TOKEN_BADGE: // 46
                info = parseInitializeTokenBadge(buffer, accounts);
                break;
            case DELETE_TOKEN_BADGE: // 47
                info = parseDeleteTokenBadge(buffer, accounts);
                break;
            default:
                return new HashMap<>();
        }
        return info;
    }

    private static Map<String, Object> parseSwap(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 解析参数
        info.put("amount", Long.toUnsignedString(buffer.getLong()));
        info.put("otherAmountThreshold", Long.toUnsignedString(buffer.getLong()));
        String low = Long.toUnsignedString(buffer.getLong());  // 读取低位
        String high = Long.toUnsignedString(buffer.getLong()); // 读取高位
        BigInteger sqrtPriceLimit = new BigInteger(high).shiftLeft(64).or(new BigInteger(low));
        info.put("sqrtPriceLimit", sqrtPriceLimit.toString());
        info.put("amountSpecifiedIsInput", buffer.get());
        info.put("aToB", buffer.get());

        // 解析账户
        info.put("whirlpool", accounts[0]);          // 池子账户
        info.put("tokenProgram", accounts[1]);       // SPL Token程序
        info.put("tokenAuthority", accounts[2]);     // 代币授权账户
        info.put("tokenOwnerAccountA", accounts[3]); // A代币所有者账户
        info.put("tokenVaultA", accounts[4]);        // A代币金库
        info.put("tokenOwnerAccountB", accounts[5]); // B代币所有者账户
        info.put("tokenVaultB", accounts[6]);        // B代币金库
        info.put("tickArray0", accounts[7]);         // 价格刻度数组0
        info.put("tickArray1", accounts[8]);         // 价格刻度数组1
        info.put("tickArray2", accounts[9]);         // 价格刻度数组2
        info.put("oracle", accounts[10]);            // 预言机账户

        return info;
    }

    private static Map<String, Object> parseOpenPosition(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 解析参数
        info.put("bumps", Byte.toUnsignedInt(buffer.get()));
        info.put("tickLowerIndex", buffer.getInt());
        info.put("tickUpperIndex", buffer.getInt());

        // 账户信息
        info.put("funder", accounts[0]); // 资金提供者账户
        info.put("owner", accounts[1]); // 位置拥有者账户
        info.put("position", accounts[2]); // 位置账户
        info.put("position_mint", accounts[3]); // 位置 mint 账户
        info.put("position_token_account", accounts[4]); // 位置代币账户
        info.put("whirlpool", accounts[5]); // whirlpool 账户
        info.put("token_program", accounts[6]); // 代币程序账户
        info.put("system_program", accounts[7]); // 系统程序账户
        info.put("rent", accounts[8]); // 租金账户
        info.put("associated_token_program", accounts[9]); // 关联代币程序账户

        return info;
    }

    private static Map<String, Object> parseIncreaseLiquidity(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 解析参数
        String low = Long.toUnsignedString(buffer.getLong());  // 读取低位
        String high = Long.toUnsignedString(buffer.getLong()); // 读取高位
        BigInteger liquidityAmount = new BigInteger(high).shiftLeft(64).or(new BigInteger(low));
        info.put("liquidityAmount", liquidityAmount.toString());
        info.put("tokenMaxA", Long.toUnsignedString(buffer.getLong()));
        info.put("tokenMaxB", Long.toUnsignedString(buffer.getLong()));

        // 账户信息
        info.put("whirlpool", accounts[0]); // 流动性池账户
        info.put("token_program", accounts[1]); // 代币程序账户
        info.put("position_authority", accounts[2]); // 位置授权账户
        info.put("position", accounts[3]); // 位置账户
        info.put("position_token_account", accounts[4]); // 位置代币账户
        info.put("token_owner_account_a", accounts[5]); // 代币 A 拥有者账户
        info.put("token_owner_account_b", accounts[6]); // 代币 B 拥有者账户
        info.put("token_vault_a", accounts[7]); // 代币 A 金库账户
        info.put("token_vault_b", accounts[8]); // 代币 B 金库账户
        info.put("tick_array_lower", accounts[9]); // 下限 tick 数组账户
        info.put("tick_array_upper", accounts[10]); // 上限 tick 数组账户

        return info;
    }

    private static Map<String, Object> parseDecreaseLiquidity(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 解析参数
        String low = Long.toUnsignedString(buffer.getLong());  // 读取低位
        String high = Long.toUnsignedString(buffer.getLong()); // 读取高位
        BigInteger liquidityAmount = new BigInteger(high).shiftLeft(64).or(new BigInteger(low));
        info.put("liquidityAmount", liquidityAmount.toString());
        info.put("tokenMinA", Long.toUnsignedString(buffer.getLong()));
        info.put("tokenMinB", Long.toUnsignedString(buffer.getLong()));

        // 账户信息
        info.put("whirlpool", accounts[0]); // 流动性池账户
        info.put("token_program", accounts[1]); // 代币程序账户
        info.put("position_authority", accounts[2]); // 位置授权账户
        info.put("position", accounts[3]); // 位置账户
        info.put("position_token_account", accounts[4]); // 位置代币账户
        info.put("token_vault_a", accounts[5]); // 代币 A 金库账户
        info.put("token_vault_b", accounts[6]); // 代币 B 金库账户
        info.put("token_owner_account_a", accounts[7]); // 代币 A 拥有者账户
        info.put("token_owner_account_b", accounts[8]); // 代币 B 拥有者账户
        info.put("tick_array_lower", accounts[9]); // 下限 tick 数组账户
        info.put("tick_array_upper", accounts[10]); // 上限 tick 数组账户
        return info;
    }

    private static Map<String, Object> parseCollectFees(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 账户信息
        info.put("whirlpool", accounts[0]); // whirlpool 账户
        info.put("position_authority", accounts[1]); // 位置授权账户
        info.put("position", accounts[2]); // 位置账户
        info.put("position_token_account", accounts[3]); // 位置代币账户
        info.put("token_owner_account_a", accounts[4]); // 代币 A 拥有者账户
        info.put("token_vault_a", accounts[5]); // 代币 A 金库账户
        info.put("token_owner_account_b", accounts[6]); // 代币 B 拥有者账户
        info.put("token_vault_b", accounts[7]); // 代币 B 金库账户
        info.put("token_program", accounts[8]); // 代币程序账户

        return info;
    }

    private static Map<String, Object> parseInitializeConfig(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 解析参数
        byte[] feeAuthority = new byte[32];
        buffer.get(feeAuthority);
        byte[] collectProtocolFeesAuthority = new byte[32];
        buffer.get(collectProtocolFeesAuthority);
        byte[] rewardEmissionsSuperAuthority = new byte[32];
        buffer.get(rewardEmissionsSuperAuthority);

        info.put("fee_authority", Base58.encode(feeAuthority));
        info.put("collect_protocol_fees_authority", Base58.encode(collectProtocolFeesAuthority));
        info.put("reward_emissions_super_authority", Base58.encode(rewardEmissionsSuperAuthority));
        info.put("default_protocol_fee_rate", Short.toUnsignedInt(buffer.getShort()));

        // 账户信息
        info.put("config", accounts[0]); // 配置账户
        info.put("funder", accounts[1]); // 资金提供者账户
        info.put("system_program", accounts[2]); // 系统程序账户

        return info;
    }

    private static Map<String, Object> parseInitializePool(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 解析参数
        int bumps = Byte.toUnsignedInt(buffer.get());
        info.put("bumps", bumps);
        int tickSpacing = Short.toUnsignedInt(buffer.getShort());
        info.put("tick_spacing", tickSpacing);
        String oneLow = Long.toUnsignedString(buffer.getLong());  // 读取低位
        String oneHigh = Long.toUnsignedString(buffer.getLong()); // 读取高位
        BigInteger initialSqrtPrice = new BigInteger(oneHigh).shiftLeft(64).or(new BigInteger(oneLow));
        info.put("initial_sqrt_price", initialSqrtPrice);

        // 解析账户
        info.put("whirlpools_config", accounts[0]); // whirlpools 配置账户
        info.put("token_mint_a", accounts[1]); // 代币 A 的 mint 账户
        info.put("token_mint_b", accounts[2]); // 代币 B 的 mint 账户
        info.put("funder", accounts[3]); // 资金提供者账户
        info.put("whirlpool", accounts[4]); // whirlpool 账户
        info.put("token_vault_a", accounts[5]); // 代币 A 金库账户
        info.put("token_vault_b", accounts[6]); // 代币 B 金库账户
        info.put("fee_tier", accounts[7]); // 费用等级账户
        info.put("token_program", accounts[8]); // 代币程序账户
        info.put("system_program", accounts[9]); // 系统程序账户
        info.put("rent", accounts[10]); // 租金账户
        return info;
    }

    private static Map<String, Object> parseInitializeTickArray(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 存储解析的字段
        info.put("start_tick_index", buffer.getInt());

        // 账户信息
        info.put("whirlpool", accounts[0]); // whirlpool 账户
        info.put("funder", accounts[1]); // 资金提供者账户
        info.put("tick_array", accounts[2]); // tick 数组账户
        info.put("system_program", accounts[3]); // 系统程序账户

        return info;
    }

    private static Map<String, Object> parseInitializeFeeTier(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 解析参数
        info.put("tickSpacing", Short.toUnsignedInt(buffer.getShort()));
        info.put("defaultFeeRate", Short.toUnsignedInt(buffer.getShort()));

        // 账户信息
        info.put("config", accounts[0]); // 配置账户
        info.put("fee_tier", accounts[1]); // 费用层账户
        info.put("funder", accounts[2]); // 资金提供者账户
        info.put("fee_authority", accounts[3]); // 费用授权账户
        info.put("system_program", accounts[4]); // 系统程序账户

        return info;
    }

    private static Map<String, Object> parseInitializeReward(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 解析参数
        info.put("reward_index", Byte.toUnsignedInt(buffer.get()));

        // 账户信息
        info.put("reward_authority", accounts[0]); // 奖励授权账户
        info.put("funder", accounts[1]); // 资金提供者账户
        info.put("whirlpool", accounts[2]); // whirlpool 账户
        info.put("reward_mint", accounts[3]); // 奖励 mint 账户
        info.put("reward_vault", accounts[4]); // 奖励金库账户
        info.put("token_program", accounts[5]); // 代币程序账户
        info.put("system_program", accounts[6]); // 系统程序账户
        info.put("rent", accounts[7]); // 租金账户

        return info;
    }

    private static Map<String, Object> parseSetRewardEmissions(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 存储解析的字段
        info.put("reward_index", Byte.toUnsignedInt(buffer.get()));
        String low = Long.toUnsignedString(buffer.getLong());  // 读取低位
        String high = Long.toUnsignedString(buffer.getLong()); // 读取高位
        BigInteger emissionsPerSecondX64 = new BigInteger(high).shiftLeft(64).or(new BigInteger(low));
        info.put("emissions_per_second_x64", emissionsPerSecondX64.toString());

        // 账户信息
        info.put("whirlpool", accounts[0]); // whirlpool 账户
        info.put("reward_authority", accounts[1]); // 奖励授权账户
        info.put("reward_vault", accounts[2]); // 奖励金库账户
        return info;
    }

    private static Map<String, Object> parseCollectReward(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 存储解析的字段
        info.put("reward_index", Byte.toUnsignedInt(buffer.get()));

        // 账户信息
        info.put("whirlpool", accounts[0]); // whirlpool 账户
        info.put("position_authority", accounts[1]); // 位置授权账户
        info.put("position", accounts[2]); // 位置账户
        info.put("position_token_account", accounts[3]); // 位置代币账户
        info.put("reward_owner_account", accounts[4]); // 奖励拥有者账户
        info.put("reward_vault", accounts[5]); // 奖励金库账户
        info.put("token_program", accounts[6]); // 代币程序账户

        return info;
    }

    private static Map<String, Object> parseCollectProtocolFees(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 账户信息
        info.put("whirlpools_config", accounts[0]); // whirlpools 配置账户
        info.put("whirlpool", accounts[1]); // whirlpool 账户
        info.put("collect_protocol_fees_authority", accounts[2]); // 收集协议费用授权账户
        info.put("token_vault_a", accounts[3]); // 代币 A 金库账户
        info.put("token_vault_b", accounts[4]); // 代币 B 金库账户
        info.put("token_destination_a", accounts[5]); // 代币 A 目标账户
        info.put("token_destination_b", accounts[6]); // 代币 B 目标账户
        info.put("token_program", accounts[7]); // 代币程序账户
        return info;
    }

    private static Map<String, Object> parseUpdateFeesAndRewards(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 解析账户
        info.put("whirlpool", accounts[0]); // whirlpool 账户
        info.put("position", accounts[1]); // 位置账户
        info.put("tick_array_lower", accounts[2]); // 下限 tick 数组账户
        info.put("tick_array_upper", accounts[3]); // 上限 tick 数组账户
        return info;
    }

    private static Map<String, Object> parseSetDefaultFeeRate(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 存储解析的字段
        info.put("default_fee_rate", Short.toUnsignedInt(buffer.getShort()));

        // 账户信息
        info.put("whirlpools_config", accounts[0]); // whirlpools 配置账户
        info.put("fee_tier", accounts[1]); // 费用层账户
        info.put("fee_authority", accounts[2]); // 费用授权账户

        return info;
    }

    private static Map<String, Object> parseSetDefaultProtocolFeeRate(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 解析参数
        int defaultProtocolFeeRate = buffer.getShort() & 0xFFFF;
        info.put("defaultProtocolFeeRate", defaultProtocolFeeRate);

        // 解析账户
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 2) {
            accountMap.put("whirlpoolsConfig", accounts[0]);
            accountMap.put("feeAuthority", accounts[1]);
        }
        info.put("accounts", accountMap);

        return info;
    }

    private static Map<String, Object> parseSetFeeAuthority(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 解析账户
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 3) {
            accountMap.put("whirlpoolsConfig", accounts[0]);
            accountMap.put("feeAuthority", accounts[1]);
            accountMap.put("newFeeAuthority", accounts[2]);
        }
        info.put("accounts", accountMap);

        return info;
    }

    private static Map<String, Object> parseSetCollectProtocolFeesAuthority(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 解析账户
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 3) {
            accountMap.put("whirlpoolsConfig", accounts[0]);
            accountMap.put("collectProtocolFeesAuthority", accounts[1]);
            accountMap.put("newCollectProtocolFeesAuthority", accounts[2]);
        }
        info.put("accounts", accountMap);

        return info;
    }

    private static Map<String, Object> parseSetRewardAuthority(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 解析参数
        info.put("rewardIndex", Byte.toUnsignedInt(buffer.get()));

        // 账户信息
        info.put("whirlpool", accounts[0]); // whirlpool 账户
        info.put("reward_authority", accounts[1]); // 奖励授权账户
        info.put("new_reward_authority", accounts[2]); // 新的奖励授权账户
        return info;
    }

    private static Map<String, Object> parseSwapV2(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        info.put("amount", Long.toUnsignedString(buffer.getLong()));
        info.put("otherAmountThreshold", Long.toUnsignedString(buffer.getLong()));
        String low = Long.toUnsignedString(buffer.getLong());  // 读取低位
        String high = Long.toUnsignedString(buffer.getLong()); // 读取高位
        BigInteger sqrtPriceLimit = new BigInteger(high).shiftLeft(64).or(new BigInteger(low));
        info.put("sqrtPriceLimit", sqrtPriceLimit.toString());
        info.put("amountSpecifiedIsInput", buffer.get());
        info.put("aToB", buffer.get());
        if (buffer.get() == 0) {
            info.put("remainingAccountsInfo", 0);
        } else {
            info.put("remainingAccountsInfo", buffer.get());
        }


        // 解析账户
        info.put("tokenProgramA", accounts[0]);
        info.put("tokenProgramB", accounts[1]);
        info.put("memoProgram", accounts[2]);
        info.put("tokenAuthority", accounts[3]);
        info.put("whirlpool", accounts[4]); // 代币 A 的 mint 账户
        info.put("tokenMintA", accounts[5]); // 代币 A 的 mint 账户
        info.put("tokenMintB", accounts[6]); // 代币 B 的 mint 账户
        info.put("tokenOwnerAccountA", accounts[7]);
        info.put("tokenVaultA", accounts[8]);
        info.put("tokenOwnerAccountB", accounts[9]);
        info.put("tokenVaultB", accounts[10]);
        info.put("tickArray0", accounts[11]);
        info.put("tickArray1", accounts[12]);
        info.put("tickArray2", accounts[13]);
        info.put("oracle", accounts[14]);

        return info;
    }

    private static Map<String, Object> parseTwoHopSwapV2(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 解析参数
        info.put("amount", Long.toUnsignedString(buffer.getLong()));
        info.put("otherAmountThreshold", Long.toUnsignedString(buffer.getLong()));
        info.put("amountSpecifiedIsInput", Byte.toUnsignedInt(buffer.get()));
        info.put("aToBOne", Byte.toUnsignedInt(buffer.get()));
        info.put("aToBTwo", Byte.toUnsignedInt(buffer.get()));
        String oneLow = Long.toUnsignedString(buffer.getLong());  // 读取低位
        String oneHigh = Long.toUnsignedString(buffer.getLong()); // 读取高位
        BigInteger sqrtPriceLimitOne = new BigInteger(oneHigh).shiftLeft(64).or(new BigInteger(oneLow));
        info.put("sqrtPriceLimitOne", sqrtPriceLimitOne);
        String twolow = Long.toUnsignedString(buffer.getLong());  // 读取低位
        String twohigh = Long.toUnsignedString(buffer.getLong()); // 读取高位
        BigInteger sqrtPriceLimitTwo = new BigInteger(twohigh).shiftLeft(64).or(new BigInteger(twolow));
        info.put("sqrtPriceLimitTwo", sqrtPriceLimitTwo);
        if (buffer.get() == 0) {
            info.put("remainingAccountsInfo", 0);
        } else {
            info.put("remainingAccountsInfo", buffer.get());
        }

        // 解析账户
        info.put("whirlpool_one", accounts[0]); // 第一个流动性池账户
        info.put("whirlpool_two", accounts[1]); // 第二个流动性池账户
        info.put("token_mint_input", accounts[2]); // 输入代币的 mint 账户
        info.put("token_mint_intermediate", accounts[3]); // 中间代币的 mint 账户
        info.put("token_mint_output", accounts[4]); // 输出代币的 mint 账户
        info.put("token_program_input", accounts[5]); // 输入代币的程序账户
        info.put("token_program_intermediate", accounts[6]); // 中间代币的程序账户
        info.put("token_program_output", accounts[7]); // 输出代币的程序账户
        info.put("token_owner_account_input", accounts[8]); // 输入代币的拥有者账户
        info.put("token_vault_one_input", accounts[9]); // 第一个流动性池的输入代币金库账户
        info.put("token_vault_one_intermediate", accounts[10]); // 第一个流动性池的中间代币金库账户
        info.put("token_vault_two_intermediate", accounts[11]); // 第二个流动性池的中间代币金库账户
        info.put("token_vault_two_output", accounts[12]); // 第二个流动性池的输出代币金库账户
        info.put("token_owner_account_output", accounts[13]); // 输出代币的拥有者账户
        info.put("token_authority", accounts[14]); // 代币授权账户
        info.put("tick_array_one_0", accounts[15]); // 第一个流动性池的下限 tick 数组账户
        info.put("tick_array_one_1", accounts[16]); // 第一个流动性池的中间 tick 数组账户
        info.put("tick_array_one_2", accounts[17]); // 第一个流动性池的上限 tick 数组账户
        info.put("tick_array_two_0", accounts[18]); // 第二个流动性池的下限 tick 数组账户
        info.put("tick_array_two_1", accounts[19]); // 第二个流动性池的中间 tick 数组账户
        info.put("tick_array_two_2", accounts[20]); // 第二个流动性池的上限 tick 数组账户
        info.put("oracle_one", accounts[21]); // 第一个流动性池的 oracle 账户
        info.put("oracle_two", accounts[22]); // 第二个流动性池的 oracle 账户
        info.put("memo_program", accounts[23]); // memo 程序账户
        return info;
    }

    private static Map<String, Object> parseCollectFeesV2(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        if (buffer.get() == 0) {
            info.put("remainingAccountsInfo", 0);
        } else {
            info.put("remainingAccountsInfo", buffer.get());
        }

        // 解析账户
        info.put("whirlpool", accounts[0]); // whirlpool 账户
        info.put("position_authority", accounts[1]); // 位置授权账户
        info.put("position", accounts[2]); // 位置账户
        info.put("position_token_account", accounts[3]); // 位置代币账户
        info.put("token_mint_a", accounts[4]); // 代币 A mint 账户
        info.put("token_mint_b", accounts[5]); // 代币 B mint 账户
        info.put("token_owner_account_a", accounts[6]); // 代币 A 拥有者账户
        info.put("token_vault_a", accounts[7]); // 代币 A 金库账户
        info.put("token_owner_account_b", accounts[8]); // 代币 B 拥有者账户
        info.put("token_vault_b", accounts[9]); // 代币 B 金库账户
        info.put("token_program_a", accounts[10]); // 代币 A 程序账户
        info.put("token_program_b", accounts[11]); // 代币 B 程序账户
        info.put("memo_program", accounts[12]); // memo 程序账户


        return info;
    }

    private static Map<String, Object> parseInitializePoolV2(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 解析参数
        int tickSpacing = Short.toUnsignedInt(buffer.getShort());
        info.put("tick_spacing", tickSpacing);
        String oneLow = Long.toUnsignedString(buffer.getLong());  // 读取低位
        String oneHigh = Long.toUnsignedString(buffer.getLong()); // 读取高位
        BigInteger initialSqrtPrice = new BigInteger(oneHigh).shiftLeft(64).or(new BigInteger(oneLow));
        info.put("initial_sqrt_price", initialSqrtPrice);

        // 账户信息
        info.put("whirlpools_config", accounts[0]); // whirlpools 配置账户
        info.put("token_mint_a", accounts[1]); // 代币 A 的 mint 账户
        info.put("token_mint_b", accounts[2]); // 代币 B 的 mint 账户
        info.put("token_badge_a", accounts[3]); // 代币 A 的 badge 账户
        info.put("token_badge_b", accounts[4]); // 代币 B 的 badge 账户
        info.put("funder", accounts[5]); // 资金提供者账户
        info.put("whirlpool", accounts[6]); // whirlpool 账户
        info.put("token_vault_a", accounts[7]); // 代币 A 金库账户
        info.put("token_vault_b", accounts[8]); // 代币 B 金库账户
        info.put("fee_tier", accounts[9]); // 费用等级账户
        info.put("token_program_a", accounts[10]); // 代币 A 程序账户
        info.put("token_program_b", accounts[11]); // 代币 B 程序账户
        info.put("system_program", accounts[12]); // 系统程序账户
        info.put("rent", accounts[13]); // 租金账户

        return info;
    }

    private static Map<String, Object> parseInitializeTokenBadge(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 账户信息
        info.put("whirlpools_config", accounts[0]); // whirlpools 配置账户
        info.put("whirlpools_config_extension", accounts[1]); // whirlpools 配置扩展账户
        info.put("token_badge_authority", accounts[2]); // 代币徽章授权账户
        info.put("token_mint", accounts[3]); // 代币 mint 账户
        info.put("token_badge", accounts[4]); // 代币徽章账户
        info.put("funder", accounts[5]); // 资金提供者账户
        info.put("system_program", accounts[6]); // 系统程序账户

        return info;
    }

    private static Map<String, Object> parseDeleteTokenBadge(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 解析账户
        info.put("whirlpools_config", accounts[0]); // whirlpools 配置账户
        info.put("whirlpools_config_extension", accounts[1]); // whirlpools 配置扩展账户
        info.put("token_badge_authority", accounts[2]); // 代币徽章授权账户
        info.put("token_mint", accounts[3]); // 代币 mint 账户
        info.put("token_badge", accounts[4]); // 代币徽章账户
        info.put("receiver", accounts[5]); // 接受账号
        return info;
    }

    private static Map<String, Object> parseSetTokenBadgeAuthority(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 解析账户
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 3) {
            accountMap.put("tokenBadge", accounts[0]);
            accountMap.put("tokenBadgeAuthority", accounts[1]);
            accountMap.put("newTokenBadgeAuthority", accounts[2]);
        }
        info.put("accounts", accountMap);

        return info;
    }

    private static Map<String, Object> parseInitializePositionBundle(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 解析账户
        info.put("position_bundle", accounts[0]); // 位置包账户
        info.put("position_bundle_mint", accounts[1]); // 位置包 mint 账户
        info.put("position_bundle_token_account", accounts[2]); // 位置包代币账户
        info.put("position_bundle_owner", accounts[3]); // 位置包拥有者账户
        info.put("funder", accounts[4]); // 资金提供者账户
        info.put("token_program", accounts[5]); // 代币程序账户
        info.put("system_program", accounts[6]); // 系统程序账户
        info.put("rent", accounts[7]); // 租金账户
        info.put("associated_token_program", accounts[8]); // 关联代币程序账户

        return info;
    }

    private static Map<String, Object> parseInitializePositionBundleWithMetadata(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 账户信息
        info.put("position_bundle", accounts[0]); // 位置包账户
        info.put("position_bundle_mint", accounts[1]); // 位置包 mint 账户
        info.put("position_bundle_metadata", accounts[2]); // 位置包元数据账户
        info.put("position_bundle_token_account", accounts[3]); // 位置包代币账户
        info.put("position_bundle_owner", accounts[4]); // 位置包拥有者账户
        info.put("funder", accounts[5]); // 资金提供者账户
        info.put("metadata_update_auth", accounts[6]); // 元数据更新授权账户
        info.put("token_program", accounts[7]); // 代币程序账户
        info.put("system_program", accounts[8]); // 系统程序账户
        info.put("rent", accounts[9]); // 租金账户
        info.put("associated_token_program", accounts[10]); // 关联代币程序账户
        info.put("metadata_program", accounts[11]); // 元数据程序账户

        return info;
    }

    private static Map<String, Object> parseOpenBundledPosition(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 解析参数
        info.put("bundleIndex", Short.toUnsignedInt(buffer.getShort()));
        info.put("tickLowerIndex", buffer.getInt());
        info.put("tickUpperIndex", buffer.getInt());

        // 解析账户
        info.put("bundled_position", accounts[0]); // 打包位置账户
        info.put("position_bundle", accounts[1]); // 位置包账户
        info.put("position_bundle_token_account", accounts[2]); // 位置包代币账户
        info.put("position_bundle_authority", accounts[3]); // 位置包授权账户
        info.put("whirlpool", accounts[4]); // whirlpool 账户
        info.put("funder", accounts[5]); // 资金提供者账户
        info.put("system_program", accounts[6]); // 系统程序账户
        info.put("rent", accounts[7]); // 租金账户

        return info;
    }

    private static Map<String, Object> parseOpenPositionWithMetadata(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 解析参数
        info.put("positionBump", Byte.toUnsignedInt(buffer.get()));
        info.put("metadataBump", Byte.toUnsignedInt(buffer.get()));
        info.put("tickLowerIndex", buffer.getInt());
        info.put("tickUpperIndex", buffer.getInt());

        // 解析账户
        info.put("funder", accounts[0]); // 资金提供者账户
        info.put("owner", accounts[1]); // 位置拥有者账户
        info.put("position", accounts[2]); // 位置账户
        info.put("position_mint", accounts[3]); // 位置 mint 账户
        info.put("position_metadata_account", accounts[4]); // 位置元数据账户
        info.put("position_token_account", accounts[5]); // 位置代币账户
        info.put("whirlpool", accounts[6]); // whirlpool 账户
        info.put("token_program", accounts[7]); // 代币程序账户
        info.put("system_program", accounts[8]); // 系统程序账户
        info.put("rent", accounts[9]); // 租金账户
        info.put("associated_token_program", accounts[10]); // 关联代币程序账户
        info.put("metadata_program", accounts[11]); // 元数据程序账户
        info.put("metadata_update_auth", accounts[12]); // 元数据更新授权账户

        return info;
    }

    private static Map<String, Object> parseCloseBundledPosition(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 存储解析的字段
        info.put("bundle_index", Short.toUnsignedInt(buffer.getShort()));

        // 账户信息
        info.put("bundled_position", accounts[0]); // 打包位置账户
        info.put("position_bundle", accounts[1]); // 位置包账户
        info.put("position_bundle_token_account", accounts[2]); // 位置包代币账户
        info.put("position_bundle_authority", accounts[3]); // 位置包授权账户
        info.put("receiver", accounts[4]); // 接收者账户

        return info;
    }

    private static Map<String, Object> parseDeletePositionBundle(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 账户信息
        info.put("position_bundle", accounts[0]); // 位置包账户
        info.put("position_bundle_mint", accounts[1]); // 位置包 mint 账户
        info.put("position_bundle_token_account", accounts[2]); // 位置包代币账户
        info.put("position_bundle_owner", accounts[3]); // 位置包拥有者账户
        info.put("receiver", accounts[4]); // 接收者账户
        info.put("token_program", accounts[5]); // 代币程序账户

        return info;
    }

    private static Map<String, Object> parseIncreaseLiquidityV2(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 解析参数
        String low = Long.toUnsignedString(buffer.getLong());  // 读取低位
        String high = Long.toUnsignedString(buffer.getLong()); // 读取高位
        BigInteger liquidityAmount = new BigInteger(high).shiftLeft(64).or(new BigInteger(low));
        info.put("liquidityAmount", liquidityAmount.toString());
        info.put("tokenMaxA", Long.toUnsignedString(buffer.getLong()));
        info.put("tokenMaxB", Long.toUnsignedString(buffer.getLong()));
        if (buffer.get() == 0) {
            info.put("remainingAccountsInfo", 0);
        } else {
            info.put("remainingAccountsInfo", buffer.get());
        }


        // 解析账户
        info.put("whirlpool", accounts[0]); // 流动性池账户
        info.put("token_program_a", accounts[1]); // 代币 A 程序账户
        info.put("token_program_b", accounts[2]); // 代币 B 程序账户
        info.put("memo_program", accounts[3]); // memo 程序账户
        info.put("position_authority", accounts[4]); // 位置授权账户
        info.put("position", accounts[5]); // 位置账户
        info.put("position_token_account", accounts[6]); // 位置代币账户
        info.put("token_mint_a", accounts[7]); // 代币 A 的 mint 账户
        info.put("token_mint_b", accounts[8]); // 代币 B 的 mint 账户
        info.put("token_owner_account_a", accounts[9]); // 代币 A 拥有者账户
        info.put("token_owner_account_b", accounts[10]); // 代币 B 拥有者账户
        info.put("token_vault_a", accounts[11]); // 代币 A 金库账户
        info.put("token_vault_b", accounts[12]); // 代币 B 金库账户
        info.put("tick_array_lower", accounts[13]); // 下限 tick 数组账户
        info.put("tick_array_upper", accounts[14]); // 上限 tick 数组账户
        return info;
    }

    private static Map<String, Object> parseDecreaseLiquidityV2(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 解析参数
        String low = Long.toUnsignedString(buffer.getLong());  // 读取低位
        String high = Long.toUnsignedString(buffer.getLong()); // 读取高位
        BigInteger liquidityAmount = new BigInteger(high).shiftLeft(64).or(new BigInteger(low));
        info.put("liquidityAmount", liquidityAmount.toString());
        info.put("tokenMinA", Long.toUnsignedString(buffer.getLong()));
        info.put("tokenMinB", Long.toUnsignedString(buffer.getLong()));
        if (buffer.get() == 0) {
            info.put("remainingAccountsInfo", 0);
        } else {
            info.put("remainingAccountsInfo", buffer.get());
        }

        // 账户信息
        info.put("whirlpool", accounts[0]); // 流动性池账户
        info.put("token_program_a", accounts[1]); // 代币 A 程序账户
        info.put("token_program_b", accounts[2]); // 代币 B 程序账户
        info.put("memo_program", accounts[3]); // memo 程序账户
        info.put("position_authority", accounts[4]); // 位置授权账户
        info.put("position", accounts[5]); // 位置账户
        info.put("position_token_account", accounts[6]); // 位置代币账户
        info.put("token_mint_a", accounts[7]); // 代笔 A mint 账号
        info.put("token_mint_b", accounts[8]); // 代笔 A mint 账号
        info.put("token_owner_account_a", accounts[9]); // 代币 A 拥有者账户
        info.put("token_owner_account_b", accounts[10]); // 代币 B 拥有者账户
        info.put("token_vault_a", accounts[11]); // 代币 A 金库账户
        info.put("token_vault_b", accounts[12]); // 代币 B 金库账户
        info.put("tick_array_lower", accounts[13]); // 下限 tick 数组账户
        info.put("tick_array_upper", accounts[14]); // 上限 tick 数组账户

        return info;
    }

    private static Map<String, Object> parseInitializeConfigExtension(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 账户信息
        info.put("config", accounts[0]); // 配置账户
        info.put("config_extension", accounts[1]); // 扩展配置账户
        info.put("funder", accounts[2]); // 资金提供者账户
        info.put("fee_authority", accounts[3]); // fee 权限账号
        info.put("system_program", accounts[4]); // 系统程序账户

        return info;
    }

    private static Map<String, Object> parseSetConfigExtensionAuthority(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 解析账户
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 3) {
            accountMap.put("configExtension", accounts[0]);
            accountMap.put("configExtensionAuthority", accounts[1]);
            accountMap.put("newConfigExtensionAuthority", accounts[2]);
        }
        info.put("accounts", accountMap);

        return info;
    }

    private static Map<String, Object> parseSetRewardEmissionsV2(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 解析参数
        info.put("reward_index", Byte.toUnsignedInt(buffer.get()));
        String low = Long.toUnsignedString(buffer.getLong());  // 读取低位
        String high = Long.toUnsignedString(buffer.getLong()); // 读取高位
        BigInteger emissionsPerSecondX64 = new BigInteger(high).shiftLeft(64).or(new BigInteger(low));
        info.put("emissions_per_second_x64", emissionsPerSecondX64.toString());


        // 账户信息
        info.put("whirlpool", accounts[0]); // whirlpool 账户
        info.put("reward_authority", accounts[1]); // 奖励授权账户
        info.put("reward_vault", accounts[2]); // 奖励金库账户
        return info;
    }

    private static Map<String, Object> parseCollectRewardV2(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 解析参数
        info.put("reward_index", Byte.toUnsignedInt(buffer.get()));
        if (buffer.get() == 0) {
            info.put("remainingAccountsInfo", 0);
        } else {
            info.put("remainingAccountsInfo", buffer.get());
        }

        // 账户信息
        info.put("whirlpool", accounts[0]); // whirlpool 账户
        info.put("position_authority", accounts[1]); // 位置授权账户
        info.put("position", accounts[2]); // 位置账户
        info.put("position_token_account", accounts[3]); // 位置代币账户
        info.put("reward_owner_account", accounts[4]); // 奖励拥有者账户
        info.put("reward_mint", accounts[5]); // 奖励 mint 账户
        info.put("reward_vault", accounts[6]); // 奖励金库账户
        info.put("reward_token_program", accounts[7]); // 奖励代币程序账户
        info.put("memo_program", accounts[8]); // memo 程序账户

        return info;
    }

    private static Map<String, Object> parseCollectProtocolFeesV2(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        if (buffer.get() == 0) {
            info.put("remainingAccountsInfo", 0);
        } else {
            info.put("remainingAccountsInfo", buffer.get());
        }

        // 解析账户
        info.put("whirlpools_config", accounts[0]); // whirlpools 配置账户
        info.put("whirlpool", accounts[1]); // whirlpool 账户
        info.put("collect_protocol_fees_authority", accounts[2]); // 收集协议费用授权账户
        info.put("token_mint_a", accounts[3]); // 代币 A mint 账户
        info.put("token_mint_b", accounts[4]); // 代币 B mint 账户
        info.put("token_vault_a", accounts[5]); // 代币 A 金库账户
        info.put("token_vault_b", accounts[6]); // 代币 B 金库账户
        info.put("token_destination_a", accounts[7]); // 代币 A 目标账户
        info.put("token_destination_b", accounts[8]); // 代币 B 目标账户
        info.put("token_program_a", accounts[9]); // 代币 A 程序账户
        info.put("token_program_b", accounts[10]); // 代币 B 程序账户
        info.put("memo_program", accounts[11]); // memo 程序账户
        return info;
    }

    private static Map<String, Object> parseInitializeRewardV2(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 存储解析的字段
        info.put("reward_index", Byte.toUnsignedInt(buffer.get()));

        // 账户信息
        info.put("reward_authority", accounts[0]); // 奖励授权账户
        info.put("funder", accounts[1]); // 资金提供者账户
        info.put("whirlpool", accounts[2]); // whirlpool 账户
        info.put("reward_mint", accounts[3]); // 奖励 mint 账户
        info.put("reward_token_badge", accounts[4]); // 奖励代币徽章账户
        info.put("reward_vault", accounts[5]); // 奖励金库账户
        info.put("reward_token_program", accounts[6]); // 奖励代币程序账户
        info.put("system_program", accounts[7]); // 系统程序账户
        info.put("rent", accounts[8]); // 租金账户

        return info;
    }

    private static Map<String, Object> parseSetFeeRate(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 解析参数
        int feeRate = buffer.getShort() & 0xFFFF;
        info.put("feeRate", feeRate);

        // 解析账户
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 3) {
            accountMap.put("whirlpoolsConfig", accounts[0]);
            accountMap.put("whirlpool", accounts[1]);
            accountMap.put("feeAuthority", accounts[2]);
        }
        info.put("accounts", accountMap);

        return info;
    }

    private static Map<String, Object> parseSetProtocolFeeRate(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 解析参数
        int protocolFeeRate = buffer.getShort() & 0xFFFF;
        info.put("protocolFeeRate", protocolFeeRate);

        // 解析账户
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 3) {
            accountMap.put("whirlpoolsConfig", accounts[0]);
            accountMap.put("whirlpool", accounts[1]);
            accountMap.put("feeAuthority", accounts[2]);
        }
        info.put("accounts", accountMap);

        return info;
    }

    private static Map<String, Object> parseSetRewardEmissionsSuperAuthority(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 账户信息
        info.put("whirlpools_config", accounts[0]); // whirlpools 配置账户
        info.put("reward_emissions_super_authority", accounts[1]); // 奖励发放超级授权账户
        info.put("new_reward_emissions_super_authority", accounts[2]); // 新的奖励发放超级授权账户

        return info;
    }

    private static Map<String, Object> parseSetRewardAuthorityBySuperAuthority(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 解析参数
        info.put("rewardIndex", Byte.toUnsignedInt(buffer.get()));

        // 账户信息
        info.put("whirlpools_config", accounts[0]); // whirlpools 配置账户
        info.put("whirlpool", accounts[1]); // whirlpool 账户
        info.put("reward_emissions_super_authority", accounts[2]); // 奖励发放超级授权账户
        info.put("new_reward_authority", accounts[3]); // 新的奖励授权账户
        return info;
    }

    private static Map<String, Object> parseTwoHopSwap(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 解析参数
        info.put("amount", Long.toUnsignedString(buffer.getLong()));
        info.put("otherAmountThreshold", Long.toUnsignedString(buffer.getLong()));
        info.put("amountSpecifiedIsInput", Byte.toUnsignedInt(buffer.get()));
        info.put("aToBOne", Byte.toUnsignedInt(buffer.get()));
        info.put("aToBTwo", Byte.toUnsignedInt(buffer.get()));
        String oneLow = Long.toUnsignedString(buffer.getLong());  // 读取低位
        String oneHigh = Long.toUnsignedString(buffer.getLong()); // 读取高位
        BigInteger sqrtPriceLimitOne = new BigInteger(oneHigh).shiftLeft(64).or(new BigInteger(oneLow));
        info.put("sqrtPriceLimitOne", sqrtPriceLimitOne);
        String twolow = Long.toUnsignedString(buffer.getLong());  // 读取低位
        String twohigh = Long.toUnsignedString(buffer.getLong()); // 读取高位
        BigInteger sqrtPriceLimitTwo = new BigInteger(twohigh).shiftLeft(64).or(new BigInteger(twolow));
        info.put("sqrtPriceLimitTwo", sqrtPriceLimitTwo);


        // 解析账户
        info.put("token_program", accounts[0]);
        info.put("token_authority", accounts[1]); // 代币授权账户
        info.put("whirlpool_one", accounts[2]); // 第一个流动性池账户
        info.put("whirlpool_two", accounts[3]); // 第二个流动性池账户
        info.put("token_owner_account_one_a", accounts[4]); // 第一个流动性池的代币 A 拥有者账户
        info.put("token_vault_one_a", accounts[5]); // 第一个流动性池的代币 A 金库账户
        info.put("token_owner_account_one_b", accounts[6]); // 第一个流动性池的代币 B 拥有者账户
        info.put("token_vault_one_b", accounts[7]); // 第一个流动性池的代币 B 金库账户
        info.put("token_owner_account_two_a", accounts[8]); // 第二个流动性池的代币 A 拥有者账户
        info.put("token_vault_two_a", accounts[9]); // 第二个流动性池的代币 A 金库账户
        info.put("token_owner_account_two_b", accounts[10]); // 第二个流动性池的代币 B 拥有者账户
        info.put("token_vault_two_b", accounts[11]); // 第二个流动性池的代币 B 金库账户
        info.put("tick_array_one_0", accounts[12]); // 第一个流动性池的下限 tick 数组账户
        info.put("tick_array_one_1", accounts[13]); // 第一个流动性池的中间 tick 数组账户
        info.put("tick_array_one_2", accounts[14]); // 第一个流动性池的上限 tick 数组账户
        info.put("tick_array_two_0", accounts[15]); // 第二个流动性池的下限 tick 数组账户
        info.put("tick_array_two_1", accounts[16]); // 第二个流动性池的中间 tick 数组账户
        info.put("tick_array_two_2", accounts[17]); // 第二个流动性池的上限 tick 数组账户
        info.put("oracle_one", accounts[18]); // 第一个流动性池的 oracle 账户
        info.put("oracle_two", accounts[19]); // 第二个流动性池的 oracle 账户

        return info;
    }

    private static Map<String, Object> parseOpenPositionWithTokenExtensions(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 解析参数
        byte bumps = buffer.get();  // OpenPositionBumps
        int tickLowerIndex = buffer.getInt();
        int tickUpperIndex = buffer.getInt();

        info.put("bumps", bumps);
        info.put("tickLowerIndex", tickLowerIndex);
        info.put("tickUpperIndex", tickUpperIndex);

        // 解析账户
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 14) {
            accountMap.put("funder", accounts[0]);
            accountMap.put("owner", accounts[1]);
            accountMap.put("position", accounts[2]);
            accountMap.put("positionMint", accounts[3]);
            accountMap.put("positionMetadataAccount", accounts[4]);
            accountMap.put("positionTokenAccount", accounts[5]);
            accountMap.put("whirlpool", accounts[6]);
            accountMap.put("tokenProgram", accounts[7]);
            accountMap.put("systemProgram", accounts[8]);
            accountMap.put("rent", accounts[9]);
            accountMap.put("associatedTokenProgram", accounts[10]);
            accountMap.put("metadataProgram", accounts[11]);
            accountMap.put("tokenBadge", accounts[12]);
            accountMap.put("tokenBadgeAuthority", accounts[13]);
        }
        info.put("accounts", accountMap);

        return info;
    }

    private static Map<String, Object> parseClosePositionWithTokenExtensions(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 解析账户
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 10) {
            accountMap.put("positionAuthority", accounts[0]);
            accountMap.put("receiver", accounts[1]);
            accountMap.put("position", accounts[2]);
            accountMap.put("positionMint", accounts[3]);
            accountMap.put("positionTokenAccount", accounts[4]);
            accountMap.put("tokenProgram", accounts[5]);
            accountMap.put("tokenBadge", accounts[6]);
            accountMap.put("tokenBadgeAuthority", accounts[7]);
            accountMap.put("whirlpool", accounts[8]);
            accountMap.put("systemProgram", accounts[9]);
        }
        info.put("accounts", accountMap);

        return info;
    }

    private static Map<String, Object> parseClosePosition(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 账户信息
        info.put("position_authority", accounts[0]); // 位置授权账户
        info.put("receiver", accounts[1]); // 接收者账户
        info.put("position", accounts[2]); // 位置账户
        info.put("position_mint", accounts[3]); // 位置 mint 账户
        info.put("position_token_account", accounts[4]); // 位置代币账户
        info.put("token_program", accounts[5]); // 代币程序账户
        return info;
    }
}