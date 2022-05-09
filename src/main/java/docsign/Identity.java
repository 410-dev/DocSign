package docsign;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import lombok.Getter;

@Getter
public class Identity {
    private String name;
    private String email;
    private String uuid;
    private String digestedPassword;

    public Identity(String name, String email, String uuid, String digestedPassword) {
        this.name = name;
        this.email = email;
        this.uuid = uuid;
        this.digestedPassword = digestedPassword;
    }

    public boolean equals(Object obj) {
        if (obj instanceof Identity) {
            Identity other = (Identity) obj;
            return this.email.equals(other.getEmail()) || this.uuid.equals(other.getUuid());
        }
        return false;
    }

    public JsonObject toJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("name", name);
        obj.addProperty("email", email);
        obj.addProperty("uuid", uuid);
        obj.addProperty("digestedPassword", digestedPassword);
        return obj;
    }

    public static Identity parse(String json) {
        JsonObject obj = JsonParser.parseString(json).getAsJsonObject();

        return new Identity(
                obj.get("name").getAsString(),
                obj.get("email").getAsString(),
                obj.get("uuid").getAsString(),
                obj.get("digestedPassword").getAsString()
        );
    }
}
