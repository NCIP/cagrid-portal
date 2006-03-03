package gov.nih.nci.cagrid.dorian.gridca.common;

import gov.nih.nci.cagrid.common.FaultUtil;
import gov.nih.nci.cagrid.gridca.common.KeyUtil;

import java.io.File;
import java.io.InputStream;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import junit.framework.TestCase;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class TestKeyUtil extends TestCase {
	
	public void testCreateWriteLoadEncryptedPrivateKey(){
		try{
			String keyFile = "test-key.pem";
			KeyPair pair = KeyUtil.generateRSAKeyPair1024();
			assertNotNull(pair);
			PrivateKey pkey = pair.getPrivate();
			assertNotNull(pkey);
			KeyUtil.writePrivateKey(pkey,new File(keyFile),"password");
			PrivateKey key=KeyUtil.loadPrivateKey(new File(keyFile),"password");
		    assertNotNull(key);
		    assertEquals(key,pkey);
		    File f = new File(keyFile);
		    f.delete();
		}catch (Exception e) {
			FaultUtil.printFault(e);;
			assertTrue(false);
		}	
	}
	
	public void testReadWritePublicKeyToString(){
		try{
			KeyPair pair = KeyUtil.generateRSAKeyPair1024();
			assertNotNull(pair);
			PublicKey pkey = pair.getPublic();
			assertNotNull(pkey);
			String str = KeyUtil.writePublicKey(pkey);
			assertNotNull(str);
			PublicKey key=KeyUtil.loadPublicKey(str);
		    assertNotNull(key);
		    assertEquals(key,pkey);
		}catch (Exception e) {
			FaultUtil.printFault(e);;
			assertTrue(false);
		}	
	}
	
	public void testCreateWriteLoadEncryptedPrivateKeyBadPassword(){
		try{
			String keyFile = "test-key.pem";
			KeyPair pair = KeyUtil.generateRSAKeyPair1024();
			assertNotNull(pair);
			PrivateKey pkey = pair.getPrivate();
			assertNotNull(pkey);
			KeyUtil.writePrivateKey(pkey,new File(keyFile),"password");
			PrivateKey key=KeyUtil.loadPrivateKey(new File(keyFile),"bad");
		    assertNotNull(key);
		    assertEquals(key,pkey);
		    File f = new File(keyFile);
		    f.delete();
		    assertTrue(false);
		}catch (Exception e) {
			
		}	
	}
	
	public void testCreateWriteLoadPrivateKey(){
		try{
			String keyFile = "test-key.pem";
			KeyPair pair = KeyUtil.generateRSAKeyPair1024();
			assertNotNull(pair);
			PrivateKey pkey = pair.getPrivate();
			assertNotNull(pkey);
			KeyUtil.writePrivateKey(pkey,new File(keyFile));
			PrivateKey key=KeyUtil.loadPrivateKey(new File(keyFile),null);
		    assertNotNull(key);
		    assertEquals(key,pkey);
		    File f = new File(keyFile);
		    f.delete();
		}catch (Exception e) {
			FaultUtil.printFault(e);;
			assertTrue(false);
		}	
	}
	
	public void testLoadEncyptedPrivateKey(){
		try{
			InputStream keyLocation = TestCase.class.getResourceAsStream(Constants.SIMPLECA_CAKEY);
			PrivateKey key=KeyUtil.loadPrivateKey(keyLocation,"gomets123");
		    assertNotNull(key);
		}catch (Exception e) {
			FaultUtil.printFault(e);;
			assertTrue(false);
		}	
	}
	
	public void testLoadPrivateKey(){
		try{
			InputStream keyLocation = TestCase.class.getResourceAsStream(Constants.BMI_CAKEY);
			PrivateKey key=KeyUtil.loadPrivateKey(keyLocation,null);
		    assertNotNull(key);
		}catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}	
		
		try{
			InputStream keyLocation = TestCase.class.getResourceAsStream(Constants.DORIAN_KEY);
			PrivateKey key=KeyUtil.loadPrivateKey(keyLocation,null);
		    assertNotNull(key);
		}catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}	
	}
	
	public void testLoadBadPrivateKey(){
		try{
			InputStream keyLocation = TestCase.class.getResourceAsStream(Constants.BAD_KEY);
			KeyUtil.loadPrivateKey(keyLocation,"");
			assertTrue(false);
		}catch (Exception e) {
			
		}
	}
	
	public void testLoadEncyptedPrivateKeyBadPassword(){
		try{
			InputStream keyLocation = TestCase.class.getResourceAsStream(Constants.SIMPLECA_CAKEY);
			KeyUtil.loadPrivateKey(keyLocation,"badpassword");
			assertTrue(false);
		}catch (Exception e) {
			
		}
		
	}

}
