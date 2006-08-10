import gov.nih.nci.cadsr.umlproject.domain.Project;
import gov.nih.nci.cagrid.cadsr.common.DomainModelBuilder;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;
import gov.nih.nci.system.applicationservice.ApplicationService;

import java.util.Iterator;
import java.util.List;


public class DomainModelGenerationExample {

	public static void main(String[] args) {
		try {
			ApplicationService appService = ApplicationService
				.getRemoteInstance("http://cabio.nci.nih.gov/cacore31/http/remoteService");

			Project proto = new Project();
			proto.setVersion("3");
			proto.setShortName("caCORE");
			List rList = appService.search(Project.class, proto);
			DomainModelBuilder builder = new DomainModelBuilder(appService);

			for (Iterator resultsIterator = rList.iterator(); resultsIterator.hasNext();) {
				Project project = (Project) resultsIterator.next();
				System.out.println("Creating domain model for project: " + project.getLongName() + " (version:"
					+ project.getVersion() + ")");

				// create the model once
				long start = System.currentTimeMillis();
				DomainModel domainModel = builder.createDomainModel(project, new String[]{"gov.nih.nci.evs.domain",
						"gov.nih.nci.cabio.domain"});
				// MetadataUtils.serializeDomainModel(domainModel, new
				// FileWriter(project.getShortName()
				// + "_DomainModel.xml"));
				double duration = (System.currentTimeMillis() - start) / 1000.0;
				System.out.println("Domain Model generation took:" + duration);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
