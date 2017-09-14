package filehub.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("group")
public class GroupController {

    @RequestMapping
    public String main(Model model) {
        model.addAttribute("page_name", "Group Page 01");
        return "group_page";
    }

    @RequestMapping("/create_group")
    public String createGroup(Model model) {
        model.addAttribute("page_name", "Create Group");
        return "group_page";
    }

}