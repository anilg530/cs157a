package filehub.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class FileController {

    @RequestMapping("file")
    public String main(Model model) {
        System.out.println("hi");
        model.addAttribute("page_name", "Group Page 01");
        return "login_page";
    }
}