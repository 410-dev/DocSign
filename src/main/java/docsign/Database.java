package docsign;

import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import docsign.signutil.UserIdentity;

public class Database {

    // Database paths
    public static final String docsignRoot = System.getProperty("user.home") + "/.docsign/";
    private static final String database = docsignRoot + "db.json";


    // Database cache
    public static JsonObject db = null;


    // Database Initialization
    public static void init() throws IOException {
        // Create the docsign root directory if it doesn't exist.
        File root = new File(docsignRoot);
        if (!root.isDirectory()) {
            root.mkdirs();
            System.out.println("Created docsign root directory.");
        }

        // Create the database file if it doesn't exist.
        File db = new File(database);
        if (!db.isFile()) {
            BufferedWriter writer = new BufferedWriter(new FileWriter(db));
            format();
            writer.write(Database.db.toString());
            writer.close();
            System.out.println("Created database file.");
        }else{
            // Read the database file.
            BufferedReader reader = new BufferedReader(new FileReader(db));
            String line;
            StringBuilder builder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            reader.close();

            // Parse the database file.
            Database.db = JsonParser.parseString(builder.toString()).getAsJsonObject();
            JsonArray users = Database.db.get("users").getAsJsonArray();
            for (JsonElement user : users) {
                Identity identity = Identity.parse(user.getAsJsonObject().toString());
                UserIdentity.addIdentity(identity);
            }
        }
    }

    private static void format() {
        db = new JsonObject();

        // Add the default values
        db.addProperty("version", "1.0");
        db.add("users", new JsonArray());

        System.out.println("Formatted database.");
    }

    private static void load() throws IOException {
        // Load the database from the file.
        BufferedReader reader = new BufferedReader(new FileReader(database));
        StringBuilder builder = new StringBuilder();

        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }

        reader.close();

        // Parse the database.
        db = JsonParser.parseString(builder.toString()).getAsJsonObject();
    }

    public static void save() throws IOException {
        // Save the database to the file.
        BufferedWriter writer = new BufferedWriter(new FileWriter(database));
        writer.write(db.toString());
        writer.close();
    }

    // Database methods
    public static void add(String key, String value) throws IOException {
        // Load the database if it hasn't been loaded yet.
        if (db == null) {
            load();
        }

        // Add the value to the database.
        db.addProperty(key, value);
        save();
    }

    public static void add(String key, JsonElement value) throws IOException {
        // Load the database if it hasn't been loaded yet.
        if (db == null) {
            load();
        }

        // Add the value to the database.
        db.add(key, value);
        save();
    }
}
