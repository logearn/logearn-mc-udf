package cn.xlystar.parse.solSwap.metaplex;

import cn.xlystar.parse.solSwap.InstructionParser;
import org.bouncycastle.util.encoders.Hex;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * Metaplex Token Metadata Program 指令解析器
 */
public class MetaplexInstructionParser extends InstructionParser {

    @Override
    public String getMethodId(ByteBuffer buffer) {
        byte[] discriminatorBytes = new byte[8];
        buffer.get(discriminatorBytes);
        return Hex.toHexString(discriminatorBytes);
    }

    @Override
    public Map<String, Object> matchInstruction(String methodId, ByteBuffer buffer, String[] accounts) {
        Map<String, Object> parsed;
        switch (MetaplexInstruction.fromValue(Integer.parseInt(methodId))) {
            case CreateMetadataAccount:              // 0: 创建元数据账户
                parsed = parseCreateMetadataAccount(buffer, accounts);
                break;
            case UpdateMetadataAccount:              // 1: 更新元数据账户
                parsed = parseUpdateMetadataAccount(buffer, accounts);
                break;
            case DeprecatedCreateMasterEdition:      // 2: 已弃用的创建主版本
                parsed = parseDeprecatedCreateMasterEdition(buffer, accounts);
                break;
            case DeprecatedMintNewEditionFromMasterEditionViaPrintingToken: // 3: 已弃用的通过打印代币从主版本铸造新版本
                parsed = parseDeprecatedMintNewEditionFromMasterEditionViaPrintingToken(buffer, accounts);
                break;
            case UpdatePrimarySaleHappenedViaToken:  // 4: 通过代币更新主要销售状态
                parsed = parseUpdatePrimarySaleHappenedViaToken(buffer, accounts);
                break;
            case DeprecatedSetReservationList:       // 5: 已弃用的设置预约列表
                parsed = parseDeprecatedSetReservationList(buffer, accounts);
                break;
            case DeprecatedCreateReservationList:    // 6: 已弃用的创建预约列表
                parsed = parseDeprecatedCreateReservationList(buffer, accounts);
                break;
            case SignMetadata:                       // 7: 签名元数据
                parsed = parseSignMetadata(buffer, accounts);
                break;
            case DeprecatedMintPrintingTokensViaToken: // 8: 已弃用的通过代币铸造打印代币
                parsed = parseDeprecatedMintPrintingTokensViaToken(buffer, accounts);
                break;
            case DeprecatedMintPrintingTokens:       // 9: 已弃用的铸造打印代币
                parsed = parseDeprecatedMintPrintingTokens(buffer, accounts);
                break;
            case CreateMasterEdition:                // 10: 创建主版本
                parsed = parseCreateMasterEdition(buffer, accounts);
                break;
            case MintNewEditionFromMasterEditionViaToken: // 11: 通过代币从主版本铸造新版本
                parsed = parseMintNewEditionFromMasterEditionViaToken(buffer, accounts);
                break;
            case ConvertMasterEditionV1ToV2:         // 12: 将主版本V1转换为V2
                parsed = parseConvertMasterEditionV1ToV2(buffer, accounts);
                break;
            case MintNewEditionFromMasterEditionViaVaultProxy: // 13: 通过保管库代理从主版本铸造新版本
                parsed = parseMintNewEditionFromMasterEditionViaVaultProxy(buffer, accounts);
                break;
            case PuffMetadata:                       // 14: 填充元数据
                parsed = parsePuffMetadata(buffer, accounts);
                break;
            case UpdateMetadataAccountV2:            // 15: 更新元数据账户V2
                parsed = parseUpdateMetadataAccountV2(buffer, accounts);
                break;
            case CreateMetadataAccountV2:            // 16: 创建元数据账户V2
                parsed = parseCreateMetadataAccountV2(buffer, accounts);
                break;
            case CreateMasterEditionV3:              // 17: 创建主版本V3
                parsed = parseCreateMasterEditionV3(buffer, accounts);
                break;
            case VerifyCollection:                   // 18: 验证收藏集
                parsed = parseVerifyCollection(buffer, accounts);
                break;
            case Utilize:                            // 19: 使用
                parsed = parseUtilize(buffer, accounts);
                break;
            case ApproveUseAuthority:                // 20: 批准使用权限
                parsed = parseApproveUseAuthority(buffer, accounts);
                break;
            case RevokeUseAuthority:                 // 21: 撤销使用权限
                parsed = parseRevokeUseAuthority(buffer, accounts);
                break;
            case UnverifyCollection:                 // 22: 取消验证收藏集
                parsed = parseUnverifyCollection(buffer, accounts);
                break;
            case ApproveCollectionAuthority:         // 23: 批准收藏集权限
                parsed = parseApproveCollectionAuthority(buffer, accounts);
                break;
            case RevokeCollectionAuthority:          // 24: 撤销收藏集权限
                parsed = parseRevokeCollectionAuthority(buffer, accounts);
                break;
            case SetAndVerifyCollection:             // 25: 设置并验证收藏集
                parsed = parseSetAndVerifyCollection(buffer, accounts);
                break;
            case FreezeDelegatedAccount:             // 26: 冻结委托账户
                parsed = parseFreezeDelegatedAccount(buffer, accounts);
                break;
            case ThawDelegatedAccount:               // 27: 解冻委托账户
                parsed = parseThawDelegatedAccount(buffer, accounts);
                break;
            case RemoveCreatorVerification:          // 28: 移除创建者验证
                parsed = parseRemoveCreatorVerification(buffer, accounts);
                break;
            case BurnNft:                           // 29: 销毁NFT
                parsed = parseBurnNft(buffer, accounts);
                break;
            case VerifySizedCollectionItem:         // 30: 验证有大小的收藏集项目
                parsed = parseVerifySizedCollectionItem(buffer, accounts);
                break;
            case UnverifySizedCollectionItem:       // 31: 取消验证有大小的收藏集项目
                parsed = parseUnverifySizedCollectionItem(buffer, accounts);
                break;
            case SetAndVerifySizedCollectionItem:   // 32: 设置并验证有大小的收藏集项目
                parsed = parseSetAndVerifySizedCollectionItem(buffer, accounts);
                break;
            case CreateMetadataAccountV3:           // 33: 创建元数据账户V3
                parsed = parseCreateMetadataAccountV3(buffer, accounts);
                break;
            case SetCollectionSize:                 // 34: 设置收藏集大小
                parsed = parseSetCollectionSize(buffer, accounts);
                break;
            case SetTokenStandard:                  // 35: 设置代币标准
                parsed = parseSetTokenStandard(buffer, accounts);
                break;
            case BubblegumSetCollectionSize:        // 36: Bubblegum设置收藏集大小
                parsed = parseBubblegumSetCollectionSize(buffer, accounts);
                break;
            case BurnEditionNft:                    // 37: 销毁版本NFT
                parsed = parseBurnEditionNft(buffer, accounts);
                break;
            case CreateEscrowAccount:               // 38: 创建托管账户
                parsed = parseCreateEscrowAccount(buffer, accounts);
                break;
            case CloseEscrowAccount:                // 39: 关闭托管账户
                parsed = parseCloseEscrowAccount(buffer, accounts);
                break;
            case TransferOutOfEscrow:               // 40: 从托管转出
                parsed = parseTransferOutOfEscrow(buffer, accounts);
                break;
            case Burn:                              // 41: 销毁
                parsed = parseBurn(buffer, accounts);
                break;
            case Create:                            // 42: 创建
                parsed = parseCreate(buffer, accounts);
                break;
            case Mint:                              // 43: 铸造
                parsed = parseMint(buffer, accounts);
                break;
            case Delegate:                          // 44: 委托
                parsed = parseDelegate(buffer, accounts);
                break;
            case Revoke:                            // 45: 撤销
                parsed = parseRevoke(buffer, accounts);
                break;
            case Lock:                              // 46: 锁定
                parsed = parseLock(buffer, accounts);
                break;
            case Unlock:                            // 47: 解锁
                parsed = parseUnlock(buffer, accounts);
                break;
            case Migrate:                           // 48: 迁移
                parsed = parseMigrate(buffer, accounts);
                break;
            case Transfer:                          // 49: 转账
                parsed = parseTransfer(buffer, accounts);
                break;
            case Update:                            // 50: 更新
                parsed = parseUpdate(buffer, accounts);
                break;
            case Use:                               // 51: 使用
                parsed = parseUse(buffer, accounts);
                break;
            case Verify:                            // 52: 验证
                parsed = parseVerify(buffer, accounts);
                break;
            case Unverify:                          // 53: 取消验证
                parsed = parseUnverify(buffer, accounts);
                break;
            case Collect:                           // 54: 收集
                parsed = parseCollect(buffer, accounts);
                break;
            case Print:                             // 55: 打印
                parsed = parsePrint(buffer, accounts);
                break;
            case Resize:                            // 56: 调整大小
                parsed = parseResize(buffer, accounts);
                break;
            case CloseAccounts:                     // 57: 关闭账户
                parsed = parseCloseAccounts(buffer, accounts);
                break;
            default:
                return new HashMap<>();
        }
        return parsed;
    }

