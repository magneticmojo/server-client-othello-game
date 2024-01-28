package server.controller;

import client.MoveRequest;
import server.model.game.Game;
import server.model.game.MoveResult;

public class OthelloGameController {
    private final Game model;

    public OthelloGameController(Game model) {
        this.model = model;
    }

    public MoveResult handleMove(MoveRequest moveRequest) {
        int x = moveRequest.x();
        int y = moveRequest.y();

        MoveResult result = model.makeMove(x, y);

        if (result.success()) {
            System.out.println(model.getCurrentPlayerColor() + " x=" + x + ", " + "y=" + y);
            model.switchTurn();
        } else {
            if (model.hasNoValidMoves(model.getCurrentPlayerColor())) {
                System.out.println(model.getCurrentPlayerColor() + " NO VALID MOVES - SWITCH TURN");
                model.switchTurn();
            }
            System.out.println("INVALID");
            System.out.println(model.getCurrentPlayerColor() + " x=" + x + ", " + "y=" + y);
        }

        return result;
    }
}
