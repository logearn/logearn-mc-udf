package cn.xlystar.parse.solSwap.pump;

/**
 * Pump.fun AMM (6EF8rrecthR5Dkzon8Nwu78hRvfCKubJ14M5uBEwF6P)
 *
 * Verified against:
 * - On-chain transactions analysis
 * - Website: https://pump.fun
 */
public enum PumpDotFunInstruction {
    CREATE("181ec828051c0777"),                    // 创建代币
    INITIALIZE("afaf6d1f0d989bed"),                // 初始化
    SET_PARAMS("1beab2349302bb8d"),               // 设置参数
    BUY("66063d1201daebea"),                      // 购买代币
    SELL("33e685a4017f83ad"),                     // 出售代币
    WITHDRAW("b712469c946da122"),                 // 提取资金
    MIGRATION("9beae792ec9ea21e"),                 // 提取资金
    ANCHOR_SELF_CPI_LOG("e445a52e51cb9a1d");                 // CPI 日志

    private final String value;

    PumpDotFunInstruction(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static PumpDotFunInstruction fromValue(String value) {
        for (PumpDotFunInstruction type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown instruction type: " + value);
    }
} 