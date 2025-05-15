package cn.xlystar.parse.solSwap.meteora.dbc;

import cn.xlystar.parse.solSwap.InstructionParser;
import org.bouncycastle.util.encoders.Hex;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * Meteora DBC 指令解析器
 */
public class MeteoraDbcInstructionParser extends InstructionParser {

    public static final String PROGRAM_ID = "dbcij3LWUppWqq96dh6gJWwBifmcGfLSB5D4DuSMaqN";

    @Override
    public String getMethodId(ByteBuffer buffer) {
        byte[] methodIdByte = new byte[8];
        buffer.get(methodIdByte);
        return Hex.toHexString(methodIdByte);
    }

    @Override
    public Map<String, Object> matchInstruction(String methodId, ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info;
        switch (MeteoraDbcInstruction.fromValue(methodId)) {
            case SWAP:
                info = parseSwap(buffer, accounts);
                break;
            case CREATE_VIRTUAL_POOL_METADATA:
                info = parseCreateVirtualPoolMetadata(buffer, accounts);
                break;
            case INITIALIZE_VIRTUAL_POOL_WITH_SPL_TOKEN:
                info = parseInitializeVirtualPoolWithSplToken(buffer, accounts);
                break;
            case INITIALIZE_VIRTUAL_POOL_WITH_TOKEN2022:
                info = parseInitializeVirtualPoolWithToken2022(buffer, accounts);
                break;
            case MIGRATE_METEORA_DAMM:
                info = parseMigrateMeteoraDamm(buffer, accounts);
                break;
            case MIGRATION_DAMM_V2:
                info = parseMigrationDammV2(buffer, accounts);
                break;
            case MIGRATION_METEORA_DAMM_CREATE_METADATA:
                info = parseMigrationMeteoraDammCreateMetadata(buffer, accounts);
                break;
            case MIGRATION_DAMM_V2_CREATE_METADATA:
                info = parseMigrationDammV2CreateMetadata(buffer, accounts);
                break;
            default:
                return new HashMap<>();
        }
        return info;
    }

    /**
     * 解析 Swap 指令
     *
     * @param buffer  数据缓冲区
     * @param accounts 账户列表
     * @return 指令信息
     */
    private static Map<String, Object> parseSwap(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        
        // 解析 SwapParameters 结构
        info.put("amount_in", new BigInteger(buffer.getLong()+""));
        info.put("minimum_amount_out", new BigInteger(buffer.getLong()+""));
        
        // 解析账户
        info.put("pool_authority", accounts[0]);
        info.put("config", accounts[1]);
        info.put("pool", accounts[2]);
        info.put("input_token_account", accounts[3]);
        info.put("output_token_account", accounts[4]);
        info.put("base_vault", accounts[5]);
        info.put("quote_vault", accounts[6]);
        info.put("base_mint", accounts[7]);
        info.put("quote_mint", accounts[8]);
        info.put("payer", accounts[9]);
        info.put("token_base_program", accounts[10]);
        info.put("token_quote_program", accounts[11]);
        
        // 可选账户
        if (accounts.length > 12) {
            info.put("referral_token_account", accounts[12]);
        }
        
        if (accounts.length > 13) {
            info.put("event_authority", accounts[13]);
        }
        
        if (accounts.length > 14) {
            info.put("program", accounts[14]);
        }
        
        return info;
    }


    /**
     * 解析 CreateVirtualPoolMetadata 指令
     *
     * @param buffer  数据缓冲区
     * @param accounts 账户列表
     * @return 指令信息
     */
    private static Map<String, Object> parseCreateVirtualPoolMetadata(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        
        // 解析账户
        info.put("virtual_pool", accounts[0]);
        info.put("virtual_pool_metadata", accounts[1]);
        info.put("creator", accounts[2]);
        info.put("payer", accounts[3]);
        info.put("system_program", accounts[4]);
        info.put("event_authority", accounts[5]);
        info.put("program", accounts[6]);
        
        return info;
    }
    
    /**
     * 解析 InitializeVirtualPoolWithSplToken 指令
     *
     * @param buffer   数据缓冲区
     * @param accounts 账户列表
     * @return 指令信息
     */
    private static Map<String, Object> parseInitializeVirtualPoolWithSplToken(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        
        // 解析 InitializePoolParameters 结构
        // 由于结构复杂，这里只解析基本参数
        // 实际应用中可能需要根据具体需求解析更多参数
//        info.put("initial_price", new BigInteger(buffer.getLong()+""));
//        info.put("initial_base_reserve", new BigInteger(buffer.getLong()+""));
//        info.put("initial_quote_reserve", new BigInteger(buffer.getLong()+""));
        
        // 解析账户
        info.put("config", accounts[0]);
        info.put("pool_authority", accounts[1]);
        info.put("creator", accounts[2]);
        info.put("base_mint", accounts[3]);
        info.put("quote_mint", accounts[4]);
        info.put("pool", accounts[5]);
        info.put("base_vault", accounts[6]);
        info.put("quote_vault", accounts[7]);
        info.put("mint_metadata", accounts[8]);
        info.put("metadata_program", accounts[9]);
        info.put("payer", accounts[10]);
        info.put("token_quote_program", accounts[11]);
        info.put("token_program", accounts[12]);
        info.put("system_program", accounts[13]);
        info.put("event_authority", accounts[14]);
        info.put("program", accounts[15]);
        
        return info;
    }
    
