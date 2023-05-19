package game.core;

import game.common.Coordinates;
import game.exceptions.CellIsNotEmptyException;
import game.exceptions.GameOverException;
import game.common.CellValue;
import game.field.CheckWinResult;
import game.field.GameField;

import java.util.ArrayList;
import java.util.function.Consumer;


public class Game {
    //считаем что игрок играет крестиком, а компьютер ноликом
    GameField gameField;
    private final Consumer<Coordinates> displayComputerMove;
    private boolean isGameOver = false;
    public enum WHO_WINS {
        YOU,//победил игрок
        COMPUTER, //победил компьютер
        DRAW,// ничья
        CONTINUE// никто, игра продолжается
    }

    private static class ThinkResult{
        final CheckWinResult computerWin;
        final Coordinates computerMoveCoordinates;
        public ThinkResult(CheckWinResult computerWin, Coordinates computerMoveCoordinates) {
            this.computerWin = computerWin;
            this.computerMoveCoordinates = computerMoveCoordinates;
        }
        public ThinkResult(Coordinates computerMoveCoordinates) {
            this.computerMoveCoordinates = computerMoveCoordinates;
            this.computerWin = null;
        }
    }
    public Game(int fieldSize, Consumer<Coordinates> displayComputerMove){
        gameField = new GameField(fieldSize);
        this.displayComputerMove = displayComputerMove;
    }
    public MoveResult makeMove(int x, int y){
        if(isGameOver)throw new GameOverException();
        if(gameField.isCellEmpty(x, y)){
            gameField.set(x, y, CellValue.X);
            {
                CheckWinResult checkWinResult = gameField.checkWin(CellValue.X, true);
                if(checkWinResult.result)return new MoveResult(WHO_WINS.YOU, checkWinResult.getWinLine());
            }
            ThinkResult myMove = think();
            if(myMove != null){
                if(displayComputerMove != null)
                    displayComputerMove.accept(myMove.computerMoveCoordinates);
                if(myMove.computerWin != null && myMove.computerWin.result){
                    return new MoveResult(WHO_WINS.COMPUTER, myMove.computerWin.getWinLine());
                }
                else return gameField.getCountEmptyCells() == 0 ? MoveResult.drawResult : MoveResult.continueResult;

            }
            else return MoveResult.drawResult;
            //else return WHO_WINS.DRAW;
        }
        throw new CellIsNotEmptyException();
    }
    public void restartGame(){
        gameField.clear();
        isGameOver = false;
    }
    private ThinkResult think(){
        ArrayList<Coordinates> emptyCellsCoordinates = gameField.getEmptyCellsCoordinates();
        if(emptyCellsCoordinates == null)return null;
        for (Coordinates emptyCellCoordinates : emptyCellsCoordinates) {
            //если мы можем походить в какую-то клетку и выиграть, ходим и выигрываем
            gameField.set(emptyCellCoordinates, CellValue.O);
            CheckWinResult checkWinResult = gameField.checkWin(CellValue.O, true);
            if(checkWinResult.result){
                gameField.set(emptyCellCoordinates, CellValue.O);
                return new ThinkResult(checkWinResult, emptyCellCoordinates);
            }
            //если игрок может походить в какую-то клетку и выиграть, ходим туда первыми и не даём ему выиграть
            gameField.set(emptyCellCoordinates, CellValue.X);
            if(gameField.checkWin(CellValue.X)){
                gameField.set(emptyCellCoordinates, CellValue.O);
                return new ThinkResult(emptyCellCoordinates);
            }
            else gameField.clearCell(emptyCellCoordinates);
        }
        int randomIndex = getRandomIndex(emptyCellsCoordinates.size());
        Coordinates result = emptyCellsCoordinates.get(randomIndex);
        gameField.set(result, CellValue.O);
        return new ThinkResult(result);
    }
    private static int getRandomIndex(int max){
        return (int) (Math.random()*max);
    }
}
