/*
 * Created on Oct 18, 2006
 */
package gov.nih.nci.cagrid.metadata.xmi;

import java.io.File;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;

public class XMI2Model
{
	/**
	 * Just a main
	 */
	private XMI2Model() { super(); }
	
	/**
	 * Get the command-line options
	 */
	private static Options getOptions()
	{
		Option xmi = OptionBuilder.withArgName("xmi")
			.hasArg()
			.isRequired(true)
			.withDescription("the input xmi file")
			.create("xmi");
		
		Option model = OptionBuilder.withArgName("model")
			.hasArg()
			.isRequired(true)
			.withDescription("the output model file")
			.create("model");

		Option projectShortName = OptionBuilder.withArgName("projectShortName")
			.hasArg()
			.isRequired(true)
			.withDescription("the short name of the project")
			.create("projectShortName");

		Option projectLongName = OptionBuilder.withArgName("projectLongName")
			.hasArg()
			.isRequired(false)
			.withDescription("the long name of the project")
			.create("projectLongName");

		Option projectVersion = OptionBuilder.withArgName("projectVersion")
			.hasArg()
			.isRequired(true)
			.withDescription("the version of the project")
			.create("projectVersion");

		Option projectDescription = OptionBuilder.withArgName("projectDescription")
			.hasArg()
			.isRequired(false)
			.withDescription("the description of the project")
			.create("projectDescription");
		
		Options options = new Options();
		options.addOption(xmi);		
		options.addOption(model);		
		options.addOption(projectShortName);		
		options.addOption(projectLongName);		
		options.addOption(projectVersion);		
		options.addOption(projectDescription);		
		return options;
	}
	
	public static void main(String[] args) 
		throws Exception
    {
        Options options = getOptions();
        CommandLine cmd = null;
		try {			
			cmd = new BasicParser().parse(options, args);
		} catch(ParseException e) {						
			System.out.println("Error parsing arguments: " + e.getMessage());
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("XMI2Model", options);
			System.exit(-1);
			return;
		}
		
		XMIParser parser = new XMIParser(
			cmd.getOptionValue("projectShortName"), cmd.getOptionValue("projectVersion")
		);

		if (cmd.hasOption("projectLongName")) parser.setProjectLongName(cmd.getOptionValue("projectLongName"));
		if (cmd.hasOption("projectDescription")) parser.setProjectDescription(cmd.getOptionValue("projectDescription"));
		
		DomainModel model = parser.parse(new File(cmd.getOptionValue("xmi")));
		XMIParser.writeDomainModel(model, new File(cmd.getOptionValue("model")));
    }
}
