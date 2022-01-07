package game;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.jar.JarEntry;

public class PokerGUI {
    private static String[] players;
    private static int[] handNum;
    private static Deck deck = new Deck();
    private static Font font = new Font(Font.SANS_SERIF, Font.BOLD, 16);

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        JPanel playerPanel = new JPanel();
        JPanel placeHolder = new JPanel();
        frame.setSize(1000, 800);
        frame.setVisible(true);
        //frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        JPanel jp = new JPanel();
        jp.setLayout(new GridLayout(1,3));

        JPanel leftside = new JPanel();
        leftside.setLayout(new GridLayout(8,2));
        JButton startNewGame = new JButton("New Game");
        startNewGame.setFont(font);
        JButton refresh = new JButton("Refresh");
        refresh.setFont(font);
        leftside.add(startNewGame);

        for(int i = 0; i < 13; i++) {
            leftside.add(Box.createRigidArea(new Dimension(1, 1)));
        }
        leftside.add(refresh);
        leftside.add(Box.createRigidArea(new Dimension(1, 1)));
        jp.add(leftside);


        jp.add(playerPanel);
        jp.add(placeHolder);







        startNewGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(null, "Start New Game?", "Choose", JOptionPane.YES_NO_OPTION);
                if(confirm == 0) {
                    getPlayerNames(frame, jp, refresh);
                }
            }
        });

        refresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for(String player : players) {
                    System.out.println(player);
                }
                int notFolded = createPlayerPanel();
                playerPanel.removeAll();
                playerPanel.setLayout(new GridLayout(notFolded, 1));
                for(int i = 0; i < notFolded; i++) {
                    playerPanel.add(new JLabel(players[i], SwingConstants.CENTER));
                }
                frame.repaint();
            }
        });


        frame.add(jp);
        frame.repaint();
        frame.revalidate();
    }

    private static void getPlayerNames(JFrame orgFrame, JPanel orgPanel, JButton refresh) {
        JTextField[] names = new JTextField[8];
        orgFrame.remove(orgPanel);
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
                    orgFrame.setSize(1000,800);
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


    private static int createPlayerPanel() {
        int notFolded = 0;
        for(int i : handNum) {
            if(i > -1) {
                notFolded++;
            }
        }
        return notFolded;
    }

    private static JLabel playerCards;
    private static void newGame() {
        handNum = new int[players.length];
        for(int i = 0; i < handNum.length; i++) {
            handNum[i] = i;
        }
    }

    private void foldPlayer(int p) {
        handNum[p] = -1;
        for (int i = p + 1; i < handNum.length; i++) {
            handNum[i] = handNum[i] - 1;
        }
    }
}
