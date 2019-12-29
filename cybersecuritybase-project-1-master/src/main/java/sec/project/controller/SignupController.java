package sec.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sec.project.domain.Signup;
import sec.project.repository.SignupRepository;

import javax.annotation.PostConstruct;
import java.security.Principal;
import java.util.List;

@Controller
public class SignupController {

    @Autowired
    private SignupRepository signupRepository;

    @PostConstruct
    public void init() {
        signupRepository.save(new Signup("Sign up by Ted", "Ted's address", "ted"));
        signupRepository.save(new Signup("Sign up by Admin", "Admin's address", "admin"));
        signupRepository.save(new Signup("Sign up by Happy Hacker", "Hacker's Place", "hacker"));
    }

    @RequestMapping("/")
    public String defaultMapping() {
        return "redirect:/form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public String loadForm() {
        return "form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    public String submitForm(@RequestParam String name, @RequestParam String address, Model model, Principal principal) {

        Integer created = signupRepository.insecureSave(new Signup(name, address, principal.getName()));

        // brutal way to retrieve last created object
        // won't work in a real concurrent environment
        Signup signup = signupRepository.findAll(new PageRequest(0, 1,
                Sort.Direction.DESC, "id")).getContent().get(0);

        model.addAttribute("signup", signup);
        model.addAttribute("created", created);

        return "done";
    }

    @RequestMapping(value = "/signups", method = RequestMethod.GET)
    public String list(Model model, Principal principal, @RequestParam(required = false) String search) {

        List<Signup> items;
        if (search != null) {

            items = signupRepository.insecureFind(principal.getName(), search);
        } else {

            items = signupRepository.findByUser(principal.getName());
        }

        model.addAttribute("items", items);
        model.addAttribute("search", search);

        return "signups";
    }

    @RequestMapping(value = "/signups/{id}", method = RequestMethod.GET)
    public String read(Model model, @PathVariable Long id) {

        // 2013-A4-Insecure Direct Object References
        // Any authenticated user can access this page using any ID and reading
        // contents that doesn't belong to them

        model.addAttribute("item", signupRepository.insecureFindOne(id));

        return "signup";
    }

    @ResponseBody
    @RequestMapping(value = "/api/signups", method = RequestMethod.GET)
    public List<Signup> apiList() {

        return signupRepository.findAll();
    }

}
