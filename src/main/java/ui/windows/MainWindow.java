package ui.windows;

import game.core.Game;
import game.core.WHO_WINS;
import scorecounter.ScoreCounter;
import scorecounter.ScoreCounterInterface;
import ui.panels.JGameField;
import ui.panels.JScorePanel;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class MainWindow extends JFrame {
    private final JGameField jGameField;
    private final Game game;
    private final ResourceBundle menuRb = ResourceBundle.getBundle("resources/locales/menu_text");
    private final ResourceBundle guiText = ResourceBundle.getBundle("resources/locales/gui_text");
    private final static Map<WHO_WINS, String> whoWinsToMessageString = new HashMap<>();
    private final ScoreCounterInterface scoreCounterInterface;
    static {
        whoWinsToMessageString.put(WHO_WINS.YOU, "youWin");
        whoWinsToMessageString.put(WHO_WINS.COMPUTER, "youLose");
        whoWinsToMessageString.put(WHO_WINS.DRAW, "youHaveDraw");
    }
    public static void main(String[] args) {
        MainWindow mw = new MainWindow();
        mw.setMinimumSize(new Dimension(300, 300));
        mw.setSize(mw.getSize());
        mw.pack();
        mw.setVisible(true);
    }
    public MainWindow(){
        int gameFieldSize = 3;
        JScorePanel jScorePanel = new JScorePanel(guiText, new ScoreCounter());
        scoreCounterInterface = jScorePanel;
        game = new Game(gameFieldSize);
        jGameField = new JGameField(gameFieldSize, game, this::processGameEnd);
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(jScorePanel, BorderLayout.NORTH);
        mainPanel.add(jGameField, BorderLayout.CENTER);
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
            scoreCounterInterface.reset();
        });
        jGameMenu.add(jRestartGameMenuItem);
        jGameMenu.add(jResetScoreMenuItem);
        return jGameMenu;
    }

    private void displayGameOverMessage(String messageKey){
        JOptionPane.showMessageDialog(this, guiText.getString(messageKey), guiText.getString("gameOver"),  JOptionPane.INFORMATION_MESSAGE);
    }
    private void processGameEnd(WHO_WINS whoWins){
        displayGameOverMessage(whoWinsToMessageString.get(whoWins));
        switch (whoWins){
            case YOU -> scoreCounterInterface.incPlayerScore();
            case COMPUTER -> scoreCounterInterface.incComputerScore();
            case DRAW -> {

            }
        }
        restartGame();
    }
    private void restartGame(){
        jGameField.reset();
        game.restartGame();
    }

}
