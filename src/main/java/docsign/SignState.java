package docsign;

import lombok.Getter;

@Getter
public class SignState {
    private String signerVersion;
    private String signedVersion;

    private boolean dateValid;
    private boolean emailMatches;
    private boolean uuidMatches;

    public SignState(String signerVersion, String signedVersion, String expectedMailAddress, String signerMailAddress, boolean dateValid, boolean uuidValid) {
        this.signerVersion = signerVersion;
        this.signedVersion = signedVersion;
        this.dateValid = dateValid;
        this.emailMatches = signerMailAddress.equals(expectedMailAddress);
        this.uuidMatches = uuidValid;

        if (expectedMailAddress.equals("")) this.emailMatches = true;
    }
}
