package com.example.myproject;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {

    public Connection con;

    public Connection getConnection(){
        String url = "jdbc:oracle:thin:@127.0.0.1:1521:XE";
        String user = "system";
        String password = "system123";

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            con = DriverManager.getConnection(url, user, password);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        System.out.println("Connection Successfully!!");
        return con;
    }

}
