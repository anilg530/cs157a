package filehub.demo;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FilenameFilter;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class FileModel {

    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://p3plcpnl0569.prod.phx3.secureserver.net:3306/cs157a";
    static final String USER = "cs157a_main";
    static final String PASS = "cs157a_db";

    public static boolean isInGroup(int userID, int groupID) {
        boolean returnBoolean = false;
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName(JDBC_DRIVER).newInstance();

            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            stmt = conn.createStatement();
            String myQuery;
            myQuery = "SELECT * FROM group_members WHERE (user_id='" + userID + "' AND group_id='" + groupID + "')";
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

    public static boolean isInRootDIR(HttpSession session, String currentPath) {
        boolean returnBoolean = false;
        if (session.getAttribute("root_dir") != null && session.getAttribute("root_dir").equals(currentPath)) {
            returnBoolean = true;
        }
        return returnBoolean;
    }

    public static ArrayList<String> getDirectory(String current_path) {
        ArrayList<String> returnArray = new ArrayList<>();
        //System.out.println("Working Directory = " + System.getProperty("user.dir"));
        File theDir = new File(current_path);

        if (!theDir.exists()) {
            System.out.println("creating directory: " + theDir.getName());
            boolean result = false;

            try {
                theDir.mkdir();
                result = true;
            } catch (SecurityException se) {
            }
            if (result) {
                System.out.println("DIR created");
            }
        }
        String[] directories = theDir.list(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isDirectory();
            }
        });
        returnArray = new ArrayList(Arrays.asList(directories));
        return returnArray;
    }

    public static ArrayList<String> getFolderInfo(HttpSession session, String folder_name) {
        ArrayList<String> returnArray = new ArrayList<>();
        String current_path = (String) session.getAttribute("current_path");
        String new_path = current_path + "/" + folder_name;
        //System.out.println(new_path);

        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName(JDBC_DRIVER).newInstance();

            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            stmt = conn.createStatement();
            String myQuery;
            myQuery = "SELECT * FROM file_data " +
                    "WHERE (file_path='" + new_path + "' AND file_status='Active' AND type='Folder')";
            ResultSet sqlResult = stmt.executeQuery(myQuery);
            if (sqlResult != null && sqlResult.next()) {
                for (int i = 1; i < 11; i++) {
                    String columnValue = sqlResult.getString(i);
                    if (columnValue == null) {
                        columnValue = "";
                    }
                    returnArray.add(columnValue);
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

        return returnArray;
    }

    public static String getFullName(String user_id) {
        String returnString = "";
        if (user_id == null || user_id.isEmpty()) {
            return returnString;
        }
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName(JDBC_DRIVER).newInstance();

            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            stmt = conn.createStatement();
            String myQuery;
            myQuery = "SELECT first_name,last_name FROM user_profile " +
                    "WHERE (user_id='" + user_id + "' AND status='Active')";
            ResultSet sqlResult = stmt.executeQuery(myQuery);
            if (sqlResult != null && sqlResult.next()) {
                String first_name = sqlResult.getString(1);
                String last_name = sqlResult.getString(2);
                if (first_name != null && !first_name.isEmpty()) {
                    returnString = returnString + first_name;
                }
                if (last_name != null && !last_name.isEmpty()) {
                    returnString = returnString + " " + last_name;
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
        return returnString;
    }

    public static String getTopLevelFolder(String currentPath) {
        return new File("group_files/" + currentPath).getName();
    }

    public static String timeStampToFormalDate(String timestamp) throws ParseException {
        String returnString = "";
        if (timestamp != null && !timestamp.isEmpty()) {
            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = dateFormatter.parse(timestamp);

            DateFormat targetFormat = new SimpleDateFormat("EEEE, MMM d, yyyy");
            returnString = targetFormat.format(date);
        }
        return returnString;
    }

    public static boolean isAllowedAddNewFolder(int userID, int groupID) {
        boolean returnBoolean = true;
        return returnBoolean;
    }

    public static boolean isAllowedDeleteFolder(int userID, int groupID) {
        boolean returnBoolean = true;
        return returnBoolean;
    }
}