package cn.xlystar.udaf;

//继承UDAF类。

import com.aliyun.odps.io.DoubleWritable;
import com.aliyun.odps.io.Writable;
import com.aliyun.odps.udf.Aggregator;
import com.aliyun.odps.udf.UDFException;
import com.aliyun.odps.udf.annotation.Resolve;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

//自定义Java类。
//@Resolve注解。
@Resolve("double->double")
public class SimpleSum extends Aggregator {

    private DoubleWritable ret = new DoubleWritable();

    @Override
    public Writable newBuffer() {
        return new DoubleWritable();
    }

    @Override
    public void iterate(Writable buffer, Writable[] args) throws UDFException {
        DoubleWritable arg = (DoubleWritable) args[0];
        DoubleWritable buf = (DoubleWritable) buffer;
        if (arg != null) {
            buf.set(buf.get() + arg.get());
        }
    }

    @Override
    public Writable terminate(Writable buffer) throws UDFException {
        DoubleWritable buf = (DoubleWritable) buffer;
        return buf;
    }

    @Override
    public void merge(Writable buffer, Writable partial) throws UDFException {
        DoubleWritable buf = (DoubleWritable) buffer;
        DoubleWritable p = (DoubleWritable) partial;
        buf.set(buf.get() + p.get());
    }
}