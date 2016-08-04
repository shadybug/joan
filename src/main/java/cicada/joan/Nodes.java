package cicada.joan;

import java.io.File;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/******************************************

Joan is technically (barely) a tree AI.
This class creates and manages all of her
nodes. The queue is so she can sort out
any checkins when she is already handling
one, and also so that she doesn't ask the
same thing five times in a row.

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

public class Nodes {
	Speech speech;
	SpeechWindow window;
	public Keeper node;
	public Queue<Keeper> q = new LinkedBlockingQueue<Keeper>(); // very convenient that q sounds the same as queue
	
	// most of these have overridden classes because I couldn't think
	// of a cleaner way to make so many of them. Subject to change
	/* ======== SPEECH ======== */
	public Keeper firstIntro = new Keeper("firstIntro", "Hello! I'm Joan, and I'm here to remind you to take care of yourself. Let's get you set up!", this, false);
	public Keeper setName = new Keeper("setName", "What should I call you?",true, this, false){
		@Override
		public void key(){
			Data.name = t.getText();
			changeNode(null);
		}
	};
	public Keeper confirmName = new Keeper("confirmName", "So I should call you " + Data.name + "? I love it!", "Yep!", "That's not it!", this, false){
		@Override
		public void buttonOne(){
			changeNode(null);
		}
		
		@Override
		public void buttonTwo(){
			changeNode(n.setName);
		}
	};
	public Keeper setDefaultInt = new Keeper("setDefaultInt", "How often would you like me to check in on you? In minutes, please!", true, this, false){
		@Override
		public void key(){
			Data.defaultInt = Integer.parseInt(t.getText());
			changeNode(null);
		}
	};
	public Keeper setMornMed = new Keeper("setMornMed", "Do you need to take any medicine in the morning?","Yes","No",this,false){
		@Override
		public void buttonOne(){
			Data.mornMed = true;
			changeNode(null);
		}
		
		@Override
		public void buttonTwo(){
			Data.mornMed = false;
			changeNode(null);
		}
	};
	public Keeper setNightMed = new Keeper("setNightMed", "What about at night?","Yes","No",this,false){
		@Override
		public void buttonOne(){
			Data.nightMed = true;
			changeNode(null);
		}
		
		@Override
		public void buttonTwo(){
			Data.nightMed = false;
			changeNode(null);
		}
	};
	public Keeper setFood = new Keeper("setFood", "Would you like me to remind you to eat? I'll check in every four hours!","Yes","No",this,false){
		@Override
		public void buttonOne(){
			Data.food = true;
			changeNode(null);
		}
		
		@Override
		public void buttonTwo(){
			Data.food = false;
			changeNode(null);
		}
	};
	public Keeper setSleep = new Keeper("setSleep","One more thing! If you'd like, I can remind you to get some sleep. What do you think?","Sure","No thanks",this,false){
		@Override
		public void buttonOne(){
			Data.sleep = true;
			changeNode(null);
		}
		
		@Override
		public void buttonTwo(){
			Data.sleep = false;
			changeNode(null);
		}
	};
	public Keeper finishSetup = new Keeper("finishSetup", "Alright, we're all set up! You can change your settings at any time via the taskbar menu. I'll check in after " + Data.defaultInt + " minutes. Feel free to hide me in the meantime, also via taskbar!", this, true);
	
	public Keeper intro = new Keeper("intro", "Hi, " + Data.name + "!", this, true);

	// how are you?
	public Keeper check = new Keeper("check", "Are you doing okay?", "Yes", "No", this, false){
		@Override
		public void buttonOne(){
			Data.totalHowAreYou++;
			Data.totalIAmGood++;
			changeNode(n.okay);
		}
		
		@Override
		public void buttonTwo(){
			Data.totalHowAreYou++;
			changeNode(n.notOkay);
		}
	};
	public Keeper okay = new Keeper("okay", "Great! I'll check in again later, then.", this, true);
	public Keeper notOkay = new Keeper("notOkay", "I'm sorry to hear that. Do you know why?", "Yes","No",this,false){
		@Override
		public void buttonOne(){
			changeNode(specificWhy);
		}
		
		@Override
		public void buttonTwo(){
			changeNode(nonSpecific);
		}
	};
	public Keeper specificWhy = new Keeper("specificWhy", "Is it something out of your control?", "Yes", "No", this, false){
		@Override
		public void buttonOne(){
			changeNode(outControl);
		}
		
		@Override
		public void buttonTwo(){
			changeNode(inControl);
		}
	};
	public Keeper outControl = new Keeper("outControl", "That's okay! Sometimes we know we can't affect the outcomes, but we still worry anyways. The best we can do is try and take care of ourselves. Try doing something to take your mind off of things! Maybe try a puzzle (my favorite is sudoku) or read a book. Sometimes crafts can help too, even if you're not much of an artist! Try not to focus on the outcome, and instead pay attention to the process.", this, true);
	public Keeper inControl = new Keeper("inControl", "If it's something you can do in small steps, I want you to take ten minutes to work on fixing the problem. Even that may seem daunting, but I know you can make it! Maybe in a little bit you can do another ten minutes, and before you know it you'll be done. If it's something big, work your way up to it. Practice in front of a mirror, write out what you need to do, or do a little Googling. It's important to remember that even if you can't fix things, there's no reason to beat yourself up! Sometimes we overestimate ourselves, we forget, or perhaps something else gets in the way. No matter the reason, mistakes are what make us individuals. I believe in you!", this, true);
	public Keeper nonSpecific = new Keeper("nonSpecific", "That's okay! It happens to the best of us. Take some deep breaths - in through the nose, and out through the mouth. When you're ready, do something nice for yourself! I recommend making some tea or interacting with a pet if you have one. You might also try doing some stretches or writing or typing things down.", this, true);
	
	// TODO add timer counter
	// meds
	public Keeper mornMed = new Keeper("mornMed","Have you taken your medicine this morning?","Yes","No",this,false){
		@Override
		public void buttonOne(){
			Data.totalMornMed++;
			Data.totalMornMedTaken++;
			changeNode(takenMed);
		}
		
		@Override
		public void buttonTwo(){
			Data.totalMornMed++;
			changeNode(notTaken);
		}
	};	
	public Keeper nightMed = new Keeper("nightMed","Have you taken your medicine tonight?","Yes","No",this,false){
		@Override
		public void buttonOne(){
			Data.totalNightMed++;
			Data.totalNightMedTaken++;
			changeNode(takenMed);
		}
		
		@Override
		public void buttonTwo(){
			Data.totalNightMed++;
			changeNode(notTaken);
		}
	};
	public Keeper takenMed = new Keeper("takenMed", "Great! I'm proud of you!",this,true);
	public Keeper notTaken = new Keeper("notMorn","Well, now's your chance! Let me know when you're done.","Done!","Not yet",this,false){
		@Override
		public void buttonOne(){
			changeNode(takenMed);
		}
		
		@Override
		public void buttonTwo(){
			changeNode(notYet);
		}
	};
	public Keeper notYet = new Keeper("notYet","I'll check in again soon, then.",this,true);
	public Keeper tooLong = new Keeper("tooLong","It's been a little while! Are you able to take your medicine?","Yes","No",this,false){
		@Override
		public void buttonOne(){
			changeNode(notAble);
		}
		
		@Override
		public void buttonTwo(){
			changeNode(null);
		}
	};
	public Keeper notAble = new Keeper("notAble","I see. Try to remember as soon as you can! If you'd like, I can keep reminding you.","Yes","No",this,false){
		@Override
		public void buttonOne(){
			changeNode(notYet);
		}
		
		@Override
		public void buttonTwo(){
			changeNode(noMeds);
		}
	};
	public Keeper able = new Keeper("able", "Please take it now, then! It's important that you take care of yourself.","Okay, done","Not now",this,false){
		@Override
		public void buttonOne(){
			changeNode(takenMed);
		}
		
		@Override
		public void buttonTwo(){
			changeNode(noMeds);
		}
	};
	public Keeper noMeds = new Keeper("noMeds", "If you're sure, then I'll trust you. Just be careful!",this,true);
	
	// food
	public Keeper food = new Keeper("food","Have you eaten in the past four hours?","Yes","No",this,false){
		@Override
		public void buttonOne(){
			Data.totalFood++;
			Data.totalFoodEaten++;
			changeNode(hasEaten);
		}
		
		@Override
		public void buttonTwo(){
			Data.totalFood++;
			changeNode(notEaten);
		}
	};
	public Keeper hasEaten = new Keeper("hasEaten","Keep up the good work!",this,true);
	public Keeper notEaten = new Keeper("notEaten","Now's a good time, then! Your body needs energy, even if it's just a snack.",this,true);
	
	// sleep
	public Keeper sleep = new Keeper("sleep","Have you slept recently? It's probably not a good idea to stay up for more than 16 hours straight!","Yes","No",this,false){
		@Override
		public void buttonOne(){
			Data.totalSleep++;
			Data.totalDidSleep++;
			changeNode(hasSleep);
		}
		
		@Override
		public void buttonTwo(){
			Data.totalSleep++;
			changeNode(noSleep);
		}
	};
	public Keeper hasSleep = new Keeper("hasSleep","Glad to hear it! Keep it up, " + Data.name + "!", this,true);
	public Keeper noSleep = new Keeper("noSleep","Go to bed, you need your rest!","Okay","I can't",this,false){
		@Override
		public void buttonOne(){
			changeNode(sleepWell);
		}
		
		@Override
		public void buttonTwo(){
			changeNode(cantSleep);
		}
	};
	public Keeper sleepWell = new Keeper("sleepWell","Sleep well, " + Data.name + "!",this,true);
	public Keeper cantSleep = new Keeper("cantSleep","Sorry to hear! Insomnia, or are you too busy?","Insomnia","Busy",this,false){
		@Override
		public void buttonOne(){
			changeNode(insomnia);
		}
		
		@Override
		public void buttonTwo(){
			changeNode(busy);
		}
	};
	public Keeper insomnia = new Keeper("insomnia","Ouch. Insomnia is tough to deal with, but you might find it a little easier to sleep if you do some stretches or meditation. Try to get rid of stimulus, too! Yes, that means leaving the computer alone for a bit. I'll be here when you get back!",this,true);
	public Keeper busy = new Keeper("busy","Try a power nap if you can, but make sure to set an alarm! If you're not somewhere you can sleep, hang in there. You can make it!",this,true);
	
	public Nodes(SpeechWindow window, Speech speech) {
		this.speech = speech;
		this.window = window;
		node = intro;
	}
	
	// switch statement to handle initial settings intake
	// or whatever the proper terminology for that would be
	// intake sounds oddly professional
	public void changeNode(Keeper k){
		if (k == null){
			switch (node.id){
			case "firstIntro": node = setName;
				break;
			case "setName": node = confirmName;
				confirmName.s="So I should call you " + Data.name + "? I love it!";
				break;
			case "confirmName": node = setDefaultInt;
				break;
			case "setDefaultInt": node = setMornMed;
				break;
			case "setMornMed": node = setNightMed;
				break;
			case "setNightMed": node = setFood;
				break;
			case "setFood": node = setSleep;
				break;
			case "setSleep": node = finishSetup;
				finishSetup.s = "Alright, we're all set up! You can change your settings at any time via the taskbar menu. I'll check in after " + Data.defaultInt + " minutes. Feel free to hide me in the meantime, also via taskbar!";
				App.settingsDone = true;
				break;
			case "intro": 
				if (!(new File("settings.json").isFile())){
					node = firstIntro;
					App.settingsDone = true;
					break;
				}
				break;
			default: if (q.peek() != null){
					node = q.poll();
					break;
				}
				break;
			}
		}
		else {
			node = k;
		}
		if (node != null){
			setNode();
		}
	}
	
	public void setNode(){
		window.buttons.removeAll();
		window.fade();
		speech.speech = node.s;
		if (node.t != null){
			window.buttons.add(node.t);
		}
		else if (node.b1 != null){
			window.buttons.add(node.b1);
			window.buttons.add(node.b2);
		}
	}
	
	// add a node to the queue
	public void addNode(Keeper k){
		if (!q.contains(k)){
			q.add(k);
		}
	}
}
