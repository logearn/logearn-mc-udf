package cn.xlystar.parse.solSwap.raydium.amm_v4;

import org.bitcoinj.core.Base58;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Map;

public class RaydiumAmmInstructionParser {

    private static final String PROGRAM_ID = "675kPX9MHTjS2zt1qfr1NYHuzeLXfQM9H24wFSUt1Mp8";

    public static Map<String, Object> parseInstruction(byte[] data, String[] accounts) {
        Map<String, Object> result = new HashMap<>();

        if (data == null || data.length == 0) {
            result.put("error", "Invalid instruction data");
            return result;
        }

        try {
            int discriminator = data[0] & 0xFF;
            RaydiumAmmInstruction instructionType = RaydiumAmmInstruction.fromValue(discriminator);

            ByteBuffer buffer = ByteBuffer.wrap(data, 1, data.length - 1).order(ByteOrder.LITTLE_ENDIAN);
            Map<String, Object> info = parseInstructionInfo(instructionType, buffer, accounts);
            result.put("info", info);

        } catch (Exception e) {
            result.put("error", "Failed to parse instruction: " + e.getMessage());
        }

        return result;
    }

    private static Map<String, Object> parseInstructionInfo(RaydiumAmmInstruction instruction, ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        try {
            switch (instruction) {
                case INITIALIZE:
                    info = parseInitialize(buffer, accounts);
                    info.put("type", "initialize");
                    return info;
                case INITIALIZE2:
                    info = parseInitialize2(buffer, accounts);
                    info.put("type", "initialize2");
                    return info;
                case MONITOR_STEP:
                    info = parseMonitorStep(buffer, accounts);
                    info.put("type", "monitorStep");
                    return info;
                case DEPOSIT:
                    info = parseDeposit(buffer, accounts);
                    info.put("type", "deposit");
                    return info;
                case WITHDRAW:
                    info = parseWithdraw(buffer, accounts);
                    info.put("type", "withdraw");
                    return info;
                case MIGRATE_TO_OPEN_BOOK:
                    info = parseMigrateToOpenBook(buffer, accounts);
                    info.put("type", "migrateToOpenBook");
                    return info;
                case SET_PARAMS:
                    info = parseSetParams(buffer, accounts);
                    info.put("type", "setParams");
                    return info;
                case WITHDRAW_PNL:
                    info = parseWithdrawPnl(buffer, accounts);
                    info.put("type", "withdrawPnl");
                    return info;
                case WITHDRAW_SRM:
                    info = parseWithdrawSrm(buffer, accounts);
                    info.put("type", "withdrawSrm");
                    return info;
                case SWAP_BASE_IN:
                    info = parseSwapBaseIn(buffer, accounts);
                    info.put("type", "swapBaseIn");
                    return info;
                case PRE_INITIALIZE:
                    info = parsePreInitialize(buffer, accounts);
                    info.put("type", "preInitialize");
                    return info;
                case SWAP_BASE_OUT:
                    info = parseSwapBaseOut(buffer, accounts);
                    info.put("type", "swapBaseOut");
                    return info;
                case SIMULATE_INFO:
                    info = parseSimulateInfo(buffer, accounts);
                    info.put("type", "simulateInfo");
                    return info;
                case ADMIN_CANCEL_ORDERS:
                    info = parseAdminCancelOrders(buffer, accounts);
                    info.put("type", "adminCancelOrders");
                    return info;
                case CREATE_CONFIG_ACCOUNT:
                    info = parseCreateConfigAccount(buffer, accounts);
                    info.put("type", "createConfigAccount");
                    return info;
                case UPDATE_CONFIG_ACCOUNT:
                    info = parseUpdateConfigAccount(buffer, accounts);
                    info.put("type", "updateConfigAccount");
                    return info;
                default:
                    info.put("error", "Unknown instruction type: " + instruction.name());
                    info.put("type", "unknown");
                    return info;
            }
        } catch (Exception e) {
            info.put("error", "Failed to parse " + instruction.name() + " parameters: " + e.getMessage());
            info.put("type", "error");
            return info;
        }
    }

    ///   Initializes a new AmmInfo.
    ///
    ///   Not supported yet, please use `Initialize2` to new a AMM pool
//    #[deprecated(note = "Not supported yet, please use `Initialize2` instead")]
//    Initialize(InitializeInstruction),
    private static Map<String, Object> parseInitialize(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        // 解析nonce值
        int nonce = Byte.toUnsignedInt(buffer.get());
        info.put("nonce", nonce);
        info.put("message", "deprecated");

        return info;
    }

    ///   Initializes a new AMM pool.
    ///
    ///   0. `[]` Spl Token program id
    ///   1. `[]` Associated Token program id
    ///   2. `[]` Sys program id
    ///   3. `[]` Rent program id
    ///   4. `[writable]` New AMM Account to create.
    ///   5. `[]` $authority derived from `create_program_address(&[AUTHORITY_AMM, &[nonce]])`.
    ///   6. `[writable]` AMM open orders Account
    ///   7. `[writable]` AMM lp mint Account
    ///   8. `[]` AMM coin mint Account
    ///   9. `[]` AMM pc mint Account
    ///   10. `[writable]` AMM coin vault Account. Must be non zero, owned by $authority.
    ///   11. `[writable]` AMM pc vault Account. Must be non zero, owned by $authority.
    ///   12. `[writable]` AMM target orders Account. To store plan orders informations.
    ///   13. `[]` AMM config Account, derived from `find_program_address(&[&&AMM_CONFIG_SEED])`.
    ///   14. `[]` AMM create pool fee destination Account
    ///   15. `[]` Market program id
    ///   16. `[writable]` Market Account. Market program is the owner.
    ///   17. `[writable, signer]` User wallet Account
    ///   18. `[]` User token coin Account
    ///   19. '[]` User token pc Account
    ///   20. `[writable]` User destination lp token ATA Account
//    Initialize2(InitializeInstruction2),

