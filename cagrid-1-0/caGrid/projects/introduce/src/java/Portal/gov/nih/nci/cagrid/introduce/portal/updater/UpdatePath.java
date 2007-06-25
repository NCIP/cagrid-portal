package gov.nih.nci.cagrid.introduce.portal.updater;

import gov.nih.nci.cagrid.introduce.portal.updater.steps.CheckForUpdatesStep;
import gov.nih.nci.cagrid.introduce.portal.updater.steps.DownloadsUpdatesStep;
import gov.nih.nci.cagrid.introduce.portal.updater.steps.FinishedStep;

import java.util.HashMap;
import java.util.Map;

import org.pietschy.wizard.models.SimplePath;

public class UpdatePath extends SimplePath {

	public UpdatePath(){
		Map globalPropertiesMap = new HashMap();

		
		CheckForUpdatesStep selectStep = new CheckForUpdatesStep();
		DownloadsUpdatesStep downloadStep = new DownloadsUpdatesStep(selectStep);
		this.addStep(selectStep);
		this.addStep(downloadStep);
		this.addStep(new FinishedStep());
		
	}

}
