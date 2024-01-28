package emailservice;

public class Email {

    private final String to;
    private final String subject;
    private final String message;

    public Email(String to) {
        this.to = to;
        this.subject = "Invitation to play Othello";
        this.message = "Connect to game session with:\nIP: " + LocalIP.getLocalIP();
    }

    public String getTo() {
        return to;
    }

    public String getSubject() {
        return subject;
    }

    public String getMessage() {
        return message;
    }
}