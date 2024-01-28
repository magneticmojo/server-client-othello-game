package client;

import emailservice.Configuration;
import emailservice.Email;
import emailservice.EmailService;
import emailservice.EmailValidator;
import jakarta.mail.MessagingException;

import javax.swing.*;

public class Client {

    private OthelloGameUI ui;

    public Client() {
        SwingUtilities.invokeLater(() -> {
            this.ui = new OthelloGameUI();
        });
    }

    public void connectToServer(String ip, int port) {
        while (this.ui == null) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        new Thread(() -> {
            OthelloClient client = new OthelloClient(ip, port, ui);
            client.connectToServer();
        }).start();
    }

    public static void main(String[] args) {
        if (args.length == 2) {
            if (EmailValidator.isValidEmailAddress(args[1])) {

                Email emailToSend = new Email(args[1]);
                Configuration configuration = new Configuration();
                EmailService emailService = new EmailService();
                try {
                    emailService.sendEmail(emailToSend, configuration);
                } catch (MessagingException e) {
                    System.out.println("Failed to send email.");
                    throw new RuntimeException(e);
                }
            } else {
                System.out.println("Invalid email address.");
                return;
            }
        }

        Client app = new Client();
        app.connectToServer(args[0], 6666);
    }
}
