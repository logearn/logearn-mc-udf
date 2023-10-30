package cn.xlystar.mc.udaf;

import com.aliyun.odps.udf.UDFException;
import com.aliyun.odps.udf.UDTF;
import com.aliyun.odps.udf.annotation.Resolve;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.math.BigInteger;
import java.util.*;

// UniSwapDataProcessExe
@Resolve("string,string,string->string,string,string,string,string,string,string,string")
public class UniSwapDataProcessExe extends UDTF {

    private static final String V3_SWAP_EXACT_IN = "00";
    private static final String V3_SWAP_EXACT_OUT = "01";
    private static final String V2_SWAP_EXACT_IN = "08";
    private static final String V2_SWAP_EXACT_OUT = "09";

    @Override
    public void process(Object[] args) throws UDFException, IOException {
        if (Objects.isNull(args[0]) && Objects.isNull(args[1])) {
            return;
        }
        String inputData = (String) args[0];
        String caller = (String) args[1];
        String hash = (String) args[2];
        List<Map<String, String>> maps = decodeInputData(inputData, caller);
        for (int i = 0; i < maps.size(); i++) {
            Map<String, String> res = maps.get(i);
            forward(res.get("caller"), res.get("methodId"), res.get("to"),
                    res.get("amountIn"), res.get("amountOut"),
                    res.get("tokenIn"), res.get("tokenOut"), hash);
        }
    }


    /**
     * v3接口解析转发
     * params inputData                             请求的data数据
     * params caller                                发起交易的调用者
     * return result List<Map<String, String>>      多笔交易，是一个数组
     * result.caller                                发起交易的地址（调用者地址）
     * result.methodId                              方法 ID
     * result.to                                    接受token的地址
     * result.tokenIn                               输入的token地址
     * result.tokenOut                              输出的token地址
     * result.amountIn                              输入的token数量
     * result.amountOut                             输出的token数量
     */
    public List<Map<String, String>> decodeInputData(String inputData, String caller) {

        // 使用 ArrayList 来存储多个 交易
        List<Map<String, String>> resList = new ArrayList<>();

        // 截取methid
        String methodId = inputData.substring(0,10);
        // 获取数据区
        String encodedData = inputData.substring(10);

        // 用来临时存放
        Map<String, Object> tmp = new HashMap<>();
        switch (methodId){

            // execute(bytes commands,bytes[] inputs)
            case "0x24856bc3":
                // execute(bytes commands,bytes[] inputs,uint256 deadline)
            case "0x3593564c":
                // 解析命令和input输入数据
                tmp = decodeExecute(encodedData);
                // 解析每个命令对应的 inputdata， 返回多个交易
                JsonObject tmpItems = decodeInput((String) tmp.get("commands"), (List<String>) tmp.get("inputs"));
                // 解析每个交易
                for (Map.Entry<String, JsonElement> entry : tmpItems.entrySet()) {
                    JsonObject item = entry.getValue().getAsJsonObject();
                    // 接受者地址
                    String to = item.get("recipient").getAsString();
                    // 0x1表示发送者即接受者
                    if(to.equals("0x0000000000000000000000000000000000000001")){
                        to = caller;
                    }
                    // 0x2表示uniswap合约是接受者，但是对于使用官方（app.uniswap.org）发起交易来说，接受者都是发起者
                    // 未来如果要统计所有的execute还要考虑更多情况
                    if(to.equals("0x0000000000000000000000000000000000000002")) {
                        to = caller;
                    }

                    // 组合数据
                    HashMap<String, String> result = new HashMap<>();
                    result.put("caller", caller);
                    result.put("methodId", methodId);
                    result.put("to", to);
                    result.put("amountIn", item.get("amountIn").getAsString());
                    result.put("amountOut", item.get("amountOut").getAsString());
                    result.put("tokenIn", item.get("tokenIn").getAsString());
                    result.put("tokenOut", item.get("tokenOut").getAsString());
                    resList.add(result);
                }
                break;

            default:
                break;
        }

        return resList;
    }

