package cn.xlystar.parse.solSwap.spl_token;

import org.bitcoinj.core.Base58;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Map;

public class SplTokenInstructionParserTest {
    public static void main(String[] args) {
        System.out.println("Starting SPL Token instruction parser tests...\n");

//        // 初始化相关指令测试
//        testInitializationInstructions();

        // 转账相关指令测试
        testTransferInstructions();

//        // 授权相关指令测试
//        testAuthorityInstructions();
//
//        // 账户状态相关指令测试
//        testAccountStateInstructions();
//
//        // 扩展功能指令测试
//        testExtensionInstructions();

        System.out.println("\nAll tests completed!");
    }

    private static void testInitializationInstructions() {
        System.out.println("\n=== Testing Initialization Instructions ===");

        // 铸造账户初始化
        testInitializeMint();      // Case 0
        testInitializeMint2();     // Case 20

        // 代币账户初始化
        testInitializeAccount();   // Case 1
        testInitializeAccount2();  // Case 16
        testInitializeAccount3();  // Case 18

        // 多重签名初始化
        testInitializeMultisig();  // Case 2
        testInitializeMultisig2(); // Case 19

        // 其他初始化
        testInitializeImmutableOwner();        // Case 22
        testInitializeMintCloseAuthority();    // Case 25
    }

    private static void testTransferInstructions() {
        System.out.println("\n=== Testing Transfer Instructions ===");

        // 基础转账
        testTransfer();           // Case 3
//        testTransferChecked();    // Case 12
//
//        // 铸造和销毁
        testMintTo();            // Case 7
//        testMintToChecked();     // Case 14
        testBurn();              // Case 8
//        testBurnChecked();       // Case 15
//
//        // 原生代币相关
//        testSyncNative();        // Case 17
//        testCreateNativeMint();  // Case 31
    }

    private static void testAuthorityInstructions() {
        System.out.println("\n=== Testing Authority Instructions ===");

        // 授权相关
        testApprove();          // Case 4
        testApproveChecked();   // Case 13
        testRevoke();           // Case 5
        testSetAuthority();     // Case 6
    }

    private static void testAccountStateInstructions() {
        System.out.println("\n=== Testing Account State Instructions ===");

        // 账户状态
        testCloseAccount();     // Case 9
        testFreezeAccount();    // Case 10
        testThawAccount();      // Case 11

        // 账户数据
        testGetAccountDataSize();  // Case 21
        testReallocate();         // Case 29
    }

    private static void testExtensionInstructions() {
        System.out.println("\n=== Testing Extension Instructions ===");

        // 金额转换
        testAmountToUiAmount();    // Case 23
        testUiAmountToAmount();    // Case 24

        // 扩展功能
        testTransferFeeExtension();           // Case 26
        testConfidentialTransferExtension();  // Case 27
        testDefaultAccountStateExtension();   // Case 28
        testMemoTransferExtension();          // Case 30
    }
    private static void testTransfer() {
        System.out.println("\n=== Testing Transfer ===");

        // Case 1: tx_hash: 5u78stQsU2fbBTgzzykRH9QUSS1JWGYaTn75nb1BYsYFDkKa2dfzHmVmitMkLqwek4jicThbVpiLwBHSSBazopzc
        String[] accounts1 = new String[]{
                "FZk2u6qtMvSfRfbbHkfgvSJPp7odcS917Un6Fx2zrW2h",
                "J5a4BoK4DWxGAWxKVgfdJBhhd2R6KHyd36niYRbC8dVt",
                "Ee7oGQLorSg8tapxb4ym7Y7vAZUuYHMZEgC6boQQJrcP"
        };
        String base58Data1 = "3Gob3y6Fg9pb";
        testInstruction("Transfer Case 1", base58Data1, accounts1);

        // Case 2 & 3 similar structure...
    }

    private static void testMintTo() {
        System.out.println("\n=== Testing MintTo ===");

        // Case 1: tx_hash: 3f4wHLPZvE3tx3DQyKYqjccRqnxuEf8r3nADrL1arAxfnUYhFTiDc5bx7RTVag3P2PUeaUyejenjK2e8RMwNH5z2
        String[] accounts1 = new String[]{
                "DEZbNByMGMNkBMFq8VAagUAcHD1THqbEepk4K7uSpump",
                "G56JoBDaMbFWD1XjPuUZacZRYK5GPgbccaakm4TUef4c",
                "HeyX2JUzc1s7Q9Z3WEZGoFBBAmuCrnvuEekzzxVZBdJE"
        };
        String base58Data1 = "6ApXSNCamGdm";
        testInstruction("MintTo Case 1", base58Data1, accounts1);

        // Case 2 & 3 similar structure...
    }


