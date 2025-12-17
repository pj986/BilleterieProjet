package database;

import java.sql.Connection;
import java.sql.DriverManager;

public class Database {

    private static final String URL =
        "jdbc:mysql://localhost:3306/billetterie?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() {
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ Connexion MySQL réussie");
            return conn;
        } catch (Exception e) {
            System.out.println("❌ Erreur de connexion MySQL");
            e.printStackTrace();
            return null;
        }
    }
}