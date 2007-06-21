/**
 * 
 */
package org.cagrid.installer.steps;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cagrid.installer.model.CaGridInstallerModel;
import org.cagrid.installer.tasks.Task;
import org.pietschy.wizard.InvalidStateException;
import org.pietschy.wizard.PanelWizardStep;
import org.pietschy.wizard.WizardModel;
import org.pietschy.wizard.models.Condition;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class RunTasksStep extends PanelWizardStep implements
		PropertyChangeListener {

	private CaGridInstallerModel model;

	private JButton startButton;

	private JLabel busyLabel;

	private JPanel descriptionPanel;

	private JPanel busyPanel;

	private JProgressBar busyProgressBar;

	private List<Task> tasks = new ArrayList<Task>();

	private Exception exception;

	private static final Log logger = LogFactory.getLog(RunTasksStep.class);

	/**
	 * 
	 */
	public RunTasksStep() {
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public RunTasksStep(String name, String description) {
		super(name, description);
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 */
	public RunTasksStep(String name, String description, Icon icon) {
		super(name, description, icon);
	}

	public void init(WizardModel m) {
		if (!(m instanceof CaGridInstallerModel)) {
			throw new IllegalStateException(
					"This step requires a StatefulWizardModel instance.");
		}
		this.model = (CaGridInstallerModel) m;

		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.fill = GridBagConstraints.BOTH;
		gridBagConstraints2.gridy = 1;
		gridBagConstraints2.weightx = 1.0D;
		gridBagConstraints2.weighty = 0.2D;
		gridBagConstraints2.gridx = 0;
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.gridx = 0;
		gridBagConstraints1.weightx = 1.0D;
		gridBagConstraints1.weighty = 1.0D;
		gridBagConstraints1.fill = GridBagConstraints.BOTH;
		gridBagConstraints1.gridy = 0;
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.weightx = 1.0D;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.weighty = 0.0D;
		gridBagConstraints.gridy = 1;
		this.setLayout(new GridBagLayout());
		this.setSize(new Dimension(263, 161));
		this.add(getDescriptionPanel(), gridBagConstraints1);

		this.add(getBusyPanel(), gridBagConstraints2);

	}

	public void applyState() throws InvalidStateException {
		if (this.exception != null) {

			throw new InvalidStateException(
					"This operation did not complete successfully: "
							+ this.exception.getMessage(), this.exception);
		}
	}

	public void prepare() {
		getStartButton().setEnabled(true);
	}

	private JPanel getDescriptionPanel() {
		if (descriptionPanel == null) {
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 0;
			gridBagConstraints3.gridy = 0;
			busyLabel = new JLabel();
			busyLabel.setText("Press Start to begin.");
			descriptionPanel = new JPanel();
			descriptionPanel.setLayout(new GridBagLayout());
			descriptionPanel.setFont(new Font("Dialog", Font.PLAIN, 12));
			descriptionPanel.add(busyLabel, gridBagConstraints3);
		}
		return descriptionPanel;
	}

	public void setBusyLabel(String description) {
		this.busyLabel.setText(description);
	}

	/**
	 * This method initializes busyPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getBusyPanel() {
		if (busyPanel == null) {
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.gridx = 1;
			gridBagConstraints5.gridy = 0;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 0;
			gridBagConstraints4.insets = new Insets(0, 0, 0, 10);
			gridBagConstraints4.gridy = 0;
			busyPanel = new JPanel();
			busyPanel.setLayout(new GridBagLayout());
			busyPanel.add(getBusyProgressBar(), gridBagConstraints4);
			busyPanel.add(getStartButton(), gridBagConstraints5);
		}
		return busyPanel;
	}

	/**
	 * This method initializes busyProgressBar
	 * 
	 * @return javax.swing.JProgressBar
	 */
	private JProgressBar getBusyProgressBar() {
		if (busyProgressBar == null) {
			busyProgressBar = new JProgressBar(0, getTasks().size());
			busyProgressBar.setStringPainted(true);
			busyProgressBar.setValue(0);
			busyProgressBar.setPreferredSize(new Dimension(148, 16));
		}
		return busyProgressBar;
	}

	/**
	 * This method initializes startButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getStartButton() {
		if (startButton == null) {
			startButton = new JButton();
			startButton.setText("Start");
			startButton.setPreferredSize(new Dimension(57, 16));
			startButton.setFont(new Font("Dialog", Font.BOLD, 10));
			final Worker w = new Worker(getTasks(), this.model);
			w.addPropertyChangeListener(this);
			startButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					getStartButton().setEnabled(false);
					w.start();
				}
			});
		}
		return startButton;
	}

	public void propertyChange(PropertyChangeEvent evt) {
		if ("progress" == evt.getPropertyName()) {
			int progress = (Integer) evt.getNewValue();
			getBusyProgressBar().setValue(progress);
			if (progress == getTasks().size()) {
				setBusyLabel("Finished.");
				setComplete(true);
			}
		} else if ("currentTask" == evt.getPropertyName()) {
			Task currentTask = (Task) evt.getNewValue();
			setBusyLabel(currentTask.getName());
		} else if ("exception" == evt.getPropertyName()) {
			this.exception = (Exception) evt.getNewValue();
			setBusyLabel("Error occurred.");
			logger.error("Error occurred", this.exception);
			setComplete(true);
		}
	}

	public List<Task> getTasks() {
		return tasks;
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}

	class Worker extends Thread {

		private PropertyChangeSupport psc;

		private Exception exception;

		private int progress;

		private Task currentTask;

		private CaGridInstallerModel model;

		private List<Task> tasks;

		Worker(List<Task> tasks, CaGridInstallerModel model) {
			this.tasks = tasks;
			this.psc = new PropertyChangeSupport(this);
			this.model = model;
		}

		public void run() {

			setProgress(0);
			for (int i = 0; i < tasks.size(); i++) {
				Task task = this.tasks.get(i);
				boolean runTask = true;
				if (task instanceof Condition) {
					runTask = ((Condition) task).evaluate(this.model);
				}
				if (runTask) {
					logger.info("Running task " + task.getName());
					setCurrentTask(task);
					try {
						task.execute(this.model.getState());
						
					} catch (Exception ex) {
						setException(ex);
					}
				}else{
					logger.info("Skipping task " + task.getName());
				}
				setProgress(i + 1);
			}
		}

		public void addPropertyChangeListener(PropertyChangeListener l) {
			this.psc.addPropertyChangeListener(l);
		}

		public Exception getException() {
			return exception;
		}

		public void setException(Exception exception) {
			Exception oldValue = this.exception;
			this.exception = exception;
			this.psc.firePropertyChange("exception", oldValue, this.exception);
		}

		public int getProgress() {
			return progress;
		}

		public void setProgress(int progress) {
			int oldValue = this.progress;
			this.progress = progress;
			this.psc.firePropertyChange("progress", oldValue, this.progress);
		}

		private void setCurrentTask(Task task) {
			Task oldValue = this.currentTask;
			this.currentTask = task;
			this.psc.firePropertyChange("currentTask", oldValue,
					this.currentTask);
		}
	}

}
