package server.model.game;

public class GameResult {
    private final Color winner;
    private final int blackChips;
    private final int whiteChips;

    public GameResult(Color winner, int blackChips, int whiteChips) {
        this.winner = winner;
        this.blackChips = blackChips;
        this.whiteChips = whiteChips;
    }

    public Color getWinner() {
        return winner;
    }


    @Override
    public String toString() {
        if (winner == null) {
            return "The game is a draw! Black Chips: " + blackChips + ", White Chips: " + whiteChips;
        } else {
            return winner + " wins! Black Chips: " + blackChips + ", White Chips: " + whiteChips;
        }
    }
}