    /**
     * 解析 execute
     * 方法：execute(bytes calldata commands, bytes[] calldata inputs, uint256 deadline?)
     * MethodID: 0x3593564c
     */
    public Map<String, Object> decodeExecute(String encodedData) {
        Map<String, Object> result = new HashMap<>();

        // 解码commands-偏移量
        String commandsOffsetHex = encodedData.substring(0, 64);
        int commandsOffset = new BigInteger(commandsOffsetHex, 16).intValue() * 2; // 为了转换为字符串的索引
        // 解码commands-长度
        String commandsLengthHex = encodedData.substring(commandsOffset, commandsOffset + 64);
        int commandsLength = new BigInteger(commandsLengthHex, 16).intValue();
        // 解码commands-数据
        String commandsHex = encodedData.substring(commandsOffset + 64, commandsOffset + 64 + commandsLength * 2);
        result.put("commands", commandsHex);

        // 解码inputs数组-偏移量
        String inputsOffsetHex = encodedData.substring(64, 128);
        int inputsOffset = new BigInteger(inputsOffsetHex, 16).intValue() * 2; // 为了转换为字符串的索引
        // 解码inputs数组-长度
        String inputsLengthHex = encodedData.substring(inputsOffset, inputsOffset + 64);
        int inputsLength = new BigInteger(inputsLengthHex, 16).intValue();
        // 解码inputs数组-数据
        ArrayList<String> inputsArray = new ArrayList();
        for (int i = 0; i < inputsLength; i++) {
            // 解码inputs数组-每一个元素的偏移量
            String inputOffsetHex = encodedData.substring(inputsOffset + 64 + i * 64, inputsOffset + 64 + i * 64 + 64);
            int inputOffset = new BigInteger(inputOffsetHex, 16).intValue() * 2; // 为了转换为字符串的索引
            inputOffset = inputsOffset + 64 + inputOffset;

            // 解码inputs数组-每一个元素的偏长度
            String inputLengthHex = encodedData.substring(inputOffset, inputOffset + 64);
            int inputLength = new BigInteger(inputLengthHex, 16).intValue();

            // 解码inputs数组-每一个元素的数据
            inputsArray.add(encodedData.substring(inputOffset + 64, inputOffset + 64 + inputLength * 2));
        }
        result.put("inputs", inputsArray);

        return result;
    }

    /**
     * 根据传入的命令和输入数据解析结果
     * @param commands 命令字符串
     * @param inputs 输入数据数组
     * @return 返回解析后的JSON对象
     */
    public static JsonObject decodeInput(String commands, List<String> inputs) {
        JsonObject result = new JsonObject();

        int currentIndex = 0;
        for (int i = 0; i < commands.length(); i += 2) {
            JsonObject decodedData = new JsonObject();
            // 每两个字符是一个命令
            String command = commands.substring(i, i + 2);
            // 以下详细的解析逻辑，请必须熟悉uniswap的Dispatcher.sol合约的第45-190行代码，才能理解
            // Dispatcher.sol合约 : https://etherscan.io/address/0x3fc91a3afd70395cd496c647d5a6cc9d4b2b7fad#code
            String tmpPath = "";
            switch (command) {
                case V3_SWAP_EXACT_IN:
                    decodedData = decodeV3CommonData(inputs.get(currentIndex));
                    decodedData.add("amountIn", decodedData.get("amountA"));
                    decodedData.add("amountOut", decodedData.get("amountB"));
                    tmpPath = decodedData.get("path").getAsString();
                    decodedData.addProperty("tokenIn", tmpPath.substring(0, 42));
                    decodedData.addProperty("tokenOut", "0x" + tmpPath.substring(tmpPath.length() - 40));
                    break;
                case V3_SWAP_EXACT_OUT:
                    decodedData = decodeV3CommonData(inputs.get(currentIndex));
                    decodedData.add("amountIn", decodedData.get("amountB"));
                    decodedData.add("amountOut", decodedData.get("amountA"));
                    tmpPath = decodedData.get("path").getAsString();
                    decodedData.addProperty("tokenOut", tmpPath.substring(0, 42));
                    decodedData.addProperty("tokenIn", "0x" + tmpPath.substring(tmpPath.length() - 40));
                    break;
                case V2_SWAP_EXACT_IN:
                    decodedData = decodeV2CommonData(inputs.get(currentIndex));
                    decodedData.add("amountIn", decodedData.get("amountA"));
                    decodedData.add("amountOut", decodedData.get("amountB"));
                    tmpPath = decodedData.get("path").getAsString();
                    decodedData.addProperty("tokenIn", tmpPath.substring(0, 42));
                    decodedData.addProperty("tokenOut", "0x" + tmpPath.substring(tmpPath.length() - 40));
                    break;
                case V2_SWAP_EXACT_OUT:
                    decodedData = decodeV2CommonData(inputs.get(currentIndex));
                    decodedData.add("amountIn", decodedData.get("amountB"));
                    decodedData.add("amountOut", decodedData.get("amountA"));
                    tmpPath = decodedData.get("path").getAsString();
                    decodedData.addProperty("tokenIn", tmpPath.substring(0, 42));
                    decodedData.addProperty("tokenOut", "0x" + tmpPath.substring(tmpPath.length() - 40));
                    break;
                default:
                    break;
            }
            if (!decodedData.entrySet().isEmpty()) {
                result.add(String.valueOf(currentIndex), decodedData);
            }
            currentIndex++;
        }

        return result;
    }

