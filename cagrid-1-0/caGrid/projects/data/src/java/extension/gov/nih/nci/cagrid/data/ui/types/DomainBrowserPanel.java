package gov.nih.nci.cagrid.data.ui.types;

import gov.nih.nci.cadsr.umlproject.domain.Project;
import gov.nih.nci.cadsr.umlproject.domain.UMLPackageMetadata;
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


	public synchronized void setSelectedProject(String proj) {
		System.out.println("Trying to select project " + proj);
		for (int i = 0; i < getProjectComboBox().getItemCount(); i++) {
			ProjectDisplay disp = (ProjectDisplay) getProjectComboBox().getItemAt(i);
			if (disp.getProject().getLongName().equals(proj)) {
				System.out.println("Project found");
				getProjectComboBox().getModel().setSelectedItem(disp);
				return;
			}
		}
		System.out.println("Project " + proj + " not found");
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
		};
		refresher.run();
		System.out.println("Done refreshing from caDSR");
	}


	/**
	 * Have to override this to ensure the package combo box gets updated before
	 * somebody calls setSelectedPackage
	 */
	public void handleProjectSelection(final Project project) {
		getPackageComboBox().removeAllItems();
		try {
			CaDSRServiceI cadsrService = new CaDSRServiceClient(getCadsr().getText());
			UMLPackageMetadata[] metadatas = cadsrService.findPackagesInProject(project);
			if (metadatas != null) {
				for (int i = 0; i < metadatas.length; i++) {
					getPackageComboBox().addItem(new PackageDisplay(metadatas[i]));
				}
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			JOptionPane.showMessageDialog(DomainBrowserPanel.this,
				"Error communicating with caDSR; please check the caDSR URL!");
		}
	}
}
