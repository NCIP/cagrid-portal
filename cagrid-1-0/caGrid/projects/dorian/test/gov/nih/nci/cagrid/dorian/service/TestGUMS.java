package gov.nih.nci.cagrid.gums.service;

import gov.nih.nci.cagrid.gums.common.FaultUtil;
import gov.nih.nci.cagrid.gums.common.SimpleResourceManager;
import gov.nih.nci.cagrid.gums.idp.IdPConfiguration;
import gov.nih.nci.cagrid.gums.ifs.IFSConfiguration;
import gov.nih.nci.cagrid.gums.test.TestUtils;
import gov.nih.nci.cagrid.gums.ca.GUMSCertificateAuthorityConf;

import java.io.File;

import junit.framework.TestCase;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class TestGUMS extends TestCase{
	public static String RESOURCES_DIR = "resources" + File.separator
	+ "general-test";
	
    public void testGUMSManager(){
    	try{
    		GUMS jm = new GUMS(RESOURCES_DIR+File.separator+"gums-conf.xml","localhost");
    		assertNotNull(jm.getGUMSConfiguration());
    		assertNotNull(jm.getDatabase());
    		assertEquals(0,jm.getDatabase().getUsedConnectionCount());
    		jm.getDatabase().destroyDatabase();
    	}catch (Exception e) {
    		FaultUtil.printFault(e);
			assertTrue(false);
		}
    }
    
    public void testGUMSgetResource(){
    	try{
    		GUMS jm = new GUMS(RESOURCES_DIR+File.separator+"gums-conf.xml","localhost");
    		assertNotNull(jm.getGUMSConfiguration());
    		assertNotNull(jm.getDatabase());
    		
    		assertNotNull(jm.getResource(IdPConfiguration.RESOURCE));
    		assertNotNull(jm.getResource(IFSConfiguration.RESOURCE));
    		assertNotNull(jm.getResource(GUMSCertificateAuthorityConf.RESOURCE));
    		
    		assertEquals(0,jm.getDatabase().getUsedConnectionCount());
    		jm.getDatabase().destroyDatabase();
    	}catch (Exception e) {
    		FaultUtil.printFault(e);
			assertTrue(false);
		}
    }
    
    public void testGUMSauthenticate(){
    	try{
    		GUMS jm = new GUMS(RESOURCES_DIR+File.separator+"gums-conf.xml","localhost");
    		assertNotNull(jm.getGUMSConfiguration());
    		assertNotNull(jm.getDatabase());
    		
    		//add in code to test authenticate
    		
    		
    		
    		assertEquals(0,jm.getDatabase().getUsedConnectionCount());
    		jm.getDatabase().destroyDatabase();
    	}catch (Exception e) {
    		FaultUtil.printFault(e);
			assertTrue(false);
		}
    }
 
    public void testGUMSfindIdPUsers(){
    	try{
    		GUMS jm = new GUMS(RESOURCES_DIR+File.separator+"gums-conf.xml","localhost");
    		assertNotNull(jm.getGUMSConfiguration());
    		assertNotNull(jm.getDatabase());
    		
    		//add in code to test findIdPUsers
    		
    		
    		
    		assertEquals(0,jm.getDatabase().getUsedConnectionCount());
    		jm.getDatabase().destroyDatabase();
    	}catch (Exception e) {
    		FaultUtil.printFault(e);
			assertTrue(false);
		}
    }
    
    public void testGUMSregisterWithIdP(){
    	try{
    		GUMS jm = new GUMS(RESOURCES_DIR+File.separator+"gums-conf.xml","localhost");
    		assertNotNull(jm.getGUMSConfiguration());
    		assertNotNull(jm.getDatabase());
    		
    		//add in code to test registerWithIdP
    		
    		
    		
    		assertEquals(0,jm.getDatabase().getUsedConnectionCount());
    		jm.getDatabase().destroyDatabase();
    	}catch (Exception e) {
    		FaultUtil.printFault(e);
			assertTrue(false);
		}
    }
    
    public void testGUMSremoveIdPUser(){
    	try{
    		GUMS jm = new GUMS(RESOURCES_DIR+File.separator+"gums-conf.xml","localhost");
    		assertNotNull(jm.getGUMSConfiguration());
    		assertNotNull(jm.getDatabase());
    		
    		//add in code to test removeIdPUser
    
    		
    		
    		assertEquals(0,jm.getDatabase().getUsedConnectionCount());
    		jm.getDatabase().destroyDatabase();
    	}catch (Exception e) {
    		FaultUtil.printFault(e);
			assertTrue(false);
		}
    }
    
    public void testGUMSupdateIdPUser(){
    	try{
    		GUMS jm = new GUMS(RESOURCES_DIR+File.separator+"gums-conf.xml","localhost");
    		assertNotNull(jm.getGUMSConfiguration());
    		assertNotNull(jm.getDatabase());
    		
    		//add in code to test updateIdPUsers
    		
    		
    		
    		assertEquals(0,jm.getDatabase().getUsedConnectionCount());
    		jm.getDatabase().destroyDatabase();
    	}catch (Exception e) {
    		FaultUtil.printFault(e);
			assertTrue(false);
		}
    }
    
    
/*   protected void setUp() throws Exception {
		super.setUp();
	}
    
    protected void tearDown() throws Exception {
		super.setUp();
	}
*/	
}
