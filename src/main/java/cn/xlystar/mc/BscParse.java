package cn.xlystar.mc;

import cn.xlystar.parse.PancakeSwapDataProcess;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.odps.udf.UDF;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * 计算引擎：maxCompute
 * bsc_parse(String num1, String num2, int scale)
 * <p>
 * num1：数值1
 * num2：数值1
 * scale: 小数点位数
 *
 */
public class BscParse extends UDF {

    public BscParse() {
    }

    public String evaluate(String inputData, String from, String to, String value, String logs, String internalTxs, String hash) throws Exception {
        List<Map<String, String>> maps = PancakeSwapDataProcess.decodeInputData(inputData, from, to, value, logs, internalTxs, hash);
        return JSON.toJSONString(maps);
    }

    public static void main(String[] args) throws Exception {

    }
}
