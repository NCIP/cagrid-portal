package org.cagrid.gme.discoverytools;

import gov.nih.nci.cagrid.common.portal.validation.IconFeedbackPanel;
import gov.nih.nci.cagrid.introduce.common.FileFilters;
import gov.nih.nci.cagrid.introduce.common.ResourceManager;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.cagrid.gme.common.FilesystemLoader;
import org.cagrid.gme.domain.XMLSchema;
import org.cagrid.grape.utils.CompositeErrorDialog;

import com.jgoodies.validation.Severity;
import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.ValidationResultModel;
import com.jgoodies.validation.message.SimpleValidationMessage;
import com.jgoodies.validation.util.DefaultValidationResultModel;
import com.jgoodies.validation.view.ValidationComponentUtils;


public class XMLSchemaListPanel extends JPanel implements ListSelectionListener {
    private static final String XSD_LIST = "xsd-list";

    private final ValidationResultModel validationModel = new DefaultValidationResultModel();

    private ValidationResult validationResult = null; // @jve:decl-index=0:

    private List<XMLSchema> xmlSchemas = new ArrayList<XMLSchema>();
    private JPanel mainPanel = null;

    private JPanel xsdListPanel = null;

    private JPanel detailPanel = null;

    private JList xsdList = null;

    private JPanel controlPanel = null;

    private JButton addButton = null;

    private JButton removeButton = null;

    private JTextField xsdErrorLabel = null;

	private JScrollPane xsdScrollPane = null;


    public XMLSchemaListPanel() {
        super();
        this.xsdErrorLabel = new JTextField("Must have at least one xml schema.");
        initialize();
    }


    /**
     * This method initializes this
     */
    private void initialize() {

        this.setLayout(new GridLayout(1,1));
        this.add(IconFeedbackPanel.getWrappedComponentTree(this.validationModel, getMainPanel()));

        this.initValidation();
        this.validateInput();

    }


    public final class TextBoxListener implements DocumentListener {

        public void changedUpdate(DocumentEvent e) {
            validateInput();
        }


        public void insertUpdate(DocumentEvent e) {
            validateInput();
        }


        public void removeUpdate(DocumentEvent e) {
            validateInput();
        }

    }


    private void initValidation() {
        ValidationComponentUtils.setMessageKey(this.xsdErrorLabel, XSD_LIST);

        validateInput();
    }


    private void validateInput() {
        this.validationResult = new ValidationResult();

        if (this.getXMLSchemas() == null || this.getXMLSchemas().isEmpty()) {
            this.validationResult.add(new SimpleValidationMessage("Must have at least one schema.", Severity.ERROR,
                XSD_LIST));
            getXMLSchemaList().setBackground(ValidationComponentUtils.getErrorBackground());
        } else {
            getXMLSchemaList().setBackground(Color.WHITE);

            Map<URI, XMLSchema> processedSchemas = new HashMap<URI, XMLSchema>();

            for (XMLSchema xsd : getXMLSchemas()) {
                if (!processedSchemas.containsKey(xsd.getTargetNamespace())) {
                    processedSchemas.put(xsd.getTargetNamespace(), xsd);
                } else {
                    this.validationResult
                        .add(new SimpleValidationMessage(
                            "The file ("
                                + xsd.getRootDocument().getSystemID()
                                + ") has the same namespace ("
                                + xsd.getTargetNamespace()
                                + ") as an existing file ("
                                + processedSchemas.get(xsd.getTargetNamespace()).getRootDocument().getSystemID()
                                + "); schemas must have unique namespaces.  If you are making use of XML Schema includes or redefines, you should not manually add those documents, they will be automatically added.",
                            Severity.ERROR, XSD_LIST));
                    getXMLSchemaList().setBackground(ValidationComponentUtils.getErrorBackground());
                }
            }

        }

        if (this.getDetailPanel().isEnabled()) {

            // if (ValidationUtils.isBlank(getFnameTextField().getText())) {
            // this.validationResult.add(new
            // SimpleValidationMessage("First name must not be blank.",
            // Severity.ERROR,
            // "first-name"));
            // }

        }

        this.validationModel.setResult(this.validationResult);

        updateComponentTreeSeverity();
    }


    private void updateComponentTreeSeverity() {
        ValidationComponentUtils.updateComponentTreeMandatoryAndBlankBackground(this);
        ValidationComponentUtils.updateComponentTreeSeverityBackground(this, this.validationModel.getResult());
        this.repaint();
    }


    public boolean validatePanel() {
        if (this.validationResult != null && this.validationResult.hasErrors()) {
            return false;
        } else {
            return true;
        }
    }


