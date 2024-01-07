package cn.xlystar.entity;

import java.math.BigInteger;

public class Tx {
    BigInteger value;
    String from;
    String to;

    public Tx(BigInteger value, String from, String to) {
        this.value = value;
        this.from = from;
        this.to = to;
    }

    public String getFrom() {
        return this.from;
    }

    public String getTo() {
        return this.to;
    }

    public BigInteger getValue() {
        return this.value;
    }
}
