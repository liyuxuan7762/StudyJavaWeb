package com.company;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;


public class IndexServelet extends ViewBaseServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 首先判断是否点击了search
        req.setCharacterEncoding("utf-8");
        HttpSession session = req.getSession();
        Integer pageNo = 1;
        String isSearch = req.getParameter("search"); // 如果issearch不为空，则为点击搜索的请求
        String keyword = null;
        if (isSearch != null && "yes".equals(isSearch)) {
            // 点击了搜索按钮
            // page应该归1 并获取keyword
            pageNo = 1;
            keyword = req.getParameter("keyword");
            if (keyword.isEmpty()) {
                keyword = "";
            }
            // 将关键字放入session 供分页时调用
            session.setAttribute("keyword", keyword);
        } else {
            // 不是点击了搜索
            if (req.getParameter("pageNo") != null) {
                pageNo = Integer.parseInt(req.getParameter("pageNo"));
            }
            Object keywordObj = session.getAttribute("keyword");
            if (keywordObj != null) {
                keyword = (String) keywordObj;
            } else {
                keyword = "";
            }
        }
        // 将pageNo存入session
        req.getSession().setAttribute("pageNo", pageNo);
        FruitDAOImpl dao = new FruitDAOImpl();
        Connection conn = BaseDAO.getConnection();
        int totalPage = dao.getCountByKeyword(conn, keyword).intValue();
        totalPage = (int) (Math.ceil(totalPage / 5) + 1);
        req.getSession().setAttribute("totalPage", totalPage);
        ArrayList<Fruit> fruits = dao.queryAllByKeyword(conn, pageNo, keyword);
        req.getSession().setAttribute("fruitList", fruits);
        super.processTemplate("index", req, resp);
    }
}
