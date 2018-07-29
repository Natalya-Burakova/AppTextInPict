package servlets;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import convert.Convert;
import json.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


@WebServlet(name = "imGet", urlPatterns = {"/api/picture/*"})
public class ImgGetServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String inText = request.getRequestURI().substring((request.getRequestURI()).lastIndexOf("/")+1);

        Gson gson = new GsonBuilder().setPrettyPrinting().create(); //объект для вывода
        JSONObject obj = new JSONObject();//объект для формирования сообщения в формате json
        String answer = "";


        for (int i = 0; i<ImgPostServlet.spisokPicture.size(); i++) {
            if (ImgPostServlet.spisokPicture.get(i).getId().equals(inText))
                inText = ImgPostServlet.spisokPicture.get(i).getText();
        }

        if (ImgPostServlet.existFile(inText)) {
            //отображаем картинку
            byte[] imageBytes = pictToMasByte(inText);
            response.setContentType("image/png");
            response.setContentLength(imageBytes.length); // imageBytes - image in bytes
            response.getOutputStream().write(imageBytes);//
        }
        else {
            //если картинка не найдена, проверяется состояние
            if (ImgPostServlet.isAliveThread(inText)) {
                //если обрабатывается потоком
                obj.setMessage("The image with this id is processed");
                answer = gson.toJson(obj);
                response.setContentType("application/json");
                PrintWriter out = response.getWriter();
                out.print(answer);
                out.flush();
            }
            else {
                //если не обрабатывается потоком
                obj.setMessage("Image does not exist");
                answer = gson.toJson(obj);
                response.sendError(HttpServletResponse.SC_NOT_FOUND, answer);
            }
        }
    }


    //функция которая переводит картинку в массив байтов
    public byte[] pictToMasByte(String inText) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(1000);
        BufferedImage image = ImageIO.read(new File(Convert.getPathPict(inText)));
        ImageIO.write(image, "png", baos);
        baos.flush();
        String base64String = Base64.encode(baos.toByteArray());
        baos.close();
        return Base64.decode(base64String);
    }

}
