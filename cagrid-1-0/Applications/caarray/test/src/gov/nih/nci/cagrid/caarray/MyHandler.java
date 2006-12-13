package gov.nih.nci.cagrid.caarray;

import java.io.StringReader;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class MyHandler extends DefaultHandler {

	private StringBuilder theContent = new StringBuilder();

	public void startElement(String namespaceUri, String localName,
			String qualifiedName, Attributes attributes) {

		int len = attributes.getLength();
		for (int i = 0; i < len; i++) {
			System.out.println(attributes.getQName(i) + " = "
					+ attributes.getValue(i));
		}

	}

	public void endElement(String namespaceUri, String localName,
			String qualifiedName) throws SAXException {
		System.out.println("content = " + theContent);
	}

	public void characters(char ch[], int start, int length) {
		theContent.append(ch, start, length);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		
		String xml = "<yadda dadda=\"\n\nsome\n\ttext\">\n\tHowdy\n\t\t\tPardner!</yadda>";
		
		SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
		parser.parse(new InputSource(new StringReader(xml)), new MyHandler());

	}

}
