package cn.xlystar.udaf;

import cn.xlystar.utils.ArithmeticUtils;
import com.aliyun.odps.io.Text;
import com.aliyun.odps.io.Writable;
import com.aliyun.odps.udf.Aggregator;
import com.aliyun.odps.udf.UDFException;
import com.aliyun.odps.udf.annotation.Resolve;
import com.aliyun.odps.utils.StringUtils;
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
 * <p>
 * amount: 统一为正数。目前不需要提前按 入还是出 的语义，处理 正负
 * <p>
 * tradeAccountAmount：之前 交易子账户的数量
 * <p>
 * 输出：tradeAccountAmount 交易子账户的数量
 */
@Resolve("STRING,STRING,STRING,STRING,STRING,STRING,STRING,STRING,STRING,STRING,STRING,STRING,STRING,STRING,STRING->STRING")
public class AccountAmountSum extends Aggregator {
    private final Logger log = LoggerFactory.getLogger(getClass());

    // 内部数据状态
    private static class AmountBuffer implements Writable {
        // 总账户的 token 数量
        private String allAccountAmount = "0";
        // 交易子账户的 token 数量
        private String tradeAccountAmount = "0";
        private String tradePositionNum = "0";
        private String nextTradePositionNum = "0";


        //  逻辑：
        //        累计 Buy 的 总 Cost、总 Amount。
        //        当 tradeAccountAmount = 0 时，Buy 的 总 Cost、总 Amount 都重置为 0。
        // 交易子账户 累计 买入的 token 的 总数量
        private String tradeAccountTokenAmountSumOfBuy = "0";
        // 交易子账户 累计 买入的 token 的 总成本
        private String tradeAccountTokenCostSumOfBuy = "0";
        private String tradeAccountTokenCostETHSumOfBuy = "0";
        private String tradeAccountTokenCostGas = "0";
        private String tradeAccountTokenSellCostGas = "0";
        private String tradeAccountTokenCostGasETH = "0";
        private String tradeAccountTokenSellCostGasETH = "0";

        @Override
        public void write(DataOutput out) throws IOException {
            out.writeUTF(allAccountAmount);
            out.writeUTF(tradeAccountAmount);
            out.writeUTF(tradePositionNum);
            out.writeUTF(nextTradePositionNum);
            out.writeUTF(tradeAccountTokenAmountSumOfBuy);
            out.writeUTF(tradeAccountTokenCostSumOfBuy);
            out.writeUTF(tradeAccountTokenCostETHSumOfBuy);
            out.writeUTF(tradeAccountTokenCostGas);
            out.writeUTF(tradeAccountTokenCostGasETH);
        }

        @Override
        public void readFields(DataInput in) throws IOException {
            allAccountAmount = in.readUTF();
            tradeAccountAmount = in.readUTF();
            tradePositionNum = in.readUTF();
            nextTradePositionNum = in.readUTF();
            tradeAccountTokenAmountSumOfBuy = in.readUTF();
            tradeAccountTokenCostSumOfBuy = in.readUTF();
            tradeAccountTokenCostETHSumOfBuy = in.readUTF();
            tradeAccountTokenCostGas = in.readUTF();
            tradeAccountTokenCostGasETH = in.readUTF();
        }

        public String getTradeAccountPerTokenCost() {
            String tradeAccountPerTokenCost =
                    "0".equals(tradeAccountTokenAmountSumOfBuy) ? "0" : ArithmeticUtils.div(tradeAccountTokenCostSumOfBuy, tradeAccountTokenAmountSumOfBuy, 18);
            return tradeAccountPerTokenCost;
        }

        public String getTradeAccountPerETHTokenCost() {
            String tradeAccountPerTokenETHCost =
                    "0".equals(tradeAccountTokenAmountSumOfBuy) ? "0" : ArithmeticUtils.div(tradeAccountTokenCostETHSumOfBuy, tradeAccountTokenAmountSumOfBuy, 18);
            return tradeAccountPerTokenETHCost;
        }

