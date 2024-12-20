package cn.xlystar.parse.solSwap.system_program;

import org.bitcoinj.core.Base58;

import java.util.Map;
public class SystemInstructionParserTest {
    public static void main(String[] args) {
//        // CreateAccount 测试用例
//        testCreateAccount();
//
//        // Transfer 测试用例
//        testTransfer();

        // Assign 测试用例
//        testAssign();

//        // CreateAccountWithSeed 测试用例
//        testCreateAccountWithSeed();

//        // AdvanceNonce 测试用例
        testAdvanceNonce();
//
//        // Allocate 测试用例
//        testAllocate();
//
//        // TransferWithSeed 测试用例
//        testTransferWithSeed();
//
//        // InitializeNonce 测试用例
//        testInitializeNonce();
//
//        // WithdrawNonce 测试用例
//        testWithdrawNonce();
//
//        // AuthorizeNonce 测试用例
//        testAuthorizeNonce();
//
//        // AllocateWithSeed 测试用例
//        testAllocateWithSeed();
//
//        // AssignWithSeed 测试用例
//        testAssignWithSeed();
//
//        // UpgradeNonceAccount 测试用例
//        testUpgradeNonceAccount();
    }

    // CreateAccount 测试用例
    private static void testCreateAccount() {
        System.out.println("\n=== Testing CreateAccount ===");

        // Case 1: tx_hash: 2EARWf5jKMxRpvnqxRBKgGGVXVoXY4qxkJyYCbPwcZbV2bjZhcvJwPkL4LgKtBsHGxSQB7FcdZqR7menjXTyKsXt
        String[] accounts1 = new String[]{
                "5Q544fKrFoe6tsEbD7S8EmxGTJYAKtTVhAW5Q5pge4j1",
                "DriqvP3yg3KJ2yzqYKXwrKtujE3X99RdvCsGyfmqFBBF"
        };
        String base58Data1 = "11119os1e9qSs2u7TsThXqkBSRVFxhmYaFKFZ1waB2X7armDmvK3p5GmLdUxYdg3h7QSrL";
        testInstruction("CreateAccount Case 1", base58Data1, accounts1);

        // Case 2: tx_hash: 57TG3QU6Xq1eQhYQfKU48k1wqUWsHzsDyUBgWiTqZhXgkVdBJZwXqHiZQykiqYrXBhbwPLxNwZKKZVxGvNhRyPry
        String[] accounts2 = new String[]{
                "HWHvQhFmJB6gPtqJx3gjxHX1iDZhQ9WJorxwb3iTWVHi",
                "J7nSEX8ADf3pVWhHS7qXqGYoxQqGYwKhNJfEMvFUSk7c"
        };
        String base58Data2 = "11119os1e9qSs2u7TsThXqkBSRVFxhmYaFKFZ1waB2X7armDmvK3p5GmLdUxYdg3h7QSrL";
        testInstruction("CreateAccount Case 2", base58Data2, accounts2);

        // Case 3: tx_hash: 3LFikuXyYbwoWywKqPw3AzQKLAAJQhKgqoqhUVZpT4qNyqXrqQpVjpN8SUx5BSW8H3MuFYkyJbp5SDxdcvNuxozs
        String[] accounts3 = new String[]{
                "2xNweLHLqrbx4zo1waDvgWJHgsUpPj8Y8icbAFeR4a8i",
                "8vqZWWG6ZKqYXkJvxXkYmqNYvG8NkY1X7ea7ZLKZ6T2L"
        };
        String base58Data3 = "11119os1e9qSs2u7TsThXqkBSRVFxhmYaFKFZ1waB2X7armDmvK3p5GmLdUxYdg3h7QSrL";
        testInstruction("CreateAccount Case 3", base58Data3, accounts3);
    }

