package cn.xlystar.parse.solSwap.meteora.almm;

import cn.xlystar.parse.solSwap.InstructionParser;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.math.BigInteger;
import org.bouncycastle.util.encoders.Hex;


public class MeteoraAlmmInstructionParser extends InstructionParser {

    private static final String PROGRAM_ID = "Eo7WjKq67rjJQSZxS6z3YkapzY3eMj6Xy8X5EQVn5UaB";

    @Override
    public String getMethodId(ByteBuffer buffer) {
        byte[] methodIdByte = new byte[8];
        buffer.get(methodIdByte);
        return Hex.toHexString(methodIdByte);
    }

    @Override
    public Map<String, Object> matchInstruction(String methodId, ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info;
        switch (MeteoraAlmmInstruction.fromValue(methodId)) {
            case INITIALIZE_PERMISSIONED_POOL:
                info = parseInitializePermissionedPool(buffer, accounts);
                break;
            // due 无数据
            case INITIALIZE_PERMISSIONLESS_POOL:
                info = parseInitializePermissionlessPool(buffer, accounts);
                break;
            case INITIALIZE_PERMISSIONLESS_POOL_WITH_FEE_TIER:
                info = parseInitializePermissionlessPoolWithFeeTier(buffer, accounts);
                break;
            case LOCK:
                info = parseLock(buffer, accounts);
                break;
            case SWAP:
                info = parseSwap(buffer, accounts);
                break;
            case CLAIM_FEE:
                info = parseClaimFee(buffer, accounts);
                break;
            // due 无数据
            case ADD_LIQUIDITY:
                info = parseAddLiquidity(buffer, accounts);
                break;
            // due 无数据                
            case REMOVE_LIQUIDITY:
                info = parseRemoveLiquidity(buffer, accounts);
                break;
            case BOOTSTRAP_LIQUIDITY:
                info = parseBootstrapLiquidity(buffer, accounts);
                break;
            case ADD_BALANCE_LIQUIDITY:
                info = parseAddBalanceLiquidity(buffer, accounts);
                break;
            case REMOVE_BALANCE_LIQUIDITY:
                info = parseRemoveBalanceLiquidity(buffer, accounts);
                break;
            case REMOVE_LIQUIDITY_SINGLE_SIDE:
                info = parseRemoveLiquiditySingleSide(buffer, accounts);
                break;
            case ADD_IMBALANCE_LIQUIDITY:
                info = parseAddImbalanceLiquidity(buffer, accounts);
                break;
            case SET_POOL_FEES:
                info = parseSetPoolFees(buffer, accounts);
                break;
            default:
                return new HashMap<>();
        }
        return info;
    }

    private static Map<String, Object> parseInitializePermissionedPool(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        
        // 解析参数 - 正确,只需要curveType一个参数
        info.put("curve_type", buffer.getInt());

        // 账户信息
        info.put("pool", accounts[0]);
        info.put("lp_mint", accounts[1]);
        info.put("token_a_mint", accounts[2]);
        info.put("token_b_mint", accounts[3]);
        info.put("a_vault", accounts[4]);
        info.put("b_vault", accounts[5]);
        info.put("a_vault_lp_mint", accounts[6]);
        info.put("b_vault_lp_mint", accounts[7]);
        info.put("a_vault_lp", accounts[8]);
        info.put("b_vault_lp", accounts[9]);
        info.put("admin_token_a", accounts[10]);
        info.put("admin_token_b", accounts[11]);
        info.put("admin_pool_lp", accounts[12]);
        info.put("protocol_token_a_fee", accounts[13]);
        info.put("protocol_token_b_fee", accounts[14]);
        info.put("admin", accounts[15]);
        info.put("fee_owner", accounts[16]);
        info.put("rent", accounts[17]);
        info.put("mint_metadata", accounts[18]);
        info.put("metadata_program", accounts[19]);
        info.put("vault_program", accounts[20]);
        if (accounts.length > 21) info.put("token_program", accounts[21]);
        if (accounts.length > 22) info.put("associated_token_program", accounts[22]);
        if (accounts.length > 23) info.put("system_program", accounts[23]);

        return info;
    }

