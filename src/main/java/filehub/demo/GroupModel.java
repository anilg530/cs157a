package filehub.demo;

import java.sql.*;
import java.util.ArrayList;

public class GroupModel {

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
            Class.forName(CommonModel.JDBC_DRIVER).newInstance();
            conn = DriverManager.getConnection(CommonModel.DB_SERVER, CommonModel.DB_USER, CommonModel.DB_PASS);

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
    return: ArrayList<Groups>
     */
    public static ArrayList<Groups> getAllGroups(int user_id) {
        ArrayList<Groups> groups = new ArrayList<>();

        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            Class.forName(CommonModel.JDBC_DRIVER).newInstance();
            conn = DriverManager.getConnection(CommonModel.DB_SERVER, CommonModel.DB_USER, CommonModel.DB_PASS);
            String myQuery;
            myQuery = "SELECT * FROM group_members JOIN groups ON groups.id=group_members.group_id WHERE group_members.user_id = ? AND groups.group_status = 'Active'";
            pstmt = conn.prepareStatement(myQuery);
            pstmt.setString(1, Integer.toString(user_id));
            ResultSet sqlResult = pstmt.executeQuery();
            if (sqlResult != null && sqlResult.isBeforeFirst()) {
                while (sqlResult.next()) {
                    int userID = sqlResult.getInt("group_id");
                    int userPermission = sqlResult.getInt("user_permission");
                    String groupName = sqlResult.getString("group_name");
                    int groupOwner = sqlResult.getInt("group_owner");
                    String createdOn = sqlResult.getString("created_on");
                    groups.add(new Groups(userID, userPermission, groupName, groupOwner, createdOn));
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
        return groups;
    }

    /*
     * add a new group
     * return true: inserted successfully
     *        false: otherwise
     */
    public static boolean insertGroup(String groupName, int groupOwner, String groupPassword) {
        boolean success = false;
        int new_group_id = 0;
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            Class.forName(CommonModel.JDBC_DRIVER).newInstance();
            conn = DriverManager.getConnection(CommonModel.DB_SERVER, CommonModel.DB_USER, CommonModel.DB_PASS);

            String myQuery;
            myQuery = "INSERT INTO groups(group_name,group_owner,group_password,group_status)" +
                    "VALUES (?, ?, ?, ?)";
            pstmt = conn.prepareStatement(myQuery, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, groupName);
            pstmt.setInt(2, groupOwner);
            pstmt.setString(3, groupPassword);
            pstmt.setString(4, "Active");
            pstmt.executeUpdate();
            ResultSet sqlResult = pstmt.getGeneratedKeys();
            if (sqlResult != null && sqlResult.isBeforeFirst()) {
                sqlResult.next();
                new_group_id = sqlResult.getInt(1);
                sqlResult.close();
            }
            if (new_group_id != 0) {
                String myQuery2;
                myQuery2 = "INSERT INTO group_members(user_id,group_id,user_permission)" +
                        "VALUES (?, ?, ?)";
                pstmt = conn.prepareStatement(myQuery2, Statement.RETURN_GENERATED_KEYS);
                pstmt.setInt(1, groupOwner);
                pstmt.setInt(2, new_group_id);
                pstmt.setInt(3, ADMIN_PERMISSION);
                pstmt.executeUpdate();
                sqlResult = pstmt.getGeneratedKeys();

                success = true;
                if (sqlResult != null && sqlResult.isBeforeFirst()) {
                    sqlResult.close();
                }
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
        return success;
    }

    /*
     * check if a group name is already exist
     * return: 1 - yes
     *         0 - no
     */
    public static int checkGroupExist(String groupName) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int found = 0;

        try {

            Class.forName(CommonModel.JDBC_DRIVER).newInstance();
            conn = DriverManager.getConnection(CommonModel.DB_SERVER, CommonModel.DB_USER, CommonModel.DB_PASS);
            //stmt = conn.createStatement();
            String myQuery = "select EXISTS(select * from groups where `group_name` = ?);";
            stmt = conn.prepareStatement(myQuery);
            stmt.setString(1, groupName);
            ResultSet re = stmt.executeQuery();
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

    /*
     * check a group is invalid
     * return 1: invalid
     *        0: valid
     */
    public static int isInvalid(int groupId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int found = 0;

        try {
            Class.forName(CommonModel.JDBC_DRIVER).newInstance();
            conn = DriverManager.getConnection(CommonModel.DB_SERVER, CommonModel.DB_USER, CommonModel.DB_PASS);


            String myQuery = "select exists(select * from groups where `id` = ? and `group_status`='Inactive');";
            stmt = conn.prepareStatement(myQuery);
            stmt.setInt(1, groupId);
            ResultSet re = stmt.executeQuery();

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

    /*
     * add new member to the group with a permission
     * return boolean - true: added successfully.
     *                  false: otherwise
     */
    public static boolean addMember(int userId, int groupId, int permission) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;

        try {
            Class.forName(CommonModel.JDBC_DRIVER).newInstance();
            conn = DriverManager.getConnection(CommonModel.DB_SERVER, CommonModel.DB_USER, CommonModel.DB_PASS);

            String myQuery = "insert into group_members values(?, ?, ?);";
            pstmt = conn.prepareStatement(myQuery);
            pstmt.setInt(1, userId);
            pstmt.setInt(2, groupId);
            pstmt.setInt(3, permission);
            int rowaffected = pstmt.executeUpdate();

            if (rowaffected == 1) {
                success = true;
            }
            pstmt.close();
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
                if (pstmt != null)
                    pstmt.close();
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

        return success;
    }


    /*
     * compare password
     * return boolean: true - passwords match
     *                 false - otherwise
     */

    public static int checkGroupExist(String groupName, int ownerId) {
        Connection conn = null;
        Statement stmt = null;
        int found = 0;

        try {
            Class.forName(CommonModel.JDBC_DRIVER).newInstance();
            conn = DriverManager.getConnection(CommonModel.DB_SERVER, CommonModel.DB_USER, CommonModel.DB_PASS);

            stmt = conn.createStatement();

            String myQuery = "select EXISTS(select * from groups where `group_name` = '" + groupName + "'  and `group_owner`=" + ownerId + ");";
            System.out.println(myQuery);
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
            Class.forName(CommonModel.JDBC_DRIVER).newInstance();
            conn = DriverManager.getConnection(CommonModel.DB_SERVER, CommonModel.DB_USER, CommonModel.DB_PASS);

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
            connection = DriverManager.getConnection(CommonModel.DB_SERVER, CommonModel.DB_USER, CommonModel.DB_PASS);
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
        PreparedStatement stmt = null;
        String temp = "";

        try {
            Class.forName(CommonModel.JDBC_DRIVER).newInstance();
            conn = DriverManager.getConnection(CommonModel.DB_SERVER, CommonModel.DB_USER, CommonModel.DB_PASS);

            String myQuery = "SELECT group_password " +
                    "FROM groups " +
                    "WHERE group_name = ? ;";
            stmt = conn.prepareStatement(myQuery);
            stmt.setString(1, groupName);
            ResultSet re = stmt.executeQuery();
            if (re != null && re.isBeforeFirst()) {
                re.next();
                temp = re.getString("group_password");
                if (inputPassword.equals(temp)) {
                    status = true;
                }
            }
            stmt.close();
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
        return status;
    }


    /*
     * delete a group given ownerId and groupId
     * return boolean - true: deleted successfully. False - otherwise
     */
    public static boolean deleteGroup(int ownerId, int groupId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean status = false;
        try {
            Class.forName(CommonModel.JDBC_DRIVER).newInstance();
            conn = DriverManager.getConnection(CommonModel.DB_SERVER, CommonModel.DB_USER, CommonModel.DB_PASS);

            String myQuery = "UPDATE groups SET group_status = ? where groups.group_owner = ? and groups.id = ?;";
            stmt = conn.prepareStatement(myQuery);
            stmt.setString(1, INACTIVE);
            stmt.setInt(2, ownerId);
            stmt.setInt(3, groupId);
            int re = stmt.executeUpdate(); //re: number of row affected
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

    /*
     * remove a member out of a group
     * return boolean - true: removed successfully, false, otherwise
     */
    public static boolean groupMemberDelete(int userId, int groupId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean status = false;
        try {
            Class.forName(CommonModel.JDBC_DRIVER).newInstance();
            conn = DriverManager.getConnection(CommonModel.DB_SERVER, CommonModel.DB_USER, CommonModel.DB_PASS);

            String myQuery = "delete from `group_members` where `group_id` = ? and `user_id` = ?;";
            pstmt = conn.prepareStatement(myQuery);
            pstmt.setString(1, String.valueOf(groupId));
            pstmt.setString(2, String.valueOf(userId));
            int sqlResult = pstmt.executeUpdate();
            if (sqlResult == 1) {
                status = true;
            } else {
                status = false;
            }
            pstmt.close();
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
                if (pstmt != null)
                    pstmt.close();
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

    /*
    * check if a user is a owner
     */
    public static boolean isOwner(int userPermission) {
        if (userPermission == 4) {
            return true;
        }
        return false;
    }

    /*
    * check if a user is a advanced user
     */
    public static boolean isAdvancedUser(int userPermission) {
        if (userPermission == 3) {
            return true;
        }
        return false;
    }

    /*
    * check if a user is a user
     */
    public static boolean isUser(int userPermission) {
        if (userPermission == 2) {
            return true;
        }
        return false;
    }

    /*
    * check if a user is a guest
     */
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
                conn = DriverManager.getConnection(CommonModel.DB_SERVER, CommonModel.DB_USER, CommonModel.DB_PASS);

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
                conn = DriverManager.getConnection(CommonModel.DB_SERVER, CommonModel.DB_USER, CommonModel.DB_PASS);

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
            conn = DriverManager.getConnection(CommonModel.DB_SERVER, CommonModel.DB_USER, CommonModel.DB_PASS);

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

    public static boolean isGroupInviteAlreadyExist(int user_id, int group_id) {
        boolean returnBoolean = false;
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            Class.forName(CommonModel.JDBC_DRIVER).newInstance();
            conn = DriverManager.getConnection(CommonModel.DB_SERVER, CommonModel.DB_USER, CommonModel.DB_PASS);

            String myQuery;
            myQuery = "select group_invites.`id`, `groups`.`group_name` " +
                    "from group_invites, groups " +
                    "where " +
                    "`group_invites`.`group_id` = groups.`id`and `user_id`=? and `group_id`=? and `invite_status`='Pending';";
            pstmt = conn.prepareStatement(myQuery);

            pstmt.setInt(1, user_id);
            pstmt.setInt(2, group_id);

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

    public static GroupInvite getGroupInvite(int user_id, int group_id) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        GroupInvite groupInvite = null;
        try {
            Class.forName(CommonModel.JDBC_DRIVER).newInstance();
            conn = DriverManager.getConnection(CommonModel.DB_SERVER, CommonModel.DB_USER, CommonModel.DB_PASS);

            String myQuery;
            myQuery = "select group_invites.`id`, `groups`.`group_name`,  `groups`.`id` as group_id, group_invites.`invite_access_level`, group_invites.`invited_by_id` " +
                    "from group_invites, groups " +
                    "where " +
                    "`group_invites`.`group_id` = groups.`id`and `user_id`=? and `group_id`=? and `invite_status`='Pending' limit 1;";
            pstmt = conn.prepareStatement(myQuery);

            pstmt.setInt(1, user_id);
            pstmt.setInt(2, group_id);
            ResultSet sqlResult = pstmt.executeQuery();
            if (sqlResult != null) {
                if (sqlResult.isBeforeFirst()) {
                    if (sqlResult.next()) {
                        groupInvite = new GroupInvite(sqlResult.getString("id"),
                                sqlResult.getInt("group_id"),
                                sqlResult.getString("group_name"),
                                sqlResult.getInt("invited_by_id"),
                                sqlResult.getInt("invite_access_level"));
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

        return groupInvite;
    }

    /**
     * get group id
     *
     * @param groupName
     * @param groupPassword
     * @return int
     */
    public static int getGroupId(String groupName, String groupPassword) {
        int id = 0;
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            Class.forName(CommonModel.JDBC_DRIVER).newInstance();
            conn = DriverManager.getConnection(CommonModel.DB_SERVER, CommonModel.DB_USER, CommonModel.DB_PASS);

            String myQuery;
            myQuery = "select `id`, `group_name`\n" +
                    "from groups\n" +
                    "where `group_name` = ? and `group_password`=?;";
            pstmt = conn.prepareStatement(myQuery);

            pstmt.setString(1, groupName);
            pstmt.setString(2, groupPassword);

            ResultSet sqlResult = pstmt.executeQuery();
            if (sqlResult != null) {
                if (sqlResult.isBeforeFirst()) {
                    if (sqlResult.next()) {
                        id = sqlResult.getInt("id");
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

        System.out.println("id " + id);
        return id;
    }

    public static ArrayList<String> getCodeData(String user_id, String invite_code) {
        ArrayList<String> returnArray = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            Class.forName(CommonModel.JDBC_DRIVER).newInstance();
            conn = DriverManager.getConnection(CommonModel.DB_SERVER, CommonModel.DB_USER, CommonModel.DB_PASS);

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
            conn = DriverManager.getConnection(CommonModel.DB_SERVER, CommonModel.DB_USER, CommonModel.DB_PASS);

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
            conn = DriverManager.getConnection(CommonModel.DB_SERVER, CommonModel.DB_USER, CommonModel.DB_PASS);

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

    public static boolean isInvited(int user_id, int group_id) {
        boolean invited = false;
        Connection conn = null;
        PreparedStatement stmt = null;
        int count = 0;
        try {

            Class.forName(CommonModel.JDBC_DRIVER).newInstance();
            conn = DriverManager.getConnection(CommonModel.DB_SERVER, CommonModel.DB_USER, CommonModel.DB_PASS);
            String myQuery;
            myQuery = "SELECT count(*) as invites FROM group_invites " +
                    "where user_id = ? and group_id =? ";
            stmt = conn.prepareStatement(myQuery);
            stmt.setInt(1, user_id);
            stmt.setInt(2, group_id);
            ResultSet re = stmt.executeQuery();
            while (re.next()) {
                count = re.getInt("invites");
                System.out.println("invitation " + count);
                if (count > 0) {
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
        System.out.println("invited? " + invited);
        return invited;
    }


    public static boolean isInvitationPending(int user_id, int group_id) {
        boolean invitedPending = false;
        Connection conn = null;
        PreparedStatement stmt = null;
        int count = 0;
        try {

            Class.forName(CommonModel.JDBC_DRIVER).newInstance();
            conn = DriverManager.getConnection(CommonModel.DB_SERVER, CommonModel.DB_USER, CommonModel.DB_PASS);
            String myQuery;
            myQuery = "SELECT count(*) as invites FROM group_invites " +
                    "where user_id = ? and group_id =? and invite_status=?;";
            stmt = conn.prepareStatement(myQuery);
            stmt.setInt(1, user_id);
            stmt.setInt(2, group_id);
            stmt.setString(3, PENDING);
            ResultSet re = stmt.executeQuery();
            while (re.next()) {
                count = re.getInt("invites");
                System.out.println("invitation pending " + count);
                if (count > 0) {
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
        return invitedPending;
    }

    public static boolean isInvitationAccepted(int user_id, int group_id) {
        boolean invitedAccepted = false;
        Connection conn = null;
        PreparedStatement stmt = null;
        int count = 0;
        try {

            Class.forName(CommonModel.JDBC_DRIVER).newInstance();
            conn = DriverManager.getConnection(CommonModel.DB_SERVER, CommonModel.DB_USER, CommonModel.DB_PASS);
            String myQuery;
            myQuery = "SELECT count(*) as invites FROM group_invites " +
                    "where user_id = ? and group_id = ? and invite_status= ?;";
            stmt = conn.prepareStatement(myQuery);
            stmt.setInt(1, user_id);
            stmt.setInt(2, group_id);
            stmt.setString(3, ACCEPTED);
            ResultSet re = stmt.executeQuery();
            while (re.next()) {
                count = re.getInt("invites");
                System.out.println("invitation accepted " + count);
                if (count > 0) {
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
        System.out.println("invitedAccepted? " + invitedAccepted);
        return invitedAccepted;
    }

    /**
     * check if a user is a member of a group
     *
     * @param user_id
     * @param group_id
     * @return boolean
     */
    public static boolean isMember(int user_id, int group_id) {
        boolean member = false;
        Connection conn = null;
        PreparedStatement stmt = null;
        int count = 0;
        try {

            Class.forName(CommonModel.JDBC_DRIVER).newInstance();
            conn = DriverManager.getConnection(CommonModel.DB_SERVER, CommonModel.DB_USER, CommonModel.DB_PASS);

            String myQuery;
            myQuery = "SELECT count(*) as isMember FROM group_members " +
                    "where user_id = ? and group_id =?;";
            stmt = conn.prepareStatement(myQuery);
            stmt.setInt(1, user_id);
            stmt.setInt(2, group_id);
            ResultSet re = stmt.executeQuery();
            while (re.next()) {
                count = re.getInt("isMember");
                if (count == 1) {
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
        return member;
    }

    /**
     * get all members of a group
     *
     * @param group_id
     * @return
     */
    public static ArrayList<GroupMember> getAllGroupMembers(int group_id) {
        ArrayList<GroupMember> members = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            Class.forName(CommonModel.JDBC_DRIVER).newInstance();
            conn = DriverManager.getConnection(CommonModel.DB_SERVER, CommonModel.DB_USER, CommonModel.DB_PASS);

            String myQuery;
            myQuery = "SELECT group_members.`user_id`, concat(user.`first_name`, ' ',user.`last_name`) as member, `group_members`.`user_permission` " +
                    "from group_members, `user` " +
                    "where group_members.`user_id` = user.`id` " +
                    "      and group_members.`group_id`=?;";
            stmt = conn.prepareStatement(myQuery);
            stmt.setInt(1, group_id);
            ResultSet re = stmt.executeQuery();
            while (re.next()) {
                String fullName = re.getString("member");
                int userPermission = re.getInt("user_permission");
                int user_id = re.getInt("user_id");
                members.add(new GroupMember(fullName, userPermission, user_id));
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

    public static String getPermissionString(int user_permission) {
        String permission = "";
        switch (user_permission) {
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

    /**
     * get group name
     *
     * @param group_id
     * @return String name
     */
    public static String getGroupName(int group_id) {
        String groupName = null;
        Connection conn = null;
        PreparedStatement stmt = null;

        try {

            Class.forName(CommonModel.JDBC_DRIVER).newInstance();
            conn = DriverManager.getConnection(CommonModel.DB_SERVER, CommonModel.DB_USER, CommonModel.DB_PASS);

            String myQuery;
            myQuery = "select groups.`group_name` " +
                    "from groups " +
                    "where groups.`id`=?;";
            stmt = conn.prepareStatement(myQuery);
            stmt.setInt(1, group_id);
            ResultSet re = stmt.executeQuery();
            while (re.next()) {
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

    /**
     * update user's permission
     *
     * @param userId
     * @param permission
     * @param groupID
     * @return boolean
     */
    public static boolean updatePermission(int userId, int permission, int groupID) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean status = false;
        try {

            Class.forName(CommonModel.JDBC_DRIVER).newInstance();
            conn = DriverManager.getConnection(CommonModel.DB_SERVER, CommonModel.DB_USER, CommonModel.DB_PASS);

            String myQuery = "update group_members " +
                    "set `user_permission`= ? where `user_id`= ? and `group_id`= ?;";
            stmt = conn.prepareStatement(myQuery);
            stmt.setInt(1, permission);
            stmt.setInt(2, userId);
            stmt.setInt(3, groupID);
            int re = stmt.executeUpdate();
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
}