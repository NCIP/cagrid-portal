package gov.nih.nci.cagrid.portal.portlet.terms;

import com.hp.hpl.jena.ontology.OntModel;
import gov.nih.nci.cagrid.portal.portlet.PortalPortletIntegrationTestBase;

import java.net.URI;
import java.util.Iterator;
import java.util.Map;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class JenaTerminologyProviderTest extends PortalPortletIntegrationTestBase {

    public void testCreate() {
        JenaTerminologyProvider terminologyProvider = (JenaTerminologyProvider) getBean("terminologyProvider");
        Map<URI, OntModel> models = terminologyProvider.getModels();
        for (Iterator iter = models.entrySet().iterator(); iter.hasNext();) {
            Map.Entry entry = (Map.Entry) iter.next();
            OntModel model = (OntModel) entry.getValue();
            assertNotNull(model);

            JenaTerminologyProvider p = new JenaTerminologyProvider();
            p.setModels(models);
            for (TerminologyBean terminologyBean : p.listTerminologies()) {
                System.out.println(terminologyBean);
                for (TermBean rootTerm : p.getRootTerms(terminologyBean)) {
                    assertNotNull(rootTerm);
                }
            }
        }

    }
}
