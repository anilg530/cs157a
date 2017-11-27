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

    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://p3plcpnl0569.prod.phx3.secureserver.net:3306/cs157a";
    static final String USER = "cs157a_main";
    static final String PASS = "cs157a_db";



    public static void insertUser(ArrayList<String> user_info){

        Connection conn = null;
        Statement stmt = null;

        String username = user_info.get(0);
        String password = user_info.get(1);
        String first_name = user_info.get(2);
        String last_name = user_info.get(3);
        String cellphone= user_info.get(4);

        try {
            Class.forName(JDBC_DRIVER).newInstance();

            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            stmt = conn.createStatement();
            String myQuery;
            myQuery = "INSERT INTO user (username, password, login_status)" +
                    "VALUES ('"+ username +"','"+ password +"', 'Active');";
           stmt.executeUpdate(myQuery, Statement.RETURN_GENERATED_KEYS);

           ResultSet genKeys = stmt.getGeneratedKeys();
           if (genKeys.next()){
               int user_id = genKeys.getInt(1);
               String myQuery2;
               myQuery2 = "INSERT INTO user_profile (user_id, first_name, last_name,cellphone)" +
                       "VALUES ('"+user_id+"', '"+first_name+"','"+last_name+"','"+cellphone+"');";
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


    public static boolean userExist(String username){

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


    public static void insertUser1(ArrayList<String> user_info) {

        Connection conn = null;
        Statement stmt = null;


        String first_name = user_info.get(0);
        String last_name = user_info.get(1);
        String username = user_info.get(2);
        String cellphone = user_info.get(3);
        String password = user_info.get(4);


        try {
            Class.forName(JDBC_DRIVER).newInstance();

            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            stmt = conn.createStatement();
            String myQuery;
            myQuery = "INSERT INTO user (first_name,last_name,username,cellphone,password)" +
                    "VALUES ('" + first_name + "','" + last_name + "', '" + username + "','" + cellphone + "','" + password + "');";
            stmt.executeUpdate(myQuery);

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


        public static void updateUser(ArrayList<String> user_info){

        Connection conn = null;
        PreparedStatement preparedStmt = null;

        String first_name = user_info.get(0);
        String last_name = user_info.get(1);
        String username = user_info.get(2);
        String cellphone = user_info.get(3);
        String password = user_info.get(4);


        try {
            Class.forName(JDBC_DRIVER).newInstance();

            conn = DriverManager.getConnection(DB_URL, USER, PASS);


            String query = "update users set first_name = ?, last_name = ?, username = ?, cellphone = ?, password = ?";
            preparedStmt = conn.prepareStatement(query);
            preparedStmt.setString   (1, first_name);
            preparedStmt.setString(2, last_name);
            preparedStmt.setString(3, username);
            preparedStmt.setString(4, cellphone);
            preparedStmt.setString(5, password);
            // execute the java preparedstatement
            preparedStmt.executeUpdate();

            preparedStmt.close();
            conn.close();

        }

        catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (preparedStmt != null)
                    preparedStmt.close();
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


    public static ArrayList<String> getUser(HttpSession session){
            //String userName = (String)session.getAttribute("username");
            String userName = "john@gmail.com";
        Connection conn = null;
        PreparedStatement preparedStmt = null;

        ArrayList<String> userinf = new ArrayList<String>();

        try {
            Class.forName(JDBC_DRIVER).newInstance();

            conn = DriverManager.getConnection(DB_URL, USER, PASS);


            String query = " SELECT * FROM user WHERE username = " + "'"+ userName+ "'" ;
            preparedStmt = conn.prepareStatement(query);

            // execute the java preparedstatement
           ResultSet sqlResult = preparedStmt.executeQuery();

            String username = sqlResult.getString(1);
            String first_name = sqlResult.getString(2);
            String last_name = sqlResult.getString(3);
            String cellphone = sqlResult.getString(4);
            userinf.add(username);
            userinf.add(first_name);
            userinf.add(last_name);
            userinf.add(cellphone);
            sqlResult.close();


        }

        catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (preparedStmt != null)
                    preparedStmt.close();
            } catch (SQLException se2) {
            }
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }

        return userinf;


    }




}
