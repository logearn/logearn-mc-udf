package cn.xlystar;

import cn.xlystar.entity.ParseEntity;
import cn.xlystar.utils.HttpClientUtil;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.odps.udf.UDF;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * parse_data(String program_account, String data)
 * program_account：program_account 地址
 * data: 要解析的数据
 */
public class SolanaParse extends UDF {
    private String BASE58 = "base58";
    private String HEX = "hex";
    private String URL = "/parse";
    private final String protocol = "https://";
    private final Logger log = LoggerFactory.getLogger(getClass());

    private Cache<String, String> loadingCache;

    public SolanaParse() {
        loadingCache = Caffeine.newBuilder()
                .recordStats()
                //cache的初始容量
                .initialCapacity(100000)
                //cache最大缓存数
                .maximumSize(100000)
                .build();
    }

    public String evaluate(String program_account, String data, String domain) {
        String domainUrl = protocol + domain + URL;
        // 1. 查询缓存
        String result = loadingCache.getIfPresent(program_account + data);
        if (result != null) {
            log.info("Hit Cache: 命中率: {}, 被驱逐的缓存数量: {}, 指标：{}, result: {}",
                    loadingCache.stats().hitRate(),
                    loadingCache.stats().evictionCount(),
                    loadingCache.stats(),
                    result);
            return result;
        }
        // 2. 发起请求
        Map<String, String> map = new HashMap<>();
        map.put("encoding", BASE58);
        map.put("programid", program_account);
        map.put("data", data);
        Gson gson = new Gson();
        String request_body = gson.toJson(map, new TypeToken<Map<String, String>>() {
        }.getType());

        // 需要返回实体
        ParseEntity parseEntity = new ParseEntity();
        // 计算请求时间
        long start = System.currentTimeMillis();

        try {
            String result_json = HttpClientUtil.postJSON(domainUrl, request_body);
            JSONObject resultObject = JSONObject.parseObject(result_json);
            parseEntity.setInstruction_name(String.valueOf(((JSONObject) resultObject.get("data")).get("command")));
            parseEntity.setInstruction_parameters(String.valueOf(((JSONObject) resultObject.get("data")).get("body")));

            log.info("success! " + resultObject);
        } catch (Exception e) {
            parseEntity.setInstruction_name("解析失败");
            parseEntity.setInstruction_parameters(e.getMessage());

            log.info("error! request_body: { error: \"{}\", program_account: \"{}\", data: {} } ", e.getMessage(), program_account, data);
        }

        result = JSONObject.toJSONString(parseEntity);
        loadingCache.put(program_account + data, result);
        log.info("Write Cache: 命中率: {}, 被驱逐的缓存数量: {}, 指标：{}",
                loadingCache.stats().hitRate(),
                loadingCache.stats().evictionCount(),
                loadingCache.stats());
        log.info("cost time: {} s", (System.currentTimeMillis() - start) / 1000F);
        return result;
    }

//    public static void main(String[] args) {
//        SolanaParse solana_parse = new SolanaParse();
//        for (int i = 0; i < 5; i++) {
//            System.out.println(solana_parse.evaluate("metaqbxxUerdq28cj1RbAWkYQm3ybzjb6a8bt518x1s", "1Ajt59NQPCA4H6Ed4kLfLF8WRwQoVjnwh3vbzBJxtoLfujKChSAZtNBPBoqJvKm3YVdDWgSnfrEaeAPoXbPArEufw63d36PyL76hVW5uRtKTT3whnSE68RHvLhmLkJu2Pn3jvXwmF2yLnLVaUBWjEjwRgEqDZFH49zGGgHH39MqmcN6gtQMKySkSVV6MvRNFgdpUFA9TswxYiMn"));
//        }
////        for (int i = 0; i < 6; i++) {
////            System.out.println(solana_parse.evaluate("metaqbxxUerdq28cj1RbAWkYQm3ybzjb6a8bt518x1s", "1Ajt59NQPCA4H6Ed4kLfLF8WRwQoVjnwh3vbzBJxtoLfujKChSAZtNBPBoqJvKm3YVdDWgSnfrEaeAPoXbPArEufw63d36PyL76hVW5uRtKTT3whnSE68RHvLhmLkJu2Pn3jvXwmF2yLnLVaUBWjEjwRgEqDZFH49zGGgHH39MqmcN6gtQMKySkSVV6MvRNFgdpUFA9TswxYiMn" + i));
////        }
////        for (int i = 0; i < 5; i++) {
////            System.out.println(solana_parse.evaluate("metaqbxxUerdq28cj1RbAWkYQm3ybzjb6a8bt518x1s", "1Ajt59NQPCA4H6Ed4kLfLF8WRwQoVjnwh3vbzBJxtoLfujKChSAZtNBPBoqJvKm3YVdDWgSnfrEaeAPoXbPArEufw63d36PyL76hVW5uRtKTT3whnSE68RHvLhmLkJu2Pn3jvXwmF2yLnLVaUBWjEjwRgEqDZFH49zGGgHH39MqmcN6gtQMKySkSVV6MvRNFgdpUFA9TswxYiMn" + i));
////        }
////        System.out.println(solana_parse.evaluate("metaplexMetadata", "1LkA5fhKvB7XqtkhvsGc4JB7rLt1PCrDzZq5w47zdJ91tok34mbjUjbxzJT6FPJsU7V8ZaNbrcXKPATv38roh8qLD1789FZAZ7VsZkUN6uacHocDPz7sHgAuLAoJPDjXE7DfAjxz6jci3qNBpvkCeQuWAJ4nFCmBAvUsrwdaywbDAUv2T3v51WZ7ayDkFq8NqVXsvA1RbtEpg8bB2KX6fNbpWv1jkc4jXYZRXWJLSGbfRoKiX4inHPeFo47rtegz74zks92zBNqbmfZ2HSg6WJkwdKgbHyo5JKBi1oGwkkzXNeaJXF9ByNHHpHGUXsGNi8Cq75Npz7qy3CaNgY1LkA5fhKvB7XqtkhvsGc4JB7rLt1PCrDzZq5w47zdJ91tok34mbjUjbxzJT6FPJsU7V8ZaNbrcXKPATv38roh8qLD1789FZAZ7VsZkUN6uacHocDPz7sHgAuLAoJPDjXE7DfAjxz6jci3qNBpvkCeQuWAJ4nFCmBAvUsrwdaywbDAUv2T3v51WZ7ayDkFq8NqVXsvA1RbtEpg8bB2KX6fNbpWv1jkc4jXYZRXWJLSGbfRoKiX4inHPeFo47rtegz74zks92zBNqbmfZ2HSg6WJkwdKgbHyo5JKBi1oGwkkzXNeaJXF9ByNHHpHGUXsGNi8Cq75Npz7qy3CaNgY"));
//    }
//

}