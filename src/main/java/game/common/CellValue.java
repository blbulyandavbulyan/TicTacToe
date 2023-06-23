package game.common;

public enum CellValue {
    X, O;
    public CellValue not(){
        if(this == X)return O;
        else return X;
    }
}
