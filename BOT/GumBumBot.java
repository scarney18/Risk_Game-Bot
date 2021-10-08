import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

//Team name: GumBumBot
//Student names: Sean Carney, Cian Dunne, Cameron Carton
//Student Numbers: 19349341,  19312591, 19720959

public class GumBumBot implements Bot {
	
	private BoardAPI board;
	private PlayerAPI player;
	private int enemy;
	
	GumBumBot (BoardAPI inBoard, PlayerAPI inPlayer) {
		board = inBoard;	
		player = inPlayer;
		if(player.getId() == 0)
			enemy = 1;
		else
			enemy = 0;
		return;
	}
	
	//Matrix of probabilities of attacker winning a battle for up to 10 troops on each side. Found from "Markov Chains for the RISK Board Game Revisited" - Jason A. Osborne
	private double[][] probabilityMatrix = {{0.417, 0.106, 0.027, 0.007, 0.002, 0, 0, 0, 0, 0},
			{0.754, 0.363 ,0.206, 0.091, 0.049, 0.021, 0.011, 0.005, 0.003, 0.001},
			{0.916, 0.656, 0.470, 0.315, 0.206, 0.134, 0.084, 0.054, 0.033, 0.021},
			{0.972, 0.785, 0.642, 0.477, 0.359, 0.253, 0.181, 0.123, 0.086, 0.057},
			{0.990, 0.890, 0.769, 0.638, 0.506, 0.397, 0.297, 0.224, 0.162, 0.118},
			{0.997, 0.934, 0.857, 0.745, 0.638, 0.521, 0.423, 0.329, 0.258, 0.193},
			{0.999, 0.967, 0.910, 0.834, 0.736, 0.640, 0.536, 0.446, 0.357, 0.287},
			{1, 0.980, 0.947, 0.888, 0.818, 0.730, 0.643, 0.547, 0.464, 0.380},
			{1, 0.990, 0.967, 0.930, 0.873, 0.808, 0.726, 0.646, 0.558, 0.480},
			{1, 0.994, 0.981, 0.954, 0.916, 0.861, 0.8, 0.724, 0.65, 0.568},
	};
	
	public String getName () {
		String command = "";
		command = "GumBumBot";
		return(command);
	}

	public String getReinforcement() {
		String command = "";
		
		ArrayList<Integer> countries = playersCountries(player.getId());
		int weak = findWeakestCountry(countries);
		
		//no enemy neighbours
		if(strongestEnemyNeighbour(weak) == -1) {
			ArrayList<Integer> neutralNeighbours = new ArrayList<Integer>();
			for(Integer country : countries) {
				for(int i : GameData.ADJACENT[country]) {
					if(board.getOccupier(i) != player.getId() && board.getOccupier(i) != enemy) {
						neutralNeighbours.add(country);
						break;
					}
				}
			}
			
			weak = neutralNeighbours.get((int) (Math.random()* neutralNeighbours.size()));
		}
		
		command = GameData.COUNTRY_NAMES[weak];
		command = command.replaceAll("\\s", "");
		
		ArrayList<Integer> weakContinents = weakContinentFinder(countries);
		ArrayList<Integer> unheldCountries;
		ArrayList<Integer> borderAllies;
		
		if(!weakContinents.isEmpty()) {
			for(Integer cont : weakContinents) {
				unheldCountries = unownedInContinent(cont);
				borderAllies = new ArrayList<Integer>();
				for(Integer uh : unheldCountries) {
					for(Integer adj : GameData.ADJACENT[uh]) {
						if(board.getOccupier(adj) == player.getId() && !borderAllies.contains(adj))
							borderAllies.add(adj);
					}
				}
				if(weaknessAssess(unitsTotal(borderAllies) + player.getNumUnits(), unitsTotal(unheldCountries))>0.6) {
					//reinforce country bordering the enemy
					command = GameData.COUNTRY_NAMES[borderAllies.get(0)];
					command = command.replaceAll("\\s", "");
					command += " " + player.getNumUnits();
					//testing
					System.out.println("continent taker:" +command);
					return command;
				}
			}
		}
		
		//chance to deposit a large amount of units in one country
		if((int) Math.round( Math.random() * 3) == 1) {
			if((int) Math.round( Math.random() * 3) == 1) {
				//all units
				command += " " + player.getNumUnits();
			}else {
				//random amount of units
				command += " " + (int) (Math.round( Math.random() * (player.getNumUnits()-1)) +1);
			}
		}else {
			command += " 1";
		}
		
		//testing
		System.out.println("reinforce: " + command);
		return(command);
	}
	
