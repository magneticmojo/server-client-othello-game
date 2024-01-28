package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Collection;

public class OthelloGameUI  {

    private static final int CELL_DIMENSION = 80;
    private static final int FRAME_WIDTH = CELL_DIMENSION * 8;
    private static final int FRAME_HEIGHT = FRAME_WIDTH + (3 * CELL_DIMENSION);
    private static final int FRAME_THICKNESS = 20;
    private static final int CHIP_DIMENSION = CELL_DIMENSION - 20;

    private static final int INIT_BLACK_CHIP_SOUTH_WEST_X = 3;
    private static final int INIT_BLACK_CHIP_SOUTH_WEST_Y = 4;
    private static final int INIT_BLACK_CHIP_NORTH_EAST_X = 4;
    private static final int INIT_BLACK_CHIP_NORTH_EAST_Y = 3;
    private static final int INIT_WHITE_CHIP_NORTH_WEST_X = 3;
    private static final int INIT_WHITE_CHIP_NORTH_WEST_Y = 3;
    private static final int INIT_WHITE_CHIP_SOUTH_EAST_X = 4;
    private static final int INIT_WHITE_CHIP_SOUTH_EAST_Y = 4;

    private static final Color FRAME_COLOR = Color.BLACK;
    private static final Color BACKGROUND_COLOR = Color.BLACK;
    private static final Color BOARD_COLOR = Color.decode("#2D9C18");
    private static final Color BOARD_DARK_COLOR = Color.decode("#1C5C11");

    private static final int CHAT_PANEL_HEIGHT = 3 * CELL_DIMENSION;

    private JFrame frame;
    private JPanel[][] boardCells;
    private JTextArea chatArea;
    private MoveRequestListener moveRequestListener;

