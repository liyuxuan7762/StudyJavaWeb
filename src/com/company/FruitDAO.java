package com.company;

import java.sql.Connection;
import java.util.ArrayList;

public interface FruitDAO {
    // 添加水果对象
    int insert(Connection conn, Fruit fruit);

    // 删除水果
    int deleteById(Connection conn, int id);

    // 修改水果信息
    int update(Connection conn, Fruit fruit);

    // 根据ID查询水果
    Fruit queryById(Connection conn, int id);

    // 查询所有水果
    ArrayList<Fruit> queryAll(Connection conn);

    // 查询水果数量
    Long getCount(Connection conn);

    // 根据关键字查询总数量
    Long getCountByKeyword(Connection conn, String keyword);

    // 分页查询
    ArrayList<Fruit> queryAll(Connection conn, int pageNo);

    // 根据关键字查询
    ArrayList<Fruit> queryAllByKeyword(Connection conn, int pageNo, String keyword);

}
