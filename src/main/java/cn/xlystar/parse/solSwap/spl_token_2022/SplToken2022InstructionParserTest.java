package cn.xlystar.parse.solSwap.spl_token_2022;

import org.bitcoinj.core.Base58;
import java.util.Map;

public class SplToken2022InstructionParserTest {


    public static void main(String[] args) {
        System.out.println("Starting SPL Token 2022 instruction parser tests...\n");
        
        // 基础代币操作测试
//        testBasicOperations();
        
        // 利息相关测试 有问题
        testInterestBearing();
        
        // 转账费用相关测试 有问题
//        testTransferFee();
        
        // CPI Guard 测试 有问题
//        testCpiGuard();
        
        // 元数据和组指针测试 有问题
//        testPointers();
        
        System.out.println("\nAll tests completed!");
    }

    private static void testBasicOperations() {
        System.out.println("\n=== Testing Basic Operations ===");
        
//        // Transfer hash: 5dzREFL7wJauJeVNsc4Ykfkk2Htbkc46N36RKe2FGsUYfVY2629smBsBJVABRwKMsby98DcXj6SMTtL9EKh5WiLu
//        String[] transferAccounts = new String[]{
//            "FAXysofACTQvzawFYycG2ksqzPKTAeqb9M6FhFg2NMYf",  // source
//            "HxjEdXrawPQQ6NhPnEPnazeCoCXWyVz7pfPeYxSd8iVs",  // destination
//            "7gEQ6syDZmyPE4JdfJm4qatawnDqvqdh6i8jJjCXio6h",   // authority
//            "7gEQ6syDZmyPE4JdfJm4qatawnDqvqdh6i8jJjCXio6h"   // authority
//        };
//        String transferData = "3MzSiboSGouR";  // Transfer 10 tokens
//        testInstruction("Transfer", transferData, transferAccounts);
        
        // TransferChecked hash:2pfXsCk1zX6HEu8a1Uct9P9tiz9ZAuYvctLJcApoSTsZKv1y5y2AtzNSSDXkh8WuUmrpcFwtjbSGA6LQUkSNsUw4
        String[] transferCheckedAccounts = new String[]{
            "2AhYWdVsvws75GXF3C1onv6KYnjwHN8LrHjMRq3L5ijQ",  // source
            "QUYQQrpQLeSk7K14a2JNeKRvbha3UFAjMgb48wXh9vC",  // mint
            "BxK65ZrAKZ1TYbc7pCC1cxPrSqcXYxLXFQQ37HZGp7te",  // destination
            "CHkDXXRG7hUbJsDCtergWJU3jYsHftbgHhGSD75vvY2D"   // authority
        };
        String transferCheckedData = "h7pgtuBkxCvA1";  // Transfer 10 tokens, 9 decimals
        testInstruction("TransferChecked", transferCheckedData, transferCheckedAccounts);
    }

    private static void testInterestBearing() {
        System.out.println("\n=== Testing Interest Bearing ===");
        
        // InitializeInterestBearingConfig   hash:4a74cQkEMTR4aKessSgjfrsgENhP25Pv61qRTh1ZXn6d7xU2dMmmsbnKzmdDqyjDwDXRF8oPrYUgcN9KEWRnYBdG
        String[] initInterestAccounts = new String[]{
            "susdabGDNbhrnCa6ncrYo81u4s9GM8ecK2UwMyZiq4X",  // mint
            "FhVcYNEe58SMtxpZGnTu2kpYJrTu2vwCZDGpPLqbd2yG"   // rate authority
        };

        String initInterestData = "qw2o6";  // 1000 bps = 10%
        testInstruction("InitializeInterestBearingConfig", initInterestData, initInterestAccounts);
        
//        // UpdateRateInterestBearingConfig 待测试
//        String[] updateRateAccounts = new String[]{
//            "DjYyqKdwYcdXNMwzaykMwr6hvRJEHGmTQQnEBvqKvUYZ",  // mint
//            "4QJwbmXp9L6NoAnyPwhat9yyGKJCTMKfH3HGEkhHGkZF"   // rate authority
//        };
//        String updateRateData = "21F4010000000000000000";  // 500 bps = 5%
//        testInstruction("UpdateRateInterestBearingConfig", updateRateData, updateRateAccounts);
    }

