package cn.xlystar.mc.udf;

import com.aliyun.odps.udf.UDF;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 计算引擎：maxCompute
 * big_num_div(String num1, String num2, int scale)
 * <p>
 * num1：被除数
 * num2：除数
 * scale: 小数点位数
 *
 */
public class BigNumDiv extends UDF {
    private final Logger log = LoggerFactory.getLogger(getClass());

    public BigNumDiv() {
    }

    public String evaluate(String num1, String num2, Integer scale) throws Exception {
        BigDecimal b1 = new BigDecimal(num1);
        BigDecimal b2 = new BigDecimal(num2);
        return b1.divide(b2, RoundingMode.HALF_UP).setScale(scale, RoundingMode.HALF_UP).toString();
    }

    public static void main(String[] args) throws Exception {
        BigNumDiv bigNumAdd = new BigNumDiv();
        String evaluate = bigNumAdd.evaluate("10000000.00002000000000000000000000000000000000000000000000000001",
                "10000000.00000000000000000000000000000000000000000000000000000001",
                5);
        System.out.println(evaluate);
    }
}