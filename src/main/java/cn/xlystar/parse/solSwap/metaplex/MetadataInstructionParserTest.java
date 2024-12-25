package cn.xlystar.parse.solSwap.metaplex;

import org.apache.commons.codec.binary.Hex;
import java.util.Map;

public class MetadataInstructionParserTest {


    public static void main(String[] args) {
        System.out.println("Starting metaplex instruction parser tests...\n");
        
        // 基础代币操作 + 账户状态相关操作
        testBasicPlusOperation();

  
        System.out.println("\nAll tests completed!");
    }

    
    private static void testBasicPlusOperation() {        

        // ✅ CreateMetadataAccount
        String tx_hash4 = "2eao1vPiNdtewnBT73qmcMPKeKjKQEUxesshscSwkfa5GwEWyZaNmuQenkpUt75WZ9XWvrBHFDaA7R9MnhwBq7fo";
        String[] accounts4 = "EeLYh1VuTitbubwGoJycPemkdfkkxVuMkoF2TZJD3ERr, C2vsfrUeKAi1FTqqAby68UvFcZfaScjah8YitvmMNkBF, DSnJgzpNcSvFVn6nEFFigkMvLgbMQwpbNVMY7t8m48gV, DSnJgzpNcSvFVn6nEFFigkMvLgbMQwpbNVMY7t8m48gV, 3nEHvmCFogb2UFuwR7gbkpU74finEkcmu77QaG66Yu4v, 11111111111111111111111111111111, SysvarRent111111111111111111111111111111111".split(", ");
        String base58Data4 = "000000000004000000424f595a6200000068747470733a2f2f746573746c61756e63686d796e66742e6d7970696e6174612e636c6f75642f697066732f516d5474587870794b554e6834726967504b50525531686b36724e676677424d774844683376776648636b4432732f32392e6a736f6e84030102000000294d8a15e508c8ecbd9b555aee8e803377cfdb237a56d4589d41112e8da5c7bb01009edbc0a850cfe2dec4d0b6f614b2f0f6b6cd5609637daa13f2f6346732c9f089006401";  // amount=6579000000000
        testInstruction("CreateMetadataAccount", base58Data4, accounts4, tx_hash4); 

        // ✅ CreateMasterEditionV3
        // String tx_hash4 = "3DhLksbqqCapPRkWzMduKrcquHk9sQ55yD5LjYe4tXvaw5M4tCSxsQagvEqm1deUQ16t5dP65A3r6Xp6pJUkRgLu";
        // String[] accounts4 = "H6CcWAULffPkWnWPoJgiQw87mSWSeTqrBHdYhYXHpF6k, 8ak3wEcryvhi7EoUG7BML3igDHXPK3eeNLzgAMdGv8z, A6eDexmTAJN2zDt6XsRnm8hEqyHKjUEivDPRM2SJzrTT, A6eDexmTAJN2zDt6XsRnm8hEqyHKjUEivDPRM2SJzrTT, A6eDexmTAJN2zDt6XsRnm8hEqyHKjUEivDPRM2SJzrTT, Dh1XmYYQNZ62A4iEkYCWQSwAjvHmzLMxMMs2iFG2N2Mw, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA, 11111111111111111111111111111111".split(", ");
        // String base58Data4 = "11010100000000000000";  // amount=6579000000000
        // testInstruction("CreateMasterEditionV3", base58Data4, accounts4, tx_hash4); 
    }       

   

    private static void testInstruction(String testName, String base58Data, String[] accounts, String tx) {
        try {
            System.out.println("\nTesting " + testName);
            System.out.println("tx: " + tx);
            System.out.println("Base58 Data: " + base58Data);
            System.out.println("Accounts:");
            for (int i = 0; i < accounts.length; i++) {
                System.out.println("  " + i + ": " + accounts[i]);
            }
            byte[] data = Hex.decodeHex(base58Data.toCharArray());
            
            Map<String, Object> result = MetadataInstructionParser.parseInstruction(data, accounts);
            System.out.println("✅ Parse result: " + result);
            
        } catch (Exception e) {
            System.err.println("❌ Test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 