package server.model.game.gamestate;

import server.model.game.GameResult;
import server.model.game.MoveResult;
import server.model.game.Player;

public interface GameState {
    MoveResult makeMove(int x, int y);
    void switchTurn();
    void endGame();
    GameResult getGameResult();
    void forceEndGame(Player winner);
}