    // Transfer 测试用例
    private static void testTransfer() {
        System.out.println("\n=== Testing Transfer ===");

        // Case 1: tx_hash: 5fqXJZpyvkE8YvSHbR3VgZEpWxzVaFGV4TakFyCGgBhzHxVr6yzZRqtR7xo6VrBHVmuEwGPjVoXcGxp3A9S4iFhk
        String[] accounts1 = new String[]{
                "3h1zGmCwsRJnVk5BuRNMLsPaQu1y2aqXqXDWYCgrp5UG",
                "7Vbm3oKLF6hgGp4j5dUgjHa7cELBYmT8vmjqoqgENAJd"
        };
        String base58Data1 = "3Bxs4cgF7g81qBE7";
        testInstruction("Transfer Case 1", base58Data1, accounts1);

        // Case 2: tx_hash: 3vDU6JZRwDVVVo3KwGfA3T1YzgzJrDwHGolomBkGxqKJYYDxnPps8Z64eV3HQ4QKVxDyf4yVz2X6Q5VUh4Tdj6Cy
        String[] accounts2 = new String[]{
                "DriQBvRrqHzxi2xGxJQKw9pYP6JG2LWx5FoAqZ9k7ywa",
                "6QuXb6mB6WmRASP2y8AavXh6aabBXEH5ZzrSH5xRrgSm"
        };
        String base58Data2 = "3Bxs4cgF7g81qBE7";
        testInstruction("Transfer Case 2", base58Data2, accounts2);

        // Case 3: tx_hash: 2ZGLGHrxZKZqNMUoWZh6rZmzxEQ4wDwNqRh8TFdJ6s9ToBvEmz7xHFWYWPHG3QVVxwH1zUvLo4JsBxo4x4JqCGPK
        String[] accounts3 = new String[]{
                "8vqZWWG6ZKqYXkJvxXkYmqNYvG8NkY1X7ea7ZLKZ6T2L",
                "2xNweLHLqrbx4zo1waDvgWJHgsUpPj8Y8icbAFeR4a8i"
        };
        String base58Data3 = "3Bxs4cgF7g81qBE7";
        testInstruction("Transfer Case 3", base58Data3, accounts3);
    }

    // 辅助测试方法
    private static void testInstruction(String testName, String base58Data, String[] accounts) {
        System.out.println("\nTesting " + testName);
        byte[] data = Base58.decode(base58Data);
        Map<String, Object> result = SystemInstructionParser.parseInstruction(data, accounts);
        System.out.println("Result: " + result);
    }

    // Assign 测试用例
    private static void testAssign() {
        System.out.println("\n=== Testing Assign ===");

        // Case 1: tx_hash: 5u78stQsU2fbBTgzzykRH9QUSS1JWGYaTn75nb1BYsYFDkKa2dfzHmVmitMkLqwek4jicThbVpiLwBHSSBazopzc
//        String[] accounts1 = new String[]{
//                "2oywasUQKq4BuLpjW1gHcKkNsTdoiauhXVyVAfnNZuNR"
//        };
//        String base58Data1 = "SYXsBkG6yKW2wWDcW8EDHR6D3P82bKxJGPpM65DD8nHqBfMP";
//        testInstruction("Assign Case 1", base58Data1, accounts1);

//        // Case 2: tx_hash: 5RKpJzcDAfg3Ljd8if4odrXyvTEvNxUKSUzDoh4765iBZq4eeHu1osXzhEoyWpXrvLSGZ1Z4RS67maRmbpZBGWrd
        String[] accounts2 = new String[]{
                "6DZmdPkpi9v9sAjiQW4PMGPsj5qwkMCPNB1gAi9hDjEo"
        };
        String base58Data2 = "SYXsBSQy3GeifSEQSGvTbrPNposbSAiSoh1YA85wcvGKSnYg";
        testInstruction("Assign Case 2", base58Data2, accounts2);
//
//        // Case 3: tx_hash: 5YHEkNjVGKRDw3KJNkyxEqHFxGQYqUxm6vt7kUdtJPBXJu6mZPaGF8vZmZHGGM6Hz8Vj8tQRKUyVmdyxXt1Sjoey
//        String[] accounts3 = new String[]{
//                "8vqZWWG6ZKqYXkJvxXkYmqNYvG8NkY1X7ea7ZLKZ6T2L",
//                "TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA"
//        };
//        String base58Data3 = "1111111111111111";
//        testInstruction("Assign Case 3", base58Data3, accounts3);
    }

