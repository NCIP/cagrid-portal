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

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.core.io.Resource;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * @author kherm manav.kher@semanticbits.com
 */
public class JenaFileModelFactoryBean implements FactoryBean {

    private Resource modelFile;
    private OntModelSpec modelSpec;

    /**
     *
     */
    public JenaFileModelFactoryBean() {

    }

    /*
      * (non-Javadoc)
      *
      * @see org.springframework.beans.factory.FactoryBean#getObject()
      */
    public Object getObject() throws Exception {
        OntModel model = ModelFactory.createOntologyModel(getModelSpec());
        model.read(modelFile.getInputStream(), null);
        return model;
    }

    /*
      * (non-Javadoc)
      *
      * @see org.springframework.beans.factory.FactoryBean#getObjectType()
      */
    public Class getObjectType() {
        return OntModel.class;
    }

    /*
      * (non-Javadoc)
      *
      * @see org.springframework.beans.factory.FactoryBean#isSingleton()
      */
    public boolean isSingleton() {
        return true;
    }

    public Resource getModelFile() {
        return modelFile;
    }

    public void setModelFile(Resource modelFile) {
        this.modelFile = modelFile;
    }

    public OntModelSpec getModelSpec() {
        return modelSpec;
    }

    public void setModelSpec(OntModelSpec modelSpec) {
        this.modelSpec = modelSpec;
    }

}
