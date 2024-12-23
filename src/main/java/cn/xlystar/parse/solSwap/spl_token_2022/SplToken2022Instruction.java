package cn.xlystar.parse.solSwap.spl_token_2022;

/**
 * Solana Token Program 2022 (TokenzQdBNbLqP5VEhdkAS6EPFLC1PHnBqCXEpPxuEb)
 *
 * Verified against:
 * - Contract source: https://github.com/solana-labs/solana-program-library/blob/master/token/program-2022/src/instruction.rs
 * - SDK: https://github.com/solana-labs/solana-program-library/tree/master/token/program-2022
 * - Docs: https://spl.solana.com/token-2022
 * - IDL: https://github.com/solana-labs/solana-program-library/blob/master/token/js/src/extensions/index.ts
 *
 * data 布局备注:
 * 1. 第一个字节为指令类型
 * 2. 后续字节为指令参数
 * 3. 对于 amount/decimals 等数值类型使用 LITTLE_ENDIAN
 * 4. 对于 pubkey 类型使用 32 字节
 * 5. 对于 string 类型先有 4 字节长度
 *
 * Token-2022 是 Token Program 的升级版本，包含了更多功能：
 * - 利息承载代币
 * - 转账费用
 * - 永久和临时代币授权
 * - 可配置的小数位数
 * - 元数据扩展
 * - 不可转让代币
 * - 默认账户状态
 * - 可关闭的铸币
 * - 非托管代币
 */
public enum SplToken2022Instruction {
    InitializeMint(0),//
    InitializeAccount(1),//
    InitializeMultisig(2),//
    Transfer(3),//
    Approve(4),//
    Revoke(5),
    SetAuthority(6),//
    MintTo(7),//
    Burn(8),//
    CloseAccount(9),
    FreezeAccount(10),
    ThawAccount(11),
    TransferChecked(12),//
    ApproveChecked(13),//
    MintToChecked(14),//
    BurnChecked(15),//
    InitializeAccount2(16),//
    SyncNative(17),
    InitializeAccount3(18),//
    InitializeMultisig2(19),//
    InitializeMint2(20),//
    GetAccountDataSize(21),
    InitializeImmutableOwner(22),
    AmountToUiAmount(23),
    UiAmountToAmount(24),
    InitializeMintCloseAuthority(25),
    TransferFeeExtension(26),
    ConfidentialTransferExtension(27),
    DefaultAccountStateExtension(28),
    Reallocate(29),
    MemoTransferExtension(30),
    CreateNativeMint(31),
    // Token 2022 新增指令
    InitializeInterestBearingConfig(32),
    UpdateRateInterestBearingConfig(33),
    EnableCpiGuard(34),
    WithdrawWithheldTokensFromAccounts(35),
    WithdrawWithheldTokensFromMint(36),
    InitializePermanentDelegate(37),
    TransferFeeConfig(38),//
    WithholdFee(39),//
    HarvestWithheldTokensToMint(40),
    UpdateInterestAccrual(41),
    DisableCpiGuard(42),
    InitializeMetadataPointer(43),
    UpdateMetadataPointer(44),
    InitializeGroupPointer(45),
    UpdateGroupPointer(46),
    InitializeGroupMemberPointer(47),
    UpdateGroupMemberPointer(48);

    private final int value;

    SplToken2022Instruction(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static SplToken2022Instruction fromValue(int value) {
        for (SplToken2022Instruction type : SplToken2022Instruction.values()) {
            if (type.value == value) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown instruction type: " + value);
    }
} 