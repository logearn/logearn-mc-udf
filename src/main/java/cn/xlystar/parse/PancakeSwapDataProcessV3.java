package cn.xlystar.parse;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.math.BigInteger;
import java.util.HashMap;

public class PancakeSwapDataProcessV3 {

    /**
     * v3接口解析转发
     * params inputData                             请求的data数据
     * params caller                                发起交易的调用者
     * return result HashMap<String, String>        交易详情
     * result.caller                                发起交易的地址（调用者地址）
     * result.methodId                              方法 ID
     * result.to                                    接受token的地址
     * result.tokenIn                               输入的token地址
     * result.tokenOut                              输出的token地址
     * result.amountIn                              输入的token数量
     * result.amountOut                             输出的token数量
     */
    public static HashMap<String, String> decodeInputData(String inputData, String caller) {

        HashMap<String, String> result = new HashMap<>();

        // 截取methid
        String methodId = inputData.substring(0,10);
        // 获取数据区
        String encodedData = inputData.substring(10);

        result.put("caller", caller);
        result.put("methodId", methodId);
        result.put("protocol", "pancake");
        result.put("chain", "56");
        // 用来临时存放
        JsonObject tmp = new JsonObject();
        String tmpPath = "";
        JsonArray tmpPathArr;
        switch (methodId){
            case "0x09b81346":
                tmp = decodeExactOutput(encodedData);
                result.put("to", tmp.get("recipient").getAsString());
                result.put("amountIn", tmp.get("amountInMaximum").getAsString());
                result.put("amountOut", tmp.get("amountOut").getAsString());
                tmpPath = tmp.get("path").getAsString();

                result.put("tokenIn", tmpPath.substring(0, 42));
                result.put("tokenOut", "0x" + tmpPath.substring(tmpPath.length()-40));
                break;

            case "0xb858183f":
                tmp = decodeExactInput(encodedData);
                result.put("to", tmp.get("recipient").getAsString());
                result.put("amountIn", tmp.get("amountIn").getAsString());
                result.put("amountOut", tmp.get("amountOutMinimum").getAsString());
                tmpPath = tmp.get("path").getAsString();

                result.put("tokenIn", tmpPath.substring(0, 42));
                result.put("tokenOut", "0x" + tmpPath.substring(tmpPath.length()-40));
                break;

            case "0x04e45aaf":
                tmp = decodeExactInputSingle(encodedData);
                result.put("to", tmp.get("recipient").getAsString());
                result.put("amountIn", tmp.get("amountIn").getAsString());
                result.put("amountOut", tmp.get("amountOutMinimum").getAsString());
                result.put("tokenIn", tmp.get("tokenIn").getAsString());
                result.put("tokenOut", tmp.get("tokenOut").getAsString());
                break;

            case "0x5023b4df":
                tmp = decodeExactOutputSingle(encodedData);
                result.put("to", tmp.get("recipient").getAsString());
                result.put("amountIn", tmp.get("amountInMaximum").getAsString());
                result.put("amountOut", tmp.get("amountOut").getAsString());
                result.put("tokenIn", tmp.get("tokenIn").getAsString());
                result.put("tokenOut", tmp.get("tokenOut").getAsString());
                break;

            case "0x42712a67":
                tmp = swapTokensForExactTokens(encodedData);
                result.put("to", tmp.get("to").getAsString());
                result.put("amountIn", tmp.get("amountInMax").getAsString());
                result.put("amountOut", tmp.get("amountOut").getAsString());
                tmpPathArr = tmp.get("path").getAsJsonArray();
                result.put("tokenIn", tmpPathArr.get(0).getAsString());
                result.put("tokenOut", tmpPathArr.get(1).getAsString());
                break;

            case "0x472b43f3":
                tmp = swapExactTokensForTokensDecoder(encodedData);
                result.put("to", tmp.get("to").getAsString());
                result.put("amountIn", tmp.get("amountIn").getAsString());
                result.put("amountOut", tmp.get("amountOutMin").getAsString());
                tmpPathArr = tmp.get("path").getAsJsonArray();
                result.put("tokenIn", tmpPathArr.get(0).getAsString());
                result.put("tokenOut", tmpPathArr.get(1).getAsString());
                break;

            default:
                break;
        }

        return result;
    }

