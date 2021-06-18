package net.victorbetoni.squadster.database;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


public class Database {
    private Connection connection;

    private static Connection conn = null;
    private static Properties props = null;

    public static void initializeConnection() {
        try {
            loadProperties();
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = (String) props.get("url");
            String user = (String) props.get("user");
            String password = (String) props.get("password");
            conn = DriverManager.getConnection(url, user, password);
        }catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static boolean isConnected() {
        try{
            return conn != null && !conn.isClosed();
        }catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public static Connection getConnection(){
        if(!isConnected()){
            initializeConnection();
        }
        return conn;
    }

    public static void closeConnection() {
        if(conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static void loadProperties() {
        try (InputStream inputStream = Database.class.getResourceAsStream("/sql/db.properties")) {
            props = new Properties();
            props.load(inputStream);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

}
