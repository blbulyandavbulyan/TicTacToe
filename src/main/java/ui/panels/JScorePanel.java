package ui.panels;

import scorecounter.ScoreCounter;
import scorecounter.ScoreCounterInterface;

import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;

public class JScorePanel extends JPanel implements ScoreCounterInterface {
    private final ScoreCounterInterface scoreCounter;
    private final JLabel jComputerScore;
    private final JLabel jPlayerScore;
    public JScorePanel(ResourceBundle rb, ScoreCounter scoreCounter){
        this.scoreCounter = scoreCounter;
        this.setLayout(new GridLayout(2,2));
        this.add(new JLabel(rb.getString("computer") + ":", SwingConstants.RIGHT));
        this.add(jComputerScore = new JLabel(Integer.toString(scoreCounter.getComputerScore()), SwingConstants.LEFT));
        this.add(new JLabel(rb.getString("you") + ":", SwingConstants.RIGHT));
        this.add(jPlayerScore = new JLabel(Integer.toString(scoreCounter.getPlayerScore()), SwingConstants.LEFT));
    }
    private void displayComputerScore(int computerScore) {
        // TODO: 21.05.2023 Добавить отображение очков компьютера здесь
        jComputerScore.setText(Integer.toString(computerScore));
    }
    private void displayPlayerScore(int playerScore) {
        // TODO: 21.05.2023 Добавить отображение очков игрока здесь
        jPlayerScore.setText(Integer.toString(playerScore));
    }
    @Override
    public int incComputerScore() {
        int computerScore = scoreCounter.incComputerScore();
        displayComputerScore(computerScore);
        return computerScore;
    }

    @Override
    public int incPlayerScore() {
        int playerScore = scoreCounter.incPlayerScore();
        displayPlayerScore(playerScore);
        return playerScore;
    }
    @Override
    public int getComputerScore() {
        return scoreCounter.getComputerScore();
    }

    @Override
    public int getPlayerScore() {
        return scoreCounter.getPlayerScore();
    }
    @Override
    public void reset() {
        scoreCounter.reset();
        // TODO: 21.05.2023 Добавить сброс счётчика здесь
    }
}
