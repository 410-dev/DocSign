package docsign.signutil;

import docsign.Sign;
import docsign.SignState;
import docsign.util.CoreBase64;
import docsign.util.CoreHEX;
import docsign.util.CoreSHA;

public class WriteSignature {
    public static void writeSignature(String document, int daysValid, String mininote) throws Exception {
        Sign s = new Sign(daysValid);
        s.setSignState(new SignState());
        s.setMininote(mininote);

        // Write signature to file
        String fileHex = CoreHEX.readFileToHex(document);
        String unsignedContent = CoreBase64.encode(CoreHEX.hexToString(fileHex));
        unsignedContent = CoreSHA.hash512(unsignedContent);
        s.setUnsignedHash(unsignedContent);
        String signatureHex = CoreHEX.stringToHex(s.toString());
        String newHex = fileHex + signatureHex;

        // Get file extension
        String ext = document.substring(document.lastIndexOf(".") + 1);
        String fileNameWithoutExt = document.substring(0, document.lastIndexOf("."));
        ext = ".signed." + ext;

        // Write hex to file
        CoreHEX.writeHexToFile(fileNameWithoutExt + ext, newHex);
    }
}
