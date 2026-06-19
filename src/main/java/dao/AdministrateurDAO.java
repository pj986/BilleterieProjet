package dao;

import at.favre.lib.crypto.bcrypt.BCrypt;
import database.Database;
import models.Administrateur;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class AdministrateurDAO {

    public static List<Administrateur> getAll() {
        List<Administrateur> administrateurs = new ArrayList<>();

        String sql = """
                SELECT id_admin, nom, email
                FROM administrateur
                ORDER BY nom ASC, email ASC
                """;

        try (Connection conn = Database.getConnection()) {

            if (conn == null) {
                return administrateurs;
            }

            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    administrateurs.add(
                            new Administrateur(
                                    rs.getInt("id_admin"),
                                    rs.getString("nom"),
                                    rs.getString("email")
                            )
                    );
                }
            }

        } catch (Exception e) {
            System.err.println("[ADMIN DAO] Erreur de récupération.");
            e.printStackTrace();
        }

        return administrateurs;
    }

    public static List<Administrateur> search(String keyword) {
        List<Administrateur> administrateurs = new ArrayList<>();

        String sql = """
                SELECT id_admin, nom, email
                FROM administrateur
                WHERE LOWER(nom) LIKE ?
                   OR LOWER(email) LIKE ?
                ORDER BY nom ASC, email ASC
                """;

        String value = "%" + (
                keyword == null ? "" : keyword.trim().toLowerCase()
        ) + "%";

        try (Connection conn = Database.getConnection()) {

            if (conn == null) {
                return administrateurs;
            }

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, value);
                ps.setString(2, value);

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        administrateurs.add(
                                new Administrateur(
                                        rs.getInt("id_admin"),
                                        rs.getString("nom"),
                                        rs.getString("email")
                                )
                        );
                    }
                }
            }

        } catch (Exception e) {
            System.err.println("[ADMIN DAO] Erreur de recherche.");
            e.printStackTrace();
        }

        return administrateurs;
    }

    public static boolean create(
            String nom,
            String email,
            String plainPassword
    ) {
        String sql = """
                INSERT INTO administrateur
                (nom, email, mot_de_passe)
                VALUES (?, ?, ?)
                """;

        String hash = BCrypt.withDefaults()
                .hashToString(12, plainPassword.toCharArray());

        try (Connection conn = Database.getConnection()) {

            if (conn == null) {
                return false;
            }

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, nom.trim());
                ps.setString(2, email.trim().toLowerCase());
                ps.setString(3, hash);

                return ps.executeUpdate() > 0;
            }

        } catch (Exception e) {
            System.err.println("[ADMIN DAO] Erreur de création.");
            e.printStackTrace();
            return false;
        }
    }

    public static boolean update(
            int idAdmin,
            String nom,
            String email
    ) {
        String sql = """
                UPDATE administrateur
                SET nom = ?,
                    email = ?
                WHERE id_admin = ?
                """;

        try (Connection conn = Database.getConnection()) {

            if (conn == null) {
                return false;
            }

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, nom.trim());
                ps.setString(2, email.trim().toLowerCase());
                ps.setInt(3, idAdmin);

                return ps.executeUpdate() > 0;
            }

        } catch (Exception e) {
            System.err.println("[ADMIN DAO] Erreur de modification.");
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updatePassword(
            int idAdmin,
            String plainPassword
    ) {
        String sql = """
                UPDATE administrateur
                SET mot_de_passe = ?
                WHERE id_admin = ?
                """;

        String hash = BCrypt.withDefaults()
                .hashToString(12, plainPassword.toCharArray());

        try (Connection conn = Database.getConnection()) {

            if (conn == null) {
                return false;
            }

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, hash);
                ps.setInt(2, idAdmin);

                return ps.executeUpdate() > 0;
            }

        } catch (Exception e) {
            System.err.println("[ADMIN DAO] Erreur changement mot de passe.");
            e.printStackTrace();
            return false;
        }
    }

    public static boolean delete(int idAdmin) {
        String sql = """
                DELETE FROM administrateur
                WHERE id_admin = ?
                """;

        try (Connection conn = Database.getConnection()) {

            if (conn == null) {
                return false;
            }

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, idAdmin);

                return ps.executeUpdate() > 0;
            }

        } catch (Exception e) {
            System.err.println("[ADMIN DAO] Erreur de suppression.");
            e.printStackTrace();
            return false;
        }
    }

    public static boolean emailExists(
            String email,
            Integer excludedId
    ) {
        String sql;

        if (excludedId == null) {
            sql = """
                    SELECT COUNT(*) AS total
                    FROM administrateur
                    WHERE LOWER(email) = ?
                    """;
        } else {
            sql = """
                    SELECT COUNT(*) AS total
                    FROM administrateur
                    WHERE LOWER(email) = ?
                      AND id_admin <> ?
                    """;
        }

        try (Connection conn = Database.getConnection()) {

            if (conn == null) {
                return false;
            }

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, email.trim().toLowerCase());

                if (excludedId != null) {
                    ps.setInt(2, excludedId);
                }

                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next() && rs.getInt("total") > 0;
                }
            }

        } catch (Exception e) {
            System.err.println("[ADMIN DAO] Erreur vérification email.");
            e.printStackTrace();
            return false;
        }
    }

    public static int countAll() {
        String sql = """
                SELECT COUNT(*) AS total
                FROM administrateur
                """;

        try (Connection conn = Database.getConnection()) {

            if (conn == null) {
                return 0;
            }

            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                return rs.next() ? rs.getInt("total") : 0;
            }

        } catch (Exception e) {
            System.err.println("[ADMIN DAO] Erreur comptage.");
            e.printStackTrace();
            return 0;
        }
    }
}