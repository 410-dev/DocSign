package docsign.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CoreHEX {
    public static String readFileToHex(String path) throws IOException {
        StringBuilder sb = new StringBuilder();
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    public static void writeHexToFile(String path, String hex) throws IOException {
        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < hex.length(); i += 2) {
            bytes[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4) + Character.digit(hex.charAt(i + 1), 16));
        }
        Files.write(Paths.get(path), bytes);
    
    }

    public static String hexToString(String hex) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < hex.length(); i += 2) {
            String str = hex.substring(i, i + 2);
            sb.append((char) Integer.parseInt(str, 16));
        }
        return sb.toString();
    }

    public static String stringToHex(String str) {
        StringBuilder sb = new StringBuilder();
        for (char c : str.toCharArray()) {
            sb.append(String.format("%02x", (int) c));
        }
        return sb.toString();
    }


}