    private JPanel getMainPanel() {
        if (this.mainPanel == null) {
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.gridx = 0;
            gridBagConstraints1.weightx = 1.0;
            gridBagConstraints1.weighty = 1.0;
            gridBagConstraints1.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints1.gridy = 1;
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridy = 0;
            gridBagConstraints.weightx = 1.0;
            gridBagConstraints.weighty = 1.0;
            gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints.gridx = 0;
            this.mainPanel = new JPanel(new GridBagLayout());
            this.mainPanel.add(getXSDListPanel(), gridBagConstraints);
            this.mainPanel.add(getDetailPanel(), gridBagConstraints1);
        }
        return this.mainPanel;
    }


    public List<XMLSchema> getXMLSchemas() {
        return this.xmlSchemas;
    }


    public void setXMLSchemas(List<XMLSchema> schemas) {
        this.xmlSchemas = schemas;
        updateView();
    }


    protected void updateView() {
        int index = getXMLSchemaList().getSelectedIndex();
        DefaultListModel model = new DefaultListModel();

        if (this.xmlSchemas != null && !this.xmlSchemas.isEmpty()) {
            for (XMLSchema xsd : this.xmlSchemas) {
                model.addElement(new XMLSchemaDisplay(xsd));
            }
        } else {
            model.addElement("At least one schema is required!");
        }

        getXMLSchemaList().setModel(model);
        getXMLSchemaList().setSelectedIndex(index);
        updateXMLSchemaView();

    }


    /**
     * This method initializes pocListPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getXSDListPanel() {
        if (this.xsdListPanel == null) {
            GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
            gridBagConstraints6.fill = GridBagConstraints.BOTH;
            gridBagConstraints6.weighty = 1.0;
            gridBagConstraints6.gridx = 0;
            gridBagConstraints6.gridy = 0;
            gridBagConstraints6.weightx = 1.0;
            GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
            gridBagConstraints3.gridx = 1;
            gridBagConstraints3.weightx = 1.0D;
            gridBagConstraints3.weighty = 1.0D;
            gridBagConstraints3.gridheight = 1;
            gridBagConstraints3.fill = GridBagConstraints.BOTH;
            gridBagConstraints3.gridy = 0;
            GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
            gridBagConstraints2.fill = java.awt.GridBagConstraints.BOTH;
            gridBagConstraints2.gridx = 0;
            gridBagConstraints2.gridy = 0;
            gridBagConstraints2.weightx = 1.0D;
            gridBagConstraints2.insets = new Insets(2, 2, 2, 2);
            gridBagConstraints2.weighty = 1.0D;
            this.xsdListPanel = new JPanel();
            this.xsdListPanel.setLayout(new GridBagLayout());
            this.xsdListPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Schemas to Publish",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
            this.xsdListPanel.add(getControlPanel(), gridBagConstraints3);
            xsdListPanel.add(getXsdScrollPane(), gridBagConstraints6);
        }
        return this.xsdListPanel;
    }


    /**
     * This method initializes detailPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getDetailPanel() {
        if (this.detailPanel == null) {

            this.detailPanel = new JPanel();
            this.detailPanel.setLayout(new GridBagLayout());
            this.detailPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null,
                "Documents comprising selected Schema", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
        }
        return this.detailPanel;
    }


    /**
     * This method initializes pocList
     * 
     * @return javax.swing.JList
     */
    private JList getXMLSchemaList() {
        if (this.xsdList == null) {
            DefaultListModel model = new DefaultListModel();
            this.xsdList = new JList(model);
            this.xsdList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
            this.xsdList.addListSelectionListener(this);
            this.xsdList.setVisibleRowCount(8);

        }
        return this.xsdList;
    }


    /**
     * This method initializes controlPanel
     * 
     * @return javax.swing.JPanel
     */
    private JPanel getControlPanel() {
        if (this.controlPanel == null) {
            GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
            gridBagConstraints5.insets = new java.awt.Insets(5, 5, 5, 5);
            gridBagConstraints5.gridy = 1;
            gridBagConstraints5.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints5.gridx = 0;
            GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
            gridBagConstraints4.insets = new java.awt.Insets(5, 5, 5, 5);
            gridBagConstraints4.gridy = 0;
            gridBagConstraints4.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints4.gridx = 0;
            this.controlPanel = new JPanel();
            this.controlPanel.setLayout(new GridBagLayout());
            this.controlPanel.add(getAddButton(), gridBagConstraints4);
            this.controlPanel.add(getRemoveButton(), gridBagConstraints5);
        }
        return this.controlPanel;
    }


