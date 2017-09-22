package filehub.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

@Controller
public class FileController {
    @RequestMapping("file")
    public String main(HttpServletRequest request, Model model) {
        request.getSession().setAttribute("user_id", 1);
        request.getSession().setAttribute("username", "bakatrinh@gmail.com");
        //System.out.println("Timestamp: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime()));
        model.addAttribute("page_name", "File Test Page");
        return "file_page";
    }

    @RequestMapping(value = "file/view/{id}")
    public String file_browser(@PathVariable("id") int group_id, HttpServletRequest request, HttpSession session, Model model) {
        if (request.getSession() == null || session.getAttribute("user_id") == null) {
            return "not_logged_in";
        }
        int user_id = (int) session.getAttribute("user_id");
        model.addAttribute("page_name", "File Browser");
        String folderKey = "/group_files/" + group_id;
        boolean isInGroup = FileModel.isInGroup(user_id, group_id);
        if (isInGroup) {
            request.getSession().setAttribute("folderKey", folderKey);
            request.getSession().setAttribute("group_id", group_id);
            ArrayList<String> folderList = FileModel.getDirectory(folderKey);
            model.addAttribute("folderList", folderList);
            model.addAttribute("folderKey", folderKey);
            model.addAttribute("group_id", group_id);
            return "file_browser";
        } else {
            return "file_browser_error";
        }
    }
}