    //    pub struct InitializeInstruction2 {
//        /// nonce used to create valid program address
//        pub nonce: u8,
//                /// utc timestamps for pool open
//                pub open_time: u64,
//                /// init token pc amount
//                pub init_pc_amount: u64,
//                /// init token coin amount
//                pub init_coin_amount: u64,
//    }
    private static Map<String, Object> parseInitialize2(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 1. 读取 nonce (1字节)
        int nonce = Byte.toUnsignedInt(buffer.get());
        // 2. 读取 open_time (8字节)
        String openTime = Long.toUnsignedString(buffer.getLong());
        // 3. 读取 init_pc_amount (8字节)
        String initPcAmount = Long.toUnsignedString(buffer.getLong());
        // 4. 读取 init_coin_amount (8字节)
        String initCoinAmount = Long.toUnsignedString(buffer.getLong());

        // 设置返回信息
        info.put("nonce", nonce);
        info.put("openTime", openTime);
        info.put("initPcAmount", initPcAmount);
        info.put("initCoinAmount", initCoinAmount);

        // 解析各个账户
        String splTokenProgramId = accounts[0]; // Spl Token program id
        String associatedTokenProgramId = accounts[1]; // Associated Token program id
        String sysProgramId = accounts[2]; // Sys program id
        String rentProgramId = accounts[3]; // Rent program id
        String newAmmAccount = accounts[4]; // New AMM Account to create
        String authority = accounts[5]; // $authority derived from create_program_address
        String ammOpenOrdersAccount = accounts[6]; // AMM open orders Account
        String ammLpMintAccount = accounts[7]; // AMM lp mint Account
        String ammCoinMintAccount = accounts[8]; // AMM coin mint Account
        String ammPcMintAccount = accounts[9]; // AMM pc mint Account
        String ammCoinVaultAccount = accounts[10]; // AMM coin vault Account
        String ammPcVaultAccount = accounts[11]; // AMM pc vault Account
        String ammTargetOrdersAccount = accounts[12]; // AMM target orders Account
        String ammConfigAccount = accounts[13]; // AMM config Account
        String ammCreatePoolFeeDestinationAccount = accounts[14]; // AMM create pool fee destination Account
        String marketProgramId = accounts[15]; // Market program id
        String marketAccount = accounts[16]; // Market Account
        String userWalletAccount = accounts[17]; // User wallet Account
        String userTokenCoinAccount = accounts[18]; // User token coin Account
        String userTokenPcAccount = accounts[19]; // User token pc Account
        String userDestinationLpTokenAtaAccount = accounts[20]; // User destination lp token ATA Account

        // 将解析的账户信息存储到 info 中
        info.put("splTokenProgramId", splTokenProgramId);
        info.put("associatedTokenProgramId", associatedTokenProgramId);
        info.put("sysProgramId", sysProgramId);
        info.put("rentProgramId", rentProgramId);
        info.put("newAmmAccount", newAmmAccount);
        info.put("ammAuthority", authority);
        info.put("ammOpenOrdersAccount", ammOpenOrdersAccount);
        info.put("ammLpMintAccount", ammLpMintAccount);
        info.put("ammCoinMintAccount", ammCoinMintAccount);
        info.put("ammPcMintAccount", ammPcMintAccount);
        info.put("ammCoinVaultAccount", ammCoinVaultAccount);
        info.put("ammPcVaultAccount", ammPcVaultAccount);
        info.put("ammTargetOrdersAccount", ammTargetOrdersAccount);
        info.put("ammConfigAccount", ammConfigAccount);
        info.put("ammCreatePoolFeeDestinationAccount", ammCreatePoolFeeDestinationAccount);
        info.put("marketProgramId", marketProgramId);
        info.put("marketAccount", marketAccount);
        info.put("userWalletAccount", userWalletAccount);
        info.put("userTokenCoinAccount", userTokenCoinAccount);
        info.put("userTokenPcAccount", userTokenPcAccount);
        info.put("userDestinationLpTokenAtaAccount", userDestinationLpTokenAtaAccount);

        return info;
    }

    ///   Continue Initializes a new Amm pool because of compute units limit.
    ///   Not supported yet, please use `Initialize2` to new a Amm pool
//    #[deprecated(note = "Not supported yet, please use `Initialize2` instead")]
//    PreInitialize(PreInitializeInstruction),
//    pub struct PreInitializeInstruction {
//        /// nonce used to create valid program address
//        pub nonce: u8,
//    }
    private static Map<String, Object> parsePreInitialize(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 解析nonce值
        int nonce = Byte.toUnsignedInt(buffer.get());
        info.put("nonce", nonce);
        info.put("message", "deprecated");
        return info;
    }

