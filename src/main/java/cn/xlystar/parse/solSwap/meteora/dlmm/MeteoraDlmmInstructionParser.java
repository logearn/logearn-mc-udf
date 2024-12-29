package cn.xlystar.parse.solSwap.meteora.dlmm;

import cn.xlystar.parse.solSwap.InstructionParser;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.math.BigInteger;
import org.bouncycastle.util.encoders.Hex;


public class MeteoraDlmmInstructionParser extends InstructionParser {

    private static final String PROGRAM_ID = "LBUZKhRxPF3XUpBCjp4YzTKgLccjZhTSDM9YuVaPwxo";

    @Override
    public String getMethodId(ByteBuffer buffer) {
        byte[] methodIdByte = new byte[8];
        buffer.get(methodIdByte);
        return Hex.toHexString(methodIdByte);
    }

    @Override
    public Map<String, Object> matchInstruction(String methodId, ByteBuffer buffer, String[] accounts) {
        try {
            Map<String, Object> info;
            switch (MeteoraDlmmInstruction.fromValue(methodId)) {
                case INITIALIZE_LB_PAIR:
                    info = parseInitializeLbPair(buffer, accounts);
                    break;
                case INITIALIZE_REWARD:
                    info = parseInitializeReward(buffer, accounts);
                    break;
                case ADD_LIQUIDITY:
                    info = parseAddLiquidity(buffer, accounts);
                    break;
                case ADD_LIQUIDITY_BY_WEIGHT:
                    info = parseAddLiquidityByWeight(buffer, accounts);
                    break;
                case ADD_LIQUIDITY_ONE_SIDE:
                    info = parseAddLiquidityOneSide(buffer, accounts);
                    break;
                case INITIALIZE_POSITION:
                    info = parseInitializePosition(buffer, accounts);
                    break;
                case SWAP:
                    info = parseSwap(buffer, accounts);
                    break;
                case WITHDRAW_PROTOCOL_FEE:
                    info = parseWithdrawProtocolFee(buffer, accounts);
                    break;
                case FUND_REWARD:
                    info = parseFundReward(buffer, accounts);
                    break;
                case UPDATE_REWARD_FUNDER:
                    info = parseUpdateRewardFunder(buffer, accounts);
                    break;
                case UPDATE_REWARD_DURATION:
                    info = parseUpdateRewardDuration(buffer, accounts);
                    break;
                case CLAIM_REWARD:
                    info = parseClaimReward(buffer, accounts);
                    break;
                case CLAIM_FEE:
                    info = parseClaimFee(buffer, accounts);
                    break;
                case CLOSE_POSITION:
                    info = parseClosePosition(buffer, accounts);
                    break;
                case REMOVE_LIQUIDITY:
                    info = parseRemoveLiquidity(buffer, accounts);
                    break;
                case REMOVE_LIQUIDITY_BY_RANGE:
                    info = parseRemoveLiquidityByRange(buffer, accounts);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown instruction type: " + methodId);
            }
            return info;
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse instruction: " + methodId, e);
        }
    }

    private static Map<String, Object> parseInitializeLbPair(ByteBuffer buffer, String[] accounts) {
        if (accounts.length < 14) {
            throw new IllegalArgumentException("Not enough accounts");
        }
        
        Map<String, Object> info = new HashMap<>();
        
        // 参数解析
        info.put("active_id", buffer.getInt());
        info.put("bin_step", buffer.getShort()); // 修改为getShort因为u16类型
        
        // 账户信���
        info.put("lb_pair", accounts[0]);
        info.put("bin_array_bitmap_extension", accounts[1]);
        info.put("token_mint_x", accounts[2]);
        info.put("token_mint_y", accounts[3]);
        info.put("reserve_x", accounts[4]);
        info.put("reserve_y", accounts[5]);
        info.put("oracle", accounts[6]);
        info.put("preset_parameter", accounts[7]);
        info.put("funder", accounts[8]);
        info.put("token_program", accounts[9]);
        info.put("system_program", accounts[10]);
        info.put("rent", accounts[11]);
        info.put("event_authority", accounts[12]);
        info.put("program", accounts[13]);
        
        return info;
    }

