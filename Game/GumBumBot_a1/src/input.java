//Team name: GumBumBot
//Student names: Sean Carney, Cian Dunne, Cameron Carton
//Student Numbers: 19349341,  19312591, 19720959
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class input extends JPanel {
	
	
	private static final long serialVersionUID = 1L;

	public input() {		
		//create the input box calling on methods below
		createTextArea();
		createButton();
		inPanel();		
	}
	
	private String TextInput;
	private JTextArea inText;
	private JButton button;
	private boolean detectedInput = false;
	
	//getter method for the text input by user
	public String getInput() {
		return TextInput;
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
				TextInput = inText.getText();
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
}

