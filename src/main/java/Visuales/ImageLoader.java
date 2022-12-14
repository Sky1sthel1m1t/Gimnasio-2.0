package Visuales;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageLoader {
    private ImageLoader imageLoader;

    public ImageLoader getImageLoader() {
        if (imageLoader == null) {
            imageLoader = new ImageLoader();
        }
        return imageLoader;
    }

    public static ImageIcon getImagen(String path, int ancho, int alto) {
        BufferedImage img = null;

        try {
            img = ImageIO.read(new File(path));
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        Image dimg = img.getScaledInstance(ancho, alto,
                Image.SCALE_SMOOTH);
        ImageIcon imageIcon = new ImageIcon(dimg);
        return imageIcon;
    }
}
