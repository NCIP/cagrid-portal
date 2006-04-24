package gov.nih.nci.cagrid.data.creation;

import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.extension.CreationExtensionUIDialog;
import gov.nih.nci.cagrid.introduce.info.ServiceInformation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;

/** 
 *  DataServiceCreationDialog
 *  TODO:DOCUMENT ME
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Apr 3, 2006 
 * @version $Id$ 
 */
public class DataServiceCreationDialog extends CreationExtensionUIDialog {

	public DataServiceCreationDialog(ServiceExtensionDescriptionType desc, ServiceInformation info) {
		super(desc, info);
		initialize();
	}
	
	
	private void initialize() {
		getContentPane().add(new JLabel("Here I am!"));
		JButton ok = new JButton("OK");
		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		getContentPane().add(ok);
		pack();
		setVisible(true);
	}
}
