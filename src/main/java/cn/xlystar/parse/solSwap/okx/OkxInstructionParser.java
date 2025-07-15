package cn.xlystar.parse.solSwap.okx;

import cn.xlystar.parse.solSwap.InstructionParser;
import org.bouncycastle.util.encoders.Hex;

import java.nio.ByteBuffer;
import java.util.*;


public class OkxInstructionParser extends InstructionParser {


    public static final String PROGRAM_ID = "6m2CDdhRgxpH4WjvdzxAYbGxwdGUz5MziiL5jek2kBma";

    @Override
    public String getMethodId(ByteBuffer buffer) {
        byte[] discriminatorBytes = new byte[8];
        buffer.get(discriminatorBytes);
        return Hex.toHexString(discriminatorBytes);
    }

    @Override
    public Map<String, Object> matchInstruction(String methodId, ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info;
        OkxInstruction instruction = OkxInstruction.fromValue(methodId);
        switch (instruction) {
            case COMMISSION_SOL_PROXY_SWAP:
                info = parseCommissionSolProxySwap(buffer, accounts);
                break;
            case COMMISSION_SOL_SWAP2:
                info = parseCommissionSolSwap2(buffer, accounts);
                break;
            case COMMISSION_SOL_FROM_SWAP:
                info = parseCommissionSolFromSwap(buffer, accounts);
                break;
            case COMMISSION_SOL_SWAP:
                info = parseCommissionSolSwap(buffer, accounts);
                break;

            case COMMISSION_SPL_FROM_SWAP:
                info = parseCommissionSplFromSwap(buffer, accounts);
                break;
            case COMMISSION_SPL_PROXY_SWAP:
                info = parseCommissionSplProxySwap(buffer, accounts);
                break;
            case COMMISSION_SPL_SWAP:
                info = parseCommissionSplSwap(buffer, accounts);
                break;
            case COMMISSION_SPL_SWAP2:
                info = parseCommissionSplSwap2(buffer, accounts);
                break;

            case PLATFORM_FEE_SPL_PROXY_SWAP:
                info = parsePlatformFeeSplProxySwap(buffer, accounts);
                break;
            case PLATFORM_FEE_SPL_PROXY_SWAP2:
                info = parsePlatformFeeSplProxySwapV2(buffer, accounts);
                break;
            case PLATFORM_FEE_SOL_PROXY_SWAP:
                info = parsePlatformFeeSolProxySwap(buffer, accounts);
                break;
            case PLATFORM_FEE_SOL_PROXY_SWAP2:
                info = parsePlatformFeeSolProxySwapV2(buffer, accounts);
                break;

            case PROXY_SWAP:
                info = parseProxySwap(buffer, accounts);
                break;
            case SWAP:
                info = parseSwap(buffer, accounts);
                break;
            case SWAP2:
                info = parseSwap2(buffer, accounts);
                break;

            case US_PLATFORM_FEE_SPL_PROXY_SWAP:
                info = parseUsPlatformFeeSplProxySwap(buffer, accounts);
                break;
            case US_PLATFORM_FEE_SOL_PROXY_SWAP:
                info = parseUsPlatformFeeSolProxySwap(buffer, accounts);
                break;

            default:
                return new HashMap<>();
        }
        return info;
    }

    /**
     * 解析 commission_spl_swap2 指令
     * 指令格式:
     * - CommissionSwapArgs data
     * - u64 order_id
     */
    private Map<String, Object> parseCommissionSplSwap2(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 1. 映射账户
        info.put("payer", accounts[0]);
        info.put("source_token_account", accounts[1]);
        info.put("destination_token_account", accounts[2]);
        info.put("source_mint", accounts[3]);
        info.put("destination_mint", accounts[4]);
        info.put("commission_token_account", accounts[5]);
        info.put("token_program", accounts[6]);

        // 2. 解析参数
        // 2.1 解析 CommissionSwapArgs 结构体
        info.put("amount_in", Long.toUnsignedString(buffer.getLong()));
        info.put("min_amount_out", Long.toUnsignedString(buffer.getLong()));
        return info;
    }

    /**
     * 解析 commission_spl_from_swap 指令
     * 指令格式:
     * - SwapArgs args
     * - u16 commission_rate
     * - BridgeToArgs bridge_to_args
     * - u8 offset
     * - u8 len
     */
    private Map<String, Object> parseCommissionSplFromSwap(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 1. 映射账户
        info.put("payer", accounts[0]);
        info.put("source_token_account", accounts[1]);
        info.put("destination_token_account", accounts[2]);
        info.put("source_mint", accounts[3]);
        info.put("destination_mint", accounts[4]);
        info.put("bridge_program", accounts[5]);
        info.put("associated_token_program", accounts[6]);
        info.put("token_program", accounts[7]);
        info.put("token_2022_program", accounts[8]);
        info.put("system_program", accounts[9]);
        info.put("commission_token_account", accounts[10]);

        // 2. 解析参数
        // 2.1 解析 SwapArgs 结构体
        Map<String, Object> swapArgs = parseSwapArgs(buffer);
        info.putAll(swapArgs);
        return info;
    }

