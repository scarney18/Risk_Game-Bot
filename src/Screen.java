//Team name: GumBumBot
//Student names: Sean Carney, Cian Dunne, Cameron Carton
//Student Numbers: 19349341,  19312591, 19720959
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Screen extends JPanel{
	
	private static final long serialVersionUID = 1L;
	//map image
	private BufferedImage image;
	private BufferedImage dice1;
	private BufferedImage dice2;
	private BufferedImage dice3;
	private BufferedImage dice4;
	private BufferedImage dice5;
	private BufferedImage dice6;
	private BufferedImage diceImage;

	float diceNum = 0;
	public Screen() {
		try {
			//read in image
			image = ImageIO.read(this.getClass().getResourceAsStream("/riskmap2.png"));
			dice1 = ImageIO.read(this.getClass().getResourceAsStream("/dice1.png"));
			dice2 = ImageIO.read(this.getClass().getResourceAsStream("/dice2.png"));
			dice3 = ImageIO.read(this.getClass().getResourceAsStream("/dice3.png"));
			dice4 = ImageIO.read(this.getClass().getResourceAsStream("/dice4.png"));
			dice5 = ImageIO.read(this.getClass().getResourceAsStream("/dice5.png"));
			dice6 = ImageIO.read(this.getClass().getResourceAsStream("/dice6.png"));
		}catch(IOException e){
			//if image not found
			e.printStackTrace();
			System.out.println("nope, image not found");
		}
		repaint();
	}
	
	public void paint(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		int width = image.getWidth();
		int height = image.getHeight();
		g2.drawImage(image, -20, -10, width, height, null);
		
		//this loop draws the lines
		for(int i = 0;(i < constants.NUM_COUNTRIES); i++) {
			MapNode country = Main.countryList.get(i);
			int player = country.playerOwnGet();
			int x = country.countryCoordGet(0);
			int y = country.countryCoordGet(1);
			//draw lines between nodes
			for(int j = 0;(j < country.adjCountriesGet().length);j++) {
				Color col2 = Color.BLACK;
				
				//get adjacent countries
				int[] adjCountryList = country.adjCountriesGet();
				int adj = adjCountryList[j];
				int coordx = Main.countryList.get(adj).countryCoordGet(0);
				int coordy = Main.countryList.get(adj).countryCoordGet(1);
				
				if(Main.countryList.get(adj).playerOwnGet() == country.playerOwnGet()) {
					player = country.playerOwnGet();
					//change color depending on which player owns adjacent country
					if(player == 0)col2 = Color.RED;
					if(player == 1)col2 = Color.BLUE;
					if(player == 2)col2 = Color.MAGENTA;
					if(player == 3)col2 = Color.GREEN;
					if(player == 4)col2 = Color.ORANGE;
					if(player == 5)col2 = Color.CYAN;
				}
				//draw lines connecting each country node
					g2.setColor(col2);
					// if Alaska or Kamchatka, offset x coord so the lines extend offscreen
					//intially i just flipped it by multiplying it by -1, but that didnt work so I added or subtracted from its coord
					int num = 0;
					if(country.getCountry() == 8 && Main.countryList.get(adj).getCountry() == 22){
						num = -10000;
					}
					if(country.getCountry() == 22 && Main.countryList.get(adj).getCountry() == 8){
						num = 898;
					}
					g2.drawLine(x+15, y+15, (coordx+15)+num, (coordy+15));		//+15 is half of the circle width
				
			}
			
		}
		
		//separated the for loop into 2 for loops so that the lines would not draw over the circles
		for(int i = 0;(i < constants.NUM_COUNTRIES); i++) {
			MapNode country = Main.countryList.get(i);
			int x = country.countryCoordGet(0);
			int y = country.countryCoordGet(1);
			
			//draw circles
			int player = country.playerOwnGet();
			Color col = Color.DARK_GRAY;
			//change color depending on which player owns country
			if(player == 0)col = Color.RED;
			if(player == 1)col = Color.BLUE;
			if(player == 2)col = Color.MAGENTA;
			if(player == 3)col = Color.GREEN;
			if(player == 4)col = Color.ORANGE;
			if(player == 5)col = Color.CYAN;
			Ellipse2D.Double circle = new Ellipse2D.Double(x,y,30,30);
			g2.setColor(col);
			g2.fill(circle);
			
			Font stringFont = new Font("SansSerif",Font.PLAIN,14);
			g2.setFont(stringFont);
			
			//labelling territories with names
			g2.drawString(constants.COUNTRY_NAMES[i],country.countryCoordGet(0), country.countryCoordGet(1));
			
			//setting army sizes
			g2.setColor(Color.WHITE);
			g2.drawString(String.valueOf(country.getArmySize()),country.countryCoordGet(0)+11,country.countryCoordGet(1)+20);
			
		}
		if(Main.getIsDiceRolling()) {
		diceNum += 0.2;
		if(diceNum >= 0 && diceNum < 20) {diceImage = dice1;}
		if(diceNum >= 20 && diceNum < 40) {diceImage = dice2;}
		if(diceNum >= 40 && diceNum < 60) {diceImage = dice3;}
		if(diceNum >= 60 && diceNum < 80) {diceImage = dice4;}
		if(diceNum >= 80 && diceNum < 100) {diceImage = dice5;}
		if(diceNum >= 100 && diceNum < 120) {diceImage = dice6;}
		if(diceNum >= 120) {diceImage = dice1;diceNum = 0;}
		int width2 = diceImage.getWidth();
		int height2 = diceImage.getHeight();
		g2.drawImage(diceImage, 30, 500, width2, height2, null);
		}
		repaint();
	}
	

}
