package dao;

import database.Database;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import models.Evenement;

public class EvenementDAO {

    public static List<Evenement> getAll() {

        List<Evenement> events = new ArrayList<>();

        String sql = "SELECT * FROM evenement ORDER BY id DESC";

        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Evenement evt = new Evenement(
                        rs.getInt("id"),
                        rs.getString("titre"),
                        rs.getString("description"),
                        rs.getString("date_evt"),
                        rs.getString("lieu"),
                        rs.getDouble("prix"),
                        rs.getString("image")
                );
                events.add(evt);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return events;
    }
}