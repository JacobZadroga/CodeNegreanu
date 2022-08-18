package project;

public class Hand 
{
	private String set;
	private int handValue;
	private int highestQuantity;
	private int secondHighestQuantity;
	private int highVal = 13;
	private int highVal2 = 13;
	private int straightCount = 1;
	private int straightVal = 13;
	private int[] kicker = {13, 13, 13, 13, 13, 13, 13};
	private String kickers = "";
	private int handType = -1;
	private int rank = -1;
	
	/*Constructor that takes in a unique combination of 7 cards (2 in hand + 5 on table) to construct a hand
	 *Order of operations is as follows:
	 *	1. Set gets the hand removing commas (2,3,4,5,6,7,8 becomes 2345678)
	 *  2. handValue gets the unique ID for the hand (using specifically picked numbers so that no two hands will result in an identical ID)
	 *  3. handValuation determines the handType (see handType() below for more information)
	 *  4. calculateKickers() adds any relevant kickers to the hand (getKickers() ensures only 5 cards are ever used to match Hold Em' rules)
	 */
	public Hand(String playerSet)
	{
		set = parseHand(playerSet);
		handValue = calcValue(set);
		handValuation(set);
		calculateKickers();
		System.out.println("Hand: " + set + " Hand Type: " + handType() + ": " + cardVal(highVal) + " Second High Card: " + cardVal(highVal2) + " Kickers: " + kickers);
	}
	
	//Returns a String that only contains characters from the deck.
	public String parseHand(String inputDeck)
	{
		String currDeck = "";
		//i+= 2 to account for commas on the initial data presented.
		for(int i = 0; i < inputDeck.length(); i += 2)
		{
			currDeck += inputDeck.charAt(i);
		}
		
		return currDeck;
	}
	
	//Gets the unique identifier for the hand
	public int calcValue(String deck)
	{
		int value = 0;
		
		for(int i = 0; i < deck.length(); i++)
		{
			value += getValue(deck.charAt(i));
		}
		
		return value;
	}
	
	public int getValue(char a)
	{
		switch(a){
			case 'A': return 0;
			case 'K': return 1;
			case 'Q': return 5;
			case 'J': return 22;
			case 'T': return 98;
			case '9': return 453;
			case '8': return 2031;
			case '7': return 8698;
			case '6': return 22854;
			case '5': return 83661;
			case '4': return 262349;
			case '3': return 636345;
			case '2': return 1479181;
		}
		return -1;
	}
	
	public String getHand()
	{
		return set;
	}
	
	public int getHandValue()
	{
		return handValue;
	}
	
	public int getKicker(int i)
	{
		return kicker[i];
	}
	
	public String getKickers()
	{
		//Returns relevant kickers up to a max of 5 dependent on hand type
		if(handType == 0)
		{
			return kickers.substring(0,1);
		}
		else if(handType == 1)
		{
			return cardVal(secondHighestQuantity);
		}
		else if(handType == 2)
		{
			return cardVal(straightVal);
		}
		else if(handType == 3)
		{
			return kickers.substring(0,2);
		}
		else if(handType == 4)
		{
			return kickers.substring(0,1);
		}
		else if(handType == 5)
		{
			return kickers.substring(0,3);
		}
		else
		{
			return kickers.substring(0,4);
		}
	}
	
	public int getHighVal()
	{
		return highVal;
	}
	
	public int getSecondHighVal()
	{
		return highVal2;
	}
	
	public int getHandType()
	{
		return handType;
	}
	
	public int getStraightVal()
	{
		return straightVal;
	}
	
