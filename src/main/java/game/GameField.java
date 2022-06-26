package game;

public class GameField {
    public final int fieldSize;

    public enum CellValue{EMPTY, X, O}
    private final CellValue[][] cellValues;
    public GameField(int size){
        cellValues = new CellValue[size][size];
        fieldSize = size;
        clear();

    }
    public void set(int x, int y, CellValue cellValue){
        cellValues[y][x] = cellValue;
    }
    public CellValue get(int x, int y){
        return cellValues[y][x];
    }
    public void clear(){
        for (int y = 0; y < fieldSize; y++) {
            for (int x = 0; x < fieldSize; x++)set(x, y, CellValue.EMPTY);
        }
    }

}
