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
    CREATE_AMM_CONFIG("8934edd4d7756c68"),              // 创建AMM配置
    UPDATE_AMM_CONFIG("313cae889a1c74c8"),              // 更新AMM配置
    CREATE_POOL("e992d18ecf6840bc"),                    // 创建流动性池
    UPDATE_POOL_STATUS("82576c062ee0757b"),             // 更新池状态
    CREATE_OPERATION_ACCOUNT("3f5794216d230868"),        // 创建操作账户
    UPDATE_OPERATION_ACCOUNT("7f467728bce33d07"),        // 更新操作账户
    TRANSFER_REWARD_OWNER("07160c53f22b3079"),          // 转移奖励所有权
    INITIALIZE_REWARD("5f87c0c4f281e644"),              // 初始化奖励
    COLLECT_REMAINING_REWARDS("12eda6c52210d590"),       // 收集剩余奖励
    UPDATE_REWARD_INFOS("a3ace0340b9a6adf"),            // 更新奖励信息
    SET_REWARD_PARAMS("7034a74b20c9d389"),             // 设置奖励参数
    COLLECT_PROTOCOL_FEE("8888fcddc2427e59"),          // 收集协议费用
    COLLECT_FUND_FEE("a78a4e95dfc2067e"),              // 收集资金费用
    OPEN_POSITION("87802f4d0f98f031"),                 // 开仓
    OPEN_POSITION_V2("4db84ad67056f1c7"),              // 开仓V2
    OPEN_POSITION_WITH_TOKEN22_NFT("4dffae527d1dc92e"), // 使用Token22 NFT开仓
    CLOSE_POSITION("7b86510031446262"),                // 关仓
    INCREASE_LIQUIDITY("2e9cf3760dcdfbb2"),            // 增加流动性
    INCREASE_LIQUIDITY_V2("851d59df45eeb00a"),         // 增加流动性V2
    DECREASE_LIQUIDITY("a026d06f685b2c01"),            // 减少流动性
    DECREASE_LIQUIDITY_V2("3a7fbc3e4f52c460"),         // 减少流动性V2
    SWAP("f8c69e91e17587c8"),                          // 交换
    SWAP_V2("2b04ed0b1ac91e62"),                       // 交换V2
    SWAP_ROUTER_BASE_IN("457d73daf5baf2c4")            // 路由交换(基于输入)
    ;

    private final String value;

    RaydiumClmmInstruction(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static RaydiumClmmInstruction fromValue(String value) {
        for (RaydiumClmmInstruction type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown instruction type: " + value);
    }
}