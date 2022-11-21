package cn.xlystar.udaf;

import cn.xlystar.utils.ArithmeticUtils;
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
 * 输入：
 * type：事件类型
 * 正常有 Buy  Sell  TransferIn  TransferOut，这 4 种。
 * 特殊值：Base，这 1 种。作用：初始化内部状态：tradeAccountAmount、allAccountAmount
 *
 * amount: 统一为正数。目前不需要提前按 入还是出 的语义，处理 正负
 *
 * tradeAccountAmount：之前 交易子账户的数量
 * <p>
 * 输出：tradeAccountAmount 交易子账户的数量
 */
@Resolve("STRING,STRING,STRING,STRING->STRING")
public class TradeAccountAmountSum2 extends Aggregator {
    private final Logger log = LoggerFactory.getLogger(getClass());

    // 内部数据状态
    private static class AmountBuffer implements Writable {
        // 总账户的 token 数量
        private String allAccountAmount = "0";
        // 交易子账户的 token 数量
        private String tradeAccountAmount = "0";


        //  逻辑：
        //        累计 Buy 的 总 Cost、总 Amount。
        //        当 tradeAccountAmount = 0 时，Buy 的 总 Cost、总 Amount 都重置为 0。
        // 交易子账户 累计 买入的 token 的 总数量
        private String tradeAccountTokenAmountSumOfBuy = "0";
        // 交易子账户 累计 买入的 token 的 总成本
        private String tradeAccountTokenCostSumOfBuy = "0";

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

        public String getTradeAccountPerTokenCost() {
            String tradeAccountPerTokenCost =
                    "0".equals(tradeAccountTokenAmountSumOfBuy) ? "0" : ArithmeticUtils.div(tradeAccountTokenCostSumOfBuy, tradeAccountTokenAmountSumOfBuy, 18);
            return tradeAccountPerTokenCost;
        }

        @Override
        public String toString() {
            String str = String.format("allAccountAmount=%s, tradeAccountAmount=%s, tradeAccountTokenAmountSumOfBuy=%s, tradeAccountTokenCostSumOfBuy=%s, tradeAccountPerTokenCost=%s", allAccountAmount, tradeAccountAmount, tradeAccountTokenAmountSumOfBuy, tradeAccountTokenCostSumOfBuy, getTradeAccountPerTokenCost());
            return str;
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
     *               Text amount
     *               Text price
     * @throws UDFException
     */
    @Override
    public void iterate(Writable buffer, Writable[] args) throws UDFException {
        String type = ((Text) args[0]).toString();
        String amount = ((Text) args[1]).toString();
        String price = ((Text) args[2]).toString();
        String tradeAccountAmount = ((Text) args[3]).toString();
        AmountBuffer buf = (AmountBuffer) buffer;
        if (amount != null) {
            if (type.equals("Base")) {
                // todo: price
                buf.tradeAccountAmount = tradeAccountAmount;
                buf.allAccountAmount = amount;
            }
            if (type.equals("Buy")) {
                buf.tradeAccountAmount = ArithmeticUtils.add(buf.tradeAccountAmount, amount).toString();
                buf.allAccountAmount = ArithmeticUtils.add(buf.allAccountAmount, amount).toString();

                // 累计 Buy 的 总 Cost、总 Amount。
                String incrCost = ArithmeticUtils.mul(price, amount).toString();
                buf.tradeAccountTokenCostSumOfBuy = ArithmeticUtils.add(buf.tradeAccountTokenCostSumOfBuy, incrCost).toString();
                buf.tradeAccountTokenAmountSumOfBuy = ArithmeticUtils.add(buf.tradeAccountTokenAmountSumOfBuy, amount).toString();
            }
            if (type.equals("Sell")) {
                boolean isTradeAccountAmountNotEnough = ArithmeticUtils.compare(amount, buf.tradeAccountAmount);
                buf.tradeAccountAmount =
                        isTradeAccountAmountNotEnough ? "0" : ArithmeticUtils.sub(buf.tradeAccountAmount, amount).toString();
                buf.allAccountAmount = ArithmeticUtils.sub(buf.allAccountAmount, amount).toString();

                //当 tradeAccountAmount = 0 时，Buy 的 总 Cost、总 Amount 都重置为 0。
                boolean isTradeAccountClean = "0".equals(buf.tradeAccountAmount);
                if (isTradeAccountClean) {
                    buf.tradeAccountTokenCostSumOfBuy = "0";
                    buf.tradeAccountTokenAmountSumOfBuy = "0";
                }
            }
            if (type.equals("TransferIn")) {
                buf.allAccountAmount = ArithmeticUtils.add(buf.allAccountAmount, amount).toString();
            }
            if (type.equals("TransferOut")) {
                boolean isTransferAccountNotEnough = ArithmeticUtils.compare(amount, ArithmeticUtils.sub(buf.allAccountAmount, buf.tradeAccountAmount).toString());
                buf.tradeAccountAmount = isTransferAccountNotEnough ? ArithmeticUtils.sub(buf.allAccountAmount, amount).toString() : buf.tradeAccountAmount;
                buf.allAccountAmount = ArithmeticUtils.sub(buf.allAccountAmount, amount).toString();

                //当 tradeAccountAmount = 0 时，Buy 的 总 Cost、总 Amount 都重置为 0。
                boolean isTradeAccountClean = "0".equals(buf.tradeAccountAmount);
                if (isTradeAccountClean) {
                    buf.tradeAccountTokenCostSumOfBuy = "0";
                    buf.tradeAccountTokenAmountSumOfBuy = "0";
                }
            }
        }
        log.info("[iterate func]: type={}, amount={}, price={}, AmountBuffer=[{}]", type, amount, price, buf.toString());
    }

    @Override
    public Writable terminate(Writable buffer) throws UDFException {
        AmountBuffer buf = (AmountBuffer) buffer;
        Text ret = new Text(buf.getTradeAccountPerTokenCost());
        return ret;
    }

    // udaf 分析函数 + 开窗函数，runtime时 不应该 走到这里
    @Override
    public void merge(Writable buffer, Writable partial) throws UDFException {
        AmountBuffer buf = (AmountBuffer) buffer;
        AmountBuffer p = (AmountBuffer) partial;
        buf.allAccountAmount = ArithmeticUtils.add(buf.allAccountAmount, p.allAccountAmount).toString();
        buf.tradeAccountAmount = ArithmeticUtils.add(buf.tradeAccountAmount, p.tradeAccountAmount).toString();
        buf.tradeAccountTokenAmountSumOfBuy = ArithmeticUtils.add(buf.tradeAccountTokenAmountSumOfBuy, p.tradeAccountTokenAmountSumOfBuy).toString();
        buf.tradeAccountTokenCostSumOfBuy = ArithmeticUtils.add(buf.tradeAccountTokenCostSumOfBuy, p.tradeAccountTokenCostSumOfBuy).toString();
        log.info("[merge func]: buffer={}", buf);
    }
}