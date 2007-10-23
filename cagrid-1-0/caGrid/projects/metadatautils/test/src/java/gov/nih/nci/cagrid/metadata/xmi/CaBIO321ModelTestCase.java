package gov.nih.nci.cagrid.metadata.xmi;

import java.io.File;

/** 
 *  CaBIO321ModelTestCase
 *  TODO:DOCUMENT ME
 * 
 * @author David Ervin
 * 
 * @created Oct 23, 2007 10:47:35 AM
 * @version $Id: CaBIO321ModelTestCase.java,v 1.1 2007-10-23 14:54:22 dervin Exp $ 
 */
public class CaBIO321ModelTestCase extends BaseXmiToModelTest {
    
    public CaBIO321ModelTestCase() {
        super("caBIO 3_2_1 XMI to Model Test");
    }
    

    public XmiToModelInfo getInfo() {
        String prefix = "test" + File.separator + "resources" + File.separator;
        prefix += CaBIO321ModelTestCase.class.getPackage().getName().replace('.', File.separatorChar);
        XmiToModelInfo info = new XmiToModelInfo(
            prefix + File.separator + "sdk321" + File.separator + "fixed_cabioExampleDomainModel.xmi",
            prefix + File.separator + "sdk321" + File.separator + "goldDomainModel.xml",
            "caBIO",
            "3.2.1");
        return info;
    }
}
