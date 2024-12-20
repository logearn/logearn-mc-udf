package cn.xlystar.mc.udf;

import com.aliyun.odps.udf.UDF;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 计算引擎：maxCompute
 * compare(String num1, int abs1, String num2, int abs2)
 * <p>
 * num1：数值1
 * num2：数值1
 * abs: 1 表示取绝对值
 *
 */
public class Compare extends UDF {
    private final Logger log = LoggerFactory.getLogger(getClass());

    public Compare() {
    }

    public Integer evaluate(String num1, Integer abs1, String num2, Integer abs2) throws Exception {
        BigDecimal b1 = StringUtils.isEmpty(num1) ? BigDecimal.ZERO : new BigDecimal(num1);
        b1 = abs1 > 0 ? b1.abs() : b1;
        BigDecimal b2 = StringUtils.isEmpty(num2) ? BigDecimal.ZERO : new BigDecimal(num2);
        b2 = abs2 > 0 ? b2.abs() : b2;
        return b1.compareTo(b2);
    }


}
