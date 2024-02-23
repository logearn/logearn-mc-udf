package cn.xlystar.mc;

import cn.xlystar.helpers.ChainConfig;
import cn.xlystar.helpers.ConfigHelper;
import cn.xlystar.parse.ammswap.AMMSwapDataProcess;
import cn.xlystar.parse.ammswap.AddressTagProcess;
import com.alibaba.fastjson.JSON;
import com.aliyun.odps.udf.UDF;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 计算引擎：maxCompute
 * parse_address_tag(String num1, String num2, int scale)
 * <p>
 * num1：数值1
 * num2：数值1
 * scale: 小数点位数
 */
public class ParseAddressTag extends UDF {

    public ParseAddressTag() {
    }

    public String evaluate(String logs, String internalTxs, String hash, String chain, String protocol) throws IOException {
        ChainConfig conf = new ConfigHelper().getConfig(chain, protocol);
        Map<String, Set<String>> addressTagList = null;
        try {
            addressTagList = AddressTagProcess.getAddressTagList(conf, logs, internalTxs, hash);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(String.format("conf:%s, logs:%s, internalTxs:%s, hash:%s", conf, logs, internalTxs, hash));
        }
        return JSON.toJSONString(addressTagList);
    }

    public static void main(String[] args) throws Exception {

    }
}
