package org.tp23.antinstaller.renderer.swing;

import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.text.Document;

import org.tp23.gui.GBCF;


/**
 * @author Paul Hinds
 * @version $Id: AIPasswordField.java,v 1.2 2007-04-25 13:50:33 joshua Exp $
 */
public class AIPasswordField extends JPasswordField {

	public AIPasswordField() {
		super();
	}

	public AIPasswordField(int columns) {
		super(columns);
	}

	public AIPasswordField(String text) {
		super(text);
	}

	public AIPasswordField(String text, int columns) {
		super(text, columns);
	}

	public AIPasswordField(Document doc, String txt, int columns) {
		super(doc, txt, columns);
	}

	private Dimension prefSize = new Dimension(SwingOutputFieldRenderer.FIELD_WIDTH, SwingOutputFieldRenderer.FIELD_HEIGHT);

	public Dimension getMinimumSize() {
		return prefSize;
	}

	public Dimension getPreferredSize() {
		return prefSize;
	}
	public void setOverflow(Dimension prefSize) {
		this.prefSize = prefSize;
	}

	public Dimension getMaximumSize() {
		return prefSize;
	}
	
}
