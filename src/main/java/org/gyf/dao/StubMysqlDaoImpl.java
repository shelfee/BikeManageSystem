package org.gyf.dao;

import org.gyf.common.*;

import java.sql.*;
import java.util.ArrayList;

/**
 * 对StubInfo表的增删查改操作
 * 增改参数为对应StubInfo类对象
 * 查可以按id查对应StubInfo信息，也可不加参数返回所有Stub信息
 * 增删操作只有管理员有权执行，增删方法考虑与其他数据库一致性
 * 查询更新操作不维护与其他数据库一致性
 */
public class StubMysqlDaoImpl implements MysqlDao {
    public boolean Insert(Info term) {
        String InsertCmd = "INSERT INTO StubInfo (StubID, LocationID) VALUES (?,?)";
        try {
            Connection MysqlConn = new MysqlConnection().getConnection();
            PreparedStatement InsertStmt = MysqlConn.prepareStatement(InsertCmd);
            StubInfo stub = (StubInfo) term;
            InsertStmt.setString(1, stub.getId());
            InsertStmt.setString(2, stub.getLocation());
            InsertStmt.executeUpdate();
            MysqlConn.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean Update(Info term) {
        StubInfo stub = (StubInfo) term;
        String id = stub.getId();
        String bike = stub.getBike();
        String UpdateCmd = "UPDATE StubInfo SET bikeID=? WHERE StubID=?";
        try {
            Connection MysqlConn = new MysqlConnection().getConnection();
            PreparedStatement UpdateStmt = MysqlConn.prepareCall(UpdateCmd);
            if(!bike.equals(""))
                UpdateStmt.setString(1, bike);
            else
                UpdateStmt.setNull(1, Types.CHAR);
            UpdateStmt.setString(2, id);
            UpdateStmt.executeUpdate();
            MysqlConn.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Info Query(String mainkey) {
        String QueryCmd = "SELECT * FROM StubInfo WHERE StubID=?";
        try {
            Connection MysqlConn = new MysqlConnection().getConnection();
            PreparedStatement QueryStmt = MysqlConn.prepareStatement(QueryCmd);
            QueryStmt.setString(1, mainkey);
            ResultSet result = QueryStmt.executeQuery();
            if(!result.next()){
                MysqlConn.close();
                return null;
            }
            else{
                String id = result.getString("StubID");
                String location = result.getString("LocationID");
                String bike = result.getString("BikeID");
                if(result.wasNull())
                    bike = "";

                StubInfo stub = new StubInfo();
                stub.setId(id);
                stub.setBike(bike);
                stub.setLocation(location);
                MysqlConn.close();
                return stub;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<StubInfo> Query(String list, String val){
        ArrayList<StubInfo> stubInfos = new ArrayList<StubInfo>();
        String QueryCmd = "SELECT * FROM StubInfo WHERE " + list + " = ?";
        Connection MysqlConn = new MysqlConnection().getConnection();

        try {
            PreparedStatement QueryStmt = MysqlConn.prepareStatement(QueryCmd);
            QueryStmt.setString(1, val);
            ResultSet result = QueryStmt.executeQuery();
            while(result.next()){
                String id = result.getString("StubID");
                String locationId = result.getString("LocationID");
                String bikeID = result.getString("BikeID");
                StubInfo stubInfo = new StubInfo();
                if(bikeID!=null)
                    stubInfo.setBike(bikeID);
                else
                    stubInfo.setBike("");
                stubInfo.setLocation(locationId);
                stubInfo.setId(id);
                stubInfos.add(stubInfo);
            }
            MysqlConn.close();
            return stubInfos;
        } catch (SQLException e) {
            e.printStackTrace();
            return stubInfos;
        }

    }

    public boolean Remove(String key) {
        Connection MysqlConn = new MysqlConnection().getConnection();
        StubMysqlDaoImpl stubDao = new StubMysqlDaoImpl();
        StubInfo stubInfo = (StubInfo) stubDao.Query(key);
        if(stubInfo == null || !stubInfo.getBike().equals(""))
            return false;
        else{
            String queryStubCmd = "select count(*) from StubInfo";
            String queryBikeCmd = "select count(*) from BikeInfo";
            try {
                Statement Stmt = MysqlConn.createStatement();
                ResultSet result = Stmt.executeQuery(queryStubCmd);
                int stubNum, bikeNum;
                if (result.next()) {
                    stubNum = result.getInt(1);
                    result = Stmt.executeQuery(queryBikeCmd);
                    if(result.next()) {
                        bikeNum = result.getInt(1);
                        if (! (bikeNum < stubNum))
                            return false;

                    }
                    else
                        return false;
                }
                else
                    return false;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }


        String RemoveCmd = "DELETE FROM StubInfo WHERE StubID=?";
        try {
            LocationMysqlDaoImpl locationMysqlDao = new LocationMysqlDaoImpl();
            LocationInfo locationInfo = (LocationInfo) locationMysqlDao.Query(stubInfo.getLocation());
            locationInfo.setStubNum(locationInfo.getStubNum() - 1);
            locationMysqlDao.Update(locationInfo);

            PreparedStatement RemoveStmt = MysqlConn.prepareStatement(RemoveCmd);
            RemoveStmt.setString(1, key);
            RemoveStmt.executeUpdate();
            MysqlConn.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
