package cn.xlystar.parse.solSwap.spl_token;

import cn.xlystar.parse.solSwap.InstructionParser;
import org.bitcoinj.core.Base58;

import java.nio.ByteBuffer;
import java.util.*;

public class SplTokenInstructionParser extends InstructionParser {

    public static final String PROGRAM_ID = "TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA";

    @Override
    public String getMethodId(ByteBuffer buffer) {
        return buffer.get() + "";
    }

    @Override
    public Map<String, Object> matchInstruction(String methodId, ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info;
        switch (SplTokenInstruction.fromValue(Integer.parseInt(methodId))) {
            case InitializeMint: // 0
                info = parseInitializeMint(buffer, accounts);
                break;
            case InitializeAccount: // 1
                info = parseInitializeAccount(accounts);
                break;
            case InitializeMultisig: // 2
                info = parseInitializeMultisig(buffer, accounts);
                break;
            case Transfer: // 3
                info = parseTransfer(buffer, accounts);
                break;
            case Approve: // 4
                info = parseApprove(buffer, accounts);
                break;
            case Revoke: // 5
                info = parseRevoke(accounts);
                break;
            case SetAuthority: // 6
                info = parseSetAuthority(buffer, accounts);
                break;
            case MintTo: // 7
                info = parseMintTo(buffer, accounts);
                break;
            case Burn: // 8
                info = parseBurn(buffer, accounts);
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
                info = parseTransferChecked(buffer, accounts);
                break;
            case ApproveChecked: // 13
                info = parseApproveChecked(buffer, accounts);
                break;
            case MintToChecked: // 14
                info = parseMintToChecked(buffer, accounts);
                break;
            case BurnChecked: // 15
                info = parseBurnChecked(buffer, accounts);
                break;
            case InitializeAccount2: // 16
                info = parseInitializeAccount2(buffer, accounts);
                break;
            case SyncNative: // 17
                info = parseSyncNative(accounts);
                break;
            case InitializeAccount3: // 18
                info = parseInitializeAccount3(buffer, accounts);
                break;
            case InitializeMultisig2: // 19
                info = parseInitializeMultisig2(buffer, accounts);
                break;
            case InitializeMint2: // 20
                info = parseInitializeMint2(buffer, accounts);
                break;
            case GetAccountDataSize: // 21
                info = parseGetAccountDataSize(buffer, accounts);
                break;
            case InitializeImmutableOwner: // 22
                info = parseInitializeImmutableOwner(accounts);
                break;
            case AmountToUiAmount: // 23
                info = parseAmountToUiAmount(buffer, accounts);
                break;
            case UiAmountToAmount: // 24
                info = parseUiAmountToAmount(buffer);
                break;
            default:
                return new HashMap<>();
        }
        return info;
    }


    /// Transfers tokens from one account to another either directly or via a
    /// delegate.  If this account is associated with the native mint then equal
    /// amounts of SOL and Tokens will be transferred to the destination
    /// account.
    ///
    /// Accounts expected by this instruction:
    ///
    ///   * Single owner/delegate
    ///   0. `[writable]` The source account.
    ///   1. `[writable]` The destination account.
    ///   2. `[signer]` The source account's owner/delegate.
    ///
    ///   * Multisignature owner/delegate
    ///   0. `[writable]` The source account.
    ///   1. `[writable]` The destination account.
    ///   2. `[]` The source account's multisignature owner/delegate.
    ///   3. ..`3+M` `[signer]` M signer accounts.
//    Transfer {
//        /// The amount of tokens to transfer.
//        amount: u64,
//    },
    private static Map<String, Object> parseTransfer(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 读取转账金额 (u64)
        String amount = Long.toUnsignedString(buffer.getLong());
        info.put("source", accounts[0]);          // 源代币账户
        info.put("destination", accounts[1]);     // 目标代币账户
        info.put("authority", accounts[2]);       // 源账户所有者
        info.put("amount", amount);
        return info;
    }

