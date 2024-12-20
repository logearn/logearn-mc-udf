package cn.xlystar.parse.solSwap.raydium.cpmm;

/**
 * Raydium Constant Product Market Maker (CPMMoo8L3F4NbTegBCKVNunggL7H1ZpdTHKxQB5qKP1C)
 *
 * Verified against:
 * - Contract source: https://github.com/raydium-io/raydium-cp-swap
 * - SDK: https://github.com/raydium-io/raydium-sdk/tree/master/src/liquidity
 * - Docs: https://docs.raydium.io/raydium/swap/overview
 * - On-chain transactions analysis
 */
public enum RaydiumCpmmInstruction {
    CREATE_AMM_CONFIG(0),           // 创建AMM配置，包含交易费率和协议费率设置
    UPDATE_AMM_CONFIG(1),           // 更新AMM配置参数
    UPDATE_POOL_STATUS(2),          // 更新流动性池状态
    COLLECT_PROTOCOL_FEE(3),        // 从流动性池中收取协议费用
    COLLECT_FUND_FEE(4),           // 从流动性池中收取资金费用
    INITIALIZE(5),                  // 使用初始流动性初始化流动性池
    DEPOSIT(6),                     // 向流动性池添加流动性
    WITHDRAW(7),                    // 从流动性池移除流动性
    SWAP_BASE_IN(8),               // 基于输入金额的代币交换
    SWAP_BASE_OUT(9);              // 基于输出金额的代币交换

    private final int value;

    RaydiumCpmmInstruction(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static RaydiumCpmmInstruction fromValue(int value) {
        for (RaydiumCpmmInstruction type : values()) {
            if (type.value == value) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown instruction type: " + value);
    }
} 