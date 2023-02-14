package studi.immo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import studi.immo.api.CityApiResponse;
import studi.immo.entity.Advertisement;
import studi.immo.entity.City;
import studi.immo.form.AddressForm;
import studi.immo.service.AdvertisementService;
import studi.immo.service.CityApiClientService;
import studi.immo.service.CityService;

import java.util.List;

@Controller
@RequestMapping(value = "test")
public class TestController {

    private CityService cityService;
    private AdvertisementService advertisementService;
    private CityApiClientService cityApiClientService;

    @Autowired
    public TestController(CityService cityService, AdvertisementService advertisementService, CityApiClientService cityApiClientService) {
        this.cityService = cityService;
        this.advertisementService = advertisementService;
        this.cityApiClientService = cityApiClientService;
    }

    @GetMapping(value = "/getcity")
    public String pageCreateCityTest (Model model){
        City city = new City();
        model.addAttribute("City", city);
        return "AddCity";
    }

    @PostMapping(value="/addcity")
    public String createCityTest (@ModelAttribute("City") City city){
        cityService.saveCity(city);
        return "Index";
    }

    @GetMapping (value="/create-address?id={id}")
    public String pageCreateAddressTest ( Model model){
        AddressForm addressForm = new AddressForm();
        model.addAttribute("Address", addressForm);

        return "AddAddress";
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
        List<CityApiResponse> cityList = cityApiClientService.getCity(null,"Paris","nom,codesPostaux","json");
        return "AddCity";
    }

}