    private static Map<String, Object> parseInitializePermissionlessPool(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        
        // 解析参数 - 正确,需要curveType和两个amount参数
        info.put("curve_type", buffer.getInt());
        info.put("token_a_amount", Long.toUnsignedString(buffer.getLong()));
        info.put("token_b_amount", Long.toUnsignedString(buffer.getLong()));

        // 账户信息
        info.put("pool", accounts[0]);
        info.put("lp_mint", accounts[1]);
        info.put("token_a_mint", accounts[2]);
        info.put("token_b_mint", accounts[3]);
        info.put("a_vault", accounts[4]);
        info.put("b_vault", accounts[5]);
        info.put("a_token_vault", accounts[6]);
        info.put("b_token_vault", accounts[7]);
        info.put("a_vault_lp_mint", accounts[8]);
        info.put("b_vault_lp_mint", accounts[9]);
        info.put("a_vault_lp", accounts[10]);
        info.put("b_vault_lp", accounts[11]);
        info.put("payer_token_a", accounts[12]);
        info.put("payer_token_b", accounts[13]);
        info.put("payer_pool_lp", accounts[14]);
        info.put("protocol_token_a_fee", accounts[15]);
        info.put("protocol_token_b_fee", accounts[16]);
        info.put("payer", accounts[17]);
        info.put("fee_owner", accounts[18]);
        info.put("rent", accounts[19]);
        info.put("mint_metadata", accounts[20]);
        info.put("metadata_program", accounts[21]);
        info.put("vault_program", accounts[22]);
        info.put("token_program", accounts[23]);
        info.put("associated_token_program", accounts[24]);
        info.put("system_program", accounts[25]);

        return info;
    }

    private static Map<String, Object> parseInitializePermissionlessPoolWithFeeTier(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        
        // 解析参数 - 正确,需要curveType、tradeFeeBps和两个amount参数
        info.put("curve_type", buffer.getInt());
        info.put("trade_fee_bps", Long.toUnsignedString(buffer.getLong()));
        info.put("token_a_amount", Long.toUnsignedString(buffer.getLong()));
        info.put("token_b_amount", Long.toUnsignedString(buffer.getLong()));

        // 账户信息
        info.put("pool", accounts[0]);
        info.put("lp_mint", accounts[1]);
        info.put("token_a_mint", accounts[2]);
        info.put("token_b_mint", accounts[3]);
        info.put("a_vault", accounts[4]);
        info.put("b_vault", accounts[5]);
        info.put("a_token_vault", accounts[6]);
        info.put("b_token_vault", accounts[7]);
        info.put("a_vault_lp_mint", accounts[8]);
        info.put("b_vault_lp_mint", accounts[9]);
        info.put("a_vault_lp", accounts[10]);
        info.put("b_vault_lp", accounts[11]);
        info.put("payer_token_a", accounts[12]);
        info.put("payer_token_b", accounts[13]);
        info.put("payer_pool_lp", accounts[14]);
        info.put("protocol_token_a_fee", accounts[15]);
        info.put("protocol_token_b_fee", accounts[16]);
        info.put("payer", accounts[17]);
        info.put("fee_owner", accounts[18]);
        info.put("rent", accounts[19]);
        info.put("mint_metadata", accounts[20]);
        info.put("metadata_program", accounts[21]);
        info.put("vault_program", accounts[22]);
        info.put("token_program", accounts[23]);
        info.put("associated_token_program", accounts[24]);
        info.put("system_program", accounts[25]);

        return info;
    }

    private static Map<String, Object> parseLock(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        
        // 解析参数 - 正确,只需要amount一个参数
        info.put("amount", Long.toUnsignedString(buffer.getLong()));

        // 账户信息
        info.put("owner", accounts[0]);
        info.put("pool", accounts[1]);
        info.put("lock_escrow", accounts[2]);
        info.put("owner_lp", accounts[3]);
        info.put("escrow_lp", accounts[4]);
        info.put("token_program", accounts[5]);

        return info;
    }

