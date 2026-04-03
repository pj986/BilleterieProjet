package dao;

import database.Database;
import models.Evenement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class EvenementDAO {

    public static boolean addEvent(Evenement evenement) {
        String sql = "INSERT INTO evenement (titre, categorie, date_evt, lieu, prix) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, evenement.getTitre());
            ps.setString(2, evenement.getCategorie());
            ps.setString(3, evenement.getDate());
            ps.setString(4, evenement.getLieu());
            ps.setDouble(5, evenement.getPrix());

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public static List<Evenement> getAll() {

        List<Evenement> events = new ArrayList<>();

        String sql = "SELECT * FROM evenement";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Evenement e = new Evenement(
                        rs.getString("titre"),
                        rs.getString("categorie"),
                        rs.getString("date_evt"),
                        rs.getString("lieu"),
                        rs.getDouble("prix"),
                        rs.getString("image") // 🔥 important
                );

                events.add(e);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return events;
    }
    public static boolean deleteEvent(String titre) {

        String sql = "DELETE FROM evenement WHERE titre = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, titre);

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public static boolean updateEvent(Evenement evt) {

        String sql = "UPDATE evenement SET categorie=?, date_evt=?, lieu=?, prix=?, image=? WHERE titre=?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, evt.getCategorie());
            ps.setString(2, evt.getDate());
            ps.setString(3, evt.getLieu());
            ps.setDouble(4, evt.getPrix());
            ps.setString(5, evt.getImage());
            ps.setString(6, evt.getTitre()); // ⚠️ clé (simple)

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}