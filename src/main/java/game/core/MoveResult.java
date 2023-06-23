package game.core;

import game.common.Coordinates;

import java.util.Collection;

public class MoveResult {
    public final WHO_WINS whoWins;
    private final Collection<Coordinates> winLine;
    public static final MoveResult drawResult = new MoveResult(WHO_WINS.DRAW), continueResult = new MoveResult(WHO_WINS.CONTINUE);
    MoveResult(WHO_WINS whoWins, Collection<Coordinates> winLine) {
        if(whoWins == null || winLine == null)
            throw new IllegalArgumentException("winLine and whoWins must be not null!!!");
        if(whoWins == WHO_WINS.CONTINUE && !winLine.isEmpty())
            throw new IllegalArgumentException("Game continues, but winline is not empty!");
        if(whoWins != WHO_WINS.DRAW && winLine.isEmpty())
            throw new IllegalArgumentException("WinLine must be not empty, because it is not draw!!");
        this.whoWins = whoWins;
        this.winLine = winLine;
    }
    private MoveResult(WHO_WINS whoWins) {
        if (whoWins == WHO_WINS.DRAW || whoWins == WHO_WINS.CONTINUE) {
            winLine = null;
            this.whoWins = whoWins;
        } else throw new IllegalArgumentException("Who win in this constructor must be DRAW or CONTINUE");
    }
    public Collection<Coordinates> getWinLine() {
        if (whoWins == WHO_WINS.DRAW || whoWins == WHO_WINS.CONTINUE)
            throw new UnsupportedOperationException("Win line is not initialized, because game still continues!");
        return winLine;
    }
}