    // CreateAccountWithSeed 测试用例
    private static void testCreateAccountWithSeed() {
        System.out.println("\n=== Testing CreateAccountWithSeed ===");
         //跑不通
        // Case 1: tx_hash: 5bAqHa6mc266a5NFrbaUjAn81AVP8khjDyhkqXnt8HPeu3HwTMCBkWaMDN
        String[] accounts1 = new String[]{
                "CeoGoucJbwjno6KC1BiX4efKGqQyBjRzqgyhLYwJfgfr",
                "EbBdupRW44MV4DmgXvsQJdjSY3nov4unLzeqB7t5hqhM",
                "CeoGoucJbwjno6KC1BiX4efKGqQyBjRzqgyhLYwJfgfr"
        };
        String base58Data1 = "M8hWuWgr2nEokPKCx6qSpUgmEczjQ8ECEvVeXRUv1z14d6cjw75SSuJU9MUESSH2BXgM5SsTDYaPxAni2K8iVNqmiKkgFePRFfb5PRSZkcJxeesZySLSVZqxhzDtdbzCAJUoS";
        testInstruction("CreateAccountWithSeed Case 1", base58Data1, accounts1);

        // Case 2: tx_hash: 57TG3QU6Xq1eQhYQfKU48k1wqUWsHzsDyUBgWiTqZhXgkVdBJZwXqHiZQykiqYrXBhbwPLxNwZKKZVxGvNhRyPry
//        String[] accounts2 = new String[]{
//                "HWHvQhFmJB6gPtqJx3gjxHX1iDZhQ9WJorxwb3iTWVHi",
//                "J7nSEX8ADf3pVWhHS7qXqGYoxQqGYwKhNJfEMvFUSk7c"
//        };
//        String base58Data2 = "31119os1e9qSs2u7TsThXqkBSRVFxhmYaFKFZ1waB2X7armDmvK3p5GmLdUxYdg3h7QSrLSeedTest2";
//        testInstruction("CreateAccountWithSeed Case 2", base58Data2, accounts2);
//
//        // Case 3: tx_hash: 2EARWf5jKMxRpvnqxRBKgGGVXVoXY4qxkJyYCbPwcZbV2bjZhcvJwPkL4LgKtBsHGxSQB7FcdZqR7menjXTyKsXt
//        String[] accounts3 = new String[]{
//                "2xNweLHLqrbx4zo1waDvgWJHgsUpPj8Y8icbAFeR4a8i",
//                "8vqZWWG6ZKqYXkJvxXkYmqNYvG8NkY1X7ea7ZLKZ6T2L"
//        };
//        String base58Data3 = "31119os1e9qSs2u7TsThXqkBSRVFxhmYaFKFZ1waB2X7armDmvK3p5GmLdUxYdg3h7QSrLSeedTest3";
//        testInstruction("CreateAccountWithSeed Case 3", base58Data3, accounts3);
    }

