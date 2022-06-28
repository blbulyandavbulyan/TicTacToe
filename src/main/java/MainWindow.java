import game.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

public class MainWindow extends JFrame {
    private final int gameFieldSize;
    private final JButton[][] cells;
    private final MainWindow mainWindow;
    private Game game;
    private ResourceBundle menuRb = ResourceBundle.getBundle("locales/menu_text");
    public static void main(String[] args) {
        MainWindow mw = new MainWindow();
        mw.setMinimumSize(new Dimension(300, 300));
        mw.setSize(mw.getSize());
        mw.pack();
        mw.setVisible(true);
    }
    public MainWindow(){
         mainWindow = this;
         gameFieldSize = 3;

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
                 cell.addActionListener(new ActionListener() {
                     @Override
                     public void actionPerformed(ActionEvent e) {
                         cell.setText("X");
                         cell.setEnabled(false);
                         boolean restartGame = false;
                         switch (game.makeMove(finalX, finalY)){
                             case YOU ->{
                                JOptionPane.showMessageDialog(mainWindow, "Вы выиграли!", "Победа!", JOptionPane.INFORMATION_MESSAGE);
                                restartGame = true;
                             }
                             case COMPUTER -> {
                                 JOptionPane.showMessageDialog(mainWindow, "Вы проиграли!", "Поражение!", JOptionPane.INFORMATION_MESSAGE);
                                 restartGame = true;
                             }
                             case DRAW -> {
                                 JOptionPane.showMessageDialog(mainWindow, "У вас ничья!", "Ничья!", JOptionPane.INFORMATION_MESSAGE);
                                 restartGame = true;
                             }
                         }
                         if(restartGame)restartGame();
                     }
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
        jRestartGameMenuItem.addActionListener(e -> {
            restartGame();
        });
        jGameMenu.add(jRestartGameMenuItem);
        return jGameMenu;
    }
    public void restartGame(){
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                JButton cell = cells[i][j];
                cell.setText("");
                cell.setEnabled(true);
            }
        }
        game.reset();
    }
}
