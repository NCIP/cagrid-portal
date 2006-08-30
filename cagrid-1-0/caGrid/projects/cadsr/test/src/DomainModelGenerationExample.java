import gov.nih.nci.cabio.domain.Chromosome;
import gov.nih.nci.cabio.domain.Gene;
import gov.nih.nci.cabio.domain.Taxon;
import gov.nih.nci.cabio.domain.Tissue;
import gov.nih.nci.cadsr.umlproject.domain.Project;
import gov.nih.nci.cagrid.cadsr.common.DomainModelBuilder;
import gov.nih.nci.cagrid.cadsr.domain.UMLAssociationExclude;
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

			// UNCOMMENT FOR: Whole project
			// DomainModel domainModel = builder.createDomainModel(project);

			// UNCOMMENT FOR: a single package
			// DomainModel domainModel =
			// builder.createDomainModelForPackages(project, new
			// String[]{"gov.nih.nci.cabio.domain"});

			// UNCOMMENT FOR: a specific set of classes
			// String classNames[] = new String[]{Gene.class.getName(),
			// Taxon.class.getName()};
			// DomainModel domainModel =
			// builder.createDomainModelForClasses(project, classNames);

			// UNCOMMENT FOR: a specific set of classes, with excluded
			// associations
			String classNames[] = new String[]{Gene.class.getName(), Chromosome.class.getName(), Taxon.class.getName(),
					Tissue.class.getName()};
			UMLAssociationExclude exclude1 = new UMLAssociationExclude(Gene.class.getName(), "geneCollection",
				Chromosome.class.getName(), "chromosome");
			UMLAssociationExclude exclude2 = new UMLAssociationExclude("*", "*", Tissue.class.getName(), "*");
			UMLAssociationExclude associationExcludes[] = new UMLAssociationExclude[]{exclude1, exclude2};
			DomainModel domainModel = builder.createDomainModelForClassesWithExcludes(project, classNames,
				associationExcludes);

			// MetadataUtils.serializeDomainModel(domainModel, new
			// FileWriter(project.getShortName() + "_"
			// + project.getVersion() + "_DomainModel.xml"));

			double duration = (System.currentTimeMillis() - start) / 1000.0;
			System.out.println("Domain Model generation took:" + duration + " seconds.");

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