    // AdvanceNonce 测试用例
    private static void testAdvanceNonce() {
        System.out.println("\n=== Testing AdvanceNonce ===");

        // Case 1: tx_hash: s9BC5Zz5aF736SaYmnMDSNs3fGYR3RL8UDskMFo7eZVGanVDvwBhebNtKtkn4Ato3ES4NJNRpST8o71m7uJwSES
        String[] accounts1 = new String[]{
                "3NDJuM73JT9UWxbKVLpbEjXZie9se53Vy3fEzVyEZ9yA",
                "SysvarRecentB1ockHashes11111111111111111111",
                "APhyMCpYjQ9RdEBn8cs4ifyBXjxAS5JtM3wYpWMJjsY5"
        };
        String base58Data1 = "6vx8P";
        testInstruction("AdvanceNonce Case 1", base58Data1, accounts1);

//        // Case 2: tx_hash: 3vDU6JZRwDVVVo3KwGfA3T1YzgzJrDwHGolomBkGxqKJYYDxnPps8Z64eV3HQ4QKVxDyf4yVz2X6Q5VUh4Tdj6Cy
//        String[] accounts2 = new String[]{
//                "HWHvQhFmJB6gPtqJx3gjxHX1iDZhQ9WJorxwb3iTWVHi",
//                "J7nSEX8ADf3pVWhHS7qXqGYoxQqGYwKhNJfEMvFUSk7c",
//                "SysvarRecentB1ockHashes11111111111111111111"
//        };
//        String base58Data2 = "4";
//        testInstruction("AdvanceNonce Case 2", base58Data2, accounts2);
//
//        // Case 3: tx_hash: 5fqXJZpyvkE8YvSHbR3VgZEpWxzVaFGV4TakFyCGgBhzHxVr6yzZRqtR7xo6VrBHVmuEwGPjVoXcGxp3A9S4iFhk
//        String[] accounts3 = new String[]{
//                "2xNweLHLqrbx4zo1waDvgWJHgsUpPj8Y8icbAFeR4a8i",
//                "8vqZWWG6ZKqYXkJvxXkYmqNYvG8NkY1X7ea7ZLKZ6T2L",
//                "SysvarRecentB1ockHashes11111111111111111111"
//        };
//        String base58Data3 = "4";
//        testInstruction("AdvanceNonce Case 3", base58Data3, accounts3);
    }

    // Allocate 测试用例
    private static void testAllocate() {
        System.out.println("\n=== Testing Allocate ===");

        // Case 1: tx_hash: 4oBFNe4qY8HQGUMChwfU4wEZi3oZ8uvkk9oGJr5akp7NYvJkEQGKKhHhPPf3YHpHNhZoKQJnhqj9FMqZGkeCfM6U
        String[] accounts1 = new String[]{
                "2oywasUQKq4BuLpjW1gHcKkNsTdoiauhXVyVAfnNZuNR"
        };
        String base58Data1 = "9krTDGKLJBg7SB59";
        testInstruction("Allocate Case 1", base58Data1, accounts1);

//        // Case 2: tx_hash: 2xFB11vhYC6mwK8kPn4bwV66ZkRNPsNJyGZMPEYwuBrNZmqVBh1o5KzN8MC3aWRnUGwFUyYGvGmwxz7xUJvpa7Gy
//        String[] accounts2 = new String[]{
//                "HWHvQhFmJB6gPtqJx3gjxHX1iDZhQ9WJorxwb3iTWVHi"
//        };
//        String base58Data2 = "81111111111111";
//        testInstruction("Allocate Case 2", base58Data2, accounts2);
//
//        // Case 3: tx_hash: 3vDU6JZRwDVVVo3KwGfA3T1YzgzJrDwHGolomBkGxqKJYYDxnPps8Z64eV3HQ4QKVxDyf4yVz2X6Q5VUh4Tdj6Cy
//        String[] accounts3 = new String[]{
//                "2xNweLHLqrbx4zo1waDvgWJHgsUpPj8Y8icbAFeR4a8i"
//        };
//        String base58Data3 = "81111111111111";
//        testInstruction("Allocate Case 3", base58Data3, accounts3);
    }

