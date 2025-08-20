package cn.xlystar.parse.solSwap;

import cn.xlystar.parse.solSwap.boop.BoopInstructionParser;
import cn.xlystar.parse.solSwap.heaven.HeavenInstructionParser;
import cn.xlystar.parse.solSwap.jupiter.JupiterInstructionParser;
import cn.xlystar.parse.solSwap.metaplex.MetadataInstructionParser;
import cn.xlystar.parse.solSwap.meteora.almm.MeteoraAlmmInstructionParser;
import cn.xlystar.parse.solSwap.meteora.dbc.MeteoraDbcInstructionParser;
import cn.xlystar.parse.solSwap.meteora.dlmm.MeteoraDlmmInstructionParser;
import cn.xlystar.parse.solSwap.meteora.dlmm_v2.MeteoraDlmmV2InstructionParser;
import cn.xlystar.parse.solSwap.moonshot.MoonshotInstructionParser;
import cn.xlystar.parse.solSwap.okx.OkxInstructionParser;
import cn.xlystar.parse.solSwap.pump.PumpDotFunInstructionParser;
import cn.xlystar.parse.solSwap.pump_swap.PumpSwapInstructionParser;
import cn.xlystar.parse.solSwap.raydium.amm_v4.RaydiumAmmInstructionParser;
import cn.xlystar.parse.solSwap.raydium.clmm.RaydiumClmmInstructionParser;
import cn.xlystar.parse.solSwap.raydium.cpmm.RaydiumCpmmInstructionParser;
import cn.xlystar.parse.solSwap.raydium.launch.RaydiumLaunchInstructionParser;
import cn.xlystar.parse.solSwap.spl_associated_token.SplAssociatedTokenInstructionParser;
import cn.xlystar.parse.solSwap.spl_token.SplTokenInstructionParser;
import cn.xlystar.parse.solSwap.spl_token_2022.SplToken2022InstructionParser;
import cn.xlystar.parse.solSwap.system_program.SystemInstructionParser;
import cn.xlystar.parse.solSwap.whirlpool.WhirlpoolInstructionParser;

import java.util.HashMap;
import java.util.Map;


public class SolInstructionParserFactory {

    private static final Map<String, InstructionParser> configMap = new HashMap<>();
    /**
     * sol 账号区分大小写
     */
    static {
        // 官方程序
        configMap.put(SystemInstructionParser.PROGRAM_ID, new SystemInstructionParser());
        configMap.put(SplTokenInstructionParser.PROGRAM_ID, new SplTokenInstructionParser());
        configMap.put(SplToken2022InstructionParser.PROGRAM_ID, new SplToken2022InstructionParser());
        configMap.put(SplAssociatedTokenInstructionParser.PROGRAM_ID, new SplAssociatedTokenInstructionParser());
//        // metaplex 元数据存储
        configMap.put(MetadataInstructionParser.PROGRAM_ID, new MetadataInstructionParser());
        // raydium 协议
        configMap.put(RaydiumAmmInstructionParser.PROGRAM_ID, new RaydiumAmmInstructionParser());
        configMap.put(RaydiumClmmInstructionParser.PROGRAM_ID, new RaydiumClmmInstructionParser());
        configMap.put(RaydiumCpmmInstructionParser.PROGRAM_ID, new RaydiumCpmmInstructionParser());
        configMap.put(RaydiumLaunchInstructionParser.PROGRAM_ID, new RaydiumLaunchInstructionParser());
//        // orca whirlpool 协议
        configMap.put(WhirlpoolInstructionParser.PROGRAM_ID, new WhirlpoolInstructionParser());
//        // pump.fun 协议
        configMap.put(PumpDotFunInstructionParser.PROGRAM_ID, new PumpDotFunInstructionParser());
        configMap.put(PumpSwapInstructionParser.PROGRAM_ID, new PumpSwapInstructionParser());
//        // moonshot 协议
        configMap.put(MoonshotInstructionParser.PROGRAM_ID, new MoonshotInstructionParser());
//        // meteora 协议
        configMap.put(MeteoraAlmmInstructionParser.PROGRAM_ID, new MeteoraAlmmInstructionParser());
        configMap.put(MeteoraDlmmInstructionParser.PROGRAM_ID, new MeteoraDlmmInstructionParser());
        configMap.put(MeteoraDlmmV2InstructionParser.PROGRAM_ID, new MeteoraDlmmV2InstructionParser());
        configMap.put(MeteoraDbcInstructionParser.PROGRAM_ID, new MeteoraDbcInstructionParser());
        // boop 协议
        configMap.put(BoopInstructionParser.PROGRAM_ID, new BoopInstructionParser());
        // heaven 协议
        configMap.put(HeavenInstructionParser.PROGRAM_ID, new HeavenInstructionParser());

        // OKX v2 协议
        configMap.put(OkxInstructionParser.PROGRAM_ID, new OkxInstructionParser());
        
        // Jupiter v6 协议
        configMap.put(JupiterInstructionParser.PROGRAM_ID, new JupiterInstructionParser());
    }

    public static InstructionParser getParser(String key) {
        return configMap.get(key);
    }

}
