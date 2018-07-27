package servlets;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Vector;

import convert.Convert;
import annotation.AnnotationAnalysis;
import json.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.graalvm.compiler.core.GraalCompiler;
import pict.Picture;
import thread.ThreadPool;
import valid.Validator;
import parse.Analysis;

import  java.util.UUID;

@WebServlet(name = "imPost", urlPatterns = {"/api/picture/generate/*"})
public class ImgPostServlet extends HttpServlet {

    public static ThreadPool threadPool = new ThreadPool();
    public static Vector<Picture> spisokPicture = new Vector<Picture>();
    public static String answer = "";

    static {
        new AnnotationAnalysis().parse(threadPool);
        threadPool.init();
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String inText = request.getParameter("in");
        Validator validator = new Validator();

        Gson gson = new GsonBuilder().setPrettyPrinting().create(); //объект для вывода
        JSONObject obj = new JSONObject();//объект для формирования сообщения в формате json

        //если запрос корректный

        if (validator.isJSONValid(inText) && Analysis.analysis(inText)!=null) {
            inText = Analysis.analysis(inText);
            //проверяем, существует ли такая картинка
            if (existFile(inText)) {
                for(int i = 0; i<spisokPicture.size();i++){
                    if (spisokPicture.get(i).getText().equals(inText)){
                        obj.setMessage("Image with this id already exists");
                        obj.setLink(getLinkPicture(spisokPicture.get(i), request));
                        answer = gson.toJson(obj);
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    }
                }
            }
            //если не существует такая картинка
            else {
                //проверяем существует ли поток, который обрабатывает картинку
                if (isAliveThread(inText)) {
                    for(int i = 0; i<spisokPicture.size();i++){
                        if (spisokPicture.get(i).getText().equals(inText)) {
                            obj.setMessage("Image with this id is processed");
                            obj.setLink(getLinkPicture(spisokPicture.get(i), request));
                            answer = gson.toJson(obj);
                            response.setStatus(HttpServletResponse.SC_ACCEPTED);
                        }
                    }
                }
                //если не существует, то отправляем новую задачу для потоков
                else {
                    String id = request.getPathInfo().substring(request.getPathInfo().indexOf("/")+1);
                    //cоздаем картинку
                    Picture picture = new Picture(inText, id);
                    spisokPicture.add(picture);
                    //создаем задачу
                    Runnable task = () -> { Convert.convertTextInPict(picture); };
                    picture.setTask(task);

                    //даем задачу потоку
                    threadPool.addTask(picture);

                    obj.setLink(getLinkPicture(picture, request));
                    answer = gson.toJson(obj);
                    response.setStatus(HttpServletResponse.SC_OK);
                }
            }
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.print(answer);
            out.flush();
        }
        //если запрос некорректный
        else {
            obj.setMessage("The request must be in the following format: {\"text\": \"*text*\" }");
            answer = gson.toJson(obj);
            response.sendError(HttpServletResponse.SC_NOT_FOUND, answer);
        }
    }


    //функция, которая проверяет существование файла
    public static boolean existFile(String text){
        if (new File(Convert.getPathPict(text)).exists())
            return true;
        return false;
    }

    //функция, которая проверяет существует ли поток, который выполняет задачу
    public static boolean isAliveThread(String name) {
        for (int i = 0; i<threadPool.getThread().length; i++) {
            if (threadPool.getThread()[i] != null && (threadPool.getThread()[i].getName()).equals(name))
                return true;
        }
        return false;
    }

    //функция, которая формирует ссылку для картинки
    public static String getLinkPicture(Picture picture, HttpServletRequest request) {
        String link = request.getRequestURL().toString();
        return link.substring(0, link.indexOf("/api"))  + "/api/picture/" +  picture.getId();
    }
}
