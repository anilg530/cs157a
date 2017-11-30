package filehub.demo;

import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.tree.RowMapper;
import javax.xml.transform.Result;
import java.sql.*;
import java.util.ArrayList;

/**
 * Created by anilgherra on 9/26/17.
 */
public class UserDatabase {

    public static void insertUser(ArrayList<String> user_info) {

        Connection conn = null;
        Statement stmt = null;

        String username = user_info.get(0);
        String password = user_info.get(1);
        String first_name = user_info.get(2);
        String last_name = user_info.get(3);
        String cellphone = user_info.get(4);

        try {
            Class.forName(CommonModel.JDBC_DRIVER).newInstance();
            conn = DriverManager.getConnection(CommonModel.DB_URL, CommonModel.USER, CommonModel.PASS);

            stmt = conn.createStatement();
            String myQuery;
            myQuery = "INSERT INTO user (username, password, login_status)" +
                    "VALUES ('" + username + "','" + password + "', 'Active');";
            stmt.executeUpdate(myQuery, Statement.RETURN_GENERATED_KEYS);

            ResultSet genKeys = stmt.getGeneratedKeys();
            if (genKeys.next()) {
                int user_id = genKeys.getInt(1);
                String myQuery2;
                myQuery2 = "INSERT INTO user_profile (user_id, first_name, last_name,cellphone)" +
                        "VALUES ('" + user_id + "', '" + first_name + "','" + last_name + "','" + cellphone + "');";
                stmt.executeUpdate(myQuery2, Statement.RETURN_GENERATED_KEYS);
            }


            stmt.close();
            conn.close();
            //allGroup.close();
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
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


    public static boolean userExist(String username) {

        boolean returnBoolean = true;
        Connection conn = null;

        PreparedStatement pstmt = null;
        try {
            Class.forName(CommonModel.JDBC_DRIVER).newInstance();
            conn = DriverManager.getConnection(CommonModel.DB_URL, CommonModel.USER, CommonModel.PASS);

            String myQuery;
            myQuery = "SELECT id FROM user WHERE (username = ?)";
            pstmt = conn.prepareStatement(myQuery);
            pstmt.setString(1, username);

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


    public static int insertUser1(ArrayList<String> user_info) {
        int returnInt = -1;

        String first_name = user_info.get(0);
        String last_name = user_info.get(1);
        String username = user_info.get(2);
        String cellphone = user_info.get(3);
        String password = user_info.get(4);

        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            Class.forName(CommonModel.JDBC_DRIVER).newInstance();
            conn = DriverManager.getConnection(CommonModel.DB_URL, CommonModel.USER, CommonModel.PASS);

            String myQuery;
            myQuery = "INSERT INTO user (first_name,last_name,username,cellphone,password,login_status)" +
                    "VALUES (?, ?, ?, ?, ?, ?);";
            pstmt = conn.prepareStatement(myQuery, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, first_name);
            pstmt.setString(2, last_name);
            pstmt.setString(3, username);
            pstmt.setString(4, cellphone);
            pstmt.setString(5, password);
            pstmt.setString(6, "Active");
            pstmt.executeUpdate();
            ResultSet sqlResult = pstmt.getGeneratedKeys();
            if (sqlResult != null && sqlResult.isBeforeFirst()) {
                sqlResult.next();
                returnInt = sqlResult.getInt(1);
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
        return returnInt;
    }


    public static void updateE(String email, String pass, int user_id) {
        Connection conn = null;
        PreparedStatement preparedStmt = null;

        try {
            Class.forName(CommonModel.JDBC_DRIVER).newInstance();
            conn = DriverManager.getConnection(CommonModel.DB_URL, CommonModel.USER, CommonModel.PASS);

            String myQuery;
            myQuery = "UPDATE user SET username = ?, password = ? WHERE (id = ?)";
            preparedStmt = conn.prepareStatement(myQuery, Statement.RETURN_GENERATED_KEYS);
            preparedStmt.setString(1, email);
            preparedStmt.setString(2, pass);
            preparedStmt.setInt(3, user_id);
            preparedStmt.executeUpdate();
            ResultSet sqlResult = preparedStmt.getGeneratedKeys();
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
                if (preparedStmt != null) {
                    preparedStmt.close();
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


    public static ArrayList<String> getUser(int user_id) {
        ArrayList<String> returnArray = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            Class.forName(CommonModel.JDBC_DRIVER).newInstance();
            conn = DriverManager.getConnection(CommonModel.DB_URL, CommonModel.USER, CommonModel.PASS);

            String myQuery;
            myQuery = "SELECT * FROM user WHERE id = ?";
            pstmt = conn.prepareStatement(myQuery);
            pstmt.setString(1, Integer.toString(user_id));
            ResultSet sqlResult = pstmt.executeQuery();
            if (sqlResult != null && sqlResult.isBeforeFirst()) {
                sqlResult.next();
                for (int i = 1; i < 8; i++) {
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

    public static boolean isValidUsernamePassword(String username, String password) {
        boolean returnBoolean = false;
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            Class.forName(CommonModel.JDBC_DRIVER).newInstance();
            conn = DriverManager.getConnection(CommonModel.DB_URL, CommonModel.USER, CommonModel.PASS);

            String myQuery;
            myQuery = "SELECT * FROM user WHERE (username = ? AND password = ?)";
            pstmt = conn.prepareStatement(myQuery);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet sqlResult = pstmt.executeQuery();
            if (sqlResult != null && sqlResult.isBeforeFirst()) {
                returnBoolean = true;
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
}