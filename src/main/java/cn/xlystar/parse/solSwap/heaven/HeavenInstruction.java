package cn.xlystar.parse.solSwap.heaven;

/**
 * HeavenInstruction - Based on IDL discriminators
 */
public enum HeavenInstruction {


    // Trading instructions
    BUY("66063d1201daebea"),
    SELL("33e685a4017f83ad")
    ;

    private final String value;

    HeavenInstruction(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static HeavenInstruction fromValue(String value) {
        for (HeavenInstruction type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown instruction type: " + value);
    }
}