    /**
     * 解析 us_platform_fee_spl_proxy_swap 指令
     * 指令格式:
     * - SwapArgs args
     * - u32 commission_info
     * - u32 platform_fee_rate
     * - u8 trim_rate
     * - u64 order_id
     */
    private Map<String, Object> parseUsPlatformFeeSplProxySwap(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 1. 映射账户
        info.put("payer", accounts[0]);
        info.put("source_token_account", accounts[1]);
        info.put("destination_token_account", accounts[2]);
        info.put("source_mint", accounts[3]);
        info.put("destination_mint", accounts[4]);
        info.put("commission_token_account", accounts[5]);
        info.put("us_platform_fee_token_account", accounts[6]);
        info.put("sa_authority", accounts[7]);

        // 可选账户
        info.put("source_token_sa", accounts[8]);
        info.put("destination_token_sa", accounts[9]);

        // 可选程序账户
        info.put("source_token_program", accounts[10]);
        info.put("destination_token_program", accounts[11]);
        info.put("associated_token_program", accounts[12]);
        info.put("system_program", accounts[13]);

        // 2. 解析参数
        // 2.1 解析 SwapArgs 结构体
        Map<String, Object> swapArgs = parseSwapArgs(buffer);
        info.putAll(swapArgs);

        return info;
    }

    /**
     * 解析 us_platform_fee_sol_proxy_swap 指令
     * 指令格式:
     * - SwapArgs args
     * - u32 commission_info
     * - u32 platform_fee_rate
     * - u8 trim_rate
     * - u64 order_id
     */
    private Map<String, Object> parseUsPlatformFeeSolProxySwap(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        // 1. 映射账户
        info.put("payer", accounts[0]);
        info.put("source_token_account", accounts[1]);
        info.put("destination_token_account", accounts[2]);
        info.put("source_mint", accounts[3]);
        info.put("destination_mint", accounts[4]);
        info.put("commission_account", accounts[5]);
        info.put("us_platform_fee_account", accounts[6]);
        info.put("sa_authority", accounts[7]);

        // 可选账户
        info.put("source_token_sa", accounts[8]);
        info.put("destination_token_sa", accounts[9]);
        // 可选程序账户
        info.put("source_token_program", accounts[10]);
        info.put("destination_token_program", accounts[11]);
        info.put("associated_token_program", accounts[12]);
        info.put("system_program", accounts[13]);

        // 2. 解析参数
        // 2.1 解析 SwapArgs 结构体
        Map<String, Object> swapArgs = parseSwapArgs(buffer);
        info.putAll(swapArgs);
        return info;
    }

    /**
     * 解析 commission_sol_swap2 指令
     * 指令格式:
     * - CommissionSwapArgs data
     * - u64 order_id
     */
    private Map<String, Object> parseCommissionSolSwap2(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 1. 映射账户
        info.put("payer", accounts[0]);
        info.put("source_token_account", accounts[1]);
        info.put("destination_token_account", accounts[2]);
        info.put("source_mint", accounts[3]);
        info.put("destination_mint", accounts[4]);
        info.put("commission_account", accounts[5]);
        info.put("system_program", accounts[6]);

        // 2. 解析参数
        // 2.1 解析 CommissionSwapArgs 结构体
        info.put("amount_in", Long.toUnsignedString(buffer.getLong()));
        info.put("min_amount_out", Long.toUnsignedString(buffer.getLong()));
        return info;
    }


    /**
     * 解析 commission_sol_from_swap 指令
     * 指令格式:
     * - SwapArgs args
     * - u16 commission_rate
     * - BridgeToArgs bridge_to_args
     * - u8 offset
     * - u8 len
     */
    private Map<String, Object> parseCommissionSolFromSwap(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 1. 映射账户
        info.put("payer", accounts[0]);
        info.put("source_token_account", accounts[1]);
        info.put("destination_token_account", accounts[2]);
        info.put("source_mint", accounts[3]);
        info.put("destination_mint", accounts[4]);
        info.put("bridge_program", accounts[5]);
        info.put("associated_token_program", accounts[6]);
        info.put("token_program", accounts[7]);
        info.put("token_2022_program", accounts[8]);
        info.put("system_program", accounts[9]);
        info.put("commission_account", accounts[10]);

        // 2. 解析参数
        // 2.1 解析 SwapArgs 结构体
        info.putAll(parseSwapArgs(buffer));

        return info;
    }

