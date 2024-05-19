package cn.xlystar.mc;

import cn.xlystar.helpers.ChainConfig;
import cn.xlystar.helpers.ConfigHelper;
import cn.xlystar.parse.ammswap.AMMSwapDataProcess;
import com.alibaba.fastjson.JSON;
import com.aliyun.odps.udf.UDF;

import java.io.IOException;
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

    public String evaluate(String originSender, String logs, String internalTxs, String hash) throws IOException {
        ChainConfig conf = new ConfigHelper().getConfig("56", "pancake");
        List<Map<String, String>> maps = AMMSwapDataProcess.decodeInputData(conf, "", originSender, "", "", logs, internalTxs, hash);
        return JSON.toJSONString(maps);
    }

    public static void main(String[] args) throws Exception {

    }
}
