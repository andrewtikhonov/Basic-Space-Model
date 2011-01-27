package modeller.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: andrew
 * Date: Jan 26, 2011
 * Time: 6:22:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImageLoaderCached {

    private static HashMap<String, Image> imageCache = new HashMap<String, Image>();

    /** Load the specified image. If the image cannot be loaded, a dummy
     *  image is returned.
     * @param path      Path to the image within the package.
     *                  e.g. "/org/package/images/image.png"
     * @return          The loaded image or a dummy image
     */

    private static Integer lock = new Integer(0);

    public static Image dummyImage = null;

    public static Image load(String path) {

        Image image = imageCache.get(path);

        if (image != null) {
            return image;
        } else {
            try {
                Image loadedimage = ImageIO.read(ImageLoaderCached.class.getResource(path));
                imageCache.put(path, loadedimage);

                return loadedimage;
            } catch (Exception ex) {
                //log.error("Icon ({}) missing", path);
                return getDummyImage();
            }
        }

    }

    /** Get a dummy image
     * @return          A dummy image
     */
    public static Image getDummyImage() {

        if (dummyImage == null) {

            // perhaps an unnecessary
            // precaution
            synchronized (lock) {
                int h = 20;
                int w = 20;

                BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

                Graphics2D g2d = img.createGraphics();

                g2d.fillRect(0,0,w,h);

                g2d.setColor(Color.BLACK);
                g2d.drawLine(2,2,w-3,h-3);
                g2d.drawLine(w-3,2,2,h-3);

                g2d.dispose();

                dummyImage = img;
            }
        }

        return dummyImage;
    }

}