package cn.xlystar.parse.solSwap.meteora.dlmm_v2;

/**
 * cpamdpZCGKUy5JxQXB4dcpGPiikHawvSWAd6mEn1sGG
 *
 */
public enum MeteoraDlmmV2Instruction {
    // 解析的方法
    INITIALIZE_LB_PAIR("2d9aedd2dd0fa65c"),                    // 初始化交易对
    INITIALIZE_REWARD("5f87c0c4f281e644"),                     // 初始化奖励
    SWAP("f8c69e91e17587c8"),                                  // 交换代币
    ADD_LIQUIDITY("b59d5943bf34348"),                          // 添加流动性
    ADD_LIQUIDITY_BY_WEIGHT("e6d1bb9a0c2b1b0d"),              // 按权重添加流动性
    ADD_LIQUIDITY_ONE_SIDE("f5d7b6c3a4b2c1d0"),               // 单边添加流动性
    REMOVE_LIQUIDITY("5055b7d4e2c7b4c0"),                      // 移除流动性
    REMOVE_LIQUIDITY_BY_RANGE("2b7c6d5e4f3a2b1c"),             // 按范围移除流动性
    INITIALIZE_POSITION("30d7c3b2a1f0e9d8"),                   // 初始化仓位
    CLAIM_FEE("b4268e9e5e5e7e94"),                             // 领取手续费
    CLAIM_REWARD("95e6b5a4c3d2e1f0"),                          // 领取奖励
    CLOSE_POSITION("7b86d6e5f4c3b2a1"),                        // 关闭仓位
    FUND_REWARD("e5d4c3b2a1f0e9d8"),                           // 资助奖励
    UPDATE_REWARD_FUNDER("d31c7b1a5e5e7e94"),                  // 更新奖励资助者
    UPDATE_REWARD_DURATION("8aaec4a9d5ebfe6b");                // 更新奖励持续时间


    private final String value;

    MeteoraDlmmV2Instruction(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static MeteoraDlmmV2Instruction fromValue(String value) {
        for (MeteoraDlmmV2Instruction type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown instruction type: " + value);
    }
} 