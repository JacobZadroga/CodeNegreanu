package game;

public class DeckTest {
    public static void main(String[] args) {
        Deck d = new Deck();
        d.newDeal(3);
        String fnl = "";
        fnl += d.dealNextCard(0);
        fnl += d.dealNextCard(1);

        fnl += d.dealNextCard(5);
        fnl += d.dealNextCard(22);

        fnl += d.dealNextCard(2031);
        fnl += d.dealNextCard(8698);

        //fnl += d.dealNextCard(98);
        //fnl += d.dealNextCard(453);
        //fnl += d.dealNextCard(121016845);
        //fnl += d.dealNextCard(119546362);

        System.out.println(fnl);

        long start = System.currentTimeMillis();
        d.PercentageWins();
        long end = System.currentTimeMillis();
        System.out.println("Milliseconds: " + (end-start));
        d.fold(0);
        d.PercentageWins();


    }
}
