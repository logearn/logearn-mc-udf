package cn.xlystar.parse.solSwap.pump_swap;

import cn.xlystar.parse.solSwap.InstructionParser;
import org.bouncycastle.util.encoders.Hex;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * Parser for Pump AMM instructions
 */
public class PumpSwapInstructionParser extends InstructionParser {

    public static final String PROGRAM_ID = "pAMMBay6oceH9fJKBRHGP5D4bD4sWpmSwMn52FMfXEA";

    @Override
    public Map<String, Object> matchInstruction(String methodId, ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        switch (PumpSwapInstruction.fromValue(methodId)) {
            case BUY:
                info.put("base_amount_out", Long.toUnsignedString(buffer.getLong()));
                info.put("max_quote_amount_in", Long.toUnsignedString(buffer.getLong()));
                info.put("pool", accounts[0]);
                info.put("user", accounts[1]);
                info.put("global_config", accounts[2]);
                info.put("base_mint", accounts[3]);
                info.put("quote_mint", accounts[4]);
                info.put("user_base_token_account", accounts[5]);
                info.put("user_quote_token_account", accounts[6]);
                info.put("pool_base_token_account", accounts[7]);
                info.put("pool_quote_token_account", accounts[8]);
                info.put("protocol_fee_recipient", accounts[9]);
                info.put("protocol_fee_recipient_token_account", accounts[10]);
                info.put("base_token_program", accounts[11]);
                info.put("quote_token_program", accounts[12]);
                info.put("system_program", accounts[13]);
                info.put("associated_token_program", accounts[14]);
                info.put("event_authority", accounts[15]);
                info.put("program", accounts[16]);
                break;

            case SELL:
                info.put("base_amount_in", Long.toUnsignedString(buffer.getLong()));
                info.put("min_quote_amount_out", Long.toUnsignedString(buffer.getLong()));

                info.put("pool", accounts[0]);
                info.put("user", accounts[1]);
                info.put("global_config", accounts[2]);
                info.put("base_mint", accounts[3]);
                info.put("quote_mint", accounts[4]);
                info.put("user_base_token_account", accounts[5]);
                info.put("user_quote_token_account", accounts[6]);
                info.put("pool_base_token_account", accounts[7]);
                info.put("pool_quote_token_account", accounts[8]);
                info.put("protocol_fee_recipient", accounts[9]);
                info.put("protocol_fee_recipient_token_account", accounts[10]);
                info.put("base_token_program", accounts[11]);
                info.put("quote_token_program", accounts[12]);
                info.put("system_program", accounts[13]);
                info.put("associated_token_program", accounts[14]);
                info.put("event_authority", accounts[15]);
                info.put("program", accounts[16]);
                break;

            case CREATE_POOL:
                info.put("index", buffer.getShort() & 0xFFFF);
                info.put("base_amount_in", Long.toUnsignedString(buffer.getLong()));
                info.put("quote_amount_in", Long.toUnsignedString(buffer.getLong()));

                info.put("pool", accounts[0]);
                info.put("global_config", accounts[1]);
                info.put("creator", accounts[2]);
                info.put("base_mint", accounts[3]);
                info.put("quote_mint", accounts[4]);
                info.put("lp_mint", accounts[5]);
                info.put("user_base_token_account", accounts[6]);
                info.put("user_quote_token_account", accounts[7]);
                info.put("user_pool_token_account", accounts[8]);
                info.put("pool_base_token_account", accounts[9]);
                info.put("pool_quote_token_account", accounts[10]);
                info.put("base_token_program", accounts[11]);
                info.put("quote_token_program", accounts[12]);
                info.put("token_2022_program", accounts[13]);
                info.put("system_program", accounts[14]);
                info.put("associated_token_program", accounts[15]);
                info.put("event_authority", accounts[16]);
                info.put("program", accounts[17]);
                break;

            case DEPOSIT:
                info.put("lp_token_amount_out", Long.toUnsignedString(buffer.getLong()));
                info.put("max_base_amount_in", Long.toUnsignedString(buffer.getLong()));
                info.put("max_quote_amount_in", Long.toUnsignedString(buffer.getLong()));

                info.put("pool", accounts[0]);
                info.put("global_config", accounts[1]);
                info.put("user", accounts[2]);
                info.put("base_mint", accounts[3]);
                info.put("quote_mint", accounts[4]);
                info.put("lp_mint", accounts[5]);
                info.put("user_base_token_account", accounts[6]);
                info.put("user_quote_token_account", accounts[7]);
                info.put("user_pool_token_account", accounts[8]);
                info.put("pool_base_token_account", accounts[9]);
                info.put("pool_quote_token_account", accounts[10]);
                info.put("token_program", accounts[11]);
                info.put("token_2022_program", accounts[12]);
                info.put("event_authority", accounts[13]);
                info.put("program", accounts[14]);
                break;

            case WITHDRAW:
                info.put("lp_token_amount_in", Long.toUnsignedString(buffer.getLong()));
                info.put("min_base_amount_out", Long.toUnsignedString(buffer.getLong()));
                info.put("min_quote_amount_out", Long.toUnsignedString(buffer.getLong()));

                info.put("pool", accounts[0]);
                info.put("global_config", accounts[1]);
                info.put("user", accounts[2]);
                info.put("base_mint", accounts[3]);
                info.put("quote_mint", accounts[4]);
                info.put("lp_mint", accounts[5]);
                info.put("user_base_token_account", accounts[6]);
                info.put("user_quote_token_account", accounts[7]);
                info.put("user_pool_token_account", accounts[8]);
                info.put("pool_base_token_account", accounts[9]);
                info.put("pool_quote_token_account", accounts[10]);
                info.put("token_program", accounts[11]);
                info.put("token_2022_program", accounts[12]);
                info.put("event_authority", accounts[13]);
                info.put("program", accounts[14]);
                break;

            case CREATE_CONFIG:
                info.put("lp_fee_basis_points", Long.toUnsignedString(buffer.getLong()));
                info.put("protocol_fee_basis_points", Long.toUnsignedString(buffer.getLong()));

                byte[][] protocolFeeRecipients = new byte[8][32];
                for (int i = 0; i < 8; i++) {
                    buffer.get(protocolFeeRecipients[i]);
                }
                info.put("protocol_fee_recipients", protocolFeeRecipients);

                info.put("admin", accounts[0]);
                info.put("global_config", accounts[1]);
                info.put("system_program", accounts[2]);
                break;

            case UPDATE_ADMIN:
                info.put("admin", accounts[0]);
                info.put("global_config", accounts[1]);
                info.put("new_admin", accounts[2]);
                break;

            case DISABLE:
                info.put("disable_create_pool", buffer.get() != 0);
                info.put("disable_deposit", buffer.get() != 0);
                info.put("disable_withdraw", buffer.get() != 0);
                info.put("disable_buy", buffer.get() != 0);
                info.put("disable_sell", buffer.get() != 0);

                info.put("admin", accounts[0]);
                info.put("global_config", accounts[1]);
                info.put("event_authority", accounts[2]);
                info.put("program", accounts[3]);
                break;

            case EXTEND_ACCOUNT:
                info.put("account", accounts[0]);
                info.put("user", accounts[1]);
                info.put("system_program", accounts[2]);
                break;

            case UPDATE_FEE_CONFIG:
                info.put("lp_fee_basis_points", Long.toUnsignedString(buffer.getLong()));
                info.put("protocol_fee_basis_points", Long.toUnsignedString(buffer.getLong()));

                byte[][] newProtocolFeeRecipients = new byte[8][32];
                for (int i = 0; i < 8; i++) {
                    buffer.get(newProtocolFeeRecipients[i]);
                }
                info.put("protocol_fee_recipients", newProtocolFeeRecipients);

                info.put("admin", accounts[0]);
                info.put("global_config", accounts[1]);
                break;

            default:
                return new HashMap<>();
        }

        return info;
    }

    @Override
    public String getMethodId(ByteBuffer buffer) {
        byte[] discriminatorBytes = new byte[8];
        buffer.get(discriminatorBytes);
        return Hex.toHexString(discriminatorBytes);
    }

}
