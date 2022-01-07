package game;

import game.HandRank;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
                0b0,
                0b1,
                0b1000,
                0b111001
        };
        int[] vals = {
                0b0,
                0b1,
                0b101,
                0b10110,
                0b1100010,
                0b111000101,
                0b11111101111,
                0b10000111111010,
                0b101100101000110,
                0b10100011011001101,
                0b1000000000011001101,
                0b10011011010110111001,
                0b101101001001000001101
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
                fnl += "♠";
                break;
            case 1:
                fnl += "♥";
                break;
            case 8:
                fnl += "♣";
                break;
            case 57:
                fnl += "♦";
                break;
        }


        return fnl;
    }

    //get the players Cards
    public String getPlayerCards(int playNum) {
        if(playNum-1 > playerhands.size()) {
            return "Invalid Player.";
        }
        int[] player = playerhands.get(playNum-1);
        return getCardDetails(player[0]) + ", " + getCardDetails(player[1]);

    }

    //get the deck size
    public int getDeckSize() {
        return curdeck.size();
    }

    //get the community cards
    public String getCommunityCards() {
        String fnl = "| ";
        for(int i = 0; i < 5; i++) {
            if(communityCards[i] == -1) break;
            fnl += getCardDetails(communityCards[i]) + " | ";
        }
        return fnl;
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
            playerhands.add(new int[4]);
            playerhands.get(i)[0] = -1;
            playerhands.get(i)[1] = -1;
        }
        for(int i = 0; i < 5; i++) {
            communityCards[i] = -1;
        }
    }

    public String dealNextCard(int card) {

        String fnl;
        if(totalDelt >= (2 * playersIn) + 5) {
            fnl = "Invalid dealing.";
        } else if(totalDelt >= (2 * playersIn)) {
            int comNum = totalDelt - (2 * playersIn);
            communityCards[comNum] = card;
            curdeck.remove((Integer) card);
            fnl = "Dealing " + getCardDetails(card) + " to Community Cards";
            totalDelt++;
        } else {
            playerhands.get(totalDelt%playersIn)[totalDelt/playersIn] = card;
            curdeck.remove((Integer) card);
            fnl = "Delt " + getCardDetails(card) + " to player " + (totalDelt%playersIn);
            totalDelt++;
        }
        return fnl+"\n";
    }


    private void possibleCards(int cardsRemaining, int start, int[] hand) {

        int a = 5 - cardsRemaining;
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
    public void PercentageWins() {
        possibleHands = new ArrayList<int[]>();
        possibleCards(5-(totalDelt - (2 * playersIn)), 0, communityCards);
        double[] wins = hr.getPercentageWin(possibleHands, playerhands);
    }


    //write possible hands depr method
        /*public void writePossibleHands() {
        try {
            FileWriter fw = new FileWriter("pothands.txt");
            int remCards = 5 - (totalDelt - (2 * playerhands.length));
            possibleCards(remCards, 0, communityCards);
            for(int i = 0; i < possibleHands.size(); i++) {
                int[] hand = possibleHands.get(i);
                fw.write(getCardDetails(hand[0]) + " | " + getCardDetails(hand[1]) + " | " + getCardDetails(hand[2])
                        + " | " + getCardDetails(hand[3]) + " | " + getCardDetails(hand[4]) + "\n");
            }
            fw.close();
        } catch(Exception e) {
            System.out.println("File Not Opened.");
        }
    }*/

}
