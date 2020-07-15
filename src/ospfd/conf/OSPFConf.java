/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ospfd.conf;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import ospfd.DBUtil;

/**
 *
 * @author mvineet Class which holds OSPF Configuration, has accessors and
 * modifiers as members
 */
public class OSPFConf {

    private static final Logger LOG = Logger.getLogger(OSPFConf.class.getName());

    private boolean enable;
    private String routerId;
    private String[] passiveInterfaces;
    private int spfDelayMsec;
    private int spfInitHoldtimeMsec;
    private int spfMaxHoldtimeMsec;
    private List<Interface> interfaces;
    private List<Area> areas;
    private List<Route> routeRedistributions;

    /**
     *
     * @param connection
     */
    public OSPFConf(Connection connection) {

        String queryStmt = "SELECT * FROM ospf_router";
        try (PreparedStatement pstmt = connection.prepareStatement(queryStmt)) {
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            this.enable = rs.getBoolean("is_enable");
            this.passiveInterfaces = (String[]) rs.getArray("passive_interfaces").getArray();
            this.routerId = rs.getString("router_id");
            this.spfDelayMsec = rs.getInt("spf_delay_msec");
            this.spfInitHoldtimeMsec = rs.getInt("spf_init_holdtime_msec");
            this.spfMaxHoldtimeMsec = rs.getInt("spf_max_holdtime_msec");
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, e.getMessage());
        }

        this.interfaces = Interface.getAllInterfaces(connection);
        this.routeRedistributions = Route.getAllRoutes(connection);
        this.areas = Area.getAllArea(connection);
    }

    /**
     *
     * @return
     */
    public boolean getEnable() {
        return enable;
    }

    /**
     *
     * @param enable
     */
    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    /**
     * @return the routerId
     */
    public String getRouterId() {
        return routerId;
    }

    /**
     * @param routerId the routerId to set
     */
    public void setRouterId(String routerId) {
        this.routerId = routerId;
    }

    /**
     * @return the passiveInterfaces
     */
    public String[] getPassiveInterfaces() {
        return passiveInterfaces;
    }

    /**
     * @param passiveInterfaces the passiveInterfaces to set
     */
    public void setPassiveInterfaces(String[] passiveInterfaces) {
        this.passiveInterfaces = passiveInterfaces;
    }

    /**
     * @return the spfDelayMsec
     */
    public int getSpfDelayMsec() {
        return spfDelayMsec;
    }

    /**
     * @param spfDelayMsec the spfDelayMsec to set
     */
    public void setSpfDelayMsec(int spfDelayMsec) {
        this.spfDelayMsec = spfDelayMsec;
    }

    /**
     * @return the spfInitHoldtimeMsec
     */
    public int getSpfInitHoldtimeMsec() {
        return spfInitHoldtimeMsec;
    }

    /**
     * @param spfInitHoldtimeMsec the spfInitHoldtimeMsec to set
     */
    public void setSpfInitHoldtimeMsec(int spfInitHoldtimeMsec) {
        this.spfInitHoldtimeMsec = spfInitHoldtimeMsec;
    }

    /**
     * @return the spfMaxHoldtimeMsec
     */
    public int getSpfMaxHoldtimeMsec() {
        return spfMaxHoldtimeMsec;
    }

    /**
     * @param spfMaxHoldtimeMsec the spfMaxHoldtimeMsec to set
     */
    public void setSpfMaxHoldtimeMsec(int spfMaxHoldtimeMsec) {
        this.spfMaxHoldtimeMsec = spfMaxHoldtimeMsec;
    }

    /**
     * @return the InterfaceAreaMap
     */
    public List<Interface> getInterfaces() {
        return interfaces;
    }

    /**
     *
     * @param i
     * @return
     */
    public boolean addInterfaces(Interface i) {
        return this.interfaces.add(i);
    }

    /**
     * @return the routeRedistributions
     */
    public List<Route> getRouteRedistributions() {
        return routeRedistributions;
    }

    /**
     *
     * @param r
     * @return
     */
    public boolean addRouteRedistribution(Route r) {
        return this.routeRedistributions.add(r);
    }

    /**
     * @return configuration file line (might be separated by newline
     */
    public String toConf() {
        String confLine = "router ospf";

        confLine += "\n";
        confLine += "ospf router-id " + getRouterId() + "\n";
        for (String passiveInterface : passiveInterfaces) {
            confLine += "passive-interface " + passiveInterface + "\n";
        }
        confLine += " timers throttle spf " + this.spfDelayMsec + " " + this.spfInitHoldtimeMsec + " " + this.spfMaxHoldtimeMsec + "\n";
        for (Interface i: this.interfaces){
            confLine += i.toConf();
        }
        for (Route r: this.routeRedistributions){
            confLine += r.toConf();
        }
        return confLine;
    }

    /**
     * @return VTYSH command line(s)
     */
    public String[] toCommand() {
        String confLine = "";
//        if (isEnable) {
//            switch (redistributeType) {
//                case CONNECTED:
//                    confLine += "redistribute connected";
//                    break;
//                case STATIC:
//                    confLine += "redistribute static";
//                    break;
//                case BGP:
//                    confLine += "redistribute bgp";
//                    break;
//            }
//            if (metric != 0) {
//                confLine += " metric " + metric;
//            }
//        } else {
//            confLine = "no " + confLine;
//        }
        return new String[]{confLine};
    }

    /**
     * Implements db_writer
     *
     * @param connection
     */
    public void toDB(Connection connection) {
        throw new UnsupportedOperationException();
    }

    public static void main(String[] args) {
        System.out.println(
                new OSPFConf(DBUtil.getDBHandle("localhost", "ospfd_conf", "ospf", "ospf".getBytes()))
                        .toConf());

    }

}
