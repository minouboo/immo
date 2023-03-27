package studi.immo.controller;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.java.Log;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import studi.immo.entity.*;
import studi.immo.service.AgreementService;
import studi.immo.service.CashService;
import studi.immo.service.PaymentRequestService;
import studi.immo.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

@Log
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Controller
@PreAuthorize("isAuthenticated()")
@RequestMapping(value = "paiement")
public class PaymentController {

    private AgreementService agreementService;
    private PaymentRequestService paymentRequestService;
    private UserService userService;
    private CashService cashService;


    public PaymentController(AgreementService agreementService, PaymentRequestService paymentRequestService, UserService userService, CashService cashService) {
        this.agreementService = agreementService;
        this.paymentRequestService = paymentRequestService;
        this.userService = userService;
        this.cashService = cashService;
    }

    @GetMapping(value = "/mon-contrat/{id}")
    public String showPayment (@PathVariable Long id, Model model){
        User currentUser = userService.getCurrentUser();
        if (currentUser == null){
            return "redirect:/login";
        }
        Agreement currentAgreement = agreementService.getAgreementById(id);
        model.addAttribute("Agreement",currentAgreement);
        List<PaymentRequest> paymentRequestList = paymentRequestService.getPaymentRequestsByAgreementId(id);
        model.addAttribute("Payments", paymentRequestList);
        boolean IsOwner = false;
        if (currentUser != null){
            IsOwner = currentUser.getId().equals(currentAgreement.getAccommodation().getUser().getId());
        }
        model.addAttribute("IsOwner", IsOwner);

        return "ShowAgreement";
    }

    @GetMapping (value = "/nouvelle-quittance/{id}")
    public String pageNewRent (@PathVariable Long id, Model model){
        PaymentRequest newPaymentRequest = new PaymentRequest();
        Agreement currentAgreement = agreementService.getAgreementById(id);
        model.addAttribute("Agreement",currentAgreement);
        model.addAttribute("Rent", newPaymentRequest);
        return "AddPaymentRequestRent";
    }

    @PostMapping (value = "/envoi-quittance/{id}")
    public String newRent (@PathVariable Long id, @ModelAttribute("Rent") PaymentRequest paymentRequest){
        Agreement currentAgreement = agreementService.getAgreementById(id);
        PaymentRequest newPaymentRequest = new PaymentRequest();
        newPaymentRequest.setAgreement(currentAgreement);
        newPaymentRequest.setIssueDate(LocalDateTime.now());
        newPaymentRequest.setRentalPrice(paymentRequest.getRentalPrice());
        newPaymentRequest.setCharges(paymentRequest.getCharges());
        newPaymentRequest.setTotalAmount(paymentRequest.getCharges().add(paymentRequest.getRentalPrice()));
        newPaymentRequest.setComments(paymentRequest.getComments());
        newPaymentRequest.setTenantPaid(Boolean.FALSE);
        newPaymentRequest.setPaymentType(PaymentType.RENT);
        paymentRequestService.savePaymentRequest(newPaymentRequest);
        return "redirect:/paiement/mon-contrat/{id}";
    }

    @GetMapping (value = "/nouvelle-caution/{id}")
    public String pageNewDeposit (@PathVariable Long id, Model model){
        PaymentRequest newPaymentRequest = new PaymentRequest();
        Agreement currentAgreement = agreementService.getAgreementById(id);
        model.addAttribute("Agreement",currentAgreement);
        model.addAttribute("Deposit", newPaymentRequest);
        return "AddPaymentRequestDeposit";
    }

    @PostMapping (value = "/envoi-caution/{id}")
    public String newDeposit (@PathVariable Long id, @ModelAttribute("Deposit") PaymentRequest paymentRequest){
        Agreement currentAgreement = agreementService.getAgreementById(id);
        PaymentRequest newPaymentRequest = new PaymentRequest();
        newPaymentRequest.setAgreement(currentAgreement);
        newPaymentRequest.setIssueDate(LocalDateTime.now());
        newPaymentRequest.setOtherAmount(paymentRequest.getOtherAmount());
        newPaymentRequest.setTotalAmount(paymentRequest.getOtherAmount());
        newPaymentRequest.setComments(paymentRequest.getComments());
        newPaymentRequest.setTenantPaid(Boolean.FALSE);
        newPaymentRequest.setPaymentType(PaymentType.DEPOSIT);
        paymentRequestService.savePaymentRequest(newPaymentRequest);
        return "redirect:/paiement/mon-contrat/{id}";
    }

