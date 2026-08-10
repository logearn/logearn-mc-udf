package cn.xlystar.parse.ammswap;

import cn.xlystar.entity.TransferEvent;
import cn.xlystar.entity.UniswapEvent;
import cn.xlystar.helpers.ChainConfig;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
public class AMMSwapDataProcessFull {

    /**
     * 格式化最后的返回值
     */
    public static List<Map<String, String>> parseFullUniswap(String sender, ChainConfig conf, String logs, String hash, List<TransferEvent> tftxs, String price, Map<String, BigDecimal> mainTokenPriceList) throws IOException {
        // 解析
        List<UniswapEvent> uniswapEvents = parseAllUniSwapLogs(sender, conf, logs, hash, tftxs, price);
        // 后缀处理
        return processSwap(hash, uniswapEvents, new ArrayList<>(), conf, price, mainTokenPriceList, null, null);
    }

    /**
     * 格式化最后的返回值
     */
    public static List<Map<String, String>> parseSolanaSwap(ChainConfig conf, String originSender, String hash, List<UniswapEvent> swapEvents, List<TransferEvent> transferEvents, String price, List<TransferEvent> transferOwnerEvents, List<TokenBalance> postTokenBalance, List<TokenBalance> preTokenBalance, Map<String, BigDecimal> mainTokenPriceList, BigInteger fee, Map<String, Object> tip) {
        String wCoinAddress = conf.getWCoinAddress();
        List<UniswapEvent> uniswapEvents = new ArrayList<>();
//        if (!CollectionUtils.isEmpty(transferOwnerEvents)) {
//            List<TransferEvent> list = transferOwnerEvents.stream().filter(t -> t.getAmount().compareTo(BigInteger.ZERO) > 0).toList();
//            transferOwnerEvents.removeAll(list);
//            transferEvents.addAll(list);
//        }
        boolean hasTokenTransfer = transferEvents != null && transferEvents.stream().anyMatch(t -> !t.getContractAddress().equals(wCoinAddress));
        if (!hasTokenTransfer && CollectionUtils.isEmpty(swapEvents) && !CollectionUtils.isEmpty(transferOwnerEvents)) {
            uniswapEvents = getTranferOwnerEvents(conf, transferOwnerEvents, postTokenBalance, preTokenBalance);
        } else {
            // 解析 solana swap
            uniswapEvents = getSolanaSwapEvents(conf, originSender, hash, swapEvents, transferEvents);
//            uniswapEvents = updateSwapEventsFromTransferOwner(conf, transferOwnerEvents, postTokenBalance, preTokenBalance, uniswapEvents);
        }

        // 后缀处理
        return processSwap(hash, uniswapEvents, new ArrayList<>(), conf, price, mainTokenPriceList, fee, tip);
    }

    private static List<UniswapEvent> updateSwapEventsFromTransferOwner(ChainConfig conf, List<TransferEvent> transferOwnerEvents, List<Map<String, Object>> postTokenBalance, List<Map<String, Object>> preTokenBalance, List<UniswapEvent> uniswapEvents) {
        if (CollectionUtils.isEmpty(transferOwnerEvents) || CollectionUtils.isEmpty(uniswapEvents))
            return uniswapEvents;

        // 创建映射: (accountIndex, mint) -> TokenBalance
        List<TokenBalance> preBalanceList = new ArrayList<>();
        for (Map<String, Object> balance : preTokenBalance) {
            preBalanceList.add(TokenBalance.builder()
                    .account(balance.get("account").toString())
                    .mint(balance.get("mint").toString())
                    .owner(balance.get("owner").toString())
                    .programId(balance.get("programId").toString())
                    .uiTokenAmount(
                            UiTokenAmount.builder()
                                    .amount(balance.get("amount").toString())
                                    .decimals(Integer.parseInt(balance.get("decimals").toString()))
                                    .build())
                    .build());
        }
        List<TokenBalance> postBalanceList = new ArrayList<>();
        for (Map<String, Object> balance : postTokenBalance) {
            postBalanceList.add(TokenBalance.builder()
                    .account(balance.get("account").toString())
                    .mint(balance.get("mint").toString())
                    .owner(balance.get("owner").toString())
                    .programId(balance.get("programId").toString())
                    .uiTokenAmount(
                            UiTokenAmount.builder()
                                    .amount(balance.get("amount").toString())
                                    .decimals(Integer.parseInt(balance.get("decimals").toString()))
                                    .build())
                    .build());
        }

        List<UniswapEvent> finalUniswapEvents = new ArrayList<>();
        postBalanceList.forEach(pre -> {
            preBalanceList.stream().filter(post ->
                    post.getAccount().equals(pre.getAccount())
                            && post.getMint().equals(pre.getMint())
                            && !post.getOwner().equals(pre.getOwner())
                            && transferOwnerEvents.stream().anyMatch(t -> t.getReceiverOrigin().equals(post.getAccount()))
                            && transferOwnerEvents.stream().anyMatch(t -> t.getReceiver().equals(post.getOwner()))
            ).findFirst().ifPresent(post -> {
                finalUniswapEvents.add(UniswapEvent.builder()
                        .sender(pre.getOwner())
                        .oriSender(pre.getOwner())
                        .to(post.getOwner())
                        .tokenIn(post.getMint())
                        .tokenOut(conf.getWCoinAddress())
                        .amountIn(new BigInteger(post.getUiTokenAmount().getAmount()))
                        .amountOut(BigInteger.ZERO)
                        .programId(post.getProgramId())
                        .logIndex(new BigInteger(String.valueOf(10_100)))
                        .innerIndex(100)
                        .outerIndex(10_000)
                        .build());
            });
        });
        if (CollectionUtils.isEmpty(finalUniswapEvents)) return finalUniswapEvents;

        return finalUniswapEvents;
    }

    private static List<UniswapEvent> getTranferOwnerEvents(ChainConfig conf, List<TransferEvent> transferOwnerEvents, List<TokenBalance> postTokenBalance, List<TokenBalance> preTokenBalance) {
        // 创建映射: (accountIndex, mint) -> TokenBalance
        List<UniswapEvent> uniswapEvents = new ArrayList<>();
        postTokenBalance.forEach(post -> {
            if (post.getMint().equals(conf.getWCoinAddress())) return;
            preTokenBalance.stream().filter(pre ->
                    post.getAccount().equals(pre.getAccount())
                            && post.getMint().equals(pre.getMint())
                            && !post.getOwner().equals(pre.getOwner())
                            && transferOwnerEvents.stream().anyMatch(t -> t.getReceiverOrigin().equals(post.getAccount()))
                            && transferOwnerEvents.stream().anyMatch(t -> t.getReceiver().equals(post.getOwner()))
            ).findFirst().ifPresent(pre -> {
                uniswapEvents.add(UniswapEvent.builder()
                        .sender(pre.getOwner())
                        .oriSender(pre.getOwner())
                        .to(post.getOwner())
                        .tokenIn(post.getMint())
                        .tokenOut(conf.getWCoinAddress())
                        .amountIn(new BigInteger(post.getUiTokenAmount().getAmount()))
                        .amountOut(BigInteger.ZERO)
                        .programId(post.getProgramId())
                        .logIndex(new BigInteger(String.valueOf(10_100)))
                        .pair(Lists.newArrayList())
                        .rawSwapLog(Lists.newArrayList())
                        .innerIndex(100)
                        .outerIndex(10_000)
                        .build());
            });
        });
        return uniswapEvents;
    }

