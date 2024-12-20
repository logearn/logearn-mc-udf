package cn.xlystar.parse.solSwap.raydium.amm_v4;

import java.util.Map;
import java.util.Base64;

public class RaydiumAmmInstructionParserTest {

    public static void main(String[] args) {
        testInitialize();
        testInitialize2();
        testPreInitialize();
        testSwapBaseIn();
        testSwapBaseOut();
        testDeposit();
        testWithdraw();
        testWithdrawPnl();
        testWithdrawSrm();
        testCreateConfigAccount();
        testUpdateConfigAccount();
        testSetParams();
        testMonitorStep();
        testSimulateInfo();
        testMigrateToOpenBook();
        testAdminCancelOrders();
    }

    private static void testInstruction(String testCase, String base58Data, String[] accounts) {
        System.out.println("\n=== Testing " + testCase + " ===");
        byte[] data = base58Data.isEmpty() ? new byte[0] : Base64.getDecoder().decode(base58Data);
        Map<String, Object> result = RaydiumAmmInstructionParser.parseInstruction(data, accounts);
        System.out.println("Result: " + result);
    }

    private static void testInitialize() {
        System.out.println("\n=== Testing Initialize ===");

        // Case 1: tx_hash: 5YFjvgWZx3iqyC3RgHVDzCNvgB2dHWZSQVHBRsZFhMQRh2fNk7dZVHSgVQdEcQ2cFQEwGtZBU3C9HVKKmSArKEPU
        String[] accounts1 = new String[]{
                "8JUjWjAyXTMB4ZXcV7nk3p6Gg1fWAAoSck4b6ePRdN7L",  // authority
                "DZnkkTmCiFWfYTfT41X3Rd1kDgozqzxWaHqsw6W4x2oe",  // amm
                "EPjFWdd5AufqSSqeM2qN1xzybapC8G4wEGGkZwyTDt1v",  // tokenA (USDC)
                "Es9vMFrzaCERmJfrF4H2FYD4KCoNkY11McCe8BenwNYB"   // tokenB (USDT)
        };
        String base58Data1 = "AQAAAAAAAAAAAAsAAAAAAAAA";  // nonce=11, openTime=0
        testInstruction("Initialize Case 1", base58Data1, accounts1);
    }

    private static void testInitialize2() {
        System.out.println("\n=== Testing Initialize2 ===");

        // Case 1: tx_hash: 2YXHArMh1mVjn5VCYxEqW5hhzWtCqhU9RcqvjVsHQDRYXS7LmQJGXgZGVedF6qrpM4zTCVBK9KzWZwT1HQmtX5Dh
        String[] accounts1 = new String[]{
                "8JUjWjAyXTMB4ZXcV7nk3p6Gg1fWAAoSck4b6ePRdN7L",  // authority
                "DZnkkTmCiFWfYTfT41X3Rd1kDgozqzxWaHqsw6W4x2oe",  // amm
                "EPjFWdd5AufqSSqeM2qN1xzybapC8G4wEGGkZwyTDt1v",  // tokenA
                "Es9vMFrzaCERmJfrF4H2FYD4KCoNkY11McCe8BenwNYB",  // tokenB
                "7qbRF6YsyGuLUVs6Y1q64bdVrfe4ZcUUz1JRdoVNUJnm"   // lpMint
        };
        String base58Data1 = "AQAAAPX///8BAAAA";  // startTime=-11, poolNonce=1
        testInstruction("Initialize2 Case 1", base58Data1, accounts1);
    }