    /**
     * 解析创建元数据账户指令
     */
    private static Map<String, Object> parseCreateMetadataAccount(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        Map<String, String> accountMap = new HashMap<>();

        if (accounts.length >= 7) {
            accountMap.put("metadata", accounts[0]);        // 元数据账户
            accountMap.put("mint", accounts[1]);           // 铸币账户
            accountMap.put("mintAuthority", accounts[2]);  // 铸币权限
            accountMap.put("payer", accounts[3]);          // 支付账户
            accountMap.put("updateAuthority", accounts[4]); // 更新权限
            accountMap.put("systemProgram", accounts[5]);   // System程序
            accountMap.put("rent", accounts[6]);           // 租金账户
        }

        info.put("accounts", accountMap);
        return info;
    }

    /**
     * 解析更新元数据账户指令
     */
    private static Map<String, Object> parseUpdateMetadataAccount(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        Map<String, String> accountMap = new HashMap<>();

        if (accounts.length >= 2) {
            accountMap.put("metadata", accounts[0]);         // 元数据账户
            accountMap.put("updateAuthority", accounts[1]);  // 更新权限账户
        }

        info.put("accounts", accountMap);
        return info;
    }


    /**
     * 解析已弃用的创建主版本指令
     */
    private static Map<String, Object> parseDeprecatedCreateMasterEdition(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        Map<String, String> accountMap = new HashMap<>();

        if (accounts.length >= 13) {
            accountMap.put("edition", accounts[0]);          // 版本账户
            accountMap.put("mint", accounts[1]);            // 铸币账户
            accountMap.put("printingMint", accounts[2]);    // 打印铸币账户
            accountMap.put("oneTimePrintingAuthorizationMint", accounts[3]); // 一次性打印授权铸币账户
            accountMap.put("updateAuthority", accounts[4]);  // 更新权限
            accountMap.put("printingMintAuthority", accounts[5]); // 打印铸币权限
            accountMap.put("mintAuthority", accounts[6]);    // 铸币权限
            accountMap.put("metadata", accounts[7]);         // 元数据账户
            accountMap.put("payer", accounts[8]);           // 支付账户
            accountMap.put("tokenProgram", accounts[9]);    // Token程序
            accountMap.put("systemProgram", accounts[10]);  // System程序
            accountMap.put("rent", accounts[11]);           // 租金账户
            accountMap.put("oneTimePrintingAuthorizationMintAuthority", accounts[12]); // 一次性打印授权铸币权限
        }

        info.put("accounts", accountMap);
        return info;
    }
    /**
     * 解析已弃用的通过打印代币从主版本铸造新版本指令
     */
    private static Map<String, Object> parseDeprecatedMintNewEditionFromMasterEditionViaPrintingToken(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        Map<String, String> accountMap = new HashMap<>();

        if (accounts.length >= 15) {
            accountMap.put("metadata", accounts[0]);           // 新元数据账户
            accountMap.put("edition", accounts[1]);            // 新版本账户
            accountMap.put("masterEdition", accounts[2]);      // 主版本账户
            accountMap.put("mint", accounts[3]);               // 新铸币账户
            accountMap.put("mintAuthority", accounts[4]);      // 铸币权限
            accountMap.put("printingMint", accounts[5]);       // 打印铸币账户
            accountMap.put("masterTokenAccount", accounts[6]); // 主代币账户
            accountMap.put("editionMarker", accounts[7]);      // 版本标记
            accountMap.put("burnAuthority", accounts[8]);      // 销毁权限
            accountMap.put("payer", accounts[9]);              // 支付账户
            accountMap.put("masterUpdateAuthority", accounts[10]); // 主更新权限
            accountMap.put("masterMetadata", accounts[11]);    // 主元数据账户
            accountMap.put("tokenProgram", accounts[12]);      // Token程序
            accountMap.put("systemProgram", accounts[13]);     // System程序
            accountMap.put("rent", accounts[14]);              // 租金账户
        }

        info.put("accounts", accountMap);
        return info;
    }