    /// Swap coin or pc from pool, base amount_in with a slippage of minimum_amount_out
    ///
    ///   0. `[]` Spl Token program id
    ///   1. `[writable]` AMM Account
    ///   2. `[]` $authority derived from `create_program_address(&[AUTHORITY_AMM, &[nonce]])`.
    ///   3. `[writable]` AMM open orders Account
    ///   4. `[writable]` (optional)AMM target orders Account, no longer used in the contract, recommended no need to add this Account.
    ///   5. `[writable]` AMM coin vault Account to swap FROM or To.
    ///   6. `[writable]` AMM pc vault Account to swap FROM or To.
    ///   7. `[]` Market program id
    ///   8. `[writable]` Market Account. Market program is the owner.
    ///   9. `[writable]` Market bids Account
    ///   10. `[writable]` Market asks Account
    ///   11. `[writable]` Market event queue Account
    ///   12. `[writable]` Market coin vault Account
    ///   13. `[writable]` Market pc vault Account
    ///   14. '[]` Market vault signer Account
    ///   15. `[writable]` User source token Account.
    ///   16. `[writable]` User destination token Account.
    ///   17. `[signer]` User wallet Account
//    SwapBaseIn(SwapInstructionBaseIn),
//    pub struct SwapInstructionBaseIn {
//        // SOURCE amount to transfer, output to DESTINATION is based on the exchange rate
//        pub amount_in: u64,
//                /// Minimum amount of DESTINATION token to output, prevents excessive slippage
//                pub minimum_amount_out: u64,
//    }
    private static Map<String, Object> parseSwapBaseIn(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        String amountIn = Long.toUnsignedString(buffer.getLong());
        String minimumAmountOut = Long.toUnsignedString(buffer.getLong());
        info.put("amountIn", amountIn);
        info.put("minimumAmountOut", minimumAmountOut);

        info.put("splTokenProgramId", accounts[0]); // Spl Token program id
        info.put("ammAccount", accounts[1]); // AMM Account
        info.put("ammAuthority", accounts[2]); // $authority
        info.put("ammOpenOrdersAccount", accounts[3]); // AMM open orders Account
        int haveTargetOrderAccount = 0;
        if (accounts.length == 18) {
            haveTargetOrderAccount = 1;
            info.put("ammTargetOrdersAccount", accounts[4]); // AMM target orders Account (optional)
        }
        info.put("ammCoinVaultAccount", accounts[4 + haveTargetOrderAccount]); // AMM coin vault Account
        info.put("ammPcVaultAccount", accounts[5 + haveTargetOrderAccount]); // AMM pc vault Account
        info.put("marketProgramId", accounts[6 + haveTargetOrderAccount]); // Market program id
        info.put("marketAccount", accounts[7 + haveTargetOrderAccount]); // Market Account
        info.put("marketBidsAccount", accounts[8 + haveTargetOrderAccount]); // Market bids Account
        info.put("marketAsksAccount", accounts[9 + haveTargetOrderAccount]); // Market asks Account
        info.put("marketEventQueueAccount", accounts[10 + haveTargetOrderAccount]); // Market event queue Account
        info.put("marketCoinVaultAccount", accounts[11 + haveTargetOrderAccount]); // Market coin vault Account
        info.put("marketPcVaultAccount", accounts[12 + haveTargetOrderAccount]); // Market pc vault Account
        info.put("marketVaultSignerAccount", accounts[13 + haveTargetOrderAccount]); // Market vault signer Account
        info.put("userSourceTokenAccount", accounts[14 + haveTargetOrderAccount]); // User source token Account
        info.put("userDestinationTokenAccount", accounts[15 + haveTargetOrderAccount]); // User destination token Account
        info.put("userWalletAccount", accounts[16 + haveTargetOrderAccount]); // User wallet Account (signer)
        return info;
    }