    private static Map<String, Object> parseInitializeReward(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        // 解析参数
        info.put("reward_index", buffer.get());
        info.put("open_time", Long.toUnsignedString(buffer.getLong()));
        info.put("end_time", Long.toUnsignedString(buffer.getLong()));
        String low = Long.toUnsignedString(buffer.getLong());
        String high = Long.toUnsignedString(buffer.getLong());
        BigInteger emissionsPerSecondX64 = new BigInteger(high).shiftLeft(64).or(new BigInteger(low));
        info.put("emissions_per_second_x64", emissionsPerSecondX64);

        // 账户信息
        info.put("reward_funder", accounts[0]);
        info.put("lb_pair", accounts[1]);
        info.put("reward_token_mint", accounts[2]);
        info.put("reward_token_vault", accounts[3]);
        info.put("token_program", accounts[4]);
        info.put("system_program", accounts[5]);
        info.put("rent", accounts[6]);
        return info;
    }

    private static Map<String, Object> parseAddLiquidity(ByteBuffer buffer, String[] accounts) {
        if (accounts.length < 16) {
            throw new IllegalArgumentException("Not enough accounts");
        }
        
        Map<String, Object> info = new HashMap<>();
        
        // 参数解析 - LiquidityParameter结构
        info.put("amount_x", Long.toUnsignedString(buffer.getLong()));
        info.put("amount_y", Long.toUnsignedString(buffer.getLong()));
        info.put("active_id", buffer.getInt());
        
        // 账户信息
        info.put("position", accounts[0]);
        info.put("lb_pair", accounts[1]);
        info.put("bin_array_bitmap_extension", accounts[2]);
        info.put("user_token_x", accounts[3]);
        info.put("user_token_y", accounts[4]);
        info.put("reserve_x", accounts[5]);
        info.put("reserve_y", accounts[6]);
        info.put("token_x_mint", accounts[7]);
        info.put("token_y_mint", accounts[8]);
        info.put("bin_array_lower", accounts[9]);
        info.put("bin_array_upper", accounts[10]);
        info.put("sender", accounts[11]);
        info.put("token_x_program", accounts[12]);
        info.put("token_y_program", accounts[13]);
        info.put("event_authority", accounts[14]);
        info.put("program", accounts[15]);
        
        return info;
    }

    private static Map<String, Object> parseAddLiquidityByWeight(ByteBuffer buffer, String[] accounts) {
        if (accounts.length < 16) {
            throw new IllegalArgumentException("Not enough accounts");
        }
        
        Map<String, Object> info = new HashMap<>();
        
        // 参数解析 - LiquidityParameterByWeight结构
        info.put("amount_x", Long.toUnsignedString(buffer.getLong()));
        info.put("amount_y", Long.toUnsignedString(buffer.getLong()));
        info.put("active_id", buffer.getInt());
        info.put("weights", buffer.getInt());
        
        // 账户信息
        info.put("position", accounts[0]);
        info.put("lb_pair", accounts[1]);
        info.put("bin_array_bitmap_extension", accounts[2]);
        info.put("user_token_x", accounts[3]);
        info.put("user_token_y", accounts[4]);
        info.put("reserve_x", accounts[5]);
        info.put("reserve_y", accounts[6]);
        info.put("token_x_mint", accounts[7]);
        info.put("token_y_mint", accounts[8]);
        info.put("bin_array_lower", accounts[9]);
        info.put("bin_array_upper", accounts[10]);
        info.put("sender", accounts[11]);
        info.put("token_x_program", accounts[12]);
        info.put("token_y_program", accounts[13]);
        info.put("event_authority", accounts[14]);
        info.put("program", accounts[15]);
        
        return info;
    }

    private static Map<String, Object> parseAddLiquidityOneSide(ByteBuffer buffer, String[] accounts) {
        if (accounts.length < 12) {
            throw new IllegalArgumentException("Not enough accounts");
        }
        
        Map<String, Object> info = new HashMap<>();
        
        // 参数解析 - LiquidityOneSideParameter结构
        info.put("amount", Long.toUnsignedString(buffer.getLong()));
        info.put("active_id", buffer.getInt());
        info.put("side", buffer.get()); // 0: X, 1: Y
        
        // 账户信息
        info.put("position", accounts[0]);
        info.put("lb_pair", accounts[1]);
        info.put("bin_array_bitmap_extension", accounts[2]);
        info.put("user_token", accounts[3]);
        info.put("reserve", accounts[4]);
        info.put("token_mint", accounts[5]);
        info.put("bin_array_lower", accounts[6]);
        info.put("bin_array_upper", accounts[7]);
        info.put("sender", accounts[8]);
        info.put("token_program", accounts[9]);
        info.put("event_authority", accounts[10]);
        info.put("program", accounts[11]);
        
        return info;
    }

