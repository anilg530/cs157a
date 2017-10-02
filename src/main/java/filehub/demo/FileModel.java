package filehub.demo;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class FileModel {

    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://p3plcpnl0569.prod.phx3.secureserver.net:3306/cs157a";
    static final String USER = "cs157a_main";
    static final String PASS = "cs157a_db";

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
            try {
                theDir.mkdirs();
            } catch (SecurityException se) {
                se.printStackTrace();
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

    public static String getFilePathByID(String id) {
        String returnString = "";

        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName(JDBC_DRIVER).newInstance();

            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            stmt = conn.createStatement();
            String myQuery;
            myQuery = "SELECT file_path FROM file_data " +
                    "WHERE (id='" + id + "' AND file_status='Active')";
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
                for (int i = 1; i < 12; i++) {
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
        String file_name = new_folder_name;
        String current_path = (String) session.getAttribute("current_path");
        String folder_path = current_path;
        String file_path = current_path + "/" + new_folder_name;
        String file_status = "Active";
        String type = "Folder";
        String uploaded_by = Integer.toString((int) session.getAttribute("user_id"));

        File theDir = new File(file_path);

        if (!theDir.exists()) {
            try {
                mkdirSuccess = theDir.mkdirs();
            } catch (SecurityException se) {
                se.printStackTrace();
            }
        }

        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName(JDBC_DRIVER).newInstance();

            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            stmt = conn.createStatement();
            String myQuery;
            myQuery = "INSERT INTO file_data(group_id,file_name,folder_Path,file_path,file_status,type,uploaded_by)" +
                    "VALUES ('" + group_id + "','" + file_name + "','" + folder_path + "','" + file_path + "','" + file_status + "','" + type + "','" + uploaded_by + "')";
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

    public static boolean deleteFolder(HttpSession session, String id) {
        boolean returnBoolean = false;
        ArrayList<String> folderInfo = getFileInfoByID(id);
        String folder_path = folderInfo.get(4);
        String today_date = CommonModel.todayDateInYMD();
        String archive_path = "archived/" + today_date + "/" + CommonModel.generateUUID() + "/" + folder_path;
        File archive_path_check = new File(archive_path);
        String modified_by = Integer.toString((int) session.getAttribute("user_id"));

        if (!archive_path_check.exists()) {
            try {
                archive_path_check.mkdirs();
            } catch (SecurityException se) {
                se.printStackTrace();
            }
        }

        try {
            Path path_temp = Files.move(Paths.get(folder_path), Paths.get(archive_path), REPLACE_EXISTING);
            if (path_temp != null) {
                Connection conn = null;
                Statement stmt = null;
                try {
                    Class.forName(JDBC_DRIVER).newInstance();

                    conn = DriverManager.getConnection(DB_URL, USER, PASS);

                    stmt = conn.createStatement();
                    String myQuery;
                    myQuery = "UPDATE file_data SET file_status = 'Deleted',modified_by=" + modified_by + " " +
                            "WHERE (folder_path='" + folder_path + "' AND file_status='Active')";
                    int affected_rows = stmt.executeUpdate(myQuery);
                    if (affected_rows > 0) {
                        returnBoolean = true;
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
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return returnBoolean;
    }

    public static ArrayList<String> getFileInfoByID(String id) {
        ArrayList<String> returnArray = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName(JDBC_DRIVER).newInstance();

            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            stmt = conn.createStatement();
            String myQuery;
            myQuery = "SELECT * FROM file_data " +
                    "WHERE (id='" + id + "')";
            ResultSet sqlResult = stmt.executeQuery(myQuery);
            if (sqlResult != null && sqlResult.next()) {
                for (int i = 1; i < 12; i++) {
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

    public static String getPreviousFolderPath(String path) {
        Path file_path = Paths.get(path);
        return file_path.getParent().toString();
    }

    public static void renameFolder(String id, String new_folder_name) {
        System.out.println("hi");
    }
}