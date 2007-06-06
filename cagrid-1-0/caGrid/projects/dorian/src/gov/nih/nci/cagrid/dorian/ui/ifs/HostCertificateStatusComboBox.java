package gov.nih.nci.cagrid.dorian.ui.ifs;

import gov.nih.nci.cagrid.dorian.ifs.bean.HostCertificateStatus;

import javax.swing.JComboBox;

public class HostCertificateStatusComboBox extends JComboBox {
	public HostCertificateStatusComboBox() {
		this.addItem("");
		this.addItem(HostCertificateStatus.Active);
		this.addItem(HostCertificateStatus.Pending);
		this.addItem(HostCertificateStatus.Rejected);
		this.addItem(HostCertificateStatus.Suspended);
		this.addItem(HostCertificateStatus.Compromised);
	}

	public HostCertificateStatus getStatus() {
		if (getSelectedItem() instanceof HostCertificateStatus) {
			return (HostCertificateStatus) getSelectedItem();
		} else {
			return null;
		}
	}

}
