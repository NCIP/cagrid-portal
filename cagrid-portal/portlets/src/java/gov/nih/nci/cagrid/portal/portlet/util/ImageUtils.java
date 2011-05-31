package gov.nih.nci.cagrid.portal.portlet.util;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

public class ImageUtils {

    public static BufferedImage scaleImage(BufferedImage source, int maxHeight, int maxWidth) 
    {
        float srcWidth = (float) source.getWidth();
        float srcHeight = (float) source.getHeight();
        
        if ((srcWidth <= maxWidth) && (srcHeight <= maxHeight))
        {
            return source;
        }

        boolean hasAlpha = hasAlpha(source);
        int imgType = (hasAlpha == true) ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB;

        int newWidth = 0;
        int newHeight = 0;
        
        float srcRatio = srcHeight / srcWidth;
        
        if (srcRatio >= 1) //portrait
        {
            newHeight = maxHeight;
            newWidth = Math.round(newHeight / srcRatio);
        }
        else //landscape 
        {
            newWidth = maxWidth;
            newHeight = Math.round(newWidth * srcRatio);
        }
        
        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, imgType);

        Graphics2D g = resizedImage.createGraphics();

        Map<RenderingHints.Key, Object> hints = new HashMap<RenderingHints.Key, Object>();
        hints.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        hints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        hints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.addRenderingHints(hints);

        g.drawImage(source, 0, 0, newWidth, newHeight, null);
        g.dispose();

        return resizedImage;
    }

    /*
     * public static BufferedImage scaleImage(BufferedImage source, int maxHeight, int maxWidth){ BufferedImage result =
     * source; if(result.getHeight() > maxHeight){ result = toBufferedImage(result.getScaledInstance(-1, maxHeight,
     * -1)); } if(result.getWidth() > maxWidth){ result = toBufferedImage(result.getScaledInstance(maxWidth, -1, -1)); }
     * return result; }
     */

    public static BufferedImage toBufferedImage(Image image) {
        if (image instanceof BufferedImage) {
            return (BufferedImage) image;
        }

        // This code ensures that all the pixels in the image are loaded
        image = new ImageIcon(image).getImage();

        // Determine if the image has transparent pixels
        boolean hasAlpha = hasAlpha(image);

        // Create a buffered image with a format that's compatible with the
        // screen
        BufferedImage bimage = null;
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        try {
            // Determine the type of transparency of the new buffered image
            int transparency = Transparency.OPAQUE;
            if (hasAlpha == true) {
                transparency = Transparency.BITMASK;
            }

            // Create the buffered image
            GraphicsDevice gs = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gs.getDefaultConfiguration();
            bimage = gc.createCompatibleImage(image.getWidth(null), image.getHeight(null), transparency);
        } catch (HeadlessException e) {
        } // No screen

        if (bimage == null) {
            // Create a buffered image using the default color model
            int type = BufferedImage.TYPE_INT_RGB;
            if (hasAlpha == true) {
                type = BufferedImage.TYPE_INT_ARGB;
            }
            bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
        }

        // Copy image to buffered image
        Graphics g = bimage.createGraphics();

        // Paint the image onto the buffered image
        g.drawImage(image, 0, 0, null);
        g.dispose();

        return bimage;
    }

    public static boolean hasAlpha(Image image) {
        // If buffered image, the color model is readily available
        if (image instanceof BufferedImage) {
            return ((BufferedImage) image).getColorModel().hasAlpha();
        }

        // Use a pixel grabber to retrieve the image's color model;
        // grabbing a single pixel is usually sufficient
        PixelGrabber pg = new PixelGrabber(image, 0, 0, 1, 1, false);
        try {
            pg.grabPixels();
        } catch (InterruptedException e) {
        }

        // Get the image's color model
        return pg.getColorModel().hasAlpha();
    }
}
