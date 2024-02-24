package cn.xlystar.entity;

import lombok.Builder;
import lombok.Data;
import lombok.val;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Data
@Builder
public class TransferEvent extends Event implements Serializable {

    private String sender;
    private String receiver;
    private BigInteger logIndex;
    private String contractAddress;
    private String assetType;
    private BigInteger amount;
    private String origin;

    /**
     * 向前找到最早的一个transferEvent
     * */
    public static TransferEvent findAfterTx(List<TransferEvent> internalTxs, BigInteger value, String from, String to, String token, String swapFrom, String swapTo) {
        if (internalTxs.isEmpty() || swapFrom.equals(to)) {
            return TransferEvent.builder()
                    .amount(value)
                    .receiver(to)
                    .sender(from)
                    .contractAddress(token)
                    .build();
        }

        List<TransferEvent> _tmpList = new ArrayList<>();
        AtomicReference<TransferEvent> foundEvent = new AtomicReference<>();
        Iterator<TransferEvent> iterator = internalTxs.iterator();
        while (iterator.hasNext()) {
            TransferEvent elem = iterator.next();
            if (elem.getSender().equalsIgnoreCase(to)
                    && elem.getContractAddress().equalsIgnoreCase(token)
                    && value.compareTo(elem.getAmount())>=0
                    && elem.getAmount().compareTo(value.divide(new BigInteger("2"))) > 0
            ) {
                _tmpList.add(elem);
            }
        }

        if (_tmpList.size() == 1) {
            foundEvent.set(_tmpList.get(0));
        } else if (_tmpList.size() > 1) {
            AtomicInteger count = new AtomicInteger();
            _tmpList.forEach(tp -> {
                if (tp.getAmount().compareTo(value) == 0) {
                    count.getAndIncrement();
                    foundEvent.set(tp);
                }
            });
            // 如果找到了多个，或者1个没找到，那么返回空
            if (count.get() > 1) {
                System.out.printf("value:%s , from:%s, to:%s, token:%s. find more to \n", value.toString(), from, to, token);
                return null;
            }
        }
        if (foundEvent.get() == null || foundEvent.get().getContractAddress().equalsIgnoreCase("")) {
            return TransferEvent.builder()
                    .amount(value)
                    .receiver(to)
                    .sender(from)
                    .contractAddress(token)
                    .build();
        }
        // 移除找到的元素
        internalTxs.remove(foundEvent.get());
        return findAfterTx(internalTxs, foundEvent.get().getAmount(), foundEvent.get().getSender(), foundEvent.get().getReceiver(), foundEvent.get().getContractAddress(), swapFrom, swapTo);
    }

    /**
     * 向后找到最晚的一个transferEvent
     * */
    public static TransferEvent findPreTx(List<TransferEvent> internalTxs, BigInteger value, String from, String to, String token, String swapFrom, String swapTo) {
        if (internalTxs.isEmpty() || from.equals(swapTo)) {
            return TransferEvent.builder()
                    .amount(value)
                    .receiver(to)
                    .sender(from)
                    .contractAddress(token)
                    .build();
        }

        List<TransferEvent> _tmpList = new ArrayList<>();
        AtomicReference<TransferEvent> foundEvent = new AtomicReference<>();
        Iterator<TransferEvent> iterator = internalTxs.iterator();
        while (iterator.hasNext()) {
            TransferEvent elem = iterator.next();
            if (elem.getReceiver().equalsIgnoreCase(from)
                    && elem.getContractAddress().equalsIgnoreCase(token)
                    && elem.getAmount().compareTo(value)>=0
                    && elem.getAmount().compareTo(value.divide(new BigInteger("2"))) > 0
            ) {
                _tmpList.add(elem);
            }
        }
        if (_tmpList.size() == 1) {
            foundEvent.set(_tmpList.get(0));
        } else if (_tmpList.size() > 1) {
            AtomicInteger count = new AtomicInteger();
            _tmpList.forEach(tp -> {
                if (tp.getAmount().compareTo(value) == 0) {
                    count.getAndIncrement();
                    foundEvent.set(tp);
                }
            });
            // 如果找到了多个，或者1个没找到，那么返回空
            if (count.get() > 1) {
                System.out.printf("value:%s , from:%s, to:%s, token:%s. find more from \n", value.toString(), from, to, token);
                return null;
            }
        }
        // 如果啥也没找到
        if (foundEvent.get() == null || foundEvent.get().getContractAddress().equalsIgnoreCase("")) {
            return TransferEvent.builder()
                    .amount(value)
                    .receiver(to)
                    .sender(from)
                    .contractAddress(token)
                    .build();
        }
        // 移除找到的元素
        internalTxs.remove(foundEvent.get());
        return findPreTx(internalTxs, foundEvent.get().getAmount(), foundEvent.get().getSender(), foundEvent.get().getReceiver(), foundEvent.get().getContractAddress(), swapFrom, swapTo);
    }

