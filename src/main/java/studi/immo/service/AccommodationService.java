package studi.immo.service;

import studi.immo.entity.Accommodation;

public interface AccommodationService {

    Accommodation saveAccommodation (Accommodation accommodation);

    Accommodation getAccommodationById (Long id);

    Accommodation getAccommodationAndUserById (Long accommodationId);



}
