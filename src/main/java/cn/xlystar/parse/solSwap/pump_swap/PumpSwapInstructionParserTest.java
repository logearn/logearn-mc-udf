package cn.xlystar.parse.solSwap.pump_swap;

public class PumpSwapInstructionParserTest {


    public static void main(String[] args) {
        System.out.println("开始测试 PumpSwap 指令解析器...\n");
        
        testTPumpSwapOperations();

        System.out.println("\n所有测试完成!");
    }

    private static void testTPumpSwapOperations() {
        System.out.println("\n=== 测试 swap 操作 ===");


        // ✅ buy
        // String tx_hash6 = "5dJABuWZi5DR3SZhZ5ndKCSDisnzxPUnYfCTRdzyRFbpWCNBac7EnobXbGFG7ZefpRerXNL3aqWEfi8FFnH1bhg1";
        // String[] accounts6 = "51V3VBfMwkNbUMSHDzKC882LuYdR8Hgi3fNtYJyxz8xQ, 36Eru7v11oU5Pfrojyn5oY3nETA1a1iqsw2WUu6afkM9, 11111111111111111111111111111111".split(", ");
        // String base58Data6 = "0deca4ad6afda4b901a781bf533a7325f9acbf0407e2d8a80768e43aad2145edcd445466253da0b12e01ac2e81c51d4fdfff8dfd1f1a8289a95fd12322366e25e5c716ce679cd50af7cc013b8e74b3d893c946d9255772aa50dabf13cb196e7b1fbebb05dd50ffb17b61a101401035d35abb53125d91e9b3003cb8b8bf465665bf0145eb0b99f883d1d0c019012b3353ce467dfb7a3b9c972c1d3cac5fa70fda60cc23c3ea1e77fd293a655dc901640001500104000000000000000100c817a8040000000100010601090180969800000000000100ca9a3b00000000010a000000";
        // testInstruction("CONFIG_INIT", base58Data6, accounts6, tx_hash6);

        // 目前找不到 测试数据， due 上面还没解析这个 合约， 看看，怎么拿真是数据去测试意义

    }


} 