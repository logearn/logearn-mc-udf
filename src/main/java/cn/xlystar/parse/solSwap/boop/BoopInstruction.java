package cn.xlystar.parse.solSwap.boop;

/**
 * BoopInstruction
 */
public enum BoopInstruction {

    BUY_TOKEN("8a7f0e5b26577369"),
    CREATE_RAYDIUM_POOL("412d"),
    CREATE_RAYDIUM_RANDOM_POOL("4e2c"),
    CREATE_TOKEN("54"),
    DEPLOY_BONDING_CURVE("b459"),
    DEPOSIT_INTO_RAYDIUM("a859"),
    INITIALIZE("af"),
    SELL_TOKEN("6d3d"),
    SWAP_SOL_FOR_TOKENS_ON_RAYDIUM("6bf8"),
    SWAP_TOKENS_FOR_SOL_ON_RAYDIUM("d8ac");

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
