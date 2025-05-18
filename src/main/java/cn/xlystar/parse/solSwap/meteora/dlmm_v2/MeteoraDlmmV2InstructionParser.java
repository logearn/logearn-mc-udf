package cn.xlystar.parse.solSwap.meteora.dlmm_v2;

import cn.xlystar.parse.solSwap.InstructionParser;
import org.bouncycastle.util.encoders.Hex;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;


public class MeteoraDlmmV2InstructionParser extends InstructionParser {

    public static final String PROGRAM_ID = "cpamdpZCGKUy5JxQXB4dcpGPiikHawvSWAd6mEn1sGG";

    @Override
    public String getMethodId(ByteBuffer buffer) {
        byte[] methodIdByte = new byte[8];
        buffer.get(methodIdByte);
        return Hex.toHexString(methodIdByte);
    }

    @Override
    public Map<String, Object> matchInstruction(String methodId, ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info;
        switch (MeteoraDlmmV2Instruction.fromValue(methodId)) {
            case SWAP:
                info = parseSwap(buffer, accounts);
                break;
            default:
                return new HashMap<>();
        }
        return info;
    }

    /**
     * 解析 Swap 指令
     *
     * @param buffer   数据缓冲区
     * @param accounts 账户列表
     * @return 指令信息
     */
    private static Map<String, Object> parseSwap(ByteBuffer buffer, String[] accounts) {


        Map<String, Object> info = new HashMap<>();

        // 参数解析 - 按照IDL定义的顺序
        info.put("amount_in", Long.toUnsignedString(buffer.getLong()));  // u64
        info.put("minimum_amount_out", Long.toUnsignedString(buffer.getLong())); // u64

        // 账户信息 - 按照IDL定义的顺序
        info.put("pool_authority", accounts[0]);
        info.put("pool", accounts[1]);
        info.put("input_token_account", accounts[2]);
        info.put("output_token_account", accounts[3]);
        info.put("token_a_vault", accounts[4]);
        info.put("token_b_vault", accounts[5]);
        info.put("token_a_mint", accounts[6]);
        info.put("token_b_mint", accounts[7]);
        info.put("payer", accounts[8]);
        info.put("token_a_program", accounts[9]);
        info.put("token_b_program", accounts[10]);
        info.put("referral_token_account", accounts[11]);
        info.put("event_authority", accounts[12]);
        info.put("program", accounts[13]);
        return info;
    }
} 