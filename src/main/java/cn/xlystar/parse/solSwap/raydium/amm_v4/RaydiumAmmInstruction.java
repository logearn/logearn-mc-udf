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
    PRE_INITIALIZE(2),          // 预初始化

    // === 交易操作 ===
    SWAP_BASE_IN(3),           // 基于输入金额的交换
    SWAP_BASE_OUT(4),          // 基于输出金额的交换

    // === 流动性操作 ===
    DEPOSIT(5),                // 存入资金
    WITHDRAW(6),               // 提取资金
    WITHDRAW_PNL(7),           // 提取收益
    WITHDRAW_SRM(8),           // 提取SRM代币

    // === 配置管理 ===
    CREATE_CONFIG_ACCOUNT(9),   // 创建配置账户
    UPDATE_CONFIG_ACCOUNT(10),  // 更新配置账户
    SET_PARAMS(11),            // 设置参数

    // === 监控和模拟 ===
    MONITOR_STEP(12),          // 监控步骤
    SIMULATE_INFO(13),         // 模拟信息

    // === 迁移和管理 ===
    MIGRATE_TO_OPEN_BOOK(14),  // 迁移到OpenBook
    ADMIN_CANCEL_ORDERS(15);   // 管理员取消订单

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