package studi.immo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import studi.immo.entity.User;
import studi.immo.service.UserService;

@Controller
@RequestMapping("/")
public class HomeController {

    private UserService userService;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public HomeController (UserService userService, PasswordEncoder passwordEncoder){
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping ({"","/","/accueil"})
    public String pageHome (){
        return "Index";
    }

    @GetMapping ({"/creation-compte"})
    public String pageCreationDeCompte(Model model){
        User user = new User();
        model.addAttribute("User", user);
        return "CreateUser";
    }

    @PostMapping (value = "/nouveau-compte")
    public String createUser(@ModelAttribute ("User") User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.saveUser(user);
        return "Index";
    }

}