    private static Map<String, Object> parseSwap(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        
        // 参数解析正确,不需要修改
        info.put("amount_in", Long.toUnsignedString(buffer.getLong()));
        info.put("minimum_amount_out", Long.toUnsignedString(buffer.getLong()));

        // 账户顺序需要修改为IDL中定义的顺序
        info.put("pool", accounts[0]);
        info.put("user_source_token", accounts[1]); 
        info.put("user_destination_token", accounts[2]);
        info.put("a_vault", accounts[3]);
        info.put("b_vault", accounts[4]);
        info.put("a_token_vault", accounts[5]);
        info.put("b_token_vault", accounts[6]);
        info.put("a_vault_lp_mint", accounts[7]);
        info.put("b_vault_lp_mint", accounts[8]);
        info.put("a_vault_lp", accounts[9]);
        info.put("b_vault_lp", accounts[10]);
        info.put("protocol_token_fee", accounts[11]);
        info.put("user", accounts[12]);
        info.put("vault_program", accounts[13]);
        info.put("token_program", accounts[14]);

        return info;
    }

    private static Map<String, Object> parseClaimFee(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        
        // 解析参数 - 正确,只需要amount一个参数
        info.put("amount", Long.toUnsignedString(buffer.getLong()));

        // 账户信息
        info.put("owner", accounts[0]);
        info.put("pool", accounts[1]);
        info.put("lock_escrow", accounts[2]);
        info.put("owner_lp", accounts[3]);
        info.put("escrow_lp", accounts[4]);
        info.put("owner_token_a_fee", accounts[5]);
        info.put("owner_token_b_fee", accounts[6]);
        info.put("protocol_token_a_fee", accounts[7]);
        info.put("protocol_token_b_fee", accounts[8]);
        info.put("token_program", accounts[9]);

        return info;
    }

    private static Map<String, Object> parseAddLiquidity(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        
        // 解析参数 - 正确,需要两个amount和minLpAmount三个参数
        info.put("token_a_amount", Long.toUnsignedString(buffer.getLong()));
        info.put("token_b_amount", Long.toUnsignedString(buffer.getLong()));
        info.put("min_lp_amount", Long.toUnsignedString(buffer.getLong()));

        // 账户信息
        info.put("pool", accounts[0]);
        info.put("lp_mint", accounts[1]); 
        info.put("user", accounts[2]);
        info.put("user_token_a", accounts[3]);
        info.put("user_token_b", accounts[4]);
        info.put("user_lp", accounts[5]);
        info.put("token_program", accounts[6]);
        info.put("a_token_vault", accounts[7]);
        info.put("b_token_vault", accounts[8]);
        info.put("a_vault", accounts[9]);
        info.put("b_vault", accounts[10]);
        info.put("a_vault_lp", accounts[11]);
        info.put("b_vault_lp", accounts[12]);
        info.put("a_vault_lp_mint", accounts[13]);
        info.put("b_vault_lp_mint", accounts[14]);
        info.put("vault_program", accounts[15]);

        return info;
    }

    private static Map<String, Object> parseRemoveLiquidity(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        
        // 解析参数 - 正确,需要lpAmount和两个minAmount三个参数
        info.put("lp_amount", Long.toUnsignedString(buffer.getLong()));
        info.put("min_token_a_amount", Long.toUnsignedString(buffer.getLong()));
        info.put("min_token_b_amount", Long.toUnsignedString(buffer.getLong()));

        // 账户信息
        info.put("user", accounts[0]);
        info.put("pool", accounts[1]);
        info.put("user_lp", accounts[2]);
        info.put("user_token_a", accounts[3]);
        info.put("user_token_b", accounts[4]);
        info.put("token_a_vault", accounts[5]);
        info.put("token_b_vault", accounts[6]);
        info.put("lp_mint", accounts[7]);
        info.put("token_program", accounts[8]);
        info.put("vault_program", accounts[9]);

        return info;
    }

    private static Map<String, Object> parseBootstrapLiquidity(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        
        // 解析参数 - 正确,需要两个amount参数
        info.put("token_a_amount", Long.toUnsignedString(buffer.getLong()));
        info.put("token_b_amount", Long.toUnsignedString(buffer.getLong()));

        // 账户信息
        info.put("admin", accounts[0]);
        info.put("pool", accounts[1]);
        info.put("admin_token_a", accounts[2]);
        info.put("admin_token_b", accounts[3]);
        info.put("admin_lp", accounts[4]);
        info.put("token_a_vault", accounts[5]);
        info.put("token_b_vault", accounts[6]);
        info.put("lp_mint", accounts[7]);
        info.put("token_program", accounts[8]);
        info.put("vault_program", accounts[9]);

        return info;
    }

