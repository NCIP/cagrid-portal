package org.cagrid.installer.path;

import java.util.ArrayList;
import java.util.List;

import org.cagrid.installer.steps.PathSelectorStep;
import org.pietschy.wizard.WizardModel;
import org.pietschy.wizard.WizardStep;
import org.pietschy.wizard.models.BranchingPath;
import org.pietschy.wizard.models.Condition;
import org.pietschy.wizard.models.SimplePath;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

public class MasterInstallerPath extends BranchingPath implements InitializingBean {
	
	private List<NamedChainablePath> paths = new ArrayList<NamedChainablePath>();
	private PathSelectorStep pathSelectorStep;
	private WizardStep beginStep;
	private WizardStep endStep;

	public MasterInstallerPath(){
	}

	public void afterPropertiesSet() throws Exception {
		Assert.notNull(getPaths(), "The paths property is required.");
		Assert.notNull(getPathSelectorStep(), "The pathSelectorStep is required.");
		Assert.notNull(getBeginStep(), "The beginStep property is required.");
		Assert.notNull(getEndStep(), "The endStep property is required.");
		
		addStep(getBeginStep());
		
		List<String> pathNames = new ArrayList<String>();
		for(NamedChainablePath path : getPaths()){
			pathNames.add(path.getName());
		}
		final PathSelectorStep selector = getPathSelectorStep();
		addStep(selector);
		
		SimplePath finishedPath = new SimplePath();
		finishedPath.addStep(getEndStep());
		
		NamedChainablePath lastPath = null;
		for(final NamedChainablePath path : getPaths()){
			if(lastPath != null){
				System.out.println("Setting " + path.getName() + " as next path of " + lastPath.getName());
				lastPath.setNextPath(path);
			}
			addBranch(path, new Condition(){
				public boolean evaluate(WizardModel model){
					return selector.isPathSelected(path.getName());
				}
			});
			lastPath = path;
		}
		if(lastPath != null){
			System.out.println("Setting finished path on " + lastPath.getName());
			lastPath.setNextPath(finishedPath);
		}
	}
	
	public WizardStep getBeginStep() {
		return beginStep;
	}

	public void setBeginStep(WizardStep beginStep) {
		this.beginStep = beginStep;
	}

	public WizardStep getEndStep() {
		return endStep;
	}

	public void setEndStep(WizardStep endStep) {
		this.endStep = endStep;
	}

	public List<NamedChainablePath> getPaths() {
		return paths;
	}

	public void setPaths(List<NamedChainablePath> paths) {
		this.paths = paths;
	}

	public PathSelectorStep getPathSelectorStep() {
		return pathSelectorStep;
	}

	public void setPathSelectorStep(PathSelectorStep pathSelectorStep) {
		this.pathSelectorStep = pathSelectorStep;
	}


}
