package cn.xlystar.parse.solSwap.meteora.dlmm;

import cn.xlystar.parse.solSwap.InstructionParser;
import org.bitcoinj.core.Base58;
import org.bouncycastle.util.encoders.Hex;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MeteoraDlmmInstructionParser extends InstructionParser {

    public static final String PROGRAM_ID = "LBUZKhRxPF3XUpBCjp4YzTKgLccjZhTSDM9YuVaPwxo";

    @Override
    public String getMethodId(ByteBuffer buffer) {
        byte[] methodIdByte = new byte[8];
        buffer.get(methodIdByte);
        return Hex.toHexString(methodIdByte);
    }

    @Override
    public Map<String, Object> matchInstruction(String methodId, ByteBuffer buffer, String[] accounts) {
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
                return new HashMap<>();
        }
        return info;
    }

    private static Map<String, Object> parseInitializeLbPair(ByteBuffer buffer, String[] accounts) {
        if (accounts.length < 14) {
            throw new IllegalArgumentException("Not enough accounts");
        }

        Map<String, Object> info = new HashMap<>();

        // 参数解析
        info.put("active_id", buffer.getInt());
        info.put("bin_step", Short.toUnsignedInt(buffer.getShort())); // 修改为getShort因为u16类型

        // 账户信息
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
        if (accounts.length < 9) {
            throw new IllegalArgumentException("Not enough accounts");
        }

        Map<String, Object> info = new HashMap<>();

        // 参数解析 - 按照IDL定义的顺序
        info.put("reward_index", Long.toUnsignedString(buffer.getLong())); // u64类型
        info.put("reward_duration", Long.toUnsignedString(buffer.getLong())); // u64类型

        // 解析publicKey类型的funder参数
        byte[] funderBytes = new byte[32];
        buffer.get(funderBytes);
        info.put("funder", Base58.encode(funderBytes)); // publicKey类型转为base58字符串

        // 账户信息 - 按照IDL定义的顺序
        info.put("lb_pair", accounts[0]);
        info.put("reward_vault", accounts[1]);
        info.put("reward_mint", accounts[2]);
        info.put("admin", accounts[3]);
        info.put("token_program", accounts[4]);
        info.put("system_program", accounts[5]);
        info.put("rent", accounts[6]);
        info.put("event_authority", accounts[7]);
        info.put("program", accounts[8]);

        return info;
    }

    private static Map<String, Object> parseAddLiquidity(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 解析LiquidityParameter结构
        info.put("amount_x", Long.toUnsignedString(buffer.getLong())); // u64
        info.put("amount_y", Long.toUnsignedString(buffer.getLong())); // u64

        // 解析binLiquidityDist向量
        int length = buffer.getInt(); // 读取vector长度
        List<Map<String, Object>> distributions = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            Map<String, Object> dist = new HashMap<>();
            dist.put("bin_id", buffer.getInt());           // i32
            dist.put("liquidity_amount", buffer.getLong()); // u64
            distributions.add(dist);
        }
        info.put("bin_liquidity_distributions", distributions);

        // 账户信息 - 按照IDL定义的顺序
        info.put("position", accounts[0]);
        info.put("lb_pair", accounts[1]);
        info.put("bin_array_bitmap_extension", accounts[2]); // optional
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
        if (accounts.length > 14) {
            info.put("event_authority", accounts[14]);
        }
        if (accounts.length > 15) {
            info.put("program", accounts[15]);
        }

        return info;
    }

    private static Map<String, Object> parseAddLiquidityByWeight(ByteBuffer buffer, String[] accounts) {
        if (accounts.length < 16) {
            throw new IllegalArgumentException("Not enough accounts");
        }

        Map<String, Object> info = new HashMap<>();

        // 解析LiquidityParameterByWeight结构
        info.put("amount_x", Long.toUnsignedString(buffer.getLong())); // u64
        info.put("amount_y", Long.toUnsignedString(buffer.getLong())); // u64
        info.put("active_id", buffer.getInt());                        // i32
        info.put("max_active_bin_slippage", buffer.getInt());         // i32

        // 解析binLiquidityDist向量
        int length = buffer.getInt(); // 读取vector长度
        List<Map<String, Object>> distributions = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            Map<String, Object> dist = new HashMap<>();
            dist.put("bin_id", buffer.getInt());    // i32
            dist.put("weight", buffer.getShort());  // u16
            distributions.add(dist);
        }
        info.put("bin_liquidity_distributions", distributions);

        // 账户信息 - 按照IDL定义的顺序
        info.put("position", accounts[0]);
        info.put("lb_pair", accounts[1]);
        info.put("bin_array_bitmap_extension", accounts[2]); // optional
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

        // Parse args according to IDL definition
        // lowerBinId (i32)
        info.put("lowerBinId", buffer.getInt());

        // width (i32)
        info.put("width", buffer.getInt());

        // Parse accounts according to IDL definition
        info.put("payer", accounts[0]);
        info.put("position", accounts[1]);
        info.put("lbPair", accounts[2]);
        info.put("owner", accounts[3]);
        info.put("systemProgram", accounts[4]);
        info.put("rent", accounts[5]);
        info.put("eventAuthority", accounts[6]);
        info.put("program", accounts[7]);

        return info;
    }

    private static Map<String, Object> parseSwap(ByteBuffer buffer, String[] accounts) {
        if (accounts.length < 15) {
            throw new IllegalArgumentException("Not enough accounts");
        }

        Map<String, Object> info = new HashMap<>();

        // 参数解析 - 按照IDL定义的顺序
        info.put("amount_in", Long.toUnsignedString(buffer.getLong()));  // u64
        info.put("min_amount_out", Long.toUnsignedString(buffer.getLong())); // u64

        // 账户信息 - 按照IDL定义的顺序
        info.put("lb_pair", accounts[0]);
        info.put("bin_array_bitmap_extension", accounts[1]); // optional
        info.put("reserve_x", accounts[2]);
        info.put("reserve_y", accounts[3]);
        info.put("user_token_in", accounts[4]);
        info.put("user_token_out", accounts[5]);
        info.put("token_x_mint", accounts[6]);
        info.put("token_y_mint", accounts[7]);
        info.put("oracle", accounts[8]);
        info.put("host_fee_in", accounts[9]); // optional
        info.put("user", accounts[10]);
        info.put("token_x_program", accounts[11]);
        info.put("token_y_program", accounts[12]);
        info.put("event_authority", accounts[13]);
        info.put("program", accounts[14]);

        return info;
    }

    private static Map<String, Object> parseFundReward(ByteBuffer buffer, String[] accounts) {
        if (accounts.length < 9) {
            throw new IllegalArgumentException("Not enough accounts");
        }

        Map<String, Object> info = new HashMap<>();

        // 参数解析 - 按照IDL定义的顺序
        info.put("reward_index", Long.toUnsignedString(buffer.getLong())); // u64类型
        info.put("amount", Long.toUnsignedString(buffer.getLong())); // u64类型
        info.put("carry_forward", buffer.get() != 0); // bool类型

        // 账户信息 - 按照IDL定义的顺序
        info.put("lb_pair", accounts[0]);
        info.put("reward_vault", accounts[1]);
        info.put("reward_mint", accounts[2]);
        info.put("funder_token_account", accounts[3]);
        info.put("funder", accounts[4]);
        info.put("bin_array", accounts[5]);
        info.put("token_program", accounts[6]);
        info.put("event_authority", accounts[7]);
        info.put("program", accounts[8]);

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
        if (accounts.length < 8) {
            throw new IllegalArgumentException("Not enough accounts");
        }

        Map<String, Object> info = new HashMap<>();

        // 账户信息 - 按照IDL定义的顺序
        info.put("position", accounts[0]);
        info.put("lb_pair", accounts[1]);
        info.put("bin_array_lower", accounts[2]);
        info.put("bin_array_upper", accounts[3]);
        info.put("sender", accounts[4]);
        info.put("rent_receiver", accounts[5]);
        info.put("event_authority", accounts[6]);
        info.put("program", accounts[7]);

        return info;
    }

    private static Map<String, Object> parseRemoveLiquidity(ByteBuffer buffer, String[] accounts) {
        if (accounts.length < 16) {
            throw new IllegalArgumentException("Not enough accounts");
        }

        Map<String, Object> info = new HashMap<>();

        // 解析 binLiquidityRemoval 参数 (vec<BinLiquidityReduction>)
        int length = buffer.getInt(); // 读取 vector 长度
        List<Map<String, Object>> binLiquidityRemovals = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            Map<String, Object> reduction = new HashMap<>();
            reduction.put("bin_id", buffer.getInt());  // i32
            reduction.put("liquidity_amount", buffer.getLong()); // u64
            binLiquidityRemovals.add(reduction);
        }
        info.put("bin_liquidity_removals", binLiquidityRemovals);

        // 账户信息 - 按照IDL定义的顺序
        info.put("position", accounts[0]);
        info.put("lb_pair", accounts[1]);
        info.put("bin_array_bitmap_extension", accounts[2]); // optional
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

    private static Map<String, Object> parseRemoveLiquidityByRange(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();

        // 解析参数 - 按照IDL定义的顺序
        info.put("from_bin_id", buffer.getInt());  // i32
        info.put("to_bin_id", buffer.getInt());    // i32
        info.put("bps_to_remove", buffer.getShort()); // u16

        // 账户信息 - 按照IDL定义的顺序
        info.put("position", accounts[0]);
        info.put("lb_pair", accounts[1]);
        info.put("bin_array_bitmap_extension", accounts[2]); // optional
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

    private Map<String, Object> parseAddLiquidityOneSide(byte[] data, String[] accounts) {
        Map<String, Object> result = new HashMap<>();

        // Parse accounts according to IDL definition
        result.put("payer", accounts[0]);
        result.put("lbPair", accounts[1]);
        result.put("binArrayBitmapExtension", accounts[2]);
        result.put("userTokenX", accounts[3]);
        result.put("userTokenY", accounts[4]);
        result.put("reserveX", accounts[5]);
        result.put("reserveY", accounts[6]);
        result.put("binArrayBitmap", accounts[7]);
        result.put("systemProgram", accounts[8]);
        result.put("tokenProgram", accounts[9]);

        // Parse args
        ByteBuffer buffer = ByteBuffer.wrap(data);
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        // Parse amountIn (u64)
        long amountIn = buffer.getLong();
        result.put("amountIn", amountIn);

        return result;
    }
} 