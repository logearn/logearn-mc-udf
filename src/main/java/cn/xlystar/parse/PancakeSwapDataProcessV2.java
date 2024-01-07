package cn.xlystar.parse;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.math.BigInteger;
import java.util.HashMap;

public class PancakeSwapDataProcessV2 {

    /**
     * v2接口解析转发
     * params inputData                         请求的data数据
     * params caller                            发起交易的调用者
     * params value                             发送的eth数量（单位wei）
     * return result HashMap<String, String>    单笔交易
     * result.caller                            发起交易的地址（调用者地址）
     * result.methodId                          方法 ID
     * result.to                                接受token的地址
     * result.tokenIn                           输入的token地址
     * result.tokenOut                          输出的token地址
     * result.amountIn                          输入的token数量
     * result.amountOut                         输出的token数量
     */
    public static HashMap<String, String> decodeInputData(String inputData, String caller, String value) {

        HashMap<String, String> result = new HashMap<>();

        String methodId = inputData.substring(0,10);
        String encodedData = inputData.substring(10);

        result.put("caller", caller);
        result.put("methodId", methodId);
        result.put("protocol", "pancake");
        result.put("chain", "56");

        JsonObject tmp = new JsonObject();
        JsonArray tmpPath = new JsonArray();
        switch (methodId){

            case "0x5c11d795":

            case "0x38ed1739":
                tmp = swapExactTokensForTokensDecoder(encodedData);
                result.put("to", tmp.get("to").getAsString());
                result.put("amountIn", tmp.get("amountIn").getAsString());
                result.put("amountOut", tmp.get("amountOutMin").getAsString());
                tmpPath = tmp.get("path").getAsJsonArray();
                result.put("tokenIn", tmpPath.get(0).getAsString());
                result.put("tokenOut", tmpPath.get(tmpPath.size()-1).getAsString());
                break;

            case "0x7ff36ab5":

            case "0xb6f9de95":
                tmp = swapExactETHForTokens(encodedData);
                result.put("to", tmp.get("to").getAsString());
                result.put("amountIn", value);
                result.put("amountOut", tmp.get("amountOutMin").getAsString());
                tmpPath = tmp.get("path").getAsJsonArray();
                result.put("tokenIn", tmpPath.get(0).getAsString());
                result.put("tokenOut", tmpPath.get(tmpPath.size()-1).getAsString());
                break;

            case "0xfb3bdb41":
                tmp = swapETHForExactTokens(encodedData);
                result.put("to", tmp.get("to").getAsString());
                result.put("amountIn", value);
                result.put("amountOut", tmp.get("amountOut").getAsString());
                tmpPath = tmp.get("path").getAsJsonArray();
                result.put("tokenIn", tmpPath.get(0).getAsString());
                result.put("tokenOut", tmpPath.get(tmpPath.size()-1).getAsString());
                break;

            case "0x8803dbee":
                tmp = swapTokensForExactTokens(encodedData);
                result.put("to", tmp.get("to").getAsString());
                result.put("amountIn", tmp.get("amountInMax").getAsString());
                result.put("amountOut", tmp.get("amountOut").getAsString());
                tmpPath = tmp.get("path").getAsJsonArray();
                result.put("tokenIn", tmpPath.get(0).getAsString());
                result.put("tokenOut", tmpPath.get(tmpPath.size()-1).getAsString());
                break;

            case "0x4a25d94a":
                tmp = swapTokensForExactETH(encodedData);
                result.put("to", tmp.get("to").getAsString());
                result.put("amountIn", tmp.get("amountInMax").getAsString());
                result.put("amountOut", tmp.get("amountOut").getAsString());
                tmpPath = tmp.get("path").getAsJsonArray();
                result.put("tokenIn", tmpPath.get(0).getAsString());
                result.put("tokenOut", tmpPath.get(tmpPath.size()-1).getAsString());
                break;

            case "0x791ac947":

            case "0x18cbafe5":
                tmp = swapExactTokensForETH(encodedData);
                result.put("to", tmp.get("to").getAsString());
                result.put("amountIn", tmp.get("amountIn").getAsString());
                result.put("amountOut", tmp.get("amountOutMin").getAsString());
                tmpPath = tmp.get("path").getAsJsonArray();
                result.put("tokenIn", tmpPath.get(0).getAsString());
                result.put("tokenOut", tmpPath.get(tmpPath.size()-1).getAsString());
                break;

            default:
                break;
        }
        return result;
    }

