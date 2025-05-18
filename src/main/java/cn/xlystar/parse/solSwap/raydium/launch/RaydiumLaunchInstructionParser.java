package cn.xlystar.parse.solSwap.raydium.launch;

import cn.xlystar.parse.solSwap.InstructionParser;
import org.bitcoinj.core.Base58;
import org.bouncycastle.util.encoders.Hex;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class RaydiumLaunchInstructionParser extends InstructionParser {

    public static final String PROGRAM_ID = "LanMV9sAd7wArD4vJFi2qDdfnVhFxYSUg6eADduJ3uj";

    @Override
    public String getMethodId(ByteBuffer buffer) {
        byte[] methodIdByte = new byte[8];
        buffer.get(methodIdByte);
        return Hex.toHexString(methodIdByte);
    }

    @Override
    public Map<String, Object> matchInstruction(String methodId, ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info;
        switch (RaydiumLaunchInstruction.fromValue(methodId)) {
            case BUY_EXACT_IN:
                info = parseBuyExactIn(buffer, accounts);
                break;
            case BUY_EXACT_OUT:
                info = parseBuyExactOut(buffer, accounts);
                break;
            case INITIALIZE:
                info = parseInitialize(buffer, accounts);
                break;
            case SELL_EXACT_OUT:
                info = parseSellExactOut(buffer, accounts);
                break;
            case SELL_EXACT_IN:
                info = parseSellExactIn(buffer, accounts);
                break;
            case MIGRATE_TO_AMM:
                info = parseMigrateToAmm(buffer, accounts);
                break;
            case MIGRATE_TO_CPSWAP:
                info = parseMigrateToCpswap(buffer, accounts);
                break;
            default:
                return new HashMap<>();
        }
        return info;
    }

    private static Map<String, Object> parseBuyExactIn(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        info.put("amount_in", Long.toUnsignedString(buffer.getLong()));
        info.put("minimum_amount_out", Long.toUnsignedString(buffer.getLong()));
        info.put("share_fee_rate", Long.toUnsignedString(buffer.getLong()));

        info.put("payer", accounts[0]);
        info.put("authority", accounts[1]);
        info.put("global_config", accounts[2]);
        info.put("platform_config", accounts[3]);
        info.put("pool_state", accounts[4]);
        info.put("user_base_token", accounts[5]);
        info.put("user_quote_token", accounts[6]);
        info.put("base_vault", accounts[7]);
        info.put("quote_vault", accounts[8]);
        info.put("base_mint", accounts[9]);
        info.put("quote_mint", accounts[10]);
        info.put("base_token_program", accounts[11]);
        info.put("quote_token_program", accounts[12]);
        info.put("event_authority", accounts[13]);
        return info;
    }

    private static Map<String, Object> parseBuyExactOut(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        info.put("amount_out", Long.toUnsignedString(buffer.getLong()));
        info.put("maximum_amount_in", Long.toUnsignedString(buffer.getLong()));
        info.put("share_fee_rate", Long.toUnsignedString(buffer.getLong()));

        info.put("payer", accounts[0]);
        info.put("authority", accounts[1]);
        info.put("global_config", accounts[2]);
        info.put("platform_config", accounts[3]);
        info.put("pool_state", accounts[4]);
        info.put("user_base_token", accounts[5]);
        info.put("user_quote_token", accounts[6]);
        info.put("base_vault", accounts[7]);
        info.put("quote_vault", accounts[8]);
        info.put("base_mint", accounts[9]);
        info.put("quote_mint", accounts[10]);
        info.put("base_token_program", accounts[11]);
        info.put("quote_token_program", accounts[12]);
        info.put("event_authority", accounts[13]);
        return info;
    }

    // https://solscan.io/tx/4iCugPNJQEzRhyHKeqRcJ3jw74FPUKC8rA6X8GkZTFbjCt5DdNDMKrfUp7uLGA7okWL3AEocJBtBMzSoJoKpdkvq
    private static Map<String, Object> parseInitialize(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
//        byte[] base_mint_param = new byte[32];
//        buffer.get(base_mint_param);
//        String baseStr = Base58.encode(base_mint_param);
//        info.put("base_mint_param", baseStr);
//
//        byte[] curve_param = new byte[32];
//        buffer.get(base_mint_param);
//        String curveStr = Base58.encode(curve_param);
//        info.put("curve_param", curveStr);
//
//        byte[] vesting_param = new byte[32];
//        buffer.get(base_mint_param);
//        String vestingStr = Base58.encode(vesting_param);
//        info.put("vesting_param", vestingStr);

        info.put("payer", accounts[0]);
        info.put("creator", accounts[1]);
        info.put("global_config", accounts[2]);
        info.put("platform_config", accounts[3]);
        info.put("authority", accounts[4]);
        info.put("pool_state", accounts[5]);
        info.put("base_mint", accounts[6]);
        info.put("quote_mint", accounts[7]);
        info.put("base_vault", accounts[8]);
        info.put("quote_vault", accounts[9]);
        info.put("metadata_account", accounts[10]);
        info.put("base_token_program", accounts[11]);
        info.put("quote_token_program", accounts[12]);
        info.put("metadata_program", accounts[13]);
        info.put("system_program", accounts[14]);
        info.put("rent_program", accounts[15]);
        info.put("event_authority", accounts[16]);
        info.put("program", accounts[17]);
        return info;
    }

    private static Map<String, Object> parseSellExactIn(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        info.put("amount_in", Long.toUnsignedString(buffer.getLong()));
        info.put("minimum_amount_out", Long.toUnsignedString(buffer.getLong()));
        info.put("share_fee_rate", Long.toUnsignedString(buffer.getLong()));

        info.put("payer", accounts[0]);
        info.put("authority", accounts[1]);
        info.put("global_config", accounts[2]);
        info.put("platform_config", accounts[3]);
        info.put("pool_state", accounts[4]);
        info.put("user_base_token", accounts[5]);
        info.put("user_quote_token", accounts[6]);
        info.put("base_vault", accounts[7]);
        info.put("quote_vault", accounts[8]);
        info.put("base_token_mint", accounts[9]);
        info.put("quote_token_mint", accounts[10]);
        info.put("base_token_program", accounts[11]);
        info.put("quote_token_program", accounts[12]);
        info.put("event_authority", accounts[13]);
        info.put("program", accounts[14]);
        return info;
    }

    private static Map<String, Object> parseSellExactOut(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        info.put("amount_out", Long.toUnsignedString(buffer.getLong()));
        info.put("maximum_amount_in", Long.toUnsignedString(buffer.getLong()));
        info.put("share_fee_rate", Long.toUnsignedString(buffer.getLong()));

        info.put("payer", accounts[0]);
        info.put("authority", accounts[1]);
        info.put("global_config", accounts[2]);
        info.put("platform_config", accounts[3]);
        info.put("pool_state", accounts[4]);
        info.put("user_base_token", accounts[5]);
        info.put("user_quote_token", accounts[6]);
        info.put("base_vault", accounts[7]);
        info.put("quote_vault", accounts[8]);
        info.put("base_token_mint", accounts[9]);
        info.put("quote_token_mint", accounts[10]);
        info.put("base_token_program", accounts[11]);
        info.put("quote_token_program", accounts[12]);
        info.put("event_authority", accounts[13]);
        info.put("program", accounts[14]);
        return info;
    }
    private static Map<String, Object> parseMigrateToAmm(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        info.put("base_lot_size", Long.toUnsignedString(buffer.getLong()));
        info.put("quote_lot_size", Long.toUnsignedString(buffer.getLong()));
        info.put("market_vault_signer_nonce", Byte.toUnsignedInt(buffer.get()));

        info.put("payer", accounts[0]);
        info.put("base_mint", accounts[1]);
        info.put("quote_mint", accounts[2]);
        info.put("openbook_program", accounts[3]);
        info.put("market", accounts[4]);
        info.put("request_queue", accounts[5]);
        info.put("event_queue", accounts[6]);
        info.put("bids", accounts[7]);
        info.put("asks", accounts[8]);
        info.put("market_vault_signer", accounts[9]);
        info.put("market_base_vault", accounts[10]);
        info.put("market_quote_vault", accounts[11]);
        info.put("amm_program", accounts[12]);
        info.put("amm_pool", accounts[13]);
        info.put("amm_authority", accounts[14]);
        info.put("amm_open_orders", accounts[15]);
        info.put("amm_lp_mint", accounts[16]);
        info.put("amm_base_vault", accounts[17]);
        info.put("amm_quote_vault", accounts[18]);
        info.put("amm_target_orders", accounts[19]);
        info.put("amm_config", accounts[20]);
        info.put("amm_create_fee_destination", accounts[21]);
        info.put("authority", accounts[22]);
        info.put("pool_state", accounts[23]);
        info.put("global_config", accounts[24]);
        info.put("base_vault", accounts[25]);
        info.put("quote_vault", accounts[26]);
        info.put("pool_lp_token", accounts[27]);
        info.put("spl_token_program", accounts[28]);
        info.put("associated_token_program", accounts[29]);
        info.put("system_program", accounts[30]);
        info.put("rent_program", accounts[31]);
        return info;
    }

    private static Map<String, Object> parseMigrateToCpswap(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        info.put("payer", accounts[0]);
        info.put("base_mint", accounts[1]);
        info.put("quote_mint", accounts[2]);
        info.put("platform_config", accounts[3]);
        info.put("cpswap_program", accounts[4]);
        info.put("cpswap_pool", accounts[5]);
        info.put("cpswap_authority", accounts[6]);
        info.put("cpswap_lp_mint", accounts[7]);
        info.put("cpswap_base_vault", accounts[8]);
        info.put("cpswap_quote_vault", accounts[9]);
        info.put("cpswap_config", accounts[10]);
        info.put("cpswap_create_pool_fee", accounts[11]);
        info.put("cpswap_observation", accounts[12]);
        info.put("lock_program", accounts[13]);
        info.put("lock_authority", accounts[14]);
        info.put("lock_lp_vault", accounts[15]);
        info.put("authority", accounts[16]);
        info.put("pool_state", accounts[17]);
        info.put("global_config", accounts[18]);
        info.put("base_vault", accounts[19]);
        info.put("quote_vault", accounts[20]);
        info.put("pool_lp_token", accounts[21]);
        info.put("base_token_program", accounts[22]);
        info.put("quote_token_program", accounts[23]);
        info.put("associated_token_program", accounts[24]);
        info.put("system_program", accounts[25]);
        info.put("rent_program", accounts[26]);
        info.put("metadata_program", accounts[27]);
        return info;
    }
}