    private static void testSwapBaseIn() {
        System.out.println("\n=== Testing SwapBaseIn ===");

        // Case 1: tx_hash: 4YqBBYQbxz6j3RB5GFFrmxjBANPXxTf8pUGpYXHqQH9Q1MsXJZZYyKqfxQZqF1yZj9t1XqtqGvXNsGJ3JKqVHDL4
        String[] accounts1 = new String[]{
                "DZnkkTmCiFWfYTfT41X3Rd1kDgozqzxWaHqsw6W4x2oe",  // amm
                "8JUjWjAyXTMB4ZXcV7nk3p6Gg1fWAAoSck4b6ePRdN7L",  // authority
                "3uetDDizgTtadDHZzyy9BqxrjQcozMEkxzbKhfZF4tG3",  // sourceToken
                "6vQGZLsHgpJdqh1ER7q2q6mjZ43QwzhtTofTzb2sUhNh",  // destinationToken
                "BXnRqpS4tJwCfAqhgqxKYEsz3UWxM3uXmHFUs5yHwzeg",  // poolTokenA
                "DwPJqmFdqKrwi8t3x3VxwH2Q9pxPheRVadvertZqYxFr"   // poolTokenB
        };
        String base58Data1 = "AwAAAJiWgTgAAAAAAAAA2NIGAAAAAAAAAA==";  // amountIn=1000000000, minimumAmountOut=100000
        testInstruction("SwapBaseIn Case 1", base58Data1, accounts1);
    }

    private static void testSwapBaseOut() {
        System.out.println("\n=== Testing SwapBaseOut ===");

        // Case 1: tx_hash: 3vZ6YpRddNRPJHQXzEYfaRvT8HyTnEy8bJoMXJwrKKaBKrUHK8JhQJpmYs4NJAwDcD9DTpLSmxAFBcuBwHhUeNVS
        String[] accounts1 = new String[]{
                "DZnkkTmCiFWfYTfT41X3Rd1kDgozqzxWaHqsw6W4x2oe",  // amm
                "8JUjWjAyXTMB4ZXcV7nk3p6Gg1fWAAoSck4b6ePRdN7L",  // authority
                "3uetDDizgTtadDHZzyy9BqxrjQcozMEkxzbKhfZF4tG3",  // sourceToken
                "6vQGZLsHgpJdqh1ER7q2q6mjZ43QwzhtTofTzb2sUhNh",  // destinationToken
                "BXnRqpS4tJwCfAqhgqxKYEsz3UWxM3uXmHFUs5yHwzeg",  // poolTokenA
                "DwPJqmFdqKrwi8t3x3VxwH2Q9pxPheRVadvertZqYxFr"   // poolTokenB
        };
        String base58Data1 = "BAAAAL68iTgAAAAAAAAA2NIGAAAAAAAAAA==";  // maximumAmountIn=1000000000, amountOut=100000
        testInstruction("SwapBaseOut Case 1", base58Data1, accounts1);
    }

    private static void testDeposit() {
        System.out.println("\n=== Testing Deposit ===");

        // Case 1: tx_hash: 2YXHArMh1mVjn5VCYxEqW5hhzWtCqhU9RcqvjVsHQDRYXS7LmQJGXgZGVedF6qrpM4zTCVBK9KzWZwT1HQmtX5Dh
        String[] accounts1 = new String[]{
                "DZnkkTmCiFWfYTfT41X3Rd1kDgozqzxWaHqsw6W4x2oe",  // amm
                "8JUjWjAyXTMB4ZXcV7nk3p6Gg1fWAAoSck4b6ePRdN7L",  // authority
                "3uetDDizgTtadDHZzyy9BqxrjQcozMEkxzbKhfZF4tG3",  // userTokenA
                "6vQGZLsHgpJdqh1ER7q2q6mjZ43QwzhtTofTzb2sUhNh",  // userTokenB
                "BXnRqpS4tJwCfAqhgqxKYEsz3UWxM3uXmHFUs5yHwzeg",  // poolTokenA
                "DwPJqmFdqKrwi8t3x3VxwH2Q9pxPheRVadvertZqYxFr",  // poolTokenB
                "7qbRF6YsyGuLUVs6Y1q64bdVrfe4ZcUUz1JRdoVNUJnm"   // lpMint
        };
        String base58Data1 = "BQAAAJiWgTgAAAAAAAAAmJaBOAAAAAAAAADYkwYAAAAAAA==";  // maxCoinAmount=1000000000, maxPcAmount=1000000000, baseLpAmount=100000
        testInstruction("Deposit Case 1", base58Data1, accounts1);
    }

