package docsign.signutil;

import docsign.Sign;
import docsign.util.CoreHEX;

public class WriteSignature {
    public static void writeSignature(String document, int daysValid, boolean useWeakSign) throws Exception {
        Sign s = new Sign(daysValid, useWeakSign);
        String signature = s.toString();

        // Write signature to file
        String hex = CoreHEX.stringToHex(signature);
        String fhex = CoreHEX.readFileToHex(document);
        String newHex = fhex + hex;

        // Get file extension
        String ext = document.substring(document.lastIndexOf(".") + 1);
        String fileNameWithoutExt = document.substring(0, document.lastIndexOf("."));
        ext = ".signed." + ext;

        // Write hex to file
        CoreHEX.writeHexToFile(fileNameWithoutExt + ext, newHex);
    }
}
