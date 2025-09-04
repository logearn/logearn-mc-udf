package cn.xlystar.mc.udf;

import cn.xlystar.entity.TransferEvent;
import cn.xlystar.entity.UniswapEvent;
import cn.xlystar.helpers.ChainConfig;
import cn.xlystar.helpers.ConfigHelper;
import cn.xlystar.parse.ammswap.AMMSwapDataProcess;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.aliyun.odps.udf.UDF;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 计算引擎：maxCompute
 * sol_amm_parse()
 * data: 要解析的数据
 */
@Slf4j
public class SolanaParse extends UDF {
    public SolanaParse() {
    }

    public String evaluate(String chainId, String protocol,
                           String slot, String blockTime,
                           String accountKeyString, String logMessageString,
                           String writableAddressString, String readonlyAddressString,
                           String postTokenBalanceString, String preTokenBalanceString,
                           String postBalanceString, String preBalanceString,
                           String innerInstructionString, String outerInstructionString,
                           String hash, String price) throws IOException {
        ChainConfig conf = new ConfigHelper().getConfig(chainId, protocol);
        List<Map<String, String>> maps = new ArrayList<>();
        String res = "";
        try {
            List<String> accountKeys = parseJsonArray(accountKeyString, String.class);
            List<String> logMessages = parseJsonArray(logMessageString, String.class);
            List<String> writableAddresses = parseJsonArray(writableAddressString, String.class);
            List<String> readonlyAddresses = parseJsonArray(readonlyAddressString, String.class);
            List<String> postBalances = parseJsonArray(postBalanceString, String.class);
            List<String> preBalances = parseJsonArray(preBalanceString, String.class);

            List<Map<String, Object>> preTokenBalances = parseJsonArray(preTokenBalanceString, new TypeReference<List<Map<String, Object>>>() {
            });
            List<Map<String, Object>> postTokenBalances = parseJsonArray(postTokenBalanceString, new TypeReference<List<Map<String, Object>>>() {
            });
            List<Map<String, Object>> innerInstructions = parseJsonArray(innerInstructionString, new TypeReference<List<Map<String, Object>>>() {
            });
            List<Map<String, Object>> outerInstructions = parseJsonArray(outerInstructionString, new TypeReference<List<Map<String, Object>>>() {
            });
            maps = AMMSwapDataProcess.parseSolTx(conf, chainId,
                    slot, blockTime,
                    accountKeys, logMessages,
                    writableAddresses, readonlyAddresses,
                    postTokenBalances, preTokenBalances,
                    postBalances, preBalances,
                    innerInstructions, outerInstructions,
                    hash, price
            );
            if (CollectionUtils.isEmpty(maps)) return JSON.toJSONString(maps);
            res = JSON.toJSONString(maps);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(String.format("sol_amm_parse error, chainId:%s, protocol:%s, slot:%s, hash:%s, price:%s, msg:%s", chainId, protocol, slot, hash, price, e.getLocalizedMessage()));
            log.error("sol_amm_parse error, chainId:{}, protocol:{}, slot:{}, hash:{}, price:{}, maps:{}", chainId, protocol, slot, hash, price, JSON.toJSONString(maps));
            throw new RuntimeException(String.format("price:%s, hash:%s, stack:%s, msg:%s", price, hash, Arrays.toString(e.getStackTrace()), e.getLocalizedMessage()));
        } catch (StackOverflowError | OutOfMemoryError e) {
            e.printStackTrace();
            maps = new ArrayList<>();
            HashMap<String, String> errorSwap = new HashMap<>();
            errorSwap.put("errorMsg", "oom");
            errorSwap.put("chain", chainId);
            errorSwap.put("protocol", protocol);
            maps.add(errorSwap);
            throw new RuntimeException(String.format("hash:%s", hash));
        }
        return res;
    }

    private <T> List<T> parseJsonArray(String json, Class<T> clazz) {
        return StringUtils.isEmpty(json) ?
                Collections.emptyList() :
                JSON.parseArray(json, clazz);
    }

