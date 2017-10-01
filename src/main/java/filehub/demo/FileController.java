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
        if (!CommonModel.isLoggedIn(request, session)) {
            return "not_logged_in";
        }
        int user_id = (int) session.getAttribute("user_id");
        model.addAttribute("page_name", "File Browser");
        boolean isInGroup = CommonModel.isInGroup(user_id, group_id);
        if (isInGroup) {
            String current_path = "group_files/" + Integer.toString(group_id);
            request.getSession().setAttribute("group_id", group_id);
            request.getSession().setAttribute("root_dir", current_path);
            ArrayList<String> folder_directory = FileModel.getDirectory(current_path);
            request.getSession().setAttribute("current_path", current_path);
            model.addAttribute("folder_directory", folder_directory);
            String group_name = CommonModel.getGroupName(Integer.toString(group_id));
            model.addAttribute("group_name", group_name);
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

    @RequestMapping(value = {"file/exit_new_folder_html_ajax"})
    public String exit_new_folder_html_ajax(HttpSession session, Model model) {
        return "includes_files_table_header";
    }

    @RequestMapping(value = {"file/add_new_folder_html_ajax"})
    public String add_new_folder_html_ajax(HttpSession session, Model model) {
        return "includes_files_table_header_edit";
    }

    @RequestMapping(value = {"file/add_new_folder_submit_ajax"})
    @ResponseBody
    public String add_new_folder_submit_ajax(HttpServletRequest request, HttpSession session, Model model) {
        HashMap<String, String> resultArray = new HashMap<>();
        Gson gson = new Gson();
        if (CommonModel.isLoggedIn(request, session) && request.getMethod().equals("POST") && request.getParameter("folder_name") != null) {
            int user_id = (int) session.getAttribute("user_id");
            int group_id = (int) session.getAttribute("group_id");
            String folder_name = request.getParameter("folder_name").trim();
            if (!CommonModel.isLettersNumbersUnderscoreOnlyString(folder_name)) {
                resultArray.put("status", "failed");
                resultArray.put("error", "The specified folder: <b>" + folder_name + "</b> can only contain letters, numbers, and underscores (no space).");
                return gson.toJson(resultArray);
            }
            else if (FileModel.isFolderAlreadyExist(session, folder_name)) {
                resultArray.put("status", "failed");
                resultArray.put("error", "The specified folder: <b>" + folder_name + "</b> already exist.");
                return gson.toJson(resultArray);
            } else if (FileModel.isAllowedAddNewFolder(user_id, group_id)) {
                boolean newFolderCheck = FileModel.createNewFolder(session, folder_name);
                if (newFolderCheck) {
                    resultArray.put("status", "success");
                    resultArray.put("toastr", "New Folder Created");
                    return gson.toJson(resultArray);
                }
            }
        }
        resultArray.put("status", "failed");
        resultArray.put("error", "Unable to create folder. You may need higher access.");
        return gson.toJson(resultArray);
    }

    @RequestMapping(value = {"file/delete_folder_submit_ajax"})
    @ResponseBody
    public String delete_folder_submit_ajax(HttpServletRequest request, HttpSession session, Model model) {
        HashMap<String, String> resultArray = new HashMap<>();
        Gson gson = new Gson();
        if (CommonModel.isLoggedIn(request, session) && request.getMethod().equals("POST") && request.getParameter("id") != null) {
            int user_id = (int) session.getAttribute("user_id");
            int group_id = (int) session.getAttribute("group_id");
            String id = request.getParameter("id").trim();
            if (FileModel.isAllowedDeleteFolder(user_id, group_id)) {
                boolean folderDeletedCheck = FileModel.deleteFolder(session, id);
                if (folderDeletedCheck) {
                    resultArray.put("status", "success");
                    resultArray.put("toastr", "Folder Deleted");
                    return gson.toJson(resultArray);
                }
            }
        }
        resultArray.put("status", "failed");
        resultArray.put("error", "Unable to delete folder. You may need higher access.");
        return gson.toJson(resultArray);
    }
}