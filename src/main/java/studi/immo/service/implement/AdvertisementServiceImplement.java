package studi.immo.service.implement;

import org.apache.tomcat.jni.File;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import studi.immo.entity.Advertisement;
import studi.immo.repository.AdvertisementRepository;
import studi.immo.service.AdvertisementService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class AdvertisementServiceImplement implements AdvertisementService {

    private AdvertisementRepository advertisementRepository;

    public AdvertisementServiceImplement (AdvertisementRepository advertisementRepository){
        super();
        this.advertisementRepository = advertisementRepository;
    }

    @Override
    public Advertisement saveAdvertisement(Advertisement advertisement) {
        return advertisementRepository.save(advertisement);
    }

    @Override
    public List<Advertisement> getAllAdvertisement(){
        return advertisementRepository.findAll();
    }

    @Override
    public List<Advertisement> getAllAdvertisementAccommodation() {
        return advertisementRepository.getAllAdvertisementAccommodation();
    }

    @Override
    public Advertisement getAdvertisementAccommodationById(Long advertisementId) {
        return advertisementRepository.getAdvertisementAccommodationById(advertisementId);
    }

    @Override
    public void deleteAdvertisementById(Long id) {
        advertisementRepository.deleteById(id);
    }

    @Override
    public List<Advertisement> getAdvertisementAccommodationByUserId(Long userId) {
        return advertisementRepository.getAdvertisementAccommodationByUserId(userId);
    }

    @Override
    public Advertisement getAdvertisementById(Long id) {
        return advertisementRepository.findById(id).get();
    }

    @Override
    public List<Advertisement> searchAdvertisement(String keyword) {
        return advertisementRepository.searchAdvertisement(keyword);
    }

    @Override
    public List<Advertisement> getAdvertisementByAccommodationId(Long accommodationId) {
        return advertisementRepository.getAdvertisementByAccommodationId(accommodationId);
    }


}
