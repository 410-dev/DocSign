package docsign.ui;

import java.io.Console;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.UUID;

import docsign.Database;
import docsign.Identity;
import docsign.Sign;
import docsign.SignState;
import docsign.signutil.ReadSignature;
import docsign.signutil.UserIdentity;
import docsign.signutil.WriteSignature;
import docsign.util.CoreSHA;
import docsign.util.RecursiveFileSearch;
import lombok.Setter;

@Setter
public class CommandLineUI {

    private String[] args;

    public final String ACTION_READSIGN = "read";
    public final String ACTION_VERIFY = "verify";
    public final String ACTION_WRITESIGN = "sign";
    public final String ACTION_REGISTER = "register";

    private String name;
    private String mailAddress;
    private String password;
    private String actionFlag;
    private String documentPath;
    private String daysValid;
    private int daysValidInt;
    private String mailToVerify;

    public String output = "";

    public void run(String[] args) {
        this.args = args;

        // Login identity
        mailAddress = getParameter("-m");
        password = getParameter("-p");

        // Action
        actionFlag = getParameter("-a");

        // Document
        documentPath = getParameter("-f");

        // name
        name = getParameter("-n");


        // Valid date
        daysValid = getParameter("-dv");
        if (daysValid != null) {
            daysValidInt = Integer.parseInt(daysValid);
        }

        // Verify mail address
        mailToVerify = getParameter("-mv");

        if (actionFlag == null) {
            System.out.println("No action specified.");
            return;
        }

        // If login identity is not specified, show the login screen.
        Scanner input = new Scanner(System.in);
        if (mailAddress == null || password == null) {
            System.out.println("Login identity not specified.");
            System.out.print("Please enter your mail address:");
            mailAddress = input.nextLine();
            Console console = System.console();
            password = new String(console.readPassword("Please enter your password: "));
            input.close();
        }

        // If user does not exist, register first.
        if (!UserIdentity.doesUserExists()) {
            if (name == null) {
                System.out.print("Please enter your name:");
                name = input.nextLine();
            }
            Identity identity = new Identity(name, mailAddress, UUID.randomUUID().toString(), CoreSHA.hash512(password, mailAddress));

            UserIdentity.addIdentity(identity);
            UserIdentity.setCurrentIdentity(identity);
            try {
                Database.db.get("users").getAsJsonArray().add(identity.toJson());
                Database.save();
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(8);
            }
        }

        input.close();

        // Login
        UserIdentity.login(mailAddress, password);
        if (!UserIdentity.isLoggedIn()) {
            System.out.println("Login failed.");
            System.exit(6);
        }

        System.exit(action());

    }

    public int action() {
        try{
            ArrayList<String> filePaths = RecursiveFileSearch.search(documentPath);

            output = "";
            int returnval = 0;
            for(String documentPath : filePaths) {
                // Action parse
                switch (actionFlag) {
                    case ACTION_READSIGN:
                        {
                            // Read and verify signature
                            Sign s = ReadSignature.readSign(documentPath, mailToVerify);
                            if (s == null) {
                                output += ("\nSignature not found.");
                                returnval = 1;
                            }else{
                                output += "\n\n" + s.toReadableString();
                                returnval = 0;
                            }
                        }
                        break;

                    case ACTION_VERIFY:
                        {
                            // Verify signature
                            Sign s = ReadSignature.readSign(documentPath, mailToVerify);
                            SignState sstate = s.getSignState();

                            if (s == null || sstate == null) {
                                output += ("\nSignature not found.");
                                returnval = 1;
                            }else{
                                output += "\n\n";
                                output += s.toReadableString() + "\n";

                                output += ("\nValidity:");
                                output += ("\nSign Version: " + (sstate.getSignedVersion().equals(Sign.VERSION) ? "OK" : "WARNING"));
                                output += ("\nMail Matches: " + (sstate.isEmailMatches() ? "OK" : "Invalid"));
                                output += ("\nTime Valids: " + (sstate.isDateValid() ? "OK" : "Invalid"));
                                output += ("\nUnsigC Matches: " + (sstate.isUnsignedHashMatches() ? "OK" : "Invalid"));

                                if (sstate.isDateValid() 
                                    && sstate.isEmailMatches() 
                                    && sstate.isUnsignedHashMatches()) {
                                    output += ("\n\nSignature is valid.");
                                    returnval = 0;
                                } else {
                                    output += ("\n\nSignature is not valid.");
                                    returnval = 2;
                                }
                            }
                        }
                        break;

                    case ACTION_WRITESIGN:
                        {
                            // Write signature
                            output += ("\nOK");
                            returnval = 0;
                        }
                        break;

                    case ACTION_REGISTER:
                        {
                            // Register identity
                            Identity identity = new Identity(name, mailAddress, UUID.randomUUID().toString(), CoreSHA.hash512(password, mailAddress));
                            UserIdentity.addIdentity(identity);
                            Database.db.get("users").getAsJsonArray().add(identity.toJson());
                            Database.save();
                            UserIdentity.setCurrentIdentity(identity);
                            returnval = 0;
                        }
                        break;

                    default:
                        output += ("Unknown action: " + actionFlag);
                        returnval = 3;
                }
            }

            System.out.println(output);

            return returnval;
        }catch(Exception e) {
            e.printStackTrace();
            return 255;
        }
    }

    // Get parameter after flag
    public String getParameter(String flag) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals(flag)) {
                return args[i + 1];
            }
        }
        return null;
    }
}
