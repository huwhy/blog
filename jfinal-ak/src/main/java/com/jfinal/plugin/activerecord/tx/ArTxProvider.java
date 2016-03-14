package com.jfinal.plugin.activerecord.tx;

import java.sql.Connection;
import java.sql.SQLException;

import com.jfinal.aop.tx.TxProvider;
import com.jfinal.plugin.activerecord.ActiveRecordException;
import com.jfinal.plugin.activerecord.DbKit;

public class ArTxProvider implements TxProvider {
    @Override
    public void beginTx(int txLevel) {
        Connection conn = DbKit.getConfig().getThreadLocalConnection();
        try {
            if (conn != null) { // Nested transaction support
                if (conn.getTransactionIsolation() < txLevel)
                    conn.setTransactionIsolation(txLevel);
            } else {
                conn = DbKit.getConfig().getDataSource().getConnection();
                DbKit.getConfig().setThreadLocalConnection(conn);
                conn.setTransactionIsolation(txLevel);
            }
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            throw new ActiveRecordException(e);
        }
    }

    @Override
    public void endTx(boolean result) {
        Connection conn = DbKit.getConfig().getThreadLocalConnection();
        try {
            if (result)
                conn.commit();
            else
                conn.rollback();
        } catch (Exception e) {
            if (conn != null)
                try {
                    conn.rollback();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                DbKit.getConfig().removeThreadLocalConnection(); // prevent memory leak
            }
        }
    }
}