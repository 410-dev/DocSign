package docsign.signutil;

import docsign.Sign;

public class VerifySignature {
    public static Sign verifySignature(String email, String document) throws Exception {
        try {
            Sign s = ReadSignature.readSign(document);
            s.setSignState(s.isValid(email));
            return s;
        }catch(Exception e) {
            return null;
        }
    }
}
