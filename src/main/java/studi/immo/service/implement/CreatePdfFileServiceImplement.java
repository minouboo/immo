package studi.immo.service.implement;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;

@Service
public class CreatePdfFileServiceImplement {

    /*public void createPdf() {
        String path = "/Users/minhbuu/Desktop//FirstPdf1.pdf";
        try {
            String paragraphText = "Je soussigné Minh BUU propriétaire du logement désigné ci dessus, déclare avoir reçu de Robert Littler la somme de 0 euros (zéro euros), au titre du paiement du loyer et des charges pour la période de location du 1er Août 2022 au 31 Août 2022 et lui en donne quittance, sous réserve de tous mes droits.";
            Paragraph paragraph1 = new Paragraph(paragraphText);
            PdfWriter pdfWriter = new PdfWriter(path);

            PdfDocument pdfDocument = new PdfDocument(pdfWriter);
            pdfDocument.addNewPage();

            Document document = new Document(pdfDocument);
            document.add(paragraph1);

            document.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }*/

}


