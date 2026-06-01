package dao;

import database.Database;
import models.User;
import at.favre.lib.crypto.bcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {

    // Méthode de connexion : vérification email et mot de passe
    public static User login(String email, String password) {

        String sql = "SELECT * FROM utilisateur WHERE email = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // Normalisation de l'email (minuscule et trim)
            ps.setString(1, email.toLowerCase().trim());

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                // Récupérer le mot de passe haché
                String hashedPassword = rs.getString("mot_de_passe");

                // Vérifier si le mot de passe est correct avec BCrypt
                BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), hashedPassword);

                // Si le mot de passe est correct, on retourne un objet User
                if (result.verified) {
                    return new User(
                            rs.getInt("id"),
                            rs.getString("email"),
                            rs.getString("role")
                    );
                }
            }

        } catch (Exception e) {
            // Exception lors de la connexion, enregistrement dans un log (ou renvoi d'une exception spécifique)
            e.printStackTrace();
        }

        // Retourner null si l'utilisateur n'existe pas ou le mot de passe est incorrect
        return null;
    }

    // Méthode pour enregistrer un nouvel utilisateur
    public static boolean createUser(String email, String hashedPassword) {

        String sql = "INSERT INTO utilisateur (email, mot_de_passe, role) VALUES (?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email.toLowerCase().trim()); // Enlever les espaces et normaliser en minuscule
            ps.setString(2, hashedPassword);
            ps.setString(3, "client"); // Par défaut, le rôle d'un utilisateur est "client"

            int rows = ps.executeUpdate();
            return rows > 0; // Si l'insertion est réussie, on retourne true

        } catch (Exception e) {
            e.printStackTrace(); // Gestion d'erreur
            return false; // Retourner false si une erreur se produit
        }
    }
}