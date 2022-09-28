package com.company;

import java.sql.Connection;
import java.util.ArrayList;

public class FruitServiceImpl implements FruitService {

    private FruitDAOImpl fruitDAO = null;

    @Override
    public void insertFruit(Connection conn, Fruit fruit) {
        fruitDAO.insert(conn, fruit);
    }

    @Override
    public void deleteFruit(Connection conn, int fid) {
        fruitDAO.deleteById(conn, fid);
    }

    @Override
    public void updateFruit(Connection conn, Fruit fruit) {
        fruitDAO.update(conn, fruit);
    }

    @Override
    public ArrayList<Fruit> queryFruit(Connection conn, int pageNo, String keyword) {
        return fruitDAO.queryAllByKeyword(conn, pageNo, keyword);
    }

    @Override
    public int getPageCount(Connection conn, String keyword) {
        int totalPage = fruitDAO.getCountByKeyword(conn, keyword).intValue();
        if (totalPage % 5 == 0) {
            totalPage = totalPage / 5;
        } else {
            totalPage = (int) (Math.ceil(totalPage / 5) + 1);
        }

        return totalPage;
    }

    @Override
    public Fruit queryById(Connection conn, int fid) {
        return fruitDAO.queryById(conn, fid);
    }
}
