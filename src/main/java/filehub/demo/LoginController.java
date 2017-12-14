package filehub.demo;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

@Controller
public class LoginController {

    @RequestMapping(value = {"login"})
    public String login(HttpServletRequest request, HttpSession session, Model model) {
        if (CommonModel.isLoggedIn(request, session)) {
            return "redirect:/";
        }
        if (request.getMethod().equals("POST")) {

            String username = request.getParameter("username").trim();
            String password = request.getParameter("password");
            boolean flag = UserDatabase.isValidUsernamePassword(username, password);

            if (flag) {
                String user_id = CommonModel.getUserIDByEmail(username);
                int user_id_int = Integer.parseInt(user_id);
                request.getSession().setAttribute("user_id", user_id_int);
                request.getSession().setAttribute("username", CommonModel.getEmailByUserID(Integer.toString(user_id_int)));
                return "redirect:/";
            } else {
                model.addAttribute("temp_username", username);
                model.addAttribute("error_message", "The password/username is incorrect.");
            }
        }
        return "login_page";
    }

    @RequestMapping(value = {"/"})
    public String homepage(HttpServletRequest request, HttpSession session, Model model) {
        model.addAttribute("page_name", "FileHub Login Page");
        return "welcome_page";
    }

    @RequestMapping(value = {"/logout"})
    public void logout(HttpServletResponse response, HttpSession session) {
        session.removeAttribute("user_id");
        session.removeAttribute("username");
        try {
            response.sendRedirect("/");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public String getForm(HttpServletRequest request, HttpServletResponse response) {
        return "create_account_page";
    }


    @RequestMapping(value = {"/sign-up"}, method = RequestMethod.POST)
    public String sign_up(HttpServletRequest request, HttpServletResponse response, Model model) {

        ArrayList<String> userSignInfo = new ArrayList<>();
        String firstname = request.getParameter("firstname").trim();
        String lastname = request.getParameter("lastname").trim();
        String username = request.getParameter("username").trim();
        String phone = request.getParameter("phone").trim();
        String password = request.getParameter("password");
        model.addAttribute("firstname", firstname);

        if (firstname == null || firstname.isEmpty() || lastname == null || lastname.isEmpty()
                || username == null || username.isEmpty() || phone == null || phone.isEmpty()
                || password == null || password.isEmpty()) {
            model.addAttribute("error_message", "You have invalid values in your form.");
            return "create_account_page";
        }

        userSignInfo.add(firstname);
        userSignInfo.add(lastname);
        userSignInfo.add(username);
        userSignInfo.add(phone);
        userSignInfo.add(password);

        if (!UserDatabase.userExist(username)) {
            int user_id = UserDatabase.insertUser1(userSignInfo);
            if (user_id != -1) {
                request.getSession().setAttribute("user_id", user_id);
                request.getSession().setAttribute("username", CommonModel.getEmailByUserID(Integer.toString(user_id)));
            }
            return "welcome_page";
        } else {
            model.addAttribute("error_message", "This user already exists.");
            return "create_account_page";
        }
    }


    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String getProfile(HttpServletRequest request, HttpServletResponse response,
                             HttpSession session, Model model) {
        if (!CommonModel.isLoggedIn(request, session)) {
            return "redirect:/";
        }
        int user_id = (int) session.getAttribute("user_id");
        ArrayList<String> userInfo = UserDatabase.getUser(user_id);

        model.addAttribute("username", userInfo.get(1));
        model.addAttribute("first_name", userInfo.get(4));
        model.addAttribute("last_name", userInfo.get(5));
        model.addAttribute("cellphone", userInfo.get(6));
        return "user_profile";
    }

    @RequestMapping(value = "/updateEmail", method = RequestMethod.POST)
    public String updateEmail(HttpServletRequest request, HttpServletResponse response, HttpSession session, Model model) {
        int user_id = (int) session.getAttribute("user_id");
        ArrayList<String> userInfo = UserDatabase.getUser(user_id);

        model.addAttribute("username", userInfo.get(1));
        model.addAttribute("first_name", userInfo.get(4));
        model.addAttribute("last_name", userInfo.get(5));
        model.addAttribute("cellphone", userInfo.get(6));

        String username = request.getParameter("usern").trim();
        String password = request.getParameter("password");

        if (!username.isEmpty() && !password.isEmpty()) {
            if (!UserDatabase.userExist(username)) {
                int user_idd = (int) session.getAttribute("user_id");
                UserDatabase.updateE(username, password, user_idd);
                if (user_id != -1) {
                    request.getSession().setAttribute("user_id", user_id);
                    request.getSession().setAttribute("username", CommonModel.getEmailByUserID(Integer.toString(user_id)));
                }
                this.getProfile(request, response, session, model);
            } else {
                model.addAttribute("error_message", "This username already exists.");
                return "user_profile";
            }
        }
        else {
            model.addAttribute("error_message", "You entered an invalid username or password");
            return "user_profile";
        }
        return "user_profile";
    }


}