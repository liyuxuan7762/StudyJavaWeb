package com.company;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;

@WebServlet("/del.do")
public class DelServlet extends ViewBaseServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 获取到ID
        int fid = Integer.parseInt(req.getParameter("fid"));
        Connection conn = BaseDAO.getConnection();
        FruitDAOImpl dao = new FruitDAOImpl();
        dao.deleteById(conn, fid);
        resp.sendRedirect("index");
    }
}
