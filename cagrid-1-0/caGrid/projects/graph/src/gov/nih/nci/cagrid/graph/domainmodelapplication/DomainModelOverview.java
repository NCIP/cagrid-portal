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
	public DomainModel model;
	
	public ImageIcon nci = new ImageIcon(System.getProperty("user.dir") + "\\resource\\nci.png");
	public ImageIcon nih = new ImageIcon(System.getProperty("user.dir") + "\\resource\\nih.png");
	public ImageIcon red = new ImageIcon(System.getProperty("user.dir") + "\\resource\\red.png");
	
	public Font bold = new Font("verdana", Font.BOLD, 12);
	public Font plain = new Font("verdana", Font.PLAIN, 13);
	
	public JTextArea text = new JTextArea();
	
	public DomainModelOverview(DomainModel m)
	{
		super();
		
		this.model = m;
		
		this.add(text);
		
		super.setLayout(null);
		
		this.setBackground(Color.white);
		
		this.text.setEditable(false);
		this.text.setFont(plain);
		
		this.addComponentListener(new DomainModelOverviewComponentListener());
		
		//if(m != null)
		{
			this.text.append("Project Name: " + "\n");
			this.text.append("Total Packages: " + "\n");
			this.text.append("Total Classes: " + "\n");
			this.text.append("______________________________________________________________\n\n");
			this.text.append("Project Description: " + "\n");
			
		}
	}
	
	public void paint(Graphics g)
	{
		super.paint(g);
		g.drawImage(red.getImage(), 0, 0, this.getWidth(), nci.getIconHeight(), this);
		//g.drawImage(nci.getImage(), 0, 0, nci.getIconWidth(), nci.getIconHeight(), this);
		g.drawImage(nih.getImage(), this.getWidth()-this.nih.getIconWidth(), 0, nih.getIconWidth(), nih.getIconHeight(), this);
		
		
	
	}
	
	
}

class DomainModelOverviewComponentListener extends ComponentAdapter
{
	public void componentResized(ComponentEvent e)
	{
		DomainModelOverview s = (DomainModelOverview) e.getSource();
		


		s.text.setBounds(35, 60, s.getWidth() - 80, s.getHeight() - 80);
		
		
		s.validate();
		
	}
	
}
