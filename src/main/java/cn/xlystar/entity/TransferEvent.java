package cn.xlystar.entity;

import lombok.Builder;
import lombok.Data;
import org.apache.commons.beanutils.BeanUtils;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
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
    public static TransferEvent findAfterTx(String originSender, List<TransferEvent> internalTxs, BigInteger value, String from, String to, String token, UniswapEvent ut) throws InvocationTargetException, IllegalAccessException {
        if (internalTxs.isEmpty()) {
            return TransferEvent.builder()
                    .amount(value)
                    .receiver(to)
                    .sender(from)
                    .contractAddress(token)
                    .build();
        }
        boolean matchOriginSender = false;
        if (ut.getSender().equals(to)) {
            matchOriginSender = true;
        }

        List<TransferEvent> _tmpList = new ArrayList<>();
        List<TransferEvent> _bigger_tmpList = new ArrayList<>();
        AtomicReference<TransferEvent> foundEvent = new AtomicReference<>();
        Iterator<TransferEvent> iterator = internalTxs.iterator();
        while (iterator.hasNext()) {
            TransferEvent elem = iterator.next();
            if (elem.getSender().equalsIgnoreCase(to)
                    && elem.getContractAddress().equalsIgnoreCase(token)
                    && !UniswapEvent.isExistMergedTransferEventList(ut.getToMergedTransferEvent(), elem)
                    && (!matchOriginSender || originSender.equals(elem.getReceiver()))
            ) {
                // 这是正常情况
                if (value.compareTo(elem.getAmount()) >= 0
                        && (elem.getReceiver().equals(originSender)
                                || (elem.getAmount().compareTo(value.divide(new BigInteger("2"))) > 0
                                    && !elem.getReceiver().equals(elem.contractAddress)
                                )
                        )
                ) {
                    _tmpList.add(elem);
                } else if (elem.getAmount().compareTo(value) > 0) {
                    _bigger_tmpList.add(elem);
                }
            }
        }

        // 如果没有找到，就要放开，允许多次交易后一次性转出的情况。这个时候就需要，找大于 swap 的 amountOut 的 Transfer
        // 比如这条交易：https://phalcon.blocksec.com/explorer/tx/eth/0x5ec00bad52aea9d98e0b00d592cd296b187ca6d00fdca3b508a3e9ed9573b953
        // 汇总 ETH 后转入到发起者手里
        if (_tmpList.size() < 1) {
            _tmpList = _bigger_tmpList;
        }

        if (_tmpList.size() == 1) {
            foundEvent.set(_tmpList.get(0));
        } else if (_tmpList.size() > 1) {
            AtomicInteger count = new AtomicInteger();
            _tmpList.forEach(tp -> {
                if (tp.getAmount().compareTo(value) == 0) {
                    count.getAndIncrement();

                    // 如果匹配到多个输入金额完全一样到，就以先匹配到谁，就是采用谁
                    if (foundEvent.get() == null) {
                        foundEvent.set(tp);
                    }
                }
            });
            // 如果找到了多个，或者1个没找到，那么返回空
            if (count.get() > 1) {
                // 目前暂时不做任何处理
//                System.out.printf("value:%s , from:%s, to:%s, token:%s. find more to \n", value.toString(), from, to, token);
//                return null;
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
        TransferEvent temp = TransferEvent.builder().build();
        BeanUtils.copyProperties(temp, foundEvent.get());
        ut.getToMergedTransferEvent().add(temp);

        // 因为，有时候可能是因为，一笔金额转入，然后，分成了多个池子去交易。
        BigInteger nextValue = foundEvent.get().getAmount();

        if (foundEvent.get().getAmount().compareTo(value) > 0) {
            foundEvent.get().setAmount(foundEvent.get().getAmount().subtract(value));
            nextValue = value;
        } else {
            internalTxs.remove(foundEvent.get());
        }

        return findAfterTx(originSender, internalTxs, nextValue, foundEvent.get().getSender(), foundEvent.get().getReceiver(), foundEvent.get().getContractAddress(), ut);
    }

    /**
     * 向后找到最晚的一个transferEvent
     * */
    public static TransferEvent findPreTx(String originSender, List<TransferEvent> internalTxs, BigInteger value, String from, String to, String token, UniswapEvent ut) throws InvocationTargetException, IllegalAccessException {
        if (internalTxs.isEmpty()) {
            return TransferEvent.builder()
                    .amount(value)
                    .receiver(to)
                    .sender(from)
                    .contractAddress(token)
                    .build();
        }
        boolean matchOriginSender = false;
        if (from.equals(ut.getTo())) {
            matchOriginSender = true;
        }

        List<TransferEvent> _tmpList = new ArrayList<>();
        AtomicReference<TransferEvent> foundEvent = new AtomicReference<>();
        Iterator<TransferEvent> iterator = internalTxs.iterator();
        while (iterator.hasNext()) {
            TransferEvent elem = iterator.next();
            if (elem.getReceiver().equalsIgnoreCase(from)
                    && elem.getContractAddress().equalsIgnoreCase(token)
                    && elem.getAmount().compareTo(value)>=0
                    && !UniswapEvent.isExistMergedTransferEventList(ut.getFromMergedTransferEvent(), elem)
                    && (!matchOriginSender || originSender.equals(elem.getSender()))
                // 因为 卖的时候，用户可以转入很多个，但是只卖一下部分，所以这个地方，只需要 转入的金额大于等于 swap 的金额即可。
//                    && elem.getAmount().divide(new BigInteger("2")).compareTo(value) > 0
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

                    // 如果匹配到多个输入金额完全一样到，就以先匹配到谁，就是采用谁
                    if (foundEvent.get() == null) {
                        foundEvent.set(tp);
                    }
                }
            });

            // 如果找到了多个，或者1个没找到，那么返回空
            if (count.get() > 1) {
                // 目前暂时不做任何处理
//                System.out.printf("value:%s , from:%s, to:%s, token:%s. find more from \n", value.toString(), from, to, token);
//                return null;
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
        TransferEvent temp = TransferEvent.builder().build();
        BeanUtils.copyProperties(temp, foundEvent.get());
        ut.getFromMergedTransferEvent().add(temp);

        // 只有完全相等的时候，移除找到的元素，否则只是把 该路径消费的 amount 减去即可
        // 因为，有时候可能是因为，一笔金额转入，然后，分成了多个池子去交易。
        BigInteger nextValue = foundEvent.get().getAmount();

        if (foundEvent.get().getAmount().equals(value)) {
            internalTxs.remove(foundEvent.get());
        }
        // 默认使用 TransferOut 金额去找下一个，但是 TransferOut > value 时候，需要使用 value
        if (foundEvent.get().getAmount().compareTo(value) > 0) {
            foundEvent.get().setAmount(foundEvent.get().getAmount().subtract(value));
            nextValue = value;
        }

        return findPreTx(originSender, internalTxs, nextValue, foundEvent.get().getSender(), foundEvent.get().getReceiver(), foundEvent.get().getContractAddress(), ut);
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
