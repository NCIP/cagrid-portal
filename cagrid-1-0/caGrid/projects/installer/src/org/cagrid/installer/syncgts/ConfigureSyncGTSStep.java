/**
 * 
 */
package org.cagrid.installer.syncgts;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.Properties;

import javax.swing.DefaultCellEditor;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.table.DefaultTableModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cagrid.installer.model.CaGridInstallerModel;
import org.cagrid.installer.steps.Constants;
import org.cagrid.installer.util.AutoSizingJTable;
import org.cagrid.installer.util.InstallerUtils;
import org.pietschy.wizard.InvalidStateException;
import org.pietschy.wizard.PanelWizardStep;
import org.pietschy.wizard.WizardModel;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 */
public class ConfigureSyncGTSStep extends PanelWizardStep implements ActionListener {

    private static final Log logger = LogFactory.getLog(ConfigureSyncGTSStep.class);

    private static final String SYNC_GTS_NS = "http://cagrid.nci.nih.gov/12/SyncGTS";

    private static final String SYNC_GTS_NS_PREFIX = "sync";

    private static final String GTS_NS = "http://cagrid.nci.nih.gov/8/gts";

    private static final String GTS_NS_PREFIX = "gts";

    private static final String XSI_NS_PREFIX = "xsi";

    private static final String XSI_NS = "http://www.w3.org/2001/XMLSchema-instance";

    private static final int NAME_COL = 0;

    private static final int CERT_DN_COL = 1;

    private static final int TRUST_LEVELS_COL = 2;

    private static final int LIFETIME_COL = 3;

    private static final int STATUS_COL = 4;

    private static final int IS_AUTH_COL = 5;

    private static final int AUTH_GTS_COL = 6;

    private static final int SOURCE_GTS_COL = 7;

    private static final String EMPTY_CHOICE = "----";

    protected CaGridInstallerModel model;

    private JTextField gtsServiceURIField;

    private JTextField expirationHoursField;

    private JTextField expirationMinutesField;

    private JTextField expirationSecondsField;

    private JCheckBox performAuthzField;

    private JTextField gtsIdentField;

    private JCheckBox deleteInvalidField;

    private JTextField nextSyncField;

    private JButton tafCmdAdd;

    private JButton tafCmdDelete;

    private DefaultTableModel tafTableModel;

    private JTable tafTable;

    private JButton ecCmdAdd;

    private JButton ecCmdDelete;

    private DefaultTableModel ecTableModel;

    private JTable ecTable;

    private XPathFactory xpFact;

    private JCheckBox performFirstSyncField;


    /**
     * 
     */
    public ConfigureSyncGTSStep() {

    }


    /**
     * @param arg0
     * @param arg1
     */
    public ConfigureSyncGTSStep(String name, String summary) {
        super(name, summary);

    }


    /**
     * @param arg0
     * @param arg1
     * @param arg2
     */
    public ConfigureSyncGTSStep(String name, String summary, Icon icon) {
        super(name, summary, icon);
    }


    public void init(WizardModel m) {

        if (!(m instanceof CaGridInstallerModel)) {
            throw new IllegalStateException("This step requires a StatefulWizardModel instance.");
        }
        this.model = (CaGridInstallerModel) m;

        this.xpFact = XPathFactory.newInstance();

        setLayout(new GridBagLayout());

        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new GridBagLayout());
        add(optionsPanel, InstallerUtils.getGridBagConstraints(0, 0));

        // Add gtsServiceURI field
        String gtsServiceURI = this.model.getProperty(Constants.SYNC_GTS_GTS_URI,
            "https://cagrid02.bmi.ohio-state.edu:8442/wsrf/services/cagrid/GTS");
        this.gtsServiceURIField = new JTextField(gtsServiceURI);
        addRequiredListener(this.gtsServiceURIField);
        JLabel gtsServiceURILabel = new JLabel(this.model.getMessage("sync.gts.gts.uri"));
        addOption(optionsPanel, gtsServiceURILabel, this.gtsServiceURIField, 0);

