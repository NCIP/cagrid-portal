/**
 * 
 */
package org.cagrid.installer.panel;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class ImageOverviewPanel extends JPanel implements InitializingBean {
	
	private String imageResource;

	public String getImageResource() {
		return imageResource;
	}

	public void setImageResource(String imageResource) {
		this.imageResource = imageResource;
	}

	/**
	 * 
	 */
	public ImageOverviewPanel() {
	}

	public void afterPropertiesSet() throws Exception {
		Assert.notNull(getImageResource(), "The imageResource property is required.");
		
		add(new JLabel(new ImageIcon(getClass().getResource(getImageResource()))), null);
	}

}
