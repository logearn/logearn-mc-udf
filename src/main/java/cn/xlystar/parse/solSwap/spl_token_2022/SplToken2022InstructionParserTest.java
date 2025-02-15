package cn.xlystar.parse.solSwap.spl_token_2022;


public class SplToken2022InstructionParserTest {


    public static void main(String[] args) {
        System.out.println("Starting SPL Token 2022 instruction parser tests...\n");
        
        // 初始化相关测试
        testInitializeOperations();
        
        // 基础代币转账操作测试
        testBasicOperations();
        
        // 基础代币操作 + 账户状态相关操作
        testBasicPlusOperation();

  
        System.out.println("\nAll tests completed!");
    }

    private static void testInitializeOperations() {
        System.out.println("\n=== Testing Initialize Operations ===");
        
        // InitializeMint
        // ✅   tx: 5PyzTwDDnwHQrSdMsqD8oPEBboouvwAUBdm7D1qBYPgH5pWZLA8HrNEpjnrseXnPwkWnN8tNF4Anxst46621hWdm
        String tx_hash1 = "5PyzTwDDnwHQrSdMsqD8oPEBboouvwAUBdm7D1qBYPgH5pWZLA8HrNEpjnrseXnPwkWnN8tNF4Anxst46621hWdm";
        String[] accounts1 = "DQLk3VyPyqUAZAT3yFcUk3kg9ottCP4vhdHCeZbkqejE, SysvarRent111111111111111111111111111111111".split(", ");
        String base58Data1 = "0000e2ae9fffb6114fd6d1bcef85f9a36d9f4af6ac0560e094d9067aa44d4cb7841101e2ae9fffb6114fd6d1bcef85f9a36d9f4af6ac0560e094d9067aa44d4cb78411";
        testInstruction("InitializeMint", base58Data1, accounts1, tx_hash1);     

        // ✅ tx: 522JAvqC9Uow1wPbz5rd8WJZQg4UmKA9XnBfdh8k845pLgQFt5LxYLcEnLRhSXz46mciKPtMzMbiS2SDZgAV2cuK
        String tx_hash2 = "522JAvqC9Uow1wPbz5rd8WJZQg4UmKA9XnBfdh8k845pLgQFt5LxYLcEnLRhSXz46mciKPtMzMbiS2SDZgAV2cuK";
        String[] accounts2 = "7HotDKvVQt1kgRNGmxhbEjbU7JAzbVXjdkaAjy6KpLvA, SysvarRent111111111111111111111111111111111".split(", ");
        String base58Data2 = "0006ff3bc1eb972b9af7c4c624f9bb20ec73088653e190627ccc51f7e7bfc7a0c2e8000000000000000000000000000000000000000000000000000000000000000000";
        testInstruction("InitializeMint", base58Data2, accounts2, tx_hash2);            
        

        // InitializeMint2
        // ✅ tx: 4r5SEKLgJ4aMPo3gZY8d76i6yuwvzFq1YnZgQpXrUPh6aQMzPCya9ZGVLYTcK8Tpz4n2y9dJnSJxgmfe3Vumd8cx
        String tx_hash3 = "4r5SEKLgJ4aMPo3gZY8d76i6yuwvzFq1YnZgQpXrUPh6aQMzPCya9ZGVLYTcK8Tpz4n2y9dJnSJxgmfe3Vumd8cx";
        String[] accounts3 = "HsMiKS4sZDGn2Hy3xZ21PnhVkAiSdska9RccvsWXnqed".split(", ");
        String base58Data3 = "1402a1bab2ffe0db3419d9e16c944a4dad2c36998abd7afb3e6abb0dab40e538db7e000000000000000000000000000000000000000000000000000000000000000000";
        testInstruction("InitializeMint2", base58Data3, accounts3, tx_hash3);           

        // ✅ tx: 2CipB4fZv5enqUbLGprRGe4icT2345WWZ73nkudMCDr2oisFqUGbiazkYGy3AMueAgUaS6qNeXy5wDzXUtygkZU8
        String tx_hash4 = "2CipB4fZv5enqUbLGprRGe4icT2345WWZ73nkudMCDr2oisFqUGbiazkYGy3AMueAgUaS6qNeXy5wDzXUtygkZU8";
        String[] accounts4 = "heG9BRkZEfBFTokyY7wjiFUKHbLjjffKNoeJMPcZHQQ".split(", ");
        String base58Data4 = "1400be1d48995a328a7d70369b161a40f45bfd5d4df76a0fa3a8561bbdea8f44a98e00";
        testInstruction("InitializeMint2", base58Data4, accounts4, tx_hash4);           

        // ✅ tx: 2STy3Kq8AJrCMQYLmkNUQPb2xDxhkmDHJPmXfd9hoLEhxCXGvTZEHDPpHCMvEkGpqnxhUXuSzdnvjZ1teWpuCS7Q
        String tx_hash5 = "2STy3Kq8AJrCMQYLmkNUQPb2xDxhkmDHJPmXfd9hoLEhxCXGvTZEHDPpHCMvEkGpqnxhUXuSzdnvjZ1teWpuCS7Q";
        String[] accounts5 = "4b7iKHb91DTyMEGNnSPXK4gBGFQTio9Yte7u93VAqrh7".split(", ");
        String base58Data5 = "14003d7aa182367d9f45a38b945403a5cb3f5c1fef6c221fe616cc35b8e3d6e41847013d7aa182367d9f45a38b945403a5cb3f5c1fef6c221fe616cc35b8e3d6e41847";
        testInstruction("InitializeMint2", base58Data5, accounts5, tx_hash5);           


        // ✅ InitializeAccount
        // tx: 5jFZEkse57KAosL4X6SpuYSWyDx1iFqyQacAU36t5Mp3224hZqPP59Xx4qvMa3yee3jp3RgPAPzgE2GTnEqkAHga
        String tx_hash6 = "5jFZEkse57KAosL4X6SpuYSWyDx1iFqyQacAU36t5Mp3224hZqPP59Xx4qvMa3yee3jp3RgPAPzgE2GTnEqkAHga";
        String[] accounts6 = "5JMHeLtYSwgYzCKJ9ZeLv1wqpL4RykjS8uEuNV8WEHvr, Bcj36ZCPXBLAb7w3DTn6tzpeXt5rZLLfdvpqXaC9SVhZ, 6XWHUrSS9JdjGStZcFf7CHZ2Fg6FH2k1kA54NSKfnhWP, SysvarRent111111111111111111111111111111111".split(", ");
        String base58Data6 = "01";
        testInstruction("InitializeAccount", base58Data6, accounts6, tx_hash6);
        // InitializeAccount2
        // tx: @warning 没有这个指令
        // String[] accounts8 = "DhkNfNLvijhJ4G7c5zCviAjCNjc9GmW9mB3Jc637XM7x, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA, 3H8XWvHhEYXBrQXdmgZvRhQdMXzWE4UYZmXqYdvYzFjE".split(", ");
        // String base58Data8 = "10";
        // testInstruction("InitializeAccount2", base58Data8, accounts8);
        // ✅ InitializeAccount3
        // tx: 31LKSjezYJ23Wtii4s3Q6NXGQv2oE1JQzGntdVRBm6iPsxFaYz6GYX2yDPyMAoLDWBB2755gXMJeByi3rMiX9qY5
        String tx_hash7 = "31LKSjezYJ23Wtii4s3Q6NXGQv2oE1JQzGntdVRBm6iPsxFaYz6GYX2yDPyMAoLDWBB2755gXMJeByi3rMiX9qY5";
        String[] accounts7 = "5mart5fwGAajHvY3oFgA33dxP9k3ebr17KYdf5pCQcUd, 2b1kV6DkPAnxd5ixfnxCpjxmKwqjjaYmCZfHsFu24GXo".split(", ");
        String base58Data7 = "1278e7af7a2469c7d1b6e0a2d9e933827716d4169a23204e29e9ba490c6a18b2f9";
        testInstruction("InitializeAccount3", base58Data7, accounts7, tx_hash7);
    }

