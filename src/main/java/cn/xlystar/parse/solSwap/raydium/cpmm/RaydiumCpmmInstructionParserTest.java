package cn.xlystar.parse.solSwap.raydium.cpmm;

import cn.xlystar.parse.solSwap.raydium.amm_v4.RaydiumAmmInstructionParser;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.util.Map;

public class RaydiumCpmmInstructionParserTest {

    public static void main(String[] args) throws DecoderException {
//        testInitialize(); // 通过
//        testSwapBaseInput(); // 通过
//        testSwapBaseOutput(); // 通过
//        testDeposit(); // 通过
//        testWithdraw(); // 通过
//        testCreateAmmConfig(); // 通过
//        testUpdateAmmConfig(); // 通过
//        testUpdatePoolStatus();
        testCollectProtocolFee();
        testCollectFundFee();
    }

    private static void testInstruction(String testCase, String base58Data, String[] accounts){
        try {
        System.out.println("\n=== Testing " + testCase + " ===");
        byte[] decode = Hex.decodeHex(base58Data.toCharArray());
//        byte[] decode = Base58.decode("15P");
        Map<String, Object> result = RaydiumCpmmInstructionParser.parseInstruction(decode, accounts);
        System.out.println("Result: " + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void testInitialize() {
        System.out.println("\n=== Testing Initialize ===");

        // Case 1: tx_hash: 2tiQbj2RPLgxjkyJWzVFS97SvkgXE4KPBjfQRNpe6KmFKreLNPaDYoZz8oXPpRfhBhPceTM7t2uPd98K5ajPispL
        String[] accounts1 = "FSkawegbt7hqFFSBJoSiV7Q1Jgp68PbFwapoGpdSHZhK, D4FPEruKEHrG5TenZ2mpDGEfu1iUvTiqBxvpU8HLBvC2, GpMZbSM2GgvTKHJirzeGfMFoaZ8UR2X7F4v8vHTvxFbL, 7M8d1TxydTADm3QRUEJjpDELj3djKp6qQ4pHtyU9Lv1T, So11111111111111111111111111111111111111112, ErSZNwsamWjR9Hj2o18GLwVyTkooyGbdptjMjuv72X8A, F9hJWd7TYSixZDd2t4jz7RwrN5Pwm6duvb4m3RotFp9k, 825efgBPMmhcxx5gb1EYf6asf4TWSiC2HRUfbU58HC3Z, EWZJQKTrLmmKPfF5rK6bCEf3rtxCvAn8UvowhFnXhgtY, WzcoTDqKKQERkgTVF2PZk83kDrRCpK4sMNpTYimcQCe, Da3MzC4RoZ3xuxptCuc67FeM1AQib3oWvRE4fHJ5k3n5, HqRowbWcM3cDZMELKNWU1RCoaHiGsodEYa1oKiQmsSFm, DNXgeM9EiiaAbaWvwjHj9fQQLAX5ZsfHyvmYUNRAdNC8, 92a8WsYVuyDqaULfhiDUGnsxkVp3fKZyDp5f8UJCUGBJ, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA, ATokenGPvbdGVxr1b2hvZbsiqW5xWH25efTNsLJA8knL, 11111111111111111111111111111111, SysvarRent111111111111111111111111111111111".split(", ");
        String data1 = "afaf6d1f0d989bed00da5aac320000001c947f945a1d0000b849696700000000";  // nonce=11, openTime=0
        testInstruction("Initialize Case 1", data1, accounts1);
    }

    private static void testSwapBaseInput() {
        System.out.println("\n=== Testing SwapBaseIn ===");

        // Case 1: tx_hash: 3gtb8gebVDuD3uBKaCLzNWM2cmW6xzu1AZSRMbSBxP9q53wfzMLnwQX54beMtUH9gVDPBiot8yVEo663GdY57XSH
        String[] accounts1 = "FChP6VZjGu7ni2Hgr2NZQeypJA7yBtcTSk5Kz4vnCp3q, GpMZbSM2GgvTKHJirzeGfMFoaZ8UR2X7F4v8vHTvxFbL, D4FPEruKEHrG5TenZ2mpDGEfu1iUvTiqBxvpU8HLBvC2, HWsaQKHEQFdD2WAsaEYxTtZKVFD51RnenFpciK4oUXHe, 7oTdPUTbWm7uork5DPdzAXbL36ibgBi5dNEuUUTg66ok, dqvjuqbpERWkjeg42MWBAnuTe3rs19eJB7Ukn24wme2, 69kR7ehfV9Lrr1tVEAxqtLkiyWJWw82tebTj6MgCoGbE, D4abjnNFc9JHpipPfFxVqm9KxMotAAD8P7gWKy5en72y, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA, So11111111111111111111111111111111111111112, LoafdJ3WSAvsrx3zppSGKA6sRvL9GrRrU1iRV7HkLkm, EaD1ALGcyEyFiqFkWzpu8TQjaeExfk731VSy8SweoyUn".split(", ");
        String base58Data1 = "8fbe5adac41e33defd423402000000000100000000000000";  // amountIn=1000000000, minimumAmountOut=100000
        testInstruction("SwapBaseIn Case 1", base58Data1, accounts1);
    }

    private static void testSwapBaseOutput() {
        System.out.println("\n=== Testing SwapBaseOut ===");

        // Case 1: tx_hash: 3P2w1PSCXfpdEgu5pstQQE4WcyPvoW4Q6N6LJ4L31QoP1WXyEwaM6oZ5TgYbSBd6BhTLXmP9QUGFmfpEy48azYCJ
        String[] accounts1 = "BtgrRuT2hjS2MppQiEVKFXUi8PyR9D5wtNV3sahHo1mJ, GpMZbSM2GgvTKHJirzeGfMFoaZ8UR2X7F4v8vHTvxFbL, D4FPEruKEHrG5TenZ2mpDGEfu1iUvTiqBxvpU8HLBvC2, HWsaQKHEQFdD2WAsaEYxTtZKVFD51RnenFpciK4oUXHe, B5CFEhLeMCDzLUumAzoDWVrthXQ1HUQj8TQdcfmF64Lf, AwKjejfuPvyxNxmMRjjnkxRbAvcDGPvYHY6dA9feuVh1, 69kR7ehfV9Lrr1tVEAxqtLkiyWJWw82tebTj6MgCoGbE, D4abjnNFc9JHpipPfFxVqm9KxMotAAD8P7gWKy5en72y, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA, So11111111111111111111111111111111111111112, LoafdJ3WSAvsrx3zppSGKA6sRvL9GrRrU1iRV7HkLkm, EaD1ALGcyEyFiqFkWzpu8TQjaeExfk731VSy8SweoyUn".split(", ");
        String base58Data1 = "37d96256a34ab4ad17d5a70f000000005957ab6121360000";  // maximumAmountIn=1000000000, amountOut=100000
        testInstruction("SwapBaseOut Case 1", base58Data1, accounts1);
    }

    private static void testDeposit() {
        System.out.println("\n=== Testing Deposit ===");

        // Case 1: tx_hash: 3eGvgSh7nVFdGCdm28BDCwMQiAT2bU9XgUsLUFegSk31B8uDF9GdMWVrc53qERCL3VZhMu3UHCcWQSmYx5Se5mDM
        String[] accounts1 = "5C3PzxcZ69p7F1knMFsCnaHpg9gVsfnzoTsPMuyfTeGH, GpMZbSM2GgvTKHJirzeGfMFoaZ8UR2X7F4v8vHTvxFbL, Fq1aXMvi7Ze1d6iZzHfrXKfqL5nTDsGrVQa9SEtTBfE6, 5oEh8nVS9iKVeVJgEJtYtKATM6aWZpj65De71S9B1Xs4, A2xaMp8aswuPwWcrMitkdyoczAU5R99dtUV3JK81mLoN, 54B8s67EYxMgF6XKBJBdugNRJ5bjhvn44ETfSFS7CL9z, 5vfMrE4kkk7RGw5XsnH8GhKkpJWZPuV4NEuN8x69D8YX, RuJdcTej8YJGwm3ed7Tq1tnue1hKhmvMTeeESopNNQR, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA, TokenzQdBNbLqP5VEhdkAS6EPFLC1PHnBqCXEpPxuEb, So11111111111111111111111111111111111111112, FishgChr4rMPf1BgnZgHN6hkP443uGTe1ZpMmMJ7gS2B, HCbzmHUsngRJwTrjCN68wHMKnyiYhGME3Q5oJoScDDeb".split(", ");
        String base58Data1 = "f223c68952e1f2b6b625c2100000000080f0fa0200000000521e119600000000";  // maxCoinAmount=1000000000, maxPcAmount=1000000000, baseLpAmount=100000
        testInstruction("Deposit Case 1", base58Data1, accounts1);
    }

    private static void testWithdraw() {
        System.out.println("\n=== Testing Withdraw ===");

        // Case 1: tx_hash: 2tMH9htGxh1pTJNSTk4nLpZUd3ALCTZbph7og4gMn7pSfFEfw34dVH6thrwYigjvb8o3VA4TqmzrvdT2WzpouNE6
        String[] accounts1 = "9dhMJthcWUuavFjwoHsS6MGQGWsD79TSRLQoTAKP7fMr, GpMZbSM2GgvTKHJirzeGfMFoaZ8UR2X7F4v8vHTvxFbL, 4SfdKr7DDqrX9sqKW5Hrvx8hG7yRbBJHLbmaruxn1ir6, BHNLntFQrnz7edcd2FpT5K1STwcFrRHtsBYSFKUzgw7s, DJURi5xgVJVTqm8nMbfPM6jPoRA4euW7gLhPEjiF4WDX, 8tKrgLq1AYLnoSKMJujuEHHFa1NJUFtvgtr9mnnodFFK, 4qhTirZFvYEVqRyoDMY3C8b36MhyEmSx4EvFqAu4oZbp, J7RpEogHWaurdBe4EeWeHbc2dGsvj2KVKvWXSqyme84Y, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA, TokenzQdBNbLqP5VEhdkAS6EPFLC1PHnBqCXEpPxuEb, So11111111111111111111111111111111111111112, 2uuja3nTnqtNKZbPmVyn3X5WaXktT3gtd8zSFGtLBVEq, HFWZi8cGJXeHBcy2NjGTtE6ecEUM4FFZfkiN7pNopg5M, MemoSq4gqABAXKb96qnH8TysNcWxMyWCqXgDLGmfcHr"
                .split(", ");
        String base58Data1 = "b712469c946da122739647b26a00000000000000000000000000000000000000";  // amount=1000000000
        testInstruction("Withdraw Case 1", base58Data1, accounts1);
    }

    public static void testCreateAmmConfig() {
        System.out.println("\n=== Testing CreateAmmConfig ===");

        // Case 1: tx_hash: 61pvyDQF7xjLf5TFUwJa431cTcCtc5ncWfePTia5hE2aNc1fEu19CM2pQqYVXt7x2CB262VJnkZqPRYpCNahj5PD
        String[] accounts1 = "GThUX1Atko4tqhN2NaiTazWSeFWMuiUvfFnyJyUghFMJ, C7Cx2pMLtjybS3mDKSfsBj4zQ3PRZGkKt7RCYTTbCSx2, 11111111111111111111111111111111".split(", ");
        String base58Data1 = "8934edd4d7756c680300409c000000000000c0d4010000000000409c00000000000080d1f00800000000";  // configVersion=1
        testInstruction("CreateConfigAccount Case 1", base58Data1, accounts1);
    }

    public static void testUpdateAmmConfig() {
        System.out.println("\n=== Testing UpdatePoolStatus ===");

        // Case 1: tx_hash: 2mHHAoxGEZeDkqDfLHhiufSdXx3Sss22F5NgXWopJpMR14KCL5JdDYGcQsWgXT6zvB2UUEh7avycjvxNjafe8BFC
        String[] accounts1 = "GThUX1Atko4tqhN2NaiTazWSeFWMuiUvfFnyJyUghFMJ, D4FPEruKEHrG5TenZ2mpDGEfu1iUvTiqBxvpU8HLBvC2, ProCXqRcXJjoUd1RNoo28bSizAA6EEqt9wURZYPDc5u".split(", ");
        String base58Data1 = "313cae889a1c74c8030000000000000000";
        testInstruction("UpdateConfigAccount Case 1", base58Data1, accounts1);
    }
    public static void testUpdatePoolStatus() {
        System.out.println("\n=== Testing UpdatePoolStatus ===");

    }

    public static void testCollectProtocolFee() {
        System.out.println("\n=== Testing CollectProtocolFee ===");

        // Case 1: tx_hash: 5QerTGg7wFuNmiio9oVdSgiiwRnEeiey7F5JBTcZA2mT7DkuFWa6rBwQAZtxPEFSgWqFpmWcNTo18YpY7y6CQLXn
        String[] accounts1 = "ProCXqRcXJjoUd1RNoo28bSizAA6EEqt9wURZYPDc5u, GpMZbSM2GgvTKHJirzeGfMFoaZ8UR2X7F4v8vHTvxFbL, EyktEFod1gAgsuM1hXmEpqkitFFk9XczkqLPx2vKiceg, G95xxie3XbkCqtE39GgQ9Ggc7xBC8Uceve7HFDEFApkc, 9UxY522tfaLZLQu17Y5iEVSD8kYoJwttAj9Vmy1W6X7L, DkbBkU6bMZKgRmuxpojhndV9vBGBnAJkRxCbkmtzuswr, So11111111111111111111111111111111111111112, 43VWkd99HjqkhFTZbWBpMpRhjG469nWa7x7uEsgSH7We, ETRpp8jWpMzySp5M9mSJCsPwu9nGEUMKnjRAb1XNmRvy, 4TkY4Zykk4fcHfxN4TLVjCTDR8q7YJToKmQUutcV9Ckk, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA, TokenzQdBNbLqP5VEhdkAS6EPFLC1PHnBqCXEpPxuEb".split(", ");
        String base58Data1 = "8888fcddc2427e59ffffffffffffffffffffffffffffffff";
        testInstruction("UpdateConfigAccount Case 1", base58Data1, accounts1);
    }

    public static void testCollectFundFee() {
        System.out.println("\n=== Testing CollectFundFee ===");

        // Case 1: tx_hash: 2jd7ndBnYS4KQfLwa2kuhUN4yTrBFZYzdEv3GnBYFpgwyvhC9uU7P8wc4hWHCxDtdUAjHKsvdprP25sEpezsCyud
        String[] accounts1 = "FUNDduJTA7XcckKHKfAoEnnhuSud2JUCUZv6opWEjrBU, GpMZbSM2GgvTKHJirzeGfMFoaZ8UR2X7F4v8vHTvxFbL, B4Jag7SokpCb5MwVZEVck7WqWSqwbB7GibV5F1NMsMgM, D4FPEruKEHrG5TenZ2mpDGEfu1iUvTiqBxvpU8HLBvC2, 6cMtGmaHZ5wfLnvWUj8T1qDVFbfkR9Br64QSHJpFEVE2, AaFy57tCPUaHVcwA5AteWHHeWnSkNXoBTaYsa7QEPmPF, oraim8c9d1nkfuQk9EzGYEUGxqL3MHQYndRw1huVo5h, 2hZWncnmsSMdbkdwzSHJfrnDVkL66g63gncdRWqjTbBB, FFQnc7gMhtCFRSyHtibU1JbnHbvCCiqNZSESov8AwtE6, 7WeYQKP2GeYsrWEcysJe7ueNGuxjzkoQ3RRaH8kDbmUQ, TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA, TokenzQdBNbLqP5VEhdkAS6EPFLC1PHnBqCXEpPxuEb".split(", ");
        String base58Data1 = "a78a4e95dfc2067effffffffffffffffffffffffffffffff";
        testInstruction("UpdateConfigAccount Case 1", base58Data1, accounts1);
    }

}