import javax.swing.text.Document;
import java.awt.*;
import java.io.FileOutputStream;
import java.io.IOException;

public class CreatePdf {
    int fontSize = 14;
    Font f = FontFactory.getFont(FontFactory.HELVETICA_BOLD, fontSize,
            BaseColor.BLACK);
    public void createPdf() throws IOException {
        // Create a new PDF document
        Document pdfD = new Document();
        // Initialize a writer instance
        try {
            PdfWriter.getInstance(pdfD, new FileOutputStream(PDF_FILE));
        }
        catch(IOException | DocumentException e){
            e.printStackTrace();
        }
        pdfD.open();

    }

}
