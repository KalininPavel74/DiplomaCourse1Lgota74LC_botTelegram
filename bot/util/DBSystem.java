package bot.util;

import bot.interfaces.IDBSystem;

import java.sql.*;
import java.util.logging.Logger;

public class DBSystem implements IDBSystem {

    static private Logger logger = Logger.getLogger(DBSystem.class.getName());
    private String serverDBMSname = null;
    private int serverDBMSport;
    private String serverDBMSlogin = null;
    private String serverDBMSpassword = null;
    private Connection conn = null;

    public DBSystem(String serverDBMSname, int serverDBMSport, String serverDBMSlogin, String serverDBMSpassword) throws SQLException, ClassNotFoundException {
        this.serverDBMSname = serverDBMSname;
        this.serverDBMSport = serverDBMSport;
        this.serverDBMSlogin = serverDBMSlogin;
        this.serverDBMSpassword = serverDBMSpassword;

        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        conn = getConn();
    }

    private Connection getConn() throws SQLException {
        return DriverManager.getConnection(
                "jdbc:sqlserver://" + this.serverDBMSname + ":" + this.serverDBMSport
                , this.serverDBMSlogin
                , this.serverDBMSpassword);
    }

    @Override
    public String getProcedureData(String query, String[] ar) {
        if (conn == null) {
            try {
                conn = getConn();
            } catch (SQLException e) {
                logger.warning("0: " + e);
            }
        }
        if (conn == null) return null;
        try (PreparedStatement ps = conn.prepareStatement(query);) {
            if(ar != null)
                for (int i = 0; i < ar.length; i++)
                    ps.setString(i + 1, ar[i]);
            try (ResultSet rs = ps.executeQuery();) {
                if (rs.next()) {
                    logger.info(rs.getString(1));
                    return rs.getString(1);
                } else System.out.println("нет данных");
            } catch (SQLException e) {
                logger.warning("1: " + e);
            }
        } catch (SQLException e) {
            logger.warning("2: " + e);
        }
        return null;
    }

}
