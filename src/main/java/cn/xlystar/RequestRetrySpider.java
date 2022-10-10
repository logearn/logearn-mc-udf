package cn.xlystar;

import cn.xlystar.utils.HttpClientUtil;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.odps.udf.UDF;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * chekcdata 无法通过后，将发送请求，进行 task 的重新爬取
 * request_retry_spider(String spiderId, List<Date> dates, String status)
 * <p>
 * spiderId：爬虫spider id
 * dates：开始时间，结束时间
 * status: 期望 task 修改成的状态
 * 时间[左闭右开)
 */
public class RequestRetrySpider extends UDF {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private final String DEV_URL = "https://dev.kingdata.work:443/chain_spider/progress";

    public RequestRetrySpider() {
    }

    public String evaluate(String spiderId, List<Date> dates, String status) throws Exception {
        // 1. 发起请求
        // 计算请求时间
        long start = System.currentTimeMillis();

        try {
            HashMap<String, Object> requestMap = new HashMap<>();
            requestMap.put("spiderId", spiderId);
            requestMap.put("startBlockTime", dates.get(0));
            requestMap.put("endBlockTime", dates.get(1));
            requestMap.put("status", status);
            String requestJson = JSONObject.toJSONString(requestMap);
            System.out.println("request_json: " + requestJson);
            String result_json = HttpClientUtil.postJSON(DEV_URL, requestJson);
            System.out.println("respone_json: " + result_json);
            JSONObject parse = JSONObject.parseObject(result_json);

            String code = parse.get("code").toString();
            if ("200".equals(code)) {
                return code;
            } else {
                String message = parse.get("message").toString();
                System.out.println(message);
                return message;
            }
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            log.info("【HTTPS: RequestRetrySpider】 -- 请求失败 error! request_body: { error: \"{}\", url: {} } ", sw.toString(), DEV_URL);
            return "请求失败: " + sw;
        } finally {
            log.info("cost time: {} s", (System.currentTimeMillis() - start) / 1000F);
        }
    }

    public static void main(String[] args) throws Exception {
        RequestRetrySpider checkoutDataReady = new RequestRetrySpider();
        List<Date> dates = new ArrayList<>();
        SimpleDateFormat format0 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dates.add(format0.parse("2022-10-10 12:30:00"));
        dates.add(format0.parse("2022-10-10 12:31:00"));
        System.out.println(dates);
        System.out.println(checkoutDataReady.evaluate("22", dates, "PENDING"));
    }
}
