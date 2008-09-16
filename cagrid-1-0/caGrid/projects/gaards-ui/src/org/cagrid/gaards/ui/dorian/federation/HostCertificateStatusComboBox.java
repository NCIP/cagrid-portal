package org.cagrid.gaards.ui.dorian.federation;

import javax.swing.JComboBox;

import org.cagrid.gaards.dorian.federation.HostCertificateStatus;

public class HostCertificateStatusComboBox extends JComboBox {
	public HostCertificateStatusComboBox(boolean includeBlank) {
		if (includeBlank) {
			this.addItem("");
		}
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
