package studi.immo.controller;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import studi.immo.entity.Accommodation;
import studi.immo.entity.PaymentRequest;
import studi.immo.entity.User;
import studi.immo.service.AccommodationService;
import studi.immo.service.PaymentRequestService;
import studi.immo.service.implement.CreatePdfFileServiceImplement;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Controller
@PreAuthorize("isAuthenticated()")
@RequestMapping("/pdf")
public class PdfController {

    @Autowired
    ServletContext servletContext;

    private CreatePdfFileServiceImplement createPdfFileServiceImplement;
    private AccommodationService accommodationService;
    private final TemplateEngine templateEngine;
    private PaymentRequestService paymentRequestService;

    @Autowired
    public PdfController(CreatePdfFileServiceImplement createPdfFileServiceImplement, AccommodationService accommodationService, TemplateEngine templateEngine, PaymentRequestService paymentRequestService) {
        this.createPdfFileServiceImplement = createPdfFileServiceImplement;
        this.accommodationService = accommodationService;
        this.templateEngine = templateEngine;
        this.paymentRequestService = paymentRequestService;

    }

    @GetMapping(value = "/voir/{id}")
    public String getPdf(@PathVariable Long id, Model model){
        PaymentRequest pdfPayment = paymentRequestService.getPaymentRequestById(id);
        User landlordUser = pdfPayment.getAgreement().getAccommodation().getUser();

        model.addAttribute("Payment", pdfPayment);
        return "PdfPayment";
    }

    /*@GetMapping (value = "/create")
    public String createPdf() throws IOException {
        HtmlConverter.convertToPdf(new File("./PdfPayment.html"),new File("demo-html.pdf"));
        return "redirect:/pdf/get";
    }*/

    @RequestMapping (path = "/telecharger/{id}")
    public ResponseEntity<?> createPDF (@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) throws IOException{

        PaymentRequest pdfPayment = paymentRequestService.getPaymentRequestById(id);

        WebContext context = new WebContext(request, response, servletContext);
        context.setVariable("Payment",pdfPayment);
        String accommodationHtml = templateEngine.process("PdfPayment",context);

        ByteArrayOutputStream target = new ByteArrayOutputStream();

        ConverterProperties converterProperties = new ConverterProperties();
        converterProperties.setBaseUri("http://localhost:3333");

        HtmlConverter.convertToPdf(accommodationHtml,target,converterProperties);

        byte[] bytes = target.toByteArray();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=order.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(bytes);

    }




}
