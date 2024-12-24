package cn.xlystar.parse.solSwap;

import cn.xlystar.parse.solSwap.whirlpool.WhirlpoolInstruction;
import org.apache.commons.collections.MapUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Map;

public abstract class InstructionParser {

    /**
     * 返回指令参数
     * @param data
     * @param accounts
     * @return
     *  Map:
     *   - info<Map>: 包含所有指令的参数 K,V
     *   - unparsed: 不能解析出来的方法符
     *   - error: 非正常的错误信息
     */
    // 解析指令
    public Map<String, Object> parseInstruction(byte[] data, String[] accounts) {
        Map<String, Object> result = new HashMap<>();
        ByteBuffer buffer = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN);

        WhirlpoolInstruction instructionType = null;
        String discriminator = "";
        try {
            discriminator = getMethodId(buffer);
            instructionType = WhirlpoolInstruction.fromValue(discriminator);
        } catch (Exception e) {
            result.put("unparsed", discriminator);
            return result;
        }
        result.put("type", instructionType.name());

        try {
            Map<String, Object> info = matchInstruction(instructionType.getValue(), buffer, accounts);
            if (MapUtils.isEmpty(info)) return result;
            else result.put("info", info);
        } catch (Exception e) {
            result.put("error", "Failed to parse instruction: " + e.getMessage());
        }
        return result;
    }

    // 匹配指令
    public abstract Map<String, Object> matchInstruction(String methodId, ByteBuffer buffer, String[] accounts);

    // 解析方法标识符
    public abstract String getMethodId(ByteBuffer buffer);


}
