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
        // 2、解析
        return parser.parseInstruction(Base58.decode(instructionData), inputAccount);
    }

}
