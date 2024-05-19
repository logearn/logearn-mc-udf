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
 */
public class EthParse extends UDF {

    public EthParse() {
    }

    public String evaluate(String originSender, String logs, String internalTxs, String hash) throws IOException {
        ChainConfig conf = new ConfigHelper().getConfig("1", "uniswap");
        List<Map<String, String>> maps = null;
        try {
            maps = AMMSwapDataProcess.decodeInputData(conf, "", originSender, "", "", logs, internalTxs, hash);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(String.format("conf:%s, logs:%s, internalTxs:%s, hash:%s", conf, logs, internalTxs, hash));
        }
        return JSON.toJSONString(maps);
    }

    public static void main(String[] args) throws Exception {

    }
}
