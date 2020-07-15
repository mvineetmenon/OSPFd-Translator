/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ospfd;

import java.sql.Connection;
import ospfd.conf.OSPFConf;

/**
 *
 * @author mvineet
 *
 */
public class OSPFd {

    public static void main(String[] args) {
        Connection connection = DBUtil.getDBHandle("localhost", "ospfd_conf", "ospf", "ospf".getBytes());
        System.out.println(new OSPFConf(connection).toConf());
    }

}
