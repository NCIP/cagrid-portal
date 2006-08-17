import gov.nih.nci.cadsr.umlproject.domain.Project;
import gov.nih.nci.cagrid.cadsr.common.DomainModelBuilder;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;
import gov.nih.nci.system.applicationservice.ApplicationService;


public class DomainModelGenerationExample {

	public static void main(String[] args) {
		try {
			ApplicationService appService = ApplicationService
				.getRemoteInstance("http://cabio.nci.nih.gov/cacore31/http/remoteService");
			DomainModelBuilder builder = new DomainModelBuilder(appService);

			Project project = new Project();
			project.setVersion("3");
			project.setShortName("caCORE");
			System.out.println("Creating domain model for project: " + project.getShortName() + " (version:"
				+ project.getVersion() + ")");

			long start = System.currentTimeMillis();

			// Whole project
			// DomainModel domainModel = builder.createDomainModel(project);

			// For a single package
			// DomainModel domainModel =
			// builder.createDomainModelForPackages(project, new
			// String[]{"gov.nih.nci.cabio.domain"});

			// For a specific set of classes
			DomainModel domainModel = builder.createDomainModelForClasses(project, new String[]{
					"gov.nih.nci.cadsr.domain.Concept", "gov.nih.nci.cabio.domain.Gene",
					"gov.nih.nci.cabio.domain.Taxon"});

//			MetadataUtils.serializeDomainModel(domainModel, new FileWriter(project.getShortName() + "_"
//				+ project.getVersion() + "_DomainModel.xml"));
//	
			double duration = (System.currentTimeMillis() - start) / 1000.0;
			System.out.println("Domain Model generation took:" + duration + " seconds.");

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
