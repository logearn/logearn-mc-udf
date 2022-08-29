package cn.xlystar;

import cn.xlystar.entity.SolanaCheckDataPO;
import cn.xlystar.utils.CustomException;
import cn.xlystar.utils.HttpClientUtil;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.odps.udf.UDF;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * checkout_data(Integer sum, String begin_time, String end_time)
 *
 * sum：数仓该阶段的总数量
 * begin_time：开始时间
 * end_time: 结束时间
 * 时间[左闭右开)
 */
public class CheckoutDataReady extends UDF {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private final String DEV_URL = "https://dev.kingdata.work:443/chain_spider/progress";
    public CheckoutDataReady() {
    }

    public Boolean evaluate(Long sum, List<String> pidlists, String begin_time, String end_time) throws Exception {
        // 1. 发起请求
        // 计算请求时间
        long start = System.currentTimeMillis();

        int result = -1;
        try {
            SolanaCheckDataPO solanaCheckDataPO = new SolanaCheckDataPO();
            solanaCheckDataPO.setBeginTime(begin_time);
            solanaCheckDataPO.setEndTime(end_time);
            solanaCheckDataPO.setPids(pidlists);
            String result_json = HttpClientUtil.postJSON(DEV_URL, JSONObject.toJSONString(solanaCheckDataPO));
            JSONObject parse = JSONObject.parseObject(result_json);
            result = parse.getInteger("data");

            log.info("数仓数据总量【SUM】 : {} ", sum);
            log.info("请求参数：{} ", solanaCheckDataPO);
            log.info("【HTTPS: CheckoutDataReady】 -- 请求成功 success! 采集服务数据总量 【SUM】: {} ", result);
        } catch (Exception e) {
            log.info("【HTTPS: CheckoutDataReady】 -- 请求失败 error! request_body: { error: \"{}\", url: {} } ", e.getMessage(), DEV_URL);
        }

        if(sum != result) {
            CustomException ex = new CustomException("数据检验: 数据未处于 Ready 状态");
            throw ex;
        }

        log.info("cost time: {} s", (System.currentTimeMillis() - start) / 1000F);
        return true;
    }

    public static void main(String[] args) throws Exception {
        CheckoutDataReady checkoutDataReady = new CheckoutDataReady();
        System.out.println(checkoutDataReady.evaluate(7381L,null,"2022-08-12 18:35:00", "2022-08-12 20:51:00"));
    }
}
