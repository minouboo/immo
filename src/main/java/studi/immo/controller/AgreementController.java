package studi.immo.controller;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.java.Log;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import studi.immo.entity.*;
import studi.immo.form.AgreementForm;
import studi.immo.service.AgreementService;
import studi.immo.service.ApartmentInventoryService;
import studi.immo.service.ChatRoomService;
import studi.immo.service.UserService;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Log
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Controller
@PreAuthorize("isAuthenticated()")
@RequestMapping(value="/contrat")
public class AgreementController {

    private UserService userService;
    private ChatRoomService chatRoomService;
    private AgreementService agreementService;
    private ApartmentInventoryService apartmentInventoryService;

    public AgreementController(UserService userService, ChatRoomService chatRoomService, AgreementService agreementService, ApartmentInventoryService apartmentInventoryService) {
        this.userService = userService;
        this.chatRoomService = chatRoomService;
        this.agreementService = agreementService;
        this.apartmentInventoryService = apartmentInventoryService;
    }

    @GetMapping(value = "/creation-contrat/{id}")
    public String pageNewAgreement(@PathVariable Long id, Model model){
        ChatRoom agreementChatRoom = chatRoomService.getChatRoomById(id);
        model.addAttribute("ChatRoom",agreementChatRoom);
        AgreementForm agreementForm = new AgreementForm();
        model.addAttribute("Agreement", agreementForm);
        return "CreateAgreement";
    }

    @PostMapping(value = "/nouveau-contrat/{id}")
    public String newAgreement (@PathVariable Long id, @ModelAttribute("Agreement") AgreementForm agreementForm){
        Accommodation agreementAccommodation = chatRoomService.getChatRoomById(id).getAccommodation();
        Set<User> agreementUser = chatRoomService.getChatRoomById(id).getUsers();
        Agreement newAgreement = new Agreement();
        newAgreement.setAccommodation(agreementAccommodation);
        newAgreement.getUsers().addAll(agreementUser);
        newAgreement.setRentalPrice(agreementForm.getRentalPrice());
        newAgreement.setCharges(agreementForm.getCharges());
        newAgreement.setDeposit(agreementForm.getDeposit());
        newAgreement.setEntryDate(agreementForm.getStartAgreementDate());
        newAgreement.setAgencyFees(agreementForm.getAgencyFees());
        agreementService.saveAgreement(newAgreement);
        ApartmentInventory newApartmentInventory = new ApartmentInventory();
        newApartmentInventory.setAgreement(newAgreement);
        newApartmentInventory.setType(Collections.singleton(InventoryType.ENTRY));
        apartmentInventoryService.saveApartmentInventory(newApartmentInventory);
        return "redirect:/contrat/mon-contrat/"+newAgreement.getId();

    }

    @GetMapping (value = "/mon-contrat/{id}")
    public String validateAgreement (@PathVariable Long id, Model model){
        Agreement validatingAgreement = agreementService.getAgreementById(id);
        model.addAttribute("Agreement", validatingAgreement);
        User targetUser = userService.getCurrentUser();
        boolean IsOwner = false;
        if (targetUser!=null){
            IsOwner = targetUser.getId().equals(validatingAgreement.getAccommodation().getUser().getId());
        }
        model.addAttribute("IsOwner", IsOwner);
        ApartmentInventory apartmentInventory = apartmentInventoryService.getApartmentInventoryByAgreementId(id);
        model.addAttribute("Inventory", apartmentInventory);


        return "ValidateAgreement";
    }

    @GetMapping (value = "/modifier-contrat/{id}")
    public String modifyAgreement (@PathVariable Long id, Model model){
        Agreement currentAgreement = agreementService.getAgreementById(id);
        model.addAttribute("CurrentAgreement",currentAgreement);
        return "ModifyAgreement";
    }

    @PostMapping (value = "contrat-modifie/{id}")
    public String updateAgreement (@PathVariable Long id, @ModelAttribute("CurrentAgreement")Agreement agreement){
        Agreement currentAgreement = agreementService.getAgreementById(id);
        currentAgreement.setRentalPrice(agreement.getRentalPrice());
        currentAgreement.setCharges(agreement.getCharges());
        currentAgreement.setDeposit(agreement.getDeposit());
        currentAgreement.setEntryDate(agreement.getEntryDate());
        currentAgreement.setAgencyFees(agreement.getAgencyFees());
        agreementService.saveAgreement(currentAgreement);
        return "redirect:/contrat/mon-contrat/"+currentAgreement.getId();
    }

    @GetMapping (value = "/etat-des-lieux-entree/{id}")
    public String pageApartmentInventory (@PathVariable Long id, Model model){
        ApartmentInventory apartmentInventory = apartmentInventoryService.getApartmentInventoryById(id);
        model.addAttribute("Inventory",apartmentInventory);
        return "ModifyApartmentInventory";
    }

    @PostMapping (value = "/valider-etat-des-lieux-entree/{id}")
    public String saveApartmentInventory (@PathVariable Long id, @ModelAttribute("Inventory")ApartmentInventory apartmentInventory){
        ApartmentInventory updateApartmentInventory = apartmentInventoryService.getApartmentInventoryById(id);
        updateApartmentInventory.setDateInventory(apartmentInventory.getDateInventory());
        updateApartmentInventory.setComment(apartmentInventory.getComment());
        apartmentInventoryService.saveApartmentInventory(updateApartmentInventory);
        return "redirect:/contrat/mon-contrat/id="+updateApartmentInventory.getAgreement().getId();
    }


    @GetMapping (value = "/mes-contrats")
    public String myAgreements (Model model){
        User currentUser = userService.getCurrentUser();
        List<Agreement> myAgreements = agreementService.getMyAgreementsByUserId(currentUser.getId());
        model.addAttribute("MyAgreements",myAgreements);
        return "MyAgreements";
    }

    @PostMapping (value = "/valider-contrat/{id}")
    public String validateAgreement (@PathVariable Long id){
        Agreement validateAgreement = agreementService.getAgreementById(id);
        validateAgreement.setTenantValidate(Boolean.TRUE);
        agreementService.saveAgreement(validateAgreement);
        return "redirect:/contrat/mon-contrat/"+validateAgreement.getId();
    }

    @GetMapping (value = "/supprimer-contrat/{id}")
    public String deleteAgreement (@PathVariable Long id){
        agreementService.deleteAgreementById(id);
        return "redirect:/contrat/mon-contrat";
    }



}