        public String getTradeAccountPerTokenCostGas() {
            String tradeAccountPerTokenCostGas =
                    "0".equals(tradeAccountTokenAmountSumOfBuy) ? "0" : ArithmeticUtils.div(tradeAccountTokenCostGas, tradeAccountTokenAmountSumOfBuy, 18);
            return tradeAccountPerTokenCostGas;
        }

        public String getTradeAccountPerTokenCostGasETH() {
            String tradeAccountPerTokenCostGasETH =
                    "0".equals(tradeAccountTokenAmountSumOfBuy) ? "0" : ArithmeticUtils.div(tradeAccountTokenCostGasETH, tradeAccountTokenAmountSumOfBuy, 18);
            return tradeAccountPerTokenCostGasETH;
        }
        @Override
        public String toString() {
            String str = String.format("{allAccountAmount=%s, tradeAccountAmount=%s, tradeAccountTokenAmountSumOfBuy=%s, tradeAccountTokenCostSumOfBuy=%s, tradeAccountPerTokenCost=%s}", allAccountAmount, tradeAccountAmount, tradeAccountTokenAmountSumOfBuy, tradeAccountTokenCostSumOfBuy, getTradeAccountPerTokenCost());
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
        String type = ((Text) args[0]).toString(); // 交易类型
        String amount = ((Text) args[1]).toString(); // 交易金额
        amount = "Base".equals(type) ? amount : amount.replaceAll("^(-)", "");
        String buy_gas_usd = args[2] == null || StringUtils.isNullOrEmpty(args[2].toString()) ? "0" : args[2].toString();// gas usd计价
        String buy_gas_bnb = args[3] == null || StringUtils.isNullOrEmpty(args[3].toString()) ? "0" : args[3].toString();// gas eth计价
        String sell_gas_usd = args[4] == null || StringUtils.isNullOrEmpty(args[4].toString()) ? "0" : args[4].toString();// gas usd计价
        String sell_gas_bnb = args[5] == null || StringUtils.isNullOrEmpty(args[5].toString()) ? "0" : args[5].toString();// gas eth计价
        String tradeAccountAmount = args[6] == null || StringUtils.isNullOrEmpty(args[6].toString()) ? "0" : args[6].toString(); // 交易账号总余额
        String trade_token_amount = args[7] == null || StringUtils.isNullOrEmpty(args[7].toString()) ? "0" : args[7].toString(); // 买入的总 amount
        String trade_token_cost_usd = args[8] == null || StringUtils.isNullOrEmpty(args[8].toString()) ? "0" : args[8].toString();
        String trade_token_cost_eth = args[9] == null || StringUtils.isNullOrEmpty(args[9].toString()) ? "0" : args[9].toString();
//        String price = ((Text) args[2]).toString(); // token:usd 汇率
//        price = StringUtils.isNullOrEmpty(price) ? "0" : price;
        String priceUSD = args[10] == null || StringUtils.isNullOrEmpty(args[10].toString()) ? "0" : args[10].toString();// token:usd 汇率
        String priceETH = args[11] == null || StringUtils.isNullOrEmpty(args[11].toString()) ? "0" : args[11].toString(); // token:eth 汇率
        String out_by_in_ratio = args[12] == null || StringUtils.isNullOrEmpty(args[12].toString()) ? "0" : args[12].toString(); // token:eth 汇率
        String tokenPositionNum = args[13] == null || StringUtils.isNullOrEmpty(args[13].toString()) ? "0" : args[13].toString(); // 仓位序号
        String decimals = args[14] == null || StringUtils.isNullOrEmpty(args[14].toString()) ? "0" : args[14].toString(); // 精度
        AmountBuffer buf = (AmountBuffer) buffer;
        log.info("[iterate func start]: type={}, amount={}, price={}, tradeAccountAmount={}", type, amount, priceUSD, tradeAccountAmount);
        //当 tradeAccountAmount = 0 时，Buy 的 总 Cost、总 Amount 都重置为 0。
        boolean isTradeAccountClean = "0".equals(buf.tradeAccountAmount);
        if (isTradeAccountClean) {
            buf.tradeAccountTokenCostSumOfBuy = "0";
            buf.tradeAccountTokenAmountSumOfBuy = "0";
            buf.tradeAccountTokenCostETHSumOfBuy = "0";
            buf.tradeAccountTokenCostGas = "0";
            buf.tradeAccountTokenCostGasETH = "0";
            buf.tradeAccountTokenSellCostGas = "0";
            buf.tradeAccountTokenSellCostGasETH = "0";
        }
        if (amount != null) {
            if ("Base".equals(type)) {
                // todo: price
                buf.tradePositionNum = tokenPositionNum;
                buf.nextTradePositionNum = tokenPositionNum;
                buf.tradeAccountAmount = tradeAccountAmount;
                buf.allAccountAmount = amount;

                buf.tradeAccountTokenCostSumOfBuy = ArithmeticUtils.add(buf.tradeAccountTokenCostSumOfBuy, trade_token_cost_usd).toString();
                buf.tradeAccountTokenAmountSumOfBuy = ArithmeticUtils.add(buf.tradeAccountTokenAmountSumOfBuy, trade_token_amount).toString();
                buf.tradeAccountTokenCostETHSumOfBuy = ArithmeticUtils.add(buf.tradeAccountTokenCostETHSumOfBuy, trade_token_cost_eth).toString();
                buf.tradeAccountTokenCostGas = ArithmeticUtils.add(buf.tradeAccountTokenCostGas, buy_gas_usd).toString();
                buf.tradeAccountTokenCostGasETH = ArithmeticUtils.add(buf.tradeAccountTokenCostGasETH, buy_gas_bnb).toString();
                buf.tradeAccountTokenSellCostGas = ArithmeticUtils.add(buf.tradeAccountTokenSellCostGas, sell_gas_usd).toString();
                buf.tradeAccountTokenSellCostGasETH = ArithmeticUtils.add(buf.tradeAccountTokenSellCostGasETH, sell_gas_bnb).toString();
            }
            if ("Buy".equals(type)) {
                if ("0".equals(buf.tradeAccountAmount)) {
                    buf.nextTradePositionNum = ArithmeticUtils.add(buf.nextTradePositionNum, "1").toString();
                    buf.tradePositionNum = buf.nextTradePositionNum;
                }
                buf.tradeAccountAmount = ArithmeticUtils.add(buf.tradeAccountAmount, amount).toString();
                buf.allAccountAmount = ArithmeticUtils.add(buf.allAccountAmount, amount).toString();

                // 累计 Buy 的 总 Cost、总 Amount。
                String incrCost = ArithmeticUtils.mul(priceUSD, amount).toString();
                String incrCostETH = ArithmeticUtils.mul(priceETH, amount).toString();
                buf.tradeAccountTokenCostSumOfBuy = ArithmeticUtils.add(buf.tradeAccountTokenCostSumOfBuy, incrCost).toString();
                buf.tradeAccountTokenAmountSumOfBuy = ArithmeticUtils.add(buf.tradeAccountTokenAmountSumOfBuy, amount).toString();
                buf.tradeAccountTokenCostETHSumOfBuy = ArithmeticUtils.add(buf.tradeAccountTokenCostETHSumOfBuy, incrCostETH).toString();
                buf.tradeAccountTokenCostGas = ArithmeticUtils.add(buf.tradeAccountTokenCostGas, buy_gas_usd).toString();
                buf.tradeAccountTokenCostGasETH = ArithmeticUtils.add(buf.tradeAccountTokenCostGasETH, buy_gas_bnb).toString();
            }
            if ("Sell".equals(type)) {
                if ("0".equals(buf.tradeAccountAmount)) {
                    buf.tradePositionNum = "-1";
                }
                boolean isTradeAccountAmountNotEnough = ArithmeticUtils.compare(amount, buf.tradeAccountAmount);
                boolean isAllAccountAmountNotEnough = ArithmeticUtils.compare(amount, buf.allAccountAmount);
                buf.tradeAccountAmount = isTradeAccountAmountNotEnough ? "0" : ArithmeticUtils.sub(buf.tradeAccountAmount, amount).toString();
                buf.allAccountAmount = isAllAccountAmountNotEnough ? "0" : ArithmeticUtils.sub(buf.allAccountAmount, amount).toString();
                buf.tradeAccountTokenSellCostGas = ArithmeticUtils.add(buf.tradeAccountTokenSellCostGas, sell_gas_usd).toString();
                buf.tradeAccountTokenSellCostGasETH = ArithmeticUtils.add(buf.tradeAccountTokenSellCostGasETH, sell_gas_bnb).toString();
//                //当 tradeAccountAmount = 0 时，Buy 的 总 Cost、总 Amount 都重置为 0。
//                boolean isTradeAccountClean = "0".equals(buf.tradeAccountAmount);
//                if (isTradeAccountClean) {
//                    buf.tradeAccountTokenCostSumOfBuy = "0";
//                    buf.tradeAccountTokenAmountSumOfBuy = "0";
//                }
            }
            if ("TransferIn".equals(type)) {
                if ("0".equals(buf.tradeAccountAmount)) {
                    buf.tradePositionNum = "-1";
                }
                buf.allAccountAmount = ArithmeticUtils.add(buf.allAccountAmount, amount).toString();
            }
            if ("TransferOut".equals(type)) {
                if ("0".equals(buf.tradeAccountAmount)) {
                    buf.tradePositionNum = "-1";
                }
//                boolean hasTransferAmount = ArithmeticUtils.compare(buf.allAccountAmount, buf.tradeAccountAmount);
//                boolean isTransferAccountNotEnough = ArithmeticUtils.compare(amount, ArithmeticUtils.sub(buf.allAccountAmount, buf.tradeAccountAmount).toString());
                boolean isAllAccountAmountNotEnough = ArithmeticUtils.compare(amount, buf.allAccountAmount);
                boolean isTradeAccountAmountNotEnough = ArithmeticUtils.compare(amount, buf.tradeAccountAmount);
//                if (isTransferAccountNotEnough && hasTransferAmount) {
//                    String tempTradeAccountAmount = ArithmeticUtils.sub(buf.tradeAccountAmount,
//                            ArithmeticUtils.sub(amount,
//                                    ArithmeticUtils.sub(buf.allAccountAmount, buf.tradeAccountAmount).toString()).toString()).toString();
//                    buf.tradeAccountAmount = ArithmeticUtils.compare("0", tempTradeAccountAmount) ? "0" : tempTradeAccountAmount;
//                } else if (isTransferAccountNotEnough) {
//                    String tempTradeAccountAmount = ArithmeticUtils.sub(buf.tradeAccountAmount, amount).toString();
//                    buf.tradeAccountAmount = ArithmeticUtils.compare("0", tempTradeAccountAmount) ? "0" : tempTradeAccountAmount;
//                }
                buf.allAccountAmount = isAllAccountAmountNotEnough ? "0" : ArithmeticUtils.sub(buf.allAccountAmount, amount).toString();
                buf.tradeAccountAmount = isTradeAccountAmountNotEnough ? "0" : ArithmeticUtils.sub(buf.tradeAccountAmount, amount).toString();

//                //当 tradeAccountAmount = 0 时，Buy 的 总 Cost、总 Amount 都重置为 0。
//                boolean isTradeAccountClean = "0".equals(buf.tradeAccountAmount);
//                if (isTradeAccountClean) {
//                    buf.tradeAccountTokenCostSumOfBuy = "0";
//                    buf.tradeAccountTokenAmountSumOfBuy = "0";
//                }
            }


            StringBuilder decimal = new StringBuilder("1");
            for (int i = 0; i < Integer.parseInt(decimals); i++) {
                decimal.append("0");
            }
            if (!ArithmeticUtils.compare(out_by_in_ratio, "0")) {
                return;
            }
            if (!ArithmeticUtils.compare(ArithmeticUtils.div(ArithmeticUtils.mul(out_by_in_ratio, buf.tradeAccountAmount).toString(), decimal.toString(), 18), "0.001")) {
                buf.tradeAccountAmount = "0";
            }

        }
        log.info("[iterate func end]: type={}, amount={}, price={}, tradeAccountAmount={}, AmountBuffer={}", type, amount, priceUSD, tradeAccountAmount, buf.toString());
    }

