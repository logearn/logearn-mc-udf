package cn.xlystar.parse.ammswap;

import cn.xlystar.entity.TransferEvent;
import cn.xlystar.entity.UniswapEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.math.BigInteger;
import java.util.*;

@Slf4j
public class Log {

    /**
     * 从log的String串中解析成对象
     */
    public static JsonNode parseLogsFromString(String logStr) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(logStr);
    }

    /**
     * 生成合约地址列表
     */
    public static Set<String> getContractAddress(JsonNode logJson) {
        JsonNode logLists = logJson.get("logs");
        Set<String> contractAddressList = new HashSet<>();
        logLists.forEach(log -> contractAddressList.add(log.get("address").asText().toLowerCase()));
        return contractAddressList;
    }

    /**
     * 解析transfer事件
     */
    public static List<TransferEvent> findTransfer(JsonNode logJson) {
        JsonNode logLists = logJson.get("logs");
        List<TransferEvent> transferLog = new ArrayList<>();
        for (JsonNode tmp : logLists) {
            String contractAddress = tmp.get("address").asText().toLowerCase();
            if (tmp.get("data").toString().length() <= 2) continue;
            String data = tmp.get("data").asText().substring(2);
            JsonNode logIndexNode = tmp.get("logIndex") != null ? tmp.get("logIndex") : tmp.get("logindex");
            BigInteger logIndex = new BigInteger(logIndexNode.asText().substring(2), 16);
            List<String> topicLists = parseTopics(tmp.get("topics"));
            if (topicLists.size() != 3) continue;
            boolean isErc404 = "0xe59fdd36d0d223c0c7d996db7ad796880f45e1936cb0bb7ac102e7082e031487".equalsIgnoreCase(topicLists.get(0));
            boolean isErc20 = "0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef".equalsIgnoreCase(topicLists.get(0));
            // Transfer (index_topic_1 address src, index_topic_2 address dst, uint256 wad)
            if ((isErc20 ||
                    isErc404 // ERC20Transfer (index_topic_1 address from, index_topic_2 address to, uint256 amount)
            )
                    && data.length() == 64) {
                // Transfer
                String sender = "0x" + topicLists.get(1).substring(26).toLowerCase();
                String receiver = "0x" + topicLists.get(2).substring(26).toLowerCase();

                // 自己给自己转账的日志，过滤掉，因为会影响 swap 去找 transfer 的准确性
                if (!sender.equals(receiver)) {
                    TransferEvent event = TransferEvent.builder()
                            .sender(sender)
                            .receiver(receiver)
                            .amount(new BigInteger(data.substring(24), 16))
                            .logIndex(logIndex)
                            .assetType(isErc404 ? "erc404" : "erc20")
                            .contractAddress(contractAddress)
                            .origin("log")
                            .build();
                    transferLog.add(event);
                }
            }
        }
        return transferLog;
    }

    /**
     * 解析Uniswapv3/PancakeV3事件
     */
    public static List<UniswapEvent> findSwapV3(String protocol, JsonNode logJson) {
        JsonNode logLists = logJson.get("logs");
        List<UniswapEvent> uniswapEvents = new ArrayList<>();
        for (JsonNode tmp : logLists) {
            String contractAddress = tmp.get("address").asText().toLowerCase();
            if (tmp.get("data").toString().length() <= 2) continue;
            String data = tmp.get("data").asText().substring(2);
            JsonNode logIndexNode = tmp.get("logIndex") != null ? tmp.get("logIndex") : tmp.get("logindex");
            BigInteger logIndex = new BigInteger(logIndexNode.asText().substring(2), 16);
            List<String> topicLists = parseTopics(tmp.get("topics"));

            boolean is_v3_1 = topicLists.size() >= 3
                    && "0xc42079f94a6350d7e6235f29174924f928cc2ac818eb64fed8004e115fbcca67".equalsIgnoreCase(topicLists.get(0))
                    && data.length() == 320;

            boolean is_v3_2 = topicLists.size() >= 3
                    && "0x19b47279256b2a23a1665c810c8d55a1758940ee09377d4f8d26497a3577dc83".equalsIgnoreCase(topicLists.get(0))
                    && data.length() == 448;

            if (is_v3_1 || is_v3_2) {
                // uniswap v3 解析
                BigInteger amount0 = web3HexToBigInteger(data.substring(0, 64));
                BigInteger amount1 = web3HexToBigInteger(data.substring(64, 128));

                String sender = "0x" + topicLists.get(1).substring(26).toLowerCase();
                String to = "0x" + topicLists.get(2).substring(26).toLowerCase();

                // 对于v3，大于0的是in，小于0的是out
                BigInteger amountIn = amount0.compareTo(amount1) > 0 ? amount0.abs() : amount1.abs();
                BigInteger amountOut = amount0.compareTo(amount1) > 0 ? amount1.abs() : amount0.abs();

                UniswapEvent uniswapEvent = UniswapEvent.builder()
                        .sender(sender)
                        .to(to)

                        .amountIn(amountIn)
                        .amountOut(amountOut)

                        .logIndex(logIndex)
                        .contractAddress(contractAddress)
                        .protocol(protocol)
                        .version("v3")
                        .build();

                uniswapEvents.add(uniswapEvent);
            }
        }
        return uniswapEvents;
    }

    /**
     * 解析Uniswapv2/PancakeV2事件
     */
    public static List<UniswapEvent> findSwapV2(String protocol, JsonNode logJson) {
        JsonNode logLists = logJson.get("logs");
        List<UniswapEvent> uniswapEvents = new ArrayList<>();
        for (JsonNode tmp : logLists) {
            String contractAddress = tmp.get("address").asText().toLowerCase();
            if (tmp.get("data").toString().length() <= 2) continue;
            String data = tmp.get("data").asText().substring(2);
            List<String> topicLists = parseTopics(tmp.get("topics"));
            JsonNode logIndexNode = tmp.get("logIndex") != null ? tmp.get("logIndex") : tmp.get("logindex");
            BigInteger logIndex = new BigInteger(logIndexNode.asText().substring(2), 16);

            if (topicLists.size() >= 3
                    && "0xd78ad95fa46c994b6551d0da85fc275fe613ce37657fb8d5e3d130840159d822".equalsIgnoreCase(topicLists.get(0))
                    && data.length() == 256) {
                String sender = "0x" + topicLists.get(1).substring(26).toLowerCase();
                String to = "0x" + topicLists.get(2).substring(26).toLowerCase();

                // uniswap v2 解析
                BigInteger amount0In = web3HexToBigInteger(data.substring(0, 64));
                BigInteger amount1In = web3HexToBigInteger(data.substring(64, 128));

                BigInteger amount0Out = web3HexToBigInteger(data.substring(128, 192));
                BigInteger amount1Out = web3HexToBigInteger(data.substring(192));

                amount0Out = amount0Out.signum() != -1 ? amount0Out : amount0Out.negate();
                amount1Out = amount1Out.signum() != -1 ? amount1Out : amount1Out.negate();

                // 对于v2，大于0的是out，其他是in
                BigInteger amountOut = amount0Out.compareTo(BigInteger.ONE) > 0 ? amount0Out : amount1Out;
                BigInteger amountIn = amount1In.compareTo(BigInteger.ONE) > 0 ? amount1In : amount0In;

                UniswapEvent uniswapEvent = UniswapEvent.builder()
                        .sender(sender)
                        .to(to)

                        .amountIn(amountIn)
                        .amountOut(amountOut)

                        .logIndex(logIndex)
                        .contractAddress(contractAddress)
                        .protocol(protocol)
                        .version("v2")
                        .build();

                uniswapEvents.add(uniswapEvent);
            }
        }
        return uniswapEvents;
    }

    /**
     * 将uniswapV2Event对象转为标准的uniswapEvent对象
     */
    public static Result fillSwapTokenInAndTokenOutWithTransferEvent(List<TransferEvent> transferEvents, List<UniswapEvent> uniswapEvents, String hash) {
        List<TransferEvent> excludetransferEvents = new ArrayList<>();

        uniswapEvents.forEach(uniswapEvent -> {

            _fillSwapTokenInAndTokenOutWithTransferEvent(transferEvents, excludetransferEvents, uniswapEvent, hash, false, false, false);

            // 2个 Swap 共用一个边的时候，第二个 swap 从 transferEvents，里面找会 会找不到，需要从 excludetransferEvents 再找一遍
            if (uniswapEvent.getTokenIn() == null || uniswapEvent.getTokenOut() == null) {
                log.debug("******* ***** 有 2个 Swap 共用一个 Transfer, 第二个 Swap 为： {}", uniswapEvent);
                _fillSwapTokenInAndTokenOutWithTransferEvent(excludetransferEvents, null, uniswapEvent, hash, false, false, false);
            }


            if (uniswapEvent.getTokenIn() == null || uniswapEvent.getTokenOut() == null) {
                log.debug("******* 共享边后，还是找不到对应的 Transfer， 然后放开 log index 限制再找一遍 ");
                // 由于 uniswap or sushi 等 swap 都是现有 2 个 transferlog, 然后再有 swap log, 所以，默认 swap 匹配 transfer 的时候，log 的顺序会参与 transfer 筛选
                // 因为这样的 log 顺序有利于，当 一个 swap pool 有多个转入或者转出操作的时候，大概率选择到那个正确的 transfer 进行匹配
                // 但是 由于 solidiy 这样的 dex 是先有 swap log， 这有 transfer, 所以这个时候，放开 log index 的限制再去 匹配一遍
                // 例如: https://etherscan.io/tx/0xff2b09ff2facfa2578c46b212af4cabffd01ee04966f32563d762728ecbccb0b#eventlog

                // suswap V2, 中，有时候，就是 transfer in 的金额 比，swap log 的 amount in 要大，所以，要允许 忽略 金额的大小比较
                // suswap v2 中，直接 swap 成 eth 或者 eth 直接 swap 成其他 token 的池子，都不是 swap 对应 2个 Transfer log, ETH 的变动不会发出log, 所以需要允许 intex 的 log 参与匹配。
                _fillSwapTokenInAndTokenOutWithTransferEvent(transferEvents, excludetransferEvents, uniswapEvent, hash, true, true, true);

                if (uniswapEvent.getTokenIn() == null || uniswapEvent.getTokenOut() == null) {
                    _fillSwapTokenInAndTokenOutWithTransferEvent(excludetransferEvents, null, uniswapEvent, hash, true, true, true);
                }
            }

            if (uniswapEvent.getTokenIn() == null || uniswapEvent.getTokenOut() == null) {
                log.debug("******* ❌ 但是Swap 还是找不到对应的 Transfer , swap: {} \n", uniswapEvent);
                uniswapEvent.setErrorMsg("error : token in or token out is null! hash : " + hash);
            }

            // 删除上次消费的 transfer, 因为如果是 2个swap 都有同一个输入，就需要删除，这样第二个才能找的准确
            transferEvents.removeAll(excludetransferEvents);
        });
        return new Result(uniswapEvents, excludetransferEvents);
    }

    private static void _fillSwapTokenInAndTokenOutWithTransferEvent(List<TransferEvent> allTransferEvents, List<TransferEvent> excludetransferEvents,
                                                                     UniswapEvent uniswapEvent, String hash,
                                                                     Boolean ignoreLogIndex,
                                                                     Boolean ignoreCompareAmount,
                                                                     Boolean ignoreTransferOrigin
    ) {
        String _tokenOut = "";
        String _tokenIn = "";
        boolean tokenOutIsNull = uniswapEvent.getTokenOut() == null;
        boolean tokenInIsNull = uniswapEvent.getTokenIn() == null;

        for (int i = 0; i < allTransferEvents.size(); i++) {

            TransferEvent transferEvent = allTransferEvents.get(i);
            String tokenAddress = transferEvent.getContractAddress();
            BigInteger tAmount = transferEvent.getAmount();
            String tSender = transferEvent.getSender();
            String tReceiver = transferEvent.getReceiver();

            if (
                    tReceiver.equalsIgnoreCase(uniswapEvent.getTo())
                            && tSender.equalsIgnoreCase(uniswapEvent.getContractAddress())
                            && (uniswapEvent.getAmountOut().compareTo(tAmount) >= 0 || ignoreCompareAmount)
                            && (uniswapEvent.getLogIndex().compareTo(transferEvent.getLogIndex()) > 0 || ignoreLogIndex)
                            && (transferEvent.getOrigin().equals("log") || ignoreTransferOrigin)
                            && tokenOutIsNull
            ) {
                // 从池子中发出了多个transfer，异常情况, 如果有2笔交易，都走一个池子，以金额一样的为准
                // https://phalcon.blocksec.com/explorer/tx/eth/0x77b10b570bccb107ab4e311bccd3034bd716ff0269bad8aa8ded3f3c499f2987： solidly-wbtc 池子
                if (StringUtils.isEmpty(_tokenOut) || uniswapEvent.getAmountOut().compareTo(tAmount) == 0) {
                    _tokenOut = tokenAddress;
                    uniswapEvent.setTokenOut(tokenAddress);
                    uniswapEvent.setAmountOut(tAmount);
                    if (excludetransferEvents != null) excludetransferEvents.add(transferEvent);
                }
            } else if (
                    tReceiver.equalsIgnoreCase(uniswapEvent.getContractAddress())
                            && (tAmount.compareTo(uniswapEvent.getAmountIn()) >= 0 || ignoreCompareAmount)
                            && (uniswapEvent.getLogIndex().compareTo(transferEvent.getLogIndex()) > 0 || ignoreLogIndex)
                            && (transferEvent.getOrigin().equals("log") || ignoreTransferOrigin)
                            && tokenInIsNull
            ) {
                // 多个transfer进入了池子，异常情况, 如果有2笔交易，都走一个池子，以金额一样的为准
                // https://phalcon.blocksec.com/explorer/tx/eth/0x77b10b570bccb107ab4e311bccd3034bd716ff0269bad8aa8ded3f3c499f2987： solidly-wbtc 池子
                if (StringUtils.isEmpty(_tokenIn) || tAmount.compareTo(uniswapEvent.getAmountIn()) == 0) {
                    _tokenIn = tokenAddress;
                    uniswapEvent.setTokenIn(tokenAddress);
                    uniswapEvent.setAmountIn(tAmount);
                    uniswapEvent.setSender(tSender);
                    if (excludetransferEvents != null) excludetransferEvents.add(transferEvent);
                }
            }
        }
    }


    /**
     * 16进制转成10进制
     */
    private static BigInteger web3HexToBigInteger(String web3Hex) {
        if (web3Hex.startsWith("0x")) {
            web3Hex = web3Hex.substring(2);
        }

        BigInteger bigInteger = new BigInteger(web3Hex, 16);

        if (web3Hex.length() > 0 && web3Hex.charAt(0) >= '8') {
            bigInteger = bigInteger.subtract(BigInteger.ONE.shiftLeft(web3Hex.length() * 4));
        }

        return bigInteger;
    }

    /**
     * 解析topics
     */
    private static List<String> parseTopics(JsonNode topics) {
        List<String> topicLists = new ArrayList<>();
        topics.forEach(t -> topicLists.add(t.asText()));
        return topicLists;
    }

}