	public String getPlacement (int forPlayer) {
		String command = "";
		ArrayList<Integer> heldCountries = playersCountries(forPlayer);
		int bestCountry = heldCountries.get(0);
		for(Integer i : heldCountries) {
			if(neutralReinforcement(i) > neutralReinforcement(bestCountry))
				bestCountry = i;
		}
		command = GameData.COUNTRY_NAMES[bestCountry];
		command = command.replaceAll("\\s", "");
		return(command);
	}
	
	//skips exchange unless forced to
	public String getCardExchange () {
		String command = "";
		
		//check if exchange is possible
		if(player.isOptionalExchange()) {
			return doExchange();
		}
		
		command = "skip";
		return(command);
	}
	
	//variables to hold a list of attacks that can be made for the bot
	private ArrayList<Attack> possibleAttacks;
	private ArrayList<Attack> bestAttacks;
	
	public String getBattle () {
		String command = "";
		// put your code here
		
		//initialising the variables and using getter methods to fill them
		possibleAttacks = new ArrayList<Attack>();
		getPossibleAttacks();
		bestAttacks = new ArrayList<Attack>();
		getBestAttacks();
		
		//if there are no good moves to make the bot will skip
		if(bestAttacks.isEmpty()) {
			command = "skip";
		}
		//otherwise return some calculated move
		else {
			//weight the attacks based on if the continent is close to being taken and if the defending player isn't neutral
			for(int i=0; i<bestAttacks.size();i++) {
				bestAttacks.get(i).probability += ratioContinent(bestAttacks.get(i).defID);
				if(board.getOccupier(bestAttacks.get(i).defID)==enemy) {
					bestAttacks.get(i).probability += 0.25;
				}
			}
			//sort the attacks by probability and get the best one
			Collections.sort(bestAttacks, compareProb);
			Attack thisAttack = bestAttacks.get(bestAttacks.size()-1);
			
			String attackCountry = GameData.COUNTRY_NAMES[thisAttack.attID].replaceAll("\\s", "");
			String defenceCountry = GameData.COUNTRY_NAMES[thisAttack.defID].replaceAll("\\s", "");
			int noTroops=0;
			//attack with the maximum number of troops
			if(thisAttack.attTroops > 3)
				noTroops = 3;
			else
				noTroops = (thisAttack.attTroops - 1);
			
			//fill the command string
			command = attackCountry + " " + defenceCountry + " " + noTroops;

		}
		//testing
		System.out.println("battle: " + command);
		return(command);
	}

	public String getDefence (int countryId) {
		//always defend with the greatest number of troops available
		if(board.getNumUnits(countryId) > 1)
			return "2";
		else return "1";
	}

	public String getMoveIn (int attackCountryId) {
		String command = "";
		Integer possible = board.getNumUnits(attackCountryId)-1;
		command = possible.toString();
		return(command);
	}

