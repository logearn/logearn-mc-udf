package cn.xlystar.parse.solSwap.spl_token;

import org.bitcoinj.core.Base58;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Map;

public class SplTokenInstructionParserTest {
    public static void main(String[] args) {
        System.out.println("Starting SPL Token instruction parser tests...\n");

//        // 初始化相关指令测试
        testInitializationInstructions();

//         转账相关指令测试
        testTransferInstructions();

        // 授权相关指令测试
        testAuthorityInstructions();

        // 账户状态相关指令测试
        testAccountStateInstructions();
//
//        // 扩展功能指令测试
        testExtensionInstructions();

        System.out.println("\nAll tests completed!");
    }

    private static void testInitializationInstructions() {
        System.out.println("\n=== Testing Initialization Instructions ===");

        // 铸造账户初始化
        testInitializeMint();      // Case 0 通过
        testInitializeMint2();     // Case 20 通过
//
//        // 代币账户初始化
        testInitializeAccount();   // Case 1 通过
        testInitializeAccount2();  // Case 16 通过
        testInitializeAccount3();  // Case 18 通过
//
//        // 多重签名初始化
        testInitializeMultisig();  // Case 2 通过
        testInitializeMultisig2(); // Case 19 通过

        // 其他初始化
        testInitializeImmutableOwner();        // Case 22 通过
    }

    private static void testTransferInstructions() {
        System.out.println("\n=== Testing Transfer Instructions ===");

        // 基础转账
        testTransfer();           // Case 3 通过
        testTransferChecked();    // Case 12 通过
//
//        // 铸造和销毁
        testMintTo();            // Case 7 通过
        testMintToChecked();     // Case 14 通过
        testBurn();              // Case 8 通过
        testBurnChecked();       // Case 15 通过
//
//        // 原生代币相关
        testSyncNative();        // Case 17 通过
    }

    private static void testAuthorityInstructions() {
        System.out.println("\n=== Testing Authority Instructions ===");

        // 授权相关
//        testApprove();          // Case 4 通过
//        testApproveChecked();   // Case 13 通过
//        testRevoke();           // Case 5 通过
//        testSetAuthority();     // Case 6 通过
    }

    private static void testAccountStateInstructions() {
        System.out.println("\n=== Testing Account State Instructions ===");

        // 账户状态
        testCloseAccount();     // Case 9 通过
        testFreezeAccount();    // Case 10 通过
        testThawAccount();      // Case 11 通过
//
//        // 账户数据
        testGetAccountDataSize();  // Case 21 通过
    }

    private static void testExtensionInstructions() {
        System.out.println("\n=== Testing Extension Instructions ===");

        // 金额转换
        testAmountToUiAmount();    // Case 23 通过
        testUiAmountToAmount();    // Case 24 通过

    }
    private static void testTransfer() {
        System.out.println("\n=== Testing Transfer ===");

        // Case 1: tx_hash: 5u78stQsU2fbBTgzzykRH9QUSS1JWGYaTn75nb1BYsYFDkKa2dfzHmVmitMkLqwek4jicThbVpiLwBHSSBazopzc
        String[] accounts1 = new String[]{
                "FZk2u6qtMvSfRfbbHkfgvSJPp7odcS917Un6Fx2zrW2h",
                "H1qQ6Hent1C5wa4Hc3GK2V1sgg4grvDBbmKd5H8dsTmo",
                "Ee7oGQLorSg8tapxb4ym7Y7vAZUuYHMZEgC6boQQJrcP"
        };
        String base58Data1 = "3mw81cMvYcWT";
        testInstruction("Transfer Case 1", base58Data1, accounts1);

        // Case 2 & 3 similar structure...
    }

    private static void testMintTo() {
        System.out.println("\n=== Testing MintTo ===");

        // Case 1: tx_hash: HV8v9TJHM9x8dqmKJk1FbPm64akAa9wseArzDDJ68YUNAVsSPcRVjBmuDNgoF6Rfuu3mQhj3Pdx78fJmFaYWmUh
        String[] accounts1 = new String[]{
                "2cvTxHKejXbWeRQoXpauz9yoVUB86YSCdEUqMvdB99i6",
                "C3xQJvDAEDi1zr1GsoMBFMBMsFTQz4ZYMj3JgFzutUKz",
                "5Q544fKrFoe6tsEbD7S8EmxGTJYAKtTVhAW5Q5pge4j1"
        };
        String base58Data1 = "6oRW73aQjRsm";
        testInstruction("MintTo Case 1", base58Data1, accounts1);

        // Case 2 & 3 similar structure...
    }