    /**
     * This method initializes addButton
     * 
     * @return javax.swing.JButton
     */
    private JButton getAddButton() {
        if (this.addButton == null) {
            this.addButton = new JButton();
            this.addButton.setText("Add Schemas...");
            this.addButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    try {
                        String[] files = ResourceManager.promptMultiFiles(SwingUtilities
                            .getRoot(XMLSchemaListPanel.this), null, FileFilters.XSD_FILTER);

                        if (files != null) {
                            List<File> fileList = new ArrayList<File>();
                            for (String fileName : files) {
                                fileList.add(new File(fileName));
                            }

                            FilesystemLoader loader = new FilesystemLoader(fileList);
                            List<XMLSchema> loadedSchemas = loader.loadSchemas();
                            XMLSchemaListPanel.this.addXMLSchemas(loadedSchemas);
                        } else {
                            return;
                        }

                    } catch (IOException e1) {
                        CompositeErrorDialog.showErrorDialog("Error selecting schema file", e1);
                    }
                }
            });
        }
        return this.addButton;
    }


    protected void addXMLSchemas(List<XMLSchema> xsds) {
        this.xmlSchemas.addAll(xsds);
        updateView();
        this.getXMLSchemaList().setSelectedIndex(getXMLSchemaList().getModel().getSize() - 1);
    }


    protected void addXMLSchema(XMLSchema xsd) {
        this.xmlSchemas.add(xsd);
        updateView();
        this.getXMLSchemaList().setSelectedIndex(getXMLSchemaList().getModel().getSize() - 1);
    }


    /**
     * This method initializes removeButton
     * 
     * @return javax.swing.JButton
     */
    private JButton getRemoveButton() {
        if (this.removeButton == null) {
            this.removeButton = new JButton();
            this.removeButton.setText("Remove Selected Schemas");
            this.removeButton.setEnabled(false);
            this.removeButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    removeSelectedXMLSchema();
                }
            });
        }
        return this.removeButton;
    }


    protected void removeSelectedXMLSchema() {
        int selectedIndex = getXMLSchemaList().getSelectedIndex();
        if (selectedIndex != -1) {
            this.xmlSchemas.remove(selectedIndex);
            if (getXMLSchemaList().getModel().getSize() >= 0) {
                if (selectedIndex > 0) {
                    getXMLSchemaList().setSelectedIndex(selectedIndex - 1);
                } else {
                    getXMLSchemaList().setSelectedIndex(0);
                }
            }
        }
        updateView();
    }


    protected void updateXSDModel() {
        XMLSchemaDisplay xsdD = (XMLSchemaDisplay) getXMLSchemaList().getSelectedValue();
        if (xsdD == null) {
            return;
        }
        XMLSchema xsd = xsdD.getXMLSchema();
        // xsd.setFirstName(getFnameTextField().getText());

        validateInput();
    }


    protected class XMLSchemaDisplay {
        XMLSchema schema;


        public XMLSchemaDisplay(XMLSchema schema) {
            this.schema = schema;
        }


        @Override
        public String toString() {
            if (this.schema == null) {
                return "null";
            }

            return this.schema.getRootDocument().getSystemID() + " - " + this.schema.getTargetNamespace().toString();
        }


        public XMLSchema getXMLSchema() {
            return this.schema;
        }


        public void setXMLSchema(XMLSchema poc) {
            this.schema = poc;
        }
    }


    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting() == false) {
            updateXMLSchemaView();
        }
    }


    private void setInfoFieldsEnabled(boolean enabled) {
        getDetailPanel().setEnabled(enabled);
        for (Component c : getDetailPanel().getComponents()) {
            c.setEnabled(enabled);
        }
        validateInput();
    }


    private void updateXMLSchemaView() {
        if (getXMLSchemaList().getSelectedValue() instanceof XMLSchemaDisplay) {
            XMLSchemaDisplay xsdD = (XMLSchemaDisplay) getXMLSchemaList().getSelectedValue();
            XMLSchema xsd = null;
            if (xsdD != null) {
                xsd = xsdD.getXMLSchema();
            }
            String fname = null;

            if (xsd == null) {
                getRemoveButton().setEnabled(false);
                setInfoFieldsEnabled(false);

            } else {
                getRemoveButton().setEnabled(true);
                setInfoFieldsEnabled(true);

                // get the values to use
                // fname = xsd.getFirstName();

            }

            // getFnameTextField().setText(fname);

        } else {
            setInfoFieldsEnabled(false);
        }

        validateInput();
    }


	/**
	 * This method initializes xsdScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getXsdScrollPane() {
		if (xsdScrollPane == null) {
			xsdScrollPane = new JScrollPane();
			xsdScrollPane.setViewportView(getXMLSchemaList());
		}
		return xsdScrollPane;
	}
} // @jve:decl-index=0:visual-constraint="10,10"
