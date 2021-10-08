//Team name: GumBumBot
//Student names: Sean Carney, Cian Dunne, Cameron Carton
//Student Numbers: 19349341,  19312591, 19720959
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.*;

public class input extends JPanel implements KeyListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public input() {		
		//create the input box calling on methods below
		createTextArea();
		createButton();
		inPanel();		
		inText.addKeyListener(this);
		inText.getDocument().putProperty("filterNewlines", Boolean.TRUE);
	}
	
	private String TextInput;
	private JTextArea inText;
	private JButton button;
	private boolean detectedInput = false;
	
	//getter method for the text input by user
	public String getInput() {
		return TextInput;
	}
	
	//ONLY FOR TESTING
	public void setInput(String in) {
		TextInput = in;
	}
	
	//method to create the input text box
	private void createTextArea() {

		inText = new JTextArea(1, 20);
		inText = new JTextArea();
		inText.setPreferredSize(new Dimension(600,50));

	}
	
	//method to check if the send input button is pressed and take the input
	class ClickListener implements ActionListener 
	{	
		public void actionPerformed(ActionEvent event) {
				TextInput = inText.getText().trim();
				inText.setText(null);
				detectedInput = true;
		}
	}
    
	public JButton getButton() {
		return button;
	}
	
	public boolean getDetectedInput() {
		return detectedInput;
	}
	
	public void setDetectedInput(boolean isTrue) {
		detectedInput = isTrue;
	}
	
	//Waits for the input of the button before acting continuing the program
	public void waitForInput() {
		while(!getDetectedInput()) {
			try {
			       Thread.sleep(200);
			    } catch(InterruptedException e) {
			    }
		}
	}
	
	//Returns the name of the player entered by the user after the button is pressed
	public String getName() {
		waitForInput();
		
		detectedInput = false;
		return TextInput;
		
	}
	
	public String getCountryInput() {
		waitForInput();

		detectedInput = false;
		if(isValidCommand(TextInput) != -1) {
			return constants.COUNTRY_NAMES[isValidCommand(TextInput)];
		}
		return "Invalid Input, try again";

	}


	//method to create a button
    private void createButton() {

    	button = new JButton("Send");
    	ActionListener click = new ClickListener();
    	button.addActionListener(click);
    }
	
    //initialising the input panel
	private void inPanel() {
		
		JPanel in = new JPanel(new BorderLayout());
		in.add(inText, BorderLayout.CENTER);
		in.add(button, BorderLayout.EAST);
		add(in);
	}

	//checks if command is valid, error handling
	//returns -1 if invalid, or the index of the country if valid
	public int isValidCommand(String str) {
		for(int i = 0;(i < constants.COUNTRY_NAMES.length);i++) {

			String cou = constants.COUNTRY_NAMES[i];

			//removes all whitespace from strings
			cou = cou.replaceAll(" ", "");
			str = str.replaceAll(" ", "");
			
			//cuts strings down to short hand
			cou = cou.substring(0, Math.min(cou.length(), 4));
			str = str.substring(0, Math.min(str.length(), 4));

			//compare input with country
			boolean same = str.equalsIgnoreCase(cou);

			//break out of for loop
			if(same) {
				return i;
			}
		}

		return -1;
	}
	
	//reinforce
	public int[] reinforce(int unplaced) {
		waitForInput();

		detectedInput = false;
		try {
			String[] cou = TextInput.split(" ",2);
			int countryToReinforce = isValidCommand(cou[0]);
			int amountToReinforce = Integer.parseInt(cou[1]);
			
			int[] arr = {countryToReinforce, amountToReinforce};
			
			if(isValidCommand(cou[0]) != -1 && amountToReinforce >= 1) {
				return arr;
				}
		}catch(Exception e) {}
	
		//returns an array of -1 if there is an error with input
		return new int[] {-1, -1};
		
	}
	
	//fortify
	public String fortify() {
		waitForInput();

		detectedInput = false;

		String in = TextInput;
		String skip = "skip";
		if(skip.equalsIgnoreCase(in)) {
			return "skipped";
		}else{
			try {
				String[] cou = TextInput.split(" ",3);
				int countryToTakeFrom = isValidCommand(cou[0]);
				int countryToFortify = isValidCommand(cou[1]);
				int amountToFortify = Integer.parseInt(cou[2]);
				
				if(amountToFortify > Main.countryList.get(countryToTakeFrom).getArmySize()-1 || amountToFortify < 0) {
					throw new Exception("Not enough units");
				}
				
				if(isValidCommand(cou[0]) != -1 && isValidCommand(cou[1]) != -1 && amountToFortify >= 1) {

					Main.countryList.get(countryToTakeFrom).addArmies(-amountToFortify);
					Main.countryList.get(countryToFortify).addArmies(amountToFortify);
					return "Sent " + amountToFortify + " units from " + constants.COUNTRY_NAMES[isValidCommand(cou[0])] + " to " +  constants.COUNTRY_NAMES[isValidCommand(cou[1])];
				}

			}catch(Exception e) {}
			return "Invalid Input, try again";

		}
	}
	
	//exchange insignias
	public int[] exchangeInsignias() {
		//gets input from user
		waitForInput();

		detectedInput = false;

		String in = TextInput;
	
		
		try {
			//converts String input to char array
			char[] charr = in.toCharArray();
			int infantry_amount = 0;
			int cavalry_amount = 0;
			int artillery_amount = 0;
			int wild_amount = 0;
			
			//input must be only 3 chars long
			if(charr.length != 3) {
				throw new Exception("invalid input");
			}
			
			//if one of the correct characters are found increment their counter, otherwise throw exception
			for(int j=0; j < charr.length;j++) {
				if(charr[j] == 'i' || charr[j] == 'I') {
					infantry_amount++;
				}
				else if(charr[j] == 'c' || charr[j] == 'C') {
					cavalry_amount++;
				}
				else if(charr[j] == 'a' || charr[j] == 'A') {
					artillery_amount++;
				}
				else if(charr[j] == 'w' || charr[j] == 'W') {
					wild_amount++;
				}else {
					throw new Exception("invalid input");
				}
				
			}

			//if the input contains 1 regular card and 2 wild cards return array with the insignia value of the regular card and 2 wild cards
			if((infantry_amount==1 || cavalry_amount==1 || artillery_amount==1) && wild_amount==2) {
				if(infantry_amount==1) {
					return new int[] {constants.INFANTRY_NO,  constants.WILD_NO, constants.WILD_NO};
				}
				if(cavalry_amount==1) {
					return new int[] {constants.CAVALRY_NO,  constants.WILD_NO, constants.WILD_NO};
				}
				if(artillery_amount==1) {
					return new int[] {constants.ARTILLERY_NO,  constants.WILD_NO, constants.WILD_NO};
				}
			}
			
			//if the input contains 2 regular cards and 1 wild card return array with 2 insignia values of the regular card and 1 wild card
			if((infantry_amount==2 || cavalry_amount==2 || artillery_amount==2) && wild_amount==1) {
				if(infantry_amount==2) {
					return new int[] {constants.INFANTRY_NO,  constants.INFANTRY_NO, constants.WILD_NO};
				}
				if(cavalry_amount==2) {
					return new int[] {constants.CAVALRY_NO,  constants.CAVALRY_NO, constants.WILD_NO};
				}
				if(artillery_amount==2) {
					return new int[] {constants.ARTILLERY_NO,  constants.ARTILLERY_NO, constants.WILD_NO};
				}
			}
			
			//if the input contains 3 regular card values with same insignia return an array with the value of that insignia 3 times
			if((infantry_amount==3 || cavalry_amount==3 || artillery_amount==3)) {
				if(infantry_amount==3) {
					return new int[] {constants.INFANTRY_NO,  constants.INFANTRY_NO, constants.INFANTRY_NO};
				}
				if(cavalry_amount==3) {
					return new int[] {constants.CAVALRY_NO,  constants.CAVALRY_NO, constants.CAVALRY_NO};
				}
				if(artillery_amount==3) {
					return new int[] {constants.ARTILLERY_NO,  constants.ARTILLERY_NO, constants.ARTILLERY_NO};
				}
			}
			
			//if the input has 3 different insignias return an array with one of each of the values
			if(infantry_amount==1 && cavalry_amount==1 && artillery_amount==1) {
				return new int[] {constants.INFANTRY_NO,  constants.CAVALRY_NO, constants.ARTILLERY_NO};
			}
			if(infantry_amount==1 && cavalry_amount==1 && wild_amount==1) {
				return new int[] {constants.INFANTRY_NO,  constants.CAVALRY_NO, constants.WILD_NO};
			}
			if(infantry_amount==1 && artillery_amount==1 && wild_amount==1) {
				return new int[] {constants.INFANTRY_NO,  constants.ARTILLERY_NO, constants.WILD_NO};
			}
			if(cavalry_amount==1 && artillery_amount==1 && wild_amount==1) {
				return new int[] {constants.CAVALRY_NO,  constants.ARTILLERY_NO, constants.WILD_NO};
			}
			
		}catch(Exception e) {}
		//if an exception is found or if nothing is returned yet, return -1 indicating an error with input
		return new int[] {-1, -1, -1};
	}

	//used to detect the enter key being pressed for input
	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_ENTER) {
			TextInput = inText.getText().trim();
			inText.setText(null);
			detectedInput = true;
		}
	}

	//allows for enter key to send message from text area
	@Override
	public void keyReleased(KeyEvent e) {}

	@Override
	public void keyTyped(KeyEvent e) {}
}
