package studi.immo.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import studi.immo.api.CityApiResponse;

import java.util.List;

@Service
@FeignClient (value = "APIVille", url="https://geo.api.gouv.fr/")
public interface CityApiClientService {

        @RequestMapping(method = RequestMethod.GET, value="communes")
        List<CityApiResponse> getCity (@RequestParam ("codesPostaux") String codesPostaux,
                                       @RequestParam("nom") String nom,
                                       @RequestParam ("fields") String fields,
                                       @RequestParam ("format") String format
                                       );

}


