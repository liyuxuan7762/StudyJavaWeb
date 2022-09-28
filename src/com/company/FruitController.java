package com.company;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;

public class FruitController {
    private FruitService fruitService = null;

    private String add(String fname, Integer price, Integer fcount, String remark) {
        Fruit f = new Fruit(0, fname, price, fcount, remark);
        fruitService.insertFruit(BaseDAO.getConnection(), f);
        return "redirect:fruit.do";
    }

    private String delete(Integer fid) {
        // 获取到ID
        Connection conn = BaseDAO.getConnection();
        fruitService.deleteFruit(conn, fid);
        return "redirect:fruit.do";
    }

    private String edit(Integer fid, HttpServletRequest req) {
        // 获取数据
        // 在这里接收到fid 通过fid查询出水果的信息然后显示在修改页面上
        // 根据fid查询
        Connection conn = BaseDAO.getConnection();
        Fruit fruit = fruitService.queryById(conn, fid);
        req.setAttribute("fruit", fruit);
        return "edit";
    }

    private String update(Integer fid, String fname, Integer price, Integer fcount, String remark) {
        // 获取数据
        // 封装对象
        Fruit f = new Fruit(fid, fname, price, fcount, remark);
        Connection conn = BaseDAO.getConnection();
        fruitService.updateFruit(conn, f);
        return "redirect:fruit.do";
    }

    protected String index(HttpSession session, Integer pageNo, String isSearch, HttpServletRequest req) {
        if (pageNo == null) {
            pageNo = 1;
        }
        // 首先判断是否点击了search
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
        Connection conn = BaseDAO.getConnection();
        int totalPage = fruitService.getPageCount(conn, keyword);

        req.getSession().setAttribute("totalPage", totalPage);
        ArrayList<Fruit> fruits = fruitService.queryFruit(conn, pageNo, keyword);
        req.getSession().setAttribute("fruitList", fruits);
        return "index";
    }
}
