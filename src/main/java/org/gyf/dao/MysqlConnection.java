package org.gyf.dao;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *  建立数据库连接供Dao类连接数据库使用
 */
public class MysqlConnection {
    private Connection conn;
    private final String DBDRIVER = "com.mysql.jdbc.Driver";
    private final String DBURL = "jdbc:mysql://localhost/BikeSystem?useUnicode=true&characterEncoding=utf8";
    private final String DBUSER = "root";
    private final String DBPASSWORD = "833877";
    public MysqlConnection(){

        try{
            Class.forName(DBDRIVER);
            this.conn = DriverManager.getConnection(DBURL,DBUSER,DBPASSWORD);
        }catch(Exception e){
            System.out.println("Error: Driver failure!");
        }

    }

    public Connection getConnection(){
        return conn;
    }

    public void close(){
        try{
            conn.close();
        }catch(Exception e){
            System.out.println("Error: Close failure!");
        }
    }
}