        // Expiration hours
        String hours = this.model.getProperty(Constants.SYNC_GTS_EXPIRATION_HOURS, "1");
        this.expirationHoursField = new JTextField(hours);
        addRequiredListener(this.expirationHoursField);
        JLabel expirationHoursLabel = new JLabel(this.model.getMessage("sync.gts.expiration.hours"));
        addOption(optionsPanel, expirationHoursLabel, this.expirationHoursField, 1);

        String minutes = this.model.getProperty(Constants.SYNC_GTS_EXPIRATION_MINUTES, "0");
        this.expirationMinutesField = new JTextField(minutes);
        addRequiredListener(this.expirationMinutesField);
        JLabel expirationMinutesLabel = new JLabel(this.model.getMessage("sync.gts.expiration.minutes"));
        addOption(optionsPanel, expirationMinutesLabel, this.expirationMinutesField, 2);

        String seconds = this.model.getProperty(Constants.SYNC_GTS_EXPIRATION_SECONDS, "0");
        this.expirationSecondsField = new JTextField(seconds);
        addRequiredListener(this.expirationSecondsField);
        JLabel expirationSecondsLabel = new JLabel(this.model.getMessage("sync.gts.expiration.seconds"));
        addOption(optionsPanel, expirationSecondsLabel, this.expirationSecondsField, 3);

