package cn.xlystar.parse.ammswap;

import cn.xlystar.entity.TransferEvent;
import cn.xlystar.entity.UniswapEvent;
import lombok.Data;

import java.util.List;

@Data
public class Result {
    private List<UniswapEvent> uniswapEvents;
    private List<TransferEvent> transferEvents;
    private List<UniswapEvent> rawUniswapEvents;

    public Result(List<UniswapEvent> uniswapEvents, List<TransferEvent> transferEvents) {
        this.uniswapEvents = uniswapEvents;
        this.transferEvents = transferEvents;
    }

    public Result(List<UniswapEvent> uniswapEvents, List<UniswapEvent> rawUniswapEvents, List<TransferEvent> transferEvents) {
        this.uniswapEvents = uniswapEvents;
        this.transferEvents = transferEvents;
        this.rawUniswapEvents = rawUniswapEvents;
    }
}
