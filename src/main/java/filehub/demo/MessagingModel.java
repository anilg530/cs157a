package filehub.demo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

public class MessagingModel {

    public static ArrayList<ArrayList<String>> getGroupMembersForMessaging(String user_id) {
        ArrayList<ArrayList<String>> returnArray = new ArrayList<>();
        String group_id_list = CommonModel.getAllGroupIDMembershipCommaSeparated(user_id);
        Connection conn = null;

        PreparedStatement pstmt = null;
        try {
            Class.forName(CommonModel.JDBC_DRIVER).newInstance();

            conn = DriverManager.getConnection(CommonModel.DB_URL, CommonModel.USER, CommonModel.PASS);

            String myQuery;
            myQuery = "SELECT group_members.user_id,user.first_name,user.last_name FROM group_members" +
                    "  JOIN user ON (user.id=group_members.user_id) WHERE group_members.group_id IN(" + group_id_list + ") AND user.login_status = ? AND group_members.user_id != ? GROUP BY group_members.user_id;";
            pstmt = conn.prepareStatement(myQuery);
            pstmt.setString(1, "Active");
            pstmt.setString(2, user_id);
            //System.out.println(pstmt);
            ResultSet sqlResult = pstmt.executeQuery();
            if (sqlResult != null) {
                if (sqlResult.isBeforeFirst()) {
                    while (sqlResult.next()) {
                        ArrayList<String> temp = new ArrayList<>();
                        temp.add(sqlResult.getString(1));
                        temp.add(sqlResult.getString(2));
                        temp.add(sqlResult.getString(3));
                        returnArray.add(temp);
                    }
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
                conn.close();
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

    public static void insertNewMessage(String send_from, String send_to, String message) {
        Connection conn = null;

        PreparedStatement pstmt = null;
        try {
            Class.forName(CommonModel.JDBC_DRIVER).newInstance();

            conn = DriverManager.getConnection(CommonModel.DB_URL, CommonModel.USER, CommonModel.PASS);

            String myQuery;
            myQuery = "INSERT INTO user_messages(message,message_status,sent_to,sent_from) values(?, ?, ?, ?) ";
            pstmt = conn.prepareStatement(myQuery, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, message);
            pstmt.setString(2, "sent");
            pstmt.setString(3, send_to);
            pstmt.setString(4, send_from);
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
                conn.close();
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

    public static int getNewMessageCount(String user_id) {
        Connection conn = null;

        PreparedStatement pstmt = null;
        try {
            Class.forName(CommonModel.JDBC_DRIVER).newInstance();

            conn = DriverManager.getConnection(CommonModel.DB_URL, CommonModel.USER, CommonModel.PASS);

            String myQuery;
            myQuery = "SELECT count(id) FROM user_messages WHERE (sent_to = ? AND message_status = ?)";
            pstmt = conn.prepareStatement(myQuery);
            pstmt.setString(1, user_id);
            pstmt.setString(2, "sent");
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
                conn.close();
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

    public static void markAllMessagesAsRead(String user_id) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            Class.forName(CommonModel.JDBC_DRIVER).newInstance();

            conn = DriverManager.getConnection(CommonModel.DB_URL, CommonModel.USER, CommonModel.PASS);

            String myQuery;
            myQuery = "UPDATE user_messages SET message_status = ?" +
                    " WHERE (sent_to = ? AND message_status = ?)";
            pstmt = conn.prepareStatement(myQuery);
            pstmt.setString(1, "read");
            pstmt.setString(2, user_id);
            pstmt.setString(3, "sent");
            int affected_rows = pstmt.executeUpdate();
            if (affected_rows > 0) {
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
                conn.close();
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

    public static ArrayList<ArrayList<String>> getAllReceivedMessages(String user_id) {
        ArrayList<ArrayList<String>> returnArray = new ArrayList<>();
        Connection conn = null;

        PreparedStatement pstmt = null;
        try {
            Class.forName(CommonModel.JDBC_DRIVER).newInstance();

            conn = DriverManager.getConnection(CommonModel.DB_URL, CommonModel.USER, CommonModel.PASS);

            String myQuery;
            myQuery = "SELECT user_messages.message,user_messages.sent_from,user_messages.message_status,user_messages.sent_on,user.first_name,user.last_name FROM user_messages" +
                    "  JOIN user ON (user.id=user_messages.sent_from) WHERE (user_messages.sent_to = ?)";
            pstmt = conn.prepareStatement(myQuery);
            pstmt.setString(1, user_id);
            //System.out.println(pstmt);
            ResultSet sqlResult = pstmt.executeQuery();
            if (sqlResult != null) {
                if (sqlResult.isBeforeFirst()) {
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
                conn.close();
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

    public static ArrayList<ArrayList<String>> getAllSentMessages(String user_id) {
        ArrayList<ArrayList<String>> returnArray = new ArrayList<>();
        Connection conn = null;

        PreparedStatement pstmt = null;
        try {
            Class.forName(CommonModel.JDBC_DRIVER).newInstance();

            conn = DriverManager.getConnection(CommonModel.DB_URL, CommonModel.USER, CommonModel.PASS);

            String myQuery;
            myQuery = "SELECT user_messages.message,user_messages.sent_to,user_messages.message_status,user_messages.sent_on,user.first_name,user.last_name FROM user_messages" +
                    "  JOIN user ON (user.id=user_messages.sent_to) WHERE (user_messages.sent_from = ?)";
            pstmt = conn.prepareStatement(myQuery);
            pstmt.setString(1, user_id);
            //System.out.println(pstmt);
            ResultSet sqlResult = pstmt.executeQuery();
            if (sqlResult != null) {
                if (sqlResult.isBeforeFirst()) {
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
                conn.close();
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