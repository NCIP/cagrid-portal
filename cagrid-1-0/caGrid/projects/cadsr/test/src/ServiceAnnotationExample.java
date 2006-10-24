import gov.nih.nci.cagrid.cadsr.client.CaDSRServiceClient;
import gov.nih.nci.cagrid.metadata.MetadataUtils;
import gov.nih.nci.cagrid.metadata.ServiceMetadata;

import java.io.FileReader;
import java.io.FileWriter;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.WindowConstants;


/**
 * @author oster
 * 
 */
public class ServiceAnnotationExample {

	public static void main(String[] args) {
		try {
			JFrame f = new JFrame();
			f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			CaDSRServiceClient client = new CaDSRServiceClient(args[0]);
			JFileChooser fc = new JFileChooser(".");
			fc.showOpenDialog(f);

			ServiceMetadata metadata = MetadataUtils.deserializeServiceMetadata(new FileReader(fc.getSelectedFile()));

			metadata=client.annotateServiceMetadata(metadata);
			FileWriter fw = new FileWriter("annotatedServiceMetadata.xml");
			MetadataUtils.serializeServiceMetadata(metadata, fw);
			fw.close();

			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
}