    @Override
    public Writable terminate(Writable buffer) throws UDFException {
        AmountBuffer buf = (AmountBuffer) buffer;
        String res = buf.allAccountAmount + ","             // 总账号余额
                + buf.tradeAccountAmount + ","              // 交易账号余额
                + buf.tradePositionNum + ","                // 仓位
                + buf.tradeAccountTokenAmountSumOfBuy + "," // 累计买入 amount
                + buf.tradeAccountTokenCostSumOfBuy + ","   // 累计买入花费的 USD
                + buf.tradeAccountTokenCostETHSumOfBuy + ","// 累计买入花费的 ETH
                + buf.tradeAccountTokenCostGas + ","        // 累计花费的 gas, USD
                + buf.tradeAccountTokenCostGasETH + ","     // 累计 花费的 gas, ETH
                + buf.tradeAccountTokenSellCostGas + ","    // 累计卖出花费的 gas, usd
                + buf.tradeAccountTokenSellCostGasETH;      // 累计卖出花费的 gas, ETH
        Text ret = new Text(res);
        return ret;
    }

    // udaf 分析函数 + 开窗函数，runtime时 不应该 走到这里
    @Override
    public void merge(Writable buffer, Writable partial) throws UDFException {
        AmountBuffer buf = (AmountBuffer) buffer;
        AmountBuffer p = (AmountBuffer) partial;
        buf.allAccountAmount = ArithmeticUtils.add(buf.allAccountAmount, p.allAccountAmount).toString();
        buf.tradeAccountAmount = ArithmeticUtils.add(buf.tradeAccountAmount, p.tradeAccountAmount).toString();
        buf.tradePositionNum = ArithmeticUtils.add(buf.tradePositionNum, p.tradePositionNum).toString();
        buf.nextTradePositionNum = ArithmeticUtils.add(buf.nextTradePositionNum, p.nextTradePositionNum).toString();
        buf.tradeAccountTokenAmountSumOfBuy = ArithmeticUtils.add(buf.tradeAccountTokenAmountSumOfBuy, p.tradeAccountTokenAmountSumOfBuy).toString();
        buf.tradeAccountTokenCostSumOfBuy = ArithmeticUtils.add(buf.tradeAccountTokenCostSumOfBuy, p.tradeAccountTokenCostSumOfBuy).toString();
        buf.tradeAccountTokenCostETHSumOfBuy = ArithmeticUtils.add(buf.tradeAccountTokenCostETHSumOfBuy, p.tradeAccountTokenCostETHSumOfBuy).toString();
        buf.tradeAccountTokenCostGas = ArithmeticUtils.add(buf.tradeAccountTokenCostGas, p.tradeAccountTokenCostGas).toString();
        buf.tradeAccountTokenCostGasETH = ArithmeticUtils.add(buf.tradeAccountTokenCostGasETH, p.tradeAccountTokenCostGasETH).toString();
        buf.tradeAccountTokenSellCostGas = ArithmeticUtils.add(buf.tradeAccountTokenSellCostGas, p.tradeAccountTokenSellCostGas).toString();
        buf.tradeAccountTokenSellCostGasETH = ArithmeticUtils.add(buf.tradeAccountTokenSellCostGasETH, p.tradeAccountTokenSellCostGasETH).toString();
        log.info("[merge func]: buffer={}", buf);
    }
}