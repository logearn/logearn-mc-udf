package cn.xlystar.entity;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
@Builder
public class PumpFunTokenPool {


    private String tokenAddress;
    private String poolAddress;
    private String token0;
    private String token1;
    private String version;
    private String initOwner;



    private String liquidity;
    private String price;
    private String amount0;
    private String amount1;
    private String blockTime;
    private String vault0;
    private String vault1;

    private String amountChange0;
    private String amountChange1;


}