    /// Swap coin or pc from pool, base amount_in with a slippage of minimum_amount_out
    ///
    ///   0. `[]` Spl Token program id
    ///   1. `[writable]` AMM Account
    ///   2. `[]` $authority derived from `create_program_address(&[AUTHORITY_AMM, &[nonce]])`.
    ///   3. `[writable]` AMM open orders Account
    ///   4. `[writable]` (optional)AMM target orders Account, no longer used in the contract, recommended no need to add this Account.
    ///   5. `[writable]` AMM coin vault Account to swap FROM or To.
    ///   6. `[writable]` AMM pc vault Account to swap FROM or To.
    ///   7. `[]` Market program id
    ///   8. `[writable]` Market Account. Market program is the owner.
    ///   9. `[writable]` Market bids Account
    ///   10. `[writable]` Market asks Account
    ///   11. `[writable]` Market event queue Account
    ///   12. `[writable]` Market coin vault Account
    ///   13. `[writable]` Market pc vault Account
    ///   14. '[]` Market vault signer Account
    ///   15. `[writable]` User source token Account.
    ///   16. `[writable]` User destination token Account.
    ///   17. `[signer]` User wallet Account
//    SwapBaseIn(SwapInstructionBaseIn),
//    pub struct SwapInstructionBaseOut {
//        // SOURCE amount to transfer, output to DESTINATION is based on the exchange rate
//        pub max_amount_in: u64,
//                /// Minimum amount of DESTINATION token to output, prevents excessive slippage
//                pub amount_out: u64,
//    }
    private static Map<String, Object> parseSwapBaseOut(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        String maximumAmountIn = Long.toUnsignedString(buffer.getLong());
        String amountOut = Long.toUnsignedString(buffer.getLong());

        info.put("maximumAmountIn", maximumAmountIn);
        info.put("amountOut", amountOut);

        info.put("splTokenProgramId", accounts[0]); // Spl Token program id
        info.put("ammAccount", accounts[1]); // AMM Account
        info.put("ammAuthority", accounts[2]); // $authority
        info.put("ammOpenOrdersAccount", accounts[3]); // AMM open orders Account
        int haveTargetOrderAccount = 0;
        if (accounts.length == 18) {
            haveTargetOrderAccount = 1;
            info.put("ammTargetOrdersAccount", accounts[4]); // AMM target orders Account (optional)
        }
        info.put("ammCoinVaultAccount", accounts[4 + haveTargetOrderAccount]); // AMM coin vault Account
        info.put("ammPcVaultAccount", accounts[5 + haveTargetOrderAccount]); // AMM pc vault Account
        info.put("marketProgramId", accounts[6 + haveTargetOrderAccount]); // Market program id
        info.put("marketAccount", accounts[7 + haveTargetOrderAccount]); // Market Account
        info.put("marketBidsAccount", accounts[8 + haveTargetOrderAccount]); // Market bids Account
        info.put("marketAsksAccount", accounts[9 + haveTargetOrderAccount]); // Market asks Account
        info.put("marketEventQueueAccount", accounts[10 + haveTargetOrderAccount]); // Market event queue Account
        info.put("marketCoinVaultAccount", accounts[11 + haveTargetOrderAccount]); // Market coin vault Account
        info.put("marketPcVaultAccount", accounts[12 + haveTargetOrderAccount]); // Market pc vault Account
        info.put("marketVaultSignerAccount", accounts[13 + haveTargetOrderAccount]); // Market vault signer Account
        info.put("userSourceTokenAccount", accounts[14 + haveTargetOrderAccount]); // User source token Account
        info.put("userDestinationTokenAccount", accounts[15 + haveTargetOrderAccount]); // User destination token Account
        info.put("userWalletAccount", accounts[16 + haveTargetOrderAccount]); // User wallet Account (signer)
        return info;
    }

//    // 账户解析辅助方法
//    private static Map<String, String> parseInitializeAccounts(String[] accounts) {
//        Map<String, String> accountMap = new HashMap<>();
//        if (accounts.length >= 4) {
//            accountMap.put("authority", accounts[0]);
//            accountMap.put("amm", accounts[1]);
//            accountMap.put("tokenA", accounts[2]);
//            accountMap.put("tokenB", accounts[3]);
//        }
//        return accountMap;
//    }


    /**
     * 解析存款指令
     * ///   Deposit some tokens into the pool.  The output is a "pool" token representing ownership
     * ///   into the pool. Inputs are converted to the current ratio.
     * ///
     * ///   0. `[]` Spl Token program id
     * ///   1. `[writable]` AMM Account
     * ///   2. `[]` $authority derived from `create_program_address(&[AUTHORITY_AMM, &[nonce]])`.
     * ///   3. `[]` AMM open_orders Account
     * ///   4. `[writable]` AMM target orders Account. To store plan orders infomations.
     * ///   5. `[writable]` AMM lp mint Account. Owned by $authority.
     * ///   6. `[writable]` AMM coin vault $authority can transfer amount,
     * ///   7. `[writable]` AMM pc vault $authority can transfer amount,
     * ///   8. `[]` Market Account. Market program is the owner.
     * ///   9. `[writable]` User coin token Account to deposit into.
     * ///   10. `[writable]` User pc token Account to deposit into.
     * ///   11. `[writable]` User lp token. To deposit the generated tokens, user is the owner.
     * ///   12. '[signer]` User wallet Account
     * ///   13. `[]` Market event queue Account.
     * Deposit(DepositInstruction),
     * pub struct DepositInstruction {
     * /// Pool token amount to transfer. token_a and token_b amount are set by
     * /// the current exchange rate and size of the pool
     * pub max_coin_amount: u64,
     * pub max_pc_amount: u64,
     * pub base_side: u64,
     * pub other_amount_min: Option<u64>,
     * }
     */
    private static Map<String, Object> parseDeposit(ByteBuffer buffer, String[] accounts) {
        if (accounts.length < 12) {
            throw new IllegalArgumentException("Deposit instruction requires at least 12 accounts");
        }

        Map<String, Object> info = new HashMap<>();
        String maxCoinAmount = Long.toUnsignedString(buffer.getLong());    // 最大代币A数量
        String maxPcAmount = Long.toUnsignedString(buffer.getLong());      // 最大代币B数量
        String baseSide = Long.toUnsignedString(buffer.getLong());           // 基础LP代币数量
        if (buffer.limit() - buffer.position() >= 8) {
            String otherAmountMin = Long.toUnsignedString(buffer.getLong());     // 读取 other_amount_min (8字节，可能为0，表示Option<u64>)
            info.put("otherAmountMin", otherAmountMin);
        }
        // 设置返回信息
        info.put("maxCoinAmount", maxCoinAmount);
        info.put("maxPcAmount", maxPcAmount);
        info.put("baseSide", baseSide);

        info.put("splTokenProgramId", accounts[0]); // Spl Token program id
        info.put("ammAccount", accounts[1]); // AMM Account
        info.put("ammAuthority", accounts[2]); // $authority
        info.put("ammOpenOrdersAccount", accounts[3]); // AMM open orders Account
        info.put("ammTargetOrdersAccount", accounts[4]); // AMM target orders Account
        info.put("ammLpMintAccount", accounts[5]); // AMM lp mint Account
        info.put("ammCoinVaultAccount", accounts[6]); // AMM coin vault Account
        info.put("ammPcVaultAccount", accounts[7]); // AMM pc vault Account
        info.put("marketAccount", accounts[8]); // Market Account
        info.put("userCoinTokenAccount", accounts[9]); // User coin token Account to deposit into
        info.put("userPcTokenAccount", accounts[10]); // User pc token Account to deposit into
        info.put("userLpTokenAccount", accounts[11]); // User lp token Account
        info.put("userWalletAccount", accounts[12]); // User wallet Account (signer)
        info.put("marketEventQueueAccount", accounts[13]); // Market event queue Account
        return info;
    }

