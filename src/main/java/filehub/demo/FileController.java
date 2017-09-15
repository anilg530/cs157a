package filehub.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.SimpleDateFormat;
import java.util.Calendar;

@Controller
public class FileController {

    @RequestMapping("file")
    public String main(Model model) {
        System.out.println("Timestamp: "+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime()));
        model.addAttribute("page_name", "Group Page 01");
        return "login_page";
    }
}