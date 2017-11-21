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

@Controller
public class AdminController {

    @RequestMapping("admin/view_reports")
    public String view_reports(HttpServletRequest request, HttpSession session, Model model) {
        if (CommonModel.isLoggedIn(request, session)) {
            int user_id = (int) session.getAttribute("user_id");
            if (CommonModel.isMaster(Integer.toString(user_id))) {
                model.addAttribute("page_name", "User Issues Report");
                ArrayList<ArrayList<String>> getUserIssues = AdminModel.getUserIssues();
                model.addAttribute("getUserIssues", getUserIssues);
                return "user_issues_browser";
            }
        }
        return "not_logged_in";
    }

    @RequestMapping("admin/view_file_log")
    public String view_file_log(HttpServletRequest request, HttpSession session, Model model) {
        if (CommonModel.isLoggedIn(request, session)) {
            int user_id = (int) session.getAttribute("user_id");
            if (CommonModel.isMaster(Integer.toString(user_id))) {
                ArrayList<ArrayList<String>> getFileUploadLog = AdminModel.getFileUploadLog();
                model.addAttribute("getFileUploadLog", getFileUploadLog);
                model.addAttribute("page_name", "File Browser Log");
                return "file_log_browser";
            }
        }
        return "not_logged_in";
    }
}