package cn.xlystar.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SolanaCheckDataPO implements Serializable {

    private List<String> pids;

    private String beginTime;

    private String endTime;


}
