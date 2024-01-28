package server.game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.model.game.Board;
import server.model.game.Chip;
import server.model.game.Color;

import java.awt.*;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    private Board board;

    private void printBoard(Board board) {
        // Printing column headers
        System.out.print("  "); // For alignment
        for (int j = 0; j < 8; j++) {
            System.out.print(j + " ");
        }
        System.out.println(); // Move to the next line

        for (int i = 0; i < 8; i++) {
            System.out.print(i + " "); // Print row header
            for (int j = 0; j < 8; j++) {
                server.model.game.Color color = board.getCellColor(i, j);
                if (color == null) {
                    System.out.print("- "); // Unoccupied cell
                } else if (color == server.model.game.Color.WHITE) {
                    System.out.print("W "); // White chip
                } else if (color == server.model.game.Color.BLACK) {
                    System.out.print("B "); // Black chip
                }
            }
            System.out.println(); // Move to the next line after printing each row
        }
    }

    private void printCollectionOnBoard(Collection<Point> points) {
        // Printing column headers
        System.out.print("  "); // For alignment
        for (int j = 0; j < 8; j++) {
            System.out.print(j + " ");
        }
        System.out.println(); // Move to the next line

        for (int i = 0; i < 8; i++) {
            System.out.print(i + " "); // Print row header
            for (int j = 0; j < 8; j++) {
                if (points.contains(new Point(i, j))) {
                    System.out.print("X "); // Mark points in the collection with 'X'
                } else {
                    System.out.print("- "); // Unmarked cell
                }
            }
            System.out.println(); // Move to the next line after printing each row
        }
    }




    @BeforeEach
    void setUp() {
        board = new Board();
    }

    @Test
    void testValidMoves() {
        assertTrue(board.isValidMove(2, 3, server.model.game.Color.BLACK));
        assertTrue(board.isValidMove(3, 2, server.model.game.Color.BLACK));
        assertTrue(board.isValidMove(4, 5, server.model.game.Color.BLACK));
        assertTrue(board.isValidMove(5, 4, server.model.game.Color.BLACK));

    }

    @Test
    void testInvalidOccupied() {
        assertFalse(board.isValidMove(3, 3, server.model.game.Color.BLACK)); // Init black
        assertFalse(board.isValidMove(4, 4, server.model.game.Color.BLACK)); // Init black
        assertFalse(board.isValidMove(4, 3, server.model.game.Color.BLACK));
        assertFalse(board.isValidMove(3, 4, server.model.game.Color.BLACK));
    }

    @Test
    void isInvalidEmptyNoAdjacent() {

        // ROW 0
        assertFalse(board.isValidMove(0,0, server.model.game.Color.BLACK));
        assertFalse(board.isValidMove(0,1, server.model.game.Color.BLACK));
        assertFalse(board.isValidMove(0,2, server.model.game.Color.BLACK));
        assertFalse(board.isValidMove(0,3, server.model.game.Color.BLACK));
        assertFalse(board.isValidMove(0,4, server.model.game.Color.BLACK));
        assertFalse(board.isValidMove(0,5, server.model.game.Color.BLACK));
        assertFalse(board.isValidMove(0,6, server.model.game.Color.BLACK));
        assertFalse(board.isValidMove(0,7, server.model.game.Color.BLACK));

        // ROW 1
        assertFalse(board.isValidMove(1,0, server.model.game.Color.BLACK));
        assertFalse(board.isValidMove(1,1, server.model.game.Color.BLACK));
        assertFalse(board.isValidMove(1,2, server.model.game.Color.BLACK));
        assertFalse(board.isValidMove(1,3, server.model.game.Color.BLACK));
        assertFalse(board.isValidMove(1,4, server.model.game.Color.BLACK));
        assertFalse(board.isValidMove(1,5, server.model.game.Color.BLACK));
        assertFalse(board.isValidMove(1,6, server.model.game.Color.BLACK));
        assertFalse(board.isValidMove(1,7, server.model.game.Color.BLACK));

        // ROW 2
        assertFalse(board.isValidMove(2,0, server.model.game.Color.BLACK));
        assertFalse(board.isValidMove(2,1, server.model.game.Color.BLACK));
        assertFalse(board.isValidMove(2,6, server.model.game.Color.BLACK));
        assertFalse(board.isValidMove(2,7, server.model.game.Color.BLACK));

        // ROW 3
        assertFalse(board.isValidMove(3,0, server.model.game.Color.BLACK));
        assertFalse(board.isValidMove(3,1, server.model.game.Color.BLACK));
        assertFalse(board.isValidMove(3,6, server.model.game.Color.BLACK));
        assertFalse(board.isValidMove(3,7, server.model.game.Color.BLACK));

        // ROW 4
        assertFalse(board.isValidMove(4,0, server.model.game.Color.BLACK));
        assertFalse(board.isValidMove(4,1, server.model.game.Color.BLACK));
        assertFalse(board.isValidMove(4,6, server.model.game.Color.BLACK));
        assertFalse(board.isValidMove(4,7, server.model.game.Color.BLACK));

        // ROW 5
        assertFalse(board.isValidMove(5,0, server.model.game.Color.BLACK));
        assertFalse(board.isValidMove(5,1, server.model.game.Color.BLACK));
        assertFalse(board.isValidMove(5,6, server.model.game.Color.BLACK));
        assertFalse(board.isValidMove(5,7, server.model.game.Color.BLACK));

        // ROW 6
        assertFalse(board.isValidMove(6,0, server.model.game.Color.BLACK));
        assertFalse(board.isValidMove(6,1, server.model.game.Color.BLACK));
        assertFalse(board.isValidMove(6,2, server.model.game.Color.BLACK));
        assertFalse(board.isValidMove(6,3, server.model.game.Color.BLACK));
        assertFalse(board.isValidMove(6,4, server.model.game.Color.BLACK));
        assertFalse(board.isValidMove(6,5, server.model.game.Color.BLACK));
        assertFalse(board.isValidMove(6,6, server.model.game.Color.BLACK));
        assertFalse(board.isValidMove(6,7, server.model.game.Color.BLACK));

        // ROW 7
        assertFalse(board.isValidMove(7,0, server.model.game.Color.BLACK));
        assertFalse(board.isValidMove(7,1, server.model.game.Color.BLACK));
        assertFalse(board.isValidMove(7,2, server.model.game.Color.BLACK));
        assertFalse(board.isValidMove(7,3, server.model.game.Color.BLACK));
        assertFalse(board.isValidMove(7,4, server.model.game.Color.BLACK));
        assertFalse(board.isValidMove(7,5, server.model.game.Color.BLACK));
        assertFalse(board.isValidMove(7,6, server.model.game.Color.BLACK));
        assertFalse(board.isValidMove(7,7, server.model.game.Color.BLACK));

    }

    @Test
    void isInvalidOutOfBounds() {
        assertFalse(board.isValidMove(-1, 1, server.model.game.Color.BLACK));
        assertFalse(board.isValidMove(8, 1, server.model.game.Color.BLACK));
        assertFalse(board.isValidMove(1, -1, server.model.game.Color.BLACK));
        assertFalse(board.isValidMove(1, 8, server.model.game.Color.BLACK));

    }

    @Test
    void testPlaceAtValid() {
        printBoard(board);
        assertNull(board.getCell(2, 3).getChip());
        board.placeChip(2, 3, server.model.game.Color.BLACK);
        assertNotNull(board.getCell(2, 3).getChip());

        printBoard(board);

        assertNull(board.getCell(2, 4).getChip());
        board.placeChip(2,4 , server.model.game.Color.WHITE);
        assertNotNull(board.getCell(2, 4).getChip());

        printBoard(board);

        assertNull(board.getCell(2, 5).getChip());
        board.placeChip(2, 5, server.model.game.Color.BLACK);
        assertNotNull(board.getCell(2, 5).getChip());

        printBoard(board);

        assertNull(board.getCell(4, 2).getChip());
        board.placeChip(4,2, server.model.game.Color.WHITE);
        assertNotNull(board.getCell(4, 2).getChip());

        printBoard(board);
    }

    private void assertChipColor(int x, int y, server.model.game.Color expectedColor) {
        Chip chip = board.getCell(x, y).getChip();
        assertNotNull(chip);
        assertEquals(expectedColor, chip.color());
    }

    @Test
    public void testPlaceAtValidInReturnedCollection() {
        printBoard(board);

        // First move
        assertNull(board.getCell(2, 3).getChip());
        Collection<Point> flippedChips1 = board.placeChip(2, 3, server.model.game.Color.BLACK);
        assertChipColor(2, 3, server.model.game.Color.BLACK);
        assertChipColor(3, 3, server.model.game.Color.BLACK);  // Expected flipped chip

        // Check the flipped chip positions
        assertTrue(flippedChips1.contains(new Point(3, 3)));

        printCollectionOnBoard(flippedChips1);
        printBoard(board);

        // Second move
        assertNull(board.getCell(2, 4).getChip());
        Collection<Point> flippedChips2 = board.placeChip(2, 4, server.model.game.Color.WHITE);
        assertChipColor(2, 4, server.model.game.Color.WHITE);

        // Check if no chips are flipped
        printCollectionOnBoard(flippedChips2);
        assertTrue(flippedChips2.contains(new Point(3, 4)));

        printBoard(board);

        // Third move
        assertNull(board.getCell(2, 5).getChip());
        Collection<Point> flippedChips3 = board.placeChip(2, 5, server.model.game.Color.BLACK);
        assertChipColor(2, 5, server.model.game.Color.BLACK);
        assertChipColor(2, 4, server.model.game.Color.BLACK);  // Expected flipped chip

        // Check the flipped chip positions
        assertTrue(flippedChips3.contains(new Point(2, 4)));

        printCollectionOnBoard(flippedChips3);
        printBoard(board);

        // Fourth move
        assertNull(board.getCell(4, 2).getChip());
        Collection<Point> flippedChips4 = board.placeChip(4, 2, server.model.game.Color.WHITE);
        assertChipColor(4, 2, Color.WHITE);

        // Check if no chips are flipped
        assertTrue(flippedChips4.contains(new Point(4, 3)));

        printCollectionOnBoard(flippedChips4);
        printBoard(board);
    }

}