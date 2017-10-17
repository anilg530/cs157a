package filehub.demo;

import com.google.gson.Gson;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.*;

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
            request.getSession().setAttribute("current_path", current_path);
            ArrayList<String> folder_directory = FileModel.getDirectory(current_path);
            ArrayList<ArrayList<String>> file_list = FileModel.getFileList(session);
            model.addAttribute("folder_directory", folder_directory);
            model.addAttribute("file_list", file_list);
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

    @RequestMapping(value = {"file/test"})
    public String test(HttpServletRequest request, HttpSession session, Model model) {
        System.out.println(CommonModel.generateRandomCode());
        return "";
    }

    @RequestMapping(value = {"file/refresh_files_table"})
    public String refresh_files_table(HttpSession session, Model model) {
        String current_path = (String) session.getAttribute("current_path");
        ArrayList<String> folder_directory = FileModel.getDirectory(current_path);
        ArrayList<ArrayList<String>> file_list = FileModel.getFileList(session);
        model.addAttribute("folder_directory", folder_directory);
        model.addAttribute("file_list", file_list);
        return "includes_files_table";
    }

    @RequestMapping(value = "file/open_folder_ajax")
    public String open_folder_ajax(HttpServletRequest request, HttpSession session, Model model) {
        if (CommonModel.isLoggedIn(request, session) && request.getMethod().equals("POST") && request.getParameter("id") != null) {
            int user_id = (int) session.getAttribute("user_id");
            int group_id = (int) session.getAttribute("group_id");
            String id = request.getParameter("id").trim();
            boolean isInGroup = CommonModel.isInGroup(user_id, group_id);
            if (isInGroup) {
                String current_path = FileModel.getFilePathByID(id);
                request.getSession().setAttribute("current_path", current_path);
                ArrayList<String> folder_directory = FileModel.getDirectory(current_path);
                ArrayList<ArrayList<String>> file_list = FileModel.getFileList(session);
                model.addAttribute("folder_directory", folder_directory);
                model.addAttribute("file_list", file_list);
                return "includes_files_table";
            }
        }
        return null;
    }

    @RequestMapping(value = "file/previous_folder_ajax")
    public String previous_folder_ajax(HttpServletRequest request, HttpSession session, Model model) {
        if (CommonModel.isLoggedIn(request, session)) {
            int user_id = (int) session.getAttribute("user_id");
            int group_id = (int) session.getAttribute("group_id");
            boolean isInGroup = CommonModel.isInGroup(user_id, group_id);
            if (isInGroup) {
                String current_path = (String) session.getAttribute("current_path");
                if (!FileModel.isInRootDIR(session, current_path)) {
                    String previous_path = FileModel.getPreviousFolderPath(current_path);
                    request.getSession().setAttribute("current_path", previous_path);
                    ArrayList<String> folder_directory = FileModel.getDirectory(previous_path);
                    ArrayList<ArrayList<String>> file_list = FileModel.getFileList(session);
                    model.addAttribute("folder_directory", folder_directory);
                    model.addAttribute("file_list", file_list);
                    return "includes_files_table";
                }
            }
        }
        return null;
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
            if (!CommonModel.isLettersNumbersUnderscoreSpaceOnlyString(folder_name)) {
                resultArray.put("status", "failed");
                resultArray.put("error", "The specified folder: <b>" + folder_name + "</b> can only contain letters, numbers, and underscores (no space).");
                return gson.toJson(resultArray);
            } else if (FileModel.isFolderAlreadyExist(session, folder_name)) {
                resultArray.put("status", "failed");
                resultArray.put("error", "The specified folder: <b>" + folder_name + "</b> already exist.");
                return gson.toJson(resultArray);
            } else if (FileModel.isAllowedAddNewFolder(user_id, group_id)) {
                if (FileModel.createNewFolder(session, folder_name)) {
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
                if (FileModel.deleteFolder(session, id)) {
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

    @RequestMapping(value = {"file/rename_folder_submit_ajax"})
    @ResponseBody
    public String rename_folder_submit_ajax(HttpServletRequest request, HttpSession session, Model model) {
        HashMap<String, String> resultArray = new HashMap<>();
        Gson gson = new Gson();
        if (CommonModel.isLoggedIn(request, session) && request.getMethod().equals("POST") && request.getParameter("id") != null
                && request.getParameter("folder_name") != null) {
            int user_id = (int) session.getAttribute("user_id");
            int group_id = (int) session.getAttribute("group_id");
            String id = request.getParameter("id").trim();
            String folder_name = request.getParameter("folder_name").trim();
            if (folder_name == null || folder_name == "") {
                resultArray.put("status", "failed");
                resultArray.put("error", "Folder name cannot be blank.");
                return gson.toJson(resultArray);
            }
            if (!CommonModel.isLettersNumbersUnderscoreSpaceOnlyString(folder_name)) {
                resultArray.put("status", "failed");
                resultArray.put("error", "The specified folder: <b>" + folder_name + "</b> can only contain letters, numbers, and underscores (no space).");
                return gson.toJson(resultArray);
            }
            if (FileModel.isAllowedRenameFolder(user_id, group_id)) {
                if (FileModel.isFolderNameTheSame(id, folder_name)) {
                    resultArray.put("status", "success");
                    return gson.toJson(resultArray);
                } else if (FileModel.isFolderAlreadyExist(session, folder_name)) {
                    resultArray.put("status", "fail");
                    resultArray.put("error", "The folder name: <b>" + folder_name + "</b> is already taken.");
                    return gson.toJson(resultArray);
                } else {
                    if (FileModel.renameFolder(session, id, folder_name)) {
                        resultArray.put("status", "success");
                        resultArray.put("toastr", "Folder Renamed Successfully");
                        return gson.toJson(resultArray);
                    }
                }
            }
        }
        resultArray.put("status", "failed");
        resultArray.put("swal_error", "Unable to rename folder. You may need higher access.");
        return gson.toJson(resultArray);
    }

    @RequestMapping(value = {"file/delete_file_submit_ajax"})
    @ResponseBody
    public String delete_file_submit_ajax(HttpServletRequest request, HttpSession session, Model model) {
        HashMap<String, String> resultArray = new HashMap<>();
        Gson gson = new Gson();
        if (CommonModel.isLoggedIn(request, session) && request.getMethod().equals("POST") && request.getParameter("id") != null) {
            int user_id = (int) session.getAttribute("user_id");
            int group_id = (int) session.getAttribute("group_id");
            String id = request.getParameter("id").trim();
            if (FileModel.isAllowedDeleteFile(user_id, group_id)) {
                if (FileModel.deleteFile(session, id)) {
                    resultArray.put("status", "success");
                    resultArray.put("toastr", "File Deleted");
                    return gson.toJson(resultArray);
                }
            }
        }
        resultArray.put("status", "failed");
        resultArray.put("error", "Unable to delete folder. You may need higher access.");
        return gson.toJson(resultArray);
    }

    @RequestMapping(value = {"file/rename_file_submit_ajax"})
    @ResponseBody
    public String rename_file_submit_ajax(HttpServletRequest request, HttpSession session, Model model) {
        HashMap<String, String> resultArray = new HashMap<>();
        Gson gson = new Gson();
        if (CommonModel.isLoggedIn(request, session) && request.getMethod().equals("POST") && request.getParameter("file_name") != null && request.getParameter("id") != null) {
            int user_id = (int) session.getAttribute("user_id");
            int group_id = (int) session.getAttribute("group_id");
            String file_name = request.getParameter("file_name").trim();
            String id = request.getParameter("id").trim();
            if (FileModel.isAllowedRenameFile(user_id, group_id)) {
                if (FileModel.isFilenameTheSame(id, file_name)) {
                    resultArray.put("status", "success");
                    return gson.toJson(resultArray);
                } else if (FileModel.isFileAlreadyExistRename(session, id, file_name)) {
                    resultArray.put("status", "failed");
                    resultArray.put("error", "Another file already have the same name: <b>" + file_name + "</b>.");
                    return gson.toJson(resultArray);
                } else {
                    FileModel.renameFile(session, id, file_name);
                    resultArray.put("status", "success");
                    resultArray.put("toastr", "File Renamed Successfully");
                    return gson.toJson(resultArray);
                }
            }
        }
        resultArray.put("status", "failed");
        resultArray.put("swal_error", "Unable to rename file. You may need higher access.");
        return gson.toJson(resultArray);
    }

    @RequestMapping(value = {"file/edit_notes_submit_ajax"})
    @ResponseBody
    public String edit_notes_submit_ajax(HttpServletRequest request, HttpSession session, Model model) {
        HashMap<String, String> resultArray = new HashMap<>();
        Gson gson = new Gson();
        if (CommonModel.isLoggedIn(request, session) && request.getMethod().equals("POST") && request.getParameter("id") != null
                && request.getParameter("notes") != null) {
            int user_id = (int) session.getAttribute("user_id");
            int group_id = (int) session.getAttribute("group_id");
            String id = request.getParameter("id").trim();
            String notes = request.getParameter("notes").trim();
            if (FileModel.isAllowedEditNotes(user_id, group_id)) {
                if (FileModel.editNotes(session, id, notes)) {
                    resultArray.put("status", "success");
                    resultArray.put("toastr", "Notes Updated Successfully");
                    return gson.toJson(resultArray);
                }
            }
        }
        resultArray.put("status", "failed");
        resultArray.put("swal_error", "Unable to edit notes. You may need higher access.");
        return gson.toJson(resultArray);
    }

    @RequestMapping(value = {"file/file_exist_check"})
    @ResponseBody
    public String file_exist_check(HttpServletRequest request, HttpSession session, Model model) {
        HashMap<String, String> resultArray = new HashMap<>();
        Gson gson = new Gson();
        if (CommonModel.isLoggedIn(request, session) && request.getMethod().equals("POST") && request.getParameter("file_name") != null) {
            int user_id = (int) session.getAttribute("user_id");
            int group_id = (int) session.getAttribute("group_id");
            String file_name = request.getParameter("file_name").trim();
            if (FileModel.isAllowedUploadFiles(user_id, group_id)) {
                if (FileModel.isFileAlreadyExist(session, file_name)) {
                    resultArray.put("status", "failed");
                    resultArray.put("file_exist", "true");
                    resultArray.put("swal_error", "The selected file <b>" + file_name + "</b> already exist.<br>Replace it?");
                    resultArray.put("error", "File (" + file_name + ") already exist.");
                    return gson.toJson(resultArray);
                } else {
                    resultArray.put("status", "success");
                    return gson.toJson(resultArray);
                }
            }
        }
        resultArray.put("status", "failed");
        resultArray.put("error", "Unable to upload files. You may need higher access.");
        return gson.toJson(resultArray);
    }

    @RequestMapping(value = {"file/group_files_upload"})
    @ResponseBody
    public String group_files_upload(@RequestParam("fileToUpload") MultipartFile file, HttpServletRequest request, HttpSession session) {
        HashMap<String, String> resultArray = new HashMap<>();
        Gson gson = new Gson();
        if (CommonModel.isLoggedIn(request, session) && !file.getOriginalFilename().isEmpty()) {
            int user_id = (int) session.getAttribute("user_id");
            int group_id = (int) session.getAttribute("group_id");
            if (FileModel.isAllowedUploadFiles(user_id, group_id) && FileModel.addNewFile(session, file)) {
                resultArray.put("status", "success");
            }
        } else {
            resultArray.put("status", "failed");
            resultArray.put("error", "Unable to upload file");
        }
        return gson.toJson(resultArray);
    }
}