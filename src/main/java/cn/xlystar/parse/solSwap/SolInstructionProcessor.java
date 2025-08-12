package cn.xlystar.parse.solSwap;

import cn.xlystar.parse.solSwap.boop.BoopInstruction;
import cn.xlystar.parse.solSwap.boop.BoopInstructionParser;
import cn.xlystar.parse.solSwap.jupiter.JupiterInstruction;
import cn.xlystar.parse.solSwap.jupiter.JupiterInstructionParser;
import cn.xlystar.parse.solSwap.meteora.almm.MeteoraAlmmInstruction;
import cn.xlystar.parse.solSwap.meteora.almm.MeteoraAlmmInstructionParser;
import cn.xlystar.parse.solSwap.meteora.dbc.MeteoraDbcInstruction;
import cn.xlystar.parse.solSwap.meteora.dbc.MeteoraDbcInstructionParser;
import cn.xlystar.parse.solSwap.meteora.dlmm.MeteoraDlmmInstruction;
import cn.xlystar.parse.solSwap.meteora.dlmm.MeteoraDlmmInstructionParser;
import cn.xlystar.parse.solSwap.meteora.dlmm_v2.MeteoraDlmmV2Instruction;
import cn.xlystar.parse.solSwap.meteora.dlmm_v2.MeteoraDlmmV2InstructionParser;
import cn.xlystar.parse.solSwap.moonshot.MoonshotInstruction;
import cn.xlystar.parse.solSwap.moonshot.MoonshotInstructionParser;
import cn.xlystar.parse.solSwap.okx.OkxInstructionParser;
import cn.xlystar.parse.solSwap.pump.PumpDotFunInstruction;
import cn.xlystar.parse.solSwap.pump.PumpDotFunInstructionParser;
import cn.xlystar.parse.solSwap.pump_swap.PumpSwapInstruction;
import cn.xlystar.parse.solSwap.pump_swap.PumpSwapInstructionParser;
import cn.xlystar.parse.solSwap.raydium.amm_v4.RaydiumAmmInstruction;
import cn.xlystar.parse.solSwap.raydium.amm_v4.RaydiumAmmInstructionParser;
import cn.xlystar.parse.solSwap.raydium.clmm.RaydiumClmmInstruction;
import cn.xlystar.parse.solSwap.raydium.clmm.RaydiumClmmInstructionParser;
import cn.xlystar.parse.solSwap.raydium.cpmm.RaydiumCpmmInstruction;
import cn.xlystar.parse.solSwap.raydium.cpmm.RaydiumCpmmInstructionParser;
import cn.xlystar.parse.solSwap.raydium.launch.RaydiumLaunchInstruction;
import cn.xlystar.parse.solSwap.raydium.launch.RaydiumLaunchInstructionParser;
import cn.xlystar.parse.solSwap.spl_associated_token.SplAssociatedTokenInstruction;
import cn.xlystar.parse.solSwap.spl_associated_token.SplAssociatedTokenInstructionParser;
import cn.xlystar.parse.solSwap.spl_token.SplTokenInstruction;
import cn.xlystar.parse.solSwap.spl_token.SplTokenInstructionParser;
import cn.xlystar.parse.solSwap.spl_token_2022.SplToken2022Instruction;
import cn.xlystar.parse.solSwap.spl_token_2022.SplToken2022InstructionParser;
import cn.xlystar.parse.solSwap.system_program.SystemInstruction;
import cn.xlystar.parse.solSwap.system_program.SystemInstructionParser;
import cn.xlystar.parse.solSwap.whirlpool.WhirlpoolInstruction;
import cn.xlystar.parse.solSwap.whirlpool.WhirlpoolInstructionParser;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.bitcoinj.core.Base58;

