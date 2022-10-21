package cn.xlystar.udaf.test;

import com.aliyun.odps.udf.local.datasource.InputSource;
import com.aliyun.odps.udf.local.datasource.TableInputSource;
import com.aliyun.odps.udf.local.runner.AggregatorRunner;
import com.aliyun.odps.udf.local.runner.BaseRunner;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

public class AggrAvgTest {

    @BeforeClass
    public static void initWarehouse() {
        TestUtil.initWarehouse();
    }

    @Test
    public void simpleInput() throws Exception {
        BaseRunner runner = new AggregatorRunner(null,
                "cn.xlystar.udaf.AmountSum");
        runner.feed(new Object[]{"Buy", 1.0}).feed(new Object[]{"Buy", 2.0})
                .feed(new Object[]{"Buy", 3.0});
        List<Object[]> out = runner.yield();
        Assert.assertEquals(1, out.size());
        Assert.assertEquals(6D, out.get(0)[0]);
    }

    @Test
    public void inputFromTable() throws Exception {
        BaseRunner runner = new AggregatorRunner(TestUtil.getOdps(),
                "cn.xlystar.udaf.AmountSum");
        // partition table
        String project = "example_project";
        String table = "tx_in";
        String[] partitions = null; // new String[]{"p2=1", "p1=2"};
        String[] columns = new String[]{"type", "amount"};
        InputSource inputSource = new TableInputSource(project, table, partitions, columns);
        Object[] data;
        while ((data = inputSource.getNextRow()) != null) {
            runner.feed(data);
        }
        List<Object[]> out = runner.yield();
        Assert.assertEquals(1, out.size());
        Assert.assertEquals(6D, out.get(0)[0]);
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