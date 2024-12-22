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

    /// Creates an associated token account for the given wallet address and
    /// token mint Returns an error if the account exists.
    ///
    ///   0. `[writeable,signer]` Funding account (must be a system account)
    ///   1. `[writeable]` Associated token account address to be created
    ///   2. `[]` Wallet address for the new associated token account
    ///   3. `[]` The token mint for the new associated token account
    ///   4. `[]` System program
    ///   5. `[]` SPL Token program
//    Create,
    private static Map<String, Object> parseCreate(String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        
        info.put("source", accounts[0]);           // 支付账户
        info.put("account", accounts[1]);   // 要创建的关联代币账户
        info.put("wallet", accounts[2]);                   // 钱包地址
        info.put("mint", accounts[3]);                     // 代币铸造地址
        info.put("systemProgram", accounts[4]);            // System Program
        info.put("tokenProgram", accounts[5]);            // Token Program
        if (accounts.length > 6) {
            info.put("rentSysvar", accounts[6]);          // Rent Sysvar (可选)
        }
        
        return info;
    }

    /// Creates an associated token account for the given wallet address and
    /// token mint, if it doesn't already exist.  Returns an error if the
    /// account exists, but with a different owner.
    ///
    ///   0. `[writeable,signer]` Funding account (must be a system account)
    ///   1. `[writeable]` Associated token account address to be created
    ///   2. `[]` Wallet address for the new associated token account
    ///   3. `[]` The token mint for the new associated token account
    ///   4. `[]` System program
    ///   5. `[]` SPL Token program
//    CreateIdempotent,
    private static Map<String, Object> parseCreateIdempotent(String[] accounts) {
        // CreateIdempotent 与 Create 的账户结构相同
        return parseCreate(accounts);
    }

    /// Transfers from and closes a nested associated token account: an
    /// associated token account owned by an associated token account.
    ///
    /// The tokens are moved from the nested associated token account to the
    /// wallet's associated token account, and the nested account lamports are
    /// moved to the wallet.
    ///
    /// Note: Nested token accounts are an anti-pattern, and almost always
    /// created unintentionally, so this instruction should only be used to
    /// recover from errors.
    ///
    ///   0. `[writeable]` Nested associated token account, must be owned by `3`
    ///   1. `[]` Token mint for the nested associated token account
    ///   2. `[writeable]` Wallet's associated token account
    ///   3. `[]` Owner associated token account address, must be owned by `5`
    ///   4. `[]` Token mint for the owner associated token account
    ///   5. `[writeable, signer]` Wallet address for the owner associated token
    ///      account
    ///   6. `[]` SPL Token program
//    RecoverNested,
    private static Map<String, Object> parseRecoverNested(String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        
        info.put("nestedSource", accounts[0]);
        info.put("nestedMint", accounts[1]);
        info.put("destination", accounts[2]);
        info.put("nestedOwner", accounts[3]);
        info.put("ownerMint", accounts[4]);
        info.put("wallet", accounts[5]);
        info.put("tokenProgram", accounts[6]);
        
        return info;
    }
} 