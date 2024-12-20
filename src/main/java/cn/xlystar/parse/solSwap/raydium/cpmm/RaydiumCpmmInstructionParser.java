package cn.xlystar.parse.solSwap.raydium.cpmm;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class RaydiumCpmmInstructionParser {

    private static final String PROGRAM_ID = "CPMMoo8L3F4NbTegBCKVNunggL7H1ZpdTHKxQB5qKP1C";

    public static Map<String, Object> parseInstruction(byte[] data, String[] accounts) {
        Map<String, Object> result = new HashMap<>();

        if (data == null || data.length == 0) {
            result.put("error", "Invalid instruction data");
            return result;
        }

        try {
            int discriminator = data[0] & 0xFF;
            RaydiumCpmmInstruction instructionType = RaydiumCpmmInstruction.fromValue(discriminator);
            result.put("type", instructionType.name());

            ByteBuffer buffer = ByteBuffer.wrap(data, 1, data.length - 1);
            Map<String, Object> info = parseInstructionInfo(instructionType, buffer, accounts);
            result.put("info", info);

        } catch (Exception e) {
            result.put("error", "Failed to parse instruction: " + e.getMessage());
        }

        return result;
    }

    private static Map<String, Object> parseInstructionInfo(RaydiumCpmmInstruction instruction, ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        try {
            switch (instruction) {
                case CREATE_AMM_CONFIG: // 0
                    return parseCreateAmmConfig(buffer, accounts);
                case UPDATE_AMM_CONFIG: // 1
                    return parseUpdateAmmConfig(buffer, accounts);
                case UPDATE_POOL_STATUS: // 2
                    return parseUpdatePoolStatus(buffer, accounts);
                case COLLECT_PROTOCOL_FEE: // 3
                    return parseCollectProtocolFee(buffer, accounts);
                case COLLECT_FUND_FEE: // 4
                    return parseCollectFundFee(buffer, accounts);
                case INITIALIZE: // 5
                    return parseInitialize(buffer, accounts);
                case DEPOSIT: // 6
                    return parseDeposit(buffer, accounts);
                case WITHDRAW: // 7
                    return parseWithdraw(buffer, accounts);
                case SWAP_BASE_IN: // 8
                    return parseSwapBaseIn(buffer, accounts);
                case SWAP_BASE_OUT: // 9
                    return parseSwapBaseOut(buffer, accounts);
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
        long tradeFeeRate = buffer.getLong();
        long protocolFeeRate = buffer.getLong();
        long fundFeeRate = buffer.getLong();
        long createPoolFee = buffer.getLong();

        info.put("index", index);
        info.put("tradeFeeRate", tradeFeeRate);
        info.put("protocolFeeRate", protocolFeeRate);
        info.put("fundFeeRate", fundFeeRate);
        info.put("createPoolFee", createPoolFee);
        info.put("accounts", parseCreateAmmConfigAccounts(accounts));

        return info;
    }

    private static Map<String, Object> parseUpdateAmmConfig(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        int param = buffer.get() & 0xFF;
        long value = buffer.getLong();

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
    private static Map<String, String> parseCreateAmmConfigAccounts(String[] accounts) {
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 3) {
            accountMap.put("admin", accounts[0]);        // 管理员账户
            accountMap.put("ammConfig", accounts[1]);    // AMM配置账户
            accountMap.put("systemProgram", accounts[2]); // 系统程序
        }
        return accountMap;
    }

    private static Map<String, String> parseUpdateAmmConfigAccounts(String[] accounts) {
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 2) {
            accountMap.put("admin", accounts[0]);     // 管理员账户
            accountMap.put("ammConfig", accounts[1]); // AMM配置账户
        }
        return accountMap;
    }

    private static Map<String, String> parseUpdatePoolStatusAccounts(String[] accounts) {
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 2) {
            accountMap.put("admin", accounts[0]);  // 管理员账户
            accountMap.put("pool", accounts[1]);   // 流动性池账户
        }
        return accountMap;
    }

    private static Map<String, String> parseCollectProtocolFeeAccounts(String[] accounts) {
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 7) {
            accountMap.put("admin", accounts[0]);                  // 管理员账户
            accountMap.put("pool", accounts[1]);                   // 流动性池账户
            accountMap.put("tokenVault0", accounts[2]);           // 代币0金库
            accountMap.put("tokenVault1", accounts[3]);           // 代币1金库
            accountMap.put("recipientTokenAccount0", accounts[4]); // 接收者代币0账户
            accountMap.put("recipientTokenAccount1", accounts[5]); // 接收者代币1账户
            accountMap.put("tokenProgram", accounts[6]);          // 代币程序
        }
        return accountMap;
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

    private static Map<String, Object> parseInitialize(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        long initAmount0 = buffer.getLong();
        long initAmount1 = buffer.getLong();
        long openTime = buffer.getLong();

        info.put("initAmount0", initAmount0);
        info.put("initAmount1", initAmount1);
        info.put("openTime", openTime);
        info.put("accounts", parseInitializeAccounts(accounts));

        return info;
    }

    private static Map<String, Object> parseDeposit(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        long lpTokenAmount = buffer.getLong();
        long maximumToken0Amount = buffer.getLong();
        long maximumToken1Amount = buffer.getLong();

        info.put("lpTokenAmount", lpTokenAmount);
        info.put("maximumToken0Amount", maximumToken0Amount);
        info.put("maximumToken1Amount", maximumToken1Amount);
        info.put("accounts", parseDepositAccounts(accounts));

        return info;
    }

    private static Map<String, Object> parseWithdraw(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        long lpTokenAmount = buffer.getLong();
        long minimumToken0Amount = buffer.getLong();
        long minimumToken1Amount = buffer.getLong();

        info.put("lpTokenAmount", lpTokenAmount);
        info.put("minimumToken0Amount", minimumToken0Amount);
        info.put("minimumToken1Amount", minimumToken1Amount);
        info.put("accounts", parseWithdrawAccounts(accounts));

        return info;
    }

    private static Map<String, Object> parseSwapBaseIn(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        long amountIn = buffer.getLong();
        long minimumAmountOut = buffer.getLong();

        info.put("amountIn", amountIn);
        info.put("minimumAmountOut", minimumAmountOut);
        info.put("accounts", parseSwapAccounts(accounts));

        return info;
    }

    private static Map<String, Object> parseSwapBaseOut(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        long maximumAmountIn = buffer.getLong();
        long amountOut = buffer.getLong();

        info.put("maximumAmountIn", maximumAmountIn);
        info.put("amountOut", amountOut);
        info.put("accounts", parseSwapAccounts(accounts));

        return info;
    }

    // 账户解析辅助方法
    private static Map<String, String> parseCollectFundFeeAccounts(String[] accounts) {
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 7) {
            accountMap.put("fundOwner", accounts[0]);              // 资金所有者账户
            accountMap.put("pool", accounts[1]);                   // 流动性池账户
            accountMap.put("tokenVault0", accounts[2]);           // 代币0金库
            accountMap.put("tokenVault1", accounts[3]);           // 代币1金库
            accountMap.put("recipientTokenAccount0", accounts[4]); // 接收者代币0账户
            accountMap.put("recipientTokenAccount1", accounts[5]); // 接收者代币1账户
            accountMap.put("tokenProgram", accounts[6]);          // 代币程序
        }
        return accountMap;
    }

    private static Map<String, String> parseInitializeAccounts(String[] accounts) {
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 11) {
            accountMap.put("poolCreator", accounts[0]);           // 池创建者
            accountMap.put("ammConfig", accounts[1]);             // AMM配置账户
            accountMap.put("pool", accounts[2]);                  // 流动性池账户
            accountMap.put("lpMint", accounts[3]);               // LP代币铸币账户
            accountMap.put("tokenMint0", accounts[4]);           // 代币0铸币账户
            accountMap.put("tokenMint1", accounts[5]);           // 代币1铸币账户
            accountMap.put("tokenVault0", accounts[6]);          // 代币0金库
            accountMap.put("tokenVault1", accounts[7]);          // 代币1金库
            accountMap.put("lpTokenAccount", accounts[8]);       // LP代币账户
            accountMap.put("tokenProgram", accounts[9]);         // 代币程序
            accountMap.put("systemProgram", accounts[10]);       // 系统程序
        }
        return accountMap;
    }

    private static Map<String, String> parseDepositAccounts(String[] accounts) {
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 9) {
            accountMap.put("depositor", accounts[0]);            // 存款人账户
            accountMap.put("pool", accounts[1]);                 // 流动性池账户
            accountMap.put("lpMint", accounts[2]);              // LP代币铸币账户
            accountMap.put("tokenAccount0", accounts[3]);        // 用户代币0账户
            accountMap.put("tokenAccount1", accounts[4]);        // 用户代币1账户
            accountMap.put("tokenVault0", accounts[5]);         // 代币0金库
            accountMap.put("tokenVault1", accounts[6]);         // 代币1金库
            accountMap.put("lpTokenAccount", accounts[7]);      // LP代币账户
            accountMap.put("tokenProgram", accounts[8]);        // 代币程序
        }
        return accountMap;
    }

    private static Map<String, String> parseWithdrawAccounts(String[] accounts) {
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 9) {
            accountMap.put("owner", accounts[0]);               // LP代币所有者
            accountMap.put("pool", accounts[1]);                // 流动性池账户
            accountMap.put("lpMint", accounts[2]);             // LP代币铸币账户
            accountMap.put("tokenAccount0", accounts[3]);       // 用户代币0账户
            accountMap.put("tokenAccount1", accounts[4]);       // 用户代币1账户
            accountMap.put("tokenVault0", accounts[5]);        // 代币0金库
            accountMap.put("tokenVault1", accounts[6]);        // 代币1金库
            accountMap.put("lpTokenAccount", accounts[7]);     // LP代币账户
            accountMap.put("tokenProgram", accounts[8]);       // 代币程序
        }
        return accountMap;
    }

    private static Map<String, String> parseSwapAccounts(String[] accounts) {
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 8) {
            accountMap.put("swapper", accounts[0]);            // 交换发起者
            accountMap.put("pool", accounts[1]);               // 流动性池账户
            accountMap.put("inputTokenAccount", accounts[2]);  // 输入代币账户
            accountMap.put("outputTokenAccount", accounts[3]); // 输出代币账户
            accountMap.put("inputVault", accounts[4]);        // 输入代币金库
            accountMap.put("outputVault", accounts[5]);       // 输出代币金库
            accountMap.put("tokenProgram", accounts[6]);      // 代币程序
            accountMap.put("ammConfig", accounts[7]);         // AMM配置账户
        }
        return accountMap;
    }
} 