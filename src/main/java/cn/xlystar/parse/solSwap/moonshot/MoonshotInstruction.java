package cn.xlystar.parse.solSwap.moonshot;

/**
 * Moonshot AMM (MoonCVVNZFSYkqNXP6bxHLPL6QQJiMagDL3qcqUQTrG)
 */
public enum MoonshotInstruction {
    TOKEN_MINT("032ca4b87b0df5b3"),           // 代币铸造
    BUY("66063d1201daebea"),                        // 购买代币
    SELL("33e685a4017f83ad"),                      // 出售代币
    MIGRATE_FUNDS("2ae50ae7bd3ec1ae"),     // 迁移资金
    CONFIG_INIT("0deca4ad6afda4b9"),         // 初始化配置
    CONFIG_UPDATE("50256d88528759f1");      // 更新配置

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