//Team name: GumBumBot
//Student names: Sean Carney, Cian Dunne, Cameron Carton
//Student Numbers: 19349341,  19312591, 19720959

import java.util.Arrays;

public class MapNode {
	
	int country;
	int[] adjCountries;
	int[] countryCoord;
	String continentName;
	int continentValue;
	int armySize = 0;
	
	//Player1: playerOwn = 0
	//Player2: playerOwn = 1
	//Neutral: playerOwn >= 2
	int playerOwn = -1;
	
	MapNode(int co){
		country = co;
		adjCountries = constants.ADJACENT[co];
		countryCoord = constants.COUNTRY_COORD[co];
		continentName = constants.CONTINENT_NAMES[constants.CONTINENT_IDS[co]];
		continentValue = constants.CONTINENT_VALUES[constants.CONTINENT_IDS[co]];
	}
	
	public int getCountry() {
		return country;
	}
	
	public void setArmySize(int i) {
		armySize = i;
	}
	
	public int getArmySize() {
		return armySize;
	}
	
	public void addArmies(int num) {
		armySize += num;
		if(armySize < 1)
			throw new IllegalArgumentException("Nodes must always have at least one unit");
	}
	
	public void playerOwnSet(int num) {
		playerOwn = num;
	}
	
	public int countryCoordGet(int num) {
		if(num == 0) {
			//x coordinate
			return countryCoord[0];
		}else {
			//y coordinate
			return countryCoord[1];
		}
	}
	
	public int[] adjCountriesGet() {
		return adjCountries;
	}
	
	public boolean contains(final int[] arr, final int key) {
	    return Arrays.stream(arr).anyMatch(i -> i == key);
	}
	
	public boolean isAdj(int country) {

		if(contains(this.adjCountriesGet(),country)) {
			return true;
		}
		return false;
	}
	
	public int playerOwnGet() {
		return playerOwn;
	}
	
	public String toString(){
		//testing purposes
		String adj = "";
		String coord = "";
		for(int i = 0;(i < adjCountries.length); i++) {
			adj += constants.COUNTRY_NAMES[adjCountries[i]]+"; ";
		}
		for(int i = 0;(i < countryCoord.length); i++) {
			coord += countryCoord[i]+" ";
		}
		return constants.COUNTRY_NAMES[country] +":    " + adj + "    " + coord + ";    " + continentName + ";   "+ continentValue;
	}
}
