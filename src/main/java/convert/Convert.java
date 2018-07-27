package convert;

import pict.Picture;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Convert {

    //функция, которая делает из текста картинку
    public static void convertTextInPict(Picture picture) {
        String pathToDirectoryWithImage = getPathPict(picture.getText());
        try {
            ImageIO.write(convert(picture.getText()), "png", new File(pathToDirectoryWithImage));
        } catch (IOException ex) {
            System.exit(-1);
        }
    }

    //функция которая формирует путь к картинке
    public static String getPathPict(String text) {
        String relativePathToClass = Convert.class.getName().replace('.', '/') + ".class";
        URL url = Convert.class.getClassLoader().getResource(relativePathToClass);
        String pathToRoot = url.getFile().substring(0, url.getFile().indexOf("target"))+ "src"+File.separator+"main"+File.separator+"resources"+File.separator+"image"+File.separator;
        String pathToDirectoryWithImage = pathToRoot + text +".png";
        return pathToDirectoryWithImage;
    }

    //функция которая рисует текст на белом фоне
    private static BufferedImage convert(String text) {
        int width = (text.length() + 1) * 28;
        int height = 3 * 28;

        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        Graphics g = bi.getGraphics();
        g.setColor(Color.WHITE);
        g.setFont(new Font("TimesRoman", Font.BOLD, 28));
        g.fillRect(0,0, width, height);
        g.setColor(Color.black);
        g.drawString(text, 28, 28);
        g.dispose();

        return bi;
    }


}