    /**
    * Function: swapExactTokensForETHSupportingFeeOnTransferTokens(uint256 amountIn, uint256 amountOutMin, address[] path, address to, uint256 deadline)
    * */

    /**
     * 解析swapExactTokensForTokensDecoder
     * 方法：swapExactTokensForTokens(uint256 amountIn, uint256 amountOutMin, address[] path, address to, uint256 deadline)
     * MethodID: 0x38ed1739\0x5c11d795
     */
    public static JsonObject swapExactTokensForTokensDecoder(String encodedData) {
        JsonObject result = new JsonObject();

        // 解析amountIn（要兑换的数量）
        String amountInHex = encodedData.substring(0, 64);
        BigInteger amountIn = new BigInteger(amountInHex, 16);
        result.addProperty("amountIn", amountIn.toString());

        // 解析amountOutMin（希望获得的最小数量）
        String amountOutMinHex = encodedData.substring(64, 128);
        BigInteger amountOutMin = new BigInteger(amountOutMinHex, 16);
        result.addProperty("amountOutMin", amountOutMin.toString());

        // 解析path（最优路径）的偏移量
        String pathLocationHex = encodedData.substring(128, 192);
        int pathLocation = new BigInteger(pathLocationHex, 16).intValue() * 2;

        // 解析path（最优路径）的长度
        String pathLengthHex = encodedData.substring(pathLocation, pathLocation + 64);
        int pathLength = new BigInteger(pathLengthHex, 16).intValue();

        // 解析path（最优路径）的元素
        JsonArray pathArray = new JsonArray();
        for (int i = 0; i < pathLength; i++) {
            String pathItem = encodedData.substring(pathLocation + 64 + i * 64, pathLocation + 128 + i * 64);
            pathArray.add("0x" + pathItem.substring(24));
        }
        result.add("path", pathArray);

        // 解析to（获得token的地址）
        String toAddressHex = encodedData.substring(192, 256);
        result.addProperty("to", "0x" + toAddressHex.substring(24));

        // 解析deadline（交易截止时间）
        String deadlineHex = encodedData.substring(256, 320);
        BigInteger deadline = new BigInteger(deadlineHex, 16);
        result.addProperty("deadline", deadline.toString());

        return result;
    }

    /**
     * 解析SwapTokensForExactTokens
     * 方法：swapTokensForExactTokens(uint256 amountOut, uint256 amountInMax, address[] path, address to, uint256 deadline)
     * MethodID: 0x8803dbee
     */
    public static JsonObject swapTokensForExactTokens(String encodedData) {
        JsonObject result = new JsonObject();

        // 解码amountOut
        String amountOutHex = encodedData.substring(0, 64);
        BigInteger amountOut = new BigInteger(amountOutHex, 16);
        result.addProperty("amountOut", amountOut.toString());

        // 解码amountInMax
        String amountInMaxHex = encodedData.substring(64, 128);
        BigInteger amountInMax = new BigInteger(amountInMaxHex, 16);
        result.addProperty("amountInMax", amountInMax.toString());

        // 解码to地址
        String toAddressHex = encodedData.substring(192, 256);
        result.addProperty("to", "0x" + toAddressHex.substring(24));

        // 解码截止时间
        String deadlineHex = encodedData.substring(256, 320);
        BigInteger deadline = new BigInteger(deadlineHex, 16);
        result.addProperty("deadline", deadline.toString());

        // 解码path数组
        String pathOffsetHex = encodedData.substring(128, 192);
        int pathOffset = new BigInteger(pathOffsetHex, 16).intValue() * 2;

        String pathLengthHex = encodedData.substring(pathOffset, pathOffset + 64);
        int pathLength = new BigInteger(pathLengthHex, 16).intValue();

        JsonArray pathArray = new JsonArray();
        for (int i = 0; i < pathLength; i++) {
            String pathItem = encodedData.substring(pathOffset + 64 + i * 64, pathOffset + 128 + i * 64);
            pathArray.add("0x" + pathItem.substring(24));
        }
        result.add("path", pathArray);

        return result;
    }

