//Team name: GumBumBot
//Student names: Sean Carney, Cian Dunne, Cameron Carton
//Student Numbers: 19349341,  19312591, 19720959
import java.util.ArrayList;
import java.util.Random;

public class Player {
	private String name;
	private ArrayList<Integer> territories;
	private boolean isNeutral;
	private int playerId;
	private int unplacedArmies;
	public ArrayList<Card> heldCards;
	private String colour;
	
	public Player(String inName, boolean neutral,int player) {
		name = inName;
		isNeutral = neutral;
		territories = new ArrayList<Integer>();
		playerId = player;
		if(getIsNeutral()) {
			setUnplacedArmies(18);
		}
		else {
			setUnplacedArmies(27);
		}
		
		switch(playerId) {
		case 0:
			colour = "RED";
			break;
		case 1:
			colour = "BLUE";
			break;
		case 2:
			colour = "MAGENTA";
			break;
		case 3:
			colour = "GREEN";
			break;
		case 4:
			colour = "ORANGE";
			break;
		default:
			colour = "CYAN";
			break;
		}
		
		heldCards = new ArrayList<Card>();
	}
	
	public void addCards(ArrayList<Card> cards) {
		heldCards.addAll(cards);
	}
	
	public void removeCards(ArrayList<Card> cards) {
		heldCards.removeAll(cards);
	}
	
	public Card removeCard(int insignia) {
		for(Card c : getCards()) {
			if(c.getInsigniaInt() == insignia) {
				heldCards.remove(c);
				return c;
			}
		}
		return null;
		
	}
	
	public void addCard(Card card) {
		heldCards.add(card);
	}
	
	public ArrayList<Card> getCards() {
		return heldCards;
	}

	public void addArmies(int i) {
		unplacedArmies += i;
	}
	
	public void removeArmies(int i) {
		if(i<=unplacedArmies)
			unplacedArmies -= i;
		else
			throw new IllegalArgumentException("unplacedArmies can't be negative");
	}
	
	public void setUnplacedArmies(int i) {
		if(i >= 0)
			unplacedArmies = i;
		else
			throw new IllegalArgumentException("unplacedArmies can't be negative");
		
	}
	
	public int getUnplacedArmies() {
		return unplacedArmies;
	}
	
	public int getPlayerID() {
		return playerId;
	}
	
	public String getName() {
		return name + " (" + colour + ")";
	}
	
	public boolean getIsNeutral() {
		return isNeutral;
	}

	public ArrayList<Integer> getTerritories() {
		return territories;
	}

	//Adds one territory to the players territory list
	public void addTerritory(int territory) {
		territories.add(territory);
	}
	
	public void removeTerritory(int territory) {
		Integer terr = (Integer) territory;
		territories.remove(terr);
	}
	
	/*
	 * Adds the initial territories to the players and neutrals
	 * Parameter: ArrayList which stores the integer values of all remaining territories not assigned
	 * Return: ArrayList parameter with all entries added to the current player removed
	 */
	public ArrayList<Integer> initialTerritoryAdd(ArrayList<Integer> territoriesRemaining) {
		Random rand = new Random();
		int random;
		if(isNeutral) {
			
			for(int i=0; i<constants.INIT_COUNTRIES_NEUTRAL; i++) {
				random = rand.nextInt(territoriesRemaining.size());
				
				//adds the territory to neutral player
				addTerritory(territoriesRemaining.get(random));
				
				//removes the added territory from the list of unallocated territories
				territoriesRemaining.remove(random);
				
			}
		}
		else {
			
			for(int i=0; i<constants.INIT_COUNTRIES_PLAYER; i++) {
				random = rand.nextInt(territoriesRemaining.size());
				
				//adds the territory to neutral player
				addTerritory(territoriesRemaining.get(random));
				
				//removes the added territory from the list of unallocated territories
				territoriesRemaining.remove(random);
			}
		}
		
		//returns the ArrayList of territories with the territories added to the player removed from it
		return territoriesRemaining;
	}
	
	//finds number of continents owned by player
	public ArrayList<Integer> continentsOwned() {
		
		int[] continentCounts = {0, 0, 0, 0, 0, 0};
		
		//increments each of the continent ids of each country the player owns
		for(Integer i: territories) {
			continentCounts[constants.CONTINENT_IDS[i]]++;
		}
			
		//if the players owns as any countries from the continent as the continent has then add the continent to the list of continents they own
		ArrayList<Integer> continentsOwned = new ArrayList<Integer>();
		for(int i=0; i<constants.NUM_CONTINENTS; i++) {
			if(continentCounts[i] == constants.CONTINENT_NUM_COUNTRIES[i]) {
				continentsOwned.add(i);
			}
			else if(continentCounts[i] > constants.CONTINENT_NUM_COUNTRIES[i]) {
				throw new IllegalArgumentException("can't own more countries from a continent than a continent has");
			}
		}
			
		return continentsOwned;
	}
	
}
