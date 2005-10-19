
package gov.nih.nci.cagrid.gums.portal.attributes;

import gov.nih.nci.cagrid.gums.portal.AttributeViewer;
import gov.nih.nci.cagrid.gums.portal.GumsLookAndFeel;
import gov.nih.nci.cagrid.gums.portal.GumsPortalSimpleFrame;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.projectmobius.common.XMLUtilities;
import org.projectmobius.portal.GridPortalComponent;

/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public class PersonAttributeViewer extends GumsPortalSimpleFrame implements AttributeViewer{

	private static final String FIRST_NAME_LABEL = "First Name";
	private static final String LAST_NAME_LABEL = "Last Name";
	private static final String STREET_LABEL = "Street";
	private static final String CITY_LABEL = "City";
	private static final String STATE_LABEL = "State";
	private static final String ZIP_CODE_LABEL = "Zip";
	private static final String PHONE_NUMBER_LABEL = "Phone Number";
	private static final String EMAIL_LABEL = "Email";
	
	private static final String NAMESPACE = "cagrid.nci.nih.gov/1/person";
	private static final String NAME = "person";
	private static final String FIRST_NAME = "first-name";
	private static final String LAST_NAME = "last-name";
	private static final String ADDRESS = "address";
	private static final String STREET = "street";
	private static final String CITY = "city";
	private static final String STATE = "state";
	private static final String ZIP = "zip";
	private static final String PHONE = "phone-number";
	private static final String EMAIL = "email";

	private boolean enabled;
	
	public PersonAttributeViewer(){
		super("Ok", "Personal Information");
		this.setTitle("Personal Information");
		this.enabled = true;
		this.addContent(FIRST_NAME_LABEL,0,enabled);
		this.addContent(LAST_NAME_LABEL,1,enabled);
		this.addContent(STREET_LABEL,2,enabled);
		this.addContent(CITY_LABEL,3,enabled);
		this.addContent(STATE_LABEL,4,enabled);
		this.addContent(ZIP_CODE_LABEL,5,enabled);
		this.addContent(PHONE_NUMBER_LABEL,6,enabled);
		this.addContent(EMAIL_LABEL,7,enabled);	
		this.setFrameIcon(GumsLookAndFeel.getHomeIcon());
	}
	
	

	public void fromXML(String xml) throws Exception {
		Document doc =XMLUtilities.stringToDocument(xml);
		Element person = doc.getRootElement();
		Namespace ns = Namespace.getNamespace(NAMESPACE);
		this.setContent(FIRST_NAME_LABEL,person.getChildText(FIRST_NAME,ns));
		this.setContent(LAST_NAME_LABEL,person.getChildText(LAST_NAME,ns));
		Element addr = person.getChild(ADDRESS,ns);
		this.setContent(STREET_LABEL,addr.getChildText(STREET,ns));
		this.setContent(CITY_LABEL,addr.getChildText(CITY,ns));
		this.setContent(STATE_LABEL,addr.getChildText(STATE,ns));
		this.setContent(ZIP_CODE_LABEL,addr.getChildText(ZIP,ns));
		this.setContent(PHONE_NUMBER_LABEL,person.getChildText(PHONE,ns));
		this.setContent(EMAIL_LABEL,person.getChildText(EMAIL,ns));
	}
	
	public GridPortalComponent getGumsPortalInternalFrame() {
		return this;
	}
	
	public String getXMLName() {
		return NAME;
	}

	public String getXMLNamespace() {
		return NAMESPACE;
	}

	
	public String toXML() throws Exception {
		try{
		StringBuffer xml = new StringBuffer();
		xml.append("<"+NAME+" xmlns=\""+getXMLNamespace()+"\">\n");
	    xml.append("<"+FIRST_NAME+">").append(this.getContentErrorIfEmpty(FIRST_NAME_LABEL)).append("</"+FIRST_NAME+">\n");
	    xml.append("<"+LAST_NAME+">").append(this.getContentErrorIfEmpty(LAST_NAME_LABEL)).append("</"+LAST_NAME+">\n");
	    xml.append("<"+ADDRESS+">\n");
	    xml.append("<"+STREET+">").append(this.getContentErrorIfEmpty(STREET_LABEL)).append("</"+STREET+">\n");
	    xml.append("<"+CITY+">").append(this.getContentErrorIfEmpty(CITY_LABEL)).append("</"+CITY+">\n");
	    xml.append("<"+STATE+">").append(this.getContentErrorIfEmpty(STATE_LABEL)).append("</"+STATE+">\n");
	    xml.append("<"+ZIP+">").append(this.getContentErrorIfEmpty(ZIP_CODE_LABEL)).append("</"+ZIP+">\n");  
	    xml.append("</"+ADDRESS+">\n");
	    xml.append("<"+EMAIL+">").append(this.getContentErrorIfEmpty(EMAIL_LABEL)).append("</"+EMAIL+">\n");  
	    xml.append("<"+PHONE+">").append(this.getContentErrorIfEmpty(PHONE_NUMBER_LABEL)).append("</"+PHONE+">\n");  
	    xml.append("</"+NAME+">\n");
		return xml.toString();
		}catch(Exception e){
			throw new Exception("Error validating the attribute "+NAMESPACE+"/"+NAME+", "+e.getMessage());
		}
	}
	private void addContent(String label,int index, boolean enabled){
		this.addContent(label,"",index,enabled);
	}
	
	
	protected void perform() {
		this.dispose();
	}
}
