package filehub.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
<<<<<<< HEAD
=======

import java.text.SimpleDateFormat;
>>>>>>> parent of 58b89ae... session, navigation
import java.util.ArrayList;
import java.util.UUID;

@Scope("session")
@Controller
public class GroupController {

    @RequestMapping("group")
    public String main(Model model) {
<<<<<<< HEAD
        System.out.println("hi there"+ UUID.randomUUID());
=======
        System.out.println("Timestamp: "+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime()));
>>>>>>> parent of 58b89ae... session, navigation
        model.addAttribute("page_name", "Group Page 01");
        return "group_page";
    }

<<<<<<< HEAD
=======
    @RequestMapping("group/create_group")
>>>>>>> parent of 58b89ae... session, navigation
    public String createGroup(Model model) {
        model.addAttribute("page_name", "Create Group");
        return "group_page";
    }

    @RequestMapping("group/add")
    public String createGroupTest(Model model) {
        model.addAttribute("page_name", "Create Group Test");
<<<<<<< HEAD
=======
        GroupModel.insertGroupTest();
>>>>>>> parent of 58b89ae... session, navigation
        return "group_page";
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