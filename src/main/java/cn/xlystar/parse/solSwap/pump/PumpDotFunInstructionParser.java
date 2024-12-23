package cn.xlystar.parse.solSwap.pump;

import org.bitcoinj.core.Base58;
import org.bouncycastle.util.encoders.Hex;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
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
            ByteBuffer buffer = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN);
            byte[] discriminatorBytes = new byte[8];
            buffer.get(discriminatorBytes);
            String discriminator = Hex.toHexString(discriminatorBytes);
            PumpDotFunInstruction instructionType = PumpDotFunInstruction.fromValue(discriminator);
            result.put("type", instructionType.name());

            Map<String, Object> info;
            switch (instructionType) {
                case INITIALIZE:
                    info = parseInitialize(buffer, accounts);
                    break;
                case SET_PARAMS:
                    info = parseSetParams(buffer, accounts);
                    break;
                case CREATE:
                    info = parseCreate(buffer, accounts);
                    break;
                case BUY:
                    info = parseBuy(buffer, accounts);
                    break;
                case SELL:
                    info = parseSell(buffer, accounts);
                    break;
                case WITHDRAW:
                    info = parseWithdraw(buffer, accounts);
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
        info.put("global", accounts[0]);
        info.put("user", accounts[1]);
        info.put("systemProgram", accounts[2]);
        return info;
    }

    private static Map<String, Object> parseSetParams(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 解析参数
        byte[] feeRecipient = new byte[32];
        buffer.get(feeRecipient);
        String initialVirtualTokenReserves = Long.toUnsignedString(buffer.getLong());
        String initialVirtualSolReserves = Long.toUnsignedString(buffer.getLong());
        String initialRealTokenReserves = Long.toUnsignedString(buffer.getLong());
        String tokenTotalSupply = Long.toUnsignedString(buffer.getLong());
        String feeBasisPoints = Long.toUnsignedString(buffer.getLong());

        info.put("feeRecipient", Base58.encode(feeRecipient));
        info.put("initialVirtualTokenReserves", initialVirtualTokenReserves);
        info.put("initialVirtualSolReserves", initialVirtualSolReserves);
        info.put("initialRealTokenReserves", initialRealTokenReserves);
        info.put("tokenTotalSupply", tokenTotalSupply);
        info.put("feeBasisPoints", feeBasisPoints);

        info.put("global", accounts[0]);
        info.put("user", accounts[1]);
        info.put("systemProgram", accounts[2]);
        info.put("eventAuthority", accounts[3]);
        info.put("program", accounts[4]);
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

        info.put("mint", accounts[0]);
        info.put("mintAuthority", accounts[1]);
        info.put("bondingCurve", accounts[2]);
        info.put("associatedBondingCurve", accounts[3]);
        info.put("global", accounts[4]);
        info.put("mplTokenMetadata", accounts[5]);
        info.put("metadata", accounts[6]);
        info.put("user", accounts[7]);
        info.put("systemProgram", accounts[8]);
        info.put("tokenProgram", accounts[9]);
        info.put("associatedTokenProgram", accounts[10]);
        info.put("rent", accounts[11]);
        info.put("eventAuthority", accounts[12]);
        info.put("program", accounts[13]);
        return info;
    }

    private static Map<String, Object> parseBuy(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        info.put("amount", Long.toUnsignedString(buffer.getLong()));
        info.put("maxSolCost", Long.toUnsignedString(buffer.getLong()));

        info.put("global", accounts[0]);
        info.put("feeRecipient", accounts[1]);
        info.put("mint", accounts[2]);
        info.put("bondingCurve", accounts[3]);
        info.put("associatedBondingCurve", accounts[4]);
        info.put("associatedUser", accounts[5]);
        info.put("user", accounts[6]);
        info.put("systemProgram", accounts[7]);
        info.put("tokenProgram", accounts[8]);
        info.put("rent", accounts[9]);
        info.put("eventAuthority", accounts[10]);
        info.put("program", accounts[11]);
        return info;
    }

    private static Map<String, Object> parseSell(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        info.put("amount", Long.toUnsignedString(buffer.getLong()));
        info.put("minSolOutput", Long.toUnsignedString(buffer.getLong()));

        info.put("global", accounts[0]);
        info.put("feeRecipient", accounts[1]);
        info.put("mint", accounts[2]);
        info.put("bondingCurve", accounts[3]);
        info.put("associatedBondingCurve", accounts[4]);
        info.put("associatedUser", accounts[5]);
        info.put("user", accounts[6]);
        info.put("systemProgram", accounts[7]);
        info.put("associatedTokenProgram", accounts[8]);
        info.put("tokenProgram", accounts[9]);
        info.put("eventAuthority", accounts[10]);
        info.put("program", accounts[11]);

        return info;
    }

    private static Map<String, Object> parseWithdraw(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        // Withdraw 没有参数需要解析
        info.put("global", accounts[0]);
        info.put("mint", accounts[1]);
        info.put("bondingCurve", accounts[2]);
        info.put("associatedBondingCurve", accounts[3]);
        info.put("associatedUser", accounts[4]);
        info.put("user", accounts[5]);
        info.put("systemProgram", accounts[6]);
        info.put("tokenProgram", accounts[7]);
        info.put("rent", accounts[8]);
        info.put("eventAuthority", accounts[9]);
        info.put("program", accounts[10]);
        return info;
    }



    // 工具方法：解析字符串
    private static String parseString(ByteBuffer buffer) {
        int length = buffer.getInt();
        byte[] bytes = new byte[length];
        buffer.get(bytes);
        return new String(bytes);
    }
}