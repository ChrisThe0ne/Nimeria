package hu.uni.ekcu.Nimeria.template;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class TemplateController {

    @GetMapping("creators")
    public String getCreators() {
        return "creators";
    }

    @GetMapping("registration")
    public String getRegistration() {
        return "registration";
    }
}
