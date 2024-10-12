package cn.xlystar.entity;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class RenounceEvent extends Event implements Serializable {

    private String tokenAddress;

    private String previousOwner;
    private String newOwner;
    private Long renounceTime;
    private String transactionHash;
    private String logIndex;
}
