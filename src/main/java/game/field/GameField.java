package game.field;

import game.common.CellValue;
import game.common.Coordinates;

import java.util.*;

public class GameField {
    private final int fieldSize;

    private final CellValue[][] cellValues;
    private final Collection<Collection<Coordinates>> winLines;
    private int countEmptyCells;
    public GameField(int size){
        cellValues = new CellValue[size][size];
        fieldSize = size;
        winLines = generateWinLines(fieldSize);
        init();
    }
    private static Collection<Collection<Coordinates>> generateWinLines(int fieldSize){
        Collection<Collection<Coordinates>> result = new ArrayList<>();
        //с левого верхнего угла в правый нижний
        {
            Collection<Coordinates> firstDiagonal = new ArrayList<>();
            for (int xy = 0; xy < fieldSize; xy++)
                firstDiagonal.add(new Coordinates(xy, xy));
            result.add(Collections.unmodifiableCollection(firstDiagonal));
        }
        {
            Collection<Coordinates> secondDiagonal = new ArrayList<>();
            //с правого верхнего в левый нижний
            for(int x = fieldSize - 1, y = 0; x >= 0 && y < fieldSize; x--, y++){
                secondDiagonal.add(new Coordinates(x, y));
            }
            result.add(Collections.unmodifiableCollection(secondDiagonal));
        }
        for (int x = 0; x < fieldSize; x++) {
            Collection<Coordinates> verticalLine = new ArrayList<>();
            Collection<Coordinates> horizontalLine = new ArrayList<>();
            for(int y = 0; y < fieldSize; y++){
                verticalLine.add(new Coordinates(x, y));
                horizontalLine.add(new Coordinates(y, x));
            }
            result.add(Collections.unmodifiableCollection(verticalLine));
            result.add(Collections.unmodifiableCollection(horizontalLine));
        }
        return result;
    }
    private CellValue get(int x, int y){
        return cellValues[y][x];
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
    public void clear(){
        init();//выполняем инициализацию заново
    }
    private void init(){
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
        return !findWinLine(winFigure).isEmpty();
    }
    public Collection<Coordinates> findWinLine(CellValue winFigure){
        for(var winLine : winLines){
            int count = 0;
            for(Coordinates winCoordinates : winLine){
                if(get(winCoordinates.x, winCoordinates.y) == winFigure){
                    count++;
                }
                else break;
            }
            if(count == winLine.size())return winLine;
        }
        return Collections.emptyList();
    }
}
