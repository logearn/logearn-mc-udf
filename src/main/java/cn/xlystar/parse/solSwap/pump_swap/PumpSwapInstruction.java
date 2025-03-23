package cn.xlystar.parse.solSwap.pump_swap;

/**
 * Pump AMM (pAMMBay6oceH9fJKBRHGP5D4bD4sWpmSwMn52FMfXEA)
 *
 * Verified against:
 * - IDL file
 * - On-chain program
 */
public enum PumpSwapInstruction {
    BUY("66063d1201daebea"),                      // Buy tokens
    SELL("33e685a4017f83ad"),                     // Sell tokens
    CREATE_POOL("e992d18ecf6840bc"),              // Create pool
    DEPOSIT("f223c68952e1f2b6"),                  // Deposit tokens
    WITHDRAW("b71246949c6da122"),                 // Withdraw tokens
    CREATE_CONFIG("c9cff3724b6f2fbd"),            // Create configuration
    UPDATE_ADMIN("a1b028d53cb8b3e4"),            // Update admin
    DISABLE("b9adba5ad80feee9"),                  // Disable pool
    EXTEND_ACCOUNT("ea66c2cb964643e5"),          // Extend account
    UPDATE_FEE_CONFIG("68b86772589b6b14");       // Update fee configuration

    private final String value;

    PumpSwapInstruction(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static PumpSwapInstruction fromValue(String value) {
        for (PumpSwapInstruction type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown instruction type: " + value);
    }
}
