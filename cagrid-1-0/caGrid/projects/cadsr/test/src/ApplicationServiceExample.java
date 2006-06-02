import gov.nih.nci.cadsr.umlproject.domain.Project;
import gov.nih.nci.system.applicationservice.ApplicationService;

import java.util.Iterator;
import java.util.List;


public class ApplicationServiceExample {

	public static void main(String[] args) {
		try {
			ApplicationService appService = ApplicationService
				.getRemoteInstance("http://cabio.nci.nih.gov/cacore31/http/remoteService");

			List rList = appService.search(Project.class, new Project());
			for (Iterator resultsIterator = rList.iterator(); resultsIterator.hasNext();) {
				Project project = (Project) resultsIterator.next();
				System.out.println(project.getLongName());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
