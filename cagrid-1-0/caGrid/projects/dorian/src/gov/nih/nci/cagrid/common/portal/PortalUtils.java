package gov.nih.nci.cagrid.common.portal;

import javax.swing.JOptionPane;

import org.projectmobius.portal.PortalResourceManager;




/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */

public class PortalUtils{
	
	public static String parseGlobusErrorMessage(Exception e){
		String err = e.getMessage();
		String ex = "java.rmi.RemoteException:";
		if(err!=null){
		int index = err.indexOf(ex);
		if(index>=0){
			err = err.substring(index+ex.length());
		}
		}else{
			err = "Unknown Error";
		}
		return err;
	}
	
	public static void showErrorMessage(String msg){
		showErrorMessage("Portal Error",msg);
	}
	
	public static void showConfigurationErrorMessage(String msg){
		showErrorMessage("Portal Configuration Error",msg);
	}
	
	public static void showMessage(String msg){
		showMessage("Information",msg);
	}
	
	public static void showMessage(String title, String msg){
		JOptionPane.showMessageDialog(PortalResourceManager.getInstance().getGridPortal(),msg,title,JOptionPane.INFORMATION_MESSAGE);
	}
	
	public static void showErrorMessage(String title, String msg){
		JOptionPane.showMessageDialog(PortalResourceManager.getInstance().getGridPortal(),msg,title,JOptionPane.ERROR_MESSAGE);
	}
}