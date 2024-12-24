package cn.xlystar.parse.solSwap.whirlpool;

import cn.xlystar.parse.solSwap.pump.PumpDotFunInstructionParser;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.util.Map;

public class WhirlpoolInstructionParserTest {

    public static void main(String[] args) throws DecoderException {
        testSwap(); // 通过
        testSwapV2(); // 通过
        testTwoHopSwap(); // 通过
        testTwoHopSwapV2(); // 通过
        testInitializePool(); // 通过
        testInitializePoolV2(); // 通过
        testIncreaseLiquidity(); // 通过
        testIncreaseLiquidityV2(); // 通过
        testDecreaseLiquidity(); // 通过
        testDecreaseLiquidityV2(); // 通过
        testDeletePositionBundle(); // 通过
        testOpenPositionWithMetadata(); // 通过
        testOpenBundledPosition(); // 通过
        testOpenPosition(); // 通过

        testCloseBundledPosition(); // 通过
        testClosePosition(); // 通过
        testCollectFees(); // 通过
        testCollectFeesV2(); // 通过
        testCollectProtocolFees(); // 通过
        testCollectProtocolFeesV2(); // 通过
        testCollectReward();  // 通过
        testCollectRewardV2();  // 通过
        testUpdateFeesAndRewards(); // 通过
        testInitializeRewardV2(); // 通过
        testInitializeReward(); // 通过
        testInitializeTickArray(); // 通过
        testInitializePositionBundle(); // 通过
        testInitializeTokenBadge(); // 通过
        testInitializePositionBundleWithMetadata();// 通过

        testInitializeConfig(); // 通过
        testInitializeFeeTier(); // 无具体 case
        testSetRewardEmissions(); // 通过
        testSetRewardEmissionsV2(); // 通过
        testSetRewardEmissionsSuperAuthority(); // 无具体 case
        testSetRewardAuthority(); // 无具体 case
        testSetRewardAuthorityBySuperAuthority(); // 通过

        testSetDefaultFeeRate(); // 通过


    }

