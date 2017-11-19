package filehub.demo;

import com.google.gson.Gson;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

@Controller
public class GroupController {

    @RequestMapping("group")
    public String main(HttpSession session, Model model) {
        //System.out.println("hi there"+ UUID.randomUUID());
        model.addAttribute("page_name", "Group Page 01");
        //System.out.println("group count "+ GroupModel.countGroup((int) session.getAttribute("user_id")));
        return "group_page";
    }
    @RequestMapping("group/create_group")
    public String createGroup(Model model) {
        model.addAttribute("page_name", "Create Group");
        return "group_create";
    }

    @RequestMapping("group/create_group/add")
    public String addGroup(HttpServletRequest request, HttpSession session, Model model) {
        model.addAttribute("page_name", "Create Group");
        String groupname = request.getParameter("group_name");
        String groupPassword = request.getParameter("group_password");
        if(request.getMethod().equals("POST")){
            int userID = (int) session.getAttribute("user_id");
            //System.out.println("group name: " + groupname);
            //System.out.println("group pass: " + groupPassword);
            GroupModel.insertGroup(groupname, userID, groupPassword);

        }
        return "redirect:/group";
    }

    @RequestMapping("group/all")
    public String listGroups(HttpServletRequest request, HttpSession session, Model model) {
        ArrayList<Groups> allGroup = GroupModel.getAllGroup();
        for (Groups e : allGroup) {
            System.out.println(e);
        }
        System.out.println("group count "+ GroupModel.countGroup((int) session.getAttribute("user_id")));
        model.addAttribute("page_name", "View all group");

        return "group_page";
    }

    @RequestMapping(value = "/group/confirmpass/{groupName}/{input}", method = RequestMethod.GET)
    @ResponseBody
    public boolean confirmGroupPass(@PathVariable("groupName") String groupName, @PathVariable("input") String input, HttpServletRequest request, HttpSession session, HttpServletResponse response) throws IOException {
       return GroupModel.isGroupPassCorrect(groupName, input);
    }

    @RequestMapping(value = {"/group/delete_group"}, method = RequestMethod.POST)
    @ResponseBody
    public String delete_group(HttpServletRequest request, HttpSession session) {
        HashMap<String, String> resultArray = new HashMap<>();
        Gson gson = new Gson();
        if (CommonModel.isLoggedIn(request, session) && request.getMethod().equals("POST") && request.getParameter("groupId") != null && request.getParameter("groupOwner") !=null) {
            int groupOwner = Integer.valueOf(request.getParameter("groupOwner").trim());
            int groupId = Integer.valueOf(request.getParameter("groupId").trim());

            System.out.println("group owner = "+ groupOwner);
            System.out.println("group id = "+ groupId);

            if(GroupModel.deleteGroup(groupOwner, groupId)){
                resultArray.put("status", "success");
                resultArray.put("title", "Success");
                resultArray.put("content", "Deleted Successfully!");
            }else{
                resultArray.put("status", "failed");
                resultArray.put("title", "Failed");
                resultArray.put("content", "Deleted failed!");
            }
        }else {
            resultArray.put("status", "failed");
            resultArray.put("title", "Failed");
            resultArray.put("content", "Deleted failed!");
        }

        System.out.println(gson.toJson(resultArray).toString());
        return gson.toJson(resultArray);
    }


    @RequestMapping(value = {"/group/refresh_group_table"})
    public String refresh_group_table(HttpServletRequest request, HttpSession session, Model model) {

        if (CommonModel.isLoggedIn(request, session)) {
            ArrayList<Groups> groups = GroupModel.getAllGroups((int) session.getAttribute("user_id"));
            model.addAttribute("user_id", (int) session.getAttribute("user_id"));
            model.addAttribute("groups", groups);
            System.out.println("/group/refresh_group_table");
            for(Groups g: groups){
                System.out.println("name " + g.getGroup_name());
            }
            return "includes_group_table";
        } else {
            return null;
        }
    }
}