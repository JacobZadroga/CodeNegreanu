package game;

public class DeckTest {
    public static void main(String[] args) {
//        Deck d = new Deck();
//        d.newDeal(3);
//        String fnl = "";
//
//        fnl += d.dealNextCard(16777216);
//        fnl += d.dealNextCard(0);
//        fnl += d.dealNextCard(8698);
//
//        fnl += d.dealNextCard(16777217);
//        fnl += d.dealNextCard(1);
//        fnl += d.dealNextCard(2031);
//
//        fnl += d.dealNextCard(2097152);
//        fnl += d.dealNextCard(2097153);
//        fnl += d.dealNextCard(2097157);
//        fnl += d.dealNextCard(2097174);
//
//
//        System.out.println(fnl);
//
//        long start = System.currentTimeMillis();
//        d.PercentageWins();
//        long end = System.currentTimeMillis();
//        System.out.println("Milliseconds: " + (end-start));
//        d.fold(1);
//        d.PercentageWins();
          HandRank hr = new HandRank();
          int[] a = new int[] {
                  1,7,2,6,8,23,6
          };
          mergeSort(a, a.length);
          for(int i : a) {
              System.out.println(i);
          }

    }

    public static void mergeSort(int[] lst, int n) {
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

    public static void merge(int[] lst, int[] low, int[] high) {
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
