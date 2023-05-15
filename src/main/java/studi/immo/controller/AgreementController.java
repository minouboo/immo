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
    private PaymentRequestService paymentRequestService;

    public AgreementController(UserService userService, ChatRoomService chatRoomService, AgreementService agreementService, ApartmentInventoryService apartmentInventoryService, CommentInventoryService commentInventoryService, PaymentRequestService paymentRequestService) {
        this.userService = userService;
        this.chatRoomService = chatRoomService;
        this.agreementService = agreementService;
        this.apartmentInventoryService = apartmentInventoryService;
        this.commentInventoryService = commentInventoryService;
        this.paymentRequestService = paymentRequestService;
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
        ApartmentInventory newApartmentInventoryEntry = new ApartmentInventory();
        newApartmentInventoryEntry.setAgreement(newAgreement);
        newApartmentInventoryEntry.setInventoryType(InventoryType.ENTRY);
        apartmentInventoryService.saveApartmentInventory(newApartmentInventoryEntry);
        ApartmentInventory newApartmentInventoryExit = new ApartmentInventory();
        newApartmentInventoryExit.setAgreement(newAgreement);
        newApartmentInventoryExit.setInventoryType(InventoryType.EXIT);
        apartmentInventoryService.saveApartmentInventory(newApartmentInventoryExit);
        return "redirect:/contrat/mon-contrat/"+newAgreement.getId();

    }

    @GetMapping (value = "/mon-contrat/{id}")
    public String validateAgreement (@PathVariable Long id, Model model){
        User targetUser = userService.getCurrentUser();
        if (targetUser == null){
            return "redirect:/login";
        }
        if (id == null){
            return "Erreur";
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

        boolean CanValidate = true;
        if (validatingAgreement.getEntryDate() == null || apartmentInventory.getDateInventory() == null){
            CanValidate = false;
        }
        model.addAttribute("CanValidate", CanValidate);

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
    public String pageApartmentInventoryEntry (@PathVariable Long id, Model model){
        ApartmentInventory apartmentInventory = apartmentInventoryService.getApartmentInventoryById(id);
        List<CommentInventory> commentInventories = commentInventoryService.getCommentInventoryByApartmentId(id);
        model.addAttribute("CommentInventory", commentInventories);
        model.addAttribute("Inventory",apartmentInventory);
        User currentUser = userService.getCurrentUser();
        if (currentUser.getRoles().contains(Role.ADMIN) || apartmentInventory.getAgreement().getAccommodation().getUser() == currentUser)
        {
            return "ModifyApartmentInventoryEntry";
        }
        return "Erreur";
    }

    @PostMapping (value = "/valider-etat-des-lieux-entree/{id}")
    public String saveApartmentInventoryEntry (@PathVariable Long id, @ModelAttribute("Inventory")ApartmentInventory apartmentInventory){
        ApartmentInventory updateApartmentInventory = apartmentInventoryService.getApartmentInventoryById(id);
        Agreement currentAgreement = updateApartmentInventory.getAgreement();
        updateApartmentInventory.setDateInventory(apartmentInventory.getDateInventory());
        updateApartmentInventory.setInventoryType(InventoryType.ENTRY);
        User currentUser = userService.getCurrentUser();
        if (currentUser.getRoles().contains(Role.ADMIN) || updateApartmentInventory.getAgreement().getAccommodation().getUser() == currentUser)
        {
            apartmentInventoryService.saveApartmentInventory(updateApartmentInventory);
            return "redirect:/contrat/mon-contrat/"+currentAgreement.getId();
        }

        return "Erreur";

    }

    @GetMapping (value = "/creer-piece/{id}")
    public String pageCommentInventory (@PathVariable Long id, Model model){
        CommentInventory newCommentInventory = new CommentInventory();
        model.addAttribute("NewComment", newCommentInventory);
        ApartmentInventory currentApartmentInventory = apartmentInventoryService.getApartmentInventoryById(id);
        model.addAttribute("ApartmentInventory", currentApartmentInventory);
        boolean isInventoryEntry = true;
        if(currentApartmentInventory.getInventoryType().equals(InventoryType.EXIT)){
            isInventoryEntry = false;
        }
        model.addAttribute("IsInventoryEntry", isInventoryEntry);
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
        model.addAttribute("Agreement", currentAgreement);
        ApartmentInventory currentApartmentInventory = apartmentInventoryService.getApartmentInventoryByAgreementId(id);
        model.addAttribute("ApartmentInventory", currentApartmentInventory);
        ApartmentInventory apartmentInventoryExit = apartmentInventoryService.getApartmentInventoryExitByAgreementId(id);
        model.addAttribute("InventoryExit", apartmentInventoryExit);
        List<PaymentRequest> listPaymentRequest = paymentRequestService.getPaymentRequestsByAgreementId(currentAgreement.getId());

        boolean agreementValidated = false;
        if (currentAgreement.getTenantValidate().equals(Boolean.TRUE) & currentAgreement.getLandlordValidate().equals(Boolean.TRUE))
        {
            agreementValidated = true;
        }
        model.addAttribute("AgreementValidated", agreementValidated);

        boolean canTerminated = true;
        for (PaymentRequest paymentRequest : listPaymentRequest){
            if (paymentRequest.getTenantPaid().equals(Boolean.FALSE)){
                canTerminated = false;
            }
        }
        if (apartmentInventoryExit.getDateInventory() == null){
            canTerminated = false;
        }
        model.addAttribute("CanTerminated", canTerminated);

        boolean isTerminated = true;
        if (currentAgreement.getIsTerminated().equals(Boolean.FALSE)){
            isTerminated = false;
        }
        model.addAttribute("IsTerminated", isTerminated);

        User currentUser = userService.getCurrentUser();
        boolean isOwnerOrAdmin = false;
        if (currentUser.getRoles().contains(Role.ADMIN) || currentAgreement.getAccommodation().getUser().equals(currentUser)){
            isOwnerOrAdmin = true;
        }
        model.addAttribute("IsOwnerOrAdmin", isOwnerOrAdmin);
        if (currentUser.getRoles().contains(Role.ADMIN) || currentAgreement.getUsers().contains(currentUser))
        {
            return "DetailsAgreement";
        }

        return"Erreur";

    }

    @GetMapping (value = "/voir-etat-des-lieux/{id}")
    public String checkApartmentInventory (@PathVariable Long id, Model model){
        model.addAttribute("Title","État des lieux d'entrée");
        ApartmentInventory currentApartmentInventory = apartmentInventoryService.getApartmentInventoryById(id);
        model.addAttribute("ApartmentInventory", currentApartmentInventory);
        Agreement currentAgreement = currentApartmentInventory.getAgreement();
        model.addAttribute("Agreement", currentAgreement);
        List<CommentInventory> commentsCurrentInventory = commentInventoryService.getCommentInventoryByApartmentId(currentApartmentInventory.getId());
        model.addAttribute("CommentInventory", commentsCurrentInventory);
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
        model.addAttribute("Title","Contrats à valider");
        boolean isActive = true;
        model.addAttribute("ButtonValidating", isActive);
        model.addAttribute("ButtonValidated", !isActive);
        model.addAttribute("ButtonTerminated", !isActive);
        List<Agreement> myAgreements = agreementService.getMyAgreementsByUserId(currentUser.getId());
        model.addAttribute("MyAgreements",myAgreements);

        return "ListAgreements";
    }

    @GetMapping (value = "/mes-contrats-valides")
    public String myAgreementsValidated (Model model){
        User currentUser = userService.getCurrentUser();
        if (currentUser == null){
            return "redirect:/login";
        }
        model.addAttribute("Title","Contrats en cours");
        boolean isActive = true;
        model.addAttribute("ButtonValidated", isActive);
        model.addAttribute("ButtonValidating", !isActive);
        model.addAttribute("ButtonTerminated", !isActive);
        List<Agreement> myAgreementsValidated = agreementService.getMyAgreementsValidatedByUserId(currentUser.getId());
        model.addAttribute("MyAgreements", myAgreementsValidated);

        return "ListAgreements";
    }

    @GetMapping (value = "/mes-contrats-termines")
    public String myAgreementsTerminated (Model model){
        User currentUser = userService.getCurrentUser();
        if (currentUser == null){
            return "redirect:/login";
        }
        model.addAttribute("Title","Contrats terminés");
        boolean isActive = true;
        model.addAttribute("ButtonTerminated", isActive);
        model.addAttribute("ButtonValidating", !isActive);
        model.addAttribute("ButtonValidated", !isActive);
        List<Agreement> myAgreementsTerminated = agreementService.getAllAgreementsTerminatedByUserId(currentUser.getId());
        model.addAttribute("MyAgreements", myAgreementsTerminated);

        return "ListAgreements";
    }

    @GetMapping (value = "/supprimer-contrat/{id}")
    public String deleteAgreement (@PathVariable Long id){
        User currentUser = userService.getCurrentUser();
        Agreement deleteAgreement = agreementService.getAgreementById(id);
        List<PaymentRequest> listPaymentDeleted = paymentRequestService.getPaymentRequestsByAgreementId(deleteAgreement.getId());
        for (PaymentRequest paymentRequestDelete : listPaymentDeleted){
            paymentRequestService.deletePaymentRequest(paymentRequestDelete.getId());
        }
        agreementService.deleteAgreementById(id);
        if(currentUser.getRoles().contains(Role.ADMIN)){
            return "redirect:/admin/les-contrats/"+deleteAgreement.getAccommodation().getId();
        }
        return "redirect:/contrat/mes-contrats";
    }

    @PostMapping (value = "/valider-contrat/{id}")
    public String validateAgreement (@PathVariable Long id, @ModelAttribute ("AgreementValidation")Agreement agreement){
        User currentUser = userService.getCurrentUser();
        if (currentUser == null){
            return "redirect:/login";
        }
        Agreement validateAgreement = agreementService.getAgreementById(id);
        ApartmentInventory validateInventory = apartmentInventoryService.getApartmentInventoryByAgreementId(validateAgreement.getId());
        if (currentUser.getId().equals(validateAgreement.getAccommodation().getUser().getId())){
            validateAgreement.setLandlordValidate(Boolean.TRUE);
            validateInventory.setLandlordValidate(Boolean.TRUE);
        } else {
            validateAgreement.setTenantValidate(Boolean.TRUE);
            validateInventory.setTenantValidate(Boolean.TRUE);
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

    @GetMapping (value = "/etat-des-lieux-sortie/{id}")
    public String pageApartmentInventoryExit (@PathVariable Long id, Model model){
        ApartmentInventory apartmentInventory = apartmentInventoryService.getApartmentInventoryById(id);
        List<CommentInventory> commentInventories = commentInventoryService.getCommentInventoryByApartmentId(id);
        model.addAttribute("CommentInventory", commentInventories);
        model.addAttribute("Inventory",apartmentInventory);
        User currentUser = userService.getCurrentUser();
        if (currentUser.getRoles().contains(Role.ADMIN) || apartmentInventory.getAgreement().getAccommodation().getUser() == currentUser)
        {
            return "ModifyApartmentInventoryExit";
        }
        return "Erreur";
    }

    @PostMapping (value = "/valider-etat-des-lieux-sortie/{id}")
    public String saveApartmentInventoryExit (@PathVariable Long id, @ModelAttribute("Inventory")ApartmentInventory apartmentInventory){
        ApartmentInventory apartmentInventoryExit = apartmentInventoryService.getApartmentInventoryById(id);
        Agreement currentAgreement = apartmentInventoryService.getApartmentInventoryById(id).getAgreement();
        apartmentInventoryExit.setInventoryType(InventoryType.EXIT);
        apartmentInventoryExit.setAgreement(currentAgreement);
        apartmentInventoryExit.setDateInventory(apartmentInventory.getDateInventory());
        User currentUser = userService.getCurrentUser();
        if (currentUser.getRoles().contains(Role.ADMIN) || apartmentInventoryExit.getAgreement().getAccommodation().getUser() == currentUser)
        {
            apartmentInventoryService.saveApartmentInventory(apartmentInventoryExit);
            return "redirect:/paiement/mon-contrat/"+apartmentInventoryExit.getAgreement().getId();
        }

        return "Erreur";

    }

    @GetMapping (value = "/voir-etat-des-lieux-sortie/{id}")
    public String checkApartmentInventoryExit (@PathVariable Long id, Model model){
        model.addAttribute("Title","État des lieux de sortie");
        ApartmentInventory currentApartmentInventory = apartmentInventoryService.getApartmentInventoryById(id);
        model.addAttribute("ApartmentInventory", currentApartmentInventory);
        Agreement currentAgreement = currentApartmentInventory.getAgreement();
        model.addAttribute("Agreement", currentAgreement);
        List<CommentInventory> commentsCurrentInventory = commentInventoryService.getCommentInventoryByApartmentId(currentApartmentInventory.getId());
        model.addAttribute("CommentInventory", commentsCurrentInventory);
        User currentUser = userService.getCurrentUser();
        if (currentUser.getRoles().contains(Role.ADMIN) || currentApartmentInventory.getAgreement().getUsers().contains(currentUser))
        {
            return "DetailsApartmentInventory";
        }

        return "Erreur";

    }

    @PostMapping (value = "/terminer-contrat/{id}")
    public String agreementTerminated (@PathVariable Long id, @ModelAttribute Agreement agreement){
        Agreement terminatedAgreement = agreementService.getAgreementById(id);
        List<PaymentRequest> listPaymentRequest = paymentRequestService.getPaymentRequestsByAgreementId(id);
        for (PaymentRequest paymentRequest:listPaymentRequest){
            if (paymentRequest.getTenantPaid().equals(Boolean.FALSE)){
                return "redirect:/paiement/mon-contrat/"+terminatedAgreement.getId();
            }
        }
        terminatedAgreement.setIsTerminated(Boolean.TRUE);
        agreementService.saveAgreement(terminatedAgreement);
        return "redirect:/contrat/mes-contrats-termines";
    }

    @PostMapping (value = "/activer-contrat/{id}")
    public String agreementActivated (@PathVariable Long id, @ModelAttribute Agreement agreement){
        Agreement terminatedAgreement = agreementService.getAgreementById(id);
        terminatedAgreement.setIsTerminated(Boolean.FALSE);
        agreementService.saveAgreement(terminatedAgreement);
        return "redirect:/contrat/mes-contrats-valides";
    }



}
