package docsign;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignState {
    private String signerVersion = Sign.VERSION;
    private String signedVersion;

    private String unsignedContentExpectedSHA;
    private String unsignedContentActualSHA;

    private boolean dateValid;
    private boolean emailMatches;
    private boolean unsignedHashMatches;
    private boolean signatureValid;

    public void checkValidty(Sign signature, String mail) {
        dateValid = signature.getSignedAt() < signature.getValidthrough();
        signedVersion = signature.getVersion();
        emailMatches = signature.getEmail().equals(mail);
        unsignedHashMatches = unsignedContentActualSHA.equals(unsignedContentExpectedSHA);
        signatureValid = dateValid && emailMatches && unsignedHashMatches;
    }
}