    /**
     * 解析通过代币更新主要销售状态指令
     */
    private static Map<String, Object> parseUpdatePrimarySaleHappenedViaToken(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        Map<String, String> accountMap = new HashMap<>();

        if (accounts.length >= 3) {
            accountMap.put("metadata", accounts[0]);    // 元数据账户
            accountMap.put("owner", accounts[1]);       // 所有者
            accountMap.put("token", accounts[2]);       // 代币账户
        }

        info.put("accounts", accountMap);
        return info;
    }

    /**
     * 解析已弃用的设置预约列表指令
     */
    private static Map<String, Object> parseDeprecatedSetReservationList(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        Map<String, String> accountMap = new HashMap<>();

        if (accounts.length >= 3) {
            accountMap.put("masterEdition", accounts[0]);    // 主版本账户
            accountMap.put("reservationList", accounts[1]);  // 预约列表账户
            accountMap.put("resource", accounts[2]);         // 资源账户
        }

        info.put("accounts", accountMap);
        return info;
    }

    /**
     * 解析已弃用的创建预约列表指令
     */
    private static Map<String, Object> parseDeprecatedCreateReservationList(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        Map<String, String> accountMap = new HashMap<>();

        if (accounts.length >= 6) {
            accountMap.put("reservationList", accounts[0]);  // 预约列表账户
            accountMap.put("payer", accounts[1]);            // 支付账户
            accountMap.put("updateAuthority", accounts[2]);  // 更新权限
            accountMap.put("masterEdition", accounts[3]);    // 主版本账户
            accountMap.put("resource", accounts[4]);         // 资源账户
            accountMap.put("metadata", accounts[5]);         // 元数据账户
        }

        info.put("accounts", accountMap);
        return info;
    }

    /**
     * 解析签名元数据指令
     */
    private static Map<String, Object> parseSignMetadata(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        Map<String, String> accountMap = new HashMap<>();

        if (accounts.length >= 2) {
            accountMap.put("metadata", accounts[0]);     // 元数据账户
            accountMap.put("creator", accounts[1]);      // 创建者账户
        }

        info.put("accounts", accountMap);
        return info;
    }


    /**
     * 解析已弃用的通过代币铸造打印代币指令
     */
    private static Map<String, Object> parseDeprecatedMintPrintingTokensViaToken(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        Map<String, String> accountMap = new HashMap<>();

        if (accounts.length >= 9) {
            accountMap.put("destination", accounts[0]);      // 目标账户
            accountMap.put("token", accounts[1]);           // 代币账户
            accountMap.put("oneTimeAuthMint", accounts[2]); // 一次性授权铸币账户
            accountMap.put("printingMint", accounts[3]);    // 打印铸币账户
            accountMap.put("burnAuthority", accounts[4]);   // 销毁权限
            accountMap.put("metadata", accounts[5]);        // 元数据账户
            accountMap.put("masterEdition", accounts[6]);   // 主版本账户
            accountMap.put("tokenProgram", accounts[7]);    // Token程序
            accountMap.put("rent", accounts[8]);           // 租金账户
        }

        info.put("accounts", accountMap);
        return info;
    }

    /**
     * 解析已弃用的铸造打印代币指令
     */
    private static Map<String, Object> parseDeprecatedMintPrintingTokens(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        Map<String, String> accountMap = new HashMap<>();

        if (accounts.length >= 9) {
            accountMap.put("destination", accounts[0]);     // 目标账户
            accountMap.put("printingMint", accounts[1]);    // 打印铸币账户
            accountMap.put("updateAuthority", accounts[2]); // 更新权限
            accountMap.put("metadata", accounts[3]);        // 元数据账户
            accountMap.put("masterEdition", accounts[4]);   // 主版本账户
            accountMap.put("tokenProgram", accounts[5]);    // Token程序
            accountMap.put("rent", accounts[6]);           // 租金账户
            accountMap.put("systemProgram", accounts[7]);  // System程序
            accountMap.put("mint", accounts[8]);           // 铸币账户
        }

        info.put("accounts", accountMap);
        return info;
    }

    /**
     * 解析创建主版本指令
     */
    private static Map<String, Object> parseCreateMasterEdition(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        Map<String, String> accountMap = new HashMap<>();

        if (accounts.length >= 9) {
            accountMap.put("edition", accounts[0]);         // 版本账户
            accountMap.put("mint", accounts[1]);           // 铸币账户
            accountMap.put("updateAuthority", accounts[2]); // 更新权限
            accountMap.put("mintAuthority", accounts[3]);   // 铸币权限
            accountMap.put("payer", accounts[4]);          // 支付账户
            accountMap.put("metadata", accounts[5]);       // 元数据账户
            accountMap.put("tokenProgram", accounts[6]);   // Token程序
            accountMap.put("systemProgram", accounts[7]);  // System程序
            accountMap.put("rent", accounts[8]);          // 租金账户
        }

        info.put("accounts", accountMap);
        return info;
    }

