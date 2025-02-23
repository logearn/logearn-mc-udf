package cn.xlystar.parse.solSwap.system_program;

import cn.xlystar.parse.solSwap.InstructionParser;
import org.bitcoinj.core.Base58;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class SystemInstructionParser extends InstructionParser {
    public static final String PROGRAM_ID = "11111111111111111111111111111111";
    @Override
    public String getMethodId(ByteBuffer buffer) {
        String methodId = buffer.get() + "";
        if (buffer.limit() >= 4) {
            // 填充移动
            buffer.get(new byte[3]);
        }
        return methodId;
    }

    public Map<String, Object> matchInstruction(String methodId, ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info;
        switch (SystemInstruction.fromValue(Integer.parseInt(methodId))) {
            case CreateAccount: // 0
                info = parseCreateAccount(buffer, accounts);
                break;
            case Assign: // 1
                info = parseAssign(buffer, accounts);
                break;
            case Transfer: // 2
                info = parseTransfer(buffer, accounts);
                break;
            case CreateAccountWithSeed: // 3
                info = parseCreateAccountWithSeed(buffer, accounts);
                break;
            case AdvanceNonceAccount: // 4
                info = parseAdvanceNonceAccount(accounts);
                break;
            case WithdrawNonceAccount: // 5
                info = parseWithdrawNonceAccount(buffer, accounts);
                break;
            case InitializeNonceAccount: // 6
                info = parseInitializeNonceAccount(buffer, accounts);
                break;
            case AuthorizeNonceAccount: //7
                info = parseAuthorizeNonceAccount(buffer, accounts);
                break;
            case Allocate: // 8
                info = parseAllocate(buffer, accounts);
                break;
            case AllocateWithSeed: // 9
                info = parseAllocateWithSeed(buffer, accounts);
                break;
            case AssignWithSeed: // 10
                info = parseAssignWithSeed(buffer, accounts);
                break;
            case TransferWithSeed: // 11
                info = parseTransferWithSeed(buffer, accounts);
                break;
            case UpgradeNonceAccount: // 12
                info = parseUpgradeNonceAccount(accounts);
                break;
            default:
                return new HashMap<>();
        }
        return info;
    }

    // CreateAccount 指令解析
    /// Create a new account
    ///
    /// # Account references
    ///   0. `[WRITE, SIGNER]` Funding account
    ///   1. `[WRITE, SIGNER]` New account
//    CreateAccount {
//        /// Number of lamports to transfer to the new account
//        lamports: u64,
//
//                /// Number of bytes of memory to allocate
//                space: u64,
//
//                /// Address of program that will own the new account
//                owner: Pubkey,
//    },
    private static Map<String, Object> parseCreateAccount(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 1. 读取 lamports (8字节)
        String lamports = Long.toUnsignedString(buffer.getLong());

        // 2. 读取 space (8字节)
//        long space = buffer.getLong();
        String space = Long.toUnsignedString(buffer.getLong());

        // 3. 读取 owner (32字节)
        byte[] owner = new byte[32];
        buffer.get(owner);
        String ownerStr = Base58.encode(owner);

        // 设置返回信息
        info.put("source", accounts[0]);
        info.put("newAccount", accounts[1]);
        info.put("lamports", lamports);
        info.put("space", space);
        info.put("owner", ownerStr);

        return info;

    }

    // Assign 指令解析
    /// Assign account to a program
    ///
    /// # Account references
    ///   0. `[WRITE, SIGNER]` Assigned account public key
//    Assign {
//        /// Owner program account
//        owner: Pubkey,
//    },
    private static Map<String, Object> parseAssign(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 读取 owner (32字节)
        byte[] newOwner = new byte[32];
        buffer.get(newOwner);

        info.put("account", accounts[0]);
        info.put("owner", Base58.encode(newOwner));

        return info;
    }

    // Transfer 指令解析
    /// Transfer lamports
    ///
    /// # Account references
    ///   0. `[WRITE, SIGNER]` Funding account
    ///   1. `[WRITE]` Recipient account
//    Transfer { lamports: u64 },
    private static Map<String, Object> parseTransfer(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        String lamports = Long.toUnsignedString(buffer.getLong());

        info.put("source", accounts[0]);
        info.put("destination", accounts[1]);
        info.put("lamports", lamports);

        return info;
    }

    // CreateAccountWithSeed 指令解析
    /// Create a new account at an address derived from a base pubkey and a seed
    ///
    /// # Account references
    ///   0. `[WRITE, SIGNER]` Funding account
    ///   1. `[WRITE]` Created account
    ///   2. `[SIGNER]` (optional) Base account; the account matching the base Pubkey below must be
    ///                          provided as a signer, but may be the same as the funding account
    ///                          and provided as account 0
    //    CreateAccountWithSeed {
    //        /// Base public key
    //        base: Pubkey,
    //
    //                /// String of ASCII chars, no longer than `Pubkey::MAX_SEED_LEN`
    //                seed: String,
    //
    //                /// Number of lamports to transfer to the new account
    //                lamports: u64,
    //
    //                /// Number of bytes of memory to allocate
    //                space: u64,
    //
    //                /// Owner program account address
    //                owner: Pubkey,
    //    }
    private static Map<String, Object> parseCreateAccountWithSeed(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 1. 读取 baee (32字节)
        byte[] baseBytes = new byte[32];
        buffer.get(baseBytes);
        String base = Base58.encode(baseBytes);

        // 2. 读取 seed 长度和内容
        int seedLength = Math.toIntExact(buffer.getLong());
        byte[] seedBytes = new byte[seedLength];
        buffer.get(seedBytes);
        String seed = new String(seedBytes);

        String lamports = Long.toUnsignedString(buffer.getLong());

        // 3. 读取 space (8字节)
//        long space = buffer.getLong();
        String space = Long.toUnsignedString(buffer.getLong());

        // 4. 读取 owner (32字节)
        byte[] ownerBytes = new byte[32];
        buffer.get(ownerBytes);
        String owner = Base58.encode(ownerBytes);

        info.put("source", accounts[0]);
        info.put("newAccount", accounts[1]);
        info.put("base", base);
        info.put("seed", seed);
        info.put("lamports", lamports);
        info.put("space", space);
        info.put("owner", owner);

        return info;
    }

    /// Consumes a stored nonce, replacing it with a successor
    ///
    /// # Account references
    ///   0. `[WRITE]` Nonce account
    ///   1. `[]` RecentBlockhashes sysvar
    ///   2. `[SIGNER]` Nonce authority
    //    AdvanceNonceAccount,
    private static Map<String, Object> parseAdvanceNonceAccount(String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        info.put("nonceAccount", accounts[0]);
        info.put("recentBlockhashesSysvar", accounts[1]);
        info.put("nonceAuthority", accounts[2]);

        return info;
    }

    /// Allocate space in a (possibly new) account without funding
    ///
    /// # Account references
    ///   0. `[WRITE, SIGNER]` New account
//    Allocate {
//        /// Number of bytes of memory to allocate
//        space: u64,
//    },

    private static Map<String, Object> parseAllocate(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        String space = Long.toUnsignedString(buffer.getLong());

        info.put("account", accounts[0]);
        info.put("space", space);
        return info;
    }

    /// Transfer lamports from a derived address
    ///
    /// # Account references
    ///   0. `[WRITE]` Funding account
    ///   1. `[SIGNER]` Base for funding account
    ///   2. `[WRITE]` Recipient account
//    TransferWithSeed {
//                /// Amount to transfer
//                lamports: u64,
//
//                /// Seed to use to derive the funding account address
//                from_seed: String,
//
//                /// Owner to use to derive the funding account address
//                from_owner: Pubkey,
//    },
    private static Map<String, Object> parseTransferWithSeed(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 1. 读取 lamports (8字节)
        String lamports = Long.toUnsignedString(buffer.getLong());

        // 2. 读取 seed 长度和内容
        int seedLength = Math.toIntExact(buffer.getLong());
        byte[] seedBytes = new byte[seedLength];
        buffer.get(seedBytes);
        String seed = new String(seedBytes);

        // 3. 读取 owner (32字节)
        byte[] ownerBytes = new byte[32];
        buffer.get(ownerBytes);

        // 设置返回信息
        info.put("source", accounts[0]);  // funding account
        info.put("sourceBase", accounts[1]);      // source account
        info.put("destination", accounts[2]);        // destination account
        info.put("sourceSeed", seed);
        info.put("sourceOwner", Base58.encode(ownerBytes));
        info.put("lamports", lamports);

        return info;
    }

    /// Withdraw funds from a nonce account
    ///
    /// # Account references
    ///   0. `[WRITE]` Nonce account
    ///   1. `[WRITE]` Recipient account
    ///   2. `[]` RecentBlockhashes sysvar
    ///   3. `[]` Rent sysvar
    ///   4. `[SIGNER]` Nonce authority
    ///
    /// The `u64` parameter is the lamports to withdraw, which must leave the
    /// account balance above the rent exempt reserve or at zero.
//    WithdrawNonceAccount(u64),
    private static Map<String, Object> parseWithdrawNonceAccount(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        String lamports = Long.toUnsignedString(buffer.getLong());

        info.put("nonceAccount", accounts[0]);
        info.put("destination", accounts[1]);
        info.put("recentBlockhashesSysvar", accounts[2]);
        info.put("recentSysvar", accounts[3]);
        info.put("nonceAuthority", accounts[4]);
        info.put("lamports", lamports);

        return info;
    }

    /// Drive state of Uninitialized nonce account to Initialized, setting the nonce value
    ///
    /// # Account references
    ///   0. `[WRITE]` Nonce account
    ///   1. `[]` RecentBlockhashes sysvar
    ///   2. `[]` Rent sysvar
    ///
    /// The `Pubkey` parameter specifies the entity authorized to execute nonce
    /// instruction on the account
    ///
    /// No signatures are required to execute this instruction, enabling derived
    /// nonce account addresses
//    InitializeNonceAccount(Pubkey),
    private static Map<String, Object> parseInitializeNonceAccount(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        byte[] authority = new byte[32];
        buffer.get(authority);

        info.put("nonceAccount", accounts[0]);
        info.put("recentBlockhashesSysvar", accounts[1]);
        info.put("rentSysvar", accounts[2]);
        info.put("nonceAuthority", Base58.encode(authority));

        return info;
    }

    /// Change the entity authorized to execute nonce instructions on the account
    ///
    /// # Account references
    ///   0. `[WRITE]` Nonce account
    ///   1. `[SIGNER]` Nonce authority
    ///
    /// The `Pubkey` parameter identifies the entity to authorize
//    AuthorizeNonceAccount(Pubkey),
    private static Map<String, Object> parseAuthorizeNonceAccount(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        byte[] newAuthority = new byte[32];
        buffer.get(newAuthority);

        info.put("nonce", accounts[0]);
        info.put("authority", accounts[1]);
        info.put("newAuthority", Base58.encode(newAuthority));
        return info;
    }

    /// Allocate space for and assign an account at an address
    ///    derived from a base public key and a seed
    ///
    /// # Account references
    ///   0. `[WRITE]` Allocated account
    ///   1. `[SIGNER]` Base account
//    AllocateWithSeed {
//                /// Base public key
//                base: Pubkey,
//
//                /// String of ASCII chars, no longer than `pubkey::MAX_SEED_LEN`
//                seed: String,
//
//                /// Number of bytes of memory to allocate
//                space: u64,
//
//                /// Owner program account
//                owner: Pubkey,
//    },
    private static Map<String, Object> parseAllocateWithSeed(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        // 1. 读取 baee (32字节)
        byte[] baseBytes = new byte[32];
        buffer.get(baseBytes);
        String base = Base58.encode(baseBytes);

        // 2. 读取 seed 长度和内容
        int seedLength = Math.toIntExact(buffer.getLong());
        byte[] seedBytes = new byte[seedLength];
        buffer.get(seedBytes);
        String seed = new String(seedBytes);

        // 3. 读取 space (8字节)
        String space = Long.toUnsignedString(buffer.getLong());

        // 4. 读取 owner (32字节)
        byte[] ownerBytes = new byte[32];
        buffer.get(ownerBytes);
        String owner = Base58.encode(ownerBytes);

        info.put("account", accounts[0]);
        info.put("base", base);
        info.put("seed", seed);
        info.put("space", space);
        info.put("owner", owner);

        return info;
    }

    /// Assign account to a program based on a seed
    ///
    /// # Account references
    ///   0. `[WRITE]` Assigned account
    ///   1. `[SIGNER]` Base account
//    AssignWithSeed {
//                /// Base public key
//                base: Pubkey,
//
//                /// String of ASCII chars, no longer than `pubkey::MAX_SEED_LEN`
//                seed: String,
//
//                /// Owner program account
//                owner: Pubkey,
//    },

    private static Map<String, Object> parseAssignWithSeed(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        // 1. 读取 baee (32字节)
        byte[] baseBytes = new byte[32];
        buffer.get(baseBytes);
        String base = Base58.encode(baseBytes);

        // 2. 读取 seed 长度和内容
        int seedLength = Math.toIntExact(buffer.getLong());
        byte[] seedBytes = new byte[seedLength];
        buffer.get(seedBytes);
        String seed = new String(seedBytes);

        // 3. 读取 owner (32字节)
        byte[] ownerBytes = new byte[32];
        buffer.get(ownerBytes);
        String owner = Base58.encode(ownerBytes);

        info.put("account", accounts[0]);
        info.put("base", base);
        info.put("seed", seed);
        info.put("owner", owner);

        return info;
    }

    /// One-time idempotent upgrade of legacy nonce versions in order to bump
    /// them out of chain blockhash domain.
    ///
    /// # Account references
    ///   0. `[WRITE]` Nonce account
//    UpgradeNonceAccount,
    private static Map<String, Object> parseUpgradeNonceAccount(String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        info.put("nonceAccount", accounts[0]);
        return info;
    }
} 