    private static void testInstruction(String testName, String base58Data, String[] accounts) {
        System.out.println("\nTesting " + testName);
        byte[] data = Base58.decode(base58Data);
        Map<String, Object> result = SplTokenInstructionParser.parseInstruction(data, accounts);
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

        // Case 1: tx_hash: 4bqEMhPCx1Yj6EFhxu4e2qAGxEJvY9w9qLrtTyEZfYKvuXXHGPFqmqrNZJr6xGfXhKTZN4TrfgVVrMqkDd7wHFBm
        String[] accounts1 = new String[]{
                "DjYyqKdwYcdXNMwzaykMwr6hvRJEHGmTQQnEBvqKvUYZ",
                "SysvarRent111111111111111111111111111111111"
        };
        // decimals=9, mintAuthority=DjY..., freezeAuthority=None
        String base58Data1 = "09DjYyqKdwYcdXNMwzaykMwr6hvRJEHGmTQQnEBvqKvUYZ00";
        testInstruction("InitializeMint Case 1", base58Data1, accounts1);

        // Case 2: tx_hash: 2ZxDm8JBCYbJ5YQYWVqNvqZgz3jBpygY7FE6HmeTEFbxqVqxgXXvKPYGrRKpvZKBdLuVtJhNvqPHeLqxrEh9tWWx
        String[] accounts2 = new String[]{
                "HWHvQhFmJB6gPtqJx3gjxHX1iDZhQ9WJorxwb3iTWVHi",
                "SysvarRent111111111111111111111111111111111"
        };
        // decimals=6, mintAuthority=HWH..., freezeAuthority=Some(HWH...)
        String base58Data2 = "06HWHvQhFmJB6gPtqJx3gjxHX1iDZhQ9WJorxwb3iTWVHi01HWHvQhFmJB6gPtqJx3gjxHX1iDZhQ9WJorxwb3iTWVHi";
        testInstruction("InitializeMint Case 2", base58Data2, accounts2);

        // Case 3: tx_hash: 5fqXJZpyvkE8YvSHbR3VgZEpWxzVaFGV4TakFyCGgBhzHxVr6yzZRqtR7xo6VrBHVmuEwGPjVoXcGxp3A9S4iFhk
        String[] accounts3 = new String[]{
                "2xNweLHLqrbx4zo1waDvgWJHgsUpPj8Y8icbAFeR4a8i",
                "SysvarRent111111111111111111111111111111111"
        };
        // decimals=8, mintAuthority=2xN..., freezeAuthority=None
        String base58Data3 = "082xNweLHLqrbx4zo1waDvgWJHgsUpPj8Y8icbAFeR4a8i00";
        testInstruction("InitializeMint Case 3", base58Data3, accounts3);
    }

