package scorecounter;

public class ScoreCounter implements ScoreCounterInterface{
    private int computerScore;
    private int playerScore;
    @Override
    public int incComputerScore() {
        return ++computerScore;
    }
    @Override
    public int incPlayerScore() {
        return ++playerScore;
    }

    @Override
    public int getComputerScore() {
        return computerScore;
    }

    @Override
    public int getPlayerScore() {
        return playerScore;
    }

    @Override
    public void reset() {
        computerScore = 0;
        playerScore = 0;
    }
}
