package studi.immo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import studi.immo.entity.*;
import studi.immo.service.AdvertisementService;
import studi.immo.service.CashService;
import studi.immo.service.TenantService;
import studi.immo.service.UserService;

import java.math.BigDecimal;

@Controller
@RequestMapping("/")
public class HomeController {

    private UserService userService;
    private PasswordEncoder passwordEncoder;
    private AdvertisementService advertisementService;
    private CashService cashService;
    private TenantService tenantService;

    @Autowired
    public HomeController (UserService userService, PasswordEncoder passwordEncoder, AdvertisementService advertisementService, CashService cashService, TenantService tenantService){
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.advertisementService = advertisementService;
        this.cashService = cashService;
        this.tenantService = tenantService;
    }

    @GetMapping ({"","/","/accueil"})
    public String pageHome (Model model) {
        User currentUser = userService.getCurrentUser();
        model.addAttribute("User",currentUser);
        return "Index";
    }

    @GetMapping ({"/creation-compte"})
    public String pageCreateUser(Model model){
        User user = new User();
        model.addAttribute("NewUser", user);
        return "CreateUser";
    }

    @PostMapping (value = "/nouveau-compte")
    public String createUser(@ModelAttribute ("NewUser") User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.saveUser(user);
        User currentUser = user;
        Cash cash = new Cash();
        cash.setUser(user);
        cash.setAmount(BigDecimal.valueOf(0));
        user.setCash(cash);
        cashService.saveCash(cash);
        if (currentUser.getRoles().contains(Role.TENANT)){
            return "redirect:/user/creation-locataire";
        }
        if (currentUser.getRoles().contains(Role.AGENCY)){
            return "redirect:/user/creation-agence";
        }
        return "redirect:/user/create-address";
    }



    @GetMapping (value = "/liste-de-logement")
    public String listAccommodation (Model model){
        model.addAttribute("Advertisement", advertisementService.getAllAdvertisementAccommodation());
        return "ListAccommodation";
    }

    @GetMapping (value = "/logement/{id}")
    public String showAccommodation (@PathVariable Long id, Model model){
        Advertisement advertisementByUser = advertisementService.getAdvertisementAccommodationById(id);
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
