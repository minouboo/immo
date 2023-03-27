package studi.immo.Service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import studi.immo.entity.Agreement;
import studi.immo.entity.User;
import studi.immo.repository.AgreementRepository;
import studi.immo.service.implement.AgreementServiceImplement;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


import static org.junit.jupiter.api.Assertions.*;
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

        Agreement agreementTest2 = new Agreement();
        agreementTest2.setUsers(agreementUserTest2);

        List<Agreement> tenantUserAgreement1 = new ArrayList<>();
        tenantUserAgreement1.add(agreementTest1);
        tenantUserAgreement1.add(agreementTest2);


        when(agreementServiceImplement.getMyAgreementsByUserId(tenantUserTest.getId())).thenReturn(tenantUserAgreement1);
        List<Agreement> listAgreementResult = agreementServiceImplement.getMyAgreementsByUserId(tenantUserTest.getId());

        assertNotNull(listAgreementResult);
        assertEquals(2,listAgreementResult.size());
    }

}
