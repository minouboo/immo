package studi.immo.controller;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import studi.immo.service.AdvertisementService;

@Log
@Controller
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping(value="admin")
public class AdminController {

    private AdvertisementService advertisementService;

    @Autowired
    public AdminController(AdvertisementService advertisementService) {
        this.advertisementService = advertisementService;
    }



}
