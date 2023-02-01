package studi.immo.service.implement;

import org.springframework.stereotype.Service;
import studi.immo.entity.City;
import studi.immo.repository.CityRepository;
import studi.immo.service.CityService;

@Service
public class CityServiceImplement implements CityService {

    private CityRepository cityRepository;

    public CityServiceImplement (CityRepository cityRepository){
        super();
        this.cityRepository = cityRepository;
    }

    @Override
    public City saveCity(City city) {
        return cityRepository.save(city);
    }


}
