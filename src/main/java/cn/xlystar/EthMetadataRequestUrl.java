package cn.xlystar;


import cn.xlystar.entity.ResponseEntity;
import cn.xlystar.utils.HttpClientUtil;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.odps.udf.UDF;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * eth_metadata_request_url(String url)
 * url：要访问的url地址
 */
public class EthMetadataRequestUrl extends UDF {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private Cache<String, String> loadingCache;
    private int retryCount = 3;

    //    private String DEV_URL = "https://dev.kingdata.work:443/parse";
    private String DEV_URL = "https://dev.kingdata.work:443/uniearn/warehouse/eth_metadata_request";

    public EthMetadataRequestUrl() {
        loadingCache = Caffeine.newBuilder()
                .recordStats()
                //cache的初始容量
                .initialCapacity(4096)
                //cache最大缓存数
                .maximumSize(10000)
                .build();
    }

    public String evaluate(String contractAddress, String tokenId) {
        // 1. 查询缓存
        String result = loadingCache.getIfPresent(contractAddress + tokenId);
        if (result != null) {
            log.info("RequestUrl -- Hit Cache: 命中率: {}, 被驱逐的缓存数量: {}, 指标：{}, result: {}",
                    loadingCache.stats().hitRate(),
                    loadingCache.stats().evictionCount(),
                    loadingCache.stats(),
                    result);
            return result;
        }
        // 2. 发起请求
        ArrayList<NameValuePair> request_body = new ArrayList<>();
        request_body.add(new BasicNameValuePair("contractAddress", contractAddress));
        request_body.add(new BasicNameValuePair("tokenId", tokenId));
        // 需要返回实体
        ResponseEntity res = new ResponseEntity();
        // 计算请求时间
        long start = System.currentTimeMillis();
        int count = 0;
        while (count++ < retryCount) {
            try {
                String result_json = HttpClientUtil.getRequest(DEV_URL, request_body);
                JSONObject resultObject = JSONObject.parseObject(result_json);
                result = String.valueOf(resultObject.get("data"));
                log.info("success! " + (result.length() > 50 ? result.substring(0, 50) : result));
                break;
            } catch (Exception e) {
                result = e.getMessage();
                log.info("RequestUrl -- error! request_body: { error: {}, contractAddress: {}, tokenId{},count: {} } ", e.getMessage(), contractAddress, tokenId, count);
            }
        }

        loadingCache.put(contractAddress + tokenId, result);
        log.info("Write Cache: 命中率: {}, 被驱逐的缓存数量: {}, 指标：{}",
                loadingCache.stats().hitRate(),
                loadingCache.stats().evictionCount(),
                loadingCache.stats());
        log.info("cost time: {} s", (System.currentTimeMillis() - start) / 1000F);
        return result;
    }

    public static void main(String[] args) {
        EthMetadataRequestUrl requestUrl = new EthMetadataRequestUrl();
        for (int i = 0; i < 3; i++) {
            System.out.println(requestUrl.evaluate("0x0da18e368271915c87935f4d83fea00953cfa2b1", "5451"));
        }
    }
}