    /**
     * 解码exactInput函数的输入数据
     * methodId 0xc04b8d59
     *
     * @param dataWithoutMethodId 编码后的字符串
     * @return 解码后的参数
     */
    public static JsonObject decodeExactInput(String dataWithoutMethodId) {
        JsonObject result = new JsonObject();

        // 由于结构体里面的path是动态的，所以这个结构体也是动态，所以要先解析这个结构体的偏移量是64，64个字符以后才是真正的数据区
        int startParams = 64;
        // 解码path（bytes类型）-偏移量
        String pathOffsetHex = dataWithoutMethodId.substring(startParams, startParams + 64);
        BigInteger pathOffset = new BigInteger(pathOffsetHex, 16);
        // 解码path（bytes类型）-长度
        int actualPathOffset = pathOffset.intValue() * 2;  // *2是因为每个字符占两个字节
        String pathLengthHex = dataWithoutMethodId.substring(startParams + actualPathOffset, startParams + actualPathOffset + 64);
        BigInteger pathLength = new BigInteger(pathLengthHex, 16);
        // 解码path（bytes类型）-数据
        String pathHex = dataWithoutMethodId.substring(startParams + actualPathOffset + 64, startParams + actualPathOffset + 64 + (pathLength.intValue() * 2));
        result.addProperty("path", "0x" + pathHex);

        // 解码recipient
        String recipientHex = dataWithoutMethodId.substring(startParams + 64, startParams + 128);
        result.addProperty("recipient", "0x" + recipientHex.substring(24));

        // 解码amountOut
        String amountOutHex = dataWithoutMethodId.substring(startParams + 128, startParams + 192);
        BigInteger amountOutBigInt = new BigInteger(amountOutHex, 16);
        result.addProperty("amountIn", amountOutBigInt.toString());

        // 解码amountInMaximum
        String amountInMaximumHex = dataWithoutMethodId.substring(startParams + 192, startParams + 256);
        BigInteger amountInMaximumBigInt = new BigInteger(amountInMaximumHex, 16);
        result.addProperty("amountOutMinimum", amountInMaximumBigInt.toString());

        return result;
    }

    /**
     * 解码exactInputSingle函数的输入数据
     * methodId 0x414bf389
     *
     * @param dataWithoutMethodId 编码后的字符串
     * @return 解码后的参数
     */
    public static JsonObject decodeExactInputSingle(String dataWithoutMethodId) {
        JsonObject result = new JsonObject();

        // 解码tokenIn
        String tokenInHex = dataWithoutMethodId.substring(0, 64);
        result.addProperty("tokenIn", "0x" + tokenInHex.substring(24));

        // 解码tokenOut
        String tokenOutHex = dataWithoutMethodId.substring(64, 128);
        result.addProperty("tokenOut", "0x" + tokenOutHex.substring(24));

        // 解码fee
        String feeHex = dataWithoutMethodId.substring(128, 192);
        BigInteger feeBigInt = new BigInteger(feeHex, 16);
        result.addProperty("fee", feeBigInt.intValue());

        // 解码recipient
        String recipientHex = dataWithoutMethodId.substring(192, 256);
        result.addProperty("recipient", "0x" + recipientHex.substring(24));

        // 解码amountIn
        String amountInHex = dataWithoutMethodId.substring(256, 320);
        BigInteger amountInBigInt = new BigInteger(amountInHex, 16);
        result.addProperty("amountIn", amountInBigInt.toString());

        // 解码amountOutMinimum
        String amountOutMinimumHex = dataWithoutMethodId.substring(320, 384);
        BigInteger amountOutMinimumBigInt = new BigInteger(amountOutMinimumHex, 16);
        result.addProperty("amountOutMinimum", amountOutMinimumBigInt.toString());

        // 解码sqrtPriceLimitX96
        String sqrtPriceLimitX96Hex = dataWithoutMethodId.substring(384, 448);
        BigInteger sqrtPriceLimitX96BigInt = new BigInteger(sqrtPriceLimitX96Hex, 16);
        result.addProperty("sqrtPriceLimitX96", sqrtPriceLimitX96BigInt.toString());

        return result;
    }