    private static void testInitializeAccount() {
        System.out.println("\n=== Testing InitializeAccount ===");

        // Case 1: tx_hash: 57TG3QU6Xq1eQhYQfKU48k1wqUWsHzsDyUBgWiTqZhXgkVdBJZwXqHiZQykiqYrXBhbwPLxNwZKKZVxGvNhRyPry
        String[] accounts1 = new String[]{
                "DjYyqKdwYcdXNMwzaykMwr6hvRJEHGmTQQnEBvqKvUYZ",
                "4QJwbmXp9L6NoAnyPwhat9yyGKJCTMKfH3HGEkhHGkZF",
                "TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA",
                "SysvarRent111111111111111111111111111111111"
        };
        String base58Data1 = "1";
        testInstruction("InitializeAccount Case 1", base58Data1, accounts1);

        // Case 2: tx_hash: 3vDU6JZRwDVVVo3KwGfA3T1YzgzJrDwHGolomBkGxqKJYYDxnPps8Z64eV3HQ4QKVxDyf4yVz2X6Q5VUh4Tdj6Cy
        String[] accounts2 = new String[]{
                "HWHvQhFmJB6gPtqJx3gjxHX1iDZhQ9WJorxwb3iTWVHi",
                "J7nSEX8ADf3pVWhHS7qXqGYoxQqGYwKhNJfEMvFUSk7c",
                "TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA",
                "SysvarRent111111111111111111111111111111111"
        };
        String base58Data2 = "1";
        testInstruction("InitializeAccount Case 2", base58Data2, accounts2);

        // Case 3: tx_hash: 2EARWf5jKMxRpvnqxRBKgGGVXVoXY4qxkJyYCbPwcZbV2bjZhcvJwPkL4LgKtBsHGxSQB7FcdZqR7menjXTyKsXt
        String[] accounts3 = new String[]{
                "2xNweLHLqrbx4zo1waDvgWJHgsUpPj8Y8icbAFeR4a8i",
                "8vqZWWG6ZKqYXkJvxXkYmqNYvG8NkY1X7ea7ZLKZ6T2L",
                "TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA",
                "SysvarRent111111111111111111111111111111111"
        };
        String base58Data3 = "1";
        testInstruction("InitializeAccount Case 3", base58Data3, accounts3);
    }

    private static void testApprove() {
        System.out.println("\n=== Testing Approve ===");

        // Case 1: tx_hash: 4oBFNe4qY8HQGUMChwfU4wEZi3oZ8uvkk9oGJr5akp7NYvJkEQGKKhHhPPf3YHpHNhZoKQJnhqj9FMqZGkeCfM6U
        String[] accounts1 = new String[]{
                "DjYyqKdwYcdXNMwzaykMwr6hvRJEHGmTQQnEBvqKvUYZ",
                "4QJwbmXp9L6NoAnyPwhat9yyGKJCTMKfH3HGEkhHGkZF",
                "TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA"
        };
        String base58Data1 = "4Bxs4cgF7g81qBE7";  // amount = 1000000000
        testInstruction("Approve Case 1", base58Data1, accounts1);
    }

    private static void testRevoke() {
        System.out.println("\n=== Testing Revoke ===");

        // Case 1: tx_hash: 2ZxDm8JBCYbJ5YQYWVqNvqZgz3jBpygY7FE6HmeTEFbxqVqxgXXvKPYGrRKpvZKBdLuVtJhNvqPHeLqxrEh9tWWx
        String[] accounts1 = new String[]{
                "HWHvQhFmJB6gPtqJx3gjxHX1iDZhQ9WJorxwb3iTWVHi",
                "TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA"
        };
        String base58Data1 = "5";
        testInstruction("Revoke Case 1", base58Data1, accounts1);
    }

    private static void testSetAuthority() {
        System.out.println("\n=== Testing SetAuthority ===");

        // Case 1: tx_hash: 5YHEkNjVGKRDw3KJNkyxEqHFxGQYqUxm6vt7kUdtJPBXJu6mZPaGF8vZmZHGGM6Hz8Vj8tQRKUyVmdyxXt1Sjoey
        String[] accounts1 = new String[]{
                "DjYyqKdwYcdXNMwzaykMwr6hvRJEHGmTQQnEBvqKvUYZ",
                "TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA"
        };
        // authorityType=0(MintTokens), newAuthority=Some(4QJ...)
        String base58Data1 = "60014QJwbmXp9L6NoAnyPwhat9yyGKJCTMKfH3HGEkhHGkZF";
        testInstruction("SetAuthority Case 1", base58Data1, accounts1);
    }

    private static void testCloseAccount() {
        System.out.println("\n=== Testing CloseAccount ===");

        // Case 1: tx_hash: 4cVseSMYC1jvLsMTyVq5KgZA3NJwUceebNjv8VNg9PpQ6PLXFJFgPvkXiYVcc4Rk6hYfZ5sUvBx7DK4zABa1zCzE
        String[] accounts1 = new String[]{
                "8F8NNmC1Thvij7RBfzrjmNJvvLyG7rcdKqEn6fu2K3k6",
                "CS34MZNEY2pdhriN4Y6rRE2FDkoYf9G3NFD2UAjCnCn9",
                "HpFaRraopmdKfAXfA6ZgAuyYr2Ymb9Qs1ck86yyJYncG"
        };
        String base58Data1 = "A";
        testInstruction("CloseAccount Case 1", base58Data1, accounts1);
    }

