package cn.xlystar.parse.solSwap.jupiter;

/**
 * Jupiter v6 program instructions
 */
public enum JupiterInstruction {
 // 路由指令
    SWAP_EVENT("40c6cde8260871e2"),

    ROUTE("e517cb977ae3ad2a"),
    ROUTE_WITH_TOKEN_LEDGER("96564774a75d0e68"),
    EXACT_OUT_ROUTE("d033ef977b2bed5c"),

     // 共享账户路由指令
    SHARED_ACCOUNTS_ROUTE("c1209b3341d69c81"),
    SHARED_ACCOUNTS_EXACT_OUT_ROUTE("b0d169a89a7d453e"),
    SHARED_ACCOUNTS_ROUTE_WITH_TOKEN_LEDGER("e6798f50779f6aaa");

    private final String value;

    JupiterInstruction(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static JupiterInstruction fromValue(String value) {
        for (JupiterInstruction type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("未知指令类型: " + value);
    }
}
