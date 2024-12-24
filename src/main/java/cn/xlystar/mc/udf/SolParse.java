package cn.xlystar.mc.udf;

import cn.xlystar.parse.solSwap.SolInstructionProcessor;
import com.aliyun.odps.udf.UDF;

import java.io.IOException;
import java.util.*;

/**
 * 计算引擎：maxCompute
 * base_coder(String input, String alg, String is_encode)
 * <p>
 * num1：数值1
 * num2：数值1
 * scale: 小数点位数
 */
public class SolParse extends UDF {

    public SolParse() {
    }

    public Map<String, Object> evaluate(String programId, String[] accounts, String data) throws IOException {
        return SolInstructionProcessor.processInstruction(programId, accounts, data);
    }

    public static void main(String[] args) throws Exception {
        SolParse ammParse = new SolParse();
    }
}
