package gov.nih.nci.cagrid.introduce.portal.modification.discovery.globus;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.introduce.beans.extension.DiscoveryExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionDescription;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespaceType;
import gov.nih.nci.cagrid.introduce.extension.ExtensionTools;
import gov.nih.nci.cagrid.introduce.portal.modification.discovery.NamespaceTypeDiscoveryComponent;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFrame;

import org.projectmobius.common.XMLUtilities;


/**
 * GMETypeExtractionPanel
 * 
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @created Jul 7, 2005
 * @version $Id: mobiusEclipseCodeTemplates.xml,v 1.2 2005/04/19 14:58:02 oster
 *          Exp $
 */
public class GlobusTypeSelectionComponent extends NamespaceTypeDiscoveryComponent {
	public static String TYPE = "GLOBUS";

	private GlobiusConfigurationPanel globusPanel = null;


	public GlobusTypeSelectionComponent(DiscoveryExtensionDescriptionType descriptor) {
		super(descriptor);
		initialize();
		this.getGmePanel().discoverFromGlobus();
	}


	/**
	 * This method initializes this
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
		gridBagConstraints4.insets = new java.awt.Insets(0, 0, 0, 0);
		gridBagConstraints4.gridy = 0;
		gridBagConstraints4.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints4.gridwidth = 1;
		gridBagConstraints4.weightx = 1.0D;
		gridBagConstraints4.weighty = 1.0D;
		gridBagConstraints4.gridx = 0;
		this.setLayout(new GridBagLayout());
		this.add(getGmePanel(), gridBagConstraints4);
	}


	/**
	 * This method initializes gmePanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private GlobiusConfigurationPanel getGmePanel() {
		if (globusPanel == null) {
			globusPanel = new GlobiusConfigurationPanel();
		}
		return globusPanel;
	}


	public NamespaceType createNamespaceType(File schemaDestinationDir) {
		NamespaceType input = new NamespaceType();
		try {
			// set the package name

			if (this.globusPanel.currentNamespace != null) {
				input.setNamespace(this.globusPanel.currentNamespace);
			} else {
				return null;
			}

			gov.nih.nci.cagrid.introduce.portal.ExtensionTools.setSchemaElements(input, XMLUtilities
				.fileNameToDocument(globusPanel.currentSchemaFile.getAbsolutePath()));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return input;
	}

	public static void main(String[] args) {
		try {
			JFrame frame = new JFrame();
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			ExtensionDescription ext = (ExtensionDescription) Utils.deserializeDocument("extensions" + File.separator
				+ "gme_discovery" + File.separator + "extension.xml", ExtensionDescription.class);
			final GlobusTypeSelectionComponent panel = new GlobusTypeSelectionComponent(ext
				.getDiscoveryExtensionDescription());
			frame.getContentPane().setLayout(new BorderLayout());
			frame.getContentPane().add(panel, BorderLayout.CENTER);

			JButton createButton = new JButton("Test Create");
			createButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					NamespaceType createdNs = panel.createNamespaceType(new File("."));
					if (createdNs != null) {
						System.out.println("Created Namespace:" + createdNs.getNamespace() + " at location:"
							+ createdNs.getLocation());
					} else {
						System.out.println("Problem creating namespace");
					}
				}
			});
			frame.getContentPane().add(createButton, BorderLayout.SOUTH);

			frame.pack();
			frame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
