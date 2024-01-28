package client;

import java.io.Serializable;

public record MoveRequest(int x, int y) implements Serializable {
}
