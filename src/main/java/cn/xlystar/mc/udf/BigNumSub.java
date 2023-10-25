package cn.xlystar.mc.udf;

import com.aliyun.odps.udf.UDF;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 计算引擎：maxCompute
 * big_num_sub(String num1, String num2, int scale)
 * <p>
 * num1：数值1
 * num2：数值2
 * scale: 小数点位数
 */
public class BigNumSub extends UDF {
    private final Logger log = LoggerFactory.getLogger(getClass());

    public BigNumSub() {
    }

    public String evaluate(String num1, String num2, Integer scale) throws Exception {
        long startTime = System.currentTimeMillis();
        BigDecimal b1 = new BigDecimal(num1);
        BigDecimal b2 = new BigDecimal(num2);
        String res = b1.setScale(scale, RoundingMode.HALF_UP).subtract(b2).toPlainString();
        System.out.println("num1:" + num1 + ", num2:" + num2 + ", res:" + res+", time:" + (System.currentTimeMillis() - startTime)+"ms");
        return res;
    }

    public static void main(String[] args) throws Exception {
        BigNumSub bigNumAdd = new BigNumSub();
        String evaluate = bigNumAdd.evaluate("100000000.0000000000000000000000000000000000000000000000000000001",
                "10000000.00000000000000000000000000000000000000000000000000000001",
                5);
        System.out.println(evaluate);
    }
}
