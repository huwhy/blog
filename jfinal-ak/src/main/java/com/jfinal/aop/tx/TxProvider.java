package com.jfinal.aop.tx;

public interface TxProvider {
    void beginTx(int txLevel);

    void endTx(boolean result);
}