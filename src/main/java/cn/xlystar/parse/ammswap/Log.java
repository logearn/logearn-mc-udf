package cn.xlystar.parse.ammswap;

import cn.xlystar.entity.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
            if (topicLists.size() != 3 && topicLists.size() != 4) continue;
            boolean isErc404 = "0xe59fdd36d0d223c0c7d996db7ad796880f45e1936cb0bb7ac102e7082e031487".equalsIgnoreCase(topicLists.get(0));
            boolean isErc20 = "0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef".equalsIgnoreCase(topicLists.get(0));
            boolean isErc1155 = "0xc3d58168c5ae7397731d063d5bbf3d657854427343f4c083240f7aacaa2d0f62".equalsIgnoreCase(topicLists.get(0));
            // Transfer (index_topic_1 address src, index_topic_2 address dst, uint256 wad)
            if ((isErc20 ||
                    isErc404 // ERC20Transfer (index_topic_1 address from, index_topic_2 address to, uint256 amount)
            ) && topicLists.size() == 3
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
            } else if (isErc1155 && topicLists.size() == 4 && data.length() == 128) {
//                TransferSingle (index_topic_1 address operator, index_topic_2 address from, index_topic_3 address to, uint256 id, uint256 value)
                String sender = "0x" + topicLists.get(2).substring(26).toLowerCase();
                String receiver = "0x" + topicLists.get(3).substring(26).toLowerCase();

                // 自己给自己转账的日志，过滤掉，因为会影响 swap 去找 transfer 的准确性
                if (!sender.equals(receiver)) {
                    TransferEvent event = TransferEvent.builder()
                            .sender(sender)
                            .receiver(receiver)
                            .amount(new BigInteger(data.substring(88), 16))
                            .logIndex(logIndex)
                            .assetType("erc20")
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
     * 解析 Uniswap v3/v2 添加/删除 流动性事件
     */
    public static List<LiquidityEvent> findLiquidityEvents(String originSender, String protocol, JsonNode logJson) {
        JsonNode logLists = logJson.get("logs");
        List<LiquidityEvent> liquidityEvents = new ArrayList<>();

        for (JsonNode tmp : logLists) {
            String contractAddress = tmp.get("address").asText().toLowerCase();
            if (tmp.get("data").toString().length() <= 2) continue;

            String data = tmp.get("data").asText().substring(2);
            JsonNode logIndexNode = tmp.get("logIndex") != null ? tmp.get("logIndex") : tmp.get("logindex");
            BigInteger logIndex = new BigInteger(logIndexNode.asText().substring(2), 16);
            List<String> topicLists = parseTopics(tmp.get("topics"));

            boolean is_v2_add_liquidity = topicLists.size() == 2
                    && "0x4c209b5fc8ad50758f13e2e1088ba56a560dff690a1c6fef26394f4c03821c4f".equalsIgnoreCase(topicLists.get(0))
                    && data.length() == 128;

//            boolean is_v2_remove_liquidity = topicLists.size() == 3
//                    && "0xdccd412f0b1252819cb1fd330b93224ca42612892bb3f4f789976e6d81936496".equalsIgnoreCase(topicLists.get(0))
//                    && data.length() == 128;

            boolean is_v3_add_liquidity = topicLists.size() == 4
                    && "0x7a53080ba414158be7ec69b987b5fb7d07dee101fe85488f0853ae16239d0bde".equalsIgnoreCase(topicLists.get(0))
                    && data.length() == 256;

//            boolean is_v3_remove_liquidity = topicLists.size() == 4
//                    && "0x0c396cd989a39f4459b5fa1aed6a9a8dcdbc45908acfd67e028cd568da98982c".equalsIgnoreCase(topicLists.get(0))
//                    && data.length() == 192;

            if (is_v3_add_liquidity || is_v2_add_liquidity) {
                BigInteger amount0 = null;
                BigInteger amount1 = null;
                String version = null;
                String eventType = null;

                if (is_v2_add_liquidity) {
                    amount0 = web3HexToBigInteger(data.substring(0, 64));
                    amount1 = web3HexToBigInteger(data.substring(64, 128));
                    version = "v2";
                    eventType = is_v2_add_liquidity ? "add" : "remove";
                }

                if (is_v3_add_liquidity) {
                    amount0 = web3HexToBigInteger(data.substring(128, 192));
                    amount1 = web3HexToBigInteger(data.substring(192, 256));
                    version = "v3";
                    eventType = "add";
                }

//                if (is_v3_remove_liquidity) {
//                    amount0 = web3HexToBigInteger(data.substring(64, 128));
//                    amount1 = web3HexToBigInteger(data.substring(128, 192));
//                    version = "v3";
//                    eventType = "remove";
//                }

                LiquidityEvent liquidityEvent = LiquidityEvent.builder()

                        .amount0(amount0)
                        .amount1(amount1)

                        .caller(originSender)
                        .logIndex(logIndex)
                        .poolAddress(contractAddress)
                        .mergedTransferEvent(new ArrayList<>())
                        .protocol(protocol)
                        .eventType(eventType)
                        .version(version)
                        .build();
                liquidityEvents.add(liquidityEvent);
            }
        }
        return liquidityEvents;
    }

    /**
     * 解析 Renounce events
     */
    public static List<RenounceEvent> findRenounceEvents(JsonNode logJson, String hash, Integer blockTimestamp) {
        JsonNode logLists = logJson.get("logs");
        List<RenounceEvent> renounceEvents = new ArrayList<>();

        for (JsonNode tmp : logLists) {
            String contractAddress = tmp.get("address").asText().toLowerCase();
            if (tmp.get("data").toString().length() <= 2) continue;

            JsonNode logIndexNode = tmp.get("logIndex") != null ? tmp.get("logIndex") : tmp.get("logindex");
            BigInteger logIndex = new BigInteger(logIndexNode.asText().substring(2), 16);
            List<String> topicLists = parseTopics(tmp.get("topics"));

            boolean isRenounce = topicLists.size() == 3
                    && "0x8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e0".equalsIgnoreCase(topicLists.get(0));

            if (isRenounce) {
                RenounceEvent renounceEvent = RenounceEvent.builder()
                        .previousOwner("0x" + topicLists.get(1).substring(26).toLowerCase())
                        .newOwner("0x" + topicLists.get(2).substring(26).toLowerCase())
                        .tokenAddress(contractAddress)
                        .transactionHash(hash)
                        .logIndex(logIndex.toString())
                        .renounceTime(Long.valueOf(blockTimestamp))
                        .build();
                renounceEvents.add(renounceEvent);
            }
        }
        return renounceEvents;
    }

    /**
     * 解析 Uniswap v3/v2 添加/删除 流动性事件
     */
    public static List<LockEvent> findLockEvents(JsonNode logJson, String hash, Integer blockTimestamp) {
        JsonNode logLists = logJson.get("logs");
        List<LockEvent> lockEvents = new ArrayList<>();

        for (JsonNode tmp : logLists) {
            String contractAddress = tmp.get("address").asText().toLowerCase();
            if (tmp.get("data").toString().length() <= 2) continue;

            JsonNode logIndexNode = tmp.get("logIndex") != null ? tmp.get("logIndex") : tmp.get("logindex");
            BigInteger logIndex = new BigInteger(logIndexNode.asText().substring(2), 16);
            List<String> topicLists = parseTopics(tmp.get("topics"));

            boolean isLock = topicLists.size() == 3
                    && "0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef".equalsIgnoreCase(topicLists.get(0))
                    && contractAddress.equals("pool");
            if (isLock) {
                LockEvent lockEvent = LockEvent.builder()
                        .lockPlatform(contractAddress)
                        .poolAddress(contractAddress)
                        .lockCaller("0x" + topicLists.get(1).substring(26).toLowerCase())
                        .lockAmount(web3HexToBigInteger(tmp.get("data").toString()).toString())
                        .transactionHash(hash)
                        .logIndex(logIndex.toString())
                        .lockDate(blockTimestamp + "")
                        .unlockDate(blockTimestamp * 10 + "")
                        .lockTime(Long.valueOf(blockTimestamp))
                        .build();
                lockEvents.add(lockEvent);
            }
        }
        return lockEvents;
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
                        .fromMergedTransferEvent(new ArrayList<>())
                        .toMergedTransferEvent(new ArrayList<>())
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
                        .fromMergedTransferEvent(new ArrayList<>())
                        .toMergedTransferEvent(new ArrayList<>())
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
        List<TransferEvent> excludeRawTransferEvents = new ArrayList<>();

        ArrayList<TransferEvent> rawTransferEvent = new ArrayList<>();
        ArrayList<UniswapEvent> rawUniswapEvents = new ArrayList<>();
        List<String> poolAddressLists = new ArrayList<>();
        transferEvents.forEach(t -> {
            TransferEvent build = TransferEvent.builder().build();
            try {
                BeanUtils.copyProperties(build, t);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
            rawTransferEvent.add(build);
        });
        uniswapEvents.forEach(u -> {
            UniswapEvent build = UniswapEvent.builder().build();
            try {
                BeanUtils.copyProperties(build, u);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
            rawUniswapEvents.add(build);
            if (!StringUtils.isEmpty(u.getContractAddress())) poolAddressLists.add(u.getContractAddress());
        });

        // 处理帐不平问题时，发现这段代码多余或者想不起来作用，后面没有对 rawUniswapEvents 做任何处理，所以这段代码注视了
//        rawUniswapEvents.forEach(uniswapEvent -> {
//
//            _fillSwapTokenInAndTokenOutWithTransferEvent(rawTransferEvent, excludeRawTransferEvents, uniswapEvent, true, hash, false, false, false);
//
//            // 2个 Swap 共用一个边的时候，第二个 swap 从 transferEvents，里面找会 会找不到，需要从 excludetransferEvents 再找一遍
//            if (uniswapEvent.getTokenIn() == null || uniswapEvent.getTokenOut() == null) {
//                log.debug("******* ***** 有 2个 Swap 共用一个 Transfer, 第二个 Swap 为： {}", uniswapEvent);
//                _fillSwapTokenInAndTokenOutWithTransferEvent(excludeRawTransferEvents, null, uniswapEvent, true, hash, false, false, false);
//            }
//
//
//            if (uniswapEvent.getTokenIn() == null || uniswapEvent.getTokenOut() == null) {
//                log.debug("******* 共享边后，还是找不到对应的 Transfer， 然后放开 log index 限制再找一遍 ");
//                // 由于 uniswap or sushi 等 swap 都是现有 2 个 transferlog, 然后再有 swap log, 所以，默认 swap 匹配 transfer 的时候，log 的顺序会参与 transfer 筛选
//                // 因为这样的 log 顺序有利于，当 一个 swap pool 有多个转入或者转出操作的时候，大概率选择到那个正确的 transfer 进行匹配
//                // 但是 由于 solidiy 这样的 dex 是先有 swap log， 这有 transfer, 所以这个时候，放开 log index 的限制再去 匹配一遍
//                // 例如: https://etherscan.io/tx/0xff2b09ff2facfa2578c46b212af4cabffd01ee04966f32563d762728ecbccb0b#eventlog
//
//                // suswap V2, 中，有时候，就是 transfer in 的金额 比，swap log 的 amount in 要大，所以，要允许 忽略 金额的大小比较
//                // suswap v2 中，直接 swap 成 eth 或者 eth 直接 swap 成其他 token 的池子，都不是 swap 对应 2个 Transfer log, ETH 的变动不会发出log, 所以需要允许 intex 的 log 参与匹配。
//                _fillSwapTokenInAndTokenOutWithTransferEvent(rawTransferEvent, excludeRawTransferEvents, uniswapEvent, true, hash, true, true, true);
//
//                if (uniswapEvent.getTokenIn() == null || uniswapEvent.getTokenOut() == null) {
//                    _fillSwapTokenInAndTokenOutWithTransferEvent(excludeRawTransferEvents, null, uniswapEvent, true, hash, true, true, true);
//                }
//            }
//
//            if (uniswapEvent.getTokenIn() == null || uniswapEvent.getTokenOut() == null) {
//                log.debug("******* ❌ 但是Swap 还是找不到对应的 Transfer , swap: {} \n", uniswapEvent);
//                uniswapEvent.setErrorMsg("error : token in or token out is null! hash : " + hash);
//            }
//
//            // 删除上次消费的 transfer, 因为如果是 2个swap 都有同一个输入，就需要删除，这样第二个才能找的准确
//            rawTransferEvent.removeAll(excludeRawTransferEvents);
//        });

        _fillSwapTokenInAndTokenOut(uniswapEvents, transferEvents, excludetransferEvents, hash);
        _fillSwapTokenInAndTokenOut(rawUniswapEvents, rawTransferEvent, excludeRawTransferEvents, hash);

        return new Result(uniswapEvents, rawUniswapEvents, excludetransferEvents, poolAddressLists);
    }

    public static void _fillSwapTokenInAndTokenOut(List<UniswapEvent> uniswapEvents, List<TransferEvent> transferEvents,
                                                   List<TransferEvent> excludetransferEvents, String hash) {
        uniswapEvents.forEach(uniswapEvent -> {

            _fillSwapTokenInAndTokenOutWithTransferEvent(transferEvents, excludetransferEvents, uniswapEvent, false, hash, false, false, false);

            // 2个 Swap 共用一个边的时候，第二个 swap 从 transferEvents，里面找会 会找不到，需要从 excludetransferEvents 再找一遍
            if (uniswapEvent.getTokenIn() == null || uniswapEvent.getTokenOut() == null) {
                log.debug("******* ***** 有 2个 Swap 共用一个 Transfer, 第二个 Swap 为： {}", uniswapEvent);
                _fillSwapTokenInAndTokenOutWithTransferEvent(excludetransferEvents, null, uniswapEvent, false, hash, false, false, false);
            }


            if (uniswapEvent.getTokenIn() == null || uniswapEvent.getTokenOut() == null) {
                log.debug("******* 共享边后，还是找不到对应的 Transfer， 然后放开 log index 限制再找一遍 ");
                // 由于 uniswap or sushi 等 swap 都是现有 2 个 transferlog, 然后再有 swap log, 所以，默认 swap 匹配 transfer 的时候，log 的顺序会参与 transfer 筛选
                // 因为这样的 log 顺序有利于，当 一个 swap pool 有多个转入或者转出操作的时候，大概率选择到那个正确的 transfer 进行匹配
                // 但是 由于 solidiy 这样的 dex 是先有 swap log， 这有 transfer, 所以这个时候，放开 log index 的限制再去 匹配一遍
                // 例如: https://etherscan.io/tx/0xff2b09ff2facfa2578c46b212af4cabffd01ee04966f32563d762728ecbccb0b#eventlog

                // suswap V2, 中，有时候，就是 transfer in 的金额 比，swap log 的 amount in 要大，所以，要允许 忽略 金额的大小比较
                // suswap v2 中，直接 swap 成 eth 或者 eth 直接 swap 成其他 token 的池子，都不是 swap 对应 2个 Transfer log, ETH 的变动不会发出log, 所以需要允许 intex 的 log 参与匹配。
                _fillSwapTokenInAndTokenOutWithTransferEvent(transferEvents, excludetransferEvents, uniswapEvent, false, hash, true, true, true);

                if (uniswapEvent.getTokenIn() == null || uniswapEvent.getTokenOut() == null) {
                    _fillSwapTokenInAndTokenOutWithTransferEvent(excludetransferEvents, null, uniswapEvent, false, hash, true, true, true);
                }
            }

            if (uniswapEvent.getTokenIn() == null || uniswapEvent.getTokenOut() == null) {
                log.debug("******* ❌ 但是Swap 还是找不到对应的 Transfer , swap: {} \n", uniswapEvent);
                uniswapEvent.setErrorMsg("error : token in or token out is null! hash : " + hash);
            }

            // 删除上次消费的 transfer, 因为如果是 2个swap 都有同一个输入，就需要删除，这样第二个才能找的准确
            transferEvents.removeAll(excludetransferEvents);
        });
    }

    private static void _fillSwapTokenInAndTokenOutWithTransferEvent(List<TransferEvent> allTransferEvents, List<TransferEvent> excludetransferEvents,
                                                                     UniswapEvent uniswapEvent,
                                                                     Boolean rawAmount,
                                                                     String hash,
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
                            && transferEvent.getLogIndex() != null
                            && uniswapEvent.getLogIndex() != null
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
                    uniswapEvent.getToMergedTransferEvent().add(transferEvent);
                    if (excludetransferEvents != null) {
                        excludetransferEvents.add(transferEvent);
                    }
                    // 共享边问题
//                    if (!rawAmount) uniswapEvent.setAmountOut(tAmount);
                    uniswapEvent.setAmountOut(tAmount);
                }
            } else if (
                    tReceiver.equalsIgnoreCase(uniswapEvent.getContractAddress())
                            && transferEvent.getLogIndex() != null
                            && uniswapEvent.getLogIndex() != null
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
                    uniswapEvent.setSender(tSender);
                    uniswapEvent.getFromMergedTransferEvent().add(transferEvent);
                    if (excludetransferEvents != null) {
                        excludetransferEvents.add(transferEvent);
                    }
                    // 共享边问题
                    uniswapEvent.setAmountIn(tAmount);
                    //                    if (!rawAmount) uniswapEvent.setAmountIn(tAmount);
                }
            }
        }
    }

    /**
     * 补充流动性事件的2个边，使用 TransferEvent
     */
    public static List<LiquidityEvent> fillLiquidityEventWithTransferEvent(List<TransferEvent> transferEvents, List<LiquidityEvent> liquidityEvents, String hash) {

        liquidityEvents.forEach(liquidityEvent -> {
            List<TransferEvent> excludetransferEvents = new ArrayList<>();
            for (int i = 0; i < transferEvents.size(); i++) {

                TransferEvent transferEvent = transferEvents.get(i);
                BigInteger tAmount = transferEvent.getAmount();
                String tReceiver = transferEvent.getReceiver();
                String token_address = transferEvent.getContractAddress();
                if (
                        tReceiver.equalsIgnoreCase(liquidityEvent.getPoolAddress())
                                && (liquidityEvent.getAmount0().compareTo(tAmount) == 0 || liquidityEvent.getAmount1().compareTo(tAmount) == 0)
                ) {
                    liquidityEvent.getMergedTransferEvent().add(transferEvent);
                    if (liquidityEvent.getAmount0().compareTo(tAmount) == 0) {
                        liquidityEvent.setToken0(token_address);
                    } else {
                        liquidityEvent.setToken1(token_address);
                    }

                    excludetransferEvents.add(transferEvent);
                }
            }


            // 删除上次消费的 transfer
            transferEvents.removeAll(excludetransferEvents);
        });
        return liquidityEvents;
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

    public static List<UniswapEvent> findSwapMM(String protocol, JsonNode logJson) {
        JsonNode logLists = logJson.get("logs");
        List<UniswapEvent> uniswapEvents = new ArrayList<>();
        for (JsonNode tmp : logLists) {
            String contractAddress = tmp.get("address").asText().toLowerCase();
            if (tmp.get("data").toString().length() <= 2) continue;
            String data = tmp.get("data").asText().substring(2);
            JsonNode logIndexNode = tmp.get("logIndex") != null ? tmp.get("logIndex") : tmp.get("logindex");
            BigInteger logIndex = new BigInteger(logIndexNode.asText().substring(2), 16);
            List<String> topicLists = parseTopics(tmp.get("topics"));

            // 这种交易就是做市商交易。
            // Swap (uint256 nonce, index_topic_1 address user, index_topic_2 address mm, address mmTreasury, address baseToken, address quoteToken, uint256 baseTokenAmount, uint256 quoteTokenAmount)
            // 所以我们 logearn 理解的交易里面的 pool 地址应该是：mm 地址
            // 所以我们 logearn 理解的交易里面的 from 地址 应该为 log 中的 contract 地址
            // 所以我们 logearn 理解的交易里面的 to 地址 应该为 log from 地址

            if (topicLists.size() >= 3
                    && "0xe7d6f812e1a54298ddef0b881cd08a4d452d9de35eb18b5145aa580fdda18b26".equalsIgnoreCase(topicLists.get(0))
                    && data.length() == 384) {
                BigInteger amountIn = web3HexToBigInteger(data.substring(256, 320));
                BigInteger amountOut = web3HexToBigInteger(data.substring(320, 384));

                String sender = contractAddress;
                String to = "0x" + topicLists.get(1).substring(26).toLowerCase();
                String poolContract = "0x" + topicLists.get(2).substring(26).toLowerCase();

                UniswapEvent uniswapEvent = UniswapEvent.builder()
                        .sender(sender)
                        .to(to)
                        .amountIn(amountIn)
                        .amountOut(amountOut)
                        .logIndex(logIndex)
                        .contractAddress(poolContract)
                        .fromMergedTransferEvent(new ArrayList<>())
                        .toMergedTransferEvent(new ArrayList<>())
                        .protocol(protocol)
                        .version("mmpool")
                        .build();
                uniswapEvents.add(uniswapEvent);
            }
        }
        return uniswapEvents;
    }

    // pancake 有这种池子
    // https://phalcon.blocksec.com/explorer/tx/bsc/0x2ce4c7dc67a95cbb2a14236ebfb3dc83a99f0c2625f1ecde8f42b6e0de805869
    public static List<UniswapEvent> findSwapStableCoin(String protocol, JsonNode logJson) {
        JsonNode logLists = logJson.get("logs");
        List<UniswapEvent> uniswapEvents = new ArrayList<>();
        for (JsonNode tmp : logLists) {
            String contractAddress = tmp.get("address").asText().toLowerCase();
            if (tmp.get("data").toString().length() <= 2) continue;
            String data = tmp.get("data").asText().substring(2);
            JsonNode logIndexNode = tmp.get("logIndex") != null ? tmp.get("logIndex") : tmp.get("logindex");
            BigInteger logIndex = new BigInteger(logIndexNode.asText().substring(2), 16);
            List<String> topicLists = parseTopics(tmp.get("topics"));

            if (topicLists.size() == 2
                    // TokenExchange (index_topic_1 address buyer, uint256 sold_id, uint256 tokens_sold, uint256 bought_id, uint256 tokens_bought)
                    && "0xb2e76ae99761dc136e598d4a629bb347eccb9532a5f8bbd72e18467c3c34cc98".equalsIgnoreCase(topicLists.get(0))) {

                BigInteger amountIn = web3HexToBigInteger(data.substring(64, 128));
                BigInteger amountOut = web3HexToBigInteger(data.substring(192, 256));

                String buyer = "0x" + topicLists.get(1).substring(26).toLowerCase();

                UniswapEvent uniswapEvent = UniswapEvent.builder()
                        .sender(buyer)
                        .to(buyer)
                        .amountIn(amountIn)
                        .amountOut(amountOut)
                        .logIndex(logIndex)
                        .contractAddress(contractAddress)
                        .fromMergedTransferEvent(new ArrayList<>())
                        .toMergedTransferEvent(new ArrayList<>())
                        .protocol(protocol)
                        .version("stablecoinswap")
                        .build();
                uniswapEvents.add(uniswapEvent);
            }
        }
        return uniswapEvents;
    }
}
