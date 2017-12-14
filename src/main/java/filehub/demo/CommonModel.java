package filehub.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

@Component
public class CommonModel {
    static String JDBC_DRIVER;
    static String DB_SERVER;
    static String DB_USER;
    static String DB_PASS;

    @Value("${JDBC_DRIVER}")
    public void jdbcDriver(String JDBC_DRIVER) {
        this.JDBC_DRIVER = JDBC_DRIVER;
    }

    @Value("${DB_SERVER}")
    public void jdbcServer(String DB_SERVER) {
        this.DB_SERVER = DB_SERVER;
    }

    @Value("${DB_USER}")
    public void jdbcUser(String DB_USER) {
        this.DB_USER = DB_USER;
    }

    @Value("${DB_PASS}")
    public void jdbcPassword(String DB_PASS) {
        this.DB_PASS = DB_PASS;
    }

    public static Boolean isLettersNumbersUnderscoreOnlyString(String string) {
        String regex = "^[a-zA-Z0-9_]*$";
        return string.matches(regex);
    }

    public static Boolean isLettersNumbersUnderscoreSpaceOnlyString(String string) {
        String regex = "^[a-zA-Z0-9_. ]*$";
        return string.matches(regex);
    }

    public static Boolean isValidFileName(String string) {
        String regex = "^[a-zA-Z0-9_.)('\\[\\] ]*$";
        return string.matches(regex);
    }

    public static Boolean isValidEmailString(String string) {
        String regex = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?";
        return string.matches(regex);
    }

    public static String timeStampToFormalDate(String timestamp) throws ParseException {
        String returnString = "";
        if (timestamp != null && !timestamp.isEmpty()) {
            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = dateFormatter.parse(timestamp);

            DateFormat targetFormat = new SimpleDateFormat("EEEE, MMM d, yyyy");
            returnString = targetFormat.format(date);
        }
        return returnString;
    }

    public static Boolean isLoggedIn(HttpServletRequest request, HttpSession session) {
        Boolean returnBoolean = false;
        if (request.getSession() != null && session.getAttribute("user_id") != null) {
            returnBoolean = true;
        }
        return returnBoolean;
    }

    public static String generateUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String todayDateInYMD() {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }

    public static String todayDateInMDY() {
        return new SimpleDateFormat("MM-dd-yyy").format(new Date());
    }

