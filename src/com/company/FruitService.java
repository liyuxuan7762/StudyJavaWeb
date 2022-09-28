package com.company;

import java.sql.Connection;
import java.util.ArrayList;

public interface FruitService {
    // 添加水果
    void insertFruit(Connection conn, Fruit fruit);
    // 删除水果
    void deleteFruit(Connection conn, int fid);
    // 修改水果
    void updateFruit(Connection conn, Fruit fruit);
    // 查询所有水果 根据页码
    ArrayList<Fruit> queryFruit(Connection conn,int pageNo, String keyword);
    // 查询页数
    int getPageCount(Connection conn, String keyword);
    // 根据fid查找水果
    Fruit queryById(Connection conn, int fid);
}
