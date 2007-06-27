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
			int progress = this.progressBar.getValue()
					+ calcAdditionalProgress(t);
			updateProgress(progress);
		}
	}

	public void reset() {
		int progress = 0;
		for (Task t : this.tasks) {
			progress += calcAdditionalProgress(t);
		}
		updateProgress(progress);
	}

	private void updateProgress(int progress) {
		this.progressBar.setValue(progress);
	}

	private int calcAdditionalProgress(Task t) {
		int additionalProgress = 0;
		int numSteps = t.getStepCount();
		double taskWeight = this.scale / this.tasks.size();
		double stepWeight = taskWeight / numSteps;
		additionalProgress = (int) stepWeight * t.getLastStep();
		String msg = "Additional progress for " + t.getName() + " is "
				+ additionalProgress + ". numSteps = " + numSteps
				+ ", taskWeight = " + taskWeight + ", stepWeight = "
				+ stepWeight + ", lastStep = " + t.getLastStep();
		logger.debug(msg);

		return additionalProgress;
	}
}
