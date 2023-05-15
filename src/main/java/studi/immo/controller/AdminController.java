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
import studi.immo.form.AgreementForm;
import studi.immo.service.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

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
    private ApartmentInventoryService apartmentInventoryService;
    private PaymentRequestService paymentRequestService;


    @Autowired
    public AdminController(AdvertisementService advertisementService, AccommodationService accommodationService, AgreementService agreementService, UserService userService, CashService cashService, PasswordEncoder passwordEncoder, AgencyService agencyService, TenantService tenantService, ApartmentInventoryService apartmentInventoryService, PaymentRequestService paymentRequestService) {
        this.advertisementService = advertisementService;
        this.accommodationService = accommodationService;
        this.agreementService = agreementService;
        this.userService = userService;
        this.cashService = cashService;
        this.passwordEncoder = passwordEncoder;
        this.agencyService = agencyService;
        this.tenantService = tenantService;
        this.apartmentInventoryService = apartmentInventoryService;
        this.paymentRequestService = paymentRequestService;
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
        model.addAttribute("Title","Contrats à valider");
        boolean isActive = true;
        model.addAttribute("ButtonValidating", isActive);
        model.addAttribute("ButtonValidated", !isActive);
        model.addAttribute("ButtonTerminated", !isActive);
        Accommodation accommodation = accommodationService.getAccommodationById(id);
        model.addAttribute("Accommodation", accommodation);
        List<Agreement> allAgreements = agreementService.getAllAgreementByAccommodationById(id);
        model.addAttribute("MyAgreements",allAgreements);
        return "AdminListAgreements";
    }

    @GetMapping (value = "/les-contrats-valides/{id}")
    public String allAgreementsValidated (@PathVariable Long id, Model model){
        model.addAttribute("Title","Contrats en cours");
        boolean isActive = true;
        model.addAttribute("ButtonValidated", isActive);
        model.addAttribute("ButtonValidating", !isActive);
        model.addAttribute("ButtonTerminated", !isActive);
        Accommodation accommodation = accommodationService.getAccommodationById(id);
        model.addAttribute("Accommodation", accommodation);
        List<Agreement> allAgreementsValidated = agreementService.getAllAgreementValidatedByAccommodationById(id);
        model.addAttribute("MyAgreements", allAgreementsValidated);
        return "AdminListAgreements";
    }

    @GetMapping (value = "/les-contrats-termines/{id}")
    public String allAgreementsTerminated (@PathVariable Long id, Model model){
        model.addAttribute("Title","Contrats terminés");
        boolean isActive = true;
        model.addAttribute("ButtonTerminated", isActive);
        model.addAttribute("ButtonValidating", !isActive);
        model.addAttribute("ButtonValidated", !isActive);
        Accommodation accommodation = accommodationService.getAccommodationById(id);
        model.addAttribute("Accommodation", accommodation);
        List<Agreement> allAgreementsTerminated = agreementService.getAllAgreementTerminatedByAccommodationById(id);
        model.addAttribute("MyAgreements", allAgreementsTerminated);
        return "AdminListAgreements";
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

    @GetMapping (value = "/les-annonces/{id}")
    public String myAdvertisements (@PathVariable Long id, Model model){
        User targetUser = accommodationService.getAccommodationById(id).getUser();
        Accommodation targetAccommodation = accommodationService.getAccommodationById(id);
        List<Advertisement> listAdvertisement = advertisementService.getAdvertisementByAccommodationId(id);
        model.addAttribute("MyAdvertisements",listAdvertisement);
        model.addAttribute("MyAccommodations",targetAccommodation);
        return "MyAdvertisements";
    }

    @GetMapping (value = "/creation-contrat/{id}")
    public String pageAdminCreateAgreement (@PathVariable Long id, Model model){
        AgreementForm newAgreement = new AgreementForm();
        Accommodation currentAccommodation =  accommodationService.getAccommodationById(id);
        model.addAttribute("AdminAgreement", newAgreement);
        model.addAttribute("CurrentAccommodation", currentAccommodation);
        return "AdminCreateAgreement";
    }

    @PostMapping (value="/nouveau-contrat/{id}")
    public String adminAgreementCreated (@PathVariable Long id, @ModelAttribute("AdminAgreement") AgreementForm agreement){
        User currentUser = userService.getCurrentUser();
        Accommodation currentAccommodation = accommodationService.getAccommodationById(id);
        Agreement newAgreement = new Agreement();
        newAgreement.setAccommodation(currentAccommodation);
        newAgreement.setRentalPrice(agreement.getRentalPrice());
        newAgreement.setCharges(agreement.getCharges());
        newAgreement.setDeposit(agreement.getDeposit());
        newAgreement.setEntryDate(agreement.getStartAgreementDate());
        newAgreement.setAgencyFees(agreement.getAgencyFees());
        newAgreement.getUsers().add(currentUser);
        agreementService.saveAgreement(newAgreement);
        ApartmentInventory newApartmentInventory = new ApartmentInventory();
        newApartmentInventory.setInventoryType(InventoryType.ENTRY);
        newApartmentInventory.setAgreement(newAgreement);
        apartmentInventoryService.saveApartmentInventory(newApartmentInventory);
        ApartmentInventory newApartmentInventoryExit = new ApartmentInventory();
        newApartmentInventoryExit.setAgreement(newAgreement);
        newApartmentInventoryExit.setInventoryType(InventoryType.EXIT);
        apartmentInventoryService.saveApartmentInventory(newApartmentInventoryExit);
        return "redirect:/contrat/mon-contrat/"+newAgreement.getId();
    }

    @GetMapping (value = "/ajouter-locataire/{id}")
    public String pageAddTenantToAgreement( @PathVariable Long id, Model model, String keyword){
        if (keyword != null)
        {
            List<User> allUsers = userService.searchUser(keyword);
            model.addAttribute("AllUsers", allUsers);
        }
        else
        {
            model.addAttribute("AllUsers", userService.getAllUser());
        }
        Agreement currentAgreement = agreementService.getAgreementById(id);
        model.addAttribute("Agreement",currentAgreement);
        Set<User> setUsersAgreement = currentAgreement.getUsers();
        model.addAttribute("AgreementUsers", setUsersAgreement);
        AgreementForm agreementForm = new AgreementForm();
        model.addAttribute("AgreementForm", agreementForm);
        return "AdminAddTenantAgreement";
    }

    @PostMapping (value="/locataire-ajoute/{id}")
    public String tenantAdded (@PathVariable Long id, @ModelAttribute("AgreementForm") AgreementForm agreement){
        Agreement currentAgreement = agreementService.getAgreementById(id);
        User tenantUser = userService.getUserById(agreement.getTenantUserId());
        if (tenantUser.getId() == null){
            return "redirect:/admin/ajouter-locataire/"+currentAgreement.getId();
        }
        currentAgreement.getUsers().add(tenantUser);
        agreementService.saveAgreement(currentAgreement);
        return "redirect:/contrat/mon-contrat/"+currentAgreement.getId();
    }

    @PostMapping (value="/retirer-locataire/{id}")
    public String deleteUserFromAgreement (@PathVariable Long id, @ModelAttribute("AgreementForm") AgreementForm agreement){
        User tenantUser = userService.getUserById(agreement.getTenantUserId());
        Agreement currentAgreement = agreementService.getAgreementById(id);
        currentAgreement.getUsers().remove(tenantUser);
        agreementService.saveAgreement(currentAgreement);
        return "redirect:/contrat/mon-contrat/"+currentAgreement.getId();
    }

    @PostMapping (value = "/valider-contrat-tous/{id}")
    public String validateAgreementForAll (@PathVariable Long id, @ModelAttribute ("AgreementValidation")Agreement agreement){
        User currentUser = userService.getCurrentUser();
        if (!currentUser.getRoles().contains(Role.ADMIN)){
            return "Erreur";
        }
        Agreement validateAgreement = agreementService.getAgreementById(id);
        validateAgreement.setTenantValidate(Boolean.TRUE);
        validateAgreement.setLandlordValidate(Boolean.TRUE);
        agreementService.saveAgreement(validateAgreement);
        return "redirect:/paiement/mon-contrat/"+validateAgreement.getId();
    }

    @PostMapping (value = "/valider-paiement/{id}")
    public String validatePayment (@PathVariable Long id, @ModelAttribute("Payment")PaymentRequest paymentRequest){
        User currentUser = userService.getCurrentUser();
        if (!currentUser.getRoles().contains(Role.ADMIN)){
            return "Erreur";
        }
        PaymentRequest currentPayment = paymentRequestService.getPaymentRequestById(id);
        currentPayment.setPaymentDate(LocalDateTime.now());
        currentPayment.setUserPayer(currentUser);
        currentPayment.setTenantPaid(Boolean.TRUE);
        paymentRequestService.savePaymentRequest(currentPayment);
        return "redirect:/paiement/voir-paiement/"+currentPayment.getId();
    }

    @PostMapping (value = "/unvalider-paiement/{id}")
    public String unvalidatePayment( @PathVariable Long id, @ModelAttribute("Payment")PaymentRequest paymentRequest){
        User currentUser = userService.getCurrentUser();
        if (!currentUser.getRoles().contains(Role.ADMIN)){
            return "Erreur";
        }
        PaymentRequest currentPayment = paymentRequestService.getPaymentRequestById(id);
        currentPayment.setTenantPaid(Boolean.FALSE);
        paymentRequestService.savePaymentRequest(currentPayment);
        return "redirect:/paiement/voir-paiement/"+currentPayment.getId();
    }
}