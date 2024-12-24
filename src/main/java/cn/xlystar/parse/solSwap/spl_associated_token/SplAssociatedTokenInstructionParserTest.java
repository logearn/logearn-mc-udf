package cn.xlystar.parse.solSwap.spl_associated_token;


import org.bitcoinj.core.Base58;
import java.util.Map;

public class SplAssociatedTokenInstructionParserTest {
    public static void main(String[] args) {
        testCreate();
        testCreateIdempotent();
        testRecoverNested();
    }

    private static void testCreate() {
        System.out.println("\n=== Testing Create ===");

        // Case 1: tx_hash: 4A3y4gnbrP3HbsmmYHhDpY29VJ6vY5v4QA95nuLRDRvh5DKpvFpg5NXrGVE137oY8w2MEaYMo6NgT2xEziMqrxZW
        String[] accounts1 = new String[]{
                "8MqRTAQnjhDYH7TWS1b1DjFog4CLZfySWE5cZeotG2VW",  // funding
                "8VeyXTU1GkwkJjAhH34AUDodXWdzcoHLP4KVorbK4mKr",  // ata
                "8MqRTAQnjhDYH7TWS1b1DjFog4CLZfySWE5cZeotG2VW",  // wallet
                "D5AfRSdTD3kWSGdUptzBTWg14sCN6aYahy3oZMfvpump",   // mint
                "11111111111111111111111111111111",   // token
                "TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA"    // rent
        };
        String base58Data1 = "1";  // Create instruction has no data
        testInstruction("Create Case 1", base58Data1, accounts1);
    }

    private static void testCreateIdempotent() {
        System.out.println("\n=== Testing CreateIdempotent ===");

        // Case 1: tx_hash: 5MxNhzL2aguvRru8Z63L5t3kfeLXmm2RyF8dUeEjHVoNCCYoaxQ9nKNer46prYjiNDTfpmmTmJUACdBA6ybWwG5C
        String[] accounts1 = new String[]{
                "5fPWHDvcp5AWv1bpjsVTpHW1JycJfhpTGu2USaFTM9ct",  // funding
                "3aELTcr3aqCq8LbEs7PXkViYPPuhaZ2WyTx5JMsrEcBG",  // ata
                "5fPWHDvcp5AWv1bpjsVTpHW1JycJfhpTGu2USaFTM9ct",  // wallet
                "5fPWHDvcp5AWv1bpjsVTpHW1JycJfhpTGu2USaFTM9ct",   // mint
                "So11111111111111111111111111111111111111112",              // system
                "TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA"   // token
        };
        String base58Data1 = "2";  // CreateIdempotent instruction type = 1
        testInstruction("CreateIdempotent Case 1", base58Data1, accounts1);
    }

    private static void testRecoverNested() {
        System.out.println("\n=== Testing RecoverNested ===");

        // Case 1: tx_hash: 2xb6Ef5gQ1tuEj3xcj3gEHwQ2mTCPUCAwgP67RU9yACaFcD4zjHtQWov2EzQuLyNR1mvh3wCT22notKahL9hLvFv
        String[] accounts1 = new String[]{
                "357Kp7tEvPJHxPWbUWfYdZ7HA9gAJT33tRGuiwWWpebY",  // nested ata
                "EPeUFDgHRxs9xxEPVaL6kfGQvCon7jmAWKVUHuux1Tpz",  // nested mint
                "JAjoE1jm648nTYsfgoj92xP1wHh6fqVueXpR3Cr51JtL",  // destination ata
                "JAjoE1jm648nTYsfgoj92xP1wHh6fqVueXpR3Cr51JtL",  // wallet
                "EPeUFDgHRxs9xxEPVaL6kfGQvCon7jmAWKVUHuux1Tpz",  // owner ata
                "5LzdVkvvP7MerNQ8DFat57vcfzaqhydKiyXHYwsUbmNz",   // owner mint
                "TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA"    // token program
        };
        String base58Data1 = "3";  // RecoverNested instruction type = 2
        testInstruction("RecoverNested Case 1", base58Data1, accounts1);
    }

    private static void testInstruction(String testName, String base58Data, String[] accounts) {
        try {
            System.out.println("\nTesting " + testName);
            System.out.println("Base58 Data: " + base58Data);
            System.out.println("Accounts:");
            for (int i = 0; i < accounts.length; i++) {
                System.out.println("  " + i + ": " + accounts[i]);
            }

            byte[] data = base58Data.isEmpty() ? new byte[0] : Base58.decode(base58Data);
            Map<String, Object> result = new SplAssociatedTokenInstructionParser().parseInstruction(data, accounts);
            System.out.println("Parse result: " + result);

        } catch (Exception e) {
            System.err.println("Test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}