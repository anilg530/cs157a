package filehub.demo;

import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class FileModel {

    public static boolean isInRootDIR(HttpSession session, String currentPath) {
        boolean returnBoolean = false;
        if (session.getAttribute("root_dir") != null && session.getAttribute("root_dir").equals(currentPath)) {
            returnBoolean = true;
        }
        return returnBoolean;
    }

    public static ArrayList<String> getDirectory(String current_path) {
        ArrayList<String> returnArray = new ArrayList<>();
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
        PreparedStatement pstmt = null;
        try {
            Class.forName(CommonModel.JDBC_DRIVER).newInstance();
            conn = DriverManager.getConnection(CommonModel.DB_URL, CommonModel.USER, CommonModel.PASS);

            String myQuery;
            myQuery = "SELECT file_path FROM file_data WHERE (id = ? AND file_status = 'Active')";
            pstmt = conn.prepareStatement(myQuery);
            pstmt.setString(1, id);
            ResultSet sqlResult = pstmt.executeQuery();
            if (sqlResult != null && sqlResult.isBeforeFirst()) {
                sqlResult.next();
                returnString = sqlResult.getString(1);
                sqlResult.close();
            }
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException se2) {
            }
            try {
                if (conn != null) {
                    conn.close();
                }
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

        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            Class.forName(CommonModel.JDBC_DRIVER).newInstance();
            conn = DriverManager.getConnection(CommonModel.DB_URL, CommonModel.USER, CommonModel.PASS);

            String myQuery;
            myQuery = "SELECT * FROM file_data WHERE (file_path = ? AND file_status = 'Active' AND type='Folder')";
            pstmt = conn.prepareStatement(myQuery);
            pstmt.setString(1, new_path);
            ResultSet sqlResult = pstmt.executeQuery();
            if (sqlResult != null && sqlResult.isBeforeFirst()) {
                sqlResult.next();
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
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException se2) {
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return returnArray;
    }

    public static ArrayList<ArrayList<String>> getFileList(HttpSession session) {
        ArrayList<ArrayList<String>> returnArray = new ArrayList<>();
        String current_path = (String) session.getAttribute("current_path");
        String group_id = Integer.toString((int) session.getAttribute("group_id"));

        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            Class.forName(CommonModel.JDBC_DRIVER).newInstance();
            conn = DriverManager.getConnection(CommonModel.DB_URL, CommonModel.USER, CommonModel.PASS);

            String myQuery;
            myQuery = "SELECT * FROM file_data WHERE (group_id = ? AND folder_path = ? AND file_status = 'Active' AND type = 'File')";
            pstmt = conn.prepareStatement(myQuery);
            pstmt.setString(1, group_id);
            pstmt.setString(2, current_path);
            ResultSet sqlResult = pstmt.executeQuery();
            if (sqlResult != null && sqlResult.isBeforeFirst()) {
                while (sqlResult.next()) {
                    ArrayList<String> temp_array = new ArrayList<>();
                    for (int i = 1; i < 12; i++) {
                        String columnValue = sqlResult.getString(i);
                        if (columnValue == null) {
                            columnValue = "";
                        }
                        temp_array.add(columnValue);
                    }
                    returnArray.add(temp_array);
                }
                sqlResult.close();
            }
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException se2) {
            }
            try {
                if (conn != null) {
                    conn.close();
                }
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
        if (CommonModel.isMaster(Integer.toString(user_id))) {
            return true;
        }
        int user_permission = CommonModel.getUserPermissions(Integer.toString(user_id), Integer.toString(group_id));
        if (user_permission == 2 || user_permission == 3 || user_permission == 4) {
            return true;
        }
        return false;
    }

    public static boolean isAllowedDeleteFolder(int user_id, int group_id) {
        if (CommonModel.isMaster(Integer.toString(user_id))) {
            return true;
        }
        int user_permission = CommonModel.getUserPermissions(Integer.toString(user_id), Integer.toString(group_id));
        if (user_permission == 3 || user_permission == 4) {
            return true;
        }
        return false;
    }

    public static boolean isAllowedDeleteFile(int user_id, int group_id) {
        if (CommonModel.isMaster(Integer.toString(user_id))) {
            return true;
        }
        int user_permission = CommonModel.getUserPermissions(Integer.toString(user_id), Integer.toString(group_id));
        if (user_permission == 3 || user_permission == 4) {
            return true;
        }
        return false;
    }

    public static boolean isAllowedRenameFolder(int user_id, int group_id) {
        if (CommonModel.isMaster(Integer.toString(user_id))) {
            return true;
        }
        int user_permission = CommonModel.getUserPermissions(Integer.toString(user_id), Integer.toString(group_id));
        if (user_permission == 3 || user_permission == 4) {
            return true;
        }
        return false;
    }

    public static boolean isAllowedRenameFile(int user_id, int group_id) {
        if (CommonModel.isMaster(Integer.toString(user_id))) {
            return true;
        }
        int user_permission = CommonModel.getUserPermissions(Integer.toString(user_id), Integer.toString(group_id));
        if (user_permission == 3 || user_permission == 4) {
            return true;
        }
        return false;
    }

    public static boolean isAllowedEditNotes(int user_id, int group_id) {
        if (CommonModel.isMaster(Integer.toString(user_id))) {
            return true;
        }
        int user_permission = CommonModel.getUserPermissions(Integer.toString(user_id), Integer.toString(group_id));
        if (user_permission == 3 || user_permission == 4) {
            return true;
        }
        return false;
    }

    public static boolean isAllowedUploadFiles(int user_id, int group_id) {
        if (CommonModel.isMaster(Integer.toString(user_id))) {
            return true;
        }
        int user_permission = CommonModel.getUserPermissions(Integer.toString(user_id), Integer.toString(group_id));
        if (user_permission == 2 || user_permission == 3 || user_permission == 4) {
            return true;
        }
        return false;
    }

    public static boolean isAllowedRemoveFileURL(int user_id, int group_id) {
        if (CommonModel.isMaster(Integer.toString(user_id))) {
            return true;
        }
        int user_permission = CommonModel.getUserPermissions(Integer.toString(user_id), Integer.toString(group_id));
        if (user_permission == 3 || user_permission == 4) {
            return true;
        }
        return false;
    }

    public static boolean isAllowedShareFile(int user_id, int group_id) {
        if (CommonModel.isMaster(Integer.toString(user_id))) {
            return true;
        }
        int user_permission = CommonModel.getUserPermissions(Integer.toString(user_id), Integer.toString(group_id));
        if (user_permission == 3 || user_permission == 4) {
            return true;
        }
        return false;
    }

    public static boolean isFilenameTheSame(String id, String new_file_name) {
        boolean returnBoolean = false;
        if (new_file_name.equals(getFileName(id))) {
            returnBoolean = true;
        }
        return returnBoolean;
    }

    public static boolean isFolderAlreadyExist(HttpSession session, String new_folder_name) {
        boolean returnBoolean = true;
        int group_id = (int) session.getAttribute("group_id");
        String current_path = (String) session.getAttribute("current_path");
        String new_path = current_path + "/" + new_folder_name;

        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            Class.forName(CommonModel.JDBC_DRIVER).newInstance();
            conn = DriverManager.getConnection(CommonModel.DB_URL, CommonModel.USER, CommonModel.PASS);

            String myQuery;
            myQuery = "SELECT id FROM file_data WHERE (group_id = ? AND file_path = ? AND file_status = ?)";
            pstmt = conn.prepareStatement(myQuery);
            pstmt.setString(1, Integer.toString(group_id));
            pstmt.setString(2, new_path);
            pstmt.setString(3, "Active");
            ResultSet sqlResult = pstmt.executeQuery();
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
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException se2) {
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return returnBoolean;
    }

    public static boolean isFileAlreadyExist(HttpSession session, String new_file_name) {
        boolean returnBoolean = true;
        int group_id = (int) session.getAttribute("group_id");
        String current_path = (String) session.getAttribute("current_path");
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            Class.forName(CommonModel.JDBC_DRIVER).newInstance();
            conn = DriverManager.getConnection(CommonModel.DB_URL, CommonModel.USER, CommonModel.PASS);

            String myQuery;
            myQuery = "SELECT id FROM file_data WHERE (group_id = ? AND folder_path = ? AND file_name = ? AND file_status = ?)";
            pstmt = conn.prepareStatement(myQuery);
            pstmt.setString(1, Integer.toString(group_id));
            pstmt.setString(2, current_path);
            pstmt.setString(3, new_file_name);
            pstmt.setString(4, "Active");
            ResultSet sqlResult = pstmt.executeQuery();
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
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException se2) {
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return returnBoolean;
    }

    public static boolean isFileAlreadyExistRename(HttpSession session, String id, String new_file_name) {
        boolean returnBoolean = true;
        int group_id = (int) session.getAttribute("group_id");
        String current_path = (String) session.getAttribute("current_path");
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            Class.forName(CommonModel.JDBC_DRIVER).newInstance();
            conn = DriverManager.getConnection(CommonModel.DB_URL, CommonModel.USER, CommonModel.PASS);

            String myQuery;
            myQuery = "SELECT id FROM file_data WHERE (group_id = ? AND folder_path = ? AND file_name = ? AND file_status = ? AND id != ?)";
            pstmt = conn.prepareStatement(myQuery);
            pstmt.setString(1, Integer.toString(group_id));
            pstmt.setString(2, current_path);
            pstmt.setString(3, new_file_name);
            pstmt.setString(4, "Active");
            pstmt.setString(5, id);
            ResultSet sqlResult = pstmt.executeQuery();
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
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException se2) {
            }
            try {
                if (conn != null) {
                    conn.close();
                }
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
        PreparedStatement pstmt = null;
        try {
            Class.forName(CommonModel.JDBC_DRIVER).newInstance();
            conn = DriverManager.getConnection(CommonModel.DB_URL, CommonModel.USER, CommonModel.PASS);

            String myQuery;
            myQuery = "INSERT INTO file_data(group_id,file_name,folder_Path,file_path,file_status,type,uploaded_by)" +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(myQuery, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, group_id);
            pstmt.setString(2, file_name);
            pstmt.setString(3, folder_path);
            pstmt.setString(4, file_path);
            pstmt.setString(5, file_status);
            pstmt.setString(6, type);
            pstmt.setString(7, uploaded_by);
            pstmt.executeUpdate();
            ResultSet sqlResult = pstmt.getGeneratedKeys();
            if (sqlResult != null && sqlResult.isBeforeFirst()) {
                if (sqlResult.next()) {
                    insertFileUploadLogEntry(uploaded_by, "New folder created: " + new_folder_name + " (Group: " + CommonModel.getGroupName(group_id) + ")");
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
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException se2) {
            }
            try {
                if (conn != null) {
                    conn.close();
                }
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
        String old_single_folder_name = folderInfo.get(2);
        String folder_path = folderInfo.get(4);
        String today_date = CommonModel.todayDateInYMD();
        String archive_path = "archived/" + today_date + "/" + CommonModel.generateUUID() + "/" + folder_path;
        File archive_path_check = new File(archive_path);
        String modified_by = Integer.toString((int) session.getAttribute("user_id"));
        String group_id = Integer.toString((int) session.getAttribute("group_id"));

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
                    Class.forName(CommonModel.JDBC_DRIVER).newInstance();

                    conn = DriverManager.getConnection(CommonModel.DB_URL, CommonModel.USER, CommonModel.PASS);

                    stmt = conn.createStatement();
                    String myQuery;
                    myQuery = "UPDATE file_data SET file_status = 'Deleted',modified_by=" + modified_by + " " +
                            "WHERE (id='" + id + "')";
                    int affected_rows = stmt.executeUpdate(myQuery);
                    if (affected_rows > 0) {
                        insertFileUploadLogEntry(modified_by, "Folder deleted: " + old_single_folder_name + " (Group: " + CommonModel.getGroupName(group_id) + ")");
                        returnBoolean = true;
                        stmt = conn.createStatement();
                        String myQuery2;
                        myQuery2 = "UPDATE file_data SET file_status = 'Deleted',modified_by=" + modified_by + " " +
                                "WHERE (group_id='" + group_id + "' AND file_status='Active')" +
                                "AND folder_path LIKE '" + folder_path + "%'";
                        stmt.executeUpdate(myQuery2);
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
                    } catch (SQLException se2) {
                    }
                    try {
                        if (conn != null) {
                            conn.close();
                        }
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

    public static boolean deleteFile(HttpSession session, String id) {
        boolean returnBoolean = false;
        String group_id = Integer.toString((int) session.getAttribute("group_id"));
        ArrayList<String> fileInfo = getFileInfoByID(id);
        String old_single_file_name = fileInfo.get(2);
        String old_path = fileInfo.get(4);
        File old_path_file = new File(old_path);
        String old_uuid_name = old_path_file.getName();
        String file_path = fileInfo.get(3);
        String today_date = CommonModel.todayDateInYMD();
        String archive_path = "archived/" + today_date + "/" + CommonModel.generateUUID() + "/" + file_path;
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
            Path path_temp = Files.move(Paths.get(old_path), Paths.get(archive_path + "/" + old_uuid_name), REPLACE_EXISTING);
            if (path_temp != null) {
                Connection conn = null;
                Statement stmt = null;
                try {
                    Class.forName(CommonModel.JDBC_DRIVER).newInstance();

                    conn = DriverManager.getConnection(CommonModel.DB_URL, CommonModel.USER, CommonModel.PASS);

                    stmt = conn.createStatement();
                    String myQuery;
                    myQuery = "UPDATE file_data SET file_status = 'Deleted',modified_by=" + modified_by + " " +
                            "WHERE (id='" + id + "')";
                    int affected_rows = stmt.executeUpdate(myQuery);
                    if (affected_rows > 0) {
                        insertFileUploadLogEntry(modified_by, "File deleted: " + old_single_file_name + " (Group: " + CommonModel.getGroupName(group_id) + ")");
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
                    } catch (SQLException se2) {
                    }
                    try {
                        if (conn != null) {
                            conn.close();
                        }
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
            Class.forName(CommonModel.JDBC_DRIVER).newInstance();

            conn = DriverManager.getConnection(CommonModel.DB_URL, CommonModel.USER, CommonModel.PASS);

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
            } catch (SQLException se2) {
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return returnArray;
    }

    public static String getFileName(String id) {
        ArrayList<String> fileInfo = FileModel.getFileInfoByID(id);
        return fileInfo.get(2);
    }

    public static String getGroupIDByFileID(String id) {
        ArrayList<String> fileInfo = FileModel.getFileInfoByID(id);
        return fileInfo.get(1);
    }

    public static String getFilePathByFileID(String id) {
        ArrayList<String> fileInfo = FileModel.getFileInfoByID(id);
        return fileInfo.get(4);
    }

    public static String getPreviousFolderPath(String path) {
        Path file_path = Paths.get(path);
        return file_path.getParent().toString();
    }

    public static boolean renameFolder(HttpSession session, String id, String new_folder_name) {
        boolean returnBoolean = false;
        String group_id = Integer.toString((int) session.getAttribute("group_id"));
        ArrayList<String> fileInfo = FileModel.getFileInfoByID(id);
        String old_single_folder_name = fileInfo.get(2);
        String old_name = fileInfo.get(4);
        String new_name = fileInfo.get(3) + "/" + new_folder_name;
        String modified_by = Integer.toString((int) session.getAttribute("user_id"));
        File new_path_check = new File(new_name);

        if (!new_path_check.exists()) {
            try {
                new_path_check.mkdirs();
            } catch (SecurityException se) {
                se.printStackTrace();
            }
        }

        try {
            Path path_temp = Files.move(Paths.get(old_name), Paths.get(new_name), REPLACE_EXISTING);
            if (path_temp != null) {
                ArrayList<ArrayList<String>> getSubfilesAndFoldersWithNewPath = FileModel.getSubfilesAndFoldersWithNewPath(old_name, new_name);

                Connection conn = null;
                Statement stmt = null;
                try {
                    Class.forName(CommonModel.JDBC_DRIVER).newInstance();

                    conn = DriverManager.getConnection(CommonModel.DB_URL, CommonModel.USER, CommonModel.PASS);

                    stmt = conn.createStatement();
                    String myQuery;
                    myQuery = "UPDATE file_data SET file_path = '" + new_name + "',file_name='" + new_folder_name + "',modified_by=" + modified_by + " " +
                            "WHERE (id='" + id + "')";
                    int affected_rows = stmt.executeUpdate(myQuery);
                    if (affected_rows > 0) {
                        insertFileUploadLogEntry(modified_by, "Folder renamed from " + old_single_folder_name + " to " + new_folder_name + " (Group: " + CommonModel.getGroupName(group_id) + ")");
                        returnBoolean = true;
                        for (ArrayList<String> a : getSubfilesAndFoldersWithNewPath) {
                            String temp_id = a.get(0);
                            String temp_folder_path = a.get(1);
                            String temp_file_path = a.get(2);
                            stmt = conn.createStatement();
                            String myQuery2;
                            myQuery2 = "UPDATE file_data SET folder_path = '" + temp_folder_path + "',file_path='" + temp_file_path + "' " +
                                    "WHERE (id='" + temp_id + "')";
                            stmt.executeUpdate(myQuery2);
                        }
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
                    } catch (SQLException se2) {
                    }
                    try {
                        if (conn != null) {
                            conn.close();
                        }
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

    public static boolean renameFile(HttpSession session, String id, String new_file_name) {
        boolean returnBoolean = false;
        String group_id = Integer.toString((int) session.getAttribute("group_id"));
        ArrayList<String> fileInfo = FileModel.getFileInfoByID(id);
        String old_single_file_name = fileInfo.get(2);
        String modified_by = Integer.toString((int) session.getAttribute("user_id"));

        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName(CommonModel.JDBC_DRIVER).newInstance();

            conn = DriverManager.getConnection(CommonModel.DB_URL, CommonModel.USER, CommonModel.PASS);

            stmt = conn.createStatement();
            String myQuery;
            myQuery = "UPDATE file_data SET file_name = '" + new_file_name + "', modified_by=" + modified_by + " " +
                    "WHERE (id='" + id + "')";
            int affected_rows = stmt.executeUpdate(myQuery);
            if (affected_rows > 0) {
                insertFileUploadLogEntry(modified_by, "File renamed from " + old_single_file_name + " to " + new_file_name + " (Group: " + CommonModel.getGroupName(group_id) + ")");
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
            } catch (SQLException se2) {
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }

        return returnBoolean;
    }

    public static ArrayList<String> getAllFilesAndFolderInFolder(String path) {
        ArrayList<String> returnArray = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName(CommonModel.JDBC_DRIVER).newInstance();

            conn = DriverManager.getConnection(CommonModel.DB_URL, CommonModel.USER, CommonModel.PASS);

            stmt = conn.createStatement();
            String myQuery;
            myQuery = "SELECT * FROM file_data " +
                    "WHERE (folder_path='" + path + "' AND file_status='Active')";
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
            } catch (SQLException se2) {
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return returnArray;
    }

    public static boolean isFolderNameTheSame(String id, String new_folder_name) {
        boolean returnBoolean = false;
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName(CommonModel.JDBC_DRIVER).newInstance();

            conn = DriverManager.getConnection(CommonModel.DB_URL, CommonModel.USER, CommonModel.PASS);

            stmt = conn.createStatement();
            String myQuery;
            myQuery = "SELECT file_name FROM file_data WHERE (id='" + id + "')";
            ResultSet sqlResult = stmt.executeQuery(myQuery);
            if (sqlResult != null && sqlResult.next()) {
                String current_folder_name = sqlResult.getString(1);
                if (new_folder_name.equals(current_folder_name)) {
                    returnBoolean = true;
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
            } catch (SQLException se2) {
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return returnBoolean;
    }

    public static ArrayList<ArrayList<String>> getSubfilesAndFoldersWithNewPath(String folder_path, String new_path) {
        ArrayList<ArrayList<String>> returnArray = new ArrayList<>();
        ArrayList<ArrayList<String>> preformattedArray = new ArrayList<>();

        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName(CommonModel.JDBC_DRIVER).newInstance();

            conn = DriverManager.getConnection(CommonModel.DB_URL, CommonModel.USER, CommonModel.PASS);

            stmt = conn.createStatement();
            String myQuery;
            myQuery = "SELECT id,folder_path,file_path FROM file_data " +
                    "WHERE (file_status='Active')" +
                    "AND folder_path LIKE '" + folder_path + "%'";
            ResultSet sqlResult = stmt.executeQuery(myQuery);
            while (sqlResult != null && sqlResult.next()) {
                ArrayList<String> tempArray = new ArrayList<>();
                for (int i = 1; i < 4; i++) {
                    String columnValue = sqlResult.getString(i);
                    if (columnValue == null) {
                        columnValue = "";
                    }
                    tempArray.add(columnValue);
                }
                preformattedArray.add(tempArray);
            }
            sqlResult.close();
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se2) {
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }

        for (ArrayList<String> e : preformattedArray) {
            String id = e.get(0);
            String temp_folder_path = e.get(1).replaceAll(folder_path, new_path);
            String temp_file_path = e.get(2).replaceAll(folder_path, new_path);
            ArrayList<String> tempArray = new ArrayList<>();
            tempArray.add(id);
            tempArray.add(temp_folder_path);
            tempArray.add(temp_file_path);
            returnArray.add(tempArray);
        }

        return returnArray;
    }

    public static boolean editNotes(HttpSession session, String id, String notes) {
        boolean returnBoolean = false;
        String group_id = Integer.toString((int) session.getAttribute("group_id"));
        String notes_by = Integer.toString((int) session.getAttribute("user_id"));
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            Class.forName(CommonModel.JDBC_DRIVER).newInstance();

            conn = DriverManager.getConnection(CommonModel.DB_URL, CommonModel.USER, CommonModel.PASS);

            String myQuery;
            myQuery = "UPDATE file_data SET notes = ?,notes_by = ? " +
                    "WHERE (id = ?)";
            pstmt = conn.prepareStatement(myQuery);
            pstmt.setString(1, notes);
            pstmt.setString(2, notes_by);
            pstmt.setString(3, id);
            int affected_rows = pstmt.executeUpdate();
            if (affected_rows > 0) {
                insertFileUploadLogEntry(notes_by, "New notes added: " + notes + " (Group: " + CommonModel.getGroupName(group_id) + ")");
                returnBoolean = true;
            }
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException se2) {
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return returnBoolean;
    }

    public static boolean addNewFile(HttpSession session, MultipartFile file) {
        boolean returnBoolean = false;
        boolean fileUploadSuccess = false;
        // Session date
        String group_id = Integer.toString((int) session.getAttribute("group_id"));
        String uploaded_by = Integer.toString((int) session.getAttribute("user_id"));
        String file_name = file.getOriginalFilename();
        String ext = "";
        // extract file extension
        if (FilenameUtils.getExtension(file_name) != null && !FilenameUtils.getExtension(file_name).isEmpty()) {
            ext = FilenameUtils.getExtension(file_name);
        }
        // rename file to a randomized unique string
        String file_uuid = CommonModel.generateUUID();
        if (!ext.isEmpty()) {
            file_uuid = file_uuid + "." + ext;
        }
        String current_path = (String) session.getAttribute("current_path");
        String file_path = current_path + "/" + file_uuid;
        File dir_temp = new File(current_path);
        if (!dir_temp.exists()) {
            try {
                dir_temp.mkdirs();
            } catch (SecurityException se) {
                se.printStackTrace();
            }
        }
        // create the file
        File temp_new_file = new File(current_path, file_uuid);
        try {
            temp_new_file.createNewFile();
            BufferedOutputStream outputStream = null;
            outputStream = new BufferedOutputStream(new FileOutputStream(temp_new_file));
            outputStream.write(file.getBytes());
            outputStream.flush();
            outputStream.close();
            fileUploadSuccess = true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        // file written to disc succesful, write history into DB
        if (fileUploadSuccess) {
            Connection conn = null;
            PreparedStatement pstmt = null;
            try {
                Class.forName(CommonModel.JDBC_DRIVER).newInstance();

                conn = DriverManager.getConnection(CommonModel.DB_URL, CommonModel.USER, CommonModel.PASS);

                String myQuery;
                myQuery = "UPDATE file_data SET file_status = ? , modified_by = ? " +
                        "WHERE (group_id = ? AND folder_path = ? AND file_name = ? AND type = ? AND file_status = ?)";
                pstmt = conn.prepareStatement(myQuery);
                pstmt.setString(1, "Overwritten");
                pstmt.setString(2, uploaded_by);
                pstmt.setString(3, group_id);
                pstmt.setString(4, current_path);
                pstmt.setString(5, file_name);
                pstmt.setString(6, "File");
                pstmt.setString(7, "Active");
                pstmt.executeUpdate();

                String myQuery2;
                myQuery2 = "INSERT INTO file_data(group_id,file_name,folder_Path,file_path,file_status,type,uploaded_by)" +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)";
                pstmt = conn.prepareStatement(myQuery2, Statement.RETURN_GENERATED_KEYS);
                pstmt.setString(1, group_id);
                pstmt.setString(2, file_name);
                pstmt.setString(3, current_path);
                pstmt.setString(4, file_path);
                pstmt.setString(5, "Active");
                pstmt.setString(6, "File");
                pstmt.setString(7, uploaded_by);
                pstmt.executeUpdate();
                ResultSet sqlResult = pstmt.getGeneratedKeys();
                if (sqlResult != null) {
                    if (sqlResult.next()) {
                        insertFileUploadLogEntry(uploaded_by, "New file uploaded: "
                                + file_name + " (Group: " + CommonModel.getGroupName(group_id) + ")");
                        returnBoolean = true;
                    }
                    sqlResult.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (pstmt != null) {
                        pstmt.close();
                    }
                } catch (SQLException se2) {
                }
                try {
                    if (conn != null) {
                        conn.close();
                    }
                } catch (SQLException se) {
                    se.printStackTrace();
                }
            }
        }
        return returnBoolean;
    }

    public static void getMIMEType(String file_path) {
        File file = new File(file_path);
        MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
        String mimeType = mimeTypesMap.getContentType(file);
        System.out.println(mimeType);
    }

    public static final String getFontAwesomeMimeIcon(String file_path) {
        String returnString = "fa-file-o";

        String ext = "";
        if (FilenameUtils.getExtension(file_path) != null && !FilenameUtils.getExtension(file_path).isEmpty()) {
            ext = FilenameUtils.getExtension(file_path);
            ext = ext.toLowerCase();
        }

        switch (ext) {
            case "txt":
            case "rtf":
            case "odt":
            case "odc":
                returnString = "fa-file-text-o";
                break;

            case "htm":
            case "html":
            case "php":
            case "css":
            case "js":
            case "json":
            case "xml":
                returnString = "fa-file-code-o";
                break;

            case "swf":
            case "flv":
            case "mkv":
            case "mov":
            case "mpg":
            case "mp4":
                returnString = "fa-file-video-o";
                break;

            case "png":
            case "jpe":
            case "jpeg":
            case "jpg":
            case "gif":
            case "bmp":
            case "ico":
            case "tiff":
            case "tif":
            case "svg":
            case "svgz":
            case "psd":
                returnString = "fa-file-image-o";
                break;

            case "mp3":
            case "m4a":
            case "wav":
                returnString = "fa-file-audio-o";
                break;

            case "doc":
            case "docx":
                returnString = "fa-file-word-o";
                break;

            case "ppt":
            case "pptx":
                returnString = "fa-file-powerpoint-o";
                break;

            case "xls":
            case "xlsx":
                returnString = "fa-file-excel-o";
                break;

            case "pdf":
                returnString = "fa-file-pdf-o";
                break;

            case "zip":
            case "rar":
            case "cab":
                returnString = "fa-file-archive-o";
                break;

            default:
                returnString = "fa-file-o";
                break;
        }
        return returnString;
    }

    public static void insertFileUploadLogEntry(String id, String message) {
        Connection conn = null;

        PreparedStatement pstmt = null;
        try {
            Class.forName(CommonModel.JDBC_DRIVER).newInstance();

            conn = DriverManager.getConnection(CommonModel.DB_URL, CommonModel.USER, CommonModel.PASS);

            String myQuery;
            myQuery = "INSERT INTO file_upload_log(action_by,action_info) values(?, ?) ";
            pstmt = conn.prepareStatement(myQuery, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, id);
            pstmt.setString(2, message);
            pstmt.executeUpdate();
            ResultSet sqlResult = pstmt.getGeneratedKeys();
            if (sqlResult != null) {
                if (sqlResult.next()) {
                }
                sqlResult.close();
            }
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException se2) {
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    public static boolean isFileURLExist(String file_id) {
        boolean returnBoolean = false;
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            Class.forName(CommonModel.JDBC_DRIVER).newInstance();

            conn = DriverManager.getConnection(CommonModel.DB_URL, CommonModel.USER, CommonModel.PASS);

            String myQuery;
            myQuery = "SELECT url_code FROM file_url WHERE (file_id = ?)";
            pstmt = conn.prepareStatement(myQuery);
            pstmt.setString(1, file_id);
            ResultSet sqlResult = pstmt.executeQuery();
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
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException se2) {
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return returnBoolean;
    }

    public static String getFileURLcode(String file_id) {
        String returnString = "";

        if (isFileURLExist(file_id)) {
            Connection conn = null;
            Statement stmt = null;
            try {
                Class.forName(CommonModel.JDBC_DRIVER).newInstance();

                conn = DriverManager.getConnection(CommonModel.DB_URL, CommonModel.USER, CommonModel.PASS);

                stmt = conn.createStatement();
                String myQuery;
                myQuery = "SELECT * FROM file_url " +
                        "WHERE (file_id='" + file_id + "')";
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
                } catch (SQLException se2) {
                }
                try {
                    if (conn != null) {
                        conn.close();
                    }
                } catch (SQLException se) {
                    se.printStackTrace();
                }
            }
        } else {
            // file url doesn't exist. create one
            returnString = CommonModel.generateRandomCode();

            Connection conn = null;

            PreparedStatement pstmt = null;
            try {
                Class.forName(CommonModel.JDBC_DRIVER).newInstance();

                conn = DriverManager.getConnection(CommonModel.DB_URL, CommonModel.USER, CommonModel.PASS);

                String myQuery;
                myQuery = "INSERT INTO file_url(url_code,file_id) values(?, ?) ";
                pstmt = conn.prepareStatement(myQuery, Statement.RETURN_GENERATED_KEYS);
                pstmt.setString(1, returnString);
                pstmt.setString(2, file_id);
                pstmt.executeUpdate();
                ResultSet sqlResult = pstmt.getGeneratedKeys();
                if (sqlResult != null) {
                    if (sqlResult.next()) {
                    }
                    sqlResult.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (pstmt != null) {
                        pstmt.close();
                    }
                } catch (SQLException se2) {
                }
                try {
                    if (conn != null) {
                        conn.close();
                    }
                } catch (SQLException se) {
                    se.printStackTrace();
                }
            }
        }
        return returnString;
    }

    public static boolean isValidFileURL(String file_url) {
        boolean returnBoolean = false;
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            Class.forName(CommonModel.JDBC_DRIVER).newInstance();

            conn = DriverManager.getConnection(CommonModel.DB_URL, CommonModel.USER, CommonModel.PASS);

            String myQuery;
            myQuery = "SELECT url_code FROM file_url WHERE (url_code = ?)";
            pstmt = conn.prepareStatement(myQuery);
            pstmt.setString(1, file_url);
            ResultSet sqlResult = pstmt.executeQuery();
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
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException se2) {
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return returnBoolean;
    }

    public static int getFileIDByFileURL(String file_url) {
        Connection conn = null;

        PreparedStatement pstmt = null;
        try {
            Class.forName(CommonModel.JDBC_DRIVER).newInstance();

            conn = DriverManager.getConnection(CommonModel.DB_URL, CommonModel.USER, CommonModel.PASS);

            String myQuery;
            myQuery = "SELECT file_id FROM file_url WHERE (url_code = ?)";
            pstmt = conn.prepareStatement(myQuery);
            pstmt.setString(1, file_url);
            ResultSet sqlResult = pstmt.executeQuery();
            if (sqlResult != null) {
                if (sqlResult.isBeforeFirst()) {
                    sqlResult.next();
                    return sqlResult.getInt(1);
                }
                sqlResult.close();
            }
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException se2) {
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        return 0;
    }

    public static void removeFileURL(String file_id) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            Class.forName(CommonModel.JDBC_DRIVER).newInstance();

            conn = DriverManager.getConnection(CommonModel.DB_URL, CommonModel.USER, CommonModel.PASS);

            String myQuery;
            myQuery = "DELETE FROM file_url WHERE (file_id = ?)";
            pstmt = conn.prepareStatement(myQuery, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, file_id);
            pstmt.executeUpdate();
            ResultSet sqlResult = pstmt.getGeneratedKeys();
            if (sqlResult != null) {
                sqlResult.close();
            }
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException se2) {
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }
}