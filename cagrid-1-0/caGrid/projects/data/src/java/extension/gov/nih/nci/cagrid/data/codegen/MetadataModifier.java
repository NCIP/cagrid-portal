package gov.nih.nci.cagrid.data.codegen;

import gov.nih.nci.cagrid.data.common.DataServiceConstants;
import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.info.ServiceInformation;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;

import java.util.HashSet;
import java.util.Set;

/** 
 *  MetadataModifier
 *  Performs modification on metadata for the data service
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created May 8, 2006 
 * @version $Id$ 
 */
public class MetadataModifier {
	
	private ServiceInformation info;
	private Set ignoreNamespaces;
	
	public MetadataModifier(ServiceInformation info) {
		this.info = info;
		ignoreNamespaces = new HashSet();
		ignoreDefaultNamespaces();
	}
	
	
	public void ignoreDefaultNamespaces() {
		ignoreNamespaces.clear();
		ignoreNamespaces.add(DataServiceConstants.CQL_QUERY_URI);
		ignoreNamespaces.add(DataServiceConstants.CQL_RESULT_SET_URI);
		ignoreNamespaces.add(IntroduceConstants.W3CNAMESPACE);
	}
	
	
	public void addIgnoreNamespace(String uri) {
		ignoreNamespaces.add(uri);
	}
	
	
	public void removeIgnoreNamespace(String uri) {
		ignoreNamespaces.remove(uri);
	}
	
	
	public Set getIgnoreNamespaces() {
		return ignoreNamespaces;
	}
	
	
	public DomainModel createDomainModel() {
		DomainModel model = new DomainModel();
		
		return model;
	}
}
