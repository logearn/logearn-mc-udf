package cn.xlystar.parse.solSwap.metaplex;

import cn.xlystar.parse.solSwap.InstructionParser;
import cn.xlystar.parse.solSwap.spl_token.SplTokenInstruction;
import org.bitcoinj.core.Base58;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.*;

public class MetadataInstructionParser extends InstructionParser {
    @Override
    public String getMethodId(ByteBuffer buffer) {
        return Byte.toUnsignedInt(buffer.get()) + "";
    }

    @Override
    public Map<String, Object> matchInstruction(String methodId, ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info;
        switch (MetadataInstruction.fromValue(Integer.parseInt(methodId))) {
            case CreateMetadataAccount:  // 0
                info = parseCreateMetadataAccount(buffer, accounts);
                break;
            case UpdateMetadataAccount:  // 1
                info = parseUpdateMetadataAccount(accounts);
                break;
            case CreateMasterEdition:  // 10
                info = parseCreateMasterEdition(accounts);
                break;
            case MintNewEditionFromMasterEditionViaToken:  // 11
                info = parseMintNewEditionFromMasterEditionViaToken(buffer, accounts);
                break;
            case UpdateMetadataAccountV2:  // 15
                info = parseUpdateMetadataAccountV2(buffer, accounts);
                break;
            case CreateMetadataAccountV2:  // 16
                info = parseCreateMetadataAccountV2(accounts);
                break;
            case CreateMasterEditionV3:  // 17
                info = parseCreateMasterEditionV3(accounts);
                break;
            case CreateMetadataAccountV3:  // 33
                info = parseCreateMetadataAccountV3(buffer, accounts);
                break;
            case Create:  // 42
                info = parseCreate(buffer, accounts);
                break;
            case Mint:  // 43
                info = parseMint(accounts);
                break;
            case Burn:  // 41
                info = parseBurn(accounts);
                break;
            case Migrate:  // 48
                info = parseMigrate(accounts);
                break;
            case Transfer:  // 49
                info = parseTransfer(accounts);
                break;
            case Update:  // 50
                info = parseUpdate(accounts);
                break;
            default:
                return new HashMap<>();
        }
        return info;
    }

    // CreateMetadataAccount 指令解析
    private static Map<String, Object> parseCreateMetadataAccount(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        info.put("metadata", accounts[0]);      // Metadata key
        info.put("mint", accounts[1]);          // Mint of token asset
        info.put("mintAuthority", accounts[2]); // Mint authority
        info.put("payer", accounts[3]);         // Payer
        info.put("updateAuthority", accounts[4]); // Update authority
        info.put("systemProgram", accounts[5]); // System program
        info.put("rent", accounts[6]);          // Rent info

        return info;
    }

    // MintNewEditionFromMasterEditionViaToken 指令解析
    private static Map<String, Object> parseMintNewEditionFromMasterEditionViaToken(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 读取版本号
        long edition = buffer.getLong();

        info.put("newMetadata", accounts[0]);    // New Metadata key
        info.put("newEdition", accounts[1]);     // New Edition
        info.put("masterEdition", accounts[2]);  // Master Edition V2
        info.put("newMint", accounts[3]);        // New mint
        info.put("editionMarkPda", accounts[4]); // Edition pda to mark creation
        info.put("newMintAuthority", accounts[5]); // New mint authority
        info.put("payer", accounts[6]);          // Payer
        info.put("tokenAccountOwner", accounts[7]); // Token account owner
        info.put("tokenAccount", accounts[8]);    // Token account
        info.put("newMetadataUpdateAuthority", accounts[9]); // Update authority
        info.put("metadata", accounts[10]);      // Metadata account
        info.put("tokenProgram", accounts[11]);  // Token program
        info.put("systemProgram", accounts[12]); // System program
        if (accounts.length > 13) {
            info.put("rent", accounts[13]);      // Rent (optional)
        }
        info.put("edition", edition);

        return info;
    }

    // Create 指令解析
    private static Map<String, Object> parseCreate(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        info.put("metadata", accounts[0]);       // Metadata account
        info.put("masterEdition", accounts[1]);  // Master edition account
        info.put("mint", accounts[2]);           // Mint account
        info.put("authority", accounts[3]);      // Authority
        info.put("payer", accounts[4]);          // Payer
        info.put("updateAuthority", accounts[5]); // Update authority
        info.put("systemProgram", accounts[6]);  // System program
        info.put("sysvarInstructions", accounts[7]); // Instructions sysvar
        if (accounts.length > 8) {
            info.put("splTokenProgram", accounts[8]); // SPL Token program (optional)
        }

        return info;
    }

