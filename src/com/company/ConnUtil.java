package com.company;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnUtil {
    private static ThreadLocal<Connection> threadLocal = new InheritableThreadLocal<>();
    public static Connection getConn() {
        Connection conn = threadLocal.get();
        if(conn == null) {
            conn = BaseDAO.getConnection();
            threadLocal.set(conn);
        }
        return threadLocal.get();
    }

    public static void closeConn() throws SQLException {
        Connection conn = threadLocal.get();
        if(conn == null) {
            return;
        }
        if(conn.isClosed()) {
            conn.close();
            threadLocal.set(null);
        }
    }
}
