package cn.xlystar.parse.solSwap.spl_associated_token;

// 合约地址： ATokenGPvbdGVxr1b2hvZbsiqW5xWH25efTNsLJA8knL
public enum SplAssociatedTokenInstruction {
    Create(0),              // 创建关联代币账户
    CreateIdempotent(1),    // 幂等创建关联代币账户
    RecoverNested(2);       // 恢复嵌套关联代币账户

    private final int value;

    SplAssociatedTokenInstruction(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static SplAssociatedTokenInstruction fromValue(int value) {
        for (SplAssociatedTokenInstruction type : SplAssociatedTokenInstruction.values()) {
            if (type.value == value) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown instruction type: " + value);
    }
} 