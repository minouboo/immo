package studi.immo.service.implement;

import org.springframework.stereotype.Service;
import studi.immo.entity.ApartmentInventory;
import studi.immo.repository.ApartmentInventoryRepository;
import studi.immo.service.ApartmentInventoryService;

@Service
public class ApartmentInventoryServiceImplement implements ApartmentInventoryService {

    private ApartmentInventoryRepository apartmentInventoryRepository;

    public ApartmentInventoryServiceImplement (ApartmentInventoryRepository apartmentInventoryRepository){
        super();
        this.apartmentInventoryRepository = apartmentInventoryRepository;
    }

    @Override
    public ApartmentInventory saveApartmentInventory(ApartmentInventory apartmentInventory) {
        return apartmentInventoryRepository.save(apartmentInventory);
    }

    @Override
    public ApartmentInventory getApartmentInventoryByAgreementId(Long agreementId) {
        return apartmentInventoryRepository.getApartmentInventoryByAgreementId(agreementId);
    }

    @Override
    public ApartmentInventory getApartmentInventoryById(Long id) {
        return apartmentInventoryRepository.findById(id).get();
    }

    @Override
    public ApartmentInventory getApartmentInventoryExitByAgreementId(Long agreementId) {
        return apartmentInventoryRepository.getApartmentInventoryExitByAgreementId(agreementId);
    }


}
