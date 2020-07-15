/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ospfd.conf;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import ospfd.DBUtil;

/**
 *
 * @author mvineet
 */
class Area {

    private static final Logger LOG = Logger.getLogger(Area.class.getName());

    private String areaId;
    private String netAddress;
    private boolean isStub;
    private boolean isNoSummary;
    private boolean enable;
    private OSPFAuthentication auth;

    public Area(boolean enable, String areaId, String netAddress, boolean isStub, boolean isSummary, OSPFAuthentication auth) {
        this.enable = enable;
        this.areaId = areaId;
        this.netAddress = netAddress;
        this.isStub = isStub;
        this.isNoSummary = isSummary;
        this.auth = auth;
    }

    /**
     * @return the areaId
     */
    public String getAreaId() {
        return areaId;
    }

    /**
     * @param areaId the areaId to set
     */
    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    /**
     * @return the netAddress
     */
    public String getNetAddress() {
        return netAddress;
    }

    /**
     * @param netAddress the netAddress to set
     */
    public void setNetAddress(String netAddress) {
        this.netAddress = netAddress;
    }

    /**
     * @return the isStub
     */
    public boolean isIsStub() {
        return isStub;
    }

    /**
     * @param isStub the isStub to set
     */
    public void setIsStub(boolean isStub) {
        this.isStub = isStub;
    }

    /**
     * @return the isNoSummary
     */
    public boolean isIsNoSummary() {
        return isNoSummary;
    }

    /**
     * @param isNoSummary the isNoSummary to set
     */
    public void setIsNoSummary(boolean isNoSummary) {
        this.isNoSummary = isNoSummary;
    }

    /**
     * @return the enable
     */
    public boolean isEnable() {
        return enable;
    }

    /**
     * @param enable the enable to set
     */
    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    /**
     * @return the auth
     */
    public OSPFAuthentication getAuth() {
        return auth;
    }

    /**
     * @param auth the auth to set
     */
    public void setAuth(OSPFAuthentication auth) {
        this.auth = auth;
    }

    public static List<Area> getAllArea(Connection connection) {
        String queryStmt = "SELECT * FROM areas INNER JOIN ospf_authentications ON areas.ospf_authentication_id = ospf_authentications.id";
        ArrayList<Area> results = new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement(queryStmt)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                results.add(new Area(
                        rs.getBoolean("enable"),
                        rs.getString("area_id"),
                        rs.getString("net_address"),
                        rs.getBoolean("is_stub"),
                        rs.getBoolean("is_no_summary"),
                        new OSPFAuthentication(
                                OSPFAuthentication.OSPF_AUTHTYPE.valueOf(rs.getString("auth_type")),
                                (byte) rs.getInt("key_id")
                        )
                ));
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, e.getMessage());
        }
        return results;
    }

    /**
     * @return configuration file line (might be separated by newline
     */
    public String toConf() {
        String permConfLine = "area " + getAreaId();
        String confLine = "";
        if (isEnable()) {
            if (isIsStub()) {
                confLine += permConfLine + " stub\n";
            } else {
                //stub and range cannot go hand in hand.
                confLine += permConfLine + " range " + getNetAddress() + "\n";
            }
            if (!isIsNoSummary()) {
                confLine += permConfLine + " no summary\n";
            }
            if (getAuth() != null) {
                confLine += permConfLine;
                confLine += " authentication ";
                if (getAuth().getAuthType() == OSPFAuthentication.OSPF_AUTHTYPE.MD5) {
                    confLine += "message-digest";
                }
            }
        } else {
            confLine = "no " + confLine;
        }
        return confLine;
    }

    /**
     * @return VTYSH command line(s)
     */
    public String toCommand() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
        for (Area a : Area.getAllArea(DBUtil.getDBHandle("localhost", "ospfd_conf", "ospf", "ospf".getBytes()))) {
            System.out.println(a.toConf());
        }
    }
}
