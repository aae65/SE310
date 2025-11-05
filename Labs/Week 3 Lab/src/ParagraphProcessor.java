import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * This class processes an input String, allowing it to be output to the console, a
 * PDF file (saved locally as output.pdf), or to audio. It can leave the text as-is,
 * convert it to uppercase, or convert it to lowercase. It uses the iText library for
 * PDF creation and the FreeTTS library for audio output. Please note, for both the
 * iText library and the FreeTTS library you will need the library to run this code.
 * They can be added manually with jar files, with maven, or with gradle.
 */
public class ParagraphProcessor {
    private static final String PDF_FILE = "output.pdf";
    private static final int NO_CASE_CONVERSION = 0;
    private static final int CONVERT_UPPER = 1;
    private static final int CONVERT_LOWER = 2;
    private static final int CONSOLE_TEXT = 0;
    private static final int PDF = 1;
    private static final int AUDIO = 2;
    private int caseConversion;
    private int outputType;
    private String content;
    private CreatePdf createPdf = new CreatePdf();

    /**
     *
     * @param paragraph The paragraph content to be processed
     */
    public ParagraphProcessor(String paragraph){
        // Use default values for case conversion and output type, use the existing
        // c'tor below
        this(paragraph, NO_CASE_CONVERSION, CONSOLE_TEXT);
    }

    /**
     *
     * @param paragraph The paragraph content to be processed
     * @param upperLowerConv 0 for no case conversion, 1 to convert to uppercase, 2 to
     *                       convert to lowercase
     * @param outputType 0 to output text to console, 1 to output text to PDF, 2 to
     *                   output to audio
     */
    public ParagraphProcessor(String paragraph, int upperLowerConv, int outputType){
        // Store the paragraph in the object
        this.content = paragraph;
        // Set the case conversion preference
        this.caseConversion = upperLowerConv;
        // Set the output type
        this.outputType = outputType;
    }

    public void process(){
        // Output to console, no case conversion
        if(this.outputType == CONSOLE_TEXT && this.caseConversion == NO_CASE_CONVERSION){
            // Not much to do here
            System.out.println(this.content);
        }
        // Output to console, convert to UPPER
        else if(this.outputType == CONSOLE_TEXT && this.caseConversion == CONVERT_UPPER){
            // Convert to uppercase
            String upperContent = this.content.toUpperCase();
            System.out.println(upperContent);
        }
        // Output to console, convert to lower
        else if(this.outputType == CONSOLE_TEXT && this.caseConversion == CONVERT_LOWER){
            // Convert to lowercase
            String lowerContent = this.content.toLowerCase();
            System.out.println(lowerContent);
        }
        // Output to PDF, no case conversion
        else if(this.outputType == PDF && this.caseConversion == NO_CASE_CONVERSION){
            createPdf.createPdf();
        }
        // Output to PDF, convert to UPPER
        else if(this.outputType == PDF && this.caseConversion == CONVERT_UPPER){
            createPdf.createPdf();
        }
        // Output to PDF, convert to lower
        else if(this.outputType == PDF && this.caseConversion == CONVERT_LOWER){
            createPdf.createPdf();
            Chunk c = new Chunk(this.content.toLowerCase(), f);
            try {
                pdfD.add(c);
            }
            catch(DocumentException e){
                e.printStackTrace();
            }
            pdfD.close();
        }
        // Output to audio, no case conversion
        else if(this.outputType == AUDIO && this.caseConversion == NO_CASE_CONVERSION){
            System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
            String voiceName = "kevin16";
            VoiceManager vm = VoiceManager.getInstance();
            Voice v = vm.getVoice(voiceName);
            v.allocate();
            v.speak(this.content);
            v.deallocate();
        }
        // Output to audio, convert to UPPER
        else if(this.outputType == AUDIO && this.caseConversion == CONVERT_UPPER){
            System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
            String voiceName = "kevin16";
            VoiceManager vm = VoiceManager.getInstance();
            Voice v = vm.getVoice(voiceName);
            v.allocate();
            v.speak(this.content.toUpperCase());
            v.deallocate();
        }
        // Output to audio, convert to lower
        else if(this.outputType == AUDIO && this.caseConversion == CONVERT_LOWER){
            System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
            String voiceName = "kevin16";
            VoiceManager vm = VoiceManager.getInstance();
            Voice v = vm.getVoice(voiceName);
            v.allocate();
            v.speak(this.content.toLowerCase());
            v.deallocate();
        }
        // Who knows what we're trying to do
        else{
            throw new RuntimeException("Unable to process the paragraph");
        }
    }

    public static void main(String[] args){
        String paragraph = "Hello, from testing main! This is a test paragraph.";
        int conversionType = 0;
        int outputType = 0;
        ParagraphProcessor p = new ParagraphProcessor(paragraph, conversionType, outputType);
        p.process();
    }
}