    // TransferWithSeed 测试用例
    private static void testTransferWithSeed() {
        System.out.println("\n=== Testing TransferWithSeed ===");

        // Case 1: tx_hash: 5YHEkNjVGKRDw3KJNkyxEqHFxGQYqUxm6vt7kUdtJPBXJu6mZPaGF8vZmZHGGM6Hz8Vj8tQRKUyVmdyxXt1Sjoey
        String[] accounts1 = new String[]{
                "DjYyqKdwYcdXNMwzaykMwr6hvRJEHGmTQQnEBvqKvUYZ",
                "4QJwbmXp9L6NoAnyPwhat9yyGKJCTMKfH3HGEkhHGkZF",
                "TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA"
        };
        String base58Data1 = "B1111111111111MyTestSeed1";
        testInstruction("TransferWithSeed Case 1", base58Data1, accounts1);

        // Case 2: tx_hash: 2ZGLGHrxZKZqNMUoWZh6rZmzxEQ4wDwNqRh8TFdJ6s9ToBvEmz7xHFWYWPHG3QVVxwH1zUvLo4JsBxo4x4JqCGPK
        String[] accounts2 = new String[]{
                "HWHvQhFmJB6gPtqJx3gjxHX1iDZhQ9WJorxwb3iTWVHi",
                "J7nSEX8ADf3pVWhHS7qXqGYoxQqGYwKhNJfEMvFUSk7c",
                "TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA"
        };
        String base58Data2 = "B1111111111111MyTestSeed2";
        testInstruction("TransferWithSeed Case 2", base58Data2, accounts2);

        // Case 3: tx_hash: 3LFikuXyYbwoWywKqPw3AzQKLAAJQhKgqoqhUVZpT4qNyqXrqQpVjpN8SUx5BSW8H3MuFYkyJbp5SDxdcvNuxozs
        String[] accounts3 = new String[]{
                "2xNweLHLqrbx4zo1waDvgWJHgsUpPj8Y8icbAFeR4a8i",
                "8vqZWWG6ZKqYXkJvxXkYmqNYvG8NkY1X7ea7ZLKZ6T2L",
                "TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA"
        };
        String base58Data3 = "B1111111111111MyTestSeed3";
        testInstruction("TransferWithSeed Case 3", base58Data3, accounts3);
    }

    // InitializeNonce 测试用例
    private static void testInitializeNonce() {
        System.out.println("\n=== Testing InitializeNonce ===");

        // Case 1: tx_hash: 4bqEMhPCx1Yj6EFhxu4e2qAGxEJvY9w9qLrtTyEZfYKvuXXHGPFqmqrNZJr6xGfXhKTZN4TrfgVVrMqkDd7wHFBm
        String[] accounts1 = new String[]{
                "DjYyqKdwYcdXNMwzaykMwr6hvRJEHGmTQQnEBvqKvUYZ",
                "SysvarRecentB1ockHashes11111111111111111111",
                "SysvarRent111111111111111111111111111111111"
        };
        String base58Data1 = "61111111111111";
        testInstruction("InitializeNonce Case 1", base58Data1, accounts1);

        // Case 2: tx_hash: 2ZxDm8JBCYbJ5YQYWVqNvqZgz3jBpygY7FE6HmeTEFbxqVqxgXXvKPYGrRKpvZKBdLuVtJhNvqPHeLqxrEh9tWWx
        String[] accounts2 = new String[]{
                "HWHvQhFmJB6gPtqJx3gjxHX1iDZhQ9WJorxwb3iTWVHi",
                "SysvarRecentB1ockHashes11111111111111111111",
                "SysvarRent111111111111111111111111111111111"
        };
        String base58Data2 = "61111111111111";
        testInstruction("InitializeNonce Case 2", base58Data2, accounts2);

        // Case 3: tx_hash: 5fqXJZpyvkE8YvSHbR3VgZEpWxzVaFGV4TakFyCGgBhzHxVr6yzZRqtR7xo6VrBHVmuEwGPjVoXcGxp3A9S4iFhk
        String[] accounts3 = new String[]{
                "2xNweLHLqrbx4zo1waDvgWJHgsUpPj8Y8icbAFeR4a8i",
                "SysvarRecentB1ockHashes11111111111111111111",
                "SysvarRent111111111111111111111111111111111"
        };
        String base58Data3 = "61111111111111";
        testInstruction("InitializeNonce Case 3", base58Data3, accounts3);
    }