    // Mint 指令解析
    private static Map<String, Object> parseMint(String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        info.put("token", accounts[0]);          // Token account
        info.put("tokenOwner", accounts[1]);     // Token owner
        info.put("metadata", accounts[2]);       // Metadata account
        info.put("masterEdition", accounts[3]);  // Master edition
        info.put("tokenRecord", accounts[4]);    // Token record
        info.put("payer", accounts[5]);          // Payer
        info.put("systemProgram", accounts[6]);  // System program
        info.put("sysvarInstructions", accounts[7]); // Instructions sysvar
        info.put("splTokenProgram", accounts[8]); // SPL Token program
        info.put("splAtaProgram", accounts[9]);  // SPL Associated Token Account program
        if (accounts.length > 10) {
            info.put("authorizationRulesProgram", accounts[10]); // Authorization Rules program (optional)
            info.put("authorizationRules", accounts[11]);       // Authorization Rules account (optional)
        }

        return info;
    }

    // 其他指令的解析方法...
    private static Map<String, Object> parseUpdateMetadataAccount(String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        // TODO: 实现具体解析逻辑
        return info;
    }

    private static Map<String, Object> parseCreateMasterEdition(String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        // TODO: 实现具体解析逻辑
        return info;
    }

    private static Map<String, Object> parseCreateMetadataAccountV2(String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        // TODO: 实现具体解析逻辑
        return info;
    }

    private static Map<String, Object> parseUpdateMetadataAccountV2(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 读取 DataV2 结构的字段
        if (buffer.get() != 0) {
            String name = parseString(buffer);
            String symbol = parseString(buffer);
            String uri = parseString(buffer);
            int sellerFeeBasisPoints = Short.toUnsignedInt(buffer.getShort());
            info.put("name", name);
            info.put("symbol", symbol);
            info.put("uri", uri);
            info.put("sellerFeeBasisPoints", sellerFeeBasisPoints);
        }
        byte[] newUpdateAuthority = null;
        if (buffer.get() != 0) { // 检查是否存在新更新权限
            newUpdateAuthority = new byte[32];
            buffer.get(newUpdateAuthority);
        }
        byte primarySaleHappened = buffer.get(); // 读取布尔值
        byte isMutable = buffer.get(); // 读取布尔值

        // 将解析后的信息放入 Map 中
        if (newUpdateAuthority != null) {
            info.put("newUpdateAuthority", Base58.encode(newUpdateAuthority));
        }
        info.put("primarySaleHappened", primarySaleHappened);
        info.put("isMutable", isMutable);

        // 账户信息
        info.put("metadataAccount", accounts[0]); // 元数据账户
        info.put("updateAuthority", accounts[1]); // 更新权限账户
        return info;
    }

    private static Map<String, Object> parseCreateMasterEditionV3(String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        info.put("edition", accounts[0]);            // Edition account
        info.put("mint", accounts[1]);               // Mint account
        info.put("updateAuthority", accounts[2]);    // Update authority
        info.put("mintAuthority", accounts[3]);      // Mint authority
        info.put("payer", accounts[4]);              // Payer
        info.put("metadata", accounts[5]);           // Metadata account
        info.put("tokenProgram", accounts[6]);       // Token program
        info.put("systemProgram", accounts[7]);      // System program
        return info;
    }

