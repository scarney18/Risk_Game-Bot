//Team name: GumBumBot
//Student names: Sean Carney, Cian Dunne, Cameron Carton
//Student Numbers: 19349341, 19312591, 19720959
public class Card {
	
    private int insignia;
    private int country;
    
    //initialising a card with it's insignia and country number
    public Card(int insignia, int country) {
    	if(insignia < 0 || insignia >= constants.CARD_INSIGNIA_NAMES.length)
    		throw new IllegalArgumentException("invalid insignia");
		this.insignia = insignia;
		this.country = country;
    }
    //getters for insignia and country of a card
    //the string representation of the insignia
    public String getInsignia() {
		return constants.CARD_INSIGNIA_NAMES[insignia];
    }
    //the int of the insignia
    public int getInsigniaInt() {
		return insignia;
    }
    
    public int getCountry() {
		return country;
    }
    
    //a toString Method for Cards
    public String toString() {
    	if(country < 42)
    		return constants.COUNTRY_NAMES[country] +" " + getInsignia();
    	else
    		return "Wild";
    }
}
