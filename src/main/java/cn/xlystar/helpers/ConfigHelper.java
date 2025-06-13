package cn.xlystar.helpers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author andy
 * @date 2023/10/17 上午11:41
 */
public class ConfigHelper {
    private final JsonNode configJson;

    public ConfigHelper() throws IOException {
        configJson = readJsonFile();
    }
    
    private JsonNode readJsonFile() throws IOException {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("chain-config.json");
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(inputStream);
    }
    
    public ChainConfig getConfig(String chainId) {
        return this.getConfig(chainId, null);
    }

    
    public JsonNode getJsonNode(String json) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(json);
    }
    
    public ChainConfig getConfig(Integer chainId, String protocol) {
    	return this.getConfig(chainId.toString(), protocol);
    }
    
    public ChainConfig getConfig(String chainId, String protocol) {
        try {
        	ChainConfig conf = new ChainConfig();
            JsonNode chainNode = configJson.get(chainId);
            conf.setChainId(chainId);
        	conf.setTokens(chainNode.get("tokens"));
            conf.setStableCoinLists(extractTokenAddresses(chainNode.get("tokens")));
            conf.setPlatformTokens(chainNode.get("platform_tokens"));
            conf.setPlatformAddressLists(getPlatformAddressLists(chainNode.get("platform_tokens")));
            conf.setChainConf(chainNode);
        	if (!StringUtils.isEmpty(protocol)) {
        		conf.setProtocol(protocol);
            	conf.setProtocolConf(chainNode.get("protocols").get(protocol));
        	}
        	return conf;
        } catch (Exception e) {
            System.out.println(String.format("[getProtocolConf] 无法加载对应的 protocol 配置信息：chain-{}, conf-{}", chainId, protocol));
            throw e;
        }
    }

    public Set<String> getPlatformAddressLists(JsonNode platformTokensNode) {
        // 1. 提取所有代币地址（key）
        Set<String> platformAddressList = new HashSet<>();
        if (platformTokensNode == null || !platformTokensNode.isObject()) return platformAddressList;
        Iterator<Map.Entry<String, JsonNode>> fields = platformTokensNode.fields();

        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> entry = fields.next();
            String tokenAddress = entry.getKey();

            platformAddressList.add(tokenAddress);
        }

        return platformAddressList;
    }

    public Set<String> extractTokenAddresses(JsonNode tokensNode) {
        Set<String> addresses = new HashSet<>();

        if (tokensNode == null || !tokensNode.isObject()) {
            return addresses; // 返回空列表
        }

        // 遍历 tokens 下的所有代币
        Iterator<Map.Entry<String, JsonNode>> tokenFields = tokensNode.fields();

        while (tokenFields.hasNext()) {
            Map.Entry<String, JsonNode> tokenEntry = tokenFields.next();
            JsonNode tokenInfo = tokenEntry.getValue();

            // 获取 address 字段的值
            if (tokenInfo != null && tokenInfo.has("address")) {
                String address = tokenInfo.get("address").asText();
                addresses.add(address);
            }
        }

        return addresses;
    }

}
