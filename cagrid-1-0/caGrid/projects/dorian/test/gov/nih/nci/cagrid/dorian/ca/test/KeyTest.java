package gov.nih.nci.cagrid.gums.ca.test;

import gov.nih.nci.cagrid.gums.ca.KeyUtil;
import gov.nih.nci.cagrid.gums.common.FaultUtil;

import java.io.File;
import java.security.KeyPair;
import java.security.PrivateKey;

import junit.framework.TestCase;
/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class KeyTest extends TestCase {
	public static String RESOURCES_DIR="resources"+File.separator+"ca-test";
	
	public void testCreateWriteLoadEncryptedPrivateKey(){
		try{
			String keyFile = "test-key.pem";
			KeyPair pair = KeyUtil.generateRSAKeyPair1024();
			assertNotNull(pair);
			PrivateKey pkey = pair.getPrivate();
			assertNotNull(pkey);
			KeyUtil.writePrivateKey(pkey,keyFile,"password");
			PrivateKey key=KeyUtil.loadPrivateKey(keyFile,"password");
		    assertNotNull(key);
		    assertEquals(key,pkey);
		    File f = new File(keyFile);
		    f.delete();
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
			KeyUtil.writePrivateKey(pkey,keyFile,"password");
			PrivateKey key=KeyUtil.loadPrivateKey(keyFile,"bad");
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
			KeyUtil.writePrivateKey(pkey,keyFile);
			PrivateKey key=KeyUtil.loadPrivateKey(keyFile,null);
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
			PrivateKey key=KeyUtil.loadPrivateKey(RESOURCES_DIR+File.separator+"simpleca-cakey.pem","gomets123");
		    assertNotNull(key);
		}catch (Exception e) {
			FaultUtil.printFault(e);;
			assertTrue(false);
		}	
	}
	
	public void testLoadPrivateKey(){
		try{
			PrivateKey key=KeyUtil.loadPrivateKey(RESOURCES_DIR+File.separator+"bmi-cakey.pem",null);
		    assertNotNull(key);
		}catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}	
		
		try{
			PrivateKey key=KeyUtil.loadPrivateKey(RESOURCES_DIR+File.separator+"gums-key.pem",null);
		    assertNotNull(key);
		}catch (Exception e) {
			FaultUtil.printFault(e);
			assertTrue(false);
		}	
	}
	
	public void testLoadBadPrivateKey(){
		try{
			KeyUtil.loadPrivateKey(RESOURCES_DIR+File.separator+"BADKEY","");
			assertTrue(false);
		}catch (Exception e) {
			
		}
	}
	
	public void testLoadEncyptedPrivateKeyBadPassword(){
		try{
			KeyUtil.loadPrivateKey(RESOURCES_DIR+File.separator+"simpleca-cakey.pem","badpassword");
			assertTrue(false);
		}catch (Exception e) {
			
		}
		
	}

}
