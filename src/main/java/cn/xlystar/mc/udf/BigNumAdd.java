package cn.xlystar.mc.udf;

import com.aliyun.odps.udf.UDF;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 计算引擎：maxCompute
 * big_num_add(String num1, String num2, int scale)
 * <p>
 * num1：数值1
 * num2：数值1
 * scale: 小数点位数
 *
 */
public class BigNumAdd extends UDF {
    private final Logger log = LoggerFactory.getLogger(getClass());

    public BigNumAdd() {
    }

    public String evaluate(String num1, String num2, Integer scale) throws Exception {
        long startTime = System.currentTimeMillis();
        if (StringUtils.isEmpty(num1) || StringUtils.isEmpty(num2) ) return "0";
        BigDecimal b1 = new BigDecimal(num1);
        BigDecimal b2 = new BigDecimal(num2);
        String res = b1.setScale(scale, RoundingMode.HALF_UP).add(b2).toPlainString();
        System.out.println("num1:" + num1 + ", num2:" + num2 + ", res:" + res+", time:" + (System.currentTimeMillis() - startTime)+"ms");
        return res;
    }

    public static void main(String[] args) throws Exception {
        BigNumAdd bigNumAdd = new BigNumAdd();
        String evaluate = bigNumAdd.evaluate("10000000.00000000000000000000000000000000000000000000000000000001",
                "10000000.00000000000000000000000000000000000000000000000000000001",
                5);
        System.out.println(evaluate);
    }
}
