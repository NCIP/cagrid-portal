package org.projectmobius.ca.test;

import java.io.File;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.Security;

import org.projectmobius.ca.KeyUtil;

import junit.framework.TestCase;

public class KeyTest extends TestCase {
	public static String RESOURCES_DIR="resources"+File.separator+"ca-test";
	
	public void testCreateWriteLoadEncryptedPrivateKey(){
		try{
			Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
			String keyFile = "test-key.pem";
			KeyPair pair = KeyUtil.generateRSAKeyPair();
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
			e.printStackTrace();
			assertTrue(false);
		}	
	}
	
	public void testCreateWriteLoadEncryptedPrivateKeyBadPassword(){
		try{
			Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
			String keyFile = "test-key.pem";
			KeyPair pair = KeyUtil.generateRSAKeyPair();
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
			Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
			String keyFile = "test-key.pem";
			KeyPair pair = KeyUtil.generateRSAKeyPair();
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
			e.printStackTrace();
			assertTrue(false);
		}	
	}
	
	public void testLoadEncyptedPrivateKey(){
		try{
			Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
			PrivateKey key=KeyUtil.loadPrivateKey(RESOURCES_DIR+File.separator+"simpleca-cakey.pem","gomets123");
		    assertNotNull(key);
		}catch (Exception e) {
			e.printStackTrace();
			assertTrue(false);
		}	
	}
	
	public void testLoadPrivateKey(){
		try{
			Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
			PrivateKey key=KeyUtil.loadPrivateKey(RESOURCES_DIR+File.separator+"mobius-cakey.pem",null);
		    assertNotNull(key);
		}catch (Exception e) {
			e.printStackTrace();
			assertTrue(false);
		}	
		
		try{
			Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
			PrivateKey key=KeyUtil.loadPrivateKey(RESOURCES_DIR+File.separator+"mobius-userkey.pem",null);
		    assertNotNull(key);
		}catch (Exception e) {
			e.printStackTrace();
			assertTrue(false);
		}	
	}
	
	public void testLoadBadPrivateKey(){
		try{
			Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
			KeyUtil.loadPrivateKey(RESOURCES_DIR+File.separator+"BADKEY","");
			assertTrue(false);
		}catch (Exception e) {
			
		}
	}
	
	public void testLoadEncyptedPrivateKeyBadPassword(){
		try{
			Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
			KeyUtil.loadPrivateKey(RESOURCES_DIR+File.separator+"simpleca-cakey.pem","badpassword");
			assertTrue(false);
		}catch (Exception e) {
			
		}
		
	}

}
