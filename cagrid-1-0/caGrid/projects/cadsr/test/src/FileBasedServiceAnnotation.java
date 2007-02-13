
import gov.nih.nci.cagrid.cadsr.common.ServiceMetadataAnnotator;
import gov.nih.nci.cagrid.cadsr.common.exceptions.CaDSRGeneralException;
import gov.nih.nci.cagrid.cadsr.uml2xml.UML2XMLBinding;
import gov.nih.nci.cagrid.cadsr.uml2xml.UML2XMLBindingConfigurationFileImpl;
import gov.nih.nci.cagrid.cadsr.uml2xml.file.configuration.UML2XMLBindingConfiguration;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.metadata.MetadataUtils;
import gov.nih.nci.cagrid.metadata.ServiceMetadata;
import gov.nih.nci.system.applicationservice.ApplicationService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;


/**
 * @author oster
 * 
 */
public class FileBasedServiceAnnotation {

	public static void usage() {
		System.err.println("USAGE: <confifile> <metadatafile>");
		System.exit(-1);
	}


	public static void main(String[] args) {
		try {

			if (args.length < 2) {
				usage();
			}

			File configFile = new File(args[0]);
			File metadataFile = new File(args[1]);

			ApplicationService appService = ApplicationService
				.getRemoteInstance("http://cabio.nci.nih.gov/cacore31/http/remoteService");

			UML2XMLBindingConfiguration config = (UML2XMLBindingConfiguration) Utils.deserializeObject(
				new InputStreamReader(new FileInputStream(configFile)), UML2XMLBindingConfiguration.class);

			ServiceMetadataAnnotator annotator = new FileBasedServiceAnnotation.FileBasedServiceMetadataAnnotator(
				appService, config);

			ServiceMetadata metadata = MetadataUtils.deserializeServiceMetadata(new FileReader(metadataFile));

			annotator.annotateServiceMetadata(metadata);
			FileWriter fw = new FileWriter("annotatedServiceMetadata.xml");
			MetadataUtils.serializeServiceMetadata(metadata, fw);
			fw.close();

			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}


	static class FileBasedServiceMetadataAnnotator extends ServiceMetadataAnnotator {
		private UML2XMLBindingConfiguration config = null;


		public FileBasedServiceMetadataAnnotator(ApplicationService cadsr, UML2XMLBindingConfiguration config) {
			super(cadsr);
			this.config = config;
		}


		protected UML2XMLBinding getUML2XMLBinding() throws CaDSRGeneralException {
			return new UML2XMLBindingConfigurationFileImpl(this.config);
		}
	}
}
