package dao;

import database.Database;
import models.User;
import at.favre.lib.crypto.bcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {

    public static User login(String email, String password) {

        String sql = "SELECT * FROM utilisateur WHERE email = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email.toLowerCase().trim());

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                String hashedPassword = rs.getString("mot_de_passe");

                BCrypt.Result result = BCrypt.verifyer()
                        .verify(password.toCharArray(), hashedPassword);

                if (result.verified) {

                    return new User(
                            rs.getInt("id"),
                            rs.getString("email"),
                            rs.getString("role")
                    );
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // 🔹 AJOUTE CETTE MÉTHODE

    public static boolean createUser(String email, String hashedPassword) {

        String sql = "INSERT INTO utilisateur (email, mot_de_passe, role) VALUES (?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email.toLowerCase().trim());
            ps.setString(2, hashedPassword);
            ps.setString(3, "client");

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}