package cn.xlystar.parse.solSwap.moonshot;

import org.apache.commons.codec.binary.Hex;
import org.bitcoinj.core.Base58;

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
        // ✅   
        // String tx_hash1 = "XNwzX92LJBcZCW9yoVymeavUpRzDiwS7zjunDfw3iZuQPazcYEf8YN5CKh99xETk9Cex9fV2xa9HcpvpKwoFx38";
        // String[] accounts1 = "GRJ9sEwMo6uVjcuwUPARRF27Bt7jS6JYEVjb86ytDoB8, Cb8Fnhp95f9dLxB3sYkNCbN3Mjxuc3v2uQZ7uVeqvNGB, Ha3b8aAmR7jPY7DCFfbCJPDSzHBxYgn1zN4N42uphirM, 8swT5CP6KsAHNLgdiBjMBZnEwkC7LtyXw4sLSoZQmoon, 8aEoiP3PmQGr2TiFYmhQ3UE9ymZoSsnpD3JvU8qQZfbY, HmWvfGpYyRvduNvKWb3URti6MYb7wwMHeAf2Hypufumr, 36Eru7v11oU5Pfrojyn5oY3nETA1a1iqsw2WUu6afkM9, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA, ATokenGPvbdGVxr1b2hvZbsiqW5xWH25efTNsLJA8knL, metaqbxxUerdq28cj1RbAWkYQm3ybzjb6a8bt518x1s, 11111111111111111111111111111111".split(", ");
        // String base58Data1 = "032ca4b87b0df5b31300000042696720426974636f696e2042756c6c72756e030000004242424400000068747470733a2f2f63646e2e64657873637265656e65722e636f6d2f636d732f746f6b656e732f6d657461646174612f3035754f646c354e7149753678716176746571680900000064a7b3b6e00d0100";
        // testInstruction("tokenMint", base58Data1, accounts1, tx_hash1);             
        // ✅ 
        // String tx_hash2 = "518uiSY12zCGVcAkbgSSmSZinFEUXPoYvWLmB6odjRCtVGGAPd7b3CLdxGZM9UUUxcNQ2rtars6JM36iwRpVSaZS";
        // String[] accounts2 = "H2G8G2nA2RjhEjTeAMhddwxiRtK6oNmQF3k5vFyEmLTv, Be12J5YBbjfr8oWrB3pdaj31ZYpbto5FF8UT2hbf9LYi, 8WuyaFG4J6NJxDav3XWnuPqSM1tLo7bxMAhkiA4sq8yY, A6DmEyRaMTPD6KkNX676NgLjgXnimkTPgzjSjaA6xf1z, 3udvfL24waJcLhskRAsStNMoNUvtyXdxrWQz4hgi953N, 5K5RtTWzzLp4P8Npi84ocf7F1vBsAu29N1irG4iiUnzt, FGti4ZTBoj4V1YQN5ZmuzaTxV66sMtcsod3xKtQbmoon, 36Eru7v11oU5Pfrojyn5oY3nETA1a1iqsw2WUu6afkM9, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA, ATokenGPvbdGVxr1b2hvZbsiqW5xWH25efTNsLJA8knL, 11111111111111111111111111111111".split(", ");
        // String base58Data2 = "66063d1201daebea000000000000000000bf2f2000000000000000000000000000";
        // testInstruction("BUY", base58Data2, accounts2, tx_hash2);    
        
        // ✅ 
        // String tx_hash3 = "NQxf9Q3RKJfF2jnoogtcJBpvUpXRNP4dFiKnP9edrdkoXGmQ6sfxNuzQ732n5VdkTE4Y7jLepKn9ZiDrd7yhDi7";
        // String[] accounts4 = "7nxsfkAwLxv846Fj5G1MVpNZqbEVLxUh8G8bvgZhcbzh, HFHWum1PpK1BLJ184YYWkUKaZnpbSTMnn9bByzekT5gX, J7tXxvnRZn51hJ6bckr9zLA7qjH8stpo284FjLagbgp9, F5AkEFa3WSCWqo3nBBGuvLQmfUDi7F4u4SpBo3opBYPa, 3udvfL24waJcLhskRAsStNMoNUvtyXdxrWQz4hgi953N, 5K5RtTWzzLp4P8Npi84ocf7F1vBsAu29N1irG4iiUnzt, AxDpRC2nDhd6FjeBC3v59ZYxnPea1fASHuLKtvmEmoon, 36Eru7v11oU5Pfrojyn5oY3nETA1a1iqsw2WUu6afkM9, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA, ATokenGPvbdGVxr1b2hvZbsiqW5xWH25efTNsLJA8knL, 11111111111111111111111111111111".split(", ");
        // String base58Data4 = "33e685a4017f83ad00b45524562f0100d9a5a4000000000000f401000000000000";
        // testInstruction("SELL", base58Data4, accounts4, tx_hash3);    
      
        // ❌ 这两个 case 没过， 同样一个 migrateFunds 指令， 但是解析结果不一样, 你要检查一下
        String tx_hash5 = "fgUZ8j9F57iBGFa9c8NDppQ2rGUt4fFPaDEr61UoYejxu6XbfAaDdALk67VBaDRhy4HAJdzk8PwzHTd7WTH4L75";
        String[] accounts5 = "Cb8Fnhp95f9dLxB3sYkNCbN3Mjxuc3v2uQZ7uVeqvNGB, CGsqR7CTqTwbmAUTPnfg9Bj9GLJgkrUD9rhjh3vHEYvh, EpCMRNKLxkuyTLQqePw9w9MTHFsDaizFBCLYsZmwKmzD, 4oeMjUuwPyf17TwGPLHEuoN9e95GkEcv8CZjWxgdYsqV, 9KURT5eq4aPN9fGV8ihLoEoZCKnnKJzeBwniaqtknpPo, GpVYN3tP5JGYHkM3zonYQFhRhJQKRJiaZwrgeQBihxHh, 36Eru7v11oU5Pfrojyn5oY3nETA1a1iqsw2WUu6afkM9, 11111111111111111111111111111111, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA, ATokenGPvbdGVxr1b2hvZbsiqW5xWH25efTNsLJA8knL".split(", ");
        String base58Data5 = "2ae50ae7bd3ec1ae";
        testInstruction("MIGRATE_FUNDS", base58Data5, accounts5, tx_hash5);    
        
        // ❌  这两个 case 没过， 同样一个 migrateFunds 指令， 但是解析结果不一样, 你要检查一下
        String tx_hash6 = "2AKHEDPWRfes1xcWihZFwidyHCtfdQ8Bk83eWkUp9zgQbHRNFYa5gHLa1AXQVNXBer8sveuFaCaR1HGrYJGCq5AY";
        String[] accounts6 = "CGsqR7CTqTwbmAUTPnfg9Bj9GLJgkrUD9rhjh3vHEYvh, DTUWHnstzraGx8ohqPVLCwcCgXRwQ3vLMnk7FC54rh7Z, EYXpjPPW4a5opqoL7cqQqW54yaPgL6mEd1cTwX4XNZAP, 9Rc1c73krCnaQtpvFG2wpgTPRn8P5cnNp41EmF3vTtF9, 2x1KALH1oBkqPmmYqsAB3CX9VnYQgCGdvwCDxjrxQj49, 3udvfL24waJcLhskRAsStNMoNUvtyXdxrWQz4hgi953N, 5K5RtTWzzLp4P8Npi84ocf7F1vBsAu29N1irG4iiUnzt, 36Eru7v11oU5Pfrojyn5oY3nETA1a1iqsw2WUu6afkM9, 11111111111111111111111111111111, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA, ATokenGPvbdGVxr1b2hvZbsiqW5xWH25efTNsLJA8knL".split(", ");
        String base58Data6 = "2ae50ae7bd3ec1ae";
        testInstruction("MIGRATE_FUNDS", base58Data6, accounts6, tx_hash6);    
        
        // ✅ 
        // String tx_hash6 = "5dJABuWZi5DR3SZhZ5ndKCSDisnzxPUnYfCTRdzyRFbpWCNBac7EnobXbGFG7ZefpRerXNL3aqWEfi8FFnH1bhg1";
        // String[] accounts6 = "51V3VBfMwkNbUMSHDzKC882LuYdR8Hgi3fNtYJyxz8xQ, 36Eru7v11oU5Pfrojyn5oY3nETA1a1iqsw2WUu6afkM9, 11111111111111111111111111111111".split(", ");
        // String base58Data6 = "0deca4ad6afda4b901a781bf533a7325f9acbf0407e2d8a80768e43aad2145edcd445466253da0b12e01ac2e81c51d4fdfff8dfd1f1a8289a95fd12322366e25e5c716ce679cd50af7cc013b8e74b3d893c946d9255772aa50dabf13cb196e7b1fbebb05dd50ffb17b61a101401035d35abb53125d91e9b3003cb8b8bf465665bf0145eb0b99f883d1d0c019012b3353ce467dfb7a3b9c972c1d3cac5fa70fda60cc23c3ea1e77fd293a655dc901640001500104000000000000000100c817a8040000000100010601090180969800000000000100ca9a3b00000000010a000000";
        // testInstruction("CONFIG_INIT", base58Data6, accounts6, tx_hash6);
        
        // ✅ 
        // String tx_hash7 = "38MyrJT55QN1AC6DCaCL3mH1xAU6DmkSWTKK2WeCSjLZui4YjVSg9SRpdtyx4ijAPK3q5fEJKaBC45ybWq8p8FrA";
        // String[] accounts7 = "51V3VBfMwkNbUMSHDzKC882LuYdR8Hgi3fNtYJyxz8xQ, 36Eru7v11oU5Pfrojyn5oY3nETA1a1iqsw2WUu6afkM9".split(", ");
        // String base58Data7 = "50256d88528759f1000001915371560946df338f1b61fdc1bca67fa35677bdcdda23c2a134b6b94e6c45650000000000000001060109000000";
        // testInstruction("CONFIG_UPDATE", base58Data7, accounts7, tx_hash7);

        // ✅ 
        // String tx_hash8 = "2z2fuhvYszbgGpj8NtGmci2K1AKtmsvW417CqQsaUfnazGV73DQ8XWvGxd9zgZrooYcAZU4HA2wQkeCNz9ct3Us1";
        // String[] accounts8 = "51V3VBfMwkNbUMSHDzKC882LuYdR8Hgi3fNtYJyxz8xQ, 36Eru7v11oU5Pfrojyn5oY3nETA1a1iqsw2WUu6afkM9".split(", ");
        // String base58Data8 = "50256d88528759f10000000000000000010088526a740000000001060109000000";
        // testInstruction("CONFIG_UPDATE", base58Data8, accounts8, tx_hash8);
    }


    private static void testInstruction(String testName, String base58Data, String[] accounts, String tx) {
        try {
            System.out.println("\n测试 " + testName);
            System.out.println("\nTX " + tx);
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