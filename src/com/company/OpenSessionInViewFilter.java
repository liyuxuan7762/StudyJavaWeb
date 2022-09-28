package com.company;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("*.do")
public class OpenSessionInViewFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            TransactionManager.beginTrans();
            System.out.println("开启事务");
            filterChain.doFilter(servletRequest, servletResponse);
            TransactionManager.commit();
            System.out.println("提交事务");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            try {
                TransactionManager.rollback();
                System.out.println("回滚事务");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void destroy() {

    }
}
