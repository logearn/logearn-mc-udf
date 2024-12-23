package cn.xlystar.parse.solSwap.raydium.amm_v4;

/**
 * Raydium AMM Program (675kPX9MHTjS2zt1qfr1NYHuzeLXfQM9H24wFSUt1Mp8)
 *
 * Verified against:
 * - Contract source: https://github.com/raydium-io/raydium-amm
 * - SDK: https://github.com/raydium-io/raydium-sdk
 * - Docs: https://docs.raydium.io/
 * - On-chain transactions analysis
 */
public enum RaydiumAmmInstruction {
    // === 初始化操作 ===
    INITIALIZE(0),              // 初始化
    INITIALIZE2(1),             // 初始化2
    MONITOR_STEP(2),          // 监控步骤
    DEPOSIT(3),                // 存入资金
    WITHDRAW(4),               // 提取资金
    MIGRATE_TO_OPEN_BOOK(5),  // 迁移到OpenBook
    SET_PARAMS(6),            // 设置参数
    WITHDRAW_PNL(7),           // 提取收益
    WITHDRAW_SRM(8),           // 提取SRM代币
    SWAP_BASE_IN(9),           // 基于输入金额的交换

    PRE_INITIALIZE(10),          // 预初始化

    // === 交易操作 ===
    SWAP_BASE_OUT(11),          // 基于输出金额的交换
    SIMULATE_INFO(12),         // 模拟信息
    ADMIN_CANCEL_ORDERS(13),   // 管理员取消订单
    CREATE_CONFIG_ACCOUNT(14),   // 创建配置账户

    UPDATE_CONFIG_ACCOUNT(15);  // 更新配置账户


    private final int value;

    RaydiumAmmInstruction(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static RaydiumAmmInstruction fromValue(int value) {
        for (RaydiumAmmInstruction type : values()) {
            if (type.value == value) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown instruction type: " + value);
    }
}