    /**
     * 解析提现指令
     * ///   Withdraw the vault tokens from the pool at the current ratio.
     * ///
     * ///   0. `[]` Spl Token program id
     * ///   1. `[writable]` AMM Account
     * ///   2. `[]` $authority derived from `create_program_address(&[AUTHORITY_AMM, &[nonce]])`.
     * ///   3. `[writable]` AMM open orders Account
     * ///   4. `[writable]` AMM target orders Account
     * ///   5. `[writable]` AMM lp mint Account. Owned by $authority.
     * ///   6. `[writable]` AMM coin vault Account to withdraw FROM,
     * ///   7. `[writable]` AMM pc vault Account to withdraw FROM,
     * ///   8. `[]` Market program id
     * ///   9. `[writable]` Market Account. Market program is the owner.
     * ///   10. `[writable]` Market coin vault Account
     * ///   11. `[writable]` Market pc vault Account
     * ///   12. '[]` Market vault signer Account
     * ///   13. `[writable]` User lp token Account.
     * ///   14. `[writable]` User token coin Account. user Account to credit.
     * ///   15. `[writable]` User token pc Account. user Account to credit.
     * ///   16. `[signer]` User wallet Account
     * ///   17. `[writable]` Market event queue Account
     * ///   18. `[writable]` Market bids Account
     * ///   19. `[writable]` Market asks Account
     * Withdraw(WithdrawInstruction),
     * pub struct WithdrawInstruction {
     * /// Pool token amount to transfer. token_a and token_b amount are set by
     * /// the current exchange rate and size of the pool
     * pub amount: u64,
     * pub min_coin_amount: Option<u64>,
     * pub min_pc_amount: Option<u64>,
     * }
     */
    private static Map<String, Object> parseWithdraw(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 1. 读取 amount (8字节)
        String amount = Long.toUnsignedString(buffer.getLong());
        info.put("amount", amount);

//        // 2. 读取 min_coin_amount (8字节，表示 Option<u64>)
//        if (buffer.limit() - buffer.position() >= 8) {
//            String minCoinAmount = Long.toUnsignedString(buffer.getLong()); // 这里假设 Option<u64> 是一个 8 字节的值
//            String minPcAmount = Long.toUnsignedString(buffer.getLong()); // 这里假设 Option<u64> 是一个 8 字节的值
//            info.put("minCoinAmount", minCoinAmount);
//            info.put("minPcAmount", minPcAmount);
//        }
        // 设置返回信息

        // 解析账户信息
        info.put("splTokenProgramId", accounts[0]); // Spl Token program id
        info.put("ammAccount", accounts[1]); // AMM Account
        info.put("authority", accounts[2]); // $authority
        info.put("ammOpenOrdersAccount", accounts[3]); // AMM open orders Account
        info.put("ammTargetOrdersAccount", accounts[4]); // AMM target orders Account
        info.put("ammLpMintAccount", accounts[5]); // AMM lp mint Account
        info.put("ammCoinVaultAccount", accounts[6]); // AMM coin vault Account
        info.put("ammPcVaultAccount", accounts[7]); // AMM pc vault Account
        info.put("marketProgramId", accounts[8]); // Market program id
        info.put("marketAccount", accounts[9]); // Market Account
        info.put("marketCoinVaultAccount", accounts[10]); // Market coin vault Account
        info.put("marketPcVaultAccount", accounts[11]); // Market pc vault Account
        info.put("marketVaultSignerAccount", accounts[12]); // Market vault signer Account
        info.put("userLpTokenAccount", accounts[13]); // User lp token Account
        info.put("userCoinTokenAccount", accounts[14]); // User coin token Account
        info.put("userPcTokenAccount", accounts[15]); // User pc token Account
        info.put("userWalletAccount", accounts[16]); // User wallet Account (signer)
        info.put("marketEventQueueAccount", accounts[17]); // Market event queue Account
        info.put("marketBidsAccount", accounts[18]); // Market bids Account
        info.put("marketAsksAccount", accounts[19]); // Market asks Account
        return info;
    }

