package filehub.demo;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class FileModel {

    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://p3plcpnl0569.prod.phx3.secureserver.net:3306/cs157a";
    static final String USER = "cs157a_main";
    static final String PASS = "cs157a_db";

    public static boolean isInGroup(int user_id, int group_id) {
        boolean returnBoolean = false;
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName(JDBC_DRIVER).newInstance();

            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            stmt = conn.createStatement();
            String myQuery;
            myQuery = "SELECT * FROM group_members WHERE (user_id='" + user_id + "' AND group_id='" + group_id + "')";
            ResultSet sqlResult = stmt.executeQuery(myQuery);
            if (sqlResult != null) {
                if (sqlResult.isBeforeFirst()) {
                    returnBoolean = true;
                } else {
                    returnBoolean = false;
                }
                sqlResult.close();
            }
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                conn.close();
            } catch (SQLException se2) {
            }
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return returnBoolean;
    }

    public static ArrayList<String> getDirectory(String key) {
        ArrayList<String> returnArray = new ArrayList<>();
        //System.out.println("Working Directory = " + System.getProperty("user.dir"));
        String baseDIR = System.getProperty("user.dir");
        String groupDIR = baseDIR + key;
        File workingDIR = new File(groupDIR);

        if (!workingDIR.exists()) {
            //System.out.println("creating directory: " + theDir.getName());
            //boolean result = false;
            try {
                workingDIR.mkdirs();
            } catch (SecurityException se) {
            }
        }
        ArrayList<File> files = new ArrayList<>(Arrays.asList(workingDIR.listFiles()));
        for (File file : files) {
            if (file.isDirectory()) {
                returnArray.add(file.getName());
            }
        }
        //returnArray = new ArrayList<>(Arrays.asList(workingDIR.list()));
        Collections.sort(returnArray, (s1, s2) -> s1.compareToIgnoreCase(s2));
        return returnArray;
    }
}