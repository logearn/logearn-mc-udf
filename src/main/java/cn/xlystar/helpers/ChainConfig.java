package cn.xlystar.helpers;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

@Data
public class ChainConfig {
    private String chainId;
    private String protocol;
    private JsonNode chainConf;
    private JsonNode protocolConf;
    private JsonNode tokens;
    
    public String getWCoinAddress() {
    	String wcoin = this.getChainConf().get("wcoin").asText();
        String wcoinAddress = this.getTokens().get(wcoin).get("address").asText();
        return wcoinAddress;
    }
}
