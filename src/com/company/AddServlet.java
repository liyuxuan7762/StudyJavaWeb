package com.company;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

@WebServlet("/add.do")
public class AddServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        String fname = req.getParameter("fname");
        int price = Integer.parseInt(req.getParameter("price"));
        int fcount = Integer.parseInt(req.getParameter("fcount"));
        String remark = req.getParameter("remark");
        Fruit f = new Fruit(0, fname, price, fcount, remark);
        FruitDAOImpl dao = new FruitDAOImpl();
        dao.insert(BaseDAO.getConnection(), f);
        resp.sendRedirect("index");
    }

}