	public String getFortify () {
		String command = "skip";
		// put code here
		int moveUnitsFrom = 0;
		int moveUnitsTo = 0;

		for(int i=0;i<GameData.ADJACENT.length;i++) {
				if(board.getOccupier(i) == player.getId()) {
					boolean surrounded = true;
					for(int j=0;j<GameData.ADJACENT[i].length;j++) {
						if(board.getOccupier(GameData.ADJACENT[i][j]) != player.getId()) {
							surrounded = false;
							break;
						}
					}
					
					if(surrounded) {
						if(board.getNumUnits(i) > board.getNumUnits(moveUnitsFrom)) {
								//reinforces countries bordering other continents depending on which country
								if(i == 31) {
									moveUnitsFrom = i;
									moveUnitsTo = 23;
								}
								if(i == 41) {
									moveUnitsFrom = i;
									moveUnitsTo = 31;
								}
								if(i == 28) {
									moveUnitsFrom = i;
									moveUnitsTo = 29;
								}
								if(i == 29 || i == 30) {
									moveUnitsFrom = i;
									moveUnitsTo = 31;
								}
								if(i == 35) {
									moveUnitsFrom = i;
									moveUnitsTo = 34;
								}
								if(i == 33) {
									moveUnitsFrom = i;
									if(board.getOccupier(7) != player.getId()) {
										moveUnitsTo = 32;
									}else{
										moveUnitsTo = 34;
									}
								}
								if(i == 5 || i == 6) {
									moveUnitsFrom = i;
									moveUnitsTo = 7;
								}
								if(i == 0 || i == 1) {
									moveUnitsFrom = i;
									moveUnitsTo = 4;
								}
								if(i == 3) {
									moveUnitsFrom = i;
									moveUnitsTo = 8;
								}
								if(i == 2) {
									moveUnitsFrom = i;
									if(board.getOccupier(14) != player.getId()) {
										moveUnitsTo = 4;
									}else{
										moveUnitsTo = 8;
									}
								}
								if(i == 19 || i == 21 || i == 24 || i == 26) {
									moveUnitsFrom = i;
									moveUnitsTo = 22;
								}
								if(i == 25) {
									moveUnitsFrom = i;
									moveUnitsTo = 20;
								}
								if(i == 27) {
									moveUnitsFrom = i;
									if(board.getOccupier(31) != player.getId()) {
										moveUnitsTo = 23;
									}else {
									moveUnitsTo = 16;
									}
								}
								if(i == 17) {
									moveUnitsFrom = i;
									if(board.getOccupier(31) != player.getId()) {
										moveUnitsTo = 23;
									}else {
									moveUnitsTo = 18;
									}
								}
								if(i == 41 || i == 38) {
									moveUnitsFrom = i;
									moveUnitsTo = 40;
								}
								if(i == 36) {
									moveUnitsFrom = i;
									moveUnitsTo = 37;
								}
								if(i == 40) {
									moveUnitsFrom = i;
									if(board.getOccupier(34) != player.getId()) {
										moveUnitsTo = 37;
									}else {
									moveUnitsTo = 39;
									}
								}
						// donator + reciever + amount of troops
						command = (GameData.COUNTRY_NAMES[moveUnitsFrom]).replaceAll("\\s", "") + " " + (GameData.COUNTRY_NAMES[moveUnitsTo]).replaceAll("\\s", "") + " " + (board.getNumUnits(moveUnitsFrom)-1);
					}
				}
			}
		}
		if((moveUnitsFrom == 0 && moveUnitsTo == 0) || board.getNumUnits(moveUnitsFrom) <= 1) {
			command = "skip";
		}else {
			System.out.println("Move units from " + GameData.COUNTRY_NAMES[moveUnitsFrom] + " to " + GameData.COUNTRY_NAMES[moveUnitsTo] + " " + (board.getNumUnits(moveUnitsFrom)-1));
		}
		if(command.equals("skip")) {
		int mostNeededFortification = 0;      //country that needs fortification
		int highestBordingCountry = 0;        //enemy country with most troops
		int highestAllyCountry = 0;           //ally country with most troops
		int availableTroops = 0;              //ally country's troops

		//cycle through all the countries bording the bots countries
		for(int i=0;i<GameData.ADJACENT.length;i++) {
			for(int j=0;j<GameData.ADJACENT[i].length;j++) {
				
				boolean bordering = false;
				for(int ii=0;ii<GameData.ADJACENT[i].length;ii++) {
					if(GameData.ADJACENT[i][ii] == GameData.ADJACENT[i][j]) {
						bordering = true;
						break;
					}
				}
				//check if bot occupies the country
				if(bordering == true && board.getOccupier(i) == player.getId() && board.getOccupier(GameData.ADJACENT[i][j]) != player.getId()) {
					//save the bording enemy country that has the highest army number
					if(board.getNumUnits(GameData.ADJACENT[i][j]) > board.getNumUnits(highestBordingCountry)) {
						mostNeededFortification = i;
						highestBordingCountry = GameData.ADJACENT[i][j];
						//System.out.println("need fort " + GameData.COUNTRY_NAMES[mostNeededFortification] + "highest threat " + GameData.COUNTRY_NAMES[highestBordingCountry]);
					}
					if(board.getNumUnits(GameData.ADJACENT[i][j]) == board.getNumUnits(highestBordingCountry)) {
						if((int) Math.round(Math.random()) == 1) {
						mostNeededFortification = i;
						highestBordingCountry = GameData.ADJACENT[i][j];
						}
					}
				}
			}
		}

		//gets the ally country with most troops
		for(int c=0;c<GameData.ADJACENT[mostNeededFortification].length;c++) {
			if(board.getNumUnits(GameData.ADJACENT[mostNeededFortification][c]) >= board.getNumUnits(highestAllyCountry)) {
				if(board.getOccupier(GameData.ADJACENT[mostNeededFortification][c]) == player.getId()) {
					highestAllyCountry = GameData.ADJACENT[mostNeededFortification][c];
					if(board.getNumUnits(GameData.ADJACENT[mostNeededFortification][c]) > 1) {
					availableTroops = (int) Math.round((board.getNumUnits(GameData.ADJACENT[mostNeededFortification][c]) -1)/4 );
					}
				}
			}

		}

		boolean bordering = false;
		for(int i=0;i<GameData.ADJACENT[mostNeededFortification].length;i++) {
			if(GameData.ADJACENT[mostNeededFortification][i] == highestAllyCountry) {
				bordering = true;
				break;
			}
		}
		
		if(bordering == true && availableTroops != 0) {
			// reciever + donator + amount of troops
			command = (GameData.COUNTRY_NAMES[highestAllyCountry]).replaceAll("\\s", "") + " " + (GameData.COUNTRY_NAMES[mostNeededFortification]).replaceAll("\\s", "") + " " + availableTroops;
		}else {
			command = "skip";
		}
		if(availableTroops == 0)
			command = "skip";
		}
		return(command);
	}
	
