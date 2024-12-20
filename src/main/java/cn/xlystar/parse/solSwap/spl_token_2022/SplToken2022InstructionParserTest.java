package cn.xlystar.parse.solSwap.spl_token_2022;

import org.bitcoinj.core.Base58;
import java.util.Map;

public class SplToken2022InstructionParserTest {
    public static void main(String[] args) {
        System.out.println("Starting SPL Token 2022 instruction parser tests...\n");
        
        // 基础代币操作测试
        testBasicOperations();
        
        // 利息相关测试
        testInterestBearing();
        
        // 转账费用相关测试
        testTransferFee();
        
        // CPI Guard 测试
        testCpiGuard();
        
        // 元数据和组指针测试
        testPointers();
        
        System.out.println("\nAll tests completed!");
    }

    private static void testBasicOperations() {
        System.out.println("\n=== Testing Basic Operations ===");
        
        // Transfer
        String[] transferAccounts = new String[]{
            "DjYyqKdwYcdXNMwzaykMwr6hvRJEHGmTQQnEBvqKvUYZ",  // source
            "4QJwbmXp9L6NoAnyPwhat9yyGKJCTMKfH3HGEkhHGkZF",  // destination
            "HWHvQhFmJB6gPtqJx3gjxHX1iDZhQ9WJorxwb3iTWVHi"   // authority
        };
        String transferData = "03000000e8d4a51000000000";  // Transfer 10 tokens
        testInstruction("Transfer", transferData, transferAccounts);
        
        // TransferChecked
        String[] transferCheckedAccounts = new String[]{
            "DjYyqKdwYcdXNMwzaykMwr6hvRJEHGmTQQnEBvqKvUYZ",  // source
            "4QJwbmXp9L6NoAnyPwhat9yyGKJCTMKfH3HGEkhHGkZF",  // mint
            "HWHvQhFmJB6gPtqJx3gjxHX1iDZhQ9WJorxwb3iTWVHi",  // destination
            "2xNweLHLqrbx4zo1waDvgWJHgsUpPj8Y8icbAFeR4a8i"   // authority
        };
        String transferCheckedData = "0C000000e8d4a5100000000009";  // Transfer 10 tokens, 9 decimals
        testInstruction("TransferChecked", transferCheckedData, transferCheckedAccounts);
    }

    private static void testInterestBearing() {
        System.out.println("\n=== Testing Interest Bearing ===");
        
        // InitializeInterestBearingConfig
        String[] initInterestAccounts = new String[]{
            "DjYyqKdwYcdXNMwzaykMwr6hvRJEHGmTQQnEBvqKvUYZ",  // mint
            "4QJwbmXp9L6NoAnyPwhat9yyGKJCTMKfH3HGEkhHGkZF"   // rate authority
        };
        String initInterestData = "20E8030000000000000000";  // 1000 bps = 10%
        testInstruction("InitializeInterestBearingConfig", initInterestData, initInterestAccounts);
        
        // UpdateRateInterestBearingConfig
        String[] updateRateAccounts = new String[]{
            "DjYyqKdwYcdXNMwzaykMwr6hvRJEHGmTQQnEBvqKvUYZ",  // mint
            "4QJwbmXp9L6NoAnyPwhat9yyGKJCTMKfH3HGEkhHGkZF"   // rate authority
        };
        String updateRateData = "21F4010000000000000000";  // 500 bps = 5%
        testInstruction("UpdateRateInterestBearingConfig", updateRateData, updateRateAccounts);
    }