    private static Map<String, Object> parseInitializePosition(ByteBuffer buffer, String[] accounts) {
        if (accounts.length < 8) {
            throw new IllegalArgumentException("Not enough accounts");
        }
        
        Map<String, Object> info = new HashMap<>();
        
        // 参数解析
        info.put("lower_bin_id", buffer.getInt());
        info.put("width", buffer.getInt());
        
        // 账户信息
        info.put("payer", accounts[0]);
        info.put("position", accounts[1]);
        info.put("lb_pair", accounts[2]);
        info.put("owner", accounts[3]);
        info.put("system_program", accounts[4]);
        info.put("rent", accounts[5]);
        info.put("event_authority", accounts[6]);
        info.put("program", accounts[7]);
        
        return info;
    }

    private static Map<String, Object> parseSwap(ByteBuffer buffer, String[] accounts) {
        if (accounts.length < 13) {
            throw new IllegalArgumentException("Not enough accounts");
        }
        
        Map<String, Object> info = new HashMap<>();
        
        // 参数解析
        info.put("amount_in", Long.toUnsignedString(buffer.getLong()));
        info.put("min_amount_out", Long.toUnsignedString(buffer.getLong()));

        // 账户信息
        info.put("lb_pair", accounts[0]);
        info.put("bin_array_bitmap_extension", accounts[1]);
        info.put("user_source_token", accounts[2]);
        info.put("user_destination_token", accounts[3]);
        info.put("source_reserve", accounts[4]);
        info.put("destination_reserve", accounts[5]);
        info.put("source_token_mint", accounts[6]);
        info.put("destination_token_mint", accounts[7]);
        info.put("oracle", accounts[8]);
        info.put("host_fee_account", accounts[9]);
        info.put("token_program", accounts[10]);
        info.put("event_authority", accounts[11]);
        info.put("program", accounts[12]);
        
        return info;
    }

    private static Map<String, Object> parseWithdrawProtocolFee(ByteBuffer buffer, String[] accounts) {
        if (accounts.length < 9) {
            throw new IllegalArgumentException("Not enough accounts");
        }
        
        Map<String, Object> info = new HashMap<>();
        
        // 参数解析
        info.put("amount_x", Long.toUnsignedString(buffer.getLong()));
        info.put("amount_y", Long.toUnsignedString(buffer.getLong()));
        
        // 账户信息
        info.put("admin", accounts[0]);
        info.put("lb_pair", accounts[1]);
        info.put("reserve_x", accounts[2]);
        info.put("reserve_y", accounts[3]);
        info.put("fee_receiver_x", accounts[4]);
        info.put("fee_receiver_y", accounts[5]);
        info.put("token_program", accounts[6]);
        info.put("event_authority", accounts[7]);
        info.put("program", accounts[8]);
        
        return info;
    }

    private static Map<String, Object> parseFundReward(ByteBuffer buffer, String[] accounts) {
        if (accounts.length < 7) {
            throw new IllegalArgumentException("Not enough accounts");
        }
        
        Map<String, Object> info = new HashMap<>();
        
        // 参数解析
        info.put("reward_index", buffer.get());  // u8类型
        info.put("amount", Long.toUnsignedString(buffer.getLong()));
        
        // 账户信息
        info.put("funder", accounts[0]);
        info.put("lb_pair", accounts[1]);
        info.put("funder_token_account", accounts[2]);
        info.put("reward_vault", accounts[3]);
        info.put("token_program", accounts[4]);
        info.put("event_authority", accounts[5]);
        info.put("program", accounts[6]);
        
        return info;
    }

    private static Map<String, Object> parseUpdateRewardFunder(ByteBuffer buffer, String[] accounts) {
        if (accounts.length < 5) {
            throw new IllegalArgumentException("Not enough accounts");
        }
        
        Map<String, Object> info = new HashMap<>();
        
        // 参数解析
        info.put("reward_index", buffer.get());  // u8类型
        
        // 账户信息
        info.put("admin", accounts[0]);
        info.put("lb_pair", accounts[1]);
        info.put("new_funder", accounts[2]);
        info.put("event_authority", accounts[3]);
        info.put("program", accounts[4]);
        
        return info;
    }

    private static Map<String, Object> parseUpdateRewardDuration(ByteBuffer buffer, String[] accounts) {
        if (accounts.length < 4) {
            throw new IllegalArgumentException("Not enough accounts");
        }
        
        Map<String, Object> info = new HashMap<>();
        
        // 参数解析
        info.put("reward_index", buffer.get());  // u8类型
        info.put("new_duration", Long.toUnsignedString(buffer.getLong()));
        
        // 账户信息
        info.put("admin", accounts[0]);
        info.put("lb_pair", accounts[1]);
        info.put("event_authority", accounts[2]);
        info.put("program", accounts[3]);
        
        return info;
    }

