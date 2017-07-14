package org.gyf.dao;

import org.gyf.common.*;

import java.sql.*;
import java.util.ArrayList;

/**
 * 对BikeInfo表的增删查改操作
 * 增改参数为对应BikeInfo类对象
 * 查可以按id查对应Bike信息，也可不加参数返回所有Bike信息
 * 增删操作只有管理员有权执行，增删方法考虑与其他数据库一致性
 * 查询更新操作不维护与其他数据库一致性
 */
public class BikeMysqlDaoImpl implements MysqlDao {
    public boolean Insert(Info term) {
        String InsertCmd = "INSERT INTO BikeInfo (BikeID, StubID) VALUES (?,?)";

        try {

            Connection MysqlConn = new MysqlConnection().getConnection();
            String queryStubCmd = "select count(*) from StubInfo";
            String queryBikeCmd = "select count(*) from BikeInfo";
            int stubNum, bikeNum;
            Statement Stmt = MysqlConn.createStatement();
            ResultSet result = Stmt.executeQuery(queryStubCmd);
            if (result.next()) {
                stubNum = result.getInt(1);
                result = Stmt.executeQuery(queryBikeCmd);
                if(result.next()) {
                    bikeNum = result.getInt(1);
                    if (!(bikeNum < stubNum)){
                        return false;
                    }
                }
                else
                    return false;
            }
            else
                return false;

            PreparedStatement InsertStmt = MysqlConn.prepareStatement(InsertCmd);
            BikeInfo bike = (BikeInfo) term;
            InsertStmt.setString(1, bike.getId());
            InsertStmt.setString(2, bike.getStub());
            InsertStmt.executeUpdate();
            MysqlConn.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;

        }
    }

    public boolean Update(Info term) {
        BikeInfo bike = (BikeInfo) term;
        String id = bike.getId();
        String stub = bike.getStub();
        String user = bike.getUser();
        long time = bike.getTime();

        String UpdateCmd = "UPDATE BikeInfo SET StubID=?, RendTime=?, RendUserID=? WHERE BikeID=?";
        try {
            Connection MysqlConn = new MysqlConnection().getConnection();
            PreparedStatement UpdateStmt = MysqlConn.prepareCall(UpdateCmd);
            if(!stub.equals(""))
                UpdateStmt.setString(1, stub);
            else
                UpdateStmt.setNull(1,Types.CHAR);
            UpdateStmt.setLong(2, time);

            if(user.equals(""))
                UpdateStmt.setNull(3, Types.CHAR);
            else
                UpdateStmt.setString(3, user);
            UpdateStmt.setString(4, id);
            UpdateStmt.executeUpdate();
            MysqlConn.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Info Query(String mainkey) {
        String QueryCmd = "SELECT * FROM BikeInfo WHERE BikeID=?";
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
                String id = result.getString("BikeID");
                String stub = result.getString("StubID");
                if(result.wasNull())
                    stub = "";
                long time = result.getLong("RendTime");
                String user = result.getString("RendUserID");
                if(result.wasNull())
                    user = "";

                BikeInfo bike = new BikeInfo();
                bike.setId(id);
                bike.setTime(time);
                bike.setStub(stub);
                bike.setUser(user);
                MysqlConn.close();
                return bike;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }

    public ArrayList<BikeInfo> Query() {
        String QueryCmd = "SELECT * FROM BikeInfo";
        ArrayList<BikeInfo>results = new ArrayList<BikeInfo>();
        try {
            Connection MysqlConn = new MysqlConnection().getConnection();
            Statement QueryStmt = MysqlConn.createStatement();

            ResultSet result = QueryStmt.executeQuery(QueryCmd);
            while(result.next()){
                String id = result.getString("BikeID");
                String stub = result.getString("StubID");
                if(result.wasNull())
                    stub = "";
                long time = result.getLong("RendTime");
                String user = result.getString("RendUserID");
                if(result.wasNull())
                    user = "";

                BikeInfo bike = new BikeInfo();
                bike.setId(id);
                bike.setTime(time);
                bike.setStub(stub);
                bike.setUser(user);
                results.add(bike);
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return results;
    }

    public boolean Remove(String mainkey) {
        String RemoveCmd = "DELETE FROM BikeInfo WHERE BikeID=?";
        BikeMysqlDaoImpl bikeMysqlDao = new BikeMysqlDaoImpl();
        BikeInfo bikeInfo = (BikeInfo) bikeMysqlDao.Query(mainkey);
        StubMysqlDaoImpl stubDao = new StubMysqlDaoImpl();
        if(bikeInfo == null)
            return false;
        String stubID = bikeInfo.getStub();
        StubInfo stubInfo = (StubInfo) stubDao.Query(stubID);
        LocationMysqlDaoImpl locationMysqlDao = new LocationMysqlDaoImpl();
        LocationInfo locationInfo = (LocationInfo) locationMysqlDao.Query(stubInfo.getLocation());
        try {
            Connection MysqlConn = new MysqlConnection().getConnection();
            if(bikeInfo.getUser().equals("")){
                if(bikeInfo.getStub().equals("")){
                    return false;
                }
                else{
                    stubInfo.setBike("");
                    locationInfo.setBikeNum(locationInfo.getBikeNum() - 1);
                    if(stubDao.Update(stubInfo)){
                        if(locationMysqlDao.Update(locationInfo)) {
                            PreparedStatement RemoveStmt = MysqlConn.prepareStatement(RemoveCmd);
                            RemoveStmt.setString(1, mainkey);
                            RemoveStmt.executeUpdate();
                            MysqlConn.close();
                            return true;
                        }
                        else{
                            stubInfo.setBike(bikeInfo.getId());
                            stubDao.Update(stubInfo);
                        }
                    }
                }
            }

        } catch (SQLException e) {
            stubInfo.setBike(bikeInfo.getId());
            stubDao.Update(stubInfo);
            locationInfo.setBikeNum(locationInfo.getBikeNum() + 1);
            locationMysqlDao.Update(locationInfo);
            e.printStackTrace();
        }
        return false;
    }
}
