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
import studi.immo.service.*;

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
    private CommentInventoryService commentInventoryService;

    public AgreementController(UserService userService, ChatRoomService chatRoomService, AgreementService agreementService, ApartmentInventoryService apartmentInventoryService, CommentInventoryService commentInventoryService) {
        this.userService = userService;
        this.chatRoomService = chatRoomService;
        this.agreementService = agreementService;
        this.apartmentInventoryService = apartmentInventoryService;
        this.commentInventoryService = commentInventoryService;
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
        newApartmentInventory.setInventoryType(InventoryType.ENTRY);
        apartmentInventoryService.saveApartmentInventory(newApartmentInventory);
        return "redirect:/contrat/mon-contrat/"+newAgreement.getId();

    }

    @GetMapping (value = "/mon-contrat/{id}")
    public String validateAgreement (@PathVariable Long id, Model model){
        User targetUser = userService.getCurrentUser();
        if (targetUser == null){
            return "redirect:/login";
        }

        Agreement validatingAgreement = agreementService.getAgreementById(id);
        model.addAttribute("Agreement", validatingAgreement);

        ApartmentInventory apartmentInventory = apartmentInventoryService.getApartmentInventoryByAgreementId(id);
        model.addAttribute("Inventory", apartmentInventory);

        boolean IsOwner = false;
        if (targetUser!=null){
            IsOwner = targetUser.getId().equals(validatingAgreement.getAccommodation().getUser().getId());
        }
        model.addAttribute("IsOwner", IsOwner);

        boolean landlordValidate = validatingAgreement.getLandlordValidate().equals(Boolean.TRUE);
        boolean tenantValidate = validatingAgreement.getTenantValidate().equals(Boolean.TRUE);

        boolean IsTenantValidated = false;
        if (tenantValidate){
            IsTenantValidated = true;
        }
        model.addAttribute("IsTenantValidated", IsTenantValidated);

        boolean IsLandLordValidated = false;
        if (landlordValidate){
            IsLandLordValidated = true;
        }
        model.addAttribute("IsLandLordValidated",IsLandLordValidated);

        boolean IsValidated = false;
        if (landlordValidate & tenantValidate ){
            IsValidated = true;
        }
        model.addAttribute("IsValidated",IsValidated);

        if (targetUser.getRoles().contains(Role.ADMIN) || validatingAgreement.getUsers().contains(targetUser))
        {
            return "ValidateAgreement";
        }
        else {
            return "Erreur";
        }



    }

    @GetMapping (value = "/modifier-contrat/{id}")
    public String modifyAgreement (@PathVariable Long id, Model model){
        User currentUser = userService.getCurrentUser();
        Agreement currentAgreement = agreementService.getAgreementById(id);
        model.addAttribute("CurrentAgreement",currentAgreement);
        if(currentUser.getRoles().contains(Role.ADMIN) || currentAgreement.getAccommodation().getUser() == currentUser)
        {
            return "ModifyAgreement";
        }
        return "Erreur";
    }

    @PostMapping (value = "contrat-modifie/{id}")
    public String updateAgreement (@PathVariable Long id, @ModelAttribute("CurrentAgreement")Agreement agreement){
        User currentUser = userService.getCurrentUser();
        Agreement currentAgreement = agreementService.getAgreementById(id);
        currentAgreement.setRentalPrice(agreement.getRentalPrice());
        currentAgreement.setCharges(agreement.getCharges());
        currentAgreement.setDeposit(agreement.getDeposit());
        currentAgreement.setEntryDate(agreement.getEntryDate());
        currentAgreement.setAgencyFees(agreement.getAgencyFees());
        if (currentUser.getRoles().contains(Role.ADMIN) || currentAgreement.getAccommodation().getUser() == currentUser)
        {
            agreementService.saveAgreement(currentAgreement);
            return "redirect:/contrat/mon-contrat/"+currentAgreement.getId();
        }
        return "Erreur";
    }

    @GetMapping (value = "/etat-des-lieux-entree/{id}")
    public String pageApartmentInventory (@PathVariable Long id, Model model){
        ApartmentInventory apartmentInventory = apartmentInventoryService.getApartmentInventoryById(id);
        List<CommentInventory> commentInventories = commentInventoryService.getCommentInventoryByApartmentId(id);
        model.addAttribute("CommentInventory", commentInventories);
        model.addAttribute("Inventory",apartmentInventory);
        User currentUser = userService.getCurrentUser();
        if (currentUser.getRoles().contains(Role.ADMIN) || apartmentInventory.getAgreement().getAccommodation().getUser() == currentUser)
        {
            return "ModifyApartmentInventory";
        }
        return "Erreur";
    }

    @PostMapping (value = "/valider-etat-des-lieux-entree/{id}")
    public String saveApartmentInventory (@PathVariable Long id, @ModelAttribute("Inventory")ApartmentInventory apartmentInventory){
        ApartmentInventory updateApartmentInventory = apartmentInventoryService.getApartmentInventoryById(id);
        updateApartmentInventory.setDateInventory(apartmentInventory.getDateInventory());
        updateApartmentInventory.setComment(apartmentInventory.getComment());
        User currentUser = userService.getCurrentUser();
        if (currentUser.getRoles().contains(Role.ADMIN) || apartmentInventory.getAgreement().getAccommodation().getUser() == currentUser)
        {
            apartmentInventoryService.saveApartmentInventory(updateApartmentInventory);
            return "redirect:/contrat/mon-contrat/"+updateApartmentInventory.getAgreement().getId();
        }

        return "Erreur";

    }

    @GetMapping (value = "/creer-piece/{id}")
    public String pageCommentInventory (@PathVariable Long id, Model model){
        CommentInventory newCommentInventory = new CommentInventory();
        model.addAttribute("NewComment", newCommentInventory);
        ApartmentInventory currentApartmentInventory = apartmentInventoryService.getApartmentInventoryById(id);
        model.addAttribute("ApartmentInventory", currentApartmentInventory);
        User currentUser = userService.getCurrentUser();
        if (currentUser.getRoles().contains(Role.ADMIN) || currentApartmentInventory.getAgreement().getAccommodation().getUser() == currentUser)
        {
            return "AddCommentInventory";
        }
        return "Erreur";

    }

    @PostMapping (value = "/piece-cree/{id}")
    public String createCommentInventory (@PathVariable Long id , @ModelAttribute("Comment")CommentInventory commentInventory){
        ApartmentInventory currentApartmentInventory = apartmentInventoryService.getApartmentInventoryById(id);
        CommentInventory newCommentInventory = new CommentInventory();
        newCommentInventory.setTitle(commentInventory.getTitle());
        newCommentInventory.setDescription(commentInventory.getDescription());
        newCommentInventory.setApartmentInventory(currentApartmentInventory);
        User currentUser = userService.getCurrentUser();
        if (currentUser.getRoles().contains(Role.ADMIN) || currentApartmentInventory.getAgreement().getAccommodation().getUser() == currentUser)
        {
            commentInventoryService.saveCommentInventory(newCommentInventory);
            return "redirect:/contrat/creer-piece/"+currentApartmentInventory.getId();
        }

        return "Erreur";
    }

    @GetMapping (value ="/supprimer-piece/{id}")
    public String deleteComment (@PathVariable Long id){
        ApartmentInventory apartmentInventory = commentInventoryService.getCommentInventoryById(id).getApartmentInventory();
        User currentUser = userService.getCurrentUser();
        if (currentUser.getRoles().contains(Role.ADMIN) || apartmentInventory.getAgreement().getAccommodation().getUser() == currentUser)
        {
            commentInventoryService.deleteCommentInventoryById(id);
            return "redirect:/contrat/etat-des-lieux-entree/"+apartmentInventory.getId();
        }
        return "Erreur";

    }

    @GetMapping (value = "/voir-contrat/{id}")
    public String checkAgreement (@PathVariable Long id, Model model){
        Agreement currentAgreement = agreementService.getAgreementById(id);
        ApartmentInventory currentApartmentInventory = apartmentInventoryService.getApartmentInventoryByAgreementId(id);
        model.addAttribute("Agreement", currentAgreement);
        model.addAttribute("ApartmentInventory", currentApartmentInventory);
        boolean agreementValidated = false;
        if (currentAgreement.getTenantValidate().equals(Boolean.TRUE) & currentAgreement.getLandlordValidate().equals(Boolean.TRUE))
        {
            agreementValidated = true;
        }
        model.addAttribute("AgreementValidated", agreementValidated);
        User currentUser = userService.getCurrentUser();
        if (currentUser.getRoles().contains(Role.ADMIN) || currentAgreement.getUsers().contains(currentUser))
        {
            return "DetailsAgreement";
        }

        return"Erreur";

    }

    @GetMapping (value = "/voir-etat-des-lieux/{id}")
    public String checkApartmentInventory (@PathVariable Long id, Model model){
        ApartmentInventory currentApartmentInventory = apartmentInventoryService.getApartmentInventoryById(id);
        Agreement currentAgreement = currentApartmentInventory.getAgreement();
        model.addAttribute("ApartmentInventory", currentApartmentInventory);
        model.addAttribute("Agreement", currentAgreement);
        User currentUser = userService.getCurrentUser();
        if (currentUser.getRoles().contains(Role.ADMIN) || currentApartmentInventory.getAgreement().getUsers().contains(currentUser))
        {
            return "DetailsApartmentInventory";
        }

        return "Erreur";

    }


    @GetMapping (value = "/mes-contrats")
    public String myAgreements (Model model){
        User currentUser = userService.getCurrentUser();
        if (currentUser == null){
            return "redirect:/login";
        }
        List<Agreement> myAgreements = agreementService.getMyAgreementsByUserId(currentUser.getId());
        List<Agreement> myAgreementsValidated = agreementService.getMyAgreementsValidatedByUserId(currentUser.getId());
        model.addAttribute("MyAgreements",myAgreements);
        model.addAttribute("MyAgreementsValidated", myAgreementsValidated);
        return "ListAgreements";
    }

    @GetMapping (value = "/supprimer-contrat/{id}")
    public String deleteAgreement (@PathVariable Long id){
        ApartmentInventory currentApartmentInventory = apartmentInventoryService.getApartmentInventoryByAgreementId(id);
        List<CommentInventory> listCurrentComments = commentInventoryService.getCommentInventoryByApartmentId(currentApartmentInventory.getId());

        agreementService.deleteAgreementById(id);
        return "redirect:/contrat/mon-contrat";
    }

    @PostMapping (value = "/valider-contrat/{id}")
    public String validateAgreement (@PathVariable Long id, @ModelAttribute ("AgreementValidation")Agreement agreement){
        User currentUser = userService.getCurrentUser();
        if (currentUser == null){
            return "redirect:/login";
        }
        Agreement validateAgreement = agreementService.getAgreementById(id);
        if (currentUser.getId().equals(validateAgreement.getAccommodation().getUser().getId())){
            validateAgreement.setLandlordValidate(Boolean.TRUE);
        } else {
            validateAgreement.setTenantValidate(Boolean.TRUE);
        }
        agreementService.saveAgreement(validateAgreement);
        return "redirect:/contrat/mon-contrat/"+validateAgreement.getId();
    }

    @PostMapping (value = "/invalider-contrat/{id}")
    public String unvalidateAgreement (@PathVariable Long id, @ModelAttribute ("AgreementValidation")Agreement agreement){
        User currentUser = userService.getCurrentUser();
        if (currentUser == null){
            return "redirect:/login";
        }
        Agreement validateAgreement = agreementService.getAgreementById(id);
        if (currentUser.getId().equals(validateAgreement.getAccommodation().getUser().getId())){
            validateAgreement.setLandlordValidate(Boolean.FALSE);
        } else {
            validateAgreement.setTenantValidate(Boolean.FALSE);
        }
        agreementService.saveAgreement(validateAgreement);
        return "redirect:/contrat/mon-contrat/"+validateAgreement.getId();
    }







}
