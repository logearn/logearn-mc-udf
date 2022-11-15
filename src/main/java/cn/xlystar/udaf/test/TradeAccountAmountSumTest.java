package cn.xlystar.udaf.test;

import com.aliyun.odps.udf.local.datasource.InputSource;
import com.aliyun.odps.udf.local.datasource.TableInputSource;
import com.aliyun.odps.udf.local.runner.AggregatorRunner;
import com.aliyun.odps.udf.local.runner.BaseRunner;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

public class TradeAccountAmountSumTest {

    @BeforeClass
    public static void initWarehouse() {
        TestUtil.initWarehouse();
    }

    @Test
    public void simpleInput() throws Exception {
        BaseRunner runner = new AggregatorRunner(null,
                "cn.xlystar.udaf.TradeAccountAmountSum");
        runner.feed(new Object[]{"Buy", "1", "0"})
                .feed(new Object[]{"Buy", "2", "0"})
                .feed(new Object[]{"Buy", "3", "0"});
        List<Object[]> out = runner.yield();
        Assert.assertEquals(1, out.size());
        Assert.assertTrue("6".compareTo(out.get(0)[0].toString()) == 0);
    }


    @Test
    public void simpleInput2() throws Exception {
        BaseRunner runner = new AggregatorRunner(null,
                "cn.xlystar.udaf.TradeAccountAmountSum");
        runner.feed(new Object[]{"Base", "20", "10"})
                .feed(new Object[]{"Buy", "2", "0"})
                .feed(new Object[]{"Buy", "3", "0"});
        List<Object[]> out = runner.yield();
        Assert.assertEquals(1, out.size());
        Assert.assertTrue("15".compareTo(out.get(0)[0].toString()) == 0);
    }

    @Test
    public void inputFromTable() throws Exception {
        BaseRunner runner = new AggregatorRunner(TestUtil.getOdps(),
                "cn.xlystar.udaf.TradeAccountAmountSum");
        // partition table
        String project = "example_project";
        String table = "tx_in2";
        String[] partitions = null; // new String[]{"p2=1", "p1=2"};
        String[] columns = new String[]{"type", "amount", "tradeAccountAmount"};
        InputSource inputSource = new TableInputSource(project, table, partitions, columns);
        Object[] data;
        while ((data = inputSource.getNextRow()) != null) {
            runner.feed(data);
        }
        List<Object[]> out = runner.yield();
        Assert.assertEquals(1, out.size());
        Assert.assertTrue("6".compareTo(out.get(0)[0].toString()) == 0);
    }


    @Test
    public void inputFromTableTxIn3() throws Exception {
        BaseRunner runner = new AggregatorRunner(TestUtil.getOdps(),
                "cn.xlystar.udaf.TradeAccountAmountSum");
        // partition table
        String project = "example_project";
        String table = "tx_in3";
        String[] partitions = null;
        String[] columns = new String[]{"type", "amount", "tradeAccountAmount"};
        InputSource inputSource = new TableInputSource(project, table, partitions, columns);
        Object[] data;
        while ((data = inputSource.getNextRow()) != null) {
            runner.feed(data);
        }
        List<Object[]> out = runner.yield();
        Assert.assertEquals(1, out.size());
//        Assert.assertTrue(bd("6").compareTo(bd(out.get(0)[0].toString())) == 0);
    }

//    @Test
//    public void resourceTest() throws Exception {
//        BaseRunner runner = new AggregatorRunner(TestUtil.getOdps(),
//                "com.aliyun.odps.examples.udf.UDAFResource");
//        runner.feed(new Object[]{"one", "one"}).feed(new Object[]{"three", "three"})
//                .feed(new Object[]{"four", "four"});
//        List<Object[]> out = runner.yield();
//        Assert.assertEquals(1, out.size());
//        // 24+3+4+4
//        Assert.assertEquals(35L, out.get(0)[0]);
//    }

}