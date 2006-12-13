package gov.nih.nci.cagrid.caarray;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import gov.nih.nci.cagrid.caarray.encoding.MGEDCubeHandler;
import gov.nih.nci.cagrid.caarray.encoding.Utils;
import gov.nih.nci.mageom.domain.BioAssayData.BioDataCube;
import gov.nih.nci.mageom.domain.BioAssayData.impl.BioDataCubeImpl;

public class TestSerialization {

	public static void main(String[] args) throws Exception {

//		System.setProperty("gov.nih.nci.cagrid.caarray.encoding.lineDelimiter", "yadda");
//		System.setProperty("gov.nih.nci.cagrid.caarray.encoding.lineDelimiterPattern", "yadda");
		
		BioDataCube bdc = getBioDataCube();
		String xml = Utils.toXML(bdc);
		System.out.println("XML:\n" + xml);

		DocumentBuilder builder = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder();
		Element el = builder.parse(new InputSource(new StringReader(xml)))
				.getDocumentElement();
		bdc = (BioDataCube) Utils.fromXML(el);

		// bdc = (BioDataCube) Utils.fromXML(xml);
		String str = MGEDCubeHandler.getCubeAsString(bdc.getCube(),
				"\n",
				"\t");
		System.out.println("Cube:\n" + str);
	}

	public static BioDataCube getBioDataCube() {
		BioDataCubeImpl bdc = null;

		int dim1 = 3;
		int dim2 = 3;
		int dim3 = 3;
		Object[][][] cube = new Object[dim1][dim2][dim3];
		for (int i = 0; i < dim1; i++) {
			for (int j = 0; j < dim2; j++) {
				for (int k = 0; k < dim3; k++) {
					Double val = new Double(i + j + k);
					cube[i][j][k] = val.toString();

				}
			}
		}
		bdc = new BioDataCubeImpl();
		bdc.setCube(cube);
		bdc.setOrder("bdq");

		return bdc;
	}

}
