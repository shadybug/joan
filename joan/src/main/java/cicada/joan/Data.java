package cicada.joan;

import java.awt.Toolkit;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/******************************************

Reads and saves data to/from the JSON file,
also stores it so that other classes may
use it. This class contains a lot of
currently useless variables which will
have a purpose in future updates.

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

public class Data {
	/* ======== VARIABLES ======== */
	// name of user
	public static String name = "friend";
	
	// os
	public static String os;
	
	// how often joan checks in
	public static long defaultInt = 30;
	
	// what does joan check?
	public static boolean mornMed = false;
	public static boolean nightMed = false;
	public static boolean food = false;
	public static boolean sleep = false;
	
	// joan position
	public static long x = 250;
	public static long y = 250;
	
	// speech bubble position
	public static long bubbX = -230;
	public static long bubbY;
	
	// how was your day?
	public static long totalHowAreYou;
	public static long totalIAmGood;
	
	// have you taken your morning meds?
	public static long totalMornMed;
	public static long totalMornMedTaken;
	
	// have you taken your night meds?
	public static long totalNightMed;
	public static long totalNightMedTaken;
	
	// have you eaten?
	public static long totalFood;
	public static long totalFoodEaten;
	
	// have you slept?
	public static long totalSleep;
	public static long totalDidSleep;
	

	
	/* ======== SPEECH ======== */
	// stuff will go here later
	
	
	// load JSON file
	 	public Data(){
			JSONParser parser = new JSONParser();
			
			try {
				JSONObject obj = (JSONObject) parser.parse(new FileReader("settings.json"));
				
				name = (String) obj.get("name");
				os = (String) obj.get("os");
				defaultInt = (long) obj.get("defaultInt");
				
				mornMed = (boolean) obj.get("mornMed");
				nightMed = (boolean) obj.get("nightMed");
				food = (boolean) obj.get("food");
				sleep = (boolean) obj.get("sleep");
				
				x = (long) obj.get("x");
				y = (long) obj.get("y");
				
				bubbX = (long) obj.get("bubbX");
				bubbY = (long) obj.get("bubbY");
				
				totalHowAreYou = (long) obj.get("totalHowAreYou");
				totalIAmGood = (long) obj.get("totalIAmGood");
				
				totalMornMed = (long) obj.get("totalMornMed");
				totalMornMedTaken = (long) obj.get("totalMornMedTaken");
				
				totalNightMed = (long) obj.get("totalNightMed");
				totalNightMedTaken = (long) obj.get("totalNightMedTaken");
				
				totalFood = (long) obj.get("totalFood");
				totalFoodEaten = (long) obj.get("totalFoodEaten");
				
				totalSleep = (long) obj.get("totalSleep");
				totalDidSleep = (long) obj.get("totalDidSleep");
			} catch (FileNotFoundException e) {
				System.out.println("Could not find settings file! Creating one.");
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				System.out.println("Could not read settings.json. Please check to make sure it is a valid JSON file!");
			}
		}
	
	// save JSON file
	@SuppressWarnings("unchecked")
	public static void saveFile(){
		x = (long) (Toolkit.getDefaultToolkit().getScreenSize().getWidth() - App.xCoord);
		y = (long) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() - App.yCoord);
		
		JSONObject obj = new JSONObject();
		obj.put("name", name);
		obj.put("os", os);
		obj.put("defaultInt", defaultInt);
		
		obj.put("mornMed", mornMed);
		obj.put("nightMed", nightMed);
		obj.put("food", food);
		obj.put("sleep", sleep);
		
		obj.put("x", x);
		obj.put("y", y);
		
		obj.put("bubbX", bubbX);
		obj.put("bubbY", bubbY);
		
		obj.put("totalHowAreYou", totalHowAreYou);
		obj.put("totalIAmGood", totalIAmGood);
		
		obj.put("totalMornMed", totalMornMed);
		obj.put("totalMornMedTaken", totalMornMedTaken);
		
		obj.put("totalNightMed", totalNightMed);
		obj.put("totalNightMedTaken", totalNightMedTaken);
		
		obj.put("totalFood", totalFood);
		obj.put("totalFoodEaten", totalFoodEaten);
		
		obj.put("totalSleep", totalSleep);
		obj.put("totalDidSleep", totalDidSleep);
		
		try {

			FileWriter file = new FileWriter("settings.json");
			file.write(obj.toJSONString());
			file.flush();
			file.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
