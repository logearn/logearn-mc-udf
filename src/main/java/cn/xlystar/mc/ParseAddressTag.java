package cn.xlystar.mc;

import cn.xlystar.helpers.ChainConfig;
import cn.xlystar.helpers.ConfigHelper;
import cn.xlystar.parse.ammswap.AMMSwapDataProcess;
import cn.xlystar.parse.ammswap.AddressTagProcess;
import com.alibaba.fastjson.JSON;
import com.aliyun.odps.udf.UDF;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 计算引擎：maxCompute
 * parse_address_tag(String num1, String num2, int scale)
 * <p>
 * num1：数值1
 * num2：数值1
 * scale: 小数点位数
 */
public class ParseAddressTag extends UDF {

    public ParseAddressTag() {
    }

    public String evaluate(String from, String to, String logs, String internalTxs, String hash, String chain, String protocol) throws IOException {
        ChainConfig conf = new ConfigHelper().getConfig(chain, protocol);
        Map<String, Set<String>> addressTagList = null;
        try {
            addressTagList = AddressTagProcess.getAddressTagList(from, to, conf, logs, internalTxs, hash);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(String.format("conf:%s, logs:%s, internalTxs:%s, hash:%s", conf, logs, internalTxs, hash));
        }
        return JSON.toJSONString(addressTagList);
    }

