package cn.xlystar.entity;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

@Data
@Builder
public class LiquidityEvent extends Event implements Serializable {

    // tx from
    private String caller;

    private String token0;
    private String token1;
    private BigInteger amount0;
    private BigInteger amount1;

    private BigInteger logIndex;
    private String poolAddress;

    // 添加还是删除，TODO，做成枚举常量
    private String eventType;
    private String protocol;
    private String version;

    // 扩展边都合并都 transfer, 记录一下，好排查bug, 但是不需要条到数据仓库
    private List<TransferEvent> mergedTransferEvent;
    private String errorMsg;
}
