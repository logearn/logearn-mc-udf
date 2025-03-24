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
        List<Map<String, String>> maps = new ArrayList<>();
        String res = "";
        if (logs.length() >= 1048576 || internalTxs.length() >= 1048576) return res;
        try {
            maps = AMMSwapDataProcess.decodeInputData(conf, "", originSender, "", "", logs, internalTxs, hash);
            res = JSON.toJSONString(maps);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(String.format("msg:%s, hash:%s. logs:%s, internalTxs:%s, conf:%s, ", e.getMessage(), hash, logs, internalTxs, conf));
        } catch (StackOverflowError | OutOfMemoryError e) {
            e.printStackTrace();
            maps = new ArrayList<>();
            HashMap<String, String> errorSwap = new HashMap<>();
            errorSwap.put("errorMsg", "oom");
            errorSwap.put("chain", chainId);
            errorSwap.put("protocol", protocol);
            maps.add(errorSwap);
            throw new RuntimeException(String.format("msg:%s, hash:%s. logs:%s, internalTxs:%s, conf:%s ", e.getMessage(), hash, logs, internalTxs, conf));
        }
        return res;
    }

    public static void main(String[] args) throws Exception {
        AMMParse ammParse = new AMMParse();
        ammParse.evaluate("0x812ba41e071c7b7fa4ebcfb62df5f45f6fa853ee", "1", "uniswap"
                , "", "", "0x29516fb5f6699286a76545e4815dac6e10da0d3fe492ae661dcadc67aecaf673");
    }
}
