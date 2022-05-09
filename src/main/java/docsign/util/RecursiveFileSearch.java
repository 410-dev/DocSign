package docsign.util;

import java.io.File;
import java.util.ArrayList;

public class RecursiveFileSearch {
    
    public static ArrayList<String> search(String path) {
        ArrayList<String> files = new ArrayList<String>();

        if (new File(path).exists()) {
            // If path is directory, search it
            if (new File(path).isDirectory()) {
                File[] list = new File(path).listFiles();
                for (File file : list) {
                    files.addAll(search(file.getAbsolutePath()));
                }

            // If path is file and does not starts with . for its name, add it
            } else if (!path.substring(path.lastIndexOf(".")).equals(".DS_Store")
                    && !path.substring(path.lastIndexOf("/")+1).startsWith(".")) {
                files.add(path);
            }
        }

        return files;
    }
}
