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
@Resolve("STRING,STRING,STRING,STRING,STRING,STRING,STRING,STRING,STRING,STRING,STRING,STRING,STRING,STRING,STRING,STRING,STRING,STRING,STRING,STRING,STRING,STRING,STRING->STRING")
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
        private String tradeAccountReceiveSumOfSell = "0";
        private String tradeAccountReceiveETHSumOfSell = "0";
        private String tradeAccountReceiveSumOfTransferOut = "0";
        private String tradeAccountReceiveAmountSumOfSell = "0";
        private String tradeAccountTokenCostHoldOfBuy = "0";
        private String tradeAccountTokenCostETHHoldOfBuy = "0";
        private String holdPriceUsd = "0";
        private String holdPriceCoin = "0";


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
            out.writeUTF(tradeAccountReceiveSumOfSell);
            out.writeUTF(tradeAccountReceiveETHSumOfSell);
            out.writeUTF(tradeAccountReceiveAmountSumOfSell);
            out.writeUTF(tradeAccountReceiveSumOfTransferOut);
            out.writeUTF(tradeAccountTokenCostHoldOfBuy);
            out.writeUTF(tradeAccountTokenCostETHHoldOfBuy);
            out.writeUTF(holdPriceUsd);
            out.writeUTF(holdPriceCoin);
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
            tradeAccountReceiveSumOfSell = in.readUTF();
            tradeAccountReceiveETHSumOfSell = in.readUTF();
            tradeAccountReceiveAmountSumOfSell = in.readUTF();
            tradeAccountReceiveSumOfTransferOut = in.readUTF();
            tradeAccountTokenCostHoldOfBuy = in.readUTF();
            tradeAccountTokenCostETHHoldOfBuy = in.readUTF();
            holdPriceUsd = in.readUTF();
            holdPriceCoin = in.readUTF();
        }

        public String getTradeAccountPerTokenCost() {
            String tradeAccountPerTokenCost =
                    "0".equals(tradeAccountTokenAmountSumOfBuy) ? "0" : ArithmeticUtils.div(tradeAccountTokenCostSumOfBuy, tradeAccountTokenAmountSumOfBuy, 18);
            return tradeAccountPerTokenCost;
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
    @SneakyThrows
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
        String sell_receive_usd = args[15] == null || StringUtils.isNullOrEmpty(args[15].toString()) ? "0" : args[15].toString(); // 精度
        String sell_receive_eth = args[16] == null || StringUtils.isNullOrEmpty(args[16].toString()) ? "0" : args[16].toString(); // 精度
        String sell_amount = args[17] == null || StringUtils.isNullOrEmpty(args[17].toString()) ? "0" : args[17].toString(); // 精度
        String transferout_amount = args[18] == null || StringUtils.isNullOrEmpty(args[18].toString()) ? "0" : args[18].toString(); // 精度
        String trade_token_cost_usd_now = args[19] == null || StringUtils.isNullOrEmpty(args[19].toString()) ? "0" : args[19].toString(); // 现持仓的开销
        String trade_token_cost_coin_now = args[20] == null || StringUtils.isNullOrEmpty(args[20].toString()) ? "0" : args[20].toString(); // 现持仓的开销
        String hold_price_usd = args[21] == null || StringUtils.isNullOrEmpty(args[21].toString()) ? "0" : args[21].toString(); // 现持仓的开销
        String hold_price_coin = args[22] == null || StringUtils.isNullOrEmpty(args[22].toString()) ? "0" : args[22].toString(); // 现持仓的开销
        AmountBuffer buf = (AmountBuffer) buffer;
        try {
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
                buf.tradeAccountReceiveSumOfSell = "0";
                buf.tradeAccountReceiveETHSumOfSell = "0";
                buf.tradeAccountReceiveAmountSumOfSell = "0";
                buf.tradeAccountReceiveSumOfTransferOut = "0";
                buf.tradeAccountTokenCostHoldOfBuy = "0";
                buf.tradeAccountTokenCostETHHoldOfBuy = "0";
                buf.holdPriceUsd = "0";
                buf.holdPriceCoin = "0";
            }
            if (amount != null) {
                if ("Base".equals(type)) {
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

                    buf.tradeAccountReceiveSumOfSell = ArithmeticUtils.add(buf.tradeAccountReceiveSumOfSell, sell_receive_usd).toString();
                    buf.tradeAccountReceiveETHSumOfSell = ArithmeticUtils.add(buf.tradeAccountReceiveETHSumOfSell, sell_receive_eth).toString();
                    buf.tradeAccountReceiveAmountSumOfSell = ArithmeticUtils.add(buf.tradeAccountReceiveAmountSumOfSell, sell_amount).toString();
                    buf.tradeAccountReceiveSumOfTransferOut = ArithmeticUtils.add(buf.tradeAccountReceiveSumOfTransferOut, transferout_amount).toString();

                    buf.tradeAccountTokenCostHoldOfBuy = ArithmeticUtils.add(buf.tradeAccountTokenCostHoldOfBuy, trade_token_cost_usd_now).toString();
                    buf.tradeAccountTokenCostETHHoldOfBuy = ArithmeticUtils.add(buf.tradeAccountTokenCostETHHoldOfBuy, trade_token_cost_coin_now).toString();
                    buf.holdPriceUsd = ArithmeticUtils.add(buf.holdPriceUsd, hold_price_usd).toString();
                    buf.holdPriceCoin = ArithmeticUtils.add(buf.holdPriceCoin, hold_price_coin).toString();
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

                    buf.tradeAccountTokenCostHoldOfBuy = ArithmeticUtils.add(buf.tradeAccountTokenCostHoldOfBuy, incrCost).toString();
                    buf.tradeAccountTokenCostETHHoldOfBuy = ArithmeticUtils.add(buf.tradeAccountTokenCostETHHoldOfBuy, incrCostETH).toString();

                    buf.holdPriceUsd = ArithmeticUtils.compare(buf.tradeAccountAmount, "0") ? ArithmeticUtils.div(buf.tradeAccountTokenCostHoldOfBuy, buf.tradeAccountAmount, 64) : "0" ;
                    buf.holdPriceCoin = ArithmeticUtils.compare(buf.tradeAccountAmount, "0") ? ArithmeticUtils.div(buf.tradeAccountTokenCostETHHoldOfBuy, buf.tradeAccountAmount, 64) : "0";

                }
                if ("Sell".equals(type)) {
                    if ("0".equals(buf.tradeAccountAmount)) {
                        buf.tradePositionNum = "-1";
                    }
                    boolean isTradeAccountAmountNotEnough = ArithmeticUtils.compare(amount, buf.tradeAccountAmount);
                    boolean isAllAccountAmountNotEnough = ArithmeticUtils.compare(amount, buf.allAccountAmount);
                    String tradeAmount = isTradeAccountAmountNotEnough ? buf.tradeAccountAmount : amount;
                    buf.tradeAccountAmount = isTradeAccountAmountNotEnough ? "0" : ArithmeticUtils.sub(buf.tradeAccountAmount, amount).toString();
                    buf.allAccountAmount = isAllAccountAmountNotEnough ? "0" : ArithmeticUtils.sub(buf.allAccountAmount, amount).toString();
                    buf.tradeAccountTokenSellCostGas = ArithmeticUtils.add(buf.tradeAccountTokenSellCostGas, sell_gas_usd).toString();
                    buf.tradeAccountTokenSellCostGasETH = ArithmeticUtils.add(buf.tradeAccountTokenSellCostGasETH, sell_gas_bnb).toString();

                    // 累计 Sell 的 总 Cost、总 Amount。
                    String incrCost = ArithmeticUtils.mul(priceUSD, tradeAmount).toString();
                    String incrCostETH = ArithmeticUtils.mul(priceETH, tradeAmount).toString();
                    buf.tradeAccountReceiveSumOfSell = ArithmeticUtils.add(buf.tradeAccountReceiveSumOfSell, incrCost).toString();
                    buf.tradeAccountReceiveETHSumOfSell = ArithmeticUtils.add(buf.tradeAccountReceiveETHSumOfSell, incrCostETH).toString();
                    buf.tradeAccountReceiveAmountSumOfSell = ArithmeticUtils.add(buf.tradeAccountReceiveAmountSumOfSell, tradeAmount).toString();

                    String decrCost = ArithmeticUtils.mul(buf.holdPriceUsd, tradeAmount).toPlainString();
                    String decrCostCoin = ArithmeticUtils.mul(buf.holdPriceCoin, tradeAmount).toPlainString();
                    buf.tradeAccountTokenCostHoldOfBuy = ArithmeticUtils.compare(decrCost, buf.tradeAccountTokenCostHoldOfBuy) ? "0" : ArithmeticUtils.sub(buf.tradeAccountTokenCostHoldOfBuy, decrCost).toString();
                    buf.tradeAccountTokenCostETHHoldOfBuy = ArithmeticUtils.compare(decrCostCoin, buf.tradeAccountTokenCostETHHoldOfBuy) ? "0" : ArithmeticUtils.sub(buf.tradeAccountTokenCostETHHoldOfBuy, decrCostCoin).toString();

                    buf.holdPriceUsd = ArithmeticUtils.compare(buf.tradeAccountAmount, "0") ? ArithmeticUtils.div(buf.tradeAccountTokenCostHoldOfBuy, buf.tradeAccountAmount, 64) : "0" ;
                    buf.holdPriceCoin = ArithmeticUtils.compare(buf.tradeAccountAmount, "0") ? ArithmeticUtils.div(buf.tradeAccountTokenCostETHHoldOfBuy, buf.tradeAccountAmount, 64) : "0";

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
                    String tradeAmount = isTradeAccountAmountNotEnough ? buf.tradeAccountAmount : amount;
                    buf.allAccountAmount = isAllAccountAmountNotEnough ? "0" : ArithmeticUtils.sub(buf.allAccountAmount, amount).toString();
                    buf.tradeAccountAmount = isTradeAccountAmountNotEnough ? "0" : ArithmeticUtils.sub(buf.tradeAccountAmount, amount).toString();
                    buf.tradeAccountReceiveSumOfTransferOut = ArithmeticUtils.add(buf.tradeAccountReceiveSumOfTransferOut, tradeAmount).toString();

                    String decrCost = ArithmeticUtils.mul(buf.holdPriceUsd, tradeAmount).toPlainString();
                    String decrCostCoin = ArithmeticUtils.mul(buf.holdPriceCoin, tradeAmount).toPlainString();
                    buf.tradeAccountTokenCostHoldOfBuy = ArithmeticUtils.compare(decrCost, buf.tradeAccountTokenCostHoldOfBuy) ? "0" : ArithmeticUtils.sub(buf.tradeAccountTokenCostHoldOfBuy, decrCost).toString();
                    buf.tradeAccountTokenCostETHHoldOfBuy = ArithmeticUtils.compare(decrCostCoin, buf.tradeAccountTokenCostETHHoldOfBuy) ? "0" : ArithmeticUtils.sub(buf.tradeAccountTokenCostETHHoldOfBuy, decrCostCoin).toString();

                    buf.holdPriceUsd = ArithmeticUtils.compare(buf.tradeAccountAmount, "0") ? ArithmeticUtils.div(buf.tradeAccountTokenCostHoldOfBuy, buf.tradeAccountAmount, 64) : "0" ;
                    buf.holdPriceCoin = ArithmeticUtils.compare(buf.tradeAccountAmount, "0") ? ArithmeticUtils.div(buf.tradeAccountTokenCostETHHoldOfBuy, buf.tradeAccountAmount, 64) : "0";

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
                    String tradeAmount = buf.tradeAccountAmount;
                    buf.tradeAccountAmount = "0";
                    // 累计 Sell 的 总 Cost、总 Amount。
                    String incrCost = "0";
                    String incrCostETH = "0";
                    buf.tradeAccountReceiveSumOfSell = ArithmeticUtils.add(buf.tradeAccountReceiveSumOfSell, incrCost).toString();
                    buf.tradeAccountReceiveETHSumOfSell = ArithmeticUtils.add(buf.tradeAccountReceiveETHSumOfSell, incrCostETH).toString();
                    buf.tradeAccountReceiveAmountSumOfSell = ArithmeticUtils.add(buf.tradeAccountReceiveAmountSumOfSell, tradeAmount).toString();

                    String decrCost = ArithmeticUtils.mul(buf.holdPriceUsd, tradeAmount).toPlainString();
                    String decrCostCoin = ArithmeticUtils.mul(buf.holdPriceCoin, tradeAmount).toPlainString();
                    buf.tradeAccountTokenCostHoldOfBuy = ArithmeticUtils.compare(decrCost, buf.tradeAccountTokenCostHoldOfBuy) ? "0" : ArithmeticUtils.sub(buf.tradeAccountTokenCostHoldOfBuy, decrCost).toString();
                    buf.tradeAccountTokenCostETHHoldOfBuy = ArithmeticUtils.compare(decrCostCoin, buf.tradeAccountTokenCostETHHoldOfBuy) ? "0" : ArithmeticUtils.sub(buf.tradeAccountTokenCostETHHoldOfBuy, decrCostCoin).toString();

                    buf.holdPriceUsd = ArithmeticUtils.compare(buf.tradeAccountAmount, "0") ? ArithmeticUtils.div(buf.tradeAccountTokenCostHoldOfBuy, buf.tradeAccountAmount, 64) : "0" ;
                    buf.holdPriceCoin = ArithmeticUtils.compare(buf.tradeAccountAmount, "0") ? ArithmeticUtils.div(buf.tradeAccountTokenCostETHHoldOfBuy, buf.tradeAccountAmount, 64) : "0";
                }

            }
            log.info("[iterate func end]: type={}, amount={}, price={}, tradeAccountAmount={}, AmountBuffer={}", type, amount, priceUSD, tradeAccountAmount, buf.toString());
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
        String res = buf.allAccountAmount + ","             // 总账号余额
                + buf.tradeAccountAmount + ","              // 交易账号余额
                + buf.tradePositionNum + ","                // 仓位
                + buf.tradeAccountTokenAmountSumOfBuy + "," // 累计买入 amount
                + buf.tradeAccountTokenCostSumOfBuy + ","   // 累计买入花费的 USD
                + buf.tradeAccountTokenCostETHSumOfBuy + ","// 累计买入花费的 ETH
                + buf.tradeAccountTokenCostGas + ","        // 累计花费的 gas, USD
                + buf.tradeAccountTokenCostGasETH + ","     // 累计 花费的 gas, ETH
                + buf.tradeAccountTokenSellCostGas + ","    // 累计卖出花费的 gas, usd
                + buf.tradeAccountTokenSellCostGasETH + ","  // 累计卖出花费的 gas, ETH
                + buf.tradeAccountReceiveSumOfSell + ","  // 累计卖出 usd
                + buf.tradeAccountReceiveETHSumOfSell + ","  // 累计卖出 ETH
                + buf.tradeAccountReceiveAmountSumOfSell + ","  // 累计卖出 usd
                + buf.tradeAccountReceiveSumOfTransferOut + ","  // 累计卖出 ETH
                + buf.tradeAccountTokenCostHoldOfBuy + "," // 持仓总花费, usd
                + buf.tradeAccountTokenCostETHHoldOfBuy + ","  // 持仓总花费, coin
                + buf.holdPriceUsd + "," // 持仓总花费, coin
                + buf.holdPriceCoin // 持仓总花费, coin
                ;
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
        buf.tradeAccountReceiveSumOfSell = ArithmeticUtils.add(buf.tradeAccountReceiveSumOfSell, p.tradeAccountReceiveSumOfSell).toString();
        buf.tradeAccountReceiveETHSumOfSell = ArithmeticUtils.add(buf.tradeAccountReceiveETHSumOfSell, p.tradeAccountReceiveETHSumOfSell).toString();
        buf.tradeAccountReceiveAmountSumOfSell = ArithmeticUtils.add(buf.tradeAccountReceiveAmountSumOfSell, p.tradeAccountReceiveAmountSumOfSell).toString();
        buf.tradeAccountReceiveSumOfTransferOut = ArithmeticUtils.add(buf.tradeAccountReceiveSumOfTransferOut, p.tradeAccountReceiveSumOfTransferOut).toString();
        buf.tradeAccountTokenCostGas = ArithmeticUtils.add(buf.tradeAccountTokenCostGas, p.tradeAccountTokenCostGas).toString();
        buf.tradeAccountTokenCostGasETH = ArithmeticUtils.add(buf.tradeAccountTokenCostGasETH, p.tradeAccountTokenCostGasETH).toString();
        buf.tradeAccountTokenSellCostGas = ArithmeticUtils.add(buf.tradeAccountTokenSellCostGas, p.tradeAccountTokenSellCostGas).toString();
        buf.tradeAccountTokenSellCostGasETH = ArithmeticUtils.add(buf.tradeAccountTokenSellCostGasETH, p.tradeAccountTokenSellCostGasETH).toString();
        buf.tradeAccountTokenCostHoldOfBuy = ArithmeticUtils.add(buf.tradeAccountTokenCostHoldOfBuy, p.tradeAccountTokenCostHoldOfBuy).toString();
        buf.tradeAccountTokenCostETHHoldOfBuy = ArithmeticUtils.add(buf.tradeAccountTokenCostETHHoldOfBuy, p.tradeAccountTokenCostETHHoldOfBuy).toString();
        buf.holdPriceUsd = ArithmeticUtils.compare(buf.tradeAccountAmount, "0") ? ArithmeticUtils.div(buf.tradeAccountTokenCostHoldOfBuy, buf.tradeAccountAmount, 64) : "0" ;
        buf.holdPriceCoin = ArithmeticUtils.compare(buf.tradeAccountAmount, "0") ? ArithmeticUtils.div(buf.tradeAccountTokenCostETHHoldOfBuy, buf.tradeAccountAmount, 64) : "0";
        log.info("[merge func]: buffer={}", buf);
    }
}
