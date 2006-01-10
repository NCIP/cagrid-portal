package gov.nih.nci.cagrid.introduce.codegen.metadata;

import gov.nih.nci.cagrid.common.CommonTools;
import gov.nih.nci.cagrid.introduce.beans.metadata.ServiceMetadataListType;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

/**
 * SyncMethodsOnDeployment
 * 
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * @created Jun 8, 2005
 * @version $Id: mobiusEclipseCodeTemplates.xml,v 1.2 2005/04/19 14:58:02 oster
 *          Exp $
 */
public class SyncMetadata {

	public static final String DIR_OPT = "d";

	public static final String DIR_OPT_FULL = "directory";

	private File baseDirectory;

	private ServiceMetadataListType metadatas;

	private Properties serviceProperties;

	public SyncMetadata(File baseDirectory) {

		this.baseDirectory = baseDirectory;
	}

	public void sync() throws Exception {
		this.metadatas = (ServiceMetadataListType) CommonTools
				.deserializeDocument(this.baseDirectory + File.separator
						+ "introduceMetadata.xml",
						ServiceMetadataListType.class);
		File servicePropertiesFile = new File(baseDirectory.getAbsolutePath()
				+ File.separator + "introduce.properties");
		serviceProperties = new Properties();
		serviceProperties.load(new FileInputStream(servicePropertiesFile));
		
		TemplateObjectContainer toc = new TemplateObjectContainer(this.metadatas,this.serviceProperties);

	}

	public static void main(String[] args) {
		Options options = new Options();
		Option directoryOpt = new Option(DIR_OPT, DIR_OPT_FULL, true,
				"The include tool directory");
		options.addOption(directoryOpt);

		CommandLineParser parser = new PosixParser();

		File directory = null;

		try {
			CommandLine line = parser.parse(options, args);
			directory = new File(line.getOptionValue(DIR_OPT));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		SyncMetadata sync = new SyncMetadata(directory);
		try {
			sync.sync();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

}
