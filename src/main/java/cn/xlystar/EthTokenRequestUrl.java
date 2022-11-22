package cn.xlystar;

import cn.xlystar.utils.HttpClientUtil;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.odps.udf.UDF;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

/**
 * eth_metadata_request_url(String url)
 * url：要访问的url地址
 */
public class EthTokenRequestUrl extends UDF {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private int retryCount = 3;

    //    private String DEV_URL = "https://dev.kingdata.work:443/parse";
    private final String protocol = "https://";
    private String URL = "/uniearn/warehouse/eth_token_request";

    public EthTokenRequestUrl() {
    }

    public String evaluate(String tokenAddress, String domain) {
        String domainUrl = protocol + domain + URL;
        // 1. 查询缓存
        String result = null;
        // 2. 发起请求
        ArrayList<NameValuePair> request_body = new ArrayList<>();
        request_body.add(new BasicNameValuePair("tokenAddress", tokenAddress));
        // 计算请求时间
        long start = System.currentTimeMillis();
        int count = 0;
        while (count++ < retryCount) {
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

        log.info("cost time: {} s", (System.currentTimeMillis() - start) / 1000F);
        return result;
    }

    public static void main(String[] args) {
        EthTokenRequestUrl requestUrl = new EthTokenRequestUrl();
        for (int i = 0; i < 3; i++) {
            System.out.println(requestUrl.evaluate("0xA0b86991c6218b36c1d19D4a2e9Eb0cE3606eB48", "uniearn.info:443"));
        }
    }
}
