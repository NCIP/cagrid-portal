package gov.nih.nci.cagrid.introduce.test;

import gov.nih.nci.cagrid.introduce.common.CommonTools;
import junit.framework.TestCase;

public class IntroduceToolsTestCase extends TestCase {


	protected void setUp() throws Exception {
		super.setUp();
	}

	
	public void testIsValidSeviceName(){
		assertTrue(CommonTools.isValidServiceName("MyService"));
		assertTrue(CommonTools.isValidServiceName("My123"));
		assertTrue(CommonTools.isValidServiceName("My123_Service"));
		assertTrue(CommonTools.isValidServiceName("My123_TestService"));
		assertTrue(CommonTools.isValidServiceName("M123_TestService23"));
		assertTrue(CommonTools.isValidServiceName("M123_TestService23$"));
	}
	
	public void testIsNotValidServiceName(){
		assertFalse(CommonTools.isValidServiceName("_MyService"));
		assertFalse(CommonTools.isValidServiceName("My1-23"));
		assertFalse(CommonTools.isValidServiceName("0My123_Service"));
		assertFalse(CommonTools.isValidServiceName("_2My123_TestService"));
		assertFalse(CommonTools.isValidServiceName("*&M123_TestService23"));
		assertFalse(CommonTools.isValidServiceName("@#M123_TestService23$"));
		assertFalse(CommonTools.isValidServiceName("M123_TestServ!@ice23$"));
	}
	
	public void testIsValidPackageName(){
		assertTrue(CommonTools.isValidPackageName("test.org"));
		assertTrue(CommonTools.isValidPackageName("sdjr23lkj23lk456jl"));
		assertTrue(CommonTools.isValidPackageName("a0193"));
		assertTrue(CommonTools.isValidPackageName("i"));
		assertTrue(CommonTools.isValidPackageName("is"));
		assertTrue(CommonTools.isValidPackageName("this.is.a.really.long.package.name.just.to.be.sure.this.is.ok.with.my.package.parser.i.will.even.put.in.some.stupid.characters.like.a0193.and.sdjr23lkj23lk456jl.test.org"));
		assertTrue(CommonTools.isValidPackageName("_test.org"));
	}
	
	public void testIsNotValidPackageName(){
		assertFalse(CommonTools.isValidPackageName("test.1org"));
		assertFalse(CommonTools.isValidPackageName("sdjr23lkj23%lk456jl"));
		assertFalse(CommonTools.isValidPackageName("a&0193"));
		assertFalse(CommonTools.isValidPackageName("1"));
		assertFalse(CommonTools.isValidPackageName("1s"));
		assertFalse(CommonTools.isValidPackageName("this.is.a.really.long.package.name.just.to.be.sure.this.is.ok.with.my.package.parser.i.will.even.put.in.some.stupid.characters.like.a0193.and.sdjr23lkj23lk456jl.test.#org"));
		assertFalse(CommonTools.isValidPackageName("_test.Forg"));
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(IntroduceToolsTestCase.class);
	}

}
