package gov.nih.nci.cagrid.sdkquery4.processor;

import java.util.List;

/** 
 *  ParameterizedHqlQuery
 *  Represents an HQL query using positional parameters
 * 
 * @author David Ervin
 * 
 * @created Dec 12, 2007 12:35:41 PM
 * @version $Id: ParameterizedHqlQuery.java,v 1.1 2007-12-12 17:37:39 dervin Exp $ 
 */
public class ParameterizedHqlQuery {

    private String hql;
    private List<String> parameters;
    
    public ParameterizedHqlQuery(String hql, List<String> parameters) {
        this.hql = hql;
        this.parameters = parameters;
    }
    
    
    public String getHql() {
        return hql;
    }
    
    
    public List<String> getParameters() {
        return parameters;
    }
}
