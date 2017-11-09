package filehub.demo;

import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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

//            String myQuery2;
//            myQuery = "INSERT INTO USER_PROFILE (username, )" +
//                    "VALUES ('"+ username +"','"+ password +"', 'Active');";
//            stmt.executeUpdate(myQuery2);



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

    }


    public static void insertUser1(ArrayList<String> user_info){

        Connection conn = null;
        Statement stmt = null;


        String username = user_info.get(0);
        String password = user_info.get(1);
        String firstname = user_info.get(2);
        String lastname = user_info.get(3);
        String cellphone= user_info.get(4);
        String email = user_info.get(5);



        try {
            Class.forName(JDBC_DRIVER).newInstance();

            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            stmt = conn.createStatement();
            String myQuery;
            if()
            myQuery = "INSERT INTO usertest (username, password,firstname,lastname,cellphone,email)" +
                    "VALUES ('"+ username +"','"+ password +"', '"+ firstname +"','"+lastname+"','"+cellphone+"','"+email+"');";
            stmt.executeUpdate(myQuery);

            stmt.close();
            conn.close();
            //allGroup.close();
        }

        catch (SQLException se) {
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
}
