package cn.xlystar.parse.solSwap;

import cn.xlystar.parse.solSwap.pump.PumpDotFunInstruction;
import cn.xlystar.parse.solSwap.pump.PumpDotFunInstructionParser;
import cn.xlystar.parse.solSwap.raydium.clmm.RaydiumClmmInstruction;
import cn.xlystar.parse.solSwap.raydium.clmm.RaydiumClmmInstructionParser;
import cn.xlystar.parse.solSwap.raydium.cpmm.RaydiumCpmmInstruction;
import cn.xlystar.parse.solSwap.raydium.cpmm.RaydiumCpmmInstructionParser;
import cn.xlystar.parse.solSwap.spl_associated_token.SplAssociatedTokenInstruction;
import cn.xlystar.parse.solSwap.spl_associated_token.SplAssociatedTokenInstructionParser;
import cn.xlystar.parse.solSwap.spl_token.SplTokenInstruction;
import cn.xlystar.parse.solSwap.spl_token.SplTokenInstructionParser;
import cn.xlystar.parse.solSwap.spl_token_2022.SplToken2022InstructionParser;
import cn.xlystar.parse.solSwap.system_program.SystemInstruction;
import cn.xlystar.parse.solSwap.system_program.SystemInstructionParser;
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
                && (parsed.get("method_id").toString().equals(SplTokenInstruction.Transfer.getValue() + "") || parsed.get("method_id").equals(SplTokenInstruction.TransferChecked.getValue() + ""))
        ) {
            result.put("sender", info.get("source"));
            result.put("receiver", info.get("destination"));
            result.put("sender_owner", info.get("authority"));
            result.put("amount", info.get("amount"));
            result.put("mint", info.get("mint"));
            result.put("instruction_type", "transfer");
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
                && parsed.get("method_id").equals(SplTokenInstruction.InitializeAccount.getValue() + "")
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
        } else if (programId.equals(PumpDotFunInstructionParser.PROGRAM_ID)
                && parsed.get("method_id").equals(PumpDotFunInstruction.CREATE.getValue() + "")
        ) {
            result.put("pool_id", info.get("bondingCurve"));
            result.put("vault_0", info.get("associatedBondingCurve"));
            result.put("vault_mint_0", info.get("mint"));
            result.put("vault_1", "So11111111111111111111111111111111111111112");
            result.put("vault_mint_1", "So11111111111111111111111111111111111111112");
            result.put("user", info.get("user"));
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
        }
        return null;
    }

}
