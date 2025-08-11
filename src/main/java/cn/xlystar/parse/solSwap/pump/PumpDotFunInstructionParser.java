package cn.xlystar.parse.solSwap.pump;

import cn.xlystar.parse.solSwap.InstructionParser;
import org.bitcoinj.core.Base58;
import org.bouncycastle.util.encoders.Hex;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;


public class PumpDotFunInstructionParser extends InstructionParser {

    public static final String PROGRAM_ID = "6EF8rrecthR5Dkzon8Nwu78hRvfCKubJ14M5uBEwF6P";
    public static final String MIGRATION_PROGRAM_ID = "39azUYFWPz3VHgKCf3VChUwbpURdCHRxjWVowf5jUJjg";
    private static final String CREATE_DISCRIMINATOR = "8530921459188068891";
    private static final String TRADE_DISCRIMINATOR = "17177263679997991869";
    private static final String COMPLETE_DISCRIMINATOR = "619296439455019615";
    private static final String SET_PARAMS_DISCRIMINATOR = "9479848787621954527";

    public static boolean isMigrated(String methodId) {
        return methodId.equals(PumpDotFunInstruction.MIGRATION.getValue());
    }

    @Override
    public String getMethodId(ByteBuffer buffer) {
        byte[] discriminatorBytes = new byte[8];
        buffer.get(discriminatorBytes);
        return Hex.toHexString(discriminatorBytes);
    }

    @Override
    public Map<String, Object> matchInstruction(String methodId, ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info;
        switch (PumpDotFunInstruction.fromValue(methodId)) {
            case INITIALIZE:
                info = parseInitialize(buffer, accounts);
                break;
            case SET_PARAMS:
                info = parseSetParams(buffer, accounts);
                break;
            case MIGRATION:
                info = parseMigrated(buffer, accounts);
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
            case ANCHOR_SELF_CPI_LOG:
                info = parseAnchorSelfCpiLog(buffer, accounts);
                break;
            default:
                return new HashMap<>();
        }
        return info;
    }

    private static Map<String, Object> parseInitialize(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        info.put("global", accounts[0]);
        info.put("user", accounts[1]);
        info.put("systemProgram", accounts[2]);
        return info;
    }

