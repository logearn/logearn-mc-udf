package cn.xlystar.constants;


public enum InsideContract {
    PUMP("6EF8rrecthR5Dkzon8Nwu78hRvfCKubJ14M5uBEwF6P"),
    Moonshot("MoonCVVNZFSYkqNXP6bxHLPL6QQJiMagDL3qcqUQTrG"),

    RaydiumLaunch("LanMV9sAd7wArD4vJFi2qDdfnVhFxYSUg6eADduJ3uj"),
    Launch_Bonk("FfYek5vEz23cMkWsdJwG2oa6EphsvXSHrGpdALN4g6W1"),
    Launch_Bonk2("BuM6KDpWiTcxvrpXywWFiw45R2RNH8WURdvqoTDV1BW4"),
    Launch_WenDev("9SkAtSxgNUMvT9bGb93v6rLU5MjW1XibykqoGtqT9dbg"),
    Launch_Labs("4Bu96XjU84XjPDSpveTVf6LYGCkfW5FK7SNkREWcEfV4"),

    Boop("boop8hVGQGqehUK2iVEMEnMrL5RbjywRzHKBmBE7ry4"),
    Heaven("HEAVENoP2qxoeuF8Dj2oT1GHEnu49U5mJYkdeC8BAX2o"),

    MeteoraDbc("dbcij3LWUppWqq96dh6gJWwBifmcGfLSB5D4DuSMaqN"),
    Dbc_Dbc("Bov7gMQ88BQbtFMTxQ5e8grtrwG4ryQGAV9Mih2j9SxK"),
    Dbc_Bags("2BH8m9E2mE6cc86GLjfodYvRn2P7LQ9ge8fN2u7q19dt"),
    Dbc_Moonshot("FbKf76ucsQssF7XZBuzScdJfugtsSKwZFYztKsMEhWZM"),
    Dbc_AnoncoinIt("GybkUNYVNk1FZMt9myAfvpSVgoKBgaueMTvszwBN4qYx"),
    Dbc_Studio("EQMdk2NDUrdCrWR2avdLcmPmtmGePiTz4LzSRuLew61P"),
    Dbc_TrendFun("7UNpFBfTdWrcfS7aBQzEaPgZCfPJe8BDgHzwmWUZaMaF"),

    BSCFlapAliseMEME("flap"),
    BSCFlapMEME("0xe2ce6ab80874fa9fa2aae65d277dd6b8e65c9de0"),
    BSCFlapProxyMEME("0x1de460f363af910f51726def188f9004276bf4bc"),
    BSCFourMEME1("0xec4549cadce5da21df6e6422d448034b5233bfbc"),
    BSCFourMEME2("0x5c952063c7fc8610ffdb798152d69f0b9550762b"),
    BSCFourMEMEAlise1("four.meme1"),
    BSCFourMEMEAlise2("four.meme2"),
    BSCFourMEMEAlise("four.meme"),
    BSCBinanceFourMEMEAlise("binance_four.meme");

    public final String contractAddress;

    InsideContract(String contractAddress) {
        this.contractAddress = contractAddress;
    }
}