    private static void testInstruction(String testCase, String base58Data, String[] accounts) {
        try {
            System.out.println("\n=== Testing " + testCase + " ===");
            byte[] decode = Hex.decodeHex(base58Data.toCharArray());
//        byte[] decode = Base58.decode("15P");
            Map<String, Object> result = new WhirlpoolInstructionParser().parseInstruction(decode, accounts);
            System.out.println("Result: " + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void testSwap() {
        System.out.println("\n=== Testing testSwap ===");

        // Case 1: tx_hash: 3Q6Uz67V7A64MhMUeCJeRnjNAjGyqWuWPpsuFWCYn8opYgDa2sSV8mNFYj4SCGudC4fpRA9mKWdsR2Nn91rxwV3b
        String[] accounts1 = "TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA, NjordRPSzFs8XQUKMjGrhPcmGo9yfC9HP3VHmh8xZpZ, 83v8iPyZihDEjDdY8RdZddyZNyUtXngz69Lgo9Kt5d6d, 9wFA5VL39C9uQhWSg6us1cehnYG7cYENk4mtj3Vnv54j, D3CDPQLoa9jY1LXCkpUqd3JQDWz8DX1LDE1dhmJt9fq4, 3JpjbbqkGQEwedw7ZHcV3E2cxWrTAbuJeiWiTXW7USTq, dwxR9YF7WwnJJu7bPC4UNcWFpcSsooH6fxbpoa3fTbJ, 6RDoEmkCCH5u7MTrmgZGrNiKeCPxVrnHHSMmZSQYjnF6, Anxv3qvhjsu6YKWtHi8Cny9PGJ7GwxMUCpTi8Nk9Rs9X, 3TVJwSBWmDrK5ok3RFLXm3Mgmd1ZjxZVHZwxa3yQzMcL, GwRSc3EPw2fCLJN7zWwcApXgHSrfmj9m4H5sfk1W2SUJ".split(", ");
        String base58Data1 = "f8c69e91e17587c86680430b000000000000000000000000503b01000100000000000000000000000101";  // amountIn=1000000000, minimumAmountOut=100000
        testInstruction("testSwap Case 1", base58Data1, accounts1);
    }

    private static void testSwapV2() {
        System.out.println("\n=== Testing testSwapV2 ===");

        // Case 1: tx_hash: 2YHqaPxEVdekqJ43S6gkGJr46pRRBSWHkthguN2cqHZ6CVvvTx8dHiVjARt9J8GABfwahZpn8Rs9Utk8c58ZFUhX
        String[] accounts1 = "TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA, MemoSq4gqABAXKb96qnH8TysNcWxMyWCqXgDLGmfcHr, HV1KXxWFaSeriyFvXyx48FqG9BoFbfinB8njCJonqP7K, AgeSxtVWWMojFWYNrXKnVp9cFuC5CQ7M4rzmrseLxfUj, EPjFWdd5AufqSSqeM2qN1xzybapC8G4wEGGkZwyTDt1v, J3NKxxXZcnNiMjKw9hYb2K4LUxgwB6t1FtPtQVsv3KFr, HjkGLCPnsMr4yP2Tmi1Uj7gV7Y2xDj2Npn9kYfVBYr2s, 7YLEgfpJ6ZVZTKukGJYNoWVYeasqB5e45ipVsZ7kNazA, G74NtxPE6G9RZZzye8bpm84vWxU3QgTfVqoUCHLJKfB7, 3g3Kg25eRoUDoorXhPDq73qmFuXZQR5zngzn2uLBm7T1, J1vwCFsEN6AdrpFE4gNVgjZ6mx2RvxQS4pfvRi9VgFHM, HNsD5Az3AJ3GjJL9R2NE6fG4KrtXPuC48LE5sWvmkUsV, DL2hpaHKozQ6x9VraJB5zJs1gZ5iZg62oB5AAL5GcXy7, 43JiBt9w9xaeApQ2xHBgJCgqvsvamJBMcg4Xb9rZ3PFa".split(", ");
        String base58Data1 = "2b04ed0b1ac91e62a4ca9d10000000000100000000000000503b0100010000000000000000000000010100";  // amountIn=1000000000, minimumAmountOut=100000
        testInstruction("testSwapV2 Case 1", base58Data1, accounts1);
    }

    private static void testTwoHopSwap() {
        System.out.println("\n=== Testing testTwoHopSwap ===");

        // Case 1: tx_hash: enZYFtnaXmKp7c78SzrBfy9JSvfT37dLVSGM4etm1mjmMbf1gWAfowUNBxjV9FKiGK3NCUAxuRtxqyRLq2sSGfp
        String[] accounts1 = "TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA, GjRnGytWoUQ44KESu8E1tn8qpR5MYz4Uw4GZXEJoum35, DkVN7RKTNjSSER5oyurf3vddQU2ZneSCYwXvpErvTCFA, 4Ui9QdDNuUaAGqCPcDSp191QrixLzQiLxJ1Gnqvz3szP, DA2BdMTyFVUCojTzJnt5JpMauBrQ8jNVrGbJDVRwcw7, HRBd7gkQbhpTuXA61a7ii3Bbmcvp6ZCNwne28aC8A8Qq, 9K2wMuWCGgDPE1cSKxcwnuaBZ9bRnBUjrcWxWT3Eqqa, EsU4fjzg2JSSgqbh3jx95qaDSpRe4gLqPWQLbmLjYaLF, DA2BdMTyFVUCojTzJnt5JpMauBrQ8jNVrGbJDVRwcw7, 9gMRWNfLXNc54ta5LxuM16p72GYap2t6rf455TTBKQW4, FSVPoWxrh6YqvY14AUYvKypnXscax6knqBvcSmeV4y7y, CYcxSC2vmbScHFcTtEM6346uqMN8b9zeSGnP9qZu1E6U, DyxveDmqWLQioxx3RFMceLaoxLdw28LzGQ2NbLsMXnq8, DBnonsVXQv3cw9HHqLUwHbp3z786p6DwJKoj6yxa67w4, C9Uc5ihhBedHweuiGoSVM2v7VJy74DtorVqMw6QwNt3o, AQ8aPice8gCxyquAVBb8Q4AdWk1fVzsdZWuzezthwqRq, 4K9Q4jatAjW3LeVxZczyGhtNMyL7UxfRAf79rTHpSRxw, ExPBjZ4MEpfTfxCQauw6curBYvLzFhEi1hhWVgBsjU6E, 2USu2fKJ6cMmqZkaMQDDiTQQz3K4a5m2oXrZ1wXpFFwP, CrkkeqLUo7n6gvzoYMPZ7CHjie1Zua2CHUPe2DFh8mmR".split(", ");
        String base58Data1 = "c360ed6c44a2dbe6e26c4a000000000082da960100000000000001af331ba8327fbb35b1c4feff00000000503b0100010000000000000000000000";  // amountIn=1000000000, minimumAmountOut=100000
        testInstruction("testTwoHopSwap Case 1", base58Data1, accounts1);
    }

    private static void testTwoHopSwapV2() {
        System.out.println("\n=== Testing testTwoHopSwapV2 ===");

        // Case 1: tx_hash: 5tPryKXuQWZgrb8VbiC7oRuft9UrRETSHyAVYYwRwCmKofRNpQrekNmpnZMe8QuYGoDEmS2qUiVwTP9EB6QHbheK
        String[] accounts1 = "9tXiuRRw7kbejLhZXtxDxYs2REe43uH2e7k1kocgdM9B, 9Vh6fqJjDkqSTZ8bDXseVxGb2yQEMkEhhtte2anQCHSf, EPjFWdd5AufqSSqeM2qN1xzybapC8G4wEGGkZwyTDt1v, 2b1kV6DkPAnxd5ixfnxCpjxmKwqjjaYmCZfHsFu24GXo, So11111111111111111111111111111111111111112, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA, TokenzQdBNbLqP5VEhdkAS6EPFLC1PHnBqCXEpPxuEb, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA, CXBeQrvuhhSWWhnkDibHrmofCbTV9w3U5Vz7BxDnBozb, MvB8poDgpDPbRgx8MXeb7EPEsawGuiBTqpkpM9exeLi, EeF6oBy6AQiBJoRx5xiRNxa6cmpQE3ayVagj28QFZuyg, BoupoyPJU1EgwRUhKw78dQKjPrv26SYgtmtXas9axdT8, EQcD4Wcp9fk1vnPT1q8r3KrPd3euTn3zuwjRtuEvDjTc, 8ULAq1ucwckC7M5mLqHKZhZK8MriTPjcS2Y4gwkugut3, JBfFx8Mk77qjecUT3MkMWntjuLx5LJAeb7ecdJ4KfcUa, 8hXTpuvJQRar4Pf6BZiEWquFgtAtSf2RFDM6EL2FCcf1, B1jXbjDzenSy8kPNaGw3GSKAVQis5K5tRLeXuaskZTpS, F352jHDFuryt4YaA3d3Mngosp1nmU4WJhq6KEV1K3iTz, GUGBkqLetiL989hBFQ5Dd8quidZBxKEzEaG11kEv6B5b, DH3F7nqe12cJEjXwXsGfwnjtWm7ebpR3RYq5yPiaFMnQ, A4gCaw3dCpXWhMMmjKW6qfN771x8Cmo8gXGCer8ZpvfR, BogFEjFPy2dHaJTUANvKnNjymswDVhDbyeCCcmScaHRG, 8YW7kG5nZGL36mEHgJSdzvrhHXPV9x9R85NHHFtcoJK9, MemoSq4gqABAXKb96qnH8TysNcWxMyWCqXgDLGmfcHr".split(", ");
        String base58Data1 = "ba8fd11dfe02c2754ec23d0000000000707b4d0100000000010000af331ba8327fbb35b1c4feff00000000af331ba8327fbb35b1c4feff0000000000";  // amountIn=1000000000, minimumAmountOut=100000
        testInstruction("testTwoHopSwapV2 Case 1", base58Data1, accounts1);
    }

    private static void testInitializePool() {
        System.out.println("\n=== Testing testInitializePool ===");

        // Case 1: tx_hash: 5zKX3N1rmX9ubnYu8k2bgb8W4G4xixf2rBsWhgLSFACBLt5SEqe4yRtCFzMK7NEmodqpQA4Vt72xr7P2AAQ2C3DW
        String[] accounts1 = "8PdyJHamdPwX9PSJ9XBdftF2XAgYpdkHrSQHQpUGaq79, So11111111111111111111111111111111111111112, 8bvMQRZkyD12MJATCwBp7Qaf4Uuqq7vHjNmL9sqSxbzv, BuFT4LG7Qxn9iJfNHoWscwHYQetTTq6PABauKffehnkh, FfjE9fvABHV9GUDazNXmp8j5iYUMGfo3ahndppaentww, Efn1nwvYWjYoE52ujibEUJqvAs6hBwYo1ZaY9aHwwu7p, MRSV4poV5zdtqNKCMshzWBggkjHUQNC24es7ec38cN2, 2CtTLeoHatHBomspRpAjZf67KE4pFA8qu8PsYCnaQU4e, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA, 11111111111111111111111111111111, SysvarRent111111111111111111111111111111111".split(", ");
        String base58Data1 = "5fb40aac54aee828ff400000000000000000000100000000000000";  // amountIn=1000000000, minimumAmountOut=100000
        testInstruction("testInitializePool Case 1", base58Data1, accounts1);
    }

    private static void testInitializePoolV2() {
        System.out.println("\n=== Testing testInitializePoolV2 ===");

        // Case 1: tx_hash: 4YgNBX3dw5yDjmv8jU85UAUyXFFvPjUFv8Aiu16t6ZPpYiGPwBoRN5DikFAznbvmiSoFbPkgi15XT9xJ4EzXgxJP
        String[] accounts1 = "2LecshUwdy9xi7meFgHtFJQNSKk4KdTrcpvaB56dP2NQ, 2b1kV6DkPAnxd5ixfnxCpjxmKwqjjaYmCZfHsFu24GXo, Es9vMFrzaCERmJfrF4H2FYD4KCoNkY11McCe8BenwNYB, HX5iftnCxhtu11ys3ZuWbvUqo7cyPYaVNZBrLL67Hrbm, ArcwiH2SrJyQcgfDGr5d5HLfT3vSzHkdzgQ1DV3drP5Z, r21Gamwd9DtyjHeGywsneoQYR39C1VDwrw7tWxHAwh6, 39GrsozbzM9Sg1U7EDnEtQ69fsVF3pmVtmV2DGDAQQJ5, 7owTEfBdJ2nEdxaUZ2Hm6XevkG7FqHgmeVhkZbUTCCQT, 5FdbJZ8KNZVmJ154EFreHCmRC3iG9VWVvwUWPHGs61hj, 62dSkn5ktwY1PoKPNMArZA4bZsvyemuknWUnnQ2ATTuN, TokenzQdBNbLqP5VEhdkAS6EPFLC1PHnBqCXEpPxuEb, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA, 11111111111111111111111111111111, SysvarRent111111111111111111111111111111111".split(", ");
        String base58Data1 = "cf2d57f21b3fcc43010000000000000000000100000000000000";  // amountIn=1000000000, minimumAmountOut=100000
        testInstruction("testInitializePoolV2 Case 1", base58Data1, accounts1);
    }

    private static void testIncreaseLiquidity() {
        System.out.println("\n=== Testing testIncreaseLiquidity ===");

        // Case 1: tx_hash: 2pRFtPTwZb26n3LkrGVBmUA2VfSBapMjswKvxLqbu3KQhwtyBGkkSmc4CaLo2RZW9cJom1FiUFY41CMMrbKBQZ1D
        String[] accounts1 = "3vXZLMWPjuVRiSpjF3zLtvoRYAm2DLdu3DBEfQHZsW1x, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA, DKa7xqomKXMsUA9Taq3tEC2kRnU8XSHRaVc4o78Pcgw2, 29Fw9WuzBBTwr4PsDCiGj38SMR9uVd4EvsnBsGo1Z41A, 5RWLPZDKatWZFGZWPXvH1w88tPfTbdKgSKZWURJifjTK, 9j2UfJjdVuYSi6FbGCWC8LdKMhaSzK5zrnKQjTVLcuxK, 4mtWwyGettB7sirMBtMes42PBaU7hst4qjPNYcDwzWFU, CxLH3HVa7pKGtxkxCzGhqweLK6AM2dF3zvCPYyPRq1c3, C5PJ1SHhwtD6aBVB3yXdQvmEFw5Gx1VpdCX57fc6iudN, Ct1PkA9z4hCsmn5icHKywZr1kuoivYteXgoPGDN3n3Sj, 8UJKJXCUgZLKLqy1fRKm1dxFQQzo82e3pmnaRF8ZtdcH".split(", ");
        String base58Data1 = "2e9cf3760dcdfbb2972f4704000000000000000000000000823f0000000000002950180700000000";  // amountIn=1000000000, minimumAmountOut=100000
        testInstruction("testIncreaseLiquidity Case 1", base58Data1, accounts1);
    }
    private static void testIncreaseLiquidityV2() {
        System.out.println("\n=== Testing testIncreaseLiquidityV2 ===");

        // Case 1: tx_hash: 5NvtysnXc2VAgYcFm3EGEAbmjjPm2HYhKkhdaeBQwh7d3ZPtTRVSnmJZFgJM3jpmF2BHj5VZmq5W5SkuPSxDPR9B
        String[] accounts1 = "3gpyRVcdbH6Jy1AR5Xz5hypNGyGWECLXM5eaA79z4GdQ, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA, MemoSq4gqABAXKb96qnH8TysNcWxMyWCqXgDLGmfcHr, 9EHk3hp8h8CAjgej1tuksj9fv5qc9hsmsL1VH5DVkaE, kpY55nSLUWW3JfCJ1WaUxVaHP1tCzcYmpWxuT7K7Wg3, 6cb5nzd35FaFuAPhcM9z9pCUkeV7Nu4GviE2UUiUMpY5, DezXAZ8z7PnrnRJjz3wXBoRgixCa6xjnB7YaB1pPB263, EKpQGSJtjMFqKZ9KQanSqYXRcF8fBopzLHYxdM65zcjm, 7y5oj5FxdJW3Yg4V1fr5cdxSQYadPLAFmKyS2UqGtd2D, DUZaJx97D8UrSL1DJndq9vYRSWdSavfSgY1L8JmivmyV, 5v42ejcpYRE7UcCnC8hifyaa7qiwqKSYepYJtLMHeVhH, EJcuGkiZ3EbDCikPuWperRDHS4afLPkgz1LF4DpJrLrd, huWwTRZA3DbijDNSUGTqkx1az9rB3W8SKXp4F55X1xz, HoDtDr91xwKf7vdWvBuc2atfWNgxpWZEa5JosQEBwmnC".split(", ");
        String base58Data1 = "851d59df45eeb00abe81a70300000000000000000000000099a46f3800000000263008000000000000";  // amountIn=1000000000, minimumAmountOut=100000
        testInstruction("testIncreaseLiquidityV2 Case 1", base58Data1, accounts1);
    }

    private static void testDecreaseLiquidity() {
        System.out.println("\n=== Testing testDecreaseLiquidity ===");

        // Case 1: tx_hash: 2Qe2ZhtCR7sLeuTQLUzXWApmZAqVifbuXMjq2XzvMDEQ45dQCTved6Jren5hUaapyHC4uXVirNj8jgLKWQ4M7TYJ
        String[] accounts1 = "83v8iPyZihDEjDdY8RdZddyZNyUtXngz69Lgo9Kt5d6d, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA, 882DFRCi5akKFyYxT4PP2vZkoQEGvm2Nsind2nPDuGqu, 3MWCP3ioLsbymqVViZhCPhrGALZXYyM4CJzxDcE5UMJn, 9uSZunezbxCJc2XdKwgmGVuj1AifjMLhDGVLqTZSEwyL, 2rw3gMo4AB5Y5YggfXyh3ZCwc7LerdR1nFD3x6aedPSt, 3stBgcqaa1UWK23ybiZjTS1h9VuCH18dPkhxyiWfDaLd, D3CDPQLoa9jY1LXCkpUqd3JQDWz8DX1LDE1dhmJt9fq4, dwxR9YF7WwnJJu7bPC4UNcWFpcSsooH6fxbpoa3fTbJ, 3fTuwBxsEMxZxZv3etR9QSr4xzpWo1FysUumvYjMvNwy, 3fTuwBxsEMxZxZv3etR9QSr4xzpWo1FysUumvYjMvNwy"
                        .split(", ");
        String base58Data1 = "a026d06f685b2c0116b2cf15060000000000000000000000000000000000000035a1330000000000";  // amountIn=1000000000, minimumAmountOut=100000
        testInstruction("testDecreaseLiquidity Case 1", base58Data1, accounts1);
    }
    private static void testDecreaseLiquidityV2() {
        System.out.println("\n=== Testing testIncreaseLiquidityV2 ===");

        // Case 1: tx_hash: eqKokekewi2hGDKLLr4fu7t9oUjWEZcHyPD3TPyZzyJCqEejdPZBBfjUPY3ZBksKTBevq9iexNkPha5oKfUsfud
        String[] accounts1 = "9qyqY3BPfc2Ex9BktiZhDJwfqVHChHXwTQpFNH136psy, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA, MemoSq4gqABAXKb96qnH8TysNcWxMyWCqXgDLGmfcHr, 8UsHVkiGp1yA3GBM8Hyr9LxpHkzDe3185t3pfm1iPtdW, A1VQUGY6FajbdLU4PXKumZtRdNu8eXDxyZANgK6rDjwq, 7byPpapg1mY7dEg7uK5cnh3Ji6yCfQVATv42ZkJk6EZY, So11111111111111111111111111111111111111112, GinNabffZL4fUj9Vactxha74GDAW8kDPGaHqMtMzps2f, HoszSfvgieRyab9kMtheJn55E66BCRVLWaAB9cjxbPhL, 3JEbSEycCp3ttZoL6mBLJKzojsWR7dBVsgRoYeo4syeg, CBUD5SDdPvuLSbAYQnkrezwNMMd5Ck7yoM74KqdZMFTT, 8kAqKgGk31bKqrTxkK6kWCQD89yqqBzr637M1nRUu23Y, 3BfdtVFQPyjBYLd7xV9TWMVH8ipPiV5pFKu94CYHZo5D, 4UrPDAYKWuPhw88fWHYjSuU4HR8nnd2hrTgChkt6c3vW".split(", ");
        String base58Data1 = "3a7fbc3e4f52c460f3ebf0552e01000000000000000000002319bd0e00000000d2e2f1110466010000";  // amountIn=1000000000, minimumAmountOut=100000
        testInstruction("testIncreaseLiquidityV2 Case 1", base58Data1, accounts1);
    }

    private static void testDeleteTokenBadge() {
        System.out.println("\n=== Testing testDeleteTokenBadge ===");

//        // Case 1: tx_hash: 2Qe2ZhtCR7sLeuTQLUzXWApmZAqVifbuXMjq2XzvMDEQ45dQCTved6Jren5hUaapyHC4uXVirNj8jgLKWQ4M7TYJ
//        String[] accounts1 = "83v8iPyZihDEjDdY8RdZddyZNyUtXngz69Lgo9Kt5d6d, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA, 882DFRCi5akKFyYxT4PP2vZkoQEGvm2Nsind2nPDuGqu, 3MWCP3ioLsbymqVViZhCPhrGALZXYyM4CJzxDcE5UMJn, 9uSZunezbxCJc2XdKwgmGVuj1AifjMLhDGVLqTZSEwyL, 2rw3gMo4AB5Y5YggfXyh3ZCwc7LerdR1nFD3x6aedPSt, 3stBgcqaa1UWK23ybiZjTS1h9VuCH18dPkhxyiWfDaLd, D3CDPQLoa9jY1LXCkpUqd3JQDWz8DX1LDE1dhmJt9fq4, dwxR9YF7WwnJJu7bPC4UNcWFpcSsooH6fxbpoa3fTbJ, 3fTuwBxsEMxZxZv3etR9QSr4xzpWo1FysUumvYjMvNwy, 3fTuwBxsEMxZxZv3etR9QSr4xzpWo1FysUumvYjMvNwy"
//                .split(", ");
//        String base58Data1 = "a026d06f685b2c0116b2cf15060000000000000000000000000000000000000035a1330000000000";  // amountIn=1000000000, minimumAmountOut=100000
//        testInstruction("testDeleteTokenBadge Case 1", base58Data1, accounts1);
    }
    private static void testDeletePositionBundle() {
        System.out.println("\n=== Testing testDeletePositionBundle ===");

        // Case 1: tx_hash: 213k8rmcLEGnz4D55Z8jZegKoZNihTMN6aDidLudHt2CEQjnVD8aYaAX4awB7DQY8Ga8Y4FaYn4UczscdX6NKQqk
        String[] accounts1 = "EFtuJH7T2SZksgRH7g9PeafuhoCSNDbJJgV4EJ3YKfBX, Cjv6WrUNk2fS5QKdn11CtZewGbLtM4Tw4W7H3zpXsCQT, 8wQ9To5R7gakhcyiyN2WhQktiEvs5go4UcTE4ZMhbCHp, 5ETxmKTHW7LthZZFzYuhroRxjrr4pDrFpSRcJYSEkqEz, 5ETxmKTHW7LthZZFzYuhroRxjrr4pDrFpSRcJYSEkqEz, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA"
                .split(", ");
        String base58Data1 = "64196302d9ef7cad";  // amountIn=1000000000, minimumAmountOut=100000
        testInstruction("testDeletePositionBundle Case 1", base58Data1, accounts1);
    }

    private static void testOpenPositionWithMetadata() {
        System.out.println("\n=== Testing testOpenPositionWithMetadata ===");

        // Case 1: tx_hash: N7dntQK5hofMwSDJuXJjoZZnW2JghCD1MwyaqxBSGj6aTZ4ZHeuTBj7w7n8qn1p3oDkNP5gC4rK8E2fUUwTzAn5
        String[] accounts1 = "9BFDWRxhGMceLhBBviTMtw6AdUAx2uTwKng5NVDyAjeZ, 9BFDWRxhGMceLhBBviTMtw6AdUAx2uTwKng5NVDyAjeZ, Cbz9N7QN4UH72bfQ8WQtucfHukiZLY28YDpsKEG5F9D4, DTBiFk8ZKZw4ofEDduFZXd8FTsx9VyModeKA5QxNW1xV, EWPSeFPwSkDA6St2KhopNjaPyhAZaCkJVzo3snXQCqv, DVcq2277gKmQ1dUbv2C3B4w4zD8togQ1LTXpNaSj1Q4S, Czfq3xZZDmsdGdUyrNLtRhGc47cXcZtLG4crryfu44zE, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA, 11111111111111111111111111111111, SysvarRent111111111111111111111111111111111, ATokenGPvbdGVxr1b2hvZbsiqW5xWH25efTNsLJA8knL, metaqbxxUerdq28cj1RbAWkYQm3ybzjb6a8bt518x1s, 3axbTs2z5GBy6usVbNVoqEgZMng3vZvMnAoX29BFfwhr"
                .split(", ");
        String base58Data1 = "f21d86303a6e0e3cfffe44bcffff20c2ffff";  // amountIn=1000000000, minimumAmountOut=100000
        testInstruction("testOpenPositionWithMetadata Case 1", base58Data1, accounts1);
    }

    private static void testOpenBundledPosition() {
        System.out.println("\n=== Testing testOpenBundledPosition ===");

        // Case 1: tx_hash: 4Gf3jHFgWTW7xsS7t3ahwG2RzApiN43EteogUHzMccms4DWeivvrMu8UXetC5WJgdUfiymQ3t5Zu5An7ZgRBjn8k
        String[] accounts1 = "3SkDD2hg7BhPupFFoLwaUgGQSXrT7xxUefWvcKpGAG4e, GeAAqtjqQ74wi32XQdRdrE6nLQYNBTFNANNgB2zUT5z8, EMhpvMKPVVFFjwY1XYtY467vNyeW37uXLuVWB3g7LQCn, GmEN3qzuvbMZG1WZ258EgFKy6h9LoqeiD5jcLZgEGyzm, 5UAbsvd6ytaumjGEi1MqtbSVvx2AY3c9Gsxnir5Uvbsz, GmEN3qzuvbMZG1WZ258EgFKy6h9LoqeiD5jcLZgEGyzm, 11111111111111111111111111111111, SysvarRent111111111111111111111111111111111"
                .split(", ");
        String base58Data1 = "a9717eabd5acd431000000f3ffff00f7ffff";  // amountIn=1000000000, minimumAmountOut=100000
        testInstruction("testOpenBundledPosition Case 1", base58Data1, accounts1);
    }
    private static void testOpenPosition() {
        System.out.println("\n=== Testing testOpenBundledPosition ===");

        // Case 1: tx_hash: 5wXGjQfQnw3KhfXsTb1iu3FrWGJ7zcCyxQJB2nVhGpGTJFoc1k1R9CRtrN3TPaZyW1PHpUVaAv6iQeJSY14GNf1S
        String[] accounts1 = "Gwyv7jywUymbwrLCeFrNXTz5SCSxbN25Yu9F2dWydyrv, C6eNhiFZAGtMTHMMam8PVsSMmYy9FEb2oewD8WxtnuMb, Gdh5gn6unqTzkCpxqFp6H1xziGRqt8u8wV5J6fQX3zsg, 3fQGfa2X22Zvuv2KjCmNCwKHkJHGXc8CmGcdmHHYMX9K, 8YmmwE7TWEUv7GhA3cbpDCnj8fNKGZA9bR9Qizifv9mB, HJaE5WCx4VNrbUa7GhNXNTTe11syxFuLd7YkSWLuvK5E, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA, 11111111111111111111111111111111, SysvarRent111111111111111111111111111111111, ATokenGPvbdGVxr1b2hvZbsiqW5xWH25efTNsLJA8knL"
                .split(", ");
        String base58Data1 = "87802f4d0f98f031fb00e3ffff00f8ffff";  // amountIn=1000000000, minimumAmountOut=100000
        testInstruction("testOpenBundledPosition Case 1", base58Data1, accounts1);
    }
    private static void testCloseBundledPosition() {
        System.out.println("\n=== Testing testCloseBundledPosition ===");

        // Case 1: tx_hash: 373ZESCCAbkDBTV4jbTUhjasfmfNw9xcRsu1uoKbfJUfDc3DLisw8GRPQSGTtm2PzM4X13RE9WCKrm6eioa5f1KX
        String[] accounts1 ="8ACEnJSDg57MzHk8nLtXN16BnWYmZXwEogAKdXdjSSEZ, CSTXMMdbSB6QVsDvF2duERHg3wggANs8RVPK7r8WSCsM, Ap627oe8sZUPGMrsT2rcTc2beEg2jhheimZEpN24cD5Y, FEESAorcqaALmwz7cSeUtWxQa7JVRhBkWK8BhQKnfarm, FEESAorcqaALmwz7cSeUtWxQa7JVRhBkWK8BhQKnfarm"
                .split(", ");
        String base58Data1 = "2924d8f51b5567434000";  // amountIn=1000000000, minimumAmountOut=100000
        testInstruction("testCloseBundledPosition Case 1", base58Data1, accounts1);
    }
    private static void testClosePosition() {
        System.out.println("\n=== Testing testClosePosition ===");

        // Case 1: tx_hash: gJggBd4RfHFNPKhU6hUAG7x56SyKK4k8v84iecWj3yXngzo6AhtMD7KtMr9fFvsfVLAtzL8FiQ193K5qVYHhban
        String[] accounts1 = "AwPtY47g7tMNFdp8Pn4owC1N9pM6StXXvePuDGvLwLC8, 61fertdALqc8Jy3k62CkfkhSMiHzhNE6Fy23vFeLBKfh, zZjPUfT4ZtaUeo52ieHB2nLCAqwLzDADUqx5ytgDDHW, GPqLCqDUBsw4DA7gCjcXdRb7FNmmaPtHjuShdWeJ8sa6, tXWoQD6C2C9KX2bjhumpCZcbDBhExXGRGc67oaiq9sn, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA"
                .split(", ");
        String base58Data1 = "7b86510031446262";  // amountIn=1000000000, minimumAmountOut=100000
        testInstruction("testClosePosition Case 1", base58Data1, accounts1);
    }

    private static void testCollectFees() {
        System.out.println("\n=== Testing testCollectFees ===");

        // Case 1: tx_hash: 2gs86sBVGzMS9qDemNYaNTBFKkHvHAfzLLh7UPYwwnoP2MHL1FEBJWek5d2Unvhf3WqLPVxZzhk5que5EXvokKD9
        String[] accounts1 = "8GsWExrRFeBj1Nh8gCuVQZo3f6yzd1YFofGbwebh7uFh, FM4nip2RVM8npyDsAL7FZA5kKtXTswe54t6UhJCdhHSS, CzidwW6nMD2SG3fxx6oo98S9BBJVMF2nYDxUT8VT2sAZ, AfMi2kUQp6YnjWmAL8hvwLiSTqkW4n4dpHvaNC7ve6CN, CC33uV52m1LAy2qwCHCBQinrrS8QLLbbxt7TkK4Whws2, HD4WL2ToqQj26pgeNY4A6C6JtaKy7avQzPxVqN54SJ6, Cwx6Gua5bfNzk7y458zpaYr2bb1QdrB1SgUTHBs5TPrW, ACQ1o1Kg28bPsz5PRbFfMCLdsqN6fKqdRoWn6yRZpGyA, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA"
                .split(", ");
        String base58Data1 = "a498cf631eba13b6";  // amountIn=1000000000, minimumAmountOut=100000
        testInstruction("testCollectFees Case 1", base58Data1, accounts1);
    }

    private static void testCollectFeesV2() {
        System.out.println("\n=== Testing testCollectFeesV2 ===");

        // Case 1: tx_hash: 5PYwMUD6wb8TNMCpRUeRi3EvDCtCjTviwGPvSwg76bsGMLyBo6S18ZdoAJYqiXhNhvTJSdqRQPwx9Gdp61ZEsLFM
        String[] accounts1 = "GMtQEzi8XkEjp2SbwnKJ6BnUM9ugkgpSQY1tafZHLxQo, HC6uWcuVpPLPD3JQiCdjtEgdtmisPiM1U6eN6MKt8bjr, 8JDhq4sy74rg5A1MKVuZW4RdzUVGDiWMkpDgLEzyMmDh, E3uYBkN2jQu8hBDiL1TAp5UwV1wPhL1qk1dXRvJymarQ, So11111111111111111111111111111111111111112, HeLp6NuQkmYB4pYWo2zYs22mESHXPQYzXbB8n4V98jwC, BBYd4fzKjZ6v13umqnK4Cs8SmL5bcKRzna7FdR6mqUJs, EomsF7FqRVLrAf7PN6QLLc1WihUBgahQrFvEQFqcxZyC, CBGtUS8dMYHevCFha3sr4pTLMiv2GTBXdKGnXLLZpyjF, EhKEfVaEa7feStM2pCTSxedG5Ya6NQ7rTLpLXKqLGTeS, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA, TokenzQdBNbLqP5VEhdkAS6EPFLC1PHnBqCXEpPxuEb, MemoSq4gqABAXKb96qnH8TysNcWxMyWCqXgDLGmfcHr"
                .split(", ");
        String base58Data1 = "cf755fbfe5b4e20f00";  // amountIn=1000000000, minimumAmountOut=100000
        testInstruction("testCollectFeesV2 Case 1", base58Data1, accounts1);
    }
    private static void testCollectProtocolFees() {
        System.out.println("\n=== Testing testCollectProtocolFees ===");

        // Case 1: tx_hash: 4Ji3DyRn3ZRFEEhyGJNRhLMgJcUujWKxwEDUthmB98EfXqsXXbCfL6eomZVSVj3r9r7D37tHMz3983772DoEMRR
        String[] accounts1 = "2LecshUwdy9xi7meFgHtFJQNSKk4KdTrcpvaB56dP2NQ, AXtdSZ2mpagmtM5aipN5kV9CyGBA8dxhSBnqMRp7UpdN, 3Pi4tc4SxZyKZivKxWnYfGNxeqFJJxPc8xRw1VnvXpbb, 5GXtHDjrM1okAYJZfrmUwpphcsSsLAJEVsn4qx5epZd, 7HxXmF3PE6oQeoxofLidBKMjvig2L8WFAGbHWLqEYcP1, 55rdM6jeyRnHHGyT1L9e7R7sJTH9pZK99N9mLK1efiWE, 2YhqnvDd113Qsa7tjDKDG9FFZef1JJt33T4yoBDHxv2D, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA"
                .split(", ");
        String base58Data1 = "1643176296b246dc";  // amountIn=1000000000, minimumAmountOut=100000
        testInstruction("testCollectProtocolFees Case 1", base58Data1, accounts1);
    }

    private static void testCollectProtocolFeesV2() {
        System.out.println("\n=== Testing testCollectProtocolFeesV2 ===");

        // Case 1: tx_hash: mwQ1L7hviFyU8YgqDEgJntkNUhWQpgiyWkAe8pU58wtx6mVkdcaFTjeoS3zUr6gtDhhCkEL5V24DPLr76eQzeAK
        String[] accounts1 = "GMtQEzi8XkEjp2SbwnKJ6BnUM9ugkgpSQY1tafZHLxQo, HC6uWcuVpPLPD3JQiCdjtEgdtmisPiM1U6eN6MKt8bjr, 8JDhq4sy74rg5A1MKVuZW4RdzUVGDiWMkpDgLEzyMmDh, E3uYBkN2jQu8hBDiL1TAp5UwV1wPhL1qk1dXRvJymarQ, So11111111111111111111111111111111111111112, HeLp6NuQkmYB4pYWo2zYs22mESHXPQYzXbB8n4V98jwC, BBYd4fzKjZ6v13umqnK4Cs8SmL5bcKRzna7FdR6mqUJs, EomsF7FqRVLrAf7PN6QLLc1WihUBgahQrFvEQFqcxZyC, CBGtUS8dMYHevCFha3sr4pTLMiv2GTBXdKGnXLLZpyjF, EhKEfVaEa7feStM2pCTSxedG5Ya6NQ7rTLpLXKqLGTeS, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA, TokenzQdBNbLqP5VEhdkAS6EPFLC1PHnBqCXEpPxuEb, MemoSq4gqABAXKb96qnH8TysNcWxMyWCqXgDLGmfcHr"
                .split(", ");
        String base58Data1 = "cf755fbfe5b4e20f00";  // amountIn=1000000000, minimumAmountOut=100000
        testInstruction("testCollectProtocolFeesV2 Case 1", base58Data1, accounts1);
    }

    private static void testCollectReward() {
        System.out.println("\n=== Testing testCollectReward ===");

        // Case 1: tx_hash: 2CaXH3wEJU4ViuoBr35JSEM1rYUuY9uYpw5mm5YzVUfcm3NuXZakFWxKoUtQCPjapKPCBjjE73sgoA3j9GvJM4M1
        String[] accounts1 = "9yZSFZaXkkEzmmaRwND9ZS4JSewWCWgE2fD9v75ek3EN, H52eTMUcGKc1hAqJbPMc6S2Xy4NXwqYRcBZtUYj6PuvD, DQDkXzEXw5WoBDVXnMYEqkMMpEY8GRurDQgMD85DSpe1, yjDig3g65XRe1QvBW9LZp38x8j7jWSxbffqonKNsX6F, 27yX2zrBjyLAmhUay3NtSZxkEy5GWhDKhBa6wKnUdaEj, 2NX6ECBxwWkbjzcisn2qqVWBrh1PwoBB5gvYsBfydwVk, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA"
                .split(", ");
        String base58Data1 = "4605845756ebb12200";  // amountIn=1000000000, minimumAmountOut=100000
        testInstruction("testCollectReward Case 1", base58Data1, accounts1);
    }

    private static void testCollectRewardV2() {
        System.out.println("\n=== Testing testCollectRewardV2 ===");

        // Case 1: tx_hash: 41GBht1rk3qmR1vEpPC5apCpdDuLCrSCgBn27fBxB3wWaeBGRqEeZp4Dxp9pya2TNEhkJ7Tqezza3nTYNDCiCG2B
        String[] accounts1 = "EzCFMMo43qLLkYQqhLG8Kjj4UL3Dwvk2paf7yqB575KP, GYPupztDPJL8NdW1KyYgghnFHCHcdKEJDAXmByY93qtw, jFDSh3N3zAPYNNLrH5hYAeNuepYwQUauMdEzcAveQQq, 9op1JvMQuVTZcj3eSCy4bkj7xF315Gw2spZ2CmLi7A1G, DVezoARgfi5WjD1X1Q4W2YRAt8NnntNDVnE26EhHteVw, orcaEKTdK7LKz57vaAYr9QeNsVEPfiu6QeMU1kektZE, E7VkAiwFK5MjkRrzrkeNXHLJYQX9jHbKbaDeZ7tHHcDN, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA, MemoSq4gqABAXKb96qnH8TysNcWxMyWCqXgDLGmfcHr"
                .split(", ");
        String base58Data1 = "b16b25b4a01331d10000";  // amountIn=1000000000, minimumAmountOut=100000
        testInstruction("testCollectRewardV2 Case 1", base58Data1, accounts1);
    }
    private static void testInitializeReward() {
        System.out.println("\n=== Testing testInitializeReward ===");

        // Case 1: tx_hash: 5YP5F1vSB2TNxiDa6TDaoWtodjSF6BC6kunNUeMp3VPHdxJAzCKMhyKZn4ZrYLsF69mLUQsnLUdVaExvRgWRTagV
        String[] accounts1 = "DjDsi34mSB66p2nhBL6YvhbcLtZbkGfNybFeLDjJqxJW, DjDsi34mSB66p2nhBL6YvhbcLtZbkGfNybFeLDjJqxJW, 6BFWHpnQA7BTHq8XXzuPFmKiZxwH866udVVXNvMrWPqB, ratioMVg27rSZbSvBopUvsdrGUzeALUfFma61mpxc8J, GthVzfLAXgw1fjzCxGNzaeAD9p1vbvgizXNGVcQ5LWkD, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA, 11111111111111111111111111111111, SysvarRent111111111111111111111111111111111"
            .split(", ");
        String base58Data1 = "5f87c0c4f281e64401";  // amountIn=1000000000, minimumAmountOut=100000
        testInstruction("testInitializeReward Case 1", base58Data1, accounts1);
    }
    private static void testInitializeRewardV2() {
        System.out.println("\n=== Testing testInitializeRewardV2 ===");

        // Case 1: tx_hash: eU5iX6F7Mg5NRfVboG44HSoqz52nBinW83ZHB7Jj3dh3zC6zdw8Fe7yyhwgNL7HqV55SDPhw9nM8MXwvUGiioTN
        String[] accounts1 = "DjDsi34mSB66p2nhBL6YvhbcLtZbkGfNybFeLDjJqxJW, DjDsi34mSB66p2nhBL6YvhbcLtZbkGfNybFeLDjJqxJW, 9Vh6fqJjDkqSTZ8bDXseVxGb2yQEMkEhhtte2anQCHSf, 2b1kV6DkPAnxd5ixfnxCpjxmKwqjjaYmCZfHsFu24GXo, HX5iftnCxhtu11ys3ZuWbvUqo7cyPYaVNZBrLL67Hrbm, 3NeH4RxZbmBUxRymdMhxkUauSUmrJVQzaWiKy2X42kii, TokenzQdBNbLqP5VEhdkAS6EPFLC1PHnBqCXEpPxuEb, 11111111111111111111111111111111, SysvarRent111111111111111111111111111111111"
                .split(", ");
        String base58Data1 = "5b014d32ebe5853100";  // amountIn=1000000000, minimumAmountOut=100000
        testInstruction("testInitializeRewardV2 Case 1", base58Data1, accounts1);
    }


    private static void testUpdateFeesAndRewards() {
        System.out.println("\n=== Testing testUpdateFeesAndRewards ===");

        // Case 1: tx_hash: 2cRvTLmVEiBfyEJVrTsLcctJUrvA2fcgrDsJrq9kHXHc5LDedzCWWJdTzhRnkUrXyymEzXFUPgqxk5YbVHAodYVn
        String[] accounts1 = "CeaZcxBNLpJWtxzt58qQmfMBtJY8pQLvursXTJYGQpbN, ELiSLhARGFQgJcQz2npZVocwjaArFrTMTDU5KbwPChA9, Dmb1Q2ewqRZ5MdTEXDknGd1joKYnzPDGrY5P2hMzfG8h, Dmb1Q2ewqRZ5MdTEXDknGd1joKYnzPDGrY5P2hMzfG8h"
                .split(", ");
        String base58Data1 = "9ae6fa0decd14bdf";  // amountIn=1000000000, minimumAmountOut=100000
        testInstruction("testUpdateFeesAndRewards Case 1", base58Data1, accounts1);
    }


    private static void testInitializeTickArray() {
        System.out.println("\n=== Testing testInitializeTickArray ===");

        // Case 1: tx_hash: ay1WTVQrynGakfqRovetmR262BX5w6xLP6aHYwCSQ1fE52npQMTgy9jjH83p4usnHexwzikPUmDPvHv9JYjJXNn
        String[] accounts1 = "EvhgMc6ZUWpSAZZp5GW27uiALfczQ8KXbC4mHcJsY4Y6, HMdxwon8wVaiCX2A8TVP7ywEqEE6R7MhZoQpkt4SiPaC, AESRBygWY6Au6mT1YP6vP3eoB8E3PAMMJnf7k4fjBZHx, 11111111111111111111111111111111"
                .split(", ");
        String base58Data1 = "0bbcc1d68d5b95b800000000";  // amountIn=1000000000, minimumAmountOut=100000
        testInstruction("testInitializeTickArray Case 1", base58Data1, accounts1);
    }


    private static void testInitializeTokenBadge() {
        System.out.println("\n=== Testing testInitializeTokenBadge ===");

        // Case 1: tx_hash: 2KPhJ2QCGh8a9L9JbGLFn1TmF9PQUFqd5owTdC2J6o7PvGQRjY7N6ALVd9kdBtdFKZw5fvw6im3jnpfK2dDCjCKb
        String[] accounts1 = "2LecshUwdy9xi7meFgHtFJQNSKk4KdTrcpvaB56dP2NQ, 777H5H3Tp9U11uRVRzFwM8BinfiakbaLT8vQpeuhvEiH, r21Gamwd9DtyjHeGywsneoQYR39C1VDwrw7tWxHAwh6, 2b1kV6DkPAnxd5ixfnxCpjxmKwqjjaYmCZfHsFu24GXo, HX5iftnCxhtu11ys3ZuWbvUqo7cyPYaVNZBrLL67Hrbm, r21Gamwd9DtyjHeGywsneoQYR39C1VDwrw7tWxHAwh6, 11111111111111111111111111111111"
                .split(", ");
        String base58Data1 = "fd4dcd5f1be059df";  // amountIn=1000000000, minimumAmountOut=100000
        testInstruction("testInitializeTokenBadge Case 1", base58Data1, accounts1);
    }

    private static void testInitializePositionBundle() {
        System.out.println("\n=== Testing testInitializePositionBundle ===");

        // Case 1: tx_hash: iWthRP59cPNPr5dX5QAyiooEUJwh9swRVnq9Ag4LBKQQJXf8TGV4qTzmmstiiX9pjwTUAbramxesEJK8z1jGfNx
        String[] accounts1 = "oSjp2qi7yftRLkFP3XukbnhHWGLX8CZRnefckrTioYR, C8emFDF9ws6CWzdPp83EBeGnjbNSb2FWAZDsAKS1j5Cp, H4TkTjeLPr57LtonimJ41t1GYaT8UDq2Ra2iEVkXnuxe, GyU6WALGDKvB6txPDesPMceeeFqTLdp5AhsoJDLAFSFC, 7VKPHnP2MnahWVAZs7WadH1AWPfGnChS1H6qknEbRPDd, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA, 11111111111111111111111111111111, SysvarRent111111111111111111111111111111111, ATokenGPvbdGVxr1b2hvZbsiqW5xWH25efTNsLJA8knL"
                .split(", ");
        String base58Data1 = "752df1951812c241";  // amountIn=1000000000, minimumAmountOut=100000
        testInstruction("testInitializePositionBundle Case 1", base58Data1, accounts1);
    }

    private static void testInitializePositionBundleWithMetadata() {
        System.out.println("\n=== Testing testInitializePositionBundleWithMetadata ===");

        // Case 1: tx_hash: 4NR6e8CunnSvkxMYdmD4pSvgKXNHyaMTnVM3RoKSpQNypKALcdS9wwrFHEvNr59JBCxCUW4WbFGyoyBcm7AtqcUo
        String[] accounts1 = "EmB35WeEpdBrZ84G2aUnyniqDbdDMAwfhCsBANp3vyQa, 77DBAN11v1UU2NNxb5UXopK8S1R2hKGS6QMXiJQyiX2R, AzLNfHbfys4vQnpCavCwijGirtiAnVPxazCvGhVT1Gop, CnkENFbstLfVQ447FE8dz7VoixPMd6Fuw16cMw6piLd5, r21Gamwd9DtyjHeGywsneoQYR39C1VDwrw7tWxHAwh6, r21Gamwd9DtyjHeGywsneoQYR39C1VDwrw7tWxHAwh6, 3axbTs2z5GBy6usVbNVoqEgZMng3vZvMnAoX29BFfwhr, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA, 11111111111111111111111111111111, SysvarRent111111111111111111111111111111111, ATokenGPvbdGVxr1b2hvZbsiqW5xWH25efTNsLJA8knL, metaqbxxUerdq28cj1RbAWkYQm3ybzjb6a8bt518x1s"
                .split(", ");
        String base58Data1 = "5d7c10b3f98373f5";  // amountIn=1000000000, minimumAmountOut=100000
        testInstruction("testInitializePositionBundleWithMetadata Case 1", base58Data1, accounts1);
    }

    private static void testInitializeConfig() {
        System.out.println("\n=== Testing testInitializePositionBundleWithMetadata ===");

        // Case 1: tx_hash: 4eUNkWUTYKoq6Hxv5DahKFo2Luei5aRNDQqcYDxUGsyTfa6jZB6Rzn7NhShLRdXxS6rRkqj8QEGNqQfnLnqbZhj8
        String[] accounts1 = "BymGuv8XRMYywTz4h8Sv7qP1kHUWEvPBx63bq8DS75NS, GnTdsyRzW2pdAeWbfWwU2Uut6ghLfW6R1dsDUyDEUHUU, 11111111111111111111111111111111"
                .split(", ");
        String base58Data1 = "d07f1501c2bec446ea848ebb5c118ea6bdab5c7f12094456c10c81db4c47dfa5805cd1587fc23181ea848ebb5c118ea6bdab5c7f12094456c10c81db4c47dfa5805cd1587fc23181ea848ebb5c118ea6bdab5c7f12094456c10c81db4c47dfa5805cd1587fc231812c01";  // amountIn=1000000000, minimumAmountOut=100000
        testInstruction("testInitializePositionBundleWithMetadata Case 1", base58Data1, accounts1);
    }

    private static void testInitializeFeeTier() {
        System.out.println("\n=== Testing testInitializeFeeTier ===");

//        // Case 1: tx_hash: 4eUNkWUTYKoq6Hxv5DahKFo2Luei5aRNDQqcYDxUGsyTfa6jZB6Rzn7NhShLRdXxS6rRkqj8QEGNqQfnLnqbZhj8
//        String[] accounts1 = "BymGuv8XRMYywTz4h8Sv7qP1kHUWEvPBx63bq8DS75NS, GnTdsyRzW2pdAeWbfWwU2Uut6ghLfW6R1dsDUyDEUHUU, 11111111111111111111111111111111"
//                .split(", ");
//        String base58Data1 = "d07f1501c2bec446ea848ebb5c118ea6bdab5c7f12094456c10c81db4c47dfa5805cd1587fc23181ea848ebb5c118ea6bdab5c7f12094456c10c81db4c47dfa5805cd1587fc23181ea848ebb5c118ea6bdab5c7f12094456c10c81db4c47dfa5805cd1587fc231812c01";  // amountIn=1000000000, minimumAmountOut=100000
//        testInstruction("testInitializeFeeTier Case 1", base58Data1, accounts1);
    }
    private static void testSetRewardEmissions() {
        System.out.println("\n=== Testing testSetRewardEmissions ===");

        // Case 1: tx_hash: 46d5LnmD3SaDzKpZfDQb463qJsvuvUiUMCjtewNiXRzjjnuSHhvoUkYcRKoTrVgEN63n3k7djofp8wgH1URFYUHk
        String[] accounts1 = "5Z66YYYaTmmx1R4mATAGLSc8aV4Vfy5tNdJQzk1GP9RF, DjDsi34mSB66p2nhBL6YvhbcLtZbkGfNybFeLDjJqxJW, 6k39qmBR4sBxtzfBnyFpZbjx1TxkrEwQGot7Q1cuJVp"
                .split(", ");
        String base58Data1 = "0dc556a86db01bf40000000000000000009b16000000000000";  // amountIn=1000000000, minimumAmountOut=100000
        testInstruction("testSetRewardEmissions Case 1", base58Data1, accounts1);
    }
    private static void testSetRewardEmissionsV2() {
        System.out.println("\n=== Testing testSetRewardEmissionsV2 ===");

        // Case 1: tx_hash: 2czz5hMrxBqysKuMYe9RF5nWtcC4CHgugCvFF7rukbiSyYQh2YYduYTCNnUmkUY7LEcM7JgemqVvkwNnigxsYGdp
        String[] accounts1 = "J7sbSyagrP3YoGPvLt1A92vjHXX7x9dhR7CV28n4vZWR, DjDsi34mSB66p2nhBL6YvhbcLtZbkGfNybFeLDjJqxJW, EZPRxGaDh8y4urGX54okAJP9MK6ZJ2eWm3Z983wymsVg"
                .split(", ");
        String base58Data1 = "72e44820c130a0660000000000000000009938bd0000000000";  // amountIn=1000000000, minimumAmountOut=100000
        testInstruction("testSetRewardEmissionsV2 Case 1", base58Data1, accounts1);
    }

    private static void testSetRewardEmissionsSuperAuthority() {
        System.out.println("\n=== Testing testSetRewardEmissionsSuperAuthority ===");
    }

    private static void testSetRewardAuthority() {
        System.out.println("\n=== Testing testSetRewardEmissions ===");

//        // Case 1: tx_hash: 46d5LnmD3SaDzKpZfDQb463qJsvuvUiUMCjtewNiXRzjjnuSHhvoUkYcRKoTrVgEN63n3k7djofp8wgH1URFYUHk
//        String[] accounts1 = "5Z66YYYaTmmx1R4mATAGLSc8aV4Vfy5tNdJQzk1GP9RF, DjDsi34mSB66p2nhBL6YvhbcLtZbkGfNybFeLDjJqxJW, 6k39qmBR4sBxtzfBnyFpZbjx1TxkrEwQGot7Q1cuJVp"
//                .split(", ");
//        String base58Data1 = "0dc556a86db01bf40000000000000000009b16000000000000";  // amountIn=1000000000, minimumAmountOut=100000
//        testInstruction("testSetRewardEmissions Case 1", base58Data1, accounts1);
    }

    private static void testSetRewardAuthorityBySuperAuthority() {
        System.out.println("\n=== Testing testSetRewardAuthorityBySuperAuthority ===");

        // Case 1: tx_hash: 37tu2Dkxup9YuJ8nv8htLLtBV7rUGnDfxYGkvvqmeEh8g14six5BusLQ55exfQHSCXCJa5qRhkCzK3nSaKUf1339
        String[] accounts1 = "2LecshUwdy9xi7meFgHtFJQNSKk4KdTrcpvaB56dP2NQ, AiMZS5U3JMvpdvsr1KeaMiS354Z1DeSg5XjA4yYRxtFf, DjDsi34mSB66p2nhBL6YvhbcLtZbkGfNybFeLDjJqxJW, DjDsi34mSB66p2nhBL6YvhbcLtZbkGfNybFeLDjJqxJW"
                .split(", ");
        String base58Data1 = "f09ac9c6945d381900";  // amountIn=1000000000, minimumAmountOut=100000
        testInstruction("testSetRewardAuthorityBySuperAuthority Case 1", base58Data1, accounts1);
    }
    private static void testSetDefaultFeeRate() {
        System.out.println("\n=== Testing testSetDefaultFeeRate ===");

        // Case 1: tx_hash: 5Q7BYpk8Ee7xBbJRXA1MrXkavGA5UXAw24cMDsALjXNJSgEHcN7Bz4JMPCJ71PDFRUCn3V1HvCQNZ5NoAHYcxM3c
        String[] accounts1 = "35VNM2KsKZKxWpEvde4NHrcfuCfXtKfdCffUmSTceKeC, D96VqzfAZb6fmW1j52ZmYfX1ZFP7AVmBSYpRgRmx57PY, 7btJUBpZq3pzhYJBF9pkCBJ4xa4de1ZnUeJ4WiA5TiHT\n"
                .split(", ");
        String base58Data1 = "76d7d69db6e5d0e4c409";  // amountIn=1000000000, minimumAmountOut=100000
        testInstruction("testSetDefaultFeeRate Case 1", base58Data1, accounts1);
    }

}