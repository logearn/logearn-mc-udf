package cn.xlystar.helpers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.io.InputStream;

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
        	conf.setChainId(chainId);
        	conf.setTokens(configJson.get(chainId).get("tokens"));
        	conf.setChainConf(configJson.get(chainId));
        	if (!StringUtils.isEmpty(protocol)) {
        		conf.setProtocol(protocol);
            	conf.setProtocolConf(configJson.get(chainId).get("protocols").get(protocol));
        	}
        	return conf;
        } catch (Exception e) {
            System.out.println(String.format("[getProtocolConf] 无法加载对应的 protocol 配置信息：chain-{}, conf-{}", chainId, protocol));
            throw e;
        }
    }

}
