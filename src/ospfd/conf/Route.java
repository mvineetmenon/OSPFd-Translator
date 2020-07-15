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
public class Route {

    private static final Logger LOG = Logger.getLogger(Route.class.getName());

    enum ROUTE_TYPE {
        STATIC,
        CONNECTED,
        BGP
    }

    enum ACTION {
        PERMIT,
        DENY
    }

    private final boolean isEnable;
    private final ROUTE_TYPE routeType;
    private final int redistribute_type;
    private final int metric;
    private final RouteMap routeMap;

    private Route(boolean isEnable, ROUTE_TYPE routeType, int redistribute_type, int metric, RouteMap routeMap) {
        this.isEnable = isEnable;
        this.routeType = routeType;
        this.redistribute_type = redistribute_type;
        this.metric = metric;
        this.routeMap = routeMap;
    }

    public static List<Route> getAllRoutes(Connection connection) {
        String queryStmt = "SELECT * FROM routes";
        ArrayList<Route> results = new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement(queryStmt)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int route_map_id = rs.getInt("route_map_id");
                results.add(new Route(
                        rs.getBoolean("is_enable"),
                        ROUTE_TYPE.valueOf(rs.getString("source_protocol_type")),
                        rs.getInt("redistribute_type"),
                        rs.getInt("metric"),
                        null)
                );
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, e.getMessage());
        }
        return results;
    }

    class RouteMap {

        private String routeMapName;
        private ACTION action;
        private int orders;
        private String matchParam;
        private String matchValue;
        private String actionParam;
        private String actionValue;
        private String exitAction;

        public String toConf() {
            String confLine = "";
            /////// TODO
            return confLine;
        }
    }

    /**
     * @return configuration file line (might be separated by newline
     */
    public String toConf() {
        String confLine = "";
        if (isEnable) {
            switch (routeType) {
                case CONNECTED:
                    confLine += "redistribute connected";
                    break;
                case STATIC:
                    confLine += "redistribute static";
                    break;
                case BGP:
                    confLine += "redistribute bgp";
                    break;
            }
            if (redistribute_type != 0) {
                confLine += " metric-type " + redistribute_type;
            }
            if (metric != 0) {
                confLine += " metric " + metric;
            }
        }
        return confLine + "\n";
    }

    /**
     * @return VTYSH command line(s)
     */
    public String[] toCommand() {
        String confLine = "";
        if (isEnable) {
            switch (routeType) {
                case CONNECTED:
                    confLine += "redistribute connected";
                    break;
                case STATIC:
                    confLine += "redistribute static";
                    break;
                case BGP:
                    confLine += "redistribute bgp";
                    break;
            }
            if (metric != 0) {
                confLine += " metric " + metric;
            }
        } else {
            confLine = "no " + confLine;
        }
        return new String[]{confLine + "\n"};
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
        for (Route r : Route.getAllRoutes(DBUtil.getDBHandle("localhost", "ospfd_conf", "ospf", "ospf".getBytes()))) {
            System.out.println(r.toConf());
        }
    }
}
