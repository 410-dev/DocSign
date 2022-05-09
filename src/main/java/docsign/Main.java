package docsign;

import java.io.IOException;

import docsign.ui.CommandLineUI;
import docsign.ui.GraphicUI;

public class Main {
    public static void main(String[] args) throws IOException {

        // Initialize the database.
        Database.init();

        // If no arguments are given, show the graphic user interface.
        // Otherwise, show the command line user interface.
        if (args.length == 0) {
            GraphicUI.run();
        } else {
            new CommandLineUI().run(args);
        }
    }
}
