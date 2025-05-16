package cn.xlystar.parse.solSwap.meteora.dbc;

public enum MeteoraDbcInstruction {
    CREATE_VIRTUAL_POOL_METADATA("2d61bb67fe6d7c86"),
    INITIALIZE_VIRTUAL_POOL_WITH_SPL_TOKEN("8c55d7b066366847"),
    INITIALIZE_VIRTUAL_POOL_WITH_TOKEN2022("a976334e916edc9b"),
    MIGRATION_DAMM_V2("9ca9e66735e45040"),
    MIGRATE_METEORA_DAMM("1b013016b43f76d9"),
    MIGRATION_DAMM_V2_CREATE_METADATA("6dbd1324c3b7de52"),
    MIGRATION_METEORA_DAMM_CREATE_METADATA("2f5e7e73dde2c285"),
    SWAP("f8c69e91e17587c8");

    private final String value;

    MeteoraDbcInstruction(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static MeteoraDbcInstruction fromValue(String value) {
        for (MeteoraDbcInstruction type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown instruction type: " + value);
    }
}