    private static Map<String, Object> parseMigrated(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        info.put("global", accounts[0]);
        info.put("withdraw_authority", accounts[1]);
        info.put("base_mint", accounts[2]);
        info.put("bondingCurve", accounts[3]);
        info.put("associatedBondingCurve", accounts[4]);
        info.put("user", accounts[5]);
        info.put("systemProgram", accounts[6]);
        info.put("tokenProgram", accounts[7]);
        info.put("pumpAmm", accounts[8]);
        info.put("pumpAmmPool", accounts[9]);
        info.put("poolAuthority", accounts[10]);
        info.put("poolAuthorityMintAccount", accounts[11]);
        info.put("poolAuthorityWsolAccount", accounts[12]);
        info.put("ammGlobalConfig", accounts[13]);
        info.put("wsolMint", accounts[14]);
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
        byte[] creatorBytes = new byte[32];
        buffer.get(creatorBytes);
        String creator = Base58.encode(creatorBytes);

        info.put("name", name);
        info.put("symbol", symbol);
        info.put("uri", uri);
        info.put("creator", creator);

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

    private static Map<String, Object> parseAnchorSelfCpiLog(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        // 获取前 8 字节作为 discriminator
        String discriminator = Long.toUnsignedString(buffer.getLong());
        String eventType = getEventType(discriminator);

        switch (eventType) {
            case "create":
                return parseCreateEvent(buffer);
            case "trade":
                return parseTradeEvent(buffer);
            case "complete":
                return parseCompleteEvent(buffer);
            case "setParams":
                return parseSetParamsEvent(buffer);
            default:
                info.put("eventType", discriminator);
                return info;
        }
    }

    /**
     * 根据 discriminator 判断事件类型
     */
    private static String getEventType(String discriminator) {
        if (discriminator.equals(CREATE_DISCRIMINATOR)) {
            return "create";
        } else if (discriminator.equals(TRADE_DISCRIMINATOR)) {
            return "trade";
        } else if (discriminator.equals(COMPLETE_DISCRIMINATOR)) {
            return "complete";
        } else if (discriminator.equals(SET_PARAMS_DISCRIMINATOR)) {
            return "setParams";
        }
        return "";
    }

    /**
     * 解析 Trade 事件
     * 参考: https://github.com/TeamRaccoons/pump/blob/master/programs/amm/src/events.rs
     */
    private static Map<String, Object> parseCreateEvent(ByteBuffer buffer) {
        Map<String, Object> event = new HashMap<>();

        event.put("name", parseString(buffer));
        event.put("symbol", parseString(buffer));
        event.put("uri", parseString(buffer));
        event.put("mint", readPubkey(buffer));
        event.put("bondingCurve", readPubkey(buffer));
        event.put("user", readPubkey(buffer));
        event.put("eventType", "create");

        return event;
    }

    private static Map<String, Object> parseTradeEvent(ByteBuffer buffer) {
        Map<String, Object> event = new HashMap<>();

        event.put("mint", readPubkey(buffer));
        event.put("solAmount", Long.toUnsignedString(buffer.getLong()));
        event.put("tokenAmount", Long.toUnsignedString(buffer.getLong()));
        event.put("isBuy", buffer.get());
        event.put("user", readPubkey(buffer));
        event.put("timestamp", Long.toUnsignedString(buffer.getLong()));
        event.put("virtual_sol_reserves", Long.toUnsignedString(buffer.getLong()));
        event.put("virtual_token_reserves", Long.toUnsignedString(buffer.getLong()));
        event.put("real_sol_reserves", Long.toUnsignedString(buffer.getLong()));
        event.put("real_token_reserves", Long.toUnsignedString(buffer.getLong()));
        event.put("eventType", "trade");

        return event;
    }

    private static Map<String, Object> parseCompleteEvent(ByteBuffer buffer) {
        Map<String, Object> event = new HashMap<>();

        event.put("user", readPubkey(buffer));
        event.put("mint", readPubkey(buffer));
        event.put("bondingCurve", readPubkey(buffer));
        event.put("timestamp", Long.toUnsignedString(buffer.getLong()));
        event.put("eventType", "complete");

        return event;
    }

    private static Map<String, Object> parseSetParamsEvent(ByteBuffer buffer) {
        Map<String, Object> event = new HashMap<>();

        event.put("feeRecipient", readPubkey(buffer));
        event.put("initialVirtualTokenReserves", Long.toUnsignedString(buffer.getLong()));
        event.put("initialVirtualSolReserves", Long.toUnsignedString(buffer.getLong()));
        event.put("initialRealTokenReserves", Long.toUnsignedString(buffer.getLong()));
        event.put("tokenTotalSupply", Long.toUnsignedString(buffer.getLong()));
        event.put("feeBasisPoints", Long.toUnsignedString(buffer.getLong()));
        event.put("eventType", "setParams");

        return event;
    }

    private static String readPubkey(ByteBuffer buffer) {
        byte[] pubkey = new byte[32];
        buffer.get(pubkey);
        return Base58.encode(pubkey);
    }


    // 工具方法：解析字符串
    private static String parseString(ByteBuffer buffer) {
        int length = buffer.getInt();
        byte[] bytes = new byte[length];
        buffer.get(bytes);
        return new String(bytes);
    }

    public static void main(String[] args) {
        String data = "e445a52e51cb9a1d1b72a94ddeeb63760a0000004d414e41204147454e54060000004d4147454e544300000068747470733a2f2f697066732e696f2f697066732f516d596b744b3576364c7a6a5a574c46644a706b7262714151615872537066376d4c7131565155655a69514b344a7f3b3dd6a7ce312fb0b3c517d7e0a87f2d95556a88bb0839b3758e64d9f1d76dcde4711190fb622a07de52bc6997724e5e3d9e5f81cd7971a3bb01b9758cae3e4e9acc3a398370427c4218f7742b63a3aa2a32d343dee315d63dfc97eee22bd0";
        PumpDotFunInstructionParser pumpfun = new PumpDotFunInstructionParser();
        System.out.println(pumpfun.parseInstruction(Hex.decode(data), new String[]{}));
    }
}