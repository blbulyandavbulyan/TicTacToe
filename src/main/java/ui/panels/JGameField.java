package ui.panels;

import game.common.CellValue;
import game.common.Coordinates;
import game.core.GameEngine;
import game.core.MoveMaker;
import game.core.MoveResult;
import game.core.WHO_WINS;
import scorecounter.IncDecScore;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.function.Consumer;

public class JGameField extends JPanel {
    private final CellButton[][] cells;
    private final Consumer<WHO_WINS> gameEndProcessor;
    public JGameField(int gameFieldSize, GameEngine gameEngine, Consumer<WHO_WINS> gameEndProcessor) {
        gameEngine.setComputerMoveDisplayer(this::displayComputerMove);
        setLayout(new GridLayout(gameFieldSize, gameFieldSize));
        this.gameEndProcessor = gameEndProcessor;
        cells = new CellButton[gameFieldSize][gameFieldSize];
        for(int y = 0; y < cells.length; y++){
            for (int x = 0; x < cells[y].length; x++) {
                final JButton cell = setCell(x, y, gameEngine);
                this.add(cell);
                cell.addComponentListener(new ComponentAdapter() {
                    @Override
                    public void componentResized(ComponentEvent e) {
                        cell.setFont(cell.getFont().deriveFont((float)(Math.min(cell.getHeight(), cell.getWidth()) - 10)));
                        super.componentResized(e);
                    }
                });
            }
        }
    }
    private void gameEndProcessing(MoveResult moveResult){
        displayWinLine(moveResult);
        gameEndProcessor.accept(moveResult.whoWins);
    }
    private void displayWinLine(MoveResult moveResult){
        if(moveResult.whoWins != WHO_WINS.DRAW){
            Consumer<CellButton> colorChanger = moveResult.whoWins == WHO_WINS.COMPUTER ? CellButton::setYouLooseStatusColor : CellButton::setYouWinStatusColor;
            for(Coordinates c : moveResult.getWinLine()){
                colorChanger.accept(getCellButton(c));
            }
        }
    }
    private void displayComputerMove(Coordinates coordinates){
        getCellButton(coordinates).displayMove(CellValue.O);
    }
    private CellButton setCell(int x, int y, MoveMaker moveMaker){
        return cells[y][x] = new CellButton(moveMaker, x, y, this::gameEndProcessing);
    }
    private CellButton getCellButton(Coordinates c){
        return cells[c.y][c.x];
    }
    public void reset(){
        for (CellButton[] cellButtons : cells) {
            for (CellButton cell : cellButtons) {
                cell.reset();
            }
        }
//        game.restartGame();
    }
}
