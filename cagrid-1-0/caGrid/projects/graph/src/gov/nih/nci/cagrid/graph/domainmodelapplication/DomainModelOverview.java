package gov.nih.nci.cagrid.graph.domainmodelapplication;

import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class DomainModelOverview extends JPanel
{
	public ImageIcon nci = new ImageIcon(System.getProperty("user.dir") + "\\resource\\nci.png");
	public ImageIcon nih = new ImageIcon(System.getProperty("user.dir") + "\\resource\\nih.png");
	public ImageIcon red = new ImageIcon(System.getProperty("user.dir") + "\\resource\\red.png");
	
	
	public JTextArea text = new JTextArea();
	
	
	public DomainModelOverview(DomainModel m)
	{
		super();
		
		text.setFont(new Font("verdana", Font.BOLD, 12));
		text.append("Project Name: \n\n");
		text.append("Total Packages: \n\n");	
		text.append("Total Classes: \n\n");	
		text.setEditable(false);
		
		this.add(text);
		
		super.setLayout(null);
		
		this.setBackground(Color.white);
		
		this.addComponentListener(new DomainModelOverviewComponentListener());
	}
	
	public void paint(Graphics g)
	{
		super.paint(g);
		g.drawImage(red.getImage(), 0, 0, this.getWidth(), nci.getIconHeight(), this);
		g.drawImage(nci.getImage(), 0, 0, nci.getIconWidth(), nci.getIconHeight(), this);
		g.drawImage(nih.getImage(), this.getWidth()-this.nih.getIconWidth(), 0, nih.getIconWidth(), nih.getIconHeight(), this);
		
	}
}

class DomainModelOverviewComponentListener extends ComponentAdapter
{
	public void componentResized(ComponentEvent e)
	{
		DomainModelOverview s = (DomainModelOverview) e.getSource();
		

		s.text.setBounds(30, 60, s.getWidth() - 100, s.getHeight() -100);
		
		if(s.getWidth() < 650)
		{
			s.setSize(650, s.getHeight());
		}
		
		
		
		s.validate();
		
	}
	
}
