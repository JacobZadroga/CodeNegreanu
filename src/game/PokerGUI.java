package game;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PokerGUI {
    private static String[] players;
    private static int[] handNum;
    private static Deck deck = new Deck();
    private static Font font = new Font(Font.SANS_SERIF, Font.BOLD, 16);

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setSize(1000, 800);
        frame.setVisible(true);
        //frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        JPanel jp = new JPanel();
        jp.setLayout(new GridLayout(8,3));
        JButton startNewGame = new JButton("New Game");
        startNewGame.setFont(font);
        jp.add(startNewGame);
        for(int i = 0; i < 23; i++) {
            jp.add(Box.createRigidArea(new Dimension(100, 20)));
        }

        startNewGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(null, "Start New Game?", "Choose", JOptionPane.YES_NO_OPTION);
                if(confirm == 0) {
                    getPlayerNames(frame, jp);
                }
            }
        });




        frame.add(jp);
        frame.repaint();
    }

    private static void getPlayerNames(JFrame orgFrame, JPanel orgPanel) {
        String[] pl = new String[8];
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

        JButton btn = new JButton("Start Game");
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(null, "Start Game?", "Choose", JOptionPane.YES_NO_OPTION);
                if(confirm == 0) {
                    players = pl;
                    orgFrame.remove(panel);
                    orgFrame.add(orgPanel);
                    orgFrame.repaint();
                }
            }
        });


        String[] options = new String[] {
                "2","3","4","5","6","7","8"
        };
        JComboBox combo = new JComboBox(options);
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

    private void newGame(String[] p) {
        players = p;
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
