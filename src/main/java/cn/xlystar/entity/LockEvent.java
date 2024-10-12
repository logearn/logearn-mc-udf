package cn.xlystar.entity;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class LockEvent extends Event implements Serializable {

    private String lockPlatform;
    private String poolAddress;
    private String lockCaller;
    private String lockAmount;

    private String lockDate;
    private String unlockDate;

    private Long lockTime;
    private String transactionHash;
    private String logIndex;
}
