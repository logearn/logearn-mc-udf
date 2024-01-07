package cn.xlystar.entity;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigInteger;

@Data
@Builder
public class TransferEvent extends Event implements Serializable {

    private String receiver;
    private String sender;
    private BigInteger amount;
    private String contractAddress;

}
