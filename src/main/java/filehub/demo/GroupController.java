package filehub.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.UUID;

@Controller
public class GroupController {

    @RequestMapping("group")
    public String main(HttpSession session, Model model) {
        System.out.println("hi there"+ UUID.randomUUID());
        model.addAttribute("page_name", "Group Page 01");
        System.out.println("group count "+ GroupModel.countGroup((int) session.getAttribute("user_id")));
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
            System.out.println("group name: " + groupname);
            System.out.println("group pass: " + groupPassword);
            GroupModel.insertGroup(groupname, userID, groupPassword);

        }
        return "group_page";
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
}