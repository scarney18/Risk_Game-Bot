//Team name: GumBumBot
//Student names: Sean Carney, Cian Dunne, Cameron Carton
//Student Numbers: 19349341,  19312591, 19720959
import javax.swing.*;
import java.awt.*;

//class to construct the output text box
public class output extends JPanel {

	
	private static final long serialVersionUID = 1L;

	public output() {
		createTextArea();
		outText.setEditable(false);
		outText.setLineWrap(true);
		outText.setPreferredSize(new Dimension(250,900));
		
		createScrollPane();
		
		outPanel();
	}
	
	private JTextArea outText;
	private JScrollPane scrollPane;
	
	//initialising the text box with the first game instruction
	private void createTextArea() {
		outText = new JTextArea("");
	}
	
	//creates a ScrollPane and adds the TextArea to it
	private void createScrollPane() {
		scrollPane = new JScrollPane(outText);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		setLayout(new BorderLayout());
		
	}
	
	//creating the output panel and adding the scroll pane to it
	public void outPanel() {
		JPanel out = new JPanel(new BorderLayout());
		out.add(scrollPane);
		add(out);
	}
	
	//Prints string to the output text area
	public void outputString(String out) {
		outText.append(out + "\n");
		
	}
	
	//Outputs the key for the colours of each of the continents
	public void displayContinentInfo(){
		StringBuilder string = new StringBuilder();
		string.append("KEY:\n");
		
		for(int i=0; i<constants.NUM_CONTINENTS; i++) {
			string.append(constants.CONTINENT_NAMES[i]);
			string.append(": " + constants.CONTINENT_COLOURS[i] + "\n");
		}
				
		outputString(string.toString());
	}
}

