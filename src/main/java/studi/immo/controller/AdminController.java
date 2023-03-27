package studi.immo.controller;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public AdminController(AdvertisementService advertisementService, AccommodationService accommodationService, AgreementService agreementService, UserService userService, CashService cashService, PasswordEncoder passwordEncoder) {
        this.advertisementService = advertisementService;
        this.accommodationService = accommodationService;
        this.agreementService = agreementService;
        this.userService = userService;
        this.cashService = cashService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping(value = "/liste-logements")
    public String allAdvertisement (Model model){
        List<Accommodation> allAccommodation = accommodationService.getAllAccommodation();
        model.addAttribute("AllAccommodation", allAccommodation);
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
    public String allUsers(Model model){
        List<User> allUsers = userService.getAllUser();
        model.addAttribute("AllUsers", allUsers);
        return "ListUsers";
    }

    @GetMapping (value = "/modifier-compte/{id}")
    public String modifyUserById(@PathVariable Long id, Model model){
        User userToModify = userService.getUserById(id);
        model.addAttribute("UserToModify",userToModify);
        /*model.addAttribute("IsTenant",userToModify.getRoles().contains(Role.TENANT));
        model.addAttribute("IsAgency",userToModify.getRoles().contains(Role.AGENCY));*/
        return "ModifyUserAdmin";
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
        userService.deleteUserById(id);
        return "redirect:/admin/tout-users";
    }




}
