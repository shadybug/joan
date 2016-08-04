package cicada.joan;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;

import javax.swing.JLabel;

/******************************************

JLabel for Joan's speech bubble. Contains
some HTML for ease of formatting,
particularly with the width. May later
become a JTextField for screen reader
support, but I had some trouble with it
earlier.

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
public class Speech extends JLabel{
	
	public String speech; // original text string
	String h = "<html><div style='margin: 10px; width: "; // HTML option one, long messages
	String h2 = "<html><div style='margin: 10px;'>"; // HTML option two, short messages
	String c = "px;'>"; // HTML close tag
	int w = 200; // width
	String finalSpeech; // string with added HTML
	
	public SpeechWindow bubble;
	
	public Speech(SpeechWindow bubble){
		this.bubble = bubble;
		
        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT, new File("OpenDyslexic.ttf")).deriveFont(12f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("OpenDyslexic.ttf")));
            setFont(font);
        } catch (IOException e) {
            System.out.println("Font file is missing! Please include OpenDyslexic.ttf in the root folder.");
        } catch(FontFormatException e) {
            System.out.println("Could not read the provided font. Please make sure it is a valid *.ttf file.");
        }
	}
	
	public void makeSquare(){
		if (getWidth() > 200){
			w = 200;
			if (getHeight() > 350){
				w = 300;
			}
			finalSpeech = h + w + c + speech;
		}
		else{
			finalSpeech = h2 + speech;
		}
	}
	
	public void sayNew(){
		  setText(speech);
		  makeSquare();
		  setText(finalSpeech);
	}
	
	 public void paintComponent(Graphics g) {
		 ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		 super.paintComponent(g);
	 }
}
