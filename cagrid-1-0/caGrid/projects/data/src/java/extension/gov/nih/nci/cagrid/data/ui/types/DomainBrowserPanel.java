package gov.nih.nci.cagrid.data.ui.types;

import gov.nih.nci.cadsr.umlproject.domain.Project;
import gov.nih.nci.cagrid.cadsr.client.CaDSRServiceClient;
import gov.nih.nci.cagrid.cadsr.common.CaDSRServiceI;
import gov.nih.nci.cagrid.cadsr.portal.CaDSRBrowserPanel;
import gov.nih.nci.cagrid.common.portal.BusyDialogRunnable;

import javax.swing.JOptionPane;

import org.projectmobius.portal.PortalResourceManager;


/**
 * DomainBrowserPanel CaDSRBrowserPanel that allows users to set the selected
 * project and package
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created May 10, 2006
 * @version $Id$
 */
public class DomainBrowserPanel extends CaDSRBrowserPanel {

	public DomainBrowserPanel() {
		super();
	}


	/**
	 * @param showQueryPanel
	 * @param showClassSelection
	 */
	public DomainBrowserPanel(boolean showQueryPanel, boolean showClassSelection) {
		super(showQueryPanel, showClassSelection);
	}


	public synchronized void setSelectedProject(String name, String version) {
		System.out.println("Trying to select project " + name + ": " + version);
		for (int i = 0; i < getProjectComboBox().getItemCount(); i++) {
			ProjectDisplay disp = (ProjectDisplay) getProjectComboBox().getItemAt(i);
			if (disp.getProject().getShortName().equals(name) && disp.getProject().getVersion().equals(version)) {
				System.out.println("Project found");
				getProjectComboBox().getModel().setSelectedItem(disp);
				return;
			}
		}
		System.out.println("Project " + name + ": " + version + " not found");
	}


	public synchronized void setSelectedPackage(String pack) {
		System.out.println("Trying to select package " + pack);
		for (int i = 0; i < getPackageComboBox().getItemCount(); i++) {
			PackageDisplay disp = (PackageDisplay) getPackageComboBox().getItemAt(i);
			if (disp.getPackage().getName().equals(pack)) {
				System.out.println("Package found");
				getPackageComboBox().getModel().setSelectedItem(disp);
				return;
			}
		}
		System.out.println("Package " + pack + " not found");
	}


	public synchronized void blockingCadsrRefresh() {
		System.out.println("Refreshing from caDSR, please wait...");
		BusyDialogRunnable refresher = new BusyDialogRunnable(PortalResourceManager.getInstance().getGridPortal(),
			"Loading caDSR information") {
			public void process() {
				setProgressText("Refreshing information from caDSR . . .");
				cadsrRefresh();
			}
		};
		refresher.run();
		System.out.println("Done refreshing from caDSR");
	}
	
	
	public void cadsrRefresh() {
		getProjectComboBox().removeAllItems();
		try {
			CaDSRServiceI cadsrService = new CaDSRServiceClient(getCadsr().getText());
			Project[] projects = cadsrService.findAllProjects();
			if (projects != null) {
				for (int i = 0; i < projects.length; i++) {
					getProjectComboBox().addItem(new ProjectDisplay(projects[i]));
				}
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			JOptionPane.showMessageDialog(DomainBrowserPanel.this,
				"Error communicating with caDSR; please check the caDSR URL!");
		}
	}
}
