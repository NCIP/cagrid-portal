package gov.nih.nci.cagrid.graph.domainmodelapplication;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class DomainModelOutlineToolBar extends JPanel
{
	
	public DomainModelOutline parent;

	public JButton expandCollapseButton = new JButton(new ImageIcon(System.getProperty("user.dir") + "\\resource\\expandcollapse.png"));
	public JButton sortButton = new JButton(new ImageIcon(System.getProperty("user.dir") + "\\resource\\sort.png"));
	public JButton refreshButton = new JButton(new ImageIcon(System.getProperty("user.dir") + "\\resource\\refresh.png"));
	public JButton searchButton = new JButton(new ImageIcon(System.getProperty("user.dir") + "\\resource\\search.png"));
	public JTextField searchBox = new JTextField(" enter search filter");
	
	public int inset = 4;
	
	public DomainModelOutlineToolBar(DomainModelOutline parent)
	{
	
		
		super();
		
		
		this.parent  = parent;
		


		this.setLayout(null);
		this.setBackground(new Color(219, 216, 209));
		
		
		expandCollapseButton.setRolloverIcon(new ImageIcon(System.getProperty("user.dir") + "\\resource\\expandcollapse_.png"));
		sortButton.setRolloverIcon(new ImageIcon(System.getProperty("user.dir") + "\\resource\\sort_.png"));
		refreshButton.setRolloverIcon(new ImageIcon(System.getProperty("user.dir") + "\\resource\\refresh_.png"));
		searchButton.setRolloverIcon(new ImageIcon(System.getProperty("user.dir") + "\\resource\\search_.png"));
		
		
		this.expandCollapseButton.setBorder(BorderFactory.createEmptyBorder());
		this.sortButton.setBorder(BorderFactory.createEmptyBorder());
		this.refreshButton.setBorder(BorderFactory.createEmptyBorder());
		this.searchButton.setBorder(BorderFactory.createEmptyBorder());
		this.searchBox.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
		
		this.searchBox.setVisible(false	);

		
		this.expandCollapseButton.setFocusable(false);
		this.sortButton.setFocusable(false);
		this.refreshButton.setFocusable(false);
		this.searchButton.setFocusable(false);
		
		this.add(expandCollapseButton);
		this.add(sortButton);
		this.add(refreshButton);
		this.add(searchButton);
		this.add(searchBox);
		
		this.expandCollapseButton.addMouseListener(new ExpandCollapseButtonMouseListener(parent));
		this.searchButton.addMouseListener(new SearchButtonMouseListener(parent));
		
		expandCollapseButton.setToolTipText("Expand/Collapse Tree");
		sortButton.setToolTipText("Sort Tree");
		refreshButton.setToolTipText("Refresh Tree");
		searchButton.setToolTipText("Specify Search Filter");
		
		this.addComponentListener(new DomainModelOutlineToolbarComponentListener());
	}
	
	
	public void paint(Graphics g)
	{
		super.paint(g);
		g.setColor(Color.lightGray);
		g.drawLine(0, this.getHeight(), this.getWidth(), this.getHeight() );
	}
}

class ExpandCollapseButtonMouseListener extends MouseAdapter
{
	public DomainModelOutline parent;
	
	public ExpandCollapseButtonMouseListener(DomainModelOutline parent)
	{
		this.parent = parent;
	}
	public void mouseClicked(MouseEvent e)
	{
		parent.tree.toggleExpansion();
	}
}


class SearchButtonMouseListener extends MouseAdapter
{
	DomainModelOutline parent ;
	
	public SearchButtonMouseListener(DomainModelOutline p)
	{
		parent = p;
	}
	
	public void mouseClicked(MouseEvent e)
	{
		if(parent.toolBar.searchBox.isVisible())
		{
			parent.toolBar.searchBox.setVisible(false);
		}
		else
		{
			parent.toolBar.searchBox.setVisible(true);
		}
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
		s.searchBox.setBounds(s.inset + 4*(s.getHeight() - 2 * s.inset + s.inset) + 1, s.inset + 1, s.getWidth() - (s.inset + 4*(s.getHeight() - 2 * s.inset + s.inset)) - s.inset  , s.getHeight() - 2* s.inset - 2);
	}
}
