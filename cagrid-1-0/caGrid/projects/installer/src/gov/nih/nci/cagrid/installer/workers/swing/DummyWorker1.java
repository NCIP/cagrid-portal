package gov.nih.nci.cagrid.installer.workers.swing;

import gov.nih.nci.cagrid.installer.workers.SwingWorker;

import javax.swing.JTextArea;

public class DummyWorker1 extends SwingWorker {

	private JTextArea jta;

	private int i;

	private String str;

	public DummyWorker1(JTextArea jta, int i, String str) {
		this.jta = jta;
		this.i = i;
		this.str = str;
	}

	public Object construct() {
		// TODO Auto-generated method stub
		try {
			Thread.sleep(i * 1000);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public void finished() {
		jta.append(str);
	}

}
