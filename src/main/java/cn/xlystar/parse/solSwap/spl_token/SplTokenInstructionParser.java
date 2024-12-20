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
                case InitializeMint: // 0
                    info = parseInitializeMint(instructionData, accounts);
                    break;
                case InitializeAccount: // 1
                    info = parseInitializeAccount(accounts);
                    break;
                case InitializeMultisig: // 2
                    info = parseInitializeMultisig(instructionData, accounts);
                    break;
                case Transfer: // 3
                    info = parseTransfer(instructionData, accounts);
                    break;
                case Approve: // 4
                    info = parseApprove(instructionData, accounts);
                    break;
                case Revoke: // 5
                    info = parseRevoke(accounts);
                    break;
                case SetAuthority: // 6
                    info = parseSetAuthority(instructionData, accounts);
                    break;
                case MintTo: // 7
                    info = parseMintTo(instructionData, accounts);
                    break;
                case Burn: // 8
                    info = parseBurn(instructionData, accounts);
                    break;
                case CloseAccount: // 9
                    info = parseCloseAccount(accounts);
                    break;
                case FreezeAccount: // 10
                    info = parseFreezeAccount(accounts);
                    break;
                case ThawAccount: // 11
                    info = parseThawAccount(accounts);
                    break;
                case TransferChecked: // 12
                    info = parseTransferChecked(instructionData, accounts);
                    break;
                case ApproveChecked: // 13
                    info = parseApproveChecked(instructionData, accounts);
                    break;
                case MintToChecked: // 14
                    info = parseMintToChecked(instructionData, accounts);
                    break;
                case BurnChecked: // 15
                    info = parseBurnChecked(instructionData, accounts);
                    break;
                case InitializeAccount2: // 16
                    info = parseInitializeAccount2(accounts);
                    break;
                case SyncNative: // 17
                    info = parseSyncNative(accounts);
                    break;
                case InitializeAccount3: // 18
                    info = parseInitializeAccount3(accounts);
                    break;
                case InitializeMultisig2: // 19
                    info = parseInitializeMultisig2(instructionData, accounts);
                    break;
                case InitializeMint2: // 20
                    info = parseInitializeMint2(instructionData, accounts);
                    break;
                case GetAccountDataSize: // 21
                    info = parseGetAccountDataSize(instructionData);
                    break;
                case InitializeImmutableOwner: // 22
                    info = parseInitializeImmutableOwner(accounts);
                    break;
                case AmountToUiAmount: // 23
                    info = parseAmountToUiAmount(instructionData);
                    break;
                case UiAmountToAmount: // 24
                    info = parseUiAmountToAmount(instructionData);
                    break;
                case InitializeMintCloseAuthority: // 25
                    info = parseInitializeMintCloseAuthority(instructionData, accounts);
                    break;
                case TransferFeeExtension: // 26
                    info = parseTransferFeeExtension(instructionData, accounts);
                    break;
                case ConfidentialTransferExtension: // 27
                    info = parseConfidentialTransferExtension(instructionData, accounts);
                    break;
                case DefaultAccountStateExtension: // 28
                    info = parseDefaultAccountStateExtension(instructionData, accounts);
                    break;
                case Reallocate: // 29
                    info = parseReallocate(instructionData, accounts);
                    break;
                case MemoTransferExtension: // 30
                    info = parseMemoTransferExtension(accounts);
                    break;
                case CreateNativeMint: // 31
                    info = parseCreateNativeMint(accounts);
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

    // InitializeAccount2 - 不需要 rent sysvar 的账户初始化
    private static Map<String, Object> parseInitializeAccount2(String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        info.put("account", accounts[0]);
        info.put("mint", accounts[1]);
        info.put("owner", accounts[2]);
        return info;
    }

    // SyncNative - 同步原生代币余额
    private static Map<String, Object> parseSyncNative(String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        info.put("account", accounts[0]);
        return info;
    }

    // InitializeAccount3 - 带有额外选项的账户初始化
    private static Map<String, Object> parseInitializeAccount3(String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        info.put("account", accounts[0]);
        info.put("mint", accounts[1]);
        info.put("owner", accounts[2]);
        return info;
    }

    // InitializeMultisig2 - 不需要 rent sysvar 的多重签名初始化
    private static Map<String, Object> parseInitializeMultisig2(byte[] data, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        ByteBuffer buffer = ByteBuffer.wrap(data);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        int m = buffer.get() & 0xFF;  // 需要的签名数量

        info.put("multisig", accounts[0]);
        String[] signers = Arrays.copyOfRange(accounts, 1, accounts.length);
        info.put("signers", signers);
        info.put("m", m);

        return info;
    }

    // InitializeMint2 - 不需要 rent sysvar 的铸造初始化
    private static Map<String, Object> parseInitializeMint2(byte[] data, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        ByteBuffer buffer = ByteBuffer.wrap(data);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        int decimals = buffer.get() & 0xFF;

        info.put("mint", accounts[0]);
        info.put("mintAuthority", accounts[1]);
        if (accounts.length > 2) {
            info.put("freezeAuthority", accounts[2]);
        }
        info.put("decimals", decimals);

        return info;
    }

    // GetAccountDataSize - 计算账户所需空间
    private static Map<String, Object> parseGetAccountDataSize(byte[] data) {
        Map<String, Object> info = new HashMap<>();

        ByteBuffer buffer = ByteBuffer.wrap(data);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        long extensionTypes = buffer.getLong();
        info.put("extensionTypes", extensionTypes);

        return info;
    }

    // InitializeImmutableOwner - 初始化不可变所有者
    private static Map<String, Object> parseInitializeImmutableOwner(String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        info.put("account", accounts[0]);
        return info;
    }

    // AmountToUiAmount - 金额转UI金额
    private static Map<String, Object> parseAmountToUiAmount(byte[] data) {
        Map<String, Object> info = new HashMap<>();

        ByteBuffer buffer = ByteBuffer.wrap(data);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        long amount = buffer.getLong();
        info.put("amount", amount);

        return info;
    }

    // UiAmountToAmount - UI金额转金额
    private static Map<String, Object> parseUiAmountToAmount(byte[] data) {
        Map<String, Object> info = new HashMap<>();

        ByteBuffer buffer = ByteBuffer.wrap(data);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        double uiAmount = buffer.getDouble();
        info.put("uiAmount", uiAmount);

        return info;
    }

    // InitializeMintCloseAuthority - 初始化铸造关闭权限
    private static Map<String, Object> parseInitializeMintCloseAuthority(byte[] data, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        info.put("mint", accounts[0]);
        if (data.length > 0) {
            info.put("closeAuthority", accounts[1]);
        }

        return info;
    }

    // TransferFeeExtension - 转账费用扩展
    private static Map<String, Object> parseTransferFeeExtension(byte[] data, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        info.put("mint", accounts[0]);
        info.put("authority", accounts[1]);

        ByteBuffer buffer = ByteBuffer.wrap(data);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        int transferFeeBasisPoints = buffer.getInt();
        long maximumFee = buffer.getLong();

        info.put("transferFeeBasisPoints", transferFeeBasisPoints);
        info.put("maximumFee", maximumFee);

        return info;
    }

    // ConfidentialTransferExtension - 机密转账扩展
    private static Map<String, Object> parseConfidentialTransferExtension(byte[] data, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        info.put("mint", accounts[0]);
        info.put("authority", accounts[1]);
        // 机密转账的具体参数解析
        return info;
    }

    // DefaultAccountStateExtension - 默认账户状态扩展
    private static Map<String, Object> parseDefaultAccountStateExtension(byte[] data, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        info.put("mint", accounts[0]);
        info.put("authority", accounts[1]);

        ByteBuffer buffer = ByteBuffer.wrap(data);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        int accountState = buffer.get() & 0xFF;
        info.put("accountState", accountState);

        return info;
    }

    // Reallocate - 重新分配账户空间
    private static Map<String, Object> parseReallocate(byte[] data, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        info.put("account", accounts[0]);
        info.put("payer", accounts[1]);
        info.put("systemProgram", accounts[2]);

        ByteBuffer buffer = ByteBuffer.wrap(data);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        long extensionTypes = buffer.getLong();
        info.put("extensionTypes", extensionTypes);

        return info;
    }

    // MemoTransferExtension - 备注转账扩展
    private static Map<String, Object> parseMemoTransferExtension(String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        info.put("account", accounts[0]);
        info.put("authority", accounts[1]);
        return info;
    }

    // CreateNativeMint - 创建原生代币铸造
    private static Map<String, Object> parseCreateNativeMint(String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        info.put("payer", accounts[0]);
        info.put("nativeMint", accounts[1]);
        info.put("systemProgram", accounts[2]);
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