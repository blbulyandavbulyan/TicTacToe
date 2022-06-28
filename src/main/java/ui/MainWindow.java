package ui;

import game.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

public class MainWindow extends JFrame {
    private final JButton[][] cells;
    private final Game game;
    private final ResourceBundle menuRb = ResourceBundle.getBundle("resources/locales/menu_text");
    private final ResourceBundle guiText = ResourceBundle.getBundle("resources/locales/gui_text");
    public static void main(String[] args) {
        MainWindow mw = new MainWindow();
        mw.setMinimumSize(new Dimension(300, 300));
        mw.setSize(mw.getSize());
        mw.pack();
        mw.setVisible(true);
    }
    public MainWindow(){
        int gameFieldSize = 3;

         JPanel mainWindowPanel = new JPanel();

         mainWindowPanel.setLayout(new GridLayout(gameFieldSize, gameFieldSize));
         cells = new JButton[gameFieldSize][gameFieldSize];
        game = new Game(gameFieldSize, (coordinates -> {
            JButton cell = cells[coordinates.y][coordinates.x];
            cell.setText("O");
            cell.setEnabled(false);
        }));
         for(int y = 0; y < cells.length; y++){
             for (int x = 0; x < cells[y].length; x++) {
                 final JButton cell = cells[y][x] = new JButton();
                 cell.setFocusPainted(false);
                 int finalX = x;
                 int finalY = y;
                 cell.addActionListener(e -> {
                     cell.setText("X");
                     cell.setEnabled(false);
                     boolean restartGame = false;
                     switch (game.makeMove(finalX, finalY)){
                         case YOU ->{
                            displayInformMessage("gameOver", "youWin");
                            restartGame = true;
                         }
                         case COMPUTER -> {
                             displayInformMessage("gameOver", "youLose");
                             restartGame = true;
                         }
                         case DRAW -> {
                             displayInformMessage("gameOver", "youHaveDraw");
                             restartGame = true;
                         }
                     }
                     if(restartGame)restartGame();
                 });
                 mainWindowPanel.add(cell);
             }
         }

         this.setContentPane(mainWindowPanel);
         this.setDefaultCloseOperation(EXIT_ON_CLOSE);
         JMenuBar jMenuBar = new JMenuBar();
         jMenuBar.add(createGameMenu());
         this.setJMenuBar(jMenuBar);
    }
    private JMenu createGameMenu(){
        JMenu jGameMenu = new JMenu(menuRb.getString("gameMenu"));
        JMenuItem jRestartGameMenuItem = new JMenuItem(menuRb.getString("restartGame"));
        jRestartGameMenuItem.addActionListener(e -> restartGame());
        jGameMenu.add(jRestartGameMenuItem);
        return jGameMenu;
    }
    public void restartGame(){
        for (JButton[] jButtons : cells) {
            for (JButton cell : jButtons) {
                cell.setText("");
                cell.setEnabled(true);
            }
        }
        game.reset();
    }
    private void displayInformMessage(String captionKey, String messageKey){
        JOptionPane.showMessageDialog(this, guiText.getString(messageKey), guiText.getString(captionKey),  JOptionPane.INFORMATION_MESSAGE);
    }
}