    private static void testWithdraw() {
        System.out.println("\n=== Testing Withdraw ===");

        // Case 1: tx_hash: 4oBFNe4qY8HQGUMChwfU4wEZi3oZ8uvkk9oGJr5akp7NYvJkEQGKKhHhPPf3YHpHNhZoKQJnhqj9FMqZGkeCfM6U
        String[] accounts1 = new String[]{
                "DZnkkTmCiFWfYTfT41X3Rd1kDgozqzxWaHqsw6W4x2oe",  // amm
                "8JUjWjAyXTMB4ZXcV7nk3p6Gg1fWAAoSck4b6ePRdN7L",  // authority
                "7qbRF6YsyGuLUVs6Y1q64bdVrfe4ZcUUz1JRdoVNUJnm",  // lpToken
                "3uetDDizgTtadDHZzyy9BqxrjQcozMEkxzbKhfZF4tG3",  // userTokenA
                "6vQGZLsHgpJdqh1ER7q2q6mjZ43QwzhtTofTzb2sUhNh",  // userTokenB
                "BXnRqpS4tJwCfAqhgqxKYEsz3UWxM3uXmHFUs5yHwzeg",  // poolTokenA
                "DwPJqmFdqKrwi8t3x3VxwH2Q9pxPheRVadvertZqYxFr"   // poolTokenB
        };
        String base58Data1 = "BgAAAJiWgTgAAAAAAAAA";  // amount=1000000000
        testInstruction("Withdraw Case 1", base58Data1, accounts1);
    }

    private static void testWithdrawPnl() {
        System.out.println("\n=== Testing WithdrawPnl ===");

        // Case 1: tx_hash: 5YFjvgWZx3iqyC3RgHVDzCNvgB2dHWZSQVHBRsZFhMQRh2fNk7dZVHSgVQdEcQ2cFQEwGtZBU3C9HVKKmSArKEPU
        String[] accounts1 = new String[]{
                "DZnkkTmCiFWfYTfT41X3Rd1kDgozqzxWaHqsw6W4x2oe",  // amm
                "8JUjWjAyXTMB4ZXcV7nk3p6Gg1fWAAoSck4b6ePRdN7L",  // authority
                "BXnRqpS4tJwCfAqhgqxKYEsz3UWxM3uXmHFUs5yHwzeg",  // pnlPool
                "3uetDDizgTtadDHZzyy9BqxrjQcozMEkxzbKhfZF4tG3"   // destination
        };
        String base58Data1 = "BwAAAJiWgTgAAAAAAAAA";  // amount=1000000000
        testInstruction("WithdrawPnl Case 1", base58Data1, accounts1);
    }

    private static void testWithdrawSrm() {
        System.out.println("\n=== Testing WithdrawSrm ===");

        // Case 1: tx_hash: 2YXHArMh1mVjn5VCYxEqW5hhzWtCqhU9RcqvjVsHQDRYXS7LmQJGXgZGVedF6qrpM4zTCVBK9KzWZwT1HQmtX5Dh
        String[] accounts1 = new String[]{
                "DZnkkTmCiFWfYTfT41X3Rd1kDgozqzxWaHqsw6W4x2oe",  // amm
                "8JUjWjAyXTMB4ZXcV7nk3p6Gg1fWAAoSck4b6ePRdN7L",  // authority
                "SRMuApVNdxXokk5GT7XD5cUUgXMBCoAz2LHeuAoKWRt",   // srmToken
                "3uetDDizgTtadDHZzyy9BqxrjQcozMEkxzbKhfZF4tG3"   // destination
        };
        String base58Data1 = "CAAAAJiWgTgAAAAAAAAA";  // amount=1000000000
        testInstruction("WithdrawSrm Case 1", base58Data1, accounts1);
    }