    public OthelloGameUI() {

        // FRAME
        frame = new JFrame("Othello");
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        // MAIN PANEL
        JPanel mainPanel = new JPanel(new BorderLayout());
        frame.add(mainPanel);
        mainPanel.setBackground(BACKGROUND_COLOR);

        // NESTED MAIN PANEL
        // Create a nested main panel for framing
        JPanel nestedMainPanel = new JPanel(new BorderLayout());
        nestedMainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.add(nestedMainPanel, BorderLayout.CENTER);

        // Adding spacing panels for frame
        JPanel topSpacing = new JPanel();
        topSpacing.setBackground(FRAME_COLOR);
        topSpacing.setPreferredSize(new Dimension(0, FRAME_THICKNESS));
        nestedMainPanel.add(topSpacing, BorderLayout.NORTH);

        JPanel leftSpacing = new JPanel();
        leftSpacing.setBackground(FRAME_COLOR);
        leftSpacing.setPreferredSize(new Dimension(FRAME_THICKNESS, 0));
        nestedMainPanel.add(leftSpacing, BorderLayout.WEST);

        JPanel rightSpacing = new JPanel();
        rightSpacing.setBackground(FRAME_COLOR);
        rightSpacing.setPreferredSize(new Dimension(FRAME_THICKNESS, 0));
        nestedMainPanel.add(rightSpacing, BorderLayout.EAST);

        JPanel downSpacing = new JPanel();
        downSpacing.setBackground(FRAME_COLOR);
        downSpacing.setPreferredSize(new Dimension(FRAME_THICKNESS, 0));
        nestedMainPanel.add(downSpacing, BorderLayout.SOUTH);

        // BOARD PANEL - CENTER OF NESTED MAIN PANEL
        JPanel gameBoardPanel = new JPanel(new GridLayout(8, 8, 2, 2));
        gameBoardPanel.setBackground(BACKGROUND_COLOR);
        nestedMainPanel.add(gameBoardPanel, BorderLayout.CENTER); // Attach to nested main panel

        initializeBoard(gameBoardPanel);

        // BOTTOM - Chat panel with added spacing
        JPanel gameStatePanel = new JPanel(new BorderLayout());

        Dimension chatPanelSize = new Dimension(FRAME_WIDTH, CHAT_PANEL_HEIGHT);
        gameStatePanel.setBackground(FRAME_COLOR);  // Set to FRAME_COLOR for visual distinction
        gameStatePanel.setPreferredSize(chatPanelSize);

        // Setting up the chat area
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setWrapStyleWord(true);
        chatArea.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(chatArea);
        gameStatePanel.add(scrollPane, BorderLayout.CENTER);

        // Creating a new chat container to include the bottomSpacing
        JPanel chatContainerPanel = new JPanel(new BorderLayout());
        chatContainerPanel.add(gameStatePanel, BorderLayout.SOUTH);

        JPanel bottomSpacing = new JPanel();
        bottomSpacing.setBackground(FRAME_COLOR);
        bottomSpacing.setPreferredSize(new Dimension(0, FRAME_THICKNESS));
        chatContainerPanel.add(bottomSpacing, BorderLayout.NORTH);

        nestedMainPanel.add(chatContainerPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private void initializeBoard(JPanel gameBoardPanel) {
        boardCells = new JPanel[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                boardCells[i][j] = new JPanel();
                boardCells[i][j] = new JPanel(new GridLayout(1,1));

                boardCells[i][j].setBackground(BOARD_COLOR);

                int finalI = i;
                int finalJ = j;

                boardCells[i][j].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        System.out.println("Cell at (" + finalI + ", " + finalJ + ") was clicked.");
                        if (moveRequestListener != null) {
                            moveRequestListener.onMoveRequested(new MoveRequest(finalI, finalJ));
                        }
                    }
                });

                if ((i == INIT_WHITE_CHIP_NORTH_WEST_X && j == INIT_WHITE_CHIP_NORTH_WEST_Y)
                        || (i == INIT_WHITE_CHIP_SOUTH_EAST_X && j == INIT_WHITE_CHIP_SOUTH_EAST_Y)) {
                    boardCells[i][j].add(new OthelloChip(Color.WHITE, CHIP_DIMENSION));
                } else if ((i == INIT_BLACK_CHIP_SOUTH_WEST_X && j == INIT_BLACK_CHIP_SOUTH_WEST_Y)
                        || (i == INIT_BLACK_CHIP_NORTH_EAST_X && j == INIT_BLACK_CHIP_NORTH_EAST_Y)) {
                    boardCells[i][j].add(new OthelloChip(Color.BLACK, CHIP_DIMENSION));
                }

                gameBoardPanel.add(boardCells[i][j]);
            }
        }
    }

    public void addChip(int x, int y, Color chipcolor) {
        System.out.println("UI - ADD");
        boardCells[x][y].add(new OthelloChip(chipcolor, CHIP_DIMENSION));
        boardCells[x][y].revalidate();
        boardCells[x][y].repaint();
    }

    public void flashCellColor(int x, int y) {
        int durationMillis = 100;
        boardCells[x][y].setBackground(BOARD_DARK_COLOR);

        Timer timer = new Timer(durationMillis, e -> {
            boardCells[x][y].setBackground(BOARD_COLOR);
            ((Timer)e.getSource()).stop();
        });
        timer.setRepeats(false);
        timer.start();
    }


    public void flipChips(Collection<Point> chipsToFlip) {
        for (Point point : chipsToFlip) {
            OthelloChip chip = (OthelloChip) boardCells[point.x][point.y].getComponent(0);
            Color newColor = chip.getChipColor() == Color.BLACK ? Color.WHITE : Color.BLACK;
            chip.setChipColor(newColor);
        }
    }

    public void displayMessage(String message) {
        chatArea.append(message + "\n");
        chatArea.setCaretPosition(chatArea.getDocument().getLength());
    }

    public interface MoveRequestListener {
        void onMoveRequested(MoveRequest moveRequest);
    }

    public void setMoveRequestListener(MoveRequestListener listener) {
        this.moveRequestListener = listener;
    }

    private static class OthelloChip extends JPanel {

        private Color chipColor;

        public OthelloChip(Color chipColor, int  dimension) {
            setPreferredSize(new Dimension(dimension, dimension));
            this.chipColor = chipColor;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(chipColor);
            g.fillOval(0, 0, getWidth(), getHeight());
        }

        public void setChipColor(Color chipColor) {
            this.chipColor = chipColor;
            repaint();
        }

        public Color getChipColor() {
            return chipColor;
        }
    }

    public void showEndGameAlert(String message) {
        JOptionPane.showMessageDialog(frame, message, "Game Over", JOptionPane.INFORMATION_MESSAGE);
    }

}