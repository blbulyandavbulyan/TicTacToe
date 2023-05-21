package ui.panels;

import game.common.CellValue;
import game.core.MoveMaker;
import game.core.MoveResult;
import game.core.WHO_WINS;
import scorecounter.IncDecScore;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class CellButton extends JButton {
    private static final Color youWinColor = new Color(0, 125, 0);
    private static final Color youLooseColor =  new Color(125, 0, 0);
    private final Color cellDefaultColor;
    public CellButton(MoveMaker moveMaker, int x, int y, Consumer<MoveResult> gameEndProcessor){
        this.cellDefaultColor = getBackground();
        setFocusPainted(false);
        addActionListener(e -> {
            displayMove(CellValue.X);
            MoveResult moveResult = moveMaker.makeMove(x, y);
            WHO_WINS whoWins = moveResult.whoWins;
            if(whoWins != WHO_WINS.CONTINUE){
                gameEndProcessor.accept(moveResult);
            }
        });
    }
    public void displayMove(CellValue cellValue){
        this.setText(cellValue.name());
        this.setEnabled(false);
    }
    public void setYouLooseStatusColor(){
        setBackground(youLooseColor);
    }
    public void setYouWinStatusColor(){
        setBackground(youWinColor);
    }
    public void reset(){
        setText("");
        setBackground(cellDefaultColor);
        setEnabled(true);
    }
}
