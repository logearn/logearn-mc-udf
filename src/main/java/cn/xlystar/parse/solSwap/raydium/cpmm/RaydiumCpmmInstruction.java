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
    CREATE_AMM_CONFIG("7524518647279989897"),           // 创建AMM配置，包含交易费率和协议费率设置
    UPDATE_AMM_CONFIG("14444201354927684657"),          // 更新AMM配置参数
    UPDATE_POOL_STATUS("2"),          // [todo] 没有case 更新流动性池状态
    COLLECT_PROTOCOL_FEE("6448665121156532360"),        // 从流动性池中收取协议费用
    COLLECT_FUND_FEE("9081159964177631911"),           // 从流动性池中收取资金费用
    INITIALIZE("17121445590508351407"),                  // 使用初始流动性初始化流动性池
    DEPOSIT("13182846803881894898"),                     // 向流动性池添加流动性
    WITHDRAW("2495396153584390839"),                    // 从流动性池移除流动性
    SWAP_BASE_IN("16011174931058048655"),               // 基于输入金额的代币交换
    SWAP_BASE_OUT("12516711329758894391");              // 基于输出金额的代币交换

    private final String value;

    RaydiumCpmmInstruction(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static RaydiumCpmmInstruction fromValue(String value) {
        for (RaydiumCpmmInstruction type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown instruction type: " + value);
    }
} 