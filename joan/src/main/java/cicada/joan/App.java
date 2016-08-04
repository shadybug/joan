package cicada.joan;

import java.awt.AWTException;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;

/******************************************

Main class. Initializes all other classes,
manages the program loop, and takes care
of taskbar support.

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

public class App{
	static Data data = new Data();
	static ImgWindow frame;
	static SpeechWindow bubble;
	
	static int xCoord;
	static int yCoord;
	
	static int mouseX;
	static int mouseY;
	
	static long oldTime;
	static long foodTime;
	static long sleepTime;
	
	static Font font;
	
	final static PopupMenu popup = new PopupMenu();
	
    static MenuItem hide = new MenuItem("Hide");
    static MenuItem show = new MenuItem("Show");
    
    static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    static int fullDay = 24*60*60*1000;
    static Timer mornMed = new Timer();
    static Timer nightMed = new Timer();
    
    static boolean settingsDone = false;
	
    public static void main( String[] args ) throws ParseException, IOException{
    	// extract some stuff
    	try{
    		File img = new File("img");
            img.mkdir();
        } 
        catch(SecurityException se){
            System.out.println("I don't have permission to extract my files! Please run with admin privileges or move me somewhere else.");
        }        
    	
    	extract("OpenDyslexic.ttf");
    	extract("LICENSE.TXT");
    	extract("img/joan.png");
    	extract("img/joanico.png");
    	
        //Data.os = System.getProperty("os.name").toLowerCase();
    	// This was important once. It will be important again one day. For now it is a comment.
        
        calcCoord();

    	frame = new ImgWindow(xCoord,yCoord);
    	bubble = new SpeechWindow(xCoord + (int)Data.bubbX,yCoord + (int)Data.bubbY);
        
        systemTray();
        
        // mouse listener
        frame.addMouseListener(new MouseAdapter()
        {
           public void mousePressed(MouseEvent e)
           {
              mouseX=e.getX();
              mouseY=e.getY();
              bubble.toFront();
              Data.saveFile();
           }
        });

        bubble.addMouseListener(new MouseAdapter(){
           public void mousePressed(MouseEvent e){
              mouseX=e.getX() - 50;
              mouseY=e.getY() - 50;
              bubble.toFront();
           }
        });
        
        // dragged
        frame.addMouseMotionListener(new MouseAdapter()
        {
             public void mouseDragged(MouseEvent evt)
             {
        		//sets frame position when mouse dragged
            	xCoord = evt.getXOnScreen()-mouseX;
            	yCoord = evt.getYOnScreen()-mouseY;
        		frame.setLocation(xCoord,yCoord);
           		bubble.setLocation(xCoord + (int)Data.bubbX, yCoord + (int)Data.bubbY);
        		bubble.toFront();
             }
        });
        
        oldTime = System.currentTimeMillis();
        sleepTime = System.currentTimeMillis();
        foodTime = System.currentTimeMillis();
        
        while(true){
        	programLoop();
        }
    }

	private static void programLoop() throws ParseException {
		if (bubble.speech.speech == null){
			bubble.node.changeNode(null);
		}
		
		// check howareyou
		if ((System.currentTimeMillis() - oldTime) > (Data.defaultInt * 60000)){
			bubble.setVisible(true);
			frame.setVisible(true);
			bubble.checkIn(0);
			
			oldTime = System.currentTimeMillis();
		}
		
		// check sleep
		if (Data.sleep && (System.currentTimeMillis() - sleepTime) > 18000000){
			bubble.setVisible(true);
			frame.setVisible(true);
			bubble.checkIn(4);
			
			sleepTime = System.currentTimeMillis();
		}
		
		// check food
		if (Data.food && (System.currentTimeMillis() - foodTime) > 14400000){
			bubble.setVisible(true);
			frame.setVisible(true);
			bubble.checkIn(3);
			
			foodTime = System.currentTimeMillis();
		}
		
		// change show/hide menu items
		boolean isShow = false;
		for (int i = 0; i < popup.getItemCount(); i++){
			if (popup.getItem(i) == show){
				isShow = true;
			}
			else if (popup.getItem(i) == hide){
				isShow = false;
			}
		}
		if (frame.isVisible() && isShow){
			popup.remove(show);
			popup.insert(hide, 0);
		}
		if (!frame.isVisible() && !isShow){
			popup.remove(hide);
			popup.insert(show, 0);
		}
		
		// set med timers
		if(settingsDone){
		    if(Data.mornMed){
			    Date mornDate = dateFormat .parse("2016-07-31 08:00:00");
			    TimerTask checkMorn = (new TimerTask(){
					@Override
					public void run() {
						bubble.checkIn(1);
					}
			    });
			    
			    mornMed.schedule(checkMorn, mornDate, fullDay);
		    }
		    else{
		    	mornMed.cancel();
		    }
		    
		    if(Data.nightMed){
			    Date nightDate = dateFormat .parse("2016-07-31 20:00:00");		    
			    TimerTask checkNight = (new TimerTask(){
					@Override
					public void run() {
						bubble.checkIn(2);
					}
			    });
			    
			    nightMed.schedule(checkNight, nightDate, fullDay);
		    }
		    else{
		    	nightMed.cancel();
		    }
		    
		    settingsDone = false;
		}
	}

	private static void calcCoord() {
		xCoord = (int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth() - Data.x);
		yCoord = (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() - Data.y);
	}
	
	private static void systemTray(){
		if (!SystemTray.isSupported()) {
            System.out.println("System tray is not supported, sorry!");
            return;
        }
		
        BufferedImage icon = null;
		try {
			icon = ImageIO.read(new File("img/joanico.png"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
        int iconWidth = new TrayIcon(icon).getSize().width;
	    
        final TrayIcon trayIcon = new TrayIcon(icon.getScaledInstance(iconWidth, -1, Image.SCALE_SMOOTH), "Joan", popup);
        final SystemTray tray = SystemTray.getSystemTray();
       
        //create/add menu items
        MenuItem settings = new MenuItem("Settings");
        MenuItem github = new MenuItem("Github");
        MenuItem exit = new MenuItem("Exit");
        
        popup.add(hide);
        popup.add(settings);
        popup.add(github);
        popup.addSeparator();
        popup.add(exit);
        
        //actionlisteners for menu items
        hide.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        	    App.frame.setVisible(false);
        	    App.bubble.setVisible(false);
        	}
        });
        show.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        	    App.frame.setVisible(true);
        	    App.bubble.setVisible(true);
        	}
        });
        settings.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	bubble.checkIn(5);
            }
        });
        github.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
                if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
                    try {
                        desktop.browse(new URL("https://github.com/shadybug/joan").toURI());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }

        });
        exit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	System.exit(0);
            }
        });
       
        trayIcon.setPopupMenu(popup);
       
        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.out.println("TrayIcon could not be added.");
        }
	}
	
	private static void extract(String fileName) throws IOException{
        File file = new File(fileName);
        if (!file.exists()){
	        ClassLoader cl = App.class.getClassLoader();
	        FileOutputStream outStream = new FileOutputStream(file);
	        InputStream inStream = cl.getResourceAsStream(fileName);
	        byte[] b = new byte[8*1024];
	        int i;
	        
	        while((i = inStream.read(b)) != -1){
	            outStream.write(b,0,i);
	        }
	        outStream.close();
	        inStream.close();
        }
    }
}
