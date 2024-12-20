package cn.xlystar.parse.solSwap.raydium;

import cn.xlystar.parse.solSwap.raydium.amm_v4.RaydiumAmmInstruction;

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
        Map<String, Object> info = new HashMap<>();
        int nonce = buffer.getInt();

        info.put("nonce", nonce);
        info.put("accounts", parsePreInitializeAccounts(accounts));

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

    private static Map<String, Object> parseDeposit(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        long maxCoinAmount = buffer.getLong();
        long maxPcAmount = buffer.getLong();
        long baseLpAmount = buffer.getLong();

        info.put("maxCoinAmount", maxCoinAmount);
        info.put("maxPcAmount", maxPcAmount);
        info.put("baseLpAmount", baseLpAmount);
        info.put("accounts", parseDepositAccounts(accounts));

        return info;
    }

    private static Map<String, Object> parseWithdraw(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        long amount = buffer.getLong();

        info.put("amount", amount);
        info.put("accounts", parseWithdrawAccounts(accounts));

        return info;
    }

    private static Map<String, Object> parseWithdrawPnl(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        long amount = buffer.getLong();

        info.put("amount", amount);
        info.put("accounts", parseWithdrawPnlAccounts(accounts));

        return info;
    }

    private static Map<String, Object> parseWithdrawSrm(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        long amount = buffer.getLong();

        info.put("amount", amount);
        info.put("accounts", parseWithdrawSrmAccounts(accounts));

        return info;
    }

    private static Map<String, Object> parseCreateConfigAccount(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        int configVersion = buffer.getInt();

        info.put("configVersion", configVersion);
        info.put("accounts", parseCreateConfigAccounts(accounts));

        return info;
    }

    private static Map<String, Object> parseUpdateConfigAccount(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        int configVersion = buffer.getInt();

        info.put("configVersion", configVersion);
        info.put("accounts", parseUpdateConfigAccounts(accounts));

        return info;
    }

    private static Map<String, Object> parseSetParams(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        long param1 = buffer.getLong();
        long param2 = buffer.getLong();

        info.put("param1", param1);
        info.put("param2", param2);
        info.put("accounts", parseSetParamsAccounts(accounts));

        return info;
    }

    private static Map<String, Object> parseMonitorStep(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        int step = buffer.getInt();

        info.put("step", step);
        info.put("accounts", parseMonitorStepAccounts(accounts));

        return info;
    }

    private static Map<String, Object> parseSimulateInfo(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        long simulationAmount = buffer.getLong();

        info.put("simulationAmount", simulationAmount);
        info.put("accounts", parseSimulateInfoAccounts(accounts));

        return info;
    }

    private static Map<String, Object> parseMigrateToOpenBook(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        long migrationFlags = buffer.getLong();

        info.put("migrationFlags", migrationFlags);
        info.put("accounts", parseMigrateToOpenBookAccounts(accounts));

        return info;
    }

    private static Map<String, Object> parseAdminCancelOrders(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        int limit = buffer.getInt();

        info.put("limit", limit);
        info.put("accounts", parseAdminCancelOrdersAccounts(accounts));

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

    private static Map<String, String> parsePreInitializeAccounts(String[] accounts) {
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 3) {
            accountMap.put("authority", accounts[0]);
            accountMap.put("amm", accounts[1]);
            accountMap.put("config", accounts[2]);
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

    private static Map<String, String> parseDepositAccounts(String[] accounts) {
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 7) {
            accountMap.put("amm", accounts[0]);
            accountMap.put("authority", accounts[1]);
            accountMap.put("userTokenA", accounts[2]);
            accountMap.put("userTokenB", accounts[3]);
            accountMap.put("poolTokenA", accounts[4]);
            accountMap.put("poolTokenB", accounts[5]);
            accountMap.put("lpMint", accounts[6]);
        }
        return accountMap;
    }

    private static Map<String, String> parseWithdrawAccounts(String[] accounts) {
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 7) {
            accountMap.put("amm", accounts[0]);
            accountMap.put("authority", accounts[1]);
            accountMap.put("lpToken", accounts[2]);
            accountMap.put("userTokenA", accounts[3]);
            accountMap.put("userTokenB", accounts[4]);
            accountMap.put("poolTokenA", accounts[5]);
            accountMap.put("poolTokenB", accounts[6]);
        }
        return accountMap;
    }

    private static Map<String, String> parseWithdrawPnlAccounts(String[] accounts) {
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 4) {
            accountMap.put("amm", accounts[0]);
            accountMap.put("authority", accounts[1]);
            accountMap.put("pnlPool", accounts[2]);
            accountMap.put("destination", accounts[3]);
        }
        return accountMap;
    }

    private static Map<String, String> parseWithdrawSrmAccounts(String[] accounts) {
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 4) {
            accountMap.put("amm", accounts[0]);
            accountMap.put("authority", accounts[1]);
            accountMap.put("srmToken", accounts[2]);
            accountMap.put("destination", accounts[3]);
        }
        return accountMap;
    }

    private static Map<String, String> parseCreateConfigAccounts(String[] accounts) {
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 3) {
            accountMap.put("authority", accounts[0]);
            accountMap.put("config", accounts[1]);
            accountMap.put("payer", accounts[2]);
        }
        return accountMap;
    }

    private static Map<String, String> parseUpdateConfigAccounts(String[] accounts) {
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 2) {
            accountMap.put("authority", accounts[0]);
            accountMap.put("config", accounts[1]);
        }
        return accountMap;
    }

    private static Map<String, String> parseSetParamsAccounts(String[] accounts) {
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 2) {
            accountMap.put("authority", accounts[0]);
            accountMap.put("amm", accounts[1]);
        }
        return accountMap;
    }

    private static Map<String, String> parseMonitorStepAccounts(String[] accounts) {
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 3) {
            accountMap.put("authority", accounts[0]);
            accountMap.put("amm", accounts[1]);
            accountMap.put("monitor", accounts[2]);
        }
        return accountMap;
    }

    private static Map<String, String> parseSimulateInfoAccounts(String[] accounts) {
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 2) {
            accountMap.put("amm", accounts[0]);
            accountMap.put("simulator", accounts[1]);
        }
        return accountMap;
    }

    private static Map<String, String> parseMigrateToOpenBookAccounts(String[] accounts) {
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 4) {
            accountMap.put("authority", accounts[0]);
            accountMap.put("amm", accounts[1]);
            accountMap.put("openBookMarket", accounts[2]);
            accountMap.put("destination", accounts[3]);
        }
        return accountMap;
    }

    private static Map<String, String> parseAdminCancelOrdersAccounts(String[] accounts) {
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 3) {
            accountMap.put("authority", accounts[0]);
            accountMap.put("amm", accounts[1]);
            accountMap.put("market", accounts[2]);
        }
        return accountMap;
    }
}