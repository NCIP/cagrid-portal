package gov.nih.nci.cagrid.common.portal;

import gov.nih.nci.cagrid.common.Utils;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.projectmobius.portal.PortalResourceManager;


/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */

public class PortalUtils {

    public static void showErrorMessage(String msg) {
        showErrorMessage("Portal Error", msg);
    }


    public static void showErrorMessage(Exception e) {
        showErrorMessage("Portal Error", e);
    }


    public static void showConfigurationErrorMessage(String msg) {
        showErrorMessage("Portal Configuration Error", new String[]{msg});
    }


    public static void showMessage(String msg) {
        showMessage(new String[]{msg});
    }


    public static void showMessage(String[] msg) {
        showMessage("Information", msg);
    }


    public static void showMessage(String title, String msg) {
        showMessage(title, new String[]{msg});
    }


    public static void showMessage(String title, String[] msg) {
        JOptionPane.showMessageDialog(PortalResourceManager.getInstance().getGridPortal(), msg, title,
            JOptionPane.INFORMATION_MESSAGE);
    }


    public static void showErrorMessage(String title, Exception e) {
        String mess = Utils.getExceptionMessage(e);
        JOptionPane.showMessageDialog(PortalResourceManager.getInstance().getGridPortal(), mess, title,
            JOptionPane.ERROR_MESSAGE);
    }


    public static void showErrorMessage(String title, String msg) {
        showErrorMessage(title, new String[]{msg});
    }


    public static void showErrorMessage(String title, String[] msg) {
        JOptionPane.showMessageDialog(PortalResourceManager.getInstance().getGridPortal(), msg, title,
            JOptionPane.ERROR_MESSAGE);
    }


    /**
     * Centers the component within the current application
     * 
     * @param comp
     */
    public final static void centerComponent(JComponent comp) {
        // nothing to do for null components
        if (comp == null) {
            return;
        }

        // Determine the new location of the window
        Component root = null;
        // try the portal first
        PortalResourceManager instance = PortalResourceManager.getInstance();
        if (instance != null) {
            root = instance.getGridPortal();
        }
        // if still null try the root window
        if (root == null) {
            root = SwingUtilities.getRoot(comp);
        }

        // only do anything if root is non-null
        if (root != null) {
            int w = root.getSize().width;
            int h = root.getSize().height;
            int x = root.getLocationOnScreen().x;
            int y = root.getLocationOnScreen().y;

            Dimension dim = comp.getSize();
            comp.setLocation(w / 2 + x - dim.width / 2, h / 2 + y - dim.height / 2);
        }
    }


    /**
     * Centers the window on the users monitor
     * 
     * @param comp
     */
    public final static void centerWindowInScreen(Window comp) {
        // Determine the new location of the window
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int w = comp.getSize().width;
        int h = comp.getSize().height;
        int x = (dim.width - w) / 2;
        int y = (dim.height - h) / 2;

        comp.setLocation(x, y);
    }


    /**
     * Centers the window within the current application window
     * 
     * @param comp
     */
    public final static void centerWindow(Window comp) {
        // Determine the new location of the window
        int w = PortalResourceManager.getInstance().getGridPortal().getSize().width;
        int h = PortalResourceManager.getInstance().getGridPortal().getSize().height;
        int x = PortalResourceManager.getInstance().getGridPortal().getLocationOnScreen().x;
        int y = PortalResourceManager.getInstance().getGridPortal().getLocationOnScreen().y;
        Dimension dim = comp.getSize();
        comp.setLocation(w / 2 + x - dim.width / 2, h / 2 + y - dim.height / 2);
    }


    public static void setContainerEnabled(Container con, boolean enable) {
        for (int i = 0; i < con.getComponentCount(); i++) {
            Component comp = con.getComponent(i);
            comp.setEnabled(enable);
            if (comp instanceof Container) {
                setContainerEnabled((Container) comp, enable);
            }
        }
    }
}