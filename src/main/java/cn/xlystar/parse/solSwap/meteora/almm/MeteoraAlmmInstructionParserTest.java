package cn.xlystar.parse.solSwap.meteora.almm;

import org.apache.commons.codec.binary.Hex;
import org.bitcoinj.core.Base58;

import cn.xlystar.parse.solSwap.meteora.dlmm.MeteoraDlmmInstructionParser;
import cn.xlystar.parse.solSwap.moonshot.MoonshotInstructionParser;

import java.util.Map;

public class MeteoraAlmmInstructionParserTest {

    public static void main(String[] args) {
        System.out.println("开始测试 Meteora 指令解析器...\n");
        
        // 测试代币铸造相关操作
        testTokenMintOperations();
     
        System.out.println("\n所有测试完成!");
    }

    private static void testTokenMintOperations() {
        System.out.println("\n=== 测试代币铸造操作 ===");

        // // ✅ initializePermissionedPool
        // String tx_hash1 = "2dxsYv3MkJLKWRkpeJSoXepVs7j7ZWWBWJDiwfBqDZqSvtaLrFwGAyeM8idTjaW9pgxe9VzPWfRq8AFj2b54rBL1";
        // String[] accounts1 = "7AtUeAW4TKPEXkR41bawBnwyemKXL4pCPrWP5tXcPMSA, Dcr6rNp8vLLyorSJXVNAPZ7jkDPtthbA4jLy9vTHSJy3, So11111111111111111111111111111111111111112, edge86g9cVz87xcpKpy3J77vbp4wYd9idEV562CCntt, FERjPVNEa7Udq8CEv68h6tPL46Tq7ieE49HrE2wea3XT, 6y93C5iNFAqqpamYWxJM4cS5kj9pVSBmT16Kq2Lx54fm, FZN7QZ8ZUUAxMPfxYEYkH3cXUASzH8EqA6B4tyCL8f1j, 7gdDXCTHQA6LGbPsiUsKgSmaYRL198i9fVPuHnvcqGfs, 3N1EAP7FgRVBapCTx2oTSaRtghmDGzpgPcVjRxi9WSuq, 4bbEqq2D1yxxsfT81pA1Kb3AqfrnncTfdzj6JEhSKYhz, Her31Y6STaGw7Lv3X1K4ntTqi9HpDW1GZE9WStPZDA1X, AexQgo67APEHRLcEqxk1cKq1yNqa2Zdn1vKZc6kJbDwZ, AHqRFdSpRnsYdLeKDGXxw2RcGLR14emx1BV1NTfQUiPB, ANA5Kp5JX3QwFgDqzDx5AHxWTY7s1Hb34LX7SBefwzpZ, Dy1NwpzBU39iinHYakkGQ7kx7k1jMDjE8xiQf2z1osZn, 5unTfT2kssBuNvHPY6LbJfJpLqEcdMxGYLWHwShaeTLi, 11111111111111111111111111111111, SysvarRent111111111111111111111111111111111, HujZrMvJDuWvLwcquAVPSbetzsPFLsQgkHUBKXpPhvTV, metaqbxxUerdq28cj1RbAWkYQm3ybzjb6a8bt518x1s, 24Uqj9JCLxUeoC3hGfh5W3s9FM9uCHDS2SG3LYwBpyTi, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA, ATokenGPvbdGVxr1b2hvZbsiqW5xWH25efTNsLJA8knL, 11111111111111111111111111111111, edgejNWAqkePLpi5sHRxT9vHi7u3kSHP9cocABPKiWZ".split(", ");
        // String base58Data1 = "4d55b29d3230d47e01e803000000000000010000000000000001000000000000000900000000000000000000000000000000030000000000000000";
        // testInstruction("initializePermissionedPool", base58Data1, accounts1, tx_hash1);             

        // // ❌ 🐶 initializePermissionedPool，不知道为啥跑不通
        // / 因为类型定义里面，定义了字段,也没有告知是可选，但是真实数据没有 data,
        // String tx_hash2 = "4eNaBpE15YhCVNi4rNLaY6Wkvu2guMVijFk8rHs9R6ageG2cBUupWnBBy3eBrvQ9em8BBzgobUGRgv6GpXh1kWAz";
        // String[] accounts2 = "BuPUTaUBe8z9xpoK2UPnNro9GKHfEDk96DSvsx5AuaQj, CpJr9KRWzG245ftRTERv8yPw6CZspeLMojh6nJS8nFBb, BANXbTpN8U2cU41FjPxe2Ti37PiT5cCxLUKDQZuJeMMR, So11111111111111111111111111111111111111112, dvJmn8EwweCwqaMbH2pATb1tiHUrarEJCo8uRXPXEA7, FERjPVNEa7Udq8CEv68h6tPL46Tq7ieE49HrE2wea3XT, 8f7eobavK4oFmft1wmQ6HcT3Y9N6V3d52LdZjrT1Z6Ve, FZN7QZ8ZUUAxMPfxYEYkH3cXUASzH8EqA6B4tyCL8f1j, 67FHVkxDe1iP9UpFnvipe14VCr1Gmhj7AyiWD3263yMM, AkfEqmXCGxwHci82nRo6MYVJ69uAwShLCzsYTy9Pmk2p, 4b8EKmWjPFJqRcGHSEBVWF8doZv92GwgH3YrbuxZnJDR, Her31Y6STaGw7Lv3X1K4ntTqi9HpDW1GZE9WStPZDA1X, 2t3ieRVcNzjZRT5araPvhk7vjqSdVSMg33S4VxtVdcVU, 2Z2yaEjBE8VCaUxrS5Kan5HBwWJzSwLaXA8owScr6Wqt, D4PyD5xGAQY9AhxYK3FtYV5V6RQTk6NZ5KBrK7f8mGGC, 5unTfT2kssBuNvHPY6LbJfJpLqEcdMxGYLWHwShaeTLi, 11111111111111111111111111111111, SysvarRent111111111111111111111111111111111, 2m8Bj4EX2DhhpDrLwePG1yfyd4dXYx7spffzy2tCNACm, metaqbxxUerdq28cj1RbAWkYQm3ybzjb6a8bt518x1s, 24Uqj9JCLxUeoC3hGfh5W3s9FM9uCHDS2SG3LYwBpyTi, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA, ATokenGPvbdGVxr1b2hvZbsiqW5xWH25efTNsLJA8knL, 11111111111111111111111111111111".split(", ");
        // String base58Data2 = "4d55b29d3230d47e00";
        // testInstruction("initializePermissionedPool", base58Data2, accounts2, tx_hash2);    
        
        // // ❌ 🐶 initializePermissionlessPoolWithFeeTier
        // 因为类型定义里面，定义了字段,也没有告知是可选，但是真实数据没有 data,
        // String tx_hash3 = "w7DTq2tBrQbNz9ZLa8Z4j6w6cjJGvcp7P4ZjZ31kg3VqdunVNcLyQ94Bjtw8Z7iVD5gecvN95QaVsk4TnyLR3RU";
        // String[] accounts4 = "DE3AiEbZAfrzrLxY7ZbwbGvoYkf4FiKPmZJMDcWwCFqa, CJC8xf8eBs1R62YwvkrhA8E8Mj5ZciKu5USdH15k4QhF, GT7ksoBf5XpceDEitBNbay7U13zqwbh6HtDiMpU9pump, So11111111111111111111111111111111111111112, Bmm8oRgRpQnKTWSjcpq7aw8APbSh8daq6tBjc5J7VwYm, FERjPVNEa7Udq8CEv68h6tPL46Tq7ieE49HrE2wea3XT, 43g9X1tTmyfXuUi2wsF5yjKYhAjEQZAib3MidhBbJcwN, HZeLxbZ9uHtSpwZC3LBr4Nubd14iHwz7bRSghRZf5VCG, FEYsdkrrkzs9MVvwbGQaMifPbtuopKpowbYiiBzpmxkZ, FZN7QZ8ZUUAxMPfxYEYkH3cXUASzH8EqA6B4tyCL8f1j, H1rGzR6KTpx1r5Tj5zHwLziThoEjobcQ2P8EieLnUoA4, 4J6FyxUVNdTW6YSCipzRoz5fHyDcG6UVLegTgziCEL28, 896qe5K3K2aZxB4HZ5NkUCecjtfpDVM2krTst5Gj2Nvb, 8fVmyyeRM9bS9E1c36eJxxCCwpuHCJm5AtNYq2dyaU3W, 2rkY928Ud5SPpZwvgNjsxFDWxbbhj1ogvZxQhe1jXh9z, 8AwumPAjfzWwUY6sN8pqjkPZRffvYTwEeDDf7x9kJXe7, DnaeCcg1vdrFzG3EXuiEMrKXeMRh4kk9czATPmo7x5eG, G9Q7EDe8YpJZERhHrRBYLBk8YUU8afGUs5QpRugUojoN, 6WaLrrRfReGKBYUSkmx2K6AuT21ida4j8at2SUiZdXu8, SysvarRent111111111111111111111111111111111, 5iayFoKxGixEqnMbqUmcos52TyneSPfuWHhKaoNEbTQZ, metaqbxxUerdq28cj1RbAWkYQm3ybzjb6a8bt518x1s, 24Uqj9JCLxUeoC3hGfh5W3s9FM9uCHDS2SG3LYwBpyTi, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA, ATokenGPvbdGVxr1b2hvZbsiqW5xWH25efTNsLJA8knL, 11111111111111111111111111111111".split(", ");
        // String base58Data4 = "06874493e552a971005802000000000000b2a3cc4b0000000009c3070000000000";
        // testInstruction("initializePermissionlessPoolWithFeeTier", base58Data4, accounts4, tx_hash3);    
      
        // // ✅ bootstrapLiquidity
        // String tx_hash7 = "4M1bc9DeK2MXCZonMEs5qBrYoZqCvc6ZteQ3dhrZ2R5unDvn7erdzaKme7328EPRhV8Zm9o94FgRjMao4827RX4a";
        // String[] accounts7 = "3cmmm1gGkR3Ympvc2PxV2hbVZYYyfUi1Pet6Lk3Sbktc, GH4KMB6u12CdTG9kJ6Z4LxbzCeYznVfEa3LNGS5dUb9U, 7kiisGMCSV4XKDtoV6pPJpRRnSyqRxjmpjgNqTCTdTFD, 97hXPZrZHY1LoKYL2R7Mjje4M1vbHVJ5fF7xVfvS3Aut, B4EDo38Ph2CQf8HG4qkVHCt5c4Yx5QaX5Z6vzwPnwaqQ, Er3WYpvPi83Zn1Harr5zD6tHXiqYpobEiqMzvycxruiD, FERjPVNEa7Udq8CEv68h6tPL46Tq7ieE49HrE2wea3XT, ADddj7UJgxmddnbJVfTZcTqYLhDVbguAfukEFuqvN2EH, FZN7QZ8ZUUAxMPfxYEYkH3cXUASzH8EqA6B4tyCL8f1j, DaU8yHBaM2vSm3vqo8vGk6mTC79xWRRActYF3gpNWnZt, HZeLxbZ9uHtSpwZC3LBr4Nubd14iHwz7bRSghRZf5VCG, KRm2fbozCPfDvTLu4VmZ6Tr8jUPyVY69RV2N2isDjD1, 98qHEzkVViGzDhn5i8vHJY1ma55Y85aJ3CBSfKc8oi52, 8nCCVCvD12HSokABM9swhwQgsv7i9YQc4cHvXGjWb2QR, 24Uqj9JCLxUeoC3hGfh5W3s9FM9uCHDS2SG3LYwBpyTi, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA".split(", ");
        // String base58Data7 = "04e4d747e1fd77ce00fe7c6c040000005de01d0000000000";
        // testInstruction("bootstrapLiquidity", base58Data7, accounts7, tx_hash7);

        // // ✅  addBalanceLiquidity
        // String tx_hash8 = "VKipZ67shFmiyM9QPDWUP6VZZnJbs3GoA6mEM9YgnTLGDER8hAHDh2a8B8SPc9DvVYNCat9MjBJPsyKPETMxZrG";
        // String[] accounts8 = "EH8xLzfq2YARgQC846NWP6EfRK9gfcjDJMxcHhxFLruv, EY89gD1J493Ya3RS5GWUfjqGwNsudNEng4Cay33Ez8g2, 5ENjTz3oXURSPEst6rQytn3qnauLBKjNgg3PKv27RcwT, 45hJQr4YECdV5sfqSqCgsuKN2fHLygoXDZajURoZxWyi, CNuspoZjJVpziHmqhTukpwbcRyUQa2YKh2JhKuhE5gqT, Ct7W49f6gkhD1QaW5WQy6icAcM4pKU3b9PAbpkPLsa3i, FERjPVNEa7Udq8CEv68h6tPL46Tq7ieE49HrE2wea3XT, sxVzBEhEK1qsUteLdNNAs42C78TYiazE2xKkBCQMjKC, FZN7QZ8ZUUAxMPfxYEYkH3cXUASzH8EqA6B4tyCL8f1j, 9f7tJD8iws3ooBUL8FzBtqxtqBH5LL3JUZe2S8KoqjQo, HZeLxbZ9uHtSpwZC3LBr4Nubd14iHwz7bRSghRZf5VCG, GZMomnjuLbL54bo3RHYmfSJQGCrWSxbeGUH9NWWzC3iC, 4Fm5qDyDKvfJw5Cw4mGpePzqNf4Vur84daReJv1UTx4Y, FkBaDBvHHYAbEXapnrR6kYTavKhGiKApZCmn46R6amwA, 24Uqj9JCLxUeoC3hGfh5W3s9FM9uCHDS2SG3LYwBpyTi, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA".split(", ");
        // String base58Data8 = "a8e3323ebdab54b06ce72700000000001b585c0000000000be70160000000000";
        // testInstruction("addBalanceLiquidity", base58Data8, accounts8, tx_hash8);

        // // ✅ removeBalanceLiquidity
        // String tx_hash9 = "43ZtCjZytr1suRZdKbm8ppmAQTyDkG3R5Ur8BwdSGSZY1VUhn1Gx3PoQKGEvVtySoarn3jqcuTU24u2xr9KYy13i";
        // String[] accounts9 = "4m3mCxkt6bp1kmwgRcApd14Pvv59qu4aLedv6z22DiBr, zyndYUQQZNfmAphUmYGgt9MfjzYxA3xRtLNgUzx8CCw, BP6vyDc8PBvedf8UecSbntyLvKyQ8FxFbrTxmWzWoKco, EwZK6YcnQQ6CXeJf2CJ1Hfwni7g88wpbLLAuX9SEcyJz, GsZpvreHVYMzYtzmkhHHikhNB2WmHizuTfa6bgdCM8r3, 3H8eKCEdDxR5vzuV9VZhxtFg2J55nEP89UMDANG9uPa7, FERjPVNEa7Udq8CEv68h6tPL46Tq7ieE49HrE2wea3XT, GwvomN74DQyfLkU7beNy2uxvvjEcgWeMv6ZwDXr1cFo5, FZN7QZ8ZUUAxMPfxYEYkH3cXUASzH8EqA6B4tyCL8f1j, 5FcwQeg1UH67Utxx1LvBfCzcZaQPuGhrczsgMF4FcR8G, HZeLxbZ9uHtSpwZC3LBr4Nubd14iHwz7bRSghRZf5VCG, EykCySS9D1GgpNwqXz9XVeFZXxCmeYAarErsXD3KZKfS, 4cpzRAT7CuRWwsUahMxkcAS63dFtX6GYZFPFhMK9o99o, 7af76cD8ahWxkqGDf44aVqwFcKUBhLFiEt61S1K6g41c, 24Uqj9JCLxUeoC3hGfh5W3s9FM9uCHDS2SG3LYwBpyTi, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA".split(", ");
        // String base58Data9 = "856d2cb338ee722100a2941a1d0000009e7e8bb5ed000000dfb615d305000000";
        // testInstruction("removeBalanceLiquidity", base58Data9, accounts9, tx_hash9);

        // ✅  removeLiquiditySingleSide
        // String tx_hash10 = "3HbGbDDLjkMTEJmpNAgUWr9SbquKZaTjQTcSrrjKmb1EN3wnKpZTwAKuKDrwz3qJ9zDgAFv2pv2Fj9SaTfkSWvhA";
        // String[] accounts10 = "32D4zRxNc1EssbJieVHfPhZM3rH6CzfUPrWUuWxD9prG, xLebAypjbaQ9tmxUKHV6DZU4mY8ATAAP2sfkNNQLXjf, 9L2XRDKeppeUohtSU1pSn9Kf1pv6ejeKmY1p8zF8rbEu, 24NYE3hHQyUTrHUT4n1CcVrMP9Xy3ULuT1Uurw1HDeck, Hv5ogVb2BZCF3ET2KnaEYj2seKHN5ffGDazm6BGt5DD9, 3ESUFCnRNgZ7Mn2mPPUMmXYaKU8jpnV9VtA17M7t2mHQ, 5XCP3oD3JAuQyDpfBFFVUxsBxNjPQojpKuL4aVhHsDok, 3RpEekjLE5cdcG15YcXJUpxSepemvq2FpmMcgo342BwC, EZun6G5514FeqYtUv26cBHWLqXjAEdjGuoX6ThBpBtKj, C2QoQ111jGHEy5918XkNXQro7gGwC9PKLXd1LqBiYNwA, DQjGWHN9ERn1zSMpWLNvSpTFUSfnxbanBt9A7xyU2bVE, 7nPHtPzdh17VwbvpmFeqJwZ2h9DjFqoSYDCVxL8qPc1G, CW3FeKCAqR54KT1KgdGw5cnc3MEk39eHRxyq9nXX5BTa, 24Uqj9JCLxUeoC3hGfh5W3s9FM9uCHDS2SG3LYwBpyTi, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA".split(", ");
        // String base58Data10 = "5454b142feb90afb3931cb96030000000000000000000000";
        // testInstruction("removeLiquiditySingleSide", base58Data10, accounts10, tx_hash10);

        // ✅ addImbalanceLiquidity
        // String tx_hash11 = "4AgbmA1uUhsrxhdRwFbkbH2FywHP2oog8KHxLxmTVQpPYFTCxhVu5EzYJDV1GM2pJ8ouKzLixATqkZuvAVZhNz8f";
        // String[] accounts11 = "4xqyRGWMRkfVo7GH74aryKjSLcpQiVHGAZY4u1n6wAbZ, 36F4LM4tK5xSteQnv7DGjkgoyeb7iWjzwGwmxiEUixPC, 2wZjBw1iM6Zf1k3bvM1qzUzUxWxBUDERA8cF9um9EQbf, 2HsREroNKJoNAVNyGbDYqYCx3s7EJVqw2UEQ2Rw3xbdP, 5GfRxTABY7pb9NTUD6mozKS21Hef2u2BYVYrTnCeMhc6, 3ESUFCnRNgZ7Mn2mPPUMmXYaKU8jpnV9VtA17M7t2mHQ, 2dH3aSpt5aEwhoeSaThKRNtNppEpg2DhGKGa1C5Wecc1, 3RpEekjLE5cdcG15YcXJUpxSepemvq2FpmMcgo342BwC, Afe5fiLmbKw7aBi1VgWZb9hEY8nRYtib6LNr5RGUJibP, C2QoQ111jGHEy5918XkNXQro7gGwC9PKLXd1LqBiYNwA, 2E9kmWYUfpw2vcpJWQF1HHW4HY3oj6aMEhDWCksEXrck, ntMBSqF1p1dmTe5PSR2EvWpMA7rPS6MhTq9txWcKLdB, ATYqTPSJjmxCbbwR7UQ9CNbqyQazw8KJZLPAZMAbN6Ve, Dt5TyjZcg6m1vmM3d8dUa4EY1mSkvrBkHy9DEowq2aZF, 24Uqj9JCLxUeoC3hGfh5W3s9FM9uCHDS2SG3LYwBpyTi, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA".split(", ");
        // String base58Data11 = "4f237a54ad0f5dbf9827020000000000a0860100000000007dd1000000000000";
        // testInstruction("addImbalanceLiquidity", base58Data11, accounts11, tx_hash11);


        // // ✅ lock
        // String tx_hash12 = "617yxt1ejY97aHjLqys45jDPuFwAMncujXr7B49TLYES5e9SA5XFad8YTJZ3VaqYkPzRJKaghiDAwAsgGDx7UB62";
        // String[] accounts12 = "3EGx4EqgbgEBUagSq5oMKDXcDGFhZEWpEC1rCYGvP7as, ECZtjek82bL767ZL6ixGXyV3rwc2NPad4xJLhYokvVf4, 2hYckWMc7x3hxcUSZeDW5hQ8KrTTQuu5TKHNjsgh63u2, 9TvjgtbkNJdwETBqGWr8Dv86N7CxFPEFkbmDUFEoovuP, 8d1p6vrFzyaC4BVB6rxx9ubnGbMDto9yBJHGhfRuxQio, CUbAemhuR4AQdha4ko4d1eQ9HVuoVW4cwKjVXjyjRR1B, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA, E5LUkP4bSxfwTSrod3sJzBUZZmGTWsS4ANUZL6aeaVoq, FERjPVNEa7Udq8CEv68h6tPL46Tq7ieE49HrE2wea3XT, 9xZjwoZUzPZYSYe18VehK2LW1AngYywvRga8g2yTXqFM, 6eREZwy3roeTuy8wJsSsAXB8chpTUpXVvkFBbtN7yJYG, 7nY73eafkr6HGHSjquapRecQUkuwCG7LAEYMvyoxqJy3, FZN7QZ8ZUUAxMPfxYEYkH3cXUASzH8EqA6B4tyCL8f1j".split(", ");
        // String base58Data12 = "1513d02bed3eff574503b39300000000";
        // testInstruction("lock", base58Data12, accounts12, tx_hash12);

        // // ✅ swap
        // String tx_hash13 = "5ohe6nJxTsi2v1uG4C4VygAPGTedqXq6WCso4P27596zE4BhJ6JMbyTn561Qbv4DnCU6MrNcUkxxgXmSdtntQeF6";
        // String[] accounts13 = "CtjrznmKMBNGTmZiRmCtHkgX3VcduwMMqFntLNbEqury, 3sWytmCawpYr23UY5xHDcVjqZQQXUjKq5UDMqtSExUUY, 9qnuXNhXCGD71Wk39kVVL4xmcGqPKNEjuTyt8Yeh5EaM, FzBsTG1wniNTXgk5BMBLtmUEQPMuKYBy7Z2BpLLhUF2r, FERjPVNEa7Udq8CEv68h6tPL46Tq7ieE49HrE2wea3XT, 868fdxcdiLXQuwwX53cxEYmwWgmiVkTwHCtJJHNAmEyK, HZeLxbZ9uHtSpwZC3LBr4Nubd14iHwz7bRSghRZf5VCG, 7N4KbjuERLSXxQJLNrghZNqzBU5rFaxm6jpHLL5okdqK, FZN7QZ8ZUUAxMPfxYEYkH3cXUASzH8EqA6B4tyCL8f1j, 2Zjmcd4JvCV4psUzPAfozKm3qCaKSWQEBJiRArURreyi, Fn65bbAyKx3rqCBpJAGhxdZUEzto4VMAP3dqHA6U3STL, 7AorkB1drr8ML37ZNZsTRYv9YDESuAmSebKzLBrm5L1, 7Lp9gCB9nZWFmNbEQYFFtPvALXyTpCrgQ6Wb6WrkT6Gj, 24Uqj9JCLxUeoC3hGfh5W3s9FM9uCHDS2SG3LYwBpyTi, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA".split(", ");
        // String base58Data13 = "f8c69e91e17587c84e8d3100000000000000000000000000";
        // testInstruction("swap", base58Data13, accounts13, tx_hash13);

        // // ✅ claimFee
        // String tx_hash14 = "e1UyBkmiXgbL4gD84zp2c4VpjxbdkrKQPExbudfTmLCNCwCQPvWxtryBVf5TonRDm5128EB6duQBjXCu7zf5Cnp";
        // String[] accounts14 = "E2tqaSHSMhWUezNn23L2hjhaCQicrukLHVnPQPKRABJ4, 4TX9gxUmcw6X85KqTr8grVHtKPcHHKMjbHCCykb8pMrH, 6a1HrLcyaQsyqkfmWTZLPvdFS29A4N5S1C9pbiHJypzm, 2S2e3rJwhjUEJqinGwV6RmGhVAKJXXkNpFsmhKmpcPH1, EKTt1B33Ut1oPsPeWU5wj6SqqEoZWN77WNFzzkq12PkC, 7R2jF18cFC8V9KfcU4AUu1Z79sQwjYT5U5BfJtE6ehWe, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA, HZeLxbZ9uHtSpwZC3LBr4Nubd14iHwz7bRSghRZf5VCG, Edm2HBpviLP2zirVVh6HYChHJFVk3fWA1FtAxjBn2KFG, FERjPVNEa7Udq8CEv68h6tPL46Tq7ieE49HrE2wea3XT, 5QHVNN1N2uKqkvd7e82htRNCCH6KjhxNEserSyFV9imP, 7ciMEu53keTDByNvB5gcB1hPg7KgZHfKhaWjaAm8JmYv, 38TBuMqoBBNX9WUEcYsdPTxPKem5kvqvg9qAfxYYuqR9, FZN7QZ8ZUUAxMPfxYEYkH3cXUASzH8EqA6B4tyCL8f1j, 3eykxQXd3pkguMyr9mjDqWMm6vD3NG3B9LUo5xSnypiE, 52xi6bHu8X44zRZZn6gLgQ2e3e4qg9TM2wNsBEsH1VhX, HtDthVc9esvRABMsd29N9bW8aQZV2UhnbgSJww9pzMfz, 24Uqj9JCLxUeoC3hGfh5W3s9FM9uCHDS2SG3LYwBpyTi".split(", ");
        // String base58Data14 = "a9204f8988e84689a5a71e9100000000";
        // testInstruction("claimFee", base58Data14, accounts14, tx_hash14);

        // ✅ setPoolFees
        // String tx_hash15 = "rvrHUNvnWnL52sVxYhrQSNQMCfZaYHBn9r2TaaMGkm2Ed8vnqPTSvhJSQ42N6aVSTBbJVNFccbZNLth9nDuYBSY";
        // String[] accounts15 = "F7cTmFKaUp78rVt1yccNjMuoe3Eg86AkyVNWTHPQnjsR, fee3qJNFpqUEYLCaCntRNqNdqrX2yCeYnpxUj2TJP9P".split(", ");
        // String base58Data15 = "662c9e36cd257e4e581b000000000000a086010000000000204e000000000000a0860100000000000000000000000000";
        // testInstruction("setPoolFees", base58Data15, accounts15, tx_hash15);
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
            
            byte[] data = Hex.decodeHex(base58Data.toCharArray());
            MeteoraAlmmInstructionParser parser = new MeteoraAlmmInstructionParser();
            Map<String, Object> result = parser.parseInstruction(data, accounts);
            System.out.println("✅ 解析结果: " + result);
            
        } catch (Exception e) {
            System.err.println("❌ 测试失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 