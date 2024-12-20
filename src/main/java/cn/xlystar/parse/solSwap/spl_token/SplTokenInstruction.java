package cn.xlystar.parse.solSwap.spl_token;

public enum SplTokenInstruction {
    InitializeMint(0),              // 初始化代币铸造
    InitializeAccount(1),           // 初始化代币账户
    InitializeMultisig(2),          // 初始化多重签名
    Transfer(3),                    // 转账
    Approve(4),                     // 授权
    Revoke(5),                      // 撤销授权
    SetAuthority(6),               // 设置权限
    MintTo(7),                     // 铸造代币
    Burn(8),                       // 销毁代币
    CloseAccount(9),               // 关闭账户
    FreezeAccount(10),             // 冻结账户
    ThawAccount(11),               // 解冻账户
    TransferChecked(12),           // 带精度检查的转账
    ApproveChecked(13),            // 带精度检查的授权
    MintToChecked(14),             // 带精度检查的铸造
    BurnChecked(15),               // 带精度检查的销毁
    InitializeAccount2(16),        // 初始化账户2
    SyncNative(17),                // 同步原生代币
    InitializeAccount3(18),        // 初始化账户3
    InitializeMultisig2(19),       // 初始化多重签名2
    InitializeMint2(20),           // 初始化铸造2
    GetAccountDataSize(21),        // 获取账户数据大小
    InitializeImmutableOwner(22),  // 初始化不可变所有者
    AmountToUiAmount(23),          // 金额转UI金额
    UiAmountToAmount(24),          // UI金额转金额
    InitializeMintCloseAuthority(25), // 初始化铸造关闭权限
    TransferFeeExtension(26),      // 转账费用扩展
    ConfidentialTransferExtension(27), // 机密转账扩展
    DefaultAccountStateExtension(28), // 默认账户状态扩展
    Reallocate(29),                // 重新分配
    MemoTransferExtension(30),     // 备注转账扩展
    CreateNativeMint(31);          // 创建原生代币铸造

    private final int value;

    SplTokenInstruction(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static SplTokenInstruction fromValue(int value) {
        for (SplTokenInstruction type : SplTokenInstruction.values()) {
            if (type.value == value) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown instruction type: " + value);
    }
}