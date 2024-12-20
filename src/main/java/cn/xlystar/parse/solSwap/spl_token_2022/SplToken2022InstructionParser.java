package cn.xlystar.parse.solSwap.spl_token_2022;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.bitcoinj.core.Base58;

public class SplToken2022InstructionParser {
    public static Map<String, Object> parseInstruction(byte[] data, String[] accounts) {
        Map<String, Object> result = new HashMap<>();
        
        if (data == null || data.length == 0) {
            throw new IllegalArgumentException("Empty instruction data");
        }

        try {
            int instructionType = data[0] & 0xFF;
            SplToken2022Instruction instruction = SplToken2022Instruction.fromValue(instructionType);
            result.put("type", instruction.name());

            byte[] instructionData = Arrays.copyOfRange(data, 1, data.length);
            Map<String, Object> info = new HashMap<>();

            switch (instruction) {
                // 基础代币操作
                case Transfer:
                case MintTo:
                case Burn:
                    info = parseAmountInstruction(instructionData, accounts, instruction);
                    break;
                    
                // 带精度检查的操作
                case TransferChecked:
                case MintToChecked:
                case BurnChecked:
                    info = parseCheckedInstruction(instructionData, accounts, instruction);
                    break;
                    
                // 利息相关操作
                case InitializeInterestBearingConfig:
                    info = parseInitializeInterestBearingConfig(instructionData, accounts);
                    break;
                case UpdateRateInterestBearingConfig:
                    info = parseUpdateRateInterestBearingConfig(instructionData, accounts);
                    break;
                case UpdateInterestAccrual:
                    info = parseUpdateInterestAccrual(accounts);
                    break;
                    
                // 转账费用相关操作
                case TransferFeeConfig:
                    info = parseTransferFeeConfig(instructionData, accounts);
                    break;
                case WithholdFee:
                    info = parseWithholdFee(instructionData, accounts);
                    break;
                case WithdrawWithheldTokensFromAccounts:
                    info = parseWithdrawWithheldTokens(instructionData, accounts);
                    break;
                case WithdrawWithheldTokensFromMint:
                    info = parseWithdrawWithheldTokensFromMint(instructionData, accounts);
                    break;
                case HarvestWithheldTokensToMint:
                    info = parseHarvestWithheldTokens(accounts);
                    break;
                    
                // CPI Guard 相关操作
                case EnableCpiGuard:
                    info = parseCpiGuard(accounts, true);
                    break;
                case DisableCpiGuard:
                    info = parseCpiGuard(accounts, false);
                    break;
                    
                // 元数据指针相关操作
                case InitializeMetadataPointer:
                case UpdateMetadataPointer:
                    info = parseMetadataPointer(instructionData, accounts, instruction);
                    break;
                    
                // 组指针相关操作
                case InitializeGroupPointer:
                case UpdateGroupPointer:
                    info = parseGroupPointer(instructionData, accounts, instruction);
                    break;
                case InitializeGroupMemberPointer:
                case UpdateGroupMemberPointer:
                    info = parseGroupMemberPointer(instructionData, accounts, instruction);
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

    // 基础金额操作解析
    private static Map<String, Object> parseAmountInstruction(byte[] data, String[] accounts, 
            SplToken2022Instruction instruction) {
        Map<String, Object> info = new HashMap<>();
        
        ByteBuffer buffer = ByteBuffer.wrap(data);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        long amount = buffer.getLong();
        
        switch (instruction) {
            case Transfer:
                info.put("source", accounts[0]);
                info.put("destination", accounts[1]);
                info.put("authority", accounts[2]);
                break;
            case MintTo:
                info.put("mint", accounts[0]);
                info.put("destination", accounts[1]);
                info.put("mintAuthority", accounts[2]);
                break;
            case Burn:
                info.put("account", accounts[0]);
                info.put("mint", accounts[1]);
                info.put("authority", accounts[2]);
                break;
        }
        
        info.put("amount", amount);
        return info;
    }

    // 带精度检查的操作解析
    private static Map<String, Object> parseCheckedInstruction(byte[] data, String[] accounts,
            SplToken2022Instruction instruction) {
        Map<String, Object> info = new HashMap<>();
        
        ByteBuffer buffer = ByteBuffer.wrap(data);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        long amount = buffer.getLong();
        int decimals = buffer.get() & 0xFF;
        
        switch (instruction) {
            case TransferChecked:
                info.put("source", accounts[0]);
                info.put("mint", accounts[1]);
                info.put("destination", accounts[2]);
                info.put("authority", accounts[3]);
                break;
            case MintToChecked:
                info.put("mint", accounts[0]);
                info.put("destination", accounts[1]);
                info.put("mintAuthority", accounts[2]);
                break;
            case BurnChecked:
                info.put("account", accounts[0]);
                info.put("mint", accounts[1]);
                info.put("authority", accounts[2]);
                break;
        }
        
        info.put("amount", amount);
        info.put("decimals", decimals);
        return info;
    }

    // 利息配置相关解析
    private static Map<String, Object> parseInitializeInterestBearingConfig(byte[] data, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        ByteBuffer buffer = ByteBuffer.wrap(data);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        long rate = buffer.getLong();  // 利率 (bps)

        info.put("mint", accounts[0]);
        info.put("rateAuthority", accounts[1]);
        info.put("rate", rate);

        return info;
    }

    private static Map<String, Object> parseUpdateRateInterestBearingConfig(byte[] data, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        ByteBuffer buffer = ByteBuffer.wrap(data);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        long newRate = buffer.getLong();  // 新利率 (bps)

        info.put("mint", accounts[0]);
        info.put("rateAuthority", accounts[1]);
        info.put("newRate", newRate);

        return info;
    }

    private static Map<String, Object> parseUpdateInterestAccrual(String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        info.put("mint", accounts[0]);
        info.put("account", accounts[1]);
        return info;
    }

    // 转账费用相关解析
    private static Map<String, Object> parseTransferFeeConfig(byte[] data, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        ByteBuffer buffer = ByteBuffer.wrap(data);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        // 转账费用配置
        long transferFeeBasisPoints = buffer.getLong();  // 转账费率 (bps)
        long maximumFee = buffer.getLong();             // 最大费用

        info.put("mint", accounts[0]);
        info.put("feeAuthority", accounts[1]);
        info.put("transferFeeBasisPoints", transferFeeBasisPoints);
        info.put("maximumFee", maximumFee);

        return info;
    }

    private static Map<String, Object> parseWithholdFee(byte[] data, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        ByteBuffer buffer = ByteBuffer.wrap(data);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        long amount = buffer.getLong();

        info.put("source", accounts[0]);
        info.put("mint", accounts[1]);
        info.put("feeAccount", accounts[2]);
        info.put("authority", accounts[3]);
        info.put("amount", amount);

        return info;
    }

    private static Map<String, Object> parseWithdrawWithheldTokens(byte[] data, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        info.put("mint", accounts[0]);
        info.put("feeAccount", accounts[1]);
        info.put("destination", accounts[2]);
        info.put("withdrawWithheldAuthority", accounts[3]);

        // 可选：解析要提取代币的账户列表
        if (accounts.length > 4) {
            String[] sourceAccounts = Arrays.copyOfRange(accounts, 4, accounts.length);
            info.put("sourceAccounts", sourceAccounts);
        }

        return info;
    }

    private static Map<String, Object> parseWithdrawWithheldTokensFromMint(byte[] data, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        info.put("mint", accounts[0]);
        info.put("destination", accounts[1]);
        info.put("withdrawWithheldAuthority", accounts[2]);

        return info;
    }

    private static Map<String, Object> parseHarvestWithheldTokens(String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        info.put("mint", accounts[0]);

        // 解析要收割代币的账户列表
        if (accounts.length > 1) {
            String[] sourceAccounts = Arrays.copyOfRange(accounts, 1, accounts.length);
            info.put("sourceAccounts", sourceAccounts);
        }

        return info;
    }

    // CPI Guard 相关解析
    private static Map<String, Object> parseCpiGuard(String[] accounts, boolean isEnable) {
        Map<String, Object> info = new HashMap<>();

        info.put("account", accounts[0]);
        info.put("authority", accounts[1]);
        info.put("action", isEnable ? "enable" : "disable");

        return info;
    }

    // 元数据指针相关解析
    private static Map<String, Object> parseMetadataPointer(byte[] data, String[] accounts,
                                                            SplToken2022Instruction instruction) {
        Map<String, Object> info = new HashMap<>();

        ByteBuffer buffer = ByteBuffer.wrap(data);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        // 读取元数据地址
        byte[] metadataAddress = new byte[32];
        buffer.get(metadataAddress);

        info.put("mint", accounts[0]);
        info.put("authority", accounts[1]);
        info.put("metadataAddress", Base58.encode(metadataAddress));

        return info;
    }

    // 组指针相关解析
    private static Map<String, Object> parseGroupPointer(byte[] data, String[] accounts,
                                                         SplToken2022Instruction instruction) {
        Map<String, Object> info = new HashMap<>();

        ByteBuffer buffer = ByteBuffer.wrap(data);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        // 读取组地址
        byte[] groupAddress = new byte[32];
        buffer.get(groupAddress);

        info.put("mint", accounts[0]);
        info.put("authority", accounts[1]);
        info.put("groupAddress", Base58.encode(groupAddress));

        return info;
    }

    private static Map<String, Object> parseGroupMemberPointer(byte[] data, String[] accounts,
                                                               SplToken2022Instruction instruction) {
        Map<String, Object> info = new HashMap<>();

        ByteBuffer buffer = ByteBuffer.wrap(data);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        // 读取成员地址
        byte[] memberAddress = new byte[32];
        buffer.get(memberAddress);

        info.put("mint", accounts[0]);
        info.put("authority", accounts[1]);
        info.put("memberAddress", Base58.encode(memberAddress));

        return info;
    }

} 