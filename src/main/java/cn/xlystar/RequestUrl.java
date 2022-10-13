package cn.xlystar;


import cn.xlystar.utils.HttpClientUtil;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.odps.udf.UDF;
import com.github.benmanes.caffeine.cache.Cache;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

/**
 * request_url(String url)
 * url：要访问的url地址
 */
public class RequestUrl extends UDF {
    private final Logger log = LoggerFactory.getLogger(getClass());

    // https://dev.kingdata.work:443
    private final String protocol = "https://";
    private String URL = "/uniearn/warehouse/request";

    public RequestUrl() {
    }

    public String evaluate(String url, String domain) {
        String domainUrl = protocol + domain + URL;
        // 1. 查询缓存
        String result = null;
        // 2. 发起请求
        ArrayList<NameValuePair> request_body = new ArrayList<>();
        request_body.add(new BasicNameValuePair("url", url));
        // 计算请求时间
        long start = System.currentTimeMillis();

        String result_json = null;
        try {
            result_json = HttpClientUtil.getRequest(domainUrl, request_body);
            JSONObject resultObject = JSONObject.parseObject(result_json);
            String error = String.valueOf(((JSONObject) resultObject.get("data")).get("error"));
            if (error == null || "null".equals(error)) {
                result = String.valueOf(((JSONObject) resultObject.get("data")).get("success"));
                log.info("success! " + (result.length() > 50 ? result.substring(0, 50) : result));
            } else {
                result = resultObject.get("data").toString();
                log.error(result);
            }
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            result = sw.toString();
            log.info("【HTTPS: RequestRetrySpider】 -- 请求失败 error! request_body: { error: \"{}\", url: {} } ", sw, domainUrl);
        }

        log.info("cost time: {} s", (System.currentTimeMillis() - start) / 1000F);
        return result;
    }

    public static void main(String[] args) throws Exception {
        RequestUrl requestUrl = new RequestUrl();
        for (int i = 0; i < 3; i++) {
            System.out.println(requestUrl.evaluate("https://ipfs.walken.io/ipfs/QmVG5FVj9W3jwA53DPbCyjSbH85MnAfvPDrbzADsvsqsD1", "https://uniearn.info:443"));
        }
    }
}