	public void handValuation(String inputDeck)
	{
		highestQuantity = 1;
        secondHighestQuantity = 1;
        evaluateQuantity();
     
        //If all values are unique, get the high card
		if(highestQuantity == 1)
		{
			highVal = calcVal(inputDeck.charAt(6));
		}
		
		//If there is no second high card that was set, set it now
		if(highVal2 == 13)
		{
			for(int i = 0; i < inputDeck.length(); i++)
			{
				if(highVal2 > calcVal(inputDeck.charAt(i)))
				{
					if(inputDeck.charAt(i) != highVal)
					{
						highVal2 = calcVal(inputDeck.charAt(i));
					}
				}
			}
		}
		
		//Check to see if the hand has a straight
		evaluateStraight();

		
		//Get kickers as long as 5 cards haven't already been used (Full House & Straight violate this principle)
		if(straightVal == 13 && !(highestQuantity == 3 && (secondHighestQuantity == 2 || secondHighestQuantity == 3)))
		{
			kickers = calculateKickers();
			
			for(int i = 0; i < kickers.length(); i++)
			{
				kicker[i] = calcVal(kickers.charAt(i));
			}
		}
		
        deckEvaluator();
	}
	
	
	public void deckEvaluator()
	{	
		String[] kickerStr = {"", "", "", "", "", "", ""};
		
		for(int i = 0; i < kicker.length; i++)
		{
			kickerStr[i] += cardVal(kicker[i]);
		}
		
		//Four of A Kind
		if(highestQuantity == 4)
		{
			handType = 0;
		}
		//Full House
		if(highestQuantity == 3 && (secondHighestQuantity >= 2))
		{
			handType = 1;
		}
		//Straight
		else if(straightVal != 13)
		{
			handType = 2;
		}
		//Three Of A Kind
		else if(highestQuantity == 3)
		{
			handType = 3;
		}
		else if(highestQuantity == 2)
		{
			//Two Pairs
			if(secondHighestQuantity == 2)
			{
				handType = 4;
			}
			//Pair
			else
			{
				handType = 5;
			}
		}
		//High Card
		else if(highestQuantity == 1)
		{
			handType = 6;
		}
	}
	
	public String calculateKickers()
	{
		//String to hold all of the kickers
		String kickers = "";
		//Flag to add kicker if it isn't dependent on a second value (example: two pairs where the lower pair shouldn't be also counted as a kicker)
		boolean secondHighValSet = false;
		int lastValAdded = -1;
		
		//Check hand right to left as hands are sorted from least val to greatest
		for(int i = (set.length() - 1); i > -1; i--)
		{
			int val = calcVal(set.charAt(i));

			if(secondHighestQuantity != 1 || secondHighestQuantity != highestQuantity)
			{
				if(val != highVal)
				{
					if(val == highVal2 && val != lastValAdded)
					{
						if(secondHighValSet == false && !(highestQuantity == 2 && secondHighestQuantity == 2))
						{
							kickers += cardVal(val);
						}
						secondHighValSet = true;
						lastValAdded = val;
					}
					else
					{
						if(val != lastValAdded)
						{
							kickers += cardVal(val);
							lastValAdded = val;
						}
					}
				}
			}
			else
			{
				if(val != lastValAdded)
				{
					kickers += cardVal(val);
					lastValAdded = val;
				}
			}
		}
		return kickers;
	}
	
	public void evaluateQuantity()
	{
		int quantity = 1;
		
		for(int i = 0; i < set.length() - 1; i++)
		{	
			//If the calculated value equals the next value
			if(calcVal(set.charAt(i)) == calcVal(set.charAt(i + 1)))
			{
				quantity++;
				
				//If the current quantity exceeds the highest
				if(quantity > highestQuantity)
				{
					highestQuantity = quantity;
					
					if(highVal != calcVal(set.charAt(i)))
					{
						highVal2 = highVal;
					}
					
					highVal = calcVal(set.charAt(i));
				}
				else if(quantity > secondHighestQuantity)
				{
					secondHighestQuantity = quantity;
					highVal2 = calcVal(set.charAt(i));
				}
				
				if(quantity == secondHighestQuantity && calcVal(set.charAt(i)) < highVal2)
				{
					highVal2 = calcVal(set.charAt(i));
				}
			}
			else
			{		
				quantity = 1;
				
				if(highestQuantity == 1)
				{
					highVal = calcVal(set.charAt(i));
				}
			}
			
			//Switch if values get inverted (ensures that in the event highest and second highest are equal, the higher
			//value card will correspond to the correct variable name.)
			if(highestQuantity == secondHighestQuantity && highVal > highVal2)
			{
				int tempVal = highVal;
				highVal = highVal2;
				highVal2 = tempVal;
			}
			
			System.out.println("i: " + i + " Card: " + set.charAt(i) + " CQ: " + quantity + " Q1: " + highestQuantity + " V: " + cardVal(highVal) + " Q2: " + secondHighestQuantity + " V2: " + cardVal(highVal2));
		}
		
		//If a second highest quantity is never established above 1, we still need to have the second highest value present
		if(secondHighestQuantity == 1)
		{
			for(int i = 0; i < set.length(); i++)
			{
				if(calcVal(set.charAt(i)) < highVal2 && highVal != calcVal(set.charAt(i)))
				{
					highVal2 = calcVal(set.charAt(i));
				}
			}
		}
		
	}
	