    /**
     * 解析通过代币从主版本铸造新版本指令
     */
    private static Map<String, Object> parseMintNewEditionFromMasterEditionViaToken(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        Map<String, String> accountMap = new HashMap<>();

        if (accounts.length >= 13) {
            accountMap.put("newMetadata", accounts[0]);        // 新元数据账户
            accountMap.put("newEdition", accounts[1]);         // 新版本账户
            accountMap.put("masterEdition", accounts[2]);      // 主版本账户
            accountMap.put("newMint", accounts[3]);            // 新铸币账户
            accountMap.put("editionMarkPda", accounts[4]);     // 版本标记PDA
            accountMap.put("newMintAuthority", accounts[5]);   // 新铸币权限
            accountMap.put("payer", accounts[6]);              // 支付账户
            accountMap.put("tokenAccountOwner", accounts[7]);  // 代币账户所有者
            accountMap.put("tokenAccount", accounts[8]);       // 代币账户
            accountMap.put("newMetadataUpdateAuthority", accounts[9]); // 新元数据更新权限
            accountMap.put("metadata", accounts[10]);          // 元数据账户
            accountMap.put("tokenProgram", accounts[11]);      // Token程序
            accountMap.put("systemProgram", accounts[12]);     // System程序
        }

        info.put("accounts", accountMap);
        return info;
    }

    /**
     * 解析将主版本V1转换为V2指令
     */
    private static Map<String, Object> parseConvertMasterEditionV1ToV2(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        Map<String, String> accountMap = new HashMap<>();

        if (accounts.length >= 4) {
            accountMap.put("masterEdition", accounts[0]);    // 主版本账户
            accountMap.put("oneTimeAuth", accounts[1]);      // 一次性授权账户
            accountMap.put("printingMint", accounts[2]);     // 打印铸币账户
            accountMap.put("tokenProgram", accounts[3]);     // Token程序
        }

        info.put("accounts", accountMap);
        return info;
    }

    /**
     * 解析通过保管库代理从主版本铸造新版本指令
     */
    private static Map<String, Object> parseMintNewEditionFromMasterEditionViaVaultProxy(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        Map<String, String> accountMap = new HashMap<>();

        if (accounts.length >= 15) {
            accountMap.put("newMetadata", accounts[0]);       // 新元数据账户
            accountMap.put("newEdition", accounts[1]);        // 新版本账户
            accountMap.put("masterEdition", accounts[2]);     // 主版本账户
            accountMap.put("newMint", accounts[3]);           // 新铸币账户
            accountMap.put("editionMarkPda", accounts[4]);    // 版本标记PDA
            accountMap.put("newMintAuthority", accounts[5]);  // 新铸币权限
            accountMap.put("payer", accounts[6]);             // 支付账户
            accountMap.put("vaultAuthority", accounts[7]);    // 保管库权限
            accountMap.put("safetyDepositStore", accounts[8]);// 安全存储账户
            accountMap.put("safetyDepositBox", accounts[9]); // 安全存储箱
            accountMap.put("vault", accounts[10]);           // 保管库
            accountMap.put("newMetadataUpdateAuthority", accounts[11]); // 新元数据更新权限
            accountMap.put("metadata", accounts[12]);        // 元数据账户
            accountMap.put("tokenProgram", accounts[13]);    // Token程序
            accountMap.put("tokenVaultProgram", accounts[14]); // Token保管库程序
        }

        info.put("accounts", accountMap);
        return info;
    }

    /**
     * 解析填充元数据指令
     */
    private static Map<String, Object> parsePuffMetadata(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        Map<String, String> accountMap = new HashMap<>();

        if (accounts.length >= 1) {
            accountMap.put("metadata", accounts[0]);         // 元数据账户
        }

        info.put("accounts", accountMap);
        return info;
    }

    /**
     * 解析更新元数据账户V2指令
     */
    private static Map<String, Object> parseUpdateMetadataAccountV2(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        Map<String, String> accountMap = new HashMap<>();

        if (accounts.length >= 2) {
            accountMap.put("metadata", accounts[0]);         // 元数据账户
            accountMap.put("updateAuthority", accounts[1]);  // 更新权限账户
        }

        info.put("accounts", accountMap);
        return info;
    }

    /**
     * 解析创建元数据账户V2指令
     */
    private static Map<String, Object> parseCreateMetadataAccountV2(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        Map<String, String> accountMap = new HashMap<>();

        if (accounts.length >= 7) {
            accountMap.put("metadata", accounts[0]);        // 元数据账户
            accountMap.put("mint", accounts[1]);           // 铸币账户
            accountMap.put("mintAuthority", accounts[2]);  // 铸币权限
            accountMap.put("payer", accounts[3]);          // 支付账户
            accountMap.put("updateAuthority", accounts[4]); // 更新权限
            accountMap.put("systemProgram", accounts[5]);   // System程序
            accountMap.put("rent", accounts[6]);           // 租金账户
        }

        info.put("accounts", accountMap);
        return info;
    }

    /**
     * 解析创建主版本V3指令
     */
    private static Map<String, Object> parseCreateMasterEditionV3(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        Map<String, String> accountMap = new HashMap<>();

        if (accounts.length >= 9) {
            accountMap.put("edition", accounts[0]);         // 版本账户
            accountMap.put("mint", accounts[1]);           // 铸币账户
            accountMap.put("updateAuthority", accounts[2]); // 更新权限
            accountMap.put("mintAuthority", accounts[3]);   // 铸币权限
            accountMap.put("payer", accounts[4]);          // 支付账户
            accountMap.put("metadata", accounts[5]);       // 元数据账户
            accountMap.put("tokenProgram", accounts[6]);   // Token程序
            accountMap.put("systemProgram", accounts[7]);  // System程序
            accountMap.put("rent", accounts[8]);          // 租金账户
        }

        info.put("accounts", accountMap);
        return info;
    }

