package cn.xlystar.parse.ammswap;

import cn.xlystar.entity.TransferEvent;
import cn.xlystar.entity.UniswapEvent;

import java.util.List;

public class Result {
    private List<UniswapEvent> uniswapEvents;
    private List<TransferEvent> transferEvents;

    public Result(List<UniswapEvent> uniswapEvents, List<TransferEvent> transferEvents) {
        this.uniswapEvents = uniswapEvents;
        this.transferEvents = transferEvents;
    }

    public List<UniswapEvent> getUniswapEvents() {
        return uniswapEvents;
    }

    public List<TransferEvent> getTransferEvents() {
        return transferEvents;
    }
}
