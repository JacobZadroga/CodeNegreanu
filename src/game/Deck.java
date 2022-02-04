package game;

import java.util.*;
import java.util.stream.Collectors;

public class Deck {

    public final int SPADE = 0;
    public final int HEART = 1;
    public final int CLUB = 8;
    public final int DIAMOND = 57;

    /*
        000000
        000001
        001000
        111001


        000000000000000000000
        000000000000000000001
        000000000000000000011
        10110
        1100010
        111000101
        11111101111
        10000111111010
        101100101000110
        10100011011001101
        1000000000011001101
        10011011010110111001
        101101001001000001101
     */

    private int[] fulldeck = new int[52];
    private List<Integer> curdeck;
    private List<int[]> playerhands = new ArrayList<int[]>();
    private int playersIn;
    private int[] communityCards = {-1,-1,-1,-1,-1};
    private int totalDelt = 0;
    private ArrayList<int[]> possibleHands = new ArrayList<int[]>();
    private HandRank hr = new HandRank();


    //constructor to initialize the Deck
    public Deck() {


        int[] suits = {
                0b0, //Spades
                0b1, //Hearts
                0b1000, //Clubs
                0b111001 //Diamonds
        };
        int[] vals = {
                0b0, //A
                0b1, //K
                0b101, //Q
                0b10110, //J
                0b1100010, //10
                0b111000101, //9
                0b11111101111, //8
                0b10000111111010, //7
                0b101100101000110, //6
                0b10100011011001101, //5
                0b1000000000011001101, //4
                0b10011011010110111001, //3
                0b101101001001000001101 //2
        };
        int index = 0;
        for(int s : suits) {
            for(int v : vals) {
                fulldeck[index] = (s<<21) + v;
                index++;
            }
        }

        curdeck = Arrays.stream(fulldeck).boxed().collect(Collectors.toList());
    }


    //print the details of the card
    public String getCardDetails(int card) {
        String fnl = "";
        int val = (int) (card & 0b0111111111111111111111);

        int suit = (int) ((card) >> 21);

        if(card == -1) {
            return "--";
        }
        switch(val) {
            case 1479181:
                fnl += "2";
                break;
            case 636345:
                fnl += "3";
                break;
            case 262349:
                fnl += "4";
                break;
            case 83661:
                fnl += "5";
                break;
            case 22854:
                fnl += "6";
                break;
            case 8698:
                fnl += "7";
                break;
            case 2031:
                fnl += "8";
                break;
            case 453:
                fnl += "9";
                break;
            case 98:
                fnl += "T";
                break;
            case 22:
                fnl += "J";
                break;
            case 5:
                fnl += "Q";
                break;
            case 1:
                fnl += "K";
                break;
            case 0:
                fnl += "A";
                break;
        }


        switch(suit) {
            case 0:
                fnl = "<span style=\"color:black\">" + fnl + "♠" + "</span>";
                break;
            case 1:
                fnl = "<span style=\"color:red\">" + fnl + "♥" + "</span>";
                break;
            case 8:
                fnl = "<span style=\"color:black\">" + fnl + "♣" + "</span>";
                break;
            case 57:
                fnl = "<span style=\"color:red\">" + fnl + "♦" + "</span>";
                break;
        }


        return fnl;
    }

    //get the players Cards
    public String getPlayerCards(int playNum) {
        if(playNum-1 > playerhands.size()) {
            return "Invalid Player.";
        }
        int[] player = playerhands.get(playNum);
        return getCardDetails(player[0]) + ", " + getCardDetails(player[1]);

    }

    //get the deck size
    public int getDeckSize() {
        return curdeck.size();
    }

    //get the community cards
    public String getCommunityCard(int i) {
        return getCardDetails(communityCards[i]);
    }

    private void resetDeck() {
        curdeck = Arrays.stream(fulldeck).boxed().collect(Collectors.toList());
    }

    public void newDeal(int numPlayers) {
        playersIn = numPlayers;
        resetDeck();
        totalDelt = 0;
        playerhands = new ArrayList<int[]>();
        for(int i = 0; i < playersIn; i++) {
            playerhands.add(new int[2]);
            playerhands.get(i)[0] = -1;
            playerhands.get(i)[1] = -1;
        }
        for(int i = 0; i < 5; i++) {
            communityCards[i] = -1;
        }
    }

    public boolean dealFromText(int[] cards, int playerNumber) {
        for(int c : cards) {
            if(!curdeck.contains(c)) {
                return false;
            }
        }
        if(cards.length == 3 || cards.length == 1) {
            if(totalDelt < (2 * playersIn)) {
                return false;
            }
            for(int c : cards) {
                int comNum = totalDelt - (2 * playersIn);
                communityCards[comNum] = c;
                curdeck.remove((Integer) c);
                totalDelt++;
            }
        } else {
            playerhands.get(playerNumber)[0] = cards[0];
            playerhands.get(playerNumber)[1] = cards[1];
            curdeck.remove((Integer) cards[0]);
            curdeck.remove((Integer) cards[1]);
            totalDelt += 2;

        }
        return true;
    }

    public int dealNextCard(int card) {

        String fnl;
        if((totalDelt >= (2 * playersIn) + 5)) {
            return 0;
        } else if(curdeck.indexOf((Integer) card) == -1) {
            return -1;
        } else if(totalDelt >= (2 * playersIn)) {
            int comNum = totalDelt - (2 * playersIn);
            communityCards[comNum] = card;
            curdeck.remove((Integer) card);
            totalDelt++;
            return 1;
        } else {
            playerhands.get(totalDelt%playersIn)[totalDelt/playersIn] = card;
            curdeck.remove((Integer) card);
            totalDelt++;
            return 1;
        }
    }


    private void possibleCards(int cardsRemaining, int start, int[] hand) {
        int a = 5 - cardsRemaining;
        if(cardsRemaining==0) {
            possibleHands.add(communityCards);
            return;
        }
        if(cardsRemaining == 1) {
            for(int i = start; i < curdeck.size(); i++) {
                hand[a] = curdeck.get(i);
                possibleHands.add(hand.clone());
            }
        } else {
            for(int i = start; i < curdeck.size()-(cardsRemaining-1);i++) {
                hand[a] = curdeck.get(i);
                possibleCards(cardsRemaining-1, i+1, hand);
            }
        }
    }


    //method tells you how many combinations of toPick cards you can select from total cards (order doesn't matter)
    public int totalPos(int toPick, int totalCards) {
        if(totalCards-toPick == 0) {
            return 1;
        } else if(toPick == 1) {
            return totalCards;
        } else {
            return totalPos(toPick-1, totalCards-1) + totalPos(toPick, totalCards-1);
        }
    }


    public void fold(int playerNum) {
        playerhands.remove(playerNum);
        //playersIn--;
    }

    public int remainingPlayers() {
        return playerhands.size();
    }

    public double[] PercentageWins() {
        possibleHands = new ArrayList<int[]>();
        possibleCards(5-(totalDelt - (2 * playersIn)), 0, communityCards);
        return hr.getPercentageWin(possibleHands, playerhands);
    }

    public int getTotalDelt() {
        return totalDelt;
    }

}
