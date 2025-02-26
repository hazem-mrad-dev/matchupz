package utils;

import java.sql.Connection;
import java.sql.DriverManager;

public class MyDataSource {

    private static MyDataSource instance ;
    private Connection conn;

    private final String URL="jdbc:mysql://localhost:3306/matchupz";
    private final String USER="root";
    private final String PASSWORD="";



    public Connection getConn() {
        return conn;
    }
    public void setConn(Connection conn) {
        this.conn = conn;
    }

    private MyDataSource() {
        try{
            conn = DriverManager.getConnection(URL,USER,PASSWORD);
            System.out.println("Connection established successfully");
        }
        catch(Exception e){
            e.getMessage();
        }

    }
    public static MyDataSource getInstance(){
        if(instance==null)
        {
            instance = new MyDataSource();
        }
        return instance;
    }

}
