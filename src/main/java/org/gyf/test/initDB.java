package org.gyf.test;

import java.sql.*;

/**
 * Created by shelfee on 2017/2/7.
 */
public class initDB {
    private final int LOWEST_BALANCE = 10;
    private Connection conn;
    public initDB(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost/BikeSystem", "root", "833877");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized String regist(String id, String key){
        String queryCmd = "SELECT UserID from UserInfo WHERE UserID =?";
        String insertCmd = "INSERT INTO UserInfo (UserID, UserPassword) VALUES (?,?)";
        try {
            PreparedStatement queryStmt = conn.prepareStatement(queryCmd);
            try {
                queryStmt.setString(1, id);
                ResultSet results = queryStmt.executeQuery();
                if(results.next())
                    return "Error: The id has been registed!";

            } catch (SQLException e) {
                e.printStackTrace();
                return "Error: read S error!";
            }
            PreparedStatement insertStmt = conn.prepareStatement(insertCmd);
            try {
                insertStmt.setString(1, id);
                insertStmt.setString(2, key);
                insertStmt.executeUpdate();
                return "sucess!";
            } catch (SQLException e) {
                e.printStackTrace();
                return "Error: write S error!";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error: sql error!";
        }
    }


    public synchronized String addLocation(String location){
        String queryCmd = "SELECT LocationID FROM LocationInfo WHERE LocationID=?";
        String insertCmd = "INSERT INTO LocationInfo (LocationID) Values (?)";
        ResultSet resultSet = null;
        try {
            PreparedStatement queryStmt = conn.prepareStatement(queryCmd);
            queryStmt.setString(1, location);
            resultSet = queryStmt.executeQuery();
            if(resultSet.next())
                return "Error: LocationID has been registed!";
            else{
                PreparedStatement insertStmt = conn.prepareStatement(insertCmd);
                insertStmt.setString(1, location);
                insertStmt.executeUpdate();
                return "success!";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error: write db_servlet error!";
        }
    }

    public synchronized String addStub(String stubid, String location){
        String queryIDCmd = "SELECT StubID FROM StubInfo WHERE StubID=?";
        String queryLocationCmd = "SELECT LocationID FROM LocationInfo WHERE LocationID=?";
        String insertCmd = "INSERT INTO StubInfo (StubID,LocationID) Values (?,?)";
        String locationUpdate = "Update LocationInfo Set StubNum=StubNum+1 WHERE LocationID=?";
        ResultSet resultSet = null;
        try {
            PreparedStatement queryIDStmt = conn.prepareStatement(queryIDCmd);
            queryIDStmt.setString(1, stubid);
            resultSet = queryIDStmt.executeQuery();
            if(resultSet.next())
                return "Error: StubID has been registed!";
            else{
                PreparedStatement queryLocationStmt = conn.prepareStatement(queryLocationCmd);
                queryLocationStmt.setString(1, location);
                resultSet = queryLocationStmt.executeQuery();
                PreparedStatement updateLocationStmt = conn.prepareStatement(locationUpdate);
                updateLocationStmt.setString(1, location);
                updateLocationStmt.executeUpdate();
                if(resultSet.next()) {
                    PreparedStatement insertStmt = conn.prepareStatement(insertCmd);
                    insertStmt.setString(1, stubid);
                    insertStmt.setString(2, location);
                    insertStmt.executeUpdate();
                    return "success!";
                }
                else
                    return "Error: Invalid locationID!";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error: write db_servlet error!";
        }
    }

    public synchronized String addBike(String bikeid, String stubid){
        String queryIDCmd = "SELECT BikeID FROM BikeInfo WHERE BikeID=?";
        String queryStubCmd = "SELECT LocationID FROM StubInfo WHERE StubID=?";;
        String insertBikeCmd = "INSERT INTO BikeInfo (BikeID,StubID) Values (?,?)";
        String updateStubCmd = "Update StubInfo Set BikeID=? WHERE StubID=?";
        String updateLocationCmd = "Update LocationInfo Set bikeNum=bikeNum+1 WHERE LocationID=?";
        ResultSet resultSet = null;
        try {
            PreparedStatement queryIDStmt = conn.prepareStatement(queryIDCmd);
            queryIDStmt.setString(1, bikeid);
            resultSet = queryIDStmt.executeQuery();
            if(resultSet.next())
                return "Error: BikeID has been registed!";
            else{
                PreparedStatement queryStubStmt = conn.prepareStatement(queryStubCmd);
                queryStubStmt.setString(1, stubid);
                resultSet = queryStubStmt.executeQuery();
                if(resultSet.next()) {
                    String location = resultSet.getString(1);
                    PreparedStatement insertBikeStmt = conn.prepareStatement(insertBikeCmd);
                    PreparedStatement updateStubStmt = conn.prepareStatement(updateStubCmd);
                    PreparedStatement updateLocationStmt = conn.prepareStatement(updateLocationCmd);
                    insertBikeStmt.setString(1, bikeid);
                    insertBikeStmt.setString(2, stubid);
                    updateStubStmt.setString(1, bikeid);
                    updateStubStmt.setString(2, stubid);
                    updateLocationStmt.setString(1, location);
                    insertBikeStmt.executeUpdate();
                    updateStubStmt.executeUpdate();
                    updateLocationStmt.executeUpdate();
                    return "Error: success!";
                }
                else
                    return "Error: Invalid locationID!";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error: write db_servlet error!";
        }
    }


    private void constructSystem(){

        for(int i = 1; i <= 3; i ++)
            addLocation("l" + String.valueOf(i));
        int Stubnum = 1;
        for(int i = 1; i < 10; i ++) {
            addStub("s" + String.valueOf(Stubnum), "l" + String.valueOf(i % 3 + 1));
            addBike("b" + String.valueOf(Stubnum),"s" + String.valueOf(Stubnum));
            Stubnum ++;
        }

        for(int i = 1; i < 10; i ++)
            regist("u" + String.valueOf(i), "123");
    }


    public static void main(String argv[]) throws SQLException {
        initDB a = new initDB();
        a.constructSystem();
        a.conn.close();
    }

}
