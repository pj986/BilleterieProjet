package dao;

import at.favre.lib.crypto.bcrypt.BCrypt;
import database.Database;
import models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {

    /**
     * Authentifie d'abord un administrateur,
     * puis un utilisateur client.
     */
    public static User login(String email, String password) {

        if (email == null || password == null
                || email.isBlank() || password.isBlank()) {

            System.out.println("[AUTH] Email ou mot de passe vide.");
            return null;
        }

        String normalizedEmail = email.trim().toLowerCase();

        // Recherche prioritaire dans la table administrateur
        User admin = loginAdmin(normalizedEmail, password);

        if (admin != null) {
            return admin;
        }

        // Si ce n'est pas un admin, recherche dans utilisateur
        return loginClient(normalizedEmail, password);
    }

    /**
     * Authentification d'un administrateur.
     */
    private static User loginAdmin(String email, String password) {

        String sql = """
                SELECT id_admin, email, mot_de_passe
                FROM administrateur
                WHERE LOWER(email) = ?
                """;

        try (Connection conn = Database.getConnection()) {

            if (conn == null) {
                System.out.println("[AUTH] Connexion MySQL impossible.");
                return null;
            }

            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, email);

                try (ResultSet rs = ps.executeQuery()) {

                    if (!rs.next()) {
                        System.out.println("[AUTH] Administrateur introuvable.");
                        return null;
                    }

                    String hash = rs.getString("mot_de_passe");

                    if (!isValidBcryptHash(hash)) {
                        System.out.println(
                                "[AUTH] Mot de passe administrateur non hashé avec BCrypt."
                        );
                        return null;
                    }

                    BCrypt.Result result = BCrypt.verifyer()
                            .verify(password.toCharArray(), hash);

                    if (!result.verified) {
                        System.out.println(
                                "[AUTH] Mot de passe administrateur incorrect."
                        );
                        return null;
                    }

                    System.out.println(
                            "[AUTH] Connexion administrateur réussie."
                    );

                    return new User(
                            rs.getInt("id_admin"),
                            rs.getString("email"),
                            "admin"
                    );
                }
            }

        } catch (Exception e) {
            System.out.println(
                    "[AUTH] Erreur pendant l'authentification administrateur."
            );
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Authentification d'un utilisateur client.
     */
    private static User loginClient(String email, String password) {

        String sql = """
                SELECT id, email, mot_de_passe, role
                FROM utilisateur
                WHERE LOWER(email) = ?
                """;

        try (Connection conn = Database.getConnection()) {

            if (conn == null) {
                return null;
            }

            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, email);

                try (ResultSet rs = ps.executeQuery()) {

                    if (!rs.next()) {
                        System.out.println("[AUTH] Utilisateur introuvable.");
                        return null;
                    }

                    String hash = rs.getString("mot_de_passe");

                    if (!isValidBcryptHash(hash)) {
                        System.out.println(
                                "[AUTH] Mot de passe utilisateur non hashé avec BCrypt."
                        );
                        return null;
                    }

                    BCrypt.Result result = BCrypt.verifyer()
                            .verify(password.toCharArray(), hash);

                    if (!result.verified) {
                        System.out.println(
                                "[AUTH] Mot de passe utilisateur incorrect."
                        );
                        return null;
                    }

                    System.out.println(
                            "[AUTH] Connexion utilisateur réussie."
                    );

                    return new User(
                            rs.getInt("id"),
                            rs.getString("email"),
                            rs.getString("role")
                    );
                }
            }

        } catch (Exception e) {
            System.out.println(
                    "[AUTH] Erreur pendant l'authentification utilisateur."
            );
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Crée un compte utilisateur client.
     * Le mot de passe reçu doit déjà être hashé par BCrypt.
     */
    public static boolean createUser(
            String email,
            String hashedPassword
    ) {

        if (email == null || email.isBlank()
                || !isValidBcryptHash(hashedPassword)) {

            System.out.println(
                    "[AUTH] Informations d'inscription invalides."
            );
            return false;
        }

        String sql = """
                INSERT INTO utilisateur
                (email, mot_de_passe, role)
                VALUES (?, ?, ?)
                """;

        try (Connection conn = Database.getConnection()) {

            if (conn == null) {
                System.out.println("[AUTH] Connexion MySQL impossible.");
                return false;
            }

            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, email.trim().toLowerCase());
                ps.setString(2, hashedPassword);
                ps.setString(3, "client");

                boolean success = ps.executeUpdate() > 0;

                if (success) {
                    System.out.println(
                            "[AUTH] Compte utilisateur créé avec succès."
                    );
                }

                return success;
            }

        } catch (Exception e) {
            System.out.println(
                    "[AUTH] Erreur pendant la création du compte."
            );
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Vérifie rapidement le format d'un hash BCrypt.
     */
    private static boolean isValidBcryptHash(String hash) {
        return hash != null
                && hash.length() == 60
                && (
                hash.startsWith("$2a$")
                        || hash.startsWith("$2b$")
                        || hash.startsWith("$2y$")
        );
    }
}