import java.math.BigInteger;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class SolInstructionProcessor {

    public static Map<String, Object> processInstruction(String programId, String[] inputAccount, String instructionData) {
        // 1、获取解析器
        InstructionParser parser = SolInstructionParserFactory.getParser(programId);
        if (parser == null) {
            return null;
        }
        // 2、解析
        return parser.parseInstruction(Base58.decode(instructionData), inputAccount);
    }

    public static Map<String, Object> processLogs(String programId, String logs) {
        // 1、获取解析器
        InstructionParser parser = SolInstructionParserFactory.getParser(programId);
        if (parser == null) {
            return null;
        }
        // 2、解析
        return parser.parseLogs(Base64.getDecoder().decode(logs));
    }

    public static Map<String, Object> processInstructionRt(String programId, String[] inputAccount, String instructionData) {
        // 1、获取解析器
        InstructionParser parser = SolInstructionParserFactory.getParser(programId);
        if (parser == null) {
            return null;
        }
        // 2、解析
        Map<String, Object> parsed = parser.parseInstruction(Base58.decode(instructionData), inputAccount);
        if (MapUtils.isEmpty(parsed) || !parsed.containsKey("method_id")) return null;

        Map<String, Object> result = new HashMap<>();
        Map<String, Object> info = (Map<String, Object>) parsed.get("info");
        boolean isTokenProgram = programId.equals(SplTokenInstructionParser.PROGRAM_ID) || programId.equals(SplToken2022InstructionParser.PROGRAM_ID);
        if (isTokenProgram
                && (
                (parsed.get("method_id").toString().equals(SplTokenInstruction.Transfer.getValue() + "") || parsed.get("method_id").equals(SplTokenInstruction.TransferChecked.getValue() + ""))
                        || (parsed.get("method_id").toString().equals(SplToken2022Instruction.Transfer.getValue() + "") || parsed.get("method_id").equals(SplToken2022Instruction.TransferChecked.getValue() + ""))
        )
        ) {
            result.put("sender", info.get("source"));
            result.put("receiver", info.get("destination"));
            result.put("sender_owner", info.get("authority"));
            result.put("amount", info.get("amount"));
            result.put("mint", info.get("mint"));
            result.put("instruction_type", "transfer");
            return result;
        } else if (isTokenProgram
                && ( parsed.get("method_id").toString().equals(SplTokenInstruction.SetAuthority.getValue() + "")
                || parsed.get("method_id").toString().equals(SplToken2022Instruction.SetAuthority.getValue() + "")
        )
        ) {
            if (info.containsKey("newAuthority") && info.get("newAuthority") != null) {
                result.put("account", info.get("account"));
                result.put("authority", info.get("authority"));
                result.put("newAuthority", info.get("newAuthority"));
                result.put("instruction_type", "transfer_owner");
                return result;
            }
        } else if (isTokenProgram
                && (parsed.get("method_id").toString().equals(SplTokenInstruction.ApproveChecked.getValue() + "")
                || parsed.get("method_id").toString().equals(SplToken2022Instruction.ApproveChecked.getValue() + "")
        )
        ) {
            result.put("account", info.get("source"));
            result.put("mint", info.get("mint"));
            result.put("authority", info.get("owner"));
            result.put("newAuthority", info.get("delegate"));
            result.put("amount", info.get("amount"));
            result.put("instruction_type", "transfer_owner");
            return result;
        } else if (programId.equals(SystemInstructionParser.PROGRAM_ID)
                && (parsed.get("method_id").equals(SystemInstruction.Transfer.getValue() + "")
//                || parsed.get("method_id").equals(SystemInstruction.TransferWithSeed.getValue())
        )
        ) {
            result.put("sender_owner", info.get("source"));
            result.put("receiver_owner", info.get("destination"));
            result.put("amount", info.get("lamports"));
            result.put("mint", "So11111111111111111111111111111111111111112");
            result.put("instruction_type", "sol_transfer");
            return result;
        } else if (isTokenProgram
                && (parsed.get("method_id").equals(SplTokenInstruction.Burn.getValue() + "") || parsed.get("method_id").equals(SplTokenInstruction.BurnChecked.getValue() + ""))
        ) {
            result.put("sender_owner", info.get("authority"));
            result.put("sender", info.get("account"));
            result.put("mint", info.get("mint"));
            result.put("amount", info.get("amount"));
            result.put("instruction_type", "burn");
            return result;
        } else if (programId.equals(SplAssociatedTokenInstructionParser.PROGRAM_ID)
                && (parsed.get("method_id").equals(SplAssociatedTokenInstruction.Create.getValue() + "") || parsed.get("method_id").equals(SplAssociatedTokenInstruction.CreateIdempotent.getValue() + ""))
        ) {

            result.put("account", info.get("account"));
            result.put("mint", info.get("mint"));
            result.put("owner", info.get("wallet"));
            result.put("instruction_type", "pda_create_account");
            return result;
        } else if (isTokenProgram
                && (parsed.get("method_id").equals(SplTokenInstruction.InitializeAccount.getValue() + "")
                || parsed.get("method_id").equals(SplTokenInstruction.InitializeAccount2.getValue() + "")
                || parsed.get("method_id").equals(SplTokenInstruction.InitializeAccount3.getValue() + "")
                || parsed.get("method_id").equals(SplToken2022Instruction.InitializeAccount.getValue() + "")
                || parsed.get("method_id").equals(SplToken2022Instruction.InitializeAccount2.getValue() + "")
                || parsed.get("method_id").equals(SplToken2022Instruction.InitializeAccount3.getValue() + "")
        )
        ) {
            // case: https://solana.fm/tx/iTm1aRzzhmcThNC3aMtR6zsLJQAxZ84cathpVGkrMmZCoSG3FDpaMRExgjQH2FjnX5dQoUTYAXDBQLP8R7rwAv9?cluster=mainnet-alpha
            result.put("account", info.get("account"));
            result.put("mint", info.get("mint"));
            result.put("owner", info.get("owner"));
            result.put("instruction_type", "pda_create_account");
            return result;
        } else if (isTokenProgram
                && parsed.get("method_id").equals(SplTokenInstruction.CloseAccount.getValue() + "")
        ) {
            // case: https://solana.fm/tx/iTm1aRzzhmcThNC3aMtR6zsLJQAxZ84cathpVGkrMmZCoSG3FDpaMRExgjQH2FjnX5dQoUTYAXDBQLP8R7rwAv9?cluster=mainnet-alpha
            result.put("account", info.get("account"));
            result.put("owner", info.get("owner"));
            result.put("instruction_type", "pda_close_account");
            return result;
        } else if (programId.equals(RaydiumClmmInstructionParser.PROGRAM_ID) && (parsed.get("method_id").equals(RaydiumClmmInstruction.SWAP.getValue() + "") || parsed.get("method_id").equals(RaydiumClmmInstruction.SWAP_V2.getValue() + ""))) {
            result.put("pool_id", info.get("pool_state"));
            result.put("input_vault", info.get("input_vault"));
            result.put("input_token_account", info.get("input_token_account"));
            result.put("output_vault", info.get("output_vault"));
            result.put("output_token_account", info.get("output_token_account"));
            result.put("instruction_type", "dex_amm");
            return result;
        } else if (programId.equals(RaydiumAmmInstructionParser.PROGRAM_ID)
                && (parsed.get("method_id").equals(RaydiumAmmInstruction.SWAP_BASE_IN.getValue())
                || parsed.get("method_id").equals(RaydiumAmmInstruction.SWAP_BASE_OUT.getValue()))
        ) {
            result.put("pool_id", info.get("ammAccount"));
            result.put("input_vault_owner", info.get("ammAuthority"));
            result.put("output_vault_owner", info.get("ammAuthority"));
            result.put("input_token_account_owner", info.get("userWalletAccount"));
            result.put("output_token_account_owner", info.get("userWalletAccount"));
            result.put("input_token_account", info.get("userSourceTokenAccount"));
            result.put("output_token_account", info.get("userDestinationTokenAccount"));
            result.put("input_vault", info.get("ammPcVaultAccount"));
            result.put("output_vault", info.get("ammCoinVaultAccount"));
            result.put("instruction_type", "dex_amm");
            return result;
        } else if (programId.equals(RaydiumAmmInstructionParser.PROGRAM_ID)
                && (parsed.get("method_id").equals(RaydiumAmmInstruction.INITIALIZE2.getValue()))
        ) {
            result.put("pool_id", info.get("newAmmAccount"));
            result.put("vault_0", info.get("ammPcVaultAccount"));
            result.put("vault_mint_0", info.get("ammPcMintAccount"));
            result.put("init_vault_0_amount", info.get("initPcAmount"));
            result.put("vault_1", info.get("ammCoinVaultAccount"));
            result.put("vault_mint_1", info.get("ammCoinMintAccount"));
            result.put("init_vault_1_amount", info.get("initCoinAmount"));
            result.put("user", info.get("userWalletAccount"));
            result.put("instruction_type", "raydium_amm_v4_instruction");
            return result;
        } else if (programId.equals(RaydiumLaunchInstructionParser.PROGRAM_ID)
                && (parsed.get("method_id").equals(RaydiumLaunchInstruction.INITIALIZE.getValue()))
        ) {
            result.put("pool_id", info.get("pool_state"));
            result.put("vault_0", info.get("base_vault"));
            result.put("vault_mint_0", info.get("base_mint"));
            result.put("vault_1", info.get("quote_vault"));
            result.put("vault_mint_1", info.get("quote_mint"));
            result.put("user", info.get("creator"));
            result.put("creator", info.get("creator"));
            result.put("platform_config", info.get("platform_config"));
            result.put("instruction_type", "raydium_launch_create_token_instruction");
            return result;
        } else if (programId.equals(RaydiumLaunchInstructionParser.PROGRAM_ID)
                && (parsed.get("method_id").equals(RaydiumLaunchInstruction.BUY_EXACT_IN.getValue())
                || parsed.get("method_id").equals(RaydiumLaunchInstruction.BUY_EXACT_OUT.getValue())
        )
        ) {
            result.put("pool_id", info.get("pool_state"));
            result.put("input_vault", info.get("quote_vault"));
            result.put("input_vault_mint", info.get("quote_mint"));
            result.put("input_token_account", info.get("user_quote_token"));
            result.put("output_vault", info.get("base_vault"));
            result.put("output_vault_mint", info.get("base_mint"));
            result.put("output_token_account", info.get("user_base_token"));
            result.put("platform_config", info.get("platform_config"));
            result.put("instruction_type", "dex_amm");
            return result;
        } else if ((programId.equals(MoonshotInstructionParser.PROGRAM_ID) && MoonshotInstructionParser.isMigrated(parsed.get("method_id").toString()))
                || (programId.equals(BoopInstructionParser.PROGRAM_ID) && BoopInstructionParser.isMigrated(parsed.get("method_id").toString()))
        ) {
            result.put("mint", info.get("mint"));
            result.put("instruction_type", "dex_migrate");
            return result;
        } else if (
                (programId.equals(RaydiumLaunchInstructionParser.PROGRAM_ID) && RaydiumLaunchInstructionParser.isMigrated(parsed.get("method_id").toString()))
                        || (programId.equals(MeteoraDbcInstructionParser.PROGRAM_ID) && MeteoraDbcInstructionParser.isMigrated(parsed.get("method_id").toString()))
                        || (programId.equals(PumpDotFunInstructionParser.PROGRAM_ID) && PumpDotFunInstructionParser.isMigrated(parsed.get("method_id").toString()))
        ) {
            result.put("mint", info.get("base_mint"));
            result.put("instruction_type", "dex_migrate");
            return result;
        } else if (programId.equals(RaydiumLaunchInstructionParser.PROGRAM_ID)
                && (parsed.get("method_id").equals(RaydiumLaunchInstruction.SELL_EXACT_IN.getValue())
                || parsed.get("method_id").equals(RaydiumLaunchInstruction.SELL_EXACT_OUT.getValue())
        )
        ) {
            result.put("pool_id", info.get("pool_state"));
            result.put("input_vault", info.get("base_vault"));
            result.put("input_vault_mint", info.get("base_mint"));
            result.put("input_token_account", info.get("user_base_token"));
            result.put("output_vault", info.get("quote_vault"));
            result.put("output_vault_mint", info.get("quote_mint"));
            result.put("output_token_account", info.get("user_quote_token"));
            result.put("platform_config", info.get("platform_config"));
            result.put("instruction_type", "dex_amm");
            return result;
        } else if (programId.equals(MeteoraDbcInstructionParser.PROGRAM_ID)
                && (parsed.get("method_id").equals(MeteoraDbcInstruction.INITIALIZE_VIRTUAL_POOL_WITH_SPL_TOKEN.getValue())
                || parsed.get("method_id").equals(MeteoraDbcInstruction.INITIALIZE_VIRTUAL_POOL_WITH_TOKEN2022.getValue())
        )
        ) {
            result.put("pool", info.get("pool_state"));
            result.put("vault_0", info.get("base_vault"));
            result.put("vault_mint_0", info.get("base_mint"));
            result.put("vault_1", info.get("quote_vault"));
            result.put("vault_mint_1", info.get("quote_mint"));
            result.put("user", info.get("creator"));
            result.put("creator", info.get("creator"));
            result.put("instruction_type", "meteora_dbc_create_token_instruction");
            return result;
        } else if (programId.equals(MeteoraDbcInstructionParser.PROGRAM_ID)
                && parsed.get("method_id").equals(MeteoraDbcInstruction.SWAP.getValue())
        ) {
            result.put("pool_id", info.get("pool"));
            result.put("input_vault", info.get("base_vault"));
            result.put("input_vault_mint", info.get("base_mint"));
            result.put("input_token_account", info.get("input_token_account"));
            result.put("output_vault", info.get("quote_vault"));
            result.put("output_vault_mint", info.get("quote_mint"));
            result.put("output_token_account", info.get("output_token_account"));
            result.put("instruction_type", "dex_amm");
            return result;
        } else if (programId.equals(BoopInstructionParser.PROGRAM_ID)
                && parsed.get("method_id").equals(BoopInstruction.DEPLOY_BONDING_CURVE.getValue())
        ) {
            result.put("pool", info.get("bonding_curve"));
            result.put("vault_0", info.get("bonding_curve_vault"));
            result.put("vault_mint_0", info.get("mint"));
            result.put("vault_1", info.get("bonding_curve_sol_vault"));
            result.put("vault_mint_1", "So11111111111111111111111111111111111111112");
            result.put("user", info.get("creator"));
            result.put("creator", info.get("creator"));
            result.put("instruction_type", "boop_create_token_instruction");
            return result;
        } else if (programId.equals(BoopInstructionParser.PROGRAM_ID)
                && parsed.get("method_id").equals(BoopInstruction.BUY_TOKEN.getValue())
        ) {
            result.put("pool_id", info.get("bonding_curve"));
            result.put("input_vault", info.get("bonding_curve_sol_vault"));
            result.put("input_vault_mint", "So11111111111111111111111111111111111111112");
            result.put("input_token_account", info.get("buyer"));
            result.put("output_vault", info.get("bonding_curve_vault"));
            result.put("output_vault_mint", info.get("mint"));
            result.put("output_token_account", info.get("recipient_token_account"));
            result.put("instruction_type", "dex_amm");
            return result;
        } else if (programId.equals(BoopInstructionParser.PROGRAM_ID)
                && parsed.get("method_id").equals(BoopInstruction.SELL_TOKEN.getValue())
        ) {
            result.put("pool_id", info.get("bonding_curve"));
            result.put("input_vault", info.get("bonding_curve_vault"));
            result.put("input_vault_mint", info.get("mint"));
            result.put("input_token_account", info.get("seller_token_account"));
            result.put("output_vault", info.get("bonding_curve_sol_vault"));
            result.put("output_vault_mint", "So11111111111111111111111111111111111111112");
            result.put("output_token_account", info.get("recipient"));
            result.put("instruction_type", "dex_amm");
            return result;
        } else if (programId.equals(PumpSwapInstructionParser.PROGRAM_ID)
                && (parsed.get("method_id").equals(PumpSwapInstruction.CREATE_POOL.getValue()))
        ) {
            result.put("pool_id", info.get("pool"));
            result.put("vault_0", info.get("pool_base_token_account"));
            result.put("vault_mint_0", info.get("base_mint"));
            result.put("init_vault_0_amount", info.get("base_amount_in"));
            result.put("vault_1", info.get("pool_quote_token_account"));
            result.put("vault_mint_1", info.get("quote_mint"));
            result.put("init_vault_1_amount", info.get("quote_amount_in"));
            result.put("user", info.get("creator"));
            result.put("instruction_type", "pump_amm_create_pool_instruction");
            return result;
        } else if (programId.equals(RaydiumCpmmInstructionParser.PROGRAM_ID)
                && (parsed.get("method_id").equals(RaydiumCpmmInstruction.SWAP_BASE_IN.getValue() + "") || parsed.get("method_id").equals(RaydiumCpmmInstruction.SWAP_BASE_OUT.getValue() + ""))) {
            result.put("pool_id", info.get("poolState"));
            result.put("input_vault", info.get("inputVault"));
            result.put("input_vault_mint", info.get("inputTokenMint"));
            result.put("input_token_account", info.get("inputTokenAccount"));
            result.put("output_vault", info.get("outputVault"));
            result.put("output_vault_mint", info.get("outputTokenMint"));
            result.put("output_token_account", info.get("outputTokenAccount"));
            result.put("instruction_type", "dex_amm");
            return result;
        } else if (programId.equals(WhirlpoolInstructionParser.PROGRAM_ID)
                && (parsed.get("method_id").equals(WhirlpoolInstruction.SWAP.getValue()) || parsed.get("method_id").equals(WhirlpoolInstruction.SWAP_V2.getValue()))) {
            result.put("pool_id", info.get("whirlpool"));
            result.put("input_vault", info.get("tokenVaultA"));
            result.put("input_vault_mint", info.get("tokenMintA"));
            result.put("input_token_account", info.get("tokenOwnerAccountA"));
            result.put("output_vault", info.get("tokenVaultB"));
            result.put("output_vault_mint", info.get("tokenMintB"));
            result.put("output_token_account", info.get("tokenOwnerAccountB"));
            result.put("instruction_type", "dex_amm");
            return result;
        } else if (programId.equals(MeteoraDlmmInstructionParser.PROGRAM_ID)
                && (parsed.get("method_id").equals(MeteoraDlmmInstruction.SWAP.getValue())
                || parsed.get("method_id").equals(MeteoraDlmmInstruction.SWAP2.getValue()))
        ) {
            result.put("pool_id", info.get("lb_pair"));
            result.put("input_vault", info.get("reserve_x"));
            result.put("input_vault_mint", info.get("token_x_mint"));
            result.put("input_token_account", info.get("user_token_in"));
            result.put("output_vault", info.get("reserve_y"));
            result.put("output_vault_mint", info.get("token_y_mint"));
            result.put("output_token_account", info.get("user_token_out"));
            result.put("instruction_type", "dex_amm");
            return result;
        } else if (programId.equals(MeteoraDlmmV2InstructionParser.PROGRAM_ID)
                && parsed.get("method_id").equals(MeteoraDlmmV2Instruction.SWAP.getValue())) {
            result.put("pool_id", info.get("pool"));
            result.put("input_vault", info.get("token_a_vault"));
            result.put("input_vault_mint", info.get("token_a_mint"));
            result.put("input_token_account", info.get("input_token_account"));
            result.put("output_vault", info.get("token_b_vault"));
            result.put("output_vault_mint", info.get("token_b_mint"));
            result.put("output_token_account", info.get("output_token_account"));
            result.put("instruction_type", "dex_amm");
            return result;
        } else if (programId.equals(OkxInstructionParser.PROGRAM_ID)) {
            if (MapUtils.isEmpty(info)) return null;
            result.put("input_vault_mint", info.getOrDefault("source_mint", null));
            result.put("input_token_account", info.getOrDefault("source_token_account", null));
            result.put("output_vault_mint", info.getOrDefault("destination_mint", null));
            result.put("output_token_account", info.getOrDefault("destination_token_account", null));
            result.put("instruction_type", "aggregator_amm");
            return result;
        } else if (programId.equals(JupiterInstructionParser.PROGRAM_ID) && !parsed.get("method_id").equals(JupiterInstruction.LOG.getValue())) {
            if (MapUtils.isEmpty(info)) return null;
            boolean isShare = !(parsed.get("method_id").equals(JupiterInstruction.ROUTE)
                    || parsed.get("method_id").equals(JupiterInstruction.ROUTE_WITH_TOKEN_LEDGER)
                    || parsed.get("method_id").equals(JupiterInstruction.EXACT_OUT_ROUTE))
                    ;
            result.put("input_vault_mint", info.getOrDefault("source_mint", null));
            result.put("input_token_account", isShare ? info.get("source_token_account") : info.get("user_destination_token_account"));
            result.put("output_vault_mint", info.getOrDefault("destination_mint", null));
            result.put("output_token_account",isShare ? info.get("destination_token_account") : info.get("user_destination_token_account"));
            result.put("instruction_type", "aggregator_amm");
            return result;
        } else if (programId.equals(MeteoraAlmmInstructionParser.PROGRAM_ID)
                && parsed.get("method_id").equals(MeteoraAlmmInstruction.SWAP.getValue())) {
            result.put("pool_id", info.get("pool"));
            result.put("input_vault", info.get("a_token_vault"));
            result.put("input_token_account", info.get("user_source_token"));
            result.put("output_vault", info.get("b_token_vault"));
            result.put("output_token_account", info.get("user_destination_token"));
            result.put("instruction_type", "dex_amm");
            return result;
        } else if (programId.equals(MoonshotInstructionParser.PROGRAM_ID)
                && (
                parsed.get("method_id").equals(MoonshotInstruction.BUY.getValue())
                        || parsed.get("method_id").equals(MoonshotInstruction.SELL.getValue())
        )) {
            boolean isBuy = parsed.get("method_id").equals(MoonshotInstruction.BUY.getValue());
            result.put("pool_id", info.get("curveAccount"));
            result.put("input_vault", isBuy ? info.get("curveTokenAccount") : info.get("curveAccount"));
            result.put("input_vault_mint", isBuy ? "So11111111111111111111111111111111111111112" : info.get("mint"));
            result.put("input_token_account", isBuy ? info.get("sender") : info.get("senderTokenAccount"));
            result.put("output_vault", isBuy ? info.get("curveAccount") : info.get("curveTokenAccount"));
            result.put("output_vault_mint", isBuy ? info.get("mint") : "So11111111111111111111111111111111111111112");
            result.put("output_token_account", isBuy ? info.get("senderTokenAccount") : info.get("sender"));
            result.put("instruction_type", "dex_amm");
            return result;
        } else if (programId.equals(MoonshotInstructionParser.PROGRAM_ID)
                && parsed.get("method_id").equals(MoonshotInstruction.TOKEN_MINT.getValue())
        ) {
            result.put("pool_id", info.get("curveAccount"));
            result.put("vault_0", info.get("curveTokenAccount"));
            result.put("vault_mint_0", info.get("mint"));
            result.put("vault_1", info.get("curveAccount"));
            result.put("vault_mint_1", "So11111111111111111111111111111111111111112");
            result.put("user", info.get("sender"));
            result.put("creator", info.get("sender"));
            result.put("instruction_type", "moonshot_amm_create_pool_instruction");
            return result;
        } else if (programId.equals(PumpDotFunInstructionParser.PROGRAM_ID)
                && parsed.get("method_id").equals(PumpDotFunInstruction.CREATE.getValue() + "")
        ) {
            result.put("pool_id", info.get("bondingCurve"));
            result.put("vault_0", info.get("associatedBondingCurve"));
            result.put("vault_mint_0", info.get("mint"));
            result.put("vault_1", "So11111111111111111111111111111111111111112");
            result.put("vault_mint_1", "So11111111111111111111111111111111111111112");
            result.put("user", info.get("user"));
            result.put("creator", info.get("creator"));
            result.put("instruction_type", "pump_create_pool");
            return result;
        } else if (programId.equals(PumpDotFunInstructionParser.PROGRAM_ID)
                && parsed.get("method_id").equals(PumpDotFunInstruction.ANCHOR_SELF_CPI_LOG.getValue())
                && info.get("eventType").toString().equals("trade")
        ) {
            result.put("solAmount", info.get("solAmount"));
            result.put("tokenAmount", info.get("tokenAmount"));
            result.put("isBuy", info.get("isBuy"));
            result.put("virtual_sol_reserves", info.get("virtual_sol_reserves"));
            result.put("virtual_token_reserves", info.get("virtual_token_reserves"));
            result.put("real_sol_reserves", info.get("real_sol_reserves"));
            result.put("real_token_reserves", info.get("real_token_reserves"));
            result.put("mint", info.get("mint"));
            result.put("instruction_type", "pump_amm_liquidity");
            return result;
        } else if (programId.equals(PumpDotFunInstructionParser.PROGRAM_ID)
                && (parsed.get("method_id").equals(PumpDotFunInstruction.BUY.getValue()) || parsed.get("method_id").equals(PumpDotFunInstruction.SELL.getValue()))
        ) {
            if (parsed.get("method_id").equals(PumpDotFunInstruction.BUY.getValue())) {
                result.put("input_vault", info.get("associatedBondingCurve"));
                result.put("input_vault_mint", "So11111111111111111111111111111111111111112");
                result.put("input_token_account", info.get("associatedUser"));
                result.put("output_amount", new BigInteger(info.get("amount").toString()));
                result.put("output_vault", info.get("associatedBondingCurve"));
                result.put("output_vault_mint", info.get("mint"));
                result.put("output_token_account", info.get("associatedUser"));
            } else {
                result.put("input_amount", new BigInteger(info.get("amount").toString()));
                result.put("input_vault", info.get("associatedBondingCurve"));
                result.put("input_vault_mint", info.get("mint"));
                result.put("input_token_account", info.get("associatedUser"));
                result.put("output_vault", info.get("associatedBondingCurve"));
                result.put("output_vault_mint", "So11111111111111111111111111111111111111112");
                result.put("output_token_account", info.get("associatedUser"));
            }
            result.put("pool_id", info.get("bondingCurve"));
            result.put("user", info.get("user"));
            result.put("instruction_type", "pump_amm_instruction");
            return result;
        } else if (programId.equals(PumpSwapInstructionParser.PROGRAM_ID)
                && (parsed.get("method_id").equals(PumpSwapInstruction.BUY.getValue()) || parsed.get("method_id").equals(PumpSwapInstruction.SELL.getValue()))
        ) {
            if (parsed.get("method_id").equals(PumpDotFunInstruction.BUY.getValue())) {
                result.put("pool_id", info.get("pool"));
                result.put("input_vault", info.get("pool_quote_token_account"));
                result.put("input_vault_mint", info.get("quote_mint"));
                result.put("input_token_account", info.get("user_quote_token_account"));
                result.put("output_vault", info.get("pool_base_token_account"));
                result.put("output_vault_mint", info.get("base_mint"));
                result.put("output_token_account", info.get("user_base_token_account"));
            } else {
                result.put("pool_id", info.get("pool"));
                result.put("input_vault", info.get("pool_base_token_account"));
                result.put("input_vault_mint", info.get("base_mint"));
                result.put("input_token_account", info.get("user_base_token_account"));
                result.put("output_vault", info.get("pool_quote_token_account"));
                result.put("output_vault_mint", info.get("quote_mint"));
                result.put("output_token_account", info.get("user_quote_token_account"));
            }
            result.put("instruction_type", "dex_amm");
            return result;
        }
        return null;
    }

}
