package studi.immo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import studi.immo.entity.Advertisement;
import studi.immo.entity.Cash;
import studi.immo.entity.User;
import studi.immo.service.AdvertisementService;
import studi.immo.service.CashService;
import studi.immo.service.UserService;

import java.math.BigDecimal;

@Controller
@RequestMapping("/")
public class HomeController {

    private UserService userService;
    private PasswordEncoder passwordEncoder;
    private AdvertisementService advertisementService;
    private CashService cashService;

    @Autowired
    public HomeController (UserService userService, PasswordEncoder passwordEncoder, AdvertisementService advertisementService, CashService cashService){
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.advertisementService = advertisementService;
        this.cashService = cashService;
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
        Cash cash = new Cash();
        cash.setUser(user);
        cash.setAmount(BigDecimal.valueOf(0));
        cashService.saveCash(cash);
        return "Index";
    }

    @GetMapping (value = "/liste-de-logement")
    public String listAccommodation (Model model){

        model.addAttribute("Advertisement", advertisementService.getAllAdvertisementAccommodation());
        return "ListAccommodation";
    }

    @GetMapping (value = "/logement/{id}")
    public String showAccommodation (@PathVariable Long id, Model model){
        Advertisement advertisementByUser = advertisementService.getAdvertisementAccommodationId(id);
        User targetUser = userService.getCurrentUser();
        boolean IsOwner = false;
        if (targetUser !=null){
            IsOwner = targetUser.getId().equals(advertisementByUser.getAccommodation().getUser().getId());
        }
        boolean NotOwner = false;
        if (targetUser!=null){
            NotOwner =! targetUser.getId().equals(advertisementByUser.getAccommodation().getUser().getId());
        }
        model.addAttribute("IsOwner", IsOwner);
        model.addAttribute("NotOwner", NotOwner);
        model.addAttribute("Advertisement", advertisementByUser);
        return "ShowAccommodation";
    }



}
