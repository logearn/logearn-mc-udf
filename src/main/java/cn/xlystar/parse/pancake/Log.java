package cn.xlystar.parse.pancake;

import cn.xlystar.entity.TransferEvent;
import cn.xlystar.entity.UniswapEvent;
import cn.xlystar.entity.UniswapV2Event;
import cn.xlystar.entity.UniswapV3Event;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class Log {

    /**
     * 从log的String串中解析成对象
     */
    public static JsonNode parseLogsFromString(String logStr) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(logStr);
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
            List<String> topicLists = parseTopics(tmp.get("topics"));

            if (topicLists.size() == 3
                    && (
                    "0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef".equalsIgnoreCase(topicLists.get(0)) ||
                            "0xe59fdd36d0d223c0c7d996db7ad796880f45e1936cb0bb7ac102e7082e031487".equalsIgnoreCase(topicLists.get(0))
            ) && data.length() == 64) {
                // Transfer
                TransferEvent event = TransferEvent.builder()
                        .sender("0x" + topicLists.get(1).substring(26).toLowerCase())
                        .receiver("0x" + topicLists.get(2).substring(26).toLowerCase())
                        .amount(new BigInteger(data.substring(24), 16))
                        .contractAddress(contractAddress)
                        .origin("log")
                        .build();
                transferLog.add(event);
            }
        }
        return transferLog;
    }

    /**
     * 解析Uniswapv3/PancakeV3事件
     */
    public static List<UniswapV3Event> findSwapV3(JsonNode logJson) {
        JsonNode logLists = logJson.get("logs");
        List<UniswapV3Event> uniswapV3Logs = new ArrayList<>();
        for (JsonNode tmp : logLists) {
            String contractAddress = tmp.get("address").asText().toLowerCase();
            if (tmp.get("data").toString().length() <= 2) continue;
            String data = tmp.get("data").asText().substring(2);
            List<String> topicLists = parseTopics(tmp.get("topics"));

            if (topicLists.size() >= 3
                    && "0xc42079f94a6350d7e6235f29174924f928cc2ac818eb64fed8004e115fbcca67".equalsIgnoreCase(topicLists.get(0))
                    && data.length() == 320) {
                // uniswap v3 解析
                BigInteger amount0 = web3HexToBigInteger(data.substring(0, 64));
                BigInteger amount1 = web3HexToBigInteger(data.substring(64, 128));

                UniswapV3Event uniswapV3Event = UniswapV3Event.builder()
                        .sender("0x" + topicLists.get(1).substring(26).toLowerCase())
                        .recipient("0x" + topicLists.get(2).substring(26).toLowerCase())
                        .amount0(amount0)
                        .amount1(amount1)
                        .protocol("pancake")
                        .version("v3")
                        .contractAddress(contractAddress)
                        .build();

                uniswapV3Logs.add(uniswapV3Event);
            } else if (topicLists.size() >= 3
                    && "0x19b47279256b2a23a1665c810c8d55a1758940ee09377d4f8d26497a3577dc83".equalsIgnoreCase(topicLists.get(0))
                    && data.length() == 448) {
                // uniswap v3 解析
                BigInteger amount0 = web3HexToBigInteger(data.substring(0, 64));
                BigInteger amount1 = web3HexToBigInteger(data.substring(64, 128));

                UniswapV3Event uniswapV3Event = UniswapV3Event.builder()
                        .sender("0x" + topicLists.get(1).substring(26).toLowerCase())
                        .recipient("0x" + topicLists.get(2).substring(26).toLowerCase())
                        .amount0(amount0)
                        .amount1(amount1)
                        .protocol("pancake")
                        .version("v3")
                        .contractAddress(contractAddress)
                        .build();

                uniswapV3Logs.add(uniswapV3Event);
            }
        }
        return uniswapV3Logs;
    }

    /**
     * 解析Uniswapv2/PancakeV2事件
     */
    public static List<UniswapV2Event> findSwapV2(JsonNode logJson) {
        JsonNode logLists = logJson.get("logs");
        List<UniswapV2Event> uniswapV2Logs = new ArrayList<>();
        for (JsonNode tmp : logLists) {
            String contractAddress = tmp.get("address").asText().toLowerCase();
            if (tmp.get("data").toString().length() <= 2) continue;
            String data = tmp.get("data").asText().substring(2);
            List<String> topicLists = parseTopics(tmp.get("topics"));

            if (topicLists.size() >= 3
                    && "0xd78ad95fa46c994b6551d0da85fc275fe613ce37657fb8d5e3d130840159d822".equalsIgnoreCase(topicLists.get(0))
                    && data.length() == 256) {
                // uniswap v2 解析
                BigInteger amount0In = web3HexToBigInteger(data.substring(0, 64));
                BigInteger amount1In = web3HexToBigInteger(data.substring(64, 128));
                BigInteger amount0Out = web3HexToBigInteger(data.substring(128, 192));
                BigInteger amount1Out = web3HexToBigInteger(data.substring(192));
                amount0Out = amount0Out.signum() != -1 ? amount0Out : amount0Out.negate();
                amount1Out = amount1Out.signum() != -1 ? amount1Out : amount1Out.negate();

                UniswapV2Event uniswapV2Event = UniswapV2Event.builder()
                        .sender("0x" + topicLists.get(1).substring(26).toLowerCase())
                        .to("0x" + topicLists.get(2).substring(26).toLowerCase())
                        .amount0In(amount0In)
                        .amount1In(amount1In)
                        .protocol("pancake")
                        .version("v2")
                        .amount0Out(amount0Out)
                        .amount1Out(amount1Out)
                        .contractAddress(contractAddress)
                        .build();

                uniswapV2Logs.add(uniswapV2Event);
            }
        }
        return uniswapV2Logs;
    }

    /**
     * 将uniswapV2Event对象转为标准的uniswapEvent对象
     */
    public static Result parseUniswapV2ToUniswapEvent(List<TransferEvent> transferEvents, List<UniswapV2Event> uniswapV2Events, String hash) {
        List<TransferEvent> excludetransferEvents = new ArrayList<>();
        ArrayList<UniswapEvent> uniswapEvents = new ArrayList<>();

        uniswapV2Events.forEach(u -> {
            String poolAddress = u.getContractAddress();
            BigInteger amount1In = u.getAmount1In();
            BigInteger amount0In = u.getAmount0In();
            BigInteger amount0Out = u.getAmount0Out();
            BigInteger amount1Out = u.getAmount1Out();
            String uSender = u.getSender();
            String uTo = u.getTo();

            // 对于v2，大于0的是out，其他是in
            BigInteger amountOut = amount0Out.compareTo(BigInteger.ONE) > 0 ? amount0Out : amount1Out;
            BigInteger amountIn = amount1In.compareTo(BigInteger.ONE) > 0 ? amount1In : amount0In;
            UniswapEvent.UniswapEventBuilder builder = UniswapEvent.builder();
            builder.contractAddress(poolAddress);

            String _tokenOut = "";
            String _tokenIn = "";

            BigInteger k1 = amountOut.multiply(new BigInteger("30")).divide(new BigInteger("10000000"));
            BigInteger k2 = amountIn.multiply(new BigInteger("30")).divide(new BigInteger("10000000"));

            for (TransferEvent transferEvent : transferEvents) {
                String tokenAddress = transferEvent.getContractAddress();
                BigInteger tAmount = transferEvent.getAmount();
                String tSender = transferEvent.getSender();
                String tReceiver = transferEvent.getReceiver();
                if (tSender.equals(tReceiver)) {
                    // builder.errorMsg("error : [v2]Transfer sender equal receiver! : " + hash);
                    excludetransferEvents.add(transferEvent);
                    continue;
                }
                // System.out.println("poolAddress = " + poolAddress + ", tReceiver = " + tReceiver + ", tAmount = " + tAmount + ", amountIn = " + amountIn + ", amount1Out = " + amount1Out + ", uTo = " + uTo + ", tSender = " + tSender + ", poolAddress = " + poolAddress + ", amount0Out = " + amount0Out + ", tokenAddress = " + tokenAddress);

                if (
                        tReceiver.equalsIgnoreCase(uTo)
                                && tSender.equalsIgnoreCase(poolAddress)
//                                && tAmount.compareTo(amountOut.subtract(k1)) > 0
//                                && tAmount.compareTo(amountOut.add(k1)) < 0
                ) {
                    // 从池子中发出了多个transfer，异常情况
                    if (!_tokenOut.equals("") && !_tokenOut.equalsIgnoreCase(tokenAddress)) {
                        builder.errorMsg("error : [v2]Had set token out!  tokenin:[" + _tokenOut + "], tokenAddress[" + tokenAddress + "], hash:[" + hash + "] \n");
                        break;
                    }
                    _tokenOut = tokenAddress;
                    builder.tokenOut(tokenAddress).amountOut(tAmount).to(uTo);
                    excludetransferEvents.add(transferEvent);
                } else if (
                        tReceiver.equalsIgnoreCase(poolAddress)
//                                && tAmount.compareTo(amountIn.subtract(k2)) > 0
//                                && tAmount.compareTo(amountIn.add(k2)) < 0
                ) {
                    // 多个transfer进入了池子，异常情况
                    if (!_tokenIn.equals("") && !_tokenIn.equalsIgnoreCase(tokenAddress)) {
                        builder.errorMsg("error : [v2]Had set token in!  tokenin:[" + _tokenIn + "], tokenAddress[" + tokenAddress + "], hash:[" + hash + "] \n");
                        break;
                    }
                    _tokenIn = tokenAddress;
                    builder.tokenIn(tokenAddress).amountIn(tAmount).sender(tSender);
                    excludetransferEvents.add(transferEvent);
                }
            }
            UniswapEvent build = builder.build();
            if (build.getTokenIn() == null || build.getTokenOut() == null) {
                build.setErrorMsg("error : token in or token out is null! hash : " + hash);
            }
            uniswapEvents.add(build);
        });
        return new Result(uniswapEvents, excludetransferEvents);
    }

    /**
     * 将uniswapV3Event对象转为标准的uniswapEvent对象
     */
    public static Result parseUniswapV3ToUniswapEvent(List<TransferEvent> transferEvents, List<UniswapV3Event> uniswapV3Events, String hash) {
        List<TransferEvent> excludetransferEvents = new ArrayList<>();
        ArrayList<UniswapEvent> uniswapEvents = new ArrayList<>();

        uniswapV3Events.forEach(u -> {
            String poolAddress = u.getContractAddress();
            BigInteger amount0 = u.getAmount0();
            BigInteger amount1 = u.getAmount1();
            boolean token0IsOut = amount0.compareTo(BigInteger.ZERO) < 0;
            // 对于v3，大于0的是in，小于0的是out
            BigInteger amountOut = token0IsOut ? amount0.abs() : amount1.abs();
            BigInteger amountIn = token0IsOut ? amount1.abs() : amount0.abs();
            String uTo = u.getRecipient();
            UniswapEvent.UniswapEventBuilder builder = UniswapEvent.builder();
            builder.contractAddress(poolAddress);

            String _tokenOut = "";
            String _tokenIn = "";
            for (TransferEvent transferEvent : transferEvents) {
                String tokenAddress = transferEvent.getContractAddress();
                BigInteger tAmount = transferEvent.getAmount();
                String tSender = transferEvent.getSender();
                String tReceiver = transferEvent.getReceiver();
                if (tSender.equals(tReceiver)) {
                    System.out.printf("error : [v3]Transfer sender equal receiver! [%s] \n", hash);
                    //builder.errorMsg("error : Transfer sender equal receiver! : " + hash);
                    excludetransferEvents.add(transferEvent);
                    continue;
                }
                // 1、uniswap 的 sender 地址 等于 transfer 的 sender 地址
                // 或者 uniswap 的 sender 地址 等于 transfer 的 receiver 地址
                // 2、同时对应的金额一致
                // 3、不相等的另一个地址等于 uniswap 的池地址
                // System.out.println("poolAddress = " + poolAddress + ", tReceiver = " + tReceiver + ", uTo = " + uTo + ", tSender = " + tSender + ", tAmount = " + tAmount + ", amountIn = " + amountIn + ", amountOut = " + amountOut  + ", poolAddress = " + poolAddress  + ", tokenAddress = " + tokenAddress);
                if (
                        tReceiver.equalsIgnoreCase(uTo)
                                && tSender.equalsIgnoreCase(poolAddress)
                                && amountOut.equals(tAmount)
                ) {
                    // 从池子中发出了多个transfer，异常情况
                    if (!_tokenOut.equals("") && !_tokenOut.equalsIgnoreCase(tokenAddress)) {
                        builder.errorMsg("error : [v3]Had set token out!  tokenin:[" + _tokenOut + "], tokenAddress[" + tokenAddress + "], hash:[" + hash + "] \n");
                        break;
                    }
                    _tokenOut = tokenAddress;
                    builder.tokenOut(tokenAddress).amountOut(tAmount).to(uTo);
                    excludetransferEvents.add(transferEvent);
                } else if (
                        tReceiver.equalsIgnoreCase(poolAddress)
                                && amountIn.equals(tAmount)
                ) {
                    // 多个transfer进入了池子，异常情况
                    if (!_tokenIn.equals("") && !_tokenIn.equalsIgnoreCase(tokenAddress)) {
                        builder.errorMsg("error : [v3]Had set token in!  tokenin:[" + _tokenIn + "], tokenAddress[" + tokenAddress + "], hash:[" + hash + "] \n");
                        break;
                    }
                    _tokenIn = tokenAddress;
                    builder.tokenIn(tokenAddress).amountIn(tAmount).sender(tSender);
                    excludetransferEvents.add(transferEvent);
                }

            }
            uniswapEvents.add(builder.build());
        });
        return new Result(uniswapEvents, excludetransferEvents);
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
