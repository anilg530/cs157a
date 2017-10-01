package filehub.demo;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FilenameFilter;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

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
                result = theDir.mkdirs();
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

    public static String getGroupName(String group_id) {
        String returnString = "";
        if (group_id == null || group_id.isEmpty()) {
            return returnString;
        }
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName(JDBC_DRIVER).newInstance();

            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            stmt = conn.createStatement();
            String myQuery;
            myQuery = "SELECT group_name FROM groups " +
                    "WHERE (id='" + group_id + "')";
            ResultSet sqlResult = stmt.executeQuery(myQuery);
            if (sqlResult != null && sqlResult.next()) {
                returnString = sqlResult.getString(1);
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

    public static boolean isAllowedAddNewFolder(int user_id, int group_id) {
        boolean returnBoolean = true;
        return returnBoolean;
    }

    public static boolean isAllowedDeleteFolder(int user_id, int group_id) {
        boolean returnBoolean = true;
        return returnBoolean;
    }

    public static boolean isFolderAlreadyExist(HttpSession session, String new_folder_name) {
        boolean returnBoolean = true;
        int group_id = (int) session.getAttribute("group_id");
        String current_path = (String) session.getAttribute("current_path");
        String new_path = current_path + "/" + new_folder_name;
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName(JDBC_DRIVER).newInstance();

            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            stmt = conn.createStatement();
            String myQuery;
            myQuery = "SELECT id FROM file_data WHERE (group_id='" + group_id + "' AND file_path='" + new_path + "' AND file_status='Active')";
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

    public static boolean createNewFolder(HttpSession session, String new_folder_name) {
        boolean returnBoolean = false;
        boolean sqlInsertSuccess = false;
        boolean mkdirSuccess = false;
        String group_id = Integer.toString((int) session.getAttribute("group_id"));
        String current_path = (String) session.getAttribute("current_path");
        String file_path = current_path + "/" + new_folder_name;
        String file_name = new_folder_name;
        String file_status = "Active";
        String type = "Folder";
        String uploaded_by = Integer.toString((int) session.getAttribute("user_id"));

        File theDir = new File(file_path);

        if (!theDir.exists()) {
            try {
                mkdirSuccess = theDir.mkdirs();
            } catch (SecurityException se) {
            }
        }

        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName(JDBC_DRIVER).newInstance();

            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            stmt = conn.createStatement();
            String myQuery;
            myQuery = "INSERT INTO file_data(group_id,file_name,file_path,file_status,type,uploaded_by)" +
                    "VALUES ('" + group_id + "','" + file_name + "','" + file_path + "','" + file_status + "','" + type + "','" + uploaded_by + "')";
            stmt.executeUpdate(myQuery, Statement.RETURN_GENERATED_KEYS);
            ResultSet sqlResult = stmt.getGeneratedKeys();
            if (sqlResult != null) {
                if (sqlResult.next()) {
                    //int insert_id = sqlResult.getInt(1);
                    sqlInsertSuccess = true;
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
        if (sqlInsertSuccess && mkdirSuccess) {
            returnBoolean = true;
        }
        return returnBoolean;
    }
}