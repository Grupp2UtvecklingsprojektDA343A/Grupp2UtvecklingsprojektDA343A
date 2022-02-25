package client;

import javax.swing.ImageIcon;
import java.net.MalformedURLException;
import java.net.URL;

public class ImageHandler {
    /**
     * Returns an ImageIcon, or null if the path was invalid.
     * https://docs.oracle.com/javase/tutorial/uiswing/components/icon.html
     * @param path Path to image file.
     * @return A new ImageIcon
     */
    public static ImageIcon createImageIcon(String path) {
        URL imgURL;
        try {
            imgURL = new URL(path);
        } catch (MalformedURLException e) {
            imgURL = ImageHandler.class.getResource(path);
        }

        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
}
