package cn.xlystar.parse.solSwap.pump_swap;

import cn.xlystar.parse.solSwap.InstructionParser;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * Parser for Pump AMM instructions
 */
public class PumpSwapInstructionParser extends InstructionParser {

    public static final String PROGRAM_ID = "pAMMBay6oceH9fJKBRHGP5D4bD4sWpmSwMn52FMfXEA";

    @Override
    public String getMethodId(ByteBuffer buffer) {
        byte[] discriminator = new byte[8];
        buffer.get(discriminator);
        StringBuilder sb = new StringBuilder();
        for (byte b : discriminator) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    @Override
    public Map<String, Object> parseInstruction(String methodId, ByteBuffer buffer, String[] accounts) {
        PumpSwapInstruction instruction = PumpSwapInstruction.fromValue(methodId);
        
        Map<String, Object> result = new HashMap<>();
        result.put("method", instruction.name());
        
        Map<String, Object> params = new HashMap<>();
        Map<String, Object> accountMap = new HashMap<>();
        
        switch (instruction) {
            case BUY:
                params.put("base_amount_out", Long.toUnsignedString(buffer.getLong()));
                params.put("max_quote_amount_in", Long.toUnsignedString(buffer.getLong()));
                
                accountMap.put("pool", accounts[0]);
                accountMap.put("user", accounts[1]);
                accountMap.put("global_config", accounts[2]);
                accountMap.put("base_mint", accounts[3]);
                accountMap.put("quote_mint", accounts[4]);
                accountMap.put("user_base_token_account", accounts[5]);
                accountMap.put("user_quote_token_account", accounts[6]);
                accountMap.put("pool_base_token_account", accounts[7]);
                accountMap.put("pool_quote_token_account", accounts[8]);
                accountMap.put("protocol_fee_recipient", accounts[9]);
                accountMap.put("protocol_fee_recipient_token_account", accounts[10]);
                accountMap.put("base_token_program", accounts[11]);
                accountMap.put("quote_token_program", accounts[12]);
                accountMap.put("system_program", accounts[13]);
                accountMap.put("associated_token_program", accounts[14]);
                accountMap.put("event_authority", accounts[15]);
                accountMap.put("program", accounts[16]);
                break;

            case SELL:
                params.put("base_amount_in", Long.toUnsignedString(buffer.getLong()));
                params.put("min_quote_amount_out", Long.toUnsignedString(buffer.getLong()));
                
                accountMap.put("pool", accounts[0]);
                accountMap.put("user", accounts[1]);
                accountMap.put("global_config", accounts[2]);
                accountMap.put("base_mint", accounts[3]);
                accountMap.put("quote_mint", accounts[4]);
                accountMap.put("user_base_token_account", accounts[5]);
                accountMap.put("user_quote_token_account", accounts[6]);
                accountMap.put("pool_base_token_account", accounts[7]);
                accountMap.put("pool_quote_token_account", accounts[8]);
                accountMap.put("protocol_fee_recipient", accounts[9]);
                accountMap.put("protocol_fee_recipient_token_account", accounts[10]);
                accountMap.put("base_token_program", accounts[11]);
                accountMap.put("quote_token_program", accounts[12]);
                accountMap.put("system_program", accounts[13]);
                accountMap.put("associated_token_program", accounts[14]);
                accountMap.put("event_authority", accounts[15]);
                accountMap.put("program", accounts[16]);
                break;

            case CREATE_POOL:
                params.put("index", buffer.getShort() & 0xFFFF);
                params.put("base_amount_in", Long.toUnsignedString(buffer.getLong()));
                params.put("quote_amount_in", Long.toUnsignedString(buffer.getLong()));
                
                accountMap.put("pool", accounts[0]);
                accountMap.put("global_config", accounts[1]);
                accountMap.put("creator", accounts[2]);
                accountMap.put("base_mint", accounts[3]);
                accountMap.put("quote_mint", accounts[4]);
                accountMap.put("lp_mint", accounts[5]);
                accountMap.put("user_base_token_account", accounts[6]);
                accountMap.put("user_quote_token_account", accounts[7]);
                accountMap.put("user_pool_token_account", accounts[8]);
                accountMap.put("pool_base_token_account", accounts[9]);
                accountMap.put("pool_quote_token_account", accounts[10]);
                accountMap.put("base_token_program", accounts[11]);
                accountMap.put("quote_token_program", accounts[12]);
                accountMap.put("token_2022_program", accounts[13]);
                accountMap.put("system_program", accounts[14]);
                accountMap.put("associated_token_program", accounts[15]);
                accountMap.put("event_authority", accounts[16]);
                accountMap.put("program", accounts[17]);
                break;

            case DEPOSIT:
                params.put("lp_token_amount_out", Long.toUnsignedString(buffer.getLong()));
                params.put("max_base_amount_in", Long.toUnsignedString(buffer.getLong()));
                params.put("max_quote_amount_in", Long.toUnsignedString(buffer.getLong()));
                
                accountMap.put("pool", accounts[0]);
                accountMap.put("global_config", accounts[1]);
                accountMap.put("user", accounts[2]);
                accountMap.put("base_mint", accounts[3]);
                accountMap.put("quote_mint", accounts[4]);
                accountMap.put("lp_mint", accounts[5]);
                accountMap.put("user_base_token_account", accounts[6]);
                accountMap.put("user_quote_token_account", accounts[7]);
                accountMap.put("user_pool_token_account", accounts[8]);
                accountMap.put("pool_base_token_account", accounts[9]);
                accountMap.put("pool_quote_token_account", accounts[10]);
                accountMap.put("token_program", accounts[11]);
                accountMap.put("token_2022_program", accounts[12]);
                accountMap.put("event_authority", accounts[13]);
                accountMap.put("program", accounts[14]);
                break;

            case WITHDRAW:
                params.put("lp_token_amount_in", Long.toUnsignedString(buffer.getLong()));
                params.put("min_base_amount_out", Long.toUnsignedString(buffer.getLong()));
                params.put("min_quote_amount_out", Long.toUnsignedString(buffer.getLong()));
                
                accountMap.put("pool", accounts[0]);
                accountMap.put("global_config", accounts[1]);
                accountMap.put("user", accounts[2]);
                accountMap.put("base_mint", accounts[3]);
                accountMap.put("quote_mint", accounts[4]);
                accountMap.put("lp_mint", accounts[5]);
                accountMap.put("user_base_token_account", accounts[6]);
                accountMap.put("user_quote_token_account", accounts[7]);
                accountMap.put("user_pool_token_account", accounts[8]);
                accountMap.put("pool_base_token_account", accounts[9]);
                accountMap.put("pool_quote_token_account", accounts[10]);
                accountMap.put("token_program", accounts[11]);
                accountMap.put("token_2022_program", accounts[12]);
                accountMap.put("event_authority", accounts[13]);
                accountMap.put("program", accounts[14]);
                break;

            case CREATE_CONFIG:
                params.put("lp_fee_basis_points", Long.toUnsignedString(buffer.getLong()));
                params.put("protocol_fee_basis_points", Long.toUnsignedString(buffer.getLong()));
                
                byte[][] protocolFeeRecipients = new byte[8][32];
                for (int i = 0; i < 8; i++) {
                    buffer.get(protocolFeeRecipients[i]);
                }
                params.put("protocol_fee_recipients", protocolFeeRecipients);
                
                accountMap.put("admin", accounts[0]);
                accountMap.put("global_config", accounts[1]);
                accountMap.put("system_program", accounts[2]);
                break;

            case UPDATE_ADMIN:
                accountMap.put("admin", accounts[0]);
                accountMap.put("global_config", accounts[1]);
                accountMap.put("new_admin", accounts[2]);
                break;

            case DISABLE:
                params.put("disable_create_pool", buffer.get() != 0);
                params.put("disable_deposit", buffer.get() != 0);
                params.put("disable_withdraw", buffer.get() != 0);
                params.put("disable_buy", buffer.get() != 0);
                params.put("disable_sell", buffer.get() != 0);
                
                accountMap.put("admin", accounts[0]);
                accountMap.put("global_config", accounts[1]);
                accountMap.put("event_authority", accounts[2]);
                accountMap.put("program", accounts[3]);
                break;

            case EXTEND_ACCOUNT:
                accountMap.put("account", accounts[0]);
                accountMap.put("user", accounts[1]);
                accountMap.put("system_program", accounts[2]);
                break;

            case UPDATE_FEE_CONFIG:
                params.put("lp_fee_basis_points", Long.toUnsignedString(buffer.getLong()));
                params.put("protocol_fee_basis_points", Long.toUnsignedString(buffer.getLong()));
                
                byte[][] newProtocolFeeRecipients = new byte[8][32];
                for (int i = 0; i < 8; i++) {
                    buffer.get(newProtocolFeeRecipients[i]);
                }
                params.put("protocol_fee_recipients", newProtocolFeeRecipients);
                
                accountMap.put("admin", accounts[0]);
                accountMap.put("global_config", accounts[1]);
                break;

            default:
                throw new IllegalArgumentException("Unknown instruction: " + methodId);
        }

        result.put("params", params);
        result.put("accounts", accountMap);
        return result;
    }
}
