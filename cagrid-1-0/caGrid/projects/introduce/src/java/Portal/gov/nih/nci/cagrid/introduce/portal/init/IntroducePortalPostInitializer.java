package gov.nih.nci.cagrid.introduce.portal.init;

import gov.nih.nci.cagrid.introduce.portal.common.IntroduceLookAndFeel;
import gov.nih.nci.cagrid.introduce.portal.updater.IntroduceUpdateWizard;
import gov.nih.nci.cagrid.introduce.portal.updater.UptodateChecker;

import javax.swing.JOptionPane;

import org.cagrid.grape.ApplicationInitializer;
import org.cagrid.grape.GridApplication;
import org.cagrid.grape.model.Application;

public class IntroducePortalPostInitializer implements ApplicationInitializer {

    org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(ApplicationInitializer.class.getName());
    
    public void intialize(Application app)  {
        
        try{
        if(UptodateChecker.introduceUptodate()){
            int option = JOptionPane.showOptionDialog(GridApplication.getContext().getApplication(), "Updates are available.\nWould you like to view them now?", "Introduce Updates Avaiable", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, IntroduceLookAndFeel.getUpdateIcon(), null, null) ;
            if(option == JOptionPane.YES_OPTION){
                IntroduceUpdateWizard.showUpdateWizard(true);
            }
        }
        } catch (Exception e){
            logger.error("Unable to check for updates:, " + e.getMessage(),e);
        }

    }

}
