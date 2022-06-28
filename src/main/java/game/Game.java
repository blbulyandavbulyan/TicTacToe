package game;

import game.exceptions.CellIsNotEmptyException;
import game.exceptions.GameOverException;

import java.util.ArrayList;
import java.util.function.Consumer;


public class Game {
    //считаем что игрок играет крестиком, а компьютер ноликом
    GameField gameField;
    private final Consumer<Coordinates> displayComputerMove;
    private final Consumer<CheckWinResult> displayWinLine;

    private boolean isGameOver = false;
    public enum WHO_WINS {
        YOU,//победил игрок
        COMPUTER, //победил компьютер
        DRAW,// ничья
        CONTINUE// никто, игра продолжается
    }
    public static class CheckWinResult{
        final boolean result;
        public final WHO_WINS whoWins;
        public final Coordinates[] winLine;

        CheckWinResult(boolean result, Coordinates[] winLine, WHO_WINS who_wins) {
            this.result = result;
            this.winLine = winLine;
            this.whoWins = who_wins;
        }
        CheckWinResult(boolean result) {
            this.result = result;
            winLine = null;
            whoWins = null;
        }

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
    public Game(int fieldSize, Consumer<Coordinates> displayComputerMove, Consumer<CheckWinResult> displayWinLine){
        gameField = new GameField(fieldSize);
        this.displayComputerMove = displayComputerMove;
        this.displayWinLine = displayWinLine;
    }
    public WHO_WINS makeMove(int x, int y){
        if(isGameOver)throw new GameOverException();
        if(gameField.get(x, y) == null){
            gameField.set(x, y, GameField.CellValue.X);
            {
                CheckWinResult checkWinResult = checkWin(GameField.CellValue.X, true);
                if(checkWinResult.result) {
                    displayWinLine.accept(checkWinResult);
                    return WHO_WINS.YOU;
                }
            }
            ThinkResult myMove = think();
            if(myMove != null){
                if(displayComputerMove != null)
                    displayComputerMove.accept(myMove.computerMoveCoordinates);
                if(myMove.computerWin != null && myMove.computerWin.result){
                    displayWinLine.accept(myMove.computerWin);
                    return WHO_WINS.COMPUTER;
                }
                else {
                    if(gameField.getCountEmptyCells() == 0)return WHO_WINS.DRAW;
                    else return WHO_WINS.CONTINUE;
                }
            }
            else return WHO_WINS.DRAW;
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
            gameField.set(emptyCellCoordinates, GameField.CellValue.O);
            CheckWinResult checkWinResult = checkWin(GameField.CellValue.O, true);
            if(checkWinResult.result){
                gameField.set(emptyCellCoordinates, GameField.CellValue.O);
                return new ThinkResult(checkWinResult, emptyCellCoordinates);
            }
            //если игрок может походить в какую-то клетку и выиграть, ходим туда первыми и не даём ему выиграть
            gameField.set(emptyCellCoordinates, GameField.CellValue.X);
            if(checkWin(GameField.CellValue.X)){
                gameField.set(emptyCellCoordinates, GameField.CellValue.O);
                return new ThinkResult(emptyCellCoordinates);
            }
            else gameField.set(emptyCellCoordinates, null);
        }
        int randomIndex = getRandomIndex(emptyCellsCoordinates.size());
        Coordinates result = emptyCellsCoordinates.get(randomIndex);
        gameField.set(result, GameField.CellValue.O);
        return new ThinkResult(result);
    }
    private Coordinates[] getWinLine(GameField.CellValue winFigure){

        return null;
    }
    private boolean checkWin(GameField.CellValue winFigure){
        return checkWin(winFigure, false).result;
    }
    private CheckWinResult checkWin(GameField.CellValue winFigure, boolean needWinLine){
        int equalCounter = 0;
        //с левого верхнего угла в правый нижний
        for (; equalCounter < gameField.fieldSize; equalCounter++) {
            if(gameField.get(equalCounter, equalCounter) != winFigure) break;
        }
        if(equalCounter == gameField.fieldSize) {
            if(needWinLine){
                Coordinates[] winLine = new Coordinates[equalCounter];
                for(int xy = 0; xy < gameField.fieldSize; xy++)
                    winLine[xy] = new Coordinates(xy, xy);
                return new CheckWinResult(true, winLine, winFigure == GameField.CellValue.X ? WHO_WINS.YOU : WHO_WINS.COMPUTER);
            }
            else  return new CheckWinResult(true);
        }
        else equalCounter = 0;
        //с правого верхнего в левый нижний
        for(int x = gameField.fieldSize - 1, y = 0; x >= 0 && y < gameField.fieldSize; x--, y++){
            if(gameField.get(x, y) == winFigure)equalCounter++;
        }
        if(equalCounter == gameField.fieldSize) {
            if(needWinLine){
                Coordinates[] winLine = new Coordinates[equalCounter];
                for(int x = gameField.fieldSize - 1, y = 0; x >= 0 && y < gameField.fieldSize; x--, y++){
                    if(gameField.get(x, y) == winFigure)
                        winLine[y] = new Coordinates(x, y);

                }
                return new CheckWinResult(true, winLine, winFigure == GameField.CellValue.X ? WHO_WINS.YOU : WHO_WINS.COMPUTER);
            }
            else  return new CheckWinResult(true);
        }
        else equalCounter = 0;
        //горизонтальные линии
        for(int y = 0; y < gameField.fieldSize; y++){
            for (int x = 0; x < gameField.fieldSize; x++) {
                if(gameField.get(x, y) == winFigure)equalCounter++;
                else{
                    equalCounter = 0;
                    break;
                }
            }
            if(equalCounter == gameField.fieldSize){
                if(needWinLine){
                    Coordinates[] winLine = new Coordinates[equalCounter];
                    for (int x = 0; x < gameField.fieldSize; x++)winLine[x] = new Coordinates(x, y);

                    return new CheckWinResult(true, winLine, winFigure == GameField.CellValue.X ? WHO_WINS.YOU : WHO_WINS.COMPUTER);
                }
                else new CheckWinResult(true);
            }
        }
        //вертикальные линии
        for (int x = 0; x < gameField.fieldSize; x++) {
            for (int y = 0; y < gameField.fieldSize; y++) {
                if(gameField.get(x, y) == winFigure)equalCounter++;
                else{
                    equalCounter = 0;
                    break;
                }
            }
            if(equalCounter == gameField.fieldSize) {
                if(needWinLine){
                    Coordinates[] winLine = new Coordinates[equalCounter];
                    for (int y = 0; y < gameField.fieldSize; y++) {
                        winLine[y] = new Coordinates(x, y);
                    }
                    return new CheckWinResult(true, winLine, winFigure == GameField.CellValue.X ? WHO_WINS.YOU : WHO_WINS.COMPUTER);
                }
                else new CheckWinResult(true);
            }
        }
        return new CheckWinResult(equalCounter == gameField.fieldSize);
    }
    private static int getRandomIndex(int max){
        return (int) (Math.random()*max);
    }
}