    /**
     * 解析提取收益指令
     * ///   Withdraw Pnl from pool by protocol
     * ///
     * ///   0. `[]` Spl Token program id
     * ///   1. `[writable]` AMM Account
     * ///   2. `[]` AMM config Account, derived from `find_program_address(&[&&AMM_CONFIG_SEED])`.
     * ///   3. `[]` $authority derived from `create_program_address(&[AUTHORITY_AMM, &[nonce]])`.
     * ///   4. `[writable]` AMM open orders Account
     * ///   5. `[writable]` AMM coin vault account to withdraw FROM,
     * ///   6. `[writable]` AMM pc vault account to withdraw FROM,
     * ///   7. `[writable]` User coin token Account to withdraw to
     * ///   8. `[writable]` User pc token Account to withdraw to
     * ///   9. `[signer]` User wallet account
     * ///   10. `[writable]` AMM target orders Account
     * ///   11. `[]` Market program id
     * ///   12. `[writable]` Market Account. Market program is the owner.
     * ///   13. `[writable]` Market event queue Account
     * ///   14. `[writable]` Market coin vault Account
     * ///   15. `[writable]` Market pc vault Account
     * ///   16. '[]` Market vault signer Account
     * ///   17. `[]` (optional) the referrer pc account used for settle back referrer
     * WithdrawPnl,
     */
    private static Map<String, Object> parseWithdrawPnl(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 解析账户信息
        info.put("splTokenProgramId", accounts[0]); // Spl Token program id
        info.put("ammAccount", accounts[1]); // AMM Account
        info.put("ammConfigAccount", accounts[2]); // AMM config Account
        info.put("ammAuthority", accounts[3]); // $authority
        info.put("ammOpenOrdersAccount", accounts[4]); // AMM open orders Account
        info.put("ammCoinVaultAccount", accounts[5]); // AMM coin vault Account
        info.put("ammPcVaultAccount", accounts[6]); // AMM pc vault Account
        info.put("userCoinTokenAccount", accounts[7]); // User coin token Account to withdraw to
        info.put("userPcTokenAccount", accounts[8]); // User pc token Account to withdraw to
        info.put("userWalletAccount", accounts[9]); // User wallet Account (signer)
        info.put("ammTargetOrdersAccount", accounts[10]); // AMM target orders Account
        info.put("marketProgramId", accounts[11]); // Market program id
        info.put("marketAccount", accounts[12]); // Market Account
        info.put("marketEventQueueAccount", accounts[13]); // Market event queue Account
        info.put("marketCoinVaultAccount", accounts[14]); // Market coin vault Account
        info.put("marketPcVaultAccount", accounts[15]); // Market pc vault Account
        info.put("marketVaultSignerAccount", accounts[16]); // Market vault signer Account
        if (accounts.length == 18)
            info.put("referrerPcAccount", accounts[17]); // (optional) the referrer pc account used for settle back referrer
        return info;
    }

    /**
     * 解析提取SRM代币指令
     * ///   Withdraw (M)SRM from the (M)SRM Account used for fee discounts by admin
     * ///
     * ///   0. `[]` Spl Token program id
     * ///   1. `[]` AMM Account.
     * ///   2. `[signer]` Admin wallet Account
     * ///   3. `[]` $authority derived from `create_program_address(&[AUTHORITY_AMM, &[nonce]])`.
     * ///   4. `[writable]` the (M)SRM Account withdraw from
     * ///   5. `[writable]` the (M)SRM Account withdraw to
     * WithdrawSrm(WithdrawSrmInstruction),
     * pub struct WithdrawSrmInstruction {
     * pub amount: u64,
     * }
     */
    private static Map<String, Object> parseWithdrawSrm(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        info.put("amount", Long.toUnsignedString(buffer.getLong()));

        // 解析账户信息
        info.put("splTokenProgramId", accounts[0]); // Spl Token program id
        info.put("ammAccount", accounts[1]); // AMM Account
        info.put("adminWalletAccount", accounts[2]); // Admin wallet Account (signer)
        info.put("ammAuthority", accounts[3]); // $authority
        info.put("srmAccountFrom", accounts[4]); // (M)SRM Account withdraw from
        info.put("srmAccountTo", accounts[5]); // (M)SRM Account withdraw to
        return info;
    }

    /**
     * 解析创建配置账户指令
     * <p>
     * /// Create amm config account by admin
     * CreateConfigAccount,
     */
    private static Map<String, Object> parseCreateConfigAccount(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 设置返回信息
        info.put("ammConfigAccount", accounts[0]); // AMM config Account
        info.put("ammAuthority", accounts[1]); // $authority (admin)
        return info;
    }

    /**
     * 解析更新配置账户指令
     * <p>
     * /// Update amm config account by admin
     * UpdateConfigAccount(ConfigArgs),
     * pub struct ConfigArgs {
     * pub param: u8,
     * pub owner: Option<Pubkey>,
     * pub create_pool_fee: Option<u64>,
     * }
     */
    private static Map<String, Object> parseUpdateConfigAccount(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        // 1. 读取 param (1字节)
        int param = Byte.toUnsignedInt(buffer.get());
        info.put("param", param);

        // 2. 读取 owner (32字节，Option<Pubkey>)
        if (buffer.limit() >= 42) {
            byte[] ownerBytes = new byte[32];
            buffer.get(ownerBytes);
            String owner = Base58.encode(ownerBytes); // 将 Pubkey 转换为 Base58 字符串
            info.put("owner", owner);
        }
        if (buffer.limit() == 10 || buffer.limit() > 42) {
            // 3. 读取 create_pool_fee (8字节，Option<u64>)
            String createPoolFee = Long.toUnsignedString(buffer.getLong()); // 这里假设 Option<u64> 是一个 8 字节的值
            info.put("createPoolFee", createPoolFee);
        }


        // 解析账户信息
        info.put("ammConfigAccount", accounts[0]); // AMM config Account
        info.put("ammAuthority", accounts[1]); // $authority (admin)
        return info;
    }

