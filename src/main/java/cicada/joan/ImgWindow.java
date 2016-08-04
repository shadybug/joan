package cicada.joan;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JDialog;

/******************************************

The main window which holds the images.
Made to give the program a friendlier face
so that people are more likely to listen
to it, but otherwise has little actual
function.

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
public class ImgWindow extends JDialog{
	public ImgWindow(int x, int y){
		add(new Images());
		setResizable(false);
		setAlwaysOnTop(true);
        setUndecorated(true);
        setLocation(x, y);
        setPreferredSize(new Dimension(200,200));
        setBackground(new Color(150, 150, 150, 0)); // light grey to compensate for OSes which don't support transparency (lookin' at you, lubuntu)
        pack();
        setVisible(true);
	}
}
