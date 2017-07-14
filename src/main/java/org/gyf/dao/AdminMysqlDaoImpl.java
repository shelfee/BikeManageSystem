package org.gyf.dao;

import org.gyf.common.AdminInfo;
import org.gyf.common.Info;
import org.gyf.servlet.SessionUserListener;

import java.sql.*;

/**
 * 对AdminInfo表查询操作
 * 只有管理者调用
 * 只实现了查询功能，用于管理者登录使用
 * 不提供增改删接口
 */
public class AdminMysqlDaoImpl implements MysqlDao {
    public boolean Insert(Info term) {
        return false;
    }

    public boolean Update(Info term) {
        return false;
    }

    public Info Query(String mainkey) {
        String QueryCmd = "SELECT * FROM AdminInfo WHERE adminID=?";
        Connection MysqlConn = new MysqlConnection().getConnection();
        try {
            PreparedStatement QueryStmt = MysqlConn.prepareStatement(QueryCmd);
            QueryStmt.setString(1, mainkey);
            ResultSet result = QueryStmt.executeQuery();
            if (!result.next()) {
                MysqlConn.close();
                return null;
            }
            else{
                String adminPassword=new String(result.getString("adminPassword"));
                AdminInfo adminInfo = new AdminInfo();
                adminInfo.setID(mainkey);
                adminInfo.setPassword(adminPassword);
                return adminInfo;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean Remove(String mainkey) {
        return false;
    }
}
