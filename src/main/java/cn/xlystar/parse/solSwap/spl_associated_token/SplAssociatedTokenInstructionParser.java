package cn.xlystar.parse.solSwap.spl_associated_token;

import java.util.HashMap;
import java.util.Map;

public class SplAssociatedTokenInstructionParser {
    public static Map<String, Object> parseInstruction(byte[] data, String[] accounts) {
        Map<String, Object> result = new HashMap<>();
        
        if (data == null || data.length == 0) {
            // Associated Token 程序的 Create 指令没有数据
            result.put("type", "Create");
            result.put("info", parseCreate(accounts));
            return result;
        }

        try {
            int instructionType = data[0] & 0xFF;
            SplAssociatedTokenInstruction instruction = SplAssociatedTokenInstruction.fromValue(instructionType);
            result.put("type", instruction.name());

            Map<String, Object> info = new HashMap<>();
            switch (instruction) {
                case Create:
                    info = parseCreate(accounts);
                    break;
                case CreateIdempotent:
                    info = parseCreateIdempotent(accounts);
                    break;
                case RecoverNested:
                    info = parseRecoverNested(accounts);
                    break;
                default:
                    info.put("error", "Unsupported instruction type: " + instruction.name());
            }
            
            result.put("info", info);
            
        } catch (Exception e) {
            result.put("error", "Failed to parse instruction: " + e.getMessage());
        }
        
        return result;
    }

    private static Map<String, Object> parseCreate(String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        
        info.put("fundingAccount", accounts[0]);           // 支付账户
        info.put("associatedTokenAccount", accounts[1]);   // 要创建的关联代币账户
        info.put("wallet", accounts[2]);                   // 钱包地址
        info.put("mint", accounts[3]);                     // 代币铸造地址
        info.put("systemProgram", accounts[4]);            // System Program
        info.put("tokenProgram", accounts[5]);            // Token Program
        if (accounts.length > 6) {
            info.put("rentSysvar", accounts[6]);          // Rent Sysvar (可选)
        }
        
        return info;
    }

    private static Map<String, Object> parseCreateIdempotent(String[] accounts) {
        // CreateIdempotent 与 Create 的账户结构相同
        return parseCreate(accounts);
    }

    private static Map<String, Object> parseRecoverNested(String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        
        info.put("nestedAssociatedTokenAccount", accounts[0]);  // 嵌套的关联代币账户
        info.put("nestedMint", accounts[1]);                    // 嵌套代币的铸造地址
        info.put("destinationAssociatedTokenAccount", accounts[2]); // 目标关联代币账户
        info.put("wallet", accounts[3]);                        // 钱包地址
        info.put("ownerAssociatedTokenAccount", accounts[4]);   // 所有者的关联代币账户
        info.put("ownerMint", accounts[5]);                     // 所有者代币的铸造地址
        info.put("tokenProgram", accounts[6]);                 // Token Program
        
        return info;
    }
} 