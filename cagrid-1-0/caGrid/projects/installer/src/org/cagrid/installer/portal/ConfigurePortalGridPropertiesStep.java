/**
 *
 */
package org.cagrid.installer.portal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cagrid.installer.model.CaGridInstallerModel;
import org.cagrid.installer.steps.Constants;
import org.cagrid.installer.steps.PropertyConfigurationStep;
import org.cagrid.installer.steps.options.BooleanPropertyConfigurationOption;
import org.cagrid.installer.steps.options.ListPropertyConfigurationOption;
import org.cagrid.installer.steps.options.TextPropertyConfigurationOption;
import org.cagrid.installer.util.AutoSizingJTable;
import org.cagrid.installer.util.InstallerUtils;
import org.pietschy.wizard.InvalidStateException;
import org.pietschy.wizard.WizardModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class ConfigurePortalGridPropertiesStep extends PropertyConfigurationStep
        implements ActionListener {

    private static final Log logger = LogFactory
            .getLog(ConfigurePortalGridPropertiesStep.class);

    private JButton idxCmdAdd;

    private JButton idxCmdDelete;

    private DefaultTableModel idxTableModel;

    private JTable idxTable;

    //Idp table
    private JButton idpCmdAdd;

    private JButton idpCmdDelete;

    private DefaultTableModel idpTableModel;

    private JTable idpTable;

    /**
     *
     */
    public ConfigurePortalGridPropertiesStep() {

    }

    /**
     * @param name
     * @param description
     */
    public ConfigurePortalGridPropertiesStep(String name, String description) {
        super(name, description);

    }

    /**
     * @param name
     * @param description
     * @param icon
     */
    public ConfigurePortalGridPropertiesStep(String name, String description,
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
        addIdpServcieURLsPanel();

        checkComplete();
    }

    protected String getIndexServiceURLsProperty(){
        return Constants.PORTAL_INDEX_SVC_URLS;
    }


    protected String getIdpServiceURLsProperty(){
           return Constants.PORTAL_IDP_SVC_URLS;
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

    protected void addIdpServcieURLsPanel(){
        JPanel idpSvcUrlsPanel = new JPanel();
        idpSvcUrlsPanel.setLayout(new BorderLayout());
        idpSvcUrlsPanel.setPreferredSize(new Dimension(500, 125));
        GridBagConstraints gbc = InstallerUtils.getGridBagConstraints(0, 3);
        add(idpSvcUrlsPanel, gbc);

        String[] idpSvcUrls = this.model
                .getProperty(getIdpServiceURLsProperty(),
                        "https://cagrid-dorian.nci.nih.gov:8443/wsrf/services/cagrid/Dorian")
                .split(",");
        Object[][] rowData = new Object[idpSvcUrls.length][1];
        for (int i = 0; i < idpSvcUrls.length; i++) {
            rowData[i][0] = idpSvcUrls[i];
        }

        JPanel idpButtonPanel = new JPanel();
        this.idpCmdAdd = new JButton(this.model.getMessage("add"));
        this.idpCmdAdd.addActionListener(this);
        this.idpCmdDelete = new JButton(this.model.getMessage("delete"));
        this.idpCmdDelete.addActionListener(this);
        idpButtonPanel.add(this.idpCmdAdd);
        idpButtonPanel.add(this.idpCmdDelete);

        String[] idpColNames = new String[] { "URLs" };
        this.idpTableModel = new DefaultTableModel(rowData, idpColNames);
        this.idpTable = new AutoSizingJTable(this.idpTableModel);
        InstallerUtils.setUpCellRenderer(this.idpTable);
        idpSvcUrlsPanel.add(BorderLayout.NORTH, new JLabel(this.model
                .getMessage("idp.svc.urls")));
        idpSvcUrlsPanel.add(BorderLayout.CENTER, new JScrollPane(
                this.idpTable));
        idpSvcUrlsPanel.add(BorderLayout.SOUTH, idpButtonPanel);
    }

    protected void addOptions() {

        getOptions().add(
                new ListPropertyConfigurationOption(
                        Constants.PORTAL_AGGR_TARGET_GRID, model
                        .getMessage("portal.aggr.target.grid"),
                        new String[]{
                                "nci_qa",
                                "nci_prod",
                                "nci_dev",
                                "nci_stage",
                                "osu_dev",
                                "training"
                        }));

        getOptions().add(
                new BooleanPropertyConfigurationOption(
                        Constants.PORTAL_TRUST_SYNCHRONIZED_ENABLED, model
                        .getMessage("portal.trust.synchronizeEnabled"),true, false));


        getOptions().add(
                new TextPropertyConfigurationOption(
                        Constants.PORTAL_GME_URL, model
                        .getMessage("portal.gme.url"), model
                        .getProperty(Constants.PORTAL_GME_URL,
                        "http://cagrid-service-qa.nci.nih.gov:8080/wsrf/services/cagrid/GlobalModelExchange"),
                        true));

        getOptions().add(
                new TextPropertyConfigurationOption(
                        Constants.PORTAL_CADSR_URL, model
                        .getMessage("portal.cadsr.url"), model
                        .getProperty(Constants.PORTAL_CADSR_URL,
                        "http://cagrid-service-qa.nci.nih.gov:8080/wsrf/services/cagrid/CaDSRService"),
                        true));

        getOptions().add(
                new TextPropertyConfigurationOption(
                        Constants.PORTAL_IFS_SVC_URL, model
                        .getMessage("portal.ifs.url"), model
                        .getProperty(Constants.PORTAL_IFS_SVC_URL,
                        "https://cagrid-dorian-qa.nci.nih.gov:8443/wsrf/services/cagrid/Dorian"),
                        true));

    }

    public void applyState() throws InvalidStateException {

        StringBuilder sbIdx = new StringBuilder();

        for (int i = 0; i < this.idxTableModel.getRowCount(); i++) {
            String url = (String) this.idxTableModel.getValueAt(i, 0);
            try {
                new URL(url);
                if(sbIdx.length() > 0){
                    sbIdx.append(",");
                }
                sbIdx.append(url);
            } catch (Exception ex) {
                // TODO: externalize error message
                String msg = "'" + url + "' is not a valid URL.";
                logger.error(msg, ex);
                throw new InvalidStateException(msg, ex);
            }
        }

        StringBuilder sbIdp = new StringBuilder();

        for (int i = 0; i < this.idpTableModel.getRowCount(); i++) {
            String url = (String) this.idpTableModel.getValueAt(i, 0);
            try {
                new URL(url);
                if(sbIdp.length() > 0){
                    sbIdp.append(",");
                }
                sbIdp.append(url);
            } catch (Exception ex) {
                // TODO: externalize error message
                String msg = "'" + url + "' is not a valid URL.";
                logger.error(msg, ex);
                throw new InvalidStateException(msg, ex);
            }
        }

        super.applyState();

        //set up a bunch of properties
        this.model.setProperty(Constants.LIFERAY_JBOSS_DIR,model.getProperty(Constants.PORTAL_INSTALL_DIR_PATH)+"/" + Constants.PORTAL_LIFERAY_DIR_NAME);
        //reuse the id of the database for name
        this.model.setProperty(Constants.PORTAL_LIFERAY_DB_NAME, model.getProperty("liferay.db.id"));
        this.model.setProperty(getIndexServiceURLsProperty(), sbIdx.toString());
        this.model.setProperty(getIdpServiceURLsProperty(), sbIdp.toString());

    }

    public void actionPerformed(ActionEvent evt) {
        Object source = evt.getSource();
        if (source == this.idxCmdAdd) {
            this.idxTableModel.addRow(new String[0]);
        } else if (source == this.idxCmdDelete) {
            if(this.idxTable.getSelectedRow()>-1)
            this.idxTableModel.removeRow(this.idxTable.getSelectedRow());
            //idp table
        } else if (source == this.idpCmdAdd) {
            this.idpTableModel.addRow(new String[0]);
        } else if (source == this.idpCmdDelete){
            if(this.idpTable.getSelectedRow()>-1)
            this.idpTableModel.removeRow(this.idpTable.getSelectedRow());
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
        boolean bUrlGiven = false;
        if (this.idpTable != null) {
            for (int i = 0; i < this.idpTableModel.getRowCount() && !bUrlGiven; i++) {
                String value = (String) this.idpTableModel.getValueAt(i, 0);
                bUrlGiven = !InstallerUtils.isEmpty(value);
            }
        }
            setComplete(isComplete() && aUrlGiven && bUrlGiven);
        }
    }
