package cn.xlystar.parse.solSwap.moonshot;

import org.apache.commons.codec.binary.Hex;
import java.util.Map;

public class MoonshotInstructionParserTest {

    public static void main(String[] args) {
        System.out.println("开始测试 Moonshot 指令解析器...\n");
        
        // 测试代币铸造相关操作
        testTokenMintOperations();
     
        System.out.println("\n所有测试完成!");
    }

    private static void testTokenMintOperations() {
        System.out.println("\n=== 测试代币铸造操作 ===");

        // InitializeMint
        // ✅   tx: 5PyzTwDDnwHQrSdMsqD8oPEBboouvwAUBdm7D1qBYPgH5pWZLA8HrNEpjnrseXnPwkWnN8tNF4Anxst46621hWdm
        String tx_hash1 = "XNwzX92LJBcZCW9yoVymeavUpRzDiwS7zjunDfw3iZuQPazcYEf8YN5CKh99xETk9Cex9fV2xa9HcpvpKwoFx38";
        String[] accounts1 = "GRJ9sEwMo6uVjcuwUPARRF27Bt7jS6JYEVjb86ytDoB8, Cb8Fnhp95f9dLxB3sYkNCbN3Mjxuc3v2uQZ7uVeqvNGB, Ha3b8aAmR7jPY7DCFfbCJPDSzHBxYgn1zN4N42uphirM, 8swT5CP6KsAHNLgdiBjMBZnEwkC7LtyXw4sLSoZQmoon, 8aEoiP3PmQGr2TiFYmhQ3UE9ymZoSsnpD3JvU8qQZfbY, HmWvfGpYyRvduNvKWb3URti6MYb7wwMHeAf2Hypufumr, 36Eru7v11oU5Pfrojyn5oY3nETA1a1iqsw2WUu6afkM9, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA, ATokenGPvbdGVxr1b2hvZbsiqW5xWH25efTNsLJA8knL, metaqbxxUerdq28cj1RbAWkYQm3ybzjb6a8bt518x1s, 11111111111111111111111111111111".split(", ");
        String base58Data1 = "032ca4b87b0df5b31300000042696720426974636f696e2042756c6c72756e030000004242424400000068747470733a2f2f63646e2e64657873637265656e65722e636f6d2f636d732f746f6b656e732f6d657461646174612f3035754f646c354e7149753678716176746571680900000064a7b3b6e00d0100";
        testInstruction("tokenMint", base58Data1, accounts1, tx_hash1);             
      
    }


    private static void testInstruction(String testName, String base58Data, String[] accounts, String tx) {
        try {
            System.out.println("\n测试 " + testName);
            System.out.println("Base58 数据: " + base58Data);
            System.out.println("账户列表:");
            for (int i = 0; i < accounts.length; i++) {
                System.out.println("  " + i + ": " + accounts[i]);
            }
            
            byte[] data = Hex.decodeHex(base58Data.toCharArray());
            MoonshotInstructionParser parser = new MoonshotInstructionParser();
            Map<String, Object> result = parser.parseInstruction(data, accounts);
            System.out.println("✅ 解析结果: " + result);
            
        } catch (Exception e) {
            System.err.println("❌ 测试失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 