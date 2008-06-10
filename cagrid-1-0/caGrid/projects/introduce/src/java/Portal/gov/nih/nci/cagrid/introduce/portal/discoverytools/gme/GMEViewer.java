package gov.nih.nci.cagrid.introduce.portal.discoverytools.gme;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.common.portal.PortalLookAndFeel;
import gov.nih.nci.cagrid.introduce.beans.extension.DiscoveryExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionDescription;
import gov.nih.nci.cagrid.introduce.codegen.SyncTools;
import gov.nih.nci.cagrid.introduce.common.ConfigurationUtil;
import gov.nih.nci.cagrid.introduce.common.FileFilters;
import gov.nih.nci.cagrid.introduce.common.ResourceManager;
import gov.nih.nci.cagrid.introduce.portal.common.jedit.JEditTextArea;
import gov.nih.nci.cagrid.introduce.portal.common.jedit.XMLTokenMarker;
import gov.nih.nci.cagrid.introduce.portal.discoverytools.NamespaceTypeToolsComponent;
import gov.nih.nci.cagrid.introduce.portal.discoverytools.gme.GMESchemaLocatorPanel.SchemaWrapper;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import org.apache.log4j.Logger;
import org.apache.tools.ant.filters.StringInputStream;
import org.cagrid.grape.utils.CompositeErrorDialog;
import org.projectmobius.castor.xml.schema.Schema;
import org.projectmobius.castor.xml.schema.reader.SchemaReader;
import org.projectmobius.common.GridServiceResolver;
import org.projectmobius.common.MobiusException;
import org.projectmobius.common.Namespace;
import org.projectmobius.common.XMLUtilities;
import org.projectmobius.common.gme.NamespaceExistsException;
import org.projectmobius.gme.XMLDataModelService;
import org.projectmobius.gme.client.GlobusGMEXMLDataModelServiceFactory;
import org.xml.sax.InputSource;


/**
 * CreationViewer
 * 
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @created Jun 22, 2005
 * @version $Id: mobiusEclipseCodeTemplates.xml,v 1.2 2005/04/19 14:58:02 oster
 *          Exp $
 */
public class GMEViewer extends NamespaceTypeToolsComponent {
    
    private static final Logger logger = Logger.getLogger(GMEViewer.class);

	private JPanel mainPanel = null;
	private JTabbedPane gmeToolsTabs = null;
	private JPanel schemaUploadPanel = null;
	private JPanel gmeBrowsePanel = null;
	private JPanel schemaViewer = null;
	private JPanel gmeViewerPanel = null;
	private GMESchemaLocatorPanel gmeSchemaLocatorPanel = null;
	private JButton uploadBrowseButton = null;
	private JTextField uploadLocationText = null;
	private JButton uploadUploadButton = null;
	private JPanel uploadSchemaViewPanel = null;
	private JPanel gmeBrowseButtonPanel = null;
	private JButton gmeDownloadButton = null;
	private JEditTextArea uploadSchemaTextPane = null;
	private JEditTextArea schemaTextPane = null;