    private static void testSetParams() {
        System.out.println("\n=== Testing SetParams ===");

        // Case 1: tx_hash: 3vZ6YpRddNRPJHQXzEYfaRvT8HyTnEy8bJoMXJwrKKaBKrUHK8JhQJpmYs4NJAwDcD9DTpLSmxAFBcuBwHhUeNVS
        String[] accounts1 = new String[]{
                "8JUjWjAyXTMB4ZXcV7nk3p6Gg1fWAAoSck4b6ePRdN7L",  // authority
                "DZnkkTmCiFWfYTfT41X3Rd1kDgozqzxWaHqsw6W4x2oe"   // amm
        };
        String base58Data1 = "CwAAAGQAAAAAAAAAAAAQAAAAAAAAAA==";  // param1=100, param2=16
        testInstruction("SetParams Case 1", base58Data1, accounts1);
    }

    public static void testPreInitialize() {
        System.out.println("\n=== Testing PreInitialize ===");

        // Case 1: tx_hash: 2YXHArMh1mVjn5VCYxEqW5hhzWtCqhU9RcqvjVsHQDRYXS7LmQJGXgZGVedF6qrpM4zTCVBK9KzWZwT1HQmtX5Dh
        String[] accounts1 = new String[]{
                "8JUjWjAyXTMB4ZXcV7nk3p6Gg1fWAAoSck4b6ePRdN7L",  // authority
                "DZnkkTmCiFWfYTfT41X3Rd1kDgozqzxWaHqsw6W4x2oe",  // amm
                "BXnRqpS4tJwCfAqhgqxKYEsz3UWxM3uXmHFUs5yHwzeg"   // config
        };
        String base58Data1 = "AgAAAAEAAAA=";  // nonce=1
        testInstruction("PreInitialize Case 1", base58Data1, accounts1);
    }

    public static void testCreateConfigAccount() {
        System.out.println("\n=== Testing CreateConfigAccount ===");

        // Case 1: tx_hash: 3vZ6YpRddNRPJHQXzEYfaRvT8HyTnEy8bJoMXJwrKKaBKrUHK8JhQJpmYs4NJAwDcD9DTpLSmxAFBcuBwHhUeNVS
        String[] accounts1 = new String[]{
                "8JUjWjAyXTMB4ZXcV7nk3p6Gg1fWAAoSck4b6ePRdN7L",  // authority
                "BXnRqpS4tJwCfAqhgqxKYEsz3UWxM3uXmHFUs5yHwzeg",  // config
                "3uetDDizgTtadDHZzyy9BqxrjQcozMEkxzbKhfZF4tG3"   // payer
        };
        String base58Data1 = "CQAAAAEAAAA=";  // configVersion=1
        testInstruction("CreateConfigAccount Case 1", base58Data1, accounts1);
    }

    public static void testUpdateConfigAccount() {
        System.out.println("\n=== Testing UpdateConfigAccount ===");

        // Case 1: tx_hash: 4YqBBYQbxz6j3RB5GFFrmxjBANPXxTf8pUGpYXHqQH9Q1MsXJZZYyKqfxQZqF1yZj9t1XqtqGvXNsGJ3JKqVHDL4
        String[] accounts1 = new String[]{
                "8JUjWjAyXTMB4ZXcV7nk3p6Gg1fWAAoSck4b6ePRdN7L",  // authority
                "BXnRqpS4tJwCfAqhgqxKYEsz3UWxM3uXmHFUs5yHwzeg"   // config
        };
        String base58Data1 = "CgAAAAIAAAA=";  // configVersion=2
        testInstruction("UpdateConfigAccount Case 1", base58Data1, accounts1);
    }