    /**
     * 解析验证收藏集指令
     */
    private static Map<String, Object> parseVerifyCollection(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        Map<String, String> accountMap = new HashMap<>();

        if (accounts.length >= 6) {
            accountMap.put("metadata", accounts[0]);           // 元数据账户
            accountMap.put("collectionAuthority", accounts[1]);// 收藏集权限
            accountMap.put("payer", accounts[2]);             // 支付账户
            accountMap.put("collectionMint", accounts[3]);    // 收藏集铸币账户
            accountMap.put("collection", accounts[4]);        // 收藏集账户
            accountMap.put("collectionMasterEdition", accounts[5]); // 收藏集主版本账户
        }

        info.put("accounts", accountMap);
        return info;
    }

    /**
     * 解析使用指令
     */
    private static Map<String, Object> parseUtilize(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        Map<String, String> accountMap = new HashMap<>();

        if (accounts.length >= 5) {
            accountMap.put("metadata", accounts[0]);      // 元数据账户
            accountMap.put("tokenAccount", accounts[1]);  // 代币账户
            accountMap.put("mint", accounts[2]);          // 铸币账户
            accountMap.put("useAuthority", accounts[3]);  // 使用权限
            accountMap.put("owner", accounts[4]);         // 所有者
        }

        info.put("accounts", accountMap);
        return info;
    }

    /**
     * 解析批准使用权限指令
     */
    private static Map<String, Object> parseApproveUseAuthority(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        Map<String, String> accountMap = new HashMap<>();

        if (accounts.length >= 7) {
            accountMap.put("useAuthorityRecord", accounts[0]); // 使用权限记录
            accountMap.put("owner", accounts[1]);              // 所有者
            accountMap.put("payer", accounts[2]);             // 支付账户
            accountMap.put("user", accounts[3]);              // 用户
            accountMap.put("ownerTokenAccount", accounts[4]); // 所有者代币账户
            accountMap.put("metadata", accounts[5]);          // 元数据账户
            accountMap.put("mint", accounts[6]);             // 铸币账户
        }

        info.put("accounts", accountMap);
        return info;
    }

    /**
     * 解析撤销使用权限指令
     */
    private static Map<String, Object> parseRevokeUseAuthority(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        Map<String, String> accountMap = new HashMap<>();

        if (accounts.length >= 7) {
            accountMap.put("useAuthorityRecord", accounts[0]); // 使用权限记录
            accountMap.put("owner", accounts[1]);              // 所有者
            accountMap.put("user", accounts[2]);               // 用户
            accountMap.put("ownerTokenAccount", accounts[3]);  // 所有者代币账户
            accountMap.put("mint", accounts[4]);              // 铸币账户
            accountMap.put("metadata", accounts[5]);          // 元数据账户
            accountMap.put("tokenProgram", accounts[6]);      // Token程序
        }

        info.put("accounts", accountMap);
        return info;
    }

    /**
     * 解析取消验证收藏集指令
     */
    private static Map<String, Object> parseUnverifyCollection(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        Map<String, String> accountMap = new HashMap<>();

        if (accounts.length >= 6) {
            accountMap.put("metadata", accounts[0]);            // 元数据账户
            accountMap.put("collectionAuthority", accounts[1]); // 收藏集权限
            accountMap.put("collectionMint", accounts[2]);      // 收藏集铸币账户
            accountMap.put("collection", accounts[3]);          // 收藏集账户
            accountMap.put("collectionMasterEdition", accounts[4]); // 收藏集主版本账户
            accountMap.put("collectionAuthorityRecord", accounts[5]); // 收藏集权限记录
        }

        info.put("accounts", accountMap);
        return info;
    }

    /**
     * 解析批准收藏集权限指令
     */
    private static Map<String, Object> parseApproveCollectionAuthority(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        Map<String, String> accountMap = new HashMap<>();

        if (accounts.length >= 7) {
            accountMap.put("collectionAuthorityRecord", accounts[0]); // 收藏集权限记录
            accountMap.put("newCollectionAuthority", accounts[1]);    // 新收藏集权限
            accountMap.put("updateAuthority", accounts[2]);           // 更新权限
            accountMap.put("payer", accounts[3]);                    // 支付账户
            accountMap.put("metadata", accounts[4]);                 // 元数据账户
            accountMap.put("mint", accounts[5]);                    // 铸币账户
            accountMap.put("systemProgram", accounts[6]);           // System程序
        }

        info.put("accounts", accountMap);
        return info;
    }

    /**
     * 解析撤销收藏集权限指令
     */
    private static Map<String, Object> parseRevokeCollectionAuthority(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        Map<String, String> accountMap = new HashMap<>();

        if (accounts.length >= 4) {
            accountMap.put("collectionAuthorityRecord", accounts[0]); // 收藏集权限记录
            accountMap.put("delegateAuthority", accounts[1]);         // 委托权限
            accountMap.put("revokeAuthority", accounts[2]);          // 撤销权限
            accountMap.put("metadata", accounts[3]);                 // 元数据账户
        }

        info.put("accounts", accountMap);
        return info;
    }

    /**
     * 解析设置并验证收藏集指令
     */
    private static Map<String, Object> parseSetAndVerifyCollection(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        Map<String, String> accountMap = new HashMap<>();

        if (accounts.length >= 7) {
            accountMap.put("metadata", accounts[0]);                 // 元数据账户
            accountMap.put("collectionAuthority", accounts[1]);      // 收藏集权限
            accountMap.put("payer", accounts[2]);                   // 支付账户
            accountMap.put("updateAuthority", accounts[3]);         // 更新权限
            accountMap.put("collectionMint", accounts[4]);         // 收藏集铸币账户
            accountMap.put("collection", accounts[5]);             // 收藏集账户
            accountMap.put("collectionMasterEdition", accounts[6]); // 收藏集主版本账户
        }

        info.put("accounts", accountMap);
        return info;
    }

