package studi.immo.service;

import studi.immo.entity.ApartmentInventory;

public interface ApartmentInventoryService {

    ApartmentInventory saveApartmentInventory (ApartmentInventory apartmentInventory);

    ApartmentInventory getApartmentInventoryByAgreementId (Long agreementId);

    ApartmentInventory getApartmentInventoryById (Long id);

}
