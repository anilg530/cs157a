package filehub.demo;

import com.google.gson.Gson;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;

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
        boolean isInGroup = FileModel.isInGroup(user_id, group_id);
        if (isInGroup) {
            String current_path = "group_files/" + Integer.toString(group_id);
            request.getSession().setAttribute("group_id", group_id);
            request.getSession().setAttribute("root_dir", current_path);
            ArrayList<String> folder_directory = FileModel.getDirectory(current_path);
            request.getSession().setAttribute("current_path", current_path);
            model.addAttribute("folder_directory", folder_directory);
            // in group
            return "file_browser";
        } else {
            // not in group
            return "file_browser_error";
        }
    }

    @RequestMapping(value = {"file/ajax_test"})
    @ResponseBody
    public String ajax_test(HttpServletRequest request, HttpSession session, Model model) {
        if (request.getMethod().equals("POST")) {
            String test = request.getParameter("bob");
            System.out.println(test);
        }
        HashMap<String, String> resultArray = new HashMap<>();
        resultArray.put("status", "success");
        resultArray.put("hello", "there");
        resultArray.put("toastr", "this is a toast message");
        model.addAttribute("extra_attribute", "this is an extra attribute");
        Gson gson = new Gson();
        System.out.println(gson.toJson(resultArray));
        return gson.toJson(resultArray);
    }

    @RequestMapping(value = {"file/refresh_files_table"})
    public String refresh_files_table(HttpSession session, Model model) {
        String current_path = (String) session.getAttribute("current_path");
        ArrayList<String> folder_directory = FileModel.getDirectory(current_path);
        model.addAttribute("folder_directory", folder_directory);
        return "includes_files_table";
    }
}