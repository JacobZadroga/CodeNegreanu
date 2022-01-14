package game;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class PokerGUI {
    private static String[] players;
    private static int[] handNum;
    private static final Deck deck = new Deck();
    private static final Font font = new Font(Font.SANS_SERIF, Font.BOLD, 24);
    private static final HashMap<String, Integer> dealmap = new HashMap<String, Integer>();

    public static void main(String[] args) {
        setUpHashMap();

        JFrame frame = new JFrame();
        frame.setSize(1920, 1080);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        //frame.setBackground(new Color(20, 157, 9));
        //frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);


        JPanel jp = new JPanel();
        jp.setLayout(new BorderLayout());


        JPanel playerPanel = new JPanel();
        playerPanel.setBorder(new EmptyBorder(15,15,15,15));
        JPanel flop = new JPanel();

        //leftside.setBackground(new Color(7, 117, 30));

        //LEFT SIDE
        JPanel leftside = new JPanel();
        leftside.setLayout(new GridLayout(7,1));
        leftside.setOpaque(false);

        JButton startNewGame = new JButton("New Game");
        startNewGame.setFont(font);
        JButton newGameRefresh = new JButton("Refresh");
        newGameRefresh.setFont(font);
        JButton foldBtn = new JButton("Fold Player");
        foldBtn.setFont(font);
        JButton dealCard = new JButton("Deal Card");
        dealCard.setFont(font);



        leftside.add(startNewGame);
        leftside.add(Box.createRigidArea(new Dimension(1, 1)));
        leftside.add(foldBtn);
        leftside.add(Box.createRigidArea(new Dimension(1, 1)));
        leftside.add(dealCard);
        leftside.add(Box.createRigidArea(new Dimension(1, 1)));
        leftside.add(newGameRefresh);


        jp.add(leftside, BorderLayout.WEST);

        JLabel[] playerLabels = new JLabel[8];

        flop.setLayout(new GridLayout(5, 1));
        flop.setBorder(new EmptyBorder(0,75,0,75));
        JLabel[] flopCards = new JLabel[5];
        for(int i = 0; i < 5; i++) {
            flopCards[i] = new JLabel("<html><span style=\"font-size:40px\">" + "--" + "</span></html>");
            flop.add(flopCards[i]);
        }


        jp.add(playerPanel,BorderLayout.CENTER);
        jp.add(flop, BorderLayout.EAST);







        startNewGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(null, "Start New Game?", "Choose", JOptionPane.YES_NO_OPTION);
                if(confirm == 0) {
                    getPlayerNames(frame, jp, newGameRefresh);
                }
            }
        });

        newGameRefresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int notFolded = players.length;
                deck.newDeal(notFolded);


                playerPanel.removeAll();
                playerPanel.setLayout(new GridLayout(notFolded, 1));
                ((GridLayout)playerPanel.getLayout()).setVgap(15);
                for(int i = 0; i < notFolded; i++) {

                    playerLabels[i] = new JLabel("<html><body style=\"text-align:center;font-size:40px\">" + players[i] + "<br> -- -- | -.-%<body></html>", SwingConstants.CENTER);
                    playerLabels[i].setBackground(Color.GRAY);
                    //playerLabels[i].setBorder(new LineBorder(Color.BLACK));
                    playerLabels[i].setOpaque(true);
                    playerPanel.add(playerLabels[i]);
                }
                frame.repaint();
            }
        });

        foldBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(players == null || players.length <= 1) {
                    return;
                }
                String[] remaining = new String[deck.remainingPlayers()];
                int k = 0;
                for(int i = 0; i < handNum.length; i++) {
                    //System.out.println(players[i] + " | " + handNum[i]);
                    if(handNum[i] > -1) {
                        remaining[k] = players[i];
                        //System.out.println(players[i]);
                        k++;
                    }
                }
                //System.out.println("----");
                String folded = (String) JOptionPane.showInputDialog(null, "Select Player to Fold", "Fold Menu", JOptionPane.PLAIN_MESSAGE, null, remaining, remaining[0]);
                //System.out.println("Selected: " + folded);
                for(int i = 0; i < players.length; i++) {
                    if(players[i] == folded) {
                        deck.fold(handNum[i]);
                        foldPlayer(i);

                        playerPanel.remove(playerLabels[i]);
                        playerPanel.setLayout(new GridLayout(deck.remainingPlayers(),1));
                        ((GridLayout)playerPanel.getLayout()).setVgap(15);

                        playerPanel.revalidate();
                        playerPanel.repaint();
                        frame.repaint();
                        break;
                    }
                }
            }
        });

        dealCard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String card = (String) JOptionPane.showInputDialog(null, "Type Cards to Deal", "Card Deal", JOptionPane.PLAIN_MESSAGE, null, null, "");
                if(card.indexOf(",") != -1) {
                    String[] cards = card.split(",");
                    for(String c : cards) {
                        if(dealmap.get(c) != null) {
                            boolean dealt = deck.dealNextCard(dealmap.get(c));
                            if(dealt) {
                                dealCards(playerLabels, frame);
                            } else {
                                JOptionPane.showConfirmDialog(null, "Card Already Dealt", "Invalid", JOptionPane.PLAIN_MESSAGE);
                                break;
                            }

                        } else {
                            JOptionPane.showConfirmDialog(null, "Not a valid card", "Invalid", JOptionPane.PLAIN_MESSAGE);
                            break;
                        }
                    }
                } else {
                    if(dealmap.get(card) != null) {
                        boolean dealt = deck.dealNextCard(dealmap.get(card));
                        if(dealt) {
                            dealCards(playerLabels, frame);
                        } else {
                            JOptionPane.showConfirmDialog(null, "Card Already Dealt", "Invalid", JOptionPane.PLAIN_MESSAGE);
                        }

                    } else {
                        JOptionPane.showConfirmDialog(null, "Not a valid card", "Invalid", JOptionPane.PLAIN_MESSAGE);
                    }
                }
            }
        });

        frame.add(jp);
        frame.repaint();
        frame.revalidate();
    }

    private static void dealCards(JLabel[] playerLabels, JFrame frame) {
        int i = deck.getTotalDelt();
        if(i <= players.length*2) {
            i = (i-1)%players.length;
            System.out.println(i);
            playerLabels[i].setText(getHandDetails(i));
            if(i == players.length*2) {
                //calculate odds
            }
            frame.revalidate();
            frame.repaint();
        } else {
            boolean calcOdds = i - (players.length*2) >= 3;
            i = i - (players.length*2);
            if(calcOdds) {

            }
        }
    }


    private static void getPlayerNames(JFrame orgFrame, JPanel orgPanel, JButton refresh) {
        JTextField[] names = new JTextField[8];
        orgFrame.remove(orgPanel);
        orgFrame.setLocationRelativeTo(null);
        orgFrame.setSize(600,400);
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5,1));
        panel.setBorder(new EmptyBorder(0,30,30,30));
        JLabel lb = new JLabel("Select Number of Players");
        lb.setHorizontalAlignment(JLabel.CENTER);
        lb.setFont(font);
        panel.add(lb);

        String[] options = new String[] {
                "2","3","4","5","6","7","8"
        };
        JComboBox combo = new JComboBox(options);

        JButton btn = new JButton("Start Game");

        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(null, "Start Game?", "Choose", JOptionPane.YES_NO_OPTION);
                if(confirm == 0) {
                    int totalnames = Integer.parseInt((String) combo.getSelectedItem());
                    players = new String[totalnames];
                    for(int i = 0; i < totalnames; i++) {
                        players[i] = names[i].getText();
                    }
                    newGame();
                    orgFrame.remove(panel);
                    orgFrame.setSize(1920, 1080);
                    orgFrame.add(orgPanel);
                    orgFrame.repaint();
                    refresh.doClick();
                }
            }
        });



        panel.add(combo);
        combo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int totalnames = Integer.parseInt((String) combo.getSelectedItem());
                int num = (orgFrame.getSize().height - 252) / 74;
                orgFrame.setSize(600,252 + (74 * totalnames));
                panel.setLayout(new GridLayout(3 + totalnames,1));
                orgFrame.repaint();
                panel.remove(btn);
                for(int i = 0; i < num; i++) {
                    panel.remove(names[i]);
                }
                for(int i = 0; i < totalnames; i++) {
                    panel.add(names[i]);
                }
                panel.add(btn);
            }
        });
        for(int i = 0; i < 8; i++) {
            names[i] = new JTextField();
            if(i < 2) {
                panel.add(names[i]);
            }

        }

        panel.add(btn);
        orgFrame.add(panel);
        orgFrame.repaint();
    }



    private static void newGame() {
        handNum = new int[players.length];
        for(int i = 0; i < handNum.length; i++) {
            handNum[i] = i;
        }
    }



    private static void foldPlayer(int p) {
        handNum[p] = -1;
        for (int i = p + 1; i < handNum.length; i++) {
            if(handNum[i] > -1) {
                handNum[i] = handNum[i] - 1;
            }
        }
    }

    private static String getHandDetails(int plnum) {
        return "<html><body style=\"text-align:center;font-size:40px\">" + players[plnum] + "<br> " + deck.getPlayerCards(plnum) + " | -.-%<body></html>";
    }

    private static String getHandOdds(int plnum) {
        //return "<html><body style=\"text-align:center;font-size:40px\">" + players[plnum] + "<br> " + deck.getPlayerCards(plnum) + " | " + deck. +"%<body></html>";
        return "ss";
    }

    private void resetHands() {

    }

    private static void setUpHashMap() {
        dealmap.put("as", 0);
        dealmap.put("ks", 1);
        dealmap.put("qs", 5);
        dealmap.put("js", 22);
        dealmap.put("ts", 98);
        dealmap.put("9s", 453);
        dealmap.put("8s", 2031);
        dealmap.put("7s", 8698);
        dealmap.put("6s", 22854);
        dealmap.put("5s", 83661);
        dealmap.put("4s", 262349);
        dealmap.put("3s", 636345);
        dealmap.put("2s", 1479181);

        dealmap.put("ah", 2097152);
        dealmap.put("kh", 2097153);
        dealmap.put("qh", 2097157);
        dealmap.put("jh", 2097174);
        dealmap.put("th", 2097250);
        dealmap.put("9h", 2097605);
        dealmap.put("8h", 2099183);
        dealmap.put("7h", 2105850);
        dealmap.put("6h", 2120006);
        dealmap.put("5h", 2180813);
        dealmap.put("4h", 2359501);
        dealmap.put("3h", 2733497);
        dealmap.put("2h", 3576333);

        dealmap.put("ac", 16777216);
        dealmap.put("kc", 16777217);
        dealmap.put("qc", 16777221);
        dealmap.put("jc", 16777238);
        dealmap.put("tc", 16777314);
        dealmap.put("9c", 16777669);
        dealmap.put("8c", 16779247);
        dealmap.put("7c", 16785914);
        dealmap.put("6c", 16800070);
        dealmap.put("5c", 16860877);
        dealmap.put("4c", 17039565);
        dealmap.put("3c", 17413561);
        dealmap.put("2c", 18256397);

        dealmap.put("ad", 119537664);
        dealmap.put("kd", 119537665);
        dealmap.put("qd", 119537669);
        dealmap.put("jd", 119537686);
        dealmap.put("td", 119537762);
        dealmap.put("9d", 119538117);
        dealmap.put("8d", 119539695);
        dealmap.put("7d", 119546362);
        dealmap.put("6d", 119560518);
        dealmap.put("5d", 119621325);
        dealmap.put("4d", 119800013);
        dealmap.put("3d", 120174009);
        dealmap.put("2d", 121016845);
    }
}
