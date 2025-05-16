package cn.xlystar.parse.solSwap.boop;

/**
 * BoopInstruction
 */
public enum BoopInstruction {

    BUY_TOKEN("8a7f0e5b26577369"),
    SELL_TOKEN("6d3d28bbe6b087ae"),
    CREATE_TOKEN("5434cce4188cea4b"),
    DEPLOY_BONDING_CURVE("b459c74ca8ecd98a"),
    GRADUATE("2debe1b511da4082"),
    CREATE_RAYDIUM_POOL("412d774dccb25402"),
    CREATE_RAYDIUM_RANDOM_POOL("4e2cad1d84b404ac"),// 没有 case
    DEPOSIT_INTO_RAYDIUM("a859631e753158e0"), // 没有 case
    INITIALIZE("afaf6d1f0d989b9bed"), // 没有 case
    SWAP_SOL_FOR_TOKENS_ON_RAYDIUM("6bf883ef98ea3623"), // 没有 case
    SWAP_TOKENS_FOR_SOL_ON_RAYDIUM("d8ac82946262d7a3"); // 没有 case

    private final String value;

    BoopInstruction(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static BoopInstruction fromValue(String value) {
        for (BoopInstruction type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown instruction type: " + value);
    }
}