    private Map<String, Object> parsePlatformFeeSolProxySwap(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // Map accounts
        info.put("payer", accounts[0]);
        info.put("source_token_account", accounts[1]);
        info.put("destination_token_account", accounts[2]);
        info.put("source_mint", accounts[3]);
        info.put("destination_mint", accounts[4]);
        info.put("commission_account", accounts[5]);

        // Optional accounts
        info.put("sa_authority", accounts[6]);
        info.put("source_token_sa", accounts[7]);
        info.put("destination_token_sa", accounts[8]);

        // Parse SwapArgs
        Map<String, Object> swapArgs = parseSwapArgs(buffer);
        info.putAll(swapArgs);

        return info;
    }

    private Map<String, Object> parsePlatformFeeSolProxySwapV2(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // Map accounts (same as v1)
        info.put("payer", accounts[0]);
        info.put("source_token_account", accounts[1]);
        info.put("destination_token_account", accounts[2]);
        info.put("source_mint", accounts[3]);
        info.put("destination_mint", accounts[4]);
        info.put("commission_account", accounts[5]);

        // Optional accounts
        info.put("sa_authority", accounts[6]);
        info.put("source_token_sa", accounts[7]);
        info.put("destination_token_sa", accounts[8]);

        // Parse SwapArgs
        Map<String, Object> swapArgs = parseSwapArgs(buffer);
        info.putAll(swapArgs);
        return info;
    }

    private Map<String, Object> parseProxySwap(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 1. 映射账户
        info.put("payer", accounts[0]);
        info.put("source_token_account", accounts[1]);
        info.put("destination_token_account", accounts[2]);
        info.put("source_mint", accounts[3]);
        info.put("destination_mint", accounts[4]);
        info.put("sa_authority", accounts[5]);

        // 可选账户
        info.put("source_token_sa", accounts[6]);
        info.put("destination_token_sa", accounts[7]);
        info.put("source_token_program", accounts[8]);
        info.put("destination_token_program", accounts[9]);
        info.put("associated_token_program", accounts[10]);
        info.put("system_program", accounts[11]);

        // 2. 解析参数
        // 2.1 解析 SwapArgs 结构体
        Map<String, Object> swapArgs = parseSwapArgs(buffer);
        info.putAll(swapArgs);

        return info;
    }

    private Map<String, Object> parseSwap(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 1. 映射账户
        info.put("payer", accounts[0]);
        info.put("source_token_account", accounts[1]);
        info.put("destination_token_account", accounts[2]);
        info.put("source_mint", accounts[3]);
        info.put("destination_mint", accounts[4]);

        // 2. 解析 SwapArgs 结构体
        Map<String, Object> swapArgs = parseSwapArgs(buffer);
        info.putAll(swapArgs);
        return info;
    }

    private Map<String, Object> parseSwap2(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = parseSwap(buffer, accounts); // 基本结构与 swap 相同
        return info;
    }

    // 辅助方法：解析 SwapArgs 结构体
    private Map<String, Object> parseSwapArgs(ByteBuffer buffer) {
        Map<String, Object> swapArgs = new HashMap<>();
        swapArgs.put("amount_in", Long.toUnsignedString(buffer.getLong()));
        swapArgs.put("min_amount_out", Long.toUnsignedString(buffer.getLong()));
        return swapArgs;
    }

    private Map<String, Object> parseCommissionSplProxySwap(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 1. 映射账户
        info.put("payer", accounts[0]);
        info.put("source_token_account", accounts[1]);
        info.put("destination_token_account", accounts[2]);
        info.put("source_mint", accounts[3]);
        info.put("destination_mint", accounts[4]);
        info.put("commission_token_account", accounts[5]);
        info.put("sa_authority", accounts[6]);

        // 可选账户
        info.put("source_token_sa", accounts[7]);
        info.put("destination_token_sa", accounts[8]);

        info.put("source_token_program", accounts[9]);
        info.put("destination_token_program", accounts[10]);
        info.put("associated_token_program", accounts[11]);
        info.put("system_program", accounts[12]);

        // 2. 解析参数
        // 2.1 解析 SwapArgs 结构体
        info.put("amount_in", Long.toUnsignedString(buffer.getLong()));
        info.put("min_amount_out", Long.toUnsignedString(buffer.getLong()));
        return info;
    }