    /**
     * 解析设置参数指令
     * ///   Set AMM params
     *     ///
     *     ///   0. `[]` Spl Token program id
     *     ///   1. `[writable]` AMM Account.
     *     ///   2. `[]` $authority derived from `create_program_address(&[AUTHORITY_AMM, &[nonce]])`.
     *     ///   3. `[writable]` AMM open orders Account
     *     ///   4. `[writable]` AMM target orders Account
     *     ///   5. `[writable]` AMM coin vault account owned by $authority,
     *     ///   6. `[writable]` AMM pc vault account owned by $authority,
     *     ///   7. `[]` Market program id
     *     ///   8. `[writable]` Market Account. Market program is the owner.
     *     ///   9. `[writable]` Market coin vault Account
     *     ///   10. `[writable]` Market pc vault Account
     *     ///   11. '[]` Market vault signer Account
     *     ///   12. `[writable]` Market event queue Account
     *     ///   13. `[writable]` Market bids Account
     *     ///   14. `[writable]` Market asks Account
     *     ///   15. `[signer]` Admin Account
     *     ///   16. `[]` (optional) New AMM open orders Account to replace old AMM open orders Account
     *     SetParams(SetParamsInstruction),
     *     pub struct SetParamsInstruction {
     *          pub param: u8,
     *          pub value: Option<u64>,
     *          pub new_pubkey: Option<Pubkey>,
     *          pub fees: Option<Fees>,
     *          pub last_order_distance: Option<LastOrderDistance>,
     *      }
     */
    private static Map<String, Object> parseSetParams(ByteBuffer buffer, String[] accounts) {

        Map<String, Object> info = new HashMap<>();
        info.put("param", Byte.toUnsignedInt(buffer.get()));

        // 解析账户信息
//        info.put("splTokenProgramId", accounts[0]); // Spl Token program id
//        info.put("ammAccount", accounts[1]); // AMM Account
//        info.put("ammAuthority", accounts[2]); // $authority
//        info.put("ammOpenOrdersAccount", accounts[3]); // AMM open orders Account
//        info.put("ammTargetOrdersAccount", accounts[4]); // AMM target orders Account
//        info.put("ammCoinVaultAccount", accounts[5]); // AMM coin vault Account
//        info.put("ammPcVaultAccount", accounts[6]); // AMM pc vault Account
//        info.put("marketProgramId", accounts[7]); // Market program id
//        info.put("marketAccount", accounts[8]); // Market Account
//        info.put("marketCoinVaultAccount", accounts[9]); // Market coin vault Account
//        info.put("marketPcVaultAccount", accounts[10]); // Market pc vault Account
//        info.put("marketVaultSignerAccount", accounts[11]); // Market vault signer Account
//        info.put("marketEventQueueAccount", accounts[12]); // Market event queue Account
//        info.put("marketBidsAccount", accounts[13]); // Market bids Account
//        info.put("marketAsksAccount", accounts[14]); // Market asks Account
//        info.put("adminAccount", accounts[15]); // Admin Account (signer)
//        if (accounts.length == 17) info.put("newAmmOpenOrdersAccount", accounts[16]); // (optional) New AMM open orders Account
        return info;
    }

    /**
     * 解析监控步骤指令
     * MonitorStep. To monitor place Amm order state machine turn around step by step.
     * <p>
     * 0. `[]` Spl Token program id
     * 1. `[]` Rent program id
     * 2. `[]` Sys Clock id
     * 3. `[writable]` AMM Account
     * 4. `[]` $authority derived from `create_program_address(&[AUTHORITY_AMM, &[nonce]])`.
     * 5. `[writable]` AMM open orders Account
     * 6. `[writable]` AMM target orders Account. To store plan orders infomations.
     * 7. `[writable]` AMM coin vault Account. Must be non zero, owned by $authority.
     * 8. `[writable]` AMM pc vault Account. Must be non zero, owned by $authority.
     * 9. `[]` Market program id
     * 10. `[writable]` Market Account. Market program is the owner.
     * 11. `[writable]` Market coin vault Account
     * 12. `[writable]` Market pc vault Account
     * 13. '[]` Market vault signer Account
     * 14. '[writable]` Market request queue Account
     * 15. `[writable]` Market event queue Account
     * 16. `[writable]` Market bids Account
     * 17. `[writable]` Market asks Account
     * 18. `[writable]` (optional) the (M)SRM account used for fee discounts
     * 19. `[writable]` (optional) the referrer pc account used for settle back referrer
     * MonitorStep(MonitorStepInstruction),
     * pub struct MonitorStepInstruction {
     * /// max value of plan/new/cancel orders
     * pub plan_order_limit: u16,
     * pub place_order_limit: u16,
     * pub cancel_order_limit: u16,
     * }
     */

