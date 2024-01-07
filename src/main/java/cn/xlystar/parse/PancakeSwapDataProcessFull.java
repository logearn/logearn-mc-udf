package cn.xlystar.parse;

import cn.xlystar.entity.TransferEvent;
import cn.xlystar.entity.UniswapEvent;
import cn.xlystar.entity.UniswapV2Event;
import cn.xlystar.entity.UniswapV3Event;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PancakeSwapDataProcessFull {

    static String Logs = "{\n" +
            "                \"transactionHash\": \"0x728c379f584b3342d649fb9d967b4275b6f3c7605f371d29f6ead6e3fb86efe6\",\n" +
            "                \"blockHash\": \"0x8d7e2a0e61027659a434d43a1215176b30ba25dca0289ef9eca794272f5ad29c\",\n" +
            "                \"blockNumber\": \"0x116f0a4\",\n" +
            "                \"logs\": [\n" +
            "                    {\n" +
            "                        \"transactionHash\": \"0x728c379f584b3342d649fb9d967b4275b6f3c7605f371d29f6ead6e3fb86efe6\",\n" +
            "                        \"address\": \"0xfd414e39155f91e94443a9fe97e856569d0f5eec\",\n" +
            "                        \"blockHash\": \"0x8d7e2a0e61027659a434d43a1215176b30ba25dca0289ef9eca794272f5ad29c\",\n" +
            "                        \"blockNumber\": \"0x116f0a4\",\n" +
            "                        \"data\": \"0x00000000000000000000000000000000000000000000000000005af3107a4000\",\n" +
            "                        \"logIndex\": \"0x73\",\n" +
            "                        \"removed\": false,\n" +
            "                        \"topics\": [\n" +
            "                            \"0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef\",\n" +
            "                            \"0x000000000000000000000000fd414e39155f91e94443a9fe97e856569d0f5eec\",\n" +
            "                            \"0x0000000000000000000000006f762176d94b657802ee4aebbdfe719a9067c553\"\n" +
            "                        ],\n" +
            "                        \"transactionIndex\": \"0xd\"\n" +
            "                    },\n" +
            "                    {\n" +
            "                        \"transactionHash\": \"0x728c379f584b3342d649fb9d967b4275b6f3c7605f371d29f6ead6e3fb86efe6\",\n" +
            "                        \"address\": \"0xc02aaa39b223fe8d0a0e5c4f27ead9083c756cc2\",\n" +
            "                        \"blockHash\": \"0x8d7e2a0e61027659a434d43a1215176b30ba25dca0289ef9eca794272f5ad29c\",\n" +
            "                        \"blockNumber\": \"0x116f0a4\",\n" +
            "                        \"data\": \"0x0000000000000000000000000000000000000000000000000062f7897ead21f0\",\n" +
            "                        \"logIndex\": \"0x74\",\n" +
            "                        \"removed\": false,\n" +
            "                        \"topics\": [\n" +
            "                            \"0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef\",\n" +
            "                            \"0x0000000000000000000000006f762176d94b657802ee4aebbdfe719a9067c553\",\n" +
            "                            \"0x0000000000000000000000007a250d5630b4cf539739df2c5dacb4c659f2488d\"\n" +
            "                        ],\n" +
            "                        \"transactionIndex\": \"0xd\"\n" +
            "                    },\n" +
            "                    {\n" +
            "                        \"transactionHash\": \"0x728c379f584b3342d649fb9d967b4275b6f3c7605f371d29f6ead6e3fb86efe6\",\n" +
            "                        \"address\": \"0x6f762176d94b657802ee4aebbdfe719a9067c553\",\n" +
            "                        \"blockHash\": \"0x8d7e2a0e61027659a434d43a1215176b30ba25dca0289ef9eca794272f5ad29c\",\n" +
            "                        \"blockNumber\": \"0x116f0a4\",\n" +
            "                        \"data\": \"0x000000000000000000000000000000000000000000000001bdc61c6f40c8eaef0000000000000000000000000000000000000000000000000198c98c73fdd844\",\n" +
            "                        \"logIndex\": \"0x75\",\n" +
            "                        \"removed\": false,\n" +
            "                        \"topics\": [\n" +
            "                            \"0x1c411e9a96e071241c2f21f7726b17ae89e3cab4c78be50e062b03a9fffbbad1\"\n" +
            "                        ],\n" +
            "                        \"transactionIndex\": \"0xd\"\n" +
            "                    },\n" +
            "                    {\n" +
            "                        \"transactionHash\": \"0x728c379f584b3342d649fb9d967b4275b6f3c7605f371d29f6ead6e3fb86efe6\",\n" +
            "                        \"address\": \"0x6f762176d94b657802ee4aebbdfe719a9067c553\",\n" +
            "                        \"blockHash\": \"0x8d7e2a0e61027659a434d43a1215176b30ba25dca0289ef9eca794272f5ad29c\",\n" +
            "                        \"blockNumber\": \"0x116f0a4\",\n" +
            "                        \"data\": \"0x000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000005af3107a40000000000000000000000000000000000000000000000000000062f7897ead21f00000000000000000000000000000000000000000000000000000000000000000\",\n" +
            "                        \"logIndex\": \"0x76\",\n" +
            "                        \"removed\": false,\n" +
            "                        \"topics\": [\n" +
            "                            \"0xd78ad95fa46c994b6551d0da85fc275fe613ce37657fb8d5e3d130840159d822\",\n" +
            "                            \"0x0000000000000000000000007a250d5630b4cf539739df2c5dacb4c659f2488d\",\n" +
            "                            \"0x0000000000000000000000007a250d5630b4cf539739df2c5dacb4c659f2488d\"\n" +
            "                        ],\n" +
            "                        \"transactionIndex\": \"0xd\"\n" +
            "                    },\n" +
            "                    {\n" +
            "                        \"transactionHash\": \"0x728c379f584b3342d649fb9d967b4275b6f3c7605f371d29f6ead6e3fb86efe6\",\n" +
            "                        \"address\": \"0xc02aaa39b223fe8d0a0e5c4f27ead9083c756cc2\",\n" +
            "                        \"blockHash\": \"0x8d7e2a0e61027659a434d43a1215176b30ba25dca0289ef9eca794272f5ad29c\",\n" +
            "                        \"blockNumber\": \"0x116f0a4\",\n" +
            "                        \"data\": \"0x0000000000000000000000000000000000000000000000000062f7897ead21f0\",\n" +
            "                        \"logIndex\": \"0x77\",\n" +
            "                        \"removed\": false,\n" +
            "                        \"topics\": [\n" +
            "                            \"0x7fcf532c15f0a6db0bd6d0e038bea71d30d808c7d98cb3bf7268a95bf5081b65\",\n" +
            "                            \"0x0000000000000000000000007a250d5630b4cf539739df2c5dacb4c659f2488d\"\n" +
            "                        ],\n" +
            "                        \"transactionIndex\": \"0xd\"\n" +
            "                    },\n" +
            "                    {\n" +
            "                        \"transactionHash\": \"0x728c379f584b3342d649fb9d967b4275b6f3c7605f371d29f6ead6e3fb86efe6\",\n" +
            "                        \"address\": \"0xfd414e39155f91e94443a9fe97e856569d0f5eec\",\n" +
            "                        \"blockHash\": \"0x8d7e2a0e61027659a434d43a1215176b30ba25dca0289ef9eca794272f5ad29c\",\n" +
            "                        \"blockNumber\": \"0x116f0a4\",\n" +
            "                        \"data\": \"0x0000000000000000000000000000000000000000000000000062f7897ead21f000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000\",\n" +
            "                        \"logIndex\": \"0x78\",\n" +
            "                        \"removed\": false,\n" +
            "                        \"topics\": [\n" +
            "                            \"0xdaf6233a382145376035341e71f8a04305f7e323d0fe3e58c908cf0a26b76df9\"\n" +
            "                        ],\n" +
            "                        \"transactionIndex\": \"0xd\"\n" +
            "                    },\n" +
            "                    {\n" +
            "                        \"transactionHash\": \"0x728c379f584b3342d649fb9d967b4275b6f3c7605f371d29f6ead6e3fb86efe6\",\n" +
            "                        \"address\": \"0xfd414e39155f91e94443a9fe97e856569d0f5eec\",\n" +
            "                        \"blockHash\": \"0x8d7e2a0e61027659a434d43a1215176b30ba25dca0289ef9eca794272f5ad29c\",\n" +
            "                        \"blockNumber\": \"0x116f0a4\",\n" +
            "                        \"data\": \"0x0000000000000000000000000000000000000000000000000010e0198eaee000\",\n" +
            "                        \"logIndex\": \"0x79\",\n" +
            "                        \"removed\": false,\n" +
            "                        \"topics\": [\n" +
            "                            \"0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef\",\n" +
            "                            \"0x00000000000000000000000088d0c82fbf8ad1c4f84d610da6aa209ea882a5c7\",\n" +
            "                            \"0x0000000000000000000000006f762176d94b657802ee4aebbdfe719a9067c553\"\n" +
            "                        ],\n" +
            "                        \"transactionIndex\": \"0xd\"\n" +
            "                    },\n" +
            "                    {\n" +
            "                        \"transactionHash\": \"0x728c379f584b3342d649fb9d967b4275b6f3c7605f371d29f6ead6e3fb86efe6\",\n" +
            "                        \"address\": \"0xc02aaa39b223fe8d0a0e5c4f27ead9083c756cc2\",\n" +
            "                        \"blockHash\": \"0x8d7e2a0e61027659a434d43a1215176b30ba25dca0289ef9eca794272f5ad29c\",\n" +
            "                        \"blockNumber\": \"0x116f0a4\",\n" +
            "                        \"data\": \"0x000000000000000000000000000000000000000000000000119f2ca8ad0c180b\",\n" +
            "                        \"logIndex\": \"0x7a\",\n" +
            "                        \"removed\": false,\n" +
            "                        \"topics\": [\n" +
            "                            \"0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef\",\n" +
            "                            \"0x0000000000000000000000006f762176d94b657802ee4aebbdfe719a9067c553\",\n" +
            "                            \"0x0000000000000000000000003fc91a3afd70395cd496c647d5a6cc9d4b2b7fad\"\n" +
            "                        ],\n" +
            "                        \"transactionIndex\": \"0xd\"\n" +
            "                    },\n" +
            "                    {\n" +
            "                        \"transactionHash\": \"0x728c379f584b3342d649fb9d967b4275b6f3c7605f371d29f6ead6e3fb86efe6\",\n" +
            "                        \"address\": \"0x6f762176d94b657802ee4aebbdfe719a9067c553\",\n" +
            "                        \"blockHash\": \"0x8d7e2a0e61027659a434d43a1215176b30ba25dca0289ef9eca794272f5ad29c\",\n" +
            "                        \"blockNumber\": \"0x116f0a4\",\n" +
            "                        \"data\": \"0x000000000000000000000000000000000000000000000001ac26efc693bcd2e400000000000000000000000000000000000000000000000001a9a9a602acb844\",\n" +
            "                        \"logIndex\": \"0x7b\",\n" +
            "                        \"removed\": false,\n" +
            "                        \"topics\": [\n" +
            "                            \"0x1c411e9a96e071241c2f21f7726b17ae89e3cab4c78be50e062b03a9fffbbad1\"\n" +
            "                        ],\n" +
            "                        \"transactionIndex\": \"0xd\"\n" +
            "                    },\n" +
            "                    {\n" +
            "                        \"transactionHash\": \"0x728c379f584b3342d649fb9d967b4275b6f3c7605f371d29f6ead6e3fb86efe6\",\n" +
            "                        \"address\": \"0x6f762176d94b657802ee4aebbdfe719a9067c553\",\n" +
            "                        \"blockHash\": \"0x8d7e2a0e61027659a434d43a1215176b30ba25dca0289ef9eca794272f5ad29c\",\n" +
            "                        \"blockNumber\": \"0x116f0a4\",\n" +
            "                        \"data\": \"0x00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000010e0198eaee000000000000000000000000000000000000000000000000000119f2ca8ad0c180b0000000000000000000000000000000000000000000000000000000000000000\",\n" +
            "                        \"logIndex\": \"0x7c\",\n" +
            "                        \"removed\": false,\n" +
            "                        \"topics\": [\n" +
            "                            \"0xd78ad95fa46c994b6551d0da85fc275fe613ce37657fb8d5e3d130840159d822\",\n" +
            "                            \"0x0000000000000000000000003fc91a3afd70395cd496c647d5a6cc9d4b2b7fad\",\n" +
            "                            \"0x0000000000000000000000003fc91a3afd70395cd496c647d5a6cc9d4b2b7fad\"\n" +
            "                        ],\n" +
            "                        \"transactionIndex\": \"0xd\"\n" +
            "                    },\n" +
            "                    {\n" +
            "                        \"transactionHash\": \"0x728c379f584b3342d649fb9d967b4275b6f3c7605f371d29f6ead6e3fb86efe6\",\n" +
            "                        \"address\": \"0xa0b86991c6218b36c1d19d4a2e9eb0ce3606eb48\",\n" +
            "                        \"blockHash\": \"0x8d7e2a0e61027659a434d43a1215176b30ba25dca0289ef9eca794272f5ad29c\",\n" +
            "                        \"blockNumber\": \"0x116f0a4\",\n" +
            "                        \"data\": \"0x000000000000000000000000000000000000000000000000000000007ca315d0\",\n" +
            "                        \"logIndex\": \"0x7d\",\n" +
            "                        \"removed\": false,\n" +
            "                        \"topics\": [\n" +
            "                            \"0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef\",\n" +
            "                            \"0x00000000000000000000000088e6a0c2ddd26feeb64f039a2c41296fcb3f5640\",\n" +
            "                            \"0x00000000000000000000000088d0c82fbf8ad1c4f84d610da6aa209ea882a5c7\"\n" +
            "                        ],\n" +
            "                        \"transactionIndex\": \"0xd\"\n" +
            "                    },\n" +
            "                    {\n" +
            "                        \"transactionHash\": \"0x728c379f584b3342d649fb9d967b4275b6f3c7605f371d29f6ead6e3fb86efe6\",\n" +
            "                        \"address\": \"0xc02aaa39b223fe8d0a0e5c4f27ead9083c756cc2\",\n" +
            "                        \"blockHash\": \"0x8d7e2a0e61027659a434d43a1215176b30ba25dca0289ef9eca794272f5ad29c\",\n" +
            "                        \"blockNumber\": \"0x116f0a4\",\n" +
            "                        \"data\": \"0x000000000000000000000000000000000000000000000000119f2ca8ad0c180b\",\n" +
            "                        \"logIndex\": \"0x7e\",\n" +
            "                        \"removed\": false,\n" +
            "                        \"topics\": [\n" +
            "                            \"0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef\",\n" +
            "                            \"0x0000000000000000000000003fc91a3afd70395cd496c647d5a6cc9d4b2b7fad\",\n" +
            "                            \"0x00000000000000000000000088e6a0c2ddd26feeb64f039a2c41296fcb3f5640\"\n" +
            "                        ],\n" +
            "                        \"transactionIndex\": \"0xd\"\n" +
            "                    },\n" +
            "                    {\n" +
            "                        \"transactionHash\": \"0x728c379f584b3342d649fb9d967b4275b6f3c7605f371d29f6ead6e3fb86efe6\",\n" +
            "                        \"address\": \"0x88e6a0c2ddd26feeb64f039a2c41296fcb3f5640\",\n" +
            "                        \"blockHash\": \"0x8d7e2a0e61027659a434d43a1215176b30ba25dca0289ef9eca794272f5ad29c\",\n" +
            "                        \"blockNumber\": \"0x116f0a4\",\n" +
            "                        \"data\": \"0xffffffffffffffffffffffffffffffffffffffffffffffffffffffff835cea30000000000000000000000000000000000000000000000000119f2ca8ad0c180b000000000000000000000000000000000000603c2969aa3fc816e2d4cf93ddd10000000000000000000000000000000000000000000000014897748a64fe51d20000000000000000000000000000000000000000000000000000000000031609\",\n" +
            "                        \"logIndex\": \"0x7f\",\n" +
            "                        \"removed\": false,\n" +
            "                        \"topics\": [\n" +
            "                            \"0xc42079f94a6350d7e6235f29174924f928cc2ac818eb64fed8004e115fbcca67\",\n" +
            "                            \"0x0000000000000000000000003fc91a3afd70395cd496c647d5a6cc9d4b2b7fad\",\n" +
            "                            \"0x00000000000000000000000088d0c82fbf8ad1c4f84d610da6aa209ea882a5c7\"\n" +
            "                        ],\n" +
            "                        \"transactionIndex\": \"0xd\"\n" +
            "                    }\n" +
            "                ],\n" +
            "                \"contractAddress\": null,\n" +
            "                \"effectiveGasPrice\": \"0x29aff5781\",\n" +
            "                \"cumulativeGasUsed\": \"0x24ca38\",\n" +
            "                \"from\": \"0x88d0c82fbf8ad1c4f84d610da6aa209ea882a5c7\",\n" +
            "                \"gasUsed\": \"0x4edd7\",\n" +
            "                \"logsBloom\": \"0x00200000010000000000000080000480000000400000000008010000040000000000000800000000000008000000000002000400080020000000000000000000000000080000000808000008020000200000000008400100000000000020000000004000000000000000000000000000000000000000040000000010200800000000800000000000004000000000000000000000010000080000004000100000000000000000200000000000800000000000000000000000002000000008000000000002000000000000000000000000000000000000003000000002000020000000200000000000200010000000000000001000000000000000000000010000\",\n" +
            "                \"status\": \"0x1\",\n" +
            "                \"to\": \"0x3fc91a3afd70395cd496c647d5a6cc9d4b2b7fad\",\n" +
            "                \"transactionIndex\": \"0xd\",\n" +
            "                \"type\": \"0x2\"\n" +
            "            }";
    static String UniswapV3LOG = "{\n" +
            "        \"blockHash\": \"0x96abbd3903588b5503a711b10eb8fedd467732a11e0f011ee5449fb1ca2aa657\",\n" +
            "        \"blockNumber\": \"0x11c5968\",\n" +
            "        \"contractAddress\": null,\n" +
            "        \"cumulativeGasUsed\": \"0x425cd\",\n" +
            "        \"effectiveGasPrice\": \"0x878a9de6f\",\n" +
            "        \"from\": \"0x6af976a249d898bb8c8497248631464aa495be26\",\n" +
            "        \"gasUsed\": \"0x425cd\",\n" +
            "        \"logs\": [\n" +
            "            {\n" +
            "                \"address\": \"0x4b788886614bfc04de489f159da3e85e23fb7d57\",\n" +
            "                \"blockHash\": \"0x96abbd3903588b5503a711b10eb8fedd467732a11e0f011ee5449fb1ca2aa657\",\n" +
            "                \"blockNumber\": \"0x11c5968\",\n" +
            "                \"data\": \"0x0000000000000000000000000000000000000000000000000000000000000020000000000000000000000000000000000000000000000000000000000000006434954a99000000000000000000000000cfa1bafc6626b36a6930f2f9059ad115850faa75000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000002c236336990000000000000000000000000000000000000000000000000000000000\",\n" +
            "                \"logIndex\": \"0x0\",\n" +
            "                \"removed\": false,\n" +
            "                \"topics\": [\n" +
            "                    \"0x7d2476ab50663f025cff0be85655bcf355f62768615c0c478f3cd5293f807365\",\n" +
            "                    \"0x000000000000000000000000f956e02c306893793522f86417e7c897cfcf9b7a\",\n" +
            "                    \"0x000000000000000000000000cfa1bafc6626b36a6930f2f9059ad115850faa75\",\n" +
            "                    \"0x00000000000000000000000000000000000000000000000000002c2363369900\"\n" +
            "                ],\n" +
            "                \"transactionHash\": \"0x11a7429058631d4c582d9db9a2c53f6a1425886c0120b87a14b5a1e1f4cba5e5\",\n" +
            "                \"transactionIndex\": \"0x0\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"address\": \"0x38e382f74dfb84608f3c1f10187f6bef5951de93\",\n" +
            "                \"blockHash\": \"0x96abbd3903588b5503a711b10eb8fedd467732a11e0f011ee5449fb1ca2aa657\",\n" +
            "                \"blockNumber\": \"0x11c5968\",\n" +
            "                \"data\": \"0x00000000000000000000000000000000000000000000001b0c388321a30742cb\",\n" +
            "                \"logIndex\": \"0x1\",\n" +
            "                \"removed\": false,\n" +
            "                \"topics\": [\n" +
            "                    \"0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef\",\n" +
            "                    \"0x000000000000000000000000844eb5c280f38c7462316aad3f338ef9bda62668\",\n" +
            "                    \"0x0000000000000000000000004b788886614bfc04de489f159da3e85e23fb7d57\"\n" +
            "                ],\n" +
            "                \"transactionHash\": \"0x11a7429058631d4c582d9db9a2c53f6a1425886c0120b87a14b5a1e1f4cba5e5\",\n" +
            "                \"transactionIndex\": \"0x0\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"address\": \"0xc02aaa39b223fe8d0a0e5c4f27ead9083c756cc2\",\n" +
            "                \"blockHash\": \"0x96abbd3903588b5503a711b10eb8fedd467732a11e0f011ee5449fb1ca2aa657\",\n" +
            "                \"blockNumber\": \"0x11c5968\",\n" +
            "                \"data\": \"0x00000000000000000000000000000000000000000000000000113dd2c153c400\",\n" +
            "                \"logIndex\": \"0x2\",\n" +
            "                \"removed\": false,\n" +
            "                \"topics\": [\n" +
            "                    \"0xe1fffcc4923d04b559f4d29a8bfc6cda04eb5b0d3c460751c2402c5c5cc9109c\",\n" +
            "                    \"0x000000000000000000000000e592427a0aece92de3edee1f18e0157c05861564\"\n" +
            "                ],\n" +
            "                \"transactionHash\": \"0x11a7429058631d4c582d9db9a2c53f6a1425886c0120b87a14b5a1e1f4cba5e5\",\n" +
            "                \"transactionIndex\": \"0x0\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"address\": \"0xc02aaa39b223fe8d0a0e5c4f27ead9083c756cc2\",\n" +
            "                \"blockHash\": \"0x96abbd3903588b5503a711b10eb8fedd467732a11e0f011ee5449fb1ca2aa657\",\n" +
            "                \"blockNumber\": \"0x11c5968\",\n" +
            "                \"data\": \"0x00000000000000000000000000000000000000000000000000113dd2c153c400\",\n" +
            "                \"logIndex\": \"0x3\",\n" +
            "                \"removed\": false,\n" +
            "                \"topics\": [\n" +
            "                    \"0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef\",\n" +
            "                    \"0x000000000000000000000000e592427a0aece92de3edee1f18e0157c05861564\",\n" +
            "                    \"0x000000000000000000000000844eb5c280f38c7462316aad3f338ef9bda62668\"\n" +
            "                ],\n" +
            "                \"transactionHash\": \"0x11a7429058631d4c582d9db9a2c53f6a1425886c0120b87a14b5a1e1f4cba5e5\",\n" +
            "                \"transactionIndex\": \"0x0\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"address\": \"0x844eb5c280f38c7462316aad3f338ef9bda62668\",\n" +
            "                \"blockHash\": \"0x96abbd3903588b5503a711b10eb8fedd467732a11e0f011ee5449fb1ca2aa657\",\n" +
            "                \"blockNumber\": \"0x11c5968\",\n" +
            "                \"data\": \"0xffffffffffffffffffffffffffffffffffffffffffffffe4f3c77cde5cf8bd3500000000000000000000000000000000000000000000000000113dd2c153c400000000000000000000000000000000000000000000cb5e09f65791b7f42d51b8000000000000000000000000000000000000000000002bbe97f022740664a3d2fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffe3cc7\",\n" +
            "                \"logIndex\": \"0x4\",\n" +
            "                \"removed\": false,\n" +
            "                \"topics\": [\n" +
            "                    \"0xc42079f94a6350d7e6235f29174924f928cc2ac818eb64fed8004e115fbcca67\",\n" +
            "                    \"0x000000000000000000000000e592427a0aece92de3edee1f18e0157c05861564\",\n" +
            "                    \"0x0000000000000000000000004b788886614bfc04de489f159da3e85e23fb7d57\"\n" +
            "                ],\n" +
            "                \"transactionHash\": \"0x11a7429058631d4c582d9db9a2c53f6a1425886c0120b87a14b5a1e1f4cba5e5\",\n" +
            "                \"transactionIndex\": \"0x0\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"address\": \"0x4b788886614bfc04de489f159da3e85e23fb7d57\",\n" +
            "                \"blockHash\": \"0x96abbd3903588b5503a711b10eb8fedd467732a11e0f011ee5449fb1ca2aa657\",\n" +
            "                \"blockNumber\": \"0x11c5968\",\n" +
            "                \"data\": \"0x00000000000000000000000000000000000000000000000000000000000000200000000000000000000000000000000000000000000000000000000000000124c04b8d59000000000000000000000000000000000000000000000000000000000000002000000000000000000000000000000000000000000000000000000000000000a00000000000000000000000004b788886614bfc04de489f159da3e85e23fb7d5700000000000000000000000000000000000000000000000000000000655f694300000000000000000000000000000000000000000000000000113dd2c153c40000000000000000000000000000000000000000000000001ac6fa81d1f0c2117c000000000000000000000000000000000000000000000000000000000000002bc02aaa39b223fe8d0a0e5c4f27ead9083c756cc200271038e382f74dfb84608f3c1f10187f6bef5951de9300000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000\",\n" +
            "                \"logIndex\": \"0x5\",\n" +
            "                \"removed\": false,\n" +
            "                \"topics\": [\n" +
            "                    \"0x7d2476ab50663f025cff0be85655bcf355f62768615c0c478f3cd5293f807365\",\n" +
            "                    \"0x000000000000000000000000f956e02c306893793522f86417e7c897cfcf9b7a\",\n" +
            "                    \"0x000000000000000000000000e592427a0aece92de3edee1f18e0157c05861564\",\n" +
            "                    \"0x00000000000000000000000000000000000000000000000000113dd2c153c400\"\n" +
            "                ],\n" +
            "                \"transactionHash\": \"0x11a7429058631d4c582d9db9a2c53f6a1425886c0120b87a14b5a1e1f4cba5e5\",\n" +
            "                \"transactionIndex\": \"0x0\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"address\": \"0x4b788886614bfc04de489f159da3e85e23fb7d57\",\n" +
            "                \"blockHash\": \"0x96abbd3903588b5503a711b10eb8fedd467732a11e0f011ee5449fb1ca2aa657\",\n" +
            "                \"blockNumber\": \"0x11c5968\",\n" +
            "                \"data\": \"0x00000000000000000000000000000000000000000000000000000000000000200000000000000000000000000000000000000000000000000000000000000000\",\n" +
            "                \"logIndex\": \"0x6\",\n" +
            "                \"removed\": false,\n" +
            "                \"topics\": [\n" +
            "                    \"0x7d2476ab50663f025cff0be85655bcf355f62768615c0c478f3cd5293f807365\",\n" +
            "                    \"0x000000000000000000000000f956e02c306893793522f86417e7c897cfcf9b7a\",\n" +
            "                    \"0x0000000000000000000000006af976a249d898bb8c8497248631464aa495be26\",\n" +
            "                    \"0x0000000000000000000000000000000000000000000000000025f866d340626e\"\n" +
            "                ],\n" +
            "                \"transactionHash\": \"0x11a7429058631d4c582d9db9a2c53f6a1425886c0120b87a14b5a1e1f4cba5e5\",\n" +
            "                \"transactionIndex\": \"0x0\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"address\": \"0xf956e02c306893793522f86417e7c897cfcf9b7a\",\n" +
            "                \"blockHash\": \"0x96abbd3903588b5503a711b10eb8fedd467732a11e0f011ee5449fb1ca2aa657\",\n" +
            "                \"blockNumber\": \"0x11c5968\",\n" +
            "                \"data\": \"0x00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000025f866d340626e\",\n" +
            "                \"logIndex\": \"0x7\",\n" +
            "                \"removed\": false,\n" +
            "                \"topics\": [\n" +
            "                    \"0x22edd2bbb0b0afbdcf90d91da8a5e2100f8d8f67cdc766dee1742e9a36d6add3\",\n" +
            "                    \"0x0000000000000000000000004b788886614bfc04de489f159da3e85e23fb7d57\",\n" +
            "                    \"0x0000000000000000000000006af976a249d898bb8c8497248631464aa495be26\"\n" +
            "                ],\n" +
            "                \"transactionHash\": \"0x11a7429058631d4c582d9db9a2c53f6a1425886c0120b87a14b5a1e1f4cba5e5\",\n" +
            "                \"transactionIndex\": \"0x0\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"address\": \"0xf956e02c306893793522f86417e7c897cfcf9b7a\",\n" +
            "                \"blockHash\": \"0x96abbd3903588b5503a711b10eb8fedd467732a11e0f011ee5449fb1ca2aa657\",\n" +
            "                \"blockNumber\": \"0x11c5968\",\n" +
            "                \"data\": \"0x0000000000000000000000000000000000000000000000000000000000000040c64593b37dda7e68910eb0bcc09055b6679f791ddb8a41af2262e4603bdb55c800000000000000000000000000000000000000000000000000000000000000e000000000000000000000000000000000000000000000000000000000000000200000000000000000000000000000000000000000000000000000000000000002000000000000000000000000000000000000000000000000000000000000004000000000000000000000000000000000000000000000000000000000000000600000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000002000000000000000000000000000000000000000000000001b0c388321a30742cb\",\n" +
            "                \"logIndex\": \"0x8\",\n" +
            "                \"removed\": false,\n" +
            "                \"topics\": [\n" +
            "                    \"0x7da4525a280527268ba2e963ee6c1b18f43c9507bcb1d2560f652ab17c76e90a\",\n" +
            "                    \"0x0000000000000000000000004b788886614bfc04de489f159da3e85e23fb7d57\",\n" +
            "                    \"0x0000000000000000000000000000000000000000000000000000000000000001\"\n" +
            "                ],\n" +
            "                \"transactionHash\": \"0x11a7429058631d4c582d9db9a2c53f6a1425886c0120b87a14b5a1e1f4cba5e5\",\n" +
            "                \"transactionIndex\": \"0x0\"\n" +
            "            }\n" +
            "        ],\n" +
            "        \"logsBloom\": \"0x0004000200000000000000008000200000000000000000000000000000000000400000000000000500000808000000000200000008002040040000000004000000004000001000080000002800000000000004000004080000000040820000000000000000100000000010000000800000000002000000400000001000080000000000000000000010800000100000010000000100000000000000000a800000000000000000000000000000800000000000004000000000000000000000000000000082000000000010000000400000400000000000000000c02240000040008000200000000000000004000004008000000002010000400000200000000000\",\n" +
            "        \"status\": \"0x1\",\n" +
            "        \"to\": \"0xf956e02c306893793522f86417e7c897cfcf9b7a\",\n" +
            "        \"transactionHash\": \"0x11a7429058631d4c582d9db9a2c53f6a1425886c0120b87a14b5a1e1f4cba5e5\",\n" +
            "        \"transactionIndex\": \"0x0\",\n" +
            "        \"type\": \"0x2\"\n" +
            "    }";


    static String UniswapV2LOG = "{\n" +
            "                \"transactionHash\": \"0x567f696f8d0ea7ee7cb0136c28092cfae31e036c059768adfdf640cd2892403a\",\n" +
            "                \"blockHash\": \"0x68c1afd1897e6f44620ab65c11ed18186d930f7f28d858065e19a67a211468e2\",\n" +
            "                \"blockNumber\": \"0x11a08c5\",\n" +
            "                \"logs\": [\n" +
            "                    {\n" +
            "                        \"transactionHash\": \"0x567f696f8d0ea7ee7cb0136c28092cfae31e036c059768adfdf640cd2892403a\",\n" +
            "                        \"address\": \"0xc02aaa39b223fe8d0a0e5c4f27ead9083c756cc2\",\n" +
            "                        \"blockHash\": \"0x68c1afd1897e6f44620ab65c11ed18186d930f7f28d858065e19a67a211468e2\",\n" +
            "                        \"blockNumber\": \"0x11a08c5\",\n" +
            "                        \"data\": \"0x00000000000000000000000000000000000000000000000004dd169800000000\",\n" +
            "                        \"logIndex\": \"0x87\",\n" +
            "                        \"removed\": false,\n" +
            "                        \"topics\": [\n" +
            "                            \"0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef\",\n" +
            "                            \"0x000000000000000000000000608cAd7819Fd05afBDFDACeB022e62b323F99a09\",\n" +
            "                            \"0x0000000000000000000000006b75d8AF000000e20B7a7DDf000Ba900b4009A80\"\n" +
            "                        ],\n" +
            "                        \"transactionIndex\": \"0x24\"\n" +
            "                    },\n" +
            "                    {\n" +
            "                        \"transactionHash\": \"0x567f696f8d0ea7ee7cb0136c28092cfae31e036c059768adfdf640cd2892403a\",\n" +
            "                        \"address\": \"0x40f74e87e03dce6480f123596c27e587af71d327\",\n" +
            "                        \"blockHash\": \"0x68c1afd1897e6f44620ab65c11ed18186d930f7f28d858065e19a67a211468e2\",\n" +
            "                        \"blockNumber\": \"0x11a08c5\",\n" +
            "                        \"data\": \"0x00000000000000000000000000000000000000000000000000000b152cd90000\",\n" +
            "                        \"logIndex\": \"0x88\",\n" +
            "                        \"removed\": false,\n" +
            "                        \"topics\": [\n" +
            "                            \"0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef\",\n" +
            "                            \"0x0000000000000000000000006b75d8AF000000e20B7a7DDf000Ba900b4009A80\",\n" +
            "                            \"0x000000000000000000000000608cAd7819Fd05afBDFDACeB022e62b323F99a09\"\n" +
            "                        ],\n" +
            "                        \"transactionIndex\": \"0x24\"\n" +
            "                    },\n" +
            "                    {\n" +
            "                        \"transactionHash\": \"0x567f696f8d0ea7ee7cb0136c28092cfae31e036c059768adfdf640cd2892403a\",\n" +
            "                        \"address\": \"0xcbe856765eeec3fdc505ddebf9dc612da995e593\",\n" +
            "                        \"blockHash\": \"0x68c1afd1897e6f44620ab65c11ed18186d930f7f28d858065e19a67a211468e2\",\n" +
            "                        \"blockNumber\": \"0x11a08c5\",\n" +
            "                        \"data\": \"0x00000000000000000000000000000000000000c8d5d34693a8be72d19d45f266000000000000000000000000000000000000000000000012b4c77e734f42d1ee\",\n" +
            "                        \"logIndex\": \"0x89\",\n" +
            "                        \"removed\": false,\n" +
            "                        \"topics\": [\n" +
            "                            \"0x1c411e9a96e071241c2f21f7726b17ae89e3cab4c78be50e062b03a9fffbbad1\"\n" +
            "                        ],\n" +
            "                        \"transactionIndex\": \"0x24\"\n" +
            "                    },\n" +
            "                    {\n" +
            "                        \"transactionHash\": \"0x567f696f8d0ea7ee7cb0136c28092cfae31e036c059768adfdf640cd2892403a\",\n" +
            "                        \"address\": \"0x608cad7819fd05afbdfdaceb022e62b323f99a09\",\n" +
            "                        \"blockHash\": \"0x68c1afd1897e6f44620ab65c11ed18186d930f7f28d858065e19a67a211468e2\",\n" +
            "                        \"blockNumber\": \"0x11a08c5\",\n" +
            "                        \"data\": \"0x00000000000000000000000000000000000000000000000000000b152cd900000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000004dd169800000000\",\n" +
            "                        \"logIndex\": \"0x8a\",\n" +
            "                        \"removed\": false,\n" +
            "                        \"topics\": [\n" +
            "                            \"0xd78ad95fa46c994b6551d0da85fc275fe613ce37657fb8d5e3d130840159d822\",\n" +
            "                            \"0x0000000000000000000000006b75d8AF000000e20B7a7DDf000Ba900b4009A80\",\n" +
            "                            \"0x0000000000000000000000006b75d8AF000000e20B7a7DDf000Ba900b4009A80\"\n" +
            "                        ],\n" +
            "                        \"transactionIndex\": \"0x24\"\n" +
            "                    }\n" +
            "                ],\n" +
            "                \"contractAddress\": null,\n" +
            "                \"effectiveGasPrice\": \"0x52d4e6340\",\n" +
            "                \"cumulativeGasUsed\": \"0x5cd998\",\n" +
            "                \"from\": \"0x29bbc2b5aff41a2143f7d28fe6944453178f1473\",\n" +
            "                \"gasUsed\": \"0x1a7c0\",\n" +
            "                \"logsBloom\": \"0x00200000000000000000000c80000000000000000000000000010000000000040000000000000020000000000000000002000000080000000800000010000000000000000000020000000008000000200000000000000000000000000000000000000000000000000000000000000000000000000000000000000010000000000000000000000000104000000000000000000000000000080000004040000000000000000000000000000000000000000000000000000000000000000000000000000002000000000000000000008000000000000000001000002000000020004000200000000000000000000000000000000000000000000000000000000000\",\n" +
            "                \"status\": \"0x1\",\n" +
            "                \"to\": \"0x7a250d5630b4cf539739df2c5dacb4c659f2488d\",\n" +
            "                \"transactionIndex\": \"0x24\",\n" +
            "                \"type\": \"0x0\"\n" +
            "            }";

    public static List<Map<String, String>> parseFullUniswap(String log) throws IOException {
        List<Map<String, String>> lists = new ArrayList<>();
        List<UniswapEvent> uniswapEvents = parseAllUniSwapLogs(log);
        uniswapEvents.forEach(t -> {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("caller", t.getSender());
                    map.put("methodId", "others");
                    map.put("to", t.getTo());
                    map.put("amountIn", t.getAmountIn().toString());
                    map.put("amountOut", t.getAmountOut().toString());
                    map.put("tokenIn", t.getTokenIn());
                    map.put("tokenOut", t.getTokenOut());
                    map.put("protocol", "pancake");
                    map.put("chain", "56");
                    if ("0xbb4cdb9cbd36b01bd1cbaebf2de08d9173bc095c".toLowerCase().equals(t.getTokenIn())) {
                        map.put("eventType", "buy");
                    } else {
                        map.put("eventType", "sell");
                    }
                    lists.add(map);
                }
        );
        return lists;
    }

    public static List<UniswapEvent> parseAllUniSwapLogs(String log) throws IOException {
        if(log.isEmpty()){
            return new ArrayList<>();
        }
        // 1、读取 单条tx 的所有 log 对象
        ArrayList<Object> lists = new ArrayList<>();
        ArrayList<UniswapEvent> eventLists = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode txLog = objectMapper.readTree(log);

        // 2、读取每个字段
        // String transactionHash = txLog.get("transactionHash").asText();
        JsonNode logLists = txLog.get("logs");
        List<TransferEvent> transferLog = new ArrayList<>();
        List<UniswapV2Event> uniswapV2Log = new ArrayList<>();
        List<UniswapV3Event> uniswapV3Log = new ArrayList<>();
        if (logLists.isArray()) {
            for (JsonNode tmp : logLists) {
                String contractAddress = tmp.get("address").asText();
                if (tmp.get("data").toString().length() <= 2) continue;
                String data = tmp.get("data").asText().substring(2);
                List<String> topicLists = parseTopics(tmp.get("topics"));

                if (topicLists.size() < 3) continue;
                if ("0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef".equalsIgnoreCase(topicLists.get(0))
                        && data.length() == 64) {
                    // Transfer
                    TransferEvent event = TransferEvent.builder()
                            .sender("0x" + topicLists.get(1).substring(26))
                            .receiver("0x" + topicLists.get(2).substring(26))
                            .amount(new BigInteger(data.substring(24), 16))
                            .contractAddress(contractAddress)
                            .build();
                    transferLog.add(event);
                } else if ("0xc42079f94a6350d7e6235f29174924f928cc2ac818eb64fed8004e115fbcca67".equalsIgnoreCase(topicLists.get(0))
                        && data.length() == 320) {
                    // uniswap v3 解析
                    BigInteger amount0 = web3HexToBigInteger(data.substring(0, 64));
                    BigInteger amount1 = web3HexToBigInteger(data.substring(64, 128));

                    UniswapV3Event uniswapV3Event = UniswapV3Event.builder()
                            .sender("0x" + topicLists.get(1).substring(26))
                            .recipient("0x" + topicLists.get(2).substring(26))
                            .amount0(amount0)
                            .amount1(amount1)
                            .protocol("pancake")
                            .version("v3")
                            .contractAddress(contractAddress)
                            .build();

                    uniswapV3Log.add(uniswapV3Event);
                } else if ("0xd78ad95fa46c994b6551d0da85fc275fe613ce37657fb8d5e3d130840159d822".equalsIgnoreCase(topicLists.get(0))
                        && data.length() == 256) {
                    // uniswap v2 解析
                    BigInteger amount0In = web3HexToBigInteger(data.substring(0, 64));
                    BigInteger amount1In = web3HexToBigInteger(data.substring(64, 128));
                    BigInteger amount0Out = web3HexToBigInteger(data.substring(128, 192));
                    BigInteger amount1Out = web3HexToBigInteger(data.substring(192));
                    amount0Out = amount0Out.signum() != -1 ? amount0Out : amount0Out.negate();
                    amount1Out = amount1Out.signum() != -1 ? amount1Out : amount1Out.negate();

                    UniswapV2Event uniswapV2Event = UniswapV2Event.builder()
                            .sender("0x" + topicLists.get(1).substring(26))
                            .to("0x" + topicLists.get(2).substring(26))
                            .amount0In(amount0In)
                            .amount1In(amount1In)
                            .protocol("pancake")
                            .version("v2")
                            .amount0Out(amount0Out)
                            .amount1Out(amount1Out)
                            .contractAddress(contractAddress)
                            .build();

                    uniswapV2Log.add(uniswapV2Event);
                }
            }
        }
//        parseShortPathUniswap(transferLog, uniswapV2Log, uniswapV3Log, lists, eventLists);
        List<TransferEvent> excludeTransferLog = new ArrayList<>();
        // System.out.println(transferLog);
        // System.out.println(uniswapV2Log);
        parseEasyUniswapV2(transferLog, excludeTransferLog, uniswapV2Log, lists, eventLists);
        parseEasyUniswapV3(transferLog, excludeTransferLog, uniswapV3Log, lists, eventLists);
//        transferLog.removeAll(excludeTransferLog);
//        transferToUniswapSell(transferLog, lists, eventLists);
        return eventLists;
    }

    private static void transferToUniswapSell(List<TransferEvent> transferLog, ArrayList<Object> lists, ArrayList<UniswapEvent> eventLists) {
        transferLog.forEach( t -> {
            // transferOut 事件构造成 Uniswap Sell 操作
            if ("0xbb4cdb9cbd36b01bd1cbaebf2de08d9173bc095c".equals(t.getContractAddress())) return;
            UniswapEvent build = UniswapEvent.builder()
                    .protocol("pancake")
                    .version("v3")
                    .amountIn(t.getAmount())
                    .amountOut(new BigInteger("0"))
                    .tokenIn(t.getContractAddress())
                    .tokenOut("0xbb4cdb9cbd36b01bd1cbaebf2de08d9173bc095c")
                    .sender(t.getSender())
                    .to(t.getSender())
                    .build();
            eventLists.add(build);
        });
    }

    private static void parseShortPathUniswap(List<TransferEvent> transferLog, List<UniswapV2Event> uniswapV2Log, List<UniswapV3Event> uniswapV3Log, ArrayList<Object> lists, ArrayList<UniswapEvent> eventLists) {
        uniswapV3Log.forEach(u -> {
            String poolAddress = u.getContractAddress();
            BigInteger amount0 = u.getAmount0();
            boolean token0IsOut = amount0.compareTo(BigInteger.ZERO) < 0;
            BigInteger amount1 = u.getAmount1();
            boolean token1IsOut = amount1.compareTo(BigInteger.ZERO) < 0;
            String uTo = u.getRecipient();
            UniswapEvent.UniswapEventBuilder builder = UniswapEvent.builder();
            for (TransferEvent transferEvent : transferLog) {
                String tokenAddress = transferEvent.getContractAddress();
                BigInteger tAmount = transferEvent.getAmount();
                String tSender = transferEvent.getSender();
                String tReceiver = transferEvent.getReceiver();
                // 1、uniswap 的 sender 地址 等于 transfer 的 sender 地址
                // 或者 uniswap 的 sender 地址 等于 transfer 的 receiver 地址
                // 2、同时对应的金额一致
                // 3、不相等的另一个地址等于 uniswap 的池地址
                if (
                        tReceiver.equalsIgnoreCase(uTo)
                                && !tSender.equals(tokenAddress)
                                && tSender.equalsIgnoreCase(poolAddress)
                                && tAmount.equals(amount0.abs())
                                && token0IsOut
                ) {
                    u.setToken0(tokenAddress);
                    builder.tokenOut(tokenAddress).amountOut(tAmount).sender(uTo).to(uTo).contractAddress(poolAddress);
                }

                if (
                        !tSender.equalsIgnoreCase(uTo)
                                && !tSender.equals(tokenAddress)
                                && tReceiver.equalsIgnoreCase(poolAddress)
                                && tAmount.equals(amount0.abs())
                                && !token0IsOut
                ) {
                    u.setToken0(tokenAddress);
                    u.setSender(tSender);
                    builder.tokenIn(tokenAddress).amountIn(tAmount).to(uTo).sender(tSender).contractAddress(poolAddress);
                }

                if (
                        tReceiver.equalsIgnoreCase(uTo)
                                && !tSender.equals(tokenAddress)
                                && tSender.equalsIgnoreCase(poolAddress)
                                && tAmount.equals(amount1.abs())
                                && token1IsOut
                ) {
                    u.setToken1(tokenAddress);
                    builder.tokenOut(tokenAddress).amountOut(tAmount).sender(uTo).to(uTo).contractAddress(poolAddress);
                }

                if (
                        !tSender.equalsIgnoreCase(uTo)
                                && !tSender.equals(tokenAddress)
                                && tReceiver.equalsIgnoreCase(poolAddress)
                                && tAmount.equals(amount1.abs())
                                && !token1IsOut
                ) {
                    u.setToken1(tokenAddress);
                    u.setSender(tSender);
                    builder.tokenIn(tokenAddress).amountIn(tAmount).to(uTo).sender(tSender).contractAddress(poolAddress);
                }

            }
            UniswapEvent build = builder.build();
            if (checkUniswapEvent(build)) {
                lists.add(JSONObject.toJSONString(u));
//                eventLists.add(build);
            }
        });

        uniswapV2Log.forEach(u -> {
            String poolAddress = u.getContractAddress();
            BigInteger amount1In = u.getAmount1In();
            BigInteger amount0In = u.getAmount0In();
            BigInteger amount0Out = u.getAmount0Out();
            BigInteger amount1Out = u.getAmount1Out();
            String uSender = u.getSender();
            String uTo = u.getTo();
            UniswapEvent.UniswapEventBuilder builder = UniswapEvent.builder();
            for (TransferEvent transferEvent : transferLog) {
                String tokenAddress = transferEvent.getContractAddress();
                BigInteger tAmount = transferEvent.getAmount();
                String tSender = transferEvent.getSender();
                String tReceiver = transferEvent.getReceiver();
                // 1、uniswap 的 sender 地址 等于 transfer 的 sender 地址
                // 或者 uniswap 的 sender 地址 等于 transfer 的 receiver 地址
                // 2、同时对应的金额一致
                // 3、不相等的另一个地址等于 uniswap 的池地址
                if (
                        tReceiver.equalsIgnoreCase(uTo)
                                && !tSender.equals(tokenAddress)
                                && tSender.equalsIgnoreCase(poolAddress)
                                && tAmount.equals(amount0Out)
                                && !BigInteger.ZERO.equals(amount0Out)
                ) {
                    u.setToken0(tokenAddress);
                    builder.tokenOut(tokenAddress).amountOut(tAmount).sender(uTo).to(uTo).contractAddress(poolAddress);
                }

                if (
                        tReceiver.equalsIgnoreCase(uTo)
                                && !tSender.equals(tokenAddress)
                                && tSender.equalsIgnoreCase(poolAddress)
                                && tAmount.equals(amount1Out)
                                && !BigInteger.ZERO.equals(amount1Out)
                ) {
                    u.setToken1(tokenAddress);
                    builder.tokenOut(tokenAddress).amountOut(tAmount).sender(uTo).to(uTo).contractAddress(poolAddress);
                }

                if (
                        !tSender.equalsIgnoreCase(uTo)
                                && !tSender.equals(tokenAddress)
                                && tReceiver.equalsIgnoreCase(poolAddress)
                                && tAmount.equals(amount1In)
                                && !BigInteger.ZERO.equals(amount1In)
                ) {
                    u.setToken1(tokenAddress);
                    u.setSender(tSender);
                    builder.tokenIn(tokenAddress).amountIn(tAmount).to(uTo).sender(tSender).contractAddress(poolAddress);
                }

                if (
                        !tSender.equalsIgnoreCase(uTo)
                                && !tSender.equals(tokenAddress)
                                && tReceiver.equalsIgnoreCase(poolAddress)
                                && tAmount.equals(amount0In)
                                && !BigInteger.ZERO.equals(amount0In)
                ) {
                    u.setToken0(tokenAddress);
                    u.setSender(tSender);
                    builder.tokenIn(tokenAddress).amountIn(tAmount).to(uTo).sender(tSender).contractAddress(poolAddress);
                }
            }
            UniswapEvent build = builder.build();
            if (checkUniswapEvent(build)) {
                lists.add(JSONObject.toJSONString(u));
//                eventLists.add(build);
            }
        });

//        eventLists.forEach(t -> {
//
//        });


    }

    private static void parseEasyUniswapV3(List<TransferEvent> transferLog, List<TransferEvent> excludeTransferLog, List<UniswapV3Event> uniswapV3Log, ArrayList<Object> lists, ArrayList<UniswapEvent> eventLists) {
        uniswapV3Log.forEach(u -> {
            String poolAddress = u.getContractAddress();
            BigInteger amount0 = u.getAmount0();
            boolean token0IsOut = amount0.compareTo(BigInteger.ZERO) < 0;
            BigInteger amount1 = u.getAmount1();
            boolean token1IsOut = amount1.compareTo(BigInteger.ZERO) < 0;
            String uTo = u.getRecipient();
            UniswapEvent.UniswapEventBuilder builder = UniswapEvent.builder();
            for (TransferEvent transferEvent : transferLog) {
                String tokenAddress = transferEvent.getContractAddress();
                BigInteger tAmount = transferEvent.getAmount();
                String tSender = transferEvent.getSender();
                String tReceiver = transferEvent.getReceiver();
                // 1、uniswap 的 sender 地址 等于 transfer 的 sender 地址
                // 或者 uniswap 的 sender 地址 等于 transfer 的 receiver 地址
                // 2、同时对应的金额一致
                // 3、不相等的另一个地址等于 uniswap 的池地址
                if (
                        tReceiver.equalsIgnoreCase(uTo)
                                && !tSender.equals(tokenAddress)
                                && tSender.equalsIgnoreCase(poolAddress)
                                && tAmount.equals(amount0.abs())
                                && token0IsOut
                ) {
                    u.setToken0(tokenAddress);
                    excludeTransferLog.add(transferEvent);
                    builder.tokenOut(tokenAddress).amountOut(tAmount).to(uTo).contractAddress(poolAddress);
                }

                if (
                        !tSender.equals(tokenAddress)
                                && tReceiver.equalsIgnoreCase(poolAddress)
                                && tAmount.equals(amount0.abs())
                                && !token0IsOut
                ) {
                    u.setToken0(tokenAddress);
                    excludeTransferLog.add(transferEvent);
                    builder.tokenIn(tokenAddress).amountIn(tAmount).sender(tSender).contractAddress(poolAddress);
                }

                if (
                        tReceiver.equalsIgnoreCase(uTo)
                                && !tSender.equals(tokenAddress)
                                && tSender.equalsIgnoreCase(poolAddress)
                                && tAmount.equals(amount1.abs())
                                && token1IsOut
                ) {
                    u.setToken1(tokenAddress);
                    excludeTransferLog.add(transferEvent);
                    builder.tokenOut(tokenAddress).amountOut(tAmount).to(uTo).contractAddress(poolAddress);
                }

                if (
                        !tSender.equals(tokenAddress)
                                && tReceiver.equalsIgnoreCase(poolAddress)
                                && tAmount.equals(amount1.abs())
                                && !token1IsOut
                ) {
                    u.setToken1(tokenAddress);
                    excludeTransferLog.add(transferEvent);
                    builder.tokenIn(tokenAddress).amountIn(tAmount).sender(tSender).contractAddress(poolAddress);
                }

            }
            UniswapEvent build = builder.build();
            if (checkUniswapEvent(build)) {
                lists.add(JSONObject.toJSONString(u));
                eventLists.add(build);
            }
        });

    }

    private static void parseEasyUniswapV2(List<TransferEvent> transferLog, List<TransferEvent> excludeTransferLog, List<UniswapV2Event> uniswapV2Log, ArrayList<Object> lists, ArrayList<UniswapEvent> eventLists) {
        uniswapV2Log.forEach(u -> {
            String poolAddress = u.getContractAddress();
            BigInteger amount1In = u.getAmount1In();
            BigInteger amount0In = u.getAmount0In();
            BigInteger amount0Out = u.getAmount0Out();
            BigInteger amount1Out = u.getAmount1Out();
            String uSender = u.getSender();
            String uTo = u.getTo();
            UniswapEvent.UniswapEventBuilder builder = UniswapEvent.builder();
            for (TransferEvent transferEvent : transferLog) {
                String tokenAddress = transferEvent.getContractAddress();
                BigInteger tAmount = transferEvent.getAmount();
                String tSender = transferEvent.getSender();
                String tReceiver = transferEvent.getReceiver();
                // 1、uniswap 的 sender 地址 等于 transfer 的 sender 地址
                // 或者 uniswap 的 sender 地址 等于 transfer 的 receiver 地址
                // 2、同时对应的金额一致
                // 3、不相等的另一个地址等于 uniswap 的池地址
                // System.out.println("poolAddress = " + poolAddress + ", tReceiver = " + tReceiver + ", uTo = " + uTo + ", tSender = " + tSender + ", poolAddress = " + poolAddress + ", amount0Out = " + amount0Out + ", tAmount = " + tAmount + ", tokenAddress = " + tokenAddress);
                if (
                        tReceiver.equalsIgnoreCase(uTo)
                                && tSender.equalsIgnoreCase(poolAddress)
                                && tAmount.equals(amount0Out)
                                && !BigInteger.ZERO.equals(amount0Out)
                ) {
                    u.setToken1(tokenAddress);
                    excludeTransferLog.add(transferEvent);
                    builder.tokenOut(tokenAddress).amountOut(tAmount).to(uTo).contractAddress(poolAddress);
                }

                if (
                        tReceiver.equalsIgnoreCase(uTo)
                                && tSender.equalsIgnoreCase(poolAddress)
                                && tAmount.equals(amount1Out)
                                && !BigInteger.ZERO.equals(amount1Out)
                ) {
                    u.setToken1(tokenAddress);
                    excludeTransferLog.add(transferEvent);
                    builder.tokenOut(tokenAddress).amountOut(tAmount).to(uTo).contractAddress(poolAddress);
                }

                // System.out.println("poolAddress = " + poolAddress + ", tReceiver = " + tReceiver + ", uTo = " + uTo + ", tSender = " + tSender + ", poolAddress = " + poolAddress + ", amount1In = " + amount1In + ", tAmount = " + tAmount + ", tokenAddress = " + tokenAddress);
                if (
                        tReceiver.equalsIgnoreCase(poolAddress)
                                && tAmount.equals(amount1In)
                                && !BigInteger.ZERO.equals(amount1In)
                ) {
                    u.setToken1(tokenAddress);
                    excludeTransferLog.add(transferEvent);
                    builder.tokenIn(tokenAddress).amountIn(tAmount).sender(tSender).contractAddress(poolAddress);
                }

                if (
                        tReceiver.equalsIgnoreCase(poolAddress)
                                && tAmount.equals(amount0In)
                                && !BigInteger.ZERO.equals(amount0In)
                ) {
                    u.setToken0(tokenAddress);
                    excludeTransferLog.add(transferEvent);
                    builder.tokenIn(tokenAddress).amountIn(tAmount).sender(tSender).contractAddress(poolAddress);
                }
            }
            UniswapEvent build = builder.build();
            if (checkUniswapEvent(build)) {
                lists.add(JSONObject.toJSONString(u));
                eventLists.add(build);
                //System.out.println("-----");
                //System.out.println(build);
                //System.out.println("-----");
            }
        });

    }

    public static BigInteger web3HexToBigInteger(String web3Hex) {
        // Remove '0x' prefix if present
        if (web3Hex.startsWith("0x")) {
            web3Hex = web3Hex.substring(2);
        }

        // Convert hex string to BigInteger
        BigInteger bigInteger = new BigInteger(web3Hex, 16);

        // Check if the highest bit is set (indicating a negative number)
        if (web3Hex.length() > 0 && web3Hex.charAt(0) >= '8') {
            // Apply two's complement to get the correct negative value
            bigInteger = bigInteger.subtract(BigInteger.ONE.shiftLeft(web3Hex.length() * 4));
        }

        return bigInteger;
    }

    private static boolean checkUniswapEvent(UniswapEvent build) {
        return build.getTokenIn() != null && build.getTokenOut() != null;
    }

    public static List<String> parseTopics(JsonNode topics) {
        List<String> topicLists = new ArrayList<>();
        topics.forEach(t -> topicLists.add(t.asText()));
        return topicLists;
    }

    public static void main(String[] args) throws IOException {
        List<UniswapEvent> uniswapV2Lists = parseAllUniSwapLogs(UniswapV2LOG);
//        List<UniswapEvent> uniswapV3Lists = parseAllUniSwapLogs(UniswapV3LOG);
        ;
//        List<UniswapEvent> uniswapLists = parseAllUniSwapLogs(Logs);

//        System.out.println(uniswapV2Lists);
//        System.out.println(uniswapV3Lists);
//        System.out.println(availableTx);

//        System.out.println(new BigInteger("ffffffffffffffffffffffffffffffffffffffffffffffffda982b7a425204cb",16).toString(10));
//        System.out.println(new BigInteger("ffffffffffffffffffffffffffffffffffffffffffffffffda982b7a425204cb",16).longValue());
//        System.out.println(new BigInteger("ffffffffffffffffffffffffffffffffffffffffffffffffda982b7a425204cb",16).signum());
//        System.out.println("0x29bbc2b5aff41a2143f7d28fe6944453178f1473".length());
//        ArrayList<UniswapEvent> list = new ArrayList<>();
//        list.addAll(uniswapV2Lists);
//        list.addAll(uniswapV3Lists);
//        list.addAll(uniswapLists);

//        Map<String, UniswapEvent> eventList = new HashMap<>();
//        for (UniswapEvent uniswapEvent : list) {
////            String sendKey = uniswapEvent.getSender()+ uniswapEvent.getToken0();
////            if (eventList.containsKey(sendKey)) {
////                UniswapEvent temp = eventList.get(sendKey);
////                temp.
////            System.out.println("-----");
//            System.out.println(uniswapEvent);
//        }
//
//        ArrayList<Integer> sendIndex = new ArrayList<>();
//        for (int i = 0; i < list.size(); i++) {
//
//        }

    }


}