    public static void main(String[] args) throws Exception {
        String internalTxs  = "[{\n" +
                "            \"action\": {\n" +
                "                \"from\": \"0xe75ed6f453c602bd696ce27af11565edc9b46b0d\",\n" +
                "                \"callType\": \"call\",\n" +
                "                \"gas\": \"0x3b271\",\n" +
                "                \"input\": \"0xcb3fa300240000000000000000000000000000c02aaa39b223fe8d0a0e5c4f27ead9083c756cc22e1a7d4d000000000000000000000000000000000000000000000000155152cf000000003fa303c4000000000000155152cf000000006000da47483062a0d734ba3dc7576ce6a0b645c43f62192e00000000000000000000000000000000000000000000000000000000000000200000000000000000000000000000000000000000000000000000000000000040000000000000000000000000000000000000000000000000000000000000032000000000000000000000000000000000000000000000000000000000000002c000000000000000000000000000000000000000000000000000000000000000200000000000000000000000000000000000000000000000000000000000000120000000000000000000000000000000000000000000000000000000006704a7fa000000000000000000000000000000000000000000000000000000006704a7fa00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000576e2bed8f7b46d34016198911cdf9886f78bea7000000000000000000000000000000000000000000000000000000ba12f28d4c000000000000000000000000000000000000000000000000000000ba12f28d4c00000000000000000000000000000000000000000000000000000000000002000000000000000000000000006000da47483062a0d734ba3dc7576ce6a0b645c40000000000000000000000002808bff867d059e8105523f9ef44b61448e8f1ec0468321892ff77e1b06efbab936008c19bde2ba872b75e01714ce34eecf39d0b00000000000000000000000000000000000000000000000000000000670de27a000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000c0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000010000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000155110ce0a7e71c0000000000000000000000000000000000000000000000000155110ce0a7e71c00000000000000000000000002808bff867d059e8105523f9ef44b61448e8f1ec0000000000000000000000000000000000000000000000000000000000000041e09ff75fd1c5eccdb2bdaab27159c9a15be19a7ba60fb411c1944bac2c9f90f4115adc1aa44a7cd2043d426cf7ff836e9c07b993c24ad05cdaefad1d7146b7f61b000000000000000000000000000000000000000000000000000000000000003fa3002400000000000000000000000000006aa56e1d98b3805921c170eb4b3fe7d4fda6d89b24a29d50000000000000000000000000000000000000000000000000000000b8369916203fa3004400000000000000000000000000006aa56e1d98b3805921c170eb4b3fe7d4fda6d89ba9059cbb00000000000000000000000056bc8f3293f53825c9fbd6ddfe0cafefa82820d0000000000000000000000000000000000000000000000000000000b65f0313580f3256bc8f3293f53825c9fbd6ddfe0cafefa82820d0155773f33c006d\",\n" +
                "                \"to\": \"0x00000000009e50a7ddb7a7b0e2ee6604fd120e49\",\n" +
                "                \"value\": \"0x0\"\n" +
                "            },\n" +
                "            \"blockHash\": \"0x98eb9c1233d316ea60be1a815c54e09d0fe3f5de06710aeec78a93e0296d2b07\",\n" +
                "            \"blockNumber\": 20918219,\n" +
                "            \"result\": {\n" +
                "                \"gasUsed\": \"0x25eb0\",\n" +
                "                \"output\": \"0x\"\n" +
                "            },\n" +
                "            \"subtraces\": 5,\n" +
                "            \"traceAddress\": [],\n" +
                "            \"transactionHash\": \"0xecfa38ff3f827adfae9dbd875dcd12ad34a34bb2752d5aabaa44c1af170d149c\",\n" +
                "            \"transactionPosition\": 135,\n" +
                "            \"type\": \"call\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"action\": {\n" +
                "                \"from\": \"0x00000000009e50a7ddb7a7b0e2ee6604fd120e49\",\n" +
                "                \"callType\": \"call\",\n" +
                "                \"gas\": \"0x3a274\",\n" +
                "                \"input\": \"0x2e1a7d4d000000000000000000000000000000000000000000000000155152cf00000000\",\n" +
                "                \"to\": \"0xc02aaa39b223fe8d0a0e5c4f27ead9083c756cc2\",\n" +
                "                \"value\": \"0x0\"\n" +
                "            },\n" +
                "            \"blockHash\": \"0x98eb9c1233d316ea60be1a815c54e09d0fe3f5de06710aeec78a93e0296d2b07\",\n" +
                "            \"blockNumber\": 20918219,\n" +
                "            \"result\": {\n" +
                "                \"gasUsed\": \"0x2ecd\",\n" +
                "                \"output\": \"0x\"\n" +
                "            },\n" +
                "            \"subtraces\": 1,\n" +
                "            \"traceAddress\": [\n" +
                "                0\n" +
                "            ],\n" +
                "            \"transactionHash\": \"0xecfa38ff3f827adfae9dbd875dcd12ad34a34bb2752d5aabaa44c1af170d149c\",\n" +
                "            \"transactionPosition\": 135,\n" +
                "            \"type\": \"call\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"action\": {\n" +
                "                \"from\": \"0xc02aaa39b223fe8d0a0e5c4f27ead9083c756cc2\",\n" +
                "                \"callType\": \"call\",\n" +
                "                \"gas\": \"0x8fc\",\n" +
                "                \"input\": \"0x\",\n" +
                "                \"to\": \"0x00000000009e50a7ddb7a7b0e2ee6604fd120e49\",\n" +
                "                \"value\": \"0x155152cf00000000\"\n" +
                "            },\n" +
                "            \"blockHash\": \"0x98eb9c1233d316ea60be1a815c54e09d0fe3f5de06710aeec78a93e0296d2b07\",\n" +
                "            \"blockNumber\": 20918219,\n" +
                "            \"result\": {\n" +
                "                \"gasUsed\": \"0x29\",\n" +
                "                \"output\": \"0x\"\n" +
                "            },\n" +
                "            \"subtraces\": 0,\n" +
                "            \"traceAddress\": [\n" +
                "                0,\n" +
                "                0\n" +
                "            ],\n" +
                "            \"transactionHash\": \"0xecfa38ff3f827adfae9dbd875dcd12ad34a34bb2752d5aabaa44c1af170d149c\",\n" +
                "            \"transactionPosition\": 135,\n" +
                "            \"type\": \"call\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"action\": {\n" +
                "                \"from\": \"0x00000000009e50a7ddb7a7b0e2ee6604fd120e49\",\n" +
                "                \"callType\": \"call\",\n" +
                "                \"gas\": \"0x35930\",\n" +
                "                \"input\": \"0x3f62192e00000000000000000000000000000000000000000000000000000000000000200000000000000000000000000000000000000000000000000000000000000040000000000000000000000000000000000000000000000000000000000000032000000000000000000000000000000000000000000000000000000000000002c000000000000000000000000000000000000000000000000000000000000000200000000000000000000000000000000000000000000000000000000000000120000000000000000000000000000000000000000000000000000000006704a7fa000000000000000000000000000000000000000000000000000000006704a7fa00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000576e2bed8f7b46d34016198911cdf9886f78bea7000000000000000000000000000000000000000000000000000000ba12f28d4c000000000000000000000000000000000000000000000000000000ba12f28d4c00000000000000000000000000000000000000000000000000000000000002000000000000000000000000006000da47483062a0d734ba3dc7576ce6a0b645c40000000000000000000000002808bff867d059e8105523f9ef44b61448e8f1ec0468321892ff77e1b06efbab936008c19bde2ba872b75e01714ce34eecf39d0b00000000000000000000000000000000000000000000000000000000670de27a000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000c0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000010000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000155110ce0a7e71c0000000000000000000000000000000000000000000000000155110ce0a7e71c00000000000000000000000002808bff867d059e8105523f9ef44b61448e8f1ec0000000000000000000000000000000000000000000000000000000000000041e09ff75fd1c5eccdb2bdaab27159c9a15be19a7ba60fb411c1944bac2c9f90f4115adc1aa44a7cd2043d426cf7ff836e9c07b993c24ad05cdaefad1d7146b7f61b00000000000000000000000000000000000000000000000000000000000000\",\n" +
                "                \"to\": \"0x6000da47483062a0d734ba3dc7576ce6a0b645c4\",\n" +
                "                \"value\": \"0x155152cf00000000\"\n" +
                "            },\n" +
                "            \"blockHash\": \"0x98eb9c1233d316ea60be1a815c54e09d0fe3f5de06710aeec78a93e0296d2b07\",\n" +
                "            \"blockNumber\": 20918219,\n" +
                "            \"result\": {\n" +
                "                \"gasUsed\": \"0x10ede\",\n" +
                "                \"output\": \"0x\"\n" +
                "            },\n" +
                "            \"subtraces\": 3,\n" +
                "            \"traceAddress\": [\n" +
                "                1\n" +
                "            ],\n" +
                "            \"transactionHash\": \"0xecfa38ff3f827adfae9dbd875dcd12ad34a34bb2752d5aabaa44c1af170d149c\",\n" +
                "            \"transactionPosition\": 135,\n" +
                "            \"type\": \"call\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"action\": {\n" +
                "                \"from\": \"0x6000da47483062a0d734ba3dc7576ce6a0b645c4\",\n" +
                "                \"callType\": \"call\",\n" +
                "                \"gas\": \"0x302ff\",\n" +
                "                \"input\": \"0x137c29fe000000000000000000000000576e2bed8f7b46d34016198911cdf9886f78bea7000000000000000000000000000000000000000000000000000000ba12f28d4c0468321892ff77e1b06efbab936008c19bde2ba872b75e01714ce34eecf39d0b00000000000000000000000000000000000000000000000000000000670de27a00000000000000000000000000000000009e50a7ddb7a7b0e2ee6604fd120e49000000000000000000000000000000000000000000000000000000ba12f28d4c0000000000000000000000002808bff867d059e8105523f9ef44b61448e8f1ec2a50572f3a46a26ffc80bfdbeb181921e37715d0544f36dc8c343d28476b6d2d0000000000000000000000000000000000000000000000000000000000000140000000000000000000000000000000000000000000000000000000000000038000000000000000000000000000000000000000000000000000000000000002084578636c757369766544757463684f72646572207769746e6573732944757463684f7574707574286164647265737320746f6b656e2c75696e74323536207374617274416d6f756e742c75696e7432353620656e64416d6f756e742c6164647265737320726563697069656e74294578636c757369766544757463684f72646572284f72646572496e666f20696e666f2c75696e74323536206465636179537461727454696d652c75696e74323536206465636179456e6454696d652c61646472657373206578636c757369766546696c6c65722c75696e74323536206578636c757369766974794f766572726964654270732c6164647265737320696e707574546f6b656e2c75696e7432353620696e7075745374617274416d6f756e742c75696e7432353620696e707574456e64416d6f756e742c44757463684f75747075745b5d206f757470757473294f72646572496e666f28616464726573732072656163746f722c6164647265737320737761707065722c75696e74323536206e6f6e63652c75696e7432353620646561646c696e652c61646472657373206164646974696f6e616c56616c69646174696f6e436f6e74726163742c6279746573206164646974696f6e616c56616c69646174696f6e4461746129546f6b656e5065726d697373696f6e73286164647265737320746f6b656e2c75696e7432353620616d6f756e74290000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000041e09ff75fd1c5eccdb2bdaab27159c9a15be19a7ba60fb411c1944bac2c9f90f4115adc1aa44a7cd2043d426cf7ff836e9c07b993c24ad05cdaefad1d7146b7f61b00000000000000000000000000000000000000000000000000000000000000\",\n" +
                "                \"to\": \"0x000000000022d473030f116ddee9f6b43ac78ba3\",\n" +
                "                \"value\": \"0x0\"\n" +
                "            },\n" +
                "            \"blockHash\": \"0x98eb9c1233d316ea60be1a815c54e09d0fe3f5de06710aeec78a93e0296d2b07\",\n" +
                "            \"blockNumber\": 20918219,\n" +
                "            \"result\": {\n" +
                "                \"gasUsed\": \"0x8286\",\n" +
                "                \"output\": \"0x\"\n" +
                "            },\n" +
                "            \"subtraces\": 1,\n" +
                "            \"traceAddress\": [\n" +
                "                1,\n" +
                "                0\n" +
                "            ],\n" +
                "            \"transactionHash\": \"0xecfa38ff3f827adfae9dbd875dcd12ad34a34bb2752d5aabaa44c1af170d149c\",\n" +
                "            \"transactionPosition\": 135,\n" +
                "            \"type\": \"call\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"action\": {\n" +
                "                \"from\": \"0x000000000022d473030f116ddee9f6b43ac78ba3\",\n" +
                "                \"callType\": \"call\",\n" +
                "                \"gas\": \"0x2d045\",\n" +
                "                \"input\": \"0x23b872dd0000000000000000000000002808bff867d059e8105523f9ef44b61448e8f1ec00000000000000000000000000000000009e50a7ddb7a7b0e2ee6604fd120e49000000000000000000000000000000000000000000000000000000ba12f28d4c\",\n" +
                "                \"to\": \"0x576e2bed8f7b46d34016198911cdf9886f78bea7\",\n" +
                "                \"value\": \"0x0\"\n" +
                "            },\n" +
                "            \"blockHash\": \"0x98eb9c1233d316ea60be1a815c54e09d0fe3f5de06710aeec78a93e0296d2b07\",\n" +
                "            \"blockNumber\": 20918219,\n" +
                "            \"result\": {\n" +
                "                \"gasUsed\": \"0x5af3\",\n" +
                "                \"output\": \"0x0000000000000000000000000000000000000000000000000000000000000001\"\n" +
                "            },\n" +
                "            \"subtraces\": 0,\n" +
                "            \"traceAddress\": [\n" +
                "                1,\n" +
                "                0,\n" +
                "                0\n" +
                "            ],\n" +
                "            \"transactionHash\": \"0xecfa38ff3f827adfae9dbd875dcd12ad34a34bb2752d5aabaa44c1af170d149c\",\n" +
                "            \"transactionPosition\": 135,\n" +
                "            \"type\": \"call\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"action\": {\n" +
                "                \"from\": \"0x6000da47483062a0d734ba3dc7576ce6a0b645c4\",\n" +
                "                \"callType\": \"call\",\n" +
                "                \"gas\": \"0x23f0\",\n" +
                "                \"input\": \"0x\",\n" +
                "                \"to\": \"0x2808bff867d059e8105523f9ef44b61448e8f1ec\",\n" +
                "                \"value\": \"0x155110ce0a7e71c0\"\n" +
                "            },\n" +
                "            \"blockHash\": \"0x98eb9c1233d316ea60be1a815c54e09d0fe3f5de06710aeec78a93e0296d2b07\",\n" +
                "            \"blockNumber\": 20918219,\n" +
                "            \"result\": {\n" +
                "                \"gasUsed\": \"0x0\",\n" +
                "                \"output\": \"0x\"\n" +
                "            },\n" +
                "            \"subtraces\": 0,\n" +
                "            \"traceAddress\": [\n" +
                "                1,\n" +
                "                1\n" +
                "            ],\n" +
                "            \"transactionHash\": \"0xecfa38ff3f827adfae9dbd875dcd12ad34a34bb2752d5aabaa44c1af170d149c\",\n" +
                "            \"transactionPosition\": 135,\n" +
                "            \"type\": \"call\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"action\": {\n" +
                "                \"from\": \"0x6000da47483062a0d734ba3dc7576ce6a0b645c4\",\n" +
                "                \"callType\": \"call\",\n" +
                "                \"gas\": \"0x23f0\",\n" +
                "                \"input\": \"0x\",\n" +
                "                \"to\": \"0x00000000009e50a7ddb7a7b0e2ee6604fd120e49\",\n" +
                "                \"value\": \"0x4200f5818e40\"\n" +
                "            },\n" +
                "            \"blockHash\": \"0x98eb9c1233d316ea60be1a815c54e09d0fe3f5de06710aeec78a93e0296d2b07\",\n" +
                "            \"blockNumber\": 20918219,\n" +
                "            \"result\": {\n" +
                "                \"gasUsed\": \"0x29\",\n" +
                "                \"output\": \"0x\"\n" +
                "            },\n" +
                "            \"subtraces\": 0,\n" +
                "            \"traceAddress\": [\n" +
                "                1,\n" +
                "                2\n" +
                "            ],\n" +
                "            \"transactionHash\": \"0xecfa38ff3f827adfae9dbd875dcd12ad34a34bb2752d5aabaa44c1af170d149c\",\n" +
                "            \"transactionPosition\": 135,\n" +
                "            \"type\": \"call\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"action\": {\n" +
                "                \"from\": \"0x00000000009e50a7ddb7a7b0e2ee6604fd120e49\",\n" +
                "                \"callType\": \"call\",\n" +
                "                \"gas\": \"0x24d83\",\n" +
                "                \"input\": \"0x24a29d50000000000000000000000000000000000000000000000000000000b836991620\",\n" +
                "                \"to\": \"0x6aa56e1d98b3805921c170eb4b3fe7d4fda6d89b\",\n" +
                "                \"value\": \"0x0\"\n" +
                "            },\n" +
                "            \"blockHash\": \"0x98eb9c1233d316ea60be1a815c54e09d0fe3f5de06710aeec78a93e0296d2b07\",\n" +
                "            \"blockNumber\": 20918219,\n" +
                "            \"result\": {\n" +
                "                \"gasUsed\": \"0x769b\",\n" +
                "                \"output\": \"0x\"\n" +
                "            },\n" +
                "            \"subtraces\": 3,\n" +
                "            \"traceAddress\": [\n" +
                "                2\n" +
                "            ],\n" +
                "            \"transactionHash\": \"0xecfa38ff3f827adfae9dbd875dcd12ad34a34bb2752d5aabaa44c1af170d149c\",\n" +
                "            \"transactionPosition\": 135,\n" +
                "            \"type\": \"call\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"action\": {\n" +
                "                \"from\": \"0x6aa56e1d98b3805921c170eb4b3fe7d4fda6d89b\",\n" +
                "                \"callType\": \"staticcall\",\n" +
                "                \"gas\": \"0x24102\",\n" +
                "                \"input\": \"0x70a082310000000000000000000000006aa56e1d98b3805921c170eb4b3fe7d4fda6d89b\",\n" +
                "                \"to\": \"0x576e2bed8f7b46d34016198911cdf9886f78bea7\",\n" +
                "                \"value\": \"0x0\"\n" +
                "            },\n" +
                "            \"blockHash\": \"0x98eb9c1233d316ea60be1a815c54e09d0fe3f5de06710aeec78a93e0296d2b07\",\n" +
                "            \"blockNumber\": 20918219,\n" +
                "            \"result\": {\n" +
                "                \"gasUsed\": \"0x253\",\n" +
                "                \"output\": \"0x00000000000000000000000000000000000000000000000000045df1fba97b24\"\n" +
                "            },\n" +
                "            \"subtraces\": 0,\n" +
                "            \"traceAddress\": [\n" +
                "                2,\n" +
                "                0\n" +
                "            ],\n" +
                "            \"transactionHash\": \"0xecfa38ff3f827adfae9dbd875dcd12ad34a34bb2752d5aabaa44c1af170d149c\",\n" +
                "            \"transactionPosition\": 135,\n" +
                "            \"type\": \"call\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"action\": {\n" +
                "                \"from\": \"0x6aa56e1d98b3805921c170eb4b3fe7d4fda6d89b\",\n" +
                "                \"callType\": \"call\",\n" +
                "                \"gas\": \"0x23b14\",\n" +
                "                \"input\": \"0x23b872dd00000000000000000000000000000000009e50a7ddb7a7b0e2ee6604fd120e490000000000000000000000006aa56e1d98b3805921c170eb4b3fe7d4fda6d89b000000000000000000000000000000000000000000000000000000b836991620\",\n" +
                "                \"to\": \"0x576e2bed8f7b46d34016198911cdf9886f78bea7\",\n" +
                "                \"value\": \"0x0\"\n" +
                "            },\n" +
                "            \"blockHash\": \"0x98eb9c1233d316ea60be1a815c54e09d0fe3f5de06710aeec78a93e0296d2b07\",\n" +
                "            \"blockNumber\": 20918219,\n" +
                "            \"result\": {\n" +
                "                \"gasUsed\": \"0x4513\",\n" +
                "                \"output\": \"0x0000000000000000000000000000000000000000000000000000000000000001\"\n" +
                "            },\n" +
                "            \"subtraces\": 0,\n" +
                "            \"traceAddress\": [\n" +
                "                2,\n" +
                "                1\n" +
                "            ],\n" +
                "            \"transactionHash\": \"0xecfa38ff3f827adfae9dbd875dcd12ad34a34bb2752d5aabaa44c1af170d149c\",\n" +
                "            \"transactionPosition\": 135,\n" +
                "            \"type\": \"call\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"action\": {\n" +
                "                \"from\": \"0x6aa56e1d98b3805921c170eb4b3fe7d4fda6d89b\",\n" +
                "                \"callType\": \"staticcall\",\n" +
                "                \"gas\": \"0x1f465\",\n" +
                "                \"input\": \"0x70a082310000000000000000000000006aa56e1d98b3805921c170eb4b3fe7d4fda6d89b\",\n" +
                "                \"to\": \"0x576e2bed8f7b46d34016198911cdf9886f78bea7\",\n" +
                "                \"value\": \"0x0\"\n" +
                "            },\n" +
                "            \"blockHash\": \"0x98eb9c1233d316ea60be1a815c54e09d0fe3f5de06710aeec78a93e0296d2b07\",\n" +
                "            \"blockNumber\": 20918219,\n" +
                "            \"result\": {\n" +
                "                \"gasUsed\": \"0x253\",\n" +
                "                \"output\": \"0x00000000000000000000000000000000000000000000000000045ea85aac8e7c\"\n" +
                "            },\n" +
                "            \"subtraces\": 0,\n" +
                "            \"traceAddress\": [\n" +
                "                2,\n" +
                "                2\n" +
                "            ],\n" +
                "            \"transactionHash\": \"0xecfa38ff3f827adfae9dbd875dcd12ad34a34bb2752d5aabaa44c1af170d149c\",\n" +
                "            \"transactionPosition\": 135,\n" +
                "            \"type\": \"call\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"action\": {\n" +
                "                \"from\": \"0x00000000009e50a7ddb7a7b0e2ee6604fd120e49\",\n" +
                "                \"callType\": \"call\",\n" +
                "                \"gas\": \"0x1d7d9\",\n" +
                "                \"input\": \"0xa9059cbb00000000000000000000000056bc8f3293f53825c9fbd6ddfe0cafefa82820d0000000000000000000000000000000000000000000000000000000b65f031358\",\n" +
                "                \"to\": \"0x6aa56e1d98b3805921c170eb4b3fe7d4fda6d89b\",\n" +
                "                \"value\": \"0x0\"\n" +
                "            },\n" +
                "            \"blockHash\": \"0x98eb9c1233d316ea60be1a815c54e09d0fe3f5de06710aeec78a93e0296d2b07\",\n" +
                "            \"blockNumber\": 20918219,\n" +
                "            \"result\": {\n" +
                "                \"gasUsed\": \"0x19fa\",\n" +
                "                \"output\": \"0x0000000000000000000000000000000000000000000000000000000000000001\"\n" +
                "            },\n" +
                "            \"subtraces\": 0,\n" +
                "            \"traceAddress\": [\n" +
                "                3\n" +
                "            ],\n" +
                "            \"transactionHash\": \"0xecfa38ff3f827adfae9dbd875dcd12ad34a34bb2752d5aabaa44c1af170d149c\",\n" +
                "            \"transactionPosition\": 135,\n" +
                "            \"type\": \"call\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"action\": {\n" +
                "                \"from\": \"0x00000000009e50a7ddb7a7b0e2ee6604fd120e49\",\n" +
                "                \"callType\": \"call\",\n" +
                "                \"gas\": \"0x1bd3f\",\n" +
                "                \"input\": \"0x022c0d9f0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000155773f30000000000000000000000000000000000000000009e50a7ddb7a7b0e2ee6604fd120e4900000000000000000000000000000000000000000000000000000000000000800000000000000000000000000000000000000000000000000000000000000000\",\n" +
                "                \"to\": \"0x56bc8f3293f53825c9fbd6ddfe0cafefa82820d0\",\n" +
                "                \"value\": \"0x0\"\n" +
                "            },\n" +
                "            \"blockHash\": \"0x98eb9c1233d316ea60be1a815c54e09d0fe3f5de06710aeec78a93e0296d2b07\",\n" +
                "            \"blockNumber\": 20918219,\n" +
                "            \"result\": {\n" +
                "                \"gasUsed\": \"0x7062\",\n" +
                "                \"output\": \"0x\"\n" +
                "            },\n" +
                "            \"subtraces\": 3,\n" +
                "            \"traceAddress\": [\n" +
                "                4\n" +
                "            ],\n" +
                "            \"transactionHash\": \"0xecfa38ff3f827adfae9dbd875dcd12ad34a34bb2752d5aabaa44c1af170d149c\",\n" +
                "            \"transactionPosition\": 135,\n" +
                "            \"type\": \"call\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"action\": {\n" +
                "                \"from\": \"0x56bc8f3293f53825c9fbd6ddfe0cafefa82820d0\",\n" +
                "                \"callType\": \"call\",\n" +
                "                \"gas\": \"0x1a36d\",\n" +
                "                \"input\": \"0xa9059cbb00000000000000000000000000000000009e50a7ddb7a7b0e2ee6604fd120e49000000000000000000000000000000000000000000000000155773f300000000\",\n" +
                "                \"to\": \"0xc02aaa39b223fe8d0a0e5c4f27ead9083c756cc2\",\n" +
                "                \"value\": \"0x0\"\n" +
                "            },\n" +
                "            \"blockHash\": \"0x98eb9c1233d316ea60be1a815c54e09d0fe3f5de06710aeec78a93e0296d2b07\",\n" +
                "            \"blockNumber\": 20918219,\n" +
                "            \"result\": {\n" +
                "                \"gasUsed\": \"0x17ae\",\n" +
                "                \"output\": \"0x0000000000000000000000000000000000000000000000000000000000000001\"\n" +
                "            },\n" +
                "            \"subtraces\": 0,\n" +
                "            \"traceAddress\": [\n" +
                "                4,\n" +
                "                0\n" +
                "            ],\n" +
                "            \"transactionHash\": \"0xecfa38ff3f827adfae9dbd875dcd12ad34a34bb2752d5aabaa44c1af170d149c\",\n" +
                "            \"transactionPosition\": 135,\n" +
                "            \"type\": \"call\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"action\": {\n" +
                "                \"from\": \"0x56bc8f3293f53825c9fbd6ddfe0cafefa82820d0\",\n" +
                "                \"callType\": \"staticcall\",\n" +
                "                \"gas\": \"0x189c4\",\n" +
                "                \"input\": \"0x70a0823100000000000000000000000056bc8f3293f53825c9fbd6ddfe0cafefa82820d0\",\n" +
                "                \"to\": \"0x6aa56e1d98b3805921c170eb4b3fe7d4fda6d89b\",\n" +
                "                \"value\": \"0x0\"\n" +
                "            },\n" +
                "            \"blockHash\": \"0x98eb9c1233d316ea60be1a815c54e09d0fe3f5de06710aeec78a93e0296d2b07\",\n" +
                "            \"blockNumber\": 20918219,\n" +
                "            \"result\": {\n" +
                "                \"gasUsed\": \"0x353\",\n" +
                "                \"output\": \"0x000000000000000000000000000000000000000000000000000036de2af01cb9\"\n" +
                "            },\n" +
                "            \"subtraces\": 0,\n" +
                "            \"traceAddress\": [\n" +
                "                4,\n" +
                "                1\n" +
                "            ],\n" +
                "            \"transactionHash\": \"0xecfa38ff3f827adfae9dbd875dcd12ad34a34bb2752d5aabaa44c1af170d149c\",\n" +
                "            \"transactionPosition\": 135,\n" +
                "            \"type\": \"call\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"action\": {\n" +
                "                \"from\": \"0x56bc8f3293f53825c9fbd6ddfe0cafefa82820d0\",\n" +
                "                \"callType\": \"staticcall\",\n" +
                "                \"gas\": \"0x184e9\",\n" +
                "                \"input\": \"0x70a0823100000000000000000000000056bc8f3293f53825c9fbd6ddfe0cafefa82820d0\",\n" +
                "                \"to\": \"0xc02aaa39b223fe8d0a0e5c4f27ead9083c756cc2\",\n" +
                "                \"value\": \"0x0\"\n" +
                "            },\n" +
                "            \"blockHash\": \"0x98eb9c1233d316ea60be1a815c54e09d0fe3f5de06710aeec78a93e0296d2b07\",\n" +
                "            \"blockNumber\": 20918219,\n" +
                "            \"result\": {\n" +
                "                \"gasUsed\": \"0x216\",\n" +
                "                \"output\": \"0x0000000000000000000000000000000000000000000000065b43b5430803121a\"\n" +
                "            },\n" +
                "            \"subtraces\": 0,\n" +
                "            \"traceAddress\": [\n" +
                "                4,\n" +
                "                2\n" +
                "            ],\n" +
                "            \"transactionHash\": \"0xecfa38ff3f827adfae9dbd875dcd12ad34a34bb2752d5aabaa44c1af170d149c\",\n" +
                "            \"transactionPosition\": 135,\n" +
                "            \"type\": \"call\"\n" +
                "        }]";
        String logs = "{\"logs\": [\n" +
                "            {\n" +
                "                \"address\": \"0xc02aaa39b223fe8d0a0e5c4f27ead9083c756cc2\",\n" +
                "                \"blockHash\": \"0x98eb9c1233d316ea60be1a815c54e09d0fe3f5de06710aeec78a93e0296d2b07\",\n" +
                "                \"blockNumber\": \"0x13f2fcb\",\n" +
                "                \"data\": \"0x000000000000000000000000000000000000000000000000155152cf00000000\",\n" +
                "                \"logIndex\": \"0x1bc\",\n" +
                "                \"removed\": false,\n" +
                "                \"topics\": [\n" +
                "                    \"0x7fcf532c15f0a6db0bd6d0e038bea71d30d808c7d98cb3bf7268a95bf5081b65\",\n" +
                "                    \"0x00000000000000000000000000000000009e50a7ddb7a7b0e2ee6604fd120e49\"\n" +
                "                ],\n" +
                "                \"transactionHash\": \"0xecfa38ff3f827adfae9dbd875dcd12ad34a34bb2752d5aabaa44c1af170d149c\",\n" +
                "                \"transactionIndex\": \"0x87\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"address\": \"0x576e2bed8f7b46d34016198911cdf9886f78bea7\",\n" +
                "                \"blockHash\": \"0x98eb9c1233d316ea60be1a815c54e09d0fe3f5de06710aeec78a93e0296d2b07\",\n" +
                "                \"blockNumber\": \"0x13f2fcb\",\n" +
                "                \"data\": \"0x00000000000000000000000000000000000000000000000000000001dc59772c\",\n" +
                "                \"logIndex\": \"0x1bd\",\n" +
                "                \"removed\": false,\n" +
                "                \"topics\": [\n" +
                "                    \"0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef\",\n" +
                "                    \"0x0000000000000000000000002808bff867d059e8105523f9ef44b61448e8f1ec\",\n" +
                "                    \"0x000000000000000000000000576e2bed8f7b46d34016198911cdf9886f78bea7\"\n" +
                "                ],\n" +
                "                \"transactionHash\": \"0xecfa38ff3f827adfae9dbd875dcd12ad34a34bb2752d5aabaa44c1af170d149c\",\n" +
                "                \"transactionIndex\": \"0x87\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"address\": \"0x576e2bed8f7b46d34016198911cdf9886f78bea7\",\n" +
                "                \"blockHash\": \"0x98eb9c1233d316ea60be1a815c54e09d0fe3f5de06710aeec78a93e0296d2b07\",\n" +
                "                \"blockNumber\": \"0x13f2fcb\",\n" +
                "                \"data\": \"0x000000000000000000000000000000000000000000000000000000b836991620\",\n" +
                "                \"logIndex\": \"0x1be\",\n" +
                "                \"removed\": false,\n" +
                "                \"topics\": [\n" +
                "                    \"0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef\",\n" +
                "                    \"0x0000000000000000000000002808bff867d059e8105523f9ef44b61448e8f1ec\",\n" +
                "                    \"0x00000000000000000000000000000000009e50a7ddb7a7b0e2ee6604fd120e49\"\n" +
                "                ],\n" +
                "                \"transactionHash\": \"0xecfa38ff3f827adfae9dbd875dcd12ad34a34bb2752d5aabaa44c1af170d149c\",\n" +
                "                \"transactionIndex\": \"0x87\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"address\": \"0x576e2bed8f7b46d34016198911cdf9886f78bea7\",\n" +
                "                \"blockHash\": \"0x98eb9c1233d316ea60be1a815c54e09d0fe3f5de06710aeec78a93e0296d2b07\",\n" +
                "                \"blockNumber\": \"0x13f2fcb\",\n" +
                "                \"data\": \"0xffffffffffffffffffffffffffffffffffffffffffffffffffffff45ed0d72b3\",\n" +
                "                \"logIndex\": \"0x1bf\",\n" +
                "                \"removed\": false,\n" +
                "                \"topics\": [\n" +
                "                    \"0x8c5be1e5ebec7d5bd14f71427d1e84f3dd0314c0f7b2291e5b200ac8c7c3b925\",\n" +
                "                    \"0x0000000000000000000000002808bff867d059e8105523f9ef44b61448e8f1ec\",\n" +
                "                    \"0x000000000000000000000000000000000022d473030f116ddee9f6b43ac78ba3\"\n" +
                "                ],\n" +
                "                \"transactionHash\": \"0xecfa38ff3f827adfae9dbd875dcd12ad34a34bb2752d5aabaa44c1af170d149c\",\n" +
                "                \"transactionIndex\": \"0x87\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"address\": \"0x6000da47483062a0d734ba3dc7576ce6a0b645c4\",\n" +
                "                \"blockHash\": \"0x98eb9c1233d316ea60be1a815c54e09d0fe3f5de06710aeec78a93e0296d2b07\",\n" +
                "                \"blockNumber\": \"0x13f2fcb\",\n" +
                "                \"data\": \"0x0468321892ff77e1b06efbab936008c19bde2ba872b75e01714ce34eecf39d0b\",\n" +
                "                \"logIndex\": \"0x1c0\",\n" +
                "                \"removed\": false,\n" +
                "                \"topics\": [\n" +
                "                    \"0x78ad7ec0e9f89e74012afa58738b6b661c024cb0fd185ee2f616c0a28924bd66\",\n" +
                "                    \"0x2a50572f3a46a26ffc80bfdbeb181921e37715d0544f36dc8c343d28476b6d2d\",\n" +
                "                    \"0x00000000000000000000000000000000009e50a7ddb7a7b0e2ee6604fd120e49\",\n" +
                "                    \"0x0000000000000000000000002808bff867d059e8105523f9ef44b61448e8f1ec\"\n" +
                "                ],\n" +
                "                \"transactionHash\": \"0xecfa38ff3f827adfae9dbd875dcd12ad34a34bb2752d5aabaa44c1af170d149c\",\n" +
                "                \"transactionIndex\": \"0x87\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"address\": \"0x576e2bed8f7b46d34016198911cdf9886f78bea7\",\n" +
                "                \"blockHash\": \"0x98eb9c1233d316ea60be1a815c54e09d0fe3f5de06710aeec78a93e0296d2b07\",\n" +
                "                \"blockNumber\": \"0x13f2fcb\",\n" +
                "                \"data\": \"0x00000000000000000000000000000000000000000000000000000001d79602c8\",\n" +
                "                \"logIndex\": \"0x1c1\",\n" +
                "                \"removed\": false,\n" +
                "                \"topics\": [\n" +
                "                    \"0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef\",\n" +
                "                    \"0x00000000000000000000000000000000009e50a7ddb7a7b0e2ee6604fd120e49\",\n" +
                "                    \"0x000000000000000000000000576e2bed8f7b46d34016198911cdf9886f78bea7\"\n" +
                "                ],\n" +
                "                \"transactionHash\": \"0xecfa38ff3f827adfae9dbd875dcd12ad34a34bb2752d5aabaa44c1af170d149c\",\n" +
                "                \"transactionIndex\": \"0x87\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"address\": \"0x576e2bed8f7b46d34016198911cdf9886f78bea7\",\n" +
                "                \"blockHash\": \"0x98eb9c1233d316ea60be1a815c54e09d0fe3f5de06710aeec78a93e0296d2b07\",\n" +
                "                \"blockNumber\": \"0x13f2fcb\",\n" +
                "                \"data\": \"0x000000000000000000000000000000000000000000000000000000b65f031358\",\n" +
                "                \"logIndex\": \"0x1c2\",\n" +
                "                \"removed\": false,\n" +
                "                \"topics\": [\n" +
                "                    \"0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef\",\n" +
                "                    \"0x00000000000000000000000000000000009e50a7ddb7a7b0e2ee6604fd120e49\",\n" +
                "                    \"0x0000000000000000000000006aa56e1d98b3805921c170eb4b3fe7d4fda6d89b\"\n" +
                "                ],\n" +
                "                \"transactionHash\": \"0xecfa38ff3f827adfae9dbd875dcd12ad34a34bb2752d5aabaa44c1af170d149c\",\n" +
                "                \"transactionIndex\": \"0x87\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"address\": \"0x576e2bed8f7b46d34016198911cdf9886f78bea7\",\n" +
                "                \"blockHash\": \"0x98eb9c1233d316ea60be1a815c54e09d0fe3f5de06710aeec78a93e0296d2b07\",\n" +
                "                \"blockNumber\": \"0x13f2fcb\",\n" +
                "                \"data\": \"0xffffffffffffffffffffffffffffffffffffffffffffffffffff8b033c77ef5a\",\n" +
                "                \"logIndex\": \"0x1c3\",\n" +
                "                \"removed\": false,\n" +
                "                \"topics\": [\n" +
                "                    \"0x8c5be1e5ebec7d5bd14f71427d1e84f3dd0314c0f7b2291e5b200ac8c7c3b925\",\n" +
                "                    \"0x00000000000000000000000000000000009e50a7ddb7a7b0e2ee6604fd120e49\",\n" +
                "                    \"0x0000000000000000000000006aa56e1d98b3805921c170eb4b3fe7d4fda6d89b\"\n" +
                "                ],\n" +
                "                \"transactionHash\": \"0xecfa38ff3f827adfae9dbd875dcd12ad34a34bb2752d5aabaa44c1af170d149c\",\n" +
                "                \"transactionIndex\": \"0x87\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"address\": \"0x6aa56e1d98b3805921c170eb4b3fe7d4fda6d89b\",\n" +
                "                \"blockHash\": \"0x98eb9c1233d316ea60be1a815c54e09d0fe3f5de06710aeec78a93e0296d2b07\",\n" +
                "                \"blockNumber\": \"0x13f2fcb\",\n" +
                "                \"data\": \"0x000000000000000000000000000000000000000000000000000000b65f031358\",\n" +
                "                \"logIndex\": \"0x1c4\",\n" +
                "                \"removed\": false,\n" +
                "                \"topics\": [\n" +
                "                    \"0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef\",\n" +
                "                    \"0x0000000000000000000000000000000000000000000000000000000000000000\",\n" +
                "                    \"0x00000000000000000000000000000000009e50a7ddb7a7b0e2ee6604fd120e49\"\n" +
                "                ],\n" +
                "                \"transactionHash\": \"0xecfa38ff3f827adfae9dbd875dcd12ad34a34bb2752d5aabaa44c1af170d149c\",\n" +
                "                \"transactionIndex\": \"0x87\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"address\": \"0x6aa56e1d98b3805921c170eb4b3fe7d4fda6d89b\",\n" +
                "                \"blockHash\": \"0x98eb9c1233d316ea60be1a815c54e09d0fe3f5de06710aeec78a93e0296d2b07\",\n" +
                "                \"blockNumber\": \"0x13f2fcb\",\n" +
                "                \"data\": \"0x000000000000000000000000000000000000000000000000000000b65f031358\",\n" +
                "                \"logIndex\": \"0x1c5\",\n" +
                "                \"removed\": false,\n" +
                "                \"topics\": [\n" +
                "                    \"0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef\",\n" +
                "                    \"0x00000000000000000000000000000000009e50a7ddb7a7b0e2ee6604fd120e49\",\n" +
                "                    \"0x00000000000000000000000056bc8f3293f53825c9fbd6ddfe0cafefa82820d0\"\n" +
                "                ],\n" +
                "                \"transactionHash\": \"0xecfa38ff3f827adfae9dbd875dcd12ad34a34bb2752d5aabaa44c1af170d149c\",\n" +
                "                \"transactionIndex\": \"0x87\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"address\": \"0xc02aaa39b223fe8d0a0e5c4f27ead9083c756cc2\",\n" +
                "                \"blockHash\": \"0x98eb9c1233d316ea60be1a815c54e09d0fe3f5de06710aeec78a93e0296d2b07\",\n" +
                "                \"blockNumber\": \"0x13f2fcb\",\n" +
                "                \"data\": \"0x000000000000000000000000000000000000000000000000155773f300000000\",\n" +
                "                \"logIndex\": \"0x1c6\",\n" +
                "                \"removed\": false,\n" +
                "                \"topics\": [\n" +
                "                    \"0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef\",\n" +
                "                    \"0x00000000000000000000000056bc8f3293f53825c9fbd6ddfe0cafefa82820d0\",\n" +
                "                    \"0x00000000000000000000000000000000009e50a7ddb7a7b0e2ee6604fd120e49\"\n" +
                "                ],\n" +
                "                \"transactionHash\": \"0xecfa38ff3f827adfae9dbd875dcd12ad34a34bb2752d5aabaa44c1af170d149c\",\n" +
                "                \"transactionIndex\": \"0x87\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"address\": \"0x56bc8f3293f53825c9fbd6ddfe0cafefa82820d0\",\n" +
                "                \"blockHash\": \"0x98eb9c1233d316ea60be1a815c54e09d0fe3f5de06710aeec78a93e0296d2b07\",\n" +
                "                \"blockNumber\": \"0x13f2fcb\",\n" +
                "                \"data\": \"0x000000000000000000000000000000000000000000000000000036de2af01cb90000000000000000000000000000000000000000000000065b43b5430803121a\",\n" +
                "                \"logIndex\": \"0x1c7\",\n" +
                "                \"removed\": false,\n" +
                "                \"topics\": [\n" +
                "                    \"0x1c411e9a96e071241c2f21f7726b17ae89e3cab4c78be50e062b03a9fffbbad1\"\n" +
                "                ],\n" +
                "                \"transactionHash\": \"0xecfa38ff3f827adfae9dbd875dcd12ad34a34bb2752d5aabaa44c1af170d149c\",\n" +
                "                \"transactionIndex\": \"0x87\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"address\": \"0x56bc8f3293f53825c9fbd6ddfe0cafefa82820d0\",\n" +
                "                \"blockHash\": \"0x98eb9c1233d316ea60be1a815c54e09d0fe3f5de06710aeec78a93e0296d2b07\",\n" +
                "                \"blockNumber\": \"0x13f2fcb\",\n" +
                "                \"data\": \"0x000000000000000000000000000000000000000000000000000000b65f03135800000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000155773f300000000\",\n" +
                "                \"logIndex\": \"0x1c8\",\n" +
                "                \"removed\": false,\n" +
                "                \"topics\": [\n" +
                "                    \"0xd78ad95fa46c994b6551d0da85fc275fe613ce37657fb8d5e3d130840159d822\",\n" +
                "                    \"0x00000000000000000000000000000000009e50a7ddb7a7b0e2ee6604fd120e49\",\n" +
                "                    \"0x00000000000000000000000000000000009e50a7ddb7a7b0e2ee6604fd120e49\"\n" +
                "                ],\n" +
                "                \"transactionHash\": \"0xecfa38ff3f827adfae9dbd875dcd12ad34a34bb2752d5aabaa44c1af170d149c\",\n" +
                "                \"transactionIndex\": \"0x87\"\n" +
                "            }\n" +
                "        ]}";
        String evaluate = new ParseAddressTag().evaluate("","",logs, internalTxs,"0x0","1","uniswap");
    }
}
