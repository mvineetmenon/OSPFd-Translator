/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ospfd;

import java.sql.Connection;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author mvineet
 */
public class DBUtilTest {
    
//    public DBUtilTest() {
//    }
//    
//    @BeforeAll
//    public static void setUpClass() {
//    }
//    
//    @AfterAll
//    public static void tearDownClass() {
//    }
//    
//    @BeforeEach
//    public void setUp() {
//    }
//    
//    @AfterEach
//    public void tearDown() {
//    }

    /**
     * Test of getDBHandle method, of class DBUtil.
     */
    @Test
    public void testGetDBHandle() {
        System.out.println("getDBHandle");
        String host = "localhost";
        String port = "5432";
        String dbName = "ospfd_conf";
        String userName = "ospf";
        byte[] password = "ospf".getBytes();
        Connection expResult = null;
        Connection result = DBUtil.getDBHandle(host, port, dbName, userName, password);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