    private static Map<String, Object> parseCreateMetadataAccountV3(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 解析参数
        // 读取 DataV2 结构的字段
        String name = parseString(buffer);
        String symbol = parseString(buffer);
        String uri = parseString(buffer);
        int sellerFeeBasisPoints = Short.toUnsignedInt(buffer.getShort());
        info.put("name", name);
        info.put("symbol", symbol);
        info.put("uri", uri);
        info.put("sellerFeeBasisPoints", sellerFeeBasisPoints);

        if (buffer.get() != 0) {
            // creators 解析逻辑
            int listLength = buffer.getInt();
            List<Object> list = new ArrayList<>();
            for (int i = 0; i < listLength; i++) {
                list.add(parseCreator(buffer));
            }
            info.put("creators", list);
        }
        if (buffer.get() != 0) {
            // collection 解析逻辑
            parseCollection(buffer);
        }
        if (buffer.get() != 0) {
            // uses 解析逻辑
            parseUses(buffer);
        }
        byte isMutable = buffer.get(); // 读取布尔值
        info.put("isMutable", isMutable);
        if (buffer.get() != 0) {
            // CollectionDetails 解析逻辑
        }
        // 账户信息
        info.put("metadataAccount", accounts[0]); // 元数据账户
        info.put("mint", accounts[1]); // 铸造账户
        info.put("mintAuthority", accounts[2]); // 铸造权限账户
        info.put("payer", accounts[3]); // 资金提供者账户
        info.put("updateAuthority", accounts[4]); // 更新权限账户
        info.put("systemProgram", accounts[5]); // 系统程序账户
        return info;
    }

    private static Map<String, Object> parseCreator(ByteBuffer buffer) {
        // 假设字符串以长度前缀存储
        Map<String, Object> creatorInfo = new HashMap<>();
        byte[] addressBytes = new byte[32];
        buffer.get(addressBytes);
        creatorInfo.put("address", Base58.encode(addressBytes));
        creatorInfo.put("verified", buffer.get());
        creatorInfo.put("share", Byte.toUnsignedInt(buffer.get()));
        return creatorInfo;
    }

    private static Map<String, Object> parseCollection(ByteBuffer buffer) {
        // 假设字符串以长度前缀存储
        Map<String, Object> collectionInfo = new HashMap<>();
        collectionInfo.put("verified", buffer.get());
        byte[] addressBytes = new byte[32];
        buffer.get(addressBytes);
        collectionInfo.put("key", Base58.encode(addressBytes));
        return collectionInfo;
    }

    private static Map<String, Object> parseUses(ByteBuffer buffer) {
        // 假设字符串以长度前缀存储
        Map<String, Object> usesInfo = new HashMap<>();
        usesInfo.put("use_method",Byte.toUnsignedInt(buffer.get()));
        usesInfo.put("remaining", Long.toUnsignedString(buffer.getLong()));
        usesInfo.put("total", Long.toUnsignedString(buffer.getLong()));
        return usesInfo;
    }
    private static String parseString(ByteBuffer buffer) {
        // 假设字符串以长度前缀存储
        int length = buffer.getInt();
        byte[] strBytes = new byte[length];
        buffer.get(strBytes);
        return new String(strBytes);
    }

    private static Map<String, Object> parseBurn(String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        info.put("metadata", accounts[0]);           // Metadata account
        info.put("owner", accounts[1]);              // Owner
        info.put("mint", accounts[2]);               // Mint account
        info.put("tokenAccount", accounts[3]);       // Token account
        info.put("masterEditionAccount", accounts[4]); // Master edition account
        info.put("splTokenProgram", accounts[5]);    // SPL Token program
        info.put("collectionMetadata", accounts[6]); // Collection metadata
        return info;
    }

    private static Map<String, Object> parseMigrate(String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        return info;
    }

    private static Map<String, Object> parseTransfer(String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        info.put("token", accounts[0]);              // Token account
        info.put("tokenOwner", accounts[1]);         // Token owner
        info.put("destination", accounts[2]);        // Destination account
        info.put("destinationOwner", accounts[3]);   // Destination owner
        info.put("mint", accounts[4]);               // Mint account
        info.put("metadata", accounts[5]);           // Metadata account
        info.put("authority", accounts[6]);          // Authority
        info.put("payer", accounts[7]);              // Payer
        info.put("systemProgram", accounts[8]);      // System program
        info.put("sysvarInstructions", accounts[9]); // Instructions sysvar
        info.put("splTokenProgram", accounts[10]);   // SPL Token program
        info.put("splAtaProgram", accounts[11]);     // SPL ATA program
        return info;
    }

    private static Map<String, Object> parseUpdate(String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        info.put("metadata", accounts[0]);           // Metadata account
        info.put("updateAuthority", accounts[1]);    // Update authority
        info.put("updateAuthorityOnMetadata", accounts[2]); // Update authority on metadata
        info.put("mint", accounts[3]);               // Mint account
        info.put("collectionMetadata", accounts[4]); // Collection metadata
        info.put("collectionAuthority", accounts[5]); // Collection authority
        return info;
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
} 