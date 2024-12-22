package cn.xlystar.parse.solSwap.spl_associated_token;

/**
 * Solana Associated Token Account Program (ATokenGPvbdGVxr1b2hvZbsiqW5xWH25efTNsLJA8knL)
 *
 * Verified against:
 * - Contract source: https://github.com/solana-labs/solana-program-library/blob/master/associated-token-account/program/src/lib.rs
 * - SDK: https://github.com/solana-labs/solana-program-library/tree/master/associated-token-account/program
 * - Docs: https://spl.solana.com/associated-token-account
 * - IDL: https://github.com/solana-labs/solana-program-library/blob/master/associated-token-account/js/src/index.ts
 *
 * data 布局备注:
 * 1. 第一个字节为指令类型
 * 2. 该程序只有一个指令 (Create)，不需要指令类型字节
 * 3. Create 指令不需要额外的数据参数
 * 4. 所有必要信息都通过账户传递
 *
 * 账户布局:
 * 1. Funding account (payer)
 * 2. Associated token account address to create
 * 3. Wallet address for the new associated token account
 * 4. Token mint address
 * 5. System Program
 * 6. Token Program
 * 7. Rent sysvar (optional in newer versions)
 */
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