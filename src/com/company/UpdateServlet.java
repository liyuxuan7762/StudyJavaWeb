package com.company;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;

@WebServlet("/update.do")
public class UpdateServlet extends ViewBaseServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 获取数据
        req.setCharacterEncoding("utf-8");
        int fid = Integer.parseInt(req.getParameter("fid"));
        String fname = req.getParameter("fname");
        int price = Integer.parseInt(req.getParameter("price"));
        int fcount = Integer.parseInt(req.getParameter("fcount"));
        String remark = req.getParameter("remark");
        // 封装对象
        Fruit f = new Fruit(fid, fname, price, fcount, remark);
        Connection conn = BaseDAO.getConnection();
        FruitDAOImpl dao = new FruitDAOImpl();
        dao.update(conn, f);
        resp.sendRedirect("index");
    }
}
