package cn.xlystar.parse.solSwap.pump;

/**
 * Pump.fun AMM (6EF8rrecthR5Dkzon8Nwu78hRvfCKubJ14M5uBEwF6P)
 *
 * Verified against:
 * - On-chain transactions analysis
 * - Website: https://pump.fun
 */
public enum PumpDotFunInstruction {
    CREATE(0),                    // 创建代币
    INITIALIZE(1),                // 初始化
    SET_PARAMS(2),               // 设置参数
    BUY(3),                      // 购买代币
    SELL(4),                     // 出售代币
    WITHDRAW(5);                 // 提取资金

    private final int value;

    PumpDotFunInstruction(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static PumpDotFunInstruction fromValue(int value) {
        for (PumpDotFunInstruction type : values()) {
            if (type.value == value) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown instruction type: " + value);
    }
} 