package game.field;

import game.common.Coordinates;

public class CheckWinResult {
    public final boolean result;
    private final Coordinates[] winLine;

    CheckWinResult(boolean result, Coordinates[] winLine) {
        this.result = result;
        this.winLine = winLine;
    }

    CheckWinResult(boolean result) {
        this.result = result;
        winLine = null;
    }

    public Coordinates[] getWinLine() {
        if (winLine != null) return winLine;
        else throw new UnsupportedOperationException("CheckWinResult was created without winLine!");
    }
}
