package server.controller;

import client.MoveRequest;
import server.GameSession;
import server.OthelloServer;
import server.ServerResponse;
import server.model.game.Game;
import server.model.game.GameResult;
import server.model.game.MoveResult;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.UUID;
import java.util.function.Consumer;

public class ClientConnectionHandler implements Runnable {

    private final Socket clientSocket;
    private final OthelloServer server;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private UUID currentGameId;

    public ClientConnectionHandler(Socket clientSocket, OthelloServer server) {
        this.clientSocket = clientSocket;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(clientSocket.getInputStream());

            Game newGame = new Game();

            Consumer<GameResult> onGameEndCallback = this::handleGameEnd;
            newGame.setOnGameEndCallback(onGameEndCallback);

            currentGameId = server.registerNewGame(newGame, this);
            System.out.println("Registered new game with id: " + currentGameId);

            MoveRequest moveRequest;
            while (true) {
                System.out.println("Waiting for move request from client.");
                moveRequest = (MoveRequest) in.readObject();
                System.out.println("Received move request from client." + moveRequest.x() + "," + moveRequest.y());
                handleReceivedMoveRequest(moveRequest);
            }
        } catch (EOFException eof) {
            System.out.println("Client disconnected.");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cleanup();
        }
    }

    private void handleReceivedMoveRequest(MoveRequest moveRequest) {
        GameSession gameSession = server.getGameSession(currentGameId);
        if (!gameSession.isGameReady()) {
            sendServerResponse("MESSAGE", "Waiting for the opponent to join...");
            return;
        }
        makeMove(moveRequest);
    }

    public void sendServerResponse(String type, Serializable message) {
        ServerResponse response = new ServerResponse(type, message);
        try {
            if (!clientSocket.isClosed() && clientSocket.isConnected()) {
                out.writeObject(response);
                out.flush();
            }
        } catch (SocketException se) {
            System.out.println("Client disconnected.");
            GameSession gameSession = server.getGameSession(currentGameId);
            ClientConnectionHandler remainingPlayer = gameSession.getOpponent(this);
            if(remainingPlayer != null) {
                remainingPlayer.sendServerResponse("OPPONENT_DISCONNECTED", "Your opponent has disconnected. You win!");
            }

            cleanup();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleGameEnd(GameResult result) {
        if (result.getWinner() == null) {
            sendServerResponse("GAMEEND", "The game is a draw!");
        } else {
            sendServerResponse("GAMEEND", result.getWinner() + " won the game!");
        }
    }

    private void makeMove(MoveRequest moveRequest) {
        GameSession session = server.getGameSession(currentGameId);

        if (!session.isPlayerTurn(this)) {
            sendServerResponse("MESSAGE", "NOT YOUR TURN");
            return;
        }

        MoveResult result = session.handleMove(moveRequest);
        sendMoveResult(result);
        session.getOpponent(this).sendMoveResult(result);
    }

    public void sendMoveResult(MoveResult result) {
        sendServerResponse("MOVERESULT", result);
    }


    private void cleanup() {
        try {
            in.close();
            out.close();
            clientSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
