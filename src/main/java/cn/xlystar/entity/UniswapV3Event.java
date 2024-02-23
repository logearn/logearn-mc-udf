package cn.xlystar.entity;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigInteger;

@Data
@Builder
public class UniswapV3Event extends Event implements Serializable {

    private String sender;
    private String recipient;
    private String token0;
    private String token1;
    private BigInteger amount0;
    private BigInteger amount1;
    private BigInteger logIndex;
    private String contractAddress;
    private String protocol;
    private String version;

}
