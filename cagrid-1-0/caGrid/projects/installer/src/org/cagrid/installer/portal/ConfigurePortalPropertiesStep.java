/**
 * 
 */
package org.cagrid.installer.portal;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cagrid.installer.model.CaGridInstallerModel;
import org.cagrid.installer.steps.Constants;
import org.cagrid.installer.steps.PropertyConfigurationStep;
import org.cagrid.installer.steps.options.TextPropertyConfigurationOption;
import org.cagrid.installer.util.AutoSizingJTable;
import org.cagrid.installer.util.InstallerUtils;
import org.pietschy.wizard.InvalidStateException;
import org.pietschy.wizard.WizardModel;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class ConfigurePortalPropertiesStep extends PropertyConfigurationStep
		implements ActionListener {

	private static final Log logger = LogFactory
			.getLog(ConfigurePortalPropertiesStep.class);

	private JButton idxCmdAdd;

	private JButton idxCmdDelete;

	private DefaultTableModel idxTableModel;

	private JTable idxTable;

	/**
	 * 
	 */
	public ConfigurePortalPropertiesStep() {

	}

	/**
	 * @param name
	 * @param description
	 */
	public ConfigurePortalPropertiesStep(String name, String description) {
		super(name, description);

	}

	/**
	 * @param name
	 * @param description
	 * @param icon
	 */
	public ConfigurePortalPropertiesStep(String name, String description,
			Icon icon) {
		super(name, description, icon);

	}

	public void init(WizardModel m) {

		this.model = (CaGridInstallerModel) m;

		addOptions();

		// Need to call this here so that the options panel is added,
		// and we can add the table panel beneath it.
		super.init(m);
		
		addIndexServiceURLsPanel();

		checkComplete();
	}
	
	protected String getIndexServiceURLsProperty(){
		return Constants.PORTAL_INDEX_SVC_URLS;
	}

	protected void addIndexServiceURLsPanel() {
		JPanel indexSvcUrlsPanel = new JPanel();
		indexSvcUrlsPanel.setLayout(new BorderLayout());
		indexSvcUrlsPanel.setPreferredSize(new Dimension(500, 125));
		GridBagConstraints gbc = InstallerUtils.getGridBagConstraints(0, 2);
		add(indexSvcUrlsPanel, gbc);

		String[] idxSvcUrls = this.model
				.getProperty(getIndexServiceURLsProperty(),
						"http://cagrid-index.nci.nih.gov:8080/wsrf/services/DefaultIndexService")
				.split(",");
		Object[][] rowData = new Object[idxSvcUrls.length][1];
		for (int i = 0; i < idxSvcUrls.length; i++) {
			rowData[i][0] = idxSvcUrls[i];
		}

		JPanel idxButtonPanel = new JPanel();
		this.idxCmdAdd = new JButton(this.model.getMessage("add"));
		this.idxCmdAdd.addActionListener(this);
		this.idxCmdDelete = new JButton(this.model.getMessage("delete"));
		this.idxCmdDelete.addActionListener(this);
		idxButtonPanel.add(this.idxCmdAdd);
		idxButtonPanel.add(this.idxCmdDelete);

		String[] idxColNames = new String[] { "URLs" };
		this.idxTableModel = new DefaultTableModel(rowData, idxColNames);
		this.idxTable = new AutoSizingJTable(this.idxTableModel);
		InstallerUtils.setUpCellRenderer(this.idxTable);
		indexSvcUrlsPanel.add(BorderLayout.NORTH, new JLabel(this.model
				.getMessage("index.svc.urls")));
		indexSvcUrlsPanel.add(BorderLayout.CENTER, new JScrollPane(
				this.idxTable));
		indexSvcUrlsPanel.add(BorderLayout.SOUTH, idxButtonPanel);
		
	}

	protected void addOptions() {
		getOptions().add(
				new TextPropertyConfigurationOption(
						Constants.PORTAL_META_AGG_FREQ, model
								.getMessage("portal.meta.agg.freq"), model
								.getProperty(Constants.PORTAL_META_AGG_FREQ,
										"3600000"), true));

		getOptions().add(
				new TextPropertyConfigurationOption(
						Constants.PORTAL_GOOGLE_MAP_KEY, model
								.getMessage("portal.google.map.key"), model
								.getProperty(Constants.PORTAL_GOOGLE_MAP_KEY,
										""), true));

		getOptions().add(
				new TextPropertyConfigurationOption(
						Constants.PORTAL_GEOCODER_APP_ID, model
								.getMessage("portal.geocoder.app.id"), model
								.getProperty(Constants.PORTAL_GEOCODER_APP_ID,
										""), true));

		getOptions().add(
				new TextPropertyConfigurationOption(
						Constants.PORTAL_CONTEXT_NAME, model
								.getMessage("portal.context.name"), model
								.getProperty(Constants.PORTAL_CONTEXT_NAME,
										""), true));
	}

	public void applyState() throws InvalidStateException {

		StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < this.idxTableModel.getRowCount(); i++) {
			String url = (String) this.idxTableModel.getValueAt(i, 0);
			try {
				new URL(url);
				if(sb.length() > 0){
					sb.append(",");
				}
				sb.append(url);
			} catch (Exception ex) {
				// TODO: externalize error message
				String msg = "'" + url + "' is not a valid URL.";
				logger.error(msg, ex);
				throw new InvalidStateException(msg, ex);
			}
		}
		
		super.applyState();
		
		this.model.setProperty(getIndexServiceURLsProperty(), sb.toString());
	}

	public void actionPerformed(ActionEvent evt) {
		Object source = evt.getSource();
		if (source == this.idxCmdAdd) {
			this.idxTableModel.addRow(new String[0]);
		} else if (source == this.idxCmdDelete) {
			this.idxTableModel.removeRow(this.idxTable.getSelectedRow());
		}
	}

	protected void checkComplete() {

		super.checkComplete();

		boolean aUrlGiven = false;
		if (this.idxTable != null) {
			for (int i = 0; i < this.idxTableModel.getRowCount() && !aUrlGiven; i++) {
				String value = (String) this.idxTableModel.getValueAt(i, 0);
				aUrlGiven = !InstallerUtils.isEmpty(value);
			}
		}
		setComplete(isComplete() && aUrlGiven);
	}
}
