import game.Coordinates;

import javax.swing.*;

public class Cell extends JButton {
    private final Coordinates cordinates;
    public enum Symbols {X, O, EMPTY}
    private Symbols symbol;
    public Cell(int x, int y){
       this(new Coordinates(x, y));
    }
    public Cell(Coordinates cordinates){
        if(cordinates == null)throw new NullPointerException("cordinates is null");
        this.cordinates = cordinates;
    }
    public void setSymbol(Symbols symbol){
        if(this.symbol == symbol)return;
        this.symbol = symbol;
        switch (symbol){
            case O -> this.setText("O");
            case X -> this.setText("X");
        }
    }

    public Symbols getSymbol() {
        return symbol;
    }
    public void clear(){
        this.setText("");
        this.symbol = Symbols.EMPTY;
    }
}
