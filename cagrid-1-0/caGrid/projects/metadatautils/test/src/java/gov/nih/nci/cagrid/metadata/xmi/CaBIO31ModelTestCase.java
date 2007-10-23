package gov.nih.nci.cagrid.metadata.xmi;

import java.io.File;

/** 
 *  CaBIO31ModelTestCase
 *  TODO:DOCUMENT ME
 * 
 * @author David Ervin
 * 
 * @created Oct 23, 2007 10:47:35 AM
 * @version $Id: CaBIO31ModelTestCase.java,v 1.1 2007-10-23 14:59:53 dervin Exp $ 
 */
public class CaBIO31ModelTestCase extends BaseXmiToModelTest {
    
    public CaBIO31ModelTestCase() {
        super("caBIO 3_1 XMI to Model Test");
    }
    

    public XmiToModelInfo getInfo() {
        String prefix = "test" + File.separator + "resources" + File.separator;
        prefix += CaBIO31ModelTestCase.class.getPackage().getName().replace('.', File.separatorChar);
        XmiToModelInfo info = new XmiToModelInfo(
            prefix + File.separator + "sdk31" + File.separator + "fixed_cabioExampleDomainModel.xmi",
            prefix + File.separator + "sdk31" + File.separator + "goldDomainModel.xml",
            "caBIO",
            "3.1");
        return info;
    }
}
