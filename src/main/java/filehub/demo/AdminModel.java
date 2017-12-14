package filehub.demo;

import java.sql.*;
import java.util.ArrayList;

public class AdminModel {

    public static ArrayList<ArrayList<String>> getFileUploadLog() {
        ArrayList<ArrayList<String>> returnArray = new ArrayList<>();
        Connection conn = null;

        PreparedStatement pstmt = null;
        try {
            Class.forName(CommonModel.JDBC_DRIVER).newInstance();
            conn = DriverManager.getConnection(CommonModel.DB_SERVER, CommonModel.DB_USER, CommonModel.DB_PASS);

            String myQuery;
            myQuery = "SELECT file_upload_log.*, user.first_name, user.last_name " +
                    "FROM file_upload_log " +
                    "JOIN user ON user.id=file_upload_log.action_by " +
                    "ORDER BY file_upload_log.action_date DESC;";
            pstmt = conn.prepareStatement(myQuery);
            ResultSet sqlResult = pstmt.executeQuery();
            if (sqlResult != null && sqlResult.isBeforeFirst()) {
                while (sqlResult.next()) {
                    ArrayList<String> temp = new ArrayList<>();
                    temp.add(sqlResult.getString(1));
                    temp.add(sqlResult.getString(2));
                    temp.add(sqlResult.getString(3));
                    temp.add(sqlResult.getString(4));
                    temp.add(sqlResult.getString(5));
                    temp.add(sqlResult.getString(6));
                    returnArray.add(temp);
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

    public static ArrayList<ArrayList<String>> getUserIssues() {
        ArrayList<ArrayList<String>> returnArray = new ArrayList<>();
        Connection conn = null;

        PreparedStatement pstmt = null;
        try {
            Class.forName(CommonModel.JDBC_DRIVER).newInstance();
            conn = DriverManager.getConnection(CommonModel.DB_SERVER, CommonModel.DB_USER, CommonModel.DB_PASS);

            String myQuery;
            myQuery = "SELECT user_issues.*, user.first_name, user.last_name " +
                    "FROM user_issues " +
                    "JOIN user ON user.id=user_issues.issues_by " +
                    "ORDER BY user_issues.issue_date DESC;";
            pstmt = conn.prepareStatement(myQuery);
            ResultSet sqlResult = pstmt.executeQuery();
            if (sqlResult != null && sqlResult.isBeforeFirst()) {
                while (sqlResult.next()) {
                    ArrayList<String> temp = new ArrayList<>();
                    temp.add(sqlResult.getString(1));
                    temp.add(sqlResult.getString(2));
                    temp.add(sqlResult.getString(3));
                    temp.add(sqlResult.getString(4));
                    temp.add(sqlResult.getString(5));
                    temp.add(sqlResult.getString(6));
                    returnArray.add(temp);
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
}