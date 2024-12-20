package cn.xlystar.parse.solSwap.whirlpool;

import org.bitcoinj.core.Base58;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class WhirlpoolInstructionParser {

    public static Map<String, Object> parseInstruction(byte[] data, String[] accounts) {
        Map<String, Object> result = new HashMap<>();

        if (data == null || data.length == 0) {
            result.put("error", "Invalid instruction data");
            return result;
        }

        try {
            ByteBuffer buffer = ByteBuffer.wrap(data);
            int discriminator = buffer.get() & 0xFF;
            WhirlpoolInstruction instruction = WhirlpoolInstruction.fromValue(discriminator);

            Map<String, Object> info;
            switch (instruction) {
                case INITIALIZE_CONFIG: // 0
                    info = parseInitializeConfig(buffer, accounts);
                    result.put("type", "initializeConfig");
                    break;
                case INITIALIZE_POOL: // 1
                    info = parseInitializePool(buffer, accounts);
                    result.put("type", "initializePool");
                    break;
                case INITIALIZE_TICK_ARRAY: // 2
                    info = parseInitializeTickArray(buffer, accounts);
                    result.put("type", "initializeTickArray");
                    break;
                case INITIALIZE_FEE_TIER: // 3
                    info = parseInitializeFeeTier(buffer, accounts);
                    result.put("type", "initializeFeeTier");
                    break;
                case INITIALIZE_REWARD: // 4
                    info = parseInitializeReward(buffer, accounts);
                    result.put("type", "initializeReward");
                    break;
                case SET_REWARD_EMISSIONS: // 5
                    info = parseSetRewardEmissions(buffer, accounts);
                    result.put("type", "setRewardEmissions");
                    break;
                case OPEN_POSITION: // 6
                    info = parseOpenPosition(buffer, accounts);
                    result.put("type", "openPosition");
                    break;
                case OPEN_POSITION_WITH_METADATA: // 7
                    info = parseOpenPositionWithMetadata(buffer, accounts);
                    result.put("type", "openPositionWithMetadata");
                    break;
                case INCREASE_LIQUIDITY: // 8
                    info = parseIncreaseLiquidity(buffer, accounts);
                    result.put("type", "increaseLiquidity");
                    break;
                case DECREASE_LIQUIDITY: // 9
                    info = parseDecreaseLiquidity(buffer, accounts);
                    result.put("type", "decreaseLiquidity");
                    break;
                case UPDATE_FEES_AND_REWARDS: // 10
                    info = parseUpdateFeesAndRewards(buffer, accounts);
                    result.put("type", "updateFeesAndRewards");
                    break;
                case COLLECT_FEES: // 11
                    info = parseCollectFees(buffer, accounts);
                    result.put("type", "collectFees");
                    break;
                case COLLECT_REWARD: // 12
                    info = parseCollectReward(buffer, accounts);
                    result.put("type", "collectReward");
                    break;
                case COLLECT_PROTOCOL_FEES: // 13
                    info = parseCollectProtocolFees(buffer, accounts);
                    result.put("type", "collectProtocolFees");
                    break;
                case SWAP: // 14
                    info = parseSwap(buffer, accounts);
                    result.put("type", "swap");
                    break;
                case CLOSE_POSITION: // 15
                    info = parseClosePosition(buffer, accounts);
                    result.put("type", "closePosition");
                    break;
                case SET_DEFAULT_FEE_RATE: // 16
                    info = parseSetDefaultFeeRate(buffer, accounts);
                    result.put("type", "setDefaultFeeRate");
                    break;
                case SET_DEFAULT_PROTOCOL_FEE_RATE: // 17
                    info = parseSetDefaultProtocolFeeRate(buffer, accounts);
                    result.put("type", "setDefaultProtocolFeeRate");
                    break;
                case SET_FEE_RATE: // 18
                    info = parseSetFeeRate(buffer, accounts);
                    result.put("type", "setFeeRate");
                    break;
                case SET_PROTOCOL_FEE_RATE: // 19
                    info = parseSetProtocolFeeRate(buffer, accounts);
                    result.put("type", "setProtocolFeeRate");
                    break;
                case SET_FEE_AUTHORITY: // 20
                    info = parseSetFeeAuthority(buffer, accounts);
                    result.put("type", "setFeeAuthority");
                    break;
                case SET_COLLECT_PROTOCOL_FEES_AUTHORITY: // 21
                    info = parseSetCollectProtocolFeesAuthority(buffer, accounts);
                    result.put("type", "setCollectProtocolFeesAuthority");
                    break;
                case SET_REWARD_AUTHORITY: // 22
                    info = parseSetRewardAuthority(buffer, accounts);
                    result.put("type", "setRewardAuthority");
                    break;
                case SET_REWARD_AUTHORITY_BY_SUPER_AUTHORITY: // 23
                    info = parseSetRewardAuthorityBySuperAuthority(buffer, accounts);
                    result.put("type", "setRewardAuthorityBySuperAuthority");
                    break;
                case SET_REWARD_EMISSIONS_SUPER_AUTHORITY: // 24
                    info = parseSetRewardEmissionsSuperAuthority(buffer, accounts);
                    result.put("type", "setRewardEmissionsSuperAuthority");
                    break;
                case TWO_HOP_SWAP: // 25
                    info = parseTwoHopSwap(buffer, accounts);
                    result.put("type", "twoHopSwap");
                    break;
                case INITIALIZE_POSITION_BUNDLE: // 26
                    info = parseInitializePositionBundle(buffer, accounts);
                    result.put("type", "initializePositionBundle");
                    break;
                case INITIALIZE_POSITION_BUNDLE_WITH_METADATA: // 27
                    info = parseInitializePositionBundleWithMetadata(buffer, accounts);
                    result.put("type", "initializePositionBundleWithMetadata");
                    break;
                case DELETE_POSITION_BUNDLE: // 28
                    info = parseDeletePositionBundle(buffer, accounts);
                    result.put("type", "deletePositionBundle");
                    break;
                case OPEN_BUNDLED_POSITION: // 29
                    info = parseOpenBundledPosition(buffer, accounts);
                    result.put("type", "openBundledPosition");
                    break;
                case CLOSE_BUNDLED_POSITION: // 30
                    info = parseCloseBundledPosition(buffer, accounts);
                    result.put("type", "closeBundledPosition");
                    break;
                case OPEN_POSITION_WITH_TOKEN_EXTENSIONS: // 31
                    info = parseOpenPositionWithTokenExtensions(buffer, accounts);
                    result.put("type", "openPositionWithTokenExtensions");
                    break;
                case CLOSE_POSITION_WITH_TOKEN_EXTENSIONS: // 32
                    info = parseClosePositionWithTokenExtensions(buffer, accounts);
                    result.put("type", "closePositionWithTokenExtensions");
                    break;
                case COLLECT_FEES_V2: // 33
                    info = parseCollectFeesV2(buffer, accounts);
                    result.put("type", "collectFeesV2");
                    break;
                case COLLECT_PROTOCOL_FEES_V2: // 34
                    info = parseCollectProtocolFeesV2(buffer, accounts);
                    result.put("type", "collectProtocolFeesV2");
                    break;
                case COLLECT_REWARD_V2: // 35
                    info = parseCollectRewardV2(buffer, accounts);
                    result.put("type", "collectRewardV2");
                    break;
                case DECREASE_LIQUIDITY_V2: // 36
                    info = parseDecreaseLiquidityV2(buffer, accounts);
                    result.put("type", "decreaseLiquidityV2");
                    break;
                case INCREASE_LIQUIDITY_V2: // 37
                    info = parseIncreaseLiquidityV2(buffer, accounts);
                    result.put("type", "increaseLiquidityV2");
                    break;
                case INITIALIZE_POOL_V2: // 38
                    info = parseInitializePoolV2(buffer, accounts);
                    result.put("type", "initializePoolV2");
                    break;
                case INITIALIZE_REWARD_V2: // 39
                    info = parseInitializeRewardV2(buffer, accounts);
                    result.put("type", "initializeRewardV2");
                    break;
                case SET_REWARD_EMISSIONS_V2: // 40
                    info = parseSetRewardEmissionsV2(buffer, accounts);
                    result.put("type", "setRewardEmissionsV2");
                    break;
                case SWAP_V2: // 41
                    info = parseSwapV2(buffer, accounts);
                    result.put("type", "swapV2");
                    break;
                case TWO_HOP_SWAP_V2: // 42
                    info = parseTwoHopSwapV2(buffer, accounts);
                    result.put("type", "twoHopSwapV2");
                    break;
                case INITIALIZE_CONFIG_EXTENSION: // 43
                    info = parseInitializeConfigExtension(buffer, accounts);
                    result.put("type", "initializeConfigExtension");
                    break;
                case SET_CONFIG_EXTENSION_AUTHORITY: // 44
                    info = parseSetConfigExtensionAuthority(buffer, accounts);
                    result.put("type", "setConfigExtensionAuthority");
                    break;
                case SET_TOKEN_BADGE_AUTHORITY: // 45
                    info = parseSetTokenBadgeAuthority(buffer, accounts);
                    result.put("type", "setTokenBadgeAuthority");
                    break;
                case INITIALIZE_TOKEN_BADGE: // 46
                    info = parseInitializeTokenBadge(buffer, accounts);
                    result.put("type", "initializeTokenBadge");
                    break;
                case DELETE_TOKEN_BADGE: // 47
                    info = parseDeleteTokenBadge(buffer, accounts);
                    result.put("type", "deleteTokenBadge");
                    break;
                default:
                    result.put("error", "Unsupported instruction: " + instruction);
                    return result;
            }
            result.put("info", info);

        } catch (Exception e) {
            result.put("error", "Failed to parse instruction: " + e.getMessage());
        }

        return result;
    }

    private static Map<String, Object> parseSwap(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 解析参数
        long amount = buffer.getLong();
        long otherAmountThreshold = buffer.getLong();
        BigInteger sqrtPriceLimit = new BigInteger(buffer.getLong() + ""); // u128
        boolean amountSpecifiedIsInput = buffer.get() != 0;
        boolean aToB = buffer.get() != 0;

        info.put("amount", amount);
        info.put("otherAmountThreshold", otherAmountThreshold);
        info.put("sqrtPriceLimit", sqrtPriceLimit);
        info.put("amountSpecifiedIsInput", amountSpecifiedIsInput);
        info.put("aToB", aToB);

        // 解析账户
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 12) {
            accountMap.put("whirlpool", accounts[0]);          // 池子账户
            accountMap.put("tokenProgram", accounts[1]);       // SPL Token程序
            accountMap.put("tokenAuthority", accounts[2]);     // 代币授权账户
            accountMap.put("tokenOwnerAccountA", accounts[3]); // A代币所有者账户
            accountMap.put("tokenVaultA", accounts[4]);        // A代币金库
            accountMap.put("tokenOwnerAccountB", accounts[5]); // B代币所有者账户
            accountMap.put("tokenVaultB", accounts[6]);        // B代币金库
            accountMap.put("tickArray0", accounts[7]);         // 价格刻度数组0
            accountMap.put("tickArray1", accounts[8]);         // 价格刻度数组1
            accountMap.put("tickArray2", accounts[9]);         // 价格刻度数组2
            accountMap.put("oracle", accounts[10]);            // 预言机账户
        }
        info.put("accounts", accountMap);

        return info;
    }

    private static Map<String, Object> parseOpenPosition(ByteBuffer buffer, String[] accounts) {
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
        if (accounts.length >= 11) {
            accountMap.put("funder", accounts[0]);             // 资金提供者
            accountMap.put("owner", accounts[1]);              // 头寸所有者
            accountMap.put("position", accounts[2]);           // 头寸账户
            accountMap.put("positionMint", accounts[3]);       // 头寸NFT铸币账户
            accountMap.put("positionTokenAccount", accounts[4]); // 头寸代币账户
            accountMap.put("whirlpool", accounts[5]);          // 池子账户
            accountMap.put("tokenProgram", accounts[6]);       // SPL Token程序
            accountMap.put("systemProgram", accounts[7]);      // System程序
            accountMap.put("rent", accounts[8]);               // 租金账户
            accountMap.put("associatedTokenProgram", accounts[9]); // 关联代币程序
        }
        info.put("accounts", accountMap);

        return info;
    }

    private static Map<String, Object> parseIncreaseLiquidity(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 解析参数
        BigInteger liquidityAmount = new BigInteger(buffer.getLong() + ""); // u128
        long tokenMaxA = buffer.getLong();
        long tokenMaxB = buffer.getLong();

        info.put("liquidityAmount", liquidityAmount);
        info.put("tokenMaxA", tokenMaxA);
        info.put("tokenMaxB", tokenMaxB);

        // 解析账户
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 12) {
            accountMap.put("whirlpool", accounts[0]);          // 池子账户
            accountMap.put("tokenProgram", accounts[1]);       // SPL Token程序
            accountMap.put("positionAuthority", accounts[2]);  // 头寸授权账户
            accountMap.put("position", accounts[3]);           // 头寸账户
            accountMap.put("positionTokenAccount", accounts[4]); // 头寸代币账户
            accountMap.put("tokenOwnerAccountA", accounts[5]); // A代币所有者账户
            accountMap.put("tokenOwnerAccountB", accounts[6]); // B代币所有者账户
            accountMap.put("tokenVaultA", accounts[7]);        // A代币金库
            accountMap.put("tokenVaultB", accounts[8]);        // B代币金库
            accountMap.put("tickArrayLower", accounts[9]);     // 下限价格刻度数组
            accountMap.put("tickArrayUpper", accounts[10]);    // 上限价格刻度数组
        }
        info.put("accounts", accountMap);

        return info;
    }

    private static Map<String, Object> parseDecreaseLiquidity(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 解析参数
        BigInteger liquidityAmount = new BigInteger(buffer.getLong() + ""); // u128
        long tokenMinA = buffer.getLong();
        long tokenMinB = buffer.getLong();

        info.put("liquidityAmount", liquidityAmount);
        info.put("tokenMinA", tokenMinA);
        info.put("tokenMinB", tokenMinB);

        // 解析账户
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 12) {
            accountMap.put("whirlpool", accounts[0]);          // 池子账户
            accountMap.put("tokenProgram", accounts[1]);       // SPL Token程序
            accountMap.put("positionAuthority", accounts[2]);  // 头寸授权账户
            accountMap.put("position", accounts[3]);           // 头寸账户
            accountMap.put("positionTokenAccount", accounts[4]); // 头寸代币账户
            accountMap.put("tokenOwnerAccountA", accounts[5]); // A代币所有者账户
            accountMap.put("tokenOwnerAccountB", accounts[6]); // B代币所有者账户
            accountMap.put("tokenVaultA", accounts[7]);        // A代币金库
            accountMap.put("tokenVaultB", accounts[8]);        // B代币金库
            accountMap.put("tickArrayLower", accounts[9]);     // 下限价格刻度数组
            accountMap.put("tickArrayUpper", accounts[10]);    // 上限价格刻度数组
        }
        info.put("accounts", accountMap);

        return info;
    }

    private static Map<String, Object> parseCollectFees(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 解析账户
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 9) {
            accountMap.put("whirlpool", accounts[0]);          // 池子账户
            accountMap.put("positionAuthority", accounts[1]);  // 头寸授权账户
            accountMap.put("position", accounts[2]);           // 头寸账户
            accountMap.put("positionTokenAccount", accounts[3]); // 头寸代币账户
            accountMap.put("tokenOwnerAccountA", accounts[4]); // A代币所有者账户
            accountMap.put("tokenVaultA", accounts[5]);        // A代币金库
            accountMap.put("tokenOwnerAccountB", accounts[6]); // B代币所有者账户
            accountMap.put("tokenVaultB", accounts[7]);        // B代币金库
            accountMap.put("tokenProgram", accounts[8]);       // SPL Token程序
        }
        info.put("accounts", accountMap);

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
        int defaultProtocolFeeRate = buffer.getShort() & 0xFFFF;

        info.put("feeAuthority", Base58.encode(feeAuthority));
        info.put("collectProtocolFeesAuthority", Base58.encode(collectProtocolFeesAuthority));
        info.put("rewardEmissionsSuperAuthority", Base58.encode(rewardEmissionsSuperAuthority));
        info.put("defaultProtocolFeeRate", defaultProtocolFeeRate);

        // 解析账户
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 3) {
            accountMap.put("config", accounts[0]);
            accountMap.put("funder", accounts[1]);
            accountMap.put("systemProgram", accounts[2]);
        }
        info.put("accounts", accountMap);

        return info;
    }

    private static Map<String, Object> parseInitializePool(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 解析参数
        int tickSpacing = buffer.getShort() & 0xFFFF;
        BigInteger initialSqrtPrice = new BigInteger(buffer.getLong() + ""); // u128

        info.put("tickSpacing", tickSpacing);
        info.put("initialSqrtPrice", initialSqrtPrice);

        // 解析账户
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 9) {
            accountMap.put("whirlpoolsConfig", accounts[0]);
            accountMap.put("tokenMintA", accounts[1]);
            accountMap.put("tokenMintB", accounts[2]);
            accountMap.put("feeAuthority", accounts[3]);
            accountMap.put("tokenVaultA", accounts[4]);
            accountMap.put("tokenVaultB", accounts[5]);
            accountMap.put("whirlpool", accounts[6]);
            accountMap.put("funder", accounts[7]);
            accountMap.put("systemProgram", accounts[8]);
        }
        info.put("accounts", accountMap);

        return info;
    }

    private static Map<String, Object> parseInitializeTickArray(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 解析参数
        int startTickIndex = buffer.getInt();
        info.put("startTickIndex", startTickIndex);

        // 解析账户
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 4) {
            accountMap.put("whirlpool", accounts[0]);
            accountMap.put("funder", accounts[1]);
            accountMap.put("tickArray", accounts[2]);
            accountMap.put("systemProgram", accounts[3]);
        }
        info.put("accounts", accountMap);

        return info;
    }

    private static Map<String, Object> parseInitializeFeeTier(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 解析参数
        int tickSpacing = buffer.getShort() & 0xFFFF;
        int defaultFeeRate = buffer.getShort() & 0xFFFF;

        info.put("tickSpacing", tickSpacing);
        info.put("defaultFeeRate", defaultFeeRate);

        // 解析账户
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 5) {
            accountMap.put("config", accounts[0]);
            accountMap.put("feeTier", accounts[1]);
            accountMap.put("funder", accounts[2]);
            accountMap.put("feeAuthority", accounts[3]);
            accountMap.put("systemProgram", accounts[4]);
        }
        info.put("accounts", accountMap);

        return info;
    }

    private static Map<String, Object> parseInitializeReward(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 解析参数
        int rewardIndex = buffer.get() & 0xFF;
        info.put("rewardIndex", rewardIndex);

        // 解析账户
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 6) {
            accountMap.put("rewardAuthority", accounts[0]);
            accountMap.put("funder", accounts[1]);
            accountMap.put("whirlpool", accounts[2]);
            accountMap.put("rewardMint", accounts[3]);
            accountMap.put("rewardVault", accounts[4]);
            accountMap.put("systemProgram", accounts[5]);
        }
        info.put("accounts", accountMap);

        return info;
    }

    private static Map<String, Object> parseSetRewardEmissions(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 解析参数
        int rewardIndex = buffer.get() & 0xFF;
        BigInteger emissionsPerSecondX64 = new BigInteger(buffer.getLong() + ""); // u128

        info.put("rewardIndex", rewardIndex);
        info.put("emissionsPerSecondX64", emissionsPerSecondX64);

        // 解析账户
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 4) {
            accountMap.put("whirlpool", accounts[0]);
            accountMap.put("rewardAuthority", accounts[1]);
            accountMap.put("rewardVault", accounts[2]);
            accountMap.put("tokenProgram", accounts[3]);
        }
        info.put("accounts", accountMap);

        return info;
    }

    private static Map<String, Object> parseCollectReward(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 解析参数
        int rewardIndex = buffer.get() & 0xFF;
        info.put("rewardIndex", rewardIndex);

        // 解析账户
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 7) {
            accountMap.put("whirlpool", accounts[0]);
            accountMap.put("positionAuthority", accounts[1]);
            accountMap.put("position", accounts[2]);
            accountMap.put("positionTokenAccount", accounts[3]);
            accountMap.put("rewardOwnerAccount", accounts[4]);
            accountMap.put("rewardVault", accounts[5]);
            accountMap.put("tokenProgram", accounts[6]);
        }
        info.put("accounts", accountMap);

        return info;
    }

    private static Map<String, Object> parseCollectProtocolFees(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 解析账户
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 8) {
            accountMap.put("whirlpoolsConfig", accounts[0]);
            accountMap.put("whirlpool", accounts[1]);
            accountMap.put("collectProtocolFeesAuthority", accounts[2]);
            accountMap.put("tokenVaultA", accounts[3]);
            accountMap.put("tokenVaultB", accounts[4]);
            accountMap.put("tokenDestinationA", accounts[5]);
            accountMap.put("tokenDestinationB", accounts[6]);
            accountMap.put("tokenProgram", accounts[7]);
        }
        info.put("accounts", accountMap);

        return info;
    }

    private static Map<String, Object> parseUpdateFeesAndRewards(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 解析账户
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 4) {
            accountMap.put("whirlpool", accounts[0]);
            accountMap.put("position", accounts[1]);
            accountMap.put("tickArrayLower", accounts[2]);
            accountMap.put("tickArrayUpper", accounts[3]);
        }
        info.put("accounts", accountMap);

        return info;
    }

    private static Map<String, Object> parseSetDefaultFeeRate(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 解析参数
        int defaultFeeRate = buffer.getShort() & 0xFFFF;
        info.put("defaultFeeRate", defaultFeeRate);

        // 解析账户
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 3) {
            accountMap.put("whirlpoolsConfig", accounts[0]);
            accountMap.put("feeTier", accounts[1]);
            accountMap.put("feeAuthority", accounts[2]);
        }
        info.put("accounts", accountMap);

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
        int rewardIndex = buffer.get() & 0xFF;
        info.put("rewardIndex", rewardIndex);

        // 解析账户
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 3) {
            accountMap.put("whirlpool", accounts[0]);
            accountMap.put("rewardAuthority", accounts[1]);
            accountMap.put("newRewardAuthority", accounts[2]);
        }
        info.put("accounts", accountMap);

        return info;
    }

    private static Map<String, Object> parseSwapV2(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 解析参数
        long amount = buffer.getLong();
        long otherAmountThreshold = buffer.getLong();
        BigInteger sqrtPriceLimit = new BigInteger(buffer.getLong() + ""); // u128
        boolean amountSpecifiedIsInput = buffer.get() != 0;
        boolean aToB = buffer.get() != 0;

        info.put("amount", amount);
        info.put("otherAmountThreshold", otherAmountThreshold);
        info.put("sqrtPriceLimit", sqrtPriceLimit);
        info.put("amountSpecifiedIsInput", amountSpecifiedIsInput);
        info.put("aToB", aToB);

        // 解析账户
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 11) {
            accountMap.put("whirlpool", accounts[0]);
            accountMap.put("tokenProgram", accounts[1]);
            accountMap.put("tokenAuthority", accounts[2]);
            accountMap.put("tokenOwnerAccountA", accounts[3]);
            accountMap.put("tokenVaultA", accounts[4]);
            accountMap.put("tokenOwnerAccountB", accounts[5]);
            accountMap.put("tokenVaultB", accounts[6]);
            accountMap.put("tickArray0", accounts[7]);
            accountMap.put("tickArray1", accounts[8]);
            accountMap.put("tickArray2", accounts[9]);
            accountMap.put("oracle", accounts[10]);
        }
        info.put("accounts", accountMap);

        return info;
    }

    private static Map<String, Object> parseTwoHopSwapV2(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 解析参数
        long amount = buffer.getLong();
        long otherAmountThreshold = buffer.getLong();
        boolean amountSpecifiedIsInput = buffer.get() != 0;
        boolean aToBOne = buffer.get() != 0;
        boolean aToBTwo = buffer.get() != 0;
        BigInteger sqrtPriceLimitOne = new BigInteger(buffer.getLong() + ""); // u128
        BigInteger sqrtPriceLimitTwo = new BigInteger(buffer.getLong() + ""); // u128

        info.put("amount", amount);
        info.put("otherAmountThreshold", otherAmountThreshold);
        info.put("amountSpecifiedIsInput", amountSpecifiedIsInput);
        info.put("aToBOne", aToBOne);
        info.put("aToBTwo", aToBTwo);
        info.put("sqrtPriceLimitOne", sqrtPriceLimitOne);
        info.put("sqrtPriceLimitTwo", sqrtPriceLimitTwo);

        // 解析账户
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 20) {
            accountMap.put("tokenProgram", accounts[0]);
            accountMap.put("tokenAuthority", accounts[1]);
            accountMap.put("whirlpoolOne", accounts[2]);
            accountMap.put("whirlpoolTwo", accounts[3]);
            accountMap.put("tokenOwnerAccountA", accounts[4]);
            accountMap.put("tokenVaultA", accounts[5]);
            accountMap.put("tokenOwnerAccountB", accounts[6]);
            accountMap.put("tokenVaultB", accounts[7]);
            accountMap.put("tokenOwnerAccountC", accounts[8]);
            accountMap.put("tokenVaultC", accounts[9]);
            accountMap.put("tickArray0", accounts[10]);
            accountMap.put("tickArray1", accounts[11]);
            accountMap.put("tickArray2", accounts[12]);
            accountMap.put("tickArray3", accounts[13]);
            accountMap.put("tickArray4", accounts[14]);
            accountMap.put("tickArray5", accounts[15]);
            accountMap.put("oracleOne", accounts[16]);
            accountMap.put("oracleTwo", accounts[17]);
        }
        info.put("accounts", accountMap);

        return info;
    }

    private static Map<String, Object> parseCollectFeesV2(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 解析账户
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 9) {
            accountMap.put("whirlpool", accounts[0]);
            accountMap.put("positionAuthority", accounts[1]);
            accountMap.put("position", accounts[2]);
            accountMap.put("positionTokenAccount", accounts[3]);
            accountMap.put("tokenOwnerAccountA", accounts[4]);
            accountMap.put("tokenVaultA", accounts[5]);
            accountMap.put("tokenOwnerAccountB", accounts[6]);
            accountMap.put("tokenVaultB", accounts[7]);
            accountMap.put("tokenProgram", accounts[8]);
        }
        info.put("accounts", accountMap);

        return info;
    }

    private static Map<String, Object> parseInitializePoolV2(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 解析参数
        int tickSpacing = buffer.getShort() & 0xFFFF;
        BigInteger initialSqrtPrice = new BigInteger(buffer.getLong() + ""); // u128

        info.put("tickSpacing", tickSpacing);
        info.put("initialSqrtPrice", initialSqrtPrice);

        // 解析账户
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 8) {
            accountMap.put("whirlpoolsConfig", accounts[0]);
            accountMap.put("tokenMintA", accounts[1]);
            accountMap.put("tokenMintB", accounts[2]);
            accountMap.put("whirlpool", accounts[3]);
            accountMap.put("tokenVaultA", accounts[4]);
            accountMap.put("tokenVaultB", accounts[5]);
            accountMap.put("funder", accounts[6]);
            accountMap.put("systemProgram", accounts[7]);
        }
        info.put("accounts", accountMap);

        return info;
    }

    private static Map<String, Object> parseInitializeTokenBadge(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 解析账户
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 5) {
            accountMap.put("tokenBadge", accounts[0]);
            accountMap.put("tokenBadgeAuthority", accounts[1]);
            accountMap.put("funder", accounts[2]);
            accountMap.put("systemProgram", accounts[3]);
            accountMap.put("rent", accounts[4]);
        }
        info.put("accounts", accountMap);

        return info;
    }

    private static Map<String, Object> parseDeleteTokenBadge(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 解析账户
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 3) {
            accountMap.put("tokenBadge", accounts[0]);
            accountMap.put("tokenBadgeAuthority", accounts[1]);
            accountMap.put("receiver", accounts[2]);
        }
        info.put("accounts", accountMap);

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
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 7) {
            accountMap.put("positionBundle", accounts[0]);
            accountMap.put("positionBundleMint", accounts[1]);
            accountMap.put("positionBundleTokenAccount", accounts[2]);
            accountMap.put("positionBundleOwner", accounts[3]);
            accountMap.put("funder", accounts[4]);
            accountMap.put("tokenProgram", accounts[5]);
            accountMap.put("systemProgram", accounts[6]);
        }
        info.put("accounts", accountMap);

        return info;
    }

    private static Map<String, Object> parseInitializePositionBundleWithMetadata(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 解析账户
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 11) {
            accountMap.put("positionBundle", accounts[0]);
            accountMap.put("positionBundleMint", accounts[1]);
            accountMap.put("positionBundleMetadata", accounts[2]);
            accountMap.put("positionBundleTokenAccount", accounts[3]);
            accountMap.put("positionBundleOwner", accounts[4]);
            accountMap.put("funder", accounts[5]);
            accountMap.put("metadataProgram", accounts[6]);
            accountMap.put("tokenProgram", accounts[7]);
            accountMap.put("systemProgram", accounts[8]);
            accountMap.put("rent", accounts[9]);
            accountMap.put("associatedTokenProgram", accounts[10]);
        }
        info.put("accounts", accountMap);

        return info;
    }

    private static Map<String, Object> parseOpenBundledPosition(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 解析参数
        int bundleIndex = buffer.getShort() & 0xFFFF;
        int tickLowerIndex = buffer.getInt();
        int tickUpperIndex = buffer.getInt();

        info.put("bundleIndex", bundleIndex);
        info.put("tickLowerIndex", tickLowerIndex);
        info.put("tickUpperIndex", tickUpperIndex);

        // 解析账户
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 8) {
            accountMap.put("bundledPosition", accounts[0]);
            accountMap.put("positionBundle", accounts[1]);
            accountMap.put("positionBundleTokenAccount", accounts[2]);
            accountMap.put("positionBundleAuthority", accounts[3]);
            accountMap.put("whirlpool", accounts[4]);
            accountMap.put("funder", accounts[5]);
            accountMap.put("systemProgram", accounts[6]);
            accountMap.put("rent", accounts[7]);
        }
        info.put("accounts", accountMap);

        return info;
    }

    private static Map<String, Object> parseOpenPositionWithMetadata(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 解析参数
        byte bumps = buffer.get();  // OpenPositionWithMetadataBumps
        int tickLowerIndex = buffer.getInt();
        int tickUpperIndex = buffer.getInt();

        info.put("bumps", bumps);
        info.put("tickLowerIndex", tickLowerIndex);
        info.put("tickUpperIndex", tickUpperIndex);

        // 解析账户
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 13) {
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
            accountMap.put("metadataUpdateAuth", accounts[12]);
        }
        info.put("accounts", accountMap);

        return info;
    }

    private static Map<String, Object> parseCloseBundledPosition(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 解析参数
        int bundleIndex = buffer.getShort() & 0xFFFF;
        info.put("bundleIndex", bundleIndex);

        // 解析账户
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 5) {
            accountMap.put("bundledPosition", accounts[0]);
            accountMap.put("positionBundle", accounts[1]);
            accountMap.put("positionBundleTokenAccount", accounts[2]);
            accountMap.put("positionBundleAuthority", accounts[3]);
            accountMap.put("receiver", accounts[4]);
        }
        info.put("accounts", accountMap);

        return info;
    }

    private static Map<String, Object> parseDeletePositionBundle(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 解析账户
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 6) {
            accountMap.put("positionBundle", accounts[0]);
            accountMap.put("positionBundleMint", accounts[1]);
            accountMap.put("positionBundleTokenAccount", accounts[2]);
            accountMap.put("positionBundleOwner", accounts[3]);
            accountMap.put("receiver", accounts[4]);
            accountMap.put("tokenProgram", accounts[5]);
        }
        info.put("accounts", accountMap);

        return info;
    }

    private static Map<String, Object> parseIncreaseLiquidityV2(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 解析参数
        BigInteger liquidityAmount = new BigInteger(buffer.getLong() + ""); // u128
        long tokenMaxA = buffer.getLong();
        long tokenMaxB = buffer.getLong();

        info.put("liquidityAmount", liquidityAmount);
        info.put("tokenMaxA", tokenMaxA);
        info.put("tokenMaxB", tokenMaxB);

        // 解析账户
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 11) {
            accountMap.put("whirlpool", accounts[0]);
            accountMap.put("tokenProgram", accounts[1]);
            accountMap.put("positionAuthority", accounts[2]);
            accountMap.put("position", accounts[3]);
            accountMap.put("positionTokenAccount", accounts[4]);
            accountMap.put("tokenOwnerAccountA", accounts[5]);
            accountMap.put("tokenOwnerAccountB", accounts[6]);
            accountMap.put("tokenVaultA", accounts[7]);
            accountMap.put("tokenVaultB", accounts[8]);
            accountMap.put("tickArrayLower", accounts[9]);
            accountMap.put("tickArrayUpper", accounts[10]);
        }
        info.put("accounts", accountMap);

        return info;
    }

    private static Map<String, Object> parseDecreaseLiquidityV2(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 解析参数
        BigInteger liquidityAmount = new BigInteger(buffer.getLong() + ""); // u128
        long tokenMinA = buffer.getLong();
        long tokenMinB = buffer.getLong();

        info.put("liquidityAmount", liquidityAmount);
        info.put("tokenMinA", tokenMinA);
        info.put("tokenMinB", tokenMinB);

        // 解析账户
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 11) {
            accountMap.put("whirlpool", accounts[0]);
            accountMap.put("tokenProgram", accounts[1]);
            accountMap.put("positionAuthority", accounts[2]);
            accountMap.put("position", accounts[3]);
            accountMap.put("positionTokenAccount", accounts[4]);
            accountMap.put("tokenOwnerAccountA", accounts[5]);
            accountMap.put("tokenOwnerAccountB", accounts[6]);
            accountMap.put("tokenVaultA", accounts[7]);
            accountMap.put("tokenVaultB", accounts[8]);
            accountMap.put("tickArrayLower", accounts[9]);
            accountMap.put("tickArrayUpper", accounts[10]);
        }
        info.put("accounts", accountMap);

        return info;
    }

    private static Map<String, Object> parseInitializeConfigExtension(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 解析账户
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 4) {
            accountMap.put("config", accounts[0]);
            accountMap.put("configExtension", accounts[1]);
            accountMap.put("funder", accounts[2]);
            accountMap.put("systemProgram", accounts[3]);
        }
        info.put("accounts", accountMap);

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
        int rewardIndex = buffer.get() & 0xFF;
        BigInteger emissionsPerSecondX64 = new BigInteger(buffer.getLong() + ""); // u128

        info.put("rewardIndex", rewardIndex);
        info.put("emissionsPerSecondX64", emissionsPerSecondX64);

        // 解析账户
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 3) {
            accountMap.put("whirlpool", accounts[0]);
            accountMap.put("rewardAuthority", accounts[1]);
            accountMap.put("rewardVault", accounts[2]);
        }
        info.put("accounts", accountMap);

        return info;
    }

    private static Map<String, Object> parseCollectRewardV2(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 解析参数
        int rewardIndex = buffer.get() & 0xFF;
        info.put("rewardIndex", rewardIndex);

        // 解析账户
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 7) {
            accountMap.put("whirlpool", accounts[0]);
            accountMap.put("positionAuthority", accounts[1]);
            accountMap.put("position", accounts[2]);
            accountMap.put("positionTokenAccount", accounts[3]);
            accountMap.put("rewardOwnerAccount", accounts[4]);
            accountMap.put("rewardVault", accounts[5]);
            accountMap.put("tokenProgram", accounts[6]);
        }
        info.put("accounts", accountMap);

        return info;
    }

    private static Map<String, Object> parseCollectProtocolFeesV2(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 解析账户
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 8) {
            accountMap.put("whirlpoolsConfig", accounts[0]);
            accountMap.put("whirlpool", accounts[1]);
            accountMap.put("collectProtocolFeesAuthority", accounts[2]);
            accountMap.put("tokenVaultA", accounts[3]);
            accountMap.put("tokenVaultB", accounts[4]);
            accountMap.put("tokenDestinationA", accounts[5]);
            accountMap.put("tokenDestinationB", accounts[6]);
            accountMap.put("tokenProgram", accounts[7]);
        }
        info.put("accounts", accountMap);

        return info;
    }

    private static Map<String, Object> parseInitializeRewardV2(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 解析参数
        int rewardIndex = buffer.get() & 0xFF;
        info.put("rewardIndex", rewardIndex);

        // 解析账户
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 6) {
            accountMap.put("rewardAuthority", accounts[0]);
            accountMap.put("funder", accounts[1]);
            accountMap.put("whirlpool", accounts[2]);
            accountMap.put("rewardMint", accounts[3]);
            accountMap.put("rewardVault", accounts[4]);
            accountMap.put("tokenProgram", accounts[5]);
        }
        info.put("accounts", accountMap);

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

        // 解析账户
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 3) {
            accountMap.put("whirlpoolsConfig", accounts[0]);
            accountMap.put("rewardEmissionsSuperAuthority", accounts[1]);
            accountMap.put("newRewardEmissionsSuperAuthority", accounts[2]);
        }
        info.put("accounts", accountMap);

        return info;
    }

    private static Map<String, Object> parseSetRewardAuthorityBySuperAuthority(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 解析参数
        int rewardIndex = buffer.get() & 0xFF;
        info.put("rewardIndex", rewardIndex);

        // 解析账户
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 4) {
            accountMap.put("whirlpoolsConfig", accounts[0]);
            accountMap.put("whirlpool", accounts[1]);
            accountMap.put("rewardEmissionsSuperAuthority", accounts[2]);
            accountMap.put("newRewardAuthority", accounts[3]);
        }
        info.put("accounts", accountMap);

        return info;
    }

    private static Map<String, Object> parseTwoHopSwap(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 解析参数
        long amount = buffer.getLong();
        long otherAmountThreshold = buffer.getLong();
        boolean amountSpecifiedIsInput = buffer.get() != 0;
        boolean aToBOne = buffer.get() != 0;
        boolean aToBTwo = buffer.get() != 0;
        BigInteger sqrtPriceLimitOne = new BigInteger(buffer.getLong() + ""); // u128
        BigInteger sqrtPriceLimitTwo = new BigInteger(buffer.getLong() + ""); // u128

        info.put("amount", amount);
        info.put("otherAmountThreshold", otherAmountThreshold);
        info.put("amountSpecifiedIsInput", amountSpecifiedIsInput);
        info.put("aToBOne", aToBOne);
        info.put("aToBTwo", aToBTwo);
        info.put("sqrtPriceLimitOne", sqrtPriceLimitOne);
        info.put("sqrtPriceLimitTwo", sqrtPriceLimitTwo);

        // 解析账户
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 18) {
            accountMap.put("tokenProgram", accounts[0]);
            accountMap.put("tokenAuthority", accounts[1]);
            accountMap.put("whirlpoolOne", accounts[2]);
            accountMap.put("whirlpoolTwo", accounts[3]);
            accountMap.put("tokenOwnerAccountA", accounts[4]);
            accountMap.put("tokenVaultA", accounts[5]);
            accountMap.put("tokenOwnerAccountB", accounts[6]);
            accountMap.put("tokenVaultB", accounts[7]);
            accountMap.put("tokenOwnerAccountC", accounts[8]);
            accountMap.put("tokenVaultC", accounts[9]);
            accountMap.put("tickArray0", accounts[10]);
            accountMap.put("tickArray1", accounts[11]);
            accountMap.put("tickArray2", accounts[12]);
            accountMap.put("tickArray3", accounts[13]);
            accountMap.put("tickArray4", accounts[14]);
            accountMap.put("tickArray5", accounts[15]);
            accountMap.put("oracleOne", accounts[16]);
            accountMap.put("oracleTwo", accounts[17]);
        }
        info.put("accounts", accountMap);

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

        // 解析账户
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 8) {
            accountMap.put("positionAuthority", accounts[0]);     // 头寸授权账户
            accountMap.put("receiver", accounts[1]);              // 接收者账户
            accountMap.put("position", accounts[2]);              // 头寸账户
            accountMap.put("positionMint", accounts[3]);          // 头寸NFT铸币账户
            accountMap.put("positionTokenAccount", accounts[4]);   // 头寸代币账户
            accountMap.put("tokenProgram", accounts[5]);          // SPL Token程序
            accountMap.put("whirlpool", accounts[6]);             // 池子账户
            accountMap.put("systemProgram", accounts[7]);         // System程序
        }
        info.put("accounts", accountMap);

        return info;
    }
}