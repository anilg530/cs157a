package filehub.demo;

import com.google.gson.Gson;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Controller
public class MessagingController {

    @RequestMapping("messaging")
    public String messaging(HttpServletRequest request, HttpSession session) {
        if (CommonModel.isLoggedIn(request, session)) {
            return "messaging_view_messages";
        } else {
            return "not_logged_in";
        }
    }

    @RequestMapping(value = {"messaging/refresh_messaging_sidebar"})
    public String refresh_messaging_sidebar(HttpServletRequest request, HttpSession session) {
        if (CommonModel.isLoggedIn(request, session)) {
            return "common/messaging_sidebar";
        } else {
            return null;
        }
    }

    @RequestMapping(value = {"messaging/refresh_message_count"})
    @ResponseBody
    public String refresh_message_count(HttpServletRequest request, HttpSession session) {
        HashMap<String, String> resultArray = new HashMap<>();
        Gson gson = new Gson();
        if (CommonModel.isLoggedIn(request, session)) {
            int user_id = (int) session.getAttribute("user_id");
            int message_count = MessagingModel.getNewMessageCount(Integer.toString(user_id));
            resultArray.put("status", "success");
            resultArray.put("data", Integer.toString(message_count));
            return gson.toJson(resultArray);
        }
        resultArray.put("status", "success");
        resultArray.put("data", "0");
        return gson.toJson(resultArray);
    }

    @RequestMapping(value = {"messaging/send_message"})
    public String send_message(HttpServletRequest request, HttpSession session, Model model) {
        if (!CommonModel.isLoggedIn(request, session)) {
            model.addAttribute("error_message", "You are not logged in");
            return "file_url_modal_error";
        }
        if (request.getMethod().equals("POST") && request.getParameter("send_to") != null) {
            String send_to = request.getParameter("send_to");
            String send_to_username = CommonModel.getEmailByUserID(send_to);
            model.addAttribute("send_to_username", send_to_username);
        }
        return "send_message_modal";
    }

    @RequestMapping(value = {"messaging/send_message_submit"})
    @ResponseBody
    public String send_message_submit(HttpServletRequest request, HttpSession session) {
        HashMap<String, String> resultArray = new HashMap<>();
        Gson gson = new Gson();
        if (CommonModel.isLoggedIn(request, session) && request.getMethod().equals("POST") && request.getParameter("send_to_email") != null && request.getParameter("message") != null) {
            String send_to_email = request.getParameter("send_to_email");
            if (!CommonModel.isEmailExist(send_to_email)) {
                HashMap<String, String> error_array = new HashMap<>();
                error_array.put("send_to_email", "The email you entered is invalid.");
                String error_array_gson = gson.toJson(error_array);
                resultArray.put("status", "fail");
                resultArray.put("error", error_array_gson);
                return gson.toJson(resultArray);
            }
            String message = request.getParameter("message").trim();
            int user_id = (int) session.getAttribute("user_id");
            String user_id_string = Integer.toString(user_id);
            String send_to_id = CommonModel.getUserIDByEmail(send_to_email);
            if (user_id_string.equals(send_to_id)) {
                HashMap<String, String> error_array = new HashMap<>();
                error_array.put("send_to_email", "You cannot send a message to yourself.");
                String error_array_gson = gson.toJson(error_array);
                resultArray.put("status", "fail");
                resultArray.put("error", error_array_gson);
                return gson.toJson(resultArray);
            }
            MessagingModel.insertNewMessage(user_id_string, send_to_id, message);
            resultArray.put("status", "success");
            resultArray.put("toastr", "Message Sent");
            return gson.toJson(resultArray);
        }
        resultArray.put("status", "failed");
        resultArray.put("swal_error", "Unable to send message. Make sure the email is correct and the message is not blank");
        return gson.toJson(resultArray);
    }

    @RequestMapping(value = {"messaging/refresh_received_messages"})
    public String refresh_received_messages(HttpServletRequest request, HttpSession session) {
        if (CommonModel.isLoggedIn(request, session)) {
            return "includes_messaging_receive";
        } else {
            return null;
        }
    }

    @RequestMapping(value = {"messaging/refresh_sent_messages"})
    public String refresh_sent_messages(HttpServletRequest request, HttpSession session) {
        if (CommonModel.isLoggedIn(request, session)) {
            return "includes_messaging_sent";
        } else {
            return null;
        }
    }

    @RequestMapping(value = {"messaging/send_message_autocomplete_suggestions"})
    @ResponseBody
    public String send_message_autocomplete_suggestions(HttpServletRequest request, HttpSession session) {
//        Map<String, String[]> temp = request.getParameterMap();
//        for (Map.Entry<String, String[]> entry : temp.entrySet()) {
//            String key = entry.getKey();
//            String value[] = entry.getValue();
//            System.out.println("key: " + key);
//            for (int i = 0; i < value.length; i++) {
//                System.out.println(value[i]);
//            }
//        }
        if (CommonModel.isLoggedIn(request, session) && request.getParameter("query") != null) {
            int user_id = (int) session.getAttribute("user_id");
            String user_id_string = Integer.toString(user_id);
            return MessagingModel.getJSONUserSuggestionSearchByEmailFormalName(user_id_string, request.getParameter("query"));
        }
        return MessagingModel.getJSONUserSuggestionSearchByEmailFormalName(null, "");
    }

    @RequestMapping(value = {"messaging/send_issue"})
    public String send_issue(HttpServletRequest request, HttpSession session, Model model) {
        if (!CommonModel.isLoggedIn(request, session)) {
            model.addAttribute("error_message", "You are not logged in");
            return "file_url_modal_error";
        }
        return "send_issue_modal";
    }

    @RequestMapping(value = {"messaging/send_issue_submit"})
    @ResponseBody
    public String send_issue_submit(HttpServletRequest request, HttpSession session) {
        HashMap<String, String> resultArray = new HashMap<>();
        Gson gson = new Gson();
        if (CommonModel.isLoggedIn(request, session) && request.getMethod().equals("POST") && request.getParameter("message") != null) {
            String message = request.getParameter("message").trim();
            int user_id = (int) session.getAttribute("user_id");
            MessagingModel.insertNewIssue(Integer.toString(user_id), message);
            resultArray.put("status", "success");
            resultArray.put("toastr", "Message Sent");
            return gson.toJson(resultArray);
        }
        resultArray.put("status", "failed");
        resultArray.put("swal_error", "Unable to send message. Internal Error.");
        return gson.toJson(resultArray);
    }
}