package studi.immo.controller;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import studi.immo.api.CityApiResponse;
import studi.immo.entity.*;
import studi.immo.form.AccommodationForm;
import studi.immo.form.AddressForm;
import studi.immo.repository.AccomodationRepository;
import studi.immo.service.*;

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

    private CityApiClientService cityApiClientService;

    @Autowired
    public UserController (UserService userService, AccommodationService accommodationService, CityService cityService, AddressService addressService, AdvertisementService advertisementService, CityApiClientService cityApiClient, CityApiClientService cityApiClient1, CityApiClientService cityApiClientService){
        this.userService= userService;
        this.accommodationService = accommodationService;
        this.cityService = cityService;
        this.addressService = addressService;
        this.advertisementService = advertisementService;
        this.cityApiClientService = cityApiClientService;
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
        model.addAttribute("MyAccommodation", advertisementService.getAdverttisementById(id));
        return "ModifyAdvertisement";
    }

    @PostMapping (value = "/annonce-modifie/{id}")
    public String updateMyAdvertisement (@PathVariable Long id, @ModelAttribute("MyAccommodation") AccommodationForm accommodation){
        Advertisement updateAd = advertisementService.getAdverttisementById(id);
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
    public String myWallet (){
        return "MyWallet";
    }





}
