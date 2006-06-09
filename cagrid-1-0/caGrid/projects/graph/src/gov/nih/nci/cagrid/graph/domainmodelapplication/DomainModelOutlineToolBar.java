package gov.nih.nci.cagrid.graph.domainmodelapplication;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

public class DomainModelOutlineToolBar extends JPanel
{

	public JButton expandCollapseButton = new JButton(new ImageIcon(System.getProperty("user.dir") + "\\resource\\expandcollapse.png"));
	public JButton sortButton = new JButton(new ImageIcon(System.getProperty("user.dir") + "\\resource\\sort.png"));
	public JButton refreshButton = new JButton(new ImageIcon(System.getProperty("user.dir") + "\\resource\\refresh.png"));
	public JButton searchButton = new JButton(new ImageIcon(System.getProperty("user.dir") + "\\resource\\search.png"));
	
	public int inset = 4;
	
	public DomainModelOutlineToolBar()
	{
		
		super();
		

		this.setLayout(null);
		this.setBackground(new Color(219, 216, 209));
		
		this.expandCollapseButton.setBorder(BorderFactory.createEmptyBorder());
		this.sortButton.setBorder(BorderFactory.createEmptyBorder());
		this.refreshButton.setBorder(BorderFactory.createEmptyBorder());
		this.searchButton.setBorder(BorderFactory.createEmptyBorder());
		
		this.expandCollapseButton.setFocusable(false);
		this.sortButton.setFocusable(false);
		this.refreshButton.setFocusable(false);
		this.searchButton.setFocusable(false);
		
		this.add(expandCollapseButton);
		this.add(sortButton);
		this.add(refreshButton);
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
		s.refreshButton.setBounds(s.inset + 2*(s.getHeight() - 2 * s.inset + s.inset), s.inset, s.getHeight() - 2* s.inset, s.getHeight() - 2* s.inset);
		s.searchButton.setBounds(s.inset + 3*(s.getHeight() - 2 * s.inset + s.inset), s.inset, s.getHeight() - 2* s.inset, s.getHeight() - 2* s.inset);
	}
}