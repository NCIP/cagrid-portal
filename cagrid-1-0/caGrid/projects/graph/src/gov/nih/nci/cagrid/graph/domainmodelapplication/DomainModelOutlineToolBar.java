package gov.nih.nci.cagrid.graph.domainmodelapplication;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

public class DomainModelOutlineToolBar extends JPanel
{
	public JButton expandCollapseButton = new JButton();
	public JButton sortButton = new JButton();
	public JButton searchButton = new JButton();
	
	public int inset = 3;
	
	public DomainModelOutlineToolBar()
	{
		super();
		
		this.setLayout(null);
		this.setBackground(new Color(219, 216, 209));
		
		this.expandCollapseButton.setBorder(BorderFactory.createLineBorder(Color.lightGray, 1));
		this.sortButton.setBorder(BorderFactory.createLineBorder(Color.lightGray, 1));
		this.searchButton.setBorder(BorderFactory.createLineBorder(Color.lightGray, 1));
		
		this.expandCollapseButton.setFocusable(false);
		this.sortButton.setFocusable(false);
		this.searchButton.setFocusable(false);
		
		this.add(expandCollapseButton);
		this.add(sortButton);
		this.add(searchButton);
		
		this.addComponentListener(new DomainModelOutlineToolbarComponentListener());
	}
	
	public void paint(Graphics g)
	{
		super.paint(g);
		g.setColor(Color.lightGray);
		g.drawLine(0, this.getHeight(), this.getWidth(), this.getHeight() );
	}
}

class DomainModelOutlineToolbarComponentListener extends ComponentAdapter
{
	public void componentResized(ComponentEvent e)
	{
		DomainModelOutlineToolBar s = (DomainModelOutlineToolBar) e.getSource();
		
		s.expandCollapseButton.setBounds( s.inset + 0*(s.getHeight() - 2 * s.inset + s.inset),s.inset, s.getHeight() - 2* s.inset, s.getHeight() - 2* s.inset);
		s.sortButton.setBounds(s.inset + 1*(s.getHeight() - 2 * s.inset + s.inset),s.inset, s.getHeight() - 2* s.inset, s.getHeight() - 2* s.inset);
		s.searchButton.setBounds(s.inset + 2*(s.getHeight() - 2 * s.inset + s.inset), s.inset, s.getHeight() - 2* s.inset, s.getHeight() - 2* s.inset);
	}
}