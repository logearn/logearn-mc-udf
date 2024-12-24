package cn.xlystar.parse.solSwap.pump;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.util.Map;

public class PumpDotFunInstructionParserTest {

    public static void main(String[] args) throws DecoderException {
//        testCreate(); // 通过
//        testInitialize();  // 通过
//        testBuy();  // 通过
//        testSell();  // 通过
//        testWithdraw();  // 通过
        testSetParams();  // 通过
    }

    private static void testInstruction(String testCase, String base58Data, String[] accounts){
        try {
        System.out.println("\n=== Testing " + testCase + " ===");
        byte[] decode = Hex.decodeHex(base58Data.toCharArray());
//        byte[] decode = Base58.decode("15P");
        Map<String, Object> result = new PumpDotFunInstructionParser().parseInstruction(decode, accounts);
        System.out.println("Result: " + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void testCreate() {
        System.out.println("\n=== Testing testCreate ===");

        // Case 1: tx_hash: 4wvLd9bDydzFFJwaTXXnxuC52EpuBNs4qK49w19opCwfDYq7sKfe35XJbf1BEugB9xbve7xD9jLxt85sjXqwHoPp
        String[] accounts1 = "CebENJ7qdv3DJJkktcFdm2UUydXYxq51TjRH4U94pump, TSLvdd1pWpHVjahSpsvCXUbgwsL3JAcvokwaKt1eokM, 7osZcFvUscm89Rb7Vt2M2gtokokvEbpjCTTb7KxYVKvs, GnztrpyGzQrgWuuQJHiqvqmtkYx1a5jh7oSwUriAwXmG, 4wTV1YmiEkRvAtNtsSGPtUrqRYQMe5SKy2uB4Jjaxnjf, metaqbxxUerdq28cj1RbAWkYQm3ybzjb6a8bt518x1s, CxGje1JeXxyMP4wjRyuDUduqJMSrUPvEBM1ncGvb3JE4, 2ju3bSihTdaVzFQ7CG9N3H3kccperx2MyPHfppUcpG7L, 11111111111111111111111111111111, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA, ATokenGPvbdGVxr1b2hvZbsiqW5xWH25efTNsLJA8knL, SysvarRent111111111111111111111111111111111, Ce6TQqeHC9p8KetsN6JsjHK7UTZk7nasjjnr7XxXp9F1, 6EF8rrecthR5Dkzon8Nwu78hRvfCKubJ14M5uBEwF6P".split(", ");
        String base58Data1 = "181ec828051c07770800000053706163792041490800000053504143592041494300000068747470733a2f2f697066732e696f2f697066732f516d656a58366970376368697962556636735a6b5a503946313363336a6d6b4a4b56713748775539724d5a587275";  // amountIn=1000000000, minimumAmountOut=100000
        testInstruction("testCreate Case 1", base58Data1, accounts1);
    }

    private static void testInitialize() {
        System.out.println("\n=== Testing testInitialize ===");

        // Case 1: tx_hash: eyyTk2QPWSKEFvU9u25gGsxcWzUXVq6EjD1Vw2HaDQepvv4g4yApJV3H7tfB9ryTm3ddDScQWpMZUdRJ8bJHawW
        String[] accounts1 = "4wTV1YmiEkRvAtNtsSGPtUrqRYQMe5SKy2uB4Jjaxnjf, DCpJReAfonSrgohiQbTmKKbjbqVofspFRHz9yQikzooP, 11111111111111111111111111111111".split(", ");
        String base58Data1 = "afaf6d1f0d989bed";  // amountIn=1000000000, minimumAmountOut=100000
        testInstruction("testInitialize Case 1", base58Data1, accounts1);
    }
    private static void testBuy() {
        System.out.println("\n=== Testing testBuy ===");

        // Case 1: tx_hash: 4v2xaBNUE12fkHNkuhNtNepMsRWWJ3rhBtXPVFfa6zrQtLoHPh7qBQSQjES6gCnrPYdbs67Dp5dBCHVk19ykkSTr
        String[] accounts1 = "4wTV1YmiEkRvAtNtsSGPtUrqRYQMe5SKy2uB4Jjaxnjf, CebN5WGQ4jvEPvsVU4EoHEpgzq1VV7AbicfhtW4xC9iM, 7wECeikAFRC8c33yLtZbXaCNX7Jm4RaBGGnhRxfVpump, 3BjH25x5YJd5rBVEv51XWq2Npfwt1tnEiD8DQQE7e8wN, 7NKqsxJ1eDv7foSZXX5thjQAiJFp6kXFkhH7u4yjmimi, 6BNvziuCgQ1rJSxVF1osBVfpeQjHHvLnaEQmHR6yoPhv, L3Jnfr8n3ms3PQni5epTWgPW7kYAppgVcMNyACHQExd, 11111111111111111111111111111111, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA, SysvarRent111111111111111111111111111111111, Ce6TQqeHC9p8KetsN6JsjHK7UTZk7nasjjnr7XxXp9F1, 6EF8rrecthR5Dkzon8Nwu78hRvfCKubJ14M5uBEwF6P\n".split(", ");
        String base58Data1 = "66063d1201daebea1f72dd31f9000000c03ae50100000000";  // amountIn=1000000000, minimumAmountOut=100000
        testInstruction("testBuy Case 1", base58Data1, accounts1);
    }
    private static void testSell() {
        System.out.println("\n=== Testing testSell ===");

        // Case 1: tx_hash: bJdCVQyeFYYUgif2HYrZZKgzjimfxLixNjxsujWpnyqdVxWJ9Zbj6KDVu2rUKxSNRq5gThX7kUkjzAVLbqEeNBx
        String[] accounts1 = "4wTV1YmiEkRvAtNtsSGPtUrqRYQMe5SKy2uB4Jjaxnjf, CebN5WGQ4jvEPvsVU4EoHEpgzq1VV7AbicfhtW4xC9iM, 5zez1en73xRL2fMwuCVrykEpbzwUJo2FTgGZoPHipump, G5dX19q3LC7Q95utQ2gTweRzyd562vVPLEn6GXZvtgAk, GxqLsfbQEe2YNeQuPBHJQQwBiXrqACbpyAKEyjJB42KK, 276aFCMYaHuoV4TfXRZwcyGCExknPzdVYa7NrnsjFXcx, LXJNQvQzbcwcUewSg6DB4tbFz35Gu3vqomzAdMcSoVo, 11111111111111111111111111111111, ATokenGPvbdGVxr1b2hvZbsiqW5xWH25efTNsLJA8knL, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA, Ce6TQqeHC9p8KetsN6JsjHK7UTZk7nasjjnr7XxXp9F1, 6EF8rrecthR5Dkzon8Nwu78hRvfCKubJ14M5uBEwF6P".split(", ");
        String base58Data1 = "33e685a4017f83ad6c8e8855760000000000000000000000";  // amountIn=1000000000, minimumAmountOut=100000
        testInstruction("testSell Case 1", base58Data1, accounts1);
    }
    private static void testWithdraw() {
        System.out.println("\n=== Testing testWithdraw ===");

        // Case 1: tx_hash: 3vFtKjgZnXtWaRN1TGqp32RueATW4TwFy9ZCbZb8SD7LaZBgmvJX6KTEvUR8MjfAHmtBNEUwpASRq2qrmxrLWHjf
        String[] accounts1 = "4wTV1YmiEkRvAtNtsSGPtUrqRYQMe5SKy2uB4Jjaxnjf, EGqbBGXmDA9QYd1XJkf3GDFoerQYeFW3FrQZZXRza9JL, 9DD71J1rp9pEJTH2JEMWwFN7vGw8L7cyoejjpy5Fpump, 8WYw81HPVVw1wzhm3c5VgD1jRY7TyUjvETFWWS4s8246, 7agkmbCWWs4VBA8SgU55DDR92xXzvigDmyA2gojVEcFn, A8svq69rdyiMPyayupV8PmD3wvHffVqmxEinhYnJwLVu, 39azUYFWPz3VHgKCf3VChUwbpURdCHRxjWVowf5jUJjg, 11111111111111111111111111111111, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA, SysvarRent111111111111111111111111111111111, Ce6TQqeHC9p8KetsN6JsjHK7UTZk7nasjjnr7XxXp9F1, 6EF8rrecthR5Dkzon8Nwu78hRvfCKubJ14M5uBEwF6P".split(", ");
        String base58Data1 = "b712469c946da122";  // amountIn=1000000000, minimumAmountOut=100000
        testInstruction("testWithdraw Case 1", base58Data1, accounts1);
    }
    private static void testSetParams() {
        System.out.println("\n=== Testing testSetParams ===");

        // Case 1: tx_hash: 51etYBydpZrU1JTMi1p2NDrxbtduAqo5sG4xGZHZeDeUfJDuZbKiQtM7wFEyFaVtHQ99kFxwQRvAA3UhY7WdMS11
        String[] accounts1 = "4wTV1YmiEkRvAtNtsSGPtUrqRYQMe5SKy2uB4Jjaxnjf, DCpJReAfonSrgohiQbTmKKbjbqVofspFRHz9yQikzooP, 11111111111111111111111111111111, Ce6TQqeHC9p8KetsN6JsjHK7UTZk7nasjjnr7XxXp9F1, 6EF8rrecthR5Dkzon8Nwu78hRvfCKubJ14M5uBEwF6P".split(", ");
        String base58Data1 = "1beab2349302bb8dad11e6a4fc2944a4fa8251bef815426e1bfb28c6b6646677607c6ad9f566a6460010d847e3cf030000ac23fc060000000078c5fb51d102000080c6a47e8d03000080c6a47e8d0300";  // amountIn=1000000000, minimumAmountOut=100000
        testInstruction("testSetParams Case 1", base58Data1, accounts1);
    }
}