	/**
	 * This method initializes
	 */
	public GMEViewer(DiscoveryExtensionDescriptionType descriptor) {
		super(descriptor);
		initialize();
		getGmeSchemaLocatorPanel().discoverFromGME();
	}


	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
		gridBagConstraints11.insets = new java.awt.Insets(5, 0, 0, 0);
		gridBagConstraints11.gridy = 0;
		gridBagConstraints11.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints11.weightx = 1.0D;
		gridBagConstraints11.weighty = 1.0D;
		gridBagConstraints11.gridx = 0;
		this.setLayout(new GridBagLayout());
		this.add(getMainPanel(), gridBagConstraints11);
	}


	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getMainPanel() {
		if (mainPanel == null) {
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.weighty = 1.0;
			gridBagConstraints.gridx = 0;
			mainPanel = new JPanel();
			mainPanel.setLayout(new GridBagLayout());
			mainPanel.add(getGmeToolsTabs(), gridBagConstraints);
		}
		return mainPanel;
	}


	/**
	 * This method initializes gmeToolsTabs
	 * 
	 * @return javax.swing.JTabbedPane
	 */
	private JTabbedPane getGmeToolsTabs() {
		if (gmeToolsTabs == null) {
			gmeToolsTabs = new JTabbedPane();
			gmeToolsTabs.addTab("Browse", null, getGmeBrowsePanel(), null);
			gmeToolsTabs.addTab("Upload", null, getSchemaUploadPanel(), null);
		}
		return gmeToolsTabs;
	}


	/**
	 * This method initializes schemaUploadPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getSchemaUploadPanel() {
		if (schemaUploadPanel == null) {
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.gridx = 0;
			gridBagConstraints8.gridwidth = 3;
			gridBagConstraints8.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints8.weighty = 1.0D;
			gridBagConstraints8.gridy = 1;
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.gridx = 2;
			gridBagConstraints7.gridy = 0;
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints6.gridy = 0;
			gridBagConstraints6.weightx = 1.0;
			gridBagConstraints6.gridx = 1;
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.gridx = 0;
			gridBagConstraints5.gridy = 0;
			schemaUploadPanel = new JPanel();
			schemaUploadPanel.setLayout(new GridBagLayout());
			schemaUploadPanel.add(getUploadBrowseButton(), gridBagConstraints5);
			schemaUploadPanel.add(getUploadLocationText(), gridBagConstraints6);
			schemaUploadPanel.add(getUploadUploadButton(), gridBagConstraints7);
			schemaUploadPanel.add(getUploadSchemaViewPanel(), gridBagConstraints8);
		}
		return schemaUploadPanel;
	}


	/**
	 * This method initializes gmeBrowsePanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getGmeBrowsePanel() {
		if (gmeBrowsePanel == null) {
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.gridx = 0;
			gridBagConstraints10.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints10.gridy = 2;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints1.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints1.weightx = 0.0D;
			gridBagConstraints1.weighty = 0.0D;
			gridBagConstraints1.gridy = 0;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints2.insets = new java.awt.Insets(2, 2, 2, 2);
			gridBagConstraints2.weightx = 1.0D;
			gridBagConstraints2.weighty = 1.0D;
			gridBagConstraints2.gridy = 1;
			gmeBrowsePanel = new JPanel();
			gmeBrowsePanel.setLayout(new GridBagLayout());
			gmeBrowsePanel.add(getSchemaViewer(), gridBagConstraints2);
			gmeBrowsePanel.add(getGmeViewerPanel(), gridBagConstraints1);
			gmeBrowsePanel.add(getGmeBrowseButtonPanel(), gridBagConstraints10);
		}
		return gmeBrowsePanel;
	}


	/**
	 * This method initializes schemaViewer
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getSchemaViewer() {
		if (schemaViewer == null) {
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints3.gridy = 0;
			gridBagConstraints3.weightx = 1.0;
			gridBagConstraints3.weighty = 1.0;
			gridBagConstraints3.gridx = 0;
			schemaViewer = new JPanel();
			schemaViewer.setLayout(new GridBagLayout());
			schemaViewer.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Schema Text",
				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.DEFAULT_POSITION, null, PortalLookAndFeel.getPanelLabelColor()));
			schemaViewer.add(getSchemaTextPane(), gridBagConstraints3);
		}
		return schemaViewer;
	}


	/**
	 * This method initializes gmeViewerPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getGmeViewerPanel() {
		if (gmeViewerPanel == null) {
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 0;
			gridBagConstraints4.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints4.weightx = 1.0D;
			gridBagConstraints4.weighty = 1.0D;
			gridBagConstraints4.gridy = 0;
			gmeViewerPanel = new JPanel();
			gmeViewerPanel.setLayout(new GridBagLayout());
			gmeViewerPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Schema Locator",
				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.DEFAULT_POSITION, null, PortalLookAndFeel.getPanelLabelColor()));
			gmeViewerPanel.add(getGmeSchemaLocatorPanel(), gridBagConstraints4);
		}
		return gmeViewerPanel;
	}


	/**
	 * This method initializes gmeSchemaLocatorPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private GMESchemaLocatorPanel getGmeSchemaLocatorPanel() {
		if (gmeSchemaLocatorPanel == null) {
			gmeSchemaLocatorPanel = new GMESchemaLocatorPanel(false);
			gmeSchemaLocatorPanel.getSchemaComboBox().addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if (gmeSchemaLocatorPanel.currentNode != null) {
						try {
							getSchemaTextPane().setText(
								XMLUtilities.formatXML(
                                    gmeSchemaLocatorPanel.currentNode.getSchemaContents()));
						} catch (MobiusException e1) {
							e1.printStackTrace();
						}
						getSchemaTextPane().setCaretPosition(0);
					}
				}
			});
		}
		return gmeSchemaLocatorPanel;
	}


	/**
	 * This method initializes uploadBrowseButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getUploadBrowseButton() {
		if (uploadBrowseButton == null) {
			uploadBrowseButton = new JButton();
			uploadBrowseButton.setText("Browse");
			uploadBrowseButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					String location = null;
					try {
						location = ResourceManager.promptFile(null, FileFilters.XSD_FILTER);
						if (location == null) {
							return;
						}
						uploadLocationText.setText(location);
					} catch (Exception e1) {
						CompositeErrorDialog.showErrorDialog("Error selecting schema file", e1);
					}
					File file = new File(location);
					if (file.exists() && file.canRead()) {
						try {
							StringBuffer buf = Utils.fileToStringBuffer(file);
							getUploadSchemaTextPane().setText(XMLUtilities.formatXML(buf.toString()));
							getUploadSchemaTextPane().setCaretPosition(0);
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					} else {
						CompositeErrorDialog.showErrorDialog("Cannot read selected file or file does not exist!");
						uploadLocationText.setText("");
					}
				}
			});
		}
		return uploadBrowseButton;
	}


	/**
	 * This method initializes uploadLocationText
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getUploadLocationText() {
		if (uploadLocationText == null) {
			uploadLocationText = new JTextField();
			uploadLocationText.setEnabled(false);
			uploadLocationText.setEditable(false);
		}
		return uploadLocationText;
	}


	/**
	 * This method initializes uploadUploadButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getUploadUploadButton() {
		if (uploadUploadButton == null) {
			uploadUploadButton = new JButton();
			uploadUploadButton.setText("Upload");
			uploadUploadButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if (uploadSchemaTextPane.getText().length() <= 0) {
						JOptionPane.showMessageDialog(GMEViewer.this, "Please select a schema");
						return;
					}

					GridServiceResolver.getInstance().setDefaultFactory(new GlobusGMEXMLDataModelServiceFactory());

					try {
						XMLDataModelService handle = (XMLDataModelService) GridServiceResolver.getInstance()
							.getGridService(
								ConfigurationUtil.getGlobalExtensionProperty(GMESchemaLocatorPanel.GME_URL).getValue());

						// get the target namespace of the schema and make sure
						// that it is acceptable by the GME

						// Load the schema into the schema object
						SchemaReader sr = new SchemaReader(new InputSource(new StringInputStream(
							uploadSchemaTextPane.getText())));
						sr.setValidation(false);
						Schema schema = sr.read();
						Namespace schemaTargetNamespace = new Namespace(schema.getTargetNamespace());
						try {
							handle.addNamespaceDomain(schemaTargetNamespace.getDomain());
						} catch (NamespaceExistsException ex) {
							// should be ok here do nothing.........
						}
						handle.publishSchema(uploadSchemaTextPane.getText());
						JOptionPane.showMessageDialog(GMEViewer.this, "Schema was successfully uploaded.");
					} catch (Exception e1) {
						e1.printStackTrace();
						CompositeErrorDialog.showErrorDialog("Error contacting GME", "Please check the GME URL and make sure that you have " +
							"the appropriate credentials " + "and make sure the schema is well formed!");
					}
					uploadLocationText.setText("");
					uploadSchemaTextPane.setText("");
				}
			});
		}
		return uploadUploadButton;
	}


	/**
	 * This method initializes uploadSchemaViewPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getUploadSchemaViewPanel() {
		if (uploadSchemaViewPanel == null) {
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints9.weighty = 1.0;
			gridBagConstraints9.weightx = 1.0;
			uploadSchemaViewPanel = new JPanel();
			uploadSchemaViewPanel.setLayout(new GridBagLayout());
			uploadSchemaViewPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Schema Preview",
				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.DEFAULT_POSITION, null, PortalLookAndFeel.getPanelLabelColor()));
			uploadSchemaViewPanel.add(getUploadSchemaTextPane(), gridBagConstraints9);
		}
		return uploadSchemaViewPanel;
	}


	/**
	 * This method initializes gmeBrowseButtonPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getGmeBrowseButtonPanel() {
		if (gmeBrowseButtonPanel == null) {
			gmeBrowseButtonPanel = new JPanel();
			gmeBrowseButtonPanel.add(getGmeDownloadButton(), null);
		}
		return gmeBrowseButtonPanel;
	}


	/**
	 * This method initializes Download
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getGmeDownloadButton() {
		if (gmeDownloadButton == null) {
			gmeDownloadButton = new JButton();
			gmeDownloadButton.setText("Download");
			gmeDownloadButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					String location = null;
					try {
						location = ResourceManager.promptDir( null);
					} catch (Exception ex) {
						CompositeErrorDialog.showErrorDialog(ex);
					}
					if (location != null && location.length() > 0) {
						GridServiceResolver.getInstance().setDefaultFactory(
							new GlobusGMEXMLDataModelServiceFactory());
						try {
							XMLDataModelService handle = (XMLDataModelService) GridServiceResolver.getInstance()
							.getGridService(ConfigurationUtil.getGlobalExtensionProperty(GMESchemaLocatorPanel.GME_URL).getValue());
							if (gmeSchemaLocatorPanel.getSchemaComboBox().getSelectedItem() != null) {
								handle.cacheSchema(((SchemaWrapper) gmeSchemaLocatorPanel.getSchemaComboBox()
									.getSelectedItem()).getNamespace(), new File(location));
							}
						} catch (Exception e1) {
							e1.printStackTrace();
							CompositeErrorDialog.showErrorDialog("Error contacting GME", 
							"Please check the GME URL and make sure that you have the appropriate credentials!");
						}
					}
				}
			});
		}
		return gmeDownloadButton;
	}


	/**
	 * This method initializes uploadSchemaTextPane
	 * 
	 * @return javax.swing.JTextArea
	 */
	private JEditTextArea getUploadSchemaTextPane() {
		if (uploadSchemaTextPane == null) {
			uploadSchemaTextPane = new JEditTextArea(GMETextAreaDefaults.createDefaults());
			uploadSchemaTextPane.setTokenMarker(new XMLTokenMarker());
		}
		return uploadSchemaTextPane;
	}


	/**
	 * This method initializes schemaTextPane
	 * 
	 * @return javax.swing.JTextArea
	 */
	private JEditTextArea getSchemaTextPane() {
		if (schemaTextPane == null) {
			schemaTextPane = new JEditTextArea(GMETextAreaDefaults.createDefaults());
			schemaTextPane.setTokenMarker(new XMLTokenMarker());
			schemaTextPane.setEditable(false);
		}
		return schemaTextPane;
	}


	public static void main(String[] args) {
		try {
			JFrame frame = new JFrame();
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			ExtensionDescription ext = (ExtensionDescription) Utils.deserializeDocument(
                "extensions" + File.separator + "gme_discovery" 
                + File.separator + "extension.xml", ExtensionDescription.class);
			final GMEViewer panel = new GMEViewer(ext.getDiscoveryExtensionDescription());
			frame.getContentPane().setLayout(new BorderLayout());
			frame.getContentPane().add(panel, BorderLayout.CENTER);

			frame.pack();
			frame.setVisible(true);
		} catch (Exception e) {
			logger.error(e);
		}
	}
}
