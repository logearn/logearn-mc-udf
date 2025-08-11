package cn.xlystar.parse.solSwap.jupiter;

import cn.xlystar.parse.solSwap.InstructionParser;
import org.bitcoinj.core.Base58;
import org.bouncycastle.util.encoders.Hex;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class JupiterInstructionParser extends InstructionParser {

    public static final String PROGRAM_ID = "JUP6LkbZbjS1jKKwapdHNy74zcZ3tLUZoi5QNyVTaV4";

    @Override
    public String getMethodId(ByteBuffer buffer) {
        byte[] discriminatorBytes = new byte[8];
        buffer.get(discriminatorBytes);
        return Hex.toHexString(discriminatorBytes);
    }

    @Override
    public Map<String, Object> matchInstruction(String methodId, ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info;
        JupiterInstruction instruction = JupiterInstruction.fromValue(methodId);
        switch (instruction) {
            case ROUTE:
                info = parseRoute(buffer, accounts);
                break;
            case ROUTE_WITH_TOKEN_LEDGER:
                info = parseRouteWithTokenLedger(buffer, accounts);
                break;
            case EXACT_OUT_ROUTE:
                info = parseExactOutRoute(buffer, accounts);
                break;

            case SHARED_ACCOUNTS_ROUTE:
                info = parseSharedAccountsRoute(buffer, accounts);
                break;
            case SHARED_ACCOUNTS_EXACT_OUT_ROUTE:
                info = parseSharedAccountsExactOutRoute(buffer, accounts);
                break;
            case SHARED_ACCOUNTS_ROUTE_WITH_TOKEN_LEDGER:
                info = parseSharedAccountsRouteWithTokenLedger(buffer, accounts);
                break;
            default:
                return new HashMap<>();
        }
        return info;
    }

    private Map<String, Object> parseRoute(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        info.put("token_program", accounts[0]);
        info.put("user_transfer_authority", accounts[1]);
        info.put("user_source_token_account", accounts[2]);
        info.put("user_destination_token_account", accounts[3]);
        info.put("destination_token_account", accounts[4]); // 可选账户
        info.put("destination_mint", accounts[5]);
        info.put("platform_fee_account", accounts[6]); // 可选账户
        info.put("event_authority", accounts[7]);
        info.put("program", accounts[8]);

        return info;
    }


    private Map<String, Object> parseExactOutRoute(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        // 映射账户
        info.put("token_program", accounts[0]);
        info.put("user_transfer_authority", accounts[1]);
        info.put("user_source_token_account", accounts[2]);
        info.put("user_destination_token_account", accounts[3]);
        info.put("destination_token_account", accounts[4]); // 可选账户
        info.put("source_mint", accounts[5]);
        info.put("destination_mint", accounts[6]);
        info.put("platform_fee_account", accounts[7]);

        return info;
    }

    private Map<String, Object> parseRouteWithTokenLedger(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        info.put("token_program", accounts[0]);
        info.put("user_transfer_authority", accounts[1]);
        info.put("user_source_token_account", accounts[2]);
        info.put("user_destination_token_account", accounts[3]);
        info.put("destination_token_account", accounts[4]); // 可选账户
        info.put("destination_mint", accounts[5]);
        info.put("platform_fee_account", accounts[6]); // 可选账户
        info.put("token_ledger", accounts[7]);
        info.put("event_authority", accounts[8]);
        info.put("program", accounts[9]);

        return info;
    }

    private Map<String, Object> parseSharedAccountsRoute(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        info.put("token_program", accounts[0]);
        info.put("program_authority", accounts[1]);
        info.put("user_transfer_authority", accounts[2]);
        info.put("source_token_account", accounts[3]);
        info.put("program_source_token_account", accounts[4]);
        info.put("program_destination_token_account", accounts[5]);
        info.put("destination_token_account", accounts[6]);
        info.put("source_mint", accounts[7]);
        info.put("destination_mint", accounts[8]);
        info.put("platform_fee_account", accounts[9]); // 可选账户
        info.put("token_2022_program", accounts[10]); // 可选账户
        info.put("event_authority", accounts[11]);
        info.put("program", accounts[12]);

        return info;
    }

    private Map<String, Object> parseSharedAccountsExactOutRoute(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        // 映射账户
        info.put("token_program", accounts[0]);
        info.put("program_authority", accounts[1]);
        info.put("user_transfer_authority", accounts[2]);
        info.put("source_token_account", accounts[3]);
        info.put("program_source_token_account", accounts[4]);
        info.put("program_destination_token_account", accounts[5]);
        info.put("destination_token_account", accounts[6]);
        info.put("source_mint", accounts[7]);
        info.put("destination_mint", accounts[8]);
        info.put("platform_fee_account", accounts[9]); // 可选账户
        info.put("token_2022_program", accounts[10]); // 可选账户
        info.put("event_authority", accounts[11]);
        info.put("program", accounts[12]);
        return info;
    }

    private Map<String, Object> parseSharedAccountsRouteWithTokenLedger(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 映射账户
        info.put("token_program", accounts[0]);
        info.put("program_authority", accounts[1]);
        info.put("user_transfer_authority", accounts[2]);
        info.put("source_token_account", accounts[3]);
        info.put("program_source_token_account", accounts[4]);
        info.put("program_destination_token_account", accounts[5]);
        info.put("destination_token_account", accounts[6]);
        info.put("source_mint", accounts[7]);
        info.put("destination_mint", accounts[8]);
        info.put("platform_fee_account", accounts[9]); // 可选账户
        info.put("token_2022_program", accounts[10]); // 可选账户
        info.put("token_ledger", accounts[11]);
        info.put("event_authority", accounts[12]);
        info.put("program", accounts[13]);

        return info;
    }


    @Override
    public Map<String, Object> matchLogEvent(ByteBuffer buffer, String eventType) {
        Map<String, Object> info = new HashMap<>();
        switch (eventType) {
            case "swapEvent":
                return parseSwapEvent(buffer);
            default:
                info.put("eventType", eventType);
                return info;
        }
    }

    private static Map<String, Object> parseSwapEvent(ByteBuffer buffer) {
        Map<String, Object> event = new HashMap<>();

        // 读取 amm 地址 (32 bytes)
        byte[] ammBytes = new byte[32];
        buffer.get(ammBytes);
        event.put("amm", Base58.encode(ammBytes));

        // 读取 input_mint 地址 (32 bytes)
        byte[] inputMintBytes = new byte[32];
        buffer.get(inputMintBytes);
        event.put("input_mint", Base58.encode(inputMintBytes));

        // 读取 input_amount (u64)
        event.put("input_amount", Long.toUnsignedString(buffer.getLong()));

        // 读取 output_mint 地址 (32 bytes)
        byte[] outputMintBytes = new byte[32];
        buffer.get(outputMintBytes);
        event.put("output_mint", Base58.encode(outputMintBytes));

        // 读取 output_amount (u64)
        event.put("output_amount", Long.toUnsignedString(buffer.getLong()));

        event.put("eventType", "swap");
        return event;
    }

    @Override
    public String getLogEventType(ByteBuffer buffer) {
        String discriminator = Long.toUnsignedString(buffer.getLong());
        if (discriminator.equals(JupiterInstruction.SWAP_EVENT.getValue())) {
            return "swapEvent";
        }
        return discriminator;
    }
}