    private static void testBasicOperations() {
        System.out.println("\n=== Testing Basic Operations ===");
        
        // ✅ Case: 
        String tx_hash1 = "fvCp2YMvtkrMsNsVQBqQXfbajzT4iHAK1bnEUp81rmuuTC5zKb2iqN2VYevjMWJGgpkhijZ9AqkQAYUwpXcPtWn";
        String[] accounts1 = "6PGN5Gpc3jCe5q17xzB2h8d216vgEj1NPfPp9ES7eveM, Ave67aActJnSxqHUYB3yejmMU1f3rxLPySNzrT2yxW8g, FukudCCa2hKbQxFAU3ZuwY3fAiNaLpkTWTBepGdY9hhz".split(", ");
        String base58Data1 = "03003ef5cafb050000";  // amount=6579000000000
        testInstruction("Transfer", base58Data1, accounts1, tx_hash1);        

        // ✅ Case
        String tx_hash2 = "qnohuqH63D7vBBP1pSLNHvYupFy6kqMxebJp7yXyux1pmFWdj1BGxuCAhsFiiejvrx5jW32JTmuY8QawzDDgyRU";
        String[] accounts2 = "5z2TrBZKmbRp543CQYdadw3uEAbQyDG6PH1Z3Du3ueRZ, 9bJYmVoTq4JUQ1PR5NMSRXjurQevM58Powhh24VEyCev, 9wuwCu4QmG4y8djN1wR5vqeM6HC5ifA17oUxG2ZvkMw".split(", ");
        String base58Data2 = "08a44fe56c00000000";  // amount=6579000000000
        testInstruction("Burn", base58Data2, accounts2, tx_hash2);     


        // ✅ Case: 
        String tx_hash3 = "3NGGGeCE1BGgBVYVdtuYRHkwYwjbr9Wgd7GeZuk9G5WxGpgTrYz2ajx7NMkVKjUQMjx3PjAVeHXsYZmMiYDS1ktW";
        String[] accounts3 = "4XGEH3dzp82ucCb3c8yTQ1VgkBF2hrsMSX15bG1XqsYX, AtUv2VVS6UdX1qjdBRHHeoa1gaE35UGBMAhrzx5WPDV8, BjNe12dowquhSvp2gvtPCNQAKuhka9EefALaFwhLGv6w".split(", ");
        String base58Data3 = "07d70f000000000000";  // amount=6579000000000
        testInstruction("MintTo", base58Data3, accounts3, tx_hash3);      
        
    }
    private static void testBasicPlusOperation() {        
        
        // SetAuthority
        String tx_hash4 = "3ShTuQxqeN6oxcuzo7nxFsspEqETM4tRtPSofJGrp7xRB8xL7Vrc5DxvpJ92Tnwj9juUXC27RhHUX13igo2ZkdWH";
        String[] accounts4 = "CbjK83rfYBmYp16kjrycYnHQUKrSfphRCYPtPEnudTtv, 3ojJf4WRpYMU4PxKfvAgEhysrtCyU6xFFoyar3YFDt3X".split(", ");
        String base58Data4 = "060000";  // amount=6579000000000
        testInstruction("SetAuthority", base58Data4, accounts4, tx_hash4); 

        String tx_hash5 = "5KRGQRupy5Dk15Y5VGDRn9H7xA3Z5GHGFb9DpYu1qxY7LxbGrLeUASPEyiHwF8EqBWf3sV1ZM6mwbsMiThgzYDRU";
        String[] accounts5 = "F5zW4yPD8jPBKsEeTqJGMngSdXhLRrV92SvJGBHgHEVG, 9wuwCu4QmG4y8djN1wR5vqeM6HC5ifA17oUxG2ZvkMw".split(", ");
        String base58Data5 = "0600000000000000000000000000000000000000000000000000000000000000000000";  // amount=6579000000000
        testInstruction("SetAuthority", base58Data5, accounts5, tx_hash5); 

        String tx_hash6 = "H84GELiqMCs4psxtTmhBuWwLNoJGrMd2YogJZRSEdJihWBuQTkgdWCdWqpntEnQVpNeNSJaSLeHbbNceHNmVWF2";
        String[] accounts6 = "9iKjvRgsJ93bpDjPaXGWS9NYY1SJevbuhZipUNbj59i8, GFsaaWHomhzSCJZKAmiC7rgD1canfHfBSmYeDKpAsWtp, GFsaaWHomhzSCJZKAmiC7rgD1canfHfBSmYeDKpAsWtp".split(", ");
        String base58Data6 = "0600014d18161594a0875dc50cdedd9d0a2eeff8bd3fb312985807a4f4263ddd7badb2";  // amount=6579000000000
        testInstruction("SetAuthority", base58Data6, accounts6, tx_hash6); 

        // FreezeAccount
        String tx_hash11 = "5oC9n3mDa9QYZwZ8TRQRCzsqSRkNzDBaE9sTrFMde9iSkSiUMHXigre7zpZtAdCcyPTFuywzLWthRNqmT7j9n986";
        String[] accounts7 = "2PJ1q2v4AjZwitzAEdUeZfjqBKa6PhD2BoPSmqXR2hki, GB5APStmEXcQyTsqLBVEwAcaNMagtSXUoAhdQVDLrWWr, BMsXzzzKy6CSHeZ8Cg7RU9pUHsxT8hKhREtJmGbnDym4".split(", ");
        String base58Data7 = "0a";  
        testInstruction("FreezeAccount", base58Data7, accounts7, tx_hash11); 
        
        // ThawAccount
        String tx_hash8 = "4pR4cBYpqDHDkc3rhjgxPWbB11Rt1q5Bri7H9jumYbfMUhMBWez7eyRgYPgjxTZNuQMdsKWYHk9ozM6cg8znR2eD";
        String[] accounts8 = "5rpJn1cxryjQoGeE1KNK2K3xPRmVyNPYr75vWMcz3Tms, o1LcF31XZ6YZ3kNRgNmDEQdTu4J3yPep12M43MePwuP, BMsXzzzKy6CSHeZ8Cg7RU9pUHsxT8hKhREtJmGbnDym4".split(", ");
        String base58Data8 = "0b"; 
        testInstruction("ThawAccount", base58Data8, accounts8, tx_hash8); 

        // CloseAccount
        String tx_hash10 = "Qnhc36qH4gnWgqRmpVRkuLaVaUdm9i7xzkJ8ZR5p9cWDSbyd7qu8VhdKyVSRfeYx6fFbYYfPux2mvtWgxMqT76z";
        String[] accounts10 = "8qAG4vQDZyoa45XChdSUQy2f4aVDzEd1Zfapk2sVQjWg, Cq3WFu7v5DFg76sQoQZ9Rt2Zn7U1jjUXpSj7A73xdw99, Bq1deJfYRwaGZL1ZCxKm9GcGYLiecR8X7F4BSxikU3Bn".split(", ");
        String base58Data10 = "09"; 
        testInstruction("CloseAccount", base58Data10, accounts10, tx_hash10); 

        // SyncNative
        String tx_hash9 = "2k8wCwperfJtpSLvrqVjvDCuyZPE5QRHLUTbwTYQtjSbTJcsDP3vhao8nfw9YWMbnweii1z3Ar79tdvLhDaJWZKz";
        String[] accounts9 = "DZW7wfPB8kDy1RHWo9HasGpBidGVBWUTtU9q3h4VSKUu".split(", ");
        String base58Data9 = "11"; 
        testInstruction("SyncNative", base58Data9, accounts9, tx_hash9); 

        // CreateNativeMint 没有该指令
        // String tx_hash8 = "4pR4cBYpqDHDkc3rhjgxPWbB11Rt1q5Bri7H9jumYbfMUhMBWez7eyRgYPgjxTZNuQMdsKWYHk9ozM6cg8znR2eD";
        // String[] accounts8 = "5rpJn1cxryjQoGeE1KNK2K3xPRmVyNPYr75vWMcz3Tms, o1LcF31XZ6YZ3kNRgNmDEQdTu4J3yPep12M43MePwuP, BMsXzzzKy6CSHeZ8Cg7RU9pUHsxT8hKhREtJmGbnDym4".split(", ");
        // String base58Data8 = "0b"; 
        // testInstruction("ThawAccount", base58Data8, accounts8, tx_hash8); 
    }       

   

    private static void testInstruction(String testName, String base58Data, String[] accounts, String tx) {
        try {
            System.out.println("\nTesting " + testName);
            System.out.println("tx: " + tx);
            System.out.println("Base58 Data: " + base58Data);
            System.out.println("Accounts:");
            for (int i = 0; i < accounts.length; i++) {
                System.out.println("  " + i + ": " + accounts[i]);
            }
//            byte[] data = Hex.decodeHex(base58Data.toCharArray());
            
//            Map<String, Object> result = new SplToken2022InstructionParser().parseInstruction(data, accounts);
//            System.out.println("✅ Parse result: " + result);
            
        } catch (Exception e) {
            System.err.println("❌ Test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 