    private static Map<String, Object> parseMonitorStep(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        info.put("planOrderLimit", Short.toUnsignedInt(buffer.getShort()));
        info.put("placeOrderLimit", Short.toUnsignedInt(buffer.getShort()));
        info.put("cancelOrderLimit", Short.toUnsignedInt(buffer.getShort()));

        // 解析账户信息
        info.put("splTokenProgramId", accounts[0]); // Spl Token program id
        info.put("rentProgramId", accounts[1]); // Rent program id
        info.put("sysClockId", accounts[2]); // Sys Clock id
        info.put("ammAccount", accounts[3]); // AMM Account
        info.put("authority", accounts[4]); // $authority
        info.put("ammOpenOrdersAccount", accounts[5]); // AMM open orders Account
        info.put("ammTargetOrdersAccount", accounts[6]); // AMM target orders Account
        info.put("ammCoinVaultAccount", accounts[7]); // AMM coin vault Account
        info.put("ammPcVaultAccount", accounts[8]); // AMM pc vault Account
        info.put("marketProgramId", accounts[9]); // Market program id
        info.put("marketAccount", accounts[10]); // Market Account
        info.put("marketCoinVaultAccount", accounts[11]); // Market coin vault Account
        info.put("marketPcVaultAccount", accounts[12]); // Market pc vault Account
        info.put("marketVaultSignerAccount", accounts[13]); // Market vault signer Account
        info.put("marketRequestQueueAccount", accounts[14]); // Market request queue Account
        info.put("marketEventQueueAccount", accounts[15]); // Market event queue Account
        info.put("marketBidsAccount", accounts[16]); // Market bids Account
        info.put("marketAsksAccount", accounts[17]); // Market asks Account
        info.put("srmAccount", accounts[18]); // (optional) the (M)SRM account used for fee discounts
        info.put("referrerPcAccount", accounts[19]); // (optional) the referrer pc account used for settle back referrer

        return info;
    }

    /**
     * 解析模拟信息指令
     * <p>
     * SimulateInfo(SimulateInstruction),
     * pub struct SimulateInstruction {
     * pub param: u8,
     * pub swap_base_in_value: Option<SwapInstructionBaseIn>,
     * pub swap_base_out_value: Option<SwapInstructionBaseOut>,
     * }
     */
    private static Map<String, Object> parseSimulateInfo(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        // 1. 读取 param (1字节)
        byte param = buffer.get();


        // 设置返回信息
        info.put("param", param);

        return info;
    }

    /**
     * 解析迁移到OpenBook指令
     *
     * ///   Migrate the associated market from Serum to OpenBook.
     *     ///
     *     ///   0. `[]` Spl Token program id
     *     ///   1. `[]` Sys program id
     *     ///   2. `[]` Rent program id
     *     ///   3. `[writable]` AMM Account
     *     ///   4. `[]` $authority derived from `create_program_address(&[AUTHORITY_AMM, &[nonce]])`.
     *     ///   5. `[writable]` AMM open orders Account
     *     ///   6. `[writable]` AMM coin vault account owned by $authority,
     *     ///   7. `[writable]` AMM pc vault account owned by $authority,
     *     ///   8. `[writable]` AMM target orders Account
     *     ///   9. `[]` Market program id
     *     ///   10. `[writable]` Market Account. Market program is the owner.
     *     ///   11. `[writable]` Market bids Account
     *     ///   12. `[writable]` Market asks Account
     *     ///   13. `[writable]` Market event queue Account
     *     ///   14. `[writable]` Market coin vault Account
     *     ///   15. `[writable]` Market pc vault Account
     *     ///   16. '[]` Market vault signer Account
     *     ///   17. '[writable]` AMM new open orders Account
     *     ///   18. '[]` mew Market program id
     *     ///   19. '[]` new Market market Account
     *     ///   20. '[]` Admin Account
     *     MigrateToOpenBook,
     */
    private static Map<String, Object> parseMigrateToOpenBook(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        return info;
    }

    /**
     * 解析管理员取消订单指令
     * <p>
     * AdminCancelOrders(AdminCancelOrdersInstruction),
     *      pub struct AdminCancelOrdersInstruction {
     *          pub limit: u16,
     *      }
     */
    private static Map<String, Object> parseAdminCancelOrders(ByteBuffer buffer, String[] accounts) {
        if (accounts.length < 8) {
            throw new IllegalArgumentException("AdminCancelOrders instruction requires at least 8 accounts");
        }

        Map<String, Object> info = new HashMap<>();
        // 1. 读取 limit (2字节)
        int limit = buffer.getShort() & 0xFFFF; // 转换为无符号值
        info.put("limit", limit);

        // 解析账户信息
        info.put("adminAccount", accounts[0]); // Admin Account (signer)
        info.put("ammAccount", accounts[1]); // AMM Account
        info.put("ammOpenOrdersAccount", accounts[2]); // AMM open orders Account
        info.put("marketAccount", accounts[3]); // Market Account
        info.put("marketBidsAccount", accounts[4]); // Market bids Account
        info.put("marketAsksAccount", accounts[5]); // Market asks Account
        info.put("marketEventQueueAccount", accounts[6]); // Market event queue Account
        return info;
    }


}