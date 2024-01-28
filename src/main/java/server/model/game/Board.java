package server.model.game;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Board {

    private Cell[][] cells;

    public Board() {
        cells = new Cell[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                cells[i][j] = new Cell();
            }
        }

        cells[3][3].setChip(new Chip(server.model.game.Color.WHITE));
        cells[3][4].setChip(new Chip(server.model.game.Color.BLACK));
        cells[4][3].setChip(new Chip(server.model.game.Color.BLACK));
        cells[4][4].setChip(new Chip(server.model.game.Color.WHITE));
    }

    public Cell getCell(int x, int y) {
        return cells[x][y];
    }

    public server.model.game.Color getCellColor(int x, int y) {
        Chip chip = cells[x][y].getChip();
        return (chip != null) ? chip.color() : null;
    }

    public boolean isValidMove(int x, int y, server.model.game.Color color) {

        if (isOutOfBounds(x, y)) {
            return false;
        }

        if (isOccupiedCell(x, y)) {
            return false;
        }

        return canFlip(x, y, color);
    }

    private boolean canFlip(int x, int y, server.model.game.Color color) {
        for (int offsetX = -1; offsetX <= 1; offsetX++) {
            for (int offsetY = -1; offsetY <= 1; offsetY++) {
                if (offsetX == 0 && offsetY == 0) continue; // Skip center

                if (checkDirection(x, y, offsetX, offsetY, color)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkDirection(int x, int y, int offsetX, int offsetY, server.model.game.Color color) {
        int i = x + offsetX;
        int j = y + offsetY;

        if (isOutOfBounds(i, j) || isEmptyCell(i, j) || hasCurrentPlayerColor(i, j, color)) {
            return false;
        }

        while (isInBounds(i, j) && isOccupiedCell(i, j) && hasNextPlayerColor(i, j, color)) {
            i += offsetX;
            j += offsetY;
        }

        return isInBounds(i, j) && isOccupiedCell(i, j) && hasCurrentPlayerColor(i, j, color);
    }

    private boolean isEmptyCell(int x, int y) {
        return cells[x][y].getChip() == null;
    }

    private boolean isOccupiedCell(int x, int y) {
        return cells[x][y].getChip() != null;
    }

    private boolean isInBounds(int x, int y) {
        return x >= 0 && x < 8 && y >= 0 && y < 8;
    }

    private boolean isOutOfBounds(int x, int y) {
        return x < 0 || x > 7 || y < 0 || y > 7;
    }

    private boolean hasCurrentPlayerColor(int x, int y, server.model.game.Color color) {
        return cells[x][y].getChip().color() == color;
    }

    private boolean hasNextPlayerColor(int x, int y, server.model.game.Color color) {
        return cells[x][y].getChip().color() != color;
    }

    public Collection<Point> placeChip(int x, int y, server.model.game.Color color) {
        if (isInvalidMove(x, y, color)) {
            return Collections.emptyList();
        }
        cells[x][y].setChip(new Chip(color));
        return flipChips(x, y, color);
    }

    private boolean isInvalidMove(int x, int y, server.model.game.Color color) {
        return !isValidMove(x, y, color);
    }

    private Collection<Point> flipChips(int x, int y, server.model.game.Color color) {
        List<Point> flippedChips = new ArrayList<>();
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue;
                if (checkDirection(x, y, dx, dy, color)) {
                    flippedChips.addAll(flipInDirection(x, y, dx, dy, color));
                }
            }
        }
        return flippedChips;
    }

    private Collection<Point> flipInDirection(int x, int y, int dx, int dy, Color color) {
        List<Point> flipped = new ArrayList<>();
        int i = x + dx;
        int j = y + dy;
        while (isInBounds(i, j) && isOccupiedCell(i, j) && hasNextPlayerColor(i, j, color)) {
            cells[i][j].setChip(new Chip(color));
            flipped.add(new Point(i, j));
            i += dx;
            j += dy;
        }
        return flipped;
    }

}

