package cn.xlystar.parse.solSwap.raydium.cpmm;

import cn.xlystar.parse.solSwap.InstructionParser;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class RaydiumCpmmInstructionParser extends InstructionParser {

    public static final String PROGRAM_ID = "CPMMoo8L3F4NbTegBCKVNunggL7H1ZpdTHKxQB5qKP1C";

    public String getMethodId(ByteBuffer buffer) {
        return Long.toUnsignedString(buffer.getLong());
    }

    @Override
    public Map<String, Object> matchInstruction(String methodId, ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info;
        switch (RaydiumCpmmInstruction.fromValue(methodId)) {
            case CREATE_AMM_CONFIG: // 0
                info = parseCreateAmmConfig(buffer, accounts);
                break;
            case UPDATE_AMM_CONFIG: // 1
                info = parseUpdateAmmConfig(buffer, accounts);
                break;
            case UPDATE_POOL_STATUS: // 2
                info = parseUpdatePoolStatus(buffer, accounts);
                break;
            case COLLECT_PROTOCOL_FEE: // 3
                info = parseCollectProtocolFee(buffer, accounts);
                break;
            case COLLECT_FUND_FEE: // 4
                info = parseCollectFundFee(buffer, accounts);
                break;
            case INITIALIZE: // 5
                info = parseInitialize(buffer, accounts);
                break;
            case DEPOSIT: // 6
                info = parseDeposit(buffer, accounts);
                break;
            case WITHDRAW: // 7
                info = parseWithdraw(buffer, accounts);
                break;
            case SWAP_BASE_IN: // 8
                info = parseSwapBaseIn(buffer, accounts);
                break;
            case SWAP_BASE_OUT: // 9
                info = parseSwapBaseOut(buffer, accounts);
                break;
            default:
                return new HashMap<>();
        }
        return info;
    }

    private static Map<String, Object> parseCreateAmmConfig(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        // 设置返回信息
        info.put("index", Short.toUnsignedInt(buffer.getShort()));
        info.put("tradeFeeRate", Long.toUnsignedString(buffer.getLong()));
        info.put("protocolFeeRate", Long.toUnsignedString(buffer.getLong()));
        info.put("fundFeeRate", Long.toUnsignedString(buffer.getLong()));
        info.put("createPoolFee", Long.toUnsignedString(buffer.getLong()));

        // 解析账户信息
        info.put("owner", accounts[0]); // Owner account
        info.put("ammConfig", accounts[1]); // AMM config account
        info.put("systemProgram", accounts[2]); // System program account

        return info;
    }

    private static Map<String, Object> parseUpdateAmmConfig(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        // 设置返回信息
        info.put("param", Byte.toUnsignedInt(buffer.get()));
        info.put("value", Long.toUnsignedString(buffer.getLong()));

        // 解析账户信息
        info.put("owner", accounts[0]); // Owner account
        info.put("ammConfig", accounts[1]); // AMM config account

        return info;
    }

    private static Map<String, Object> parseUpdatePoolStatus(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        // 设置返回信息
        info.put("status", Byte.toUnsignedInt(buffer.get()));

        // 解析账户信息
        info.put("authority", accounts[0]); // Authority account
        info.put("poolState", accounts[1]); // Pool state account
        return info;
    }

    private static Map<String, Object> parseCollectProtocolFee(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        info.put("amount0Requested", Long.toUnsignedString(buffer.getLong()));
        info.put("amount1Requested", Long.toUnsignedString(buffer.getLong()));

        // 解析账户信息
        info.put("owner", accounts[0]); // Owner account
        info.put("authority", accounts[1]); // Authority account
        info.put("poolState", accounts[2]); // Pool state account
        info.put("ammConfig", accounts[3]); // AMM config account
        info.put("token0Vault", accounts[4]); // Token 0 vault account
        info.put("token1Vault", accounts[5]); // Token 1 vault account
        info.put("vault0Mint", accounts[6]); // Vault 0 mint account
        info.put("vault1Mint", accounts[7]); // Vault 1 mint account
        info.put("recipientToken0Account", accounts[8]); // Recipient token 0 account
        info.put("recipientToken1Account", accounts[9]); // Recipient token 1 account
        info.put("tokenProgram", accounts[10]); // Token program
        info.put("tokenProgram2022", accounts[11]); // Token program 2022
        return info;
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
        // 设置返回信息
        info.put("amount0Requested", Long.toUnsignedString(buffer.getLong()));
        info.put("amount1Requested", Long.toUnsignedString(buffer.getLong()));

        // 解析账户信息
        info.put("owner", accounts[0]); // Owner account
        info.put("authority", accounts[1]); // Authority account
        info.put("poolState", accounts[2]); // Pool state account
        info.put("ammConfig", accounts[3]); // AMM config account
        info.put("token0Vault", accounts[4]); // Token 0 vault account
        info.put("token1Vault", accounts[5]); // Token 1 vault account
        info.put("vault0Mint", accounts[6]); // Vault 0 mint account
        info.put("vault1Mint", accounts[7]); // Vault 1 mint account
        info.put("recipientToken0Account", accounts[8]); // Recipient token 0 account
        info.put("recipientToken1Account", accounts[9]); // Recipient token 1 account
        info.put("tokenProgram", accounts[10]); // Token program
        info.put("tokenProgram2022", accounts[11]); // Token program 2022

        return info;
    }

    private static Map<String, Object> parseInitialize(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 设置返回信息
        info.put("initAmount0", Long.toUnsignedString(buffer.getLong()));
        info.put("initAmount1", Long.toUnsignedString(buffer.getLong()));
        info.put("openTime", Long.toUnsignedString(buffer.getLong()));

        // 解析账户信息
        info.put("creator", accounts[0]); // Creator account
        info.put("ammConfig", accounts[1]); // AMM config account
        info.put("authority", accounts[2]); // Authority account
        info.put("poolState", accounts[3]); // Pool state account
        info.put("token0Mint", accounts[4]); // Token 0 mint account
        info.put("token1Mint", accounts[5]); // Token 1 mint account
        info.put("lpMint", accounts[6]); // LP mint account
        info.put("creatorToken0", accounts[7]); // Creator token 0 account
        info.put("creatorToken1", accounts[8]); // Creator token 1 account
        info.put("creatorLpToken", accounts[9]); // Creator LP token account
        info.put("token0Vault", accounts[10]); // Token 0 vault account
        info.put("token1Vault", accounts[11]); // Token 1 vault account
        info.put("createPoolFee", accounts[12]); // Create pool fee account
        info.put("observationState", accounts[13]); // Observation state account
        info.put("tokenProgram", accounts[14]); // Token program
        info.put("token0Program", accounts[15]); // Token 0 program
        info.put("token1Program", accounts[16]); // Token 1 program
        info.put("associatedTokenProgram", accounts[17]); // Associated token program
        info.put("systemProgram", accounts[18]); // System program
        info.put("rent", accounts[19]); // Rent sysvar
        return info;
    }

    private static Map<String, Object> parseDeposit(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 设置返回信息
        info.put("lpTokenAmount", Long.toUnsignedString(buffer.getLong()));
        info.put("maximumToken0Amount", Long.toUnsignedString(buffer.getLong()));
        info.put("maximumToken1Amount", Long.toUnsignedString(buffer.getLong()));

        // 解析账户信息
        info.put("owner", accounts[0]); // Owner account
        info.put("authority", accounts[1]); // Authority account
        info.put("poolState", accounts[2]); // Pool state account
        info.put("ownerLpToken", accounts[3]); // Owner LP token account
        info.put("token0Account", accounts[4]); // Token 0 account
        info.put("token1Account", accounts[5]); // Token 1 account
        info.put("token0Vault", accounts[6]); // Token 0 vault account
        info.put("token1Vault", accounts[7]); // Token 1 vault account
        info.put("tokenProgram", accounts[8]); // Token program
        info.put("tokenProgram2022", accounts[9]); // Token program 2022
        info.put("vault0Mint", accounts[10]); // Vault 0 mint account
        info.put("vault1Mint", accounts[11]); // Vault 1 mint account
        info.put("lpMint", accounts[12]); // LP mint account
        return info;
    }

    private static Map<String, Object> parseWithdraw(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 设置返回信息
        info.put("lpTokenAmount", Long.toUnsignedString(buffer.getLong()));
        info.put("minimumToken0Amount", Long.toUnsignedString(buffer.getLong()));
        info.put("minimumToken1Amount", Long.toUnsignedString(buffer.getLong()));

        // 解析账户信息
        info.put("owner", accounts[0]); // Owner account
        info.put("authority", accounts[1]); // Authority account
        info.put("poolState", accounts[2]); // Pool state account
        info.put("ownerLpToken", accounts[3]); // Owner LP token account
        info.put("token0Account", accounts[4]); // Token 0 account
        info.put("token1Account", accounts[5]); // Token 1 account
        info.put("token0Vault", accounts[6]); // Token 0 vault account
        info.put("token1Vault", accounts[7]); // Token 1 vault account
        info.put("tokenProgram", accounts[8]); // Token program
        info.put("tokenProgram2022", accounts[9]); // Token program 2022
        info.put("vault0Mint", accounts[10]); // Vault 0 mint account
        info.put("vault1Mint", accounts[11]); // Vault 1 mint account
        info.put("lpMint", accounts[12]); // LP mint account
        info.put("memoProgram", accounts[13]); // Memo program
        return info;
    }

    private static Map<String, Object> parseSwapBaseIn(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        // 设置返回信息
        info.put("amountIn", Long.toUnsignedString(buffer.getLong()));
        info.put("minimumAmountOut", Long.toUnsignedString(buffer.getLong()));

        // 解析账户信息
        info.put("payer", accounts[0]); // Payer account
        info.put("authority", accounts[1]); // Authority account
        info.put("ammConfig", accounts[2]); // AMM config account
        info.put("poolState", accounts[3]); // Pool state account
        info.put("inputTokenAccount", accounts[4]); // Input token account
        info.put("outputTokenAccount", accounts[5]); // Output token account
        info.put("inputVault", accounts[6]); // Input vault account
        info.put("outputVault", accounts[7]); // Output vault account
        info.put("inputTokenProgram", accounts[8]); // Input token program
        info.put("outputTokenProgram", accounts[9]); // Output token program
        info.put("inputTokenMint", accounts[10]); // Input token mint account
        info.put("outputTokenMint", accounts[11]); // Output token mint account
        info.put("observationState", accounts[12]); // Observation state account

        return info;
    }

    private static Map<String, Object> parseSwapBaseOut(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 设置返回信息
        info.put("maxAmountIn", Long.toUnsignedString(buffer.getLong()));
        info.put("amountOut", Long.toUnsignedString(buffer.getLong()));

        // 解析账户信息
        info.put("payer", accounts[0]); // Payer account
        info.put("authority", accounts[1]); // Authority account
        info.put("ammConfig", accounts[2]); // AMM config account
        info.put("poolState", accounts[3]); // Pool state account
        info.put("inputTokenAccount", accounts[4]); // Input token account
        info.put("outputTokenAccount", accounts[5]); // Output token account
        info.put("inputVault", accounts[6]); // Input vault account
        info.put("outputVault", accounts[7]); // Output vault account
        info.put("inputTokenProgram", accounts[8]); // Input token program
        info.put("outputTokenProgram", accounts[9]); // Output token program
        info.put("inputTokenMint", accounts[10]); // Input token mint account
        info.put("outputTokenMint", accounts[11]); // Output token mint account
        info.put("observationState", accounts[12]); // Observation state account

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