    /**
     * 解析swapExactETHForTokens
     * 方法：swapExactETHForTokens(uint256 amountOutMin, address[] path, address to, uint256 deadline)
     * MethodID: 0x7ff36ab5、0xb6f9de95
     */
    public static JsonObject swapExactETHForTokens(String encodedData) {
        JsonObject result = new JsonObject();

        // 解码amountOutMin
        String amountOutMinHex = encodedData.substring(0, 64);
        BigInteger amountOutMin = new BigInteger(amountOutMinHex, 16);
        result.addProperty("amountOutMin", amountOutMin.toString());

        // 解码to地址
        String toAddressHex = encodedData.substring(128, 192);
        result.addProperty("to", "0x" + toAddressHex.substring(24));

        // 解码截止时间
        String deadlineHex = encodedData.substring(192, 256);
        BigInteger deadline = new BigInteger(deadlineHex, 16);
        result.addProperty("deadline", deadline.toString());

        // 解码path数组
        String pathOffsetHex = encodedData.substring(64, 128);
        int pathOffset = new BigInteger(pathOffsetHex, 16).intValue() * 2; // 为了转换为字符串的索引

        String pathLengthHex = encodedData.substring(pathOffset, pathOffset + 64);
        int pathLength = new BigInteger(pathLengthHex, 16).intValue();

        JsonArray pathArray = new JsonArray();
        for (int i = 0; i < pathLength; i++) {
            String pathItem = encodedData.substring(pathOffset + 64 + i * 64, pathOffset + 128 + i * 64);
            pathArray.add("0x" + pathItem.substring(24));
        }
        result.add("path", pathArray);

        return result;
    }

    /**
     * 解析swapTokensForExactETH
     * 方法：swapTokensForExactETH(uint256 amountOut, uint256 amountInMax, address[] path, address to, uint256 deadline)
     * MethodID: 0x4a25d94a
     */
    public static JsonObject swapTokensForExactETH(String encodedData) {
        JsonObject result = new JsonObject();

        // 解码amountOut
        String amountOutHex = encodedData.substring(0, 64);
        BigInteger amountOut = new BigInteger(amountOutHex, 16);
        result.addProperty("amountOut", amountOut.toString());

        // 解码amountInMax
        String amountInMaxHex = encodedData.substring(64, 128);
        BigInteger amountInMax = new BigInteger(amountInMaxHex, 16);
        result.addProperty("amountInMax", amountInMax.toString());

        // 解码to地址
        String toAddressHex = encodedData.substring(192, 256);
        result.addProperty("to", "0x" + toAddressHex.substring(24));

        // 解码截止时间
        String deadlineHex = encodedData.substring(256, 320);
        BigInteger deadline = new BigInteger(deadlineHex, 16);
        result.addProperty("deadline", deadline.toString());

        // 解码path数组
        String pathOffsetHex = encodedData.substring(128, 192);
        int pathOffset = new BigInteger(pathOffsetHex, 16).intValue() * 2;

        String pathLengthHex = encodedData.substring(pathOffset, pathOffset + 64);
        int pathLength = new BigInteger(pathLengthHex, 16).intValue();

        JsonArray pathArray = new JsonArray();
        for (int i = 0; i < pathLength; i++) {
            String pathItem = encodedData.substring(pathOffset + 64 + i * 64, pathOffset + 128 + i * 64);
            pathArray.add("0x" + pathItem.substring(24));
        }
        result.add("path", pathArray);

        return result;
    }

