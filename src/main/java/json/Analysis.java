package json;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class Analysis {

    public static String analysis(String jsonLine) {
        Gson gson = new Gson();
        String text;
        try {
            text = gson.fromJson(jsonLine, JsonObject.class).getAsJsonObject().get("text").getAsString();
        }

        catch (Exception e) {
            return null;
        }

        return text;
    }
}