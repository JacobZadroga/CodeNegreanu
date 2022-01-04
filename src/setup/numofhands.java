package setup;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

//49205 possible non suited hands
public class numofhands {
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
        000000000000000010110
        000000000000001100010
        000000000000111000101
        000000000011111101111
        000000010000111111010
        000000101100101000110
        000010100011011001101
        001000000000011001101
        010011011010110111001
        101101001001000001101
     */
    public static void main(String[] args) {
        //System.out.println("Test");
        //0,1,5,22,98,453,2031,8698,22854,83661,262349,636345,1479181
        int[] curDigits = {
                0,1,5,22,98,453,2031,8698,22854,83661,262349,
                636345,1479181
        };


        flushFile();

//        int[] curSDigits = {
//                0, 1, 8, 57
//        };

//        int test = 9;
//        while(true) {
//            if(suiteWorks(test, 4, curSDigits)) {
//                break;
//            }
//            test++;
//        }
//        System.out.println(test);


//        int numS = 3;
//        int ne;
//        while(numS <= 13) {
//            ne = (int) (curDigits[numS-2]*3.1356);
//            //System.out.println(ne);
//            while(true) {
//                if(works(ne, numS, curDigits)) {
//                    break;
//                }
//
//                ne++;
//                //System.out.println(ne);
//            }
//            curDigits[numS-1] = ne;
//            System.out.println(ne);
//            numS++;
//        }
    }
    public static void flushFile() {
        try {
            Set<String> dupes = new HashSet<String>();
            FileWriter fw = new FileWriter("flushrank.txt");
            char[] suits = {
                    'S','S','S','S','S','S','S',
                    'H','H','H','H','H','H','H',
                    'C','C','C','C','C','C','C',
                    'D','D','D','D','D','D','D'
            };
            int[] suits2 = {
                    0,0,0,0,0,0,0,
                    1,1,1,1,1,1,1,
                    8,8,8,8,8,8,8,
                    57,57,57,57,57,57,57
            };

            for(int a = 0; a < 22; a++) {
                for (int b = a + 1; b < 23; b++) {
                    for (int c = b + 1; c < 24; c++) {
                        for (int d = c + 1; d < 25; d++) {
                            for (int e = d + 1; e < 26; e++) {
                                for (int f = e + 1; f < 27; f++) {
                                    for (int g = f + 1; g < 28; g++) {
                                        String toAdd = suits[a] + "" + suits[b] + "" + suits[c] + "" + suits[d] + "" + suits[e] + "" + suits[f] + "" + suits[g];
                                        if(dupes.add(toAdd)) {
                                            int val = suits2[a] + suits2[b] + suits2[c] + suits2[d] + suits2[e] + suits2[f] + suits2[g];
                                            if(toAdd.indexOf("SSSSS") != -1) {
                                                fw.write( val + ",1\n");
                                            } else if(toAdd.indexOf("HHHHH") != -1) {
                                                fw.write( val + ",2\n");
                                            } else if(toAdd.indexOf("CCCCC") != -1) {
                                                fw.write( val + ",3\n");
                                            } else if(toAdd.indexOf("DDDDD") != -1) {
                                                fw.write( val + ",4\n");
                                            } else {
                                                fw.write( val + ",0\n");
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            fw.close();
        } catch(Exception e) {

        }
    }

    public static boolean suiteWorks(int ne, int numsolve, int[] curDigits) {
        int maxindex = numsolve * 7;
        Set<Integer> sums = new HashSet<Integer>();
        Set<String> asd = new HashSet<String>();
        int[] pos = new int[28];
        int k = 0;
        int q = 0;
        while(curDigits[k] != -1) {
            for(int i = 0; i < 7; i++) {
                pos[q] = curDigits[k];
                q++;
            }
            k++;
        }
        for(int i = 0; i < 7; i++) {
            pos[q] = ne;
            q++;
        }

        for(int a = 0; a < 22; a++) {
            for(int b = a+1; b < 23; b++) {
                for(int c = b+1; c < 24; c++) {
                    for(int d = c+1; d < 25; d++) {
                        for(int e = d+1; e < 26; e++) {
                            for(int f = e+1; f < 27; f++) {
                                for(int g = f+1; g < 28; g++) {
                                    if(a < maxindex && b < maxindex && c < maxindex && d < maxindex && e < maxindex && f < maxindex && g < maxindex) {
                                        if(asd.add(pos[a]+ "" +pos[b]+ "" +pos[c]+ "" +pos[d]+ "" +pos[e]+ "" +pos[f]+ "" +pos[g])) {
                                            //System.out.println(pos[a]+ " " +pos[b]+ " " +pos[c]+ " " +pos[d]+ " " +pos[e]+ " " +pos[f]+ " " +pos[g] + " ");
                                            if(!sums.add(pos[a]+pos[b]+pos[c]+pos[d]+pos[e]+pos[f]+pos[g])) {
                                                return false;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return true;
    }

    public static boolean works(int ne, int numsolve, int[] curDigits) {
        int maxindex = numsolve * 4;
        Set<Integer> sums = new HashSet<Integer>();
        Set<String> asd = new HashSet<String>();
        int[] pos = new int[52];
        int c = 0;
        int q = 0;
        while(curDigits[c] != -1) {
            for(int i = 0; i < 4; i++) {
                pos[q] = curDigits[c];
                q++;
            }
            c++;
        }
        for(int i = 0; i < 4; i++) {
            pos[q] = ne;
            q++;
        }

        int tot = 0;
        int temp;
        for(int i = 0; i < 46; i++) {
            for(int j = i+1; j < 47; j++) {
                for(int k = j+1; k < 48; k++) {
                    for(int l = k+1; l < 49; l++) {
                        for(int m = l+1; m < 50; m++) {
                            for(int n = m+1; n < 51; n++) {
                                for(int o = n+1; o < 52; o++) {
                                    if(o < maxindex && n < maxindex && m < maxindex && l < maxindex && k < maxindex && j < maxindex && i < maxindex) {
                                        if(asd.add(pos[i] +""+ pos[j] +""+ pos[k] +""+ pos[l] +""+ pos[m] +""+ pos[n] +""+ pos[o])) {
                                            temp = pos[i] + pos[j] + pos[k] + pos[l] + pos[m] + pos[n] + pos[o];
                                            tot++;
                                            //fw.write(pos[i] +","+ pos[j] +","+ pos[k] +","+ pos[l] +","+ pos[m] +","+ pos[n] +","+ pos[o] + "\n");
                                            if(!sums.add(temp)) {
                                                return false;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        //System.out.println(tot);
        return true;
    }

    public static void loophands() throws IOException {
        FileWriter fw = new FileWriter("pokerhands.txt");
        Set<String> asd = new HashSet<String>();
        char[] pos =  {
            '2','2','2','2',
            '3','3','3','3',
            '4','4','4','4',
            '5','5','5','5',
            '6','6','6','6',
            '7','7','7','7',
            '8','8','8','8',
            '9','9','9','9',
            'T','T','T','T',
            'J','J','J','J',
            'Q','Q','Q','Q',
            'K','K','K','K',
            'A','A','A','A'
        };
        for(int i = 0; i < 46; i++) {
            for(int j = i+1; j < 47; j++) {
                for(int k = j+1; k < 48; k++) {
                    for(int l = k+1; l < 49; l++) {
                        for(int m = l+1; m < 50; m++) {
                            for(int n = m+1; n < 51; n++) {
                                for(int o = n+1; o < 52; o++) {
                                        if(asd.add(pos[i] +""+ pos[j] +""+ pos[k] +""+ pos[l] +""+ pos[m] +""+ pos[n] +""+ pos[o])) {
                                            //temp = pos[i] + pos[j] + pos[k] + pos[l] + pos[m] + pos[n] + pos[o];
                                            //tot++;
                                            fw.write(pos[i] +","+ pos[j] +","+ pos[k] +","+ pos[l] +","+ pos[m] +","+ pos[n] +","+ pos[o] + "\n");
                                            //if(!sums.add(temp)) {
                                            //return false;
                                            //}
                                        }
                                }
                            }
                        }
                    }
                }
            }
        }
        fw.close();
    }
}