    /**
     * 统计所有transferEvents的 用户-token-余额
     * */
    public static Map<String, Map<String, BigInteger>> calculateBalances(List<TransferEvent> transactions) {
        Map<String, Map<String, BigInteger>> balances = new HashMap<>();

        for (TransferEvent transaction : transactions) {
            String receiver = transaction.getReceiver();
            String sender = transaction.getSender();
            BigInteger amount = transaction.getAmount();
            String contractAddress = transaction.getContractAddress();

            balances.putIfAbsent(receiver, new HashMap<>());
            balances.putIfAbsent(sender, new HashMap<>());

            balances.get(receiver).put(contractAddress,
                    balances.get(receiver).getOrDefault(contractAddress, BigInteger.ZERO).add(amount));
            balances.get(sender).put(contractAddress,
                    balances.get(sender).getOrDefault(contractAddress, BigInteger.ZERO).subtract(amount));
        }

        return balances;
    }

    /**
     * 获取有价值的地址
     * */
    public static List<String> validAddrs(Map<String, Map<String, BigInteger>> tokenBalances){
        Map<String, Map<String, BigInteger>> _balances = tokenBalances.entrySet().stream()
                .filter(entry -> entry.getValue().size() >= 2 && hasPositiveAndNegative(entry.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        List<String> addrs = new ArrayList<>();
        for (Map.Entry<String, Map<String, BigInteger>> entry : _balances.entrySet()) {
            String address = entry.getKey();
            Map<String, BigInteger> tokens = entry.getValue();

            addrs.add(address.toLowerCase());
        }

        return addrs;
    }

    /**
     * 有价值的地址筛选，拥有的token个数 >=2 并且 其中的余额必须有正数和负数（有进有出）
     * */
    private static boolean hasPositiveAndNegative(Map<String, BigInteger> tokenBalances) {
        boolean hasPositive = false;
        boolean hasNegative = false;

        for (BigInteger balance : tokenBalances.values()) {
            if (balance.compareTo(BigInteger.ZERO) > 0) {
                hasPositive = true;
            } else if (balance.compareTo(BigInteger.ZERO) < 0) {
                hasNegative = true;
            }
            if (hasPositive && hasNegative) {
                return true;
            }
        }
        return false;
    }

    /**
     * 人性化打印计算余额的结果 用户-token-余额
     * */
    public static void printBalances(Map<String, Map<String, BigInteger>> balances) {
        // 使用 TreeMap 以保证地址的顺序
        TreeMap<String, Map<String, BigInteger>> sortedBalances = new TreeMap<>(balances);

        for (Map.Entry<String, Map<String, BigInteger>> entry : sortedBalances.entrySet()) {
            String address = entry.getKey();
            Map<String, BigInteger> tokens = entry.getValue();

            System.out.println("Address: " + address);
            for (Map.Entry<String, BigInteger> tokenEntry : tokens.entrySet()) {
                System.out.println("  Token: " + tokenEntry.getKey() + ", Balance: " + tokenEntry.getValue());
            }
        }
    }

}
