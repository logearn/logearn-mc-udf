package cn.xlystar.parse.solSwap.boop;

import cn.xlystar.parse.solSwap.InstructionParser;
import org.bitcoinj.core.Base58;
import org.bouncycastle.util.encoders.Hex;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class BoopInstructionParser extends InstructionParser {

    public static final String PROGRAM_ID = "boop8hVGQGqehUK2iVEMEnMrL5RbjywRzHKBmBE7ry4";

    @Override
    public String getMethodId(ByteBuffer buffer) {
        byte[] methodIdByte = new byte[8];
        buffer.get(methodIdByte);
        return Hex.toHexString(methodIdByte);
    }

    @Override
    public Map<String, Object> matchInstruction(String methodId, ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info;
        switch (BoopInstruction.fromValue(methodId)) {
            case BUY_TOKEN:
                info = parseBuyToken(buffer, accounts);
                break;
            case SELL_TOKEN:
                info = parseSellToken(buffer, accounts);
                break;
            case GRADUATE:
                info = parseGraduate(buffer, accounts);
                break;
            case CREATE_RAYDIUM_POOL:
                info = parseCreateRaydiumPool(buffer, accounts);
                break;
            case CREATE_RAYDIUM_RANDOM_POOL:
                info = parseCreateRaydiumRandomPool(buffer, accounts);
                break;
            case CREATE_TOKEN:
                info = parseCreateToken(buffer, accounts);
                break;
            case DEPLOY_BONDING_CURVE:
                info = parseDeployBondingCurve(buffer, accounts);
                break;
            case DEPOSIT_INTO_RAYDIUM:
                info = parseDepositIntoRaydium(buffer, accounts);
                break;
            case INITIALIZE:
                info = parseInitialize(buffer, accounts);
                break;
            case SWAP_SOL_FOR_TOKENS_ON_RAYDIUM:
                info = parseSwapSolForTokensOnRaydium(buffer, accounts);
                break;
            case SWAP_TOKENS_FOR_SOL_ON_RAYDIUM:
                info = parseSwapTokensForSolOnRaydium(buffer, accounts);
                break;
            default:
                return new HashMap<>();
        }
        return info;
    }

    // 2uGsaw6Yit3WuBGuDDx2ZRnd8prrU4XP1v2TPCom78ySthH6bWtQNp5xVxmRB6mTQGe5wYJXxGvX3CwjDTGU3XJF
    private static Map<String, Object> parseBuyToken(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        info.put("buy_amount", Long.toUnsignedString(buffer.getLong()));
        info.put("amount_out_min", Long.toUnsignedString(buffer.getLong()));

        info.put("mint", accounts[0]);
        info.put("bonding_curve", accounts[1]);
        info.put("trading_fees_vault", accounts[2]);
        info.put("bonding_curve_vault", accounts[3]);
        info.put("bonding_curve_sol_vault", accounts[4]);
        info.put("recipient_token_account", accounts[5]);
        info.put("buyer", accounts[6]);
        info.put("config", accounts[7]);
        info.put("vault_authority", accounts[8]);
        info.put("wsol", accounts[9]);
        info.put("system_program", accounts[10]);
        info.put("token_program", accounts[11]);
        info.put("associated_token_program", accounts[12]);
        return info;
    }


    /**
     * 解析 CreateRaydiumPool 指令
     * AcRJLfG2zTFSyj2kyXgWeoUe6vCJ1XdUHBeA4K9BNSGeQWmWfQwP8Y8AxbkaCX2uSi7k8LBd4gdErMKFBuWyLyS
     * @param buffer   数据缓冲区
     * @param accounts 账户列表
     * @return 指令信息
     */
    private static Map<String, Object> parseCreateRaydiumPool(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 解析账户
        info.put("cpmm_program", accounts[0]);
        info.put("amm_config", accounts[1]);
        info.put("authority", accounts[2]);
        info.put("pool_state", accounts[3]);
        info.put("token_0_mint", accounts[4]);
        info.put("token_1_mint", accounts[5]);
        info.put("lp_mint", accounts[6]);
        info.put("vault_authority", accounts[7]);
        info.put("bonding_curve", accounts[8]);
        info.put("bonding_curve_vault", accounts[9]);
        info.put("bonding_curve_wsol_vault", accounts[10]);
        info.put("creator_lp_token", accounts[11]);
        info.put("token_0_vault", accounts[12]);
        info.put("token_1_vault", accounts[13]);
        info.put("create_pool_fee", accounts[14]);
        info.put("observation_state", accounts[15]);
        info.put("operator", accounts[16]);
        info.put("config", accounts[17]);
        info.put("token_program", accounts[18]);
        info.put("associated_token_program", accounts[19]);
        info.put("system_program", accounts[20]);
        info.put("rent", accounts[21]);

        return info;
    }
    /**
     * 解析 Graduate 指令
     *
     * @param buffer   数据缓冲区
     * @param accounts 账户列表
     * @return 指令信息
     */
    private static Map<String, Object> parseGraduate(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 解析账户
        info.put("mint", accounts[0]);
        info.put("wsol", accounts[1]);
        info.put("protocol_fee_recipient", accounts[2]);
        info.put("token_distributor", accounts[3]);
        info.put("token_distributor_token_account", accounts[4]);
        info.put("vault_authority", accounts[5]);
        info.put("bonding_curve_sol_vault", accounts[6]);
        info.put("bonding_curve", accounts[7]);
        info.put("bonding_curve_vault", accounts[8]);
        info.put("bonding_curve_wsol_account", accounts[9]);
        info.put("operator", accounts[10]);
        info.put("config", accounts[11]);
        info.put("system_program", accounts[12]);
        info.put("token_program", accounts[13]);
        info.put("associated_token_program", accounts[14]);

        return info;
    }
    /**
     * 解析 CreateRaydiumRandomPool 指令
     *
     * @param buffer   数据缓冲区
     * @param accounts 账户列表
     * @return 指令信息
     */
    private static Map<String, Object> parseCreateRaydiumRandomPool(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 解析账户
        info.put("cpmm_program", accounts[0]);
        info.put("amm_config", accounts[1]);
        info.put("authority", accounts[2]);
        info.put("pool_state", accounts[3]);
        info.put("token_0_mint", accounts[4]);
        info.put("token_1_mint", accounts[5]);
        info.put("lp_mint", accounts[6]);
        info.put("vault_authority", accounts[7]);
        info.put("bonding_curve", accounts[8]);
        info.put("bonding_curve_vault", accounts[9]);
        info.put("bonding_curve_wsol_vault", accounts[10]);
        info.put("creator_lp_token", accounts[11]);
        info.put("token_0_vault", accounts[12]);
        info.put("token_1_vault", accounts[13]);
        info.put("create_pool_fee", accounts[14]);
        info.put("observation_state", accounts[15]);
        info.put("operator", accounts[16]);
        info.put("config", accounts[17]);
        info.put("token_program", accounts[18]);
        info.put("associated_token_program", accounts[19]);
        info.put("system_program", accounts[20]);
        info.put("rent", accounts[21]);

        return info;
    }

    /**
     * 解析 CreateToken 指令
     *
     * @param buffer   数据缓冲区
     * @param accounts 账户列表
     * @return 指令信息
     */
    private static Map<String, Object> parseCreateToken(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 解析参数
        info.put("salt", Long.toUnsignedString(buffer.getLong()));

        // 读取字符串参数：name
        info.put("name", parseString(buffer));
        // 读取字符串参数：symbol
        info.put("symbol", parseString(buffer));
        // 读取字符串参数：uri
        info.put("uri", parseString(buffer));

        // 解析账户
        info.put("config", accounts[0]);
        info.put("metadata", accounts[1]);
        info.put("mint", accounts[2]);
        info.put("payer", accounts[3]);
        info.put("rent", accounts[4]);
        info.put("system_program", accounts[5]);
        info.put("token_program", accounts[6]);
        info.put("token_metadata_program", accounts[7]);

        return info;
    }

    /**
     * 解析 DeployBondingCurve 指令
     *
     * @param buffer   数据缓冲区
     * @param accounts 账户列表
     * @return 指令信息
     */
    private static Map<String, Object> parseDeployBondingCurve(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 解析参数
        byte[] creatorBytes = new byte[32];
        buffer.get(creatorBytes);
        info.put("creator", Base58.encode(creatorBytes));
        info.put("salt", Long.toUnsignedString(buffer.getLong()));

        // 解析账户
        info.put("mint", accounts[0]);
        info.put("vault_authority", accounts[1]);
        info.put("bonding_curve", accounts[2]);
        info.put("bonding_curve_sol_vault", accounts[3]);
        info.put("bonding_curve_vault", accounts[4]);
        info.put("config", accounts[5]);
        info.put("payer", accounts[6]);
        info.put("system_program", accounts[7]);
        info.put("token_program", accounts[8]);
        info.put("associated_token_program", accounts[9]);

        return info;
    }

    /**
     * 解析 DepositIntoRaydium 指令
     *
     * @param buffer   数据缓冲区
     * @param accounts 账户列表
     * @return 指令信息
     */
    private static Map<String, Object> parseDepositIntoRaydium(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 解析参数
        info.put("lp_token_amount", Long.toUnsignedString(buffer.getLong()));
        info.put("maximum_token_0_amount", Long.toUnsignedString(buffer.getLong()));
        info.put("maximum_token_1_amount", Long.toUnsignedString(buffer.getLong()));

        // 解析账户
        info.put("config", accounts[0]);
        info.put("amm_config", accounts[1]);
        info.put("operator", accounts[2]);
        info.put("operator_wsol_account", accounts[3]);
        info.put("vault_authority", accounts[4]);
        info.put("authority", accounts[5]);
        info.put("pool_state", accounts[6]);
        info.put("token_0_vault", accounts[7]);
        info.put("token_1_vault", accounts[8]);
        info.put("bonding_curve_vault", accounts[9]);
        info.put("bonding_curve_wsol_vault", accounts[10]);
        info.put("token_program", accounts[11]);
        info.put("token_program_2022", accounts[12]);
        info.put("system_program", accounts[13]);
        info.put("associated_token_program", accounts[14]);
        info.put("lp_mint", accounts[15]);
        info.put("cpmm_program", accounts[16]);
        info.put("owner_lp_token", accounts[17]);
        info.put("bonding_curve", accounts[18]);
        info.put("token_0_mint", accounts[19]);
        info.put("token_1_mint", accounts[20]);

        return info;
    }

    /**
     * 解析 Initialize 指令
     *
     * @param buffer   数据缓冲区
     * @param accounts 账户列表
     * @return 指令信息
     */
    private static Map<String, Object> parseInitialize(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 解析参数
        byte[] protocolFeeRecipientBytes = new byte[32];
        buffer.get(protocolFeeRecipientBytes);
        info.put("protocol_fee_recipient", Base58.encode(protocolFeeRecipientBytes));

        byte[] tokenDistributorBytes = new byte[32];
        buffer.get(tokenDistributorBytes);
        info.put("token_distributor", Base58.encode(tokenDistributorBytes));

        // 解析账户
        info.put("config", accounts[0]);
        info.put("authority", accounts[1]);
        info.put("system_program", accounts[2]);

        return info;
    }
    /**
     * 解析 SwapSolForTokensOnRaydium 指令
     *
     * @param buffer   数据缓冲区
     * @param accounts 账户列表
     * @return 指令信息
     */
    private static Map<String, Object> parseSwapSolForTokensOnRaydium(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 解析参数
        info.put("amount_in", Long.toUnsignedString(buffer.getLong()));
        info.put("minimum_amount_out", Long.toUnsignedString(buffer.getLong()));

        // 解析账户
        info.put("config", accounts[0]);
        info.put("bonding_curve", accounts[1]);
        info.put("amm_config", accounts[2]);
        info.put("operator", accounts[3]);
        info.put("vault_authority", accounts[4]);
        info.put("authority", accounts[5]);
        info.put("pool_state", accounts[6]);
        info.put("input_vault", accounts[7]);
        info.put("output_vault", accounts[8]);
        info.put("bonding_curve_vault", accounts[9]);
        info.put("bonding_curve_wsol_vault", accounts[10]);
        info.put("output_token_mint", accounts[11]);
        info.put("input_token_mint", accounts[12]);
        info.put("token_program", accounts[13]);
        info.put("cp_swap_program", accounts[14]);
        info.put("observation_state", accounts[15]);

        return info;
    }

    /**
     * 解析 SwapTokensForSolOnRaydium 指令
     *
     * @param buffer   数据缓冲区
     * @param accounts 账户列表
     * @return 指令信息
     */
    private static Map<String, Object> parseSwapTokensForSolOnRaydium(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 解析参数
        info.put("amount_in", Long.toUnsignedString(buffer.getLong()));
        info.put("minimum_amount_out", Long.toUnsignedString(buffer.getLong()));

        // 解析账户
        info.put("config", accounts[0]);
        info.put("bonding_curve", accounts[1]);
        info.put("amm_config", accounts[2]);
        info.put("operator", accounts[3]);
        info.put("vault_authority", accounts[4]);
        info.put("authority", accounts[5]);
        info.put("pool_state", accounts[6]);
        info.put("input_vault", accounts[7]);
        info.put("output_vault", accounts[8]);
        info.put("bonding_curve_vault", accounts[9]);
        info.put("bonding_curve_wsol_vault", accounts[10]);
        info.put("input_token_mint", accounts[11]);
        info.put("output_token_mint", accounts[12]);
        info.put("token_program", accounts[13]);
        info.put("cp_swap_program", accounts[14]);
        info.put("observation_state", accounts[15]);

        return info;
    }

    /**
     * 解析 SellToken 指令
     * 2i44HFsocHgKGGNGn8mHddLNymbwXGpNJHpS3YdQvXWJd7AFvEvKsezYbRW37QWEYr7oS8dZQf7E5rH6RiXuE4wH
     * @param buffer   数据缓冲区
     * @param accounts 账户列表
     * @return 指令信息
     */
    private static Map<String, Object> parseSellToken(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 解析参数
        info.put("sell_amount", Long.toUnsignedString(buffer.getLong()));
        info.put("amount_out_min", Long.toUnsignedString(buffer.getLong()));

        // 解析账户
        info.put("mint", accounts[0]);
        info.put("bonding_curve", accounts[1]);
        info.put("trading_fees_vault", accounts[2]);
        info.put("bonding_curve_vault", accounts[3]);
        info.put("bonding_curve_sol_vault", accounts[4]);
        info.put("seller_token_account", accounts[5]);
        info.put("seller", accounts[6]);
        info.put("recipient", accounts[7]);
        info.put("config", accounts[8]);
        info.put("system_program", accounts[9]);
        info.put("token_program", accounts[10]);
        info.put("associated_token_program", accounts[11]);

        return info;
    }

    private static String parseString(ByteBuffer buffer) {
        int length = buffer.getInt();
        byte[] bytes = new byte[length];
        buffer.get(bytes);
        return new String(bytes);
    }

}