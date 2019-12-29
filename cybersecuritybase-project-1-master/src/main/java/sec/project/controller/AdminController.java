package sec.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import sec.project.repository.SignupRepository;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private SignupRepository signupRepository;

    // 2013-A7-Missing Function Level Access Control
    // This is an administrative controller that is not secured.
    // Link to reach administrative functions are shown to administrators only,
    // but the functions are still accessible by anybody who can guess their URL

    @RequestMapping
    public String index(Model model) {

        model.addAttribute("items", signupRepository.findAll());

        return "admin/index";
    }

}