    /**
     * 解码exactOutputSingle函数的输入数据
     *  methodId 0xdb3e2198
     *
     * @param dataWithoutMethodId 编码后的字符串
     * @return 解码后的参数
     */
    public static JsonObject decodeExactOutputSingle(String dataWithoutMethodId) {
        JsonObject result = new JsonObject();

        // 解码tokenIn
        String tokenInHex = dataWithoutMethodId.substring(0, 64);
        result.addProperty("tokenIn", "0x" + tokenInHex.substring(24));

        // 解码tokenOut
        String tokenOutHex = dataWithoutMethodId.substring(64, 128);
        result.addProperty("tokenOut", "0x" + tokenOutHex.substring(24));

        // 解码fee
        String feeHex = dataWithoutMethodId.substring(128, 192);
        BigInteger feeBigInt = new BigInteger(feeHex, 16);
        result.addProperty("fee", feeBigInt.intValue());

        // 解码recipient
        String recipientHex = dataWithoutMethodId.substring(192, 256);
        result.addProperty("recipient", "0x" + recipientHex.substring(24));

        // 解码amountOut
        String amountOutHex = dataWithoutMethodId.substring(256, 320);
        BigInteger amountOutBigInt = new BigInteger(amountOutHex, 16);
        result.addProperty("amountOut", amountOutBigInt.toString());

        // 解码amountInMaximum
        String amountInMaximumHex = dataWithoutMethodId.substring(320, 384);
        BigInteger amountInMaximumBigInt = new BigInteger(amountInMaximumHex, 16);
        result.addProperty("amountInMaximum", amountInMaximumBigInt.toString());

        // 解码sqrtPriceLimitX96
        String sqrtPriceLimitX96Hex = dataWithoutMethodId.substring(384, 448);
        BigInteger sqrtPriceLimitX96BigInt = new BigInteger(sqrtPriceLimitX96Hex, 16);
        result.addProperty("sqrtPriceLimitX96", sqrtPriceLimitX96BigInt.toString());

        return result;
    }

    /**
     * 解码exactOutput函数的输入数据
     *  methodId 0xf28c0498
     *
     * @param dataWithoutMethodId 编码后的字符串
     * @return 解码后的参数
     */
    public static JsonObject decodeExactOutput(String dataWithoutMethodId) {
        JsonObject result = new JsonObject();

        // 由于结构体里面的path是动态的，所以这个结构体也是动态，所以要先解析这个结构体的偏移量是64，64个字符以后才是真正的数据区
        int startParams = 64;
        // 解码path（bytes类型）-偏移量
        String pathOffsetHex = dataWithoutMethodId.substring(startParams, startParams + 64);
        BigInteger pathOffset = new BigInteger(pathOffsetHex, 16);
        // 解码path（bytes类型）-长度
        int actualPathOffset = pathOffset.intValue() * 2;  // *2是因为每个字符占两个字节
        String pathLengthHex = dataWithoutMethodId.substring(startParams + actualPathOffset, startParams + actualPathOffset + 64);
        BigInteger pathLength = new BigInteger(pathLengthHex, 16);
        // 解码path（bytes类型）-数据
        String pathHex = dataWithoutMethodId.substring(startParams + actualPathOffset + 64, startParams + actualPathOffset + 64 + (pathLength.intValue() * 2));
        result.addProperty("path", "0x" + pathHex);

        // 解码recipient
        String recipientHex = dataWithoutMethodId.substring(startParams + 64, startParams + 128);
        result.addProperty("recipient", "0x" + recipientHex.substring(24));

        // 解码amountOut
        String amountOutHex = dataWithoutMethodId.substring(startParams + 128, startParams + 192);
        BigInteger amountOutBigInt = new BigInteger(amountOutHex, 16);
        result.addProperty("amountOut", amountOutBigInt.toString());

        // 解码amountInMaximum
        String amountInMaximumHex = dataWithoutMethodId.substring(startParams + 192, startParams + 256);
        BigInteger amountInMaximumBigInt = new BigInteger(amountInMaximumHex, 16);
        result.addProperty("amountInMaximum", amountInMaximumBigInt.toString());

        return result;
    }

