package cn.xlystar.helpers;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

import java.util.Set;

@Data
public class ChainConfig {
    private String chainId;
    private String protocol;
    private JsonNode chainConf;
    private JsonNode protocolConf;
    private JsonNode platformTokens;
    private Set<String> platformAddressLists;
    private Set<String> stableCoinLists;
    private JsonNode tokens;
    
    public String getWCoinAddress() {
    	String wcoin = this.getChainConf().get("wcoin").asText();
        String wcoinAddress = this.getTokens().get(wcoin).get("address").asText();
        return wcoinAddress;
    }

    public String getSplTokenProgramId() {
        return this.getProtocolConf().get("SPL_TOKEN_PROGRAM_ID").asText();
    }
    public String getSystemProgramId() {
        return this.getProtocolConf().get("SYSTEM_PROGRAM_ID").asText();
    }
}
