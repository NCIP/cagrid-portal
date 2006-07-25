/**
 * 
 */
package gov.nih.nci.cagrid.discovery;

import gov.nih.nci.cagrid.metadata.ServiceMetadata;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;

import junit.framework.TestCase;


/**
 * @author oster
 */
public class MetadataUtilsTest extends TestCase {

	private static final String DOMAIN_XML = "domainModel.xml";
	private static final String SERVICE_XML = "serviceMetadata.xml";


	public static void main(String[] args) {
		junit.textui.TestRunner.run(MetadataUtilsTest.class);
	}


	public void testServiceMetadataSerialization() {
		try {
			InputStream is = getClass().getResourceAsStream(SERVICE_XML);
			assertNotNull(is);

			ServiceMetadata model = MetadataUtils.deserializeServiceMetadata(new InputStreamReader(is));
			assertNotNull(model);

			File tmpFile = File.createTempFile("metadata", ".xml");
			tmpFile.deleteOnExit();
			FileWriter tmpFileWriter = new FileWriter(tmpFile);
			MetadataUtils.serializeServiceMetadata(model, tmpFileWriter);
			tmpFileWriter.close();
			System.out.println("Wrote to file: " + tmpFile.getCanonicalPath());
			assertTrue(tmpFile.exists());

			ServiceMetadata model2 = MetadataUtils.deserializeServiceMetadata(new FileReader(tmpFile));
			assertNotNull(model2);

			assertEquals(model, model2);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}


	public void testDomainModelSerialization() {
		try {
			InputStream is = getClass().getResourceAsStream(DOMAIN_XML);
			assertNotNull(is);

			DomainModel model = MetadataUtils.deserializeDomainModel(new InputStreamReader(is));
			assertNotNull(model);

			File tmpFile = File.createTempFile("metadata", ".xml");
			tmpFile.deleteOnExit();
			FileWriter tmpFileWriter = new FileWriter(tmpFile);
			MetadataUtils.serializeDomainModel(model, tmpFileWriter);
			tmpFileWriter.close();
			System.out.println("Wrote to file: " + tmpFile.getCanonicalPath());
			assertTrue(tmpFile.exists());

			DomainModel model2 = MetadataUtils.deserializeDomainModel(new FileReader(tmpFile));
			assertNotNull(model2);

			assertEquals(model, model2);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
}
