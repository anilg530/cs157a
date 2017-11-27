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

    @RequestMapping(value={"/", "login"})
    public String login(HttpServletRequest request, HttpSession session, Model model) {
        if (request.getMethod().equals("POST")) {
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            System.out.println(username);
            System.out.println(password);
            model.addAttribute("username", username);
            // the user_id should be retrieved from DB based on the correct
            // username provided and set it below
            request.getSession().setAttribute("user_id",1);
            request.getSession().setAttribute("username",username);

        }
        model.addAttribute("page_name", "FileHub Login Page");
        return "welcome_page";

    }



    @RequestMapping(value={"/logout"})
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


    @RequestMapping(value={"/sign-up"}, method = RequestMethod.POST)
    public String sign_up(HttpServletRequest request, HttpServletResponse response, Model model) {

        //if (request.getMethod().equals("POST")) {
            ArrayList<String> userSignInfo = new ArrayList<>();
            String firstname = request.getParameter("firstname");
            String lastname = request.getParameter("lastname");
            String username = request.getParameter("username");
            String phone = request.getParameter("phone");
            String password = request.getParameter("password");
            model.addAttribute("firstname", firstname);


            userSignInfo.add(firstname);
            userSignInfo.add(lastname);
            userSignInfo.add(username);
            userSignInfo.add(phone);
            userSignInfo.add(password);


           if (!UserDatabase.userExist(username)) {
               UserDatabase.insertUser1(userSignInfo);
           }
           else
           {
               System.out.println("username exists");
           }

        return "welcome_page";
    }


    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String getProfile(HttpServletRequest request, HttpServletResponse response, HttpSession session){

        UserDatabase.getUser(session);

// return that array

        // add each thing to the model model.addObject(" ", )



            return "user_profile";

    }

}