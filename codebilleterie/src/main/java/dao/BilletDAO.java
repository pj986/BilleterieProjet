package dao;

import database.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class BilletDAO {

    public static boolean createBillet(int evenementId, String emailClient) {

        String sql = "INSERT INTO billet (evenement_id, email_client, statut) VALUES (?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, evenementId);
            ps.setString(2, emailClient);
            ps.setString(3, "valide");

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}