package server.model.game;

import java.awt.*;
import java.io.Serializable;
import java.util.Collection;

public record MoveResult(boolean success, int x, int y, Color color, Collection<Point> flippedChips) implements Serializable {
}
