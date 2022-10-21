package cn.xlystar.udaf;

import com.aliyun.odps.io.DoubleWritable;
import com.aliyun.odps.io.Text;
import com.aliyun.odps.io.Writable;
import com.aliyun.odps.udf.Aggregator;
import com.aliyun.odps.udf.UDFException;
import com.aliyun.odps.udf.annotation.Resolve;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;


/**
 * 输入的列：type: string， amount: double ；
     * type：Buy  Sell  TransferIn  TransferOut
     * amount 统一为正数。目前不需要提前按 入还是出 的语义，处理为 正数或负数
 * 输出：tradeAccountAmount 交易子账户的金额
 */
// todo: double 改为 BigDecimalWritable
@Resolve("string,double->double")
public class AmountSum extends Aggregator {
    private final Logger log = LoggerFactory.getLogger(getClass());

    //实现Java类的方法。
    private static class AmountBuffer implements Writable {
        // 总账户的金额
        private double allAccountAmount = 0;
        // 交易子账户的金额
        private double tradeAccountAmount = 0;

        @Override
        public void write(DataOutput out) throws IOException {
            out.writeDouble(allAccountAmount);
            out.writeDouble(tradeAccountAmount);
        }

        @Override
        public void readFields(DataInput in) throws IOException {
            allAccountAmount = in.readDouble();
            tradeAccountAmount = in.readDouble();
        }
    }

    @Override
    public Writable newBuffer() {
        return new AmountBuffer();
    }

    /**
     * =IF(G2="Buy",I2,0)
     * <p>
     * =IF(G3="Buy",H2+I3,0) +
     * IF(G3="Sell",IF(H2+I3<0,0,H2+I3),0) +
     * IF(G3="Transfer",IF(J2-H2<-I3,J2+I3,H2), 0)
     * <p>
     * =IF(type="Buy",tradeAccountAmount+amount,0) +
     * IF(type="Sell",IF(tradeAccountAmount+amount<0,0,tradeAccountAmount+amount),0) +
     * IF(type="Transfer",IF(allAccountAmount-tradeAccountAmount<-amount,allAccountAmount+amount,tradeAccountAmount), 0)
     * <p>
     * 最终版 逻辑：
     * =IF(type="Buy",tradeAccountAmount+amount,0) +
     * IF(type="Sell",IF(tradeAccountAmount-amount<0,0,tradeAccountAmount-amount),0) +
     * IF(type="TransferIn", tradeAccountAmount) +
     * IF(type="TransferOut", IF(allAccountAmount-tradeAccountAmount<-amount,allAccountAmount+amount,tradeAccountAmount), 0)
     *
     * @param buffer AmountBuffer buffer
     * @param args   Text type
     *               DoubleWritable amount
     * @throws UDFException
     */
    @Override
    public void iterate(Writable buffer, Writable[] args) throws UDFException {
        String type = ((Text) args[0]).toString();
        DoubleWritable amount = (DoubleWritable) args[1];
        AmountBuffer buf = (AmountBuffer) buffer;
        if (amount != null) {
            if (type.equals("Buy")) {
                buf.tradeAccountAmount = buf.tradeAccountAmount + amount.get();
                buf.allAccountAmount += amount.get();
            }
            if (type.equals("Sell")) {
                buf.tradeAccountAmount = buf.tradeAccountAmount - amount.get() < 0 ? 0 : buf.tradeAccountAmount - amount.get();
                buf.allAccountAmount -= amount.get();
            }
            if (type.equals("TransferIn")) {
                buf.allAccountAmount += amount.get();
            }
            if (type.equals("TransferOut")) {
                buf.tradeAccountAmount = buf.allAccountAmount - buf.tradeAccountAmount < amount.get() ? buf.allAccountAmount - amount.get() : buf.tradeAccountAmount;
                buf.allAccountAmount -= amount.get();
            }
        }
        log.info("[iterate func]: allAccountAmount={}, tradeAccountAmount={}", buf.allAccountAmount, buf.tradeAccountAmount);
    }

    @Override
    public Writable terminate(Writable buffer) throws UDFException {
        AmountBuffer buf = (AmountBuffer) buffer;
        DoubleWritable ret = new DoubleWritable(buf.tradeAccountAmount);
        return ret;
    }

    @Override
    public void merge(Writable buffer, Writable partial) throws UDFException {
        AmountBuffer buf = (AmountBuffer) buffer;
        AmountBuffer p = (AmountBuffer) partial;
        buf.allAccountAmount += p.allAccountAmount;
        buf.tradeAccountAmount += p.tradeAccountAmount;
        log.info("[merge func]: allAccountAmount={}, tradeAccountAmount={}", buf.allAccountAmount, buf.tradeAccountAmount);
    }
}