    private static void testTransferFee() {
        System.out.println("\n=== Testing Transfer Fee ===");
        
        // TransferFeeConfig
        String[] transferFeeConfigAccounts = new String[]{
            "DjYyqKdwYcdXNMwzaykMwr6hvRJEHGmTQQnEBvqKvUYZ",  // mint
            "4QJwbmXp9L6NoAnyPwhat9yyGKJCTMKfH3HGEkhHGkZF"   // fee authority
        };
        String transferFeeConfigData = "26E803000000000000000000E1F50500000000000000";  // 1000 bps, max 100000
        testInstruction("TransferFeeConfig", transferFeeConfigData, transferFeeConfigAccounts);
        
        // WithholdFee
        String[] withholdFeeAccounts = new String[]{
            "DjYyqKdwYcdXNMwzaykMwr6hvRJEHGmTQQnEBvqKvUYZ",  // source
            "4QJwbmXp9L6NoAnyPwhat9yyGKJCTMKfH3HGEkhHGkZF",  // mint
            "HWHvQhFmJB6gPtqJx3gjxHX1iDZhQ9WJorxwb3iTWVHi",  // fee account
            "2xNweLHLqrbx4zo1waDvgWJHgsUpPj8Y8icbAFeR4a8i"   // authority
        };
        String withholdFeeData = "27000000e8d4a51000000000";  // 10 tokens
        testInstruction("WithholdFee", withholdFeeData, withholdFeeAccounts);
    }

    private static void testCpiGuard() {
        System.out.println("\n=== Testing CPI Guard ===");
        
        // EnableCpiGuard
        String[] enableCpiGuardAccounts = new String[]{
            "DjYyqKdwYcdXNMwzaykMwr6hvRJEHGmTQQnEBvqKvUYZ",  // account
            "4QJwbmXp9L6NoAnyPwhat9yyGKJCTMKfH3HGEkhHGkZF"   // authority
        };
        testInstruction("EnableCpiGuard", "29", enableCpiGuardAccounts);
        
        // DisableCpiGuard
        String[] disableCpiGuardAccounts = new String[]{
            "DjYyqKdwYcdXNMwzaykMwr6hvRJEHGmTQQnEBvqKvUYZ",  // account
            "4QJwbmXp9L6NoAnyPwhat9yyGKJCTMKfH3HGEkhHGkZF"   // authority
        };
        testInstruction("DisableCpiGuard", "2A", disableCpiGuardAccounts);
    }

    private static void testPointers() {
        System.out.println("\n=== Testing Pointers ===");
        
        // InitializeMetadataPointer
        String[] initMetadataAccounts = new String[]{
            "DjYyqKdwYcdXNMwzaykMwr6hvRJEHGmTQQnEBvqKvUYZ",  // mint
            "4QJwbmXp9L6NoAnyPwhat9yyGKJCTMKfH3HGEkhHGkZF"   // authority
        };
        String metadataAddress = "HWHvQhFmJB6gPtqJx3gjxHX1iDZhQ9WJorxwb3iTWVHi";
        String initMetadataData = "2B" + Base58.decode(metadataAddress);
        testInstruction("InitializeMetadataPointer", initMetadataData, initMetadataAccounts);
        
        // InitializeGroupPointer
        String[] initGroupAccounts = new String[]{
            "DjYyqKdwYcdXNMwzaykMwr6hvRJEHGmTQQnEBvqKvUYZ",  // mint
            "4QJwbmXp9L6NoAnyPwhat9yyGKJCTMKfH3HGEkhHGkZF"   // authority
        };
        String groupAddress = "2xNweLHLqrbx4zo1waDvgWJHgsUpPj8Y8icbAFeR4a8i";
        String initGroupData = "2D" + Base58.decode(groupAddress);
        testInstruction("InitializeGroupPointer", initGroupData, initGroupAccounts);
    }

    private static void testInstruction(String testName, String base58Data, String[] accounts) {
        try {
            System.out.println("\nTesting " + testName);
            System.out.println("Base58 Data: " + base58Data);
            System.out.println("Accounts:");
            for (int i = 0; i < accounts.length; i++) {
                System.out.println("  " + i + ": " + accounts[i]);
            }
            
            byte[] data = Base58.decode(base58Data);
            Map<String, Object> result = SplToken2022InstructionParser.parseInstruction(data, accounts);
            System.out.println("Parse result: " + result);
            
        } catch (Exception e) {
            System.err.println("Test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 