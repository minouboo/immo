package studi.immo.service.implement;

import org.springframework.stereotype.Service;
import studi.immo.entity.Advertisement;
import studi.immo.repository.AdvertisementRepository;
import studi.immo.service.AdvertisementService;

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
}
