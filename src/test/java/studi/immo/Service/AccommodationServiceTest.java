package studi.immo.Service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import studi.immo.entity.Accommodation;
import studi.immo.entity.User;
import studi.immo.repository.AccomodationRepository;
import studi.immo.service.AccommodationService;
import studi.immo.service.implement.AccommodationServiceImplement;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AccommodationServiceTest {

    @Mock
    private AccomodationRepository accomodationRepository;

    @InjectMocks
    private AccommodationServiceImplement accommodationServiceImplement;



    @Test
    public void testGetAccommodationByUserId(){

        User userTest = new User();
        userTest.setUserName("test");
        Accommodation accommodationTest1 = new Accommodation();
        accommodationTest1.setUser(userTest);
        Accommodation accommodationTest2= new Accommodation();
        accommodationTest2.setUser(userTest);
        List<Accommodation> listAccommodationTest=new ArrayList<>();
        listAccommodationTest.add(accommodationTest1);
        listAccommodationTest.add(accommodationTest2);


        when (accomodationRepository.getAccommodationByUserId(userTest.getId())).thenReturn(listAccommodationTest);
        List<Accommodation> listAccommodationResult = accommodationServiceImplement.getAccommodationByUserId(userTest.getId());

        assertNotNull(listAccommodationResult);
        assertEquals(2,listAccommodationResult.size());
    }


}
