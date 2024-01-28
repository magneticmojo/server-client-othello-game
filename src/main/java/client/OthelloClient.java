package client;

import server.ServerResponse;
import server.model.game.MoveResult;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class OthelloClient {

    private String serverIp;
    private int serverPort;
    private Socket clientSocket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private OthelloGameUI ui;

    public OthelloClient(String serverIp, int serverPort, OthelloGameUI ui) {
        this.serverIp = serverIp;
        this.serverPort = serverPort;
        this.ui = ui;

        ui.setMoveRequestListener(moveRequest -> {
            try {
                sendMove(moveRequest.x(), moveRequest.y());
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Failed to send move to server.");
            }
        });
    }

    public void connectToServer() {
        try {
            clientSocket = new Socket(serverIp, serverPort);
            System.out.println("Connected to server.");

            out = new ObjectOutputStream(clientSocket.getOutputStream());
            in = new ObjectInputStream(clientSocket.getInputStream());

            new Thread(this::listenToServer).start();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Unable to connect to server.");
        }
    }

    private void listenToServer() {
        try {
            while (true) {
                ServerResponse response = (ServerResponse) in.readObject();
                SwingUtilities.invokeLater(() -> {
                    processServerResponse(response);
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error listening to server updates.");
        }
    }


    private void handleMoveResult(MoveResult moveResult) {
        if (moveResult.success()) {
            ui.addChip(moveResult.x(), moveResult.y(), moveResult.color().toAWTColor());

            displayInTextArea(moveResult.color() + " PLAYER:\n" + "Valid move: " + moveResult.x() + ", " + moveResult.y());


            if (!moveResult.flippedChips().isEmpty()) {


                ui.flipChips(moveResult.flippedChips());
                StringBuilder message = new StringBuilder("Flipped Chips: ");
                for (Point point : moveResult.flippedChips()) {
                    message.append(String.format("(%d, %d) ", point.x, point.y));
                }

                displayInTextArea(message.toString().trim());
            }
        } else {
            displayInTextArea(moveResult.color() + " PLAYER:\n" + "Invalid move: " + moveResult.x() + ", " + moveResult.y());

            ui.flashCellColor(moveResult.x(), moveResult.x());
        }
    }

    private void processServerResponse(ServerResponse response) {
        switch (response.getMessageType()) {
            case "MOVERESULT":
                MoveResult moveResult = (MoveResult) response.getData();
                handleMoveResult(moveResult);
                break;
            case "MESSAGE":
                String message = (String) response.getData();
                displayInTextArea(message);
                break;
            case "OPPONENT_DISCONNECTED":
                String disconnectMessage = (String) response.getData();
                displayInTextArea(disconnectMessage);
                endGame();
                break;
            default:
                System.out.println("Unknown server response type: " + response.getMessageType());
                break;
        }
    }

    private void endGame() {
        ui.showEndGameAlert("The game has ended!");
        closeClient();
    }

    private void closeClient() {
        try {
            if(in != null) in.close();
            if(out != null) out.close();
            if(clientSocket != null) clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    private void displayInTextArea(String message) {
        ui.displayMessage(message);
    }


    public void sendMove(int x, int y) throws IOException {
        System.out.println("SENDING MOVE: " + x + "," + y);

        MoveRequest move = new MoveRequest(x, y);
        out.writeObject(move);
    }
}