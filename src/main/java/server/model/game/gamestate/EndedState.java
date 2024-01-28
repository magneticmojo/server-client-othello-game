package server.model.game.gamestate;

import server.model.game.GameResult;
import server.model.game.MoveResult;
import server.model.game.Player;

import java.util.Collections;

public class EndedState implements GameState {

    @Override
    public MoveResult makeMove(int x, int y) {
        // Game has ended, so no moves are allowed
        return new MoveResult(false, x, y, null, Collections.emptyList());
    }

    @Override
    public void switchTurn() {
        // Do nothing, game has ended
    }

    @Override
    public void endGame() {
        // Game is already ended
    }

    @Override
    public GameResult getGameResult() {
        return null;
    }

    @Override
    public void forceEndGame(Player winner) {

    }
}