    /**
     * 解析 platform_fee_spl_proxy_swap 指令
     * 指令格式:
     * - SwapArgs args
     * - u16 commission_info
     * - u16 platform_fee_rate
     * - u64 order_id
     */
    private Map<String, Object> parsePlatformFeeSplProxySwap(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 1. 映射账户
        info.put("payer", accounts[0]);
        info.put("source_token_account", accounts[1]);
        info.put("destination_token_account", accounts[2]);
        info.put("source_mint", accounts[3]);
        info.put("destination_mint", accounts[4]);
        info.put("commission_token_account", accounts[5]);
        info.put("sa_authority", accounts[6]);

        // 可选账户
        info.put("source_token_sa", accounts[7]);
        info.put("destination_token_sa", accounts[8]);

        // 可选程序账户
        info.put("source_token_program", accounts[9]);
        info.put("destination_token_program", accounts[10]);
        info.put("associated_token_program", accounts[11]);
        info.put("system_program", accounts[12]);

        // 2. 解析参数
        // 2.1 解析 SwapArgs 结构体
        Map<String, Object> swapArgs = parseSwapArgs(buffer);
        info.putAll(swapArgs);

        return info;
    }

    /**
     * 解析 platform_fee_spl_proxy_swap_v2 指令
     * 指令格式:
     * - SwapArgs args
     * - u32 commission_info
     * - u32 platform_fee_rate
     * - u8 trim_rate
     * - u64 order_id
     */
    private Map<String, Object> parsePlatformFeeSplProxySwapV2(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 1. 映射账户 (与 v1 相同)
        info.put("payer", accounts[0]);
        info.put("source_token_account", accounts[1]);
        info.put("destination_token_account", accounts[2]);
        info.put("source_mint", accounts[3]);
        info.put("destination_mint", accounts[4]);
        info.put("commission_token_account", accounts[5]);
        info.put("sa_authority", accounts[6]);

        // 可选账户
        info.put("source_token_sa", accounts[7]);
        info.put("destination_token_sa", accounts[8]);

        // 可选程序账户
        info.put("source_token_program", accounts[9]);
        info.put("destination_token_program", accounts[10]);
        info.put("associated_token_program", accounts[11]);
        info.put("system_program", accounts[12]);

        // 2. 解析参数
        // 2.1 解析 SwapArgs 结构体
        Map<String, Object> swapArgs = parseSwapArgs(buffer);
        info.putAll(swapArgs);

        return info;
    }

    private Map<String, Object> parseCommissionSplSwap(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 1. 映射账户
        info.put("payer", accounts[0]);
        info.put("source_token_account", accounts[1]);
        info.put("destination_token_account", accounts[2]);
        info.put("source_mint", accounts[3]);
        info.put("destination_mint", accounts[4]);
        info.put("commission_token_account", accounts[5]);
        info.put("token_program", accounts[6]);

        // 2. 解析 CommissionSwapArgs 结构体
        info.put("amount_in", Long.toUnsignedString(buffer.getLong()));
        info.put("min_amount_out", Long.toUnsignedString(buffer.getLong()));
        return info;
    }

    private Map<String, Object> parseCommissionSolSwap(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        info.put("payer", accounts[0]);
        info.put("source_token_account", accounts[1]);
        info.put("destination_token_account", accounts[2]);
        info.put("source_mint", accounts[3]);
        info.put("destination_mint", accounts[4]);
        info.put("commission_account", accounts[5]);
        info.put("system_program", accounts[6]);

        info.put("amount_in", Long.toUnsignedString(buffer.getLong()));
        info.put("min_amount_out", Long.toUnsignedString(buffer.getLong()));
        return info;
    }

    private Map<String, Object> parseCommissionSolProxySwap(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        // 1. 映射账户
        info.put("payer", accounts[0]);
        info.put("source_token_account", accounts[1]);
        info.put("destination_token_account", accounts[2]);
        info.put("source_mint", accounts[3]);
        info.put("destination_mint", accounts[4]);
        info.put("commission_account", accounts[5]);
        info.put("sa_authority", accounts[6]); // PDA 账户

        // 可选账户
        info.put("source_token_sa", accounts[7]);
        info.put("destination_token_sa", accounts[8]);

        info.put("source_token_program", accounts[9]);
        info.put("destination_token_program", accounts[10]);
        info.put("associated_token_program", accounts[11]);
        info.put("system_program", accounts[12]);

        // 2. 解析参数
        // 2.1 解析 SwapArgs 结构体
        // 假设 SwapArgs 包含 amount_in 和 min_amount_out 字段
        info.put("amount_in", Long.toUnsignedString(buffer.getLong()));
        info.put("min_amount_out", Long.toUnsignedString(buffer.getLong()));

        return info;
    }


}