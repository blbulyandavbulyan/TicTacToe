package scorecounter;

public interface ScoreCounterInterface extends IncDecScore{
    int getComputerScore();
    int getPlayerScore();
    void reset();
}
