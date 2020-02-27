package com.erving.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbUtil {
    private static final String URL="jdbc:Mysql://localhost:3306/simpleenterprisepotal?useSSL=false&serverTimezone = GMT";
    private static final String NAME = "root";
    private static final String PASSWORD = "liang0802";
    private Connection conn =null;
    public Connection getConn(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, NAME, PASSWORD);
            return conn;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return null;
        }

    }
}
