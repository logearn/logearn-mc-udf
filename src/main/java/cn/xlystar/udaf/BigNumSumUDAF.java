package cn.xlystar.udaf;

//继承UDAF类。

import cn.xlystar.utils.ArithmeticUtils;
import com.aliyun.odps.io.Text;
import com.aliyun.odps.io.Writable;
import com.aliyun.odps.udf.Aggregator;
import com.aliyun.odps.udf.UDFException;
import com.aliyun.odps.udf.annotation.Resolve;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

//自定义Java类。
//@Resolve注解。
@Resolve("STRING,INT->STRING")
public class BigNumSumUDAF extends Aggregator {
    private final Logger log = LoggerFactory.getLogger(getClass());


    private static class SumBuffer implements Writable {
        // 交易子账户的金额
        private String sum = "0";
        private Integer scale = 128;

        @Override
        public void write(DataOutput out) throws IOException {
            out.writeUTF(sum);
            out.writeUTF(String.valueOf(scale));
        }

        @Override
        public void readFields(DataInput in) throws IOException {
            sum = in.readUTF();
            scale = Integer.valueOf(in.readUTF());
        }

        @Override
        public String toString() {
            return String.format("{sum=%s, scale=%d}", sum, scale);
        }
    }

    @Override
    public Writable newBuffer() {
        return new SumBuffer();
    }

    @Override
    public void iterate(Writable buffer, Writable[] args) throws UDFException {
        if (args[0] == null) {
            return;
        }
        String num = args[0].toString();
        int scale = Integer.parseInt(args[1].toString());
        SumBuffer buf = (SumBuffer) buffer;
        buf.scale = scale;
        buf.sum = ArithmeticUtils.add(buf.sum, num, scale);
        log.info("[iterate func]: buffer={}", buf);
    }

    @Override
    public Writable terminate(Writable buffer) throws UDFException {
        SumBuffer buf = (SumBuffer) buffer;
        return new Text(buf.sum);
    }

    @Override
    public void merge(Writable buffer, Writable partial) throws UDFException {
        SumBuffer buf = (SumBuffer) buffer;
        SumBuffer p = (SumBuffer) partial;
        buf.sum = ArithmeticUtils.add(buf.sum, p.sum).toPlainString();
        log.info("[merge func]: buffer={}, p={}", buf, p);
    }
}