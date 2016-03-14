package com.jfinal.aop.tx;

    public class AtomExecutor {

    public AtomExecutor(TxProvider txProvider){
        this.txProvider = txProvider;
    }

    private TxProvider txProvider;

    public void call(int txLevel, Atom atom) throws Exception {
        txProvider.beginTx(txLevel);
        boolean result = true;
        try {
            result = atom.run();
        }catch (Exception e){
            result = false;
            throw e;
        }finally {
            txProvider.endTx(result);
        }
    }
}