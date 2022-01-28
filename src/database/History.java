package database;

import data.Message;
import org.apache.commons.lang3.SerializationUtils;
import java.io.File;
import java.io.Serializable;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class History {
    
    private static final String defaultName = "history.sqlite";
    private static History instance = null;
    private String url = "";

    private History() {
        setName(defaultName);
        File f = new File(System.getProperty("user.dir") + File.separator + defaultName);
        if (!f.exists() || f.isDirectory()) {
            createHistoryDB();
            createHistory();
        }
    }

    public static History getInstance() {
        if (instance == null) {
            instance = new History();
        }
        return instance;
    }

    private Connection connect() {
        Connection c = null;
        try {
            c = DriverManager.getConnection(url);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return c;
    }

    public void setName(String name) {
        String current = System.getProperty("user.dir");
        url = "jdbc:sqlite:" + current + File.separator + name;
    }

    public void createHistoryDB() {
        try (Connection c = DriverManager.getConnection(url)) {
            if (c != null) {
                DatabaseMetaData meta = c.getMetaData();
                System.out.println("The driver names" + meta.getDriverName());
                System.out.println("New database for History created.");
            }

        } catch (SQLException e) {
        }
    }

    public void createHistory() {
        String sql
                = "CREATE TABLE IF NOT EXISTS history (\n"
                + "	hostsource VARCHAR(50) NOT NULL,\n"
                + "	hostdest VARCHAR(50) PRIMARY KEY NOT NULL,\n"
                + "	log BLOB\n"
                + ");";

        try (Connection conn = DriverManager.getConnection(url);
                Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
            System.out.println("ChatLog created");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addHistory(Message l) {
        if (!this.existHistory(l)) {
            String sql = "INSERT INTO history(hostsource, hostdest, log) VALUES(?,?,?)";

            try (Connection conn = this.connect();
                    PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, l.getSenderHost());
                pstmt.setString(2, l.getReceiverHost());
                pstmt.setBytes(3, SerializationUtils.serialize(l));
                pstmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void updateHistory(Message l) {
        String sql = "UPDATE history SET hostsource = ?, hostdest = ?, log = ? WHERE hostsource = ? and hostdest = ?";
        try (Connection conn = this.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, l.getSenderHost());
            pstmt.setString(2, l.getReceiverHost());
            pstmt.setBytes(3, SerializationUtils.serialize((Serializable) l));
            pstmt.setString(4, l.getSenderHost());
            pstmt.setString(5, l.getReceiverHost());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Message getMessage(String source, String dest) {
        String sql = "SELECT log"
                + " FROM history WHERE hostsource = ? AND hostdest = ?";

        Object result = null;
        try (Connection conn = this.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the value
            pstmt.setString(1, source);
            pstmt.setString(2, dest);
            //
            ResultSet rs = pstmt.executeQuery();

            // loop through the result set
            while (rs.next()) {
                byte[] log = rs.getBytes("log");
                result = SerializationUtils.deserialize(log);
            }
        } catch (SQLException ex) {
            Logger.getLogger(History.class.getName()).log(Level.SEVERE, null, ex);
        }
        return (Message) result;
    }

    public boolean existHistory(Message l) {
        String sql = "SELECT ROWID FROM history WHERE hostsource = ? AND hostdest = ?";

        //Object result = null;
        try (Connection conn = this.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the value
            pstmt.setString(1, l.getSenderHost());
            pstmt.setString(2, l.getReceiverHost());
            //
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

        return false;
    }
    
}