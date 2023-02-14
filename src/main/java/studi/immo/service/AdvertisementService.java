package studi.immo.service;

import studi.immo.entity.Advertisement;

import java.util.List;

public interface AdvertisementService {

    Advertisement saveAdvertisement (Advertisement advertisement);

    List<Advertisement> getAllAdvertisement();

    List<Advertisement> getAllAdvertisementAccommodation();

    Advertisement getAdvertisementAccommodationId (Long advertisementId);

    void deleteAdvertisementById (Long id);

    List<Advertisement> getAdvertisementAccommodationByUserId (Long userId);

    Advertisement getAdverttisementById (Long id);

}