    private static void testFreezeAccount() {
        System.out.println("\n=== Testing FreezeAccount ===");

        // Case 1: tx_hash: 4bqEMhPCx1Yj6EFhxu4e2qAGxEJvY9w9qLrtTyEZfYKvuXXHGPFqmqrNZJr6xGfXhKTZN4TrfgVVrMqkDd7wHFBm
        String[] accounts1 = new String[]{
                "DjYyqKdwYcdXNMwzaykMwr6hvRJEHGmTQQnEBvqKvUYZ",
                "4QJwbmXp9L6NoAnyPwhat9yyGKJCTMKfH3HGEkhHGkZF",
                "TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA"
        };
        String base58Data1 = "10";
        testInstruction("FreezeAccount Case 1", base58Data1, accounts1);
    }

    private static void testThawAccount() {
        System.out.println("\n=== Testing ThawAccount ===");

        // Case 1: tx_hash: 57TG3QU6Xq1eQhYQfKU48k1wqUWsHzsDyUBgWiTqZhXgkVdBJZwXqHiZQykiqYrXBhbwPLxNwZKKZVxGvNhRyPry
        String[] accounts1 = new String[]{
                "DjYyqKdwYcdXNMwzaykMwr6hvRJEHGmTQQnEBvqKvUYZ",
                "4QJwbmXp9L6NoAnyPwhat9yyGKJCTMKfH3HGEkhHGkZF",
                "TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA"
        };
        String base58Data1 = "11";
        testInstruction("ThawAccount Case 1", base58Data1, accounts1);
    }

    private static void testTransferChecked() {
        System.out.println("\n=== Testing TransferChecked ===");

        // Case 1: tx_hash: 2ZGLGHrxZKZqNMUoWZh6rZmzxEQ4wDwNqRh8TFdJ6s9ToBvEmz7xHFWYWPHG3QVVxwH1zUvLo4JsBxo4x4JqCGPK
        String[] accounts1 = new String[]{
                "DjYyqKdwYcdXNMwzaykMwr6hvRJEHGmTQQnEBvqKvUYZ",
                "4QJwbmXp9L6NoAnyPwhat9yyGKJCTMKfH3HGEkhHGkZF",
                "HWHvQhFmJB6gPtqJx3gjxHX1iDZhQ9WJorxwb3iTWVHi",
                "TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA"
        };
        String base58Data1 = "12Bxs4cgF7g81qBE709"; // amount = 1000000000, decimals = 9
        testInstruction("TransferChecked Case 1", base58Data1, accounts1);
    }

    private static void testApproveChecked() {
        System.out.println("\n=== Testing ApproveChecked ===");

        // Case 1: tx_hash: 3vDU6JZRwDVVVo3KwGfA3T1YzgzJrDwHGolomBkGxqKJYYDxnPps8Z64eV3HQ4QKVxDyf4yVz2X6Q5VUh4Tdj6Cy
        String[] accounts1 = new String[]{
                "DjYyqKdwYcdXNMwzaykMwr6hvRJEHGmTQQnEBvqKvUYZ",
                "4QJwbmXp9L6NoAnyPwhat9yyGKJCTMKfH3HGEkhHGkZF",
                "HWHvQhFmJB6gPtqJx3gjxHX1iDZhQ9WJorxwb3iTWVHi",
                "TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA"
        };
        String base58Data1 = "13Bxs4cgF7g81qBE709"; // amount = 1000000000, decimals = 9
        testInstruction("ApproveChecked Case 1", base58Data1, accounts1);
    }
    private static void testMintToChecked() {
        System.out.println("\n=== Testing MintToChecked ===");

        // Case 1: tx_hash: 2EARWf5jKMxRpvnqxRBKgGGVXVoXY4qxkJyYCbPwcZbV2bjZhcvJwPkL4LgKtBsHGxSQB7FcdZqR7menjXTyKsXt
        String[] accounts1 = new String[]{
                "DjYyqKdwYcdXNMwzaykMwr6hvRJEHGmTQQnEBvqKvUYZ",
                "4QJwbmXp9L6NoAnyPwhat9yyGKJCTMKfH3HGEkhHGkZF",
                "TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA"
        };
        String base58Data1 = "14Bxs4cgF7g81qBE709"; // amount = 1000000000, decimals = 9
        testInstruction("MintToChecked Case 1", base58Data1, accounts1);
    }

