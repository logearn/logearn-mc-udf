package cn.xlystar.parse.solSwap.metaplex;

/**
 * Metaplex Token Metadata Program (metaqbxxUerdq28cj1RbAWkYQm3ybzjb6a8bt518x1s)
 *
 * 参考:
 * - IDL: src/main/java/cn/xlystar/parse/solSwap/metaplax/idl.json
 *
 * data 布局说明:
 * 1. 第一个字节为指令类型(discriminant)
 * 2. 后续字节为指令参数
 */
public enum MetadataInstruction {
    CreateMetadataAccount(0),
    UpdateMetadataAccount(1),
    CreateMasterEdition(10),
    MintNewEditionFromMasterEditionViaToken(11),
    UpdateMetadataAccountV2(15),
    CreateMetadataAccountV2(16),
    CreateMasterEditionV3(17), 
    CreateMetadataAccountV3(33),
    Burn(41),
    Create(42),
    Mint(43),
    Migrate(48),
    Transfer(49),
    Update(50);

    // 以下是弃用的指令， 目前没解析
    // DeprecatedCreateMasterEdition(2),
    // DeprecatedMintNewEditionFromMasterEditionViaPrintingToken(3),
    // UpdatePrimarySaleHappenedViaToken(4),
    // DeprecatedSetReservationList(5),
    // DeprecatedCreateReservationList(6),
    // SignMetadata(7),
    // DeprecatedMintPrintingTokensViaToken(8),
    // DeprecatedMintPrintingTokens(9),
    // ConvertMasterEditionV1ToV2(12),
    // MintNewEditionFromMasterEditionViaVaultProxy(13),
    // PuffMetadata(14),
    // VerifyCollection(18),
    // Utilize(19),
    // ApproveUseAuthority(20),
    // RevokeUseAuthority(21),
    // UnverifyCollection(22),
    // ApproveCollectionAuthority(23),
    // RevokeCollectionAuthority(24),
    // SetAndVerifyCollection(25),
    // FreezeDelegatedAccount(26),
    // ThawDelegatedAccount(27),
    // RemoveCreatorVerification(28),
    // BurnNft(29),
    // VerifySizedCollectionItem(30),
    // UnverifySizedCollectionItem(31),
    // SetAndVerifySizedCollectionItem(32),
    // SetCollectionSize(34),
    // SetTokenStandard(35),
    // BubblegumSetCollectionSize(36),
    // BurnEditionNft(37),
    // CreateEscrowAccount(38),
    // CloseEscrowAccount(39),
    // TransferOutOfEscrow(40),
    // Delegate(44),
    // Revoke(41),
    // Lock(42),
    // Unlock(43),
    // Use(47),
    // Verify(48),
    // Collect(50),
    // Print(51),
    // Resize(52),
    // Unverify(53),
    // CloseAccounts(54);

    private final int value;

    MetadataInstruction(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static MetadataInstruction fromValue(int value) {
        for (MetadataInstruction type : MetadataInstruction.values()) {
            if (type.value == value) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown instruction type: " + value);
    }
}
