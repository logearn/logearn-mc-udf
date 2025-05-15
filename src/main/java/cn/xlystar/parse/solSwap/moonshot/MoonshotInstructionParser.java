package cn.xlystar.parse.solSwap.moonshot;

import cn.xlystar.parse.solSwap.InstructionParser;
import org.bitcoinj.core.Base58;
import org.bouncycastle.util.encoders.Hex;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class MoonshotInstructionParser extends InstructionParser {

    public static final String PROGRAM_ID = "MoonCVVNZFSYkqNXP6bxHLPL6QQJiMagDL3qcqUQTrG";
    @Override
    public String getMethodId(ByteBuffer buffer) {
        byte[] discriminatorBytes = new byte[8];
        buffer.get(discriminatorBytes);
        return Hex.toHexString(discriminatorBytes);
    }

    @Override
    public Map<String, Object> matchInstruction(String methodId, ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info;
        switch (MoonshotInstruction.fromValue(methodId)) {
            case TOKEN_MINT:
                info = parseTokenMint(buffer, accounts);
                break;
            case BUY:
                info = parseBuy(buffer, accounts);
                break;
            case SELL:
                info = parseSell(buffer, accounts);
                break;
            case MIGRATE_FUNDS:
                info = parseMigrateFunds(buffer, accounts);
                break;
            case CONFIG_INIT:
                info = parseConfigInit(buffer, accounts);
                break;
            case CONFIG_UPDATE:
                info = parseConfigUpdate(buffer, accounts);
                break;
            default:
                return new HashMap<>();
        }
        return info;
    }

    private Map<String, Object> parseTokenMint(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        
        // 解析 TokenMintParams
        String name = parseString(buffer);
        String symbol = parseString(buffer);
        String uri = parseString(buffer);
        int decimals = buffer.get();
        int collateralCurrency = buffer.get();
        long amount = buffer.getLong();
        int curveType = buffer.get();
        int migrationTarget = buffer.get();

        // 添加参数信息
        info.put("name", name);
        info.put("symbol", symbol);
        info.put("uri", uri);
        info.put("decimals", decimals);
        info.put("collateralCurrency", collateralCurrency);
        info.put("amount", Long.toUnsignedString(amount));
        info.put("curveType", curveType);
        info.put("migrationTarget", migrationTarget);

        // 添加账户信息
        info.put("sender", accounts[0]);
        info.put("backendAuthority", accounts[1]);
        info.put("curveAccount", accounts[2]);
        info.put("mint", accounts[3]);
        info.put("mintMetadata", accounts[4]);
        info.put("curveTokenAccount", accounts[5]);
        info.put("configAccount", accounts[6]);
        info.put("tokenProgram", accounts[7]);
        info.put("associatedTokenProgram", accounts[8]);
        info.put("mplTokenMetadata", accounts[9]);
        info.put("systemProgram", accounts[10]);

        return info;
    }

    private Map<String, Object> parseBuy(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        
        // 解析 TradeParams
        long tokenAmount = buffer.getLong();
        long collateralAmount = buffer.getLong();
        int fixedSide = buffer.get();
        long slippageBps = buffer.getLong();

        // 添加参数信息
        info.put("tokenAmount", Long.toUnsignedString(tokenAmount));
        info.put("collateralAmount", Long.toUnsignedString(collateralAmount));
        info.put("fixedSide", fixedSide);
        info.put("slippageBps", Long.toUnsignedString(slippageBps));

        // 添加账户信息
        info.put("sender", accounts[0]);
        info.put("senderTokenAccount", accounts[1]);
        info.put("curveAccount", accounts[2]);
        info.put("curveTokenAccount", accounts[3]);
        info.put("dexFee", accounts[4]);
        info.put("helioFee", accounts[5]);
        info.put("mint", accounts[6]);
        info.put("configAccount", accounts[7]);
        info.put("tokenProgram", accounts[8]);
        info.put("associatedTokenProgram", accounts[9]);
        info.put("systemProgram", accounts[10]);

        return info;
    }

    private Map<String, Object> parseSell(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        
        // 解析 TradeParams (与 Buy 相同的参数结构)
        long tokenAmount = buffer.getLong();
        long collateralAmount = buffer.getLong();
        int fixedSide = buffer.get();
        long slippageBps = buffer.getLong();

        // 添加参数信息
        info.put("tokenAmount", Long.toUnsignedString(tokenAmount));
        info.put("collateralAmount", Long.toUnsignedString(collateralAmount));
        info.put("fixedSide", fixedSide);
        info.put("slippageBps", Long.toUnsignedString(slippageBps));

        // 添加账户信息 (与 Buy 相同的账户结构)
        info.put("sender", accounts[0]);
        info.put("senderTokenAccount", accounts[1]);
        info.put("curveAccount", accounts[2]);
        info.put("curveTokenAccount", accounts[3]);
        info.put("dexFee", accounts[4]);
        info.put("helioFee", accounts[5]);
        info.put("mint", accounts[6]);
        info.put("configAccount", accounts[7]);
        info.put("tokenProgram", accounts[8]);
        info.put("associatedTokenProgram", accounts[9]);
        info.put("systemProgram", accounts[10]);

        return info;
    }

    private Map<String, Object> parseMigrateFunds(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        
        // migrateFunds 没有参数需要解析
        
        // 添加账户信息
        info.put("backendAuthority", accounts[0]);
        info.put("migrationAuthority", accounts[1]);
        info.put("curveAccount", accounts[2]);
        info.put("curveTokenAccount", accounts[3]);
        info.put("migrationAuthorityTokenAccount", accounts[4]);
        info.put("mint", accounts[5]);
        int offset = 0;
        if (accounts.length > 11) {
            info.put("dexFeeAccount", accounts[6]);
            info.put("helioFeeAccount", accounts[7]);
            offset = 2;
        }
        info.put("configAccount", accounts[6+offset]);
        info.put("systemProgram", accounts[7+offset]);
        info.put("tokenProgram", accounts[8+offset]);
        info.put("associatedTokenProgram", accounts[9+offset]);

        return info;
    }

    private Map<String, Object> parseConfigInit(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        
        // 解析 ConfigParams
        parseConfigParams(buffer, info);

        // 添加账户信息
        info.put("configAuthority", accounts[0]);
        info.put("configAccount", accounts[1]);
        info.put("systemProgram", accounts[2]);

        return info;
    }

    private Map<String, Object> parseConfigUpdate(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        
        // 解析 ConfigParams
        parseConfigParams(buffer, info);

        // 添加账户信息
        info.put("configAuthority", accounts[0]);
        info.put("configAccount", accounts[1]);

        return info;
    }

    private void parseConfigParams(ByteBuffer buffer, Map<String, Object> info) {
        // 解析所有可选字段
        if (buffer.get() == 1) info.put("migrationAuthority", readPubkey(buffer));
        if (buffer.get() == 1) info.put("backendAuthority", readPubkey(buffer));
        if (buffer.get() == 1) info.put("configAuthority", readPubkey(buffer));
        if (buffer.get() == 1) info.put("helioFee", readPubkey(buffer));
        if (buffer.get() == 1) info.put("dexFee", readPubkey(buffer));
        if (buffer.get() == 1) info.put("feeBps", buffer.getShort());
        if (buffer.get() == 1) info.put("dexFeeShare", buffer.get());
        if (buffer.get() == 1) info.put("migrationFee", Long.toUnsignedString(buffer.getLong()));
        if (buffer.get() == 1) info.put("marketcapThreshold", Long.toUnsignedString(buffer.getLong()));
        if (buffer.get() == 1) info.put("marketcapCurrency", buffer.get());
        if (buffer.get() == 1) info.put("minSupportedDecimalPlaces", buffer.get());
        if (buffer.get() == 1) info.put("maxSupportedDecimalPlaces", buffer.get());
        if (buffer.get() == 1) info.put("minSupportedTokenSupply", Long.toUnsignedString(buffer.getLong()));
        if (buffer.get() == 1) info.put("maxSupportedTokenSupply", Long.toUnsignedString(buffer.getLong()));
        if (buffer.get() == 1) info.put("coefB", buffer.getInt());
    }

    // 工具方法：解析字符串
    private static String parseString(ByteBuffer buffer) {
        int length = buffer.getInt();
        byte[] bytes = new byte[length];
        buffer.get(bytes);
        return new String(bytes);
    }

    // 工具方法：读取公钥
    private static String readPubkey(ByteBuffer buffer) {
        byte[] pubkey = new byte[32];
        buffer.get(pubkey);
        return Base58.encode(pubkey);
    }


    @Override
    public Map<String, Object> matchLogEvent(ByteBuffer buffer, String eventType) {
        Map<String, Object> info = new HashMap<>();
        switch (eventType) {
            case "trade":
                return parseMoonTradeEvent(buffer);
            case "migration":
                return parseMoonMigrationEvent(buffer);
            default:
                info.put("eventType", eventType);
                return info;
        }
    }

    /**
     * 根据 discriminator 判断事件类型
     */
    @Override
    public String getLogEventType(ByteBuffer buffer) {
        String discriminator = Long.toUnsignedString(buffer.getLong());
        if (discriminator.equals("17177263679997991869")) {
            return "trade";
        } else if (discriminator.equals("")) {
            return "migration";
        }
        return discriminator;
    }


    private static Map<String, Object> parseMoonTradeEvent(ByteBuffer buffer) {
        Map<String, Object> event = new HashMap<>();

        event.put("amount", Long.toUnsignedString(buffer.getLong()));           // u64
        event.put("collateralAmount", Long.toUnsignedString(buffer.getLong())); // u64
        event.put("dexFee", Long.toUnsignedString(buffer.getLong()));           // u64
        event.put("helioFee", Long.toUnsignedString(buffer.getLong()));         // u64
        event.put("allocation", Long.toUnsignedString(buffer.getLong()));       // u64
        event.put("curve", readPubkey(buffer));                                 // publicKey
        event.put("costToken", readPubkey(buffer));                             // publicKey
        event.put("sender", readPubkey(buffer));                                // publicKey
        event.put("type", buffer.get());                                        // TradeType (enum) buy/sell
        event.put("label", parseString(buffer));                                // string
        event.put("eventType", "trade");
        return event;
    }

    private static Map<String, Object> parseMoonMigrationEvent(ByteBuffer buffer) {
        Map<String, Object> event = new HashMap<>();

        // 按照 IDL 中的字段顺序解析
        event.put("tokensMigrated", Long.toUnsignedString(buffer.getLong()));     // u64
        event.put("tokensBurned", Long.toUnsignedString(buffer.getLong()));       // u64
        event.put("collateralMigrated", Long.toUnsignedString(buffer.getLong())); // u64
        event.put("fee", Long.toUnsignedString(buffer.getLong()));                // u64
        event.put("label", parseString(buffer));                                  // string
        event.put("eventType", "migration");
        return event;
    }
} 