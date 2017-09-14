package filehub.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {

    @RequestMapping(value={"/", "login"})
    public String login(Model model) {
        model.addAttribute("page_name", "FileHub Login Page");
        return "login_page";
    }

}