    /**
     * 解析swapExactTokensForETH
     * 方法：swapExactTokensForETH(uint256 amountIn, uint256 amountOutMin, address[] path, address to, uint256 deadline)
     * MethodID: 0x18cbafe5
     */
    public static JsonObject swapExactTokensForETH(String encodedData) {
        JsonObject result = new JsonObject();

        // 解码amountIn
        String amountInHex = encodedData.substring(0, 64);
        BigInteger amountIn = new BigInteger(amountInHex, 16);
        result.addProperty("amountIn", amountIn.toString());

        // 解码amountOutMin
        String amountOutMinHex = encodedData.substring(64, 128);
        BigInteger amountOutMin = new BigInteger(amountOutMinHex, 16);
        result.addProperty("amountOutMin", amountOutMin.toString());

        // 解码to地址
        String toAddressHex = encodedData.substring(192, 256);
        BigInteger toAddress = new BigInteger(toAddressHex, 16);
        result.addProperty("to", "0x" + toAddress.toString(16));

        // 解码截止时间
        String deadlineHex = encodedData.substring(256, 320);
        BigInteger deadline = new BigInteger(deadlineHex, 16);
        result.addProperty("deadline", deadline.toString());

        // 解码path数组
        String pathOffsetHex = encodedData.substring(128, 192);
        int pathOffset = new BigInteger(pathOffsetHex, 16).intValue() * 2;

        String pathLengthHex = encodedData.substring(pathOffset, pathOffset + 64);
        int pathLength = new BigInteger(pathLengthHex, 16).intValue();

        JsonArray pathArray = new JsonArray();
        for (int i = 0; i < pathLength; i++) {
            String pathItem = encodedData.substring(pathOffset + 64 + i * 64, pathOffset + 128 + i * 64);
            pathArray.add("0x" + pathItem.substring(24));
        }
        result.add("path", pathArray);

        return result;
    }

    /**
     * 解析swapETHForExactTokens
     * 方法：swapETHForExactTokens(uint256 amountOut, address[] path, address to, uint256 deadline)
     * MethodID: 0xfb3bdb41
     */
    public static JsonObject swapETHForExactTokens(String encodedData) {
        JsonObject result = new JsonObject();


        // 解码amountOut
        String amountOutHex = encodedData.substring(0, 64);
        BigInteger amountOut = new BigInteger(amountOutHex, 16);
        result.addProperty("amountOut", amountOut.toString());

        // 解码to地址
        String toAddressHex = encodedData.substring(128, 192);
        BigInteger toAddress = new BigInteger(toAddressHex, 16);
        result.addProperty("to", "0x" + toAddress.toString(16));

        // 解码截止时间
        String deadlineHex = encodedData.substring(192, 256);
        BigInteger deadline = new BigInteger(deadlineHex, 16);
        result.addProperty("deadline", deadline.toString());

        // 解码path数组
        String pathOffsetHex = encodedData.substring(64, 128);
        int pathOffset = new BigInteger(pathOffsetHex, 16).intValue() * 2; // 为了转换为字符串的索引

        String pathLengthHex = encodedData.substring(pathOffset, pathOffset + 64);
        int pathLength = new BigInteger(pathLengthHex, 16).intValue();

        JsonArray pathArray = new JsonArray();
        for (int i = 0; i < pathLength; i++) {
            String pathItem = encodedData.substring(pathOffset + 64 + i * 64, pathOffset + 128 + i * 64);
            pathArray.add("0x" + pathItem.substring(24));
        }
        result.add("path", pathArray);

        return result;
    }

