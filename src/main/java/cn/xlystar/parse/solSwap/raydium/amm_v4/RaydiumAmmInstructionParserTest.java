package cn.xlystar.parse.solSwap.raydium.amm_v4;

import cn.xlystar.parse.solSwap.raydium.clmm.RaydiumClmmInstructionParser;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import java.util.Map;

public class RaydiumAmmInstructionParserTest {

    public static void main(String[] args) throws DecoderException {
//        testInitialize(); // 通过
//        testInitialize2(); // 通过
//        testMonitorStep(); // 通过
//        testPreInitialize(); // 通过
//        testSwapBaseIn(); // 通过
//        testSwapBaseOut(); // 通过
//        testDeposit(); // 通过
//        testWithdraw(); // 通过
//        testWithdrawPnl(); // 通过
//        testWithdrawSrm(); // 通过
//        testCreateConfigAccount(); // 通过
//        testUpdateConfigAccount(); // 通过
//        testSetParams(); // 通过
        testSimulateInfo(); // 通过
        testMigrateToOpenBook();
        testAdminCancelOrders();
    }

    private static void testInstruction(String testCase, String base58Data, String[] accounts){
        try {
        System.out.println("\n=== Testing " + testCase + " ===");
        byte[] decode = Hex.decodeHex(base58Data.toCharArray());
//        byte[] decode = Base58.decode("15P");
        Map<String, Object> result = new RaydiumAmmInstructionParser().parseInstruction(decode, accounts);
        System.out.println("Result: " + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void testInitialize() {
        System.out.println("\n=== Testing Initialize ===");

        // Case 1: tx_hash: 5YFjvgWZx3iqyC3RgHVDzCNvgB2dHWZSQVHBRsZFhMQRh2fNk7dZVHSgVQdEcQ2cFQEwGtZBU3C9HVKKmSArKEPU
        String[] accounts1 = "pZZnhu72gxcKVpa1A2oagWsxr3bjgK9QDqEdAQEgSTGV9w7ksXNk4WMtnJjuVdNEvNDCnGiv92J9aGepFjqUBKM, 11111111111111111111111111111111, SysvarRent111111111111111111111111111111111, rdtaAMno2UmPjDNnTW4VqPbx91PPnFL29eQ6DECNLdw, 5Q544fKrFoe6tsEbD7S8EmxGTJYAKtTVhAW5Q5pge4j1, ESuLRUtsLHtuCnPptNfxK3Rgdqx6DR5qweb18VNuTLB6, FeLPZdsP27t1RG9keR4de1ecqViTwGBpLFqytJxN6rH, DqxzPWQ2FKHn8pRoy9jCpA6M3GkEqYfieiAVwMYWVyXr, EPjFWdd5AufqSSqeM2qN1xzybapC8G4wEGGkZwyTDt1v, 97Y7UJ2wWhHhn62Za3mtzDxnvLB34BaBGhqxPjr1Toho, B1Yu7YJqNqWNqBWsC77yDxq8QC5fqjjRF4gxK2JWJ8fu, AyLzpZgmgRhPy2NdbmNyBRpv8W7HGic7f69MGFAqManL, FZC1EkZMcVMzYzNUiH1CMTcKaRbBgeXrS6jXMyf6cQxs, 3dHapHBu9iHHZYPfXx8P4XhbbnjrVbYXYpSqvShiDAAK, Bvst4rb59qirpvKsQrSSV11kAdeF16gsF6TEQ2HVvXGH, 9xQeWvG816bUx9EPjHmaT23yvVM2ZWbrrpZb9PusVFin, AY22bWZUi6HgLjNafrEPNFLBthgGEAHWXH8wiiVhycDh, 9QZ6ngH9CAyYHmp6y2YAPFjb7B44ZnXohjX4QSQUBv6W".split(", ");
        String data1 = "00fe";  // nonce=11, openTime=0
        testInstruction("Initialize Case 1", data1, accounts1);
    }

    private static void testInitialize2() {
        System.out.println("\n=== Testing Initialize2 ===");

        // Case 1: tx_hash: L7g698ADzt3ghwm93wxX7pxUfuRf2go9PwcGcQkK754ebK6G7HTxbQCa5YyFSkUpejwe2UZPSWTtvFzaSzqq1mj
        String[] accounts1 = "TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA, ATokenGPvbdGVxr1b2hvZbsiqW5xWH25efTNsLJA8knL, 11111111111111111111111111111111, SysvarRent111111111111111111111111111111111, 7VtjeiZpKh3yrKYtyF2eJ6wV1Dn3D7b9q5ggq8ETjC8, 5Q544fKrFoe6tsEbD7S8EmxGTJYAKtTVhAW5Q5pge4j1, 56XbXhfvxm6GQcA9nd9ZLgCuhCj1hPqSbC6XZXq7brw3, 5jQP2HFADA92sNbTvjiAeYgbfUKBqWE634SP3jo7mBQX, VQCmwg8dzK2tQHqQtDP2v57hnX9AaxLY6xEPP49vRNb, So11111111111111111111111111111111111111112, 9QX8KM1jWoeDDUQoyiS5xP368cbw4w2hdjeUGN4eQUUf, 9bchB2VFA2sbhgi1GYYXZFGcu5TUSLv9fNkxE2FnWnRo, FiCfXNCjtPbMpAhy7ef7ZBEoQhUzZDoWim4oZuKVfQYS, 9DCxsMizn3H1hprZ7xWe6LDzeUeZBksYFpBWBtSf1PQX, 7YttLkHDoNj9wyDur5pM1ejNaAvT9X4eqaYcHQqtj2G5, srmqPvymJeFKQ4zGQed1GFppgkRHL9kaELCbyksJtPX, DpJ7Mb4GN4bET5cUEn83oMUsWNLSx7P2cANavCUwbcD5, 2chifLqU3yQ5rFRQpUy5hRZM758u3KwsFyrLxKYcaUuw, FGLjRouv2LxTk24eteutchQJ3poU4AkzWTjiWRxNiScs, 5tcLVLcKNqauoFLTPwUo5D59t1rZM2mHKEpn7P1xvjjT, 476iSKa28MH5xA2m9dvA2f65uZCFaUbPS3fpJKbWnujt".split(", ");
        String base58Data1 = "01fe5ac568670000000000ca9a3b0000000000406352bfc60100";  // startTime=-11, poolNonce=1
        testInstruction("Initialize2 Case 1", base58Data1, accounts1);
    }

    private static void testSwapBaseIn() {
        System.out.println("\n=== Testing SwapBaseIn ===");

        // Case 1: tx_hash: Xcw6zeWd93kKa2mJLFkPR7QsNodovHwwMgBxqTwxLStud574TzKVagq4BhYdZ2NszVsp3hewMkb6bnM1w4jZmbi
        String[] accounts1 = "TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA, HRcEMCRE6NETMxBxTC8ht4htxrqV1SSJetE52xtN9Qmm, 5Q544fKrFoe6tsEbD7S8EmxGTJYAKtTVhAW5Q5pge4j1, 7NmgY33Jf6fB7mJTuRDvUyvZM7wzijXfWsNcERJgfcHQ, 6VzXUdKaQMGtS77aNzADJhkzji4PZHFAAts3AiQvwitk, CaRXZxE5Kw7uc8eSgvvTjaWeCzw19tvjqaRBG2ruEa1f, srmqPvymJeFKQ4zGQed1GFppgkRHL9kaELCbyksJtPX, 7mKfut4HjsKbM7MQBDuCDFBezc63nPGH875TbQsBteB2, G7BFPE5GikafT5zPVyguia8qCJnoHRJSC5EUkKLDHq1X, 9LVVFuERWqS4TkgJaUtbN5utJJAb7GcMLEhcSoTxhgAs, zEgAgJ5z12hcSM1Cq5nBhMvDXcNqKyKyKiKuV2JzWtj, DZUGiJ9ehJZCGGawHY6zjUaZrbiQdCZC9KS6V5xHSXAR, 78Tp9uzx2A3Rfdcg4mXKLq6g2SZ2LuBMJmtgxgZUNaWC, 5AyJ6XuRA8nWCns2YjrTaDoni6M2YSeGxA9LxggttgH7, 6TQ3maa2AJ4u2DTuhKLcHvNQghniZ3DscqkpJ62LK7cd, yiSGaAHUyFnihuEMx4Lb1oELS7gc5PYNjVgKqJBG6Ke, ERwM6sWMFSazJmxd2TcwETaWouVhSn18g54fvXszDibL".split(", ");
        String base58Data1 = "0924270000000000000f0af10000000000";  // amountIn=1000000000, minimumAmountOut=100000
        testInstruction("SwapBaseIn Case 1", base58Data1, accounts1);
    }

    private static void testSwapBaseOut() {
        System.out.println("\n=== Testing SwapBaseOut ===");

        // Case 1: tx_hash: 4nr64hDprByHDe6y41wAPZFoNpjAexhBqjuBoXCkpV7PwUUsC2MWeLznr2QUZQtfDar3mnCNobBh1o9EwiT9QExf
        String[] accounts1 = "TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA, 6FdEy632w5jVg2kgivKppXwMepsYAzFwoPwiEH2QCdkd, 5Q544fKrFoe6tsEbD7S8EmxGTJYAKtTVhAW5Q5pge4j1, 7aw7wcsF2vAWEVYsLk4PDoP65kNZ67AZ26VXBdHHL5zF, FV4WnkSUNVUQPWnNJAY12DbqdXR817WQ3BhWkFhxWKX9, E5N6yTHUjwk9cY7je9kKg1GJuydXtXWgf7Wt7rQjzT8N, 43aDgHV1ZE1nfxU5xnkqYRDZZBJJecwWCTpstvRWfwcg, srmqPvymJeFKQ4zGQed1GFppgkRHL9kaELCbyksJtPX, 9sk16NiCsPW3ofLTZMUwBf24xdyXbcBz6ypXospwJdKq, 5rHsmH1HrccH445pA7RzerZ8goapskJP1MdSx4DTubMZ, 67m8LLMyCgxCH7vsAH9C3tW5GQruMEvxwPWYFxScu1pZ, 7znxthMrWxvVFQELJd5ibjm6s2ZSdWauCixbNmqZV3jk, BR1ve6DJUfWH2VeVNUHo1w3unfpYntjH6HJk6j5hcpDJ, FpMbBtqp3kAcjQN3c6oUrAETnkpyQahG8xvBYV3ex55F, HfSkYWnyDKvzKfvQzCoHUwnH7HAz2QPnrm6n1TY1Hn3c, 64xV5ahF6cmBuwnyRLoM2Vc4XTFV7geHfdxXuyVnsrmJ, H68xFWsRSHXk9TfYHQmcrn7wyMVucmgMNz6efPA8XBKA, AvYwohK2odA5Nh9DcXm8rKttxbZFfXjG95HGtdkGyLtQ".split(", ");
        String base58Data1 = "0bf7cf2727000000006212bc1017000000";  // maximumAmountIn=1000000000, amountOut=100000
        testInstruction("SwapBaseOut Case 1", base58Data1, accounts1);
    }

    private static void testDeposit() {
        System.out.println("\n=== Testing Deposit ===");

        // Case 1: tx_hash: 4Qctn7G3674kPUAL2RNYwjBithBjudyHMPY5i5kK6FU8xAAVPtw5TU4GXCyq3GrZp7DytgH8qsMfjLSaDUPbFi7R
        String[] accounts1 = "TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA, EeUc6XrkyezAknE8oDyHYSYLQV5zRz1971eE4Dgmyw5t, 5Q544fKrFoe6tsEbD7S8EmxGTJYAKtTVhAW5Q5pge4j1, 4wUMTK3TNXmcZfmC7afPAoy5biBvvyEqz9E8mCzPiMZw, 4MCyxVG4bLF9W18mw8Jxfxxix2D9kefD6uvfU4Joa7yZ, 7JinbeLA9qgH8oDw5KBshW8BpWRpuy8Soq33JQoKNHqN, 5vxKKTGDo3s6ZbFiQjnPEQWyhHcNwuWfqSYswZAYc7Yr, FGe328spY8azZZ5HQx9AFDaG8L63VHKYyCu37wJnrZTG, DwCjHNEyNBvP2DFc11cJ6P3DfwLnpfariiJhXcajZhY, 8X9bugqxLWAEpryjn4KEjjAvkSrZ3W8YVaxMeDU4dEbg, 7mhx4VSZmcj8hXW7qcFckBMjeq1171ZvgDoNaYYw79GA, 7y7qujFnuwDLRmcbyWRdmeVsbHHtbsPxWHy17n8uLZs3, 4114PWXXPfdAhC2qkKpW36NYSHe8kCJn7t6uYbS3jsV7, 7xom3izEoj5XrGBATfWJjt42CAAXH2K7bA7n9ovn5rcN".split(", ");
        String base58Data1 = "0307cdcb7200000000e98affa62900000001000000000000005233d03d00000000";  // maxCoinAmount=1000000000, maxPcAmount=1000000000, baseLpAmount=100000
        testInstruction("Deposit Case 1", base58Data1, accounts1);
    }

    private static void testWithdraw() {
        System.out.println("\n=== Testing Withdraw ===");

        // Case 1: tx_hash: 3SDSyy5Xa8tvD5Py74BqsALxrPiHtG2gu9D1ge6m5sSBZtJx9xxRQSvwuN8qwyscWPrqScektswpGMZHojSUWJKy
        String[] accounts1 = "TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA, 4wqVdms4u77wFdsssScc9AsA48nuzLMQaay3umQuWxMA, 5Q544fKrFoe6tsEbD7S8EmxGTJYAKtTVhAW5Q5pge4j1, BEiGWMUDwN5GcEqZNqA1CpH2bm11Z6zKZqSbB982QGXZ, 2opJ4PACQpk1pAXkYAhj7pZjyb1Riqep9Hp8hU8RfJyK, Fp1bukbjkMsHszDg3FPurBMWDqWUFTxE1coM2umu47Cx, GHSkXZxor83kxzq9ueH7JsJkD5XgZNWwWvT5n5Ue1nod, 6Qw1ff69LhoTHwdbhqmzmqb4YJ3V2p32YhvqMgBn2Rdn, 11111111111111111111111111111111, 11111111111111111111111111111111, srmqPvymJeFKQ4zGQed1GFppgkRHL9kaELCbyksJtPX, DWdas1qm76eqeQWHhzUdvJYtx9amkXmdxAzDas4DeBgD, GSLHQzFRnvhXZFnafqywbpBxvrmPmsQ9vc7A8vvvWR8J, 3EHE6oX64HTHTeAvnfWVPXWTPbXsqKWMGpLkmLqYiWHg, 95GmrZVa7NE2EDjMNvBZrSnJ1VQuN3ewmhv3iZU9mXWn, 7yCsPLrLaWVAzKog36gyF5SJmKGqQKbYByeruNdPgpsy, BSdWaoaz9BjDNGVvYdU2fUnfB95UcLFtGk1H9hGGX2DP, 5Lmo42eRJRCmoVZactRd7qvHbGSavDbNe9QRSrG1j4Cr, 7Af7C5h5X5VQx7G6nQDWdCA5RZVBBwxXiBAQw48rL1uc, 2Mes1nPcvkafVNCy7YhfxA9qX6RTnebmKfQYAGfHTQmi, 6U7zoXHRxhnbJrAWqcLLpm4jTB9MS2LgpYuvJ2yWvVeB, 8tdGRvT2yzybN3Wbe5R2vbtCsjYYakmiuz5Edo6LbtN7".split(", ");
        String base58Data1 = "04141e90dac0570500";  // amount=1000000000
        testInstruction("Withdraw Case 1", base58Data1, accounts1);
    }

    private static void testWithdrawPnl() {
        System.out.println("\n=== Testing WithdrawPnl ===");

        // Case 1: tx_hash: 5m1TY5NCGwjCj9XqS62xkw88rEcarzeEiTqT2t9HYmkG2Z3t2ZrdETN3XRxEqZsSe6JCSEv1R9o7dRmoFYjJQiom
        String[] accounts1 = "TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA, Gci6mSbkSwLXC88qysV7debpcHpbh97h2eRCUQahP2rQ, 9DCxsMizn3H1hprZ7xWe6LDzeUeZBksYFpBWBtSf1PQX, 5Q544fKrFoe6tsEbD7S8EmxGTJYAKtTVhAW5Q5pge4j1, 7uohJzkBsM8nNbgZUg68SrCyUGY43wqSSCkMHS9q7w5U, D5wz58RonMkZi6d5n2opcekLStxBALuVvSPEKdKH7iwA, FMHHMF4PKQSq7vWanGBbh7Vq4ZALmXq5vzssQdXRcx3j, 8Xz796XJ5cRSHNoU5WUSPrxr2j7Hw37FTaSG5WCpKFtB, F6AaqgjntsNjjAjsopzkMtXx2TQp8E8Z1bU6omzvxRpA, PNLCQcVCD26aC7ZWgRyr5ptfaR7bBrWdTFgRWwu2tvF, Dk68YUu2Q3rUnC1oK25xNrzX2CGpxmdPQkdw3LZX8d3A, srmqPvymJeFKQ4zGQed1GFppgkRHL9kaELCbyksJtPX, 51UmJvawok3mHmR7SF2KpPKxxZcYRMd5STqVHrhH1Mqh, FaSBLWQbA211QodqkVtQWc2AKsLHLwAedUSfmpzSVhyB, EsEmQsfHCJio5ayhUseBbGgJFHf545hkFGnV1dnLtyPF, CV1W5EJbJMUCUbBuJLLvhtSpcAdbcvh79hbEg4xYARNv, ETYM1kW3g2zEsbjWRgHXGuvYwz3MDw7Y4THHdvZ7GAed".split(", ");
        String base58Data1 = "07";  // amount=1000000000
        testInstruction("WithdrawPnl Case 1", base58Data1, accounts1);
    }

    private static void testWithdrawSrm() {
        System.out.println("\n=== Testing WithdrawSrm ===");

        // Case 1: tx_hash: 4dTFXhVVWF4angqUiKQeqQfZNQUBXsHmekiNYyJV4LDup41Tu3TNcW2xvKqUhbksvGjBrRVKvANHGxvPZzt2XoMh
        String[] accounts1 = "TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA, 2dRNngAm729NzLbb1pzgHtfHvPqR4XHFmFyYK78EfEeX, HggGrUeg4ReGvpPMLJMFKV69NTXL1r4wQ9Pk9Ljutwyv, 5Q544fKrFoe6tsEbD7S8EmxGTJYAKtTVhAW5Q5pge4j1, D5p31TVUYsyQHkXwCyQC9R1LE5AzkEEkXMTzpV2XpDSJ, 4Ca4iaWDPfpv5ftVrh7YPXWDS9G46PyDJzn2U8sQQsjn".split(", ");
        String base58Data1 = "08a086010000000000";  // amount=1000000000
        testInstruction("WithdrawSrm Case 1", base58Data1, accounts1);
    }

    private static void testSetParams() {
        System.out.println("\n=== Testing SetParams ===");

        // Case 1: tx_hash: 24XjhFdQtw7AHhcp18mrc3fXYuW1k6Ni2dZqhRLqm2wPecPDWodRJqFt7vKnnT5AgZGqG7ugSqUukF4x6gRa5gjK
        String[] accounts1 = "7MenoSwHyaAKLoYTR1A6Mt3aD6B54RbBnEENLLKsT9DD, 5Q544fKrFoe6tsEbD7S8EmxGTJYAKtTVhAW5Q5pge4j1, HggGrUeg4ReGvpPMLJMFKV69NTXL1r4wQ9Pk9Ljutwyv, Ecpxt3nETp6c4oJiCUhgDdErfiKQsXtYKbhngY1ju2R".split(", ");
        String base58Data1 = "060d00000000000000000000000000000000";  // param1=100, param2=16
        testInstruction("SetParams Case 1", base58Data1, accounts1);
    }

    public static void testPreInitialize() {
        System.out.println("\n=== Testing PreInitialize ===");

        // Case 1: tx_hash: 3V5wmNard64DP8FeWs48jpoYJRGwha6PthFRxSPR5bNxdEb5Ti13kzQgAyujM4AsYU7QjXa8NfzcCXXduLY5X7cs
        String[] accounts1 = "TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA, 11111111111111111111111111111111, SysvarRent111111111111111111111111111111111, AV33o3n1tQzrkSciWbrwGiumPiXv3uHEktQEyYMT71Fe, 9F84ikfS4bUps1GVWffconxiTE8Lo772vUZKsnCSs9bD, 5Q544fKrFoe6tsEbD7S8EmxGTJYAKtTVhAW5Q5pge4j1, 6GuC4sLxUZDrbLzZmFZ5zpYKpG48pjouHQoyMckYwcg5, 4pt7LgdYHcWMz1owyhaBYBRjW5g8SaKEWiisesFyGbzx, EPjFWdd5AufqSSqeM2qN1xzybapC8G4wEGGkZwyTDt1v, 6D9Gy54giwnDDPJbVX9RK5KXvDUULotqFWLniKHxuB66, BogsSB1fV5Fdd84HMXnevK6vS7zd4stzJ9RSjKc1N23x, J2PhSjbwvfvcPvjjKQzyLSNjv457RHKJXht5dfRCcfnL, CxH4fNHrxMGjyhzghWr6d2yQHJp6FfsxpThJkLaMdPRG, op17ztJTZswByiQpePb8vNAqvo3hCaQ5NAH6VHtEWnJ".split(", ");
        String base58Data1 = "0afe";  // nonce=1
        testInstruction("PreInitialize Case 1", base58Data1, accounts1);
    }

    public static void testCreateConfigAccount() {
        System.out.println("\n=== Testing CreateConfigAccount ===");

        // Case 1: tx_hash: 3umXgtWZgLo15vfxL9xsP7oJjB6k3Rp1QVSLJcCUrNWrfP5DdEbcwGdDL3Gv6Gnti6eW2GGnFmF5RYUpBnaNModM
        String[] accounts1 = "3vs14NuhsLrZB2bVP7cuP95z75XEyQNhHtkFC2LAZ51e, EyRZH1vDYxaxNjfDPY4bYD8ayQgAdazc1AnCt4rXF68T".split(", ");
        String base58Data1 = "0e";  // configVersion=1
        testInstruction("CreateConfigAccount Case 1", base58Data1, accounts1);
    }

    public static void testUpdateConfigAccount() {
        System.out.println("\n=== Testing UpdateConfigAccount ===");

        // Case 1: tx_hash: 2bpJfGofQnSmqMVnRXfofqoLLKEiBdAMzaBsxgeNSeWFHsAVv5naH6psgrGbEzAjwBauvF2AyCeodqheSz6vTvLE
        String[] accounts1 = "GThUX1Atko4tqhN2NaiTazWSeFWMuiUvfFnyJyUghFMJ, 9DCxsMizn3H1hprZ7xWe6LDzeUeZBksYFpBWBtSf1PQX".split(", ");
        String base58Data1 = "0f0200fa872800000000";
        testInstruction("UpdateConfigAccount Case 1", base58Data1, accounts1);
    }

    public static void testMonitorStep() {
        System.out.println("\n=== Testing MonitorStep ===");

        // Case 1: tx_hash: 3AZHpj2Zb2Go1wPMA8gRpoYduJrGrRk433dSw2VPA8KPMAvnnGFt5yWffRmKUZWMp6ja8w52s24PXyq1XSGW386i
        String[] accounts1 = "TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA, SysvarRent111111111111111111111111111111111, SysvarC1ock11111111111111111111111111111111, AvreMagEVCmJE5rEnUXQ9RDWEgZ9cEej12prY4iNYEjr, 5Q544fKrFoe6tsEbD7S8EmxGTJYAKtTVhAW5Q5pge4j1, 4JAy22v17FGtbXindnU5yQ3vbnJSbeLL27JBrXT3zdJ6, 2GkudKCH2FHZ2qBbax4fH74YSd2rfuV9hMSuhZ553mT6, 9Vo1JLWRRaubF8YEpcLNcBovtrNkYnNcdXDBMNUhRbgd, AzuKQFSUxiWhJhafj8CsGtcWbq54cBLDsTQMxNqdRHXj, 9M8Py9qcFVZu7NTtC5WgEbGbdcPVMCb5bGvXcLJk2UAC, 9xQeWvG816bUx9EPjHmaT23yvVM2ZWbrrpZb9PusVFin, 3jszawPiXjuqg5MwAAHS8wehWy1k7de5u5pWmmPZf6dM, 9LrpyxzRB7uGLF2Eu8PBjLKPtQMQkDfKsZC7MgydiTvk, AFKfsQKPhe2DocpATdQP4f68PCq4PTj9wDz4GogPkwxN, 6MSY81TyRSZEdxNJQ7WVno7douW6e9AV6DwkDRVT6QUN, CkjM1gg6gurozeVLUqznZmyWyB6rnsDegLDgHsHLeVCS, 28kLGyTz1auPiW8a3dUpKXGjyp23bkAN2QW6DA8jdjZK, 8gTcVqcAXLm3pqr7qCTM39QjqeFSHCWdhn1h7aH5sHnv, ARRPvDKHKjoyT2C6o13fD21hryGovRBBNzPH2zi2TQB3, 9R9R7jVhJ4TmzmCFzaAkH2jJULbwed2FQVQNL9d4brfT, 3L6uA8zrbM6cVz68ySsYBdbvU6mFM451XXGLEq8YDzsG".split(", ");
        String base58Data1 = "02050001000500";  // step=12
        testInstruction("MonitorStep Case 1", base58Data1, accounts1);
    }

    public static void testSimulateInfo() {
        System.out.println("\n=== Testing SimulateInfo ===");

        // Case 1: tx_hash: 2wEZ3rDvhcm1ofe7FNNVLTX3NdEEo4L1XD4868imqtfCzVByDCsfGjFoys58hB61KdqBEhU5VQ3NwWcxC5gVQDrA
        String[] accounts1 = "7XawhbbxtsRcQA8KTkHT9f9nc6d69UwqCDh6U5EEbEmX, 5Q544fKrFoe6tsEbD7S8EmxGTJYAKtTVhAW5Q5pge4j1, 4NJVwEAoudfSvU5kdxKm5DsQe4AAqG6XxpZcNdQVinS4, 876Z9waBygfzUrwwKFfnRcc7cfY4EQf6Kz1w7GRgbVYW, CB86HtaqpXbNWbq67L18y5x2RhqoJ6smb7xHUcyWdQAQ, Epm4KfTj4DMrvqn6Bwg2Tr2N8vhQuNbuK8bESFp4k33K, HWHvQhFmJB3NUcu1aihKmrKegfVxBEHzwVX6yZCKEsi1".split(", ");
        String base58Data1 = "0c00";  // simulationAmount=1000000000
        testInstruction("SimulateInfo Case 1", base58Data1, accounts1);
    }

    public static void testMigrateToOpenBook() {
        System.out.println("\n=== Testing MigrateToOpenBook ===");

        // Case 1: tx_hash: 865v3sGRbddTwZi32qb17rJ8sWTnKLNUWBeKxLjSCWDt8ohVwoDYfwnvF19hsf355v1gQjExh5JFmSZ8JXAJeYQ
        String[] accounts1 = "TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA, 11111111111111111111111111111111, SysvarRent111111111111111111111111111111111, 6UmmUiYoBjSrhakAobJw8BvkmJtDVxaeBtbt7rxWo1mg, 5Q544fKrFoe6tsEbD7S8EmxGTJYAKtTVhAW5Q5pge4j1, J8u8nTHYtvudyqwLrXZboziN95LpaHFHpd97Jm5vtbkW, FdmKUE4UMiJYFK5ogCngHzShuVKrFXBamPWcewDr31th, Eqrhxd7bDUCH3MepKmdVkgwazXRzY6iHhEoBpY7yAohk, 9xQeWvG816bUx9EPjHmaT23yvVM2ZWbrrpZb9PusVFin, 2xiv8A5xrJ7RnGdxXB42uFEkYHJjszEhaJyKKt4WaLep, GGcdamvNDYFhAXr93DWyJ8QmwawUHLCyRqWL3KngtLRa, 22jHt5WmosAykp3LPGSAKgY45p7VGh4DFWSwp21SWBVe, FmhXe9uG6zun49p222xt3nG1rBAkWvzVz7dxERQ6ouGw, CSCS9J8eVQ4vnWfWCx59Dz8oLGtcdQ5R53ea4V9o2eUp, srmqPvymJeFKQ4zGQed1GFppgkRHL9kaELCbyksJtPX, DZjbn4XC8qoHKikZqzmhemykVzmossoayV9ffbsUqxVj, HggGrUeg4ReGvpPMLJMFKV69NTXL1r4wQ9Pk9Ljutwyv".split(", ");
        String base58Data1 = "05";  // migrationFlags=1
        testInstruction("MigrateToOpenBook Case 1", base58Data1, accounts1);
    }

    public static void testAdminCancelOrders() {
        System.out.println("\n=== Testing AdminCancelOrders ===");

        // Case 1: tx_hash: 4Sfh8EHrVL3YsQvhQ9koTAaYbQyMk32ztVxVjcmi3jJFS2RV65oZC8keDeH6o8ry2QgNEHSxzUm1om76dxS1oH1w
        String[] accounts1 = "TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA, E3sxW3YW1Y5pDYFzV3FX4ibXT97Pf71HbnUNqha4rZ2, 5Q544fKrFoe6tsEbD7S8EmxGTJYAKtTVhAW5Q5pge4j1, 2mn4z81pM9TuyCpknULA5GwUWDuVdWzMHGpWAPMDWycc, F57uy93DAvPFpnoYxhR22bV5qATVQ6fMuiq1nBjnabqU, 5QWRwMsX78aNxC9PAgzY2qqjepHGY1wu2ezLqSwZVqm9, 94m32ErWTRFuHDvJYoHSuXpJHuvPS1Jqj8mQFR3h3GQa, HggGrUeg4ReGvpPMLJMFKV69NTXL1r4wQ9Pk9Ljutwyv, 9xQeWvG816bUx9EPjHmaT23yvVM2ZWbrrpZb9PusVFin, 2yAM8Vnpv3rNtN5TDunH8r5QwG9sZhQsGKcEdpRi245n, APAiQKDacwG3c7TYw2tyMhF3oCTVXDirGAfA3VDSv78Z, 9M54xcAtorhYXngkpdhJrqm5SVHSFvomdYKQLq5ABpRN, 2LSW8jhXU3ZvmRhkfQ1EssErAY22WfKih1qtWBcrdmkZ, 5mjZVJcnh3A7MqXJKknS2y3Xbvtn9VTtosdK6v2P2g9c, 2tXGq8B9hGMfZmZR935J228WFJNqzPbUzoTwqgPwDZhH, 3zpBEHjqnbL69RnTLzDX9xW8Bepr4SxTeVCcvqy6MjSx, 9R9R7jVhJ4TmzmCFzaAkH2jJULbwed2FQVQNL9d4brfT, 7US3Dnw9VkC3yDFP95ERVdAChW6g3rDQPftdyBHd6vci".split(", ");
        String base58Data1 = "0d0800";  // limit=10
        testInstruction("AdminCancelOrders Case 1", base58Data1, accounts1);
    }

}