	//CARD EXCHANGE
	//finds a String which is valid for the bot to exchange cards
	private String doExchange() {
		int numWild=0, numInfantry=0, numCavalry=0, numArtillery=0;
		
		for(Card c : player.getCards()) {
			switch(c.getInsigniaId()) {
			case 0:
				numInfantry++;
				break;
			case 1:
				numCavalry++;
				break;
			case 2:
				numArtillery++;
				break;
			default:
				numWild++;
				break;
			}
		}
		
		if(numInfantry >= 3)
			return "iii";
		else if(numCavalry >= 3)
			return "ccc";
		else if(numArtillery >= 3)
			return "aaa";
		
		else if(numInfantry >= 1 && numCavalry >= 1 && numArtillery >= 1)
			return "ica";
		
		else if(numWild == 1) {
			if(numInfantry == 2)
				return "wii";
			else if(numCavalry == 2) {
				return "wcc";
			}else  if(numArtillery == 2) {
				return "waa";
			}else {
				return "skip";
			}
		}
		
		else if(numWild == 2){
			if(numInfantry > 0)
				return "wwi";
			else if(numCavalry > 0)
				return "wwc";
			else
				return "wwa";
		}else {
			return "skip";
		}
	}
			
	//adds 1 every for every border with the enemy and subtracts 1 for every border with the bot
	//meant for neutral reinforcement
	private int neutralReinforcement(int countryId) {
		int count = 0;
		for(int i : GameData.ADJACENT[countryId]) {
			if(board.getOccupier(i) == enemy)
				count++;
			else if(board.getOccupier(countryId) == player.getId())
				count--;
		}
		return count;
	}
	
