package json;


import java.net.MalformedURLException;
import java.net.URL;


public class JSONObject {
    private String message;
    private URL link;

    public JSONObject() { }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage(){
        return message;
    }

    public void setLink(String link) {
        try {
            this.link = new URL(link);
        } catch (MalformedURLException e) { }
    }

    public URL getLink(){
        return link;
    }

}