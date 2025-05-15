package cn.xlystar.parse.solSwap.raydium.launch;

public enum RaydiumLaunchInstruction {
    BUY_EXACT_IN("faeadb7bd59c13ec"),
    BUY_EXACT_OUT("4516a4c8cd98e798"),
    INITIALIZE("89b11f3e473e3f1f"),
    SELL_EXACT_OUT("a7a1e0f41f4d2d47"),
    SELL_EXACT_IN("e3512337475c9c94"),
    MIGRATE_TO_AMM("d4c0f908f12d0d6f"),
    MIGRATE_TO_CPSWAP("1d3aa6aaf1231326");

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