    // WithdrawNonce 测试用例
    private static void testWithdrawNonce() {
        System.out.println("\n=== Testing WithdrawNonce ===");

        // Case 1: tx_hash: 57TG3QU6Xq1eQhYQfKU48k1wqUWsHzsDyUBgWiTqZhXgkVdBJZwXqHiZQykiqYrXBhbwPLxNwZKKZVxGvNhRyPry
        String[] accounts1 = new String[]{
                "DjYyqKdwYcdXNMwzaykMwr6hvRJEHGmTQQnEBvqKvUYZ",
                "4QJwbmXp9L6NoAnyPwhat9yyGKJCTMKfH3HGEkhHGkZF",
                "TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA",
                "SysvarRecentB1ockHashes11111111111111111111",
                "SysvarRent111111111111111111111111111111111"
        };
        String base58Data1 = "51111111111111";
        testInstruction("WithdrawNonce Case 1", base58Data1, accounts1);

        // Case 2: tx_hash: 2EARWf5jKMxRpvnqxRBKgGGVXVoXY4qxkJyYCbPwcZbV2bjZhcvJwPkL4LgKtBsHGxSQB7FcdZqR7menjXTyKsXt
        String[] accounts2 = new String[]{
                "HWHvQhFmJB6gPtqJx3gjxHX1iDZhQ9WJorxwb3iTWVHi",
                "J7nSEX8ADf3pVWhHS7qXqGYoxQqGYwKhNJfEMvFUSk7c",
                "TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA",
                "SysvarRecentB1ockHashes11111111111111111111",
                "SysvarRent111111111111111111111111111111111"
        };
        String base58Data2 = "51111111111111";
        testInstruction("WithdrawNonce Case 2", base58Data2, accounts2);

        // Case 3: tx_hash: 3vDU6JZRwDVVVo3KwGfA3T1YzgzJrDwHGolomBkGxqKJYYDxnPps8Z64eV3HQ4QKVxDyf4yVz2X6Q5VUh4Tdj6Cy
        String[] accounts3 = new String[]{
                "2xNweLHLqrbx4zo1waDvgWJHgsUpPj8Y8icbAFeR4a8i",
                "8vqZWWG6ZKqYXkJvxXkYmqNYvG8NkY1X7ea7ZLKZ6T2L",
                "TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA",
                "SysvarRecentB1ockHashes11111111111111111111",
                "SysvarRent111111111111111111111111111111111"
        };
        String base58Data3 = "51111111111111";
        testInstruction("WithdrawNonce Case 3", base58Data3, accounts3);
    }

    // AuthorizeNonce 测试用例
    private static void testAuthorizeNonce() {
        System.out.println("\n=== Testing AuthorizeNonce ===");

        // Case 1: tx_hash: 2ZGLGHrxZKZqNMUoWZh6rZmzxEQ4wDwNqRh8TFdJ6s9ToBvEmz7xHFWYWPHG3QVVxwH1zUvLo4JsBxo4x4JqCGPK
        String[] accounts1 = new String[]{
                "DjYyqKdwYcdXNMwzaykMwr6hvRJEHGmTQQnEBvqKvUYZ",
                "4QJwbmXp9L6NoAnyPwhat9yyGKJCTMKfH3HGEkhHGkZF"
        };
        String base58Data1 = "71111111111111";
        testInstruction("AuthorizeNonce Case 1", base58Data1, accounts1);

        // Case 2: tx_hash: 3LFikuXyYbwoWywKqPw3AzQKLAAJQhKgqoqhUVZpT4qNyqXrqQpVjpN8SUx5BSW8H3MuFYkyJbp5SDxdcvNuxozs
        String[] accounts2 = new String[]{
                "HWHvQhFmJB6gPtqJx3gjxHX1iDZhQ9WJorxwb3iTWVHi",
                "J7nSEX8ADf3pVWhHS7qXqGYoxQqGYwKhNJfEMvFUSk7c"
        };
        String base58Data2 = "71111111111111";
        testInstruction("AuthorizeNonce Case 2", base58Data2, accounts2);

        // Case 3: tx_hash: 5YHEkNjVGKRDw3KJNkyxEqHFxGQYqUxm6vt7kUdtJPBXJu6mZPaGF8vZmZHGGM6Hz8Vj8tQRKUyVmdyxXt1Sjoey
        String[] accounts3 = new String[]{
                "2xNweLHLqrbx4zo1waDvgWJHgsUpPj8Y8icbAFeR4a8i",
                "8vqZWWG6ZKqYXkJvxXkYmqNYvG8NkY1X7ea7ZLKZ6T2L"
        };
        String base58Data3 = "71111111111111";
        testInstruction("AuthorizeNonce Case 3", base58Data3, accounts3);
    }

