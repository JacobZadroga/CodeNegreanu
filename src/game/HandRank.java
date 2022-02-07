package game;

import javax.swing.*;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Semaphore;

public class HandRank {
    HashMap<Integer, Integer> flushHash = new HashMap<Integer, Integer>();
    HashMap<Integer, Integer> handHash = new HashMap<Integer, Integer>();
    HashMap<Integer, Integer> flushStengthHash = new HashMap<Integer, Integer>();
    HashMap<Integer, Integer> straightflushStengthHash = new HashMap<Integer, Integer>();


    public HandRank() {
        loadFlushHand();
    }

    private void loadFlushHand() {
        try {
            String path = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath();
            path = path.substring(0, path.length()-16);
            //System.out.println(path);
            FileReader fp = new FileReader(path + "flushrank.txt");
            String str = "";
            char[] line = new char[20];
            int rcv;
            while((rcv = fp.read(line)) == 20) {
                str += new String(line);
            }
            str+= new String(line).substring(0, rcv);

            for(String ln : str.split("\n")) {
                String[] keyPair = ln.split(",");
                flushHash.put(Integer.parseInt(keyPair[0]), Integer.parseInt(keyPair[1]));
            }
            fp.close();
            fp = new FileReader(path + "handrank.txt");
            str = "";
            line = new char[20];
            while((rcv = fp.read(line)) == 20) {
                str += new String(line);
            }
            str+= new String(line).substring(0, rcv);

            for(String ln : str.split("\n")) {
                String[] keyPair = ln.split(",");
                handHash.put((Integer) Integer.parseInt(keyPair[0]), (Integer) Integer.parseInt(keyPair[1].strip()));
            }
            fp.close();

            //System.out.println(str);
            flushStengthHash.put(0, 333);
            flushStengthHash.put(1, 334);
            flushStengthHash.put(5, 335);
            flushStengthHash.put(22, 336);
            flushStengthHash.put(98, 337);
            flushStengthHash.put(453, 338);
            flushStengthHash.put(2031, 339);
            flushStengthHash.put(8698, 340);
            flushStengthHash.put(22854, 341);
            flushStengthHash.put(83661, 342);
            flushStengthHash.put(262349, 343);
            flushStengthHash.put(636345, 344);
            flushStengthHash.put(1479181, 345);

            straightflushStengthHash.put(0, 1);
            straightflushStengthHash.put(1, 2);
            straightflushStengthHash.put(5, 3);
            straightflushStengthHash.put(22, 4);
            straightflushStengthHash.put(98, 5);
            straightflushStengthHash.put(453, 6);
            straightflushStengthHash.put(2031, 7);
            straightflushStengthHash.put(8698, 8);
            straightflushStengthHash.put(22854, 9);
            straightflushStengthHash.put(83661, 10);
            straightflushStengthHash.put(262349, 11);
            straightflushStengthHash.put(636345, 12);
            straightflushStengthHash.put(1479181, 13);
        } catch(Exception e) {
            JOptionPane.showConfirmDialog(null, e.toString(), "Hand Rank Error", JOptionPane.PLAIN_MESSAGE);
        }
    }

    public int[][] range(int total, int numofthreads) {
        int[][] ranges = new int[numofthreads][2];
        int plusones = total % numofthreads;
        int div = total / numofthreads;
        int start = 0;
        for(int i = 0; i < numofthreads; i++) {
            if(start > total-1) {
                ranges[i][0] = -1;
                continue;
            }
            ranges[i][0] = start;
            if(plusones > 0) {
                start += div + 1;
                plusones--;
            } else {
                start += div;
            }
            ranges[i][1] = start-1;
        }
        return ranges;
    }

    public double[] getPercentageWin(ArrayList<int[]> possibleHands, List<int[]> playerhands) {
        int size = playerhands.size();
        //System.out.println("SIZE: " + possibleHands.size());

        double[] wins = new double[size*2];
        for(int i = 0; i<wins.length;i++) {
            wins[i]=0;
        }
        for(int l = 0; l < possibleHands.size(); l++) {
            int best = 4000;
            int[] stren = new int[size];
            for(int i = 0; i < size; i++) {
                //calculate strength
                int sum = 0, comsum = 0, valsum = 0;
                for(int k : playerhands.get(i)) {
                    sum += (k >> 21);
                    valsum += k&2097151;
                }
                for(int k : possibleHands.get(l)) {
                    int g = (k >> 21);
                    comsum += g;
                    sum += g;
                    valsum += k & 2097151;
                }

                int flushStren = flushStrength(playerhands.get(i), possibleHands.get(l), sum, comsum);
                int handStren = handHash.get(valsum);
                if(flushStren > 0) {
                    if(handStren >= 347 && handStren <= 355) {
                        int k = straightFlushStrength(playerhands.get(i), possibleHands.get(l), flushStren>>16);
                        if(k != 0) {
                            stren[i] = k;
                        } else {
                            stren[i] = Math.min((flushStren&0xffff), handStren);
                        }
                    } else {
                        stren[i] = Math.min((flushStren&0xffff), handStren);
                    }
                } else {
                    stren[i] = handStren;
                }

                if(stren[i] < best) {
                    best = stren[i];
                }
            }
            int tie = -1;
            for(int i = 0; i < stren.length; i++) {
                if(stren[i] == best) {
                    if(tie == -1) {
                        wins[i]++;
                        tie = i;
                    } else if(tie == -2) {
                        wins[i+size]++;
                    } else {
                        wins[i+size]++;
                        wins[tie]--;
                        wins[tie+size]++;
                        tie = -2;
                    }
                }
            }

        }
        for(int win = 0; win < size*2; win++) {
             //percent[win] = wins[win];
             wins[win] = (wins[win] / (double)possibleHands.size()) * 100;
        }
        return wins;
    }

