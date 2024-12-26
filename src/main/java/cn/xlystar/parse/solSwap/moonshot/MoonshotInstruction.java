package cn.xlystar.parse.solSwap.moonshot;

/**
 * Moonshot AMM (MoonCVVNZFSYkqNXP6bxHLPL6QQJiMagDL3qcqUQTrG)
 */
public enum MoonshotInstruction {
    TOKEN_MINT("tokenMint"),           // 代币铸造
    BUY("buy"),                        // 购买代币
    SELL("sell"),                      // 出售代币
    MIGRATE_FUNDS("migrateFunds"),     // 迁移资金
    CONFIG_INIT("configInit"),         // 初始化配置
    CONFIG_UPDATE("configUpdate");      // 更新配置

    private final String value;

    MoonshotInstruction(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static MoonshotInstruction fromValue(String value) {
        for (MoonshotInstruction type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("未知指令类型: " + value);
    }
} 