package gov.nih.nci.cagrid.sdkquery4.processor.testing;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;
import gov.nih.nci.cagrid.sdkquery4.beans.domaininfo.DomainTypesInformation;
import gov.nih.nci.cagrid.sdkquery4.processor.CQL2ParameterizedHQL;
import gov.nih.nci.cagrid.sdkquery4.processor.DomainTypesInformationUtil;
import gov.nih.nci.cagrid.sdkquery4.processor.ParameterizedHqlQuery;
import gov.nih.nci.cagrid.sdkquery4.processor.RoleNameResolver;
import gov.nih.nci.system.applicationservice.ApplicationService;
import gov.nih.nci.system.client.ApplicationServiceProvider;
import gov.nih.nci.system.query.hibernate.HQLCriteria;

import java.io.FileReader;
import java.util.List;

/** 
 *  HQLGenerator
 *  TODO:DOCUMENT ME
 * 
 * @author David Ervin
 * 
 * @created Feb 14, 2008 11:26:11 AM
 * @version $Id: HQLGenerator.java,v 1.1 2008-02-15 14:45:02 dervin Exp $ 
 */
public class HQLGenerator {
    
    

    public static void main(String[] args) {
        try {
            FileReader typesInfoReader = new FileReader("test/resources/example40-domainTypesInformation.xml");
            DomainTypesInformation dti = DomainTypesInformationUtil.deserializeDomainTypesInformation(typesInfoReader);
            typesInfoReader.close();
            FileReader modelReader = new FileReader("test/resources/sdkExampleDomainModel.xml");
            DomainModel model = (DomainModel) Utils.deserializeObject(modelReader, DomainModel.class);
            modelReader.close();
            RoleNameResolver resolver = new RoleNameResolver(model);
            
            CQL2ParameterizedHQL translator = new CQL2ParameterizedHQL(dti, resolver, false);
            
            FileReader queryReader = new FileReader("test/resources/testQueries/nestedGroups.xml");
            CQLQuery query = (CQLQuery) Utils.deserializeObject(queryReader, CQLQuery.class);
            queryReader.close();
            
            long start = System.currentTimeMillis();
            ParameterizedHqlQuery hql = translator.convertToHql(query);
            System.out.println("Conversion in " + (System.currentTimeMillis() - start));
            System.out.println(hql);
            
            ApplicationService service = ApplicationServiceProvider.getApplicationServiceFromUrl("http://kramer.bmi.ohio-state.edu:8080/example40");
            List results = service.query(new HQLCriteria(hql.getHql(), hql.getParameters()));
            for (Object o : results) {
                System.out.println(o);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
