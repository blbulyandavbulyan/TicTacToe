package game;

import game.exceptions.CellIsNotEmptyException;

import java.util.function.Consumer;

public class Game {
    GameField gameField;
    private boolean isMyFigureX = false;
    private final Consumer<Coordinates> updateCell;

    public enum WHO_WINS {YOU, ME, NOBODY};
    public Game(int fieldSize, boolean isComputerFigureX, Consumer<Coordinates> updateCell){
        gameField = new GameField(fieldSize);
        this.isMyFigureX = isComputerFigureX;
        this.updateCell = updateCell;
    }
    public WHO_WINS makeMove(int x, int y){
        if(gameField.get(x, y) == GameField.CellValue.EMPTY){
            gameField.set(x, y, isMyFigureX ? GameField.CellValue.O : GameField.CellValue.X);
            if(updateCell != null)updateCell.accept(think());

        }
        throw new CellIsNotEmptyException();
    }
    private Coordinates think(){
        return null;
    }
    private WHO_WINS checkWinner(){

        return WHO_WINS.NOBODY;
    }
}