    public static String timestampInSQLFormat() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    public static String getFullName(String user_id) {
        String returnString = "";
        if (user_id == null || user_id.isEmpty()) {
            return returnString;
        }
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            Class.forName(CommonModel.JDBC_DRIVER).newInstance();
            conn = DriverManager.getConnection(CommonModel.DB_SERVER, CommonModel.DB_USER, CommonModel.DB_PASS);

            String myQuery;
            myQuery = "SELECT first_name,last_name FROM user WHERE (id = ? AND login_status = 'Active')";
            pstmt = conn.prepareStatement(myQuery);
            pstmt.setString(1, user_id);
            ResultSet sqlResult = pstmt.executeQuery();
            if (sqlResult != null) {
                if (sqlResult.isBeforeFirst()) {
                    sqlResult.next();
                    String first_name = sqlResult.getString(1);
                    String last_name = sqlResult.getString(2);
                    if (first_name != null && !first_name.isEmpty()) {
                        returnString = returnString + first_name;
                    }
                    if (last_name != null && !last_name.isEmpty()) {
                        returnString = returnString + " " + last_name;
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
        return returnString;
    }

    public static boolean isInGroup(int user_id, int group_id) {
        boolean returnBoolean = false;
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            Class.forName(CommonModel.JDBC_DRIVER).newInstance();
            conn = DriverManager.getConnection(CommonModel.DB_SERVER,
                    CommonModel.DB_USER, CommonModel.DB_PASS);

            String myQuery;
            myQuery = "SELECT * FROM group_members WHERE (user_id = ? AND group_id = ?)";
            pstmt = conn.prepareStatement(myQuery);
            pstmt.setInt(1, user_id);
            pstmt.setInt(2, group_id);
            ResultSet sqlResult = pstmt.executeQuery();
            if (sqlResult != null) {
                if (sqlResult.isBeforeFirst()) {
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
        return returnBoolean;
    }

    public static String getGroupName(String group_id) {
        String returnString = "";
        if (group_id == null || group_id.isEmpty()) {
            return returnString;
        }
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            Class.forName(CommonModel.JDBC_DRIVER).newInstance();
            conn = DriverManager.getConnection(CommonModel.DB_SERVER, CommonModel.DB_USER, CommonModel.DB_PASS);

            String myQuery;
            myQuery = "SELECT group_name FROM groups WHERE id = ?";
            pstmt = conn.prepareStatement(myQuery);
            pstmt.setString(1, group_id);
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
        return returnString;
    }

    public static boolean isCodeAlreadyOnDB(String code) {
        boolean returnBoolean = false;
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            Class.forName(CommonModel.JDBC_DRIVER).newInstance();

            conn = DriverManager.getConnection(CommonModel.DB_SERVER, CommonModel.DB_USER, CommonModel.DB_PASS);

            String myQuery;
            myQuery = "SELECT url_code FROM file_url WHERE (url_code = ?)";
            pstmt = conn.prepareStatement(myQuery);
            pstmt.setString(1, code);
            ResultSet sqlResult = pstmt.executeQuery();
            if (sqlResult != null) {
                if (sqlResult.isBeforeFirst()) {
                    returnBoolean = true;
                }
                sqlResult.close();
            }
            if (!returnBoolean) {
                String myQuery2;
                myQuery2 = "SELECT id FROM group_invites WHERE (id = ?)";
                pstmt = conn.prepareStatement(myQuery2);
                pstmt.setString(1, code);
                ResultSet sqlResult2 = pstmt.executeQuery();
                if (sqlResult2 != null) {
                    if (sqlResult2.isBeforeFirst()) {
                        returnBoolean = true;
                    }
                    sqlResult2.close();
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
        return returnBoolean;
    }

    private static String getCode() {
        String returnString = "";
        Random r = new Random();

        String characters = "123456789abcdefghijkmnpqrstuvwxyzABCDEFGHJKLMNPQRSTUVWXYZ";
        for (int i = 0; i < 10; i++) {
            returnString = returnString + characters.charAt(r.nextInt(characters.length()));
        }
        return returnString;
    }

    public static String generateRandomCode() {
        String returnString = getCode();
        while (isCodeAlreadyOnDB(returnString)) {
            returnString = getCode();
        }
        return returnString;
    }

    public static String getEmailByUserID(String user_id) {
        String returnString = null;
        Connection conn = null;

        PreparedStatement pstmt = null;
        try {
            Class.forName(CommonModel.JDBC_DRIVER).newInstance();

            conn = DriverManager.getConnection(CommonModel.DB_SERVER, CommonModel.DB_USER, CommonModel.DB_PASS);

            String myQuery;
            myQuery = "SELECT username FROM user WHERE (id = ?)";
            pstmt = conn.prepareStatement(myQuery);
            pstmt.setString(1, user_id);
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
        return returnString;
    }

    public static String getUserIDByEmail(String email) {
        String returnString = null;
        Connection conn = null;

        PreparedStatement pstmt = null;
        try {
            Class.forName(CommonModel.JDBC_DRIVER).newInstance();

            conn = DriverManager.getConnection(CommonModel.DB_SERVER, CommonModel.DB_USER, CommonModel.DB_PASS);

            String myQuery;
            myQuery = "SELECT id FROM user WHERE (username = ?)";
            pstmt = conn.prepareStatement(myQuery);
            pstmt.setString(1, email);
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
        return returnString;
    }

    public static boolean isEmailExist(String email) {
        boolean returnBoolean = false;
        Connection conn = null;

        PreparedStatement pstmt = null;
        try {
            Class.forName(CommonModel.JDBC_DRIVER).newInstance();

            conn = DriverManager.getConnection(CommonModel.DB_SERVER, CommonModel.DB_USER, CommonModel.DB_PASS);

            String myQuery;
            myQuery = "SELECT id FROM user WHERE (username = ?)";
            pstmt = conn.prepareStatement(myQuery);
            pstmt.setString(1, email);
            ResultSet sqlResult = pstmt.executeQuery();
            if (sqlResult != null) {
                if (sqlResult.isBeforeFirst()) {
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
        return returnBoolean;
    }

    public static String getAllGroupIDMembershipCommaSeparated(String user_id) {
        String returnString = "";

        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            Class.forName(CommonModel.JDBC_DRIVER).newInstance();
            conn = DriverManager.getConnection(CommonModel.DB_SERVER, CommonModel.DB_USER, CommonModel.DB_PASS);

            String myQuery;
            myQuery = "SELECT DISTINCT group_members.group_id FROM group_members" +
                    " JOIN groups ON (groups.id=group_members.group_id) " +
                    "WHERE (group_members.user_id = ? AND groups.group_status = ?)";
            pstmt = conn.prepareStatement(myQuery);
            pstmt.setString(1, user_id);
            pstmt.setString(2, "Active");
            ResultSet sqlResult = pstmt.executeQuery();
            if (sqlResult != null && sqlResult.isBeforeFirst()) {
                ArrayList<String> temp = new ArrayList<>();
                while (sqlResult.next()) {
                    temp.add(sqlResult.getString(1));
                }
                String idList = temp.toString();
                returnString = idList.substring(1, idList.length() - 1).replace(", ", ",");
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

    public static int getUserPermissions(String user_id, String group_id) {
        int returnInt = -1;
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            Class.forName(CommonModel.JDBC_DRIVER).newInstance();
            conn = DriverManager.getConnection(CommonModel.DB_SERVER, CommonModel.DB_USER, CommonModel.DB_PASS);

            String myQuery;
            myQuery = "SELECT user_permission FROM group_members WHERE (user_id = ? AND group_id = ?)";
            pstmt = conn.prepareStatement(myQuery);
            pstmt.setString(1, user_id);
            pstmt.setString(2, group_id);
            ResultSet sqlResult = pstmt.executeQuery();
            if (sqlResult != null) {
                if (sqlResult.isBeforeFirst()) {
                    sqlResult.next();
                    returnInt = sqlResult.getInt(1);
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
        return returnInt;
    }

    public static boolean isMaster(String user_id) {
        boolean returnBoolean = false;
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            Class.forName(CommonModel.JDBC_DRIVER).newInstance();

            conn = DriverManager.getConnection(CommonModel.DB_SERVER, CommonModel.DB_USER, CommonModel.DB_PASS);

            String myQuery;
            myQuery = "SELECT role FROM user WHERE (id = ?)";
            pstmt = conn.prepareStatement(myQuery);
            pstmt.setString(1, user_id);
            ResultSet sqlResult = pstmt.executeQuery();
            if (sqlResult != null && sqlResult.isBeforeFirst()) {
                sqlResult.next();
                String role = sqlResult.getString(1);
                if (role != null && role.equals("5")) {
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
        return returnBoolean;
    }

    public static String getUserPermissionsString(String user_id, String group_id) {
        String returnString = "";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            Class.forName(CommonModel.JDBC_DRIVER).newInstance();
            conn = DriverManager.getConnection(CommonModel.DB_SERVER, CommonModel.DB_USER, CommonModel.DB_PASS);

            String myQuery;
            myQuery = "SELECT user_permissions_type.permission_formal FROM group_members JOIN user_permissions_type ON user_permissions_type.id=group_members.user_permission WHERE (group_members.user_id = ? AND group_members.group_id = ?)";
            pstmt = conn.prepareStatement(myQuery);
            pstmt.setString(1, user_id);
            pstmt.setString(2, group_id);
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
        return returnString;
    }

    public static HashMap<String, String> getUserPermissionsType() {
        HashMap<String, String> returnMap = new HashMap<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            Class.forName(CommonModel.JDBC_DRIVER).newInstance();

            conn = DriverManager.getConnection(CommonModel.DB_SERVER, CommonModel.DB_USER, CommonModel.DB_PASS);

            String myQuery;
            myQuery = "SELECT * FROM user_permissions_type WHERE id!='4'";
            pstmt = conn.prepareStatement(myQuery, Statement.RETURN_GENERATED_KEYS);
            ResultSet sqlResult = pstmt.executeQuery();
            if (sqlResult != null) {
                while (sqlResult.next()) {
                    String user_type_id = sqlResult.getString(1);
                    String user_type_formal = sqlResult.getString(2);
                    returnMap.put(user_type_id, user_type_formal);
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
        return returnMap;
    }

}