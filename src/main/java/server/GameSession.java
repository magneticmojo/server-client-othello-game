package server;

import client.MoveRequest;
import server.controller.ClientConnectionHandler;
import server.controller.OthelloGameController;
import server.model.game.Color;
import server.model.game.Game;
import server.model.game.MoveResult;

public class GameSession {
    private final Game game;
    private final ClientConnectionHandler blackPlayer;
    private ClientConnectionHandler whitePlayer;
    private OthelloGameController gameController;
    private boolean isGameReady = false;

    public GameSession(Game game, ClientConnectionHandler blackPlayer) {
        this.game = game;
        this.blackPlayer = blackPlayer;
    }

    public void setOpponent(ClientConnectionHandler whitePlayer) {
        this.whitePlayer = whitePlayer;
        this.gameController = new OthelloGameController(game);
        this.isGameReady = true;
    }

    public MoveResult handleMove(MoveRequest moveRequest) {
        return gameController.handleMove(moveRequest);
    }

    public ClientConnectionHandler getOpponent(ClientConnectionHandler handler) {
        return (handler == blackPlayer) ? whitePlayer : blackPlayer;
    }

    public boolean isPlayerTurn(ClientConnectionHandler handler) {
        return (handler == blackPlayer && game.getCurrentPlayerColor() == Color.BLACK) ||
                (handler == whitePlayer && game.getCurrentPlayerColor() == Color.WHITE);
    }

    public boolean isGameReady() {
        return isGameReady;
    }

    public Game getGame() {
        return game;
    }

}