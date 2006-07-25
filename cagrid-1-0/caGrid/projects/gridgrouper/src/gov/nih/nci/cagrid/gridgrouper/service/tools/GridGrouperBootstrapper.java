package gov.nih.nci.cagrid.gridgrouper.service.tools;

import gov.nih.nci.cagrid.common.Utils;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class GridGrouperBootstrapper {

	public static final String ADD_ADMIN_OPT = "a";
	public static final String ADD_ADMIN_OPT_FULL = "addAdmin";
	public static final String USER_ID_OPT = "u";
	public static final String USER_ID_OPT_FULL = "userId";
	public static final String HELP_OPT = "h";
	public static final String HELP_OPT_FULL = "help";
	
	public static void addAdminMember(String memberId){
		
	}


	public static void main(String[] args) {
		Options options = new Options();
		Option help = new Option(HELP_OPT, HELP_OPT_FULL, false, "Prints this message.");
		Option addAdmin = new Option(ADD_ADMIN_OPT, ADD_ADMIN_OPT_FULL, false,
			"Specifies to add a grid grouper administrator.");
		addAdmin.setRequired(false);
		Option userId = new Option(USER_ID_OPT, USER_ID_OPT_FULL, true,
		"The user id of the user to add as a grid grouper administrator.");
		userId.setRequired(false);
		options.addOption(help);
		options.addOption(addAdmin);
		options.addOption(userId);

		try {
			CommandLineParser parser = new PosixParser();
			CommandLine line = parser.parse(options, args);

			if (line.hasOption(HELP_OPT)) {
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp(GridGrouperBootstrapper.class.getName(), options);
				System.exit(0);
			} else {
				boolean printMenu = true;
				if (line.hasOption(ADD_ADMIN_OPT)) {
					printMenu = false;
					addAdminMember(Utils.clean(line.getOptionValue(USER_ID_OPT)));
				}
				if(printMenu){
					
				}
				
			}

		} catch (ParseException exp) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(GridGrouperBootstrapper.class.getName(), options, false);
			System.exit(1);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

	}

}