    private static void testBurnChecked() {
        System.out.println("\n=== Testing BurnChecked ===");

        // Case 1: tx_hash: 5fqXJZpyvkE8YvSHbR3VgZEpWxzVaFGV4TakFyCGgBhzHxVr6yzZRqtR7xo6VrBHVmuEwGPjVoXcGxp3A9S4iFhk
        String[] accounts1 = new String[]{
                "DjYyqKdwYcdXNMwzaykMwr6hvRJEHGmTQQnEBvqKvUYZ",
                "4QJwbmXp9L6NoAnyPwhat9yyGKJCTMKfH3HGEkhHGkZF",
                "TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA"
        };
        String base58Data1 = "15Bxs4cgF7g81qBE709"; // amount = 1000000000, decimals = 9
        testInstruction("BurnChecked Case 1", base58Data1, accounts1);
    }

    private static void testInitializeMultisig() {
        System.out.println("\n=== Testing InitializeMultisig ===");

        // Case 1: tx_hash: 4bqEMhPCx1Yj6EFhxu4e2qAGxEJvY9w9qLrtTyEZfYKvuXXHGPFqmqrNZJr6xGfXhKTZN4TrfgVVrMqkDd7wHFBm
        String[] accounts1 = new String[]{
                "DjYyqKdwYcdXNMwzaykMwr6hvRJEHGmTQQnEBvqKvUYZ",
                "SysvarRent111111111111111111111111111111111",
                "4QJwbmXp9L6NoAnyPwhat9yyGKJCTMKfH3HGEkhHGkZF",
                "HWHvQhFmJB6gPtqJx3gjxHX1iDZhQ9WJorxwb3iTWVHi",
                "2xNweLHLqrbx4zo1waDvgWJHgsUpPj8Y8icbAFeR4a8i"
        };
        String base58Data1 = "203"; // m = 3 (需要3个签名)
        testInstruction("InitializeMultisig Case 1", base58Data1, accounts1);
    }


    private static void testInitializeAccount2() {
        System.out.println("\n=== Testing InitializeAccount2 ===");

        String[] accounts = new String[]{
                "TokenAccount111111111111111111111111111111111",  // account
                "TokenMint11111111111111111111111111111111111",  // mint
                "Owner111111111111111111111111111111111111111"   // owner
        };
        String base58Data = "10";  // instruction type = 16
        testInstruction("InitializeAccount2", base58Data, accounts);
    }

    private static void testSyncNative() {
        System.out.println("\n=== Testing SyncNative ===");

        String[] accounts = new String[]{
                "TokenAccount111111111111111111111111111111111"  // account
        };
        String base58Data = "11";  // instruction type = 17
        testInstruction("SyncNative", base58Data, accounts);
    }

    private static void testInitializeAccount3() {
        System.out.println("\n=== Testing InitializeAccount3 ===");

        String[] accounts = new String[]{
                "TokenAccount111111111111111111111111111111111",  // account
                "TokenMint11111111111111111111111111111111111",  // mint
                "Owner111111111111111111111111111111111111111"   // owner
        };
        String base58Data = "12";  // instruction type = 18
        testInstruction("InitializeAccount3", base58Data, accounts);
    }

    private static void testInitializeMultisig2() {
        System.out.println("\n=== Testing InitializeMultisig2 ===");

        String[] accounts = new String[]{
                "Multisig11111111111111111111111111111111111",  // multisig
                "Signer111111111111111111111111111111111111",   // signer 1
                "Signer222222222222222222222222222222222222",   // signer 2
                "Signer333333333333333333333333333333333333"    // signer 3
        };
        // instruction type = 19, m = 2
        String base58Data = "1302";
        testInstruction("InitializeMultisig2", base58Data, accounts);
    }

