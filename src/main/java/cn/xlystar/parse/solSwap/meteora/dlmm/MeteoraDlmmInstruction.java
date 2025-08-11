package cn.xlystar.parse.solSwap.meteora.dlmm;

/**
 * Meteora Concentrated Liquidity Market Maker (LBUZKhRxPF3XUpBCjp4YzTKgLccjZhTSDM9YuVaPwxo)
 *
 * Verified against:
 * - Contract source: https://github.com/meteora-ag/meteora
 * - IDL: src/main/java/cn/xlystar/parse/solSwap/meteora/clmm/idl.json
 */
public enum MeteoraDlmmInstruction {
    // 解析的方法
    INITIALIZE_LB_PAIR("2d9aedd2dd0fa65c"),                    // 初始化交易对
    INITIALIZE_REWARD("5f87c0c4f281e644"),                    // 初始化奖励

    ADD_LIQUIDITY("b59d59438fb63448"),                            // 添加流动性
    ADD_LIQUIDITY_BY_WEIGHT("1c8cee63e7a21595"),          // 按权重添加流动性
    ADD_LIQUIDITY_ONE_SIDE("2ae50ae7bd3ec1ae"),           // 单边添加流动性    
    INITIALIZE_POSITION("dbc0ea47bebf6650"),                 // 初始化仓位
    SWAP("f8c69e91e17587c8"),                                             // 交换
    SWAP2("414b3f4ceb5b5b88"),                                             // 交换

    WITHDRAW_PROTOCOL_FEE("502564d88528759f1"),             // 提取协议费用
    FUND_REWARD("bc32f9a55d97263f"),                               // 注资奖励
    UPDATE_REWARD_FUNDER("50256d885286759f1"),              // 更新奖励注资者
    UPDATE_REWARD_DURATION("50256d88528759f1"),          // 更新奖励持续时间
    CLAIM_REWARD("claimReward"),                             // 领���奖励
    CLAIM_FEE("claimFee"),                                   // 领取费用

    CLOSE_POSITION("7b86510031446262"),                         // 关闭仓位
    REMOVE_LIQUIDITY("5055d14818ceb16c"),                      // 移除流动性
    REMOVE_LIQUIDITY_BY_RANGE("removeLiquidityByRange"),     // 按范围移除流动性

    // due 无数据,暂时不解析
    INITIALIZE_PERMISSION_LB_PAIR("initializePermissionLbPair"), // 初始化权限交易对
    ADD_LIQUIDITY_BY_STRATEGY("addLiquidityByStrategy"),      // 按策略添加流动性
    ADD_LIQUIDITY_BY_STRATEGY_ONE_SIDE("addLiquidityByStrategyOneSide"), // 单边按策略添加流动性
    ADD_LIQUIDITY_ONE_SIDE_PRECISE("addLiquidityOneSidePrecise"), // 精确单边添加流动性
    INITIALIZE_POSITION_PDA("initializePositionPda"),         // 初始化PDA仓位
    INITIALIZE_POSITION_BY_OPERATOR("initializePositionByOperator"), // 由操作者初始化仓位
    UPDATE_POSITION_OPERATOR("updatePositionOperator"),       // 更新仓位操作者
    SWAP_EXACT_OUT("swapExactOut"),                          // 精确输出交换
    SWAP_WITH_PRICE_IMPACT("swapWithPriceImpact"),           // 带价格影响的交换
    REMOVE_ALL_LIQUIDITY("removeAllLiquidity"),              // 移除所有流动性
    TOGGLE_PAIR_STATUS("togglePairStatus"),                  // 切换交易对状态
    MIGRATE_POSITION("migratePosition"),                     // 迁移仓位
    MIGRATE_BIN_ARRAY("migrateBinArray"),                   // 迁移bin数组
    UPDATE_FEES_AND_REWARDS("updateFeesAndRewards"),         // 更新费用和奖励
    WITHDRAW_INELIGIBLE_REWARD("withdrawIneligibleReward"),  // 提取不合格奖励
    SET_ACTIVATION_POINT("setActivationPoint"),              // 设置激活点

    // 如下指令不解析
    GO_TO_A_BIN("goToABin"),                                // 转到指定bin
    UPDATE_FEE_PARAMETERS("updateFeeParameters"),            // 更新费用参数
    INCREASE_ORACLE_LENGTH("increaseOracleLength"),          // 增加预言机长度
    INITIALIZE_PRESET_PARAMETER("initializePresetParameter"), // 初始化预设参数
    CLOSE_PRESET_PARAMETER("closePresetParameter"),          // 关闭预设参数
    SET_PRE_ACTIVATION_DURATION("setPreActivationDuration"), // 设置预激活持续时间
    SET_PRE_ACTIVATION_SWAP_ADDRESS("setPreActivationSwapAddress"), // 设置预激活交换地址
    INITIALIZE_BIN_ARRAY("initializeBinArray"),                // 初始化bin数组
    INITIALIZE_CUSTOMIZABLE_PERMISSIONLESS_LB_PAIR("initializeCustomizablePermissionlessLbPair"), // 初始化自定义无权限交易对
    INITIALIZE_BIN_ARRAY_BITMAP_EXTENSION("initializeBinArrayBitmapExtension"); // 初始化bin数组位图扩展

    private final String value;

    MeteoraDlmmInstruction(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static MeteoraDlmmInstruction fromValue(String value) {
        for (MeteoraDlmmInstruction type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown instruction type: " + value);
    }
} 