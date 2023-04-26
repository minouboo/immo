package studi.immo.controller;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import studi.immo.entity.*;
import studi.immo.service.*;

import java.math.BigDecimal;
import java.util.List;

@Log
@Controller
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping(value="admin")
public class AdminController {

    private AdvertisementService advertisementService;
    private AccommodationService accommodationService;
    private AgreementService agreementService;
    private UserService userService;
    private CashService cashService;
    private PasswordEncoder passwordEncoder;
    private AgencyService agencyService;
    private TenantService tenantService;


    @Autowired
    public AdminController(AdvertisementService advertisementService, AccommodationService accommodationService, AgreementService agreementService, UserService userService, CashService cashService, PasswordEncoder passwordEncoder, AgencyService agencyService, TenantService tenantService) {
        this.advertisementService = advertisementService;
        this.accommodationService = accommodationService;
        this.agreementService = agreementService;
        this.userService = userService;
        this.cashService = cashService;
        this.passwordEncoder = passwordEncoder;
        this.agencyService = agencyService;
        this.tenantService = tenantService;
    }

    @GetMapping(value = "/liste-logements")
    public String allAdvertisement (Model model, String searchword){
        if (searchword != null)
        {
            List<Accommodation> searchAccommodation = accommodationService.searchAccommodation(searchword);
            model.addAttribute("AllAccommodation", searchAccommodation);
        }
        else
        {
            List<Accommodation> allAccommodation = accommodationService.getAllAccommodation();
            model.addAttribute("AllAccommodation", allAccommodation);
        }
        return "ListAllAccommodations";
    }

    @GetMapping (value = "/les-contrats/{id}")
    public String allAgreements (@PathVariable Long id, Model model){
        List<Agreement> allAgreements = agreementService.getAllAgreementByAccommodationById(id);
        List<Agreement> allAgreementsValidated = agreementService.getAllAgreementValidatedByAccommodationById(id);
        model.addAttribute("MyAgreements",allAgreements);
        model.addAttribute("MyAgreementsValidated", allAgreementsValidated);
        return "ListAgreements";
    }

    @GetMapping (value = "/tout-users")
    public String allUsers(Model model, String keyword){

        if (keyword != null)
        {
            List<User> allUsers = userService.searchUser(keyword);
            model.addAttribute("AllUsers", allUsers);
        }
        else
        {
            model.addAttribute("AllUsers", userService.getAllUser());
        }
        return "ListUsers";
    }

    @GetMapping (value = "/modifier-compte/{id}")
    public String modifyUserById(@PathVariable Long id, Model model){
        User userToModify = userService.getUserById(id);
        model.addAttribute("UserToModify",userToModify);
        model.addAttribute("IsTenant",userToModify.getRoles().contains(Role.TENANT));
        model.addAttribute("IsAgency",userToModify.getRoles().contains(Role.AGENCY));
        return "AdminModifyUser";
    }

    @PostMapping(value = "/compte-modifie/{id}")
    public String updateUserById(@PathVariable Long id, @ModelAttribute("UserToModify") User user){
        User userToModify = userService.getUserById(id);
        userToModify.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.saveUser(userToModify);
        return "redirect:/admin/tout-users/";
    }

    @GetMapping(value = "/modifier-portefeuille/{id}")
    public String userWallet (@PathVariable Long id, Model model){
        User userToModifyCash = userService.getUserById(id);
        Cash updateCash = cashService.getCashByUserID(userToModifyCash.getId());
        Cash newCash = new Cash();
        model.addAttribute("MyCash", updateCash);
        model.addAttribute("AddCash",newCash);
        return "MyWallet";
    }

    @PostMapping(value = "/reappro-portefeuille/{id}")
    public String addMoneyUserWallet (@PathVariable Long id, @ModelAttribute("AddCash") Cash cash){
        Cash updateCash = cashService.getCashById(id);
        BigDecimal oldAmount = updateCash.getAmount();
        updateCash.setAmount(cash.getAmount().add(oldAmount));
        cashService.saveCash(updateCash);
        return "redirect:/admin/tout-users/";
    }

    @GetMapping(value="/user/suppression/{id}")
    public String deleteAdvertisement (@PathVariable Long id){
        /*User targetUser = userService.getUserById(id);
        if (targetUser.getRoles().equals(Role.AGENCY)){
            agencyService.deleteAgencyById(agencyService.getAgencyByUserId(id).getId());
        }*/
        Tenant tenant = tenantService.getTenantByUserId(id);
        if (tenant == null)
        {
            userService.deleteUserById(id);
            return "redirect:/admin/tout-users";
        }
        tenantService.deleteTenantById(tenant.getId());
        userService.deleteUserById(id);
        return "redirect:/admin/tout-users";
    }

    @GetMapping ({"/creation-compte"})
    public String pageCreateUser(Model model){
        User user = new User();
        model.addAttribute("NewUser", user);
        return "AdminCreateUser";
    }

    @PostMapping (value = "/nouveau-compte")
    public String createUser(@ModelAttribute ("NewUser") User user){
        try {
            userService.saveUser(user);
        } catch (DataIntegrityViolationException e){
            return "redirect:/admin/tout-users";
        }

        User currentUser = user;
        Cash cash = new Cash();
        cash.setUser(user);
        cash.setAmount(BigDecimal.valueOf(0));
        user.setCash(cash);
        cashService.saveCash(cash);
        if (currentUser.getRoles().contains(Role.TENANT)){
            return "redirect:/admin/creation-locataire/"+user.getId();
        }
        if (currentUser.getRoles().contains(Role.AGENCY)){
            return "redirect:/admin/creation-agence/"+user.getId();
        }
        return "redirect:/admin/tout-users";
    }

    @GetMapping(value = "/creation-locataire/{id}")
    public String pageCreateTenant(@PathVariable Long id, Model model){
        User newUser = userService.getUserById(id);
        Tenant tenantUser = tenantService.getTenantByUserId(newUser.getId());
        if (tenantUser == null)
        {
            Tenant tenant = new Tenant();
            model.addAttribute("Tenant", tenant);
            model.addAttribute("NewUser", newUser);
            return "AdminCreateTenant";
        }
        model.addAttribute("Tenant", tenantUser);
        model.addAttribute("NewUser", newUser);
        return "AdminCreateTenant";
    }

    @PostMapping (value = "/nouveau-locataire/{id}")
    public String createTenant (@PathVariable Long id, @ModelAttribute ("Tenant") Tenant tenant){
        User tenantUser = userService.getUserById(id);
        Tenant currentTenant = tenantService.getTenantByUserId(tenantUser.getId());
        if (tenantUser == null){
            return "redirect:/admin/tout-users";
        }
        if (currentTenant == null)
        {
            tenant.setRevenues(tenant.getRevenues());
            tenant.setUser(tenantUser);
            tenantService.saveTenant(tenant);
            return "redirect:/admin/tout-users";
        }
        else
        {
            currentTenant.setRevenues(tenant.getRevenues());
            currentTenant.setUser(tenantUser);
            tenantService.saveTenant(currentTenant);
            return "redirect:/admin/tout-users";
        }
    }








}
