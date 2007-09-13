package gov.nih.nci.cagrid.dorian.service.upgrader;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.dorian.conf.DorianConfiguration;
import gov.nih.nci.cagrid.dorian.service.PropertyManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cagrid.tools.database.Database;


public class Upgrader {

	public static final String HELP_OPT = "h";

	public static final String HELP_OPT_FULL = "help";

	public static final String CONFIG_FILE_OPT = "f";

	public static final String CONFIG_FILE_FULL = "conf";

	public static final String TRIAL_OPT = "t";

	public static final String TRIAL_OPT_FULL = "trial";

	private DorianConfiguration conf;

	private Log log;
	private PropertyManager properties;
	private Map<Float, Upgrade> upgradeSet;


	public Upgrader(DorianConfiguration conf) throws Exception {
		this.conf = conf;
		this.log = LogFactory.getLog(this.getClass().getName());
		Database db = new Database(conf.getDatabaseConfiguration(), conf.getDorianInternalId());
		db.createDatabaseIfNeeded();
		this.properties = new PropertyManager(db);
		buildUpgraders();
	}


	private void buildUpgraders() {
		upgradeSet = new HashMap<Float, Upgrade>();
		// Upgrade u = new Upgrade1_0To1_1();
		//upgradeSet.put(u.getStartingVersion(), u);
	}


	public void upgrade(boolean trialRun) {
		try {

			List<Upgrade> upgrades = determineUpgrades(this.properties.getVersion(), new ArrayList());
			if (upgrades.size() == 0) {
				log.info("No upgrades required, Dorian is already upgraded to the latest version ("
					+ PropertyManager.CURRENT_VERSION + ").");
			} else {
				log.info("Attempting to upgrade Dorian from version " + properties.getVersion() + " to version "
					+ PropertyManager.CURRENT_VERSION + ".");
				for (int i = 0; i < upgrades.size(); i++) {
					Upgrade u = upgrades.get(i);
					if (properties.getVersion() != u.getStartingVersion()) {
						if (!trialRun) {		
							throw new Exception("Cannot run the upgrader " + getClass().getName()
								+ ", this upgrader starts with version 1.0, your system is using version "
								+ properties.getVersion() + ".");
						}
					}

					log.info("Attempting to run upgrader " + u.getClass().getName() + " which upgrades from Dorian "
						+ u.getStartingVersion() + " to Dorian " + u.getUpgradedVersion() + ".");
					u.upgrade(conf, trialRun);

					if (!trialRun) {
						log.info("Dorian upgraded from version " + u.getStartingVersion() + " to version "
							+ u.getUpgradedVersion() + ".");
					}
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}

	}


	private List<Upgrade> determineUpgrades(float version, List<Upgrade> upgrades) throws Exception {
		if (version == PropertyManager.CURRENT_VERSION) {
			return upgrades;
		} else {
			Upgrade u = upgradeSet.get(Float.valueOf(version));
			if (u == null) {
				throw new Exception("No upgrade to version " + version + " could be determined.");
			} else {
				upgrades.add(u);
				return determineUpgrades(u.getUpgradedVersion(), upgrades);
			}
		}
	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Options options = new Options();
		Option service = new Option(CONFIG_FILE_OPT, CONFIG_FILE_FULL, true, "The config file for the Dorian CA.");
		service.setRequired(true);
		Option help = new Option(HELP_OPT, HELP_OPT_FULL, false, "Prints this message.");
		Option trial = new Option(TRIAL_OPT, TRIAL_OPT_FULL, false, "Prints this message.");

		options.addOption(help);
		options.addOption(service);
		options.addOption(trial);

		try {
			CommandLineParser parser = new PosixParser();
			CommandLine line = parser.parse(options, args);

			if (line.getOptionValue(HELP_OPT) != null) {
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp(Upgrader.class.getName(), options);
				System.exit(0);
			} else {
				String configFile = line.getOptionValue(CONFIG_FILE_OPT);
				gov.nih.nci.cagrid.dorian.conf.DorianConfiguration c = (DorianConfiguration) Utils.deserializeDocument(
					configFile, gov.nih.nci.cagrid.dorian.conf.DorianConfiguration.class);
				boolean trialRun = false;
				if (line.hasOption(TRIAL_OPT)) {
					trialRun = true;
				}
				Upgrader u = new Upgrader(c);
				u.upgrade(trialRun);
			}
		} catch (ParseException exp) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(Upgrader.class.getName(), options, false);
			System.exit(1);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

	}

}
