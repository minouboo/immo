package studi.immo.controller;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import studi.immo.entity.*;
import studi.immo.form.AccommodationForm;
import studi.immo.form.UserForm;
import studi.immo.service.*;

import java.math.BigDecimal;
import java.util.List;

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
    private ChatRoomService chatRoomService;
    private AgreementService agreementService;



    @Autowired
    public UserController (UserService userService, AccommodationService accommodationService, CityService cityService, AddressService addressService, AdvertisementService advertisementService, CashService cashService, TenantService tenantService, AgencyService agencyService, PasswordEncoder passwordEncoder, ChatRoomService chatRoomService, AgreementService agreementService){
        this.userService= userService;
        this.accommodationService = accommodationService;
        this.cityService = cityService;
        this.addressService = addressService;
        this.advertisementService = advertisementService;
        this.cashService = cashService;
        this.tenantService = tenantService;
        this.agencyService = agencyService;
        this.passwordEncoder = passwordEncoder;
        this.chatRoomService = chatRoomService;
        this.agreementService = agreementService;
    }

    @GetMapping (value = "/modifier-compte")
    public String modifyUser(Model model){
        User currentUser = userService.getCurrentUser();
        if (currentUser == null){
            return "redirect:/login";
        }
        model.addAttribute("CurrentUser",currentUser);
        model.addAttribute("IsTenant",currentUser.getRoles().contains(Role.TENANT));
        model.addAttribute("IsAgency",currentUser.getRoles().contains(Role.AGENCY));
        return "ModifyUser";
    }

    @PostMapping (value = "/compte-modifie")
    public String updateUser(@ModelAttribute("CurrentUser")User user){
        User updateUser = userService.getCurrentUser();
        if (updateUser == null){
            return "redirect:/login";
        }
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
        User currentUser = userService.getCurrentUser();
        Tenant currentTenant = tenantService.getTenantByUserId(currentUser.getId());
        if(currentTenant !=null){
            return "redirect:/user/modifier-locataire";
        }
        Tenant tenant = new Tenant();
        model.addAttribute("Tenant", tenant);
        return "CreateTenant";
    }

    @PostMapping (value = "/nouveau-locataire")
    public String createTenant (@ModelAttribute ("Tenant") Tenant tenant){
        User tenantUser = userService.getCurrentUser();
        if (tenantUser == null){
            return "redirect:/login";
        }
        tenant.setRevenues(tenant.getRevenues());
        tenant.setUser(tenantUser);
        tenantService.saveTenant(tenant);
        return "redirect:/liste-de-logement";
    }

    @GetMapping(value = "/modifier-locataire")
    public String modifyTenant(Model model){
        User currentUser = userService.getCurrentUser();
        if (currentUser == null){
            return "redirect:/login";
        }
        Tenant currentTenant = tenantService.getTenantByUserId(currentUser.getId());
        if(currentTenant==null){
            return "redirect:/";
        }
        model.addAttribute("CurrentTenant",currentTenant);
        return "ModifyTenant";
    }

    @PostMapping(value = "/locataire-modifie")
    public String updateTenant (@ModelAttribute("CurrentTenant")Tenant tenant){
        User currentUser = userService.getCurrentUser();
        if (currentUser == null){
            return "redirect:/login";
        }
        Tenant currentTenant = tenantService.getTenantByUserId(currentUser.getId());
        currentTenant.setRevenues(tenant.getRevenues());
        tenantService.saveTenant(currentTenant);
        return "redirect:/user/modifier-compte";
    }

    @GetMapping(value = "/creation-agence")
        public String pageCreateAgency(Model model) {
        User currentUser = userService.getCurrentUser();
        if (currentUser == null){
            return "redirect:/login";
        }
        Agency currentAgency = agencyService.getAgencyByUserId(currentUser.getId());
        if (currentAgency != null){
            return "redirect:/user/modifier-agence";
        }
        UserForm agency = new UserForm();
        model.addAttribute("NewAgency", agency);
        return "CreateAgency";
    }

    @PostMapping (value = "/nouvelle-agence")
    public String createAgency (@ModelAttribute("NewAgency")UserForm agencyForm){
        User userAgency = userService.getCurrentUser();
        if (userAgency == null){
            return "redirect:/login";
        }
        Agency newAgency = new Agency();
        City newCity = new City();
        newCity.setName(agencyForm.getCityName());
        newCity.setZipCode(agencyForm.getZipCode());
        cityService.saveCity(newCity);
        Address newAddress = new Address();
        newAddress.setCity(newCity);
        newAddress.setStreetNumber(agencyForm.getStreetNumber());
        newAddress.setStreetName(agencyForm.getCityName());
        addressService.saveAddress(newAddress);
        userAgency.setAddress(newAddress);
        userService.saveUser(userAgency);
        newAgency.setAgencyName(agencyForm.getAgencyName());
        newAgency.setUser(userAgency);
        agencyService.saveAgency(newAgency);
        return "redirect:/liste-de-logement";
    }

    @GetMapping (value = "/modifier-agence")
    public String modifyAgency (Model model){
        User currentUser = userService.getCurrentUser();
        if (currentUser == null){
            return "redirect:/login";
        }
        Agency currentAgency = agencyService.getAgencyByUserId(currentUser.getId());
        if(currentAgency==null){
            return "redirect:/";
        }
        model.addAttribute("CurrentAgency",currentAgency);
        return "ModifyAgency";
    }

    @PostMapping (value = "/agence-modifie")
    public String updateAgency (@ModelAttribute("CurrentAgency")Agency agency){
        User currentUser = userService.getCurrentUser();
        if (currentUser == null){
            return "redirect:/login";
        }
        Agency currentAgency = agencyService.getAgencyByUserId(currentUser.getId());
        currentAgency.setAgencyName(agency.getAgencyName());
        currentAgency.setUser(currentUser);
        agencyService.saveAgency(currentAgency);
        return "redirect:/user/modifier-compte";
    }

    @GetMapping (value="/creation-adresse")
    public String pageCreateAddress ( Model model){
        AccommodationForm accommodationForm = new AccommodationForm();
        model.addAttribute("Address", accommodationForm);
        return "AddAddress";
    }

    @PostMapping (value = "/nouvelle-adresse")
    public String createAddress (@ModelAttribute("Address") AccommodationForm address){
        City c = new City();
        c.setName(address.getCityName());
        c.setZipCode(address.getZipCode());
        c = cityService.saveCity(c);
        Address ad = new Address();
        ad.setStreetNumber(address.getStreetNumber());
        ad.setStreetName(address.getStreetName());
        ad.setCity(c);
        addressService.saveAddress(ad);
        return "redirect:/user/creation-logement/id="+ad.getId();
    }

    @GetMapping (value = "/creation-logement/id={id}")
    public String pageCreateAccommodation(@PathVariable Long id, Model model){
        AccommodationForm accommodationForm = new AccommodationForm();
        model.addAttribute("Accommodation", accommodationForm);
        model.addAttribute("address", addressService.getAddressByID(id));
        return "AddAccommodation";
    }

    @PostMapping (value = "/nouveau-logement/id={id}")
    public String createAccommodation(@PathVariable Long id, @ModelAttribute("Accommodation") AccommodationForm accommodation){
        Address existingAddress = addressService.getAddressByID(id);
        if(existingAddress == null){
            return "redirect:/user/creation-adresse";
        }
        User user = userService.getCurrentUser();
        if (user == null){
            return "redirect:/login";
        }
        Accommodation a = new Accommodation();
        a.setRooms(accommodation.getRooms());
        a.setSquareMeter(accommodation.getSquareMeter());
        a.setUser(user);
        a.setAddress(existingAddress);
        a.setTitle(accommodation.getTitle());
        accommodationService.saveAccommodation(a);
        Advertisement adv = new Advertisement();
        adv.setAccommodation(a);
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
        if (user == null){
            return "redirect:/login";
        }
        model.addAttribute("MyAdvertisements",advertisementService.getAdvertisementAccommodationByUserId(user.getId()));
        model.addAttribute("MyAccommodations",accommodationService.getAccommodationByUserId(user.getId()));
        return "MyAdvertisements";
    }

    @GetMapping (value = "/creation-annonce/{id}")
    public String pageNewAdvertisement (@PathVariable Long id, Model model){
        Advertisement addAdvertisement = new Advertisement();
        Accommodation currentAccommodation = accommodationService.getAccommodationById(id);
        if (currentAccommodation == null){
            return "redirect:/user/creation-adresse";
        }
        model.addAttribute("AddAdvertisement", addAdvertisement);
        model.addAttribute("CurrentAccommodation", currentAccommodation);
        return "AddAdvertisement";
    }

    @PostMapping(value = "/nouvelle-annonce/{id}")
    public String newAdvertisement (@PathVariable Long id, @ModelAttribute("AddAdvertisement")AccommodationForm advertisement){
        Accommodation currentAccommodation = accommodationService.getAccommodationById(id);
        Advertisement addAdvertisement = new Advertisement();
        addAdvertisement.setAccommodation(currentAccommodation);
        addAdvertisement.setRentalPrice(advertisement.getRentalPrice());
        addAdvertisement.setCharges(advertisement.getCharges());
        addAdvertisement.setDeposit(advertisement.getDeposit());
        addAdvertisement.setAgencyFees(addAdvertisement.getAgencyFees());
        addAdvertisement.setDescription(addAdvertisement.getDescription());
        advertisementService.saveAdvertisement(addAdvertisement);
        return "redirect:/user/mes-annonces";
    }

    @GetMapping (value="/modification-mon-annonce/{id}")
    public String modifyMyAdvertisement (@PathVariable Long id, Model model){
        Advertisement myAdvertisement = advertisementService.getAdvertisementById(id);
        if(myAdvertisement == null){
            return "redirect:/";
        }
        User currentUser = userService.getCurrentUser();
        if (currentUser.getRoles().contains(Role.ADMIN) || currentUser == myAdvertisement.getAccommodation().getUser())
        {
            model.addAttribute("MyAdvertisement", myAdvertisement);
            return "ModifyAdvertisement";
        }
        else
        {
            return "Erreur";
        }
    }

    @PostMapping (value = "/annonce-modifie/{id}")
    public String updateMyAdvertisement (@PathVariable Long id, @ModelAttribute("MyAccommodation") AccommodationForm accommodation){
        Advertisement updateAd = advertisementService.getAdvertisementById(id);
        User currentUser = userService.getCurrentUser();
        if (currentUser.getRoles().contains(Role.ADMIN) || updateAd.getAccommodation().getUser() == currentUser)
        {
            updateAd.setDescription(accommodation.getDescription());
            updateAd.setRentalPrice(accommodation.getRentalPrice());
            updateAd.setCharges(accommodation.getCharges());
            updateAd.setAgencyFees(accommodation.getAgencyFees());
            updateAd.setDeposit(accommodation.getDeposit());
            updateAd.setDescription(accommodation.getDescription());
            advertisementService.saveAdvertisement(updateAd);
            return "redirect:/user/mes-annonces" ;
        }
        return "Erreur";

    }

    @GetMapping(value="/annonce/suppression/{id}")
    public String deleteAdvertisement (@PathVariable Long id){
        Advertisement updateAd = advertisementService.getAdvertisementById(id);
        User currentUser = userService.getCurrentUser();
        if (currentUser.getRoles().contains(Role.ADMIN) || updateAd.getAccommodation().getUser() == currentUser)
        {
            advertisementService.deleteAdvertisementById(id);
            return "redirect:/user/mes-annonces" ;
        }
        return "Erreur";
    }

    @GetMapping(value = "/modification-logement/{id}")
    public String pageUpdateAccommodation (@PathVariable Long id, Model model){
        User currentUser = userService.getCurrentUser();
        Accommodation updateAccommodation = accommodationService.getAccommodationById(id);
        if (currentUser.getRoles().contains(Role.ADMIN) || currentUser == updateAccommodation.getUser())
        {
            model.addAttribute("Accommodation", updateAccommodation);
            return "ModifyAccommodation";
        }

        return "Erreur";
    }

    @PostMapping(value = "/logement-modifie/{id}")
    public String updateAccommodation (@PathVariable Long id, @ModelAttribute("Accommodation")Accommodation accommodation){
        User currentUser = userService.getCurrentUser();
        Accommodation updateAccommodation = accommodationService.getAccommodationById(id);
        if(currentUser.getRoles().contains(Role.ADMIN) || updateAccommodation.getUser()== currentUser)
        {
            updateAccommodation.setTitle(accommodation.getTitle());
            updateAccommodation.setRooms(accommodation.getRooms());
            updateAccommodation.setSquareMeter(accommodation.getSquareMeter());
            accommodationService.saveAccommodation(updateAccommodation);
            return "redirect:/user/mes-annonces";
        }
        return"Erreur";
    }

    @GetMapping (value="/modification-adresse/{id}")
    public String updateAddress (@PathVariable Long id, Model model){
        User currentUser = userService.getCurrentUser();
        Accommodation updateAccommodation = accommodationService.getAccommodationById(id);
        Address updateAddress = addressService.getAddressByID(updateAccommodation.getAddress().getId());
        if(currentUser.getRoles().contains(Role.ADMIN) || updateAccommodation.getUser() == currentUser){
            model.addAttribute("Accommodation", updateAccommodation);
            model.addAttribute("Address", updateAddress);
            return "ModifyAddress";
        }
        return "Erreur";
    }

    @PostMapping (value ="/adresse-modifiee/{id}")
    public String addressUpdated (@PathVariable Long id, @ModelAttribute("Address")Address address){
        User currentUser = userService.getCurrentUser();
        Accommodation updateAccommodation = accommodationService.getAccommodationById(id);
        Address updateAdress = updateAccommodation.getAddress();
        if(currentUser.getRoles().contains(Role.ADMIN) || updateAccommodation.getUser() == currentUser){
            updateAdress.setStreetNumber(address.getStreetNumber());
            updateAdress.setStreetName(address.getStreetName());
            addressService.saveAddress(updateAdress);
            return "redirect:/user/modification-logement/"+updateAccommodation.getId();
        }
        return "Erreur";
    }

    @GetMapping (value="/modification-ville/{id}")
    public String updateCity (@PathVariable Long id, Model model){
        User currentUser = userService.getCurrentUser();
        Accommodation updateAccommodation = accommodationService.getAccommodationById(id);
        City updateCity = accommodationService.getAccommodationById(id).getAddress().getCity();
        if(currentUser.getRoles().contains(Role.ADMIN) || updateAccommodation.getUser() == currentUser){
            model.addAttribute("Accommodation", updateAccommodation);
            model.addAttribute("City", updateCity);
            return "ModifyCity";
        }
        return "Erreur";
    }

    @PostMapping (value ="/ville-modifiee/{id}")
    public String cityUpdated (@PathVariable Long id, @ModelAttribute("Address")City city){
        User currentUser = userService.getCurrentUser();
        Accommodation updateAccommodation = accommodationService.getAccommodationById(id);
        City updateCity = accommodationService.getAccommodationById(id).getAddress().getCity();
        if(currentUser.getRoles().contains(Role.ADMIN) || updateAccommodation.getUser() == currentUser){
            updateCity.setName(city.getName());
            updateCity.setZipCode(city.getZipCode());
            cityService.saveCity(updateCity);
            return "redirect:/user/modification-logement/"+updateAccommodation.getId();
        }
        return "Erreur";
    }

    @GetMapping (value = "/logement/suppression/{id}")
    public String deleteAccommodation (@PathVariable Long id ,Model model){
        User currentUser = userService.getCurrentUser();
        Accommodation updateAccommodation = accommodationService.getAccommodationById(id);
        List<Agreement> listAgreementAccommodation = agreementService.getAllAgreementWithAccommodationId(updateAccommodation.getId());
        if (currentUser.getRoles().contains(Role.ADMIN) || currentUser == updateAccommodation.getUser())
        {
            if ( listAgreementAccommodation.size() == 0 && listAgreementAccommodation.isEmpty())
            {
                accommodationService.deleteAccommodationById(id);
                return "redirect:/user/mes-annonces";
            } else if (listAgreementAccommodation.size() != 0) {
                return "ErreurLogementSuppression";
            }

        }
        return "Erreur";
    }

    @GetMapping(value = "/mon-portefeuille")
    public String myWallet (Model model){
        User user = userService.getCurrentUser();
        if (user == null){
            return "redirect:/login";
        }
        Cash updateCash = cashService.getCashByUserID(user.getId());
        Cash newCash = new Cash();
        model.addAttribute("MyCash", updateCash);
        model.addAttribute("AddCash",newCash);
        return "MyWallet";
    }

    @PostMapping (value = "/reappro-portefeuille")
    public String addMoney (@ModelAttribute("AddCash") Cash cash){
        User user = userService.getCurrentUser();
        if (user == null){
            return "redirect:/login";
        }
        Cash updateCash = cashService.getCashByUserID(user.getId());
        BigDecimal oldAmount = updateCash.getAmount();

        updateCash.setAmount(cash.getAmount().add(oldAmount));
        cashService.saveCash(updateCash);
        return "redirect:/user/mon-portefeuille";
    }


}
