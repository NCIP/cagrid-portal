package org.cagrid.gaards.cds.service;

import java.util.Set;

public class ProxyPolicy {

	private Set<Integer> supportedKeySizes;

	public ProxyPolicy() {

	}

	public Set<Integer> getSupportedKeySizes() {
		return supportedKeySizes;
	}

	public void setSupportedKeySizes(Set<Integer> keySizes) {
		this.supportedKeySizes = keySizes;
	}

	public boolean isKeySizeSupported(int keySize) {
		if (this.supportedKeySizes.contains(new Integer(keySize))) {
			return true;
		} else {
			return false;
		}
	}
}
