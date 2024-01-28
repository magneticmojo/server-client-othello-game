package server.model.game;

public enum Color {
    BLACK, WHITE;

    public java.awt.Color toAWTColor() {
        return switch (this) {
            case BLACK -> java.awt.Color.BLACK;
            case WHITE -> java.awt.Color.WHITE;
        };
    }
}
