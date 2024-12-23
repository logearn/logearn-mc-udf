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

//        // CreateAccountWithSeed
//        testCreateAccountWithSeed();

//        // AdvanceNonce 测试用例
//        testAdvanceNonce();
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
//        // WithdrawNonce
        testWithdrawNonce();
//
//        // AuthorizeNonce
//        testAuthorizeNonce();
//
//        // AllocateWithSeed
//        testAllocateWithSeed();
//
//        // AssignWithSeed
//        testAssignWithSeed();
//
//        // UpgradeNonceAccount
//        testUpgradeNonceAccount();
    }

    // CreateAccount 测试用例
    private static void testCreateAccount() {
        System.out.println("\n=== Testing CreateAccount ===");

        // Case 1: tx_hash: 2EARWf5jKMxRpvnqxRBKgGGVXVoXY4qxkJyYCbPwcZbV2bjZhcvJwPkL4LgKtBsHGxSQB7FcdZqR7menjXTyKsXt
        String[] accounts1 = new String[]{
                "3nTcdN7iZ2afeBWsX1WPUj7vqsdRycRNGEmhhAhGxHWG",
                "FDGoUfdVawJcceyYSUqADsGhbxdqCdvH7SaeZ6VYupLU"
        };
        String base58Data1 = "11119os1e9qSs2u7TsThXqkBSRVFxhmYaFKFZ1waB2X7armDmvK3p5GmLdUxYdg3h7QSrL";
        testInstruction("CreateAccount Case 1", base58Data1, accounts1);

        // Case 2: tx_hash: 57TG3QU6Xq1eQhYQfKU48k1wqUWsHzsDyUBgWiTqZhXgkVdBJZwXqHiZQykiqYrXBhbwPLxNwZKKZVxGvNhRyPry
//        String[] accounts2 = new String[]{
//                "HWHvQhFmJB6gPtqJx3gjxHX1iDZhQ9WJorxwb3iTWVHi",
//                "J7nSEX8ADf3pVWhHS7qXqGYoxQqGYwKhNJfEMvFUSk7c"
//        };
//        String base58Data2 = "11119os1e9qSs2u7TsThXqkBSRVFxhmYaFKFZ1waB2X7armDmvK3p5GmLdUxYdg3h7QSrL";
//        testInstruction("CreateAccount Case 2", base58Data2, accounts2);
//
//        // Case 3: tx_hash: 3LFikuXyYbwoWywKqPw3AzQKLAAJQhKgqoqhUVZpT4qNyqXrqQpVjpN8SUx5BSW8H3MuFYkyJbp5SDxdcvNuxozs
//        String[] accounts3 = new String[]{
//                "2xNweLHLqrbx4zo1waDvgWJHgsUpPj8Y8icbAFeR4a8i",
//                "8vqZWWG6ZKqYXkJvxXkYmqNYvG8NkY1X7ea7ZLKZ6T2L"
//        };
//        String base58Data3 = "11119os1e9qSs2u7TsThXqkBSRVFxhmYaFKFZ1waB2X7armDmvK3p5GmLdUxYdg3h7QSrL";
//        testInstruction("CreateAccount Case 3", base58Data3, accounts3);
    }

    // Transfer 测试用例
    private static void testTransfer() {
        System.out.println("\n=== Testing Transfer ===");

        // Case 1: tx_hash: 22CvLyjZh74kk3Zx1V7UsevawvkDdHG7E6Zfrcmm5xmU9bxQ2gNoFPs26B2NWqYEj9HTg2ixajreBZRnb5J3GNTY
        String[] accounts1 = new String[]{
                "ANmzgnZPpcW1SpeuWoctPZoMLFNLYCERAFVGcrnDdq12",
                "CgANddXc7FKSsdLSdFv67X8faZqQaRTeLMXkAVANkZD4"
        };
        String base58Data1 = "3Bxs43ZMjSRQLs6o";
        testInstruction("Transfer Case 1", base58Data1, accounts1);

        // Case 2: tx_hash: 3vDU6JZRwDVVVo3KwGfA3T1YzgzJrDwHGolomBkGxqKJYYDxnPps8Z64eV3HQ4QKVxDyf4yVz2X6Q5VUh4Tdj6Cy
//        String[] accounts2 = new String[]{
//                "DriQBvRrqHzxi2xGxJQKw9pYP6JG2LWx5FoAqZ9k7ywa",
//                "6QuXb6mB6WmRASP2y8AavXh6aabBXEH5ZzrSH5xRrgSm"
//        };
//        String base58Data2 = "3Bxs4cgF7g81qBE7";
//        testInstruction("Transfer Case 2", base58Data2, accounts2);
//
//        // Case 3: tx_hash: 2ZGLGHrxZKZqNMUoWZh6rZmzxEQ4wDwNqRh8TFdJ6s9ToBvEmz7xHFWYWPHG3QVVxwH1zUvLo4JsBxo4x4JqCGPK
//        String[] accounts3 = new String[]{
//                "8vqZWWG6ZKqYXkJvxXkYmqNYvG8NkY1X7ea7ZLKZ6T2L",
//                "2xNweLHLqrbx4zo1waDvgWJHgsUpPj8Y8icbAFeR4a8i"
//        };
//        String base58Data3 = "3Bxs4cgF7g81qBE7";
//        testInstruction("Transfer Case 3", base58Data3, accounts3);
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

        // Case 1: tx_hash: 33YxcEUMKq4oS9EnsSTFW9FMp8zmEpdjXwUur3keJwAMtySY5GrvE6hM8TgDBm2tC2FHNC6ktnCY3qaMJvovv9Hj
        String[] accounts1 = new String[]{
                "4Ysj3UTb51DXzJbTZ1fiUeiW3fJe54UNPzPj6EutPyq9",
                "AyMxdnoKcfk1vTABTV3VQiAncrYGojATJ38VHciCPffq",
                "AyMxdnoKcfk1vTABTV3VQiAncrYGojATJ38VHciCPffq"
        };
        String base58Data1 = "3ipZWfp3a73q9NetBMTv89y7k8CLcN6xtTtDHmVoiBYzLYSy2bJtV2aacYyYvqUciyC3XEDGhi5vHb3Qyvcrh3NMY1ovRD567smu99MyZ8rMiuHa2DvGMyQYHDdpr7fcy6rvRUkqZgsoJxbNHM31MFWhdDhmfJEG2YGW3j1CU";
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

//        // Case 1: tx_hash: s9BC5Zz5aF736SaYmnMDSNs3fGYR3RL8UDskMFo7eZVGanVDvwBhebNtKtkn4Ato3ES4NJNRpST8o71m7uJwSES
//        String[] accounts1 = new String[]{
//                "3NDJuM73JT9UWxbKVLpbEjXZie9se53Vy3fEzVyEZ9yA",
//                "SysvarRecentB1ockHashes11111111111111111111",
//                "APhyMCpYjQ9RdEBn8cs4ifyBXjxAS5JtM3wYpWMJjsY5"
//        };
//        String base58Data1 = "6vx8P";
//        testInstruction("AdvanceNonce Case 1", base58Data1, accounts1);

        // Case 2: tx_hash: 4UCUtNsbXYEeBGcjB7hjUQx3AR3ysYBD1rSUf2691QS5AvSjbJmfWd97TstdnJaAUW8JJpXNwQeU43z4hLvkncRH
        String[] accounts2 = new String[]{
                "Dy63hjpZa4mrQp8JV5zRmsuBV6snKESBWT6SfzRaQWKJ",
                "SysvarRecentB1ockHashes11111111111111111111",
                "DScDQ1zV4qVMU8HQmfcJkjZhfo5QqCWdV7dbxkb2gU9C"
        };
        String base58Data2 = "6vx8P";
        testInstruction("AdvanceNonce Case 2", base58Data2, accounts2);
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

        // Case 1: tx_hash: De7RzAqADpgsUzUDGDKQD6fgYU5MN4qw5UxrL3mNXvBNB8HR6rBMrgcb1tCA8QMjBEVLiJAGfMjps8Mx59xQag1
        String[] accounts1 = new String[]{
                "2nxmj5cGnsES3pcxfkGdFcJsAodhj7KoUm6pqtvT8BEn"
        };
        String base58Data1 = "9krTDN16mhNzZ1rw";
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

        // Case 1: tx_hash: 3jm4Np9pnDg5P5BdxAbQHKZ9VWSVWmH7qtZpcLEP2uHU1PdxRb5vkg66rPHFWpxgupsdbq6fEzrbvJmU3KiVuGby
        String[] accounts1 = new String[]{
                "NexTbLoCkWykbLuB1NkjXgFWkX9oAtcoagQegygXXA2",
                "9eo5mwyw85qRWo9afx5KFUCQdqQx6Z1N8po58GaetyNR",
                "9eo5mwyw85qRWo9afx5KFUCQdqQx6Z1N8po58GaetyNR"
        };
        String base58Data1 = "2SUBFezdPDqe5KgNmihzukRHVSacuiNGApJNFQHeuzBjf34ZNojHS2Uzh5agLNvXA345NrdG8CeYWcm7r4Mi2dtKSZucs";
        testInstruction("TransferWithSeed Case 1", base58Data1, accounts1);

//        // Case 2: tx_hash: 2ZGLGHrxZKZqNMUoWZh6rZmzxEQ4wDwNqRh8TFdJ6s9ToBvEmz7xHFWYWPHG3QVVxwH1zUvLo4JsBxo4x4JqCGPK
//        String[] accounts2 = new String[]{
//                "HWHvQhFmJB6gPtqJx3gjxHX1iDZhQ9WJorxwb3iTWVHi",
//                "J7nSEX8ADf3pVWhHS7qXqGYoxQqGYwKhNJfEMvFUSk7c",
//                "TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA"
//        };
//        String base58Data2 = "B1111111111111MyTestSeed2";
//        testInstruction("TransferWithSeed Case 2", base58Data2, accounts2);
//
//        // Case 3: tx_hash: 3LFikuXyYbwoWywKqPw3AzQKLAAJQhKgqoqhUVZpT4qNyqXrqQpVjpN8SUx5BSW8H3MuFYkyJbp5SDxdcvNuxozs
//        String[] accounts3 = new String[]{
//                "2xNweLHLqrbx4zo1waDvgWJHgsUpPj8Y8icbAFeR4a8i",
//                "8vqZWWG6ZKqYXkJvxXkYmqNYvG8NkY1X7ea7ZLKZ6T2L",
//                "TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA"
//        };
//        String base58Data3 = "B1111111111111MyTestSeed3";
//        testInstruction("TransferWithSeed Case 3", base58Data3, accounts3);
    }

    // InitializeNonce 测试用例
    private static void testInitializeNonce() {
        System.out.println("\n=== Testing InitializeNonce ===");

        // Case 1: tx_hash: 26TWWVBWewPP93NGngEPsV6K5HfwnuoUfsVNiLhT7Gu9Wm8YYHnZbNjCBueKEyZ638TkDy52pf1n5y7wwXeiQ9KK
        String[] accounts1 = new String[]{
                "HRhARjmyD9kiXmxr9buyJ2u5rw2zrxuzR54EMypbcYv7",
                "SysvarRecentB1ockHashes11111111111111111111",
                "SysvarRent111111111111111111111111111111111"
        };
        String base58Data1 = "3eGCCKHLSkdAP7K9FjZDL9kQg1LYkASCUbhqfQvthZP9ZtpwD";
        testInstruction("InitializeNonce Case 1", base58Data1, accounts1);

//        // Case 2: tx_hash: 2ZxDm8JBCYbJ5YQYWVqNvqZgz3jBpygY7FE6HmeTEFbxqVqxgXXvKPYGrRKpvZKBdLuVtJhNvqPHeLqxrEh9tWWx
//        String[] accounts2 = new String[]{
//                "HWHvQhFmJB6gPtqJx3gjxHX1iDZhQ9WJorxwb3iTWVHi",
//                "SysvarRecentB1ockHashes11111111111111111111",
//                "SysvarRent111111111111111111111111111111111"
//        };
//        String base58Data2 = "61111111111111";
//        testInstruction("InitializeNonce Case 2", base58Data2, accounts2);
//
//        // Case 3: tx_hash: 5fqXJZpyvkE8YvSHbR3VgZEpWxzVaFGV4TakFyCGgBhzHxVr6yzZRqtR7xo6VrBHVmuEwGPjVoXcGxp3A9S4iFhk
//        String[] accounts3 = new String[]{
//                "2xNweLHLqrbx4zo1waDvgWJHgsUpPj8Y8icbAFeR4a8i",
//                "SysvarRecentB1ockHashes11111111111111111111",
//                "SysvarRent111111111111111111111111111111111"
//        };
//        String base58Data3 = "61111111111111";
//        testInstruction("InitializeNonce Case 3", base58Data3, accounts3);
    }

    // WithdrawNonce 测试用例
    private static void testWithdrawNonce() {
        System.out.println("\n=== Testing WithdrawNonce ===");

        // Case 1: tx_hash:599ZoSXKdQBPp6zxMjZGK6ge6ERGHJycz2XFZx5y8tsm9hVTBZmsAPiZ5V1a8991bGZb7SzyL8oGadDUeMm6WsEw
        String[] accounts1 = new String[]{
                "C4i7U33e6evz2jig3MnaKQCQWGkLBqHv6YgnEwKE67hx",
                "13MY2yZ6SedFhbmu92dAK5hdhkGRGZncUwfSpxwedtyR",
                "SysvarRecentB1ockHashes11111111111111111111",
                "SysvarRent111111111111111111111111111111111",
                "13MY2yZ6SedFhbmu92dAK5hdhkGRGZncUwfSpxwedtyR"
        };
        String base58Data1 = "6UQf8s8v7Laq63RZ";
        testInstruction("WithdrawNonce Case 1", base58Data1, accounts1);

        // Case 2: tx_hash: 3jLiy4E3QDFXybgUwP62hBW8K7YLvSZVZFCBTjgfUUGtRzqgEEf496huNQcjoZgs1RasQ1QobTiBwbtgVTkmqCDb
//        String[] accounts2 = new String[]{
//                "758KJwH9DEdrdYocVqciERzesTTPjWzkKfx3G3DrGSJL",
//                "13MY2yZ6SedFhbmu92dAK5hdhkGRGZncUwfSpxwedtyR",
//                "SysvarRecentB1ockHashes11111111111111111111",
//                "SysvarRent111111111111111111111111111111111",
//                "13MY2yZ6SedFhbmu92dAK5hdhkGRGZncUwfSpxwedtyR"
//        };
//        String base58Data2 = "6UQf8s8v7Laq63RZ";
//        testInstruction("WithdrawNonce Case 2", base58Data2, accounts2);
//
//        // Case 3: tx_hash: 3vDU6JZRwDVVVo3KwGfA3T1YzgzJrDwHGolomBkGxqKJYYDxnPps8Z64eV3HQ4QKVxDyf4yVz2X6Q5VUh4Tdj6Cy
//        String[] accounts3 = new String[]{
//                "2xNweLHLqrbx4zo1waDvgWJHgsUpPj8Y8icbAFeR4a8i",
//                "8vqZWWG6ZKqYXkJvxXkYmqNYvG8NkY1X7ea7ZLKZ6T2L",
//                "TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA",
//                "SysvarRecentB1ockHashes11111111111111111111",
//                "SysvarRent111111111111111111111111111111111"
//        };
//        String base58Data3 = "51111111111111";
//        testInstruction("WithdrawNonce Case 3", base58Data3, accounts3);
    }

    // AuthorizeNonce 测试用例
    private static void testAuthorizeNonce() {
        System.out.println("\n=== Testing AuthorizeNonce ===");

        // Case 1: tx_hash:4YAwhVntSf5aMwKGmzu7f2PiHG7QuoYwdk4TvYMKTTjTbEvQPzw4XrYH4PS74uRcMRWQY3bRZ2i4AaWvjW1PFi33
        String[] accounts1 = new String[]{
                "4iGULHsAFnn7wbpiLi5pZAhci7Rc9evfmk2biReaHwtk",
                "48wSxFkr8HJyQboRQBZUFgU7ij2H6DUM9QbvpWwD7uwQ"
        };
        String base58Data1 = "45oj4G16aDtSpUK9Db3KRTH1FSrmAcXY65FyjcFAWaD88GepP";
        testInstruction("AuthorizeNonce Case 1", base58Data1, accounts1);

//        // Case 2: tx_hash: 3LFikuXyYbwoWywKqPw3AzQKLAAJQhKgqoqhUVZpT4qNyqXrqQpVjpN8SUx5BSW8H3MuFYkyJbp5SDxdcvNuxozs
//        String[] accounts2 = new String[]{
//                "HWHvQhFmJB6gPtqJx3gjxHX1iDZhQ9WJorxwb3iTWVHi",
//                "J7nSEX8ADf3pVWhHS7qXqGYoxQqGYwKhNJfEMvFUSk7c"
//        };
//        String base58Data2 = "71111111111111";
//        testInstruction("AuthorizeNonce Case 2", base58Data2, accounts2);
//
//        // Case 3: tx_hash: 5YHEkNjVGKRDw3KJNkyxEqHFxGQYqUxm6vt7kUdtJPBXJu6mZPaGF8vZmZHGGM6Hz8Vj8tQRKUyVmdyxXt1Sjoey
//        String[] accounts3 = new String[]{
//                "2xNweLHLqrbx4zo1waDvgWJHgsUpPj8Y8icbAFeR4a8i",
//                "8vqZWWG6ZKqYXkJvxXkYmqNYvG8NkY1X7ea7ZLKZ6T2L"
//        };
//        String base58Data3 = "71111111111111";
//        testInstruction("AuthorizeNonce Case 3", base58Data3, accounts3);
    }

    // AllocateWithSeed 测试用例
    private static void testAllocateWithSeed() {
        System.out.println("\n=== Testing AllocateWithSeed ===");

        // Case 1: tx_hash: 3GX5P5g77PUo6oAmMJCmuLmoqf1kRwzgo3kt2kK1WpLJVpscLfkY5YbgcDAwTqGuKrktxzXoGUjZT5SsfXc4P8zV
        String[] accounts1 = new String[]{
                "rqjuEoKx3WteksddYnfshDXBHT1qHHEPzLdp69M2WJX",
                "ExmipM2EAJ9DeShWcGStTrWkgNH69RvMB5MooUTbCts9"
        };
        String base58Data1 = "7E6e8hLRfHc9nUvGD7MFYE4jGTbcer1kgZu91MU6RwXDV8QqX4tBzqqh7rt3oThJvD8b1mnZmQze97wdWaVvxoBC23axHFkCdHYC7EynT34jispvhzZGnr7x34Uw";
        testInstruction("AllocateWithSeed Case 1", base58Data1, accounts1);

//        // Case 2: tx_hash: 2xFB11vhYC6mwK8kPn4bwV66ZkRNPsNJyGZMPEYwuBrNZmqVBh1o5KzN8MC3aWRnUGwFUyYGvGmwxz7xUJvpa7Gy
//        String[] accounts2 = new String[]{
//                "HWHvQhFmJB6gPtqJx3gjxHX1iDZhQ9WJorxwb3iTWVHi",
//                "J7nSEX8ADf3pVWhHS7qXqGYoxQqGYwKhNJfEMvFUSk7c"
//        };
//        String base58Data2 = "91111111111111MyAllocSeed2";
//        testInstruction("AllocateWithSeed Case 2", base58Data2, accounts2);
//
//        // Case 3: tx_hash: 3vDU6JZRwDVVVo3KwGfA3T1YzgzJrDwHGolomBkGxqKJYYDxnPps8Z64eV3HQ4QKVxDyf4yVz2X6Q5VUh4Tdj6Cy
//        String[] accounts3 = new String[]{
//                "2xNweLHLqrbx4zo1waDvgWJHgsUpPj8Y8icbAFeR4a8i",
//                "8vqZWWG6ZKqYXkJvxXkYmqNYvG8NkY1X7ea7ZLKZ6T2L"
//        };
//        String base58Data3 = "91111111111111MyAllocSeed3";
//        testInstruction("AllocateWithSeed Case 3", base58Data3, accounts3);
    }

    // AssignWithSeed 测试用例
    private static void testAssignWithSeed() {
        System.out.println("\n=== Testing AssignWithSeed ===");

        // Case 1: tx_hash:
        String[] accounts1 = new String[]{
                "382oStxnxZKoavBuU15nsXDhh2oHGi2EQ4f2zfn8LL1V",
                "5ZiE3vAkrdXBgyFL7KqG3RoEGBws4CjRcXVbABDLZTgx"
        };
        String base58Data1 = "aRGRRQFzMfde4oJpLSPWxwnmJ2JHqhEudtCaBiBtxbsZBh2jjYo4Ctoxk3N6v7mf5ouVyZvQ41iFqCnCc15TFkMtZRXef9nnGnMryxBMBJgsUyVxoVK1SQUYvM3go56hXSWinunmz9w6";
        testInstruction("AssignWithSeed Case 1", base58Data1, accounts1);

//        // Case 2: tx_hash: 2ZGLGHrxZKZqNMUoWZh6rZmzxEQ4wDwNqRh8TFdJ6s9ToBvEmz7xHFWYWPHG3QVVxwH1zUvLo4JsBxo4x4JqCGPK
//        String[] accounts2 = new String[]{
//                "HWHvQhFmJB6gPtqJx3gjxHX1iDZhQ9WJorxwb3iTWVHi",
//                "J7nSEX8ADf3pVWhHS7qXqGYoxQqGYwKhNJfEMvFUSk7c"
//        };
//        String base58Data2 = "A1111111111111MyAssignSeed2";
//        testInstruction("AssignWithSeed Case 2", base58Data2, accounts2);
//
//        // Case 3: tx_hash: 3LFikuXyYbwoWywKqPw3AzQKLAAJQhKgqoqhUVZpT4qNyqXrqQpVjpN8SUx5BSW8H3MuFYkyJbp5SDxdcvNuxozs
//        String[] accounts3 = new String[]{
//                "2xNweLHLqrbx4zo1waDvgWJHgsUpPj8Y8icbAFeR4a8i",
//                "8vqZWWG6ZKqYXkJvxXkYmqNYvG8NkY1X7ea7ZLKZ6T2L"
//        };
//        String base58Data3 = "A1111111111111MyAssignSeed3";
//        testInstruction("AssignWithSeed Case 3", base58Data3, accounts3);
    }

    // UpgradeNonceAccount 测试用例
    private static void testUpgradeNonceAccount() {
        System.out.println("\n=== Testing UpgradeNonceAccount ===");

        // Case 1: tx_hash: sxrhaSTPew1Tb3ddsys3iCxpSfTGhzLXUSvFS7Yjt2hdEDGeXFxgi76PcDaqfWTmv8qRYogQ6giUBv1Tq8qV5Mp
        String[] accounts1 = new String[]{
                "DBWG3rviEHUdf4iQ3d6oCMcMzVyiFodBKLvesmsjJDhM"

        };
        String base58Data1 = "JnrP9";  // 12 in decimal
        testInstruction("UpgradeNonceAccount Case 1", base58Data1, accounts1);

//        // Case 2: tx_hash: 2ZxDm8JBCYbJ5YQYWVqNvqZgz3jBpygY7FE6HmeTEFbxqVqxgXXvKPYGrRKpvZKBdLuVtJhNvqPHeLqxrEh9tWWx
//        String[] accounts2 = new String[]{
//                "HWHvQhFmJB6gPtqJx3gjxHX1iDZhQ9WJorxwb3iTWVHi",
//                "J7nSEX8ADf3pVWhHS7qXqGYoxQqGYwKhNJfEMvFUSk7c"
//        };
//        String base58Data2 = "C";
//        testInstruction("UpgradeNonceAccount Case 2", base58Data2, accounts2);
//
//        // Case 3: tx_hash: 5fqXJZpyvkE8YvSHbR3VgZEpWxzVaFGV4TakFyCGgBhzHxVr6yzZRqtR7xo6VrBHVmuEwGPjVoXcGxp3A9S4iFhk
//        String[] accounts3 = new String[]{
//                "2xNweLHLqrbx4zo1waDvgWJHgsUpPj8Y8icbAFeR4a8i",
//                "8vqZWWG6ZKqYXkJvxXkYmqNYvG8NkY1X7ea7ZLKZ6T2L"
//        };
//        String base58Data3 = "C";
//        testInstruction("UpgradeNonceAccount Case 3", base58Data3, accounts3);
    }

}