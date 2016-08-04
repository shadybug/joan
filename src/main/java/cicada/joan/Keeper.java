package cicada.joan;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JTextField;

/******************************************

Node Keeper. Default template for all of
Joan's messages. All values are initialized
in the beginning of the Nodes class so that
it has easy access to all of the data it
needs.

===========================================

Copyright 2016 Cicada Scott

	This file is part of Joan.

    Joan is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Joan is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Joan.  If not, see <http://www.gnu.org/licenses/>.

******************************************/

public class Keeper {
	public String id;
	public String s;
	public JButton b1;
	public JButton b2;
	public JTextField t;
	public Nodes n;
	public boolean end; // tells if the node is the end of a node
	// sequence or not, so that Joan can know when to replace notifications.

	// constructor for nodes without input
	public Keeper(String string, String string2, Nodes node, boolean end) {
		id = string;
		s = string2;
		n = node;
		this.end = end;
	}

	// constructor for nodes with text input
	public Keeper(String string, String string2, boolean b, Nodes node, boolean end) {
		id = string;
		s = string2;
		t = new JTextField(20);
		n = node;
		this.end = end;
		keyListen();
	}

	// constructor for nodes with button input
	public Keeper(String string, String string2, String string3, String string4, Nodes node, boolean end) {
		id = string;
		s = string2;
		b1 = new JButton(string3);
		b2 = new JButton(string4);
		n = node;
		this.end = end;
		mouseListen();
	}
	
	public void key(){
		// overridden in the nodes class for text input
	}
	
	public void buttonOne(){
		// overridden in the nodes class for button input
	}
	
	public void buttonTwo(){
		// overridden in the nodes class for button input
	}
	
	public void keyListen(){
		t.addKeyListener(new KeyListener(){
			@Override
			public void keyPressed(KeyEvent e) {
			    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			        key();
			        Data.saveFile();
			    }
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
				
			}
		});
	}
	
	public void mouseListen(){
		b1.addMouseListener(new MouseAdapter(){
	        public void mouseClicked(MouseEvent e){
	        	buttonOne();
		        Data.saveFile();
	       	}
		});
		
		b2.addMouseListener(new MouseAdapter(){
	        public void mouseClicked(MouseEvent e){
	        	buttonTwo();
		        Data.saveFile();
	       	}
		});
	}
}