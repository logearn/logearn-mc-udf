package cn.xlystar.constants;

public class FeiShuMessageTemplate {
    public static String checkdataFail = "{\n" +
            "  \"config\": {\n" +
            "    \"wide_screen_mode\": true\n" +
            "  },\n" +
            "  \"header\": {\n" +
            "    \"template\": \"red\",\n" +
            "    \"title\": {\n" +
            "      \"content\": \"【应急通知】7月26日江浙沪地区居家办公通知\",\n" +
            "      \"tag\": \"plain_text\"\n" +
            "    }\n" +
            "  },\n" +
            "  \"elements\": [\n" +
            "    {\n" +
            "      \"fields\": [\n" +
            "        {\n" +
            "          \"is_short\": true,\n" +
            "          \"text\": {\n" +
            "            \"content\": \"**时间**\\n2021-07-25 15:35:00\",\n" +
            "            \"tag\": \"lark_md\"\n" +
            "          }\n" +
            "        },\n" +
            "        {\n" +
            "          \"is_short\": true,\n" +
            "          \"text\": {\n" +
            "            \"content\": \"**地点**\\n江苏省、浙江省、上海市\",\n" +
            "            \"tag\": \"lark_md\"\n" +
            "          }\n" +
            "        }\n" +
            "      ],\n" +
            "      \"tag\": \"div\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"tag\": \"div\",\n" +
            "      \"text\": {\n" +
            "        \"content\": \"亲爱的同事们，\\n气象局发布台风橙色预警，7月26日江浙沪地区预计平均风力可达10级以上。\\n建议江浙沪地区同学明日居家办公。如有值班等特殊情况，请各部门视情况安排。\\n请同学们关好门窗，妥善安置室外用品，停止一切户外活动，注意保护自身安全。\\n如有疑问，请联系[值班号](https://open.feishu.cn/)。\",\n" +
            "        \"tag\": \"lark_md\"\n" +
            "      }\n" +
            "    },\n" +
            "    {\n" +
            "      \"actions\": [\n" +
            "        {\n" +
            "          \"tag\": \"button\",\n" +
            "          \"text\": {\n" +
            "            \"content\": \"我已知悉\",\n" +
            "            \"tag\": \"plain_text\"\n" +
            "          },\n" +
            "          \"type\": \"primary\",\n" +
            "          \"value\": {\n" +
            "            \"key\": \"value\"\n" +
            "          }\n" +
            "        }\n" +
            "      ],\n" +
            "      \"tag\": \"action\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"tag\": \"hr\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"elements\": [\n" +
            "        {\n" +
            "          \"content\": \"[来自应急通知](https://www.open.feishu.cn/)\",\n" +
            "          \"tag\": \"lark_md\"\n" +
            "        }\n" +
            "      ],\n" +
            "      \"tag\": \"note\"\n" +
            "    }\n" +
            "  ]\n" +
            "}";
}
