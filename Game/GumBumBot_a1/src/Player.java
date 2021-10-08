//Team name: GumBumBot
//Student names: Sean Carney, Cian Dunne, Cameron Carton
//Student Numbers: 19349341,  19312591, 19720959
import java.util.ArrayList;

public class Player {
	private String name;
	private ArrayList<Integer> territories;
	private boolean isNeutral;
	private int playerId;
	
	public Player(String inName, boolean neutral,int player) {
		name = inName;
		isNeutral = neutral;
		territories = new ArrayList<Integer>();
		playerId = player;
	}

	public String getName() {
		return name;
	}

	public ArrayList<Integer> getTerritories() {
		return territories;
	}

	//Adds one territory to the players territory list
	private void addTerritory(int territory) {
		territories.add(territory);
	}
	
	/*
	 * Adds the initial territories to the players and neutrals
	 * Parameter: ArrayList which stores the integer values of all remaining territories not assigned
	 * Return: ArrayList parameter with all entries added to the current player removed
	 */
	public ArrayList<Integer> initialTerritoryAdd(ArrayList<Integer> territoriesRemaining) {
		
		if(isNeutral) {
			
			for(int i=0; i<constants.INIT_COUNTRIES_NEUTRAL; i++) {
				//adds the territory to neutral player
				addTerritory(territoriesRemaining.get(0));
				
				//adds the corresponding mapNode to neutral player
				Main.countryList.get(territoriesRemaining.get(0)).playerOwnSet(playerId);
				Main.countryList.get(territoriesRemaining.get(0)).setArmySize(1);
				territoriesRemaining.remove(0);
			}
		}
		else {
			
			for(int i=0; i<constants.INIT_COUNTRIES_PLAYER; i++) {
				//adds the territory to player
				addTerritory(territoriesRemaining.get(0));
				
				//adds the corresponding mapNode to player
				Main.countryList.get(territoriesRemaining.get(0)).playerOwnSet(playerId);
				Main.countryList.get(territoriesRemaining.get(0)).setArmySize(1);
				territoriesRemaining.remove(0);
			}
		}
		
		//returns the ArrayList of territories with the territories added to the player removed from it
		return territoriesRemaining;
	}
	
}
