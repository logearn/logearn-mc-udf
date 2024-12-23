package cn.xlystar.parse.solSwap.raydium.clmm;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.util.Map;

public class RaydiumClmmInstructionParserTest {

    public static void main(String[] args) throws DecoderException {
//        testSwap(); // 通过
//        testSwap2(); // 通过
//        testCreatePool(); // 通过
//        testClosePosition(); // 通过
//        testOpenPosition(); // 通过
//        testOpenPositionV2(); // 通过
//        testIncreaseLiquidity(); // 通过
//        testIncreaseLiquidityV2(); // 通过
//        testDecreaseLiquidity(); // 通过
//        testDecreaseLiquidityV2(); // 通过
//        testCreateAmmConfig(); // 通过
//        testUpdateAmmConfig(); // 通过
//        testUpdatePoolStatus(); // 通过
//        testCollectProtocolFee(); // 通过
//        testCollectFundFee(); // 通过
//        testSwapRouterBaseIn(); // 通过
//        testCreateOperationAccount();  // 通过
//        testUpdateOperationAccount(); // 通过
//        testUpdateRewardInfos(); // 通过
//        testCollectRemainingRewards(); // 通过
//        testInitializeReward(); // 通过
//        testTransferRewardOwner(); // 通过
        testSetRewardParams(); // 通过

    }