    // MintTo 指令解析
    /// Mints new tokens to an account.  The native mint does not support
    /// minting.
    ///
    /// Accounts expected by this instruction:
    ///
    ///   * Single authority
    ///   0. `[writable]` The mint.
    ///   1. `[writable]` The account to mint tokens to.
    ///   2. `[signer]` The mint's minting authority.
    ///
    ///   * Multisignature authority
    ///   0. `[writable]` The mint.
    ///   1. `[writable]` The account to mint tokens to.
    ///   2. `[]` The mint's multisignature mint-tokens authority.
    ///   3. ..`3+M` `[signer]` M signer accounts.
//    MintTo {
//        /// The amount of new tokens to mint.
//        amount: u64,
//    },
    private static Map<String, Object> parseMintTo(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 读取铸造金额 (u64)
        String amount = Long.toUnsignedString(buffer.getLong());

        info.put("mint", accounts[0]);           // 代币铸造账户
        info.put("destination", accounts[1]);     // 目标代币账户
        info.put("mintAuthority", accounts[2]);   // 铸造权限账户
        info.put("amount", amount);
        return info;
    }

    // Burn 指令解析
    /// Burns tokens by removing them from an account.  `Burn` does not support
    /// accounts associated with the native mint, use `CloseAccount` instead.
    ///
    /// Accounts expected by this instruction:
    ///
    ///   * Single owner/delegate
    ///   0. `[writable]` The account to burn from.
    ///   1. `[writable]` The token mint.
    ///   2. `[signer]` The account's owner/delegate.
    ///
    ///   * Multisignature owner/delegate
    ///   0. `[writable]` The account to burn from.
    ///   1. `[writable]` The token mint.
    ///   2. `[]` The account's multisignature owner/delegate.
    ///   3. ..`3+M` `[signer]` M signer accounts.
//    Burn {
//        /// The amount of tokens to burn.
//        amount: u64,
//    },
    // [todo] 多签解析没做，需要 case
    private static Map<String, Object> parseBurn(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 读取销毁金额 (u64)
        String amount = Long.toUnsignedString(buffer.getLong());
        info.put("account", accounts[0]);         // 要销毁代币的账户
        info.put("mint", accounts[1]);           // 代币铸造账户
        info.put("authority", accounts[2]);       // 代币账户所有者
        info.put("amount", amount);
        return info;
    }