    @GetMapping (value = "/nouveau-paiement-divers/{id}")
    public String pageNewOtherPayment (@PathVariable Long id, Model model){
        PaymentRequest newPaymentRequest = new PaymentRequest();
        Agreement currentAgreement = agreementService.getAgreementById(id);
        model.addAttribute("Agreement",currentAgreement);
        model.addAttribute("Payment", newPaymentRequest);
        return "AddPaymentRequestOther";
    }

    @PostMapping (value = "/envoi-paiement-divers/{id}")
    public String newOtherPayment (@PathVariable Long id, @ModelAttribute("Payment") PaymentRequest paymentRequest){
        Agreement currentAgreement = agreementService.getAgreementById(id);
        PaymentRequest newPaymentRequest = new PaymentRequest();
        newPaymentRequest.setAgreement(currentAgreement);
        newPaymentRequest.setIssueDate(LocalDateTime.now());
        newPaymentRequest.setOtherAmount(paymentRequest.getOtherAmount());
        newPaymentRequest.setTotalAmount(paymentRequest.getOtherAmount());
        newPaymentRequest.setComments(paymentRequest.getComments());
        newPaymentRequest.setTenantPaid(Boolean.FALSE);
        newPaymentRequest.setPaymentType(PaymentType.OTHER);
        paymentRequestService.savePaymentRequest(newPaymentRequest);
        return "redirect:/paiement/mon-contrat/{id}";
    }

    /*@GetMapping (value = "modification-paiement/{id}")
    public String modifyPayment (@PathVariable Long id, @PathVariable Long id2, Model model){
        Agreement currentAgreement = agreementService.getAgreementById(id);
        PaymentRequest modifyPayment = paymentRequestService.getPaymentRequestById(id2);

        if (modifyPayment.getPaymentType().equals(PaymentType.RENT))
        {
            model.addAttribute("Agreement",currentAgreement);
            model.addAttribute("Rent", modifyPayment);
            return "AddPaymentRequestRent";
        }
        if(modifyPayment.getPaymentType().equals(PaymentType.DEPOSIT))
        {
            model.addAttribute("Agreement",currentAgreement);
            model.addAttribute("Deposit",modifyPayment);
            return "AddPaymentRequestDeposit";
        }
        model.addAttribute("Agreement",currentAgreement);
        model.addAttribute("Payment", modifyPayment);
        return "AddPaymentRequestOther";
    }*/

    @GetMapping (value = "/modifier-paiement/{id}")
    public String modifyPayment (@PathVariable Long id, Model model){
        PaymentRequest modifyPayment = paymentRequestService.getPaymentRequestById(id);
        if (modifyPayment.getPaymentType().equals(PaymentType.RENT)) {
            model.addAttribute("Rent", modifyPayment);
            return "ModifyPaymentRequestRent";
        }
        if (modifyPayment.getPaymentType().equals(PaymentType.DEPOSIT)){
            model.addAttribute("Deposit",modifyPayment);
            return "ModifyPaymentRequestDeposit";
        }
        model.addAttribute("Payment",modifyPayment);
        return "ModifyPaymentRequestOther";

    }

    @PostMapping (value = "/quittance-modifie/{id}")
    public String rentModified (@PathVariable Long id, @ModelAttribute("Rent")PaymentRequest paymentRequest){
        PaymentRequest modifyRent = paymentRequestService.getPaymentRequestById(id);
        modifyRent.setIssueDate(LocalDateTime.now());
        modifyRent.setRentalPrice(paymentRequest.getRentalPrice());
        modifyRent.setCharges(paymentRequest.getCharges());
        modifyRent.setComments(paymentRequest.getComments());
        modifyRent.setTotalAmount(paymentRequest.getCharges().add(paymentRequest.getRentalPrice()));
        paymentRequestService.savePaymentRequest(modifyRent);
        return "redirect:/paiement/mon-contrat/"+modifyRent.getAgreement().getId();
    }