    public static void main(String[] args) {
        //String inputData = "0x38ed173900000000000000000000000000000000000000000185bc8414e2ab533aa4fbf100000000000000000000000000000000000000000001c0fe93594a6aba3aa66f00000000000000000000000000000000000000000000000000000000000000a00000000000000000000000001900a3f43054819b1e38000d02a16a806b1bebc60000000000000000000000000000000000000000000000000000000060b40fcd0000000000000000000000000000000000000000000000000000000000000003000000000000000000000000a3e059c0b01f07f211c85bf7b4f1d907afb011df000000000000000000000000c02aaa39b223fe8d0a0e5c4f27ead9083c756cc200000000000000000000000015874d65e649880c2614e7a480cb7c9a55787ff6";
        //String inputData = "0x7ff36ab500000000000000000000000000000000000000000000000001829ddc08658e8300000000000000000000000000000000000000000000000000000000000000800000000000000000000000004e9c3ac6970ee90757a04baf3f6189d187854b3d00000000000000000000000000000000000000000000000000000000609b3ab20000000000000000000000000000000000000000000000000000000000000002000000000000000000000000c02aaa39b223fe8d0a0e5c4f27ead9083c756cc2000000000000000000000000389999216860ab8e0175387a0c90e5c52522c945";
        //String inputData = "0xfb3bdb410000000000000000000000000000000000000000000035b361845a620296cb160000000000000000000000000000000000000000000000000000000000000080000000000000000000000000102f25b9c5a7093cda87f8726a2fccfe327a075800000000000000000000000000000000000000000000000000000000609ac9100000000000000000000000000000000000000000000000000000000000000002000000000000000000000000c02aaa39b223fe8d0a0e5c4f27ead9083c756cc200000000000000000000000095ad61b0a150d79219dcf64e1e6cc01f0b64c4ce";
        //String inputData = "0x8803dbee000000000000000000000000000000000000000000000004a18290e621be800000000000000000000000000000000000000000000000000000000000224a53be00000000000000000000000000000000000000000000000000000000000000a0000000000000000000000000d6c8fc05d64a314184fc6f35ac0840494687eaa00000000000000000000000000000000000000000000000000000000064ec125f0000000000000000000000000000000000000000000000000000000000000003000000000000000000000000a0b86991c6218b36c1d19d4a2e9eb0ce3606eb48000000000000000000000000c02aaa39b223fe8d0a0e5c4f27ead9083c756cc2000000000000000000000000a8b919680258d369114910511cc87595aec0be6d";
        //String inputData = "0x4a25d94a0000000000000000000000000000000000000000000000000214e8348c4f000000000000000000000000000000000000000000004d9beb996f4c7bbf552c01c200000000000000000000000000000000000000000000000000000000000000a000000000000000000000000031f4210fa3544eeaaf2a1baf5f10b554097db5d50000000000000000000000000000000000000000000000000000000064ec19970000000000000000000000000000000000000000000000000000000000000002000000000000000000000000471a202f69d6e975da55e363dab1bdb2e86e0c0f000000000000000000000000c02aaa39b223fe8d0a0e5c4f27ead9083c756cc2";
        //String inputData = "0x18cbafe50000000000000000000000000000000000000000565179fa7a28836b43fa6503000000000000000000000000000000000000000000000000026b3ac308f2e40100000000000000000000000000000000000000000000000000000000000000a0000000000000000000000000d401ddf9b6eeb544bec61a7ff628c6a3ee5c3bf40000000000000000000000000000000000000000000000000000000064ec1a3f0000000000000000000000000000000000000000000000000000000000000002000000000000000000000000471a202f69d6e975da55e363dab1bdb2e86e0c0f000000000000000000000000c02aaa39b223fe8d0a0e5c4f27ead9083c756cc2";
        String inputData = "0x5c11d7950000000000000000000000000000000000000000000000004b37902831db3000000000000000000000000000000000000000000000000001226bf12fcd2e7d9f00000000000000000000000000000000000000000000000000000000000000a0000000000000000000000000baf2a1dde40e2aa8e191d8716eab59ca3826682b00000000000000000000000000000000000000000000000000000000657c5299000000000000000000000000000000000000000000000000000000000000000200000000000000000000000043b35e89d15b91162dea1c51133c4c93bdd1c4af00000000000000000000000055d398326f99059ff775485246999027b3197955";
        HashMap<String, String> result = decodeInputData(inputData, "", "119988398889");
        System.out.println(result);
    }

}
