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

@Controller
public class FileController {

    @RequestMapping("file")
    public String main(Model model) {
        System.out.println("Timestamp: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime()));
        model.addAttribute("page_name", "File Test Page");
        return "file_page";
    }

    @RequestMapping(value = "file/view/{id}")
    public String file_browser(@PathVariable("id") int id, ModelMap model) {
        model.addAttribute("page_name", "File Browser");
        return "file_browser";
    }

    @RequestMapping(value = "file/test1")
    public String test1(HttpServletRequest request, ModelMap model) {
        request.getSession().setAttribute("cart","this is a session");
        model.addAttribute("page_name", "Test1");
        return "file_browser";
    }

    @RequestMapping(value = "file/test2")
    public String test2(HttpSession session, ModelMap model) {
        String tempString = (String) session.getAttribute("cart");
        System.out.println(tempString);
        model.addAttribute("page_name", "Test2");
        return "file_browser";
    }
}