    // InitializeMint 指令解析
    /// Initializes a new mint and optionally deposits all the newly minted
    /// tokens in an account.
    ///
    /// The `InitializeMint` instruction requires no signers and MUST be
    /// included within the same Transaction as the system program's
    /// `CreateAccount` instruction that creates the account being initialized.
    /// Otherwise another party can acquire ownership of the uninitialized
    /// account.
    ///
    /// Accounts expected by this instruction:
    ///
    ///   0. `[writable]` The mint to initialize.
    ///   1. `[]` Rent sysvar
//    InitializeMint {
//                /// Number of base 10 digits to the right of the decimal place.
//                decimals: u8,
//                /// The authority/multisignature to mint tokens.
//                mint_authority: Pubkey,
//                /// The freeze authority/multisignature of the mint.
//                freeze_authority: COption<Pubkey>,
//    },
    private static Map<String, Object> parseInitializeMint(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 读取精度 (u8)
        int decimals = Byte.toUnsignedInt(buffer.get());

        // 读取铸造权限公钥 (32 bytes)
        byte[] mintAuthority = new byte[32];
        buffer.get(mintAuthority);

        // 读取冻结权限公钥 (option<32 bytes>)
        boolean hasFreezeAuthority = (buffer.get() & 0xFF) == 1;
        byte[] freezeAuthority = new byte[32];
        if (hasFreezeAuthority) {
            buffer.get(freezeAuthority);
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
    /// Initializes a new account to hold tokens.  If this account is associated
    /// with the native mint then the token balance of the initialized account
    /// will be equal to the amount of SOL in the account. If this account is
    /// associated with another mint, that mint must be initialized before this
    /// command can succeed.
    ///
    /// The `InitializeAccount` instruction requires no signers and MUST be
    /// included within the same Transaction as the system program's
    /// `CreateAccount` instruction that creates the account being initialized.
    /// Otherwise another party can acquire ownership of the uninitialized
    /// account.
    ///
    /// Accounts expected by this instruction:
    ///
    ///   0. `[writable]`  The account to initialize.
    ///   1. `[]` The mint this account will be associated with.
    ///   2. `[]` The new account's owner/multisignature.
    ///   3. `[]` Rent sysvar
//    InitializeAccount,
    private static Map<String, Object> parseInitializeAccount(String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        info.put("account", accounts[0]);         // 要初始化的代币账户
        info.put("mint", accounts[1]);           // 代币铸造账户
        info.put("owner", accounts[2]);          // 代币账户所有者
        info.put("rent", accounts[3]);           // 租金账户

        return info;
    }

    // Approve 指令解析
    /// Approves a delegate.  A delegate is given the authority over tokens on
    /// behalf of the source account's owner.
    ///
    /// Accounts expected by this instruction:
    ///
    ///   * Single owner
    ///   0. `[writable]` The source account.
    ///   1. `[]` The delegate.
    ///   2. `[signer]` The source account owner.
    ///
    ///   * Multisignature owner
    ///   0. `[writable]` The source account.
    ///   1. `[]` The delegate.
    ///   2. `[]` The source account's multisignature owner.
    ///   3. ..`3+M` `[signer]` M signer accounts
//    Approve {
//        /// The amount of tokens the delegate is approved for.
//        amount: u64,
//    },
    private static Map<String, Object> parseApprove(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        // 读取授权金额 (u64)
        String amount = Long.toUnsignedString(buffer.getLong());

        info.put("source", accounts[0]);          // 源代币账户
        info.put("delegate", accounts[1]);        // 被授权账户
        info.put("authority", accounts[2]);       // 源账户所有者
        info.put("amount", amount);

        return info;
    }


    // Revoke 指令解析
    /// Revokes the delegate's authority.
    ///
    /// Accounts expected by this instruction:
    ///
    ///   * Single owner
    ///   0. `[writable]` The source account.
    ///   1. `[signer]` The source account owner.
    ///
    ///   * Multisignature owner
    ///   0. `[writable]` The source account.
    ///   1. `[]` The source account's multisignature owner.
    ///   2. ..`2+M` `[signer]` M signer accounts
//    Revoke,
    private static Map<String, Object> parseRevoke(String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        info.put("source", accounts[0]);          // 源代币账户
        info.put("owner", accounts[1]);       // 源账户所有者
        return info;
    }

    // SetAuthority 指令解析
    /// Sets a new authority of a mint or account.
    ///
    /// Accounts expected by this instruction:
    ///
    ///   * Single authority
    ///   0. `[writable]` The mint or account to change the authority of.
    ///   1. `[signer]` The current authority of the mint or account.
    ///
    ///   * Multisignature authority
    ///   0. `[writable]` The mint or account to change the authority of.
    ///   1. `[]` The mint's or account's current multisignature authority.
    ///   2. ..`2+M` `[signer]` M signer accounts
//    SetAuthority {
//                /// The type of authority to update.
//                authority_type: AuthorityType,
//                /// The new authority
//                new_authority: COption<Pubkey>,
//    },
    // [todo] AuthorityType data 存的是 index, 改成 value
    private static Map<String, Object> parseSetAuthority(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        // 读取权限类型 (u8)
        int authorityType = Byte.toUnsignedInt(buffer.get());

        // 读取新权限公钥 (option<32 bytes>)
        boolean hasNewAuthority = (Byte.toUnsignedInt(buffer.get()) & 0xFF) == 1;
        byte[] newAuthority = new byte[32];
        if (hasNewAuthority) {
            buffer.get(newAuthority);
        }

        info.put("account", accounts[0]);         // 要修改权限的账户
        info.put("authority", accounts[1]); // 当前权限账户
        info.put("authorityType", authorityType);
        if (hasNewAuthority) {
            info.put("newAuthority", Base58.encode(newAuthority));
        }

        return info;
    }

    /// Close an account by transferring all its SOL to the destination account.
    /// Non-native accounts may only be closed if its token amount is zero.
    ///
    /// Accounts expected by this instruction:
    ///
    ///   * Single owner
    ///   0. `[writable]` The account to close.
    ///   1. `[writable]` The destination account.
    ///   2. `[signer]` The account's owner.
    ///
    ///   * Multisignature owner
    ///   0. `[writable]` The account to close.
    ///   1. `[writable]` The destination account.
    ///   2. `[]` The account's multisignature owner.
    ///   3. ..`3+M` `[signer]` M signer accounts.
//    CloseAccount,
    private static Map<String, Object> parseCloseAccount(String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        info.put("account", accounts[0]);         // 要关闭的代币账户
        info.put("destination", accounts[1]);     // 接收租金的账户
        info.put("owner", accounts[2]);           // 代币账户所有者

        return info;
    }

    // FreezeAccount 指令解析
    /// Freeze an Initialized account using the Mint's `freeze_authority` (if
    /// set).
    ///
    /// Accounts expected by this instruction:
    ///
    ///   * Single owner
    ///   0. `[writable]` The account to freeze.
    ///   1. `[]` The token mint.
    ///   2. `[signer]` The mint freeze authority.
    ///
    ///   * Multisignature owner
    ///   0. `[writable]` The account to freeze.
    ///   1. `[]` The token mint.
    ///   2. `[]` The mint's multisignature freeze authority.
    ///   3. ..`3+M` `[signer]` M signer accounts.
//    FreezeAccount,
    private static Map<String, Object> parseFreezeAccount(String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        info.put("account", accounts[0]);         // 要冻结的代币账户
        info.put("mint", accounts[1]);           // 代币铸造账户
        info.put("freezeAuthority", accounts[2]);       // 冻结权限账户

        return info;
    }

    // ThawAccount 指令解析
    /// Thaw a Frozen account using the Mint's `freeze_authority` (if set).
    ///
    /// Accounts expected by this instruction:
    ///
    ///   * Single owner
    ///   0. `[writable]` The account to freeze.
    ///   1. `[]` The token mint.
    ///   2. `[signer]` The mint freeze authority.
    ///
    ///   * Multisignature owner
    ///   0. `[writable]` The account to freeze.
    ///   1. `[]` The token mint.
    ///   2. `[]` The mint's multisignature freeze authority.
    ///   3. ..`3+M` `[signer]` M signer accounts.
//    ThawAccount,
    private static Map<String, Object> parseThawAccount(String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        info.put("account", accounts[0]);         // 要解冻的代币账户
        info.put("mint", accounts[1]);           // 代币铸造账户
        info.put("freezeAuthority", accounts[2]);       // 冻结权限账户

        return info;
    }

    /// Transfers tokens from one account to another either directly or via a
    /// delegate.  If this account is associated with the native mint then equal
    /// amounts of SOL and Tokens will be transferred to the destination
    /// account.
    ///
    /// This instruction differs from Transfer in that the token mint and
    /// decimals value is checked by the caller.  This may be useful when
    /// creating transactions offline or within a hardware wallet.
    ///
    /// Accounts expected by this instruction:
    ///
    ///   * Single owner/delegate
    ///   0. `[writable]` The source account.
    ///   1. `[]` The token mint.
    ///   2. `[writable]` The destination account.
    ///   3. `[signer]` The source account's owner/delegate.
    ///
    ///   * Multisignature owner/delegate
    ///   0. `[writable]` The source account.
    ///   1. `[]` The token mint.
    ///   2. `[writable]` The destination account.
    ///   3. `[]` The source account's multisignature owner/delegate.
    ///   4. ..`4+M` `[signer]` M signer accounts.
//    TransferChecked {
//                /// The amount of tokens to transfer.
//                amount: u64,
//                /// Expected number of base 10 digits to the right of the decimal place.
//                decimals: u8,
//    }
    private static Map<String, Object> parseTransferChecked(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        // 读取转账金额 (u64)
        String amount = Long.toUnsignedString(buffer.getLong());
        // 读取精度 (u8)
        int decimals = buffer.get() & 0xFF;

        info.put("source", accounts[0]);          // 源代币账户
        info.put("mint", accounts[1]);           // 代币铸造账户
        info.put("destination", accounts[2]);     // 目标代币账户
        info.put("authority", accounts[3]);       // 源账户所有者
        info.put("amount", amount);
        info.put("decimals", decimals);
        return info;
    }

    // ApproveChecked 指令解析
    /// Approves a delegate.  A delegate is given the authority over tokens on
    /// behalf of the source account's owner.
    ///
    /// This instruction differs from Approve in that the token mint and
    /// decimals value is checked by the caller.  This may be useful when
    /// creating transactions offline or within a hardware wallet.
    ///
    /// Accounts expected by this instruction:
    ///
    ///   * Single owner
    ///   0. `[writable]` The source account.
    ///   1. `[]` The token mint.
    ///   2. `[]` The delegate.
    ///   3. `[signer]` The source account owner.
    ///
    ///   * Multisignature owner
    ///   0. `[writable]` The source account.
    ///   1. `[]` The token mint.
    ///   2. `[]` The delegate.
    ///   3. `[]` The source account's multisignature owner.
    ///   4. ..`4+M` `[signer]` M signer accounts
//    ApproveChecked {
//        /// The amount of tokens the delegate is approved for.
//        amount: u64,
//                /// Expected number of base 10 digits to the right of the decimal place.
//                decimals: u8,
//    },
    private static Map<String, Object> parseApproveChecked(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 读取授权金额 (u64)
        String amount = Long.toUnsignedString(buffer.getLong());
        // 读取精度 (u8)
        int decimals = buffer.get() & 0xFF;

        info.put("source", accounts[0]);          // 源代币账户
        info.put("mint", accounts[1]);           // 代币铸造账户
        info.put("delegate", accounts[2]);        // 被授权账户
        info.put("owner", accounts[3]);       // 源账户所有者
        info.put("amount", amount);
        info.put("decimals", decimals);

        return info;
    }

    // MintToChecked 指令解析
    /// Mints new tokens to an account.  The native mint does not support
    /// minting.
    ///
    /// This instruction differs from `MintTo` in that the decimals value is
    /// checked by the caller. This may be useful when creating transactions
    /// offline or within a hardware wallet.
    ///
    /// Accounts expected by this instruction:
    ///
    ///   * Single authority
    ///   0. `[writable]` The mint.
    ///   1. `[writable]` The account to mint tokens to.
    ///   2. `[signer]` The mint's minting authority.
    ///
    ///   * Multisignature authority
    ///   0. `[writable]` The mint.
    ///   1. `[writable]` The account to mint tokens to.
    ///   2. `[]` The mint's multisignature mint-tokens authority.
    ///   3. ..`3+M` `[signer]` M signer accounts.
//    MintToChecked {
//        /// The amount of new tokens to mint.
//        amount: u64,
//                /// Expected number of base 10 digits to the right of the decimal place.
//                decimals: u8,
//    },
    private static Map<String, Object> parseMintToChecked(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 读取铸造金额 (u64)
        String amount = Long.toUnsignedString(buffer.getLong());
        // 读取精度 (u8)
        int decimals = buffer.get() & 0xFF;

        info.put("mint", accounts[0]);           // 代币铸造账户
        info.put("destination", accounts[1]);     // 目标代币账户
        info.put("mintAuthority", accounts[2]);   // 铸造权限账户
        info.put("amount", amount);
        info.put("decimals", decimals);

        return info;
    }

    // BurnChecked 指令解析
    /// Burns tokens by removing them from an account.  `BurnChecked` does not
    /// support accounts associated with the native mint, use `CloseAccount`
    /// instead.
    ///
    /// This instruction differs from Burn in that the decimals value is checked
    /// by the caller. This may be useful when creating transactions offline or
    /// within a hardware wallet.
    ///
    /// Accounts expected by this instruction:
    ///
    ///   * Single owner/delegate
    ///   0. `[writable]` The account to burn from.
    ///   1. `[writable]` The token mint.
    ///   2. `[signer]` The account's owner/delegate.
    ///
    ///   * Multisignature owner/delegate
    ///   0. `[writable]` The account to burn from.
    ///   1. `[writable]` The token mint.
    ///   2. `[]` The account's multisignature owner/delegate.
    ///   3. ..`3+M` `[signer]` M signer accounts.
//    BurnChecked {
//        /// The amount of tokens to burn.
//        amount: u64,
//                /// Expected number of base 10 digits to the right of the decimal place.
//                decimals: u8,
//    },
    private static Map<String, Object> parseBurnChecked(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        // 读取铸造金额 (u64)
        String amount = Long.toUnsignedString(buffer.getLong());
        // 读取精度 (u8)
        int decimals = buffer.get() & 0xFF;

        info.put("account", accounts[0]);         // 要销毁代币的账户
        info.put("mint", accounts[1]);           // 代币铸造账户
        info.put("authority", accounts[2]);       // 代币账户所有者
        info.put("amount", amount);
        info.put("decimals", decimals);

        return info;
    }

    // InitializeMultisig 指令解析
    /// Initializes a multisignature account with N provided signers.
    ///
    /// Multisignature accounts can used in place of any single owner/delegate
    /// accounts in any token instruction that require an owner/delegate to be
    /// present.  The variant field represents the number of signers (M)
    /// required to validate this multisignature account.
    ///
    /// The `InitializeMultisig` instruction requires no signers and MUST be
    /// included within the same Transaction as the system program's
    /// `CreateAccount` instruction that creates the account being initialized.
    /// Otherwise another party can acquire ownership of the uninitialized
    /// account.
    ///
    /// Accounts expected by this instruction:
    ///
    ///   0. `[writable]` The multisignature account to initialize.
    ///   1. `[]` Rent sysvar
    ///   2. ..`2+N`. `[]` The signer accounts, must equal to N where `1 <= N <=
    ///      11`.
//    InitializeMultisig {
//        /// The number of signers (M) required to validate this multisignature
//        /// account.
//        m: u8,
//    },
    private static Map<String, Object> parseInitializeMultisig(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 读取签名者数量 (u8)
        int m = buffer.get() & 0xFF;

        info.put("account", accounts[0]);         // 多重签名账户
        info.put("rent", accounts[1]);           // 租金账户
        info.put("m", m);

        // 添加所有签名者
        List<String> signers = new ArrayList<>(Arrays.asList(accounts).subList(2, accounts.length));
        info.put("signers", signers);

        return info;
    }

    // InitializeAccount2 - 不需要 rent sysvar 的账户初始化
    /// Like [`InitializeAccount`], but the owner pubkey is passed via
    /// instruction data rather than the accounts list. This variant may be
    /// preferable when using Cross Program Invocation from an instruction
    /// that does not need the owner's `AccountInfo` otherwise.
    ///
    /// Accounts expected by this instruction:
    ///
    ///   0. `[writable]`  The account to initialize.
    ///   1. `[]` The mint this account will be associated with.
    ///   3. `[]` Rent sysvar
//    InitializeAccount2 {
//        /// The new account's owner/multisignature.
//        owner: Pubkey,
//    },
    private static Map<String, Object> parseInitializeAccount2(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        byte[] owner = new byte[32];
        buffer.get(owner);
        info.put("account", accounts[0]);
        info.put("mint", accounts[1]);
        info.put("rentSysvar", accounts[2]);
        info.put("owner", Base58.encode(owner));
        return info;
    }

    // SyncNative - 同步原生代币余额
    /// Given a wrapped / native token account (a token account containing SOL)
    /// updates its amount field based on the account's underlying `lamports`.
    /// This is useful if a non-wrapped SOL account uses
    /// `system_instruction::transfer` to move lamports to a wrapped token
    /// account, and needs to have its token `amount` field updated.
    ///
    /// Accounts expected by this instruction:
    ///
    ///   0. `[writable]`  The native token account to sync with its underlying
    ///      lamports.
//    SyncNative,
    private static Map<String, Object> parseSyncNative(String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        info.put("account", accounts[0]);
        return info;
    }

    // InitializeAccount3 - 带有额外选项的账户初始化
    /// Like [`InitializeAccount2`], but does not require the Rent sysvar to be
    /// provided
    ///
    /// Accounts expected by this instruction:
    ///
    ///   0. `[writable]`  The account to initialize.
    ///   1. `[]` The mint this account will be associated with.
//    InitializeAccount3 {
//        /// The new account's owner/multisignature.
//        owner: Pubkey,
//    },
    private static Map<String, Object> parseInitializeAccount3(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        byte[] ownerBytes = new byte[32];
        buffer.get(ownerBytes);

        info.put("account", accounts[0]);
        info.put("mint", accounts[1]);
        info.put("owner", Base58.encode(ownerBytes));
        return info;
    }

    // InitializeMultisig2 - 不需要 rent sysvar 的多重签名初始化
    /// Like [`InitializeMultisig`], but does not require the Rent sysvar to be
    /// provided
    ///
    /// Accounts expected by this instruction:
    ///
    ///   0. `[writable]` The multisignature account to initialize.
    ///   1. ..`1+N` `[]` The signer accounts, must equal to N where `1 <= N <=
    ///      11`.
//    InitializeMultisig2 {
//        /// The number of signers (M) required to validate this multisignature
//        /// account.
//        m: u8,
//    },
    private static Map<String, Object> parseInitializeMultisig2(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        int m = buffer.get() & 0xFF;  // 需要的签名数量

        info.put("account", accounts[0]);
        info.put("m", m);
        List<String> signers = new ArrayList<>(Arrays.asList(accounts).subList(1, accounts.length));
        info.put("signers", signers);
        return info;
    }

    /// Like [`InitializeMint`], but does not require the Rent sysvar to be
    /// provided
    ///
    /// Accounts expected by this instruction:
    ///
    ///   0. `[writable]` The mint to initialize.
//    InitializeMint2 {
//                /// Number of base 10 digits to the right of the decimal place.
//                decimals: u8,
//                /// The authority/multisignature to mint tokens.
//                mint_authority: Pubkey,
//                /// The freeze authority/multisignature of the mint.
//                freeze_authority: COption<Pubkey>,
//    },
    private static Map<String, Object> parseInitializeMint2(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        int decimals = buffer.get() & 0xFF;

        byte[] ownerBytes = new byte[32];
        buffer.get(ownerBytes);

        info.put("mint", accounts[0]);
        info.put("mintAuthority", Base58.encode(ownerBytes));
        if (accounts.length > 2) {
            info.put("freezeAuthority", accounts[2]);
        }
        info.put("decimals", decimals);

        return info;
    }

    /// Gets the required size of an account for the given mint as a
    /// little-endian `u64`.
    ///
    /// Return data can be fetched using `sol_get_return_data` and deserializing
    /// the return data as a little-endian `u64`.
    ///
    /// Accounts expected by this instruction:
    ///
    ///   0. `[]` The mint to calculate for
//    GetAccountDataSize, // typically, there's also data, but this program ignores it
    // [todo] extensionTypes 具体含义
    private static Map<String, Object> parseGetAccountDataSize(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 官方文档中说忽略这个参数
//        ByteBuffer buffer = ByteBuffer.wrap(data);
//        buffer.order(ByteOrder.LITTLE_ENDIAN);
//        long extensionTypes = buffer.getShort();
//        info.put("extensionTypes", extensionTypes);
        info.put("mint", accounts[0]);

        return info;
    }

    // InitializeImmutableOwner - 初始化不可变所有者
    /// Initialize the Immutable Owner extension for the given token account
    ///
    /// Fails if the account has already been initialized, so must be called
    /// before `InitializeAccount`.
    ///
    /// No-ops in this version of the program, but is included for compatibility
    /// with the Associated Token Account program.
    ///
    /// Accounts expected by this instruction:
    ///
    ///   0. `[writable]`  The account to initialize.
    ///
    /// Data expected by this instruction:
    ///   None
//    InitializeImmutableOwner,
    private static Map<String, Object> parseInitializeImmutableOwner(String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        info.put("account", accounts[0]);
        return info;
    }

    // AmountToUiAmount - 金额转UI金额
    /// Convert an Amount of tokens to a `UiAmount` string, using the given
    /// mint. In this version of the program, the mint can only specify the
    /// number of decimals.
    ///
    /// Fails on an invalid mint.
    ///
    /// Return data can be fetched using `sol_get_return_data` and deserialized
    /// with `String::from_utf8`.
    ///
    /// Accounts expected by this instruction:
    ///
    ///   0. `[]` The mint to calculate for
//    AmountToUiAmount {
//        /// The amount of tokens to reformat.
//        amount: u64,
//    },
    private static Map<String, Object> parseAmountToUiAmount(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        String amount = Long.toUnsignedString(buffer.getLong());
        info.put("amount", amount);
        info.put("mint", accounts[0]);

        return info;
    }

    // UiAmountToAmount - UI金额转金额
    /// Convert a `UiAmount` of tokens to a little-endian `u64` raw Amount,
    /// using the given mint. In this version of the program, the mint can
    /// only specify the number of decimals.
    ///
    /// Return data can be fetched using `sol_get_return_data` and deserializing
    /// the return data as a little-endian `u64`.
    ///
    /// Accounts expected by this instruction:
    ///
    ///   0. `[]` The mint to calculate for
//    UiAmountToAmount {
//        /// The `ui_amount` of tokens to reformat.
//        ui_amount: &'a str,
//    },
    private static Map<String, Object> parseUiAmountToAmount(ByteBuffer buffer) {
        Map<String, Object> info = new HashMap<>();

        double uiAmount = buffer.getDouble();
        info.put("uiAmount", uiAmount);

        return info;
    }


    // TransferFeeExtension - 转账费用扩展
    private static Map<String, Object> parseTransferFeeExtension(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        info.put("transferFeeBasisPoints", Integer.toUnsignedString(buffer.getInt()));
        info.put("maximumFee", Long.toUnsignedString(buffer.getLong()));
        info.put("mint", accounts[0]);
        info.put("authority", accounts[1]);
        return info;
    }

    // ConfidentialTransferExtension - 机密转账扩展
    private static Map<String, Object> parseConfidentialTransferExtension(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        info.put("mint", accounts[0]);
        info.put("authority", accounts[1]);
        // 机密转账的具体参数解析
        return info;
    }

    // DefaultAccountStateExtension - 默认账户状态扩展
    private static Map<String, Object> parseDefaultAccountStateExtension(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        info.put("mint", accounts[0]);
        info.put("authority", accounts[1]);
        info.put("accountState", Byte.toUnsignedInt(buffer.get()));

        return info;
    }

    // Reallocate - 重新分配账户空间
    private static Map<String, Object> parseReallocate(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        info.put("account", accounts[0]);
        info.put("payer", accounts[1]);
        info.put("systemProgram", accounts[2]);
        info.put("extensionTypes", Long.toUnsignedString(buffer.getLong()));
        return info;
    }

    // MemoTransferExtension - 备注转账扩展
    private static Map<String, Object> parseMemoTransferExtension(String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        info.put("account", accounts[0]);
        info.put("authority", accounts[1]);
        return info;
    }

}