package cn.xlystar.entity;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigInteger;

@Data
@Builder
public class UniswapEvent extends Event implements Serializable {

    private String sender;
    private String to;
    private String tokenIn;
    private String tokenOut;
    private BigInteger amountIn;
    private BigInteger amountOut;
    private String contractAddress;
    private String protocol;
    private String version;

}
