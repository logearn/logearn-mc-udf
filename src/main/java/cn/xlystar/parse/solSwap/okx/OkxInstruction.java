// OkxInstruction.java
package cn.xlystar.parse.solSwap.okx;

import java.util.HashMap;
import java.util.Map;

/**
 * OKX v2 program instructions
 */
public enum OkxInstruction {
    // SOL swap 相关指令
    COMMISSION_SOL_FROM_SWAP("8145450a842c2314"),    // [无交易]commission_sol_from_swap
    COMMISSION_SOL_PROXY_SWAP("1e21d05b1f9d2512"),  // commission_sol_proxy_swap
    COMMISSION_SOL_SWAP("ca0d43f1d3e5a2b"),        // commission_sol_swap
    COMMISSION_SOL_SWAP2("7184d3a1c8d5c3b9"),      // [无交易]commission_sol_swap2

    // SPL token swap 相关指令
    COMMISSION_SPL_FROM_SWAP("054d9032dee4e9ab"),   // [无交易] commission_spl_from_swap
    COMMISSION_SPL_PROXY_SWAP("60430c9781a41247"),  // commission_spl_proxy_swap
    COMMISSION_SPL_SWAP("eb47d3c472c78f5c"),       // commission_spl_swap
    COMMISSION_SPL_SWAP2("ad834e2696a57b0f"),      // [无交易] commission_spl_swap2

    // 平台手续费相关 swap 指令
    PLATFORM_FEE_SOL_PROXY_SWAP("9a9ccc0ca0c04f50"),           // [无交易] platform_fee_sol_proxy_swap
    PLATFORM_FEE_SOL_PROXY_SWAP2("45c8fef7283476ca"),                // platform_fee_sol_swap2
    PLATFORM_FEE_SPL_PROXY_SWAP("f24c1e8a31faca2a"),           // [无交易] platform_fee_spl_proxy_swap
    PLATFORM_FEE_SPL_PROXY_SWAP2("45a4d25992d6ad43"),                // platform_fee_spl_swap2

    US_PLATFORM_FEE_SOL_PROXY_SWAP("afc8378d08bfc7ee"),    // [无交易] us_platform_fee_sol_proxy_swap
    US_PLATFORM_FEE_SPL_PROXY_SWAP("728de3dccfc99e14"),    // [无交易] us_platform_fee_spl_proxy_swap

    // 直接 swap 指令
    PROXY_SWAP("132c829448382cee"),               // proxy_swap
    SWAP("f8c69e91e17587c8"),                    // swap
    SWAP2("414b3f4ceb5b5b88");                   // swap2


    private final String value;
    private static final Map<String, OkxInstruction> BY_VALUE = new HashMap<>();

    static {
        for (OkxInstruction instruction : values()) {
            BY_VALUE.put(instruction.value, instruction);
        }
    }

    OkxInstruction(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static OkxInstruction fromValue(String value) {
        for (OkxInstruction type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("未知指令类型: " + value);
    }
}