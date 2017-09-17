package filehub.demo;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@Scope("session")
@Controller
public class LoginController {

    @RequestMapping(value={"/", "login"})
    public String login(HttpServletRequest request, HttpSession session, Model model) {
        if (request.getMethod().equals("POST")) {
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            System.out.println(username);
            System.out.println(password);
            model.addAttribute("username", username);
            request.getSession().setAttribute("user_id",1);
            request.getSession().setAttribute("username",username);
        }
        model.addAttribute("page_name", "FileHub Login Page");
        return "login_page";
    }

}