package docsign;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import docsign.signutil.UserIdentity;
import docsign.util.CoreAES;
import docsign.util.CoreBase64;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Sign {

    public static final String SIGN_PASS = "44CA24B7-3FD0-4B1B-9331-538948B806D7";
    public static final String HEADER = "<SIGHEAD>";
    public static final String FOOTER = "<SIGFOOT>";
    public static final String VERSION = "1.0";

    private String version;
    private String name;
    private String email;
    private long signedAt;
    private long validthrough;
    @Setter private String unsignedHash;
    
    @Getter @Setter private SignState signState;

    public Sign(String version, String name, String email, long signedAt, long validthrough, String unsignedHash) throws Exception {
        this.version = version;
        this.name = name;
        this.email = email;
        this.signedAt = signedAt;
        this.validthrough = validthrough;
        this.unsignedHash = unsignedHash;
    }

    public Sign(int daysValid, boolean useWeakSign) throws Exception {
        this.version = VERSION;
        this.name = UserIdentity.getCurrentIdentity().getName();
        this.email = useWeakSign ? "" : UserIdentity.getCurrentIdentity().getEmail();
        this.signedAt = System.currentTimeMillis() / 1000;
        this.validthrough = this.signedAt + (60 * 60 * 24 * daysValid);
    }

    public Sign(Identity identity, int validityInDays) {
        this.name = identity.getName();
        this.email = identity.getEmail();
        this.signedAt = System.currentTimeMillis() / 1000;
        this.validthrough = this.signedAt + (validityInDays * 24 * 60 * 60);
    }

    public boolean equals(Object obj) {
        if (obj instanceof Sign) {
            Sign other = (Sign) obj;
            return this.unsignedHash.equals(other.unsignedHash);
        }
        return false;
    }

    public JsonObject toJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("version", VERSION);
        obj.addProperty("name", name);
        obj.addProperty("email", email);
        obj.addProperty("signedAt", signedAt);
        obj.addProperty("validthrough", validthrough);
        obj.addProperty("unsignedHash", unsignedHash);
        return obj;
    }

    public String toString() {
        String s = "";
        try {
            s += CoreAES.encrypt(toJson().toString(), SIGN_PASS);
        }catch(Exception e) {
            s = "null";
        }

        return HEADER + s + FOOTER;
    }

    public String toReadableString() {
        String s = "Signature Information: ";
        s += "\nVersion: " + version;
        s += "\nName: " + name;
        s += "\nEmail: " + email;
        s += "\nSigned At: " + getSignedDate();
        s += "\nValid Through: " + getValidDate();
        s += "\nCurrently valid: " + ((System.currentTimeMillis() / 1000L < validthrough) ? "Yes" : "No");
        s += "\nUnsigned Hash: " + unsignedHash;
        return s;
    }

    public String getValidDate() {
        return String.format("%tF", validthrough * 1000L);
    }

    public String getSignedDate() {
        return String.format("%tF", signedAt * 1000L);
    }

    public String getSign() {
        String s = toJson().toString();
        return CoreBase64.encode(s);
    }

    public static Sign parse(String json) throws Exception {
        JsonObject obj = JsonParser.parseString(json).getAsJsonObject();
        return new Sign(
                obj.get("version").getAsString(),
                obj.get("name").getAsString(),
                obj.get("email").getAsString(),
                obj.get("signedAt").getAsLong(),
                obj.get("validthrough").getAsLong(),
                obj.get("unsignedHash").getAsString()
        );
    }
}
