package cn.xlystar.parse.solSwap;

import cn.xlystar.parse.solSwap.system_program.SystemInstructionParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;
import org.bitcoinj.core.Base58;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class SolParseData {

    public static Map<String, Object> parseInstruction(String programId, String[] inputAccount, String instructionData) throws Exception {
        Map<String, Object> res = new HashMap<>();
        // 1: 解码 Base58 字符串
        if (StringUtils.isEmpty(instructionData)) return res;
        byte[] decodedData = Base58.decode(instructionData);

        // 2: 匹配程序
        JsonNode idlFile = readIdlFile("/Users/xiaolingyong/java_project/kingdata-mc-udf/src/main/resources/solana/idl/spl-token.json");
        JsonNode programIdlNode = idlFile.get(programId);
        if (programIdlNode.isNull()) return res;

        // 3: 匹配指令
        byte methodId = decodedData[0];
        for (JsonNode instructionNode : programIdlNode) {
            int conditionMethodId = instructionNode.get("input_method_id").asInt();

            if (methodId == conditionMethodId) {
                String instructionName = instructionNode.get("instruction_name").asText();
                Map<String, Object> info = parseInstructionParams(instructionNode.get("instruction_params"), inputAccount, Arrays.copyOfRange(decodedData, 1, decodedData.length));
                res.put("type", instructionName);
                res.put("info", info);
                System.out.println("[parseInstruction] 结果:" + res);
                break;
            }
        }
        return res;
    }

    public static Map<String, Object> parseInstructionParams(JsonNode instructionParams, String[] inputAccount, byte[] decodedData) {
        Map<String, Object> result = new LinkedHashMap<>();

        int position = 0;
        for (JsonNode paramNode : instructionParams) {
            String type = paramNode.get("type").asText();
            String name = paramNode.get("name").asText();
            if (type.equals("account_index")) {
                int index = paramNode.get("index").asInt();
                result.put(name, inputAccount[index]);
            } else if (type.equals("account")) {
                int length = paramNode.get("length").asInt();
                byte[] ownerBytes = Arrays.copyOfRange(decodedData, position, length);
                String owner = Base58.encode(ownerBytes);
                result.put(name, owner);
                position = position + length;
            } else if (type.equals("int")) {
                int length = paramNode.get("length").asInt();
                byte[] ownerBytes = Arrays.copyOfRange(decodedData, position, length);
                long owner = byteArrayToLong(ownerBytes, ByteOrder.LITTLE_ENDIAN);
                result.put(name, owner);
                position = position + length;
            }
        }
        return result;
    }

    public static void transfer(String data) {
        byte[] decodedData = Base58.decode(data);
        // 源账户地址（32 bytes）
        byte[] sourceAccount = new byte[32];
        System.arraycopy(decodedData, 1, sourceAccount, 0, 32);
        String sourceAccountStr = Base58.encode(sourceAccount);

        // 目标账户地址（32 bytes）
        byte[] destinationAccount = new byte[32];
        System.arraycopy(decodedData, 33, destinationAccount, 0, 32);
        String destinationAccountStr = Base58.encode(destinationAccount);

        // 转账金额（4 bytes，little-endian）
        byte[] amountBytes = new byte[4];
        System.arraycopy(decodedData, 65, amountBytes, 0, 4);
        int amount = ByteBuffer.wrap(amountBytes).order(java.nio.ByteOrder.LITTLE_ENDIAN).getInt();
        System.out.printf("sourceAccountStr:%s\n" +
                "destinationAccountStr:%s\n" +
                "amount:%s\n", sourceAccountStr, destinationAccountStr, amount);
    }

    // 读取 IDL 文件并解析
    public static JsonNode readIdlFile(String filePath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(new File(filePath));
    }

    // 使用 ByteBuffer 将 byte[] 转换为 long
    public static long byteArrayToLong(byte[] bytes, ByteOrder byteOrder) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);  // 包装字节数组
        buffer.order(byteOrder);  // 设置字节顺序（大端或小端）
        return buffer.getLong();  // 获取 long 值
    }

    public static void parseSystemInstruction(String encodedData, String[] accounts) {
        byte[] data = Base58.decode(encodedData);
        Map<String, Object> result = SystemInstructionParser.parseInstruction(data, accounts);
        System.out.println("解析结果: " + result);
    }
}
