package cn.xlystar.parse.solSwap;

import org.apache.commons.lang.StringUtils;
import org.bitcoinj.core.Base58;
import java.util.HashMap;
import java.util.Map;

public class SolInstructionProcessor {

    public static Map<String, Object> processInstruction(String programId, String[] inputAccount, String instructionData) {
        // 1、获取解析器
        InstructionParser parser = SolInstructionParserFactory.getParser(programId);
        if (parser == null) {
            return null;
        }
        // 2、异常处理
        Map<String, Object> res = new HashMap<>();
        if (StringUtils.isEmpty(instructionData) || StringUtils.isEmpty(programId)) {
            res.put("error", "programId is null or instruction data is null");
            return res;
        }
        // 3、解析
        return parser.parseInstruction(Base58.decode(instructionData), inputAccount);
    }

}
