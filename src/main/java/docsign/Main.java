package docsign;

import java.io.IOException;
import java.util.Scanner;

import javax.swing.JOptionPane;

import docsign.ui.CommandLineUI;
import docsign.ui.GraphicUI;
import updateutil.UpdateUtility;
import updateutil.Versioning;

public class Main {
    public static void main(String[] args) throws IOException {

        // Initialize the database.
        Database.init();

        // Check for update
        update(args);

        // If no arguments are given, show the graphic user interface.
        // Otherwise, show the command line user interface.
        if (args.length == 0) {
            GraphicUI.run();
        } else {
            new CommandLineUI().run(args);
        }
    }

    private static void update(String[] args) {
        Versioning.setBuildNum(300);
        Versioning.setUpdateCheckerURL("https://raw.githubusercontent.com/410-dev/DocSign/main/latest");
        Versioning.useStableRelease();

        try {
            Object[] result = UpdateUtility.checkUpdate();
            if ((Boolean) result[0] == true) {   // Has update
                System.out.println("Update available!");
                System.out.println("Current version: " + Versioning.getBuildNum());
                System.out.println("Latest version: " + result[1]);
                System.out.println("Download URL: " + result[2]);

                // Show user the prompt
                int resultInt = -1;
                if (args.length == 0) {
                    resultInt = JOptionPane.showConfirmDialog(null, "Update available!\nCurrent build: " + Versioning.getBuildNum() + "\nLatest build: " + result[1] + "\nDownload URL: " + result[2] + "\n\nUpdate now?", "Update available", JOptionPane.YES_NO_OPTION);
                }else{
                    System.out.println("Update now?");
                    System.out.print("[Y/N] :");
                    Scanner s = new Scanner(System.in);
                    while(true) {
                        String input = s.nextLine();
                        if (input.equalsIgnoreCase("y")) {
                            resultInt = JOptionPane.YES_OPTION;
                            break;
                        }else if (input.equalsIgnoreCase("n")) {
                            resultInt = JOptionPane.NO_OPTION;
                            break;
                        }else{
                            System.out.print("[Y/N] :");
                        }
                    }

                    s.close();
                }

                // If user wants, do update
                if (resultInt == JOptionPane.YES_OPTION) {
                    UpdateUtility.doUpdate("./DocSign.jar");
                    if (args.length == 0) JOptionPane.showMessageDialog(null, "Update downloaded.");
                    else System.out.println("Update downloaded.");
                    System.exit(0);
                }
            }
        }catch(Exception e) {
            System.out.println("Update failed");
        }
    }

    public static void log(String s) {
        // System.out.println(s);
    }
}