    @PostMapping (value = "/caution-modifie/{id}")
    public String depositModified (@PathVariable Long id, @ModelAttribute("Deposit") PaymentRequest paymentRequest){
        PaymentRequest modifyDeposit = paymentRequestService.getPaymentRequestById(id);
        modifyDeposit.setIssueDate(LocalDateTime.now());
        modifyDeposit.setOtherAmount(paymentRequest.getOtherAmount());
        modifyDeposit.setComments(paymentRequest.getComments());
        modifyDeposit.setTotalAmount(paymentRequest.getOtherAmount());
        paymentRequestService.savePaymentRequest(modifyDeposit);
        return "redirect:/paiement/mon-contrat/"+modifyDeposit.getAgreement().getId();
    }

    @PostMapping (value = "/paiement-divers-modifie/{id}")
    public String otherPaymentModified (@PathVariable Long id, @ModelAttribute("Payment") PaymentRequest paymentRequest){
        PaymentRequest modifyOtherPayment = paymentRequestService.getPaymentRequestById(id);
        modifyOtherPayment.setIssueDate(LocalDateTime.now());
        modifyOtherPayment.setOtherAmount(paymentRequest.getOtherAmount());
        modifyOtherPayment.setComments(paymentRequest.getComments());
        modifyOtherPayment.setTotalAmount(paymentRequest.getOtherAmount());
        paymentRequestService.savePaymentRequest(modifyOtherPayment);
        return "redirect:/paiement/mon-contrat/"+modifyOtherPayment.getAgreement().getId();
    }

    @GetMapping (value = "/supprimer-paiement/{id}")
    public String deletePaymentRequest (@PathVariable Long id){
        PaymentRequest currentPayment = paymentRequestService.getPaymentRequestById(id);
        paymentRequestService.deletePaymentRequest(id);
        return "redirect:/paiement/mon-contrat/"+currentPayment.getAgreement().getId();
    }

    @GetMapping (value = "/voir-paiement/{id}")
    public String pagePayment (@PathVariable Long id, Model model){
        PaymentRequest currentPayment = paymentRequestService.getPaymentRequestById(id);
        Agreement currentAgreement = paymentRequestService.getPaymentRequestById(id).getAgreement();
        Cash tenantCash = userService.getCurrentUser().getCash();
        boolean canPay = true;
        if (tenantCash.getAmount().compareTo(currentPayment.getTotalAmount()) >= 0 ){
            canPay = false;
        }
        model.addAttribute("Payment",currentPayment);
        model.addAttribute("Agreement",currentAgreement);
        model.addAttribute("CanPay", canPay);
        return "ShowPayment";
    }

    @PostMapping (value = "/payer-paiement/{id}")
    public String payment (@PathVariable Long id, @ModelAttribute("TenantCash")Cash cash){
        PaymentRequest currentPayment = paymentRequestService.getPaymentRequestById(id);
        User currentUser = userService.getCurrentUser();
        Cash tenantCash = currentUser.getCash();
        Cash landlordCash = currentPayment.getAgreement().getAccommodation().getUser().getCash();


        if (tenantCash.getAmount().compareTo(currentPayment.getTotalAmount()) >= 0 ){
            tenantCash.setAmount(tenantCash.getAmount().subtract(currentPayment.getTotalAmount()));
            cashService.saveCash(tenantCash);
            landlordCash.setAmount(landlordCash.getAmount().add(currentPayment.getTotalAmount()));
            cashService.saveCash(landlordCash);
            currentPayment.setPaymentDate(LocalDateTime.now());
            currentPayment.setUserPayer(currentUser);
            currentPayment.setTenantPaid(Boolean.TRUE);
            paymentRequestService.savePaymentRequest(currentPayment);
            return "redirect:/paiement/mon-contrat/"+currentPayment.getAgreement().getId();
        }
        return "redirect:/paiement/mon-contrat/"+currentPayment.getAgreement().getId();
    }


}
