package studi.immo.Service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import studi.immo.entity.Accommodation;
import studi.immo.entity.Agreement;
import studi.immo.entity.User;
import studi.immo.repository.AgreementRepository;
import studi.immo.service.implement.AgreementServiceImplement;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AgreementServiceTest {

    @Mock
    AgreementRepository agreementRepository;

    @InjectMocks
    AgreementServiceImplement agreementServiceImplement;

    @Test
    public void testGetMyAgreementsByUserId(){
        User tenantUserTest = new User();
        tenantUserTest.setUserName("Tenant");

        User landlordUserTest1 = new User();
        landlordUserTest1.setUserName("Landlord1");

        User landlordUserTest2 = new User();
        landlordUserTest1.setUserName("Landlord2");

        Set<User> agreementUserTest1= new HashSet<>();
        agreementUserTest1.add(tenantUserTest);
        agreementUserTest1.add(landlordUserTest1);

        Set<User> agreementUserTest2= new HashSet<>();
        agreementUserTest2.add(tenantUserTest);
        agreementUserTest2.add(landlordUserTest2);

        Agreement agreementTest1 = new Agreement();
        agreementTest1.setUsers(agreementUserTest1);
        agreementTest1.setTenantValidate(Boolean.TRUE);
        agreementTest1.setLandlordValidate(Boolean.FALSE);

        Agreement agreementTest2 = new Agreement();
        agreementTest2.setUsers(agreementUserTest2);
        agreementTest2.setTenantValidate(Boolean.TRUE);
        agreementTest2.setLandlordValidate(Boolean.TRUE);

        List<Agreement> userTenantAgreement = new ArrayList<>();
        userTenantAgreement.add(agreementTest1);

        when(agreementRepository.getMyAgreementsByUserId(tenantUserTest.getId())).thenReturn(userTenantAgreement);
        List<Agreement> listAgreementResult = agreementServiceImplement.getMyAgreementsByUserId(tenantUserTest.getId());

        assertNotNull(listAgreementResult);
        assertEquals(1,listAgreementResult.size());
        assertFalse(agreementTest1.getLandlordValidate());
    }



    @Test
    public void testGetMyAgreementsValidatedByUserId(){
        User tenantUserTest = new User();
        tenantUserTest.setUserName("Tenant");

        User landlordUserTest1 = new User();
        landlordUserTest1.setUserName("Landlord1");

        User landlordUserTest2 = new User();
        landlordUserTest1.setUserName("Landlord2");

        Set<User> agreementUserTest1= new HashSet<>();
        agreementUserTest1.add(tenantUserTest);
        agreementUserTest1.add(landlordUserTest1);

        Set<User> agreementUserTest2= new HashSet<>();
        agreementUserTest2.add(tenantUserTest);
        agreementUserTest2.add(landlordUserTest2);

        Agreement agreementTest1 = new Agreement();
        agreementTest1.setUsers(agreementUserTest1);
        agreementTest1.setTenantValidate(Boolean.TRUE);
        agreementTest1.setLandlordValidate(Boolean.FALSE);

        Agreement agreementTest2 = new Agreement();
        agreementTest2.setUsers(agreementUserTest2);
        agreementTest2.setTenantValidate(Boolean.TRUE);
        agreementTest2.setLandlordValidate(Boolean.TRUE);

        List<Agreement> userTenantAgreement = new ArrayList<>();
        userTenantAgreement.add(agreementTest2);

        when(agreementRepository.getMyAgreementsValidatedByUserId(tenantUserTest.getId())).thenReturn(userTenantAgreement);
        List<Agreement> listAgreementResult = agreementServiceImplement.getMyAgreementsValidatedByUserId(tenantUserTest.getId());

        assertNotNull(listAgreementResult);
        assertEquals(1,listAgreementResult.size());
        assertTrue(agreementTest2.getLandlordValidate());
    }

    @Test
    public void testGetAllAgreementsTerminatedByUserId () {

        User landlordUser = new User();
        landlordUser.setUserName("Landlord");

        User tenantUser1 = new User();
        tenantUser1.setUserName("Tenant1");

        User tenantUser2 = new User();
        tenantUser2.setUserName("Tenant2");

        Set<User> agreementUsers1 = new HashSet<>();
        agreementUsers1.add(landlordUser);
        agreementUsers1.add(tenantUser1);

        Set<User> agreementUsers2 = new HashSet<>();
        agreementUsers2.add(landlordUser);
        agreementUsers2.add(tenantUser2);

        Agreement agreementTest1 = new Agreement();
        agreementTest1.setUsers(agreementUsers1);
        agreementTest1.setIsTerminated(Boolean.FALSE);

        Agreement agreementTest2 = new Agreement();
        agreementTest2.setUsers(agreementUsers2);
        agreementTest2.setIsTerminated(Boolean.TRUE);

        List<Agreement> userLandlordAgreement = new ArrayList<>();
        userLandlordAgreement.add(agreementTest2);

        when(agreementRepository.getAllAgreementsTerminatedByUserId(landlordUser.getId())).thenReturn(userLandlordAgreement);
        List<Agreement> listAgreementResultTerminated = agreementServiceImplement.getAllAgreementsTerminatedByUserId(landlordUser.getId());

        assertNotNull(listAgreementResultTerminated);
        assertEquals(1,listAgreementResultTerminated.size());
        assertTrue(agreementTest2.getIsTerminated());

    }

    @Test
    public void testgetAllAgreementTerminatedByAccommodationById(){

        Accommodation accommodationTest = new Accommodation();
        accommodationTest.setId(1L);

        Agreement agreementWaiting = new Agreement();
        agreementWaiting.setAccommodation(accommodationTest);
        agreementWaiting.setTenantValidate(Boolean.TRUE);
        agreementWaiting.setLandlordValidate(Boolean.FALSE);

        Agreement agreementValidated = new Agreement();
        agreementValidated.setAccommodation(accommodationTest);
        agreementValidated.setLandlordValidate(Boolean.TRUE);
        agreementValidated.setTenantValidate(Boolean.TRUE);

        Agreement agreementTerminated = new Agreement();
        agreementTerminated.setAccommodation(accommodationTest);
        agreementTerminated.setIsTerminated(Boolean.TRUE);

        List<Agreement> listAgreementTerminated = new ArrayList<>();
        listAgreementTerminated.add(agreementTerminated);

        when(agreementRepository.getAllAgreementTerminatedByAccommodationById(1L).thenReturn(ListAgreementTerminated));
        List<Agreement> ListAgreementTerminatedResult = agreementServiceImplement.getAllAgreementTerminatedByAccommodationById(1L);

        assertNotNull(ListAgreementTerminatedResult);
        assertEquals(1,listAgreementTerminated.size());
        assertTrue(agreementTerminated.getIsTerminated());



    }

}
