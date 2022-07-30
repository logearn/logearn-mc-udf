package cn.xlystar.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class ParseEntity implements Serializable {

    private String instruction_name;
    private String instruction_parameters;

    @Override
    public String toString() {
        return "ParseEntity{" +
                "instruction_name='" + instruction_name + '\'' +
                ", instruction_parameters='" + instruction_parameters + '\'' +
                '}';
    }
}
