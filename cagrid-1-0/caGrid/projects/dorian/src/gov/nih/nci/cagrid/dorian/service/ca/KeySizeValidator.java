package gov.nih.nci.cagrid.dorian.service.ca;

public class KeySizeValidator {

	public static boolean isKeySizeValid(int keySize) {
		if ((keySize == 512) || (keySize == 1024) || (keySize == 2048)) {
			return true;
		} else {
			return false;
		}
	}

}
