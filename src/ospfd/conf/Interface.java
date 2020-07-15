/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ospfd.conf;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import ospfd.DBUtil;

/**
 *
 * @author mvineet
 */
class Interface {

    private static final Logger LOG = Logger.getLogger(Interface.class.getName());

    public enum NETWORK_TYPE {
        BROADCAST,
        NON_BROADCAST,
        POINT_TO_POINT,
        POINT_TO_MULTIPOINT,
    }

    private boolean enable;
    private String interfaceName;
    private boolean isPassive;
    private Area area;
    private String key;
    private int cost;
    private int deadInterval;
    private int helloInterval;
    private NETWORK_TYPE networkType;
    private int priority;
    private int retransmitInterval;
    private int transmitDelay;

    public Interface(boolean enable, String interfaceName, boolean isPassive, Area area, String key, int cost, int deadInterval, int helloInterval, NETWORK_TYPE networkType, int priority, int retransmitInterval, int transmitDelay) {
        this.enable = enable;
        this.interfaceName = interfaceName;
        this.isPassive = isPassive;
        this.area = area;
        this.key = key;
        this.cost = cost;
        this.deadInterval = deadInterval;
        this.helloInterval = helloInterval;
        this.networkType = networkType;
        this.priority = priority;
        this.retransmitInterval = retransmitInterval;
        this.transmitDelay = transmitDelay;
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
     * @return the interfaceName
     */
    public String getInterfaceName() {
        return interfaceName;
    }

    /**
     * @param interfaceName the interfaceName to set
     */
    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    /**
     * @return the isPassive
     */
    public boolean isIsPassive() {
        return isPassive;
    }

    /**
     * @param isPassive the isPassive to set
     */
    public void setIsPassive(boolean isPassive) {
        this.isPassive = isPassive;
    }

    /**
     * @return the cost
     */
    public int getCost() {
        return cost;
    }

    /**
     * @param cost the cost to set
     */
    public void setCost(int cost) {
        this.cost = cost;
    }

    /**
     * @return the deadInterval
     */
    public int getDeadInterval() {
        return deadInterval;
    }

    /**
     * @param deadInterval the deadInterval to set
     */
    public void setDeadInterval(int deadInterval) {
        this.deadInterval = deadInterval;
    }

    /**
     * @return the helloInterval
     */
    public int getHelloInterval() {
        return helloInterval;
    }

    /**
     * @param helloInterval the helloInterval to set
     */
    public void setHelloInterval(int helloInterval) {
        this.helloInterval = helloInterval;
    }

    /**
     * @return the networkType
     */
    public NETWORK_TYPE getNetworkType() {
        return networkType;
    }

    /**
     * @param networkType the networkType to set
     */
    public void setNetworkType(NETWORK_TYPE networkType) {
        this.networkType = networkType;
    }

    /**
     * @return the priority
     */
    public int getPriority() {
        return priority;
    }

    /**
     * @param priority the priority to set
     */
    public void setPriority(int priority) {
        this.priority = priority;
    }

    /**
     * @return the retransmitInterval
     */
    public int getRetransmitInterval() {
        return retransmitInterval;
    }

    /**
     * @param retransmitInterval the retransmitInterval to set
     */
    public void setRetransmitInterval(int retransmitInterval) {
        this.retransmitInterval = retransmitInterval;
    }

    /**
     * @return the transmitDelay
     */
    public int getTransmitDelay() {
        return transmitDelay;
    }

    /**
     * @param transmitDelay the transmitDelay to set
     */
    public void setTransmitDelay(int transmitDelay) {
        this.transmitDelay = transmitDelay;
    }

    /**
     * @return the key
     */
    public String getKey() {
        return key;
    }

    /**
     * @param key the key to set
     */
    public void setKey(String key) {
        this.key = key;
    }

    public static List<Interface> getAllInterfaces(Connection connection) {
        String queryStmt = "SELECT * FROM ospf_interfaces INNER JOIN areas ON ospf_interfaces.ospf_area_id = areas.area_id INNER JOIN ospf_authentications ON areas.ospf_authentication_id = ospf_authentications.id";
        ArrayList<Interface> results = new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement(queryStmt)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                results.add(new Interface(
                        rs.getBoolean("enable"),
                        rs.getString("interface_name"),
                        rs.getBoolean("is_passive"),
                        new Area(
                                rs.getBoolean("enable"),
                                rs.getString("area_id"),
                                rs.getString("net_address"),
                                rs.getBoolean("is_stub"),
                                rs.getBoolean("is_no_summary"),
                                new OSPFAuthentication(
                                        OSPFAuthentication.OSPF_AUTHTYPE.valueOf(rs.getString("auth_type")),
                                        (byte) rs.getInt("key_id")
                                )
                        ),
                        rs.getString("ospf_authentication_key"),
                        rs.getInt("cost"),
                        rs.getInt("dead_interval"),
                        rs.getInt("hello_interval"),
                        NETWORK_TYPE.valueOf(rs.getString("network_type")),
                        rs.getInt("priority"),
                        rs.getInt("retransmit_interval"),
                        rs.getInt("transmit_delay"))
                );
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
        String confLine = "";
        if (isEnable()) {
            confLine += "interface " + interfaceName;

            confLine += "\n";
            if (area != null) {
                if (area.getAuth().getAuthType() == OSPFAuthentication.OSPF_AUTHTYPE.MD5) {
                    confLine += " ip ospf authentication message-digest\n";
                    confLine += " ip ospf message-digest-key "
                            + area.getAuth().getKeyId() + " "
                            + "md5 "
                            + getKey() + "\n";
                    if (area.getAuth().getAuthType() == OSPFAuthentication.OSPF_AUTHTYPE.PASSWORD) {
                        confLine += "message-digest";
                    }
                }
            }
            confLine += " ip ospf cost " + cost + "\n";
            confLine += " ip ospf dead-interval " + deadInterval + "\n";
            confLine += " ip ospf hello-interval " + helloInterval + "\n";
            confLine += " ip ospf priority " + priority + "\n";
            confLine += " ip ospf retransmit-interval " + retransmitInterval + "\n";
            confLine += " ip ospf transmit-delay " + transmitDelay + "\n";
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
        for (Interface i : Interface.getAllInterfaces(DBUtil.getDBHandle("localhost", "ospfd_conf", "ospf", "ospf".getBytes()))) {
            System.out.println(i.toConf());
        }
    }

}
