package com.company;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;

@WebServlet("/edit.do")
public class EditServlet extends ViewBaseServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 在这里接收到fid 通过fid查询出水果的信息然后显示在修改页面上
        int fid = Integer.parseInt(req.getParameter("fid"));
        // 根据fid查询
        Connection conn = BaseDAO.getConnection();
        FruitDAOImpl dao = new FruitDAOImpl();
        Fruit fruit = dao.queryById(conn, fid);
        req.setAttribute("fruit", fruit);
        processTemplate("edit", req, resp);
    }
}
