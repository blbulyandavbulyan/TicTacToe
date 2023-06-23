package game.core;

import game.common.CellValue;
import game.common.Coordinates;
import game.exceptions.CellIsNotEmptyException;
import game.exceptions.GameOverException;
import game.field.GameField;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.function.Consumer;


public class Game implements GameEngine{
    //считаем что игрок играет крестиком, а компьютер ноликом
    private final GameField gameField;
    private Consumer<Coordinates> displayComputerMove;
    private boolean isGameOver = false;
    private static class ThinkResult{

        final Coordinates computerMoveCoordinates;
        final Collection<Coordinates> computerWinLine;
        public ThinkResult(Collection<Coordinates> computerWinLine, Coordinates computerMoveCoordinates) {
            this.computerWinLine = computerWinLine;
            this.computerMoveCoordinates = computerMoveCoordinates;
        }
        public ThinkResult(Coordinates computerMoveCoordinates) {
            this.computerMoveCoordinates = computerMoveCoordinates;
            this.computerWinLine = Collections.emptyList();
        }
    }
    private static class ThinkScore{
        private final Coordinates coordinates;
        long countOfWins;
        long countOfLoses;
        long countOfDraws;
        public ThinkScore(Coordinates coordinates) {
            this.coordinates = coordinates;
        }
        public Coordinates getCoordinates() {
            return coordinates;
        }
    }
    public Game(int fieldSize, Consumer<Coordinates> displayComputerMove){
        gameField = new GameField(fieldSize);
        this.displayComputerMove = displayComputerMove;
    }

    public Game(int fieldSize) {
        this(fieldSize, null);
    }
    @Override
    public void setComputerMoveDisplayer(Consumer<Coordinates> computerMoveDisplayer) {
        displayComputerMove = computerMoveDisplayer;
    }
    @Override
    public MoveResult makeMove(int x, int y){
        if(isGameOver)throw new GameOverException();
        if(gameField.isCellEmpty(x, y)){
            gameField.set(x, y, CellValue.X);
            {
                var winLine = gameField.findWinLine(CellValue.X);
                if(!winLine.isEmpty()) return new MoveResult(WHO_WINS.YOU, winLine);
            }
            ThinkResult myMove = think();
            if(myMove != null){
                if(displayComputerMove != null)
                    displayComputerMove.accept(myMove.computerMoveCoordinates);
                if(!myMove.computerWinLine.isEmpty()){
                    return new MoveResult(WHO_WINS.COMPUTER, myMove.computerWinLine);
                }
                else return gameField.getCountEmptyCells() == 0 ? MoveResult.drawResult : MoveResult.continueResult;
            }
            else return MoveResult.drawResult;
            //else return WHO_WINS.DRAW;
        }
        throw new CellIsNotEmptyException();
    }
    public void restartGame(){
        gameField.clear();
        isGameOver = false;
    }
    private ThinkResult think(){
        ArrayList<Coordinates> emptyCellsCoordinates = gameField.getEmptyCellsCoordinates();
        if(emptyCellsCoordinates == null)return null;
        for (Coordinates emptyCellCoordinates : emptyCellsCoordinates) {
            //если мы можем походить в какую-то клетку и выиграть, ходим и выигрываем
            gameField.set(emptyCellCoordinates, CellValue.O);
            Collection<Coordinates> winLine = gameField.findWinLine(CellValue.O);
            if(!winLine.isEmpty()){
                gameField.set(emptyCellCoordinates, CellValue.O);
                return new ThinkResult(winLine, emptyCellCoordinates);
            }
            //если игрок может походить в какую-то клетку и выиграть, ходим туда первыми и не даём ему выиграть
            gameField.set(emptyCellCoordinates, CellValue.X);
            if(gameField.checkWin(CellValue.X)){
                gameField.set(emptyCellCoordinates, CellValue.O);
                return new ThinkResult(emptyCellCoordinates);
            }
            gameField.clearCell(emptyCellCoordinates);
        }
        Coordinates result = findOptimalMove(emptyCellsCoordinates);
        gameField.set(result, CellValue.O);
        return new ThinkResult(result);
    }
    private Coordinates findOptimalMove(Collection<Coordinates> emtpyCoordinates){
        ArrayList<ThinkScore> thinkScores = new ArrayList<>(emtpyCoordinates.size());
        for (Coordinates cellToMove : emtpyCoordinates){
            gameField.set(cellToMove, CellValue.O);
            ThinkScore thinkScore = new ThinkScore(cellToMove);
            calculateScoreForEmptyCell(emtpyCoordinates, CellValue.O, CellValue.X, thinkScore);
            gameField.clearCell(cellToMove);
            thinkScores.add(thinkScore);
        }
        //выбираем максимальное по сумме ничей и побед
        return thinkScores.stream().max(Comparator.comparingLong(e->e.countOfWins + e.countOfDraws)).map(ThinkScore::getCoordinates).or(()->emtpyCoordinates.stream().findFirst()).orElseThrow(()->new RuntimeException("Strange and impossible exception happened"));
    }
    private void calculateScoreForEmptyCell(Collection<Coordinates> emptyCells, CellValue checkWinFigure, CellValue moveFigure, ThinkScore thinkScore){
        // TODO: 21.05.2023 написать функцию расчёта очков для пустой клетки
        //победила наша проверяемая фигура
        //
        if(gameField.checkWin(checkWinFigure)) thinkScore.countOfWins += 1;
        //победила фигура противника
        else if(gameField.checkWin(checkWinFigure.not())) thinkScore.countOfLoses += 1;
        //ничья :(
        else if(gameField.getCountEmptyCells() == 0) thinkScore.countOfDraws += 1;
        else {
            for (Coordinates emptyCellCoordinate : emptyCells) {
                gameField.set(emptyCellCoordinate.x, emptyCellCoordinate.y, moveFigure);
                calculateScoreForEmptyCell(gameField.getEmptyCellsCoordinates(), checkWinFigure, moveFigure.not(), thinkScore);
                gameField.clearCell(emptyCellCoordinate);
            }
        }
    }
}