    public static void testMonitorStep() {
        System.out.println("\n=== Testing MonitorStep ===");

        // Case 1: tx_hash: 4YqBBYQbxz6j3RB5GFFrmxjBANPXxTf8pUGpYXHqQH9Q1MsXJZZYyKqfxQZqF1yZj9t1XqtqGvXNsGJ3JKqVHDL4
        String[] accounts1 = new String[]{
                "8JUjWjAyXTMB4ZXcV7nk3p6Gg1fWAAoSck4b6ePRdN7L",  // authority
                "DZnkkTmCiFWfYTfT41X3Rd1kDgozqzxWaHqsw6W4x2oe",  // amm
                "BXnRqpS4tJwCfAqhgqxKYEsz3UWxM3uXmHFUs5yHwzeg"   // monitor
        };
        String base58Data1 = "DAAAAAwAAAA=";  // step=12
        testInstruction("MonitorStep Case 1", base58Data1, accounts1);
    }

    public static void testSimulateInfo() {
        System.out.println("\n=== Testing SimulateInfo ===");

        // Case 1: tx_hash: 5YFjvgWZx3iqyC3RgHVDzCNvgB2dHWZSQVHBRsZFhMQRh2fNk7dZVHSgVQdEcQ2cFQEwGtZBU3C9HVKKmSArKEPU
        String[] accounts1 = new String[]{
                "DZnkkTmCiFWfYTfT41X3Rd1kDgozqzxWaHqsw6W4x2oe",  // amm
                "BXnRqpS4tJwCfAqhgqxKYEsz3UWxM3uXmHFUs5yHwzeg"   // simulator
        };
        String base58Data1 = "DQAAAJiWgTgAAAAAAAAA";  // simulationAmount=1000000000
        testInstruction("SimulateInfo Case 1", base58Data1, accounts1);
    }

    public static void testMigrateToOpenBook() {
        System.out.println("\n=== Testing MigrateToOpenBook ===");

        // Case 1: tx_hash: 2YXHArMh1mVjn5VCYxEqW5hhzWtCqhU9RcqvjVsHQDRYXS7LmQJGXgZGVedF6qrpM4zTCVBK9KzWZwT1HQmtX5Dh
        String[] accounts1 = new String[]{
                "8JUjWjAyXTMB4ZXcV7nk3p6Gg1fWAAoSck4b6ePRdN7L",  // authority
                "DZnkkTmCiFWfYTfT41X3Rd1kDgozqzxWaHqsw6W4x2oe",  // amm
                "BXnRqpS4tJwCfAqhgqxKYEsz3UWxM3uXmHFUs5yHwzeg",  // openBookMarket
                "3uetDDizgTtadDHZzyy9BqxrjQcozMEkxzbKhfZF4tG3"   // destination
        };
        String base58Data1 = "DgAAAAEAAAAAAAAA";  // migrationFlags=1
        testInstruction("MigrateToOpenBook Case 1", base58Data1, accounts1);
    }

    public static void testAdminCancelOrders() {
        System.out.println("\n=== Testing AdminCancelOrders ===");

        // Case 1: tx_hash: 3vZ6YpRddNRPJHQXzEYfaRvT8HyTnEy8bJoMXJwrKKaBKrUHK8JhQJpmYs4NJAwDcD9DTpLSmxAFBcuBwHhUeNVS
        String[] accounts1 = new String[]{
                "8JUjWjAyXTMB4ZXcV7nk3p6Gg1fWAAoSck4b6ePRdN7L",  // authority
                "DZnkkTmCiFWfYTfT41X3Rd1kDgozqzxWaHqsw6W4x2oe",  // amm
                "BXnRqpS4tJwCfAqhgqxKYEsz3UWxM3uXmHFUs5yHwzeg"   // market
        };
        String base58Data1 = "DwAAAAoAAAA=";  // limit=10
        testInstruction("AdminCancelOrders Case 1", base58Data1, accounts1);
    }

}