package ui;

import game.common.Coordinates;
import game.core.Game;
import game.core.MoveResult;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class MainWindow extends JFrame {
    private final JButton[][] cells;
    private final JLabel scoreLabel;
    private final Game game;
    private final Color defaultCellColor;
    private final ResourceBundle menuRb = ResourceBundle.getBundle("resources/locales/menu_text");
    private final ResourceBundle guiText = ResourceBundle.getBundle("resources/locales/gui_text");
    private final static Map<Game.WHO_WINS, String> whoWinsToMessageString = new HashMap<>();
    static {
        whoWinsToMessageString.put(Game.WHO_WINS.YOU, "youWin");
        whoWinsToMessageString.put(Game.WHO_WINS.COMPUTER, "youLose");
        whoWinsToMessageString.put(Game.WHO_WINS.DRAW, "youHaveDraw");
    }
    private final int[] score = new int[2];
    public static void main(String[] args) {
        MainWindow mw = new MainWindow();
        mw.setMinimumSize(new Dimension(300, 300));
        mw.setSize(mw.getSize());
        mw.pack();
        mw.setVisible(true);
    }
    public MainWindow(){
        int gameFieldSize = 3;
        JPanel cellsPanel = new JPanel();
        scoreLabel = new JLabel("0:0", SwingConstants.CENTER);
        cellsPanel.setLayout(new GridLayout(gameFieldSize, gameFieldSize));
        cells = new JButton[gameFieldSize][gameFieldSize];
        game = new Game(gameFieldSize, coordinates -> {
            JButton cell = getCell(coordinates);
            cell.setText("O");
            cell.setEnabled(false);
        });
        for(int y = 0; y < cells.length; y++){
            for (int x = 0; x < cells[y].length; x++) {
               final JButton cell = setCell(x, y, new JButton());
               cell.setFocusPainted(false);
               int finalX = x;
               int finalY = y;
               cell.addActionListener(e -> {
                   cell.setText("X");
                   cell.setEnabled(false);
                   MoveResult moveResult = game.makeMove(finalX, finalY);
                   Game.WHO_WINS whoWins = moveResult.whoWins;
                   switch (whoWins) {
                       case YOU -> score[0]++;
                       case COMPUTER -> score[1]++;
                       case DRAW -> {
                           score[0]++;
                           score[1]++;
                       }
                   }
                   if(whoWins != Game.WHO_WINS.CONTINUE){
                       if(whoWins != Game.WHO_WINS.DRAW){
                           Color winLineColor = whoWins == Game.WHO_WINS.COMPUTER ? new Color(125, 0, 0) : new Color(0, 125, 0);
                           for (Coordinates coordinates : moveResult.getWinLine()) {
                               getCell(coordinates).setBackground(winLineColor);
                           }
                       }
                       displayInformMessage("gameOver", whoWinsToMessageString.get(whoWins));
                       scoreLabel.setText(String.format("%d:%d", score[0], score[1]));
                       restartGame();
                   }
               });
               cellsPanel.add(cell);
               cell.addComponentListener(new ComponentAdapter() {
                   @Override
                   public void componentResized(ComponentEvent e) {
                       cell.setFont(cell.getFont().deriveFont((float)(Math.min(cell.getHeight(), cell.getWidth()) - 10)));
                       super.componentResized(e);
                   }
               });
            }
        }
        defaultCellColor = cells[0][0].getBackground();
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(scoreLabel, BorderLayout.NORTH);
        mainPanel.add(cellsPanel, BorderLayout.CENTER);
        this.setContentPane(mainPanel);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        JMenuBar jMenuBar = new JMenuBar();
        jMenuBar.add(createGameMenu());
        this.setJMenuBar(jMenuBar);
    }
    private JMenu createGameMenu(){
        JMenu jGameMenu = new JMenu(menuRb.getString("gameMenu"));
        JMenuItem jRestartGameMenuItem = new JMenuItem(menuRb.getString("restartGame"));
        JMenuItem jResetScoreMenuItem = new JMenuItem(menuRb.getString("resetScore"));
        jRestartGameMenuItem.addActionListener(e -> restartGame());
        jResetScoreMenuItem.addActionListener(e -> {
            score[0] = 0;
            score[1] = 0;
            scoreLabel.setText("0:0");
        });
        jGameMenu.add(jRestartGameMenuItem);
        jGameMenu.add(jResetScoreMenuItem);
        return jGameMenu;
    }
    public void restartGame(){
        for (JButton[] jButtons : cells) {
            for (JButton cell : jButtons) {
                cell.setText("");
                cell.setEnabled(true);
                cell.setBackground(defaultCellColor);
            }
        }
        game.restartGame();
    }
    private void displayInformMessage(String captionKey, String messageKey){
        JOptionPane.showMessageDialog(this, guiText.getString(messageKey), guiText.getString(captionKey),  JOptionPane.INFORMATION_MESSAGE);
    }
    private JButton getCell(int x, int y){
        return cells[y][x];
    }
    private JButton getCell(Coordinates coordinates){
        return getCell(coordinates.x, coordinates.y);
    }
    private JButton setCell(int x, int y, JButton cell){
        cells[y][x] = cell;
        return cell;
    }
}
