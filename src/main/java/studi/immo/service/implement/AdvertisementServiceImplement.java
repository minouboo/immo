package studi.immo.service.implement;

import org.springframework.stereotype.Service;
import studi.immo.entity.Advertisement;
import studi.immo.repository.AdvertisementRepository;
import studi.immo.service.AdvertisementService;

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
    public Advertisement getAdvertisementAccommodationId(Long advertisementId) {
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
    public Advertisement getAdverttisementById(Long id) {
        return advertisementRepository.findById(id).get();
    }


}
