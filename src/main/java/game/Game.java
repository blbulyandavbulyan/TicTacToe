package game;

import game.exceptions.CellIsNotEmptyException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.function.Consumer;


public class Game {
    //считаем что игрок играет крестиком, а компьютер ноликом
    GameField gameField;
    private final Consumer<Coordinates> updateCell;
    public enum WHO_WINS {
        YOU,//победил игрок
        COMPUTER, //победил компьютер
        DRAW,// ничья
        CONTINUE// никто, игра продолжается
    }
    public Game(int fieldSize, Consumer<Coordinates> updateCell){
        gameField = new GameField(fieldSize);
        this.updateCell = updateCell;
    }
    public WHO_WINS makeMove(int x, int y){
        if(gameField.get(x, y) == null){
            gameField.set(x, y, GameField.CellValue.X);
            if(checkWin(GameField.CellValue.X))return WHO_WINS.YOU;
            Coordinates myMove = think();
            if(updateCell != null && myMove != null)
                updateCell.accept(myMove);

            if(checkWin(GameField.CellValue.O))return WHO_WINS.COMPUTER;
            if(gameField.getCountEmptyCells() == 0)return WHO_WINS.DRAW;
            else return WHO_WINS.CONTINUE;
        }
        throw new CellIsNotEmptyException();
    }
    public void reset(){
        gameField.clear();
    }
    private Coordinates think(){
        ArrayList<Coordinates> emptyCellsCoordinates = gameField.getEmptyCellsCoordinates();
        if(emptyCellsCoordinates == null)return null;
        for (Coordinates emptyCellCoordinates : emptyCellsCoordinates) {
            //если мы можем походить в какую-то клетку и выиграть, ходим и выигрываем
            gameField.set(emptyCellCoordinates, GameField.CellValue.O);
            if(checkWin(GameField.CellValue.O)){
                gameField.set(emptyCellCoordinates, GameField.CellValue.O);
                return emptyCellCoordinates;
            }
            //если игрок может походить в какую-то клетку и выиграть, ходим туда первыми и не даём ему выиграть
            gameField.set(emptyCellCoordinates, GameField.CellValue.X);
            if(checkWin(GameField.CellValue.X)){
                gameField.set(emptyCellCoordinates, GameField.CellValue.O);
                return emptyCellCoordinates;
            }
            else gameField.set(emptyCellCoordinates, null);
        }
        int randomIndex = getRandomIndex(emptyCellsCoordinates.size());
        Coordinates result = emptyCellsCoordinates.get(randomIndex);
        gameField.set(result, GameField.CellValue.O);
        return result;
    }
    private Coordinates[] getWinLine(GameField.CellValue winFigure){
        int equalCounter = 0;
        //с левого верхнего угла в правый нижний
        for (; equalCounter < gameField.fieldSize; equalCounter++) {
            if(gameField.get(equalCounter, equalCounter) != winFigure) break;
        }
        if(equalCounter == gameField.fieldSize) {
            Coordinates[] winLineCoordinates = new Coordinates[equalCounter];
            for (int xy = 0; xy < gameField.fieldSize; xy++)
                winLineCoordinates[xy] = new Coordinates(xy, xy);
            return winLineCoordinates;
        }
        else equalCounter = 0;
        //с правого верхнего в левый нижний
        for(int x = gameField.fieldSize - 1, y = 0; x >= 0 && y < gameField.fieldSize; x--, y++){
            if(gameField.get(x, y) == winFigure)equalCounter++;
        }
        if(equalCounter == gameField.fieldSize){
            Coordinates[] winLineCoordinates = new Coordinates[equalCounter];
            for(int x = gameField.fieldSize - 1, y = 0; x >= 0 && y < gameField.fieldSize; x--, y++){
                if(gameField.get(x, y) == winFigure)winLineCoordinates[y] = new Coordinates(x, y);
            }
            return winLineCoordinates;
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
        }
        if(equalCounter == gameField.fieldSize) {
            Coordinates[] winLineCoordinates = new Coordinates[equalCounter];
            equalCounter = 0;
            for(int y = 0; y < gameField.fieldSize; y++){
                for (int x = 0; x < gameField.fieldSize; x++) {
                    if(gameField.get(x, y) == winFigure) {
                        winLineCoordinates[equalCounter++] = new Coordinates(x, y);
                    }
                }
            }
            return winLineCoordinates;
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
        }
        return null;
    }
    private boolean checkWin(GameField.CellValue winFigure){
        int equalCounter = 0;
        //с левого верхнего угла в правый нижний
        for (; equalCounter < gameField.fieldSize; equalCounter++) {
            if(gameField.get(equalCounter, equalCounter) != winFigure) break;
        }
        if(equalCounter == gameField.fieldSize)return true;
        else equalCounter = 0;
        //с правого верхнего в левый нижний
        for(int x = gameField.fieldSize - 1, y = 0; x >= 0 && y < gameField.fieldSize; x--, y++){
            if(gameField.get(x, y) == winFigure)equalCounter++;
        }
        if(equalCounter == gameField.fieldSize)return true;
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
            if(equalCounter == gameField.fieldSize)break;
        }
        if(equalCounter == gameField.fieldSize)return true;
        //вертикальные линии
        for (int x = 0; x < gameField.fieldSize; x++) {
            for (int y = 0; y < gameField.fieldSize; y++) {
                if(gameField.get(x, y) == winFigure)equalCounter++;
                else{
                    equalCounter = 0;
                    break;
                }
            }
            if(equalCounter == gameField.fieldSize)break;
        }
        return equalCounter == gameField.fieldSize;
    }
    private static int getRandomIndex(int max){
        return (int) (Math.random()*max);
    }
}
