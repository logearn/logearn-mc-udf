package cn.xlystar.parse.solSwap.pump;

import org.bitcoinj.core.Base58;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class PumpDotFunInstructionParser {

    private static final String PROGRAM_ID = "6EF8rrecthR5Dkzon8Nwu78hRvfCKubJ14M5uBEwF6P";

    public static Map<String, Object> parseInstruction(byte[] data, String[] accounts) {
        Map<String, Object> result = new HashMap<>();

        if (data == null || data.length == 0) {
            result.put("error", "Invalid instruction data");
            return result;
        }

        try {
            ByteBuffer buffer = ByteBuffer.wrap(data);
            int discriminator = buffer.get() & 0xFF;
            Map<String, Object> info;
            switch (discriminator) {
                case 0:
                    info = parseInitialize(buffer, accounts);
                    result.put("type", "initialize");
                    break;
                case 1:
                    info = parseSetParams(buffer, accounts);
                    result.put("type", "setParams");
                    break;
                case 2:
                    info = parseCreate(buffer, accounts);
                    result.put("type", "create");
                    break;
                case 3:
                    info = parseBuy(buffer, accounts);
                    result.put("type", "buy");
                    break;
                case 4:
                    info = parseSell(buffer, accounts);
                    result.put("type", "sell");
                    break;
                case 5:
                    info = parseWithdraw(buffer, accounts);
                    result.put("type", "withdraw");
                    break;
                default:
                    result.put("error", "Unknown instruction type: " + discriminator);
                    return result;
            }
            result.put("info", info);

        } catch (Exception e) {
            result.put("error", "Failed to parse instruction: " + e.getMessage());
        }

        return result;
    }

    private static Map<String, Object> parseInitialize(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        // Initialize 没有参数需要解析
        info.put("accounts", parseInitializeAccounts(accounts));
        return info;
    }

    private static Map<String, Object> parseSetParams(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 解析参数
        byte[] feeRecipient = new byte[32];
        buffer.get(feeRecipient);
        long initialVirtualTokenReserves = buffer.getLong();
        long initialVirtualSolReserves = buffer.getLong();
        long initialRealTokenReserves = buffer.getLong();
        long tokenTotalSupply = buffer.getLong();
        long feeBasisPoints = buffer.getLong();

        info.put("feeRecipient", Base58.encode(feeRecipient));
        info.put("initialVirtualTokenReserves", initialVirtualTokenReserves);
        info.put("initialVirtualSolReserves", initialVirtualSolReserves);
        info.put("initialRealTokenReserves", initialRealTokenReserves);
        info.put("tokenTotalSupply", tokenTotalSupply);
        info.put("feeBasisPoints", feeBasisPoints);
        info.put("accounts", parseSetParamsAccounts(accounts));

        return info;
    }

    private static Map<String, Object> parseCreate(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 解析字符串参数
        String name = parseString(buffer);
        String symbol = parseString(buffer);
        String uri = parseString(buffer);

        info.put("name", name);
        info.put("symbol", symbol);
        info.put("uri", uri);
        info.put("accounts", parseCreateAccounts(accounts));

        return info;
    }

    private static Map<String, Object> parseBuy(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        long amount = buffer.getLong();
        long maxSolCost = buffer.getLong();

        info.put("amount", amount);
        info.put("maxSolCost", maxSolCost);
        info.put("accounts", parseBuyAccounts(accounts));

        return info;
    }

    private static Map<String, Object> parseSell(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        long amount = buffer.getLong();
        long minSolOutput = buffer.getLong();

        info.put("amount", amount);
        info.put("minSolOutput", minSolOutput);
        info.put("accounts", parseSellAccounts(accounts));

        return info;
    }

    private static Map<String, Object> parseWithdraw(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        // Withdraw 没有参数需要解析
        info.put("accounts", parseWithdrawAccounts(accounts));
        return info;
    }

    // 账户解析辅助方法
    private static Map<String, String> parseInitializeAccounts(String[] accounts) {
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 3) {
            accountMap.put("global", accounts[0]);
            accountMap.put("user", accounts[1]);
            accountMap.put("systemProgram", accounts[2]);
        }
        return accountMap;
    }

    private static Map<String, String> parseSetParamsAccounts(String[] accounts) {
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 5) {
            accountMap.put("global", accounts[0]);
            accountMap.put("user", accounts[1]);
            accountMap.put("systemProgram", accounts[2]);
            accountMap.put("eventAuthority", accounts[3]);
            accountMap.put("program", accounts[4]);
        }
        return accountMap;
    }

    private static Map<String, String> parseCreateAccounts(String[] accounts) {
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 14) {
            accountMap.put("mint", accounts[0]);
            accountMap.put("mintAuthority", accounts[1]);
            accountMap.put("bondingCurve", accounts[2]);
            accountMap.put("associatedBondingCurve", accounts[3]);
            accountMap.put("global", accounts[4]);
            accountMap.put("mplTokenMetadata", accounts[5]);
            accountMap.put("metadata", accounts[6]);
            accountMap.put("user", accounts[7]);
            accountMap.put("systemProgram", accounts[8]);
            accountMap.put("tokenProgram", accounts[9]);
            accountMap.put("associatedTokenProgram", accounts[10]);
            accountMap.put("rent", accounts[11]);
            accountMap.put("eventAuthority", accounts[12]);
            accountMap.put("program", accounts[13]);
        }
        return accountMap;
    }

    private static Map<String, String> parseBuyAccounts(String[] accounts) {
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 12) {
            accountMap.put("global", accounts[0]);
            accountMap.put("feeRecipient", accounts[1]);
            accountMap.put("mint", accounts[2]);
            accountMap.put("bondingCurve", accounts[3]);
            accountMap.put("associatedBondingCurve", accounts[4]);
            accountMap.put("associatedUser", accounts[5]);
            accountMap.put("user", accounts[6]);
            accountMap.put("systemProgram", accounts[7]);
            accountMap.put("tokenProgram", accounts[8]);
            accountMap.put("rent", accounts[9]);
            accountMap.put("eventAuthority", accounts[10]);
            accountMap.put("program", accounts[11]);
        }
        return accountMap;
    }

    private static Map<String, String> parseSellAccounts(String[] accounts) {
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 12) {
            accountMap.put("global", accounts[0]);
            accountMap.put("feeRecipient", accounts[1]);
            accountMap.put("mint", accounts[2]);
            accountMap.put("bondingCurve", accounts[3]);
            accountMap.put("associatedBondingCurve", accounts[4]);
            accountMap.put("associatedUser", accounts[5]);
            accountMap.put("user", accounts[6]);
            accountMap.put("systemProgram", accounts[7]);
            accountMap.put("associatedTokenProgram", accounts[8]);
            accountMap.put("tokenProgram", accounts[9]);
            accountMap.put("eventAuthority", accounts[10]);
            accountMap.put("program", accounts[11]);
        }
        return accountMap;
    }

    private static Map<String, String> parseWithdrawAccounts(String[] accounts) {
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 11) {
            accountMap.put("global", accounts[0]);
            accountMap.put("mint", accounts[1]);
            accountMap.put("bondingCurve", accounts[2]);
            accountMap.put("associatedBondingCurve", accounts[3]);
            accountMap.put("associatedUser", accounts[4]);
            accountMap.put("user", accounts[5]);
            accountMap.put("systemProgram", accounts[6]);
            accountMap.put("tokenProgram", accounts[7]);
            accountMap.put("rent", accounts[8]);
            accountMap.put("eventAuthority", accounts[9]);
            accountMap.put("program", accounts[10]);
        }
        return accountMap;
    }

    // 工具方法：解析字符串
    private static String parseString(ByteBuffer buffer) {
        int length = buffer.getInt();
        byte[] bytes = new byte[length];
        buffer.get(bytes);
        return new String(bytes);
    }
}