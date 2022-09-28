package com.company;

import java.sql.Connection;
import java.sql.SQLException;

public class TransactionManager {

    private ThreadLocal<Connection> threadLocal = new InheritableThreadLocal<>();

    public static void beginTrans() throws SQLException {
        ConnUtil.getConn().setAutoCommit(false);
    }

    public static void commit() throws SQLException {
        ConnUtil.getConn().commit();
        ConnUtil.closeConn();
    }

    public static void rollback() throws SQLException {
        ConnUtil.getConn().rollback();
        ConnUtil.closeConn();
    }
}
