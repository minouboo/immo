package studi.immo.service.implement;

import org.springframework.stereotype.Service;
import studi.immo.entity.Accommodation;
import studi.immo.repository.AccomodationRepository;
import studi.immo.service.AccommodationService;

import java.util.List;

@Service
public class AccommodationServiceImplement implements AccommodationService {

    private AccomodationRepository accomodationRepository;

    public AccommodationServiceImplement (AccomodationRepository accomodationRepository){
        super();
        this.accomodationRepository = accomodationRepository;
    }

    @Override
    public Accommodation saveAccommodation(Accommodation accommodation) {
        return accomodationRepository.save(accommodation);
    }

    @Override
    public Accommodation getAccommodationById(Long id) {
        return accomodationRepository.findById(id).get();
    }

    @Override
    public Accommodation getAccommodationAndUserById(Long accommodationId) {
        return accomodationRepository.getAccommodationAndUserById(accommodationId);
    }

    @Override
    public List<Accommodation> getAccommodationByUserId(Long userId) {
        return accomodationRepository.getAccommodationByUserId(userId);
    }

    @Override
    public void deleteAccommodationById(Long id) {
        accomodationRepository.deleteById(id);
    }


}
