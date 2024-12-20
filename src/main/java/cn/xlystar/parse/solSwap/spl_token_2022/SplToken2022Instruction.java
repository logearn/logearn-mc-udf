package cn.xlystar.parse.solSwap.spl_token_2022;

public enum SplToken2022Instruction {
    InitializeMint(0),
    InitializeAccount(1),
    InitializeMultisig(2),
    Transfer(3),
    Approve(4),
    Revoke(5),
    SetAuthority(6),
    MintTo(7),
    Burn(8),
    CloseAccount(9),
    FreezeAccount(10),
    ThawAccount(11),
    TransferChecked(12),
    ApproveChecked(13),
    MintToChecked(14),
    BurnChecked(15),
    InitializeAccount2(16),
    SyncNative(17),
    InitializeAccount3(18),
    InitializeMultisig2(19),
    InitializeMint2(20),
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
    UpdateInterestAccrual(34),
    WithdrawWithheldTokensFromAccounts(35),
    WithdrawWithheldTokensFromMint(36),
    InitializePermanentDelegate(37),
    TransferFeeConfig(38),
    WithholdFee(39),
    HarvestWithheldTokensToMint(40),
    EnableCpiGuard(41),
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