    /**
     * V3_SWAP_EXACT_IN\V3_SWAP_EXACT_OUT:解析公共数据结构
     *
     * @param inputData 输入数据字符串
     * @return 返回解析后的JSON对象
     */
    private static JsonObject decodeV3CommonData(String inputData) {
        JsonObject result = new JsonObject();

        // 解析接收者地址
        String recipient = inputData.substring(24, 64);
        // 解析第一个金额值
        BigInteger amountA = new BigInteger(inputData.substring(64, 128), 16);
        // 解析第二个金额值
        BigInteger amountB = new BigInteger(inputData.substring(128, 192), 16);
        // 判断是否由用户支付, 暂时没有用到
        BigInteger payerIsUserBigInt = new BigInteger(inputData.substring(256, 320), 16);
        boolean payerIsUser = payerIsUserBigInt.equals(BigInteger.ONE);

        // 将解析出的结果存入JSON对象
        result.addProperty("recipient", "0x" + recipient);
        result.addProperty("amountA", amountA.toString());
        result.addProperty("amountB", amountB.toString());
        result.addProperty("payerIsUser", payerIsUser);

        // 解析路径信息-偏移量
        String pathOffsetHex = inputData.substring(192, 256);
        int pathOffset = new BigInteger(pathOffsetHex, 16).intValue() * 2; // 为了转换为字符串的索引
        // 解析路径信息-长度，因为v3的path是bytes类型，所以v3的path的长度是总位数
        String pathLengthHex = inputData.substring(pathOffset, pathOffset + 64);
        int pathLength = new BigInteger(pathLengthHex, 16).intValue();
        // 解析路径信息-数据，v3的path构成是token地址 + fee（手续费6个字符）+ token地址，每个token地址占40个字符
        String pathHex = inputData.substring(pathOffset + 64, pathOffset + 64 + pathLength * 2);
        // 如果您需要从pathHex中提取更多信息，可以在此处进行
        result.addProperty("path", "0x" + pathHex);

        return result;
    }

