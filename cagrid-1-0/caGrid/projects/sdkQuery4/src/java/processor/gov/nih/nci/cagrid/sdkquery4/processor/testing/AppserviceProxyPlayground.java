package gov.nih.nci.cagrid.sdkquery4.processor.testing;

import gov.nih.nci.cacoresdk.domain.manytomany.bidirectional.Employee;
import gov.nih.nci.cacoresdk.domain.manytomany.bidirectional.Project;
import gov.nih.nci.cacoresdk.domain.onetoone.bidirectional.Product;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlquery.Object;
import gov.nih.nci.cagrid.sdkquery4.processor.CQL2ParameterizedHQL;
import gov.nih.nci.cagrid.sdkquery4.processor.ParameterizedHqlQuery;
import gov.nih.nci.system.applicationservice.ApplicationService;
import gov.nih.nci.system.client.ApplicationServiceProvider;
import gov.nih.nci.system.query.hibernate.HQLCriteria;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;



/** 
 *  AppserviceProxyPlayground
 *  TODO:DOCUMENT ME
 * 
 * @author David Ervin
 * 
 * @created Jan 7, 2008 12:55:41 PM
 * @version $Id: AppserviceProxyPlayground.java,v 1.1 2008-01-18 15:13:29 dervin Exp $ 
 */
public class AppserviceProxyPlayground {
    
    public static final String URL = "http://kramer.bmi.ohio-state.edu:8080/example40";

    /**
     * @param args
     */
    public static void main(String[] args) {
        
        try {
            // CQL2ParameterizedHQL converter = new CQL2ParameterizedHQL(
            //    new File("ext/lib/sdk/remote-client/lib/example40-beans.jar"), false);
            
            ApplicationService service = ApplicationServiceProvider.getApplicationService("ServiceInfo");
            
            CQLQuery query = new CQLQuery();
            Object target = new Object();
            target.setName(Product.class.getName());
            // Attribute idAttrib = new Attribute("name", Predicate.EQUAL_TO, "Student_Name1");
            // target.setAttribute(idAttrib);
            
            query.setTarget(target);
            
            // ParameterizedHqlQuery hql = converter.convertToHql(query);
            
            String rawHql = "From gov.nih.nci.cacoresdk.domain.manytomany.bidirectional.Project p join fetch p.employeeCollection as foo join fetch foo.projectCollection";
            ParameterizedHqlQuery hql = new ParameterizedHqlQuery(rawHql, new ArrayList<java.lang.Object>());
            System.out.println("Query:");
            System.out.println(hql);
            
            HQLCriteria criteria = new HQLCriteria(hql.getHql(), hql.getParameters());
            
            List<java.lang.Object> results = service.query(criteria);
            for (java.lang.Object o : results) {
                System.out.println(o.getClass().getName());
                Project p = (Project) o;
                System.out.println("Project: " + p.getName());
                Collection<Employee> emps = p.getEmployeeCollection();
                for (Employee e : emps) {
                    System.out.println("\t" + e.getClass());
                    System.out.println("\tEmployee: " + e.getName());
                    Collection<Project> projs = e.getProjectCollection();
                    for (Project subP : projs) {
                        System.out.println("\t\t" + subP.getClass().getName());
                        System.out.println("\t\tSubproject: " + subP.getName());
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
