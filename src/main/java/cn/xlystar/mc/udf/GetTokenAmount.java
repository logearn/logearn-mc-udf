package cn.xlystar.mc.udf;

import cn.xlystar.entity.SolanaCheckDataPO;
import cn.xlystar.utils.CustomException;
import cn.xlystar.utils.HttpClientUtil;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.odps.udf.UDF;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 计算引擎：maxCompute
 * get_token_amount(String wallet, String token_address, int decimals, String chain, String domain)
 *
 * sum：数仓该阶段的总数量
 * begin_time：开始时间
 * end_time: 结束时间
 * 时间[左闭右开)
 */
public class GetTokenAmount extends UDF {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final String protocol = "https://";
    private final String URL = "/logearn/warehouse/get_token_amount";
    public GetTokenAmount() {
    }

    public String evaluate(String wallet, String tokenAddress, Integer decimals, String chain, String domain) throws Exception {
        String domainUrl = protocol + domain + URL;
        // 1. 发起请求
        // 计算请求时间
        long start = System.currentTimeMillis();

        ArrayList<NameValuePair> request_body = new ArrayList<>();
        request_body.add(new BasicNameValuePair("tokenAddress", tokenAddress));
        request_body.add(new BasicNameValuePair("wallet", wallet));
        request_body.add(new BasicNameValuePair("chain", chain));

        int count = 0;
        String result = null;
        while (count++ < 3) {
            try {
                String result_json = HttpClientUtil.getRequest(domainUrl, request_body);
                JSONObject resultObject = JSONObject.parseObject(result_json);
                result = String.valueOf(resultObject.get("data"));
                log.info("success! " + (result.length() > 50 ? result.substring(0, 50) : result));
                break;
            } catch (Exception e) {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                e.printStackTrace(pw);
                result = sw.toString();
                log.info("RequestUrl -- error! request_body: { error: {}, contractAddress: {}, count: {} } ", result, tokenAddress, count);
            }
        }
        if (result.length() > 50) {
            return "999999999999999999";
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(1);
        for (int i = 0; i < decimals; i++) {
            stringBuffer.append("0");
        }
        log.info("cost time: {} s", (System.currentTimeMillis() - start) / 1000F);
        return new BigDecimal(result).divide(new BigDecimal(stringBuffer.toString()), 6, RoundingMode.UP).toString();
    }

    public static void main(String[] args) throws Exception {
        GetTokenAmount checkoutDataReady = new GetTokenAmount();
        System.out.println(checkoutDataReady.evaluate("0xdc97d09a5fc43cb18987ea9d09cc3bd49e23950b","0x9e9fbde7c7a83c43913bddc8779158f1368f0413", 18,"ETH", "logearn.com:443"));
    }
}
