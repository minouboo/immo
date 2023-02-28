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
import studi.immo.form.AccommodationForm;
import studi.immo.form.AddressForm;
import studi.immo.service.*;

import java.math.BigDecimal;

@Log
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Controller
@PreAuthorize("isAuthenticated()")
@RequestMapping(value="user")


public class UserController {

    private UserService userService;
    private AccommodationService accommodationService;
    private CityService cityService;
    private AddressService addressService;
    private AdvertisementService advertisementService;
    private AgencyService agencyService;
    private CashService cashService;
    private TenantService tenantService;
    private PasswordEncoder passwordEncoder;


    @Autowired
    public UserController (UserService userService, AccommodationService accommodationService, CityService cityService, AddressService addressService, AdvertisementService advertisementService, CityApiClientService cityApiClient, CityApiClientService cityApiClient1, CityApiClientService cityApiClientService, CashService cashService, TenantService tenantService, MessageService messageService, ChatRoomService chatRoomService, AgencyService agencyService, PasswordEncoder passwordEncoder){
        this.userService= userService;
        this.accommodationService = accommodationService;
        this.cityService = cityService;
        this.addressService = addressService;
        this.advertisementService = advertisementService;
        this.cashService = cashService;
        this.tenantService = tenantService;
        this.agencyService = agencyService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping (value = "/modifier-compte")
    public String modifyUser(Model model){
        User currentUser = userService.getCurrentUser();
        model.addAttribute("CurrentUser",currentUser);
        model.addAttribute("IsTenant",currentUser.getRoles().contains(Role.TENANT));
        model.addAttribute("IsAgency",currentUser.getRoles().contains(Role.AGENCY));
        return "ModifyUser";
    }

    @PostMapping (value = "/compte-modifie")
    public String updateUser(@ModelAttribute("CurrentUser")User user){
        User updateUser = userService.getCurrentUser();
        updateUser.setFirstName(user.getFirstName());
        updateUser.setLastName(user.getLastName());
        updateUser.setUserName(user.getUserName());
        updateUser.setEmail(user.getEmail());
        updateUser.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.saveUser(updateUser);
        return "redirect:/";
    }

    @GetMapping(value = "/creation-locataire")
    public String pageCreateTenant(Model model){
        Tenant tenant = new Tenant();
        model.addAttribute("Tenant", tenant);
        return "CreateTenant";
    }

    @PostMapping (value = "/nouveau-locataire")
    public String createTenant (@ModelAttribute ("Tenant") Tenant tenant){
        User tenantUser = userService.getCurrentUser();
        tenant.setRevenues(tenant.getRevenues());
        tenant.setUser(tenantUser);
        tenantService.saveTenant(tenant);
        return "redirect:/liste-de-logement";
    }

    @GetMapping(value = "/modifier-locataire")
    public String modifyTenant(Model model){
        User currentUser = userService.getCurrentUser();
        Tenant currentTenant = tenantService.getTenantByUserId(currentUser.getId());
        model.addAttribute("CurrentTenant",currentTenant);
        return "ModifyTenant";
    }

    @PostMapping(value = "/locataire-modifie")
    public String updateTenant (@ModelAttribute("CurrentTenant")Tenant tenant){
        User currentUser = userService.getCurrentUser();
        Tenant currentTenant = tenantService.getTenantByUserId(currentUser.getId());
        currentTenant.setRevenues(tenant.getRevenues());
        tenantService.saveTenant(currentTenant);
        return "redirect:/";
    }

    @GetMapping(value = "/creation-agence")
        public String pageCreateAgency(Model model) {
        Agency agency = new Agency();
        model.addAttribute("NewAgency", agency);
        return "CreateAgency";
    }

    @PostMapping (value = "/nouvelle-agence")
    public String createAgency (@ModelAttribute("NewAgency")Agency agency){
        User userAgency = userService.getCurrentUser();
        agency.setAgencyName(agency.getAgencyName());
        agency.setUser(userAgency);
        agencyService.saveAgency(agency);
        return "redirect:/liste-de-logement";
    }

    @GetMapping (value = "/modifier-agence")
    public String modifyAgency (Model model){
        User currentUser = userService.getCurrentUser();
        Agency currentAgency = agencyService.getAgencyByUserId(currentUser.getId());
        model.addAttribute("CurrentAgency",currentAgency);
        return "ModifyAgency";
    }

    @PostMapping (value = "/agence-modifie")
    public String updateAgency (@ModelAttribute("CurrentAgency")Agency agency){
        User currentUser = userService.getCurrentUser();
        Agency currentAgency = agencyService.getAgencyByUserId(currentUser.getId());
        currentAgency.setAgencyName(agency.getAgencyName());
        currentAgency.setUser(currentUser);
        agencyService.saveAgency(currentAgency);
        return "redirect:/";
    }

    @GetMapping (value="/create-address")
    public String pageCreateAddress ( Model model){
        AddressForm addressForm = new AddressForm();
        model.addAttribute("Address", addressForm);
        return "AddAddress";
    }

    @PostMapping (value = "/new-address")
    public String createAddress (@ModelAttribute("Address") AddressForm address){
        City c = new City();
        c.setName(address.getCityName());
        c.setZipCode(address.getZipCode());
        c = cityService.saveCity(c);
        Address ad = new Address();
        ad.setStreetNumber(address.getStreetNumber());
        ad.setStreetName(address.getStreetName());
        ad.setCity(c);
        addressService.saveAddress(ad);
        return "redirect:/user/create-accommodation/id="+ad.getId();
    }

    @GetMapping (value = "/create-accommodation/id={id}")
    public String pageCreateAccommodation(@PathVariable Long id, Model model){
        AccommodationForm accommodationForm = new AccommodationForm();
        model.addAttribute("Accommodation", accommodationForm);
        model.addAttribute("address", addressService.getAddressByID(id));
        return "AddAccommodation";
    }

    @PostMapping (value = "/new-accommodation/id={id}")
    public String createAccommodation(@PathVariable Long id, @ModelAttribute("Accommodation") AccommodationForm accommodation){
        Address existingAddress = addressService.getAddressByID(id);
        Accommodation a = new Accommodation();
        User user = userService.getCurrentUser();
        a.setRooms(accommodation.getRooms());
        a.setSquareMeter(accommodation.getSquareMeter());
        a.setUser(user);
        a.setAddress(existingAddress);
        accommodationService.saveAccommodation(a);
        Advertisement adv = new Advertisement();
        adv.setAccommodation(a);
        adv.setTitle(accommodation.getTitle());
        adv.setRentalPrice(accommodation.getRentalPrice());
        adv.setCharges(accommodation.getCharges());
        adv.setDeposit(accommodation.getDeposit());
        adv.setAgencyFees(accommodation.getAgencyFees());
        adv.setDescription(accommodation.getDescription());
        advertisementService.saveAdvertisement(adv);
        return "redirect:/user/mes-annonces";
    }

    @GetMapping (value = "/mes-annonces")
    public String myAdvertisements (Model model){
        User user = userService.getCurrentUser();
        model.addAttribute("MyAdvertisements",advertisementService.getAdvertisementAccommodationByUserId(user.getId()));
        return "MyAdvertisements";
    }

    @GetMapping (value="/modification-mon-annonce/{id}")
    public String modifyMyAdvertisement (@PathVariable Long id, Model model){
        model.addAttribute("MyAccommodation", advertisementService.getAdvertisementById(id));
        return "ModifyAdvertisement";
    }

    @PostMapping (value = "/annonce-modifie/{id}")
    public String updateMyAdvertisement (@PathVariable Long id, @ModelAttribute("MyAccommodation") AccommodationForm accommodation){
        Advertisement updateAd = advertisementService.getAdvertisementById(id);
        updateAd.setTitle(accommodation.getTitle());
        updateAd.setDescription(accommodation.getDescription());
        updateAd.setRentalPrice(accommodation.getRentalPrice());
        updateAd.setCharges(accommodation.getCharges());
        updateAd.setAgencyFees(accommodation.getAgencyFees());
        updateAd.setDeposit(accommodation.getDeposit());
        updateAd.setDescription(accommodation.getDescription());
        advertisementService.saveAdvertisement(updateAd);
        return "redirect:/user/mes-annonces" ;
    }

    @GetMapping(value = "/mon-portefeuille")
    public String myWallet (Model model){
        User user = userService.getCurrentUser();
        Cash updateCash = cashService.getCashByUserID(user.getId());
        model.addAttribute("MyCash", cashService.getCashByUserID(user.getId()));
        model.addAttribute("AddCash",updateCash);
        return "MyWallet";
    }

    @PostMapping (value = "/reappro-portefeuille")
    public String addMoney (@ModelAttribute("AddCash") Cash cash){
        User user = userService.getCurrentUser();
        Cash updateCash = cashService.getCashByUserID(user.getId());
        BigDecimal oldAmount = updateCash.getAmount();

        updateCash.setAmount(cash.getAmount().add(oldAmount));
        cashService.saveCash(updateCash);
        return "redirect:/user/mon-portefeuille";
    }

}
