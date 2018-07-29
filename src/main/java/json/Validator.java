package json;


import com.google.gson.Gson;

public class Validator {

    private final Gson gson = new Gson();

    public boolean isJSONValid(String json) {
        try {
            if (json!=null){
                gson.fromJson(json, Object.class);
                return true;
            }
            else
                return false;
        } catch(Exception ex) {
            return false;
        }
    }
}