    /**
     * 解析冻结委托账户指令
     */
    private static Map<String, Object> parseFreezeDelegatedAccount(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        Map<String, String> accountMap = new HashMap<>();

        if (accounts.length >= 5) {
            accountMap.put("delegate", accounts[0]);       // 委托账户
            accountMap.put("tokenAccount", accounts[1]);   // 代币账户
            accountMap.put("edition", accounts[2]);        // 版本账户
            accountMap.put("mint", accounts[3]);          // 铸币账户
            accountMap.put("tokenProgram", accounts[4]);  // Token程序
        }

        info.put("accounts", accountMap);
        return info;
    }

    /**
     * 解析解冻委托账户指令
     */
    private static Map<String, Object> parseThawDelegatedAccount(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        Map<String, String> accountMap = new HashMap<>();

        if (accounts.length >= 5) {
            accountMap.put("delegate", accounts[0]);       // 委托账户
            accountMap.put("tokenAccount", accounts[1]);   // 代币账户
            accountMap.put("edition", accounts[2]);        // 版本账户
            accountMap.put("mint", accounts[3]);          // 铸币账户
            accountMap.put("tokenProgram", accounts[4]);  // Token程序
        }

        info.put("accounts", accountMap);
        return info;
    }

    /**
     * 解析移除创建者验证指令
     */
    private static Map<String, Object> parseRemoveCreatorVerification(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        Map<String, String> accountMap = new HashMap<>();

        if (accounts.length >= 2) {
            accountMap.put("metadata", accounts[0]);    // 元数据账户
            accountMap.put("creator", accounts[1]);     // 创建者账户
        }

        info.put("accounts", accountMap);
        return info;
    }

    /**
     * 解析销毁NFT指令
     */
    private static Map<String, Object> parseBurnNft(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        Map<String, String> accountMap = new HashMap<>();

        if (accounts.length >= 7) {
            accountMap.put("metadata", accounts[0]);       // 元数据账户
            accountMap.put("owner", accounts[1]);          // 所有者
            accountMap.put("mint", accounts[2]);           // 铸币账户
            accountMap.put("tokenAccount", accounts[3]);   // 代币账户
            accountMap.put("masterEdition", accounts[4]);  // 主版本账户
            accountMap.put("splTokenProgram", accounts[5]); // SPL Token程序
            accountMap.put("collectionMetadata", accounts[6]); // 收藏集元数据账户
        }

        info.put("accounts", accountMap);
        return info;
    }

    /**
     * 解析验证有大小的收藏集项目指令
     */
    private static Map<String, Object> parseVerifySizedCollectionItem(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        Map<String, String> accountMap = new HashMap<>();

        if (accounts.length >= 7) {
            accountMap.put("metadata", accounts[0]);                // 元数据账户
            accountMap.put("collectionAuthority", accounts[1]);     // 收藏集权限
            accountMap.put("payer", accounts[2]);                  // 支付账户
            accountMap.put("collectionMint", accounts[3]);         // 收藏集铸币账户
            accountMap.put("collection", accounts[4]);             // 收藏集账户
            accountMap.put("collectionMasterEdition", accounts[5]); // 收藏集主版本账户
            accountMap.put("collectionAuthorityRecord", accounts[6]); // 收藏集权限记录
        }

        info.put("accounts", accountMap);
        return info;
    }

    /**
     * 解析取消验证有大小的收藏集项目指令
     */
    private static Map<String, Object> parseUnverifySizedCollectionItem(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        Map<String, String> accountMap = new HashMap<>();

        if (accounts.length >= 7) {
            accountMap.put("metadata", accounts[0]);                // 元数据账户
            accountMap.put("collectionAuthority", accounts[1]);     // 收藏集权限
            accountMap.put("payer", accounts[2]);                  // 支付账户
            accountMap.put("collectionMint", accounts[3]);         // 收藏集铸币账户
            accountMap.put("collection", accounts[4]);             // 收藏集账户
            accountMap.put("collectionMasterEdition", accounts[5]); // 收藏集主版本账户
            accountMap.put("collectionAuthorityRecord", accounts[6]); // 收藏集权限记录
        }

        info.put("accounts", accountMap);
        return info;
    }

