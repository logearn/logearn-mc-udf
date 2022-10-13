package cn.xlystar;

import cn.xlystar.entity.AlarmMessage;
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

    private final String protocol = "https://";
    private final String URL = "/spider/retry_spider";

    public RequestRetrySpider() {
    }

    public String feiShuWebHook(AlarmMessage alarmMessage, String webHookUrl) {
        try {
            String webHookRequest = JSONObject.toJSONString(alarmMessage);
            log.info("[webHook request body] {}", webHookRequest);
            String resultJson = HttpClientUtil.postJSON(webHookUrl, webHookRequest);
            log.info("[webHook response body] {}", resultJson);
            JSONObject parse = JSONObject.parseObject(resultJson);

            String data = parse.get("data").toString();
            if ("true".equals(data)) {
                return data;
            } else {
                return resultJson;
            }
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            log.info("【HTTPS: feiShuWebHook】 -- 请求失败 error! request_body: { error: \"{}\", url: {} } ", sw, webHookUrl);
            return sw.toString();
        }
    }

    public String evaluate(String spiderId, List<Date> dates, String status, String domain, String webHookUrl) throws Exception {
        String domainUrl = protocol + domain + URL;
        // 计算请求时间
        long start = System.currentTimeMillis();

        // 1. 发起飞书 WebHook请求
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        AlarmMessage alarmMessage = AlarmMessage.builder()
                .degree("P0")
                .title("DataWorks 数据调度 Job CheckData Failed 数据检验失败")
                .occurrenceTime(format.format(new Date()))
                .occurrencePlace("DataWorks 调度系统 JOB -> dev_checkout_data_minute ")
                .describe(String.format("检测到数据缺失的时间区间: [%s, %s] \\n尝试重新采集的时间区间: [%s, %s]", format.format(dates.get(0)), format.format(dates.get(1)), format.format(dates.get(0)), format.format(dates.get(1))))
                .confirmButton("我已知悉")
                .confirmButtonUrl("https://workbench2-ap-southeast-1.data.aliyun.com/?defaultProjectId=33666&env=prod#/")
                .messageSource("来自 DataWorks P0 通知")
                .url("https://workbench2-ap-southeast-1.data.aliyun.com/?defaultProjectId=33666&env=prod#/")
                .build();
        String webHook_response = feiShuWebHook(alarmMessage, webHookUrl);
        if (!"true".equals(webHook_response)) {
            log.error(webHook_response);
        }

        try {
            HashMap<String, Object> requestMap = new HashMap<>();
            requestMap.put("spiderId", spiderId);
            requestMap.put("startBlockTime", dates.get(0));
            requestMap.put("endBlockTime", dates.get(1));
            requestMap.put("status", status);
            String requestJson = JSONObject.toJSONString(requestMap);
            log.info("[requestRetrySpider request body] {}", requestJson);
            String resultJson = HttpClientUtil.postJSON(domainUrl, requestJson);
            log.info("[requestRetrySpider response body] {}", resultJson);
            JSONObject parse = JSONObject.parseObject(resultJson);

            String code = parse.get("code").toString();
            if ("200".equals(code)) {
                return code;
            } else {
                String message = parse.get("message").toString();
                log.info(message);
                return message;
            }
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            log.info("【HTTPS: RequestRetrySpider】 -- 请求失败 error! request_body: { error: \"{}\", url: {} } ", sw.toString(), domainUrl);
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
        dates.add(format0.parse("2022-10-10 12:30:00"));
        System.out.println(checkoutDataReady.evaluate("22", dates, "PENDING","dev.kingdata.work:443","https://dev.kingdata.work:443/uniearn/warehouse/feishu/webhook/checkdataWebHook"));
    }
}
