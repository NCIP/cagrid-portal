package gov.nih.nci.cagrid.introduce.portal.steps;

import org.projectmobius.portal.GridPortal;
import org.projectmobius.portal.PortalResourceManager;
import org.uispec4j.Panel;
import org.uispec4j.Trigger;
import org.uispec4j.Window;
import org.uispec4j.interception.WindowHandler;
import org.uispec4j.interception.WindowInterceptor;

import com.atomicobject.haste.framework.Step;

public class CreateServiceStep extends Step {
	Window mainWindow;

	public CreateServiceStep(Window mainWindow) {
		this.mainWindow = mainWindow;
	}

	public void runStep() throws Throwable {
		System.out.println("Creating a service");
		mainWindow.getMenuBar().getMenu("Tools").getSubMenu(
				"Create Service").click();
		Thread.sleep(3000);
		GridPortal portal = PortalResourceManager.getInstance().getGridPortal();
		Panel panel = new Panel(portal.getLastComponent());
		System.out.println(panel.getDescription());
		WindowInterceptor.init(panel.getButton("Create").triggerClick())
				.process(new WindowHandler() {
					public Trigger process(Window window) {
						int count = 0;
						while (window.isVisible()) {
							try {
								Thread.sleep(3);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							count++;
							if (count >= 100) {
								assertFalse(
										"Service Creation took longer than 300 seconds....",
										false);
							}
						}
						// return empty trigger because this modal dialog closes
						// itself
						return new Trigger() {
							public void run() throws Exception {
							}
						};
					}
				}).run();

		//close out the modification panel the pops up.....
		Panel modificationPanel = new Panel(portal.getLastComponent());
		modificationPanel.getButton("Cancel").click();
	}

}
