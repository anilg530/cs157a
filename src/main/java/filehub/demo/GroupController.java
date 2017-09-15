package filehub.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.ArrayList;
import java.util.UUID;

@Controller
public class GroupController {

    @RequestMapping("group")
    public String main(Model model) {
        System.out.println("hi there"+ UUID.randomUUID());
        model.addAttribute("page_name", "Group Page 01");
        return "group_page";
    }

    @RequestMapping("group/create_group")
    public String createGroup(Model model) {
        model.addAttribute("page_name", "Create Group");
        return "group_page";
    }

    @RequestMapping("group/add")
    public String createGroupTest(Model model) {
        model.addAttribute("page_name", "Create Group Test");
        GroupModel.insertGroupTest();
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