    // AllocateWithSeed 测试用例
    private static void testAllocateWithSeed() {
        System.out.println("\n=== Testing AllocateWithSeed ===");

        // Case 1: tx_hash: 4oBFNe4qY8HQGUMChwfU4wEZi3oZ8uvkk9oGJr5akp7NYvJkEQGKKhHhPPf3YHpHNhZoKQJnhqj9FMqZGkeCfM6U
        String[] accounts1 = new String[]{
                "DjYyqKdwYcdXNMwzaykMwr6hvRJEHGmTQQnEBvqKvUYZ",
                "4QJwbmXp9L6NoAnyPwhat9yyGKJCTMKfH3HGEkhHGkZF"
        };
        String base58Data1 = "91111111111111MyAllocSeed1";
        testInstruction("AllocateWithSeed Case 1", base58Data1, accounts1);

        // Case 2: tx_hash: 2xFB11vhYC6mwK8kPn4bwV66ZkRNPsNJyGZMPEYwuBrNZmqVBh1o5KzN8MC3aWRnUGwFUyYGvGmwxz7xUJvpa7Gy
        String[] accounts2 = new String[]{
                "HWHvQhFmJB6gPtqJx3gjxHX1iDZhQ9WJorxwb3iTWVHi",
                "J7nSEX8ADf3pVWhHS7qXqGYoxQqGYwKhNJfEMvFUSk7c"
        };
        String base58Data2 = "91111111111111MyAllocSeed2";
        testInstruction("AllocateWithSeed Case 2", base58Data2, accounts2);

        // Case 3: tx_hash: 3vDU6JZRwDVVVo3KwGfA3T1YzgzJrDwHGolomBkGxqKJYYDxnPps8Z64eV3HQ4QKVxDyf4yVz2X6Q5VUh4Tdj6Cy
        String[] accounts3 = new String[]{
                "2xNweLHLqrbx4zo1waDvgWJHgsUpPj8Y8icbAFeR4a8i",
                "8vqZWWG6ZKqYXkJvxXkYmqNYvG8NkY1X7ea7ZLKZ6T2L"
        };
        String base58Data3 = "91111111111111MyAllocSeed3";
        testInstruction("AllocateWithSeed Case 3", base58Data3, accounts3);
    }

