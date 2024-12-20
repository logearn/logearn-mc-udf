package cn.xlystar.parse.solSwap.system_program;

import org.bitcoinj.core.Base58;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class SystemInstructionParser {
    public static Map<String, Object> parseInstruction(byte[] data, String[] accounts) {
        Map<String, Object> result = new HashMap<>();
        
        if (data == null || data.length == 0) {
            throw new IllegalArgumentException("Empty instruction data");
        }

        try {
            int instructionType = data[0] & 0xFF;
            SystemInstruction instruction = SystemInstruction.fromValue(instructionType);
            result.put("type", instruction.name());

            byte[] instructionData = Arrays.copyOfRange(data, 1, data.length);
            Map<String, Object> info = new HashMap<>();

            switch (instruction) {
                case CreateAccount: // 0
                    info = parseCreateAccount(instructionData, accounts);
                    break;
                case Assign: // 1
                    info = parseAssign(instructionData, accounts);
                    break;
                case Transfer: // 2
                    info = parseTransfer(instructionData, accounts);
                    break;
                case CreateAccountWithSeed: // 3
                    info = parseCreateAccountWithSeed(instructionData, accounts);
                    break;
                case AdvanceNonce: // 4
                    info = parseAdvanceNonce(accounts);
                    break;
                case WithdrawNonce: // 5
                    info = parseWithdrawNonce(instructionData, accounts);
                    break;
                case InitializeNonce: // 6
                    info = parseInitializeNonce(instructionData, accounts);
                    break;
                case AuthorizeNonce: //7
                    info = parseAuthorizeNonce(instructionData, accounts);
                    break;
                case Allocate: // 8
                    info = parseAllocate(instructionData, accounts);
                    break;
                case AllocateWithSeed: // 9
                    info = parseAllocateWithSeed(instructionData, accounts);
                    break;
                case AssignWithSeed: // 10
                    info = parseAssignWithSeed(instructionData, accounts);
                    break;
                case TransferWithSeed: // 11
                    info = parseTransferWithSeed(instructionData, accounts);
                    break;
                case UpgradeNonceAccount: // 12
                    info = parseUpgradeNonceAccount(accounts);
                    break;                default:
                    info.put("message", "Unsupported instruction type: " + instruction.name());
            }

            result.put("info", info);
            
        } catch (Exception e) {
            e.printStackTrace();
            result.put("error", "Failed to parse instruction: " + e.getMessage());
        }
        
        return result;
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    // CreateAccount 指令解析
    private static Map<String, Object> parseCreateAccount(byte[] data, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        System.out.println("CreateAccount raw data (hex): " + bytesToHex(data));
        
        int position = data.length;
        position -= 32; // owner
        byte[] owner = Arrays.copyOfRange(data, position, position + 32);
        
        position -= 8; // space
        ByteBuffer spaceBuffer = ByteBuffer.wrap(data, position, position + 8);
        spaceBuffer.order(ByteOrder.LITTLE_ENDIAN);
        long space = spaceBuffer.getLong();
        
        position -= 8; // lamports
        ByteBuffer lamportsBuffer = ByteBuffer.wrap(data, position, position + 8);
        lamportsBuffer.order(ByteOrder.LITTLE_ENDIAN);
        long lamports = lamportsBuffer.getLong();

        info.put("source", accounts[0]);
        info.put("newAccount", accounts[1]);
        info.put("lamports", lamports);
        info.put("space", space);
        info.put("owner", Base58.encode(owner));
        
        return info;
    }

    // Assign 指令解析
    private static Map<String, Object> parseAssign(byte[] data, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        System.out.println("Assign raw data (hex): " + bytesToHex(data));
        
        int position = data.length;
        position -= 32; // owner
        byte[] newOwner = Arrays.copyOfRange(data, position, position + 32);

        info.put("account", accounts[0]);
        info.put("owner", Base58.encode(newOwner));
        
        return info;
    }

    // Transfer 指令解析
    private static Map<String, Object> parseTransfer(byte[] data, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        System.out.println("Transfer raw data (hex): " + bytesToHex(data));
        
        int position = data.length;
        position -= 8; // lamports
        ByteBuffer transferBuffer = ByteBuffer.wrap(data, position, position + 8);
        transferBuffer.order(ByteOrder.LITTLE_ENDIAN);
        long amount = transferBuffer.getLong();

        info.put("source", accounts[0]);
        info.put("destination", accounts[1]);
        info.put("lamports", amount);
        
        return info;
    }

    // CreateAccountWithSeed 指令解析
    private static Map<String, Object> parseCreateAccountWithSeed(byte[] data, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        System.out.println("CreateAccountWithSeed raw data (hex): " + bytesToHex(data));
        
        int position = data.length;
        position -= 32; // owner
        byte[] programId = Arrays.copyOfRange(data, position, position + 32);
        
        position -= 8; // space
        ByteBuffer spaceBuffer = ByteBuffer.wrap(data, position, position + 8);
        spaceBuffer.order(ByteOrder.LITTLE_ENDIAN);
        long space = spaceBuffer.getLong();
        
        position -= 8; // lamports
        ByteBuffer lamportsBuffer = ByteBuffer.wrap(data, position, position + 8);
        lamportsBuffer.order(ByteOrder.LITTLE_ENDIAN);
        long lamports = lamportsBuffer.getLong();
        
        position -= 4; // seed length
        int seedLength = ByteBuffer.wrap(data, position, position + 4)
                .order(ByteOrder.LITTLE_ENDIAN).getInt();
        
        position -= seedLength; // seed
        byte[] seed = Arrays.copyOfRange(data, position, position + seedLength);

        info.put("base", accounts[0]);
        info.put("account", accounts[1]);
        info.put("seed", new String(seed));
        info.put("lamports", lamports);
        info.put("space", space);
        info.put("owner", Base58.encode(programId));
        
        return info;
    }

    // AdvanceNonce 指令解析
    private static Map<String, Object> parseAdvanceNonce(String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        
        info.put("nonceAccount", accounts[0]);
        info.put("recentBlockhashesSysvar", accounts[1]);
        info.put("nonceAuthority", accounts[2]);

        return info;
    }

    // Allocate 指令解析
    private static Map<String, Object> parseAllocate(byte[] data, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        System.out.println("Allocate raw data (hex): " + bytesToHex(data));

        int position = data.length;
        position -= 8; // space
        ByteBuffer allocateBuffer = ByteBuffer.wrap(data, position, 8);
        allocateBuffer.order(ByteOrder.LITTLE_ENDIAN);
        long space = allocateBuffer.getLong();

        info.put("account", accounts[0]);
        info.put("space", space);

        return info;
    }

    // TransferWithSeed 指令解析
    private static Map<String, Object> parseTransferWithSeed(byte[] data, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        System.out.println("TransferWithSeed raw data (hex): " + bytesToHex(data));

        int position = data.length;
        position -= 32; // owner
        byte[] fromOwner = Arrays.copyOfRange(data, position, position + 32);

        position -= 8; // lamports
        ByteBuffer lamportsBuffer = ByteBuffer.wrap(data, position, position + 8);
        lamportsBuffer.order(ByteOrder.LITTLE_ENDIAN);
        long lamports = lamportsBuffer.getLong();

        position -= 4; // seed length
        int seedLength = ByteBuffer.wrap(data, position, position + 4)
                .order(ByteOrder.LITTLE_ENDIAN).getInt();

        position -= seedLength; // seed
        byte[] seed = Arrays.copyOfRange(data, position, position + seedLength);

        info.put("fromBase", accounts[0]);
        info.put("from", accounts[1]);
        info.put("to", accounts[2]);
        info.put("seed", new String(seed));
        info.put("owner", Base58.encode(fromOwner));
        info.put("lamports", lamports);

        return info;
    }

    // WithdrawNonce 指令解析
    private static Map<String, Object> parseWithdrawNonce(byte[] data, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        System.out.println("WithdrawNonce raw data (hex): " + bytesToHex(data));
        
        int position = data.length;
        position -= 8; // lamports
        ByteBuffer lamportsBuffer = ByteBuffer.wrap(data, position, position + 8);
        lamportsBuffer.order(ByteOrder.LITTLE_ENDIAN);
        long lamports = lamportsBuffer.getLong();

        info.put("nonce", accounts[0]);
        info.put("recipient", accounts[1]);
        info.put("authority", accounts[2]);
        info.put("recentBlockhashes", accounts[3]);
        info.put("rentSysvar", accounts[4]);
        info.put("lamports", lamports);
        
        return info;
    }

    // InitializeNonce 指令解析
    private static Map<String, Object> parseInitializeNonce(byte[] data, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        System.out.println("InitializeNonce raw data (hex): " + bytesToHex(data));
        
        int position = data.length;
        position -= 32; // authority
        byte[] authority = Arrays.copyOfRange(data, position, position + 32);

        info.put("nonceAccount", accounts[0]);
        info.put("recentBlockhashesSysvar", accounts[1]);
        info.put("rentSysvar", accounts[2]);
        info.put("nonceAuthority", Base58.encode(authority));
        
        return info;
    }

    // AuthorizeNonce 指令解析
    private static Map<String, Object> parseAuthorizeNonce(byte[] data, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        System.out.println("AuthorizeNonce raw data (hex): " + bytesToHex(data));

        int position = data.length;
        position -= 32; // new authority
        byte[] newAuthority = Arrays.copyOfRange(data, position, position + 32);

        info.put("nonce", accounts[0]);
        info.put("authority", accounts[1]);
        info.put("newAuthority", Base58.encode(newAuthority));

        return info;
    }

    // AllocateWithSeed 指令解析
    private static Map<String, Object> parseAllocateWithSeed(byte[] data, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        System.out.println("AllocateWithSeed raw data (hex): " + bytesToHex(data));

        int position = data.length;
        position -= 32; // owner
        byte[] owner = Arrays.copyOfRange(data, position, position + 32);

        position -= 8; // space
        ByteBuffer spaceBuffer = ByteBuffer.wrap(data, position, position + 8);
        spaceBuffer.order(ByteOrder.LITTLE_ENDIAN);
        long space = spaceBuffer.getLong();

        position -= 4; // seed length
        int seedLength = ByteBuffer.wrap(data, position, position + 4)
                .order(ByteOrder.LITTLE_ENDIAN).getInt();

        position -= seedLength; // seed
        byte[] seed = Arrays.copyOfRange(data, position, position + seedLength);

        info.put("account", accounts[0]);
        info.put("base", accounts[1]);
        info.put("seed", new String(seed));
        info.put("space", space);
        info.put("owner", Base58.encode(owner));

        return info;
    }

    // AssignWithSeed 指令解析
    private static Map<String, Object> parseAssignWithSeed(byte[] data, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        System.out.println("AssignWithSeed raw data (hex): " + bytesToHex(data));

        int position = data.length;
        position -= 32; // owner
        byte[] owner = Arrays.copyOfRange(data, position, position + 32);

        position -= 4; // seed length
        int seedLength = ByteBuffer.wrap(data, position, position + 4)
                .order(ByteOrder.LITTLE_ENDIAN).getInt();

        position -= seedLength; // seed
        byte[] seed = Arrays.copyOfRange(data, position, position + seedLength);

        info.put("account", accounts[0]);
        info.put("base", accounts[1]);
        info.put("seed", new String(seed));
        info.put("owner", Base58.encode(owner));

        return info;
    }

    // UpgradeNonceAccount 指令解析
    private static Map<String, Object> parseUpgradeNonceAccount(String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        info.put("nonce", accounts[0]);
        info.put("authority", accounts[1]);

        return info;
    }
} 