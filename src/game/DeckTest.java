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
          System.out.println(hr.flushStrength(new int[] {
                  120174009, 2097174
          }, new int[] {
                  0, 1, 5, 2031, 22854
          }));

    }
}
