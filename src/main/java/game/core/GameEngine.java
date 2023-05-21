package game.core;

import game.common.Coordinates;

import java.util.function.Consumer;

public interface GameEngine extends MoveMaker{
    void setComputerMoveDisplayer(Consumer<Coordinates> computerMoveDisplayer);
}
