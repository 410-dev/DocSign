package docsign.signutil;

import docsign.Sign;
import docsign.util.CoreAES;
import docsign.util.CoreHEX;

public class ReadSignature {
    public static Sign readSign(String filePath) throws Exception {
        // Read file as hexadecimal
        String hex = CoreHEX.readFileToHex(filePath);

        // Encode hex to string
        String str = CoreHEX.hexToString(hex);

        // Search for header
        if (!str.contains(Sign.HEADER)) {
            return null;
        }

        // Search for footer
        if (!str.endsWith(Sign.FOOTER)) {
            return null;
        }

        // Remove header and footer
        str = str.substring(str.indexOf(Sign.HEADER) + Sign.HEADER.length(), str.lastIndexOf(Sign.FOOTER));

        System.out.println("SIGN: " + CoreHEX.stringToHex(str));

        // Decode string to sign
        String signature = CoreAES.decrypt(str, Sign.SIGN_PASS);
        Sign s = Sign.parse(signature);

        return s;
    }
}
