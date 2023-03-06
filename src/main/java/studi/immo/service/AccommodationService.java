package studi.immo.service;

import studi.immo.entity.Accommodation;

import java.util.List;

public interface AccommodationService {

    Accommodation saveAccommodation (Accommodation accommodation);

    Accommodation getAccommodationById (Long id);

    Accommodation getAccommodationAndUserById (Long accommodationId);

    List<Accommodation> getAccommodationByUserId (Long userId);

    void deleteAccommodationById (Long id);



}
