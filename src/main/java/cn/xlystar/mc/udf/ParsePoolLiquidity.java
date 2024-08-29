package cn.xlystar.mc.udf;

import cn.xlystar.entity.LiquidityEvent;
import cn.xlystar.helpers.ChainConfig;
import cn.xlystar.helpers.ConfigHelper;
import cn.xlystar.parse.ammswap.AMMLiquidityDataProcess;
import com.alibaba.fastjson.JSON;
import com.aliyun.odps.udf.UDF;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 计算引擎：maxCompute
 * amm_parse_liquidity
 * <p>
 * num1：数值1
 * num2：数值1
 * scale: 小数点位数
 */
public class ParsePoolLiquidity extends UDF {

    public ParsePoolLiquidity() {
    }

    public String evaluate(String from, String chain, String protocol, String logs, String internalTxs, String hash) throws IOException {
        ChainConfig conf = new ConfigHelper().getConfig(chain, protocol);
        List<LiquidityEvent> liquidityList = new ArrayList<>();
        try {
            liquidityList = AMMLiquidityDataProcess.parseFullLiquidity(from,  conf, logs, internalTxs, hash);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(String.format("conf:%s, logs:%s, internalTxs:%s, hash:%s", conf, logs, internalTxs, hash));
        }
        return JSON.toJSONString(liquidityList);
    }

    public static void main(String[] args) throws Exception {
    }
}
