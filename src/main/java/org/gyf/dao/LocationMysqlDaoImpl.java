package org.gyf.dao;

import org.gyf.common.Info;
import org.gyf.common.LocationInfo;
import org.gyf.common.StubInfo;

import javax.persistence.criteria.CriteriaBuilder;
import javax.tools.DocumentationTool;
import java.sql.*;
import java.util.ArrayList;

/**
 * 对LocationInfo表的增删查改操作
 * 增改参数为对应LocationInfo类对象
 * 查可以按id查对应Location信息，也可不加参数返回所有Location信息
 * 增删操作只有管理员有权执行，增删方法考虑与其他数据库一致性
 * 查询更新操作不维护与其他数据库一致性
 */
public class LocationMysqlDaoImpl implements MysqlDao{
    public boolean Insert(Info term) {
        String InsertCmd = "INSERT INTO LocationInfo (LocationID, cord_X, cord_Y) VALUES (?, ?, ?)";
        try {
            Connection MysqlConn = new MysqlConnection().getConnection();
            PreparedStatement InsertStmt = MysqlConn.prepareStatement(InsertCmd);
            LocationInfo location = (LocationInfo) term;
            InsertStmt.setString(1, location.getId());
            InsertStmt.setFloat(2, location.getX());
            InsertStmt.setFloat(3, location.getY());
            InsertStmt.executeUpdate();
            MysqlConn.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean Update(Info term) {
        LocationInfo locationInfo = (LocationInfo) term;
        String id = locationInfo.getId();
        int bikeNum = locationInfo.getBikeNum();
        int stubNum = locationInfo.getStubNum();
        String UpdateCmd = "UPDATE LocationInfo SET BikeNum=?, StubNum=? WHERE LocationID=?";
        try {
            Connection MysqlConn = new MysqlConnection().getConnection();
            PreparedStatement UpdateStmt = MysqlConn.prepareCall(UpdateCmd);
            UpdateStmt.setInt(1, bikeNum);
            UpdateStmt.setInt(2, stubNum);
            UpdateStmt.setString(3, id);
            UpdateStmt.executeUpdate();
            MysqlConn.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Info Query(String mainkey) {
        String QueryCmd = "SELECT * FROM LocationInfo WHERE LocationID=?";
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
                String id = result.getString("LocationID");
                int bikeNum = result.getInt("BikeNum");
                int stubNum = result.getInt("StubNum");
                LocationInfo location = new LocationInfo();
                location.setId(id);
                location.setStubNum(stubNum);
                location.setBikeNum(bikeNum);
                MysqlConn.close();
                return location;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<LocationInfo> Query() {
        String QueryCmd = "SELECT * FROM LocationInfo";
        ArrayList<LocationInfo>locs = new ArrayList<LocationInfo>();
        try {
            Connection MysqlConn = new MysqlConnection().getConnection();
            Statement QueryStmt =  MysqlConn.createStatement();
            ResultSet result = QueryStmt.executeQuery(QueryCmd);
            while(result.next()){
                String id = result.getString("LocationID");
                int bikeNum = result.getInt("BikeNum");
                int stubNum = result.getInt("StubNum");
                LocationInfo location = new LocationInfo();
                location.setId(id);
                location.setBikeNum(bikeNum);
                location.setStubNum(stubNum);
                locs.add(location);
            }
            MysqlConn.close();
            return locs;
        } catch (SQLException e) {
            e.printStackTrace();
            return locs;
        }
    }

    public boolean Remove(String mainkey) {
        LocationMysqlDaoImpl locationMysqlDao = new LocationMysqlDaoImpl();
        LocationInfo locationInfo = (LocationInfo) locationMysqlDao.Query(mainkey);
        if(locationInfo == null || locationInfo.getStubNum() > 0){
            return false;
        }

        String RemoveCmd = "DELETE FROM LocationInfo WHERE LocationID=?";
        try {
            Connection MysqlConn = new MysqlConnection().getConnection();
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
