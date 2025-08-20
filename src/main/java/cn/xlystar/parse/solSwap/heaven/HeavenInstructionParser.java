package cn.xlystar.parse.solSwap.heaven;

import cn.xlystar.parse.solSwap.InstructionParser;
import org.bouncycastle.util.encoders.Hex;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class HeavenInstructionParser extends InstructionParser {

    public static final String PROGRAM_ID = "HEAVENoP2qxoeuF8Dj2oT1GHEnu49U5mJYkdeC8BAX2o";

    @Override
    public String getMethodId(ByteBuffer buffer) {
        byte[] methodIdByte = new byte[8];
        buffer.get(methodIdByte);
        return Hex.toHexString(methodIdByte);
    }

    @Override
    public Map<String, Object> matchInstruction(String methodId, ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info;
        switch (HeavenInstruction.fromValue(methodId)) {
            case BUY:
                info = parseBuy(buffer, accounts);
                break;
            case SELL:
                info = parseSell(buffer, accounts);
                break;
            default:
                return new HashMap<>();
        }
        return info;
    }


    /**
     * 解析 Buy 指令
     * @param buffer 数据缓冲区
     * @param accounts 账户列表
     * @return 指令信息
     */
    private static Map<String, Object> parseBuy(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        
        // 解析账户（根据IDL定义的14个账户）
        info.put("token_a_program", accounts[0]);
        info.put("token_b_program", accounts[1]);
        info.put("associated_token_program", accounts[2]);
        info.put("system_program", accounts[3]);
        info.put("pool_state", accounts[4]);
        info.put("user", accounts[5]);

        info.put("token_a_mint", accounts[6]);
        info.put("token_b_mint", accounts[7]);
        info.put("user_token_a_vault", accounts[8]);
        info.put("user_token_b_vault", accounts[9]);
        info.put("token_a_vault", accounts[10]);
        info.put("token_b_vault", accounts[11]);
        info.put("protocol_config", accounts[12]);
        info.put("instruction_sysvar_account_info", accounts[13]);
        
        return info;
    }
    
    /**
     * 解析 Sell 指令
     * @param buffer 数据缓冲区
     * @param accounts 账户列表
     * @return 指令信息
     */
    private static Map<String, Object> parseSell(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 解析账户（根据IDL定义的14个账户）
        info.put("token_a_program", accounts[0]);
        info.put("token_b_program", accounts[1]);
        info.put("associated_token_program", accounts[2]);
        info.put("system_program", accounts[3]);
        info.put("pool_state", accounts[4]);
        info.put("user", accounts[5]);

        info.put("token_a_mint", accounts[6]);
        info.put("token_b_mint", accounts[7]);
        info.put("user_token_a_vault", accounts[8]);
        info.put("user_token_b_vault", accounts[9]);
        info.put("token_a_vault", accounts[10]);
        info.put("token_b_vault", accounts[11]);
        info.put("protocol_config", accounts[12]);
        info.put("instruction_sysvar_account_info", accounts[13]);

        return info;
    }
    
    private static String parseString(ByteBuffer buffer) {
        int length = buffer.getInt();
        byte[] bytes = new byte[length];
        buffer.get(bytes);
        return new String(bytes);
    }

}