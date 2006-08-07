package gov.nih.nci.cagrid.gridgrouper.ui;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

/**
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella</A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster</A>
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings</A>
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @version $Id: GridGrouperBaseTreeNode.java,v 1.1 2006/08/04 03:49:26 langella
 *          Exp $
 */
public class GridGrouperProgressBar extends JProgressBar {

	private Map events;

	private int id;

	public GridGrouperProgressBar() {
		this.events = new HashMap();
		this.id = 0;
		setForeground(GridGrouperLookAndFeel.getPanelLabelColor());
		setString("");
		setStringPainted(true);
	}

	public synchronized int startEvent(String message) {
		id = id + 1;
		Integer bid = new Integer(id);
		events.put(bid, message);
		if (events.size() == 1) {
			updateProgress(true, message);
		}
		return id;
	}

	public synchronized void stopEvent(int id, String message) {
		Integer bid = new Integer(id);
		events.remove(bid);
		if (events.size() == 0) {
			updateProgress(false, message);
		} else {
			Integer min = null;
			Iterator itr = events.keySet().iterator();
			while (itr.hasNext()) {
				Integer num = (Integer) itr.next();
				if ((min == null) || (num.intValue() < min.intValue())) {
					min = num;
				}
			}
			String s = (String) events.get(min);
			updateProgress(true, s);
		}
	}

	private void updateProgress(final boolean working, final String s) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				setString(s);
				setIndeterminate(working);
			}
		});

	}

}
