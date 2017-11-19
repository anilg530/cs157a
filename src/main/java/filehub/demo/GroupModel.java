package filehub.demo;

import com.sun.istack.internal.Nullable;

import java.sql.*;
import java.util.ArrayList;

public class GroupModel {

    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://p3plcpnl0569.prod.phx3.secureserver.net:3306/cs157a";
    static final String USER = "cs157a_main";
    static final String PASS = "cs157a_db";
    static final String ACTIVE = "Active";
    static final String INACTIVE = "Inactive";
    static final int GUEST_PERMISSION = 1;
    static final int USER_PERMISSION = 2;
    static final int ADVANCED_USER_PERMISSION = 3;
    static final int ADMIN_PERMISSION = 4; //owner
    static final int MASTER_PERMISSION = 5;

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
                        //System.out.println(created_on);
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


    /*
    get all group of a user
     */
    public static ArrayList<Groups> getAllGroups(int user_id) {
        Connection conn = null;
        Statement stmt = null;
        ArrayList<Groups> groups = new ArrayList<>();
        try {
            Class.forName(JDBC_DRIVER).newInstance();

            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            stmt = conn.createStatement();
            conn.setAutoCommit(false);
           String query = "select group_members.group_id, group_members.user_permission, " +
                          "groups.group_name, groups.group_owner, groups.created_on " +
                          "from group_members " +
                          "inner join groups on group_members.group_id=groups.id " +
                          "inner join user on groups.group_owner=user.id " +
                          " where group_members.user_id=" + user_id +"" +
                          " and groups.group_status = 'Active';";
            /*
            select group_members.group_id, group_members.user_permission,
	               groups.`group_name`, groups.group_owner, groups.created_on
            from group_members
            inner join groups on group_members.`group_id`=groups.`id`
            inner join user on groups.`group_owner`=user.`id`
            where group_members.user_id=1
	              and groups.`group_status`= 'Active';
             */
           // System.out.println(query);
            ResultSet rs  = stmt.executeQuery(query);
            while(rs.next()){
                int userID = rs.getInt("group_id");
                int userPermission = rs.getInt("user_permission");
                String groupName = rs.getString("group_name");
                int groupOwner = rs.getInt("group_owner");
                String createdOn = rs.getString("created_on");
                groups.add(new Groups(userID, userPermission, groupName, groupOwner, createdOn));
            }

            /*for(Groups g: groups){
                System.out.println("id "+ g.getId());
                System.out.println("permission "+ g.getUser_permission());
                System.out.println("group name "+ g.getGroup_name());
                System.out.println("group owner "+ g.getGroup_owner());
                System.out.println("created on "+ g.getCreated_on());
            }*/

            conn.commit();

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

        return groups;
    }

    public static void insertGroup(String groupName, int groupOwner, String groupPassword) {
        Connection conn = null;
        Statement stmt = null;

        try {
            Class.forName(JDBC_DRIVER).newInstance();

            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            stmt = conn.createStatement();
            conn.setAutoCommit(false);

            ArrayList<String> queries = new ArrayList<>();
            int maxGroupId =0;
            ResultSet maxIdQuery = stmt.executeQuery("SELECT * FROM groups ORDER BY id DESC LIMIT 1");
            if (maxIdQuery.next()) {
                maxGroupId = maxIdQuery.getInt("id") +1;
            }
            queries.add("start transaction;");
           queries.add("INSERT INTO groups (id, group_name, group_owner, group_password, group_status)" +
                            " VALUES ("+ maxGroupId+ ", '" + groupName+"'" +", " + groupOwner+ ", " + "'"+groupPassword+"'" + ", 'Active') ");

            queries.add( "INSERT INTO group_members (user_id, group_id, user_permission)" +
                            " VALUES (" + groupOwner +","+ maxGroupId+", " + ADMIN_PERMISSION + ") ");
            queries.add("COMMIT;");
            for(String query: queries){
                stmt.addBatch(query);
            }
            stmt.executeBatch();

            conn.commit();

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
           // System.out.println("userid" + userId+ ": " + count + "groups");
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

    public static boolean isGroupPassCorrect(String groupName, String inputPassword){
        boolean status = false;
        Connection conn = null;
        Statement stmt = null;
        String temp = "";

        try {
            Class.forName(JDBC_DRIVER).newInstance();
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();

            String myQuery = "SELECT group_password " +
                             "FROM groups " +
                             "WHERE group_name = '" + groupName.trim()+"';";
            System.out.println(myQuery);
            ResultSet re = stmt.executeQuery(myQuery);

            while(re.next()){
                temp = re.getString("group_password");
            }
            System.out.println("input pass= " + inputPassword+ " pass retrieved " + temp);
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

        if(temp.equals("")){
            status = false;
        }else{
            if(inputPassword.equals(temp)){
                status = true;
            }else {
                status = false;
            }
        }
        return status;
    }


    public static boolean deleteGroup(int ownerId, int groupId){
        Connection conn = null;
        Statement stmt = null;
        boolean status = false;
        try {
            Class.forName(JDBC_DRIVER).newInstance();
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();

            String myQuery = "UPDATE groups SET group_status = '"+ INACTIVE+"' where groups.group_owner = "+ownerId+" and groups.id = "+groupId+";";
            System.out.println(myQuery);
            int re = stmt.executeUpdate(myQuery);
            System.out.println("total lines updated " + re);
            if(re ==1 ){
                status = true;
            }else {
                status = false;
            }
            stmt.close();
            conn.close();
        }catch (SQLException e){
            e.printStackTrace();
            status = false;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            status = false;
        } catch (InstantiationException e) {
            e.printStackTrace();
            status = false;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            status = false;
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

        return status;

    }

    public static boolean isOwner(int userPermission){
        if(userPermission==4){
            return true;
        }
        return false;
    }

    public static boolean isAdvancedUser(int userPermission){
        if(userPermission==3){
            return true;
        }
        return false;
    }

    public static boolean isUser(int userPermission){
        if(userPermission==2){
            return true;
        }
        return false;
    }

    public static boolean isGuest(int userPermission){
        if(userPermission==1){
            return true;
        }
        return false;
    }

}