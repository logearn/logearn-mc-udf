package cn.xlystar.parse.solSwap.raydium.launch;

public enum RaydiumLaunchInstruction {
    BUY_EXACT_IN("faea0d7bd59c13ec"),
    BUY_EXACT_OUT("18d3742869039938"),
    INITIALIZE("afaf6d1f0d989bed"),
    SELL_EXACT_OUT("5fc8472208090ba6"),
    SELL_EXACT_IN("9527de9bd37c981a"),
    MIGRATE_TO_AMM("cf52c091fecf91df"),
    MIGRATE_TO_CPSWAP("885cc8671cda908c");

    private final String value;

    RaydiumLaunchInstruction(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static RaydiumLaunchInstruction fromValue(String value) {
        for (RaydiumLaunchInstruction type : RaydiumLaunchInstruction.values()) {
            if (type.getValue().equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown instruction: " + value);
    }
}