    /**
     * 解析 InitializeVirtualPoolWithToken2022 指令
     *
     * @param buffer   数据缓冲区
     * @param accounts 账户列表
     * @return 指令信息
     */
    private static Map<String, Object> parseInitializeVirtualPoolWithToken2022(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        
        // 解析 InitializePoolParameters 结构
        // 由于结构复杂，这里只解析基本参数
        // 实际应用中可能需要根据具体需求解析更多参数
//        info.put("initial_price", new BigInteger(buffer.getLong()+""));
//        info.put("initial_base_reserve", new BigInteger(buffer.getLong()+""));
//        info.put("initial_quote_reserve", new BigInteger(buffer.getLong()+""));
        
        // 解析账户
        info.put("config", accounts[0]);
        info.put("pool_authority", accounts[1]);
        info.put("creator", accounts[2]);
        info.put("base_mint", accounts[3]);
        info.put("quote_mint", accounts[4]);
        info.put("pool", accounts[5]);
        info.put("base_vault", accounts[6]);
        info.put("quote_vault", accounts[7]);
        info.put("payer", accounts[8]);
        info.put("token_quote_program", accounts[9]);
        info.put("token_program", accounts[10]);
        info.put("system_program", accounts[11]);
        info.put("event_authority", accounts[12]);
        info.put("program", accounts[13]);
        
        return info;
    }


    /**
     * 解析 MigrateMeteoraDamm 指令
     *
     * @param buffer  数据缓冲区
     * @param accounts 账户列表
     * @return 指令信息
     */
    private static Map<String, Object> parseMigrateMeteoraDamm(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 解析账户
        info.put("virtual_pool", accounts[0]);
        info.put("migration_metadata", accounts[1]);
        info.put("config", accounts[2]);
        info.put("pool_authority", accounts[3]);
        info.put("pool", accounts[4]);

        info.put("damm_config", accounts[5]);
        info.put("lp_mint", accounts[6]);
        info.put("token_a_mint", accounts[8]);
        info.put("token_b_mint", accounts[9]);
        info.put("a_vault", accounts[10]);

        info.put("b_vault", accounts[11]);
        info.put("a_token_vault", accounts[12]);
        info.put("b_token_vault", accounts[13]);
        info.put("a_vault_lp_mint", accounts[14]);
        info.put("b_vault_lp_mint", accounts[15]);

        info.put("a_vault_lp", accounts[16]);
        info.put("b_vault_lp", accounts[17]);
        info.put("base_vault", accounts[18]);
        info.put("quote_vault", accounts[19]);
        info.put("virtual_pool_lp", accounts[20]);

        info.put("protocol_token_a_fee", accounts[21]);
        info.put("protocol_token_b_fee", accounts[22]);
        info.put("payer", accounts[23]);
        info.put("rent", accounts[24]);
        info.put("mint_metadata", accounts[25]);

        info.put("metadata_program", accounts[26]);
        info.put("amm_program", accounts[27]);
        info.put("vault_program", accounts[28]);
        info.put("token_program", accounts[29]);
        info.put("associated_token_program", accounts[30]);

        info.put("system_program", accounts[31]);

        return info;
    }


    /**
     * 解析 MigrationDammV2 指令
     *
     * @param buffer  数据缓冲区
     * @param accounts 账户列表
     * @return 指令信息
     */
    private static Map<String, Object> parseMigrationDammV2(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        
        // 解析账户
        info.put("virtual_pool", accounts[0]);
        info.put("migration_metadata", accounts[1]);
        info.put("config", accounts[2]);
        info.put("pool_authority", accounts[3]);
        info.put("pool", accounts[4]);
        info.put("first_position_nft_mint", accounts[5]);
        info.put("first_position_nft_account", accounts[6]);
        info.put("first_position", accounts[7]);
        info.put("second_position_nft_mint", accounts[8]);
        info.put("second_position_nft_account", accounts[9]);
        info.put("second_position", accounts[10]);
        info.put("damm_pool_authority", accounts[11]);

        info.put("amm_program", accounts[12]);
        info.put("base_mint", accounts[13]);
        info.put("quote_mint", accounts[14]);
        info.put("token_a_vault", accounts[15]);
        info.put("token_b_vault", accounts[16]);
        info.put("base_vault", accounts[17]);
        info.put("quote_vault", accounts[18]);
        info.put("payer", accounts[19]);

        info.put("token_base_program", accounts[20]);
        info.put("token_quote_program", accounts[21]);
        info.put("token_2022_program", accounts[22]);
        info.put("damm_event_authority", accounts[23]);
        info.put("system_program", accounts[24]);
        
        return info;
    }

    private static Map<String, Object> parseMigrationDammV2CreateMetadata(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        // 解析账户
        info.put("virtual_pool", accounts[0]);
        info.put("config", accounts[1]);
        info.put("migration_metadata", accounts[2]);
        info.put("payer", accounts[3]);
        info.put("system_program", accounts[4]);
        info.put("event_authority", accounts[5]);
        info.put("program", accounts[6]);
        return info;
    }
    private static Map<String, Object> parseMigrationMeteoraDammCreateMetadata(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        // 解析账户
        info.put("virtual_pool", accounts[0]);
        info.put("config", accounts[1]);
        info.put("migration_metadata", accounts[2]);
        info.put("payer", accounts[3]);
        info.put("system_program", accounts[4]);
        info.put("event_authority", accounts[5]);
        info.put("program", accounts[6]);
        return info;
    }
}