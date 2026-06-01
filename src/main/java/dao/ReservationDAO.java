package dao;

import database.Database;
import models.Evenement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO {
    public static void create(String email, int eventId) {

        String sql = "INSERT INTO reservation (user_email, evenement_id) VALUES (?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.setInt(2, eventId);

            stmt.executeUpdate();

            System.out.println("Réservation enregistrée !");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static boolean exists(String email, int eventId) {

        String sql = "SELECT COUNT(*) FROM reservation WHERE user_email = ? AND evenement_id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.setInt(2, eventId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
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

        String sql = "SELECT COUNT(*) FROM reservation WHERE email = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
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
