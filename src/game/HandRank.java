package game;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class HandRank {
    HashMap<Integer, Integer> flushHash = new HashMap<Integer, Integer>();
    HashMap<Integer, Integer> flushStengthHash = new HashMap<Integer, Integer>();
    HashMap<Integer, Integer> straightflushStengthHash = new HashMap<Integer, Integer>();


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

            straightflushStengthHash.put(0, 213);
            straightflushStengthHash.put(1, 212);
            straightflushStengthHash.put(5, 211);
            straightflushStengthHash.put(22, 210);
            straightflushStengthHash.put(98, 209);
            straightflushStengthHash.put(453, 208);
            straightflushStengthHash.put(2031, 207);
            straightflushStengthHash.put(8698, 206);
            straightflushStengthHash.put(22854, 205);
            straightflushStengthHash.put(83661, 204);
            straightflushStengthHash.put(262349, 203);
            straightflushStengthHash.put(636345, 202);
            straightflushStengthHash.put(1479181, 201);
        } catch(Exception e) {

        }
    }


    public double[] getPercentageWin(ArrayList<int[]> possibleHands, List<int[]> playerhands) {
        //System.out.println("SIZE: " + possibleHands.size());
        double[] wins = new double[playerhands.size()];
        for(int[] com : possibleHands) {
            int best = 0;
            int[] stren = new int[playerhands.size()];
            for(int i = 0; i < playerhands.size(); i++) {
                //calculate strength
                int sum = 0, comsum = 0, valsum = 0;
                for(int k : playerhands.get(i)) {
                    sum += (k >> 21);
                    valsum += k&2097151;
                }
                for(int k : com) {
                    int g = (k >> 21);
                    comsum += g;
                    sum += g;
                    valsum += k&2097151;
                }
                int flushStren = flushStrength(playerhands.get(i), com, sum, comsum);
                int handStren = 0;
                if(flushStren >= 0) {
                    if(handStren >= 6 && handStren <= 53) {

                    } else {
                        stren[i] = Math.max(flushStren, handStren);
                    }
                }
                stren[i] = handStren;

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
        for(int win = 0; win < playerhands.size(); win++) {
             //percent[win] = wins[win];
             wins[win] = (wins[win] / (double)possibleHands.size()) * 100;
        }
        return wins;
    }

    //if hand is flush return the flush type
    public int flushStrength(int[] hand, int[] community, int sum, int comsum) {
        int type = flushHash.get(sum);
        boolean communityFlush;
        int strength;
        if(type == -1) {
            return -1 << 16;
        } else {
            communityFlush = flushHash.get(comsum) != -1;
        }
        if((hand[0]>>21) == type) {
            if((hand[1]>>21) == type) {
                strength = Math.min(hand[0], hand[1]);
            } else {
                strength = hand[0];
            }
        } else if(hand[1]>>21 == type) {
            strength = hand[1];
        } else {
            // bug where a community flush doesn't take into account if your own hand has worse cards and therefore just uses community cards
            return flushStengthHash.get(Math.max(Math.max(Math.max(Math.max(community[0], community[1]), community[2]), community[3]), community[4])&2097151) + (type << 16);
        }
        if(!communityFlush) {
            return flushStengthHash.get(strength&2097151) + (type << 16);
        } else {
            int g = Math.max(Math.max(Math.max(Math.max(community[0], community[1]), community[2]), community[3]), community[4]);
            return flushStengthHash.get(Math.min(g, strength)&2097151) + (type << 16);
        }
    }

    public int straightFlushStrength(int[] hand, int[] community, int flushtype, int straighthigh) {
        return 1;
    }

    public void mergeSort(int[] lst, int n) {
        if(n <= 1) {
            return;
        }
        int middle = n/2;
        int[] low = new int[middle];
        int[] high = new int[n - (middle)];
        for(int i = 0; i < middle; i++) {
            low[i] = lst[i];
        }
        for(int i = middle; i < n; i++) {
            high[i-middle] = lst[i];
        }
        mergeSort(low, low.length);
        mergeSort(high, high.length);

        merge(lst, low, high);
    }

    public void merge(int[] lst, int[] low, int[] high) {
        int i = 0, j = 0, k = 0;
        while(j < low.length && k < high.length) {
            if(low[j] <= high[k]) {
                lst[i] = low[j];
                i++;
                j++;
            } else {
                lst[i] = high[k];
                k++;
                i++;
            }
        }
        while(j < low.length) {
            lst[i] = low[j];
            i++;
            j++;
        } while(k < high.length) {
            lst[i] = high[k];
            k++;
            i++;
        }
    }
}
