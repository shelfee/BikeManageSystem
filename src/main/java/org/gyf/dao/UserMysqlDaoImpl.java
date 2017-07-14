package org.gyf.dao;

import org.gyf.common.Info;
import org.gyf.common.UserInfo;

import java.sql.*;
import java.util.ArrayList;

/**
 * 对UserInfo表的增删查改操作
 * 增改参数为对应UserInfo类对象
 * 查可以按id查对应UserInfo信息，也可不加参数返回所有User信息
 * 增删操作只有管理员有权执行，增删方法考虑与其他数据库一致性
 * 查询更新操作不维护与其他数据库一致性
 */
public class UserMysqlDaoImpl implements MysqlDao {
    public boolean Insert(Info term) {
        String InsertCmd = "INSERT INTO UserInfo (UserID, UserPassword, Gender, NickName, Age) VALUES (?,?,?,?,?)";
        try {
            Connection MysqlConn = new MysqlConnection().getConnection();
            PreparedStatement InsertStmt = MysqlConn.prepareStatement(InsertCmd);
            UserInfo user = (UserInfo) term;
            InsertStmt.setString(1, user.getId());
            InsertStmt.setString(2, user.getKey());
            InsertStmt.setString(3, user.getGender());
            InsertStmt.setString(4, user.getNickname());
            InsertStmt.setInt(5, user.getAge());
            InsertStmt.executeUpdate();
            MysqlConn.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean Update(Info term) {
        UserInfo user = (UserInfo) term;
        String id = user.getId();
        String password = user.getKey();
        String bike = user.getBike();
        int balance = user.getBalance();
        long time = user.getTime();
        String UpdateActionCmd = "UPDATE UserInfo SET Balance=?, RendTime=?, RendBikeID=?, UserPassword=? WHERE UserID=?";
        try {
            Connection MysqlConn = new MysqlConnection().getConnection();
            PreparedStatement UpdateActionStmt = MysqlConn.prepareCall(UpdateActionCmd);
            UpdateActionStmt.setInt(1,balance);
            UpdateActionStmt.setLong(2, time);
            if(bike.equals(""))
                UpdateActionStmt.setNull(3, Types.CHAR);
            else
                UpdateActionStmt.setString(3, bike);
            UpdateActionStmt.setString(4, password);
            UpdateActionStmt.setString(5, id);
            UpdateActionStmt.executeUpdate();
            MysqlConn.close();
            return true;


        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Info Query(String mainkey) {
        String QueryCmd = "SELECT * FROM UserInfo WHERE UserID=?";
        try {
            Connection MysqlConn = new MysqlConnection().getConnection();
            PreparedStatement QueryStmt = MysqlConn.prepareStatement(QueryCmd);
            QueryStmt.setString(1,mainkey);
            ResultSet result = QueryStmt.executeQuery();
            if(!result.next()){
                MysqlConn.close();
                return null;
            }
            else{
                String id = result.getString("UserID");
                String password = result.getString("UserPassword");
                int balance = result.getInt("Balance");
                long time = result.getLong("RendTime");
                String Gender = result.getString("Gender");
                String NickName = result.getString("NickName");
                int age = result.getInt("Age");
                String bike = result.getString("RendBikeID");

                if(result.wasNull())
                    bike = "";
                UserInfo user = new UserInfo();
                user.setTime(time);
                user.setBalance(balance);
                user.setBike(bike);
                user.setId(id);
                user.setKey(password);
                user.setGender(Gender);
                user.setNickname(NickName);
                user.setAge(age);
                MysqlConn.close();
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<UserInfo> Query() {
        String QueryCmd = "SELECT * FROM UserInfo";
        ArrayList<UserInfo> userInfos = new ArrayList<UserInfo>();
        try {
            Connection MysqlConn = new MysqlConnection().getConnection();
            Statement QueryStmt = MysqlConn.createStatement();
            ResultSet result = QueryStmt.executeQuery(QueryCmd);
            while(result.next()){
                String id = result.getString("UserID");
                String password = result.getString("UserPassword");
                int balance = result.getInt("Balance");
                long time = result.getLong("RendTime");
                String bike = result.getString("RendBikeID");
                if(result.wasNull())
                    bike = "";
                String Gender = result.getString("Gender");
                String NickName = result.getString("NickName");
                int age = result.getInt("Age");

                UserInfo user = new UserInfo();
                user.setTime(time);
                user.setBalance(balance);
                user.setBike(bike);
                user.setId(id);
                user.setKey(password);
                user.setGender(Gender);
                user.setNickname(NickName);
                user.setAge(age);
                userInfos.add(user);

            }
            MysqlConn.close();
        } catch (SQLException e) {
            e.printStackTrace();

        }
       return userInfos;
    }

    public boolean Remove(String mainkey) {

        Connection MysqlConn = new MysqlConnection().getConnection();
        UserMysqlDaoImpl userMysqlDao = new UserMysqlDaoImpl();
        UserInfo userInfo = (UserInfo) userMysqlDao.Query(mainkey);
        if(userInfo == null || !userInfo.getBike().equals("") || userInfo.getBalance() != 0)
            return false;


        String RemoveCmd = "DELETE FROM UserInfo WHERE UserID=?";
        try {

            PreparedStatement RemoveStmt = MysqlConn.prepareStatement(RemoveCmd);
            RemoveStmt.setString(1, mainkey);
            RemoveStmt.executeUpdate();
            MysqlConn.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
