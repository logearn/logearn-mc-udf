package cn.xlystar.parse.solSwap.spl_token;

import org.bitcoinj.core.Base58;

import java.util.Map;

public class SplTokenInstructionParserTest {
    public static void main(String[] args) {
        testTransfer();
        testMintTo();
        testBurn();
        testInitializeMint();
        testInitializeAccount();
        testApprove();
        testRevoke();
        testSetAuthority();
        testCloseAccount();
        testFreezeAccount();
        testThawAccount();
        testTransferChecked();
        testApproveChecked();
        testMintToChecked();
        testBurnChecked();
        testInitializeMultisig();
    }

    private static void testTransfer() {
        System.out.println("\n=== Testing Transfer ===");

        // Case 1: tx_hash: 4oBFNe4qY8HQGUMChwfU4wEZi3oZ8uvkk9oGJr5akp7NYvJkEQGKKhHhPPf3YHpHNhZoKQJnhqj9FMqZGkeCfM6U
        String[] accounts1 = new String[]{
                "DjYyqKdwYcdXNMwzaykMwr6hvRJEHGmTQQnEBvqKvUYZ",
                "4QJwbmXp9L6NoAnyPwhat9yyGKJCTMKfH3HGEkhHGkZF",
                "TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA"
        };
        String base58Data1 = "3Bxs4cgF7g81qBE7";
        testInstruction("Transfer Case 1", base58Data1, accounts1);

        // Case 2 & 3 similar structure...
    }

    private static void testMintTo() {
        System.out.println("\n=== Testing MintTo ===");

        // Case 1: tx_hash: 2ZxDm8JBCYbJ5YQYWVqNvqZgz3jBpygY7FE6HmeTEFbxqVqxgXXvKPYGrRKpvZKBdLuVtJhNvqPHeLqxrEh9tWWx
        String[] accounts1 = new String[]{
                "HWHvQhFmJB6gPtqJx3gjxHX1iDZhQ9WJorxwb3iTWVHi",
                "J7nSEX8ADf3pVWhHS7qXqGYoxQqGYwKhNJfEMvFUSk7c",
                "TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA"
        };
        String base58Data1 = "7Bxs4cgF7g81qBE7";
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

        // Case 1: tx_hash: 5YHEkNjVGKRDw3KJNkyxEqHFxGQYqUxm6vt7kUdtJPBXJu6mZPaGF8vZmZHGGM6Hz8Vj8tQRKUyVmdyxXt1Sjoey
        String[] accounts1 = new String[]{
                "DjYyqKdwYcdXNMwzaykMwr6hvRJEHGmTQQnEBvqKvUYZ",
                "4QJwbmXp9L6NoAnyPwhat9yyGKJCTMKfH3HGEkhHGkZF",
                "TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA"
        };
        String base58Data1 = "8Bxs4cgF7g81qBE7";
        testInstruction("Burn Case 1", base58Data1, accounts1);

        // Case 2: tx_hash: 2ZGLGHrxZKZqNMUoWZh6rZmzxEQ4wDwNqRh8TFdJ6s9ToBvEmz7xHFWYWPHG3QVVxwH1zUvLo4JsBxo4x4JqCGPK
        String[] accounts2 = new String[]{
                "HWHvQhFmJB6gPtqJx3gjxHX1iDZhQ9WJorxwb3iTWVHi",
                "J7nSEX8ADf3pVWhHS7qXqGYoxQqGYwKhNJfEMvFUSk7c",
                "TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA"
        };
        String base58Data2 = "8Bxs4cgF7g81qBE7";
        testInstruction("Burn Case 2", base58Data2, accounts2);

        // Case 3: tx_hash: 3LFikuXyYbwoWywKqPw3AzQKLAAJQhKgqoqhUVZpT4qNyqXrqQpVjpN8SUx5BSW8H3MuFYkyJbp5SDxdcvNuxozs
        String[] accounts3 = new String[]{
                "2xNweLHLqrbx4zo1waDvgWJHgsUpPj8Y8icbAFeR4a8i",
                "8vqZWWG6ZKqYXkJvxXkYmqNYvG8NkY1X7ea7ZLKZ6T2L",
                "TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA"
        };
        String base58Data3 = "8Bxs4cgF7g81qBE7";
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

        // Case 1: tx_hash: 3LFikuXyYbwoWywKqPw3AzQKLAAJQhKgqoqhUVZpT4qNyqXrqQpVjpN8SUx5BSW8H3MuFYkyJbp5SDxdcvNuxozs
        String[] accounts1 = new String[]{
                "DjYyqKdwYcdXNMwzaykMwr6hvRJEHGmTQQnEBvqKvUYZ",
                "4QJwbmXp9L6NoAnyPwhat9yyGKJCTMKfH3HGEkhHGkZF",
                "TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA"
        };
        String base58Data1 = "9";
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


    // 辅助方法：将字节数组转换为十六进制字符串
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

}