    private static Map<String, Object> parseClaimReward(ByteBuffer buffer, String[] accounts) {
        if (accounts.length < 10) {
            throw new IllegalArgumentException("Not enough accounts");
        }
        
        Map<String, Object> info = new HashMap<>();
        
        // 参数解析
        info.put("reward_index", buffer.get());  // u8类型
        
        // 账户信息
        info.put("position", accounts[0]);
        info.put("lb_pair", accounts[1]);
        info.put("bin_array_lower", accounts[2]);
        info.put("bin_array_upper", accounts[3]);
        info.put("owner", accounts[4]);
        info.put("reward_vault", accounts[5]);
        info.put("user_reward_token", accounts[6]);
        info.put("token_program", accounts[7]);
        info.put("event_authority", accounts[8]);
        info.put("program", accounts[9]);
        
        return info;
    }

    private static Map<String, Object> parseClaimFee(ByteBuffer buffer, String[] accounts) {
        if (accounts.length < 13) {
            throw new IllegalArgumentException("Not enough accounts");
        }
        
        Map<String, Object> info = new HashMap<>();
        
        // 账户信息
        info.put("position", accounts[0]);
        info.put("lb_pair", accounts[1]);
        info.put("bin_array_lower", accounts[2]);
        info.put("bin_array_upper", accounts[3]);
        info.put("owner", accounts[4]);
        info.put("reserve_x", accounts[5]);
        info.put("reserve_y", accounts[6]);
        info.put("user_token_x", accounts[7]);
        info.put("user_token_y", accounts[8]);
        info.put("token_x_program", accounts[9]);
        info.put("token_y_program", accounts[10]);
        info.put("event_authority", accounts[11]);
        info.put("program", accounts[12]);
        
        return info;
    }

    private static Map<String, Object> parseClosePosition(ByteBuffer buffer, String[] accounts) {
        if (accounts.length < 9) {
            throw new IllegalArgumentException("Not enough accounts");
        }
        
        Map<String, Object> info = new HashMap<>();
        
        // 账户信息
        info.put("position", accounts[0]);
        info.put("lb_pair", accounts[1]);
        info.put("bin_array_lower", accounts[2]);
        info.put("bin_array_upper", accounts[3]);
        info.put("owner", accounts[4]);
        info.put("rent_recipient", accounts[5]);
        info.put("system_program", accounts[6]);
        info.put("event_authority", accounts[7]);
        info.put("program", accounts[8]);
        
        return info;
    }

    private static Map<String, Object> parseRemoveLiquidity(ByteBuffer buffer, String[] accounts) {
        if (accounts.length < 14) {
            throw new IllegalArgumentException("Not enough accounts");
        }
        
        Map<String, Object> info = new HashMap<>();
        
        // 参数解析
        info.put("bin_liquidity_removal", Long.toUnsignedString(buffer.getLong()));
        
        // 账户信息
        info.put("position", accounts[0]);
        info.put("lb_pair", accounts[1]);
        info.put("bin_array_bitmap_extension", accounts[2]);
        info.put("user_token_x", accounts[3]);
        info.put("user_token_y", accounts[4]);
        info.put("reserve_x", accounts[5]);
        info.put("reserve_y", accounts[6]);
        info.put("bin_array_lower", accounts[7]);
        info.put("bin_array_upper", accounts[8]);
        info.put("owner", accounts[9]);
        info.put("token_x_program", accounts[10]);
        info.put("token_y_program", accounts[11]);
        info.put("event_authority", accounts[12]);
        info.put("program", accounts[13]);
        
        return info;
    }

    private static Map<String, Object> parseRemoveLiquidityByRange(ByteBuffer buffer, String[] accounts) {
        if (accounts.length < 13) {
            throw new IllegalArgumentException("Not enough accounts");
        }
        
        Map<String, Object> info = new HashMap<>();
        
        // 参数解析
        info.put("bin_array_index", buffer.getInt());
        info.put("start_bin", buffer.getInt());
        info.put("end_bin", buffer.getInt());
        
        // 账户信息
        info.put("position", accounts[0]);
        info.put("lb_pair", accounts[1]);
        info.put("bin_array_bitmap_extension", accounts[2]);
        info.put("user_token_x", accounts[3]);
        info.put("user_token_y", accounts[4]);
        info.put("reserve_x", accounts[5]);
        info.put("reserve_y", accounts[6]);
        info.put("bin_array", accounts[7]);
        info.put("owner", accounts[8]);
        info.put("token_x_program", accounts[9]);
        info.put("token_y_program", accounts[10]);
        info.put("event_authority", accounts[11]);
        info.put("program", accounts[12]);
        
        return info;
    }
} 