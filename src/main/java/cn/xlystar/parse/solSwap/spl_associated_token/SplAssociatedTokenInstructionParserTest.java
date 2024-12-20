package cn.xlystar.parse.solSwap.spl_associated_token;


import org.bitcoinj.core.Base58;
import java.util.Map;

public class SplAssociatedTokenInstructionParserTest {
    public static void main(String[] args) {
        testCreate();
        testCreateIdempotent();
        testRecoverNested();
    }

    private static void testCreate() {
        System.out.println("\n=== Testing Create ===");

        // Case 1: tx_hash: 4oBFNe4qY8HQGUMChwfU4wEZi3oZ8uvkk9oGJr5akp7NYvJkEQGKKhHhPPf3YHpHNhZoKQJnhqj9FMqZGkeCfM6U
        String[] accounts1 = new String[]{
                "DjYyqKdwYcdXNMwzaykMwr6hvRJEHGmTQQnEBvqKvUYZ",  // funding
                "4QJwbmXp9L6NoAnyPwhat9yyGKJCTMKfH3HGEkhHGkZF",  // ata
                "HWHvQhFmJB6gPtqJx3gjxHX1iDZhQ9WJorxwb3iTWVHi",  // wallet
                "TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA",   // mint
                "11111111111111111111111111111111",              // system
                "TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA",   // token
                "SysvarRent111111111111111111111111111111111"    // rent
        };
        String base58Data1 = "";  // Create instruction has no data
        testInstruction("Create Case 1", base58Data1, accounts1);
    }

    private static void testCreateIdempotent() {
        System.out.println("\n=== Testing CreateIdempotent ===");

        // Case 1: tx_hash: 26DYrwW9tLeMhRM93vqhjYq1j9VQZwUsFUUnAU2asSaRLzbVkcLCNnLkghYzEDf8cBrMBmTHXNv1Mo4v8fCqzBpn
        String[] accounts1 = new String[]{
                "13jonn5DNhVd3H6UzwXLnpvootXpUGZzL9RNiUPRnGTR",  // funding
                "46Sfh6h49EQ95cPDhET1JjPMjvUWnMhfcQaZLBVZcX2s",  // ata
                "13jonn5DNhVd3H6UzwXLnpvootXpUGZzL9RNiUPRnGTR",  // wallet
                "FNu1NKrgyZQjhaULhFH5phXqUwCPBAMkzbddjdBHfRAV",   // mint
                "11111111111111111111111111111111",              // system
                "TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA",   // token
                "SysvarRent111111111111111111111111111111111"    // rent
        };
        String base58Data1 = "1";  // CreateIdempotent instruction type = 1
        testInstruction("CreateIdempotent Case 1", base58Data1, accounts1);
    }

    private static void testRecoverNested() {
        System.out.println("\n=== Testing RecoverNested ===");

        // Case 1: tx_hash: 5YHEkNjVGKRDw3KJNkyxEqHFxGQYqUxm6vt7kUdtJPBXJu6mZPaGF8vZmZHGGM6Hz8Vj8tQRKUyVmdyxXt1Sjoey
        String[] accounts1 = new String[]{
                "DjYyqKdwYcdXNMwzaykMwr6hvRJEHGmTQQnEBvqKvUYZ",  // nested ata
                "4QJwbmXp9L6NoAnyPwhat9yyGKJCTMKfH3HGEkhHGkZF",  // nested mint
                "HWHvQhFmJB6gPtqJx3gjxHX1iDZhQ9WJorxwb3iTWVHi",  // destination ata
                "2xNweLHLqrbx4zo1waDvgWJHgsUpPj8Y8icbAFeR4a8i",  // wallet
                "8vqZWWG6ZKqYXkJvxXkYmqNYvG8NkY1X7ea7ZLKZ6T2L",  // owner ata
                "TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA",   // owner mint
                "TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA"    // token program
        };
        String base58Data1 = "2";  // RecoverNested instruction type = 2
        testInstruction("RecoverNested Case 1", base58Data1, accounts1);
    }

    private static void testInstruction(String testName, String base58Data, String[] accounts) {
        try {
            System.out.println("\nTesting " + testName);
            System.out.println("Base58 Data: " + base58Data);
            System.out.println("Accounts:");
            for (int i = 0; i < accounts.length; i++) {
                System.out.println("  " + i + ": " + accounts[i]);
            }

            byte[] data = base58Data.isEmpty() ? new byte[0] : Base58.decode(base58Data);
            Map<String, Object> result = SplAssociatedTokenInstructionParser.parseInstruction(data, accounts);
            System.out.println("Parse result: " + result);

        } catch (Exception e) {
            System.err.println("Test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}