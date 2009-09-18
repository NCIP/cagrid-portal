/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.terms;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.core.io.Resource;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
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
		model.read("file:" + getModelFile().getFile().getAbsolutePath());
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
