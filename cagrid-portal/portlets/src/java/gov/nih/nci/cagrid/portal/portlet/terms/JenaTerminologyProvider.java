/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-portal/LICENSE.txt for details.
*============================================================================
**/
/**
 *
 */
package gov.nih.nci.cagrid.portal.portlet.terms;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.Ontology;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * @author kherm manav.kher@semanticbits.com
 */

public class JenaTerminologyProvider implements TerminologyProvider {

    private static final Log logger = LogFactory
            .getLog(JenaTerminologyProvider.class);

    Map<URI, OntModel> models = new HashMap<URI, OntModel>();

    /**
     *
     */
    public JenaTerminologyProvider() {

    }

    /*
      * (non-Javadoc)
      *
      * @see
      * gov.nih.nci.cagrid.portal.semweb.TerminologyProvider#getChildTerms(gov
      * .nih.nci.cagrid.portal.semweb.TerminologyBean)
      */
    public List<TermBean> getChildTerms(TermBean term) {
        return getSubClasses(term, true);
    }

    private OntModel getModel(TerminologyBean terminology) {
        try {
            return getModels().get(new URI(terminology.getUri()));
        } catch (URISyntaxException ex) {
            throw new RuntimeException("Bad terminology URI: "
                    + ex.getMessage(), ex);
        }
    }

    /*
      * (non-Javadoc)
      *
      * @see
      * gov.nih.nci.cagrid.portal.semweb.TerminologyProvider#getDescendants(gov
      * .nih.nci.cagrid.portal.semweb.TerminologyBean)
      */
    public List<TermBean> getDescendants(TermBean term) {
        return getSubClasses(term, false);
    }

    private List<TermBean> getSubClasses(TermBean term, boolean direct) {
        List<TermBean> descTerms = new ArrayList<TermBean>();
        OntModel model = getModel(term.getTerminology());
        OntClass klass = model.getOntClass(term.getUri());
        for (ExtendedIterator<OntClass> i = klass.listSubClasses(direct); i
                .hasNext();) {
            OntClass childClass = i.next();
            descTerms.add(newTermBean(term.getTerminology(), childClass));
        }
        return descTerms;
    }

    /*
      * (non-Javadoc)
      *
      * @see
      * gov.nih.nci.cagrid.portal.semweb.TerminologyProvider#getPathToRoot(gov
      * .nih.nci.cagrid.portal.semweb.TerminologyBean)
      */
    public List<TermBean> getPathToRoot(TermBean term) {
        // Set<OntClass> seen = new HashSet<OntClass>();

        List<TermBean> anscTerms = new ArrayList<TermBean>();
        // OntModel model = getModels().get(term.getTerminology());
        // TerminologyBean terminology = getTerminology(term);
        // OntClass superClass = model.getOntClass(term.getUri());
        // while (superClass != null) {
        // List<OntClass> scs = new ArrayList<OntClass>();
        // for (ExtendedIterator<OntClass> i = superClass
        // .listSuperClasses(true); i.hasNext();) {
        // scs.add(i.next());
        // }
        // if (scs.size() > 0) {
        // OntClass sc = scs.iterator().next();
        // if (scs.size() > 1) {
        // logger.warn("Found " + scs.size() + " parents for "
        // + superClass.getURI() + ", taking " + sc.getURI());
        // }
        // anscTerms.add(new TermBean(terminology, sc.getURI(), sc
        // .getLabel(null), sc.getComment(null)));
        // superClass = sc;
        // }else{
        // superClass = null;
        // }
        // }

        return anscTerms;
    }

    /*
      * (non-Javadoc)
      *
      * @see
      * gov.nih.nci.cagrid.portal.semweb.TerminologyProvider#listTerminologies()
      */
    public List<TerminologyBean> listTerminologies() {
        List<TerminologyBean> terminologyBeans = new ArrayList<TerminologyBean>();
        for (URI uri : getModels().keySet()) {
            OntModel model = getModels().get(uri);
            terminologyBeans.add(newTerminologyBean(uri, model));
        }
        return terminologyBeans;
    }

    protected TerminologyBean newTerminologyBean(URI uri, OntModel model) {
        Ontology ont = model.getOntology(uri.toString());
        return new TerminologyBean(uri.toString(), ont.getLabel(null), ont
                .getComment(null));
    }

    public static void main(String[] args) {
        try {

            OntModel model = ModelFactory
                    .createOntologyModel(OntModelSpec.OWL_MEM);
            InputStream modelFile = JenaTerminologyProvider.class.getClassLoader().getResourceAsStream("terms/area_of_focus_1_0.owl");
            model.read(modelFile, null);

            Map<URI, OntModel> m = new HashMap<URI, OntModel>();
            String uri = "http://cagrid.org/terms/area_of_focus_1_0.owl";
            Ontology ont = model.getOntology(uri);
            m.put(new URI(uri), model);

            JenaTerminologyProvider p = new JenaTerminologyProvider();
            p.setModels(m);
            for (TerminologyBean terminologyBean : p.listTerminologies()) {
                System.out.println(terminologyBean);
                for (TermBean rootTerm : p.getRootTerms(terminologyBean)) {
                    printHierarchy(p, rootTerm, 1);
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void printHierarchy(JenaTerminologyProvider p,
                                       TermBean parentTerm, int depth) {
        for (int i = 0; i < depth; i++) {
            System.out.print("\t");
        }
        System.out.println(parentTerm);
        for (Iterator<TermBean> i = p.getChildTerms(parentTerm).iterator(); i
                .hasNext();) {
            printHierarchy(p, i.next(), depth + 1);
        }
    }

    public Map<URI, OntModel> getModels() {
        return models;
    }

    public void setModels(Map<URI, OntModel> models) {
        this.models = models;
    }

    public List<TermBean> getRootTerms(TerminologyBean terminology) {
        List<TermBean> rootTerms = new ArrayList<TermBean>();
        OntModel model = null;
        try {
            model = getModels().get(new URI(terminology.getUri()));
        } catch (URISyntaxException ex) {
            throw new RuntimeException("Bad terminology URI: "
                    + ex.getMessage(), ex);
        }
        for (ExtendedIterator<OntClass> i = model.listHierarchyRootClasses(); i
                .hasNext();) {
            OntClass c = i.next();
            TermBean rootTerm = newTermBean(terminology, c);
            rootTerms.add(rootTerm);
        }
        return rootTerms;
    }

    protected TermBean newTermBean(TerminologyBean terminology, OntClass c) {
        return new TermBean(terminology, c.getURI(), c.getLabel(null), c
                .getComment(null));
    }

    public TermBean getTermForUri(String termUriStr) {
        TermBean term = null;
        URI ontUri = null;
        OntModel ontModel = null;
        OntClass ontClass = null;
        for (URI uri : getModels().keySet()) {
            ontUri = uri;
            ontModel = getModels().get(uri);
            ontClass = ontModel.getOntClass(termUriStr);
            if (ontClass != null) {
                break;
            }
        }
        if (ontClass != null) {
            term = newTermBean(newTerminologyBean(ontUri, ontModel), ontClass);
        }
        return term;
    }

}
