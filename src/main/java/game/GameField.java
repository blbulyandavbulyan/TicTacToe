package game;

import java.util.*;

public class GameField {
    public final int fieldSize;

    public enum CellValue{X, O}
    private final CellValue[][] cellValues;
    private Collection<Coordinates> emptyCells;
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
    public CellValue get(int x, int y){
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
}
