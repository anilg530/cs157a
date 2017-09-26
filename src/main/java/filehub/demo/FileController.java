package filehub.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@Controller
public class FileController {
    @RequestMapping("file")
    public String main(HttpServletRequest request, Model model) {
        request.getSession().setAttribute("user_id", 1);
        request.getSession().setAttribute("username", "bakatrinh@gmail.com");
        System.out.println("Timestamp: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime()));
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
        boolean isInGroup = FileModel.isInGroup(user_id, 1);
        if (isInGroup) {
            FileModel.getDirectory(Integer.toString(group_id));
            System.out.println("in group!");
            return "file_browser";
        } else {
            System.out.println("not in group!");
            return "file_browser_error";
        }
    }
}