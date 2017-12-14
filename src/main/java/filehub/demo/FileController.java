package filehub.demo;

import com.google.gson.Gson;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
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

    @RequestMapping(value = "file/view/{group_id}")
    public String file_browser(@PathVariable("group_id") int group_id, HttpServletRequest request, HttpSession session, Model model) {
        if (!CommonModel.isLoggedIn(request, session)) {
            return "not_logged_in";
        }
        int user_id = (int) session.getAttribute("user_id");
        model.addAttribute("page_name", "File Browser");
        boolean isInGroup = CommonModel.isInGroup(user_id, group_id);
        if (isInGroup) {
            // in group
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
            return "file_browser";
        } else {
            // not in group
            return "file_browser_error";
        }
    }

    @RequestMapping(value = {"file/refresh_files_table"})
    public String refresh_files_table(HttpServletRequest request, HttpSession session, Model model) {
        if (CommonModel.isLoggedIn(request, session)) {
            String current_path = (String) session.getAttribute("current_path");
            ArrayList<String> folder_directory = FileModel.getDirectory(current_path);
            ArrayList<ArrayList<String>> file_list = FileModel.getFileList(session);
            model.addAttribute("folder_directory", folder_directory);
            model.addAttribute("file_list", file_list);
            return "includes_files_table";
        } else {
            return null;
        }
    }

    @RequestMapping(value = "file/open_folder_ajax")
    public String open_folder_ajax(HttpServletRequest request, HttpSession session, Model model) {
        if (CommonModel.isLoggedIn(request, session) && request.getMethod().equals("POST") && request.getParameter("id") != null) {
            int user_id = (int) session.getAttribute("user_id");
            String id = request.getParameter("id").trim();
            String group_id_string = FileModel.getGroupIDByFileID(id);
            int group_id = Integer.parseInt(group_id_string);
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
            String id = request.getParameter("id").trim();
            String group_id_string = FileModel.getGroupIDByFileID(id);
            int group_id = Integer.parseInt(group_id_string);
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
            String id = request.getParameter("id").trim();
            String group_id_string = FileModel.getGroupIDByFileID(id);
            int group_id = Integer.parseInt(group_id_string);
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
            String id = request.getParameter("id").trim();
            String group_id_string = FileModel.getGroupIDByFileID(id);
            int group_id = Integer.parseInt(group_id_string);
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
            String id = request.getParameter("id").trim();
            String group_id_string = FileModel.getGroupIDByFileID(id);
            int group_id = Integer.parseInt(group_id_string);
            String file_name = request.getParameter("file_name").trim();
            if (FileModel.isAllowedRenameFile(user_id, group_id)) {
                if (FileModel.isFilenameTheSame(id, file_name)) {
                    resultArray.put("status", "success");
                    return gson.toJson(resultArray);
                } else if (FileModel.isFileAlreadyExistRename(session, id, file_name)) {
                    resultArray.put("status", "failed");
                    resultArray.put("error", "Another file already have the same name: <b>" + file_name + "</b>.");
                    return gson.toJson(resultArray);
                } else if (!CommonModel.isValidFileName(file_name)) {
                    resultArray.put("status", "failed");
                    resultArray.put("error", "Invalid file name (contains illegal characters): <b>" + file_name + "</b>.");
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
            String id = request.getParameter("id").trim();
            String group_id_string = FileModel.getGroupIDByFileID(id);
            int group_id = Integer.parseInt(group_id_string);
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

    @RequestMapping(value = "file/open/{id}")
    public void open(@PathVariable("id") int file_id, HttpServletRequest request, HttpSession session, HttpServletResponse response) throws IOException {
        if (CommonModel.isLoggedIn(request, session)) {
            int user_id = (int) session.getAttribute("user_id");
            String group_id_string = FileModel.getGroupIDByFileID(Integer.toString(file_id));
            int group_id = Integer.parseInt(group_id_string);
            boolean isInGroup = CommonModel.isInGroup(user_id, group_id);
            if (isInGroup) {
                if (isInGroup) {
                    String file_path = FileModel.getFilePathByFileID(Integer.toString(file_id));
                    String file_name = FileModel.getFileName(Integer.toString(file_id));
                    try {
                        response.setHeader("Content-Disposition", "filename=\"" + file_name + "\"");
                        InputStream is = new FileInputStream(file_path);
                        org.apache.commons.io.IOUtils.copy(is, response.getOutputStream());
                        response.flushBuffer();
                    } catch (IOException ex) {
                        throw new RuntimeException("IOError writing file to output stream");
                    }
                }
            } else {
                PrintWriter out = response.getWriter();
                out.write("Not In Group");
            }
        } else {
            PrintWriter out = response.getWriter();
            out.write("Not Logged In");
        }
    }

    @RequestMapping(value = "file/open_file/{file_url}")
    public void open_file(@PathVariable("file_url") String file_url, HttpServletRequest request, HttpSession session, HttpServletResponse response) throws IOException {
        file_url = file_url.trim();
        if (FileModel.isValidFileURL(file_url)) {
            int file_id = FileModel.getFileIDByFileURL(file_url);
            String file_path = FileModel.getFilePathByFileID(Integer.toString(file_id));
            String file_name = FileModel.getFileName(Integer.toString(file_id));
            try {
                response.setHeader("Content-Disposition", "filename=\"" + file_name + "\"");
                InputStream is = new FileInputStream(file_path);
                org.apache.commons.io.IOUtils.copy(is, response.getOutputStream());
                response.flushBuffer();
            } catch (IOException ex) {
                throw new RuntimeException("IOError writing file to output stream");
            }
        } else {
            PrintWriter out = response.getWriter();
            out.write("Invalid URL");
        }
    }

    @RequestMapping(value = {"file/group_share_file"})
    public String group_share_file(HttpServletRequest request, HttpSession session, Model model) {
        if (!CommonModel.isLoggedIn(request, session)) {
            model.addAttribute("error_message", "You are not logged in");
            return "file_url_modal_error";
        }
        if (request.getMethod().equals("POST") && request.getParameter("file_id") != null) {
            String file_id_string = request.getParameter("file_id");
            int file_id = Integer.parseInt(file_id_string);
            int user_id = (int) session.getAttribute("user_id");
            String group_id_string = FileModel.getGroupIDByFileID(Integer.toString(file_id));
            int group_id = Integer.parseInt(group_id_string);
            boolean isInGroup = CommonModel.isInGroup(user_id, group_id);
            boolean isAllowedShareFile = FileModel.isAllowedShareFile(user_id, group_id);
            if (isInGroup && isAllowedShareFile) {
                String file_name = FileModel.getFileName(file_id_string);
                String file_url = FileModel.getFileURLcode(file_id_string);
                model.addAttribute("file_id", file_id);
                model.addAttribute("file_name", file_name);
                model.addAttribute("file_url", file_url);
                return "file_url_modal";
            } else {
                model.addAttribute("error_message", "You do not have enough access privilege to generate URL for this file");
                return "file_url_modal_error";
            }
        }
        model.addAttribute("error_message", "Invalid Request. You may not have authorized access to this file or group.");
        return "file_url_modal_error";
    }

    @RequestMapping(value = {"file/remove_file_url"})
    @ResponseBody
    public String remove_file_url(HttpServletRequest request, HttpSession session, Model model) {
        HashMap<String, String> resultArray = new HashMap<>();
        Gson gson = new Gson();
        if (CommonModel.isLoggedIn(request, session) && request.getMethod().equals("POST") && request.getParameter("file_id") != null) {
            int user_id = (int) session.getAttribute("user_id");
            String file_id = request.getParameter("file_id").trim();
            String group_id_string = FileModel.getGroupIDByFileID(file_id);
            int group_id = Integer.parseInt(group_id_string);
            if (FileModel.isAllowedRemoveFileURL(user_id, group_id)) {
                FileModel.removeFileURL(file_id);
                resultArray.put("status", "success");
                resultArray.put("toastr", "File URL Removed");
                return gson.toJson(resultArray);
            }
        }
        resultArray.put("status", "failed");
        resultArray.put("error", "Unable to remove file URL. You may need higher access.");
        return gson.toJson(resultArray);
    }
}