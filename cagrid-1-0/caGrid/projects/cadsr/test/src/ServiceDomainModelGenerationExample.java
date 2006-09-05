import gov.nih.nci.cabio.domain.Chromosome;
import gov.nih.nci.cabio.domain.Gene;
import gov.nih.nci.cabio.domain.Taxon;
import gov.nih.nci.cabio.domain.Tissue;
import gov.nih.nci.cadsr.umlproject.domain.Project;
import gov.nih.nci.cagrid.cadsr.client.CaDSRServiceClient;
import gov.nih.nci.cagrid.cadsr.domain.UMLAssociationExclude;
import gov.nih.nci.cagrid.metadata.MetadataUtils;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;

import java.io.FileWriter;


public class ServiceDomainModelGenerationExample {

	public static void main(String[] args) {
		try {
			String url = "http://localhost:8080/wsrf/services/cagrid/CaDSRService";
			if (args.length == 1) {
				url = args[0];
			}

			CaDSRServiceClient cadsr = new CaDSRServiceClient(url);

			Project project = new Project();
			project.setVersion("3");
			project.setShortName("caCORE");
			System.out.println("Creating domain model for project: " + project.getShortName() + " (version:"
				+ project.getVersion() + ")");

			long start = System.currentTimeMillis();

			// UNCOMMENT FOR: Whole project
			// DomainModel domainModel =
			// cadsr.generateDomainModelForProject(project);

			// UNCOMMENT FOR: a single package
			// DomainModel domainModel =
			// cadsr.generateDomainModelForPackages(project,
			// new String[]{"gov.nih.nci.cabio.domain"});

			// UNCOMMENT FOR: a specific set of classes
			// String classNames[] = new String[]{Gene.class.getName(),
			// Taxon.class.getName()};
			// DomainModel domainModel =
			// cadsr.generateDomainModelForClasses(project, classNames);

			// UNCOMMENT FOR: a specific set of classes, with excluded
			// associations
			String classNames[] = new String[]{Gene.class.getName(), Chromosome.class.getName(), Taxon.class.getName(),
					Tissue.class.getName()};
			UMLAssociationExclude exclude1 = new UMLAssociationExclude(Gene.class.getName(), "geneCollection",
				Chromosome.class.getName(), "chromosome");
			UMLAssociationExclude exclude2 = new UMLAssociationExclude("*", "*", Tissue.class.getName(), "*");
			UMLAssociationExclude associationExcludes[] = new UMLAssociationExclude[]{exclude1, exclude2};
			DomainModel domainModel = cadsr.generateDomainModelForClassesWithExcludes(project, classNames,
				associationExcludes);

			MetadataUtils.serializeDomainModel(domainModel, new FileWriter(project.getShortName() + "_"
				+ project.getVersion() + "_DomainModel.xml"));

			double duration = (System.currentTimeMillis() - start) / 1000.0;
			System.out.println("Domain Model generation took:" + duration + " seconds.");
			System.out.println("\t exposed Association count:"
				+ domainModel.getExposedUMLAssociationCollection().getUMLAssociation().length);
			System.out.println("\t exposed Class count:"
				+ domainModel.getExposedUMLClassCollection().getUMLClass().length);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
