package studi.immo.controller;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import studi.immo.api.CityApiResponse;
import studi.immo.entity.*;
import studi.immo.form.AccommodationForm;
import studi.immo.form.AddressForm;
import studi.immo.service.*;

import java.util.List;

@Log
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Controller
@RequestMapping(value="user")
@PreAuthorize("isAuthenticated()")

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

    @GetMapping (value = "/create-accommodation")
    public String pageCreateAccommodation(Model model){
        AccommodationForm accommodationForm = new AccommodationForm();
        model.addAttribute("Accommodation", accommodationForm);
        return "AddAccommodation";
    }

    @PostMapping (value = "/new-accommodation")
    public String createAccommodation(@ModelAttribute("Accommodation") AccommodationForm accommodation){
        Accommodation a = new Accommodation();
        User user = userService.getCurrentUser();
        a.setRooms(accommodation.getRooms());
        a.setSquareMeter(accommodation.getSquareMeter());
        a.setUser(user);
        accommodationService.saveAccommodation(a);
        Advertisement adv = new Advertisement();
        adv.setAccommodation(a);
        adv.setRentalPrice(accommodation.getRentalPrice());
        adv.setCharges(accommodation.getCharges());
        adv.setDeposit(accommodation.getDeposit());
        adv.setAgencyFees(accommodation.getAgencyFees());
        adv.setDescription(accommodation.getDescription());
        advertisementService.saveAdvertisement(adv);
        return "redirect:/user/create-address";
    }

    @GetMapping (value="/create-address")
    public String pageCreateAddress (Model model){
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
        return "redirect:/user/create-advertisement";
    }

    @GetMapping (value = "/create-advertisement")
    public String pageCreateAdvertisement (Model model){
        Advertisement advertisement = new Advertisement();
        model.addAttribute("Advertisement", advertisement);
        return "AddAdvertisement";
    }

    @PostMapping (value = "/new-advertisement")
    public String createAdvertisement (@ModelAttribute("Advertisement")Advertisement advertisement){
        advertisementService.saveAdvertisement(advertisement);
        return "Index";
    }

    @GetMapping (value = "/create-city")
    public String pageCreateCity(Model model){
        City city = new City();
        model.addAttribute("City", city);
        List<CityApiResponse> cityList = cityApiClientService.getCity(null,"Paris");
        return "AddCity";
    }


}