    private static void testInstruction(String testName, String base58Data, String[] accounts) {
        System.out.println("\nTesting " + testName);
        byte[] data = Base58.decode(base58Data);
        Map<String, Object> result = new SplTokenInstructionParser().parseInstruction(data, accounts);
        System.out.println("Result: " + result);
    }

    private static void testBurn() {
        System.out.println("\n=== Testing Burn ===");

        // Case 1: tx_hash: 4cVseSMYC1jvLsMTyVq5KgZA3NJwUceebNjv8VNg9PpQ6PLXFJFgPvkXiYVcc4Rk6hYfZ5sUvBx7DK4zABa1zCzE
        String[] accounts1 = new String[]{
                "2YbgGQD6cugB4kbsG5kxDQX6VTCqqvApyUJ3JYDKLMri",
                "AdjNkNpFYyq8VtY16ebMiM4BdtHX7PLBCWTEuP4w2Zj8",
                "FZiFnxb4mGmfEEbS66ospcmEeumytfrWZ7x5UxBqgqiM"
        };
        String base58Data1 = "7LTzq4boYv4f";
        testInstruction("Burn Case 1", base58Data1, accounts1);

        // Case 2: tx_hash: 4cVseSMYC1jvLsMTyVq5KgZA3NJwUceebNjv8VNg9PpQ6PLXFJFgPvkXiYVcc4Rk6hYfZ5sUvBx7DK4zABa1zCzE
        String[] accounts2 = new String[]{
                "9fsN6o3nCfJRLPQogdYcAMdGPRr7CGSr3LoxN25w28W2",
                "AdjNkNpFYyq8VtY16ebMiM4BdtHX7PLBCWTEuP4w2Zj8",
                "AhGLPpXNqgHxg4vw3GV5T6iXiRpjdBjwEpRc1RpfYdSw"
        };
        String base58Data2 = "6zSpuZ2R39Lb";
        testInstruction("Burn Case 2", base58Data2, accounts2);

        // Case 3: tx_hash: 4cVseSMYC1jvLsMTyVq5KgZA3NJwUceebNjv8VNg9PpQ6PLXFJFgPvkXiYVcc4Rk6hYfZ5sUvBx7DK4zABa1zCzE
        String[] accounts3 = new String[]{
                "8F8NNmC1Thvij7RBfzrjmNJvvLyG7rcdKqEn6fu2K3k6",
                "AdjNkNpFYyq8VtY16ebMiM4BdtHX7PLBCWTEuP4w2Zj8",
                "HpFaRraopmdKfAXfA6ZgAuyYr2Ymb9Qs1ck86yyJYncG"
        };
        String base58Data3 = "7AJzEnuAAQGf";
        testInstruction("Burn Case 3", base58Data3, accounts3);
    }

    private static void testInitializeMint() {
        System.out.println("\n=== Testing InitializeMint ===");

        // Case 1: tx_hash: HV8v9TJHM9x8dqmKJk1FbPm64akAa9wseArzDDJ68YUNAVsSPcRVjBmuDNgoF6Rfuu3mQhj3Pdx78fJmFaYWmUh
        String[] accounts1 = new String[]{
                "2cvTxHKejXbWeRQoXpauz9yoVUB86YSCdEUqMvdB99i6",
                "SysvarRent111111111111111111111111111111111"
        };
        // decimals=9, mintAuthority=DjY..., freezeAuthority=None
        String base58Data1 = "1D8qpeSmcAZXbhY6jAPqguwXxxrrFAnmcbUaH5dxdLLS3Ub";
        testInstruction("InitializeMint Case 1", base58Data1, accounts1);

    }

    private static void testInitializeAccount() {
        System.out.println("\n=== Testing InitializeAccount ===");

        // Case 1: tx_hash: HV8v9TJHM9x8dqmKJk1FbPm64akAa9wseArzDDJ68YUNAVsSPcRVjBmuDNgoF6Rfuu3mQhj3Pdx78fJmFaYWmUh
        String[] accounts1 = new String[]{
                "36fu9ZBqmLDyvgDgFrc547wgfz8BDWb3s5SaPnPrstfZ",
                "So11111111111111111111111111111111111111112",
                "39azUYFWPz3VHgKCf3VChUwbpURdCHRxjWVowf5jUJjg",
                "SysvarRent111111111111111111111111111111111"
        };
        String base58Data1 = "2";
        testInstruction("InitializeAccount Case 1", base58Data1, accounts1);


    }