	//finds all the countries owned by a certain player
	private ArrayList<Integer> playersCountries(int playerId){
		ArrayList<Integer> countries = new ArrayList<Integer>();
		for(int i=0; i<GameData.NUM_COUNTRIES; i++) {
			if(board.getOccupier(i) == playerId)
				countries.add(i);
		}
		return countries;
	}
	
	//finds the weakest country by assessing how likely it is to get beat in a fight by its strongest neighbour
	//for reinforcement
	private int findWeakestCountry(ArrayList<Integer> countries) {
		int weak=countries.get(0);
		
		for(int c : countries) {
			if(strongestEnemyNeighbour(c) == -1) {
			}
			else if(strongestEnemyNeighbour(weak) == -1) {
				weak = c;
			}
			else if(board.getNumUnits(strongestEnemyNeighbour(c)) > 10 || board.getNumUnits(strongestEnemyNeighbour(weak)) > 10 
					|| board.getNumUnits(c) > 10 || board.getNumUnits(weak) > 10) {
				if(board.getNumUnits(strongestEnemyNeighbour(c))/board.getNumUnits(c)*1.0 > board.getNumUnits(strongestEnemyNeighbour(weak))/board.getNumUnits(weak)*1.0)
					weak = c;
			}
			else if(probabilityMatrix[board.getNumUnits(strongestEnemyNeighbour(c))-1][board.getNumUnits(c)-1] >
					probabilityMatrix[board.getNumUnits(strongestEnemyNeighbour(weak))-1][board.getNumUnits(weak)-1]){
				weak = c;
			}
		}
		
		return weak;
	}
	
	//finds the strongest enemy neighbour of a country
	private int strongestEnemyNeighbour(int country) {
		int strongest = -1;
		for(int i : GameData.ADJACENT[country]) {
			if(board.getOccupier(i) == enemy)
				if(strongest == -1)
					strongest = i;
				else if(board.getNumUnits(i) > board.getNumUnits(strongest)) {
					strongest = i;
				}
		}
		return strongest;
	}
	
	//finds weakness as a probability of losing a fight
	private double weaknessAssess(int attackUnits, int defenseUnits) {
		if(attackUnits >= 10|| defenseUnits >= 10) {
			return attackUnits/defenseUnits*1.0;
		}
		else
			return probabilityMatrix[attackUnits][defenseUnits];
	}
	
	//finds a weak continent that the bot might be able to take
	private ArrayList<Integer> weakContinentFinder(ArrayList<Integer> countries) {
		double[] contCount = new double[] {0,0,0,0,0,0};
		for(Integer c : countries) {
			contCount[GameData.CONTINENT_IDS[c]] += ratioContinent(c);
		}
		ArrayList<Integer> weakContinents = new ArrayList<Integer>();
		for(int i=0; i<contCount.length; i++) {
			//ratio owned must be > 0.7 for most continents but is only >= 0.5 for small continents Australia and South America  
			if((contCount[i] > 0.7 ||(contCount[i] >= 0.5 && (i == 3 || i== 4))) && contCount[i]<1)
				weakContinents.add(i);
		}
		return weakContinents;
	}

	//finds all countries in a continent that the bot doesn't own
	private ArrayList<Integer> unownedInContinent(int continent){
		ArrayList<Integer> unheld = new ArrayList<Integer>();
		
		for(int country : GameData.CONTINENT_COUNTRIES[continent]) {
			if(board.getOccupier(country) != player.getNumUnits())
				unheld.add(country);
		}
		return unheld;
	}
	
	//finds the total units of an arraylist of countries
	private int unitsTotal(ArrayList<Integer> countries) {
		int total = 0;
		for(Integer c : countries)
			total += board.getNumUnits(c);
		return total;
	}
		
	//A class to hold attacks and their probabilities for each attacking and defending country
	public class Attack{
		
		int attTroops, defTroops;
		int attID, defID;
		double probability = 0;
		
