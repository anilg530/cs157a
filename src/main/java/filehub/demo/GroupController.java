package filehub.demo;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

@Controller
public class GroupController {

    @RequestMapping("group")
    public String main(HttpServletRequest request, HttpSession session, Model model) {
        // temporary setting the user_id to 2. This makes you log in as jennifer on the DB
        request.getSession().setAttribute("user_id",2);
        request.getSession().setAttribute("username","jennifer");
        System.out.println("Timestamp: "+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime()));
        model.addAttribute("page_name", "Group Page 01");
        return "group_page";
    }

    @RequestMapping("group/create_group")
    public String createGroup(HttpServletRequest request, HttpSession session, Model model) {
        if (request.getMethod().equals("POST")) {
            String group_name = request.getParameter("group_name");
            String group_password = request.getParameter("group_password");
            System.out.println(group_name);
            System.out.println(group_password);
            model.addAttribute("group_name", group_name);
            // code to check if group is not already on DB goes here.
            // if group doesn't already exist, add it to DB
        }
        model.addAttribute("page_name", "Create Group");
        return "group_create";
    }

    @RequestMapping("group/all")
    public String listGroups(Model model) {
        ArrayList<Groups> allGroup = GroupModel.getAllGroup();
        for (Groups e : allGroup) {
            System.out.println(e);
        }
        model.addAttribute("page_name", "View all group");

        return "group_page";
    }
}