	public void evaluateStraight()
	{
		//Account for low or high straight
		boolean containsAce = false;
		boolean highStraight = false;
		int aceCounter = 0;
		String aceString = "";
		
		for(int i = 0; i < set.length(); i++)
		{
			if(calcVal(set.charAt(i)) == 0)
			{
				containsAce = true;
				aceCounter++;
			}
		}
		
		//Look for high straight first (ace does not matter low)
		for(int i = 0; i < set.length() - 1; i++)
		{
			//Sequential Ascending Order
			if((calcVal(set.charAt(i)) - 1) == calcVal(set.charAt(i + 1)))
			{
				straightCount++;

				
				if(straightCount >= 5)
				{
					highStraight = true;
					straightVal = calcVal(set.charAt(i + 1));
				}
			}
			//If the value is otherwise greater, then reset the straight count
			else if((calcVal(set.charAt(i)) > calcVal(set.charAt(i + 1))))
			{
				straightCount = 1;
			}
			//If value is equal, do nothing
		}
		
		//Rewrite string
		if(containsAce == true && highStraight == false)
		{
			straightCount = 1; 
			
			for(int i = 0; i < aceCounter; i++)
			{
				aceString += "1";
			}
			
			aceString += set.substring(0, set.length() - aceCounter);
			
			//Look for low straight next
			for(int i = 0; i < aceString.length() - 1; i++)
			{
				//Sequential Ascending Order
				if((calcVal(aceString.charAt(i)) - 1) == calcVal(aceString.charAt(i + 1)))
				{
					straightCount++;
					
					if(straightCount >= 5)
					{
						straightVal = calcVal(aceString.charAt(i + 1));
					}
				}
				//If the value is otherwise greater, then reset the straight count
				else if((calcVal(aceString.charAt(i)) > calcVal(aceString.charAt(i + 1))))
				{
					straightCount = 1;
				}
				//If value is equal, do nothing
				
				//Debug tool for straights
				//System.out.println(aceString);
				//System.out.println("Hand: " + set + " Card: " + set.charAt(i) + " #: " + straightCount + " ?:" + highStraight);
			}
		}
	}
	
	public void setRank(int setRank)
	{
		rank = setRank;
	}
	
	public int getRank()
	{
		return rank;
	}
	
	//Order cards from best to worst in pure numeric fashion
	public static int calcVal(char a)
	{
		switch(a){
		    case '1': return 13;
			case '2': return 12;
			case '3': return 11;
			case '4': return 10;
			case '5': return 9;
			case '6': return 8;
			case '7': return 7;
			case '8': return 6;
			case '9': return 5;
			case 'T': return 4;
			case 'J': return 3;
			case 'Q': return 2;
			case 'K': return 1;
			case 'A': return 0;
		}
		return 14;
	}
	
	
	//Get string representation of cards that were transferred using calcVal()
	public static String cardVal(int a)
	{
		switch(a) {
			case 0: return "A";
			case 1: return "K";
			case 2: return "Q";
			case 3: return "J";
			case 4: return "T";
			case 5: return "9";
			case 6: return "8";
			case 7: return "7";
			case 8: return "6";
			case 9: return "5";
			case 10: return "4";
			case 11: return "3";
			case 12: return "2";
		}
		return "X";
	}
	
	
	//Turn handType into an easily identifiable Hold 'Em format
	public String handType()
	{
		switch(handType){
		case 0: return "Four of a Kind";
		case 1: return "Full House";
		case 2: return "Straight";
		case 3: return "Three of a Kind";
		case 4: return "Two Pair";
		case 5: return "Pair";
		case 6: return "High Card";
		}
		return "ERROR";
	}

}