    private static List<Map<String, String>> processSwap(String hash, List<UniswapEvent> swapEvents, List<Map<String, String>> finalSwap, ChainConfig conf, final String price, Map<String, BigDecimal> mainTokenPriceList, BigInteger fee, Map<String, Object> tip) {
        String wCoinAddress = conf.getWCoinAddress();
        if (CollectionUtils.isEmpty(swapEvents)) return finalSwap;
        Map<String, BigDecimal> tokenPriceList = new HashMap<>();
        UniswapEvent swap = swapEvents.get(0);
        try {
            if (!CollectionUtils.isEmpty(swap.getRawSwapLog()) && !StringUtils.isEmpty(price)) {
                if (conf.getChainId().equals("3")) {
                    swap.getRawSwapLog().forEach(u -> convertToSol(conf, u, new BigDecimal(price)));
                } else {
                    // 稳定币对处理
                    swap.getRawSwapLog().forEach(u -> convertToNative(conf, u, new BigDecimal(price), mainTokenPriceList));
                    // 非稳定币对处理
                    convertPlatformToNative(swap.getRawSwapLog(), conf);
                }
            }
            swap.getRawSwapLog().forEach(t -> {
                if (t.getTokenIn() == null || t.getTokenOut() == null) return;
                if (t.getAmountIn().compareTo(BigInteger.ZERO) <= 0 || t.getAmountOut().compareTo(BigInteger.ZERO) <= 0)
                    return;
                if ((!t.getTokenIn().equals(wCoinAddress) && !t.getTokenOut().equals(wCoinAddress))) return;
                BigInteger wcoinAmount = t.getTokenIn().equals(wCoinAddress) ? t.getAmountIn() : t.getAmountOut();
                BigInteger tokenAmount = t.getTokenIn().equals(wCoinAddress) ? t.getAmountOut() : t.getAmountIn();
                String tokenAddress = t.getTokenIn().equals(wCoinAddress) ? t.getTokenOut() : t.getTokenIn();
                BigDecimal tokenPrice = new BigDecimal(wcoinAmount).divide(new BigDecimal(tokenAmount.toString()), 20, RoundingMode.HALF_UP);
                tokenPriceList.put(tokenAddress, tokenPrice);
            });
            String tips = tip != null ? tip.get("amount").toString() : "0";
            String tipPlatform = tip != null ? tip.get("tips_platform").toString() : null;
            swapEvents.forEach(t -> {
                        t.setTips(tips);
                        t.setTipPlatform(tipPlatform);
                        t.setGasFee(fee);
                        if (t.getTokenIn() != null && t.getTokenOut() != null && !t.getTokenIn().equals(conf.getWCoinAddress()) && !t.getTokenOut().equals(conf.getWCoinAddress())) {
                            switch (conf.getChainId()) {
                                case "3":
                                    if (convertToSol(conf, t, new BigDecimal(price))) break;
                                case "56":
                                    if (convertToNative(conf, t, new BigDecimal(price), mainTokenPriceList)) break;
                                default:
                                    List<UniswapEvent> connectedPools = t.getConnectedPools();
                                    if (conf.getChainId().equals("3") && t.isAggregator()) {
                                        UniswapEvent.UniswapEventBuilder ethBuyUniswap = null;
                                        UniswapEvent.UniswapEventBuilder ethSellUniswap = null;
                                        String sellToken = t.getTokenIn();
                                        BigDecimal sellPrice = tokenPriceList.getOrDefault(sellToken, BigDecimal.ZERO);
                                        BigInteger receiveCoin = new BigDecimal(t.getAmountIn()).multiply(sellPrice).toBigInteger();
                                        if (receiveCoin.compareTo(BigInteger.ZERO) > 0) {
                                            ethSellUniswap = UniswapEvent.builder()
                                                    .sender(t.getSender())
                                                    .to(t.getSender())
                                                    .tokenIn(t.getTokenIn())
                                                    .amountIn(t.getAmountIn())
                                                    .tokenOut(conf.getWCoinAddress())
                                                    .amountOut(receiveCoin)
                                                    .pair(t.getPair())
                                                    .fromMergedTransferEvent(t.getFromMergedTransferEvent())
                                                    .toMergedTransferEvent(t.getToMergedTransferEvent())
                                                    .connectedPools(t.getConnectedPools())
                                                    .rawSwapLog(t.getRawSwapLog())
                                                    .version(t.getVersion())
                                                    .gasFee(fee)
                                                    .tips(tips)
                                                    .tipPlatform(tipPlatform)
                                                    .errorMsg(t.getErrorMsg());
                                        } else {
                                            ethSellUniswap = UniswapEvent.builder()
                                                    .sender(t.getSender())
                                                    .to("0x0")
                                                    .tokenIn(t.getTokenIn())
                                                    .amountIn(t.getAmountIn())
                                                    .tokenOut(conf.getWCoinAddress())
                                                    .amountOut(BigInteger.ZERO)
                                                    .pair(t.getPair())
                                                    .fromMergedTransferEvent(t.getFromMergedTransferEvent())
                                                    .toMergedTransferEvent(t.getToMergedTransferEvent())
                                                    .connectedPools(t.getConnectedPools())
                                                    .rawSwapLog(t.getRawSwapLog())
                                                    .version(t.getVersion())
                                                    .gasFee(fee)
                                                    .tips(tips)
                                                    .tipPlatform(tipPlatform)
                                                    .errorMsg(t.getErrorMsg());
                                        }
                                        finalSwap.add(swapResultStruct(conf, ethSellUniswap.build()));

                                        // buy eth币对
                                        String buyToken = t.getTokenOut();
                                        BigDecimal buyPrice = tokenPriceList.getOrDefault(buyToken, BigDecimal.ZERO);
                                        BigInteger costCoin = new BigDecimal(t.getAmountOut()).multiply(buyPrice).toBigInteger();
                                        if (costCoin.compareTo(BigInteger.ZERO) > 0) {
                                            ethBuyUniswap = UniswapEvent.builder()
                                                    .sender(t.getTo())
                                                    .to(t.getTo())
                                                    .tokenIn(conf.getWCoinAddress())
                                                    .amountIn(costCoin)
                                                    .tokenOut(t.getTokenOut())
                                                    .amountOut(t.getAmountOut())
                                                    .pair(t.getPair())
                                                    .fromMergedTransferEvent(t.getFromMergedTransferEvent())
                                                    .toMergedTransferEvent(t.getToMergedTransferEvent())
                                                    .connectedPools(t.getConnectedPools())
                                                    .rawSwapLog(t.getRawSwapLog())
                                                    .version(t.getVersion())
                                                    .gasFee(fee)
                                                    .tips(tips)
                                                    .tipPlatform(tipPlatform)
                                                    .errorMsg(t.getErrorMsg());
                                        } else {
                                            ethBuyUniswap = UniswapEvent.builder()
                                                    .sender("0x0")
                                                    .to(t.getTo())
                                                    .tokenIn(t.getTokenOut())
                                                    .amountIn(t.getAmountOut())
                                                    .tokenOut(conf.getWCoinAddress())
                                                    .amountOut(BigInteger.ZERO)
                                                    .pair(t.getPair())
                                                    .fromMergedTransferEvent(t.getFromMergedTransferEvent())
                                                    .toMergedTransferEvent(t.getToMergedTransferEvent())
                                                    .connectedPools(t.getConnectedPools())
                                                    .rawSwapLog(t.getRawSwapLog())
                                                    .version(t.getVersion())
                                                    .gasFee(fee)
                                                    .tips(tips)
                                                    .tipPlatform(tipPlatform)
                                                    .errorMsg(t.getErrorMsg());
                                        }
                                        finalSwap.add(swapResultStruct(conf, ethBuyUniswap.build()));
                                        return;
                                    } else if (connectedPools != null && connectedPools.size() > 0) {
                                        UniswapEvent.UniswapEventBuilder ethBuyUniswap = null;
                                        UniswapEvent.UniswapEventBuilder ethSellUniswap = null;
                                        for (int i = 0; i < connectedPools.size(); i++) {
                                            UniswapEvent uniswapEvent = connectedPools.get(i);
                                            if (uniswapEvent.getTokenOut().equals(conf.getWCoinAddress())) {
                                                // sell
                                                ethSellUniswap = UniswapEvent.builder()
                                                        .sender(t.getSender())
                                                        .to(t.getSender())
                                                        .tokenIn(t.getTokenIn())
                                                        .amountIn(t.getAmountIn())
                                                        .tokenOut(conf.getWCoinAddress())
                                                        .amountOut(uniswapEvent.getAmountOut())
                                                        .pair(t.getPair())
                                                        .fromMergedTransferEvent(t.getFromMergedTransferEvent())
                                                        .toMergedTransferEvent(t.getToMergedTransferEvent())
                                                        .connectedPools(t.getConnectedPools())
                                                        .rawSwapLog(t.getRawSwapLog())
                                                        .version(t.getVersion())
                                                        .gasFee(fee)
                                                        .tips(tips)
                                                        .tipPlatform(tipPlatform)
                                                        .errorMsg(t.getErrorMsg());
                                                finalSwap.add(swapResultStruct(conf, ethSellUniswap.build()));

                                                // buy eth币对
                                                ethBuyUniswap = UniswapEvent.builder()
                                                        .sender(t.getSender())
                                                        .to(t.getTo())
                                                        .tokenIn(conf.getWCoinAddress())
                                                        .amountIn(uniswapEvent.getAmountOut())
                                                        .tokenOut(t.getTokenOut())
                                                        .amountOut(t.getAmountOut())
                                                        .pair(t.getPair())
                                                        .fromMergedTransferEvent(t.getFromMergedTransferEvent())
                                                        .toMergedTransferEvent(t.getToMergedTransferEvent())
                                                        .connectedPools(t.getConnectedPools())
                                                        .rawSwapLog(t.getRawSwapLog())
                                                        .gasFee(fee)
                                                        .tips(tips)
                                                        .tipPlatform(tipPlatform)
                                                        .version(t.getVersion())
                                                        .errorMsg(t.getErrorMsg());

                                                finalSwap.add(swapResultStruct(conf, ethBuyUniswap.build()));
                                                return;
                                            }

                                        }
                                    }
                                    // 不存在 coin 的池子
                                    t.setAmountOut(BigInteger.ZERO);
                                    t.setTokenOut(conf.getWCoinAddress());
                            }
                        }

                        finalSwap.add(swapResultStruct(conf, t));
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
            log.error("processSwap error {} {}", e, hash);
        }
        Set<Integer> logIndexRange = new HashSet<>();
        int minLogIndex = Integer.MAX_VALUE;
        for (int i = 0; i < finalSwap.size(); i++) {
            Map<String, String> entry = finalSwap.get(i);
            if (entry.get("logIndex") == null) continue;

            Integer logIndex = Integer.valueOf(entry.get("logIndex"));
            logIndexRange.add(logIndex);
            minLogIndex = Math.min(minLogIndex, logIndex);
        }

        // 设定初始的 logIndex 值，从 0 开始自增
        int nextLogIndex = 0;
        for (int i = 0; i < finalSwap.size(); i++) {
            Map<String, String> map = finalSwap.get(i);
            if (map.get("logIndex") == null) {
                // 找到不重复的 logIndex 值
                while (logIndexRange.contains(nextLogIndex)) {
                    nextLogIndex++;
                }
                // 给当前 map 设置 logIndex 值
                map.put("logIndex", String.valueOf(nextLogIndex));
                // 更新 logIndexRange
                logIndexRange.add(nextLogIndex);
                nextLogIndex++;
            }
        }

        // 删除有问题的 swap
        for (int i = 0, len = finalSwap.size(); i < len; i++) {
            Map<String, String> t = finalSwap.get(i);
            if (t.get("errorMsg") != null || t.get("tokenIn") == null || t.get("tokenOut") == null
                    || (t.get("eventType").equals("transferOut") && t.get("caller").equals(t.get("to")))
            ) {
                finalSwap.remove(i);
                len--;
                i--;
            }
        }
        return finalSwap;
    }

    private static boolean convertToSol(ChainConfig conf, UniswapEvent t, BigDecimal price) {
        String wcoinDecimals = conf.getTokens().get(conf.getChainConf().get("wcoin").asText()).get("scale").asText();
        String wcoinAddress = conf.getTokens().get(conf.getChainConf().get("wcoin").asText()).get("address").asText();
        if (t.getTokenIn() == null || t.getTokenOut() == null || t.getTokenIn().equals(wcoinAddress) || t.getTokenOut().equals(wcoinAddress))
            return false;
        String usdcAddress = conf.getTokens().get("USDC").get("address").asText();
        String usdcDecimals = conf.getTokens().get("USDC").get("scale").asText();
        String usdtAddress = conf.getTokens().get("USDT").get("address").asText();
        String usdtDecimals = conf.getTokens().get("USDT").get("scale").asText();
        String usd1Address = conf.getTokens().get("USD1").get("address").asText();
        String usd1Decimals = conf.getTokens().get("USD1").get("scale").asText();
        boolean hasUsdc = usdcAddress.equals(t.getTokenIn()) || usdcAddress.equals(t.getTokenOut());
        boolean hasUsdt = usdtAddress.equals(t.getTokenIn()) || usdtAddress.equals(t.getTokenOut());
        boolean hasUsd1 = usd1Address.equals(t.getTokenIn()) || usd1Address.equals(t.getTokenOut());

        String tokenAddress = "";
        String decimals = "";

        if (hasUsdc) {
            tokenAddress = usdcAddress;
            decimals = usdcDecimals;
        } else if (hasUsdt) {
            tokenAddress = usdtAddress;
            decimals = usdtDecimals;
        } else if (hasUsd1) {
            tokenAddress = usd1Address;
            decimals = usd1Decimals;
        }

        if (StringUtils.isEmpty(tokenAddress)) return false;

        BigInteger tokenAmount = t.getTokenIn().equals(tokenAddress) ? t.getAmountIn() : t.getAmountOut();
        BigInteger wcoinAmount = new BigDecimal(tokenAmount).divide(price, 20, RoundingMode.HALF_UP).multiply(new BigDecimal(wcoinDecimals)).divide(new BigDecimal(decimals)).toBigInteger();
        if (t.getTokenIn().equals(tokenAddress)) {
            t.setTokenIn(conf.getWCoinAddress());
            t.setAmountIn(wcoinAmount);
        } else {
            t.setTokenOut(conf.getWCoinAddress());
            t.setAmountOut(wcoinAmount);
        }

        return true;
    }

    public static Map<String, BigDecimal> computePlatformPrice(ChainConfig conf, List<UniswapEvent> list) {
        Map<String, BigDecimal> platformPrice = new HashMap<>();
        list.forEach(t -> {
            String wcoinAddress = conf.getWCoinAddress();
            if (t.getTokenIn() == null || t.getTokenOut() == null || wcoinAddress.equals(t.getTokenIn()) || wcoinAddress.equals(t.getTokenOut()))
                return;
            if (!conf.getPlatformAddressLists().contains(t.getTokenIn()) && !conf.getPlatformAddressLists().contains(t.getTokenOut()))
                return;

            String tokenAddress = conf.getPlatformAddressLists().contains(t.getTokenOut()) ? t.getTokenOut() : t.getTokenIn();
            platformPrice.put(tokenAddress, conf.getPlatformAddressLists().contains(t.getTokenOut()) ? new BigDecimal(t.getAmountIn()).divide(new BigDecimal(t.getAmountOut()), 20, RoundingMode.HALF_UP) : new BigDecimal(t.getAmountOut()).divide(new BigDecimal(t.getAmountIn()), 20, RoundingMode.HALF_UP));
        });
        return platformPrice;
    }

    public static void convertPlatformToNative(List<UniswapEvent> list, ChainConfig conf) {
        Map<String, BigDecimal> platformPrice = computePlatformPrice(conf, list);
        String wcoin = conf.getChainConf().get("wcoin").asText();
        String wcoinDecimals = conf.getTokens().get(wcoin).get("scale").asText();
        String wcoinAddress = conf.getWCoinAddress();
        if (platformPrice.isEmpty()) return;
        list.forEach(t -> {
            if (t.getTokenIn() == null || t.getTokenOut() == null || wcoinAddress.equals(t.getTokenIn()) || wcoinAddress.equals(t.getTokenOut()))
                return;
            if (!platformPrice.containsKey(t.getTokenIn()) && !platformPrice.containsKey(t.getTokenOut()))
                return;
            String tokenAddress = "";
            String decimals = "";
            BigDecimal price = null;
            if (platformPrice.containsKey(t.getTokenIn())) {
                tokenAddress = t.getTokenIn();
                decimals = conf.getPlatformTokens().get(tokenAddress).get("scale").asText();
                price = platformPrice.get(tokenAddress);
            } else if (platformPrice.containsKey(t.getTokenOut())) {
                tokenAddress = t.getTokenOut();
                decimals = conf.getPlatformTokens().get(tokenAddress).get("scale").asText();
                price = platformPrice.get(tokenAddress);
            }

            if (StringUtils.isEmpty(tokenAddress) || price == null) return;

            BigInteger tokenAmount = t.getTokenIn().equals(tokenAddress) ? t.getAmountIn() : t.getAmountOut();
            BigInteger wcoinAmount = new BigDecimal(tokenAmount).divide(price, 20, RoundingMode.HALF_UP).multiply(new BigDecimal(wcoinDecimals)).divide(new BigDecimal(decimals)).toBigInteger();
            if (t.getTokenIn().equals(tokenAddress)) {
                t.setTokenIn(conf.getWCoinAddress());
                t.setAmountIn(wcoinAmount);
            } else {
                t.setTokenOut(conf.getWCoinAddress());
                t.setAmountOut(wcoinAmount);
            }

        });

    }


    private static boolean convertToNative(ChainConfig conf, UniswapEvent t, BigDecimal price, Map<String, BigDecimal> tokenPriceList) {
        String wcoin = conf.getChainConf().get("wcoin").asText();
        String wcoinDecimals = conf.getTokens().get(wcoin).get("scale").asText();
        String wcoinAddress = conf.getTokens().get(wcoin).get("address").asText();
        if (t.getTokenIn() == null || t.getTokenOut() == null || t.getTokenIn().equals(wcoinAddress) || t.getTokenOut().equals(wcoinAddress))
            return false;
        JsonNode usdc = conf.getTokens().get("USDC");
        JsonNode usdt = conf.getTokens().get("USDT");
        JsonNode usd1 = conf.getTokens().get("USD1");
        JsonNode cake = conf.getTokens().get("CAKE");
        JsonNode aster = conf.getTokens().get("ASTER");
        JsonNode btc = conf.getTokens().get("BTCB");
        JsonNode spcx = conf.getTokens().get("SPCXB");
        JsonNode nvda = conf.getTokens().get("NVDAB");
        JsonNode tsla = conf.getTokens().get("TSLAB");
        JsonNode amd = conf.getTokens().get("AMDB");
        JsonNode mu = conf.getTokens().get("MUB");
        JsonNode qqq = conf.getTokens().get("QQQB");
        JsonNode skhy = conf.getTokens().get("SKHYB");
        JsonNode spyb = conf.getTokens().get("SPYB");
        JsonNode aapl = conf.getTokens().get("AAPLB");
        JsonNode xaut = conf.getTokens().get("XAUTB");
        JsonNode u = conf.getTokens().get("U");
        JsonNode four = conf.getTokens().get("FOUR");
        boolean hasUsdc = false;
        boolean hasUsdt = false;
        boolean hasUsd1 = false;
        boolean hasCake = false;
        boolean hasAster = false;
        boolean hasU = false;
        boolean hasFour = false;
        boolean hasBtc = false;
        boolean hasSpcx = false;
        boolean hasTsla = false;
        boolean hasNvda = false;
        boolean hasAmd = false;
        boolean hasMu = false;
        boolean hasQqq = false;
        boolean hasSkhy = false;
        boolean hasSpyb = false;
        boolean hasAapl = false;
        boolean hasXaut = false;
        String skhyAddress = "";
        String skhyDecimals = "";
        String xautAddress = "";
        String xautDecimals = "";
        String spybAddress = "";
        String spybDecimals = "";
        String aaplAddress = "";
        String aaplDecimals = "";
        String usdcDecimals = "";
        String usdcAddress = "";
        String usdtDecimals = "";
        String usdtAddress = "";
        String uDecimals = "";
        String uAddress = "";
        String usd1Decimals = "";
        String usd1Address = "";
        String cakeDecimals = "";
        String cakeAddress = "";
        String asterDecimals = "";
        String asterAddress = "";
        String spcxDecimals = "";
        String spcxAddress = "";
        String nvdaDecimals = "";
        String nvdaAddress = "";
        String tslaDecimals = "";
        String tslaAddress = "";
        String amdDecimals = "";
        String amdAddress = "";
        String muDecimals = "";
        String muAddress = "";
        String qqqDecimals = "";
        String qqqAddress = "";
        String btcDecimals = "";
        String btcAddress = "";
        String fourDecimals = "";
        String fourAddress = "";
        if (usdc != null) {
            usdcAddress = usdc.get("address").asText();
            usdcDecimals = usdc.get("scale").asText();
            hasUsdc = usdcAddress.equals(t.getTokenIn()) || usdcAddress.equals(t.getTokenOut());
        }
        if (usd1 != null) {
            usd1Address = usd1.get("address").asText();
            usd1Decimals = usd1.get("scale").asText();
            hasUsd1 = usd1Address.equals(t.getTokenIn()) || usd1Address.equals(t.getTokenOut());
        }
        if (usdt != null) {
            usdtAddress = usdt.get("address").asText();
            usdtDecimals = usdt.get("scale").asText();
            hasUsdt = usdtAddress.equals(t.getTokenIn()) || usdtAddress.equals(t.getTokenOut());
        }
        if (u != null) {
            uAddress = u.get("address").asText();
            uDecimals = u.get("scale").asText();
            hasU = uAddress.equals(t.getTokenIn()) || uAddress.equals(t.getTokenOut());
        }
        if (btc != null) {
            btcAddress = btc.get("address").asText();
            hasBtc = btcAddress.equals(t.getTokenIn()) || btcAddress.equals(t.getTokenOut());
            BigDecimal mainPrice = tokenPriceList.get("BTC");

            if (mainPrice == null) hasBtc = false;
            if (hasBtc) {
                price = price.divide(mainPrice, 20, RoundingMode.HALF_UP);
                btcDecimals = btc.get("scale").asText();
            }
        }
        if (tsla != null) {
            tslaAddress = tsla.get("address").asText();
            hasTsla = tslaAddress.equals(t.getTokenIn()) || tslaAddress.equals(t.getTokenOut());
            BigDecimal mainPrice = tokenPriceList.get("TSLAB");

            if (mainPrice == null) hasTsla = false;
            if (hasTsla) {
                price = price.divide(mainPrice, 20, RoundingMode.HALF_UP);
                tslaDecimals = tsla.get("scale").asText();
            }
        }
        if (qqq != null) {
            qqqAddress = qqq.get("address").asText();
            hasQqq = qqqAddress.equals(t.getTokenIn()) || qqqAddress.equals(t.getTokenOut());
            BigDecimal mainPrice = tokenPriceList.get("QQQB");

            if (mainPrice == null) hasQqq = false;
            if (hasQqq) {
                price = price.divide(mainPrice, 20, RoundingMode.HALF_UP);
                qqqDecimals = qqq.get("scale").asText();
            }
        }
        if (xaut != null) {
            xautAddress = xaut.get("address").asText();
            hasXaut = xautAddress.equals(t.getTokenIn()) || xautAddress.equals(t.getTokenOut());
            BigDecimal mainPrice = tokenPriceList.get("XAUTB");

            if (mainPrice == null) hasXaut = false;
            if (hasXaut) {
                price = price.divide(mainPrice, 20, RoundingMode.HALF_UP);
                xautDecimals = xaut.get("scale").asText();
            }
        }
        if (spcx != null) {
            spcxAddress = spcx.get("address").asText();
            hasSpcx = spcxAddress.equals(t.getTokenIn()) || spcxAddress.equals(t.getTokenOut());
            BigDecimal mainPrice = tokenPriceList.get("SPCXB");

            if (mainPrice == null) hasSpcx = false;
            if (hasSpcx) {
                price = price.divide(mainPrice, 20, RoundingMode.HALF_UP);
                spcxDecimals = spcx.get("scale").asText();
            }
        }
        if (nvda != null) {
            nvdaAddress = nvda.get("address").asText();
            hasNvda = nvdaAddress.equals(t.getTokenIn()) || nvdaAddress.equals(t.getTokenOut());
            BigDecimal mainPrice = tokenPriceList.get("NVDAB");

            if (mainPrice == null) hasNvda = false;
            if (hasNvda) {
                price = price.divide(mainPrice, 20, RoundingMode.HALF_UP);
                nvdaDecimals = nvda.get("scale").asText();
            }
        }
        if (amd != null) {
            amdAddress = amd.get("address").asText();
            hasAmd = amdAddress.equals(t.getTokenIn()) || amdAddress.equals(t.getTokenOut());
            BigDecimal mainPrice = tokenPriceList.get("AMDB");

            if (mainPrice == null) hasAmd = false;
            if (hasAmd) {
                price = price.divide(mainPrice, 20, RoundingMode.HALF_UP);
                amdDecimals = amd.get("scale").asText();
            }
        }
        if (mu != null) {
            muAddress = mu.get("address").asText();
            hasMu = muAddress.equals(t.getTokenIn()) || muAddress.equals(t.getTokenOut());
            BigDecimal mainPrice = tokenPriceList.get("MUB");

            if (mainPrice == null) hasMu = false;
            if (hasMu) {
                price = price.divide(mainPrice, 20, RoundingMode.HALF_UP);
                muDecimals = mu.get("scale").asText();
            }
        }
        if (skhy != null) {
            skhyAddress = skhy.get("address").asText();
            hasSkhy = skhyAddress.equals(t.getTokenIn()) || skhyAddress.equals(t.getTokenOut());
            BigDecimal mainPrice = tokenPriceList.get("SKHYB");

            if (mainPrice == null) hasSkhy = false;
            if (hasSkhy) {
                price = price.divide(mainPrice, 20, RoundingMode.HALF_UP);
                skhyDecimals = skhy.get("scale").asText();
            }
        }
        if (spyb != null) {
            spybAddress = spyb.get("address").asText();
            hasSpyb = spybAddress.equals(t.getTokenIn()) || spybAddress.equals(t.getTokenOut());
            BigDecimal mainPrice = tokenPriceList.get("SPYB");

            if (mainPrice == null) hasSpyb = false;
            if (hasSpyb) {
                price = price.divide(mainPrice, 20, RoundingMode.HALF_UP);
                spybDecimals = spyb.get("scale").asText();
            }
        }
        if (aapl != null) {
            aaplAddress = aapl.get("address").asText();
            hasAapl = aaplAddress.equals(t.getTokenIn()) || aaplAddress.equals(t.getTokenOut());
            BigDecimal mainPrice = tokenPriceList.get("AAPLB");

            if (mainPrice == null) hasAapl = false;
            if (hasAapl) {
                price = price.divide(mainPrice, 20, RoundingMode.HALF_UP);
                aaplDecimals = aapl.get("scale").asText();
            }
        }
        if (aster != null) {
            asterAddress = aster.get("address").asText();
            hasAster = asterAddress.equals(t.getTokenIn()) || asterAddress.equals(t.getTokenOut());
            BigDecimal mainPrice = tokenPriceList.get("ASTER");

            if (mainPrice == null) hasAster = false;
            if (hasAster) {
                price = price.divide(mainPrice, 20, RoundingMode.HALF_UP);
                asterDecimals = aster.get("scale").asText();
            }

        }
        if (four != null) {
            fourAddress = four.get("address").asText();
            hasFour = fourAddress.equals(t.getTokenIn()) || fourAddress.equals(t.getTokenOut());
            BigDecimal mainPrice = tokenPriceList.get("FOUR");

            if (mainPrice == null) hasFour = false;
            if (hasFour) {
                price = price.divide(mainPrice, 20, RoundingMode.HALF_UP);
                fourDecimals = four.get("scale").asText();
            }
        }
        if (cake != null) {
            cakeAddress = cake.get("address").asText();
            hasCake = cakeAddress.equals(t.getTokenIn()) || cakeAddress.equals(t.getTokenOut());
            BigDecimal mainPrice = tokenPriceList.get("CAKE");

            if (mainPrice == null) hasCake = false;
            if (hasCake) {
                price = price.divide(mainPrice, 20, RoundingMode.HALF_UP);
                cakeDecimals = cake.get("scale").asText();
            }
        }

        String tokenAddress = "";
        String decimals = "";

        if (hasUsdc) {
            tokenAddress = usdcAddress;
            decimals = usdcDecimals;
        } else if (hasUsdt) {
            tokenAddress = usdtAddress;
            decimals = usdtDecimals;
        } else if (hasUsd1) {
            tokenAddress = usd1Address;
            decimals = usd1Decimals;
        } else if (hasU) {
            tokenAddress = uAddress;
            decimals = uDecimals;
        } else if (hasBtc) {
            tokenAddress = btcAddress;
            decimals = btcDecimals;
        } else if (hasSpcx) {
            tokenAddress = spcxAddress;
            decimals = spcxDecimals;
        } else if (hasXaut) {
            tokenAddress = xautAddress;
            decimals = xautDecimals;
        } else if (hasTsla) {
            tokenAddress = tslaAddress;
            decimals = tslaDecimals;
        } else if (hasQqq) {
            tokenAddress = qqqAddress;
            decimals = qqqDecimals;
        } else if (hasSkhy) {
            tokenAddress = skhyAddress;
            decimals = skhyDecimals;
        } else if (hasSpyb) {
            tokenAddress = spybAddress;
            decimals = spybDecimals;
        } else if (hasAapl) {
            tokenAddress = aaplAddress;
            decimals = aaplDecimals;
        } else if (hasNvda) {
            tokenAddress = nvdaAddress;
            decimals = nvdaDecimals;
        } else if (hasAmd) {
            tokenAddress = amdAddress;
            decimals = amdDecimals;
        } else if (hasMu) {
            tokenAddress = muAddress;
            decimals = muDecimals;
        } else if (hasAster) {
            tokenAddress = asterAddress;
            decimals = asterDecimals;
        } else if (hasFour) {
            tokenAddress = fourAddress;
            decimals = fourDecimals;
        } else if (hasCake) {
            tokenAddress = cakeAddress;
            decimals = cakeDecimals;
        }

        if (StringUtils.isEmpty(tokenAddress)) return false;

        BigInteger tokenAmount = t.getTokenIn().equals(tokenAddress) ? t.getAmountIn() : t.getAmountOut();
        BigInteger wcoinAmount = new BigDecimal(tokenAmount).divide(price, 20, RoundingMode.HALF_UP).multiply(new BigDecimal(wcoinDecimals)).divide(new BigDecimal(decimals)).toBigInteger();
        if (t.getTokenIn().equals(tokenAddress)) {
            t.setTokenIn(conf.getWCoinAddress());
            t.setAmountIn(wcoinAmount);
        } else {
            t.setTokenOut(conf.getWCoinAddress());
            t.setAmountOut(wcoinAmount);
        }

        return true;
    }



    private static HashMap<String, String> swapResultStruct(ChainConfig conf, UniswapEvent swap) {
        HashMap<String, String> map = new HashMap<>();
        map.put("caller", swap.getSender());
        map.put("methodId", "others");
        map.put("to", swap.getTo());
        map.put("amountIn", swap.getAmountIn() == null ? null : swap.getAmountIn().toString());
        map.put("amountOut", swap.getAmountOut() == null ? null : swap.getAmountOut().toString());
        map.put("tokenIn", swap.getTokenIn());
        map.put("tokenOut", swap.getTokenOut());
        map.put("pair", swap.getPair().toString());
        map.put("logIndex", swap.getLogIndex() == null ? null : swap.getLogIndex().toString());
        map.put("fromMergedTransferEvent", swap.getFromMergedTransferEvent() == null ? null : swap.getFromMergedTransferEvent().toString());
        map.put("toMergedTransferEvent", swap.getToMergedTransferEvent() == null ? null : swap.getToMergedTransferEvent().toString());
        map.put("connectedPools", JSONObject.parseObject(JSON.toJSONString(swap.getRawSwapLog().stream().map(UniswapEvent::getContractAddress).collect(Collectors.toList())), List.class).toString());
        map.put("protocol", conf.getProtocol());
        map.put("raw_swap_log", JSONObject.parseObject(JSON.toJSONString(swap.getRawSwapLog()), List.class).toString());
        map.put("version", swap.getVersion());
        map.put("errorMsg", swap.getErrorMsg());
        map.put("gasFee", swap.getGasFee() == null ? null : swap.getGasFee().toString());
        map.put("tips", swap.getTips());
        map.put("tipPlatform", swap.getTipPlatform());
        map.put("chain", conf.getChainId());
        if (conf.getWCoinAddress().equals(swap.getTokenIn())) {
            map.put("eventType", "buy");
        } else {
            if (swap.getAmountOut() != null && swap.getAmountOut().compareTo(BigInteger.ZERO) == 0) {
                map.put("version", "transferOut");
                map.put("eventType", "transferOut");
            } else map.put("eventType", "sell");
        }
        return map;
    }

    public static List<UniswapEvent> getSolanaSwapEvents(ChainConfig conf, String originSender, String hash, List<UniswapEvent> uniswapEvents, List<TransferEvent> transferEvents) {
        // 1、移除 swap 的 transfer event
//        List<TransferEvent> transferEvents = CollectionUtils.isEmpty(uniswapEvents) ? rawTransferEvents : rawTransferEvents.stream().filter(t -> {
//            for (int i = 0; i < uniswapEvents.size(); i++) {
//                if (uniswapEvents.get(i).getLogIndex().compareTo(t.getLogIndex()) == 0) return false;
//            }
//            return true;
//        }).collect(Collectors.toList());

        // 2、将构建好的所有 swapEvent 的首尾串联，串联规则：前一个swap的receiver = 后一个swap的sender
        List<UniswapEvent> rawSwapEvents = new ArrayList<>();
        // 去除 aggregator 的 dex
        List<UniswapEvent> aggList = uniswapEvents.stream().filter(UniswapEvent::isAggregator).collect(Collectors.toList());
        uniswapEvents.forEach(t -> {
            try {
                if (t.isAggregator()) return;
                UniswapEvent temp = UniswapEvent.builder().build();
                BeanUtils.copyProperties(temp, t);
                rawSwapEvents.add(temp);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });


//        uniswapEvents = uniswapEvents.stream().filter(t -> {
//            return t.isAggregator() || !aggOuterIndex.contains(t.getOuterIndex());
//        }).collect(Collectors.toList());

        List<UniswapEvent> fullUniswapEvents = UniswapEvent.merge(uniswapEvents, aggList, false);
        log.debug("******* 所有 Swap 首尾相连后为： {} 条", fullUniswapEvents.size());

        // 2、循环遍历swapEvents， 从transferEvents找到每一个swapEvent的最开始的入地址和最终的转出地址
        fullUniswapEvents.forEach(ut -> {
            TransferEvent _tmpPreTf = null;
            try {
                _tmpPreTf = TransferEvent.findPreTx(originSender, transferEvents, null, ut.getAmountIn(), ut.getSender(), ut.getTo(), ut.getTokenIn(), ut);
                if (_tmpPreTf == null || _tmpPreTf.getSender() == null) {
                    log.debug("******* ❌  not fond any pre transfer to merge \n");
                    ut.setErrorMsg(ut.getErrorMsg() + " | " + "multity from :" + hash);
                    return;
                }
                ut.setSender(_tmpPreTf.getSender());

                TransferEvent _tmpAftTf = TransferEvent.findAfterTx(conf.getChainId(), originSender, transferEvents, null, ut.getAmountOut(), ut.getSender(), ut.getTo(), ut.getTokenOut(), ut);
                if (_tmpAftTf == null || _tmpAftTf.getSender().equalsIgnoreCase("")) {
                    log.debug("******* ❌  not fond any after transfer to merge \n");
                    ut.setErrorMsg(ut.getErrorMsg() + " | " + "multity to :" + hash);
                    return;
                }
                ut.setAmountOut(_tmpAftTf.getAmount());
                ut.setTo(_tmpAftTf.getReceiver());
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        });
        fullUniswapEvents = UniswapEvent.merge(fullUniswapEvents, Lists.newArrayList(), true);

        // 3、将最终的 swap 关联上的 transfer 去掉 | 池子、token的 transfer 去除。保留没有使用的 transfer, 将这些 transfer 封装为 uniswap
//        List<TransferEvent> getFinalTransferOutEvent = TransferEvent.calculateBalances(transferEvents);
        transferToUniswapSell(conf, transferEvents, fullUniswapEvents);
        log.debug("******* 最终有效 Swap： {} 条", fullUniswapEvents.size());

        // 4、保存原始 swap
        fullUniswapEvents.forEach(t -> t.setSwapRawSwapLog(rawSwapEvents));
        return fullUniswapEvents;
    }


    /**
     * 解析所有的swap
     */
    public static List<UniswapEvent> parseAllUniSwapLogs(String originSender, ChainConfig conf, String logs, String hash, List<TransferEvent> validInternalTxs, String price) throws IOException {
        if (logs.isEmpty()) {
            return new ArrayList<>();
        }

        // 1、将log string转为对象
        JsonNode txLog = Log.parseLogsFromString(logs);

        // 2、从log对象中解析 transfer 事件
        List<TransferEvent> transferEvents = Log.findTransfer(txLog);
        log.debug("******* Log 中找到符合条件 TransferEvent： {} 条", transferEvents.size());
        Collections.sort(transferEvents, Comparator.comparing(TransferEvent::getLogIndex));

        // 3、将有效的内部交易添加到transfer事件
        transferEvents.addAll(validInternalTxs);
        List<TransferEvent> rawTransferEvents = new ArrayList<>();
        transferEvents.forEach(t -> {
            try {
                TransferEvent temp = TransferEvent.builder().build();
                BeanUtils.copyProperties(temp, t);
                rawTransferEvents.add(temp);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        log.debug("******* InnerTx 和 Transfer 合并, 当前 总共 transferEvents： {} 条。", transferEvents.size());

        // 4、获取 swap 事件, 且删除构建中使用的transferEvent
        Result resultEvent = getUniswapEvents(conf, transferEvents, txLog, hash, price);
        // 3、将构建好的所有 swapEven t的首尾串联，串联规则：前一个swap的receiver = 后一个swap的sender
        List<UniswapEvent> fullUniswapEvents = UniswapEvent.merge(resultEvent.getUniswapEvents(), Lists.newArrayList(), false);
        log.debug("******* 所有 Swap 首尾相连后为： {} 条", fullUniswapEvents.size());

        // 5、循环遍历swapEvents， 从transferEvents找到每一个swapEvent的最开始的入地址和最终的转出地址
        fullUniswapEvents.forEach(ut -> {
            try {
                TransferEvent _tmpPreTf = TransferEvent.findPreTx(originSender, transferEvents, resultEvent.getPoolAddressLists(), ut.getAmountIn(), ut.getSender(), ut.getTo(), ut.getTokenIn(), ut);
                if (_tmpPreTf == null || _tmpPreTf.getSender() == null) {
                    log.debug("******* ❌  not fond any pre transfer to merge \n");
                    ut.setErrorMsg(ut.getErrorMsg() + " | " + "multity from :" + hash);
                    return;
                }
                ut.setSender(_tmpPreTf.getSender());

                TransferEvent _tmpAftTf = TransferEvent.findAfterTx(conf.getChainId(), originSender, transferEvents, resultEvent.getPoolAddressLists(), ut.getAmountOut(), ut.getSender(), ut.getTo(), ut.getTokenOut(), ut);
                if (_tmpAftTf == null || _tmpAftTf.getSender().equalsIgnoreCase("")) {
                    log.debug("******* ❌  not fond any after transfer to merge \n");
                    ut.setErrorMsg(ut.getErrorMsg() + " | " + "multity to :" + hash);
                    return;
                }
                ut.setAmountOut(_tmpAftTf.getAmount());
                ut.setTo(_tmpAftTf.getReceiver());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        fullUniswapEvents = UniswapEvent.merge(fullUniswapEvents, Lists.newArrayList(), false);

        log.debug("******* 所有 Swap 收尾再各自向前后链接 TransferEvent后，当前还剩余 transferEvents： {} 条", transferEvents.size());
        boolean isVerify = verifyBalanceReconciliation(rawTransferEvents, fullUniswapEvents, hash);

        // 9.5、兜底：已知 DEX 的 Swap 日志签名均未识别到时，尝试从 Transfer 净额直接反推出一笔完整 swap
        // 前提：1) 日志里根本没有匹配到任何已知 DEX 的 Swap topic（rawUniswapEvents 为空则说明连"疑似 swap"都没有，不兜底）
        //       2) 已匹配到的 swap 都跟 originSender 无关（sender/to 均不是 originSender），
        //          避免 originSender 本来就有一笔已识别但链接失败的 swap 时，又被兜底逻辑猜出一笔冲突/重复的记录
        boolean swapsInvolveSender = fullUniswapEvents.stream().anyMatch(u ->
                (u.getSender() != null && u.getSender().equals(originSender)) && (u.getTo() != null && u.getTo().equals(originSender)))
                ;
        if (!resultEvent.getRawUniswapEvents().isEmpty()
                && (!swapsInvolveSender || !isVerify)
        ) {
            List<UniswapEvent> finalSwap = new ArrayList<>();
            UniswapEvent netSwap = buildSwapFromTransferBalance(conf, originSender, rawTransferEvents);
            if (netSwap != null) {
                finalSwap.add(netSwap);
                // 把已被净额兜底消费的资金流转链从 transferEvents 中消费掉，
                // 避免下面 transferToUniswapSell 再把它们重复包装成单腿碎片记录
                consumeTransferChain(rawTransferEvents, originSender, netSwap.getTokenIn(), netSwap.getAmountIn());
                consumeTransferChain(rawTransferEvents, originSender, netSwap.getTokenOut(), netSwap.getAmountOut());
            }
            transferToUniswap(conf, rawTransferEvents, finalSwap);
            finalSwap.forEach(t -> t.setSwapRawSwapLog(resultEvent.getRawUniswapEvents()));
            return finalSwap;
        }

        // 10、将最终的 swap 关联上的 transfer 去掉 | 池子、token的 transfer 去除。保留没有使用的 transfer, 将这些 transfer 封装为 uniswap
//        Map<String, Map<String, BigInteger>> finalTransfer = TransferEvent.calculateBalances(transferEvents);
//        List<TransferEvent> getFinalTransferOutEvent = getTransferOutEvent(finalTransfer);
        transferToUniswap(conf, transferEvents, fullUniswapEvents);
        log.debug("******* 最终有效 Swap： {} 条", fullUniswapEvents.size());
        fullUniswapEvents.forEach(t -> t.setSwapRawSwapLog(resultEvent.getRawUniswapEvents()));
        return fullUniswapEvents;
    }
    /**
     * 校验 fullUniswapEvents 隐含的地址级余额变化是否与原始 transferEvents 平账。
     * 只比较 swap 涉及的边界地址（sender/to），池子内部的中转余额本就被 merge 有意吞掉，不参与比较。
     */
    private static boolean verifyBalanceReconciliation(List<TransferEvent> rawTransferEvents,
                                                       List<UniswapEvent> fullUniswapEvents, String hash) {
        if (CollectionUtils.isEmpty(fullUniswapEvents)) {
            return true;
        }
        Map<String, Map<String, BigInteger>> rawBalances = TransferEvent.calculateBalances(rawTransferEvents);

        Map<String, Map<String, BigInteger>> swapBalances = new HashMap<>();
        for (UniswapEvent u : fullUniswapEvents) {
            if (u.getSender() != null && u.getTokenIn() != null && u.getAmountIn() != null) {
                addDelta(swapBalances, u.getSender(), u.getTokenIn(), u.getAmountIn().negate());
            }
            if (u.getTo() != null && u.getTokenOut() != null && u.getAmountOut() != null) {
                addDelta(swapBalances, u.getTo(), u.getTokenOut(), u.getAmountOut());
            }
        }

        boolean isConsistent = true;
        for (Map.Entry<String, Map<String, BigInteger>> entry : swapBalances.entrySet()) {
            String address = entry.getKey();
            Map<String, BigInteger> tokens = entry.getValue();
            for (Map.Entry<String, BigInteger> tokenEntry : tokens.entrySet()) {
                String token = tokenEntry.getKey();
                BigInteger swapDelta = tokenEntry.getValue();
                BigInteger rawDelta = rawBalances.getOrDefault(address, Collections.emptyMap())
                        .getOrDefault(token, BigInteger.ZERO);
                if (rawDelta.compareTo(swapDelta) != 0) {
                    isConsistent = false;
                    break;
                }
            }
        }
        return isConsistent;
    }

    private static void addDelta(Map<String, Map<String, BigInteger>> balances, String address, String token, BigInteger delta) {
        balances.computeIfAbsent(address, k -> new HashMap<>())
                .merge(token, delta, BigInteger::add);
    }
    /**
     * 兜底方案：当已知 DEX 的 Swap 日志签名均未识别到时（fullUniswapEvents 为空），
     * 直接对 originSender 在这笔交易里涉及的全部 TransferEvent 做逐 token 净额结算，
     * 若呈现"净减少 1 种 token、净增加 1 种 token"的干净形态，则视为一次完整 swap。
     * 若净变化涉及多种 token（夹带 dust/税费），取绝对值最大的一负一正配对，其余较小的
     * 净变化 token 不消费，仍交由 transferToUniswapSell 按现有逻辑生成单腿碎片记录。
     */
    private static UniswapEvent buildSwapFromTransferBalance(ChainConfig conf, String originSender, List<TransferEvent> transferEvents) {
        if (StringUtils.isEmpty(originSender)) return null;
        Map<String, Map<String, BigInteger>> balances = TransferEvent.calculateBalances(transferEvents);
        Map<String, BigInteger> senderBalance = balances.get(originSender);
        if (senderBalance == null || senderBalance.size() != 2) return null;

        String tokenIn = null;
        BigInteger amountIn = null;
        String tokenOut = null;
        BigInteger amountOut = null;
        for (Map.Entry<String, BigInteger> entry : senderBalance.entrySet()) {
            BigInteger delta = entry.getValue();
            if (delta.compareTo(BigInteger.ZERO) < 0) {
                BigInteger abs = delta.negate();
                if (amountIn == null || abs.compareTo(amountIn) > 0) {
                    tokenIn = entry.getKey();
                    amountIn = abs;
                }
            } else if (delta.compareTo(BigInteger.ZERO) > 0) {
                if (amountOut == null || delta.compareTo(amountOut) > 0) {
                    tokenOut = entry.getKey();
                    amountOut = delta;
                }
            }
        }
        if (tokenIn == null || tokenOut == null) return null;

        return UniswapEvent.builder()
                .sender(originSender)
                .to(originSender)
                .tokenIn(tokenIn)
                .amountIn(amountIn)
                .tokenOut(tokenOut)
                .amountOut(amountOut)
                .protocol(conf.getProtocol())
                .version("transferNetSwap")
                .pair(Lists.newArrayList(tokenIn, tokenOut))
                .rawSwapLog(Lists.newArrayList())
                .connectedPools(new ArrayList<>())
                .fromMergedTransferEvent(new ArrayList<>())
                .toMergedTransferEvent(new ArrayList<>())
                .build();
    }

    private static void consumeTransferChain(List<TransferEvent> transferEvents, String startAddr, String token, BigInteger amount, boolean outgoing) {
        if (amount == null || amount.compareTo(BigInteger.ZERO) <= 0) return;
        List<TransferEvent> consumed = new ArrayList<>();
        String currentAddr = startAddr;
        BigInteger threshold = amount.multiply(BigInteger.valueOf(80)).divide(BigInteger.valueOf(100));
        while (true) {
            final String addr = currentAddr;
            final BigInteger need = threshold;
            TransferEvent next = transferEvents.stream()
                    .filter(t -> !consumed.contains(t))
                    .filter(t -> t.getContractAddress().equals(token))
                    .filter(t -> outgoing ? addr.equals(t.getSender()) : addr.equals(t.getReceiver()))
                    .filter(t -> t.getAmount().compareTo(need) >= 0)
                    .findFirst()
                    .orElse(null);
            if (next == null) break;
            consumed.add(next);
            currentAddr = outgoing ? next.getReceiver() : next.getSender();
            threshold = next.getAmount().multiply(BigInteger.valueOf(80)).divide(BigInteger.valueOf(100));
        }
        transferEvents.removeAll(consumed);
    }

    public static Result getUniswapEvents(ChainConfig conf, List<TransferEvent> transferEvents, JsonNode txLog, String hash, String price) {
        List<UniswapEvent> uniswapEvents = new ArrayList<>();

        // 1、解析 swap v2\v3
        List<UniswapEvent> uniswapV2Events = Log.findSwapV2(conf.getProtocol(), txLog);
        List<UniswapEvent> uniswapV3Events = Log.findSwapV3(conf.getProtocol(), txLog);
        List<UniswapEvent> uniswapMMEvents = Log.findSwapMM(conf.getProtocol(), txLog);
        List<UniswapEvent> uniswapStableCoinEvents = Log.findSwapStableCoin(conf.getProtocol(), txLog);
        List<UniswapEvent> flapSwap = Log.findFlapSwap(conf, txLog, transferEvents, price);
        List<UniswapEvent> fourMemeSwapV2 = Log.findFourMemeSwapV2(conf, txLog, transferEvents, price);
        List<UniswapEvent> findFourMemeV1 = Log.findFourMemeSwapV1(conf, txLog, transferEvents);

        log.debug("******* log 中 找到 uniswapV2Events：{} 条，uniswapV3Events： {} 条。uniswapMMEvents: {} 条，uniswapStableCoinEvents： {} 条\n", uniswapV2Events.size(), uniswapV3Events.size(), uniswapMMEvents.size(), uniswapStableCoinEvents.size());

        uniswapEvents.addAll(uniswapV2Events);
        uniswapEvents.addAll(uniswapV3Events);
        uniswapEvents.addAll(uniswapMMEvents);
        uniswapEvents.addAll(uniswapStableCoinEvents);
        uniswapEvents.addAll(flapSwap);
        uniswapEvents.addAll(fourMemeSwapV2);
        uniswapEvents.addAll(findFourMemeV1);
        Collections.sort(uniswapEvents, Comparator.comparing(UniswapEvent::getLogIndex));

        // 2、 结合 TransferEvent 将v2和v3构建为标准的 SwapEvent事件，构建完成以后并且删除构建中使用的 TransferEvent
        Result parseMapResult = Log.fillSwapTokenInAndTokenOutWithTransferEvent(transferEvents, uniswapEvents, hash);
        log.debug("******* 将 {} 条 swapEvent 转化成标准 Swap，共消费 {} 条 transferEvents", parseMapResult.getUniswapEvents().size(), parseMapResult.getTransferEvents().size());
        log.debug("******* 当前 Swap: {}, 条， 还剩余 transferEvents： {} 条", uniswapEvents.size(), transferEvents.size());
        return parseMapResult;
    }

    /**
     * 从 addr 出发，在 transferEvents 中找同一 token、且不在 excluded 里的下一跳 transfer。
     * 同时匹配 sender=addr（资金流出）和 receiver=addr（资金流入）两个方向，命中金额需 >= amount 的 80%
     * （容忍 dust/税费）。是否延续、往哪个方向延续完全由实际连上的边决定，而不是提前固定死一个方向 ——
     * 否则中途方向一旦反转（比如经过一个既转入又转出的中转地址）就会断链，
     * 导致本应属于同一条资金流的边找不到，被误当作独立的碎片记录，最终帐不平。
     */
    private static TransferEvent findNextChainHop(List<TransferEvent> transferEvents, List<TransferEvent> excluded, String addr, String token, BigInteger amount) {
        BigInteger need = amount.multiply(BigInteger.valueOf(80)).divide(BigInteger.valueOf(100));
        return transferEvents.stream()
                .filter(t -> !excluded.contains(t))
                .filter(t -> t.getContractAddress().equals(token))
                .filter(t -> addr.equals(t.getSender()) || addr.equals(t.getReceiver()))
                .filter(t -> t.getAmount().compareTo(need) >= 0)
                .findFirst()
                .orElse(null);
    }

    /**
     * 消费掉一笔 transfer 中被链条实际用掉的 amount 部分：一个 TransferEvent 可能承载了不止一条资金流
     * （比如池子把多个用户的资金合并成一笔转出），不能因为它被某条链用到了就整笔从 transferEvents 中
     * 移除 —— 只应扣减本次实际用掉的金额，只有扣到 <= 0（归 0）才真正移除，剩余部分留给后面别的链继续消费。
     */
    private static void consumeAmount(List<TransferEvent> transferEvents, TransferEvent t, BigInteger amount) {
        BigInteger remaining = t.getAmount().subtract(amount);
        if (remaining.compareTo(BigInteger.ZERO) <= 0) {
            transferEvents.remove(t);
        } else {
            t.setAmount(remaining);
        }
    }

    /**
     * 从 startAddr 持有的一笔 token 资金开始，沿着 transfer 图逐跳串联，找出实际构成这笔资金流转的
     * 完整 transfer 链。分两步：
     * 1) 先只查找、不消费，把链上每一跳都走一遍，同时把 bottleneck（链上出现过的最小金额）
     *    一路取 min 记录下来 —— 因为链条中途只要有一跳金额变小（税费/dust），说明这条资金流
     *    实际能端到端流转的金额就是这个更小值，前面已经"找到"的那几跳里超出 bottleneck 的部分
     *    根本没有真正参与这条链，不能被当成已消费。
     * 2) 再用这个统一的 bottleneck 对链上每一跳做真正的扣减（consumeAmount，归 0 才移除），
     *    金额大于 bottleneck 的那一跳会留下 (原金额 - bottleneck) 的剩余，交给 transferToUniswapSell
     *    按同样的逻辑单独处理，而不是被这条链错误地整笔吃掉。
     */
    private static void consumeTransferChain(List<TransferEvent> transferEvents, String startAddr, String token, BigInteger amount) {
        if (amount == null || amount.compareTo(BigInteger.ZERO) <= 0) return;
        List<TransferEvent> visited = new ArrayList<>();
        List<TransferEvent> chain = new ArrayList<>();
        String currentAddr = startAddr;
        BigInteger bottleneck = amount;
        while (true) {
            TransferEvent next = findNextChainHop(transferEvents, visited, currentAddr, token, bottleneck);
            if (next == null) break;
            visited.add(next);
            chain.add(next);
            currentAddr = currentAddr.equals(next.getSender()) ? next.getReceiver() : next.getSender();
            bottleneck = bottleneck.min(next.getAmount());
        }
        for (TransferEvent hop : chain) {
            consumeAmount(transferEvents, hop, bottleneck);
        }
    }

    private static void transferToUniswapSell(ChainConfig conf, List<TransferEvent> transferLog, List<UniswapEvent> eventLists) {
        AtomicInteger count = new AtomicInteger();
        transferLog.forEach(t -> {
            // transferOut 事件构造成 Uniswap Sell 操作
            if (t.getAmount() == null || t.getAmount().equals(BigInteger.ZERO)) return;
            // from是0地址，表示增加总供应，实际并没有转账，所以过滤
//            if (t.getSender().equals(AMMSwapDataProcess.ZEROADDR)) return;
            if (conf.getWCoinAddress().equals(t.getContractAddress())) return;
            UniswapEvent build = UniswapEvent.builder()
                    .protocol("transferOut")
                    .version("transferOut")
                    .amountIn(t.getAmount())
                    .amountOut(new BigInteger("0"))
                    .tokenIn(t.getContractAddress())
                    .tokenOut(conf.getWCoinAddress())
                    .pair(Lists.newArrayList(t.getContractAddress(), conf.getWCoinAddress()))
                    .connectedPools(new ArrayList<>())
                    .sender(t.getSender())
                    .to(t.getReceiver())
                    .build();
            eventLists.add(build);
            count.getAndIncrement();
        });
        log.debug("******* 将 {} 条 transferOut 中的转化为 Swap 事件", count.get());
    }


    private static void transferToUniswap(ChainConfig conf, List<TransferEvent> transferLog, List<UniswapEvent> eventLists) {
        AtomicInteger count = new AtomicInteger();
        List<TransferEvent> visited = new ArrayList<>();
        // 用快照迭代，避免下面 consumeAmount 从 transferLog 中移除元素时触发 ConcurrentModificationException
        for (TransferEvent t : new ArrayList<>(transferLog)) {
            if (visited.contains(t)) continue;
            // transferOut 事件构造成 Uniswap Sell 操作
            if (t.getAmount() == null || t.getAmount().equals(BigInteger.ZERO)) continue;
            // from是0地址，表示增加总供应，实际并没有转账，所以过滤
//            if (t.getSender().equals(AMMSwapDataProcess.ZEROADDR)) continue;
            if (conf.getWCoinAddress().equals(t.getContractAddress())) continue;

            String token = t.getContractAddress();
            visited.add(t);

            LinkedList<TransferEvent> chain = new LinkedList<>();
            chain.add(t);
            BigInteger bottleneck = t.getAmount();

            // 向后延伸：从 receiver 端继续找资金的下一站，同步收窄 bottleneck
            String tailAddr = t.getReceiver();
            while (true) {
                TransferEvent next = findNextChainHop(transferLog, visited, tailAddr, token, bottleneck);
                if (next == null) break;
                visited.add(next);
                chain.addLast(next);
                tailAddr = tailAddr.equals(next.getSender()) ? next.getReceiver() : next.getSender();
                bottleneck = bottleneck.min(next.getAmount());
            }

            // 向前延伸：从 sender 端回溯资金的上一站，继续沿用同一个 bottleneck 收窄
            String headAddr = t.getSender();
            while (true) {
                TransferEvent prev = findNextChainHop(transferLog, visited, headAddr, token, bottleneck);
                if (prev == null) break;
                visited.add(prev);
                chain.addFirst(prev);
                headAddr = headAddr.equals(prev.getSender()) ? prev.getReceiver() : prev.getSender();
                bottleneck = bottleneck.min(prev.getAmount());
            }

            // 整条链（含 t 自身）统一按最终 bottleneck 扣减，金额更大的那几跳保留剩余，留给后续迭代单独处理
            for (TransferEvent hop : chain) {
                consumeAmount(transferLog, hop, bottleneck);
            }

            UniswapEvent build = UniswapEvent.builder()
                    .protocol("transferOut")
                    .version("transferOut")
                    .amountIn(bottleneck)
                    .amountOut(new BigInteger("0"))
                    .tokenIn(token)
                    .tokenOut(conf.getWCoinAddress())
                    .pair(Lists.newArrayList(token, conf.getWCoinAddress()))
                    .connectedPools(new ArrayList<>())
                    .sender(headAddr)
                    .to(tailAddr)
                    .build();
            eventLists.add(build);
            count.getAndIncrement();
        }
        log.debug("******* 将 {} 条 transferOut 中的转化为 Swap 事件", count.get());
    }

    @Data
    @Builder
    public static class TokenBalance {
        private String account;
        private String mint;
        private String owner;
        private String programId;
        private UiTokenAmount uiTokenAmount;


    }

    @Data
    @Builder
    public static class UiTokenAmount {
        private String amount;
        private int decimals;

    }
}

