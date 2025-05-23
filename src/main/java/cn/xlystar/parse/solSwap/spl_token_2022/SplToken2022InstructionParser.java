package cn.xlystar.parse.solSwap.spl_token_2022;

import cn.xlystar.parse.solSwap.InstructionParser;
import org.bitcoinj.core.Base58;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class SplToken2022InstructionParser extends InstructionParser {

    public static final String PROGRAM_ID = "TokenzQdBNbLqP5VEhdkAS6EPFLC1PHnBqCXEpPxuEb";

    @Override
    public String getMethodId(ByteBuffer buffer) {
        return Byte.toUnsignedInt(buffer.get()) + "";
    }

    @Override
    public Map<String, Object> matchInstruction(String methodId, ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info;
        SplToken2022Instruction splToken2022Instruction = SplToken2022Instruction.fromValue(Integer.parseInt(methodId));
        switch (splToken2022Instruction) {
            // 初始化相关指令
            case InitializeMint:
            case InitializeMint2:
                info = parseInitializeMint(buffer, accounts);
                break;
            case InitializeAccount:
                info = parseInitializeAccount(accounts);
                break;
            case InitializeAccount2:
                info = parseInitializeAccount2(buffer, accounts);
                break;
            case InitializeAccount3:
                info = parseInitializeAccount3(buffer, accounts);
                break;

            // 基础代币操作
            case Transfer:
            case MintTo:
            case Burn:
                info = parseAmountInstruction(buffer, accounts, splToken2022Instruction);
                break;

            case SetAuthority:
                info = parseSetAuthorityInstruction(buffer, accounts);
                break;

            // 账户状态相关操作
            case FreezeAccount:
            case ThawAccount:
                info = parseFreezeThawInstruction(accounts);
                break;
            case CloseAccount:
                info = parseCloseAccountInstruction(accounts);
                break;
            // 原生代币相关
            case SyncNative:
                info = parseSyncNativeInstruction(accounts);
                break;
            case CreateNativeMint:
                info = parseCreateNativeMintInstruction(accounts);
                break;
            case TransferChecked:
            case MintToChecked:
            case BurnChecked:
                info = parseCheckedInstruction(buffer, accounts, splToken2022Instruction);
                break;

            // @warning 以下代码，没有测试 case 保护
            // case InitializeMultisig:
            // case InitializeMultisig2:
            //     info = parseInitializeMultisig(buffer, accounts);
            //     break;
            // case InitializeImmutableOwner:
            //     info = parseInitializeImmutableOwner(accounts);
            //     break;
            // case InitializeNonTransferableMint:
            //     info = parseInitializeNonTransferableMint(accounts);
            //     break;
            // case InitializePermanentDelegate:
            //     info = parseInitializePermanentDelegate(accounts);
            //     break;
            // // 带精度检查的操作

            // // 授权相关操作
            // case Approve:
            // case ApproveChecked:
            //     info = parseApproveInstruction(buffer, accounts, instruction);
            //     break;
            // case Revoke:
            //     info = parseRevokeInstruction(accounts);
            //     break;
            // // 扩展功能相关
            // case GetAccountDataSize:
            //     info = parseGetAccountDataSizeInstruction(buffer);
            //     break;
            // case Reallocate:
            //     info = parseReallocateInstruction(accounts);
            //     break;
            // case WithdrawExcessLamports:
            //     info = parseWithdrawExcessLamportsInstruction(accounts);
            //     break;

            // // 其他扩展功能
            // case InterestBearingMintExtension:
            // case CpiGuardExtension:
            // case TransferHookExtension:
            // case MetadataPointerExtension:
            // case GroupPointerExtension:
            // case GroupMemberPointerExtension:
            // case PausableExtension:
            //     info = parseExtensionInstruction(buffer, accounts, instruction);
            //     break;

            default:
                return new HashMap<>();
        }
        return info;
    }


    private static Map<String, Object> parseInitializeMint(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        info.put("mint", accounts[0]);

        // 如果 accounts 长度大于 1，则表示是 InitializeMint2
        if (accounts.length > 1) {
            info.put("rentSysvar", accounts[1]);
        }

        info.put("decimals", buffer.get() & 0xFF);
        byte[] mintAuthority = new byte[32];
        buffer.get(mintAuthority);
        info.put("mintAuthority", Base58.encode(mintAuthority));

        if (buffer.get() == 1) {
            byte[] freezeAuthority = new byte[32];
            buffer.get(freezeAuthority);
            info.put("freezeAuthority", Base58.encode(freezeAuthority));
        }

        return info;
    }

    private static Map<String, Object> parseInitializeAccount(String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        info.put("account", accounts[0]);
        info.put("mint", accounts[1]);
        info.put("owner", accounts[2]);
        info.put("rentSysvar", accounts[3]);
        return info;
    }

    private static Map<String, Object> parseInitializeAccount2(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        info.put("account", accounts[0]);
        info.put("mint", accounts[1]);
        info.put("rentSysvar", accounts[2]);

        byte[] mintAuthority = new byte[32];
        buffer.get(mintAuthority);
        info.put("owner", Base58.encode(mintAuthority));

        return info;
    }

    private static Map<String, Object> parseInitializeAccount3(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        info.put("account", accounts[0]);
        info.put("mint", accounts[1]);

        byte[] mintAuthority = new byte[32];
        buffer.get(mintAuthority);
        info.put("owner", Base58.encode(mintAuthority));
        return info;
    }

    private static Map<String, Object> parseFreezeThawInstruction(String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        info.put("account", accounts[0]);
        info.put("mint", accounts[1]);
        info.put("authority", accounts[2]);
        return info;
    }

    private static Map<String, Object> parseCloseAccountInstruction(String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        info.put("account", accounts[0]);
        info.put("destination", accounts[1]);
        info.put("owner", accounts[2]);
        return info;
    }

    private static Map<String, Object> parseSyncNativeInstruction(String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        info.put("account", accounts[0]);
        return info;
    }

    private static Map<String, Object> parseCreateNativeMintInstruction(String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        info.put("payer", accounts[0]);
        info.put("nativeMint", accounts[1]);
        return info;
    }

    private static Map<String, Object> parseSetAuthorityInstruction(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        info.put("mint", accounts[0]);
        info.put("authority", accounts[1]);

        info.put("authorityType", buffer.get() & 0xFF);
        if (buffer.get() == 1) {
            byte[] freezeAuthority = new byte[32];
            buffer.get(freezeAuthority);
            info.put("newAuthority", Base58.encode(freezeAuthority));
        }
        return info;
    }

    private static Map<String, Object> parseAmountInstruction(ByteBuffer buffer, String[] accounts, SplToken2022Instruction instruction) {
        Map<String, Object> info = new HashMap<>();
        switch (instruction) {
            case Transfer:
                info.put("source", accounts[0]);
                info.put("destination", accounts[1]);
                info.put("authority", accounts[2]);
                break;
            case MintTo:
                info.put("mint", accounts[0]);
                info.put("account", accounts[1]);
                info.put("authority", accounts[2]);
                break;
            case Burn:
                info.put("account", accounts[0]);
                info.put("mint", accounts[1]);
                info.put("authority", accounts[2]);
                break;
        }

        info.put("amount", Long.toUnsignedString(buffer.getLong()));
        return info;
    }

    // private static Map<String, Object> parseInitializeMultisig(ByteBuffer buffer, String[] accounts) {
    //     Map<String, Object> info = new HashMap<>();
    //     ByteBuffer buffer = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN);

    //     info.put("account", accounts[0]);
    //     info.put("signers", Arrays.copyOfRange(accounts, 1, accounts.length));
    //     info.put("m", buffer.get() & 0xFF);

    //     return info;
    // }

    // private static Map<String, Object> parseInitializeImmutableOwner(String[] accounts) {
    //     Map<String, Object> info = new HashMap<>();
    //     info.put("account", accounts[0]);
    //     return info;
    // }

    // private static Map<String, Object> parseInitializeNonTransferableMint(String[] accounts) {
    //     Map<String, Object> info = new HashMap<>();
    //     info.put("mint", accounts[0]);
    //     return info;
    // }

    // private static Map<String, Object> parseInitializePermanentDelegate(String[] accounts) {
    //     Map<String, Object> info = new HashMap<>();
    //     info.put("mint", accounts[0]);
    //     info.put("delegate", accounts[1]);
    //     return info;
    // }

     private static Map<String, Object> parseCheckedInstruction(ByteBuffer buffer, String[] accounts, SplToken2022Instruction instruction) {
         Map<String, Object> info = new HashMap<>();

         switch (instruction) {
             case TransferChecked:
                 info.put("source", accounts[0]);
                 info.put("mint", accounts[1]);
                 info.put("destination", accounts[2]);
                 info.put("authority", accounts[3]);
                 break;
             case MintToChecked:
                 info.put("mint", accounts[0]);
                 info.put("account", accounts[1]);
                 info.put("authority", accounts[2]);
                 break;
             case BurnChecked:
                 info.put("account", accounts[0]);
                 info.put("mint", accounts[1]);
                 info.put("authority", accounts[2]);
                 break;
         }

         info.put("amount", buffer.getLong());
         info.put("decimals", buffer.get() & 0xFF);
         return info;
     }

    // private static Map<String, Object> parseApproveInstruction(ByteBuffer buffer, String[] accounts, SplToken2022Instruction instruction) {
    //     Map<String, Object> info = new HashMap<>();
    //     ByteBuffer buffer = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN);

    //     info.put("source", accounts[0]);
    //     info.put("delegate", accounts[1]);
    //     info.put("authority", accounts[2]);

    //     info.put("amount", buffer.getLong());
    //     if (instruction == SplToken2022Instruction.ApproveChecked) {
    //         info.put("decimals", buffer.get() & 0xFF);
    //     }

    //     return info;
    // }

    // private static Map<String, Object> parseRevokeInstruction(String[] accounts) {
    //     Map<String, Object> info = new HashMap<>();
    //     info.put("source", accounts[0]);
    //     info.put("authority", accounts[1]);
    //     return info;
    // }

    // private static Map<String, Object> parseGetAccountDataSizeInstruction(ByteBuffer buffer) {
    //     Map<String, Object> info = new HashMap<>();
    //     ByteBuffer buffer = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN);
    //     info.put("extensionTypes", buffer.getInt());
    //     return info;
    // }

    // private static Map<String, Object> parseReallocateInstruction(String[] accounts) {
    //     Map<String, Object> info = new HashMap<>();
    //     info.put("account", accounts[0]);
    //     info.put("payer", accounts[1]);
    //     info.put("systemProgram", accounts[2]);
    //     return info;
    // }

    // private static Map<String, Object> parseWithdrawExcessLamportsInstruction(String[] accounts) {
    //     Map<String, Object> info = new HashMap<>();
    //     info.put("source", accounts[0]);
    //     info.put("destination", accounts[1]);
    //     info.put("authority", accounts[2]);
    //     return info;
    // }

    // private static Map<String, Object> parseExtensionInstruction(ByteBuffer buffer, String[] accounts, SplToken2022Instruction instruction) {
    //     Map<String, Object> info = new HashMap<>();
    //     info.put("mint", accounts[0]);
    //     info.put("extensionType", instruction.name());

    //     ByteBuffer buffer = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN);
    //     switch (instruction) {
    //         case InterestBearingMintExtension:
    //             info.put("rateAuthority", accounts[1]);
    //             info.put("rate", buffer.getInt());
    //             break;
    //         case CpiGuardExtension:
    //             info.put("authority", accounts[1]);
    //             info.put("enabled", buffer.get() != 0);
    //             break;
    //         case TransferHookExtension:
    //             info.put("authority", accounts[1]);
    //             info.put("programId", accounts[2]);
    //             break;
    //         case MetadataPointerExtension:
    //         case GroupPointerExtension:
    //         case GroupMemberPointerExtension:
    //             info.put("authority", accounts[1]);
    //             info.put("metadataAddress", accounts[2]);
    //             break;
    //         case PausableExtension:
    //             info.put("authority", accounts[1]);
    //             info.put("paused", buffer.get() != 0);
    //             break;
    //     }

    //     return info;
    // }
}