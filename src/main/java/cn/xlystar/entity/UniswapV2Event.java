package cn.xlystar.entity;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigInteger;

@Data
@Builder
public class UniswapV2Event extends Event implements Serializable {

    private String sender;
    private String to;
    private String token0;
    private String token1;
    private BigInteger amount0In;
    private BigInteger amount1In;
    private BigInteger amount0Out;
    private BigInteger amount1Out;
    private String contractAddress;
    private String protocol;
    private String version;

}
