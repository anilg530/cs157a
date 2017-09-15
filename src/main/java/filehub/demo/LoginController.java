package filehub.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.SimpleDateFormat;
import java.util.Calendar;

@Controller
public class LoginController {

    @RequestMapping(value={"/", "login"})
    public String login(Model model) {
        System.out.println("Timestamp: "+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime()));
        model.addAttribute("page_name", "FileHub Login Page");
        return "login_page";
    }

}