    private static Map<String, Object> parseAddBalanceLiquidity(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        
        // 解析参数 - 正确,只需要lpAmount一个参数
        info.put("lp_amount", Long.toUnsignedString(buffer.getLong()));

        // 账户信息
        info.put("user", accounts[0]);
        info.put("pool", accounts[1]);
        info.put("user_token_a", accounts[2]);
        info.put("user_token_b", accounts[3]);
        info.put("user_lp", accounts[4]);
        info.put("token_a_vault", accounts[5]);
        info.put("token_b_vault", accounts[6]);
        info.put("lp_mint", accounts[7]);
        info.put("token_program", accounts[8]);
        info.put("vault_program", accounts[9]);

        return info;
    }

    private static Map<String, Object> parseRemoveBalanceLiquidity(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        
        // 解析参数 - 正确,需要lpAmount和两个minAmount三个参数
        info.put("lp_amount", Long.toUnsignedString(buffer.getLong()));
        info.put("min_token_a_amount", Long.toUnsignedString(buffer.getLong()));
        info.put("min_token_b_amount", Long.toUnsignedString(buffer.getLong()));

        // 账户信息
        info.put("user", accounts[0]);
        info.put("pool", accounts[1]);
        info.put("user_lp", accounts[2]);
        info.put("user_token_a", accounts[3]);
        info.put("user_token_b", accounts[4]);
        info.put("token_a_vault", accounts[5]);
        info.put("token_b_vault", accounts[6]);
        info.put("lp_mint", accounts[7]);
        info.put("token_program", accounts[8]);
        info.put("vault_program", accounts[9]);

        return info;
    }

    private static Map<String, Object> parseRemoveLiquiditySingleSide(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        
        // 解析参数 - 正确,需要lpAmount、minTokenAmount和isTokenA三个参数
        info.put("lp_amount", Long.toUnsignedString(buffer.getLong()));
        info.put("min_token_amount", Long.toUnsignedString(buffer.getLong()));
        info.put("is_token_a", buffer.get() != 0);

        // 账户信息
        info.put("user", accounts[0]);
        info.put("pool", accounts[1]);
        info.put("user_lp", accounts[2]);
        info.put("user_token", accounts[3]);
        info.put("token_vault", accounts[4]);
        info.put("other_token_vault", accounts[5]);
        info.put("lp_mint", accounts[6]);
        info.put("token_program", accounts[7]);
        info.put("vault_program", accounts[8]);

        return info;
    }

    private static Map<String, Object> parseAddImbalanceLiquidity(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        
        // 解析参数 - 正确,需要两个amount和minLpAmount三个参数
        info.put("token_a_amount", Long.toUnsignedString(buffer.getLong()));
        info.put("token_b_amount", Long.toUnsignedString(buffer.getLong()));
        info.put("min_lp_amount", Long.toUnsignedString(buffer.getLong()));

        // 账户信息
        info.put("user", accounts[0]);
        info.put("pool", accounts[1]);
        info.put("user_token_a", accounts[2]);
        info.put("user_token_b", accounts[3]);
        info.put("user_lp", accounts[4]);
        info.put("token_a_vault", accounts[5]);
        info.put("token_b_vault", accounts[6]);
        info.put("lp_mint", accounts[7]);
        info.put("token_program", accounts[8]);
        info.put("vault_program", accounts[9]);

        return info;
    }

    private static Map<String, Object> parseSetPoolFees(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        
        // 解析参数 - 正确,需要三个fee rate参数
        info.put("trade_fee_rate", Integer.toUnsignedString(buffer.getInt()));
        info.put("protocol_fee_rate", Integer.toUnsignedString(buffer.getInt()));
        info.put("fund_fee_rate", Integer.toUnsignedString(buffer.getInt()));

        // 账户信息
        info.put("pool", accounts[0]);
        info.put("feeOperator", accounts[1]);

        return info;
    }
} 