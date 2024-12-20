package cn.xlystar.udaf;

import cn.xlystar.utils.ArithmeticUtils;
import com.aliyun.odps.io.Text;
import com.aliyun.odps.io.Writable;
import com.aliyun.odps.udf.Aggregator;
import com.aliyun.odps.udf.UDFException;
import com.aliyun.odps.udf.annotation.Resolve;
import com.aliyun.odps.utils.StringUtils;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.math.BigInteger;


/**
 * 函数定义：position_analysis
 */
@Resolve("STRING,STRING,STRING,STRING,STRING," +
        "STRING,STRING,STRING,STRING,STRING," +
        "STRING,STRING->STRING")
public class AccountAllAmountSum extends Aggregator {
    private final Logger log = LoggerFactory.getLogger(getClass());

    // 内部数据状态
    private static class AmountBuffer implements Writable {

        // 交易账户的 token 数量
        private String holdingAmount = "0";
//        private String tradePositionNum = "0";
//        private String nextTradePositionNum = "0";

        private String buyAmount = "0";
        private String buyTxCount = "0";
        private String costCoin = "0";

        private String sellAmount = "0";
        private String sellTxCount = "0";
        private String receiverCoin = "0";


        private String transferInAmount = "0";
        private String transferInTxCount = "0";

        private String transferOutAmount = "0";
        private String transferOutTxCount = "0";

        @Override
        public void write(DataOutput out) throws IOException {
            out.writeUTF(holdingAmount);
//            out.writeUTF(tradePositionNum);
//            out.writeUTF(nextTradePositionNum);

            out.writeUTF(buyAmount);
            out.writeUTF(buyTxCount);
            out.writeUTF(costCoin);

            out.writeUTF(sellAmount);
            out.writeUTF(sellTxCount);
            out.writeUTF(receiverCoin);

            out.writeUTF(transferInAmount);
            out.writeUTF(transferInTxCount);

            out.writeUTF(transferOutAmount);
            out.writeUTF(transferOutTxCount);
        }

        @Override
        public void readFields(DataInput in) throws IOException {
            holdingAmount = in.readUTF();
//            tradePositionNum = in.readUTF();
//            nextTradePositionNum = in.readUTF();

            buyAmount = in.readUTF();
            buyTxCount = in.readUTF();
            costCoin = in.readUTF();

            sellAmount = in.readUTF();
            sellTxCount = in.readUTF();
            receiverCoin = in.readUTF();

            transferInAmount = in.readUTF();
            transferInTxCount = in.readUTF();

            transferOutAmount = in.readUTF();
            transferOutTxCount = in.readUTF();
        }


    }

    @Override
    public Writable newBuffer() {
        return new AmountBuffer();
    }