		//constructor which takes country numbers for the attacking and defending country
		public Attack(int a, int d) {
			attID = a;
			defID = d;
			//variables to hold the troop numbers for both countries
			attTroops = board.getNumUnits(attID);
			defTroops = board.getNumUnits(defID);
			if(attTroops > defTroops) {
				probability = attackProbability(attTroops, defTroops);
			}
			
		}
		
		//returns the probability of attack succes from the probability matrix, or an approximation otherwise
		public double attackProbability(int a, int d) {
			
			if(a-1 < 11 && d < 11) {
				return probabilityMatrix[a-2][d-1];
			}
			else {
				int diff = (a-1)-d;
				switch(diff) {
 					case 0:
						return 0.56;
					case 1:
						return 0.65;
					case 2:
						return 0.72;
					case 3:
						return 0.8;
					case 4:
						return 0.86;
					case 5:
						return 0.92;
					case 6:
						return 0.96;
					default:
						return 1;
				}
			}
		}
	}
	
	//returns the ratio of continent that would be owned by the bot if it invades a country, which weights attacks which give a larger proportion of a continent to the bot
	private double ratioContinent(int country){
		//stores the ratio owned by the bot currently by looping through the owned countries
		double Af=0, As=0, Eu=0, Na=0, Sa=0 ,Oc=0;
		for(int i=0; i<GameData.NUM_CONTINENTS; i++) {
			for(int j=0; j<GameData.CONTINENT_COUNTRIES[i].length; j++) {
				if(board.getOccupier(GameData.CONTINENT_COUNTRIES[i][j])==player.getId()){
					if(i==0) {
						Na += (double)1/9;
					}
					else if(i==1) {
						Eu += (double)1/7;
					}
					else if(i==2) {
						As += (double)1/13;
					}
					else if(i==3) {
						Oc += (double)1/4;
					}
					else if(i==4) {
						Sa += (double)1/4;
					}
					else if(i==5) {
						Af += (double)1/6;
					}
					
				}
			}
			
		}
		//returns the ratio the bot would then get by attacking
		for(int i=0; i<GameData.NUM_CONTINENTS; i++) {
			if(contains(GameData.CONTINENT_COUNTRIES[i],country)) {
				if(i==0) {
					return Na + (double)1/9;
				}
				if(i==1) {
					return Eu + (double)1/7;
				}
				if(i==2) {
					return As + (double)1/13;
				}
				if(i==3) {
					return Oc + (double)1/4;
				}
				if(i==4) {
					return Sa + (double)1/4;
				}
				if(i==5) {
					return Af + (double)1/6;
				}
			}
		}
		return 0;
	}
	//helper function to check if an int array contains a given integer
	public static boolean contains(final int[] array, final int v) {

		boolean result = false;

		for(int i : array){
			if(i == v){
				result = true;
				break;
			}
		}

		return result;
	}

	//returns all possible attacks for the bot
	private void getPossibleAttacks() {
		for(int i=0; i<GameData.NUM_COUNTRIES; i++) {
			if(board.getOccupier(i) == player.getId() && board.getNumUnits(i)>1) {
				for(int j=0; j<GameData.ADJACENT[i].length; j++) {
					if(board.getOccupier(GameData.ADJACENT[i][j]) != player.getId()) {
						possibleAttacks.add(new Attack(i, GameData.ADJACENT[i][j]));
					}
				}
			}
		}
	}
	
	//returns the attacks that are 50% or more likely
	private void getBestAttacks() {
		for(int i=0; i<possibleAttacks.size(); i++) {
			if(possibleAttacks.get(i).probability >= 0.5) {
				bestAttacks.add(possibleAttacks.get(i));
			}
		}
	}
	//comparator method for probabilities of attacks
	Comparator<Attack> compareProb = new Comparator<Attack>() {
		@SuppressWarnings("deprecation")
		@Override
		public int compare(Attack a, Attack b) {
			return new Double (a.probability).compareTo(b.probability);
		}
	};
	

}
