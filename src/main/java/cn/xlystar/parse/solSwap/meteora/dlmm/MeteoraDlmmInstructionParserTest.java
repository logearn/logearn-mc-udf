package cn.xlystar.parse.solSwap.meteora.dlmm;


public class MeteoraDlmmInstructionParserTest {

    public static void main(String[] args) {
        System.out.println("开始测试 MeteoraDlmm 指令解析器...\n");
        
        // 测试相关操作
        testOperations();
     
        System.out.println("\n所有测试完成!");
    }

    private static void testOperations() {
        System.out.println("\n=== 测试代币铸造操作 ===");

        // ✅ initializeLbPair
        // String tx_hash1 = "4r6AtgzQcDjqBSN4tGaNeimV3G6dZkzQM9PyNJU2xFQZYQrsxwPTmvFavmdhnH87hg2EPmgKFBesVz4uBhP4PGs";
        // String[] accounts1 = "9rCLgAemie88mfMZVdas9np46Kzx3FqqABcusGXx91WN, LBUZKhRxPF3XUpBCjp4YzTKgLccjZhTSDM9YuVaPwxo, 74SBV4zDXxTRgv1pEMoECskKBkZHc2yGPnc7GYVepump, MEFNBXixkEbait3xn9bkm8WsJzXtVsaJEn4c8Sam21u, 4PBA3YubeBBVKMD2qthdbTphNJjKhMLXkc3zPSTT9XVq, EAiDQ7jJh7gRKe3zLcxGsRnfdU9WoxFz8ZHMwFGUvUWk, 8tHidv6B1cJ1j7NcPEdUGxLfUptfnBJcbcwMgnp3zVtr, BYQtcDyv2BoFuf5ghsYDGPA8iX5F4WquK7zCzUsDwJ63, CXWow32cBFayFvD38QMEirCnbJ3yhEXiSFArj4gKtw3k, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA, 11111111111111111111111111111111, SysvarRent111111111111111111111111111111111, D1ZN9Wj1fRSUQfCjhvnu1hqDMT7hzjzBBpi12nVniYD6, LBUZKhRxPF3XUpBCjp4YzTKgLccjZhTSDM9YuVaPwxo".split(", ");
        // String base58Data1 = "2d9aedd2dd0fa65cfaedffff0a00";
        // testInstruction("initializeLbPair", base58Data1, accounts1, tx_hash1);             
        // ✅ initializeReward
        // String tx_hash2 = "2yTnc4wSgDBbSgkAduwKaLkaZU4dFJ96fgYLQVrMjJXJHy9cAVrwYjqVrbEiat1tG7NHj7X7igNkVoScAHCfwbgx";
        // String[] accounts2 = "2QrWsSWrGvoAqkDC5XSGqjS752RWLaopqAaGrbugSxBL, FD7ydpv3Yi9SsX72aafGuzfP8TsVdBRRTtKn9tW8piUr, jtojtomepa8beP8AuQc6eXt5FriJwfFMwQx2v2f9mCL, ChSAh3XXTxpp5n2EmgSCm6vVvVPoD1L9VrK3mcQkYz7m, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA, 11111111111111111111111111111111, SysvarRent111111111111111111111111111111111, D1ZN9Wj1fRSUQfCjhvnu1hqDMT7hzjzBBpi12nVniYD6, LBUZKhRxPF3XUpBCjp4YzTKgLccjZhTSDM9YuVaPwxo".split(", ");
        // String base58Data2 = "5f87c0c4f281e644000000000000000000ea240000000000ac560c5317ad6cfa65805322c877ed618ab37f0d916cbc7b104a319b255dc68d";
        // testInstruction("initializeReward", base58Data2, accounts2, tx_hash2);    
        
        // // ❌ addLiquidity, 最新数据，都是 2023年 11月的了，链上数据和 idl 文件的接口对不上
        // String tx_hash3 = "LNefDb36JaZpugqXqw9qkcqn4DepHXNrtr7eBZyLbtvUaRUT1dbnwVRjXibm7cU3xy8AzpoUVrR2ZUGCUBQo5nc";
        // String[] accounts4 = "AwBRrmtQ2P8isSxb6D4KVQxsopsKk2DqrgtALHAwfjSE, 3WHNf55iBawfL7U1cC4Htj11KKmC8hvrrbdM31yPUr4C, LBUZKhRxPF3XUpBCjp4YzTKgLccjZhTSDM9YuVaPwxo, A5mtDFgmSC8zBE5ZaV4oUKWNeqVpZVzuqK2QKbyFMCpP, yNEbLbwD828G3JCsCxZ3QCtqZ6mjfsF6tVNP3yr6vfh, Dp7VdsZ2N6sxsrezjt1EMHkELBfaAYSVJXVR753zdYJV, CZULKXqq1fPYs8xwQGMamZm1pHH5nVnRZzmm5RLnQ33r, So11111111111111111111111111111111111111112, EPjFWdd5AufqSSqeM2qN1xzybapC8G4wEGGkZwyTDt1v, AQMsmV4kTjZqjCVaSAzdcogZFqMPhvbCucec9S6Ja2iS, CMNgaXdjgZxLMvCsM9QU7TRTWvomdrjycWe5WAJJzGa2, JCyncpDM3cKGibbq2dKJEJXRhLUupdVS5RuRqVmu77kn, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA".split(", ");
        // String base58Data4 = "b59d59438fb6344880b2e60e0000000070b3a500000000000b000000b9f3ffff00001a07baf3ffff00001a07bbf3ffff00001a07bcf3ffff00001a07bdf3ffff00001a07bef3ffff8e038e03bff3ffff1a070000c0f3ffff1a070000c1f3ffff1a070000c2f3ffff1a070000c3f3ffff1a070000";
        // testInstruction("addLiquidity", base58Data4, accounts4, tx_hash3);    

        // String tx_hash31 = "2qNW5JnaZonhRSULVDHaPk5DXX1BosFxuSNsPSmLwpw1zTzFpcMuKHmszczdqvW67PYLBWurRiXKjCwhNagaMg27";
        // String[] accounts41 = "8DXtqc9uTiKpBKvJekyqMHxhoGp2iCuyCRwxpmmPGdpn, 3WHNf55iBawfL7U1cC4Htj11KKmC8hvrrbdM31yPUr4C, LBUZKhRxPF3XUpBCjp4YzTKgLccjZhTSDM9YuVaPwxo, 8jgzAwFhrgjtBFaozNAQLUmNAUHaLsmGPeQ9JvLXUqRJ, CfnkXb9hS45AmBSArPfXzYD5z6hSXDL4En3ZD3VqmQDK, Dp7VdsZ2N6sxsrezjt1EMHkELBfaAYSVJXVR753zdYJV, CZULKXqq1fPYs8xwQGMamZm1pHH5nVnRZzmm5RLnQ33r, So11111111111111111111111111111111111111112, EPjFWdd5AufqSSqeM2qN1xzybapC8G4wEGGkZwyTDt1v, EVN8VHguPSFNNrRQphYZCJ2CvbY626f8qYvMV6w43Jjp, H5wjm3JauZ9KYFQEUTDs1sbTsNa4ZhiqyD6n98BwWZ29, JBeYA7dmBGCNgaEdtqdoUnESwKJho5YvgXVNLgo4n3MM, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA, D1ZN9Wj1fRSUQfCjhvnu1hqDMT7hzjzBBpi12nVniYD6, LBUZKhRxPF3XUpBCjp4YzTKgLccjZhTSDM9YuVaPwxo".split(", ");
        // String base58Data41 = "b59d59438fb6344880969800000000004f660800000000004500000089f4ffff000021018af4ffff000021018bf4ffff000021018cf4ffff000021018df4ffff000021018ef4ffff000021018ff4ffff0000210190f4ffff0000210191f4ffff0000210192f4ffff0000210193f4ffff0000210194f4ffff0000210195f4ffff0000210196f4ffff0000210197f4ffff0000210198f4ffff0000210199f4ffff000021019af4ffff000021019bf4ffff000021019cf4ffff000021019df4ffff000021019ef4ffff000021019ff4ffff00002101a0f4ffff00002101a1f4ffff00002101a2f4ffff00002101a3f4ffff00002101a4f4ffff00002101a5f4ffff00002101a6f4ffff00002101a7f4ffff00002101a8f4ffff00002101a9f4ffff00002101aaf4ffff00002101abf4ffffae00ae00acf4ffff21010000adf4ffff21010000aef4ffff21010000aff4ffff21010000b0f4ffff21010000b1f4ffff21010000b2f4ffff21010000b3f4ffff21010000b4f4ffff21010000b5f4ffff21010000b6f4ffff21010000b7f4ffff21010000b8f4ffff21010000b9f4ffff21010000baf4ffff21010000bbf4ffff21010000bcf4ffff21010000bdf4ffff21010000bef4ffff21010000bff4ffff21010000c0f4ffff21010000c1f4ffff21010000c2f4ffff21010000c3f4ffff21010000c4f4ffff21010000c5f4ffff21010000c6f4ffff21010000c7f4ffff21010000c8f4ffff21010000c9f4ffff21010000caf4ffff21010000cbf4ffff21010000ccf4ffff21010000cdf4ffff21010000";
        // testInstruction("addLiquidity", tx_hash31, accounts41, base58Data41);    
        
        // String tx_hash32 = "45611TPuvCny1i1pqXEteCLFFfmGLzcTfKwbG3dWyNgDWMK7am2a4FkCC9jK1qTNEEK2MTXBmdgxkvsV7jZ49iBN";
        // String[] accounts42 = "C3WfXtX46C9RgsNiz8MKS2TAGug8tgsP5vGrEmQt2Cxg, 3WHNf55iBawfL7U1cC4Htj11KKmC8hvrrbdM31yPUr4C, LBUZKhRxPF3XUpBCjp4YzTKgLccjZhTSDM9YuVaPwxo, 8kiPAa4rqMnFqYhD3zssMBdJgL3iuuKra3U6hrzzsKMQ, 8jynYuLiZYDayNSbsLWp7J547vtMU1xGVBSG4jheohF3, Dp7VdsZ2N6sxsrezjt1EMHkELBfaAYSVJXVR753zdYJV, CZULKXqq1fPYs8xwQGMamZm1pHH5nVnRZzmm5RLnQ33r, So11111111111111111111111111111111111111112, EPjFWdd5AufqSSqeM2qN1xzybapC8G4wEGGkZwyTDt1v, EVN8VHguPSFNNrRQphYZCJ2CvbY626f8qYvMV6w43Jjp, H5wjm3JauZ9KYFQEUTDs1sbTsNa4ZhiqyD6n98BwWZ29, 3PyEQp3zWCq3UxpPYdSkeGTeSFo6UaeAAiu1tY5YgV3M, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA".split(", ");
        // String base58Data42 = "b59d59438fb634480094357700000000aae6a506000000000d000000b1f4ffff0000b000b2f4ffff00006c01b3f4ffff00009f02b4f4ffff00005204b5f4ffff00006006b6f4ffff00006b08b7f4ffff0000f209b8f4ffff91054605b9f4ffff840a0000baf4ffffe6080000bbf4ffffbe060000bcf4ffff92040000bdf4ffffc5020000";
        // testInstruction("addLiquidity", tx_hash32, accounts42, base58Data42);    
        
        // String tx_hash33 = "G5gBVrXKaWWyJXtYirsg3de68iPr43DP4WcCF1SVZRH8HgYSqBqQTMcz6Ts1BfEa3F5DhuHFJsxNkoFCw25JFRX";
        // String[] accounts43 = "C3WfXtX46C9RgsNiz8MKS2TAGug8tgsP5vGrEmQt2Cxg, 3WHNf55iBawfL7U1cC4Htj11KKmC8hvrrbdM31yPUr4C, LBUZKhRxPF3XUpBCjp4YzTKgLccjZhTSDM9YuVaPwxo, 8kiPAa4rqMnFqYhD3zssMBdJgL3iuuKra3U6hrzzsKMQ, 8jynYuLiZYDayNSbsLWp7J547vtMU1xGVBSG4jheohF3, Dp7VdsZ2N6sxsrezjt1EMHkELBfaAYSVJXVR753zdYJV, CZULKXqq1fPYs8xwQGMamZm1pHH5nVnRZzmm5RLnQ33r, So11111111111111111111111111111111111111112, EPjFWdd5AufqSSqeM2qN1xzybapC8G4wEGGkZwyTDt1v, EVN8VHguPSFNNrRQphYZCJ2CvbY626f8qYvMV6w43Jjp, H5wjm3JauZ9KYFQEUTDs1sbTsNa4ZhiqyD6n98BwWZ29, 3PyEQp3zWCq3UxpPYdSkeGTeSFo6UaeAAiu1tY5YgV3M, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA".split(", ");
        // String base58Data43 = "b59d59438fb6344800e40b540200000003e0f30c000000001100000084f4ffff0000020685f4ffff0000020686f4ffff0000020687f4ffff0000020688f4ffff0000020689f4ffff000002068af4ffffe00104038bf4ffffb80300008cf4ffffb80300008df4ffffb80300008ef4ffffb80300008ff4ffffb803000090f4ffffb803000091f4ffffb803000092f4ffffb803000093f4ffffb803000094f4ffffb8030000";
        // testInstruction("addLiquidity", tx_hash33, accounts43, base58Data43);            
        
        // ✅ addLiquidityByWeight
        // String tx_hash5 = "ftPt5ckjBbSCF7ZGhj7hiKBshdtE16qq9oRTP3KjDaPTJ3n2QmZx2RU9rXi7oYoU5M1fk4hCvXRXAyjY53qQtxG";
        // String[] accounts5 = "9KP8stZfU2BzEc4S7uevaMF5vVdVBEzkwASbRXArdv6t, 3eDLxhhkW95xpaBMdGuNTCaP8T1QCPYT3jbM9zZgV5qZ, LBUZKhRxPF3XUpBCjp4YzTKgLccjZhTSDM9YuVaPwxo, FkHKg9Nboy7xj2f2shLwW9VtJhZgh28bAaboxa8v5uDa, 5uFE5pEMZw9E7ANT1YBahPyjnzvJQc7Y6CXnm8q8M9xb, HVQNZSesja71i6RvxbuPRYMa6zi15QrRbhKdsYeLgzkT, 4JnyEGSBUZfksg9XjEoN9TkNsiPQgp1wwaZ1NoH9fmaP, 27G8MtK7VtTcCHkpASjSDdkWWYfoqT6ggEuKidVJidD4, J1toso1uCk3RLmjorhTtrVwY9HJ7X8V9yYac6Y7kGCPn, Gom4HbXJ3bBatCpHJgAsPTPYSzD7c7nRwQas83KdYkPo, 3M6YF4VEaqD43B8nG219yG2Kp37rLpg1MHEvRGeqobg8, 2f2mqEuyMW5x9XG5B1ohZi9MCg5tZRmxeXtSQk2eUrru, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA, D1ZN9Wj1fRSUQfCjhvnu1hqDMT7hzjzBBpi12nVniYD6, LBUZKhRxPF3XUpBCjp4YzTKgLccjZhTSDM9YuVaPwxo".split(", ");
        // String base58Data5 = "1c8cee63e7a2159563cc4500000000007936a600000000004b020000ffffff7f340000003502000001003602000001003702000001003802000001003902000001003a02000001003b02000001003c02000001003d02000001003e02000001003f02000001004002000001004102000001004202000001004302000001004402000001004502000001004602000001004702000001004802000001004902000001004a02000001004b02000001004c02000001004d02000001004e02000001004f02000001005002000001005102000001005202000001005302000001005402000001005502000001005602000001005702000001005802000001005902000001005a02000001005b02000001005c02000001005d02000001005e02000001005f0200000100600200000100610200000100620200000100630200000100640200000100650200000100660200000100670200000100680200000100";
        // testInstruction("addLiquidityByWeight", base58Data5, accounts5, tx_hash5);    
        
        // ❌  addLiquidityOneSide
        // String tx_hash6 = "4sas4qgVkb3ypdEJgjTESnEeKSfmXc1qtbGzz4Seh6u8ftbmBWrZKDVpHCV2uDTigJojGZ2UcZjPfYktFReostvF";
        // String[] accounts6 = "AcFgcXGgHQwX2Vyc5KFeF8sJzdDHw117rVCgUSSk4YLg, Bsq1y5hbHU8Gp3XX1TaTgCv9hPHRvpNunfzps1u9PSkB, LBUZKhRxPF3XUpBCjp4YzTKgLccjZhTSDM9YuVaPwxo, 46js4GFusB3wbPGtRBFMrBJrSQGAzKzVWLscUZDDbocW, 3KMx4aDxAHb1J3U6dMSdQ3q4T2y5UGtSBHb91VN64y3a, So11111111111111111111111111111111111111112, BvGKnDvqnNoCKXTCCU8iCxMAaY7qZt4FYtpm2kMTQXvV, bq5xfcDnL2JzzYZaGChYATZztxDi21Qbs39wR7DD8Pm, F71X8qpepBRQcZJxHjoupyUsW9kXu62RoJEpmfDwxFp5, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA, D1ZN9Wj1fRSUQfCjhvnu1hqDMT7hzjzBBpi12nVniYD6, LBUZKhRxPF3XUpBCjp4YzTKgLccjZhTSDM9YuVaPwxo".split(", ");
        // String base58Data6 = "5e9b6797465fdca500943577000000006dffffff030000004500000028ffffff8e0529ffffff81052affffff73052bffffff66052cffffff59052dffffff4c052effffff3f052fffffff2b0530ffffff1e0531ffffff110532ffffff040533fffffff70434ffffffea0435ffffffdd0436ffffffd00437ffffffbc0438ffffffaf0439ffffffa2043affffff95043bffffff87043cffffff7a043dffffff6d043effffff60043fffffff4c0440ffffff3f0441ffffff320442ffffff250443ffffff180444ffffff0b0445fffffffe0346ffffffea0347ffffffdd0348ffffffd00349ffffffc3034affffffb6034bffffffa9034cffffff9c034dffffff8e034effffff7b034fffffff6e0350ffffff610351ffffff530352ffffff460353ffffff390354ffffff2c0355ffffff180356ffffff0b0357fffffffe0258fffffff10259ffffffe4025affffffd7025bffffffca025cffffffbd025dffffffa9025effffff9c025fffffff8f0260ffffff820261ffffff750262ffffff680263ffffff5a0264ffffff4d0265ffffff3a0266ffffff2d0267ffffff1f0268ffffff120269ffffff05026afffffff8016bffffffeb016cffffffd701";
        // testInstruction("addLiquidityOneSide", base58Data6, accounts6, tx_hash6);    
        
        // // ✅ initializePosition
        // String tx_hash7 = "5tAEaiKrX61519TnEe62zfhWWeRrE8R2c1gwaB8M5NHpddc1fgEpomroHr7DaKK41azTwwJZETJ87npsAGS9qwsv";
        // String[] accounts7 = "BUf33gn6V4Eqqr6W8o9b3PEoSiVGjd9Yp3vc1cLsXXrv, 5fCPAhYwbd2E8cZoHkm4eguDkEGRNtFkRRCRGzJqRHHn, 8SHWfsb4azTpi6k7KhJJiFQ1m8ojwC1ByRAkSnq2t81b, BUf33gn6V4Eqqr6W8o9b3PEoSiVGjd9Yp3vc1cLsXXrv, 11111111111111111111111111111111, SysvarRent111111111111111111111111111111111, D1ZN9Wj1fRSUQfCjhvnu1hqDMT7hzjzBBpi12nVniYD6, LBUZKhRxPF3XUpBCjp4YzTKgLccjZhTSDM9YuVaPwxo".split(", ");
        // String base58Data7 = "dbc0ea47bebf66500bffffff31000000";
        // testInstruction("initializePosition", base58Data7, accounts7, tx_hash7);

        // ✅  swap
        String tx_hash8 = "4FqRM4PH7UkJLAaNS3rf2aQhiChEqygvFDWVthue1gybzPfwuHprZHZsK9gwDbDZEL9Brv3YyBhghASQ4fkSnxSd";
        String[] accounts8 = "HToiT8XK8GHgAT4N3oGXadc7opdApPwsbCL9tFRYa3Rg, LBUZKhRxPF3XUpBCjp4YzTKgLccjZhTSDM9YuVaPwxo, 5tNzVLXoHM4TJUhGb6WpW2dw46swNHUsanu2CWaD3AwP, HCDehQz9ta8WD7qBawGneTzqRLybBSuyjydmFVhg1FBf, 2p29nqD7DN1PczBMmgrFdtYKTfv6rJ7H3yMut4eu7nYT, 3saCCUgPAZFXvZXFyFi8ce8LYmSx7pbi7gGEWfi4ptyF, Es9vMFrzaCERmJfrF4H2FYD4KCoNkY11McCe8BenwNYB, So11111111111111111111111111111111111111112, 64ydAvRZ5zwoH9diqPc2D6dtGVN72Zna7kJnjQyoZfrS, LBUZKhRxPF3XUpBCjp4YzTKgLccjZhTSDM9YuVaPwxo, HFqp6ErWHY6Uzhj8rFyjYuDya2mXUpYEk8VW75K9PSiY, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA, D1ZN9Wj1fRSUQfCjhvnu1hqDMT7hzjzBBpi12nVniYD6, LBUZKhRxPF3XUpBCjp4YzTKgLccjZhTSDM9YuVaPwxo, FDRh3H8uJbE6rAmuZMjfQX5v6gvbtd9mANsmVv6nEDou, 97f3GBhd7puqfCnBhCzKxYN43mxZBfLwXViiLDeYH8N9, 7jrjkBvo7KtsAmciaTxjYdoKUd6y1kWytXofKAvac1C".split(", ");
        String base58Data8 = "f8c69e91e17587c800879303000000000000000000000000";
        testInstruction("swap", base58Data8, accounts8, tx_hash8);
 
        //  ✅ fundReward
        // String tx_hash10 = "Zf3H77TXqu4vYKv33vWKZ4Rw4kJTciMAYnkPbp94EStinKUpcftyM41Z79BrR9fTtaWM6mbsv4DkifgxhuQ1oU3";
        // String[] accounts10 = "2QrWsSWrGvoAqkDC5XSGqjS752RWLaopqAaGrbugSxBL, FD7ydpv3Yi9SsX72aafGuzfP8TsVdBRRTtKn9tW8piUr, jtojtomepa8beP8AuQc6eXt5FriJwfFMwQx2v2f9mCL, 4pTdZ2wJzv86JsQAGL3tkD8eufKGjcc2f2uaTjLiV76Q, CbjE5LrBn3awhs9vrM3YSPEwqhxyuyi76Y4M9S5mgoBz, FfJbTaJSsfnf8ePEckL2FGW9pbL3mmccZVpmjyXWhHvu, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA, D1ZN9Wj1fRSUQfCjhvnu1hqDMT7hzjzBBpi12nVniYD6, LBUZKhRxPF3XUpBCjp4YzTKgLccjZhTSDM9YuVaPwxo".split(", ");
        // String base58Data10 = "bc32f9a55d97263f0000000000000000204e00000000000001";
        // testInstruction("fundReward", base58Data10, accounts10, tx_hash10);

        // ❌ updateRewardFunder due 没数据
        // String tx_hash11 = "";
        // String[] accounts11 = "".split(", ");
        // String base58Data11 = "";
        // testInstruction("updateRewardFunder", base58Data11, accounts11, tx_hash11);

        // // ❌ updateRewardDuration，due 没数据
        // String tx_hash12 = "";
        // String[] accounts12 = "".split(", ");
        // String base58Data12 = "";
        // testInstruction("updateRewardDuration", base58Data12, accounts12, tx_hash12);

        // // ✅ claimReward
        // String tx_hash13 = "3ddroJFx9CkrqjhTKRSRXABQ2qwEgKjKcKk3Ze9RTdXtuuMazRRuhn8BRQB7Gptx1exGjfEtVfe2wj4ucksDyQ4T";
        // String[] accounts13 = "4W5Fm2U9JHvXEExsa9C4smPkRp5coVe3RVBJVVAAvKVY, GRYaiP43hUh2X1oXKbddjtTGmMnf2pfZdsEMvDUn3eif, FwZrNcXsMiJVpqST6n6NvqrWp9woG3mgvismqAY1otJw, FVARB5m6hVHR6orstKtThD7Svsja2vop34kDFBHkAj1S, kmQzXPDdXd1n4pMTU28HKyDdHP1eZkRiYhfbw7hq6VC, GQpCQ9PkcGfy8CB1PsBbd2yWtqZMVmrFeiS8bemVXa1x, rndrizKT3MK1iimdxRdWabcF7Zg7AR5T4nud4EkHBof, 7eadco8QLydim98x98Xz9rpTRQL5ryBTrcigdQb6tQgs, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA, D1ZN9Wj1fRSUQfCjhvnu1hqDMT7hzjzBBpi12nVniYD6, LBUZKhRxPF3XUpBCjp4YzTKgLccjZhTSDM9YuVaPwxo".split(", ");
        // String base58Data13 = "955fb5f25e5a9ea20000000000000000";
        // testInstruction("claimReward", base58Data13, accounts13, tx_hash13);

        // // // ✅ claimFee
        // String tx_hash14 = "5HjBARYNoWkyRTT1vHyMDTJyiRXDKcCM8qmgafyGBJNqsHkDcaQF7Ya7ebSmStF7qKFauDedDPJ2bFEJjig4gWJk";
        // String[] accounts14 = "BVRbyLjjfSBcoyiYFuxbgKYnWuiFaF9CSXEa5vdSZ9Hh, Dy53zyb7f9qnq16FLPZXWG3wCPFURJ9eprvnUKurpJ9b, GkhKtqtAbC3hYWCzLzSEfCFuj1z1viv2q1n567weypsC, 8WNH83M4EduPoiWq24tfPceaoofhwgLAe4YeL2y8WDMx, iCckEsUNNZgNAVHe7EDAc7KPB3CvYTLBXm4fdtGYmEK, FMzVsENjscefpAtUJYBUTeJAYaKNfFQBHjTZE1AQRFYY, 7du3jFJK4rhf9JnZSQmhr6qPkgdQyJ88528qyxpYPPtL, DFWTPgKVxEWNFUNZMQCaHwCn9tDbvMnu5ETmfVRuE9xd, GXfhve1xkeiA2qSaCkNaEMhdfzpn4aPrNR1SyMHU4q9S, So11111111111111111111111111111111111111112, EPjFWdd5AufqSSqeM2qN1xzybapC8G4wEGGkZwyTDt1v, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA, D1ZN9Wj1fRSUQfCjhvnu1hqDMT7hzjzBBpi12nVniYD6, LBUZKhRxPF3XUpBCjp4YzTKgLccjZhTSDM9YuVaPwxo".split(", ");
        // String base58Data14 = "a9204f8988e84689";
        // testInstruction("claimFee", base58Data14, accounts14, tx_hash14);

        // // ✅ closePosition
        // String tx_hash15 = "4e4JowKHtUtvxjG3Krbtv4437vpcMHAAboPH79ufwpDFQaq783majwPhrdApnaQuMGQda7kcQAMbSsyGNff77P4q";
        // String[] accounts15 = "9vWLbSLZBFWLNZDMVJSoDSY8VL8FMevxuL1jfnLXXLZt, 4bYUZ5api1sdey97NuyE5Uv1YU6LKpbCGdfvR3xCzxDx, 263AaFRtgMFE91hfbibjjSJ49ivCod9hTmAAWwt4eNvs, HgMeqE3E2xh9AjgZpFQkuu6G7syDf46ABJZPi1nFYJ7J, 7UZsaE8tKVCDWX6X42hug6shYXbmkdUU8PJWwtSj5PDJ, 7UZsaE8tKVCDWX6X42hug6shYXbmkdUU8PJWwtSj5PDJ, D1ZN9Wj1fRSUQfCjhvnu1hqDMT7hzjzBBpi12nVniYD6, LBUZKhRxPF3XUpBCjp4YzTKgLccjZhTSDM9YuVaPwxo".split(", ");
        // String base58Data15 = "7b86510031446262";
        // testInstruction("closePosition", base58Data15, accounts15, tx_hash15);

        // // ❌ removeLiquidity，因为 BinLiquidityReduction 这个 data 参数解析失败, 根据 data 的可选性，提供 3个 case
        // String tx_hash16 = "4PebCimdeAca95s29VwBPqNKtBzdEy1oxKQchWHG9E6zFvnuwAZmdPfzEwMFGbkYLpQCBkzZ1F1GZwrutTpeBXMA";
        // String[] accounts16 = "4jUupWCTgKLQ4mYLUfJ5NPvBn9ebLyFM7MgjUudd1oTi, 2QrWsSWrGvoAqkDC5XSGqjS752RWLaopqAaGrbugSxBL, LBUZKhRxPF3XUpBCjp4YzTKgLccjZhTSDM9YuVaPwxo, HJ9Yw2EY9km3ATwfWZDvXVDfKzwH8zP7pTKXd8aazfgE, HCsRMGAZTkfqapgqXfaoUDy7V6VW6UHZmgWAhnyHA8Ae, 594567HZxxmCNxLshew2svWndJHwjkwfMnX71q65fY17, GZmdfPRXR7Yu7kARif23yB4ycQrW4BDP2hA5z2SvP3tR, JUPyiwrYJFskUPiHa7hkeR8VUtAeFoSYbKedZNsDvCN, J1toso1uCk3RLmjorhTtrVwY9HJ7X8V9yYac6Y7kGCPn, CgERVXwz1Gmc8mjjoctquNgoJyAnYt7WQhf3PY6CFuoi, FfJbTaJSsfnf8ePEckL2FGW9pbL3mmccZVpmjyXWhHvu, 4EqEZX6e4DRD6DCW15Ujugc6tZDb9MUd6pbEGZ789qCy, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA, D1ZN9Wj1fRSUQfCjhvnu1hqDMT7hzjzBBpi12nVniYD6, LBUZKhRxPF3XUpBCjp4YzTKgLccjZhTSDM9YuVaPwxo".split(", ");
        // String base58Data16 = "5055d14818ceb16c330000003700000003003800000003003900000003003a00000003003b00000003003c00000003003d00000003003e00000003003f00000003004000000003004100000003004200000003004300000003004400000003004500000003004600000003004700000003004800000003004900000003004a00000003004b00000003004c00000003004d00000003004e00000003004f00000003005000000003005100000003005200000003005300000003005400000003005500000003005600000003005700000003005800000003005900000003005a00000003005b00000003005c00000003005d00000003005e00000003005f0000000300600000000300610000000300620000000300630000000300640000000300650000000300660000000300670000000300680000000300690000000300";
        // testInstruction("removeLiquidity", base58Data16, accounts16, tx_hash16);

        // String tx_hash17 = "4BNc9t4BL1Zczyn4yXPo69ToMq2ojg7hRcmrnHEUom3rF1k7c7W2t9PCcgq1Cf8TwVHw5Y4pAG24DHeuz1QTK88X";
        // String[] accounts17 = "7U1iHcgngHchoPpL6WiDZsxEXFKNodAMNpbnM4DkA5yq, BoeMUkCLHchTD31HdXsbDExuZZfcUppSLpYtV3LZTH6U, LBUZKhRxPF3XUpBCjp4YzTKgLccjZhTSDM9YuVaPwxo, 6MZw9Y8FiGGjnL4DVXS812w9t9p5B9tK5xcrCWu6p2Ev, H1HwdDkL5qHM8AZR9tBWYZQDTt9wwigKqCdF4KWuX7zP, 93d6ukn24o1xMcMDip2SACKG8GbvhGUZim1e3ZEcQVm2, CodroyzrRNvc5kHRoAQYjpVSr1jA9fLcUWVFouiuWGsD, J1toso1uCk3RLmjorhTtrVwY9HJ7X8V9yYac6Y7kGCPn, So11111111111111111111111111111111111111112, 94Uy7tT83239SrC9unLBNDAquRFp9MgkJfBg9qdxtZCS, 41T5e3uw9MdVPFRBi85iC7aW6KuA8Ff7LoQzoeqWp7jW, HdZCvCH4qwUqfy5YukMyyy5gYDhtmMWK7GvqEbLVsSWj, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA, D1ZN9Wj1fRSUQfCjhvnu1hqDMT7hzjzBBpi12nVniYD6, LBUZKhRxPF3XUpBCjp4YzTKgLccjZhTSDM9YuVaPwxo".split(", ");
        // String base58Data17 = "5055d14818ceb16c17000000e40500000100e50500000100e60500000100e70500000100e80500000100e90500000100ea0500000100eb0500000100ec0500000100ed0500000100ee0500000100ef0500000100f00500000100f10500000100f20500000100f30500000100f40500000100f50500000100f60500000100f70500000100f80500000100f90500000100fa0500000100";
        // testInstruction("removeLiquidity", base58Data17, accounts17, tx_hash17);
        
        // String tx_hash19 = "57vRTghvmgYRVui27xXwNFU9kaTQTLChUSPbKJumDTkvCBPDonEVkdFtVgRGH6gaYc35uHkFuqB9w8Pph8Unn64g";
        // String[] accounts19 = "2fRugYNMye1Yrbo4wnQZfWckGVprNLpsdrKNSqfbUk5r, WimP21rGWJ19os6i5gPAxWEKCa9n3s63jrnnagSCxUv, LBUZKhRxPF3XUpBCjp4YzTKgLccjZhTSDM9YuVaPwxo, FaLrErWwXunwunuMgPGemxhKNUtLcb5siPvJfz4SAc8e, HB7wgqzT7Mgdt7SMTALSHmFU11y1yN7Krrx6CZv5Ytwu, CvLiDzSjUErqrT7czNCNgZsUsHkBaRsj2ARwW2Kowfo2, 9McWn5bfLRGtZyejqp8jRaUDwb1pU3TmFmFUVBJoCqEp, M3M3pSFptfpZYnWNUgAbyWzKKgPo5d1eWmX6tbiSF2K, So11111111111111111111111111111111111111112, D81J7Wtsd3njztp3Ux9Q9HSDNVHR5G9psvWexGMPautJ, 4vpH1QcQ8h4vmoEBsTARNsx8mKWDfyM9pjZ3n2GJwAp1, HvKfyhBkm1e8tWUPwkPjC8oUGSnwKtyDNhbfgfbTQupw, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA, D1ZN9Wj1fRSUQfCjhvnu1hqDMT7hzjzBBpi12nVniYD6, LBUZKhRxPF3XUpBCjp4YzTKgLccjZhTSDM9YuVaPwxo".split(", ");
        // String base58Data19 = "5055d14818ceb16c0e00000030ffffff4a0031ffffff4a0032ffffff4a0033ffffff4a0034ffffff4a0035ffffff4a0036ffffff4a0037ffffff4a0038ffffff4a0039ffffff4a003affffff4a003bffffff4a003cffffff4a003dffffff4a00";
        // testInstruction("removeLiquidity", base58Data19, accounts19, tx_hash19);        
        
        // ❌ removeLiquidityByRange， due 没数据
        // String tx_hash17 = "2z2fuhvYszbgGpj8NtGmci2K1AKtmsvW417CqQsaUfnazGV73DQ8XWvGxd9zgZrooYcAZU4HA2wQkeCNz9ct3Us1";
        // String[] accounts17 = "51V3VBfMwkNbUMSHDzKC882LuYdR8Hgi3fNtYJyxz8xQ, 36Eru7v11oU5Pfrojyn5oY3nETA1a1iqsw2WUu6afkM9".split(", ");
        // String base58Data17 = "50256d88528759f10000000000000000010088526a740000000001060109000000";
        // testInstruction("removeLiquidityByRange", base58Data17, accounts17, tx_hash17);
    }


    private static void testInstruction(String testName, String base58Data, String[] accounts, String tx) {
        try {
            System.out.println("\n测试 " + testName);
            System.out.println("\nTX " + tx);
            System.out.println("Base58 数据: " + base58Data);
            System.out.println("账户列表:");
            for (int i = 0; i < accounts.length; i++) {
                System.out.println("  " + i + ": " + accounts[i]);
            }
            
//            byte[] data = Hex.decodeHex(base58Data.toCharArray());
//            MeteoraDlmmInstructionParser parser = new MeteoraDlmmInstructionParser();
//            Map<String, Object> result = parser.parseInstruction(data, accounts);
//            System.out.println("✅ 解析结果: " + result);
            
        } catch (Exception e) {
            System.err.println("❌ 测试失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 