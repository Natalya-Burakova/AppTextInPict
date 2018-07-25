package servlets;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
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
import thread.ThreadPool;
import valid.Validator;
import parse.Analysis;



public class ImgPostServlet extends HttpServlet {

    public static ThreadPool threadPool = new ThreadPool();

    static {
        new AnnotationAnalysis().parse(threadPool);
        threadPool.init();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String inText = request.getRequestURI().substring((request.getRequestURI()).lastIndexOf("/")+1);
        Validator validator = new Validator();

        Gson gson = new GsonBuilder().setPrettyPrinting().create(); //объект для вывода
        JSONObject obj = new JSONObject();//объект для формирования сообщения в формате json
        String answer = "";

        //если запрос корректный
        if (!inText.equals("error")) {
            //проверяем, существует ли такая картинка
            if (existFile(inText)) {
                obj.setMessage("Image with this id already exists");
                obj.setLink(getLinkPicture(inText, request));
                answer = gson.toJson(obj);
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
            //если не существует такая картинка
            else {
                //проверяем существует ли поток, который обрабатывает картинку
                if (isAliveThread(inText)) {
                    obj.setMessage("Image with this id is processed");
                    obj.setLink(getLinkPicture(inText, request));
                    answer = gson.toJson(obj);
                    response.setStatus(HttpServletResponse.SC_ACCEPTED);
                }
                //если не существует, то отправляем новую задачу для потоков
                else {
                    Runnable task = () -> { Convert.convertTextInPict(inText); };
                    threadPool.addTask(task, inText);
                    obj.setLink(getLinkPicture(inText, request));
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
    public static boolean isAliveThread(String inText) {
        for (int i = 0; i<threadPool.getThreadTask().length; i++) {
            if (threadPool.getThreadTask()[i] != null && (threadPool.getThreadTask()[i].getName()).equals(inText))
                return true;
        }
        return false;
    }

    //функция, которая формирует ссылку для картинки
    public static String getLinkPicture(String text, HttpServletRequest request) {
        String link = request.getRequestURL().toString();
        return link.substring(0, link.indexOf("/api"))  + "/api/picture/" + text;
    }
}