    private static void testInstruction(String testCase, String base58Data, String[] accounts){
        try {
        System.out.println("\n=== Testing " + testCase + " ===");
        byte[] decode = Hex.decodeHex(base58Data.toCharArray());
//        byte[] decode = Base58.decode("15P");
        Map<String, Object> result = RaydiumClmmInstructionParser.parseInstruction(decode, accounts);
        System.out.println("Result: " + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void testSwap() {
        // Case 1: tx_hash: 2oTLwK47V8KavdTyxLNDZkufEfsm57iZvkZQJwrnzQc3XKjFJr4wEHCxxRqNfgZR7ggTyjhFQ7Dh2gE8g8hGGSJV
        String[] accounts1 = "GP8StUXNYSZjPikyRsvkTbvRV1GBxMErb59cpeCJnDf1, 9iFER3bpjf1PTTCQCfTRu17EJgvsxo9pVyA9QWwEuX4x, 3nMFwZXwY1s1M5s8vYAHqd4wGs4iSxXE4LRoUMMYqEgF, GyY4VgEpJQhiKZRAJJmoM4hv5Q2xC4pvX68MGrGidxyG, GA3vDJofNobRY9tWbQpoA7spgJ6LWhJTo51CjKkKWqkc, AbcuyoPeYnddzFoFQudsiFka8qd6tTwvLgxwtpTKTpKC, 2n6fxuD6PA5NYgEnXXYMh2iWD1JBJ1LGf76kFJAayZmX, Cqb16WaM7dDDP8koxYASDJLWgan4STDB1R3LiSH8r3GR, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA, A8UkBjyEy2FdCcxi7GTYNqTuY45d2PVpTkP2WyGuvrkp, 2ncinnTcJxbZ1nUHavBVJ3Ap3R4CE7p2LJ6Jtpd1vLzd, 4whzu1hcYVG4VPRX8h9VGugiS6Nbze8ybscuyoaWo1Yj, ALd6in9wZtzbHjncXFRGGho2Rr82dvFs2xhMC6nmvd1u".split(", ");
        String base58Data1 = "f8c69e91e17587c89c25f68a0000000000000000000000000000000000000000000000000000000001";  // amountIn=1000000000, minimumAmountOut=100000
        testInstruction("testSwap Case 1", base58Data1, accounts1);
    }

    private static void testSwap2() {

        // Case 1: tx_hash: 5yciL4Ez4ij2rkLEm8tvnhWTf4Q1wA7RSPo6YT2EGz7pNSdV8K8AhduzdFZaQMmE4frBRUXcGYvvrsHD94xXafoc
        String[] accounts1 = "8ucx97WvvWfvLWnh951wodFjYp2BBT9FdgpU8j1154i3, Gex2NJRS3jVLPfbzSFM5d5DRsNoL5ynnwT1TXoDEhanz, CdZU7gSNtZ5tzvYpWXijHaH5wMSGYzCUyjpTW29kAWr2, HWQqeYsvTvzjMmKaJW21bfR6DgtQjxZhB2A7fFa1nUJh, 5RyruV6omz3E9haW3cYHL73cvHFJoyyazQsU2qk2wNKS, 6AL88BX5TFFJ5H1MVHbtHaCmfmbcH1udh1fvBYbNxrk5, 3ZZDCWSPdCiSZjJqkaVE1cJRiwodft3rveY1tCvTe9Ee, 8sX9eRY27ytCuDgbSJNJCnRiz9J7d41JoCmoibMUJF9h, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA, TokenzQdBNbLqP5VEhdkAS6EPFLC1PHnBqCXEpPxuEb, MemoSq4gqABAXKb96qnH8TysNcWxMyWCqXgDLGmfcHr, So11111111111111111111111111111111111111112, CX9YDTED9TWgVXVoFy8JL9gSSAEWcTv4mzjLSv17LQsj, EG4j44t2vShtcNXYELmzMqr1oPkwfQg458QZhZXi5Ucy, 6xrVuDoZwcfKkhKnkA86vgjeeLSvHKuoQLcy7ksZf7T5, Gfw3d2MWgC1zmPT1XWfUS21B2nf9F9SQZ7UAvSpL3Vrd, 3gaCJ31rFnj2F2LkAzpyFN9j27B5jQjXCVDfbAYhzJgw".split(", ");
        String base58Data1 = "2b04ed0b1ac91e620065cd1d00000000e8ec02ca11020000513b010001000000000000000000000001";  // amountIn=1000000000, minimumAmountOut=100000
        testInstruction("testSwap2 Case 1", base58Data1, accounts1);
    }

    private static void testCreatePool() {
        // Case 1: tx_hash: 4oAuSD3YXLZHzzXsXoVd2r4182VqfRrDCnCk9sv2xoEQnVwL7U2isWeRvjBKL3ARdEEvLjKKWAt8pfn7ngFmVGyQ
        String[] accounts1 = "4Yx8JQHEHbmafTmHMPF2RQqLbVF9mf44PQEJV6mmaJQF, 9iFER3bpjf1PTTCQCfTRu17EJgvsxo9pVyA9QWwEuX4x, 86RqXc2ez8veMAdtGXGuP2A4qAKTDfjigSQwfyUaD3rS, 8SKCz9wtNR5hVCno22T1a8uKaa4qcSdwfkQCv6Xjaegy, EPjFWdd5AufqSSqeM2qN1xzybapC8G4wEGGkZwyTDt1v, CxJheyD4U1cuksHYpyrcf2BPjqmKr6Dgj56eKaHroaar, 6V5CkKwgqZKLJ14h48q1jda3tCTBv38fZCbbZLwReMQH, FbeJGcUnhbmhnmbAMbnjy67u9mLngpacs272tFvrx7MT, 9b1qhMAQrGUUPPJz8BPZhszRA2ooyinv83iaKuVpJndw, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA, 11111111111111111111111111111111, SysvarRent111111111111111111111111111111111".split(", ");
        String base58Data1 = "e992d18ecf6840bcff8fcc301303ab380000000000000000c14c696700000000";  // amountIn=1000000000, minimumAmountOut=100000
        testInstruction("testSwap2 Case 1", base58Data1, accounts1);
    }

    private static void testClosePosition() {
        // Case 1: tx_hash: 3VCSdhvHnhWQfyCdzZrgMVJ9aoXnAqqygVoxP1YHjBgrT7oUDVUve68FMh6qaz8YEnJp8ZvD3uX6CDASvHUmb4hZ
        String[] accounts1 = "8Bicz24aLP3hyx7bLBmVHBjhPpAHmrm5TzGpsSmRWmyQ, 6jtMbNBpFyu6caFTSNL34ttXbEyzpWDSiAhXjfnXgsYT, EKzT6zXm2KvioFT6pS9FKiTscFEewUMg8CZGYJqyUae7, AFxjRLoGmTRFK2zb6xUTPz1mgR6x5BxJyYUpeTEUtPdh, 11111111111111111111111111111111, TokenzQdBNbLqP5VEhdkAS6EPFLC1PHnBqCXEpPxuEb".split(", ");
        String base58Data1 = "7b86510031446262";  // amountIn=1000000000, minimumAmountOut=100000
        testInstruction("testClosePosition Case 1", base58Data1, accounts1);
    }

    private static void testOpenPosition() {
        // Case 1: tx_hash: 4ooKFsPvzjrskP3meokLix9tQ22M6hoaKQYmLP4EvpN2m5Q3H5WesxacoVJLD5735WFJZhgjWzqJkciwLWSmAcMJ
        String[] accounts1 = "BabJ4KTDUDqaBRWLFza3Ek3zEcjXaPDmeRGRwusQyLPS, 51nMmaNa4DkqEwxVTQdVEvLGy9uuCk7iwqgfSktySv4D, 36xwF6P5AbNNXT1HFT6H9RBS28JkQAyM2GKm5Hr2yR3T, 7Pc1YJTW3YhVCUY2Bb6ygFuPQjzXynCpdZ61hZBgzD6k, 5CUZMARtA66LNMgg5gF6nNh41ZCftGBJ7gBJCqcaXP8C, 9RCPYmG5C8SnNuK4ojTQmbmdxxVR5mGWo79qMTzVLSMu, 4cWRXam5L9V4yfn56XNT6SN3YRJgNtivm2qCGLLwbMoH, 9hcmXkfEWjPykrW8F98bARE46VWCF1DS5iUvThgnpSKF, 9hcmXkfEWjPykrW8F98bARE46VWCF1DS5iUvThgnpSKF, 6tPeErC6zLXPCGVKkTpZCX7hWo3Hk9Wt7UkHx4J3xQc1, 8MVNiFm7be5dmNqfRDRq38CXPAXsKj9X1UoHrcsPPZzn, Cpqh8f23RpwRnzSWhBWtgVKn766pzJvHY7ymVCnQCk7d, 29Fthg52ZkjxRsQsd7PYhAYQZd6TVftNFFcYaY8m8M7E, 7qLiT9HWFbbSgqT7c3C8MnBK1oMwdL3geNUMRXMXT9HE, SysvarRent111111111111111111111111111111111, 11111111111111111111111111111111, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA, ATokenGPvbdGVxr1b2hvZbsiqW5xWH25efTNsLJA8knL, metaqbxxUerdq28cj1RbAWkYQm3ybzjb6a8bt518x1s, BVqLaSiAmT8Pfokmh5XHZeD3iW8BDS6UpntRm1qnCZiT, F5uGPiNVTAWDARo2mt4ZF8MrumKm8heu2t3pbn9HPYHZ".split(", ");
        String base58Data1 = "87802f4d0f98f031880e0000c40e0000100e0000100e00000000000000000000000000000000000000000000000000000000000000000000";  // amountIn=1000000000, minimumAmountOut=100000
        testInstruction("testClosePosition Case 1", base58Data1, accounts1);
    }

    private static void testOpenPositionV2() {
        // Case 1: tx_hash: 3tyehX1DeoyPSFbh194wyVVeRxKZWRGKZgd6UwvDLFdUf1jbwcUyRdRT3gnXka8PjZAXxCwMt5NvDQ11KXHymuNQ
        String[] accounts1 = "FBrpnyXLoGCJ6VSpw6FE2U3FqYAwF7PWLQU92wHxFyHv, FBrpnyXLoGCJ6VSpw6FE2U3FqYAwF7PWLQU92wHxFyHv, 8ZTLQbCRe1Tbjj38bS6yTYWExKUGt4kjFu2mUoZJXhmZ, Dmx8up11iDEdBrbSqkq9Wt4tgdoACBbP3w2vZJiYW1e7, 9SLdJ7bSPpcLEkBApXRss9W2PWXbyhMX3gYDiwPZKaQw, 4uRh2az1zgAZXizbDS5M2kWEMyBJJnhLREHmw2e9X9RV, 25aZypm3jm4PFUkpGMmVqgLrhJ4Exp1y41fD7JFdduTG, CyDNmEoefMwTe6qjb9iB4isagAFh4kcAwCZrBgtifvqP, CyDNmEoefMwTe6qjb9iB4isagAFh4kcAwCZrBgtifvqP, 9p3R237geGfsu4qvFEC8yH2Ni7u65gSbSpCfXMdv2ika, 2dFVH71mVdquQuX7jhYUqKQV4QB4ZS1ob5SZCcaT1giQ, EfFadJ7GEehthg1NgTUw3RL5d7sLDnRoEGcLtcG1M4AM, GNcqTWJvidWogMqjYTecCRBhWqubUk7FN5pxkvxYm56H, 6ihwiDnhe5Dc4Xw2ZCYpHnq1kdvy915N8AdKEpuS39J4, SysvarRent111111111111111111111111111111111, 11111111111111111111111111111111, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA, ATokenGPvbdGVxr1b2hvZbsiqW5xWH25efTNsLJA8knL, metaqbxxUerdq28cj1RbAWkYQm3ybzjb6a8bt518x1s, TokenzQdBNbLqP5VEhdkAS6EPFLC1PHnBqCXEpPxuEb, So11111111111111111111111111111111111111112, 74SBV4zDXxTRgv1pEMoECskKBkZHc2yGPnc7GYVepump".split(", ");
        String base58Data1 = "4db84ad67056f1c7e8440000584d00004038000040380000c82236290400000000000000000000008bbbb30d0000000084f40dd400000000010100";  // amountIn=1000000000, minimumAmountOut=100000
        testInstruction("testClosePosition Case 1", base58Data1, accounts1);
    }
    private static void testIncreaseLiquidity() {
        // Case 1: tx_hash: 4EGBx3uQjBEaHZF6x9qdz5NJQhR4vXXeNbcc55jCPCMM6NBMhdEmXFVrBdf2zvyyJnidiigNqLyh9ckz4PbtNkB6
        String[] accounts1 = "2PA56LMZiAHANDcimEjpgxjiHv1ETj1Aj4eYoxNXT6Dy, AzFW5Da9JAka2dstFb1fm6TDS7EZJL6LzGDRAFGJjCU1, 2QdhepnKRTLjjSqPL1PtKNwqrUkoLee5Gqs8bvZhRdMv, DDXqLbnqHWYaY8VPWaCkeKVkcyuVabKKNQwxvoPehy6V, 6AurFvc9toGkDW7wX58Jes4XWhaNVZRRDi92CCenR8g5, 4zNMSCni6fNg23uvgY8LjrZWYa1f4gnUHMM4u7CLs9FD, DugZNXBvwVtDQZHkCcTrJaXaDFrXi9QHqigt4QfBHZQM, 3Qj3oDW4sgx7gyapqAcFSh2NttdC3LxX9bMtgZrzr5gP, BabfQ9BnJNm6yCpTZwzSkxWcBLb98XzdbEqxRnwNoKCw, E2BcoCeJLTa27mAXDA4xwEq3pBUcyH6XXEHYk4KvKYTv, 4d35yC7C8zhCDec7JbPptL9SEb4NUddKHxURgmvD8hfo, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA".split(", ");
        String base58Data1 = "2e9cf3760dcdfbb209d809030000000000000000000000004ee3880200000000773e350000000000";  // amountIn=1000000000, minimumAmountOut=100000
        testInstruction("testIncreaseLiquidity Case 1", base58Data1, accounts1);
    }

    private static void testIncreaseLiquidityV2() {
        // Case 1: tx_hash: 4bPm2wZcaAVWq21Y5E5xVcP2TF4HdcN39tuZLDMMzhfiSRDqkHoCKozyhWY9UCdPiiVmVLMVCJx2cFUgDgbTYM4F
        String[] accounts1 = "fjaRgJEeUfxrcoSFd8i6wxF8PdDibetfvVZta1eB4fT, 2nQ2hM8KoTHbhWLrvb9KoNXHqUJtje8oRCAF9ygKQJgy, BjZKz1z4UMjJPvPfKwTwjPErVBWnewnJFvcZB6minymy, 3J3py5U62HzmChAgUPjDVGyT1m1SJ7FcxgYByednVXg7, Fory4bSPLo8hJyUk7vPYcanh8qr6TK8tJBEZEHt2kke2, 2Bfo7bN4Kz2QFq3CSSTZv3y1pFj9KyXvHUkrR5No4cGu, 356PY9nEafGh7aa2hxCPU21NLcZ9rbLYTH4UEnaLvc9v, FtYbV7vPC2zK5u5Q7PYpTjbXVF7hMLZwZyhm4rj1kNuh, A1CZ3km7Jys2r9sNmhYXgVwFuUmz8DGbkS4Deo9fxh2F, Q7UFU9VeeTCuMVGJdp6bukcySc2LVrafhcnAM6SPG9W, 2LUJugRCHHo8gcJkQtGivx4LbRZKPRDZua5BoMUz1Z5B, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA, TokenzQdBNbLqP5VEhdkAS6EPFLC1PHnBqCXEpPxuEb, So11111111111111111111111111111111111111112, DezXAZ8z7PnrnRJjz3wXBoRgixCa6xjnB7YaB1pPB263".split(", ");
        String base58Data1 = "851d59df45eeb00ae684fcaa05000000000000000000000075f22c05000000008155f9a70d0000000000";  // amountIn=1000000000, minimumAmountOut=100000
        testInstruction("testIncreaseLiquidityV2 Case 1", base58Data1, accounts1);
    }

    private static void testDecreaseLiquidity() {
        // Case 1: tx_hash: 5c9gNsfpJguK7e3sadVu52zdm3c24LzjiPrkG1vPTdH1szewkb42NLFqicr2ErDH9yj654ygXCvfaZEe2KYvto9R
        String[] accounts1 = "5K5ivYKtaNCF2ogCL2jTUn65T5VBo4ybeYa2hykRWk6M, 3eo1Kg9gKKH6GCsFrWEH2kuAC3rhjNAFnwUBveQhT1MM, 2YoWxGawoLbvdJgdZJFwfZtoFewu1AEwtMsAW4LHrExd, HCfytQ49w6Dn9UhHCqjNYTZYQ6z5SwqmsyYYqW4EKDdA, 59yHkJREMuUF5azLiwyDrdxu1kpjtyHT9zGzUCLt7Fj, FnytZ88bXZoPLCtHQ9odBuHtbATxJY5VvzyaAa9LmZWL, FZMUNsPggujx7aQF9RY129fjKfvFTyXBmA8x2Y1P6wSJ, 7KmmmoTVw8njoqFzZCbgJjDPrKZrCN3caGmvrdSTrs7f, 8HFbBET9o64TnSeHU2tQwRYjfZUGtZCq3agtGj4oXVPp, DokJNiC1pnJJnJgARiDrJoVEMtPSJHaXi1P23Ydgeg3u, t6AG37jf9F5vHX2toshMYwWAeoe1GXbiqFmwFZb3Tw7, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA, 9PNZim5sACD3fdEFCFWMM2vxoiCDtYXUiHocrncTa6St, EtXr7zDGJ2UzH75crwJe2DKjzzWaPHPx46isCZjRFiRS".split(", ");
        String base58Data1 = "a026d06f685b2c010000000000000000000000000000000000000000000000000000000000000000";  // amountIn=1000000000, minimumAmountOut=100000
        testInstruction("testDecreaseLiquidity Case 1", base58Data1, accounts1);
    }
    private static void testDecreaseLiquidityV2() {
        // Case 1: tx_hash: 2tMZpyeD9h1oq7vDmZWBQoYNb8SeDu2E1ru55oEcm2uYzi6qcj3nZZXcmMk5HwFS45qtk33cE2EtnC7M4DmD1yBg
        String[] accounts1 = "3QcwPNx8MkaMkrJjqG31qKbzrVwnkStgzEqgWQg9o7Zh, 23CJDn1aeEDrq9YHi6ZB6vyT91yLw3eC7eR6Bhr4EGwT, CyrJ4u2pHbyEVYe45bggapPcsJuwKWxPnUT6vrexWkxT, 7ESTvPaxJGbfGTZgHtmKEfJxQYjNsKfyTaKK23CZnrLF, AtGK2GbTtDZc3xcjAkP8vCdAgqRnKoPW5A3YNjDopJpu, CnWPdB9o5ABFYEsnXgDY8nowHaDH3xNiCBopG7R87qHU, HqHCDvP4VG5HcjXux5LnBiaixvQtyiP43YEVwmQXqUhz, gTTsKuq53u9WX1CoMyESxJ7FsURyy2dgSJQEzqLDXoZ, Ct72fLU845U5UzMq4nizc3jUkdabEkDHHNMDoZRPJa5g, Edav7HiKEvYrYZvutCvSLmUVWz5GZ5eM3xwrsUxS34V4, 3BchP5J47Ga3dAj2vWMDdbxjYYnAE6XhRHYiye626yA4, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA, TokenzQdBNbLqP5VEhdkAS6EPFLC1PHnBqCXEpPxuEb, MemoSq4gqABAXKb96qnH8TysNcWxMyWCqXgDLGmfcHr, KQijDbNJ6rPCqhtXrfH6gKa5cH3a39At8vHq34nnPbF, So11111111111111111111111111111111111111112".split(", ");
        String base58Data1 = "3a7fbc3e4f52c4600000000000000000000000000000000000000000000000000000000000000000";  // amountIn=1000000000, minimumAmountOut=100000
        testInstruction("testDecreaseLiquidityV2 Case 1", base58Data1, accounts1);
    }

    public static void testCreateAmmConfig() {
        System.out.println("\n=== Testing CreateAmmConfig ===");

        // Case 1: tx_hash: afHpNUw1hp3WF8ASmpSzYzV1bHsBXXuRjiThGyqSyySBi8Ty4vUAMN7eq4T6DkuPgGU9KGUNY9tLsbkJyjDSser
        String[] accounts1 = "GThUX1Atko4tqhN2NaiTazWSeFWMuiUvfFnyJyUghFMJ, EdPxg8QaeFSrTYqdWJn6Kezwy9McWncTYueD9eMGCuzR, 11111111111111111111111111111111".split(", ");
        String base58Data1 = "8934edd4d7756c6806000100c8000000c0d40100409c0000";  // configVersion=1
        testInstruction("CreateConfigAccount Case 1", base58Data1, accounts1);
    }

    public static void testUpdateAmmConfig() {
        System.out.println("\n=== Testing UpdatePoolStatus ===");

        // Case 1: tx_hash: 3HhyG3KQYVqinHJuCQ8sryc63nJBw4t6MvFCfM19XTyAskTg231rtpFs6zGPc3a5Snr6oVVLYcAJUNtCJpHbGSDF
        String[] accounts1 = "HggGrUeg4ReGvpPMLJMFKV69NTXL1r4wQ9Pk9Ljutwyv, E64NGkDLLCdQ2yFNPcavaKptrEgmiQaNykUuLC1Qgwyp, NCV2Uo3hfW5LSZXAJe19y6SpC5K98PuQwShCSZgTki3".split(", ");
        String base58Data1 = "313cae889a1c74c80300000000";
        testInstruction("UpdateConfigAccount Case 1", base58Data1, accounts1);
    }

    public static void testUpdatePoolStatus() {
        System.out.println("\n=== Testing UpdatePoolStatus ===");
        // Case 1: tx_hash: 5QerTGg7wFuNmiio9oVdSgiiwRnEeiey7F5JBTcZA2mT7DkuFWa6rBwQAZtxPEFSgWqFpmWcNTo18YpY7y6CQLXn
        String[] accounts1 = "GThUX1Atko4tqhN2NaiTazWSeFWMuiUvfFnyJyUghFMJ, 77e14Dkf6K2Hk5YMJpputqxqi3mvyhZH9R1wNaxPvdW".split(", ");
        String base58Data1 = "82576c062ee0757b11";
        testInstruction("UpdateConfigAccount Case 1", base58Data1, accounts1);

    }

    public static void testCollectProtocolFee() {
        System.out.println("\n=== Testing CollectProtocolFee ===");

        // Case 1: tx_hash: 5Y3vBHaftkLm1so3id7mBkAqWHXLACahSvzVEiAoo2dyaxutqi2KYAX899U4VmCETW6G3WngbQWFTNh5yhLsr1Bc
        String[] accounts1 = "projjosVCPQH49d5em7VYS7fJZzaqKixqKtus7yk416, UqYKdsVCTjv7gRtkhtYAu7RYQXVxFKadFrhoQ5iPyHu, E64NGkDLLCdQ2yFNPcavaKptrEgmiQaNykUuLC1Qgwyp, FmdJkNsuvTKXAaAPMUytozFv3hiDrMjDKPsgnk9vtWSb, D2dLFAfHmC5hBgVFWkevqqZ7QviA3tob4cMMTfjqaVk6, So11111111111111111111111111111111111111112, HDa3zJc12ahykSsBRvgiWzr6WLEByf36yzKKbVvy4gnF, 8awFmuH8YGXpm4geYYRcqDsLiqrzPUnJrNCDi8rfmFPg, DB3KquZG98DXkU86CkoUermdZQV6m3fZbNmDZS4koudg, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA, TokenzQdBNbLqP5VEhdkAS6EPFLC1PHnBqCXEpPxuEb".split(", ");
        String base58Data1 = "8888fcddc2427e59ffffffffffffffffffffffffffffffff";
        testInstruction("testCollectProtocolFee Case 1", base58Data1, accounts1);
    }

    public static void testCollectFundFee() {
        System.out.println("\n=== Testing CollectFundFee ===");

        // Case 1: tx_hash: 4LWSAL7uCX9ToeBz38BvhWH863jsywxfRMb48w7JeyGYvCHeabJicwY5c3gERyoECfGaMhvYZg7gYZuYUBAojm8z
        String[] accounts1 = "FundHfY8oo8J9KYGyfXFFuQCHe7Z1VBNmsj84eMcdYs4, HHLtFgj64c25mQcnCnxMNoWLvyPJkRY4sPiP7c6qFMVu, A1BBtTYJd4i3xU8D6Tc2FzU6ZN4oXZWXKZnCxwbHXr8x, 5gsf6mV48NWqhmxtXgDH2evJge4BGx5fGnofJFsEUzcW, 9RYVw4uwtdUv9YHqbYuvJYdAWCqwuGhwyuTuN6koYzTR, So11111111111111111111111111111111111111112, GKHgTd6tqvycgG3mqcZraSZDFR32hXhRgo6sZQtudMsC, 91ouaASxL6HVgpyccQRUqcBet3em1aZ7X9tCv8qNjCb7, 7DmDjZJr7uuhML2FG96RMcdev7EVre5eQqaDMPCGw328, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA, TokenzQdBNbLqP5VEhdkAS6EPFLC1PHnBqCXEpPxuEb".split(", ");
        String base58Data1 = "a78a4e95dfc2067effffffffffffffffffffffffffffffff";
        testInstruction("testCollectFundFee Case 1", base58Data1, accounts1);
    }

    private static void testSwapRouterBaseIn() {
        System.out.println("\n=== Testing SwapBaseIn ===");

        // Case 1: tx_hash: 4MzGS34d49eAvJtKxX4jcjroN36FQG7Cp4BnJjjHzacH2Zg4pJkSjU8yxidvkUvoWRt3YuizQDe8ef7BgfNEPYD
        String[] accounts1 = "3gJqkocMWaMm, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA, AzuMwxBxR6Fx9tKrotwLdkxLZjhQnaNccrBD23vQybmz, E3otMvL4REQsF3KkofqNwmz1Xgd2pvG2w4cVSzuRcHtH, BXQ94NosJk3pnT9s6gNMx2WYbMSNnsJJ22zpRmbRZtMm, 3ekPng3b9Ytw, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA, CCTepNPop1uVYUyrqS2yk7qdHELPWUoNtbibkfxycDSk, EuMo4GipT4FiFT7HUfSbU32ZAht5cxJFjsyC11p2D1qb, 3tD34VtprDSkYCnATtQLCiVgTkECU3d12KtjupeR6N2X, 3ekPng3b9Ytw, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA, EuMo4GipT4FiFT7HUfSbU32ZAht5cxJFjsyC11p2D1qb, JBGAcAQP59HrqWsvUF9pXyQjfSpL7ivH3uEt95t7KahY, BXQ94NosJk3pnT9s6gNMx2WYbMSNnsJJ22zpRmbRZtMm, 3XDA4NeC1bRy, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA, EtjUEdstCK856io1ZNoGs9aamsjBJaSm6rmNYz5uTKqv, GfrJpVyaL4eqwzRHzbh3pnv7VU9gWn2bX1cGZVsGubbK, 61R1ndXxvsWXXkWSyNkCxnzwd3zUNB8Q2ibmkiLPC8ht".split(", ");
        String base58Data1 = "457d73daf5baf2c4a0860100000000000000000000000000";  // amountIn=1000000000, minimumAmountOut=100000
        testInstruction("testSwapRouterBaseIn Case 1", base58Data1, accounts1);
    }

    private static void testCreateOperationAccount() {
        System.out.println("\n=== Testing SwapBaseIn ===");

        // Case 1: tx_hash: 3nUAdorNqcAv9RdfCP2AQPzGopR8MoavMu58GkiZUWzp1HXuLZAdEhpE2JMWCkf8u2h7HFZpAU8Km6RUke5GB5jn
        String[] accounts1 = "HggGrUeg4ReGvpPMLJMFKV69NTXL1r4wQ9Pk9Ljutwyv, 7EJTuZgJKbWZCX7R2cLm54asioKQk61VijX56s2RAGwN, 11111111111111111111111111111111".split(", ");
        String base58Data1 = "3f5794216d230868";  // amountIn=1000000000, minimumAmountOut=100000
        testInstruction("createOperationAccount Case 1", base58Data1, accounts1);
    }
    private static void testUpdateOperationAccount() {
        System.out.println("\n=== Testing SwapBaseIn ===");

        // Case 1: tx_hash: YTkZXFbY5tTnJP7hZGtxiPTxKPMP3QnjRkaC8ppW5qZigGskocUWs7V1HgTL3TdgT5ChrRGanFX4WN6vYXVZtKD
        String[] accounts1 = "GThUX1Atko4tqhN2NaiTazWSeFWMuiUvfFnyJyUghFMJ, 7EJTuZgJKbWZCX7R2cLm54asioKQk61VijX56s2RAGwN, 11111111111111111111111111111111".split(", ");
        String base58Data1 = "7f467728bce33d0702010000000afcf8968b8dab88481e2d2ae689c952c757aeba643e3919e89f2e55795c76c1";  // amountIn=1000000000, minimumAmountOut=100000
        testInstruction("createOperationAccount Case 1", base58Data1, accounts1);
    }

    private static void testUpdateRewardInfos() {
        System.out.println("\n=== Testing testUpdateewardInfos ===");

        // Case 1: tx_hash: 66sMNnBCKEULB9YHcA2ADYbi65dN2gmUDfTdruWvN8ynCp5fUZvHpi2zf5EGYDttpYapti5bJdmsaNr7Mfhocq2q
        String[] accounts1 = "9n3dSLrERZQp95dHXywft7xV8D8xnGFLaUHtEhQVaXaC".split(", ");
        String base58Data1 = "a3ace0340b9a6adf";  // amountIn=1000000000, minimumAmountOut=100000
        testInstruction("testUpdateRewardInfos Case 1", base58Data1, accounts1);
    }
    private static void testCollectRemainingRewards() {
        System.out.println("\n=== Testing testCollectRemainingRewards ===");

        // Case 1: tx_hash: 3saNVrkjC51azUbWKWDmPFV1gkuqJAmfZ1zXJ64gYTqyapsttE5E5aCxu1ePoQx4wiQSoSTirW5Q3GGg4xT1TRvW
        String[] accounts1 = "2KbzCQjhTCH2VsKFYzQU51BLRWkEUN3pH8FwSKw2URz7, 5bRzCqrK78kCfmX7QgP7nNmmETK2zWs6ws1HLohe7bUt, AWQiBEiTNV9G6an2XS7ySk8P1Aw3krCbXNPGRGbgR6k7, HcFK5GcJqVY9SbggR9PvzMosYFX4GodGu84sKQuDBjy8, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA".split(", ");
        String base58Data1 = "12eda6c52210d59000";  // amountIn=1000000000, minimumAmountOut=100000
        testInstruction("testCollectRemainingRewards Case 1", base58Data1, accounts1);
    }


    private static void testInitializeReward() {
        System.out.println("\n=== Testing testInitializeReward ===");

        // Case 1: tx_hash: 3Y6BDoA2Mi4J4QWpeWB3nKyAbeSWStS1Z5rQDbEa7Xi6noS1xF4gwxXogWX9S7RfNbJzJDMWpJgum2GCHbMePX9z
        String[] accounts1 = "HCUWaXZ6zqR79T3kb2V7iiTDShujJJWMQSWDGR6b715Y, 61du1gxpptwAEXWxg68Y2cmDZE9pvr1V5aTZBQR2Z5yK, Gex2NJRS3jVLPfbzSFM5d5DRsNoL5ynnwT1TXoDEhanz, 4z911Mgz8HCe5ZJCPMPkFdLVKWhJqbox4DGs5oceykrv, 7EJTuZgJKbWZCX7R2cLm54asioKQk61VijX56s2RAGwN, q6n6ip93iQjJAkMGw71LvKcNh7tRwEJgvxuwm8p1Qfj, GfKUjwoCBvk1VLF489vH16XfseGpsA6KW2znqrTqiitR, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA, 11111111111111111111111111111111, SysvarRent111111111111111111111111111111111".split(", ");
        String base58Data1 = "5f87c0c4f281e6443f66696700000000bfa072670000000000000000000000007765c3ec01000000";  // amountIn=1000000000, minimumAmountOut=100000
        testInstruction("testInitializeReward Case 1", base58Data1, accounts1);
    }
    private static void testTransferRewardOwner() {
        System.out.println("\n=== Testing testInitializeReward ===");

        // Case 1: tx_hash: xza6DL7GKZAPUKJm9dHwJwqBM7rVUSuHMh8e36vzo4kXksafFTRcNCNK9hUec1DhvVKzR6QuwBX1vjDX3D188E9
        String[] accounts1 = "HggGrUeg4ReGvpPMLJMFKV69NTXL1r4wQ9Pk9Ljutwyv, HS245zT4sjkzrhzqiNDr1WWpqbd9FDisiU61sUsf8MnV".split(", ");
        String base58Data1 = "07160c53f22b3079056e2e5b8ae85ac72f492a91c12a5ad509f605754f44dc29dfa9cd8bcf50c998";  // amountIn=1000000000, minimumAmountOut=100000
        testInstruction("testInitializeReward Case 1", base58Data1, accounts1);
    }

    private static void testSetRewardParams() {
        System.out.println("\n=== Testing testSetRewardParams ===");

        // Case 1: tx_hash: 2swtpEKptt8SdaER9oi1RwdrSJnEd8gnjVwvZp4MHch73B92n4p6ieFViKPa9j7hBYKQYrJWA93py7qyziDyonpT
        String[] accounts1 = "NCV2Uo3hfW5LSZXAJe19y6SpC5K98PuQwShCSZgTki3, 9iFER3bpjf1PTTCQCfTRu17EJgvsxo9pVyA9QWwEuX4x, AS5MV3ear4NZPMWXbCsEz3AdbCaXEnq4ChdaWsvLgkcM, 7EJTuZgJKbWZCX7R2cLm54asioKQk61VijX56s2RAGwN, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA, TokenzQdBNbLqP5VEhdkAS6EPFLC1PHnBqCXEpPxuEb, Aa2v73DrdU89qzAhauW9PBqH7seSFjaeTFGLrJiFcX4P, DTvboR7EyiG6vZFWxJJVbMXfNxzo7uBYcppWd4YgoNVH, USDSwr9ApdHk5bvJKMjzff41FfuX8bSxdKcR81vTwcA".split(", ");
        String base58Data1 = "7034a74b20c9d38901a00963561a3da202cc2400000000000000896967000000000044816700000000";  // amountIn=1000000000, minimumAmountOut=100000
        testInstruction("testSetRewardParams Case 1", base58Data1, accounts1);
    }
}