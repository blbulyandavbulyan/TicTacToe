package game.field;

import game.common.CellValue;
import game.common.Coordinates;

import java.util.*;

public class GameField {
    private final int fieldSize;

    private final CellValue[][] cellValues;
    private int countEmptyCells;
    public GameField(int size){
        cellValues = new CellValue[size][size];
        fieldSize = size;
        clear();

    }
    public void set(final Coordinates coordinates, CellValue cellValue){
        set(coordinates.x, coordinates.y, cellValue);
    }
    public void set(int x, int y, CellValue cellValue){
        if (cellValue != null && get(x, y) == null && countEmptyCells > 0) countEmptyCells--;
        else if(cellValue == null && get(x, y) != null)countEmptyCells++;
        cellValues[y][x] = cellValue;
    }
    public boolean isCellEmpty(int x, int y){
        return get(x, y) == null;
    }
    public void clearCell(Coordinates coordinates){
        set(coordinates.x, coordinates.y, null);
    }
    //устанавливает по заданным координатам moveFigure если при ходе в заданные координаты побеждает winFigure
    private CellValue get(int x, int y){
        return cellValues[y][x];
    }
    public void clear(){
        for (int y = 0; y < fieldSize; y++) {
            for (int x = 0; x < fieldSize; x++)set(x, y, null);
        }
        countEmptyCells = fieldSize*fieldSize;
    }
    public int getCountEmptyCells() {
        return countEmptyCells;
    }
    public ArrayList<Coordinates> getEmptyCellsCoordinates(){
        if(countEmptyCells == 0)return null;
        ArrayList<Coordinates> emptyCellsCoordinates = new ArrayList<>();
        for (int y = 0; y < fieldSize; y++) {
            for (int x = 0; x < fieldSize; x++) {
                if(get(x, y) == null)emptyCellsCoordinates.add(new Coordinates(x, y));
            }
        }
        return emptyCellsCoordinates;
    }
    public boolean checkWin(CellValue winFigure){
        return checkWin(winFigure, false).result;
    }
    public CheckWinResult checkWin(CellValue winFigure, boolean needWinLine){
        int equalCounter = 0;
        //с левого верхнего угла в правый нижний
        for (; equalCounter < this.fieldSize; equalCounter++) {
            if(this.get(equalCounter, equalCounter) != winFigure) break;
        }
        if(equalCounter == this.fieldSize) {
            if(needWinLine){
                Coordinates[] winLine = new Coordinates[equalCounter];
                for(int xy = 0; xy < this.fieldSize; xy++)
                    winLine[xy] = new Coordinates(xy, xy);
                return new CheckWinResult(true, winLine);
            }
            else  return new CheckWinResult(true);
        }
        else equalCounter = 0;
        //с правого верхнего в левый нижний
        for(int x = this.fieldSize - 1, y = 0; x >= 0 && y < this.fieldSize; x--, y++){
            if(this.get(x, y) == winFigure)equalCounter++;
        }
        if(equalCounter == this.fieldSize) {
            if(needWinLine){
                Coordinates[] winLine = new Coordinates[equalCounter];
                for(int x = this.fieldSize - 1, y = 0; x >= 0 && y < this.fieldSize; x--, y++){
                    if(this.get(x, y) == winFigure)
                        winLine[y] = new Coordinates(x, y);

                }
                return new CheckWinResult(true, winLine);
            }
            else  return new CheckWinResult(true);
        }
        else equalCounter = 0;
        //горизонтальные линии
        for(int y = 0; y < this.fieldSize; y++){
            for (int x = 0; x < this.fieldSize; x++) {
                if(this.get(x, y) == winFigure)equalCounter++;
                else{
                    equalCounter = 0;
                    break;
                }
            }
            if(equalCounter == this.fieldSize){
                if(needWinLine){
                    Coordinates[] winLine = new Coordinates[equalCounter];
                    for (int x = 0; x < this.fieldSize; x++)winLine[x] = new Coordinates(x, y);
                    return new CheckWinResult(true, winLine);
                }
                else return new CheckWinResult(true);
            }
        }
        //вертикальные линии
        for (int x = 0; x < this.fieldSize; x++) {
            for (int y = 0; y < this.fieldSize; y++) {
                if(this.get(x, y) == winFigure)equalCounter++;
                else{
                    equalCounter = 0;
                    break;
                }
            }
            if(equalCounter == this.fieldSize) {
                if(needWinLine){
                    Coordinates[] winLine = new Coordinates[equalCounter];
                    for (int y = 0; y < this.fieldSize; y++) {
                        winLine[y] = new Coordinates(x, y);
                    }
                    return new CheckWinResult(true, winLine);
                }
                else return new CheckWinResult(true);
            }
        }
        return new CheckWinResult(equalCounter == this.fieldSize);
    }
}
