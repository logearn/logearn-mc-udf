package cn.xlystar;


import cn.xlystar.entity.ResponseEntity;
import cn.xlystar.utils.HttpClientUtil;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.odps.udf.UDF;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * request_url(String url)
 * url：要访问的url地址
 */
public class RequestUrl extends UDF {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private Cache<String, String> loadingCache;

    public RequestUrl() {
        loadingCache = Caffeine.newBuilder()
                .recordStats()
                //cache的初始容量
                .initialCapacity(10000)
                //cache最大缓存数
                .maximumSize(10000)
                .build();
    }

    public String evaluate(String url) {
        // 1. 查询缓存
        String result = loadingCache.getIfPresent(url);
        if (result != null) {
            log.info("RequestUrl -- Hit Cache: 命中率: {}, 被驱逐的缓存数量: {}, 指标：{}, result: {}",
                    loadingCache.stats().hitRate(),
                    loadingCache.stats().evictionCount(),
                    loadingCache.stats(),
                    result);
            return result;
        }
        // 2. 发起请求
        // 需要返回实体
        ResponseEntity res = new ResponseEntity();
        // 计算请求时间
        long start = System.currentTimeMillis();

        try {
            String result_json = HttpClientUtil.getRequest(url, new ArrayList<>());
            res.setSuccess(result_json);
            log.info("success! " + result_json);
        } catch (Exception e) {
            res.setError(e.getMessage());
            log.info("RequestUrl -- error! request_body: { error: \"{}\", url: {} } ", e.getMessage(), url);
        }

        result = JSONObject.toJSONString(res);
        loadingCache.put(url, result);
        log.info("Write Cache: 命中率: {}, 被驱逐的缓存数量: {}, 指标：{}",
                loadingCache.stats().hitRate(),
                loadingCache.stats().evictionCount(),
                loadingCache.stats());
        log.info("cost time: {} s", (System.currentTimeMillis() - start) / 1000F);
        return result;
    }

//    public static void main(String[] args) {
//        RequestUrl requestUrl = new RequestUrl();
//        for (int i = 0; i < 3; i++) {
//            System.out.println(requestUrl.evaluate("https://ipfs.walken.io/ipfs/QmVG5FVj9W3jwA53DPbCyjSbH85MnAfvPDrbzADsvsqsD1"));
//        }
//    }
}
