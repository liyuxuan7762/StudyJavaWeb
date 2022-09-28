package com.company;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class Test {

    public static void main(String[] args) throws Exception {
        Properties prop = new Properties();
        prop.load(new FileReader("C:\\Users\\李宇轩\\IdeaProjects\\javaweb\\druid.properties"));
        String username = prop.getProperty("username");
        String password = prop.getProperty("password");
        String url = prop.getProperty("url");
        Class.forName(prop.getProperty("className"));
        // 获取连接
        Connection connection = DriverManager.getConnection(url, username, password);
    }
}
