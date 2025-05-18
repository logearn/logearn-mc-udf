package cn.xlystar.parse.solSwap;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SolDexTransferMatchRule {
    RAYDIUM_CLMM(1, 2),           // Raydium CLMM规则：tr1 = index+1, tr2 = index+2
    ORCA_WHIRLPOOL(1, 2),         // Orca Whirlpool规则：tr1 = index+2, tr2 = index+3
    PUPMFUN_AMM(1, 2),         // Orca Whirlpool规则：tr1 = index+2, tr2 = index+3
    METEORA_DLMM(1, 2),
    METEORA_DLMM_V2(1, 2),
    METEORA_ALMM(0, 0),
    METEORA_DBC(1, 2),
    PHOENIX(1, 3),                // Phoenix规则：tr1 = index+1, tr2 = index+3
    RAYDIUM_CPMM_V1(1, 2),       // Raydium CPMM V1规则：tr1 = index+1, tr2 = index+2
    RAYDIUM_CPMM_V2(2, 3),       // Raydium CPMM V2规则：tr1 = index+2, tr2 = index+3
    RAYDIUM_AMM_V4(1, 2),       // Raydium CPMM V2规则：tr1 = index+1, tr2 = index+2
    RAYDIUM_LAUNCH(2, 3),       // Raydium CPMM V2规则：tr1 = index+1, tr2 = index+2
    MOOTSHOT(1, 2),       // Raydium CPMM V2规则：tr1 = index+1, tr2 = index+2
    BOOPFUN(1, 2),       // Raydium CPMM V2规则：tr1 = index+1, tr2 = index+2
    PUPMFUN(1, 2); // Pumpfun 规则：tr1 = index+1, tr2 = index+2

    private final int transfer1Offset;
    private final int transfer2Offset;
}