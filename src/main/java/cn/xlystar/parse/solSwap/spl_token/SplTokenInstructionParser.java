package cn.xlystar.parse.solSwap.spl_token;

import org.bitcoinj.core.Base58;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SplTokenInstructionParser {
    public static Map<String, Object> parseInstruction(byte[] data, String[] accounts) {
        Map<String, Object> result = new HashMap<>();

        if (data == null || data.length == 0) {
            throw new IllegalArgumentException("Empty instruction data");
        }

        try {
            int instructionType = data[0] & 0xFF;
            SplTokenInstruction instruction = SplTokenInstruction.fromValue(instructionType);
            result.put("type", instruction.name());

            byte[] instructionData = Arrays.copyOfRange(data, 1, data.length);
            Map<String, Object> info = new HashMap<>();

            switch (instruction) {
                case InitializeMint:
                    info = parseInitializeMint(instructionData, accounts);
                    break;
                case InitializeAccount:
                    info = parseInitializeAccount(accounts);
                    break;
                case InitializeMultisig:
                    info = parseInitializeMultisig(instructionData, accounts);
                    break;
                case Transfer:
                    info = parseTransfer(instructionData, accounts);
                    break;
                case Approve:
                    info = parseApprove(instructionData, accounts);
                    break;
                case Revoke:
                    info = parseRevoke(accounts);
                    break;
                case SetAuthority:
                    info = parseSetAuthority(instructionData, accounts);
                    break;
                case MintTo:
                    info = parseMintTo(instructionData, accounts);
                    break;
                case Burn:
                    info = parseBurn(instructionData, accounts);
                    break;
                case CloseAccount:
                    info = parseCloseAccount(accounts);
                    break;
                case FreezeAccount:
                    info = parseFreezeAccount(accounts);
                    break;
                case ThawAccount:
                    info = parseThawAccount(accounts);
                    break;
                case TransferChecked:
                    info = parseTransferChecked(instructionData, accounts);
                    break;
                case ApproveChecked:
                    info = parseApproveChecked(instructionData, accounts);
                    break;
                case MintToChecked:
                    info = parseMintToChecked(instructionData, accounts);
                    break;
                case BurnChecked:
                    info = parseBurnChecked(instructionData, accounts);
                    break;
                default:
                    info.put("message", "Unsupported instruction type: " + instruction.name());
            }

            result.put("info", info);

        } catch (Exception e) {
            e.printStackTrace();
            result.put("error", "Failed to parse instruction: " + e.getMessage());
        }

        return result;
    }

    // Transfer 指令解析
    private static Map<String, Object> parseTransfer(byte[] data, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        System.out.println("Transfer raw data (hex): " + bytesToHex(data));

        // 读取转账金额 (u64)
        ByteBuffer amountBuffer = ByteBuffer.wrap(data);
        amountBuffer.order(ByteOrder.LITTLE_ENDIAN);
        long amount = amountBuffer.getLong();

        info.put("source", accounts[0]);          // 源代币账户
        info.put("destination", accounts[1]);     // 目标代币账户
        info.put("authority", accounts[2]);       // 源账户所有者
        info.put("amount", amount);

        return info;
    }

    // MintTo 指令解析
    private static Map<String, Object> parseMintTo(byte[] data, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        System.out.println("MintTo raw data (hex): " + bytesToHex(data));

        // 读取铸造金额 (u64)
        ByteBuffer amountBuffer = ByteBuffer.wrap(data);
        amountBuffer.order(ByteOrder.LITTLE_ENDIAN);
        long amount = amountBuffer.getLong();

        info.put("mint", accounts[0]);           // 代币铸造账户
        info.put("destination", accounts[1]);     // 目标代币账户
        info.put("mintAuthority", accounts[2]);   // 铸造权限账户
        info.put("amount", amount);

        return info;
    }

    // Burn 指令解析
    private static Map<String, Object> parseBurn(byte[] data, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        System.out.println("Burn raw data (hex): " + bytesToHex(data));

        // 读取销毁金额 (u64)
        ByteBuffer amountBuffer = ByteBuffer.wrap(data);
        amountBuffer.order(ByteOrder.LITTLE_ENDIAN);
        long amount = amountBuffer.getLong();

        info.put("account", accounts[0]);         // 要销毁代币的账户
        info.put("mint", accounts[1]);           // 代币铸造账户
        info.put("authority", accounts[2]);       // 代币账户所有者
        info.put("amount", amount);

        return info;
    }

    // InitializeMint 指令解析
    private static Map<String, Object> parseInitializeMint(byte[] data, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        System.out.println("InitializeMint raw data (hex): " + bytesToHex(data));

        // 读取精度 (u8)
        int decimals = data[0] & 0xFF;

        // 读取铸造权限公钥 (32 bytes)
        byte[] mintAuthority = Arrays.copyOfRange(data, 1, 33);

        // 读取冻结权限公钥 (option<32 bytes>)
        boolean hasFreezeAuthority = (data[33] & 0xFF) == 1;
        byte[] freezeAuthority = null;
        if (hasFreezeAuthority) {
            freezeAuthority = Arrays.copyOfRange(data, 34, 66);
        }

        info.put("mint", accounts[0]);           // 代币铸造账户
        info.put("rent", accounts[1]);           // 租金账户
        info.put("decimals", decimals);
        info.put("mintAuthority", Base58.encode(mintAuthority));
        if (hasFreezeAuthority) {
            info.put("freezeAuthority", Base58.encode(freezeAuthority));
        }

        return info;
    }

    // InitializeAccount 指令解析
    private static Map<String, Object> parseInitializeAccount(String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        info.put("account", accounts[0]);         // 要初始化的代币账户
        info.put("mint", accounts[1]);           // 代币铸造账户
        info.put("owner", accounts[2]);          // 代币账户所有者
        info.put("rent", accounts[3]);           // 租金账户

        return info;
    }

    // Approve 指令解析
    private static Map<String, Object> parseApprove(byte[] data, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        System.out.println("Approve raw data (hex): " + bytesToHex(data));

        // 读取授权金额 (u64)
        ByteBuffer amountBuffer = ByteBuffer.wrap(data);
        amountBuffer.order(ByteOrder.LITTLE_ENDIAN);
        long amount = amountBuffer.getLong();

        info.put("source", accounts[0]);          // 源代币账户
        info.put("delegate", accounts[1]);        // 被授权账户
        info.put("authority", accounts[2]);       // 源账户所有者
        info.put("amount", amount);

        return info;
    }


    // Revoke 指令解析
    private static Map<String, Object> parseRevoke(String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        info.put("source", accounts[0]);          // 源代币账户
        info.put("authority", accounts[1]);       // 源账户所有者

        return info;
    }

    // SetAuthority 指令解析
    private static Map<String, Object> parseSetAuthority(byte[] data, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        System.out.println("SetAuthority raw data (hex): " + bytesToHex(data));

        // 读取权限类型 (u8)
        int authorityType = data[0] & 0xFF;

        // 读取新权限公钥 (option<32 bytes>)
        boolean hasNewAuthority = (data[1] & 0xFF) == 1;
        byte[] newAuthority = null;
        if (hasNewAuthority) {
            newAuthority = Arrays.copyOfRange(data, 2, 34);
        }

        info.put("account", accounts[0]);         // 要修改权限的账户
        info.put("currentAuthority", accounts[1]); // 当前权限账户
        info.put("authorityType", authorityType);
        if (hasNewAuthority) {
            info.put("newAuthority", Base58.encode(newAuthority));
        }

        return info;
    }

    // CloseAccount 指令解析
    private static Map<String, Object> parseCloseAccount(String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        info.put("account", accounts[0]);         // 要关闭的代币账户
        info.put("destination", accounts[1]);     // 接收租金的账户
        info.put("authority", accounts[2]);       // 代币账户所有者

        return info;
    }

    // FreezeAccount 指令解析
    private static Map<String, Object> parseFreezeAccount(String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        info.put("account", accounts[0]);         // 要冻结的代币账户
        info.put("mint", accounts[1]);           // 代币铸造账户
        info.put("authority", accounts[2]);       // 冻结权限账户

        return info;
    }

    // ThawAccount 指令解析
    private static Map<String, Object> parseThawAccount(String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        info.put("account", accounts[0]);         // 要解冻的代币账户
        info.put("mint", accounts[1]);           // 代币铸造账户
        info.put("authority", accounts[2]);       // 冻结权限账户

        return info;
    }

    // TransferChecked 指令解析
    private static Map<String, Object> parseTransferChecked(byte[] data, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        System.out.println("TransferChecked raw data (hex): " + bytesToHex(data));

        // 读取转账金额 (u64)
        ByteBuffer amountBuffer = ByteBuffer.wrap(data);
        amountBuffer.order(ByteOrder.LITTLE_ENDIAN);
        long amount = amountBuffer.getLong();

        // 读取精度 (u8)
        int decimals = data[8] & 0xFF;

        info.put("source", accounts[0]);          // 源代币账户
        info.put("mint", accounts[1]);           // 代币铸造账户
        info.put("destination", accounts[2]);     // 目标代币账户
        info.put("authority", accounts[3]);       // 源账户所有者
        info.put("amount", amount);
        info.put("decimals", decimals);

        return info;
    }

    // ApproveChecked 指令解析
    private static Map<String, Object> parseApproveChecked(byte[] data, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        System.out.println("ApproveChecked raw data (hex): " + bytesToHex(data));

        // 读取授权金额 (u64)
        ByteBuffer amountBuffer = ByteBuffer.wrap(data);
        amountBuffer.order(ByteOrder.LITTLE_ENDIAN);
        long amount = amountBuffer.getLong();

        // 读取精度 (u8)
        int decimals = data[8] & 0xFF;

        info.put("source", accounts[0]);          // 源代币账户
        info.put("mint", accounts[1]);           // 代币铸造账户
        info.put("delegate", accounts[2]);        // 被授权账户
        info.put("authority", accounts[3]);       // 源账户所有者
        info.put("amount", amount);
        info.put("decimals", decimals);

        return info;
    }

    // MintToChecked 指令解析
    private static Map<String, Object> parseMintToChecked(byte[] data, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        System.out.println("MintToChecked raw data (hex): " + bytesToHex(data));

        // 读取铸造金额 (u64)
        ByteBuffer amountBuffer = ByteBuffer.wrap(data);
        amountBuffer.order(ByteOrder.LITTLE_ENDIAN);
        long amount = amountBuffer.getLong();

        // 读取精度 (u8)
        int decimals = data[8] & 0xFF;

        info.put("mint", accounts[0]);           // 代币铸造账户
        info.put("destination", accounts[1]);     // 目标代币账户
        info.put("mintAuthority", accounts[2]);   // 铸造权限账户
        info.put("amount", amount);
        info.put("decimals", decimals);

        return info;
    }

    // BurnChecked 指令解析
    private static Map<String, Object> parseBurnChecked(byte[] data, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        System.out.println("BurnChecked raw data (hex): " + bytesToHex(data));

        // 读取销毁金额 (u64)
        ByteBuffer amountBuffer = ByteBuffer.wrap(data);
        amountBuffer.order(ByteOrder.LITTLE_ENDIAN);
        long amount = amountBuffer.getLong();

        // 读取精度 (u8)
        int decimals = data[8] & 0xFF;

        info.put("account", accounts[0]);         // 要销毁代币的账户
        info.put("mint", accounts[1]);           // 代币铸造账户
        info.put("authority", accounts[2]);       // 代币账户所有者
        info.put("amount", amount);
        info.put("decimals", decimals);

        return info;
    }

    // InitializeMultisig 指令解析
    private static Map<String, Object> parseInitializeMultisig(byte[] data, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        System.out.println("InitializeMultisig raw data (hex): " + bytesToHex(data));

        // 读取签名者数量 (u8)
        int m = data[0] & 0xFF;

        info.put("account", accounts[0]);         // 多重签名账户
        info.put("rent", accounts[1]);           // 租金账户
        info.put("m", m);

        // 添加所有签名者
        String[] signers = new String[accounts.length - 2];
        System.arraycopy(accounts, 2, signers, 0, signers.length);
        info.put("signers", signers);

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