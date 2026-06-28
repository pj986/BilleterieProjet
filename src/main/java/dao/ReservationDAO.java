package dao;

import database.Database;
import models.Evenement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO {
    public static boolean create(String email, int evenementId) {

        String sql = """
            INSERT INTO reservation
            (user_email, evenement_id)
            VALUES (?, ?)
            """;

        try (Connection conn = Database.getConnection()) {

            if (conn == null) {
                return false;
            }

            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, email.trim().toLowerCase());
                ps.setInt(2, evenementId);

                return ps.executeUpdate() > 0;
            }

        } catch (Exception e) {
            System.err.println(
                    "[RESERVATION DAO] Erreur lors de la création."
            );
            e.printStackTrace();
            return false;
        }
    }
    public static boolean exists(String email, int evenementId) {

        if (email == null || email.isBlank()) {
            return false;
        }

        String sql = """
            SELECT COUNT(*) AS total
            FROM reservation
            WHERE LOWER(user_email) = LOWER(?)
              AND evenement_id = ?
            """;

        try (Connection conn = Database.getConnection()) {

            if (conn == null) {
                return false;
            }

            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, email.trim());
                ps.setInt(2, evenementId);

                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next() && rs.getInt("total") > 0;
                }
            }

        } catch (Exception e) {
            System.err.println("[RESERVATION DAO] Erreur vérification réservation.");
            e.printStackTrace();
            return false;
        }
    }
    public static List<Evenement> getByUser(String email) {

        List<Evenement> list = new ArrayList<>();

        String sql = """
        SELECT e.*
        FROM reservation r
        JOIN evenement e ON r.evenement_id = e.id
        WHERE r.user_email = ?
        ORDER BY r.date_reservation DESC
    """;

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                Evenement e = new Evenement(

                        rs.getString("titre"),
                        rs.getString("categorie"),
                        rs.getString("date"),
                        rs.getString("lieu"),
                        rs.getDouble("prix"),
                        rs.getString("image")
                );

                list.add(e);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public static int countByEmail(String email) {

        if (email == null || email.isBlank()) {
            return 0;
        }

        String sql = """
            SELECT COUNT(*) AS total
            FROM reservation
            WHERE LOWER(user_email) = LOWER(?)
            """;

        try (Connection conn = Database.getConnection()) {

            if (conn == null) {
                return 0;
            }

            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, email.trim());

                try (ResultSet rs = ps.executeQuery()) {

                    if (rs.next()) {
                        return rs.getInt("total");
                    }
                }
            }

        } catch (Exception e) {
            System.err.println(
                    "[RESERVATION DAO] Erreur lors du comptage pour : " + email
            );
            e.printStackTrace();
        }

        return 0;
    }
    public static int countAll() {

        String sql = "SELECT COUNT(*) FROM reservation";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
}
