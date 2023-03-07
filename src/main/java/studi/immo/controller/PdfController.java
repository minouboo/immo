package studi.immo.controller;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import studi.immo.entity.Accommodation;
import studi.immo.service.AccommodationService;
import studi.immo.service.implement.CreatePdfFileServiceImplement;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Controller
@RequestMapping("/pdf")
public class PdfController {

    @Autowired
    ServletContext servletContext;

    private CreatePdfFileServiceImplement createPdfFileServiceImplement;
    private AccommodationService accommodationService;
    private final TemplateEngine templateEngine;

    @Autowired
    public PdfController(CreatePdfFileServiceImplement createPdfFileServiceImplement, AccommodationService accommodationService, TemplateEngine templateEngine) {
        this.createPdfFileServiceImplement = createPdfFileServiceImplement;
        this.accommodationService = accommodationService;
        this.templateEngine = templateEngine;
    }

    @GetMapping(value = "/get")
    public String getPdf(Model model){
        Accommodation pdfAccommodation = accommodationService.getAccommodationById(14L);
        model.addAttribute("Accommodation", pdfAccommodation);
        return "PdfAgreement";
    }

    /*@GetMapping (value = "/create")
    public String createPdf() throws IOException {
        HtmlConverter.convertToPdf(new File("./PdfAgreement.html"),new File("demo-html.pdf"));
        return "redirect:/pdf/get";
    }*/

    @RequestMapping (path = "/create")
    public ResponseEntity<?> createPDF (HttpServletRequest request, HttpServletResponse response) throws IOException{

        Accommodation pdfAccommodation = accommodationService.getAccommodationById(14L);

        WebContext context = new WebContext(request, response, servletContext);
        context.setVariable("Accommodation",pdfAccommodation);
        String accommodationHtml = templateEngine.process("PdfAgreement",context);

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
