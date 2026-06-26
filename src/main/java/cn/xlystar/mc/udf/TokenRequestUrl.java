package cn.xlystar.mc.udf;

import cn.xlystar.utils.HttpClientUtil;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.odps.udf.UDF;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 计算引擎：maxCompute
 * token_request(String tokenAddress, String chain, String url)
 * url：要访问的url地址
 */
public class TokenRequestUrl extends UDF {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private int retryCount = 2;

    //    private String DEV_URL = "https://dev.kingdata.work:443/parse";
    private final String protocol = "https://";
    private String URL = "/logearn/proxy/token/request";

    public TokenRequestUrl() {
    }

    public String evaluate(String tokenAddress, String chain, String domain, String addressList) {
        String domainUrl = protocol + domain + URL;
        // 1. 查询缓存
        String result = null;
        // 2. 发起请求
        ArrayList<NameValuePair> request_body = new ArrayList<>();
        HashMap<Object, Object> params = new HashMap<>();
        params.put("tokenList", tokenAddress);
        params.put("chain", chain);
        params.put("addressList", addressList);
        HashMap<Object, Object> req = new HashMap<>();
        req.put("params", params);
        // 计算请求时间
        long start = System.currentTimeMillis();
        int count = 0;
        while (count++ < retryCount) {
            try {
                String result_json = HttpClientUtil.postJSON(domainUrl, JSONObject.toJSONString(req));
                JSONObject resultObject = JSONObject.parseObject(result_json);
                result = ((List<Object>) resultObject.get("data")).stream().map(String::valueOf)
                        .collect(Collectors.joining(","));
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

        log.info("cost time: {} s", (System.currentTimeMillis() - start) / 1000F);
        return result;
    }

    public static void main(String[] args) {
        TokenRequestUrl requestUrl = new TokenRequestUrl();
        for (int i = 0; i < 3; i++) {
            System.out.println(requestUrl.evaluate("0xd0d5ff4c23f294f0b2f629dc1e2c57b09c374444,0xd0d5ff4c23f294f0b2f629dc1e2c57b09c374444,0xd0d5ff4c23f294f0b2f629dc1e2c57b09c374444", "56","logearn.com:443", "0x126af0aAFdBfCc0B233d1A9aF32111E6ad274ce4,0x6990E7E90ab50C12111f99b84183D3fE298Bb3e4,0x07342C439003fC49Fdc19baEC03CDB773Eb1C8A0"));
        }
    }
}