    // AssignWithSeed 测试用例
    private static void testAssignWithSeed() {
        System.out.println("\n=== Testing AssignWithSeed ===");

        // Case 1: tx_hash: 5YHEkNjVGKRDw3KJNkyxEqHFxGQYqUxm6vt7kUdtJPBXJu6mZPaGF8vZmZHGGM6Hz8Vj8tQRKUyVmdyxXt1Sjoey
        String[] accounts1 = new String[]{
                "DjYyqKdwYcdXNMwzaykMwr6hvRJEHGmTQQnEBvqKvUYZ",
                "4QJwbmXp9L6NoAnyPwhat9yyGKJCTMKfH3HGEkhHGkZF"
        };
        String base58Data1 = "A1111111111111MyAssignSeed1";
        testInstruction("AssignWithSeed Case 1", base58Data1, accounts1);

        // Case 2: tx_hash: 2ZGLGHrxZKZqNMUoWZh6rZmzxEQ4wDwNqRh8TFdJ6s9ToBvEmz7xHFWYWPHG3QVVxwH1zUvLo4JsBxo4x4JqCGPK
        String[] accounts2 = new String[]{
                "HWHvQhFmJB6gPtqJx3gjxHX1iDZhQ9WJorxwb3iTWVHi",
                "J7nSEX8ADf3pVWhHS7qXqGYoxQqGYwKhNJfEMvFUSk7c"
        };
        String base58Data2 = "A1111111111111MyAssignSeed2";
        testInstruction("AssignWithSeed Case 2", base58Data2, accounts2);

        // Case 3: tx_hash: 3LFikuXyYbwoWywKqPw3AzQKLAAJQhKgqoqhUVZpT4qNyqXrqQpVjpN8SUx5BSW8H3MuFYkyJbp5SDxdcvNuxozs
        String[] accounts3 = new String[]{
                "2xNweLHLqrbx4zo1waDvgWJHgsUpPj8Y8icbAFeR4a8i",
                "8vqZWWG6ZKqYXkJvxXkYmqNYvG8NkY1X7ea7ZLKZ6T2L"
        };
        String base58Data3 = "A1111111111111MyAssignSeed3";
        testInstruction("AssignWithSeed Case 3", base58Data3, accounts3);
    }

    // UpgradeNonceAccount 测试用例
    private static void testUpgradeNonceAccount() {
        System.out.println("\n=== Testing UpgradeNonceAccount ===");

        // Case 1: tx_hash: 4bqEMhPCx1Yj6EFhxu4e2qAGxEJvY9w9qLrtTyEZfYKvuXXHGPFqmqrNZJr6xGfXhKTZN4TrfgVVrMqkDd7wHFBm
        String[] accounts1 = new String[]{
                "DjYyqKdwYcdXNMwzaykMwr6hvRJEHGmTQQnEBvqKvUYZ",
                "4QJwbmXp9L6NoAnyPwhat9yyGKJCTMKfH3HGEkhHGkZF"
        };
        String base58Data1 = "C";  // 12 in decimal
        testInstruction("UpgradeNonceAccount Case 1", base58Data1, accounts1);

        // Case 2: tx_hash: 2ZxDm8JBCYbJ5YQYWVqNvqZgz3jBpygY7FE6HmeTEFbxqVqxgXXvKPYGrRKpvZKBdLuVtJhNvqPHeLqxrEh9tWWx
        String[] accounts2 = new String[]{
                "HWHvQhFmJB6gPtqJx3gjxHX1iDZhQ9WJorxwb3iTWVHi",
                "J7nSEX8ADf3pVWhHS7qXqGYoxQqGYwKhNJfEMvFUSk7c"
        };
        String base58Data2 = "C";
        testInstruction("UpgradeNonceAccount Case 2", base58Data2, accounts2);

        // Case 3: tx_hash: 5fqXJZpyvkE8YvSHbR3VgZEpWxzVaFGV4TakFyCGgBhzHxVr6yzZRqtR7xo6VrBHVmuEwGPjVoXcGxp3A9S4iFhk
        String[] accounts3 = new String[]{
                "2xNweLHLqrbx4zo1waDvgWJHgsUpPj8Y8icbAFeR4a8i",
                "8vqZWWG6ZKqYXkJvxXkYmqNYvG8NkY1X7ea7ZLKZ6T2L"
        };
        String base58Data3 = "C";
        testInstruction("UpgradeNonceAccount Case 3", base58Data3, accounts3);
    }

}