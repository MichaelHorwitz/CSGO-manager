/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Test;

/**
 *
 * @author Michael Horwitz
 */
import java.io.File;
import net.sourceforge.tess4j.*;

public class TryingToUseTesseract {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        File imageFile = new File("C:\\Users\\Nicole\\Documents\\NetBeansProjects\\TryingToUseTesseract\\TestImage.png");
        ITesseract instance = new Tesseract();  // JNA Interface Mapping
        // ITesseract instance = new Tesseract1(); // JNA Direct Mapping
        instance.setDatapath("C:\\Users\\Nicole\\Documents\\NetBeansProjects\\TryingToUseTesseract\\tessdata"); // path to tessdata directory

        try {
            String result = instance.doOCR(imageFile);
            System.out.println(result);
        } catch (TesseractException e) {
            System.err.println(e.getMessage());
        }
    }

}
