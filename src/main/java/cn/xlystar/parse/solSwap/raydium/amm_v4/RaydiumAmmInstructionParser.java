package cn.xlystar.parse.solSwap.raydium.amm_v4;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class RaydiumAmmInstructionParser {
    
    private static final String PROGRAM_ID = "675kPX9MHTjS2zt1qfr1NYHuzeLXfQM9H24wFSUt1Mp8";

    public static Map<String, Object> parseInstruction(byte[] data, String[] accounts) {
        Map<String, Object> result = new HashMap<>();
        
        if (data == null || data.length == 0) {
            result.put("error", "Invalid instruction data");
            return result;
        }

        try {
            int discriminator = data[0] & 0xFF;
            RaydiumAmmInstruction instructionType = RaydiumAmmInstruction.fromValue(discriminator);
            result.put("type", instructionType.name());

            ByteBuffer buffer = ByteBuffer.wrap(data, 1, data.length - 1);
            Map<String, Object> info = parseInstructionInfo(instructionType, buffer, accounts);
            result.put("info", info);
            
        } catch (Exception e) {
            result.put("error", "Failed to parse instruction: " + e.getMessage());
        }
        
        return result;
    }

    private static Map<String, Object> parseInstructionInfo(RaydiumAmmInstruction instruction, ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        
        try {
            switch (instruction) {
                case INITIALIZE:
                    return parseInitialize(buffer, accounts);
                case INITIALIZE2:
                    return parseInitialize2(buffer, accounts);
                case PRE_INITIALIZE:
                    return parsePreInitialize(buffer, accounts);
                case SWAP_BASE_IN:
                    return parseSwapBaseIn(buffer, accounts);
                case SWAP_BASE_OUT:
                    return parseSwapBaseOut(buffer, accounts);
                case DEPOSIT:
                    return parseDeposit(buffer, accounts);
                case WITHDRAW:
                    return parseWithdraw(buffer, accounts);
                case WITHDRAW_PNL:
                    return parseWithdrawPnl(buffer, accounts);
                case WITHDRAW_SRM:
                    return parseWithdrawSrm(buffer, accounts);
                case CREATE_CONFIG_ACCOUNT:
                    return parseCreateConfigAccount(buffer, accounts);
                case UPDATE_CONFIG_ACCOUNT:
                    return parseUpdateConfigAccount(buffer, accounts);
                case SET_PARAMS:
                    return parseSetParams(buffer, accounts);
                case MONITOR_STEP:
                    return parseMonitorStep(buffer, accounts);
                case SIMULATE_INFO:
                    return parseSimulateInfo(buffer, accounts);
                case MIGRATE_TO_OPEN_BOOK:
                    return parseMigrateToOpenBook(buffer, accounts);
                case ADMIN_CANCEL_ORDERS:
                    return parseAdminCancelOrders(buffer, accounts);
                default:
                    info.put("error", "Unknown instruction type: " + instruction.name());
                    return info;
            }
        } catch (Exception e) {
            info.put("error", "Failed to parse " + instruction.name() + " parameters: " + e.getMessage());
            return info;
        }
    }

    private static Map<String, Object> parseInitialize(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        int nonce = buffer.getInt();
        long openTime = buffer.getLong();

        info.put("nonce", nonce);
        info.put("openTime", openTime);
        info.put("accounts", parseInitializeAccounts(accounts));

        return info;
    }

    private static Map<String, Object> parseInitialize2(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        long startTime = buffer.getLong();
        int poolNonce = buffer.getInt();
        
        info.put("startTime", startTime);
        info.put("poolNonce", poolNonce);
        info.put("accounts", parseInitialize2Accounts(accounts));
        
        return info;
    }

    private static Map<String, Object> parsePreInitialize(ByteBuffer buffer, String[] accounts) {
        // 参数验证
        if (accounts.length < 15) {
            throw new IllegalArgumentException("PreInitialize instruction requires at least 15 accounts");
        }

        Map<String, Object> info = new HashMap<>();

        // 解析nonce值
        int nonce = buffer.getInt();
        info.put("nonce", nonce);

        // 解析账户信息
        Map<String, String> accountMap = new HashMap<>();
        accountMap.put("tokenProgram", accounts[0]);
        accountMap.put("systemProgram", accounts[1]);
        accountMap.put("rent", accounts[2]);
        accountMap.put("ammTargetOrders", accounts[3]);
        accountMap.put("poolWithdrawQueue", accounts[4]);
        accountMap.put("ammAuthority", accounts[5]);
        accountMap.put("lpMintAddress", accounts[6]);
        accountMap.put("coinMintAddress", accounts[7]);
        accountMap.put("pcMintAddress", accounts[8]);
        accountMap.put("poolCoinTokenAccount", accounts[9]);
        accountMap.put("poolPcTokenAccount", accounts[10]);
        accountMap.put("poolTempLpTokenAccount", accounts[11]);
        accountMap.put("serumMarket", accounts[12]);
        accountMap.put("userWallet", accounts[13]);
        accountMap.put("splAssociatedTokenAccount", accounts[14]);

        info.put("accounts", accountMap);

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
    private static Map<String, String> parseInitializeAccounts(String[] accounts) {
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 4) {
            accountMap.put("authority", accounts[0]);
            accountMap.put("amm", accounts[1]);
            accountMap.put("tokenA", accounts[2]);
            accountMap.put("tokenB", accounts[3]);
        }
        return accountMap;
    }

    private static Map<String, String> parseInitialize2Accounts(String[] accounts) {
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 5) {
            accountMap.put("authority", accounts[0]);
            accountMap.put("amm", accounts[1]);
            accountMap.put("tokenA", accounts[2]);
            accountMap.put("tokenB", accounts[3]);
            accountMap.put("lpMint", accounts[4]);
        }
        return accountMap;
    }

    private static Map<String, String> parseSwapAccounts(String[] accounts) {
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 6) {
            accountMap.put("amm", accounts[0]);
            accountMap.put("authority", accounts[1]);
            accountMap.put("sourceToken", accounts[2]);
            accountMap.put("destinationToken", accounts[3]);
            accountMap.put("poolTokenA", accounts[4]);
            accountMap.put("poolTokenB", accounts[5]);
        }
        return accountMap;
    }
    /**
     * 解析存款指令
     * @param buffer 包含存款参数的数据缓冲区
     * @param accounts 必需的账户列表:
     *                 - tokenProgram: Token程序ID
     *                 - ammId: AMM账户
     *                 - ammAuthority: AMM权限账户
     *                 - ammOpenOrders: AMM开放订单账户
     *                 - lpMintAddress: LP代币铸造地址
     *                 - poolCoinTokenAccount: 池子代币A账户
     *                 - poolPcTokenAccount: 池子代币B账户
     *                 - serumMarket: Serum市场账户
     *                 - userCoinTokenAccount: 用户代币A账户
     *                 - userPcTokenAccount: 用户代币B账户
     *                 - userLpTokenAccount: 用户LP代币账户
     *                 - userOwner: 用户所有者账户
     */
    private static Map<String, Object> parseDeposit(ByteBuffer buffer, String[] accounts) {
        if (accounts.length < 12) {
            throw new IllegalArgumentException("Deposit instruction requires at least 12 accounts");
        }

        Map<String, Object> info = new HashMap<>();
        long maxCoinAmount = buffer.getLong();    // 最大代币A数量
        long maxPcAmount = buffer.getLong();      // 最大代币B数量
        long baseLpAmount = buffer.getLong();     // 基础LP代币数量

        info.put("maxCoinAmount", maxCoinAmount);
        info.put("maxPcAmount", maxPcAmount);
        info.put("baseLpAmount", baseLpAmount);

        Map<String, String> accountMap = new HashMap<>();
        accountMap.put("tokenProgram", accounts[0]);
        accountMap.put("ammId", accounts[1]);
        accountMap.put("ammAuthority", accounts[2]);
        accountMap.put("ammOpenOrders", accounts[3]);
        accountMap.put("lpMintAddress", accounts[4]);
        accountMap.put("poolCoinTokenAccount", accounts[5]);
        accountMap.put("poolPcTokenAccount", accounts[6]);
        accountMap.put("serumMarket", accounts[7]);
        accountMap.put("userCoinTokenAccount", accounts[8]);
        accountMap.put("userPcTokenAccount", accounts[9]);
        accountMap.put("userLpTokenAccount", accounts[10]);
        accountMap.put("userOwner", accounts[11]);

        info.put("accounts", accountMap);
        return info;
    }

    /**
     * 解析提现指令
     * @param buffer 包含提现参数的数据缓冲区
     * @param accounts 必需的账户列表:
     *                 - tokenProgram: Token程序ID
     *                 - ammId: AMM账户
     *                 - ammAuthority: AMM权限账户
     *                 - ammOpenOrders: AMM开放订单账户
     *                 - lpMintAddress: LP代币铸造地址
     *                 - poolCoinTokenAccount: 池子代币A账户
     *                 - poolPcTokenAccount: 池子代币B账户
     *                 - poolWithdrawQueue: 池子提现队列
     *                 - serumMarket: Serum市场账户
     *                 - userLpTokenAccount: 用户LP代币账户
     *                 - userCoinTokenAccount: 用户代币A账户
     *                 - userPcTokenAccount: 用户代币B账户
     *                 - userOwner: 用户所有者账户
     */
    private static Map<String, Object> parseWithdraw(ByteBuffer buffer, String[] accounts) {
        if (accounts.length < 13) {
            throw new IllegalArgumentException("Withdraw instruction requires at least 13 accounts");
        }

        Map<String, Object> info = new HashMap<>();
        long amount = buffer.getLong();    // 提现金额

        info.put("amount", amount);

        Map<String, String> accountMap = new HashMap<>();
        accountMap.put("tokenProgram", accounts[0]);
        accountMap.put("ammId", accounts[1]);
        accountMap.put("ammAuthority", accounts[2]);
        accountMap.put("ammOpenOrders", accounts[3]);
        accountMap.put("lpMintAddress", accounts[4]);
        accountMap.put("poolCoinTokenAccount", accounts[5]);
        accountMap.put("poolPcTokenAccount", accounts[6]);
        accountMap.put("poolWithdrawQueue", accounts[7]);
        accountMap.put("serumMarket", accounts[8]);
        accountMap.put("userLpTokenAccount", accounts[9]);
        accountMap.put("userCoinTokenAccount", accounts[10]);
        accountMap.put("userPcTokenAccount", accounts[11]);
        accountMap.put("userOwner", accounts[12]);

        info.put("accounts", accountMap);
        return info;
    }

    /**
     * 解析提取收益指令
     * @param buffer 包含提取收益参数的数据缓冲区
     * @param accounts 必需的账户列表:
     *                 - tokenProgram: Token程序ID
     *                 - ammId: AMM账户
     *                 - ammAuthority: AMM权限账户
     *                 - pnlPool: 收益池账户
     *                 - destination: 目标账户
     */
    private static Map<String, Object> parseWithdrawPnl(ByteBuffer buffer, String[] accounts) {
        if (accounts.length < 5) {
            throw new IllegalArgumentException("WithdrawPnl instruction requires at least 5 accounts");
        }

        Map<String, Object> info = new HashMap<>();
        long amount = buffer.getLong();    // 提取金额

        info.put("amount", amount);

        Map<String, String> accountMap = new HashMap<>();
        accountMap.put("tokenProgram", accounts[0]);
        accountMap.put("ammId", accounts[1]);
        accountMap.put("ammAuthority", accounts[2]);
        accountMap.put("pnlPool", accounts[3]);
        accountMap.put("destination", accounts[4]);

        info.put("accounts", accountMap);
        return info;
    }

    /**
     * 解析提取SRM代币指令
     * @param buffer 包含提取参数的数据缓冲区
     * @param accounts 必需的账户列表:
     *                 - tokenProgram: Token程序ID
     *                 - ammId: AMM账户
     *                 - ammAuthority: AMM权限账户
     *                 - srmToken: SRM代币账户
     *                 - destination: 目标账户
     *                 - userOwner: 用户所有者账户
     */
    private static Map<String, Object> parseWithdrawSrm(ByteBuffer buffer, String[] accounts) {
        if (accounts.length < 6) {
            throw new IllegalArgumentException("WithdrawSrm instruction requires at least 6 accounts");
        }

        Map<String, Object> info = new HashMap<>();
        long amount = buffer.getLong();    // 提取金额

        info.put("amount", amount);

        Map<String, String> accountMap = new HashMap<>();
        accountMap.put("tokenProgram", accounts[0]);
        accountMap.put("ammId", accounts[1]);
        accountMap.put("ammAuthority", accounts[2]);
        accountMap.put("srmToken", accounts[3]);
        accountMap.put("destination", accounts[4]);
        accountMap.put("userOwner", accounts[5]);

        info.put("accounts", accountMap);
        return info;
    }

    /**
     * 解析创建配置账户指令
     * @param buffer 包含配置参数的数据缓冲区
     * @param accounts 必需的账户列表:
     *                 - systemProgram: System程序ID
     *                 - authority: 权限账户
     *                 - config: 配置账户
     *                 - payer: 支付账户
     *                 - rent: 租金账户
     */
    private static Map<String, Object> parseCreateConfigAccount(ByteBuffer buffer, String[] accounts) {
        if (accounts.length < 5) {
            throw new IllegalArgumentException("CreateConfigAccount instruction requires at least 5 accounts");
        }

        Map<String, Object> info = new HashMap<>();
        int configVersion = buffer.getInt();    // 配置版本

        info.put("configVersion", configVersion);

        Map<String, String> accountMap = new HashMap<>();
        accountMap.put("systemProgram", accounts[0]);
        accountMap.put("authority", accounts[1]);
        accountMap.put("config", accounts[2]);
        accountMap.put("payer", accounts[3]);
        accountMap.put("rent", accounts[4]);

        info.put("accounts", accountMap);
        return info;
    }

    /**
     * 解析更新配置账户指令
     * @param buffer 包含更新参数的数据缓冲区
     * @param accounts 必需的账户列表:
     *                 - authority: 权限账户
     *                 - config: 配置账户
     *                 - newAuthority: 新权限账户（可选）
     */
    private static Map<String, Object> parseUpdateConfigAccount(ByteBuffer buffer, String[] accounts) {
        if (accounts.length < 2) {
            throw new IllegalArgumentException("UpdateConfigAccount instruction requires at least 2 accounts");
        }

        Map<String, Object> info = new HashMap<>();
        int configVersion = buffer.getInt();    // 配置版本

        info.put("configVersion", configVersion);

        Map<String, String> accountMap = new HashMap<>();
        accountMap.put("authority", accounts[0]);
        accountMap.put("config", accounts[1]);
        if (accounts.length > 2) {
            accountMap.put("newAuthority", accounts[2]);
        }

        info.put("accounts", accountMap);
        return info;
    }

    /**
     * 解析设置参数指令
     * @param buffer 包含参数设置的数据缓冲区
     * @param accounts 必需的账户列表:
     *                 - authority: 权限账户
     *                 - ammId: AMM账户
     *                 - config: 配置账户（可选）
     */
    private static Map<String, Object> parseSetParams(ByteBuffer buffer, String[] accounts) {
        if (accounts.length < 2) {
            throw new IllegalArgumentException("SetParams instruction requires at least 2 accounts");
        }

        Map<String, Object> info = new HashMap<>();
        long param1 = buffer.getLong();    // 参数1
        long param2 = buffer.getLong();    // 参数2

        info.put("param1", param1);
        info.put("param2", param2);

        Map<String, String> accountMap = new HashMap<>();
        accountMap.put("authority", accounts[0]);
        accountMap.put("ammId", accounts[1]);
        if (accounts.length > 2) {
            accountMap.put("config", accounts[2]);
        }

        info.put("accounts", accountMap);
        return info;
    }

    /**
     * 解析监控步骤指令
     * @param buffer 包含监控参数的数据缓冲区
     * @param accounts 必需的账户列表:
     *                 - authority: 权限账户
     *                 - ammId: AMM账户
     *                 - ammAuthority: AMM权限账户
     *                 - monitor: 监控账户
     *                 - serumMarket: Serum市场账户
     *                 - openOrders: 开放订单账户
     */
    private static Map<String, Object> parseMonitorStep(ByteBuffer buffer, String[] accounts) {
        if (accounts.length < 6) {
            throw new IllegalArgumentException("MonitorStep instruction requires at least 6 accounts");
        }

        Map<String, Object> info = new HashMap<>();
        int step = buffer.getInt();        // 监控步骤
        long timestamp = buffer.getLong(); // 时间戳

        info.put("step", step);
        info.put("timestamp", timestamp);

        Map<String, String> accountMap = new HashMap<>();
        accountMap.put("authority", accounts[0]);
        accountMap.put("ammId", accounts[1]);
        accountMap.put("ammAuthority", accounts[2]);
        accountMap.put("monitor", accounts[3]);
        accountMap.put("serumMarket", accounts[4]);
        accountMap.put("openOrders", accounts[5]);

        info.put("accounts", accountMap);
        return info;
    }

    /**
     * 解析模拟信息指令
     * @param buffer 包含模拟参数的数据缓冲区
     * @param accounts 必需的账户列表:
     *                 - ammId: AMM账户
     *                 - ammAuthority: AMM权限账户
     *                 - simulator: 模拟器账户
     *                 - serumMarket: Serum市场账户
     *                 - poolCoinTokenAccount: 池子代币A账户
     *                 - poolPcTokenAccount: 池子代币B账户
     */
    private static Map<String, Object> parseSimulateInfo(ByteBuffer buffer, String[] accounts) {
        if (accounts.length < 6) {
            throw new IllegalArgumentException("SimulateInfo instruction requires at least 6 accounts");
        }

        Map<String, Object> info = new HashMap<>();
        long simulationAmount = buffer.getLong();    // 模拟金额
        int direction = buffer.getInt();             // 交易方向

        info.put("simulationAmount", simulationAmount);
        info.put("direction", direction);

        Map<String, String> accountMap = new HashMap<>();
        accountMap.put("ammId", accounts[0]);
        accountMap.put("ammAuthority", accounts[1]);
        accountMap.put("simulator", accounts[2]);
        accountMap.put("serumMarket", accounts[3]);
        accountMap.put("poolCoinTokenAccount", accounts[4]);
        accountMap.put("poolPcTokenAccount", accounts[5]);

        info.put("accounts", accountMap);
        return info;
    }

    /**
     * 解析迁移到OpenBook指令
     * @param buffer 包含迁移参数的数据缓冲区
     * @param accounts 必需的账户列表:
     *                 - authority: 权限账户
     *                 - ammId: AMM账户
     *                 - ammAuthority: AMM权限账户
     *                 - openBookMarket: OpenBook市场账户
     *                 - serumProgram: Serum程序ID
     *                 - oldOpenOrders: 旧开放订单账户
     *                 - newOpenOrders: 新开放订单账户
     *                 - marketAuthority: 市场权限账户
     */
    private static Map<String, Object> parseMigrateToOpenBook(ByteBuffer buffer, String[] accounts) {
        if (accounts.length < 8) {
            throw new IllegalArgumentException("MigrateToOpenBook instruction requires at least 8 accounts");
        }

        Map<String, Object> info = new HashMap<>();
        long migrationFlags = buffer.getLong();    // 迁移标志

        info.put("migrationFlags", migrationFlags);

        Map<String, String> accountMap = new HashMap<>();
        accountMap.put("authority", accounts[0]);
        accountMap.put("ammId", accounts[1]);
        accountMap.put("ammAuthority", accounts[2]);
        accountMap.put("openBookMarket", accounts[3]);
        accountMap.put("serumProgram", accounts[4]);
        accountMap.put("oldOpenOrders", accounts[5]);
        accountMap.put("newOpenOrders", accounts[6]);
        accountMap.put("marketAuthority", accounts[7]);

        info.put("accounts", accountMap);
        return info;
    }

    /**
     * 解析管理员取消订单指令
     * @param buffer 包含取消订单参数的数据缓冲区
     * @param accounts 必需的账户列表:
     *                 - authority: 权限账户
     *                 - ammId: AMM账户
     *                 - ammAuthority: AMM权限账户
     *                 - market: 市场账户
     *                 - openOrders: 开放订单账户
     *                 - eventQueue: 事件队列账户
     *                 - bids: 买单账户
     *                 - asks: 卖单账户
     */
    private static Map<String, Object> parseAdminCancelOrders(ByteBuffer buffer, String[] accounts) {
        if (accounts.length < 8) {
            throw new IllegalArgumentException("AdminCancelOrders instruction requires at least 8 accounts");
        }

        Map<String, Object> info = new HashMap<>();
        int limit = buffer.getInt();       // 取消订单数量限制

        info.put("limit", limit);

        Map<String, String> accountMap = new HashMap<>();
        accountMap.put("authority", accounts[0]);
        accountMap.put("ammId", accounts[1]);
        accountMap.put("ammAuthority", accounts[2]);
        accountMap.put("market", accounts[3]);
        accountMap.put("openOrders", accounts[4]);
        accountMap.put("eventQueue", accounts[5]);
        accountMap.put("bids", accounts[6]);
        accountMap.put("asks", accounts[7]);

        info.put("accounts", accountMap);
        return info;
    }


} 