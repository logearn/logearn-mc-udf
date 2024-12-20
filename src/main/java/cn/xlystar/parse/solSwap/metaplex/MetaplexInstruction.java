package cn.xlystar.parse.solSwap.metaplex;

/**
 * Metaplex Token Metadata Program (metaqbxxUerdq28cj1RbAWkYQm3ybzjb6a8bt518x1s)
 *
 * Verified against:
 * - Contract source: https://github.com/metaplex-foundation/metaplex-program-library/tree/master/token-metadata/program
 * - SDK: https://github.com/metaplex-foundation/js
 * - Docs: https://docs.metaplex.com/programs/token-metadata/
 * - IDL: https://github.com/metaplex-foundation/metaplex-program-library/blob/master/token-metadata/js/idl/mpl_token_metadata.json
 */
public enum MetaplexInstruction {
    CreateMetadataAccount(0),              // 创建元数据账户
    UpdateMetadataAccount(1),              // 更新元数据账户
    DeprecatedCreateMasterEdition(2),      // 已弃用的创建主版本
    DeprecatedMintNewEditionFromMasterEditionViaPrintingToken(3), // 已弃用的通过打印代币从主版本铸造新版本
    UpdatePrimarySaleHappenedViaToken(4),  // 通过代币更新主要销售状态
    DeprecatedSetReservationList(5),       // 已弃用的设置预约列表
    DeprecatedCreateReservationList(6),    // 已弃用的创建预约列表
    SignMetadata(7),                       // 签名元数据
    DeprecatedMintPrintingTokensViaToken(8), // 已弃用的通过代币铸造打印代币
    DeprecatedMintPrintingTokens(9),       // 已弃用的铸造打印代币
    CreateMasterEdition(10),               // 创建主版本
    MintNewEditionFromMasterEditionViaToken(11), // 通过代币从主版本铸造新版本
    ConvertMasterEditionV1ToV2(12),        // 将主版本V1转换为V2
    MintNewEditionFromMasterEditionViaVaultProxy(13), // 通过保管库代理从主版本铸造新版本
    PuffMetadata(14),                      // 填充元数据
    UpdateMetadataAccountV2(15),           // 更新元数据账户V2
    CreateMetadataAccountV2(16),           // 创建元数据账户V2
    CreateMasterEditionV3(17),             // 创建主版本V3
    VerifyCollection(18),                  // 验证收藏集
    Utilize(19),                           // 使用
    ApproveUseAuthority(20),              // 批准使用权限
    RevokeUseAuthority(21),               // 撤销使用权限
    UnverifyCollection(22),               // 取消验证收藏集
    ApproveCollectionAuthority(23),       // 批准收藏集权限
    RevokeCollectionAuthority(24),        // 撤销收藏集权限
    SetAndVerifyCollection(25),           // 设置并验证收藏集
    FreezeDelegatedAccount(26),           // 冻结委托账户
    ThawDelegatedAccount(27),             // 解冻委托账户
    RemoveCreatorVerification(28),        // 移除创建者验证
    BurnNft(29),                          // 销毁NFT
    VerifySizedCollectionItem(30),        // 验证有大小的收藏集项目
    UnverifySizedCollectionItem(31),      // 取消验证有大小的收藏集项目
    SetAndVerifySizedCollectionItem(32),  // 设置并验证有大小的收藏集项目
    CreateMetadataAccountV3(33),          // 创建元数据账户V3
    SetCollectionSize(34),                // 设置收藏集大小
    SetTokenStandard(35),                 // 设置代币标准
    BubblegumSetCollectionSize(36),       // Bubblegum设置收藏集大小
    BurnEditionNft(37),                   // 销毁版本NFT
    CreateEscrowAccount(38),              // 创建托管账户
    CloseEscrowAccount(39),               // 关闭托管账户
    TransferOutOfEscrow(40),              // 从托管转出
    Burn(41),                             // 销毁
    Create(42),                           // 创建
    Mint(43),                             // 铸造
    Delegate(44),                         // 委托
    Revoke(45),                           // 撤销
    Lock(46),                             // 锁定
    Unlock(47),                           // 解锁
    Migrate(48),                          // 迁移
    Transfer(49),                         // 转账
    Update(50),                           // 更新
    Use(51),                              // 使用
    Verify(52),                           // 验证
    Unverify(53),                         // 取消验证
    Collect(54),                          // 收集
    Print(55),                            // 打印
    Resize(56),                           // 调整大小
    CloseAccounts(57);                    // 关闭账户

    private final int value;

    MetaplexInstruction(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static MetaplexInstruction fromValue(int value) {
        for (MetaplexInstruction type : values()) {
            if (type.value == value) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown MetaplexInstruction value: " + value);
    }
}