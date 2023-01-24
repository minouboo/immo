package studi.immo.controller;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import studi.immo.entity.*;
import studi.immo.service.*;

@Log
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Controller
@RequestMapping(value="user")

public class UserController {

    private UserService userService;
    private AccommodationService accommodationService;
    private CityService cityService;
    private AddressService addressService;
    private AdvertisementService advertisementService;

    @Autowired
    public UserController (UserService userService, AccommodationService accommodationService, CityService cityService, AddressService addressService, AdvertisementService advertisementService){
        this.userService= userService;
        this.accommodationService = accommodationService;
        this.cityService = cityService;
        this.addressService = addressService;
        this.advertisementService = advertisementService;
    }


    @GetMapping (value = "/create-account")
    public String pageCreateAccount(Model model){
        User user = new User();
        model.addAttribute("User", user);
        return "CreateUser";
    }

    @PostMapping (value = "/new-account")
    public String createUser(@ModelAttribute ("User") User user){
        userService.saveUser(user);
        return "Index";
    }

    @GetMapping (value = "/create-accommodation")
    public String pageCreateAccommodation(Model model){
        Accommodation accommodation = new Accommodation();
        model.addAttribute("Accommodation", accommodation);
        return "AddAccommodation";
    }

    @PostMapping (value = "/new-accommodation")
    public String createAccommodation(@ModelAttribute("Accommodation") Accommodation accommodation){
        accommodationService.saveAccommodation(accommodation);
        return "redirect:/user/create-city";
    }

    @GetMapping (value = "/create-city")
    public String pageCreateCity(Model model){
        City city = new City();
        model.addAttribute("City", city);
        return "AddCity";
    }

    @PostMapping (value = "/new-city")
    public String createCity(@ModelAttribute("City") City city){
        cityService.saveCity(city);
        return "redirect:/user/create-address";
    }

    @GetMapping (value = "/create-address")
    public String pageCreateAddress(Model model){
        Address address = new Address();
        model.addAttribute("Address", address);
        return "AddAddress";
    }

    @PostMapping (value = "/new-address")
    public String createAddress (@ModelAttribute("Address") Address address){
        addressService.saveAddress(address);
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
}
