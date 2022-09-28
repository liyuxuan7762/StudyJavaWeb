package com.company;

import javax.sql.DataSource;
import java.io.FileReader;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

public class BaseDAO {
    // 使用连接池获取连接
    private static DataSource dataSource;

    // 获取连接
    public static Connection getConnection() {
        try {
            Properties prop = new Properties();
            prop.load(new FileReader("C:\\Users\\李宇轩\\IdeaProjects\\StudyJavaWeb\\druid.properties"));
            String username = prop.getProperty("username");
            String password = prop.getProperty("password");
            String url = prop.getProperty("url");
            Class.forName(prop.getProperty("className"));
            // 获取连接
            Connection connection = DriverManager.getConnection(url, username, password);
            return connection;
        } catch (Exception e) {
            e.printStackTrace();
            throw new FruitException("BaseDAO出错");
        }
    }

    // 关闭连接
    public static void closeResource(Connection conn, PreparedStatement ps, ResultSet rs) {
        try {
            if (conn != null)
                conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (ps != null)
                ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (rs != null)
                rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void closeResource(Connection conn, PreparedStatement ps) {
        try {
            if (conn != null)
                conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (ps != null)
                ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 考虑事务后的增删改
    public int updateFruit(Connection conn, String sql, Object... args) {
        PreparedStatement ps = null;
        try {
            // 获取ps
            ps = conn.prepareStatement(sql);
            // 设置参数
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            // 执行sql
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw new FruitException("BaseDAO出错");
        } finally {
            closeResource(null, ps);
        }
    }

    // 考虑事务后的返回一条结果的查询
    public <T> T querySingle(Connection conn, Class<T> clazz, String sql, Object... args) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            // 获取PS
            ps = conn.prepareStatement(sql);
            // 设置参数
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            // 执行sql
            rs = ps.executeQuery();
            ResultSetMetaData metaData = rs.getMetaData();
            // 处理结果集
            if (rs.next()) {
                // 反射创建对象
                T t = clazz.getDeclaredConstructor().newInstance();
                for (int i = 0; i < metaData.getColumnCount(); i++) {
                    Object value = rs.getObject(i + 1);
                    String columnLabel = metaData.getColumnLabel(i + 1);
                    // 获取字段
                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t, value);
                }
                return t;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new FruitException("BaseDAO出错");
        } finally {
            closeResource(null, ps, rs);
        }
        return null;
    }

    // 返回一个结果列表
    public <T> ArrayList<T> queryList(Connection conn, Class<T> clazz, String sql, Object... args) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            rs = ps.executeQuery();
            ResultSetMetaData metaData = rs.getMetaData();
            ArrayList<T> list = new ArrayList<T>();
            while (rs.next()) {
                T t = clazz.getDeclaredConstructor().newInstance();
                for (int i = 0; i < metaData.getColumnCount(); i++) {
                    Object value = rs.getObject(i + 1);
                    String columnLabel = metaData.getColumnLabel(i + 1);
                    Field declaredField = clazz.getDeclaredField(columnLabel);
                    declaredField.setAccessible(true);
                    declaredField.set(t, value);
                }
                list.add(t);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            throw new FruitException("BaseDAO出错");
        } finally {
            closeResource(null, ps, rs);
        }
    }

    // 返回聚合函数
    public <T> T querySingleValue(Connection conn, String sql, Object... args) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            rs = ps.executeQuery();
            if (rs.next()) {
                return (T) rs.getObject(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new FruitException("BaseDAO出错");
        } finally {
            closeResource(null, ps, rs);
        }
        return null;
    }
}