    private static void testApprove() {
        System.out.println("\n=== Testing Approve ===");

        // Case 1: tx_hash: 41Zav1hLdQeibXy5wicyYyWqVCd9JVsqfwch2f9z8w2xh8LfC4EV26CHhE5rdqbtMrGexXPxeHG9p7EKsWvqwGTy
        String[] accounts1 = new String[]{
                "DjYyqKdwYcdXNMwzaykMwr6hvRJEHGmTQQnEBvqKvUYZ",
                "4QJwbmXp9L6NoAnyPwhat9yyGKJCTMKfH3HGEkhHGkZF",
                "TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA"
        };
        String base58Data1 = "4FLuzWts11Ub";  // amount = 1000000000
        testInstruction("Approve Case 1", base58Data1, accounts1);
    }

    private static void testRevoke() {
        System.out.println("\n=== Testing Revoke ===");

        // Case 1: tx_hash: 4D6zSMaN8kwjqeykPqauL1qH6NSLfQWqG8usxdZnQw2pSesT6tm7LE95Q8eszWWaJrCzsVdD3xcPVcs9upRNnCsJ
        String[] accounts1 = new String[]{
                "3Z16u4d3gm159VPo7E7r3zkQR4uX9eDxHzqiT8tTzNSm",
                "yugM7J32DrpcuhHegKq8P2pyqp7jgWzjxpJjKkpEG4D"
        };
        String base58Data1 = "6";
        testInstruction("Revoke Case 1", base58Data1, accounts1);
    }

    private static void testSetAuthority() {
        System.out.println("\n=== Testing SetAuthority ===");

        // Case 1: tx_hash: 29Zd1Um3Mzapvzr2EW5WEXgiXyRDB4ZeQaN4fEQsQx7rQobRBxhBnggvHycBbp3ZqP41a3kqmSxxwRRBAmZvmEj4
        String[] accounts1 = new String[]{
                "93CijDZyNosATpBi1V6wpitgPnTBKVM8pQNXwdbhYwXa",
                "TSLvdd1pWpHVjahSpsvCXUbgwsL3JAcvokwaKt1eokM"
        };
        // authorityType=0(MintTokens), newAuthority=Some(4QJ...)
        String base58Data1 = "31tb";
        testInstruction("SetAuthority Case 1", base58Data1, accounts1);
    }

    private static void testCloseAccount() {
        System.out.println("\n=== Testing CloseAccount ===");

        // Case 1: tx_hash: 5u78stQsU2fbBTgzzykRH9QUSS1JWGYaTn75nb1BYsYFDkKa2dfzHmVmitMkLqwek4jicThbVpiLwBHSSBazopzc
        String[] accounts1 = new String[]{
                "FZk2u6qtMvSfRfbbHkfgvSJPp7odcS917Un6Fx2zrW2h",
                "Ee7oGQLorSg8tapxb4ym7Y7vAZUuYHMZEgC6boQQJrcP",
                "Ee7oGQLorSg8tapxb4ym7Y7vAZUuYHMZEgC6boQQJrcP"
        };
        String base58Data1 = "A";
        testInstruction("CloseAccount Case 1", base58Data1, accounts1);
    }

    private static void testFreezeAccount() {
        System.out.println("\n=== Testing FreezeAccount ===");

        // Case 1: tx_hash: 3iCA62bohkXaUuxBSQ2mHCNxGLtk35mqpWe3Ri2fCQk52K2G6bdAguk91FR2UVD9mQJXk63aDFbA147fGNi2Eqpm
        String[] accounts1 = new String[]{
                "5FdeWTWV6xfd7Qt3vSyY8g1ehULzrD2sF43PpQXsjo3B",
                "dcuc8Amr83Wz27ZkQ2K9NS6r8zRpf1J6cvArEBDZDmm",
                "D1LbvrJQ9K2WbGPMbM3Fnrf5PSsDH1TDpjqJdHuvs81n"
        };
        String base58Data1 = "B";
        testInstruction("FreezeAccount Case 1", base58Data1, accounts1);
    }

