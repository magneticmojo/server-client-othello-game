package server.model.game;

import server.model.game.gamestate.GameState;
import server.model.game.gamestate.PlayerTurnState;

import java.util.function.Consumer;

public class Game {
    private final Board board;
    private Player currentPlayer;
    private Player nextPlayer;
    private GameState currentState;
    private Consumer<GameResult> onGameEndCallback;

    public Game() {
        board = new Board();
        this.currentPlayer = new Player(Color.BLACK);
        this.nextPlayer = new Player(Color.WHITE);
        this.currentState = null;
    }

    public void setOnGameEndCallback(Consumer<GameResult> callback) {
        this.onGameEndCallback = callback;
        this.currentState = new PlayerTurnState(this, currentPlayer, onGameEndCallback);
    }

    public Color getCurrentPlayerColor() {
        return currentPlayer.color();
    }

    public boolean hasNoValidMoves(Color color) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board.isValidMove(i, j, color)) {
                    return false;
                }
            }
        }
        return true;
    }

    public int getBlackChips() {
        int blackChips = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Chip chip = board.getCell(i, j).getChip();
                if (chip != null && chip.color() == Color.BLACK) {
                    blackChips++;
                }
            }
        }
        return blackChips;
    }

    public int getWhiteChips() {
        int whiteChips = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Chip chip = board.getCell(i, j).getChip();
                if (chip != null && chip.color() == Color.WHITE) {
                    whiteChips++;
                }
            }
        }
        return whiteChips;
    }

    public Board getBoard() {
        return board;
    }

    public MoveResult makeMove(int x, int y) {
        return currentState.makeMove(x, y);
    }

    public void switchPlayer() {
        Player temp = currentPlayer;
        currentPlayer = nextPlayer;
        nextPlayer = temp;
    }

    public Color getNextPlayerColor() {
        return nextPlayer.color();
    }

    public void switchTurn() {
        currentState.switchTurn();
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentState(GameState newState) {
        this.currentState = newState;
    }

    public GameState getCurrentState() {
        return currentState;
    }

    public Player getNextPlayer() {
        return nextPlayer;
    }

}

