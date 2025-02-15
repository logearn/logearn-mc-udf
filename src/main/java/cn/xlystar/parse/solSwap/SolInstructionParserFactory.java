package cn.xlystar.parse.solSwap;

import cn.xlystar.parse.solSwap.metaplex.MetadataInstructionParser;
import cn.xlystar.parse.solSwap.meteora.almm.MeteoraAlmmInstructionParser;
import cn.xlystar.parse.solSwap.meteora.dlmm.MeteoraDlmmInstructionParser;
import cn.xlystar.parse.solSwap.moonshot.MoonshotInstructionParser;
import cn.xlystar.parse.solSwap.pump.PumpDotFunInstructionParser;
import cn.xlystar.parse.solSwap.raydium.amm_v4.RaydiumAmmInstructionParser;
import cn.xlystar.parse.solSwap.raydium.clmm.RaydiumClmmInstructionParser;
import cn.xlystar.parse.solSwap.raydium.cpmm.RaydiumCpmmInstructionParser;
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
        configMap.put("11111111111111111111111111111111", new SystemInstructionParser());
        configMap.put("TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA", new SplTokenInstructionParser());
        configMap.put("TokenzQdBNbLqP5VEhdkAS6EPFLC1PHnBqCXEpPxuEb", new SplToken2022InstructionParser());
        configMap.put("ATokenGPvbdGVxr1b2hvZbsiqW5xWH25efTNsLJA8knL", new SplAssociatedTokenInstructionParser());
//        // metaplex 元数据存储
        configMap.put("metaqbxxUerdq28cj1RbAWkYQm3ybzjb6a8bt518x1s", new MetadataInstructionParser());
        // raydium 协议
        configMap.put("675kPX9MHTjS2zt1qfr1NYHuzeLXfQM9H24wFSUt1Mp8", new RaydiumAmmInstructionParser());
        configMap.put("CAMMCzo5YL8w4VFF8KVHrK22GGUsp5VTaW7grrKgrWqK", new RaydiumClmmInstructionParser());
        configMap.put("CPMMoo8L3F4NbTegBCKVNunggL7H1ZpdTHKxQB5qKP1C", new RaydiumCpmmInstructionParser());
//        // orca whirlpool 协议
        configMap.put("whirLbMiicVdio4qvUfM5KAg6Ct8VwpYzGff3uctyCc", new WhirlpoolInstructionParser());
//        // pump.fun 协议
        configMap.put("6EF8rrecthR5Dkzon8Nwu78hRvfCKubJ14M5uBEwF6P", new PumpDotFunInstructionParser());
//        // moonshot 协议
        configMap.put("MoonCVVNZFSYkqNXP6bxHLPL6QQJiMagDL3qcqUQTrG", new MoonshotInstructionParser());
//        // meteora 协议
        configMap.put("Eo7WjKq67rjJQSZxS6z3YkapzY3eMj6Xy8X5EQVn5UaB", new MeteoraAlmmInstructionParser());
        configMap.put("LBUZKhRxPF3XUpBCjp4YzTKgLccjZhTSDM9YuVaPwxo", new MeteoraDlmmInstructionParser());
    }

    public static InstructionParser getParser(String key) {
        return configMap.get(key);
    }

}
