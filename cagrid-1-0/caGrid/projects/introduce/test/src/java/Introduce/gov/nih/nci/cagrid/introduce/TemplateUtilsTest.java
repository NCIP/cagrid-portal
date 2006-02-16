package gov.nih.nci.cagrid.introduce;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.introduce.beans.ServiceDescription;
import gov.nih.nci.cagrid.introduce.beans.metadata.MetadataListType;
import gov.nih.nci.cagrid.introduce.beans.method.MethodsType;
import gov.nih.nci.cagrid.introduce.codegen.TemplateUtils;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.namespace.QName;

import junit.framework.TestCase;

public class TemplateUtilsTest extends TestCase {
	public static String GOLD_DIRECTORY = "/test/resources/gold/";

	public static String GOLD_FILE = "introduce_Example.xml";

	private ServiceDescription info = null;

	private MetadataListType metadataList = null;

	private MethodsType methods = null;

	/**
	 * <Metadata xmlns:a="http://ns1" className="AType" namespace="http://ns1"
	 * type="A" location="." populateFromFile="true" register="true"
	 * QName="a:A"/> <Metadata xmlns:a="http://ns2" className="AType"
	 * namespace="http://ns2" type="A" location="." populateFromFile="true"
	 * register="true" QName="a:A"/> <Metadata xmlns:a="http://ns1"
	 * className="BType" namespace="http://ns1" type="B" location="."
	 * populateFromFile="true" register="true" QName="b:B"/> <Metadata
	 * xmlns:a="http://ns2" className="BType" namespace="http://ns2" type="B"
	 * location="." populateFromFile="false" register="false" QName="b:B"/>
	 * <Metadata xmlns:a="http://ns3" className="BType" namespace="http://ns3"
	 * type="B" location="." populateFromFile="false" register="true"
	 * QName="b:B"/>
	 */

	public void setUp() {
		String pathtobasedir = System.getProperty("basedir", ".");
		try {

			info = (ServiceDescription) Utils.deserializeDocument(
					pathtobasedir + GOLD_DIRECTORY + GOLD_FILE,
					ServiceDescription.class);

			metadataList = info.getMetadataList();

			methods = info.getMethods();
		} catch (Exception e) {
			e.printStackTrace();
			fail("Error in setup:" + e.getMessage());
		}

		assertNotNull(metadataList);
		assertNotNull(metadataList.getMetadata());
		assertNotNull(methods);
		assertNotNull(methods.getMethod());
	}

	public void testGetResourcePropertyVariableName() {
		// make sure the pattern is right
		String computedVarName1 = TemplateUtils
				.getResourcePropertyVariableName(metadataList, 0);
		assertNotNull(computedVarName1);
		assertTrue(computedVarName1.matches("([a-z])+([a-zA-Z])*"));

		// make sure the name is uniq when only the name space is different
		String computedVarName2 = TemplateUtils
				.getResourcePropertyVariableName(metadataList, 1);
		assertNotNull(computedVarName2);
		assertFalse(computedVarName1.equals(computedVarName2));
		assertTrue(computedVarName2.matches("([a-z])+([a-zA-Z])*[1-9]+"));

		// store all the names in a set to check for uniqueness
		Set names = new HashSet();
		for (int i = 0; i < metadataList.getMetadata().length; i++) {
			names.add(TemplateUtils.getResourcePropertyVariableName(
					metadataList, i));
		}
		// makes sure we got a unique name for all items
		assertEquals(names.size(), metadataList.getMetadata().length);
	}

	public void testBuildQNameNamespacePrefixMap() {
		Map map = TemplateUtils.buildQNameNamespacePrefixMap(metadataList);
		assertNotNull(map);

		assertTrue(map.keySet().size() <= metadataList.getMetadata().length);

		for (int i = 0; i < metadataList.getMetadata().length; i++) {
			QName qname = metadataList.getMetadata()[i].getQName();
			String prefix = (String) map.get(qname.getNamespaceURI());
			assertNotNull(prefix);
		}

	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(TemplateUtilsTest.class);
	}

}
