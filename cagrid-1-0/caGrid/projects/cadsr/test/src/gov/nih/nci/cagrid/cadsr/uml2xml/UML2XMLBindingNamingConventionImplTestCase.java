package gov.nih.nci.cagrid.cadsr.uml2xml;

import gov.nih.nci.cagrid.cadsr.common.CaDSRGeneralException;

import javax.xml.namespace.QName;

import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.textui.TestRunner;


public class UML2XMLBindingNamingConventionImplTestCase extends TestCase {

	private static final String CONTEXT = "caBIG";
	private static final String PROJ = "caCORE";
	private static final String VERSION = "3.0";
	private static final String PACKAGE = "gov.nih.nci.cabio.domain";

	private static final String TEST_NS = "gme://" + PROJ + "." + CONTEXT + "/" + VERSION + "/" + PACKAGE;
	private static final String TEST_NAME = "Gene";
	private static final QName TEST_QNAME = new QName(TEST_NS, TEST_NAME);
	private static final QName INVALID_QNAME = new QName("asdf", "asdf");
	private UML2XMLBinding binder = new UML2XMLBindingNamingConventionImpl();


	public UML2XMLBindingNamingConventionImplTestCase(String name) {
		super(name);
	}


	protected void setUp() throws Exception {
		super.setUp();
	}


	protected void tearDown() throws Exception {
		super.tearDown();
	}


	public void testDetermineClassName() {
		try {
			String string = binder.determineClassName(TEST_QNAME);
			assertEquals(TEST_NAME, string);
		} catch (CaDSRGeneralException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}

		try {
			String string = binder.determineClassName(INVALID_QNAME);
			fail("Should have thrown exception.");
		} catch (CaDSRGeneralException e) {
		}

	}


	public void testDeterminePackageName() {
		try {
			String string = binder.determinePackageName(TEST_QNAME);
			assertEquals(PACKAGE, string);
		} catch (CaDSRGeneralException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		try {
			String string = binder.determinePackageName(INVALID_QNAME);
			fail("Should have thrown exception.");
		} catch (CaDSRGeneralException e) {
		}

	}


	public void testDetermineCaDSRContext() {
		try {
			String string = binder.determineCaDSRContext(TEST_QNAME);
			assertEquals(CONTEXT, string);
		} catch (CaDSRGeneralException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		try {
			String string = binder.determineCaDSRContext(INVALID_QNAME);
			fail("Should have thrown exception.");
		} catch (CaDSRGeneralException e) {
		}

	}


	public void testDetermineCaDSRProjectShortName() {
		try {
			String string = binder.determineCaDSRProjectShortName(TEST_QNAME);
			assertEquals(PROJ, string);
		} catch (CaDSRGeneralException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}

		try {
			String string = binder.determineCaDSRProjectShortName(INVALID_QNAME);
			fail("Should have thrown exception.");
		} catch (CaDSRGeneralException e) {
		}

	}


	public void testDetermineCaDSRProjectVersion() {
		try {
			String string = binder.determineCaDSRProjectVersion(TEST_QNAME);
			assertEquals(VERSION, string);
		} catch (CaDSRGeneralException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		try {
			String string = binder.determineCaDSRProjectVersion(INVALID_QNAME);
			fail("Should have thrown exception.");
		} catch (CaDSRGeneralException e) {
		}

	}


	public static void main(String args[]) {
		TestRunner runner = new TestRunner();
		TestResult result = runner.doRun(new TestSuite(UML2XMLBindingNamingConventionImplTestCase.class));
		System.exit(result.errorCount() + result.failureCount());
	}
}
