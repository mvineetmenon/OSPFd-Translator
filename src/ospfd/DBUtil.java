package ospfd;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.util.logging.Level;
import java.util.logging.Logger;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author mvineet
 */
public class DBUtil {

    private static final Logger LOG = Logger.getLogger(DBUtil.class.getName());

    public static Connection getDBHandle(String host, String port, String dbName, String userName, byte[] password) {
        Connection connection = null;
        try {
            String connectionString = "jdbc:postgresql://" + host + ":" + port + "/" + dbName;
            connection = DriverManager.getConnection(connectionString, userName, new String(password));
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, e.getMessage());
        }
        return connection;
    }

    public static Connection getDBHandle(String host, String dbName, String userName, byte[] password) {
        return getDBHandle(host, "5432", dbName, userName, password);
    }

//    public static void main(String[] args) {
//        DBUtil.getDBHandle("localhost", "ospfd_conf", "ospf", "ospf".getBytes());
//    }
}
