package gov.nih.nci.cagrid.portal.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.annotation.Aspect;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
@Aspect
public class ActivateFilterAspect  extends HibernateDaoSupport {

    String filterName;
    String parameterName;
    Object parameterValue;

    private static final Log logger = LogFactory
            .getLog(ActivateFilterAspect.class);


    public void enableFilter(){
        logger.debug("Activating filter :" + filterName);
        try {
            Session session = getSession(false);
            if(session!=null){
                Filter filter = session.getEnabledFilter(filterName);
                if (filter == null) {
                    filter = session.enableFilter(getFilterName()).setParameter(parameterName, parameterValue);
                }
            }
            else
                logger.debug("No hibernate session available. Will not apply filters");
        } catch (Exception e) {

        }
    }

    public String getFilterName() {
        return filterName;
    }

    public void setFilterName(String filterName) {
        this.filterName = filterName;
    }

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    public Object getParameterValue() {
        return parameterValue;
    }

    public void setParameterValue(Object parameterValue) {
        this.parameterValue = parameterValue;
    }
}
