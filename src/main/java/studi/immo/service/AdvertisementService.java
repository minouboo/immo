package studi.immo.service;

import org.springframework.web.multipart.MultipartFile;
import studi.immo.entity.Advertisement;

import java.io.IOException;
import java.util.List;

public interface AdvertisementService {

    Advertisement saveAdvertisement (Advertisement advertisement);

    List<Advertisement> getAllAdvertisement();

    List<Advertisement> getAllAdvertisementAccommodation();

    Advertisement getAdvertisementAccommodationById (Long advertisementId);

    void deleteAdvertisementById (Long id);

    List<Advertisement> getAdvertisementAccommodationByUserId (Long userId);

    Advertisement getAdvertisementById (Long id);

    List<Advertisement> searchAdvertisement (String keyword);

    List<Advertisement> getAdvertisementByAccommodationId (Long accommodationId);

}
