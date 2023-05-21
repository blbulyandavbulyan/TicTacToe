package game.core;

import game.common.Coordinates;

public class MoveResult {
    public final WHO_WINS whoWins;
    private final Coordinates[] winLine;
    public static final MoveResult drawResult = new MoveResult(WHO_WINS.DRAW), continueResult = new MoveResult(WHO_WINS.CONTINUE);
    MoveResult(WHO_WINS whoWins, Coordinates[] winLine) {
        if ((winLine == null || winLine.length == 0) && (whoWins != WHO_WINS.DRAW && whoWins != WHO_WINS.CONTINUE))
            throw new IllegalArgumentException("WinLine must be not null and not empty for no DRAW or CONTINUE who wins values");
        this.whoWins = whoWins;
        this.winLine = winLine;
    }
    private MoveResult(WHO_WINS whoWins) {
        if (whoWins == WHO_WINS.DRAW || whoWins == WHO_WINS.CONTINUE) {
            winLine = null;
            this.whoWins = whoWins;
        } else throw new IllegalArgumentException("Who win in this constructor must be DRAW or CONTINUE");
    }
    public Coordinates[] getWinLine() {
        if (whoWins == WHO_WINS.DRAW || whoWins == WHO_WINS.CONTINUE)
            throw new UnsupportedOperationException("Win line is not initialized, because game still continues!");
        return winLine;
    }
}
