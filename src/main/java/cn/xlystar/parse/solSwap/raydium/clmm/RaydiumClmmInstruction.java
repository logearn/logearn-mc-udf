package cn.xlystar.parse.solSwap.raydium.clmm;

/**
 * Raydium Concentrated Liquidity Market Maker (CAMMCzo5YL8w4VFF8KVHrK22GGUsp5VTaW7grrKgrWqK)
 *
 * Verified against:
 * - Contract source: https://github.com/raydium-io/raydium-clmm
 * - SDK: https://github.com/raydium-io/raydium-sdk/tree/master/src/clmm
 * - Docs: https://docs.raydium.io/concentrated-liquidity/overview
 * - On-chain transactions analysis
 */
public enum RaydiumClmmInstruction {
    CREATE_AMM_CONFIG(0),              // 创建AMM配置
    UPDATE_AMM_CONFIG(1),              // 更新AMM配置
    CREATE_POOL(2),                    // 创建流动性池
    UPDATE_POOL_STATUS(3),             // 更新池状态
    CREATE_OPERATION_ACCOUNT(4),        // 创建操作账户
    UPDATE_OPERATION_ACCOUNT(5),        // 更新操作账户
    TRANSFER_REWARD_OWNER(6),          // 转移奖励所有权
    INITIALIZE_REWARD(7),              // 初始化奖励
    COLLECT_REMAINING_REWARDS(8),       // 收集剩余奖励
    UPDATE_REWARD_INFOS(9),            // 更新奖励信息
    SET_REWARD_PARAMS(10),             // 设置奖励参数
    COLLECT_PROTOCOL_FEE(11),          // 收集协议费用
    COLLECT_FUND_FEE(12),              // 收集资金费用
    OPEN_POSITION(13),                 // 开仓
    OPEN_POSITION_V2(14),              // 开仓V2
    OPEN_POSITION_WITH_TOKEN22_NFT(15),// 使用Token22 NFT开仓
    CLOSE_POSITION(16),                // 关仓
    INCREASE_LIQUIDITY(17),            // 增加流动性
    INCREASE_LIQUIDITY_V2(18),         // 增加流动性V2
    DECREASE_LIQUIDITY(19),            // 减少流动性
    DECREASE_LIQUIDITY_V2(20),         // 减少流动性V2
    SWAP(21),                          // 交换
    SWAP_V2(22),                       // 交换V2
    SWAP_ROUTER_BASE_IN(23)            // 路由交换(基于输入)
    ;

    private final int value;

    RaydiumClmmInstruction(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static RaydiumClmmInstruction fromValue(int value) {
        for (RaydiumClmmInstruction type : values()) {
            if (type.value == value) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown instruction type: " + value);
    }
}