package org.gyf.dao;

import org.gyf.common.Info;


import java.sql.Connection;

/**
 * Dao类的接口，定义了增删查改方法
 */
public interface MysqlDao {
    //public static Connection MysqlConn = new MysqlConnection().getConnection();
    public boolean Insert(Info term);
    public boolean Update(Info term);
    public Info Query(String mainkey);
    public boolean Remove(String mainkey);
}