    private static void testThawAccount() {
        System.out.println("\n=== Testing ThawAccount ===");

        // Case 1: tx_hash: 3iCA62bohkXaUuxBSQ2mHCNxGLtk35mqpWe3Ri2fCQk52K2G6bdAguk91FR2UVD9mQJXk63aDFbA147fGNi2Eqpm
        String[] accounts1 = new String[]{
                "5FdeWTWV6xfd7Qt3vSyY8g1ehULzrD2sF43PpQXsjo3B",
                "dcuc8Amr83Wz27ZkQ2K9NS6r8zRpf1J6cvArEBDZDmm",
                "D1LbvrJQ9K2WbGPMbM3Fnrf5PSsDH1TDpjqJdHuvs81n"
        };
        String base58Data1 = "C";
        testInstruction("ThawAccount Case 1", base58Data1, accounts1);
    }

    private static void testTransferChecked() {
        System.out.println("\n=== Testing TransferChecked ===");

        // Case 1: tx_hash: 5u78stQsU2fbBTgzzykRH9QUSS1JWGYaTn75nb1BYsYFDkKa2dfzHmVmitMkLqwek4jicThbVpiLwBHSSBazopzc
        String[] accounts1 = new String[]{
                "H1qQ6Hent1C5wa4Hc3GK2V1sgg4grvDBbmKd5H8dsTmo",
                "So11111111111111111111111111111111111111112",
                "EsU4fjzg2JSSgqbh3jx95qaDSpRe4gLqPWQLbmLjYaLF",
                "2MFoS3MPtvyQ4Wh4M9pdfPjz6UhVoNbFbGJAskCPCj3h"
        };
        String base58Data1 = "iaHbb21z8khhJ"; // amount = 1000000000, decimals = 9
        testInstruction("TransferChecked Case 1", base58Data1, accounts1);
    }

    private static void testApproveChecked() {
        System.out.println("\n=== Testing ApproveChecked ===");

        // Case 1: tx_hash: 32XQvPbcuNKzRDMNgDnSEXcGF3z8q6ojy9UmVXF6nhnXbi194fo8hU87b83YiXMsxPdLLXY9eAVTCTm7UBTyR1Vi
        String[] accounts1 = new String[]{
                "8bLd1VB8YX5PSpyuiacZHJky2P4s3yDShYnpSLotBvGV",
                "5DAFLie57exnYe7ixHgmmy8nWwQ3r6sz8M2K6Mtqpump",
                "5LEE5U5ExkJTcPv6hxspEp6z4bs91iqSZe4jqan86ASm",
                "Lg2xA33QywG4bZ5UrvY1skC9JMfg6RWN64Y8p1vWvWx"
        };
        String base58Data1 = "nXDfJt9D8sazy"; // amount = 1000000000, decimals = 9
        testInstruction("ApproveChecked Case 1", base58Data1, accounts1);
    }
    private static void testMintToChecked() {
        System.out.println("\n=== Testing MintToChecked ===");

        // Case 1: tx_hash: 65sWRjNxrFc6vDRs4Mp8VisPMF7a3qY92N6UKUq3U8TppGBd5xi6YzKY8EBk78JFjQwZ98o1ECjZT2cNj6vzbcuK
        String[] accounts1 = new String[]{
                "2k3wjubTgANDgqPpsvTi7AER43YQiMYGFiiD4nQ3bmGE",
                "CwsTtdMeD4eUwrsxPUny2oa6utTS5Mrndz2y4CiGS9Z6",
                "2FcJbN2kgx3eB1JeJgoBKczpAsXxJzosq269CoidxfhA"
        };
        String base58Data1 = "qswCQYa4L3J7f"; // amount = 1000000000, decimals = 9
        testInstruction("MintToChecked Case 1", base58Data1, accounts1);
    }

    private static void testBurnChecked() {
        System.out.println("\n=== Testing BurnChecked ===");

        // Case 1: tx_hash: 5ZwcBiXRhUH2gT56V1xa8Vs6LoS95x7w1VfgD6NRvUmQ2y2XfjPuHG7xEWkjHTMtvH9bobFz5P7oCWYCQfBJuAwN
        String[] accounts1 = new String[]{
                "DjYyqKdwYcdXNMwzaykMwr6hvRJEHGmTQQnEBvqKvUYZ",
                "4QJwbmXp9L6NoAnyPwhat9yyGKJCTMKfH3HGEkhHGkZF",
                "TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA"
        };
        String base58Data1 = "sdtoZWHcJ2LyN"; // amount = 1000000000, decimals = 9
        testInstruction("BurnChecked Case 1", base58Data1, accounts1);
    }

