package cn.xlystar.parse.uniswap;

import cn.xlystar.entity.TransferEvent;
import cn.xlystar.entity.Tx;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class InternalTx {

    /**
     * 将内部交易 string 转为 json
     * */
    public static JsonNode parseTrace(String trace) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode txTrace = objectMapper.readTree(trace);
        return txTrace;
    }

    /**
     * 找到value>0的交易，并且将交易转为transferEvent
     * */
    public static List<TransferEvent> parseTx(JsonNode txTrace, String mainToken) {
        ArrayList<Tx> txs = new ArrayList<>();
        if (txTrace.isArray()) {
            for (JsonNode tmp : txTrace) {
                JsonNode action = tmp.get("action");
                if (action == null) {
                    continue;
                }
                JsonNode type = action.get("callType");
                if (type == null || !"call".equals(type.asText())) continue;
                JsonNode value = action.get("value");
                if (value == null || new BigInteger(value.asText()).compareTo(BigInteger.ZERO) <= 0)
                    continue;
                JsonNode from = action.get("from");
                if (from == null) continue;
                JsonNode to = action.get("to");
                if (to == null) continue;
                Tx tx = new Tx(new BigInteger(value.asText()), from.asText(), to.asText());
                txs.add(tx);
            }
        }

        List<TransferEvent> transfers = new ArrayList<>();
        txs.forEach(t -> {
            TransferEvent tevent = TransferEvent.builder().sender(t.getFrom())
                    .receiver(t.getTo())
                    .amount(t.getValue())
                    .contractAddress(mainToken)
                    .origin("internal")
                    .build();
            transfers.add(tevent);
        });
        return transfers;
    }
}
