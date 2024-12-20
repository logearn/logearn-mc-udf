package cn.xlystar.parse.solSwap.whirlpool;

/**
 * Whirlpool Program (whirLbMiicVdio4qvUfM5KAg6Ct8VwpYzGff3uctyCc)
 *
 * Verified against:
 * - Contract source: https://github.com/orca-so/whirlpools
 * - SDK: https://github.com/orca-so/whirlpools/tree/main/sdk
 * - Docs: https://docs.orca.so/whirlpools/
 */
public enum WhirlpoolInstruction {
    INITIALIZE_CONFIG(0),                    // 初始化配置
    INITIALIZE_POOL(1),                      // 初始化池子
    INITIALIZE_TICK_ARRAY(2),                // 初始化价格刻度数组
    INITIALIZE_FEE_TIER(3),                  // 初始化费率层级
    INITIALIZE_REWARD(4),                    // 初始化奖励
    SET_REWARD_EMISSIONS(5),                 // 设置奖励发放
    OPEN_POSITION(6),                        // 开仓
    OPEN_POSITION_WITH_METADATA(7),          // 开仓(带元数据)
    INCREASE_LIQUIDITY(8),                   // 增加流动性
    DECREASE_LIQUIDITY(9),                  // 减少流动性
    UPDATE_FEES_AND_REWARDS(10),             // 更新费用和奖励
    COLLECT_FEES(11),                        // 收取费用
    COLLECT_REWARD(12),                      // 收取奖励
    COLLECT_PROTOCOL_FEES(13),               // 收取协议费
    SWAP(14),                                // 交换
    CLOSE_POSITION(15),                      // 平仓
    SET_DEFAULT_FEE_RATE(16),                // 设置默认费率
    SET_DEFAULT_PROTOCOL_FEE_RATE(17),       // 设置默认协议费率
    SET_FEE_RATE(18),                       // 设置费率
    SET_PROTOCOL_FEE_RATE(19),              // 设置协议费率
    SET_FEE_AUTHORITY(20),                   // 设置费用权限
    SET_COLLECT_PROTOCOL_FEES_AUTHORITY(21), // 设置收取协议费权限
    SET_REWARD_AUTHORITY(22),                // 设置奖励权限
    SET_REWARD_AUTHORITY_BY_SUPER_AUTHORITY(23), // 通过超级权限设置奖励权限
    SET_REWARD_EMISSIONS_SUPER_AUTHORITY(24), // 设置奖励发放超级权限
    TWO_HOP_SWAP(25),                       // 两跳交换
    INITIALIZE_POSITION_BUNDLE(26),          // 初始化头寸包
    INITIALIZE_POSITION_BUNDLE_WITH_METADATA(27), // 初始化带元数据的头寸包
    DELETE_POSITION_BUNDLE(28),              // 删除头寸包
    OPEN_BUNDLED_POSITION(29),               // 开启打包头寸
    CLOSE_BUNDLED_POSITION(30),              // 关闭打包头寸
    OPEN_POSITION_WITH_TOKEN_EXTENSIONS(31), // 使用代币扩展开仓
    CLOSE_POSITION_WITH_TOKEN_EXTENSIONS(32), // 使用代币扩展平仓
    COLLECT_FEES_V2(33),                    // 收取费用 V2
    COLLECT_PROTOCOL_FEES_V2(34),           // 收取协议费 V2
    COLLECT_REWARD_V2(35),                  // 收取奖励 V2
    DECREASE_LIQUIDITY_V2(36),              // 减少流动性 V2
    INCREASE_LIQUIDITY_V2(37),              // 增加流动性 V2
    INITIALIZE_POOL_V2(38),                 // 初始化池子 V2
    INITIALIZE_REWARD_V2(39),               // 初始化奖励 V2
    SET_REWARD_EMISSIONS_V2(40),            // 设置奖励发放 V2
    SWAP_V2(41),                           // 交换 V2
    TWO_HOP_SWAP_V2(42),                   // 两跳交换 V2
    INITIALIZE_CONFIG_EXTENSION(43),         // 初始化配置扩展
    SET_CONFIG_EXTENSION_AUTHORITY(44),      // 设置配置扩展权限
    SET_TOKEN_BADGE_AUTHORITY(45),          // 设置代币徽章权限
    INITIALIZE_TOKEN_BADGE(46),             // 初始化代币徽章
    DELETE_TOKEN_BADGE(47);                 // 删除代币徽章

    private final int value;

    WhirlpoolInstruction(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static WhirlpoolInstruction fromValue(int value) {
        for (WhirlpoolInstruction type : values()) {
            if (type.value == value) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown instruction type: " + value);
    }
}