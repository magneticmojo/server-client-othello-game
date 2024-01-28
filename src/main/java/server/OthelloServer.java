package server;

import server.controller.ClientConnectionHandler;
import server.model.game.Game;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OthelloServer {

    private static final int PORT = 6666;
    private ServerSocket serverSocket;
    private final ExecutorService threadPool;
    private final ConcurrentHashMap<UUID, GameSession> activeGames;
    private final Queue<UUID> waitingGames = new LinkedList<>();

    public OthelloServer() {
        this.threadPool = Executors.newFixedThreadPool(10);
        this.activeGames = new ConcurrentHashMap<>();
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(PORT);
            String localIP = InetAddress.getLocalHost().getHostAddress();

            System.out.println("OthelloServer started on IP: " + localIP + " and port: " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientConnectionHandler clientHandler = new ClientConnectionHandler(clientSocket, this);
                System.out.println("CLIENT CONNECTED: ".concat(clientSocket.getInetAddress().getHostAddress()));
                threadPool.execute(clientHandler);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized UUID registerNewGame(Game game, ClientConnectionHandler client) {
        System.out.println("SERVER - REGISTER NEW GAME");
        UUID gameId;

        if (!waitingGames.isEmpty()) {
            gameId = waitingGames.poll();
            GameSession session = activeGames.get(gameId);
            session.setOpponent(client);
        } else {
            gameId = UUID.randomUUID();
            GameSession session = new GameSession(game, client);
            activeGames.put(gameId, session);
            waitingGames.add(gameId);
        }

        GameSession session = activeGames.get(gameId);
        if (session.isGameReady()) {
            client.sendServerResponse("MESSAGE", "Game can start!");
            session.getOpponent(client).sendServerResponse("MESSAGE", "Game can start!");
        }

        return gameId;
    }

    public synchronized GameSession getGameSession(UUID gameId) {
        return activeGames.get(gameId);
    }

    public static void main(String[] args) {
        OthelloServer othelloServer = new OthelloServer();
        othelloServer.start();
    }
}
