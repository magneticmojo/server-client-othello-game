package server.model.game.gamestate;

import server.model.game.*;
import server.model.game.Color;

import java.awt.*;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Consumer;

public class PlayerTurnState implements GameState {
    private final Player player;
    private final Game game;
    private final Consumer<GameResult> onGameEndCallback;

    public PlayerTurnState(Game game, Player player, Consumer<GameResult> onGameEndCallback) {
        this.game = game;
        this.player = player;
        this.onGameEndCallback = onGameEndCallback;
    }

    @Override
    public MoveResult makeMove(int x, int y) {
        Color color = player.color();
        if (game.getBoard().isValidMove(x, y, color)) {
            Collection<Point> flippedChips = game.getBoard().placeChip(x, y, color);
            return new MoveResult(true, x, y, color, flippedChips);
        }
        return new MoveResult(false, x, y, color, Collections.emptyList());
    }

    @Override
    public void switchTurn() {
        if (game.hasNoValidMoves(player.color())) {
            if (game.hasNoValidMoves(game.getNextPlayerColor())) {
                endGame();
                return;
            }
            game.switchPlayer();
            game.setCurrentState(new PlayerTurnState(game, game.getCurrentPlayer(), onGameEndCallback)); // TODO SHOULD I PASS THE CONSUMER HERE??
        } else {
            game.switchPlayer();
            game.setCurrentState(new PlayerTurnState(game, game.getCurrentPlayer(), onGameEndCallback)); // TODO SHOULD I PASS THE CONSUMER HERE??
        }
    }

    @Override
    public void endGame() {
        GameResult gameResult = calculateGameResult();
        onGameEndCallback.accept(gameResult);
    }

    private GameResult calculateGameResult() {
        int blackChips = game.getBlackChips();
        int whiteChips = game.getWhiteChips();
        if (blackChips > whiteChips) {
            return new GameResult(Color.BLACK, blackChips, whiteChips);
        } else if (whiteChips > blackChips) {
            return new GameResult(Color.WHITE, whiteChips, blackChips);
        } else {
            return new GameResult(null, blackChips, whiteChips); // Draw
        }
    }

    @Override
    public GameResult getGameResult() {
        if (game.hasNoValidMoves(Color.BLACK) && game.hasNoValidMoves(Color.WHITE)) {
            return calculateGameResult();
        }
        return null;
    }

    @Override
    public void forceEndGame(Player winner) {
        GameResult gameResult;
        if (winner.color() == Color.BLACK) {
            gameResult = new GameResult(Color.BLACK, game.getBlackChips(), game.getWhiteChips());
        } else {
            gameResult = new GameResult(Color.WHITE, game.getWhiteChips(), game.getBlackChips());
        }
        onGameEndCallback.accept(gameResult);

        game.setCurrentState(new EndedState());
    }


}