    /**
     * 解析SwapTokensForExactTokens
     * 方法：swapTokensForExactTokens(uint256 amountOut, uint256 amountInMax, address[] path, address to)
     * MethodID: 0x42712a67
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
     * 解析swapExactTokensForTokensDecoder
     * 方法：swapExactTokensForTokens(uint256 amountIn, uint256 amountOutMin, address[] path, address to)
     * MethodID: 0x472b43f3
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

        return result;
    }

    public static void main(String[] args) {
        //String inputData = "0x414bf3890000000000000000000000000e09fabb73bd3ade0a17ecc321fd13a19e81ce82000000000000000000000000bb4cdb9cbd36b01bd1cbaebf2de08d9173bc095c00000000000000000000000000000000000000000000000000000000000009c4000000000000000000000000b98c6340a49cc87256435d7fbd795d95c8f25d11000000000000000000000000000000000000000000000000000000006501d695000000000000000000000000000000000000000000000015af1d78b58c4000000000000000000000000000000000000000000000000000001e77627094ae4280000000000000000000000000000000000000000000000000000000000000000";
        //String inputData = "0xdb3e2198000000000000000000000000bb4cdb9cbd36b01bd1cbaebf2de08d9173bc095c0000000000000000000000000e09fabb73bd3ade0a17ecc321fd13a19e81ce8200000000000000000000000000000000000000000000000000000000000009c4000000000000000000000000b98c6340a49cc87256435d7fbd795d95c8f25d11000000000000000000000000000000000000000000000000000000006501d356000000000000000000000000000000000000000000000015af1d78b58c4000000000000000000000000000000000000000000000000000001ea5d54c3317dedc0000000000000000000000000000000000000000000000000000000000000000";
        //String inputData = "0xc04b8d59000000000000000000000000000000000000000000000000000000000000002000000000000000000000000000000000000000000000000000000000000000a00000000000000000000000009a32e025a8eed451c629031e105c2a26361c5c3c000000000000000000000000000000000000000000000000016345785d8a0000000000000000000000000000000000000000000000000021b4695f50a0c400000000000000000000000000000000000000000000000000215ecee6f952840000000000000000000000000000000000000000000000000000000000000000002b8ac76a51cc950d9822d68b83fe1ad97b32cd580d00006455d398326f99059ff775485246999027b3197955000000000000000000000000000000000000000000";
        String inputData = "0xdb3e2198000000000000000000000000c02aaa39b223fe8d0a0e5c4f27ead9083c756cc2000000000000000000000000c18360217d8f7ab5e7c516566761ea12ce7f9d720000000000000000000000000000000000000000000000000000000000000bb80000000000000000000000008648ad727134b048f99721f1ee346878c28bace00000000000000000000000000000000000000000000000000000000064f84bdb000000000000000000000000000000000000000000000018502aabacdc0d39600000000000000000000000000000000000000000000000001d52a73f655137700000000000000000000000000000000000000000000000000000000000000000";

        HashMap<String, String> result = decodeInputData(inputData, "");
        System.out.println(result);
    }
}

