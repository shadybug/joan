package cicada.joan;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

/******************************************

Image JPanel for the ImgWindow. Loads and
displays image from file.

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
public class Images extends JPanel {
	BufferedImage image;
	
	public Images() {
        setOpaque(false);
        try {
            image = ImageIO.read(new File("img/joan.png"));
        } catch (IOException e) {
            System.out.println("Could not read image file. Make sure a file by the name of 'joan.png' exists in the 'img' folder.");
        }
    }
	
	protected void paintComponent(Graphics g) {		
		super.paintComponent(g);
		// center the image
        g.drawImage(image, (getWidth() - image.getWidth())/2, (getHeight() - image.getHeight())/2, image.getWidth(), image.getHeight(), this);
    }
	
	@Override
    public Dimension getPreferredSize() {
        return new Dimension(200, 200);
    }
}