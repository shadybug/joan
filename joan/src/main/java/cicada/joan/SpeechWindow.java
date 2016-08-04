package cicada.joan;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;

/******************************************

Joan's speech bubble. This class is a mess
because I am an amateur coder who was too
excited to release the alpha to spend too
long cleaning up code. Does some cool text
fading and also manages checkins. Plus it
has cool follow-the-leader action with the
main window.

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

@SuppressWarnings("serial")
public class SpeechWindow extends JDialog{
	GroupLayout layout;
	Speech speech;
	Nodes node;
	JComponent panel = new JPanel();
	JComponent buttons = new JPanel();
	JButton button1;
	JButton button2;
	JTextField text;
	
	int value = 254;
	int ms = 10;
	ActionListener fade = new ActionListener() {
	    public void actionPerformed(ActionEvent evt) {
	    	if (out){
	    		value += 10;
		    	if (value > 255){
		    		value = 255;
		    		sayNew();
		    		out = false;
		    	}
	    	}
	    	else{
		    	value -= 10;
	    		if (value < 0){
		    		value = 0;
		    		fading.stop();
		    		out = true;
		    	}
	    	}
	    	speech.setForeground(new Color(value,value,value));
	    }
	};
	Timer fading = new Timer(ms, fade);
	boolean out = true;
	
	public SpeechWindow(int x, int y){
		setAlwaysOnTop(true);
		setLocation(x,y);
		setUndecorated(true);
		setMinimumSize(new Dimension(25, 25));
		speech = new Speech(this);
		node = new Nodes(this, speech);
		
		speech.setFocusable(true);
		
		add(panel);
		
		layout = new GroupLayout(panel);
		panel.setLayout(layout);
		GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();
		hGroup.addGroup(layout.createParallelGroup().
	            addComponent(speech).addComponent(buttons));
		layout.setHorizontalGroup(hGroup);
		GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
		vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE).
	            addComponent(speech));
		vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE).
	            addComponent(buttons));
		layout.setVerticalGroup(vGroup);
		
		panel.setBackground(Color.white);
		buttons.setOpaque(false);
		
		addListeners();
		
		packWindow();
		
		setVisible(true);
	}
	
	public void packWindow(){
		pack();
		setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));
	}
	
	public void checkIn(int i){
		Keeper k;
		switch(i){
			case 0: k = node.check;
				break;
			case 1: k = node.mornMed;
				break;
			case 2: k = node.nightMed;
				break;
			case 3: k = node.food;
				break;
			case 4: k = node.sleep;
				break;
			case 5: k = node.firstIntro;
				break;
			default: k = node.intro;
		}
		node.addNode(k);
		setVisible(true);
		App.frame.setVisible(true);
		if(node.node.end){
			node.node = node.q.poll();
			node.setNode();
		}
	}
	
	public void addListeners(){
		addMouseMotionListener(new MouseAdapter()
        {
            public void mouseDragged(MouseEvent evt){
	       		//sets frame position when mouse dragged
            	//but why 45 and 50? we just don't know.
	       		setLocation((evt.getXOnScreen() - App.mouseX) - (45), (evt.getYOnScreen() - App.mouseY) - (50));
	        	
	        	if (Data.bubbY > 250 + getHeight()/2){
	        		App.yCoord += Data.bubbY - (250 + getHeight()/2);
	        		App.frame.setLocation(App.xCoord, App.yCoord);
	        		Data.bubbY = 250 + getHeight()/2;
	        	}
	        	
	        	if (Data.bubbY < -100 - getHeight()/2){
	        		App.yCoord += Data.bubbY - (-100 - getHeight()/2);
	        		App.frame.setLocation(App.xCoord, App.yCoord);
	        		Data.bubbY = -100 - getHeight()/2;
	        	}
	        	
	        	if (Data.bubbX >= 300 - getWidth()/2){
	        		App.xCoord += Data.bubbX - (300 - getWidth()/2);
	        		App.frame.setLocation(App.xCoord, App.yCoord);
	        		Data.bubbX = 300 - getWidth()/2;
	        	}
	        	
	        	if (Data.bubbX <= -100 - getWidth()/2){
	        		App.xCoord += Data.bubbX - (-100 - getWidth()/2);
	        		App.frame.setLocation(App.xCoord, App.yCoord);
	        		Data.bubbX = -100 - getWidth()/2;
	        	}
	        	
	        	Data.bubbX = getX() - App.frame.getX();
	        	Data.bubbY = getY() - App.frame.getY();
            }
       });
       
       addMouseListener(new MouseAdapter(){
        	public void mouseClicked(MouseEvent e){
        		if (node.node.t == null && node.node.b1 == null){
            		node.changeNode(null);
        		}
        	}
       });
	}
	
	public void fade(){
		value = 254;
		if (speech.speech != node.node.s){
			fading.start();
		}
	}
	
	public void sayNew(){
		speech.sayNew();
		packWindow();
	}
}
