package servlets;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import convert.Convert;
import annotation.AnnotationAnalysis;
import json.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import thread.ThreadPool;
import valid.Validator;
import parse.Analysis;

@WebServlet(name = "pict", urlPatterns = {"/"})
public class PictServlet extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String inText = request.getParameter("in");
        Validator validator = new Validator();

        if (validator.isJSONValid(inText)) {
            String text = Analysis.analysis(inText);
            if (text!=null)
                response.sendRedirect(request.getContextPath() + "/api/picture/generate/" + text);
            else
                response.sendRedirect(request.getContextPath() + "/api/picture/generate/error");
        }
        else
            response.sendRedirect(request.getContextPath() + "/api/picture/generate/error");
    }
}
