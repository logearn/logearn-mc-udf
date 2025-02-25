package cn.xlystar.parse.ammswap;

import cn.xlystar.entity.TransferEvent;
import cn.xlystar.entity.Tx;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.math.BigInteger;
import java.util.*;

@Slf4j
public class InternalTx {

    /**
     * 将内部交易 string 转为 json
     * */
    public static JsonNode getTraceJsonNode(String trace) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(trace);
    }

    /**
     * 找到value>0 切 callType == call 的inner交易，并且将交易转为transferEvent
     * */
    public static List<TransferEvent> parseTx(String internalTxs, String mainToken) throws IOException {
        JsonNode txTrace = InternalTx.getTraceJsonNode(internalTxs);
        // 将内部交易的string转为json
        ArrayList<Tx> txs = new ArrayList<>();
        if (txTrace == null) return new ArrayList<>();
        if (txTrace.isArray()) {
            for (JsonNode tmp : txTrace) {
                JsonNode action = tmp.get("action");
                if (action == null) {
                    continue;
                }
                JsonNode type = action.get("callType") == null ? action.get("calltype") : action.get("callType");
                if (type == null || !"call".equals(type.asText())) continue;
                JsonNode value = action.get("value");
                if (value == null || new BigInteger(value.asText().substring(2), 16).compareTo(BigInteger.ZERO) <= 0)
                    continue;
                JsonNode from = action.get("from");
                if (from == null) continue;
                JsonNode to = action.get("to");
                if (to == null) continue;
                Tx tx = new Tx(new BigInteger(value.asText().substring(2), 16), from.asText(), to.asText());
                txs.add(tx);
            }
        }

        List<TransferEvent> transfers = new ArrayList<>();
        txs.forEach(t -> {
            TransferEvent tevent = TransferEvent.builder().sender(t.getFrom())
                    .receiver(t.getTo())
                    .amount(t.getValue())
                    .contractAddress(mainToken)
                    .assetType("erc20")
                    .origin("internal")
                    .build();
            transfers.add(tevent);
        });
        return transfers;
    }

    public static Set<String> getContractAddress(String internalTxs) throws IOException {
        JsonNode txTrace = InternalTx.getTraceJsonNode(internalTxs);
        Set<String> contractAddressList = new HashSet<>();
        // 将内部交易的string转为json
        if (txTrace.isArray()) {
            for (JsonNode tmp : txTrace) {
                JsonNode action = tmp.get("action");
                if (action == null) continue;
                JsonNode value = action.get("value");
                // value == 0
                if (value != null && new BigInteger(value.asText().substring(2), 16).compareTo(BigInteger.ZERO) > 0) continue;
                JsonNode input = action.get("input");
                // input 长度大于 10
                if (input == null || input.asText().length() < 10) continue;
                JsonNode to = action.get("to");
                if (to == null) continue;
                contractAddressList.add(to.asText());
            }
        }
        return contractAddressList;
    }

}
