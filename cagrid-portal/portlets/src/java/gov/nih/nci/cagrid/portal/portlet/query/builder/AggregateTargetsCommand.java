package gov.nih.nci.cagrid.portal.portlet.query.builder;

import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.UMLClass;

import java.util.List;
import java.util.ArrayList;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class AggregateTargetsCommand {
    private List<String> selected = new ArrayList<String>();
    private List<UMLClass> available = new ArrayList<UMLClass>();


    public List<String> getSelected() {
        return selected;
    }

    public void setSelected(List<String> selected) {
        this.selected = selected;
    }

    public List<UMLClass> getAvailable() {
        return available;
    }

    public void setAvailable(List<UMLClass> available) {
        this.available = available;
    }


}