    public double[] getPercentageWinThreaded(ArrayList<int[]> possibleHands, List<int[]> playerhands) {
        int size = playerhands.size();
        //System.out.println("SIZE: " + possibleHands.size());

        Thread[] threads = new Thread[4];
        int[][] ranges = range(possibleHands.size(), threads.length);

        double[] wins = new double[size*2];
        for(int i = 0; i<wins.length;i++) {
            wins[i]=0;
        }

        final Semaphore flg = new Semaphore(1);
        for(int j = 0; j < threads.length; j++) {
            final int start = ranges[j][0];
            final int end = ranges[j][1];
            threads[j] = new Thread() {
                public void run() {
                    int s = start;
                    int e = end;
                    double[] winsT = new double[size*2];
                    Semaphore flag = flg;
                    //System.out.println("Start Thread " + System.currentTimeMillis());
                    for(int l = start; l <= end; l++) {
                        int best = 4000;
                        int[] stren = new int[size];
                        for(int i = 0; i < size; i++) {
                            //calculate strength
                            int sum = 0, comsum = 0, valsum = 0;
                            for(int k : playerhands.get(i)) {
                                sum += (k >> 21);
                                valsum += k&2097151;
                            }
                            for(int k : possibleHands.get(l)) {
                                int g = (k >> 21);
                                comsum += g;
                                sum += g;
                                valsum += k & 2097151;
                            }

                            int flushStren = flushStrength(playerhands.get(i), possibleHands.get(l), sum, comsum);
                            int handStren = handHash.get(valsum);
                            if(flushStren > 0) {
                                if(handStren >= 347 && handStren <= 355) {
                                    int k = straightFlushStrength(playerhands.get(i), possibleHands.get(l), flushStren>>16);
                                    if(k != 0) {
                                        stren[i] = k;
                                    } else {
                                        stren[i] = Math.min((flushStren&0xffff), handStren);
                                    }
                                } else {
                                    stren[i] = Math.min((flushStren&0xffff), handStren);
                                }
                            } else {
                                stren[i] = handStren;
                            }

                            if(stren[i] < best) {
                                best = stren[i];
                            }
                        }
                        int tie = -1;
                        for(int i = 0; i < stren.length; i++) {
                            if(stren[i] == best) {
                                if(tie == -1) {
                                    winsT[i]++;
                                    tie = i;
                                } else if(tie == -2) {
                                    winsT[i+size]++;
                                } else {
                                    winsT[i+size]++;
                                    winsT[tie]--;
                                    winsT[tie+size]++;
                                    tie = -2;
                                }
                            }
                        }
                        //flag.release();

                    }
                    //System.out.println("End Thread " + System.currentTimeMillis());
                    try {
                        flag.acquire();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    for(int i = 0; i < wins.length; i++) {
                        wins[i] += winsT[i];
                    }
                    flag.release();
                }
            };
            threads[j].start();
        }
        for(int j = 0; j < threads.length; j++) {
            try {
                threads[j].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        for(int win = 0; win < size*2; win++) {
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

    public int straightFlushStrength(int[] hand, int[] community, int flushtype) {
        int[] cards = new int[] {
                hand[0], hand[1], community[0], community[1], community[2], community[3], community[4]
        };
        mergeSort(cards, 7);
        int inarow = 0;

        int prev = straightflushStengthHash.get(cards[0]&2097151);
        for(int i = 0; i < cards.length; i++) {
            int thiscard = cards[i]&2097151;
            if((straightflushStengthHash.get(thiscard) == prev+1 || inarow == 0) && cards[i] >> 21 == flushtype) {
                inarow++;
            } else if(inarow >= 5) {
                break;
            } else {
                inarow = 1;
            }
            prev = straightflushStengthHash.get(thiscard);
        }
        if(inarow >= 5) {
            return prev-(inarow-1);
        } else {
            return 0;
        }
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
