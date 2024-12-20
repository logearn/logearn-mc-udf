package cn.xlystar.mc.udf;

import cn.xlystar.parse.solSwap.SolParseData;
import com.aliyun.odps.udf.UDF;
import org.apache.commons.lang.StringUtils;
import org.bitcoinj.core.Base58;

import java.io.IOException;
import java.math.BigInteger;
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

    public String evaluate(String input, String protocol) throws IOException {
        if (StringUtils.isEmpty(input) || StringUtils.isEmpty(protocol)) return "";
        String result = "";
        if (protocol.equals("spl_token")) SolParseData.transfer(input);
        return result;
    }

    public static void main(String[] args) throws Exception {
        SolParse ammParse = new SolParse();
        String base58 = ammParse.evaluate("1B2QHX28YDABQZcX2tnnKVAzvTawWRfuwnFfNfcyRxPVug15f441AApFAavpqAKk8DrpaCC8pucneNcf", "spl_token");
        System.out.println(base58);
    }
}
