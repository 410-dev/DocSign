package docsign.signutil;

import java.util.ArrayList;

import docsign.Identity;
import docsign.util.CoreSHA;

public class UserIdentity {
    private static ArrayList<Identity> identities = new ArrayList<>();
    private static Identity currentIdentity = null;

    private static boolean isLoggedIn = false;

    public static void addIdentity(Identity identity) {

        if (identities.contains(identity)) {
            throw new RuntimeException("User already exists.");
        }
        
        identities.add(identity);
    }

    public static void removeIdentity(Identity identity) {
        identities.remove(identity);
    }

    public static void setCurrentIdentity(Identity identity) {
        currentIdentity = identity;
    }

    public static Identity getCurrentIdentity() {
        return currentIdentity;
    }

    public static void login(String email, String password) {
        password = CoreSHA.hash512(password, email);
        isLoggedIn = false;

        for (Identity identity : identities) {
            if (identity.getEmail().equals(email) && 
                identity.getDigestedPassword().equals(password)) {
                setCurrentIdentity(identity);
                isLoggedIn = true;
                return;
            }
        }

        isLoggedIn = false;
    }

    public static boolean isLoggedIn() {
        return isLoggedIn;
    }

    public static boolean doesUserExists() {
        return identities.size() > 0;
    }
}
