package filehub.demo;

import com.sun.istack.internal.Nullable;

import java.sql.*;
import java.util.ArrayList;

public class GroupModel {

    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://p3plcpnl0569.prod.phx3.secureserver.net:3306/cs157a";
    static final String USER = "cs157a_main";
    static final String PASS = "cs157a_db";

    public static ArrayList<Groups> getAllGroup() {
        ArrayList<Groups> returnGroup = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName(JDBC_DRIVER).newInstance();

            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            stmt = conn.createStatement();
            String myQuery;
            myQuery = "SELECT * FROM groups";
            ResultSet allGroup = stmt.executeQuery(myQuery);

            if (allGroup != null) {
                try {
                    while (allGroup.next()) {
                        int id = allGroup.getInt("id");
                        String group_name = allGroup.getString("group_name");
                        int group_owner = allGroup.getInt("group_owner");
                        String group_password = allGroup.getString("group_password");
                        String group_status = allGroup.getString("group_status");
                        String created_on = allGroup.getString("created_on");
                        System.out.println(created_on);
                        returnGroup.add(new Groups(id, group_name, group_owner, group_password, group_status, created_on));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            stmt.close();
            conn.close();
            allGroup.close();
            return returnGroup;
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
        return null;
    }


    public static void insertGroup(String groupName, int groupOwner, String groupPassword) {
        Connection conn = null;
        Statement stmt = null;

        try {
            Class.forName(JDBC_DRIVER).newInstance();

            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            stmt = conn.createStatement();
            String myQuery;
            myQuery = "INSERT INTO groups (group_name, group_owner, group_password, group_status) " +
                    "VALUES ('"+groupName+"', "+groupOwner+", '"+groupPassword+"', 'Active');";
            stmt.executeUpdate(myQuery);

            stmt.close();
            conn.close();
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

    public static int checkGroupExit(String groupName){
        Connection conn = null;
        Statement stmt = null;
        int found = 0;

        try {
            Class.forName(JDBC_DRIVER).newInstance();
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();

            String myQuery = "SELECT EXISTS (SELECT * FROM  groups WHERE group_name=" + "'" + groupName + "')";

            ResultSet re = stmt.executeQuery(myQuery);

            while(re.next()){
                found = re.getInt(1);
            }

            stmt.close();
            conn.close();
        }catch (SQLException e){
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }

        return found;
    }

    public static int countGroup(int userId){
        Connection conn = null;
        Statement stmt = null;
        int count = 0;

        try {
            Class.forName(JDBC_DRIVER).newInstance();
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();

            String myQuery = "SELECT count(*)  FROM  groups WHERE group_owner="  + userId;

            ResultSet re = stmt.executeQuery(myQuery);

            while(re.next()){
                count = re.getInt(1);
            }
            System.out.println("userid" + userId+ ": " + count + "groups");
            stmt.close();
            conn.close();
        }catch (SQLException e){
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }

        return count;


    }
    public static void testSQLConnectionWorking() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("Connection OK");
        } catch (SQLException e) {
            System.out.println("Connection Not OK");
        } finally {
            if (connection != null) try {
                connection.close();
            } catch (SQLException ignore) {
            }
        }
    }
}