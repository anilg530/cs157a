package filehub.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @RequestMapping(value={"/", "login", " * "})
    public String login(Model model) {
        model.addAttribute("page_name", "Filehub Login Page");
        return "login_page";
    }

}