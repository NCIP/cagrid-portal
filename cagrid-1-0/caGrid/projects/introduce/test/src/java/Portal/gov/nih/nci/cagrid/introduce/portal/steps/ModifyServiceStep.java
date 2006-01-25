package gov.nih.nci.cagrid.introduce.portal.steps;

import org.projectmobius.portal.GridPortal;
import org.projectmobius.portal.PortalResourceManager;
import org.uispec4j.Panel;
import org.uispec4j.Trigger;
import org.uispec4j.Window;
import org.uispec4j.interception.WindowHandler;
import org.uispec4j.interception.WindowInterceptor;

import com.atomicobject.haste.framework.Step;

public class ModifyServiceStep extends Step {
	Window mainWindow;

	public ModifyServiceStep(Window mainWindow) {
		this.mainWindow = mainWindow;
	}

	public void runStep() throws Throwable {
		System.out.println("Modifying a service");
		WindowInterceptor.init(mainWindow.getMenuBar().getMenu("Tools")
				.getSubMenu("Modify").triggerClick())
		.process(new WindowHandler() {
			public Trigger process(Window window) {
				window.getTextBox().setText("HelloWorld");
				return window.getButton("Open").triggerClick();
			}
		}).run();
		
		Thread.sleep(3000);
		GridPortal portal = PortalResourceManager.getInstance().getGridPortal();
		Panel panel = new Panel(portal.getLastComponent());
		System.out.println(panel.getDescription());
	}
}