    private static void testInitializeMint2() {
        System.out.println("\n=== Testing InitializeMint2 ===");

        String[] accounts = new String[]{
                "TokenMint11111111111111111111111111111111111",  // mint
                "Authority11111111111111111111111111111111111",  // mint authority
                "Freeze11111111111111111111111111111111111111"   // freeze authority
        };
        // instruction type = 20, decimals = 9
        String base58Data = "1409";
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

        String[] accounts = new String[]{
                "DjYyqKdwYcdXNMwzaykMwr6hvRJEHGmTQQnEBvqKvUYZ"  // account - 要设置为不可变所有者的账户
        };

        // 构造数据：只需要指令类型
        ByteBuffer buffer = ByteBuffer.allocate(1);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.put((byte)22);    // instruction type

        String base58Data = Base58.encode(buffer.array());
        testInstruction("InitializeImmutableOwner", base58Data, accounts);
    }

    private static void testInitializeMintCloseAuthority() {
        System.out.println("\n--- Testing InitializeMintCloseAuthority (Case 25) ---");

        String[] accounts = new String[]{
                "DjYyqKdwYcdXNMwzaykMwr6hvRJEHGmTQQnEBvqKvUYZ",  // mint - 代币铸造账户
                "4QJwbmXp9L6NoAnyPwhat9yyGKJCTMKfH3HGEkhHGkZF"   // closeAuthority - 关闭权限账户
        };

        // 构造数据：
        // 1. 指令类型 (25)
        // 2. 是否设置关闭权限 (1 = 设置, 0 = 移除)
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.put((byte)25);    // instruction type
        buffer.put((byte)1);     // 设置关闭权限

        String base58Data = Base58.encode(buffer.array());
        testInstruction("InitializeMintCloseAuthority", base58Data, accounts);

        // 测试移除关闭权限的情况
        System.out.println("\n--- Testing InitializeMintCloseAuthority (Remove Authority) ---");
        buffer = ByteBuffer.allocate(2);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.put((byte)25);    // instruction type
        buffer.put((byte)0);     // 移除关闭权限

        String[] removeAccounts = new String[]{
                "DjYyqKdwYcdXNMwzaykMwr6hvRJEHGmTQQnEBvqKvUYZ"  // mint - 代币铸造账户
        };

        base58Data = Base58.encode(buffer.array());
        testInstruction("InitializeMintCloseAuthority (Remove)", base58Data, removeAccounts);

    }
    private static void testCreateNativeMint() {
        System.out.println("\n--- Testing CreateNativeMint (Case 31) ---");

        String[] accounts = new String[]{
                "DjYyqKdwYcdXNMwzaykMwr6hvRJEHGmTQQnEBvqKvUYZ",  // payer
                "So11111111111111111111111111111111111111112",   // native mint
                "11111111111111111111111111111111"               // system program
        };

        ByteBuffer buffer = ByteBuffer.allocate(1);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.put((byte)31);    // instruction type

        String base58Data = Base58.encode(buffer.array());
        testInstruction("CreateNativeMint", base58Data, accounts);
    }

    private static void testGetAccountDataSize() {
        System.out.println("\n--- Testing GetAccountDataSize (Case 21) ---");

        String[] accounts = new String[]{
                "TokenAccount111111111111111111111111111111111"  // account
        };

        // 构造数据：extensionTypes = 7 (TransferFee | ConfidentialTransfer | MemoTransfer)
        ByteBuffer buffer = ByteBuffer.allocate(9);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.put((byte)21);    // instruction type
        buffer.putLong(7L);      // extensionTypes bitmap

        String base58Data = Base58.encode(buffer.array());
        testInstruction("GetAccountDataSize", base58Data, accounts);
    }

    private static void testReallocate() {
        System.out.println("\n--- Testing Reallocate (Case 29) ---");

        String[] accounts = new String[]{
                "DjYyqKdwYcdXNMwzaykMwr6hvRJEHGmTQQnEBvqKvUYZ",  // account
                "4QJwbmXp9L6NoAnyPwhat9yyGKJCTMKfH3HGEkhHGkZF",  // payer
                "11111111111111111111111111111111"               // system program
        };

        // 构造数据：extensionTypes = 3 (TransferFee | ConfidentialTransfer)
        ByteBuffer buffer = ByteBuffer.allocate(9);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.put((byte)29);    // instruction type
        buffer.putLong(3L);      // extensionTypes bitmap

        String base58Data = Base58.encode(buffer.array());
        testInstruction("Reallocate", base58Data, accounts);
    }

