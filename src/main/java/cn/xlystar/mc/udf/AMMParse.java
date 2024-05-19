package cn.xlystar.mc.udf;

import cn.xlystar.helpers.ChainConfig;
import cn.xlystar.helpers.ConfigHelper;
import cn.xlystar.parse.ammswap.AMMSwapDataProcess;
import com.alibaba.fastjson.JSON;
import com.aliyun.odps.udf.UDF;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 计算引擎：maxCompute
 * amm_parse(String chainId, String protocol, String logs, String internalTxs, String hash)
 * <p>
 * num1：数值1
 * num2：数值1
 * scale: 小数点位数
 */
public class AMMParse extends UDF {

    public AMMParse() {
    }

    public String evaluate(String originSender, String chainId, String protocol, String logs, String internalTxs, String hash) throws IOException {
        ChainConfig conf = new ConfigHelper().getConfig(chainId, protocol);
        List<Map<String, String>> maps = null;
        try {
            maps = AMMSwapDataProcess.decodeInputData(conf, "", originSender, "", "", logs, internalTxs, hash);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(String.format("conf:%s, logs:%s, internalTxs:%s, hash:%s", conf, logs, internalTxs, hash));
        } catch (StackOverflowError e) {
            e.printStackTrace();
            maps = new ArrayList<>();
            HashMap<String, String> errorSwap = new HashMap<>();
            errorSwap.put("errorMsg", "oom");
            errorSwap.put("chain", chainId);
            errorSwap.put("protocol", protocol);
            maps.add(errorSwap);
        }
        return JSON.toJSONString(maps);
    }

    public static void main(String[] args) throws Exception {

    }
}