    /**
     * V2_SWAP_EXACT_IN\V2_SWAP_EXACT_OUT:解析公共数据结构
     *
     * @param inputData 输入数据字符串
     * @return 返回解析后的JSON对象
     */
    private static JsonObject decodeV2CommonData(String inputData) {
        JsonObject result = new JsonObject();

        // 解析接收者地址
        String recipient = inputData.substring(24, 64);
        // 解析第一个金额值
        BigInteger amountA = new BigInteger(inputData.substring(64, 128), 16);
        // 解析第二个金额值
        BigInteger amountB = new BigInteger(inputData.substring(128, 192), 16);
        // 判断是否由用户支付
        BigInteger payerIsUserBigInt = new BigInteger(inputData.substring(256, 320), 16);
        boolean payerIsUser = payerIsUserBigInt.equals(BigInteger.ONE);

        // 将解析出的结果存入JSON对象
        result.addProperty("recipient", "0x" + recipient);
        result.addProperty("amountA", amountA.toString());
        result.addProperty("amountB", amountB.toString());
        result.addProperty("payerIsUser", payerIsUser);

        // 解析路径信息-偏移量
        String pathOffsetHex = inputData.substring(192, 256);
        int pathOffset = new BigInteger(pathOffsetHex, 16).intValue() * 2;
        // 解析路径信息-长度，因为v2的path是address[]类型，所以v2的path的长度是数组的长度
        String pathLengthHex = inputData.substring(pathOffset, pathOffset + 64);
        int pathLength = new BigInteger(pathLengthHex, 16).intValue();
        // 解析路径信息-数据，v2的path构成是token地址+token地址，每个token地址占64个字符
        StringBuilder pathHex = new StringBuilder();
        for (int i = 0; i < pathLength; i += 1) {
            String tpath = inputData.substring(pathOffset + 64 + i * 64, pathOffset + 128 + i * 64);
            pathHex.append(tpath.substring(24, 64));
        }
        // 如果您需要从pathHex中提取更多信息，可以在此处进行
        result.addProperty("path", "0x" + pathHex);

        return result;
    }

    public static void main(String[] args) {
        String inputData = "0x3593564c000000000000000000000000000000000000000000000000000000000000006000000000000000000000000000000000000000000000000000000000000000a000000000000000000000000000000000000000000000000000000000652d89370000000000000000000000000000000000000000000000000000000000000003090101000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000030000000000000000000000000000000000000000000000000000000000000060000000000000000000000000000000000000000000000000000000000000018000000000000000000000000000000000000000000000000000000000000002a000000000000000000000000000000000000000000000000000000000000001000000000000000000000000000000000000000000000000000000000000000001000000000000000000000000000000000000000000000000d02ab486cedc0000000000000000000000000000000000000000000000000000000000058cedce3f00000000000000000000000000000000000000000000000000000000000000a000000000000000000000000000000000000000000000000000000000000000010000000000000000000000000000000000000000000000000000000000000002000000000000000000000000a0b86991c6218b36c1d19d4a2e9eb0ce3606eb48000000000000000000000000c02aaa39b223fe8d0a0e5c4f27ead9083c756cc20000000000000000000000000000000000000000000000000000000000000100000000000000000000000000000000000000000000000000000000000000000100000000000000000000000000000000000000000000000ea300b17a8b78000000000000000000000000000000000000000000000000000000000063eafc733f00000000000000000000000000000000000000000000000000000000000000a00000000000000000000000000000000000000000000000000000000000000001000000000000000000000000000000000000000000000000000000000000002bc02aaa39b223fe8d0a0e5c4f27ead9083c756cc20001f4a0b86991c6218b36c1d19d4a2e9eb0ce3606eb4800000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000001200000000000000000000000000000000000000000000000000000000000000001000000000000000000000000000000000000000000000000d02ab486cedc0000000000000000000000000000000000000000000000000000000000058d82885500000000000000000000000000000000000000000000000000000000000000a000000000000000000000000000000000000000000000000000000000000000010000000000000000000000000000000000000000000000000000000000000042c02aaa39b223fe8d0a0e5c4f27ead9083c756cc20001f42260fac5e5542a773aa44fbcfedf7c193bc2c599000bb8a0b86991c6218b36c1d19d4a2e9eb0ce3606eb48000000000000000000000000000000000000000000000000000000000000";
        UniSwapDataProcessExe uniSwapDataProcessExe = new UniSwapDataProcessExe();

        System.out.println( uniSwapDataProcessExe.decodeInputData(inputData,"xx"));
//        List<Map<String, String>> result = decodeInputData(inputData, "");
//        System.out.println(result);
    }


}

