package filehub.demo;

import jdk.nashorn.internal.runtime.regexp.joni.Regex;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CommonModel {

    public static Boolean isLettersNumbersUnderscoreOnlyString(String string) {
        String regex = "^[a-zA-Z0-9_]*$";
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
}