    /**
     * 解析设置并验证有大小的收藏集项目指令
     */
    private static Map<String, Object> parseSetAndVerifySizedCollectionItem(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 7) {
            accountMap.put("metadata", accounts[0]);                // 元数据账户
            accountMap.put("collectionAuthority", accounts[1]);     // 收藏集权限
            accountMap.put("payer", accounts[2]);                  // 支付账户
            accountMap.put("updateAuthority", accounts[3]);        // 更新权限
            accountMap.put("collectionMint", accounts[4]);         // 收藏集铸币账户
            accountMap.put("collection", accounts[5]);             // 收藏集账户
            accountMap.put("collectionAuthorityRecord", accounts[6]); // 收藏集权限记录
        }
        info.put("accounts", accountMap);
        return info;
    }

    /**
     * 解析创建元数据账户V3指令
     */
    private static Map<String, Object> parseCreateMetadataAccountV3(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 7) {
            accountMap.put("metadata", accounts[0]);        // 元数据账户
            accountMap.put("mint", accounts[1]);           // 铸币账户
            accountMap.put("mintAuthority", accounts[2]);  // 铸币权限
            accountMap.put("payer", accounts[3]);          // 支付账户
            accountMap.put("updateAuthority", accounts[4]); // 更新权限
            accountMap.put("systemProgram", accounts[5]);   // System程序
            accountMap.put("rent", accounts[6]);           // 租金账户
        }
        info.put("accounts", accountMap);
        return info;
    }

    /**
     * 解析设置收藏集大小指令
     */
    private static Map<String, Object> parseSetCollectionSize(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 4) {
            accountMap.put("collectionMetadata", accounts[0]); // 收藏集元数据账户
            accountMap.put("collectionAuthority", accounts[1]); // 收藏集权限
            accountMap.put("collectionMint", accounts[2]);     // 收藏集铸币账户
            accountMap.put("collectionAuthorityRecord", accounts[3]); // 收藏集权限记录
        }
        info.put("accounts", accountMap);
        return info;
    }

    /**
     * 解析设置代币标准指令
     */
    private static Map<String, Object> parseSetTokenStandard(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 4) {
            accountMap.put("metadata", accounts[0]);        // 元数据账户
            accountMap.put("updateAuthority", accounts[1]); // 更新权限
            accountMap.put("mint", accounts[2]);           // 铸币账户
            accountMap.put("edition", accounts[3]);        // 版本账户
        }
        info.put("accounts", accountMap);
        return info;
    }

    /**
     * 解析Bubblegum设置收藏集大小指令
     */
    private static Map<String, Object> parseBubblegumSetCollectionSize(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 3) {
            accountMap.put("collectionMetadata", accounts[0]); // 收藏集元数据账户
            accountMap.put("collectionAuthority", accounts[1]); // 收藏集权限
            accountMap.put("collectionMint", accounts[2]);     // 收藏集铸币账户
        }
        info.put("accounts", accountMap);
        return info;
    }

    /**
     * 解析销毁版本NFT指令
     */
    private static Map<String, Object> parseBurnEditionNft(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 8) {
            accountMap.put("metadata", accounts[0]);         // 元数据账户
            accountMap.put("owner", accounts[1]);           // 所有者
            accountMap.put("printEditionMint", accounts[2]); // 打印版本铸币账户
            accountMap.put("masterEditionMint", accounts[3]); // 主版本铸币账户
            accountMap.put("printEditionToken", accounts[4]); // 打印版本代币账户
            accountMap.put("masterEditionToken", accounts[5]); // 主版本代币账户
            accountMap.put("masterEdition", accounts[6]);    // 主版本账户
            accountMap.put("printEdition", accounts[7]);     // 打印版本账户
        }
        info.put("accounts", accountMap);
        return info;
    }

    /**
     * 解析创建托管账户指令
     */
    private static Map<String, Object> parseCreateEscrowAccount(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 4) {
            accountMap.put("escrow", accounts[0]);         // 托管账户
            accountMap.put("metadata", accounts[1]);       // 元数据账户
            accountMap.put("mint", accounts[2]);          // 铸币账户
            accountMap.put("tokenAccount", accounts[3]);   // 代币账户
        }
        info.put("accounts", accountMap);
        return info;
    }

    /**
     * 解析关闭托管账户指令
     */
    private static Map<String, Object> parseCloseEscrowAccount(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 3) {
            accountMap.put("escrow", accounts[0]);      // 托管账户
            accountMap.put("metadata", accounts[1]);    // 元数据账户
            accountMap.put("mint", accounts[2]);       // 铸币账户
        }
        info.put("accounts", accountMap);
        return info;
    }

    /**
     * 解析从托管转出指令
     */
    private static Map<String, Object> parseTransferOutOfEscrow(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 4) {
            accountMap.put("escrow", accounts[0]);         // 托管账户
            accountMap.put("metadata", accounts[1]);       // 元数据账户
            accountMap.put("payer", accounts[2]);         // 支付账户
            accountMap.put("attributeMint", accounts[3]);  // 属性铸币账户
        }
        info.put("accounts", accountMap);
        return info;
    }

    /**
     * 解析销毁指令
     */
    private static Map<String, Object> parseBurn(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 4) {
            accountMap.put("authority", accounts[0]);     // 权限账户
            accountMap.put("collectionMetadata", accounts[1]); // 收藏集元数据账户
            accountMap.put("metadata", accounts[2]);      // 元数据账户
            accountMap.put("mint", accounts[3]);         // 铸币账户
        }
        info.put("accounts", accountMap);
        return info;
    }

    /**
     * 解析创建指令
     */
    private static Map<String, Object> parseCreate(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 4) {
            accountMap.put("authority", accounts[0]);     // 权限账户
            accountMap.put("mint", accounts[1]);         // 铸币账户
            accountMap.put("metadata", accounts[2]);      // 元数据账户
            accountMap.put("payer", accounts[3]);        // 支付账户
        }
        info.put("accounts", accountMap);
        return info;
    }

    /**
     * 解析铸造指令
     */
    private static Map<String, Object> parseMint(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 4) {
            accountMap.put("authority", accounts[0]);     // 权限账户
            accountMap.put("mint", accounts[1]);         // 铸币账户
            accountMap.put("metadata", accounts[2]);      // 元数据账户
            accountMap.put("tokenAccount", accounts[3]);  // 代币账户
        }
        info.put("accounts", accountMap);
        return info;
    }

    /**
     * 解析委托指令
     */
    private static Map<String, Object> parseDelegate(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 4) {
            accountMap.put("delegate", accounts[0]);      // 委托账户
            accountMap.put("metadata", accounts[1]);      // 元数据账户
            accountMap.put("mint", accounts[2]);         // 铸币账户
            accountMap.put("tokenAccount", accounts[3]);  // 代币账户
        }
        info.put("accounts", accountMap);
        return info;
    }

    /**
     * 解析撤销指令
     */
    private static Map<String, Object> parseRevoke(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 4) {
            accountMap.put("authority", accounts[0]);     // 权限账户
            accountMap.put("delegate", accounts[1]);      // 委托账户
            accountMap.put("metadata", accounts[2]);      // 元数据账户
            accountMap.put("mint", accounts[3]);         // 铸币账户
        }
        info.put("accounts", accountMap);
        return info;
    }

    /**
     * 解析锁定指令
     */
    private static Map<String, Object> parseLock(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 4) {
            accountMap.put("authority", accounts[0]);     // 权限账户
            accountMap.put("metadata", accounts[1]);      // 元数据账户
            accountMap.put("mint", accounts[2]);         // 铸币账户
            accountMap.put("tokenAccount", accounts[3]);  // 代币账户
        }
        info.put("accounts", accountMap);
        return info;
    }

    /**
     * 解析解锁指令
     */
    private static Map<String, Object> parseUnlock(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 4) {
            accountMap.put("authority", accounts[0]);     // 权限账户
            accountMap.put("metadata", accounts[1]);      // 元数据账户
            accountMap.put("mint", accounts[2]);         // 铸币账户
            accountMap.put("tokenAccount", accounts[3]);  // 代币账户
        }
        info.put("accounts", accountMap);
        return info;
    }

    /**
     * 解析迁移指令
     */
    private static Map<String, Object> parseMigrate(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 3) {
            accountMap.put("metadata", accounts[0]);      // 元数据账户
            accountMap.put("edition", accounts[1]);       // 版本账户
            accountMap.put("mint", accounts[2]);         // 铸币账户
        }
        info.put("accounts", accountMap);
        return info;
    }

    /**
     * 解析转账指令
     */
    private static Map<String, Object> parseTransfer(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 4) {
            accountMap.put("authority", accounts[0]);     // 权限账户
            accountMap.put("metadata", accounts[1]);      // 元数据账户
            accountMap.put("mint", accounts[2]);         // 铸币账户
            accountMap.put("tokenAccount", accounts[3]);  // 代币账户
        }
        info.put("accounts", accountMap);
        return info;
    }
    /**
     * 解析更新指令
     */
    private static Map<String, Object> parseUpdate(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 3) {
            accountMap.put("authority", accounts[0]);     // 权限账户
            accountMap.put("metadata", accounts[1]);      // 元数据账户
            accountMap.put("mint", accounts[2]);         // 铸币账户
        }
        info.put("accounts", accountMap);
        return info;
    }

    /**
     * 解析使用指令
     */
    private static Map<String, Object> parseUse(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 4) {
            accountMap.put("authority", accounts[0]);     // 权限账户
            accountMap.put("metadata", accounts[1]);      // 元数据账户
            accountMap.put("mint", accounts[2]);         // 铸币账户
            accountMap.put("tokenAccount", accounts[3]);  // 代币账户
        }
        info.put("accounts", accountMap);
        return info;
    }

    /**
     * 解析验证指令
     */
    private static Map<String, Object> parseVerify(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 4) {
            accountMap.put("authority", accounts[0]);     // 权限账户
            accountMap.put("metadata", accounts[1]);      // 元数据账户
            accountMap.put("collectionMint", accounts[2]); // 收藏集铸币账户
            accountMap.put("collectionMetadata", accounts[3]); // 收藏集元数据账户
        }
        info.put("accounts", accountMap);
        return info;
    }

    /**
     * 解析取消验证指令
     */
    private static Map<String, Object> parseUnverify(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 4) {
            accountMap.put("authority", accounts[0]);     // 权限账户
            accountMap.put("metadata", accounts[1]);      // 元数据账户
            accountMap.put("collectionMint", accounts[2]); // 收藏集铸币账户
            accountMap.put("collectionMetadata", accounts[3]); // 收藏集元数据账户
        }
        info.put("accounts", accountMap);
        return info;
    }

    /**
     * 解析收集指令
     */
    private static Map<String, Object> parseCollect(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 4) {
            accountMap.put("authority", accounts[0]);     // 权限账户
            accountMap.put("metadata", accounts[1]);      // 元数据账户
            accountMap.put("mint", accounts[2]);         // 铸币账户
            accountMap.put("tokenAccount", accounts[3]);  // 代币账户
        }
        info.put("accounts", accountMap);
        return info;
    }

    /**
     * 解析打印指令
     */
    private static Map<String, Object> parsePrint(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 4) {
            accountMap.put("authority", accounts[0]);     // 权限账户
            accountMap.put("metadata", accounts[1]);      // 元数据账户
            accountMap.put("masterEdition", accounts[2]); // 主版本账户
            accountMap.put("edition", accounts[3]);      // 版本账户
        }
        info.put("accounts", accountMap);
        return info;
    }

    /**
     * 解析调整大小指令
     */
    private static Map<String, Object> parseResize(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 3) {
            accountMap.put("metadata", accounts[0]);      // 元数据账户
            accountMap.put("mint", accounts[1]);         // 铸币账户
            accountMap.put("payer", accounts[2]);        // 支付账户
        }
        info.put("accounts", accountMap);
        return info;
    }

    /**
     * 解析关闭账户指令
     */
    private static Map<String, Object> parseCloseAccounts(ByteBuffer buffer, String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        Map<String, String> accountMap = new HashMap<>();
        if (accounts.length >= 3) {
            accountMap.put("metadata", accounts[0]);      // 元数据账户
            accountMap.put("authority", accounts[1]);     // 权限账户
            accountMap.put("mint", accounts[2]);         // 铸币账户
        }
        info.put("accounts", accountMap);
        return info;
    }

    /**
     * 解析默认账户列表
     */
    private static Map<String, Object> parseDefaultAccounts(String[] accounts) {
        Map<String, Object> info = new HashMap<>();
        Map<String, String> accountMap = new HashMap<>();

        // 添加更多调试信息
        info.put("warning", "Using default account parser - instruction may be unknown or not yet implemented");
        info.put("accountCount", accounts.length);

        // 记录所有账户
        for (int i = 0; i < accounts.length; i++) {
            accountMap.put("account_" + i, accounts[i]);
        }

        info.put("accounts", accountMap);
        return info;
    }

}