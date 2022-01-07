package game;

import javax.print.attribute.standard.JobMessageFromOperator;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.jar.JarEntry;

public class PokerGUI {
    private static String[] players;
    private static int[] handNum;
    private static Deck deck = new Deck();
    private static Font font = new Font(Font.SANS_SERIF, Font.BOLD, 24);
    private static Font plfont = new Font(Font.SANS_SERIF, Font.BOLD, 140); //65-8

    public static void main(String[] args) {
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
        JLabel[] flopCards = new JLabel[5];


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
                    if(handNum[i] > -1) {
                        remaining[k] = players[i];
                        k++;
                    }
                }
                String folded = (String) JOptionPane.showInputDialog(null, "Select Player to Fold", "Fold Menu", JOptionPane.PLAIN_MESSAGE, null, remaining, remaining[0]);
                for(int i = 0; i < players.length; i++) {
                    if(players[i] == folded) {
                        deck.fold(handNum[i]);
                        foldPlayer(handNum[i]);

                        playerPanel.remove(playerLabels[i]);
                        playerPanel.setLayout(new GridLayout(deck.remainingPlayers(),1));

                        for(k = 0; k < players.length; k++) {
                            if(handNum[k] > -1) {
                                //playerPanel.add(playerLabels[k]);
                            }
                        }

                        playerPanel.revalidate();
                        playerPanel.repaint();
                        frame.repaint();
                        break;
                    }
                }
            }
        });

        frame.add(jp);
        frame.repaint();
        frame.revalidate();
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
            handNum[i] = handNum[i] - 1;
        }
    }

    private static String getHandDetails(int plnum) {
        String s = "asd";
        return s;
    }

    private void resetHands() {

    }
}
