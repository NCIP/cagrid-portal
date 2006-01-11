package gov.nih.nci.cagrid.introduce.codegen.metadata;

import gov.nih.nci.cagrid.common.CommonTools;
import gov.nih.nci.cagrid.introduce.beans.metadata.ServiceMetadataListType;
import gov.nih.nci.cagrid.introduce.codegen.MetadataTemplateUtils;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.namespace.QName;

import junit.framework.TestCase;


public class MetadataTemplateUtilsTest extends TestCase {
	public static String GOLD_DIRECTORY = "/test/resources/gold/";
	public static String GOLD_FILE = "ServiceMetadata_Example.xml";

	private ServiceMetadataListType metadataList = null;


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
			metadataList = (ServiceMetadataListType) CommonTools.deserializeDocument(pathtobasedir + GOLD_DIRECTORY
				+ GOLD_FILE, ServiceMetadataListType.class);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Error in setup:" + e.getMessage());
		}

		assertNotNull(metadataList);
		assertNotNull(metadataList.getMetadata());
	}


	public void testGetResourcePropertyVariableName() {
		// make sure the pattern is right
		String computedVarName1 = MetadataTemplateUtils.getResourcePropertyVariableName(metadataList, 0);
		assertNotNull(computedVarName1);
		assertTrue(computedVarName1.matches("([a-z])+([a-zA-Z])*"));

		// make sure the name is uniq when only the name space is different
		String computedVarName2 = MetadataTemplateUtils.getResourcePropertyVariableName(metadataList, 1);
		assertNotNull(computedVarName2);
		assertFalse(computedVarName1.equals(computedVarName2));
		assertTrue(computedVarName2.matches("([a-z])+([a-zA-Z])*[1-9]+"));

		// store all the names in a set to check for uniqueness
		Set names = new HashSet();
		for (int i = 0; i < metadataList.getMetadata().length; i++) {
			names.add(MetadataTemplateUtils.getResourcePropertyVariableName(metadataList, i));
		}
		// makes sure we got a unique name for all items
		assertEquals(names.size(), metadataList.getMetadata().length);
	}


	public void testBuildQNameNamespacePrefixMap() {
		Map map = MetadataTemplateUtils.buildQNameNamespacePrefixMap(metadataList);
		assertNotNull(map);

		assertTrue(map.keySet().size() <= metadataList.getMetadata().length);

		for (int i = 0; i < metadataList.getMetadata().length; i++) {
			QName qname = metadataList.getMetadata()[i].getQName();
			String prefix = (String) map.get(qname.getNamespaceURI());
			assertNotNull(prefix);			
		}
		
	}


	public static void main(String[] args) {
		junit.textui.TestRunner.run(MetadataTemplateUtilsTest.class);
	}

}
