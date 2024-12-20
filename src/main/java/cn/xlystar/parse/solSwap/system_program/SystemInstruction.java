package cn.xlystar.parse.solSwap.system_program;

// data 有填充，所有参数从后往前读
public enum SystemInstruction {
    CreateAccount(0),            // 创建账户
    Assign(1),                   // 分配所有者
    Transfer(2),                 // 转账
    CreateAccountWithSeed(3),    // 使用种子创建账户
    AdvanceNonce(4),            // 推进 Nonce
    WithdrawNonce(5),           // 提取 Nonce 账户的租金
    InitializeNonce(6),         // 初始化 Nonce 账户
    AuthorizeNonce(7),          // 授权 Nonce 账户
    Allocate(8),                // 分配空间
    AllocateWithSeed(9),        // 使用种子分配空间
    AssignWithSeed(10),         // 使用种子分配所有者
    TransferWithSeed(11),       // 使用种子转账
    UpgradeNonceAccount(12)     // 升级 Nonce 账户
    ;

    private final int value;

    SystemInstruction(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static SystemInstruction fromValue(int value) {
        for (SystemInstruction type : SystemInstruction.values()) {
            if (type.value == value) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown instruction type: " + value);
    }
}