package gov.nih.nci.cagrid.introduce.portal;

import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;

import org.projectmobius.common.MobiusException;
import org.projectmobius.portal.GridPortal;
import org.projectmobius.portal.PortalResourceManager;

/**
* Launch the application.
*
*<P>Perform tasks in this order :
*<ul>
* <li>promptly show a splash screen upon startup
* <li>show the main screen
* <li>remove the splash screen once the main screen is shown
*</ul>
*
* These tasks are performed in a thread-safe manner.
*
* @author <a href="http://www.javapractices.com/">javapractices.com</a>
*/
public final class Introduce {

  /**
  * Launch the application and display the main window.
  *
  * @param aArguments are ignored by this application, and may take any value.
  */
  public static void main (String[] aArguments) {
    Introduce duce = new Introduce();
    showIntroduceSplash();
    showMainWindow();
    EventQueue.invokeLater( new IntroduceSplashCloser() );
  }
  
  // PRIVATE //
  
  private static IntroduceSplash fIntroduceSplash; 

  /**
  * Show a simple graphical splash screen, as a quick preliminary to the main screen.
  */
  private static void showIntroduceSplash(){
    fIntroduceSplash = new IntroduceSplash("/introduceSplash.gif");
    fIntroduceSplash.splash();
  }
  
  /**
  * Display the main window of the application to the user.
  */
  private static void showMainWindow(){
    try {
		GridPortal portal = new GridPortal("conf/introduce/introduce-portal-conf.xml");
		Dimension dim = PortalResourceManager.getInstance().getGridPortalConfig().getApplicationDimensions();
        portal.pack();
        portal.setSize(dim);
        portal.setVisible(true);
        portal.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	} catch (MobiusException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
  }

  /**
  * Removes the splash screen. 
  *
  * Invoke this <code>Runnable</code> using 
  * <code>EventQueue.invokeLater</code>, in order to remove the splash screen
  * in a thread-safe manner.
  */
  private static final class IntroduceSplashCloser implements Runnable {
    public void run(){
      fIntroduceSplash.dispose();
    }
  }
} 

