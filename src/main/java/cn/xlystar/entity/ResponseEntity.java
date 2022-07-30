package cn.xlystar.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class ResponseEntity implements Serializable {
    private String error;
    private String success;

    @Override
    public String toString() {
        return "ResponseEntity{" +
                "error='" + error + '\'' +
                ", success='" + success + '\'' +
                '}';
    }
}
