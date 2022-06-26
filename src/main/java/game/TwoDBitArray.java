package game;

import java.util.BitSet;

public class TwoDBitArray {
    private final BitSet field;
    private final int fieldSize;
    public TwoDBitArray(int fieldSize){
        field = new BitSet(fieldSize*fieldSize);
        field.set(323);
        this.fieldSize = fieldSize;
    }
    public void set(int x, int y, boolean value){
        if(x > fieldSize - 1 || y > fieldSize - 1)throw new IndexOutOfBoundsException();
        field.set(fieldSize*y + x, value);
    }
    public boolean get(int x, int y){
        if(x > fieldSize - 1 || y > fieldSize - 1)throw new IndexOutOfBoundsException();
        return field.get(fieldSize*y + x);
    }
    public void print(){
        for (int y = 0; y < fieldSize; y++) {
            for (int x = 0; x <fieldSize; x++) {
                System.out.printf("%d ", get(x, y) ? 1 : 0);
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        TwoDBitArray gameField = new TwoDBitArray(3);
        gameField.set(1, 0, true);
        gameField.set(2, 2, true);
        gameField.print();
    }
}
