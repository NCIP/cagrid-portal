package gov.nih.nci.cagrid.introduce.common;

import java.io.File;

import javax.swing.filechooser.FileFilter;


public interface FileFilters {

	public static final FileFilter XSD_FILTER = new XSDFileFilter();
	public static final FileFilter XML_FILTER = new XMLFileFilter();


	public class XSDFileFilter extends javax.swing.filechooser.FileFilter {
		public boolean accept(File file) {
			String filename = file.getName();
			return filename.endsWith(".xsd");
		}


		public String getDescription() {
			return "XML Schema Files (*.xsd)";
		}
	}


	public class XMLFileFilter extends javax.swing.filechooser.FileFilter {
		public boolean accept(File file) {
			String filename = file.getName();
			return filename.endsWith(".xml");
		}


		public String getDescription() {
			return "XML Files (*.xml)";
		}
	}

}
