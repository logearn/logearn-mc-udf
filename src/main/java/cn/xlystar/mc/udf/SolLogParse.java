package cn.xlystar.mc.udf;

import cn.xlystar.parse.solSwap.SolInstructionProcessor;
import com.alibaba.fastjson.JSON;
import com.aliyun.odps.udf.UDF;

import java.io.IOException;
import java.util.List;

/**
 * 计算引擎：maxCompute
 * sol_log_parse(String programId, String logData)
 * <p>
 * num1：数值1
 * num2：数值1
 * scale: 小数点位数
 */
public class SolLogParse extends UDF {

    public SolLogParse() {
    }

    public String evaluate(String programId, String logData) throws IOException {
        return JSON.toJSONString(SolInstructionProcessor.processLogs(programId, logData));
    }

    public static void main(String[] args) throws Exception {
        SolLogParse ammParse = new SolLogParse();
        String programId = "MoonCVVNZFSYkqNXP6bxHLPL6QQJiMagDL3qcqUQTrG";
        String logData = "vdt/007mYe4A+gZv1E4NAICEPAcAAAAAdx0LAAAAAAD6aAcAAAAAAKhwAZm60QgBQok4nBtVgCp8oLp3bN62oyuOa8Kb/mRVSQrrR5n2XW4Gm4hX/quBhPtof2NGGMA12sQ53BrrO1WYoPAAAAAAAY2fGnHwVs/If3+0/eRnZxJ7J7xZgXGpW/2npoQ4hyWgAQUAAAB0cmFkZQ==";
        System.out.println(ammParse.evaluate(programId, logData));;
    }
}
