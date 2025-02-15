package cn.xlystar.parse.solSwap;

import org.apache.commons.collections.MapUtils;
import org.bouncycastle.util.encoders.Hex;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public abstract class InstructionParser {

    /**
     * 返回指令参数
     *
     * @param data
     * @param accounts
     * @return Map:
     * - info<Map>: 包含所有指令的参数 K,V
     * - unparsed: 不能解析出来的方法符
     * - error: 非正常的错误信息
     */
    // 解析指令
    public Map<String, Object> parseInstruction(byte[] data, String[] accounts) {
        Map<String, Object> result = new HashMap<>();
        ByteBuffer buffer = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN);

        try {
            String discriminator = getMethodId(buffer);
            result.put("method_id", discriminator);
            Map<String, Object> info = matchInstruction(discriminator, buffer, accounts);
            if (MapUtils.isEmpty(info)) result.put("unparsed", discriminator);
            else result.put("info", info);
        } catch (Exception e) {
            result.put("error", "data:" + Hex.toHexString(data) + ", accounts: " + Arrays.toString(accounts) + ", Failed to parse instruction: " + Arrays.toString(Arrays.stream(e.getStackTrace()).toArray()));
        }
        return result;
    }

    // 匹配指令
    public abstract Map<String, Object> matchInstruction(String methodId, ByteBuffer buffer, String[] accounts);

    public Map<String, Object> parseLogs(byte[] data) {
        Map<String, Object> result = new HashMap<>();
        ByteBuffer buffer = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN);

        try {
            String discriminator = getLogEventType(buffer);
            result.put("log_type", discriminator);
            Map<String, Object> info = matchLogEvent(buffer, discriminator);
            if (MapUtils.isEmpty(info) || info.size() <= 1) result.put("unparsed", discriminator);
            else result.put("info", info);
        } catch (Exception e) {
            result.put("error", "data:" + Hex.toHexString(data) + ", Failed to parse logs: " + Arrays.toString(Arrays.stream(e.getStackTrace()).toArray()));
        }
        return result;

    }

    public Map<String, Object> matchLogEvent(ByteBuffer buffer, String eventType) {
        return new HashMap<>();
    }

    public String getLogEventType(ByteBuffer buffer) {
        return "";
    }

    // 解析方法标识符
    public abstract String getMethodId(ByteBuffer buffer);


}
