package filehub.demo;

import com.sun.istack.internal.Nullable;

import javax.servlet.http.HttpSession;
import java.sql.*;
import java.util.ArrayList;

public class GroupModel {

    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://p3plcpnl0569.prod.phx3.secureserver.net:3306/cs157a";
    static final String USER = "cs157a_main";
    static final String PASS = "cs157a_db";
    static final String ACTIVE = "Active";
    static final String INACTIVE = "Inactive";
    static final String ACCEPTED = "Accepted";
    static final String PENDING = "Pending";
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
                    " where group_members.user_id=" + user_id + "" +
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
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
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
            int maxGroupId = 0;
            ResultSet maxIdQuery = stmt.executeQuery("SELECT * FROM groups ORDER BY id DESC LIMIT 1");
            if (maxIdQuery.next()) {
                maxGroupId = maxIdQuery.getInt("id") + 1;
            }
            queries.add("start transaction;");
            queries.add("INSERT INTO groups (id, group_name, group_owner, group_password, group_status)" +
                    " VALUES (" + maxGroupId + ", '" + groupName + "'" + ", " + groupOwner + ", " + "'" + groupPassword + "'" + ", 'Active') ");

            queries.add("INSERT INTO group_members (user_id, group_id, user_permission)" +
                    " VALUES (" + groupOwner + "," + maxGroupId + ", " + ADMIN_PERMISSION + ") ");
            queries.add("COMMIT;");
            for (String query : queries) {
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

    public static int checkGroupExit(String groupName) {
        Connection conn = null;
        Statement stmt = null;
        int found = 0;

        try {
            Class.forName(JDBC_DRIVER).newInstance();
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();

            String myQuery = "SELECT EXISTS (SELECT * FROM  groups WHERE group_name=" + "'" + groupName + "')";

            ResultSet re = stmt.executeQuery(myQuery);

            while (re.next()) {
                found = re.getInt(1);
            }

            stmt.close();
            conn.close();
        } catch (SQLException e) {
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

    public static int countGroup(int userId) {
        Connection conn = null;
        Statement stmt = null;
        int count = 0;

        try {
            Class.forName(JDBC_DRIVER).newInstance();
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();

            String myQuery = "SELECT count(*)  FROM  groups WHERE group_owner=" + userId;

            ResultSet re = stmt.executeQuery(myQuery);

            while (re.next()) {
                count = re.getInt(1);
            }
            // System.out.println("userid" + userId+ ": " + count + "groups");
            stmt.close();
            conn.close();
        } catch (SQLException e) {
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

    public static boolean isGroupPassCorrect(String groupName, String inputPassword) {
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
                    "WHERE group_name = '" + groupName.trim() + "';";
            System.out.println(myQuery);
            ResultSet re = stmt.executeQuery(myQuery);

            while (re.next()) {
                temp = re.getString("group_password");
            }
            System.out.println("input pass= " + inputPassword + " pass retrieved " + temp);
            stmt.close();
            conn.close();
        } catch (SQLException e) {
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

        if (temp.equals("")) {
            status = false;
        } else {
            if (inputPassword.equals(temp)) {
                status = true;
            } else {
                status = false;
            }
        }
        return status;
    }


    public static boolean deleteGroup(int ownerId, int groupId) {
        Connection conn = null;
        Statement stmt = null;
        boolean status = false;
        try {
            Class.forName(JDBC_DRIVER).newInstance();
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();

            String myQuery = "UPDATE groups SET group_status = '" + INACTIVE + "' where groups.group_owner = " + ownerId + " and groups.id = " + groupId + ";";
            System.out.println(myQuery);
            int re = stmt.executeUpdate(myQuery);
            System.out.println("total lines updated " + re);
            if (re == 1) {
                status = true;
            } else {
                status = false;
            }
            stmt.close();
            conn.close();
        } catch (SQLException e) {
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

    public static boolean isOwner(int userPermission) {
        if (userPermission == 4) {
            return true;
        }
        return false;
    }

    public static boolean isAdvancedUser(int userPermission) {
        if (userPermission == 3) {
            return true;
        }
        return false;
    }

    public static boolean isUser(int userPermission) {
        if (userPermission == 2) {
            return true;
        }
        return false;
    }

    public static boolean isGuest(int userPermission) {
        if (userPermission == 1) {
            return true;
        }
        return false;
    }

    public static String getGroupInviteCode(String from_user_id, String user_id, String group_id, String invite_access_level) {
        String returnString = "";

        if (isGroupInviteAlreadyExist(from_user_id, user_id, group_id, invite_access_level)) {
            Connection conn = null;
            PreparedStatement pstmt = null;
            try {
                Class.forName(CommonModel.JDBC_DRIVER).newInstance();

                conn = DriverManager.getConnection(CommonModel.DB_URL, CommonModel.USER, CommonModel.PASS);

                String myQuery;
                myQuery = "SELECT id FROM group_invites WHERE (invite_status = ? AND user_id = ? AND invited_by_id = ? AND group_id = ? AND invite_access_level = ?)";
                pstmt = conn.prepareStatement(myQuery);
                pstmt.setString(1, "Pending");
                pstmt.setString(2, user_id);
                pstmt.setString(3, from_user_id);
                pstmt.setString(4, group_id);
                pstmt.setString(5, invite_access_level);
                ResultSet sqlResult = pstmt.executeQuery();
                if (sqlResult != null) {
                    if (sqlResult.isBeforeFirst()) {
                        sqlResult.next();
                        returnString = sqlResult.getString(1);
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
        } else {
            // Invite code doesn't exist. Create one
            returnString = CommonModel.generateRandomCode();

            Connection conn = null;

            PreparedStatement pstmt = null;
            try {
                Class.forName(CommonModel.JDBC_DRIVER).newInstance();

                conn = DriverManager.getConnection(CommonModel.DB_URL, CommonModel.USER, CommonModel.PASS);

                String myQuery;
                myQuery = "INSERT INTO group_invites(id,invite_status,user_id,invited_by_id,group_id,invite_access_level) values(?, ?, ?, ?, ?, ?) ";
                pstmt = conn.prepareStatement(myQuery, Statement.RETURN_GENERATED_KEYS);
                pstmt.setString(1, returnString);
                pstmt.setString(2, "Pending");
                pstmt.setString(3, user_id);
                pstmt.setString(4, from_user_id);
                pstmt.setString(5, group_id);
                pstmt.setString(6, invite_access_level);
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

    public static boolean isGroupInviteAlreadyExist(String from_user_id, String user_id, String group_id, String invite_access_level) {
        boolean returnBoolean = false;
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            Class.forName(CommonModel.JDBC_DRIVER).newInstance();

            conn = DriverManager.getConnection(CommonModel.DB_URL, CommonModel.USER, CommonModel.PASS);

            String myQuery;
            myQuery = "SELECT id FROM group_invites WHERE (invite_status = ? AND user_id = ? AND invited_by_id = ? AND group_id = ? AND invite_access_level = ?)";
            pstmt = conn.prepareStatement(myQuery);
            pstmt.setString(1, "Pending");
            pstmt.setString(2, user_id);
            pstmt.setString(3, from_user_id);
            pstmt.setString(4, group_id);
            pstmt.setString(5, invite_access_level);
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

    public static ArrayList<String> getCodeData(String user_id, String invite_code) {
        ArrayList<String> returnArray = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            Class.forName(CommonModel.JDBC_DRIVER).newInstance();

            conn = DriverManager.getConnection(CommonModel.DB_URL, CommonModel.USER, CommonModel.PASS);

            String myQuery;
            myQuery = "SELECT * FROM group_invites WHERE id = ? AND user_id = ? AND invite_status = 'Pending'";
            pstmt = conn.prepareStatement(myQuery);
            pstmt.setString(1, invite_code);
            pstmt.setString(2, user_id);
            ResultSet sqlResult = pstmt.executeQuery();
            if (sqlResult != null) {
                if (sqlResult.isBeforeFirst()) {
                    if (sqlResult.next()) {
                        returnArray.add(sqlResult.getString(1));
                        returnArray.add(sqlResult.getString(2));
                        returnArray.add(sqlResult.getString(3));
                        returnArray.add(sqlResult.getString(4));
                        returnArray.add(sqlResult.getString(5));
                        returnArray.add(sqlResult.getString(6));
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

    public static void addNewMemberByInviteCode(String user_id, String group_id, String invite_access_level) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            Class.forName(CommonModel.JDBC_DRIVER).newInstance();

            conn = DriverManager.getConnection(CommonModel.DB_URL, CommonModel.USER, CommonModel.PASS);

            String myQuery;
            myQuery = "INSERT INTO group_members(user_id,group_id,user_permission) values(?, ?, ?) ";
            pstmt = conn.prepareStatement(myQuery, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, user_id);
            pstmt.setString(2, group_id);
            pstmt.setString(3, invite_access_level);
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

    public static void removeAllInvitesByUserIDAndGroup(String user_id, String group_id) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            Class.forName(CommonModel.JDBC_DRIVER).newInstance();

            conn = DriverManager.getConnection(CommonModel.DB_URL, CommonModel.USER, CommonModel.PASS);

            String myQuery;
            myQuery = "UPDATE group_invites SET invite_status = 'Accepted' WHERE (user_id = ? AND group_id = ?)";
            pstmt = conn.prepareStatement(myQuery, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, user_id);
            pstmt.setString(2, group_id);
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

    public static boolean isInvited(int user_id, int group_id){
        boolean invited = false;
        Connection conn = null;
        Statement stmt = null;
        int count = 0;
        try {
            Class.forName(JDBC_DRIVER).newInstance();
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            String myQuery;
            myQuery = "SELECT count(*) as invites FROM group_invites " +
                      "where user_id = "+user_id+
                           " and group_id ="+group_id +";";
            ResultSet re = stmt.executeQuery(myQuery);
            while (re.next()){
                count = re.getInt("invites");
                System.out.println("invitation "+ count);
                if(count > 0){
                    invited = true;
                }
            }
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
        System.out.println("invited? "+ invited);
        return invited;
    }



    public static boolean isInvitationPending(int user_id, int group_id){
        boolean invitedPending = false;
        Connection conn = null;
        Statement stmt = null;
        int count = 0;
        try {
            Class.forName(JDBC_DRIVER).newInstance();
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            String myQuery;
            myQuery = "SELECT count(*) as invites FROM group_invites " +
                    "where user_id = "+user_id+
                    " and group_id ="+group_id +
                    " and invite_status='"+PENDING+"';";
            ResultSet re = stmt.executeQuery(myQuery);
            while (re.next()){
                count = re.getInt("invites");
                System.out.println("invitation pending "+ count);
                if(count > 0){
                    invitedPending = true;
                }
            }
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
        System.out.println("invitedPending? "+ invitedPending);
        return invitedPending;
    }

    public static boolean isInvitationAccepted(int user_id, int group_id){
        boolean invitedAccepted = false;
        Connection conn = null;
        Statement stmt = null;
        int count = 0;
        try {
            Class.forName(JDBC_DRIVER).newInstance();
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            String myQuery;
            myQuery = "SELECT count(*) as invites FROM group_invites " +
                    "where user_id = "+user_id+
                    " and group_id ="+group_id +
                    " and invite_status='"+ACCEPTED+"';";
            ResultSet re = stmt.executeQuery(myQuery);
            while (re.next()){
                count = re.getInt("invites");
                System.out.println("invitation accepted "+ count);
                if(count > 0){
                    invitedAccepted = true;
                }
            }
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
        System.out.println("invitedAccepted? "+ invitedAccepted);
        return invitedAccepted;
    }

    public static boolean isMember(int user_id, int group_id){
        boolean member = false;
        Connection conn = null;
        Statement stmt = null;
        int count = 0;
        try {
            Class.forName(JDBC_DRIVER).newInstance();
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            String myQuery;
            myQuery = "SELECT count(*) as isMember FROM group_members " +
                    "where user_id = "+user_id+
                    " and group_id ="+group_id +";";
            ResultSet re = stmt.executeQuery(myQuery);
            while (re.next()){
                count = re.getInt("isMember");
                System.out.println("isMember "+ count);
                if(count == 1){
                    member = true;
                }
            }
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
        System.out.println("member? "+ member);
        return member;
    }

    public static ArrayList<GroupMember> getAllGroupMembers(int group_id){
       ArrayList<GroupMember> members = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;

        try {
            Class.forName(JDBC_DRIVER).newInstance();
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            String myQuery;
            myQuery = "SELECT concat(user.`first_name`, ' ',user.`last_name`) as member, `group_members`.`user_permission` " +
                    "from group_members, `user` " +
                    "where group_members.`user_id` = user.`id` " +
                    "      and group_members.`group_id`="+group_id+";";
            ResultSet re = stmt.executeQuery(myQuery);
            while (re.next()){
                String fullName = re.getString("member");
                int userPermission = re.getInt("user_permission");
                members.add(new GroupMember(fullName, userPermission));
            }
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

        return members;
    }

    public static String getPermissionString(int user_permission){
        String permission = "";
        switch (user_permission){
            case 1:
                permission = "Guest";
                break;
            case 2:
                permission = "User";
                break;
            case 3:
                permission = "Advanced User";
                break;
            case 4:
                permission = "Admin";
                break;

        }

        return permission;
    }


    public static String getGroupName(int group_id){
       String groupName = null;
        Connection conn = null;
        Statement stmt = null;

        try {
            Class.forName(JDBC_DRIVER).newInstance();
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            String myQuery;
            myQuery = "select groups.`group_name` " +
                    "from groups " +
                    "where groups.`id`="+group_id+";";
            ResultSet re = stmt.executeQuery(myQuery);
            while (re.next()){
                groupName = re.getString("group_name");
            }
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

        return groupName;
    }
}