        String performAuthz = this.model.getProperty(Constants.SYNC_GTS_PERFORM_AUTHZ, Constants.TRUE);
        this.performAuthzField = new JCheckBox();
        this.performAuthzField.setSelected(Constants.TRUE.equals(performAuthz));
        JLabel performAuthzLabel = new JLabel(this.model.getMessage("sync.gts.perform.authz"));
        addOption(optionsPanel, performAuthzLabel, this.performAuthzField, 4);
        this.performAuthzField.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent evt) {
                ConfigureSyncGTSStep.this.checkComplete();
                boolean selected = evt.getStateChange() == ItemEvent.SELECTED;
                ConfigureSyncGTSStep.this.gtsIdentField.setEnabled(selected);
            }

        });

        String gtsIdent = this.model.getProperty(Constants.SYNC_GTS_GTS_IDENT,
            "/O=OSU/OU=BMI/OU=caGrid/OU=Trust Fabric/CN=host/cagrid02.bmi.ohio-state.edu");
        this.gtsIdentField = new JTextField(gtsIdent);
        JLabel gtsIdentLabel = new JLabel(this.model.getMessage("sync.gts.gts.ident"));
        addOption(optionsPanel, gtsIdentLabel, this.gtsIdentField, 5);
        this.gtsIdentField.setEnabled(this.performAuthzField.isSelected());

        String deleteInvalid = this.model.getProperty(Constants.SYNC_GTS_DELETE_INVALID, Constants.FALSE);
        this.deleteInvalidField = new JCheckBox();
        this.deleteInvalidField.setSelected(Constants.TRUE.equals(deleteInvalid));
        JLabel deleteInvalidLabel = new JLabel(this.model.getMessage("sync.gts.delete.invalid"));
        addOption(optionsPanel, deleteInvalidLabel, this.deleteInvalidField, 6);
        this.deleteInvalidField.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent evt) {
                ConfigureSyncGTSStep.this.checkComplete();
            }

        });

        String nextSync = this.model.getProperty(Constants.SYNC_GTS_NEXT_SYNC, "600");
        this.nextSyncField = new JTextField(nextSync);
        addRequiredListener(this.nextSyncField);
        JLabel nextSyncLabel = new JLabel(this.model.getMessage("sync.gts.next.sync"));
        addOption(optionsPanel, nextSyncLabel, this.nextSyncField, 7);

        if (isShowPerformFirstSyncField()) {
            String performFirstSync = this.model.getProperty(Constants.SYNC_GTS_PERFORM_FIRST_SYNC, Constants.TRUE);
            this.performFirstSyncField = new JCheckBox();
            this.performFirstSyncField.setSelected(Constants.TRUE.equals(performFirstSync));
            JLabel performFirstSyncLabel = new JLabel(this.model.getMessage("sync.gts.perform.first.sync"));
            addOption(optionsPanel, performFirstSyncLabel, this.performFirstSyncField, 8);
        }

        JPanel trustedAuthFilterPanel = new JPanel();
        trustedAuthFilterPanel.setLayout(new BorderLayout());
        trustedAuthFilterPanel.setPreferredSize(new Dimension(500, 150));
        add(trustedAuthFilterPanel, InstallerUtils.getGridBagConstraints(0, 1));

        JPanel buttonPanel = new JPanel();
        this.tafCmdAdd = new JButton(this.model.getMessage("add"));
        this.tafCmdAdd.addActionListener(this);
        this.tafCmdDelete = new JButton(this.model.getMessage("delete"));
        this.tafCmdDelete.addActionListener(this);
        buttonPanel.add(this.tafCmdAdd);
        buttonPanel.add(this.tafCmdDelete);

        String[] colNames = new String[]{"Name", "CertificateDN", "TrustLevels", "Lifetime", "Status", "IsAuthority",
                "AuthorityGTS", "SourceGTS"};
        this.tafTableModel = new DefaultTableModel(new Object[0][0], colNames);
        this.tafTable = new AutoSizingJTable(this.tafTableModel);
        InstallerUtils.setUpCellRenderer(this.tafTable);

        JComboBox lifetimeChoices = new JComboBox();
        lifetimeChoices.addItem(EMPTY_CHOICE);
        lifetimeChoices.addItem("Valid");
        lifetimeChoices.addItem("Expired");
        this.tafTable.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(lifetimeChoices));

        JComboBox statusChoices = new JComboBox();
        statusChoices.addItem(EMPTY_CHOICE);
        statusChoices.addItem("Trusted");
        statusChoices.addItem("Suspended");
        this.tafTable.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(statusChoices));

        JComboBox isAuthChoices = new JComboBox();
        isAuthChoices.addItem(EMPTY_CHOICE);
        isAuthChoices.addItem(Constants.TRUE);
        isAuthChoices.addItem(Constants.FALSE);
        this.tafTable.getColumnModel().getColumn(5).setCellEditor(new DefaultCellEditor(isAuthChoices));

        JScrollPane scrollPane = new JScrollPane(this.tafTable);
        trustedAuthFilterPanel.add(BorderLayout.NORTH, new JLabel(this.model.getMessage("sync.gts.auth.filter")));
        trustedAuthFilterPanel.add(BorderLayout.CENTER, scrollPane);
        trustedAuthFilterPanel.add(BorderLayout.SOUTH, buttonPanel);

        JPanel excludedCAsPanel = new JPanel();
        excludedCAsPanel.setLayout(new BorderLayout());
        excludedCAsPanel.setPreferredSize(new Dimension(500, 150));
        add(excludedCAsPanel, InstallerUtils.getGridBagConstraints(0, 2));

        JPanel ecButtonPanel = new JPanel();
        this.ecCmdAdd = new JButton(this.model.getMessage("add"));
        this.ecCmdAdd.addActionListener(this);
        this.ecCmdDelete = new JButton(this.model.getMessage("delete"));
        this.ecCmdDelete.addActionListener(this);
        ecButtonPanel.add(this.ecCmdAdd);
        ecButtonPanel.add(this.ecCmdDelete);

        String[] ecColNames = new String[]{"ExcludedCAs"};
        this.ecTableModel = new DefaultTableModel(new Object[0][0], ecColNames);
        this.ecTable = new AutoSizingJTable(this.ecTableModel);
        InstallerUtils.setUpCellRenderer(this.ecTable);
        excludedCAsPanel.add(BorderLayout.NORTH, new JLabel(this.model.getMessage("sync.gts.excluded.cas")));
        excludedCAsPanel.add(BorderLayout.CENTER, new JScrollPane(this.ecTable));
        excludedCAsPanel.add(BorderLayout.SOUTH, ecButtonPanel);

    }


    protected boolean isShowPerformFirstSyncField() {
        return true;
    }


    private void addOption(JPanel panel, JLabel label, Component field, int y) {
        GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.fill = GridBagConstraints.BOTH;
        gridBagConstraints1.gridy = y;
        panel.add(label, gridBagConstraints1);

        GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
        gridBagConstraints2.gridx = 1;
        gridBagConstraints2.fill = GridBagConstraints.BOTH;
        gridBagConstraints2.gridy = y;
        gridBagConstraints2.weightx = 1;
        gridBagConstraints2.insets = new Insets(2, 5, 2, 2);
        panel.add(field, gridBagConstraints2);

    }


    private void addRequiredListener(JTextField field) {
        field.addCaretListener(new CaretListener() {

            public void caretUpdate(CaretEvent evt) {
                ConfigureSyncGTSStep.this.checkComplete();
            }

        });
    }


    protected void checkComplete() {
        if (!isEmpty(this.gtsServiceURIField) && !isEmpty(this.expirationHoursField)
            && !isEmpty(this.expirationMinutesField) && !isEmpty(this.expirationSecondsField)
            && !isEmpty(this.nextSyncField)) {
            setComplete(true);
        } else {
            setComplete(false);
        }
    }


    private boolean isEmpty(JTextField field) {
        return field.getText() == null || field.getText().trim().length() == 0;
    }


    public void prepare() {
        try {
            // Read in sync-description.xml
            String fileName = getSyncDescriptionFileName();

            logger.info("Looking for " + fileName);

            File syncDescriptionFile = new File(fileName);
            if (syncDescriptionFile.exists()) {

                logger.info("Parsing " + fileName);

                DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();
                fact.setValidating(false);
                fact.setNamespaceAware(true);
                DocumentBuilder builder = fact.newDocumentBuilder();
                Document doc = builder.parse(syncDescriptionFile);
                Element root = doc.getDocumentElement();

                // Get the first SyncDescriptor element
                Element syncDescEl = (Element) xpFact.newXPath().compile(
                    "/*[local-name()='SyncDescription']/*[local-name()='SyncDescriptor'][1]").evaluate(root,
                    XPathConstants.NODE);
                if (syncDescEl != null) {

                    logger.debug("Found SyncDescriptor");

                    // Get gtsServiceURI
                    String gtsServiceURI = getChildElementText(syncDescEl, "gtsServiceURI");
                    logger.debug("Setting gtsServiceURI to '" + gtsServiceURI + "'");
                    this.gtsServiceURIField.setText(InstallerUtils.trim(gtsServiceURI));

                    // Get Expiration
                    Element expirationEl = (Element) xpFact.newXPath().compile("./*[local-name()='Expiration']")
                        .evaluate(syncDescEl, XPathConstants.NODE);
                    if (expirationEl != null) {
                        String hours = getValue(expirationEl.getAttribute("hours"), "1");
                        String minutes = getValue(expirationEl.getAttribute("minutes"), "0");
                        String seconds = getValue(expirationEl.getAttribute("seconds"), "0");
                        logger.debug("Setting Expiration to hours=" + hours + ", minutes=" + minutes + ", seconds="
                            + seconds);
                        this.expirationHoursField.setText(hours);
                        this.expirationMinutesField.setText(minutes);
                        this.expirationSecondsField.setText(seconds);
                    }

                    // Perform authorization
                    Element performAuthzEl = (Element) xpFact.newXPath().compile(
                        "./*[local-name()='PerformAuthorization']").evaluate(syncDescEl, XPathConstants.NODE);
                    if (performAuthzEl != null) {
                        this.performAuthzField.setSelected(Constants.TRUE.equals(performAuthzEl.getTextContent()));
                    }

                    // GTS Identity
                    Element gtsIdentEl = (Element) xpFact.newXPath().compile("./*[local-name()='GTSIdentity']")
                        .evaluate(syncDescEl, XPathConstants.NODE);
                    if (gtsIdentEl != null) {
                        String gtsIdent = gtsIdentEl.getTextContent();
                        if (!InstallerUtils.isEmpty(gtsIdent)) {
                            this.gtsIdentField.setText(InstallerUtils.trim(gtsIdent));
                        }
                    }

                    // Perform first sync

                    // Get TrustedAuthorityFilter elements
                    NodeList filters = (NodeList) xpFact.newXPath().compile(
                        "./*[local-name()='TrustedAuthorityFilter']").evaluate(syncDescEl, XPathConstants.NODESET);
                    logger.debug("Found " + filters.getLength() + " TrustedAuthorityFilter elments.");

                    // Remove existing rows
                    while (this.tafTableModel.getRowCount() > 0) {
                        this.tafTableModel.removeRow(0);
                    }

                    // Create new rows
                    for (int i = 0; i < filters.getLength(); i++) {
                        Element filter = (Element) filters.item(i);
                        String name = getValue(getChildElementText(filter, "Name"), "");
                        String certDN = getValue(getChildElementText(filter, "CertificateDN"), "");

                        // Concat trust levels
                        NodeList levels = (NodeList) xpFact.newXPath().compile(
                            "./*[local-name()='TrustLevels']/*[local-name()='TrustLevel']").evaluate(filter,
                            XPathConstants.NODESET);
                        logger.debug("Found " + levels.getLength() + " trust levels");
                        StringBuilder sb = new StringBuilder();
                        for (int j = 0; j < levels.getLength(); j++) {
                            Element levelEl = (Element) levels.item(j);
                            String level = InstallerUtils.trim(levelEl.getTextContent());
                            sb.append(level);
                            if (j + 1 < levels.getLength()) {
                                sb.append(",");
                            }
                        }
                        String trustLevels = sb.toString();

                        String lifetime = getValue(getChildElementText(filter, "Lifetime"), EMPTY_CHOICE);

                        String status = getValue(getChildElementText(filter, "Status"), EMPTY_CHOICE);

                        String isAuth = getValue(getChildElementText(filter, "IsAuthority"), EMPTY_CHOICE);

                        String authGTS = getValue(getChildElementText(filter, "AuthorityGTS"), "");

                        String sourceGTS = getValue(getChildElementText(filter, "SourceGTS"), "");

                        String[] trustedAuthFilter = new String[]{name, certDN, trustLevels, lifetime, status, isAuth,
                                authGTS, sourceGTS};

                        logger.debug("Adding trusted authority filter: Name=" + name + ", CertificateDN=" + certDN
                            + ", TrustLevels=" + trustLevels + ", Lifetime=" + lifetime + ", Status=" + status
                            + ", IsAuthority=" + isAuth + ", AuthorityGTS=" + authGTS + ", sourceGTS=" + sourceGTS);
                        this.tafTableModel.addRow(trustedAuthFilter);
                    }
                }// done adding SyncDescriptor elements

                // Get ExcludedCAs
                NodeList caSubjEls = (NodeList) xpFact.newXPath().compile(
                    "/*[local-name()='SyncDescription']/*[local-name()='ExcludedCAs']/*[local-name()='CASubject']")
                    .evaluate(root, XPathConstants.NODESET);
                logger.debug("Found " + caSubjEls.getLength() + " excluded CAs.");

                // Remove existing rows
                while (this.ecTableModel.getRowCount() > 0) {
                    this.ecTableModel.removeRow(0);
                }

                // Create new rows
                for (int i = 0; i < caSubjEls.getLength(); i++) {
                    Element caSubjEl = (Element) caSubjEls.item(i);
                    String text = InstallerUtils.trim(caSubjEl.getTextContent());
                    if (text != null) {
                        String[] excludedCARow = new String[]{text};
                        logger.debug("Adding excluded CA: " + text);
                        this.ecTableModel.addRow(excludedCARow);
                    }
                }

                String deleteInvalidFiles = getValue(getChildElementText(root, "DeleteInvalidFiles"), Constants.TRUE);
                logger.debug("Setting DeleteInvalidFiles = " + deleteInvalidFiles);
                this.deleteInvalidField.setSelected(Constants.TRUE.equals(deleteInvalidFiles));

                String nextSync = getValue(getChildElementText(root, "NextSync"), "600");
                logger.debug("Setting NextSync = " + nextSync);
                this.nextSyncField.setText(nextSync);

                if (isShowPerformFirstSyncField()) {
                    String servicePropsFile = this.model.getServiceDestDir() + "/syncgts/service.properties";
                    Properties props = new Properties();
                    props.load(new FileInputStream(servicePropsFile));
                    this.performFirstSyncField
                        .setSelected(Constants.TRUE.equals(props.getProperty("performFirstSync")));
                }

            }
            checkComplete();
        } catch (Exception ex) {
            String msg = "Error preparing syncgts editor: " + ex.getMessage();
            logger.error(msg, ex);
            throw new RuntimeException(msg, ex);
        }
    }


    protected String getSyncDescriptionFileName() {
        return this.model.getServiceDestDir() + "/syncgts/ext/target_grid/sync-description.xml";
    }


    private String getChildElementText(Element parentEl, String childName) {
        String text = null;
        try {
            Element childEl = (Element) this.xpFact.newXPath().compile("./*[local-name()='" + childName + "']")
                .evaluate(parentEl, XPathConstants.NODE);
            if (childEl != null) {
                text = childEl.getTextContent();
            }
        } catch (Exception ex) {
            throw new RuntimeException("Error getting child element text: " + ex.getMessage(), ex);
        }
        return text;
    }


    private String getValue(String value, String defaultValue) {
        return value == null ? defaultValue : value;
    }


    public void applyState() throws InvalidStateException {
        try {
            File syncDescFile = new File(getSyncDescriptionFileName());
            if (syncDescFile.exists()) {
                // String bakFile = syncDescFile.getAbsolutePath() + ".bak";
                // logger.info(syncDescFile + " exists. Copying to " + bakFile);
                // Utils.copyFile(syncDescFile, new File(bakFile));
                logger.info("Deleting " + syncDescFile.getAbsolutePath());
                syncDescFile.delete();
            }

            DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();
            fact.setValidating(false);
            fact.setNamespaceAware(true);
            DocumentBuilder builder = fact.newDocumentBuilder();
            Document doc = builder.newDocument();

            Element root = doc.createElementNS(SYNC_GTS_NS, SYNC_GTS_NS_PREFIX + ":SyncDescription");
            doc.appendChild(root);

            Element syncDescEl = doc.createElementNS(SYNC_GTS_NS, SYNC_GTS_NS_PREFIX + ":SyncDescriptor");
            root.appendChild(syncDescEl);

            Element gtsServiceURIEl = doc.createElementNS(SYNC_GTS_NS, SYNC_GTS_NS_PREFIX + ":gtsServiceURI");
            syncDescEl.appendChild(gtsServiceURIEl);
            gtsServiceURIEl.setTextContent(this.gtsServiceURIField.getText());

            Element expirationEl = doc.createElementNS(SYNC_GTS_NS, SYNC_GTS_NS_PREFIX + ":Expiration");
            syncDescEl.appendChild(expirationEl);
            expirationEl.setAttribute("hours", this.expirationHoursField.getText());
            expirationEl.setAttribute("minutes", this.expirationMinutesField.getText());
            expirationEl.setAttribute("seconds", this.expirationSecondsField.getText());

            int tafRowCount = this.tafTableModel.getRowCount();
            for (int row = 0; row < tafRowCount; row++) {

                Element filterEl = doc.createElementNS(SYNC_GTS_NS, SYNC_GTS_NS_PREFIX + ":TrustedAuthorityFilter");
                filterEl.setAttributeNS(XSI_NS, XSI_NS_PREFIX + ":type", GTS_NS_PREFIX + ":TrustedAuthorityFilter");

                addFilterChildEl(doc, filterEl, row, NAME_COL, "Name");
                addFilterChildEl(doc, filterEl, row, CERT_DN_COL, "CertificateDN");

                // Do the trust levels
                String trustLevelsStr = (String) this.tafTableModel.getValueAt(row, TRUST_LEVELS_COL);
                if (!isEmpty(trustLevelsStr)) {
                    Element trustLevelsEl = doc.createElementNS(GTS_NS, GTS_NS_PREFIX + ":TrustLevels");
                    filterEl.appendChild(trustLevelsEl);
                    trustLevelsEl.setAttributeNS(XSI_NS, XSI_NS_PREFIX + ":type", GTS_NS_PREFIX + ":TrustLevels");
                    String[] trustLevels = trustLevelsStr.split(",");
                    for (int i = 0; i < trustLevels.length; i++) {
                        Element trustLevelEl = doc.createElementNS(GTS_NS, GTS_NS_PREFIX + ":TrustLevel");
                        trustLevelsEl.appendChild(trustLevelEl);
                        trustLevelEl.setAttributeNS(XSI_NS, XSI_NS_PREFIX + ":type", GTS_NS_PREFIX + ":TrustLevel");
                        trustLevelEl.setTextContent(trustLevels[i].trim());
                    }
                }

                addFilterChildEl(doc, filterEl, row, LIFETIME_COL, "Lifetime");
                addFilterChildEl(doc, filterEl, row, STATUS_COL, "Status");
                addFilterChildEl(doc, filterEl, row, IS_AUTH_COL, "IsAuthority");
                addFilterChildEl(doc, filterEl, row, AUTH_GTS_COL, "AuthorityGTS");
                addFilterChildEl(doc, filterEl, row, SOURCE_GTS_COL, "SourceGTS");

                // Add only if there is at least one filter criterion
                if (filterEl.getChildNodes().getLength() > 0) {
                    syncDescEl.appendChild(filterEl);
                }
            }

            String performAuth = String.valueOf(this.performAuthzField.isSelected());
            Element performAuthEl = doc.createElementNS(SYNC_GTS_NS, SYNC_GTS_NS_PREFIX + ":PerformAuthorization");
            syncDescEl.appendChild(performAuthEl);
            performAuthEl.setTextContent(performAuth);

            if (Constants.TRUE.equals(performAuth)) {
                Element gtsIdentEl = doc.createElementNS(SYNC_GTS_NS, SYNC_GTS_NS_PREFIX + ":GTSIdentity");
                syncDescEl.appendChild(gtsIdentEl);
                gtsIdentEl.setTextContent(this.gtsIdentField.getText());
            }

            int ecRowCount = this.ecTableModel.getRowCount();
            Element excludedCAsEl = doc.createElementNS(SYNC_GTS_NS, SYNC_GTS_NS_PREFIX + ":ExcludedCAs");
            for (int row = 0; row < ecRowCount; row++) {
                String caSubj = (String) this.ecTableModel.getValueAt(row, 0);
                if (!isEmpty(caSubj)) {
                    Element caSubjEl = doc.createElementNS(SYNC_GTS_NS, SYNC_GTS_NS_PREFIX + ":CASubject");
                    excludedCAsEl.appendChild(caSubjEl);
                    caSubjEl.setTextContent(caSubj);
                }
            }

            // Append only if has child nodes
            if (excludedCAsEl.getChildNodes().getLength() > 0) {
                root.appendChild(excludedCAsEl);
            }

            String deleteInvalid = String.valueOf(this.deleteInvalidField.isSelected());
            Element deleteInvalidEl = doc.createElementNS(SYNC_GTS_NS, SYNC_GTS_NS_PREFIX + ":DeleteInvalidFiles");
            root.appendChild(deleteInvalidEl);
            deleteInvalidEl.setTextContent(deleteInvalid);

            Element cacheSizeEl = doc.createElementNS(SYNC_GTS_NS, SYNC_GTS_NS_PREFIX + ":CacheSize");
            root.appendChild(cacheSizeEl);
            Element cacheSizeYearEl = doc.createElementNS(SYNC_GTS_NS, SYNC_GTS_NS_PREFIX + ":year");
            cacheSizeEl.appendChild(cacheSizeYearEl);
            cacheSizeYearEl.setTextContent("0");
            Element cacheSizeMonthEl = doc.createElementNS(SYNC_GTS_NS, SYNC_GTS_NS_PREFIX + ":month");
            cacheSizeEl.appendChild(cacheSizeMonthEl);
            cacheSizeMonthEl.setTextContent("1");
            Element cacheSizeDayEl = doc.createElementNS(SYNC_GTS_NS, SYNC_GTS_NS_PREFIX + ":day");
            cacheSizeEl.appendChild(cacheSizeDayEl);
            cacheSizeDayEl.setTextContent("0");

            String nextSync = getValue(this.nextSyncField.getText(), "600");
            Element nextSyncEl = doc.createElementNS(SYNC_GTS_NS, SYNC_GTS_NS_PREFIX + ":NextSync");
            root.appendChild(nextSyncEl);
            nextSyncEl.setTextContent(nextSync);

            // Write to file
            String xml = InstallerUtils.toString(doc);
            FileWriter w = new FileWriter(syncDescFile);
            w.write(xml);
            w.flush();
            w.close();

        } catch (Exception ex) {
            logger.error(ex);
            throw new InvalidStateException("Error configuring sync-description.xml file: " + ex.getMessage(), ex);
        }

        // Also, edit the service.propeties file
        if (isShowPerformFirstSyncField()) {
            String servicePropsFile = this.model.getServiceDestDir() + "/syncgts/service.properties";
            try {
                Properties props = new Properties();
                props.load(new FileInputStream(servicePropsFile));
                props.setProperty("performFirstSync", String.valueOf(this.performFirstSyncField.isSelected()));
                props.store(new FileOutputStream(servicePropsFile), "");
            } catch (Exception ex) {
                logger.error(ex);
                throw new InvalidStateException("Error configuring " + servicePropsFile + ": " + ex.getMessage(), ex);
            }
        }
    }


    private void addFilterChildEl(Document doc, Element filterEl, int row, int col, String elName) {
        String value = (String) this.tafTableModel.getValueAt(row, col);
        if (!isEmpty(value)) {
            Element el = doc.createElementNS(GTS_NS, GTS_NS_PREFIX + ":" + elName);
            filterEl.appendChild(el);
            el.setAttributeNS(XSI_NS, XSI_NS_PREFIX + ":type", GTS_NS_PREFIX + ":" + elName);
            el.setTextContent(value);
        }
    }


    private boolean isEmpty(String value) {
        return value == null || value.trim().length() == 0 || EMPTY_CHOICE.equals(value);
    }


    public void actionPerformed(ActionEvent evt) {
        Object source = evt.getSource();
        if (source == this.tafCmdAdd) {
            this.tafTableModel.addRow(new String[8]);
        } else if (source == this.tafCmdDelete) {
            this.tafTableModel.removeRow(this.tafTable.getSelectedRow());
        } else if (source == this.ecCmdAdd) {
            this.ecTableModel.addRow(new String[8]);
        } else if (source == this.ecCmdDelete) {
            this.ecTableModel.removeRow(this.ecTable.getSelectedRow());
        }
    }
}
