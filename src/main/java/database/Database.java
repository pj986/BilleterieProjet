package database;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class Database {

    private static final Properties PROPERTIES = new Properties();

    static {
        loadConfiguration();
    }

    private static void loadConfiguration() {
        Path configPath = Path.of("config.properties");

        try {
            if (!Files.exists(configPath)) {
                throw new IllegalStateException(
                        "Le fichier config.properties est introuvable à la racine du projet."
                );
            }

            try (InputStream input = Files.newInputStream(configPath)) {
                PROPERTIES.load(input);
            }

        } catch (Exception e) {
            throw new ExceptionInInitializerError(
                    "Impossible de charger config.properties : " + e.getMessage()
            );
        }
    }

    public static Connection getConnection() {
        try {
            String url = PROPERTIES.getProperty("db.url");
            String user = PROPERTIES.getProperty("db.user");
            String password = PROPERTIES.getProperty("db.password");

            Connection conn = DriverManager.getConnection(
                    url,
                    user,
                    password
            );

            System.out.println("✅ Connexion MySQL AWS réussie");
            return conn;

        } catch (Exception e) {
            System.out.println("❌ Erreur de connexion MySQL AWS");
            e.printStackTrace();
            return null;
        }
    }
}