    private static void testAmountToUiAmount() {
        System.out.println("\n--- Testing AmountToUiAmount (Case 23) ---");

        String[] accounts = new String[]{
                "DjYyqKdwYcdXNMwzaykMwr6hvRJEHGmTQQnEBvqKvUYZ"  // mint
        };

        // 构造数据：amount = 1000000000 (1 SOL)
        ByteBuffer buffer = ByteBuffer.allocate(9);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.put((byte)23);         // instruction type
        buffer.putLong(1000000000L);  // amount

        String base58Data = Base58.encode(buffer.array());
        testInstruction("AmountToUiAmount", base58Data, accounts);
    }

    private static void testUiAmountToAmount() {
        System.out.println("\n--- Testing UiAmountToAmount (Case 24) ---");

        String[] accounts = new String[]{
                "DjYyqKdwYcdXNMwzaykMwr6hvRJEHGmTQQnEBvqKvUYZ"  // mint
        };

        // 构造数据：ui_amount = 1.0
        ByteBuffer buffer = ByteBuffer.allocate(9);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.put((byte)24);     // instruction type
        buffer.putDouble(1.0);    // ui_amount

        String base58Data = Base58.encode(buffer.array());
        testInstruction("UiAmountToAmount", base58Data, accounts);
    }

    private static void testTransferFeeExtension() {
        System.out.println("\n--- Testing TransferFeeExtension (Case 26) ---");

        String[] accounts = new String[]{
                "DjYyqKdwYcdXNMwzaykMwr6hvRJEHGmTQQnEBvqKvUYZ",  // mint
                "4QJwbmXp9L6NoAnyPwhat9yyGKJCTMKfH3HGEkhHGkZF"   // authority
        };

        // 构造数据：transferFeeBasisPoints = 100 (1%), maximumFee = 1000000
        ByteBuffer buffer = ByteBuffer.allocate(13);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.put((byte)26);     // instruction type
        buffer.putInt(100);       // transferFeeBasisPoints
        buffer.putLong(1000000L); // maximumFee

        String base58Data = Base58.encode(buffer.array());
        testInstruction("TransferFeeExtension", base58Data, accounts);
    }

    private static void testConfidentialTransferExtension() {
        System.out.println("\n--- Testing ConfidentialTransferExtension (Case 27) ---");

        String[] accounts = new String[]{
                "DjYyqKdwYcdXNMwzaykMwr6hvRJEHGmTQQnEBvqKvUYZ",  // mint
                "4QJwbmXp9L6NoAnyPwhat9yyGKJCTMKfH3HGEkhHGkZF"   // authority
        };

        // 构造数据：confidentialTransferMint配置
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.put((byte)27);    // instruction type
        buffer.put((byte)1);     // 启用机密转账

        String base58Data = Base58.encode(buffer.array());
        testInstruction("ConfidentialTransferExtension", base58Data, accounts);
    }

    private static void testDefaultAccountStateExtension() {
        System.out.println("\n--- Testing DefaultAccountStateExtension (Case 28) ---");

        String[] accounts = new String[]{
                "DjYyqKdwYcdXNMwzaykMwr6hvRJEHGmTQQnEBvqKvUYZ",  // mint
                "4QJwbmXp9L6NoAnyPwhat9yyGKJCTMKfH3HGEkhHGkZF"   // authority
        };

        // 构造数据：accountState = 1 (frozen)
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.put((byte)28);    // instruction type
        buffer.put((byte)1);     // accountState

        String base58Data = Base58.encode(buffer.array());
        testInstruction("DefaultAccountStateExtension", base58Data, accounts);
    }

    private static void testMemoTransferExtension() {
        System.out.println("\n--- Testing MemoTransferExtension (Case 30) ---");

        String[] accounts = new String[]{
                "DjYyqKdwYcdXNMwzaykMwr6hvRJEHGmTQQnEBvqKvUYZ",  // account
                "4QJwbmXp9L6NoAnyPwhat9yyGKJCTMKfH3HGEkhHGkZF"   // authority
        };

        // 构造数据：启用/禁用备注转账
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.put((byte)30);    // instruction type
        buffer.put((byte)1);     // enable memo transfers

        String base58Data = Base58.encode(buffer.array());
        testInstruction("MemoTransferExtension", base58Data, accounts);
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