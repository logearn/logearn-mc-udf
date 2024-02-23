package cn.xlystar.entity;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.*;

@Data
@Builder
public class UniswapEvent extends Event implements Serializable {

    private String sender;
    private List<String> senderTag;
    private String to;
    private String tokenIn;
    private String tokenOut;
    private BigInteger amountIn;
    private BigInteger amountOut;
    private BigInteger logIndex;
    private List<String> pair;
    // swap 串联之前的 池子地址
    private String contractAddress;
    // swap 串联之后的池子地址
    private List<String> connectedPoolAddress;
    private String protocol;
    private String version;
    private String errorMsg;

    /**
     * 将多个events，串联起来，形成完整的uniswapEvents事件， 串联规则：前一个swap的to是后一个sender
     * */
    public static List<UniswapEvent> parseFullUniswapEvents(List<UniswapEvent> uniswapEvents){
        ArrayList<UniswapEvent> fullUniswapEvents = new ArrayList<>();
        ArrayList<UniswapEvent> tmpUniswapEvents = new ArrayList<>(uniswapEvents);
        uniswapEvents.forEach(u -> {
            UniswapEvent.UniswapEventBuilder builder = UniswapEvent.builder();
            List<String> originPair = new ArrayList<>(2);
            builder.protocol(u.getProtocol())
                    .version(u.getVersion())
                    .connectedPoolAddress(new ArrayList<>(Collections.singletonList(u.getContractAddress())))
                    .amountIn(u.getAmountIn())
                    .amountOut(u.getAmountOut())
                    .logIndex(u.getLogIndex())
                    .pair(originPair)
                    .tokenIn(u.getTokenIn())
                    .tokenOut(u.getTokenOut())
                    .errorMsg(u.getErrorMsg());

            // 向前找最早1个swap事件
            UniswapEvent _tpre = findPreEvent(tmpUniswapEvents, u);
            builder.tokenIn(_tpre.getTokenIn());
            builder.amountIn(_tpre.getAmountIn());
            builder.sender(_tpre.getSender());
            originPair.add(0, _tpre.getTokenIn());
            // 向后找最后一个swap事件
            UniswapEvent _taft = findAfterEvent(tmpUniswapEvents, u);
            builder.tokenOut(_taft.getTokenOut());
            builder.amountOut(_taft.getAmountOut());
            builder.to(_taft.getTo());
            originPair.add(1, _taft.getTokenOut());
            UniswapEvent build = builder.build();
            fullUniswapEvents.add(build);
        });


        // 过滤重复event事件
        Set<String> seen = new HashSet<>();
        List<UniswapEvent> returnUniswapEvents = new ArrayList<>();

        for (UniswapEvent map : fullUniswapEvents) {
            String key = map.getSender() + map.getTo() + map.getTokenIn() + map.getTokenOut() + map.getAmountIn() + map.getAmountOut();
            if (!seen.contains(key)) {
                seen.add(key);
                returnUniswapEvents.add(map);
            }
        }
        return  returnUniswapEvents;
    }

    /**
     * 向前找到最早的一个swapEvent
     * */
    private static UniswapEvent findPreEvent(ArrayList<UniswapEvent> events, UniswapEvent target) {
        if (events.isEmpty()) {
            return target;
        }
        Iterator<UniswapEvent> iterator = events.iterator();
        while (iterator.hasNext()) {
            UniswapEvent elem = iterator.next();
            if (elem.getTokenOut() != null && target.getTokenIn() != null && elem.getAmountOut() != null && target.getAmountIn() != null) {
                if (elem.getTokenOut().equalsIgnoreCase(target.getTokenIn()) && elem.getAmountOut().equals(target.getAmountIn())
                        && (elem.getTo().equalsIgnoreCase(target.getSender()) || elem.getTo().equalsIgnoreCase(target.getContractAddress()))
                ) {
                    iterator.remove(); // 移除匹配到的元素
                    if (target.getConnectedPoolAddress() == null) target.setConnectedPoolAddress(new ArrayList<>());
                    target.getConnectedPoolAddress().add(elem.getContractAddress());
                    return findPreEvent(events, elem);
                }
            }
        }
        return target; // 或者返回null，如果你希望在未找到时返回空
    }

    /**
     * 向后找到最晚的一个swapEvent
     * */
    private static UniswapEvent findAfterEvent(ArrayList<UniswapEvent> events, UniswapEvent target) {
        if (events.isEmpty()) {
            return target;
        }
        Iterator<UniswapEvent> iterator = events.iterator();
        while (iterator.hasNext()) {
            UniswapEvent elem = iterator.next();
            if (elem.getTokenIn() != null && target.getTokenOut() != null && elem.getAmountIn() != null && target.getAmountOut() != null) {
                if (elem.getTokenIn().equalsIgnoreCase(target.getTokenOut()) && elem.getAmountIn().equals(target.getAmountOut())
                        && (elem.getSender().equalsIgnoreCase(target.getTo()) || elem.getSender().equalsIgnoreCase(target.getContractAddress()))
                ) {
                    iterator.remove(); // 移除匹配到的元素
                    if (target.getConnectedPoolAddress() == null) target.setConnectedPoolAddress(new ArrayList<>());
                    target.getConnectedPoolAddress().add(elem.getContractAddress());
                    return findAfterEvent(events, elem);
                }
            }
        }
        return target; // 或者返回null，如果你希望在未找到时返回空
    }

}
