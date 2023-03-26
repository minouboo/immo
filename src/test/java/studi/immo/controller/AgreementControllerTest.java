package studi.immo.controller;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.ui.Model;
import studi.immo.entity.Accommodation;
import studi.immo.entity.Agreement;
import studi.immo.entity.User;
import studi.immo.repository.AgreementRepository;
import studi.immo.repository.UserRepository;
import studi.immo.service.AgreementService;
import studi.immo.service.UserService;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class AgreementControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AgreementRepository agreementRepository;

    @Mock
    private UserService userService;

    @Mock
    private AgreementService agreementService;

    @InjectMocks
    private AgreementController agreementController;

    @Test
    void validateAgreement() {
        User tenantUserTest = new User();
        tenantUserTest.setId(1L);
        User landlordUserTest = new User();
        landlordUserTest.setId(2L);
        Set<User> listUserAgreementTest = new HashSet<>();
        listUserAgreementTest.add(tenantUserTest);
        listUserAgreementTest.add(landlordUserTest);
        Accommodation accommodationTest = new Accommodation();
        accommodationTest.setUser(landlordUserTest);
        Agreement agreementTest = new Agreement();
        agreementTest.setId(3L);
        agreementTest.setAccommodation(accommodationTest);
        agreementTest.setUsers(listUserAgreementTest);


        when(userService.getUserById(1L)).thenReturn(tenantUserTest);
        when(agreementService.getAgreementById(3L)).thenReturn(agreementTest);
        agreementController.validateAgreement(1L, new Model() {
            @Override
            public Model addAttribute(String attributeName, Object attributeValue) {
                return null;
            }

            @Override
            public Model addAttribute(Object attributeValue) {
                return null;
            }

            @Override
            public Model addAllAttributes(Collection<?> attributeValues) {
                return null;
            }

            @Override
            public Model addAllAttributes(Map<String, ?> attributes) {
                return null;
            }

            @Override
            public Model mergeAttributes(Map<String, ?> attributes) {
                return null;
            }

            @Override
            public boolean containsAttribute(String attributeName) {
                return false;
            }

            @Override
            public Object getAttribute(String attributeName) {
                return null;
            }

            @Override
            public Map<String, Object> asMap() {
                return null;
            }
        });

        assertTrue(agreementTest.getTenantValidate());


    }
}