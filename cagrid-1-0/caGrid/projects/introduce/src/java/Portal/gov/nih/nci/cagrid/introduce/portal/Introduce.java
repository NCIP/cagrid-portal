package gov.nih.nci.cagrid.introduce.portal;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.common.portal.SplashScreen;
import gov.nih.nci.cagrid.introduce.common.IntroducePropertiesManager;

import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;

import org.apache.log4j.Logger;
import org.cagrid.grape.GridApplication;
import org.cagrid.grape.model.Application;


public final class Introduce {
    
    private static final Logger logger = Logger.getLogger(Introduce.class);

    private static SplashScreen introduceSplash;


    private static void showIntroduceSplash() {
        try {
            introduceSplash = new SplashScreen("/introduceSplash.png");
            // centers in screen
            introduceSplash.setLocationRelativeTo(null);
            introduceSplash.setVisible(true);
        } catch (Exception e) {
            logger.error(e);
        }
    }


    private static void initialize() {

    }


    private static void showGridPortal(String confFile) {
        try {
            initialize();
            showIntroduceSplash();

            if (confFile == null) {
                confFile = IntroducePropertiesManager.getIntroduceConfigurationFile();
            }

            Application app = (Application) Utils.deserializeDocument(confFile, Application.class);

            // launch the portal with the passed config
            GridApplication applicationInstance = GridApplication.getInstance(app);
            Dimension d = new Dimension(app.getDimensions().getWidth(), app.getDimensions().getHeight());

            try {
                applicationInstance.pack();
            } catch (Exception e) {
                applicationInstance.setIconImage(null);
                applicationInstance.pack();
            }
            applicationInstance.setSize(d);
            applicationInstance.setVisible(true);
            applicationInstance.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    private static final class IntroduceSplashCloser implements Runnable {
        public void run() {
            try {
                introduceSplash.dispose();
            } catch (Exception e) {

            }
        }
    }


    public static void main(String[] args) {
        if (args.length > 0) {
            showGridPortal(args[0]);
        } else {
            showGridPortal(null);
        }
        EventQueue.invokeLater(new IntroduceSplashCloser());
    }

}