    private static void testTransferFee() {
        System.out.println("\n=== Testing Transfer Fee ===");
        
        // TransferFeeConfig hash：4nXPQ9duCbWuUw6jtoY1VJ32k5WDAqtE6TTcRNkFmxgpS1qbR137dyW7ev2w8AzQWnRHNdTMvAxPF6RcoXPbsD1o
        String[] transferFeeConfigAccounts = new String[]{
            "DjYyqKdwYcdXNMwzaykMwr6hvRJEHGmTQQnEBvqKvUYZ",  // mint
            "4QJwbmXp9L6NoAnyPwhat9yyGKJCTMKfH3HGEkhHGkZF"   // fee authority
        };
        String transferFeeConfigData = "26E803000000000000000000E1F50500000000000000";  // 1000 bps, max 100000
        testInstruction("TransferFeeConfig", transferFeeConfigData, transferFeeConfigAccounts);
        
        // WithholdFee
//        String[] withholdFeeAccounts = new String[]{
//            "DjYyqKdwYcdXNMwzaykMwr6hvRJEHGmTQQnEBvqKvUYZ",  // source
//            "4QJwbmXp9L6NoAnyPwhat9yyGKJCTMKfH3HGEkhHGkZF",  // mint
//            "HWHvQhFmJB6gPtqJx3gjxHX1iDZhQ9WJorxwb3iTWVHi",  // fee account
//            "2xNweLHLqrbx4zo1waDvgWJHgsUpPj8Y8icbAFeR4a8i"   // authority
//        };
//        String withholdFeeData = "27000000e8d4a51000000000";  // 10 tokens
//        testInstruction("WithholdFee", withholdFeeData, withholdFeeAccounts);
    }

    private static void testCpiGuard() {
        System.out.println("\n=== Testing CPI Guard ===");
        
//        // EnableCpiGuard hash:PBjLJ1wBHqsBUCuwdBF7ijmyKVdAymSgK6M3eLMwmn42gs1jsBnnFHVZtf91tiTHFNmHABem56H56f5S3cA1jap
//        String[] enableCpiGuardAccounts = new String[]{
//            "3LuiQnAHjE3sw6ouSBtYpxKJcdNx589TigDNo4iA6Xfb",  // account
//            "iSoLfZGY7nau3UfyTJZDW9ZPdormm1Vc5UAFqsAT81L"   // authority
//        };
//        testInstruction("EnableCpiGuard", "3b5", enableCpiGuardAccounts);
        
        // DisableCpiGuard 没有这个指令
//        String[] disableCpiGuardAccounts = new String[]{
//            "DjYyqKdwYcdXNMwzaykMwr6hvRJEHGmTQQnEBvqKvUYZ",  // account
//            "4QJwbmXp9L6NoAnyPwhat9yyGKJCTMKfH3HGEkhHGkZF"   // authority
//        };
//        testInstruction("DisableCpiGuard", "2A", disableCpiGuardAccounts);
    }

    private static void testPointers() {
        System.out.println("\n=== Testing Pointers ===");
        
        // InitializeMetadataPointer 没有这个指令
//        String[] initMetadataAccounts = new String[]{
//            "DjYyqKdwYcdXNMwzaykMwr6hvRJEHGmTQQnEBvqKvUYZ",  // mint
//            "4QJwbmXp9L6NoAnyPwhat9yyGKJCTMKfH3HGEkhHGkZF"   // authority
//        };
//        String metadataAddress = "HWHvQhFmJB6gPtqJx3gjxHX1iDZhQ9WJorxwb3iTWVHi";
//        String initMetadataData = "2B" + Base58.decode(metadataAddress);
//        testInstruction("InitializeMetadataPointer", initMetadataData, initMetadataAccounts);
        
//        // InitializeGroupPointer 没有这个指令
//        String[] initGroupAccounts = new String[]{
//            "DjYyqKdwYcdXNMwzaykMwr6hvRJEHGmTQQnEBvqKvUYZ",  // mint
//            "4QJwbmXp9L6NoAnyPwhat9yyGKJCTMKfH3HGEkhHGkZF"   // authority
//        };
//        String groupAddress = "2xNweLHLqrbx4zo1waDvgWJHgsUpPj8Y8icbAFeR4a8i";
//        String initGroupData = "2D" + Base58.decode(groupAddress);
//        testInstruction("InitializeGroupPointer", initGroupData, initGroupAccounts);
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