    private static void testInitializeMultisig() {
        System.out.println("\n=== Testing InitializeMultisig ===");

        // Case 1: tx_hash: 4bqEMhPCx1Yj6EFhxu4e2qAGxEJvY9w9qLrtTyEZfYKvuXXHGPFqmqrNZJr6xGfXhKTZN4TrfgVVrMqkDd7wHFBm
        String[] accounts1 = new String[]{
                "58ndTczxqCTNFP6hwWkkVeFZqv37s1ECvrjBXWXcy1q",
                "SysvarRent111111111111111111111111111111111",
                "CoQ89zQEkHiGXSW4HQKzD9jUqFYWKxrVNjLirdugTQc8",
                "8wmfh4PCnRrjMHCSsMMtgVq3xyyZraxQX4uxtXf1eXWs"
        };
        String base58Data1 = "9s"; // m = 3 (需要3个签名)
        testInstruction("InitializeMultisig Case 1", base58Data1, accounts1);
    }


    private static void testInitializeAccount2() {
        System.out.println("\n=== Testing InitializeAccount2 ===");

        // hash: 2ZoMZ8vNKjyyTm1vDNX8XufPv31kZRyot2JzpqsUgbgjPCuzNt3Z6PryCBrF5vhdz7xiFDuEh51ioDn42s6wvxaE
        String[] accounts = new String[]{
                "EfiKaLLSbefVexc97DuxcEq3xPzMvYiJAqjPytjgc3Zo",  // account
                "Es9vMFrzaCERmJfrF4H2FYD4KCoNkY11McCe8BenwNYB",  // mint
                "SysvarRent111111111111111111111111111111111"   // owner
        };
        String base58Data = "61xy68JZ7wnuexogCce9H3TpW2YdvYHTAmhPiNTLGSait";  // instruction type = 16
        testInstruction("InitializeAccount2", base58Data, accounts);
    }

    private static void testSyncNative() {
        System.out.println("\n=== Testing SyncNative ===");

        // HV8v9TJHM9x8dqmKJk1FbPm64akAa9wseArzDDJ68YUNAVsSPcRVjBmuDNgoF6Rfuu3mQhj3Pdx78fJmFaYWmUh
        String[] accounts = new String[]{
                "7YttLkHDoNj9wyDur5pM1ejNaAvT9X4eqaYcHQqtj2G5"  // account
        };
        String base58Data = "J";  // instruction type = 17
        testInstruction("SyncNative", base58Data, accounts);
    }

    private static void testInitializeAccount3() {
        System.out.println("\n=== Testing InitializeAccount3 ===");

        // hash:5u78stQsU2fbBTgzzykRH9QUSS1JWGYaTn75nb1BYsYFDkKa2dfzHmVmitMkLqwek4jicThbVpiLwBHSSBazopzc
        String[] accounts = new String[]{
                "FZk2u6qtMvSfRfbbHkfgvSJPp7odcS917Un6Fx2zrW2h",  // account
                "So11111111111111111111111111111111111111112"   // owner
        };
        String base58Data = "6ampUCbsc9HwyA4vUgEB6bWZJfT4ix2dFwbMx6P6Dc13M";  // instruction type = 18
        testInstruction("InitializeAccount3", base58Data, accounts);
    }

    private static void testInitializeMultisig2() {
//        System.out.println("\n=== Testing InitializeMultisig2 ===");

        // 暂时没有发现 case dune 中没有
        String[] accounts = new String[]{
                "Multisig11111111111111111111111111111111111",  // multisig
                "Signer111111111111111111111111111111111111",   // signer 1
                "Signer222222222222222222222222222222222222",   // signer 2
                "Signer333333333333333333333333333333333333"    // signer 3
        };
        // instruction type = 19, m = 2
        String base58Data = "1302";
//        testInstruction("InitializeMultisig2", base58Data, accounts);
    }