    @SneakyThrows
    @Override
    public void iterate(Writable buffer, Writable[] args) throws UDFException {

        String tradeAccountAmount = args[0] == null || StringUtils.isNullOrEmpty(args[0].toString()) ? "0" : args[0].toString(); // 交易账号总余额

        String buyAmount = args[1] == null || StringUtils.isNullOrEmpty(args[1].toString()) ? "0" : args[1].toString();
        String buyTxCount = args[2] == null || StringUtils.isNullOrEmpty(args[2].toString()) ? "0" : args[2].toString();
        String costCoin = args[3] == null || StringUtils.isNullOrEmpty(args[3].toString()) ? "0" : args[3].toString();

        String sellAmount = args[4] == null || StringUtils.isNullOrEmpty(args[4].toString()) ? "0" : args[4].toString();
        String sellTxCount = args[5] == null || StringUtils.isNullOrEmpty(args[5].toString()) ? "0" : args[5].toString();
        String receiverCoin = args[6] == null || StringUtils.isNullOrEmpty(args[6].toString()) ? "0" : args[6].toString();

        String transferInAmount = args[7] == null || StringUtils.isNullOrEmpty(args[7].toString()) ? "0" : args[7].toString();
        String transferInTxCount = args[8] == null || StringUtils.isNullOrEmpty(args[8].toString()) ? "0" : args[8].toString();

        String transferOutAmount = args[9] == null || StringUtils.isNullOrEmpty(args[9].toString()) ? "0" : args[9].toString();
        String transferOutTxCount = args[10] == null || StringUtils.isNullOrEmpty(args[10].toString()) ? "0" : args[10].toString();

        String type = ((Text) args[11]).toString();
//        String tokenPositionNum = ((Text) args[12]).toString();

        AmountBuffer buf = (AmountBuffer) buffer;
        try {
//            //当 tradeAccountAmount = 0 时，Buy 的 总 Cost、总 Amount 都重置为 0。
//            boolean isTradeAccountClean = !ArithmeticUtils.compare(buf.holdingAmount, "0");
//            if (isTradeAccountClean) {
//
//                buf.buyAmount = "0";
//                buf.buyTxCount = "0";
//                buf.costCoin = "0";
//
//                buf.sellAmount = "0";
//                buf.sellTxCount = "0";
//                buf.receiverCoin = "0";
//
//                buf.transferInAmount = "0";
//                buf.transferInTxCount = "0";
//
//                buf.transferOutAmount = "0";
//                buf.transferOutTxCount = "0";
//            }
//            if ("Base".equals(type)) {
//                buf.tradePositionNum = tokenPositionNum;
//                buf.nextTradePositionNum = tokenPositionNum;
                buf.holdingAmount = tradeAccountAmount;

                buf.buyAmount = ArithmeticUtils.add(buf.buyAmount, buyAmount).toString();
                buf.buyTxCount = ArithmeticUtils.add(buf.buyTxCount, buyTxCount).toString();
                buf.costCoin = ArithmeticUtils.add(buf.costCoin, costCoin).toString();

                buf.sellAmount = ArithmeticUtils.add(buf.sellAmount, sellAmount).toString();
                buf.sellTxCount = ArithmeticUtils.add(buf.sellTxCount, sellTxCount).toString();
                buf.receiverCoin = ArithmeticUtils.add(buf.receiverCoin, receiverCoin).toString();

                buf.transferInAmount = ArithmeticUtils.add(buf.transferInAmount, transferInAmount).toString();
                buf.transferInTxCount = ArithmeticUtils.add(buf.transferInTxCount, transferInTxCount).toString();

                buf.transferOutAmount = ArithmeticUtils.add(buf.transferOutAmount, transferOutAmount).toString();
                buf.transferOutTxCount = ArithmeticUtils.add(buf.transferOutTxCount, transferOutTxCount).toString();

//            }


//            if ("Buy".equals(type)) {
////                if (!ArithmeticUtils.compare(buf.holdingAmount, "0")) {
////                    buf.nextTradePositionNum = ArithmeticUtils.add(buf.nextTradePositionNum, "1").toString();
////                    buf.tradePositionNum = buf.nextTradePositionNum;
////                }
//                buf.holdingAmount = ArithmeticUtils.add(buf.holdingAmount, tradeAccountAmount).toString();
//                buf.buyAmount = ArithmeticUtils.add(buf.buyAmount, buyAmount).toString();
//                buf.buyTxCount = ArithmeticUtils.add(buf.buyTxCount, buyTxCount).toString();
//                buf.costCoin = ArithmeticUtils.add(buf.costCoin, costCoin).toString();
//
//            }
//            if ("Sell".equals(type)) {
////                if ("0".equals(buf.holdingAmount)) {
////                    buf.tradePositionNum = "-1";
////                }
////                boolean isAllAccountAmountNotEnough = ArithmeticUtils.compare(sellAmount, buf.holdingAmount);
////                sellAmount = isAllAccountAmountNotEnough ? buf.holdingAmount : sellAmount;
//                buf.holdingAmount = ArithmeticUtils.add(buf.holdingAmount, tradeAccountAmount).toString();
//                buf.sellAmount = ArithmeticUtils.add(buf.sellAmount, sellAmount).toString();
//                buf.sellTxCount = ArithmeticUtils.add(buf.sellTxCount, sellTxCount).toString();
//                buf.receiverCoin = ArithmeticUtils.add(buf.receiverCoin, receiverCoin).toString();
//            }
//            if ("TransferIn".equals(type)) {
////                if (!ArithmeticUtils.compare(buf.holdingAmount, "0")) {
////                    buf.nextTradePositionNum = ArithmeticUtils.add(buf.nextTradePositionNum, "1").toString();
////                    buf.tradePositionNum = buf.nextTradePositionNum;
////                }
//                buf.holdingAmount = ArithmeticUtils.add(buf.holdingAmount, tradeAccountAmount).toString();
//                buf.transferInAmount = ArithmeticUtils.add(buf.transferInAmount, transferInAmount).toString();
//                buf.transferInTxCount = ArithmeticUtils.add(buf.transferInTxCount, transferInTxCount).toString();
//            }
//            if ("TransferOut".equals(type)) {
////                if ("0".equals(buf.holdingAmount)) {
////                    buf.tradePositionNum = "-1";
////                }
////                boolean isAllAccountAmountNotEnough = ArithmeticUtils.compare(tradeAccountAmount, buf.holdingAmount);
////                transferOutAmount = isAllAccountAmountNotEnough ? buf.holdingAmount : transferOutAmount;
//
//                buf.holdingAmount = ArithmeticUtils.add(buf.holdingAmount, tradeAccountAmount).toString();
//                buf.transferOutAmount = ArithmeticUtils.add(buf.transferOutAmount, transferOutAmount).toString();
//                buf.transferOutTxCount = ArithmeticUtils.add(buf.transferOutTxCount, transferOutTxCount).toString();
//
//            }

        } catch (Exception e) {
            e.printStackTrace();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < args.length; i++) {
                builder.append("| index-" + i + ":" + args[i].toString());
            }
            throw new Exception("buf:" + buf + ", args:" + builder.toString());
        }
    }

    @Override
    public Writable terminate(Writable buffer) throws UDFException {
        AmountBuffer buf = (AmountBuffer) buffer;
        String res = buf.holdingAmount + ","
//                + buf.tradePositionNum + ","
                + buf.buyAmount + ","
                + buf.buyTxCount + ","
                + buf.costCoin + ","
                + buf.sellAmount + ","
                + buf.sellTxCount + ","
                + buf.receiverCoin + ","
                + buf.transferInAmount + ","
                + buf.transferInTxCount + ","
                + buf.transferOutAmount + ","
                + buf.transferOutTxCount
                ;
        Text ret = new Text(res);
        return ret;
    }

    // udaf 分析函数 + 开窗函数，runtime时 不应该 走到这里
    @Override
    public void merge(Writable buffer, Writable partial) throws UDFException {
        AmountBuffer buf = (AmountBuffer) buffer;
        AmountBuffer p = (AmountBuffer) partial;
        buf.holdingAmount = ArithmeticUtils.add(buf.holdingAmount, p.holdingAmount).toString();
//        buf.tradePositionNum = ArithmeticUtils.add(buf.tradePositionNum, p.tradePositionNum).toString();
//        buf.nextTradePositionNum = ArithmeticUtils.add(buf.nextTradePositionNum, p.nextTradePositionNum).toString();
        buf.buyAmount = ArithmeticUtils.add(buf.buyAmount, p.buyAmount).toString();
        buf.buyTxCount = ArithmeticUtils.add(buf.buyTxCount, p.buyTxCount).toString();
        buf.costCoin = ArithmeticUtils.add(buf.costCoin, p.costCoin).toString();
        buf.sellAmount = ArithmeticUtils.add(buf.sellAmount, p.sellAmount).toString();
        buf.sellTxCount = ArithmeticUtils.add(buf.sellTxCount, p.sellTxCount).toString();
        buf.receiverCoin = ArithmeticUtils.add(buf.receiverCoin, p.receiverCoin).toString();
        buf.transferInAmount = ArithmeticUtils.add(buf.transferInAmount, p.transferInAmount).toString();
        buf.transferInTxCount = ArithmeticUtils.add(buf.transferInTxCount, p.transferInTxCount).toString();
        buf.transferOutAmount = ArithmeticUtils.add(buf.transferOutAmount, p.transferOutAmount).toString();
        buf.transferOutTxCount = ArithmeticUtils.add(buf.transferOutTxCount, p.transferOutTxCount).toString();
        log.info("[merge func]: buffer={}", buf);
    }
}
