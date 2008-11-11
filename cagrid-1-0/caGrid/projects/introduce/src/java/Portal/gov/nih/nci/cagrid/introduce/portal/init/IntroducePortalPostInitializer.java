package gov.nih.nci.cagrid.introduce.portal.init;

import gov.nih.nci.cagrid.introduce.portal.updater.UptodateChecker;

import javax.swing.JOptionPane;

import org.cagrid.grape.ApplicationInitializer;
import org.cagrid.grape.GridApplication;
import org.cagrid.grape.model.Application;

public class IntroducePortalPostInitializer implements ApplicationInitializer {

    public void intialize(Application app) throws Exception {
        if(!UptodateChecker.introduceUptodate()){
            JOptionPane.showMessageDialog(GridApplication.getContext().getApplication(), "Updates are available for Introduce.\nPlease go the Help-->Software Updates menu to view and update your Introduce.");
        }

    }

}
