package com.company;

import java.sql.Connection;
import java.util.ArrayList;

public class FruitDAOImpl extends BaseDAO implements FruitDAO {
    @Override
    public int insert(Connection conn, Fruit fruit) {
        String sql = "insert into t_fruit (fname, price, fcount, remark) values (?, ?, ?, ?)";
        return updateFruit(conn, sql, fruit.getFname(), fruit.getPrice(), fruit.getFcount(), fruit.getRemark());
    }

    @Override
    public int deleteById(Connection conn, int fid) {
        String sql = "delete from t_fruit where fid = ?";
        return updateFruit(conn, sql, fid);
    }

    @Override
    public int update(Connection conn, Fruit fruit) {
        String sql = "update t_fruit set fname = ?, price = ?, fcount = ?, remark = ? where fid = ?";
        return updateFruit(conn, sql, fruit.getFname(), fruit.getPrice(), fruit.getFcount(), fruit.getRemark(), fruit.getFid());
    }

    @Override
    public Fruit queryById(Connection conn, int id) {
        String sql = "select fid, fname, price, fcount, remark from t_fruit where fid = ?";
        return querySingle(conn, Fruit.class, sql, id);
    }

    @Override
    public ArrayList<Fruit> queryAll(Connection conn) {
        String sql = "select fid, fname, price, fcount, remark from t_fruit";
        return queryList(conn, Fruit.class, sql);
    }

    @Override
    public Long getCount(Connection conn) {
        String sql = "select count(*) from t_fruit";
        return (Long) querySingleValue(conn, sql);
    }

    @Override
    public Long getCountByKeyword(Connection conn, String keyword) {
        String sql = "select count(*) from t_fruit where fname like ?";
        return (Long) querySingleValue(conn, sql, "%" + keyword + "%");
    }

    @Override
    public ArrayList<Fruit> queryAll(Connection conn, int pageNo) {
        String sql = "select fid, fname, price, fcount, remark from t_fruit limit ?, 5";
        return queryList(conn, Fruit.class, sql, (pageNo - 1)*5);
    }

    @Override
    public ArrayList<Fruit> queryAllByKeyword(Connection conn, int pageNo, String keyword) {
        String sql = "select fid, fname, price, fcount, remark from t_fruit where fname like ? limit ?, 5";
        return queryList(conn, Fruit.class, sql, "%" + keyword + "%", (pageNo - 1)*5);
    }
}
