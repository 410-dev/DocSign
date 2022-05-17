package docsign.signutil;

import docsign.Main;
import docsign.Sign;
import docsign.SignState;
import docsign.util.CoreAES;
import docsign.util.CoreBase64;
import docsign.util.CoreHEX;
import docsign.util.CoreSHA;

public class ReadSignature {
    
    public static Sign readSign(String filePath, String mailToVerify, boolean useWeakSign) throws Exception {
        Main.log("Reading signature from file: " + filePath);

        // Read file as hexadecimal
        String hex = CoreHEX.readFileToHex(filePath);

        // Encode hex to string
        String str = CoreHEX.hexToString(hex);
        String orig = new String(str);

        Main.log("Data: " + str);

        // Search for header
        if (!str.contains(Sign.HEADER)) {
            Main.log("Header not found.");
            return null;
        }

        Main.log("Header found.");


        // Search for footer
        boolean signatureIsAtTheEnd = true;
        if (!str.endsWith(Sign.FOOTER)) {
            signatureIsAtTheEnd = false;
            Main.log("Warning: Signature is not at the end of the file.");

            // If footer is not at the end of file, signature is invalid
            if (!str.contains(Sign.FOOTER)) {
                Main.log("Error: Footer not found.");
                return null;
            }
        }

        

        // Remove header and footer
        str = str.substring(str.indexOf(Sign.HEADER) + Sign.HEADER.length(), str.lastIndexOf(Sign.FOOTER));
        
        
        // Decrypt
        str = CoreAES.decrypt(str, Sign.SIGN_PASS);
        Main.log("SIGN: " + str);

        // Get unsigned hash
        String unsignedHash = orig.substring(0, orig.indexOf(Sign.HEADER));
        unsignedHash = CoreBase64.encode(unsignedHash);
        unsignedHash = CoreSHA.hash512(unsignedHash);
        Main.log("UNSIGNED HASH: " + unsignedHash);


        // Decode string to sign
        Sign s = Sign.parse(str);
        SignState state = new SignState();
        state.setSignatureValid(signatureIsAtTheEnd);
        state.setUnsignedContentActualSHA(unsignedHash);
        state.setUnsignedContentExpectedSHA(s.getUnsignedHash());
        state.checkValidty(s, mailToVerify);

        if (useWeakSign) {
            state.setEmailMatches(true);
        }

        s.setSignState(state);
        

        return s;
    }
}
