package emailservice;

import java.io.InputStream;
import java.util.Properties;

/**
 * Represents the configuration for sending emails. This includes
 * server details, port, username, and password, loaded from
 * the "config.properties" file.
 */
public class Configuration {
    private String emailServer;
    private int port;
    private String username;
    private String password;

    /**
     * Constructs a Configuration instance and loads properties from the "config.properties" file.
     */
    public Configuration() {
        Properties properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            properties.load(input);

            this.emailServer = properties.getProperty("emailservice.server", "smtp.gmail.com");
            this.port = Integer.parseInt(properties.getProperty("emailservice.port", "587"));
            this.username = properties.getProperty("emailservice.address");
            this.password = properties.getProperty("emailservice.password");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getEmailServer() {
        return emailServer;
    }

    public int getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
