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
    INITIALIZE_CONFIG("d07f1501c2bec446"),                    // 初始化配置
    INITIALIZE_POOL("5fb40aac54aee828"),                     // 初始化池子
    INITIALIZE_TICK_ARRAY("0bbcc1d68d5b95b8"),                // 初始化价格刻度数组
    INITIALIZE_FEE_TIER("b74a9ca070022a1e"),                  // 初始化费率层级
    INITIALIZE_REWARD("5f87c0c4f281e644"),                    // 初始化奖励
    SET_REWARD_EMISSIONS("0dc556a86db01bf4"),                 // 设置奖励发放
    OPEN_POSITION("87802f4d0f98f031"),                        // 开仓
    OPEN_POSITION_WITH_METADATA("f21d86303a6e0e3c"),          // 开仓(带元数据)
    INCREASE_LIQUIDITY("2e9cf3760dcdfbb2"),                   // 增加流动性
    DECREASE_LIQUIDITY("a026d06f685b2c01"),                  // 减少流动性
    UPDATE_FEES_AND_REWARDS("9ae6fa0decd14bdf"),             // 更新费用和奖励
    COLLECT_FEES("a498cf631eba13b6"),                        // 收取费用
    COLLECT_REWARD("4605845756ebb122"),                      // 收取奖励
    COLLECT_PROTOCOL_FEES("1643176296b246dc"),               // 收取协议费
    SWAP("f8c69e91e17587c8"),                                // 交换
    CLOSE_POSITION("7b86510031446262"),                      // 平仓
    SET_DEFAULT_FEE_RATE("76d7d69db6e5d0e4"),                // 设置默认费率
    SET_DEFAULT_PROTOCOL_FEE_RATE("6bcdf9e297235600"),       // 设置默认协议费率
    SET_FEE_RATE("35f38941088c9e06"),          // 设置费率 [todo] 无具体case
    SET_PROTOCOL_FEE_RATE("5f0704329a4f9c83"), // 设置协议费率 [todo] 无具体case
    SET_FEE_AUTHORITY("1f013257ed656184"),     // 设置费用权限 [todo] 无具体case
    SET_COLLECT_PROTOCOL_FEES_AUTHORITY("21"), // 设置收取协议费权限 [todo] 无具体case
    SET_REWARD_AUTHORITY("2227b7fc531c557f"),                // 设置奖励权限 [todo] 无具体case
    SET_REWARD_AUTHORITY_BY_SUPER_AUTHORITY("f09ac9c6945d3819"), // 通过超级权限设置奖励权限
    SET_REWARD_EMISSIONS_SUPER_AUTHORITY("cf05c8d17a3852b7"), // 设置奖励发放超级权限 [todo] 无具体case
    TWO_HOP_SWAP("c360ed6c44a2dbe6"),                       // 两跳交换
    INITIALIZE_POSITION_BUNDLE("752df1951812c241"),          // 初始化头寸包
    INITIALIZE_POSITION_BUNDLE_WITH_METADATA("5d7c10b3f98373f5"), // 初始化带元数据的头寸包
    DELETE_POSITION_BUNDLE("64196302d9ef7cad"),              // 删除头寸包
    OPEN_BUNDLED_POSITION("a9717eabd5acd431"),               // 开启打包头寸
    CLOSE_BUNDLED_POSITION("2924d8f51b556743"),              // 关闭打包头寸
    OPEN_POSITION_WITH_TOKEN_EXTENSIONS("d42f5f5c726683fa"), // 使用代币扩展开仓
    CLOSE_POSITION_WITH_TOKEN_EXTENSIONS(""), // 使用代币扩展平仓 [todo] 缺少case
    COLLECT_FEES_V2("cf755fbfe5b4e20f"),                    // 收取费用 V2
    COLLECT_PROTOCOL_FEES_V2("6780de8672c816c8"),           // 收取协议费 V2
    COLLECT_REWARD_V2("b16b25b4a01331d1"),                  // 收取奖励 V2
    DECREASE_LIQUIDITY_V2("3a7fbc3e4f52c460"),              // 减少流动性 V2
    INCREASE_LIQUIDITY_V2("851d59df45eeb00a"),              // 增加流动性 V2
    INITIALIZE_POOL_V2("cf2d57f21b3fcc43"),                 // 初始化池子 V2
    INITIALIZE_REWARD_V2("5b014d32ebe58531"),               // 初始化奖励 V2
    SET_REWARD_EMISSIONS_V2("72e44820c130a066"),            // 设置奖励发放 V2
    SWAP_V2("2b04ed0b1ac91e62"),                           // 交换 V2
    TWO_HOP_SWAP_V2("ba8fd11dfe02c275"),                   // 两跳交换 V2
    INITIALIZE_CONFIG_EXTENSION("370935097239d134"),         // 初始化配置扩展
    SET_CONFIG_EXTENSION_AUTHORITY("44"),      // 设置配置扩展权限 [todo] 缺少case
    SET_TOKEN_BADGE_AUTHORITY("45"),          // 设置代币徽章权限 [todo] 缺少case
    INITIALIZE_TOKEN_BADGE("fd4dcd5f1be059df"),             // 初始化代币徽章
    DELETE_TOKEN_BADGE("01b6873b9b1963df");                 // 删除代币徽章

    private final String value;

    WhirlpoolInstruction(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static WhirlpoolInstruction fromValue(String value) {
        for (WhirlpoolInstruction type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown instruction type: " + value);
    }
}