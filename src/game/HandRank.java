package game;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class HandRank {
    HashMap<Integer, Integer> flushHash = new HashMap<Integer, Integer>();
    HashMap<Integer, Integer> flushStengthHash = new HashMap<Integer, Integer>();

    public HandRank() {
        loadFlushHand();
    }

    private void loadFlushHand() {
        try {
            FileReader fp = new FileReader("flushrank.txt");
            String str = "";
            char[] line = new char[20];
            int rcv;
            while((rcv = fp.read(line)) == 20) {
                str += new String(line);
            }
            str+= new String(line).substring(0, rcv);

            for(String ln : str.split("\n")) {
                String[] keyPair = ln.split(",");
                flushHash.put((Integer) Integer.parseInt(keyPair[0]), (Integer) Integer.parseInt(keyPair[1]));
            }
            //System.out.println(str);
            flushStengthHash.put(0, 113);
            flushStengthHash.put(1, 112);
            flushStengthHash.put(5, 111);
            flushStengthHash.put(22, 110);
            flushStengthHash.put(98, 109);
            flushStengthHash.put(453, 108);
            flushStengthHash.put(2031, 107);
            flushStengthHash.put(8698, 106);
            flushStengthHash.put(22854, 105);
            flushStengthHash.put(83661, 104);
            flushStengthHash.put(262349, 103);
            flushStengthHash.put(636345, 102);
            flushStengthHash.put(1479181, 101);
        } catch(Exception e) {

        }
    }


    public double[] getPercentageWin(ArrayList<int[]> possibleHands, List<int[]> playerhands) {
        //System.out.println("SIZE: " + possibleHands.size());
        int[] wins = new int[playerhands.size()];
        int[] cur = new int[playerhands.size()];
        for(int[] com : possibleHands) {

            int best = 0;
            int[] stren = new int[playerhands.size()];
            for(int i = 0; i < playerhands.size(); i++) {
                stren[i] = flushStrength(playerhands.get(i), com);
                if(stren[i] > best) {
                    best = stren[i];
                }
            }

            for(int i = 0; i < stren.length; i++) {
                if(stren[i] == best && best > 0) {
                    wins[i]++;
                }
            }

        }
        double[] percent = new double[playerhands.size()];
        for(int win = 0; win < playerhands.size(); win++) {
             //percent[win] = wins[win];
             percent[win] = (wins[win] / (double)possibleHands.size()) * 100;
             System.out.println(percent[win]);
        }
        return percent;
    }

    //if hand is flush return the flush type
    public int flushStrength(int[] hand, int[] community) {
        int sum = 0;
        for(int i : hand) {
            sum += (i >> 21);
        }
        for(int i : community) {
            sum += (i >> 21);
        }
        int type = flushHash.get(sum);
        if(type == -1) return 0;
        if((hand[0]>>21) == type) {
            if((hand[1]>>21) == type) {
                return flushStengthHash.get(Math.min(hand[0], hand[1])&2097151);
            } else {
                return flushStengthHash.get(hand[0]&2097151);
            }
        } else if(hand[1]>>21 == type) {
            return flushStengthHash.get(hand[1]&2097151);
        } else {
            return flushStengthHash.get(Math.max(Math.max(Math.max(Math.max(community[0], community[1]), community[2]), community[3]), community[4])&2097151);
        }
    }
}
