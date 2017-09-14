package filehub.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("file")
public class FileController {

    @RequestMapping
    public String main(Model model) {
        System.out.println("hi");
        model.addAttribute("page_name", "Group Page 01");
        return "group_page";
    }
}