package pict;


import convert.Convert;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.UUID;


public class Picture {

    String text;
    String id;
    Runnable task;

    public Picture(String text, String id) {
        this.text = text;
        this.id = id;
    }

    public void setTask(Runnable task){
        this.task = task;
    }

    public String getText(){
        return text;
    }

    public String getId(){
        return id;
    }

    public Runnable getTask(){
        return task;
    }



}
