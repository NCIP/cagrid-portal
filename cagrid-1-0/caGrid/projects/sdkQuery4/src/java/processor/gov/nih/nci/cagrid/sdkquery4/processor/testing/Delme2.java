package gov.nih.nci.cagrid.sdkquery4.processor.testing;

import java.io.FileWriter;

import javax.xml.namespace.QName;

import gov.nih.nci.cacoresdk.domain.inheritance.multiplechild.Student;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.cqlquery.Attribute;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlquery.Object;
import gov.nih.nci.cagrid.cqlquery.Predicate;
import gov.nih.nci.cagrid.data.DataServiceConstants;
import gov.nih.nci.cagrid.data.mapping.ClassToQname;
import gov.nih.nci.cagrid.data.mapping.Mappings;


/** 
 *  Delme2
 *  TODO:DOCUMENT ME
 * 
 * @author David Ervin
 * 
 * @created Dec 4, 2007 1:42:58 PM
 * @version $Id: Delme2.java,v 1.1 2008-01-18 15:13:29 dervin Exp $ 
 */
public class Delme2 {

    /**
     * @param args
     */
    public static void main(String[] args) {
        
        Mappings map = new Mappings();
        ClassToQname c2q = new ClassToQname();
        c2q.setClassName(Student.class.getName());
        c2q.setQname(new QName("gme://caCORE.caCORE/4.0/gov.nih.nci.cacoresdk.domain.inheritance.multiplechild", "Student").toString());
        map.setMapping(new ClassToQname[] {c2q});
        
        try {
            Utils.serializeDocument("mapping.xml", map, DataServiceConstants.MAPPING_QNAME);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        /*
        CQLQuery query = new CQLQuery();
        Object target = new Object();
        
        target.setName(Student.class.getName());
        Attribute idAttrib = new Attribute("name", Predicate.EQUAL_TO, "Student_Name1");
        target.setAttribute(idAttrib);
        
        query.setTarget(target);
        
        try {
            FileWriter writer = new FileWriter("studentWithName.xml");
            Utils.serializeObject(query, DataServiceConstants.CQL_QUERY_QNAME, writer);
            writer.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        */
    }
}
