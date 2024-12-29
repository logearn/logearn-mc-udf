package cn.xlystar.parse.solSwap.meteora.almm;

/**
 * Meteora Automated Liquidity Market Maker (Eo7WjKq67rjJQSZxS6z3YkapzY3eMj6Xy8X5EQVn5UaB)
 *
 * Verified against:
 * - Contract source: [source URL if available]
 * - IDL: src/main/java/cn/xlystar/parse/solSwap/meteora/almm/idl.json
 */
public enum MeteoraAlmmInstruction {
    // 初始化相关指令
    INITIALIZE_PERMISSIONED_POOL("4d55b29d3230d47e"), // 初始化权限池
    INITIALIZE_PERMISSIONLESS_POOL("initializePermissionlessPool"), // DUE 无数据  初始化无权限池
    INITIALIZE_PERMISSIONLESS_POOL_WITH_FEE_TIER("06874493e552a971"), // 初始化带自定义费率的无权限池

    // 流动性相关指令
    ADD_LIQUIDITY("addLiquidity"),    // DUE 无数据     // 添加流动性
    REMOVE_LIQUIDITY("removeLiquidity"),  // DUE 无数据  // 移除流动性
    BOOTSTRAP_LIQUIDITY("04e4d747e1fd77ce"),                // 启动流动性
    ADD_BALANCE_LIQUIDITY("a8e3323ebdab54b0"),             // 添加平衡流动性
    REMOVE_BALANCE_LIQUIDITY("856d2cb338ee7221"),       // 移除平衡流动性
    REMOVE_LIQUIDITY_SINGLE_SIDE("5454b142feb90afb"),// 单边移除流动性
    ADD_IMBALANCE_LIQUIDITY("4f237a54ad0f5dbf"),         // 添加不平衡流动性
    LOCK("1513d02bed3eff57"),                                             // 锁定LP代币
    // 交易相关指令
    SWAP("f8c69e91e17587c8"),                                             // 交换
    // 奖励相关指令
    CLAIM_FEE("a9204f8988e84689"),                                    // 领取费用
    SET_POOL_FEES("662c9e36cd257e4e"),                             // 设置池费用

    // 配置相关指令，TODO 这些指令暂时不解析
    OVERRIDE_CURVE_PARAM("overrideCurveParam"),               // 覆盖曲线参数
    GET_POOL_INFO("getPoolInfo"),                             // 获取池信息    
    // 其他指令, TODO 这些指令暂时不解析
    CREATE_MINT_METADATA("createMintMetadata"),               // 创建铸币元数据
    CREATE_LOCK_ESCROW("createLockEscrow"),                   // 创建锁定托管账户
    CREATE_CONFIG("createConfig"),                            // 创建配置
    CLOSE_CONFIG("closeConfig"),                              // 关闭配置
    WITHDRAW_PROTOCOL_FEES("withdrawProtocolFees"),           // 提取协议费用
    PARTNER_CLAIM_FEE("partnerClaimFee"),                   // 合作伙伴领取费用
    SET_WHITELISTED_VAULT("setWhitelistedVault"),           // 设置白名单金库

    TRANSFER_ADMIN("transferAdmin"),                          // 转移管理员权限
    MIGRATE_FEE_ACCOUNT("migrateFeeAccount"),                 // 迁移费用账户
    ENABLE_OR_DISABLE_POOL("enableOrDisablePool");            // 启用或禁用池 

    private final String value;

    MeteoraAlmmInstruction(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static MeteoraAlmmInstruction fromValue(String value) {
        for (MeteoraAlmmInstruction type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown instruction type: " + value);
    }
} 