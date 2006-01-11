package gov.nih.nci.cagrid.dorian.common.ca;

import org.bouncycastle.asn1.x509.KeyUsage;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		KeyUsage uk = new KeyUsage(KeyUsage.digitalSignature | KeyUsage.keyEncipherment);
		System.out.println(uk.getPadBits());
		
		int num = (KeyUsage.digitalSignature << uk.getPadBits());
		System.out.println(num +"=="+KeyUsage.digitalSignature);
		
		if((num&KeyUsage.digitalSignature)==KeyUsage.digitalSignature){
			System.out.println("digitalSignature");
		}

	}

}
