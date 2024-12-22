package cn.xlystar.parse.solSwap.system_program;

/**
 *  Solana System Program (11111111111111111111111111111111)
 *  Verified against:
 *  - Contract source: https://github.com/solana-labs/solana/blob/master/sdk/program/src/system_instruction.rs
 *  - SDK: https://github.com/solana-labs/solana/blob/master/sdk/program/src/system_program.rs
 *  - Docs: https://docs.solana.com/developing/runtime-facilities/programs#system-program
 *  - IDL: https://github.com/solana-labs/solana/blob/master/sdk/program/src/system_instruction.rs#L11-L138
 *
 *  data 布局备注: data 有填充，所有参数从后往前读
  */

public enum SystemInstruction {
    CreateAccount(0),            // 创建账户
    Assign(1),                   // 分配所有者
    Transfer(2),                 // 转账
    CreateAccountWithSeed(3),    // 使用种子创建账户
    AdvanceNonceAccount(4),            // 推进 Nonce
    WithdrawNonceAccount(5),           // 提取 Nonce 账户的租金
    InitializeNonceAccount(6),         // 初始化 Nonce 账户
    AuthorizeNonceAccount(7),          // 授权 Nonce 账户
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