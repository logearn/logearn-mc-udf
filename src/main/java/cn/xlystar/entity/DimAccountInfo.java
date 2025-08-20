package cn.xlystar.entity;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigInteger;


@Data
@Accessors(chain = true)
@Builder
public class DimAccountInfo {

    private String account;
    private String owner;
    private String mint;
    private BigInteger tokenAmount;
    private BigInteger solAmount;
    private BigInteger solBalance;

    private Long lastBlockId;
    private Integer lastTxTime;

    private Long earliestBlockId;
    private Integer earliestTxTime;




}


