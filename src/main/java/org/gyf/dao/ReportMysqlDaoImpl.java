package org.gyf.dao;

import org.gyf.common.Info;
import org.gyf.common.ReportInfo;
import org.gyf.common.StubInfo;
import org.gyf.common.UserInfo;

import java.sql.*;
import java.util.ArrayList;

/**
 * 对ReportInfo表的增删查改操作
 * 增改参数为对应ReportInfo类对象
 * 查可以按id查对应Report信息，也可不加参数返回所有Report信息
 * 增删操作只有管理员有权执行，增删方法考虑与其他数据库一致性
 * 查询更新操作不维护与其他数据库一致性
 */
public class ReportMysqlDaoImpl implements MysqlDao{

    public boolean Insert(Info term) {
        String InsertCmd = "INSERT INTO ReportInfo (ReportContent, reporterID) VALUES (?,?)";
        try {
            Connection MysqlConn = new MysqlConnection().getConnection();
            PreparedStatement InsertStmt = MysqlConn.prepareStatement(InsertCmd);
            ReportInfo reportInfo = (ReportInfo) term;
            InsertStmt.setString(2, reportInfo.getReporterID());
            InsertStmt.setString(1, reportInfo.getReportContent());
            InsertStmt.executeUpdate();
            MysqlConn.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }


    }

    public boolean Update(Info term) {
        return false;
    }

    public Info Query(String mainkey) {
        return null;
    }

    public ArrayList<ReportInfo>Query(Date from, Date to){
        ArrayList<ReportInfo> reportInfos = new ArrayList<ReportInfo>();
        String QueryCmd = "select * from ReportInfo where reportTime>=? and reportTime<=?";
        try {
            Connection MysqlConn = new MysqlConnection().getConnection();
            PreparedStatement QueryStmt = MysqlConn.prepareStatement(QueryCmd);
            QueryStmt.setDate(2, to);
            QueryStmt.setDate(1, from);
            ResultSet resultSet = QueryStmt.executeQuery();
            while(resultSet.next()){
                Date time = resultSet.getDate("reportTime");
                String reporterID = resultSet.getString("reporterID");
                String reportCotent = resultSet.getString("reportContent");
                int reportID = resultSet.getInt("reportID");
                ReportInfo reportInfo = new ReportInfo();
                reportInfo.setReportContent(reportCotent);
                reportInfo.setReporterID(reporterID);
                reportInfo.setReportTime(time);
                reportInfo.setReportID(reportID);
                reportInfos.add(reportInfo);
            }
            MysqlConn.close();
            return reportInfos;
        } catch (SQLException e) {
            e.printStackTrace();
            return reportInfos;
        }

    }

    public boolean Remove(String mainkey) {
        return false;
    }

    public boolean Remove(int mainkey) {
        Connection MysqlConn = new MysqlConnection().getConnection();
        String RemoveCmd = "DELETE FROM ReportInfo WHERE reportID=?";
        try {
            PreparedStatement RemoveStmt = MysqlConn.prepareStatement(RemoveCmd);
            RemoveStmt.setInt(1, mainkey);
            RemoveStmt.executeUpdate();
            MysqlConn.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }
}
