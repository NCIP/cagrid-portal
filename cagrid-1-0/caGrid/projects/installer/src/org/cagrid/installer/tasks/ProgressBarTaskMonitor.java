/**
 * 
 */
package org.cagrid.installer.tasks;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JProgressBar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class ProgressBarTaskMonitor implements PropertyChangeListener {

	private static final Log logger = LogFactory
			.getLog(ProgressBarTaskMonitor.class);

	private JProgressBar progressBar;

	private List<Task> tasks = new ArrayList<Task>();

	private int scale;

	private double actualProgress;

	/**
	 * 
	 */
	public ProgressBarTaskMonitor(JProgressBar progressBar, int scale) {
		this.progressBar = progressBar;
		this.progressBar.setMaximum(scale);
		this.scale = scale;
	}

	public void addTask(Task t) {
		logger.info("Adding task: " + t.getName());
		t.addPropertyChangeListener(this);
		this.tasks.add(t);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		if ("stepCount".equals(evt.getPropertyName())) {
			reset();
		} else if ("lastStep".equals(evt.getPropertyName())) {
			Task t = (Task) evt.getSource();
			this.actualProgress += calcAdditionalProgress(t);
			updateProgress((int)Math.round(this.actualProgress));
		}
	}

	public void reset() {
		this.actualProgress = (double)0;
		for (Task t : this.tasks) {
			this.actualProgress += calcAdditionalProgress(t);
		}
		updateProgress((int)Math.round(this.actualProgress));
	}

	private void updateProgress(int progress) {
		this.progressBar.setValue(progress);
	}

	private double calcAdditionalProgress(Task t) {
		double additionalProgress = 0;
		int numSteps = t.getStepCount();
		double taskWeight = this.scale / (double)this.tasks.size();
		double stepWeight = taskWeight / numSteps;
		additionalProgress = stepWeight * t.getLastStep();
//		String msg = "Additional progress for " + t.getName() + " is "
//				+ additionalProgress + ". numSteps = " + numSteps
//				+ ", taskWeight = " + taskWeight + ", stepWeight = "
//				+ stepWeight + ", lastStep = " + t.getLastStep();
//		logger.debug(msg);

		return additionalProgress;
	}
	
	
}