    private <T> List<T> parseJsonArray(String json, TypeReference<List<T>> typeReference) {
        if (StringUtils.isEmpty(json)) {
            return Collections.emptyList();
        }
        return JSON.parseObject(json, typeReference.getType());
    }

    public static void main(String[] args) throws IOException {
        SolanaParse solanaParse = new SolanaParse();
        String chainId = "3";
        String protocol = "swap";
        String slot = "1";
        String blockTime = "1";
        String price = "160";
        String hash = "4Qtj2YnkMRCQzuAtgj7FU5PeQANA7BSxTjSynetrByDmrEjZf1zz6GR7uNtz27Z1fY6b8gp2zdTDQ2yHedUwNJ82";
        String accountKeys = "[\"7oBZ3U459B8GtQCSSXQ4EKm4Gcw2fRoMbsrcZoZHGg8k\",\"9MvedQsvB9Kc9yUSh6QoSbyizkd8VtjqbG8pEec7SKrC\",\"2HphKmGcQe3Hu1Dze2gguF5TtcQmNWzkTf71erMLdijY\",\"6ggUeW7Lk3Lr52YgkSfJWBzFmyC8N4as1t6F1M4j9jxN\",\"3bkus91Lv81Fafhiu5z1Japz8DWXgABadJ9ksm8zTyMz\",\"5kvcbr4VSCcVBkkn8AAhnKL4YbmVjT16ktfkEEbzZnv\",\"77hQ6r3pHyXpUo7uHzSBET7z5YhbpAbEmE7aQh8niZVQ\",\"ComputeBudget111111111111111111111111111111\",\"ATokenGPvbdGVxr1b2hvZbsiqW5xWH25efTNsLJA8knL\",\"AXWMNeT96U246PqgxPyLp9SzFhzTmwA2eTHXw1C8pump\",\"11111111111111111111111111111111\",\"TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA\",\"pAMMBay6oceH9fJKBRHGP5D4bD4sWpmSwMn52FMfXEA\",\"48srx6cuYzEa98MgVLJg7xhLCRuzH1sx6R1ysxnx7ikj\",\"ADyA8hdefvWN2dbGGWFotbzWxrAvLW83WG6QCVXvJKqw\",\"So11111111111111111111111111111111111111112\",\"FWsW1xNtWscwNmKv6wVsU1iTzRN6wmmk3MjxRP5tT7hz\",\"GS4CU59F31iL7aR2Q8zVS8DRrcRnXX1yjQ66TqNVQnaR\",\"8N3GDaZ2iwN65oxVatKTLPNooAVUJTbfiVJ1ahyqwjSk\",\"5PHirr8joyTMp9JMm6nW7hNDVyEYdkzDqazxPD7RaTjx\",\"pfeeUxB6jkeY1Hxd7CsFCAjcbHA9rWtchMGdZ6VojVZ\"]";

        String readonlyAddressString = "[]";
        String writableAddressString = "[]";
        String logMessageString = "[\"Program ComputeBudget111111111111111111111111111111 invoke [1]\",\"Program ComputeBudget111111111111111111111111111111 success\",\"Program ComputeBudget111111111111111111111111111111 invoke [1]\",\"Program ComputeBudget111111111111111111111111111111 success\",\"Program ATokenGPvbdGVxr1b2hvZbsiqW5xWH25efTNsLJA8knL invoke [1]\",\"Program log: CreateIdempotent\",\"Program ATokenGPvbdGVxr1b2hvZbsiqW5xWH25efTNsLJA8knL consumed 4338 of 119700 compute units\",\"Program ATokenGPvbdGVxr1b2hvZbsiqW5xWH25efTNsLJA8knL success\",\"Program pAMMBay6oceH9fJKBRHGP5D4bD4sWpmSwMn52FMfXEA invoke [1]\",\"Program log: Instruction: Sell\",\"Program pfeeUxB6jkeY1Hxd7CsFCAjcbHA9rWtchMGdZ6VojVZ invoke [2]\",\"Program log: Instruction: GetFees\",\"Program pfeeUxB6jkeY1Hxd7CsFCAjcbHA9rWtchMGdZ6VojVZ consumed 3074 of 77627 compute units\",\"Program return: pfeeUxB6jkeY1Hxd7CsFCAjcbHA9rWtchMGdZ6VojVZ FAAAAAAAAAAFAAAAAAAAAAUAAAAAAAAA\",\"Program pfeeUxB6jkeY1Hxd7CsFCAjcbHA9rWtchMGdZ6VojVZ success\",\"Program TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA invoke [2]\",\"Program log: Instruction: TransferChecked\",\"Program TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA consumed 6238 of 70825 compute units\",\"Program TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA success\",\"Program TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA invoke [2]\",\"Program log: Instruction: TransferChecked\",\"Program TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA consumed 6147 of 61714 compute units\",\"Program TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA success\",\"Program TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA invoke [2]\",\"Program log: Instruction: TransferChecked\",\"Program TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA consumed 6147 of 52679 compute units\",\"Program TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA success\",\"Program data: Pi83CqUD3Co22LRoAAAAAAdawgUAAAAA2xB61yUAAACV64QTAAAAAHGDA1eBAAAAFM5L/RsAAACSFLHdn7oAADRQml4mAAAAFAAAAAAAAAAyLKUTAAAAAAUAAAAAAAAADUvpBAAAAAACJPVKJgAAAPXYC0YmAAAALpeiqQSqC5PpJdYvtYfBcn09fEfNH5GHFOHLUzKKMexk+sU9/LU0/8zCOivpRME7q4qsagy1Cy45q1EBXP8UFRMq6LcO9VwH4xR9U94TxSzsv8RB5oum6C5h68PMP6V3fDnFvipXo27M5erGWry1OVYMBTcIkmjbi0NqRqBjyZXXqo+wYNgpG0xNR12v92LJa9wNrOs2wBLq0S7TqUhBYQE3/RqP4tuXlSEdY+Y0qTGkv/jT1srg9vfIWxTLr/PPAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAFAAAAAAAAAAAAAAAAAAAA\",\"Program pAMMBay6oceH9fJKBRHGP5D4bD4sWpmSwMn52FMfXEA invoke [2]\",\"Program pAMMBay6oceH9fJKBRHGP5D4bD4sWpmSwMn52FMfXEA consumed 2027 of 40047 compute units\",\"Program pAMMBay6oceH9fJKBRHGP5D4bD4sWpmSwMn52FMfXEA success\",\"Program pAMMBay6oceH9fJKBRHGP5D4bD4sWpmSwMn52FMfXEA consumed 77903 of 115362 compute units\",\"Program pAMMBay6oceH9fJKBRHGP5D4bD4sWpmSwMn52FMfXEA success\"]";

        String postTokenBalanceString = "[{\"accountIndex\":1,\"mint\":\"AXWMNeT96U246PqgxPyLp9SzFhzTmwA2eTHXw1C8pump\",\"owner\":\"7oBZ3U459B8GtQCSSXQ4EKm4Gcw2fRoMbsrcZoZHGg8k\",\"programId\":\"TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA\",\"uiTokenAmount\":{\"uiAmount\":719894.568038,\"decimals\":6,\"amount\":\"719894568038\",\"uiAmountString\":\"719894.568038\"}},{\"accountIndex\":2,\"mint\":\"So11111111111111111111111111111111111111112\",\"owner\":\"7oBZ3U459B8GtQCSSXQ4EKm4Gcw2fRoMbsrcZoZHGg8k\",\"programId\":\"TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA\",\"uiTokenAmount\":{\"uiAmount\":0.230855054,\"decimals\":9,\"amount\":\"230855054\",\"uiAmountString\":\"0.230855054\"}},{\"accountIndex\":3,\"mint\":\"So11111111111111111111111111111111111111112\",\"owner\":\"48srx6cuYzEa98MgVLJg7xhLCRuzH1sx6R1ysxnx7ikj\",\"programId\":\"TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA\",\"uiTokenAmount\":{\"uiAmount\":120.310343707,\"decimals\":9,\"amount\":\"120310343707\",\"uiAmountString\":\"120.310343707\"}},{\"accountIndex\":4,\"mint\":\"AXWMNeT96U246PqgxPyLp9SzFhzTmwA2eTHXw1C8pump\",\"owner\":\"48srx6cuYzEa98MgVLJg7xhLCRuzH1sx6R1ysxnx7ikj\",\"programId\":\"TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA\",\"uiTokenAmount\":{\"uiAmount\":205031315.599504,\"decimals\":6,\"amount\":\"205031315599504\",\"uiAmountString\":\"205031315.599504\"}},{\"accountIndex\":5,\"mint\":\"AXWMNeT96U246PqgxPyLp9SzFhzTmwA2eTHXw1C8pump\",\"owner\":\"FWsW1xNtWscwNmKv6wVsU1iTzRN6wmmk3MjxRP5tT7hz\",\"programId\":\"TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA\",\"uiTokenAmount\":{\"uiAmount\":43159.889816,\"decimals\":6,\"amount\":\"43159889816\",\"uiAmountString\":\"43159.889816\"}},{\"accountIndex\":6,\"mint\":\"AXWMNeT96U246PqgxPyLp9SzFhzTmwA2eTHXw1C8pump\",\"owner\":\"8N3GDaZ2iwN65oxVatKTLPNooAVUJTbfiVJ1ahyqwjSk\",\"programId\":\"TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA\",\"uiTokenAmount\":{\"uiAmount\":0.0,\"decimals\":6,\"amount\":\"0\",\"uiAmountString\":\"0\"}}]";
        String preTokenBalanceString = "[{\"accountIndex\":1,\"mint\":\"AXWMNeT96U246PqgxPyLp9SzFhzTmwA2eTHXw1C8pump\",\"owner\":\"7oBZ3U459B8GtQCSSXQ4EKm4Gcw2fRoMbsrcZoZHGg8k\",\"programId\":\"TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA\",\"uiTokenAmount\":{\"uiAmount\":555510.629233,\"decimals\":6,\"amount\":\"555510629233\",\"uiAmountString\":\"555510.629233\"}},{\"accountIndex\":2,\"mint\":\"So11111111111111111111111111111111111111112\",\"owner\":\"7oBZ3U459B8GtQCSSXQ4EKm4Gcw2fRoMbsrcZoZHGg8k\",\"programId\":\"TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA\",\"uiTokenAmount\":{\"uiAmount\":0.327478165,\"decimals\":9,\"amount\":\"327478165\",\"uiAmountString\":\"0.327478165\"}},{\"accountIndex\":3,\"mint\":\"So11111111111111111111111111111111111111112\",\"owner\":\"48srx6cuYzEa98MgVLJg7xhLCRuzH1sx6R1ysxnx7ikj\",\"programId\":\"TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA\",\"uiTokenAmount\":{\"uiAmount\":120.213720596,\"decimals\":9,\"amount\":\"120213720596\",\"uiAmountString\":\"120.213720596\"}},{\"accountIndex\":4,\"mint\":\"AXWMNeT96U246PqgxPyLp9SzFhzTmwA2eTHXw1C8pump\",\"owner\":\"48srx6cuYzEa98MgVLJg7xhLCRuzH1sx6R1ysxnx7ikj\",\"programId\":\"TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA\",\"uiTokenAmount\":{\"uiAmount\":205195781.936274,\"decimals\":6,\"amount\":\"205195781936274\",\"uiAmountString\":\"205195781.936274\"}},{\"accountIndex\":5,\"mint\":\"AXWMNeT96U246PqgxPyLp9SzFhzTmwA2eTHXw1C8pump\",\"owner\":\"FWsW1xNtWscwNmKv6wVsU1iTzRN6wmmk3MjxRP5tT7hz\",\"programId\":\"TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA\",\"uiTokenAmount\":{\"uiAmount\":43077.491851,\"decimals\":6,\"amount\":\"43077491851\",\"uiAmountString\":\"43077.491851\"}},{\"accountIndex\":6,\"mint\":\"AXWMNeT96U246PqgxPyLp9SzFhzTmwA2eTHXw1C8pump\",\"owner\":\"8N3GDaZ2iwN65oxVatKTLPNooAVUJTbfiVJ1ahyqwjSk\",\"programId\":\"TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA\",\"uiTokenAmount\":{\"uiAmount\":0.0,\"decimals\":6,\"amount\":\"0\",\"uiAmountString\":\"0\"}}]";
        String postBalanceString = "[2948371,2039280,232894334,120312382987,2039280,2039280,2039280,1,747831958,1461600,1,4674972223,109153227,2582160,4457474,1127253295471,13259293735015,1002000,0,18374400,1141440]";
        String preBalanceString = "[2954521,2039280,329517445,120215759876,2039280,2039280,2039280,1,747831958,1461600,1,4674972223,109153227,2582160,4457474,1127253295471,13259293735015,1002000,0,18374400,1141440]";
        String innerInstructionString = "[{\"index\":3,\"instructions\":[{\"accounts\":[19,12],\"data\":\"2BfZXS1GQrCLYKfSSHGxWziZfgGAyj1VLmdFcQZ9CPPVLK\",\"programIdIndex\":20,\"stackHeight\":2},{\"accounts\":[2,15,3,0],\"data\":\"gCNsdpAJ5aFrp\",\"programIdIndex\":11,\"stackHeight\":2},{\"accounts\":[4,9,1,13],\"data\":\"jESpzMPmF3cqB\",\"programIdIndex\":11,\"stackHeight\":2},{\"accounts\":[4,9,5,13],\"data\":\"gGmJida95wNEq\",\"programIdIndex\":11,\"stackHeight\":2},{\"accounts\":[17],\"data\":\"9k6unfwB8yYie7YGjfXzMuUwMXBEqhB1BtCQkdDVfTkQFsCHg2J4ZrkZnubLCbsNeSQ5L9wyAQaySTPFuzekxckeaD2dDEVPkEiD7Bp3HMnqntrQH2nJHcfsyPZa1piDz5iCBjjydWX58TqtMdKZ5VcFazyYMiwCmjFatVC5a4hgfjM5rwQtkngJyLvCtL8Xtx8QTzxSp8z6SQRJBvWDCjUifNzcRyAsQCF8DNTMEXNsoTPGZoLYiEjNaT55ErR5CGKcc2Qd1JcqQm49Z1Jn3LRaicorPWsynTQYKa9b18omtsh3Nn95gwhE4k3thRJN2MpuzYNKpkFtgFS9K6ydQ1tain1m7dUHPj2vaBwzW5vnEAv8D27522Jr5RsLRyvy57sDRGmMbuxYq75otFSkyWKU5Y1o5EKSjEriot43wdeVoCZQ2rSYxfthfk3AK8BL6Sc9MUJfqwNDbZQ1sThVoXgVf4YRQeoJB8ngsdVxmYdAwejy2G1RwJo\",\"programIdIndex\":12,\"stackHeight\":2}]}]";
        String outerInstructionString = "[{\"data\":\"3XiQEz8t9BJo\",\"programIdIndex\":7},{\"data\":\"K1wVZZ\",\"programIdIndex\":7},{\"accounts\":[0,1,0,9,10,11],\"data\":\"2\",\"programIdIndex\":8},{\"accounts\":[13,0,14,15,9,2,1,3,4,16,5,11,11,10,8,17,12,6,18,19,20],\"data\":\"5jRcjdixRUDEye1x2rEQTtDhuhr3YAa3H\",\"programIdIndex\":12}]";

        solanaParse.evaluate(chainId, protocol,
                slot, blockTime,
                accountKeys, logMessageString,
                writableAddressString, readonlyAddressString,
                postTokenBalanceString, preTokenBalanceString,
                postBalanceString, preBalanceString,
                innerInstructionString, outerInstructionString,
                hash, price

        );


    }


}