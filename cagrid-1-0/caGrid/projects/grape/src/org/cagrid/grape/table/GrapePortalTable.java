/*-----------------------------------------------------------------------------
 * Copyright (c) 2003-2004, The Ohio State University,
 * Department of Biomedical Informatics, Multiscale Computing Laboratory
 * All rights reserved.
 * 
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided
 * that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * 3  All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement: This product includes
 *    material developed by the Mobius Project (http://www.projectmobius.org/).
 * 
 * 4. Neither the name of the Ohio State University, Department of Biomedical
 *    Informatics, Multiscale Computing Laboratory nor the names of its
 *    contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 * 
 * 5. Products derived from this Software may not be called "Mobius"
 *    nor may "Mobius" appear in their names without prior written
 *    permission of Ohio State University, Department of Biomedical
 *    Informatics, Multiscale Computing Laboratory
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *---------------------------------------------------------------------------*/
package org.cagrid.grape.table;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;


/**
 * @author <A href="mailto:langella@bmi.osu.edu">Stephen Langella </A>
 * @author <A href="mailto:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A href="mailto:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @created Oct 14, 2004
 * @version $Id: ArgumentManagerTable.java,v 1.2 2004/10/15 16:35:16 langella
 *          Exp $
 */
public abstract class GrapePortalTable extends JTable {

	private final static Color DEFAULT_FOREGROUND_1 = Color.BLACK;
	private final static Color DEFAULT_BACKGROUND_1 = Color.WHITE;
	private final static Color DEFAULT_FOREGROUND_2 = Color.BLACK;
	private final static Color DEFAULT_BACKGROUND_2 = Color.WHITE;
	private final static Color DEFAULT_SELECTED_FOREGROUND = Color.BLACK;
	private final static Color DEFAULT_SELECTED_BACKGROUND = Color.WHITE;


	public GrapePortalTable(DefaultTableModel model) {
		this(model, DEFAULT_BACKGROUND_1, DEFAULT_FOREGROUND_1, DEFAULT_BACKGROUND_2, DEFAULT_FOREGROUND_2,
			DEFAULT_SELECTED_BACKGROUND, DEFAULT_SELECTED_FOREGROUND);
	}


	public GrapePortalTable(DefaultTableModel model, Color bg1, Color fg1, Color bg2, Color fg2, Color sbg, Color sfg) {
		super(model);
		setDefaultRenderer(Object.class, new GrapeTableCellRenderer(bg1, fg1, bg2, fg2, sbg, sfg));
		// setDefaultEditor(JComponent.class, new JComponentCellEditor());
		// this.setCellSelectionEnabled(true);
		this.setRowSelectionAllowed(true);
		this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					try {
						doubleClick();
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				} else if (e.getClickCount() == 1) {
					try {
						singleClick();
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		});
		// this.setOpaque(true);
	}


	public Dimension getPreferredScrollableViewportSize() {
		return getPreferredSize();
	}


	public abstract void doubleClick() throws Exception;


	public abstract void singleClick() throws Exception;


	public boolean isCellEditable(int row, int column) {
		return false;
	}


	public synchronized void addRow(Vector v) {
		((DefaultTableModel) this.getModel()).addRow(v);
	}


	public synchronized void removeRow(int i) {
		((DefaultTableModel) this.getModel()).removeRow(i);
	}


	public synchronized void clearTable() {
		DefaultTableModel model = (DefaultTableModel) this.getModel();
		while (model.getRowCount() != 0) {
			model.removeRow(0);
		}

	}

}