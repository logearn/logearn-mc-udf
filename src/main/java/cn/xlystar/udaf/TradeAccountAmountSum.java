package cn.xlystar.udaf;

import cn.xlystar.utils.ArithmeticUtils;
import com.aliyun.odps.io.BigDecimalWritable;
import com.aliyun.odps.io.Text;
import com.aliyun.odps.io.Writable;
import com.aliyun.odps.udf.Aggregator;
import com.aliyun.odps.udf.UDFException;
import com.aliyun.odps.udf.annotation.Resolve;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;


/**
 * 输入：
 * type：事件类型
 * 正常有 Buy  Sell  TransferIn  TransferOut，这 4 种。
 * 特殊值：Base，这 1 种。作用：初始化内部状态：tradeAccountAmount、allAccountAmount
 * amount: 统一为正数。目前不需要提前按 入还是出 的语义，处理 正负
 * tradeAccountAmount：之前 交易子账户的数量
 * <p>
 * 输出：tradeAccountAmount 交易子账户的数量
 */
@Resolve("STRING,DECIMAL,DECIMAL->DECIMAL")
public class TradeAccountAmountSum extends Aggregator {
    private final Logger log = LoggerFactory.getLogger(getClass());

    // 内部数据状态
    private static class AmountBuffer implements Writable {
        // 总账户的金额
        private String allAccountAmount = "0";
        // 交易子账户的金额
        private String tradeAccountAmount = "0";

        @Override
        public void write(DataOutput out) throws IOException {
            out.writeUTF(allAccountAmount);
            out.writeUTF(tradeAccountAmount);
        }

        @Override
        public void readFields(DataInput in) throws IOException {
            allAccountAmount = in.readUTF();
            tradeAccountAmount = in.readUTF();
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
     *               BigDecimalWritable amount
     * @throws UDFException
     */
    @Override
    public void iterate(Writable buffer, Writable[] args) throws UDFException {
        String type = ((Text) args[0]).toString();
        BigDecimalWritable amount = (BigDecimalWritable) args[1];
        BigDecimalWritable tradeAccountAmount = (BigDecimalWritable) args[2];
        AmountBuffer buf = (AmountBuffer) buffer;
        if (amount != null) {
            if (type.equals("Base")) {
                buf.tradeAccountAmount = tradeAccountAmount.get().toString();
                buf.allAccountAmount = amount.get().toString();
            }
            if (type.equals("Buy")) {
                buf.tradeAccountAmount = ArithmeticUtils.add(buf.tradeAccountAmount, amount.get().toString()).toString();
                buf.allAccountAmount = ArithmeticUtils.add(buf.allAccountAmount, amount.get().toString()).toString();
            }
            if (type.equals("Sell")) {
                boolean isTradeAccountAmountNotEnough = ArithmeticUtils.compare(amount.get().toString(), buf.tradeAccountAmount);
                buf.tradeAccountAmount =
                        isTradeAccountAmountNotEnough ? "0" : ArithmeticUtils.sub(buf.tradeAccountAmount, amount.get().toString()).toString();
                buf.allAccountAmount = ArithmeticUtils.sub(buf.allAccountAmount, amount.get().toString()).toString();
            }
            if (type.equals("TransferIn")) {
                buf.allAccountAmount = ArithmeticUtils.add(buf.allAccountAmount, amount.get().toString()).toString();
            }
            if (type.equals("TransferOut")) {
                boolean isTransferAccountNotEnough = ArithmeticUtils.compare(amount.get().toString(), ArithmeticUtils.sub(buf.allAccountAmount, buf.tradeAccountAmount).toString());
                buf.tradeAccountAmount = isTransferAccountNotEnough ? ArithmeticUtils.sub(buf.allAccountAmount, amount.get().toString()).toString() : buf.tradeAccountAmount;
                buf.allAccountAmount = ArithmeticUtils.sub(buf.allAccountAmount, amount.get().toString()).toString();
            }
        }
        log.info("[iterate func]: allAccountAmount={}, tradeAccountAmount={}", buf.allAccountAmount, buf.tradeAccountAmount);
    }

    @Override
    public Writable terminate(Writable buffer) throws UDFException {
        AmountBuffer buf = (AmountBuffer) buffer;
        BigDecimalWritable ret = new BigDecimalWritable(new BigDecimal(buf.tradeAccountAmount));
        return ret;
    }

    @Override
    public void merge(Writable buffer, Writable partial) throws UDFException {
        AmountBuffer buf = (AmountBuffer) buffer;
        AmountBuffer p = (AmountBuffer) partial;
        buf.allAccountAmount = ArithmeticUtils.add(buf.allAccountAmount, p.allAccountAmount).toString();
        buf.tradeAccountAmount = ArithmeticUtils.add(buf.tradeAccountAmount, p.tradeAccountAmount).toString();
        log.info("[merge func]: allAccountAmount={}, tradeAccountAmount={}", buf.allAccountAmount, buf.tradeAccountAmount);
    }
}