    private static void testInitializeMint2() {
        System.out.println("\n=== Testing InitializeMint2 ===");

        // hash: 29Zd1Um3Mzapvzr2EW5WEXgiXyRDB4ZeQaN4fEQsQx7rQobRBxhBnggvHycBbp3ZqP41a3kqmSxxwRRBAmZvmEj4
        String[] accounts = new String[]{
                "93CijDZyNosATpBi1V6wpitgPnTBKVM8pQNXwdbhYwXa"   // freeze authority
        };
        String base58Data = "2zt6UCCHp66bJGRS4G7bTsjdxFh6FQ9sBEyRfGyPQKxYisAw";
        testInstruction("InitializeMint2", base58Data, accounts);
    }

    private static void testExtensions() {
        System.out.println("\n=== Testing Extensions ===");

        // TransferFeeExtension
        String[] transferFeeAccounts = new String[]{
                "TokenMint11111111111111111111111111111111111",  // mint
                "Authority11111111111111111111111111111111111"   // authority
        };
        // instruction type = 26, fee = 100 bps, max = 1000000
        String transferFeeData = "1A" + bytesToBase58(new byte[]{(byte)100, 0, 0, 0, 64, 66, 15, 0, 0, 0, 0, 0});
        testInstruction("TransferFeeExtension", transferFeeData, transferFeeAccounts);

        // DefaultAccountStateExtension
        String[] accountStateAccounts = new String[]{
                "TokenMint11111111111111111111111111111111111",  // mint
                "Authority11111111111111111111111111111111111"   // authority
        };
        // instruction type = 28, state = 1 (frozen)
        String accountStateData = "1C01";
        testInstruction("DefaultAccountStateExtension", accountStateData, accountStateAccounts);

        // MemoTransferExtension
        String[] memoTransferAccounts = new String[]{
                "TokenAccount111111111111111111111111111111111",  // account
                "Authority11111111111111111111111111111111111"   // authority
        };
        String memoTransferData = "1E";
        testInstruction("MemoTransferExtension", memoTransferData, memoTransferAccounts);
    }

    // 辅助方法：将字节数组转换为Base58字符串
    private static String bytesToBase58(byte[] bytes) {
        byte[] withType = new byte[bytes.length + 1];
        System.arraycopy(bytes, 0, withType, 1, bytes.length);
        return Base58.encode(withType);
    }

    private static void testInitializeImmutableOwner() {
        System.out.println("\n--- Testing InitializeImmutableOwner (Case 22) ---");

        // 1、hash: 5u78stQsU2fbBTgzzykRH9QUSS1JWGYaTn75nb1BYsYFDkKa2dfzHmVmitMkLqwek4jicThbVpiLwBHSSBazopzc
        String[] accounts = new String[]{
                "FZk2u6qtMvSfRfbbHkfgvSJPp7odcS917Un6Fx2zrW2h"  // account - 要设置为不可变所有者的账户
        };

        String base58Data = "P";
        testInstruction("InitializeImmutableOwner", base58Data, accounts);
    }


    private static void testGetAccountDataSize() {
        System.out.println("\n--- Testing GetAccountDataSize (Case 21) ---");
        // 1、tx_hash: 5u78stQsU2fbBTgzzykRH9QUSS1JWGYaTn75nb1BYsYFDkKa2dfzHmVmitMkLqwek4jicThbVpiLwBHSSBazopzc
        String[] accounts = new String[]{
                "TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA"  // account
        };

        String base58Data = "84eT";
        testInstruction("GetAccountDataSize", base58Data, accounts);
    }

    private static void testAmountToUiAmount() {
        System.out.println("\n--- Testing AmountToUiAmount (Case 23) ---");

        // hash:3WzkPqe2CzNVnz5nsF8vDF3soGLydtj7jbawgsBEs9wLVKeinKZ62dqvyUeTmEgSmTBtDPcnMVUhZFBYktDbwU9c
        String[] accounts = new String[]{
                "EPjFWdd5AufqSSqeM2qN1xzybapC8G4wEGGkZwyTDt1v"  // mint
        };


        String base58Data = "J1WzK5za9Lwh";
        testInstruction("AmountToUiAmount", base58Data, accounts);
    }

    private static void testUiAmountToAmount() {
//        System.out.println("\n--- Testing UiAmountToAmount (Case 24) ---");

        // dune 中 暂时无case
        String[] accounts = new String[]{
                "DjYyqKdwYcdXNMwzaykMwr6hvRJEHGmTQQnEBvqKvUYZ"  // mint
        };


        String base58Data = "";
//        testInstruction("UiAmountToAmount", base58Data, accounts);
    }



    // 辅助方法：将字节数组转换为十六进制字符串
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

}