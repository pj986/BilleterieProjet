package dao;

import database.Database;
import models.Client;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO {

    /**
     * Récupère tous les clients depuis MySQL.
     */
    public static List<Client> getAll() {

        List<Client> clients = new ArrayList<>();

        String sql = """
                SELECT
                    id_client,
                    nom,
                    email,
                    telephone,
                    adresse,
                    date_inscription
                FROM client
                ORDER BY date_inscription DESC
                """;

        try (Connection conn = Database.getConnection()) {

            if (conn == null) {
                System.out.println("[CLIENT DAO] Connexion MySQL impossible.");
                return clients;
            }

            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    Timestamp timestamp = rs.getTimestamp("date_inscription");

                    Client client = new Client(
                            rs.getInt("id_client"),
                            rs.getString("nom"),
                            rs.getString("email"),
                            rs.getString("telephone"),
                            rs.getString("adresse"),
                            timestamp != null ? timestamp.toLocalDateTime() : null
                    );

                    clients.add(client);
                }
            }

        } catch (Exception e) {
            System.out.println("[CLIENT DAO] Erreur lors de la récupération des clients.");
            e.printStackTrace();
        }

        return clients;
    }

    /**
     * Recherche les clients par nom, email, téléphone ou adresse.
     */
    public static List<Client> search(String keyword) {

        List<Client> clients = new ArrayList<>();

        String sql = """
                SELECT
                    id_client,
                    nom,
                    email,
                    telephone,
                    adresse,
                    date_inscription
                FROM client
                WHERE LOWER(nom) LIKE ?
                   OR LOWER(email) LIKE ?
                   OR telephone LIKE ?
                   OR LOWER(adresse) LIKE ?
                ORDER BY nom ASC
                """;

        String searchValue =
                "%" + (keyword == null ? "" : keyword.trim().toLowerCase()) + "%";

        try (Connection conn = Database.getConnection()) {

            if (conn == null) {
                return clients;
            }

            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, searchValue);
                ps.setString(2, searchValue);
                ps.setString(3, searchValue);
                ps.setString(4, searchValue);

                try (ResultSet rs = ps.executeQuery()) {

                    while (rs.next()) {

                        Timestamp timestamp = rs.getTimestamp("date_inscription");

                        clients.add(
                                new Client(
                                        rs.getInt("id_client"),
                                        rs.getString("nom"),
                                        rs.getString("email"),
                                        rs.getString("telephone"),
                                        rs.getString("adresse"),
                                        timestamp != null
                                                ? timestamp.toLocalDateTime()
                                                : null
                                )
                        );
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("[CLIENT DAO] Erreur pendant la recherche.");
            e.printStackTrace();
        }

        return clients;
    }

    /**
     * Récupère un client à partir de son identifiant.
     */
    public static Client findById(int idClient) {

        String sql = """
                SELECT
                    id_client,
                    nom,
                    email,
                    telephone,
                    adresse,
                    date_inscription
                FROM client
                WHERE id_client = ?
                """;

        try (Connection conn = Database.getConnection()) {

            if (conn == null) {
                return null;
            }

            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setInt(1, idClient);

                try (ResultSet rs = ps.executeQuery()) {

                    if (rs.next()) {

                        Timestamp timestamp = rs.getTimestamp("date_inscription");

                        return new Client(
                                rs.getInt("id_client"),
                                rs.getString("nom"),
                                rs.getString("email"),
                                rs.getString("telephone"),
                                rs.getString("adresse"),
                                timestamp != null
                                        ? timestamp.toLocalDateTime()
                                        : null
                        );
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("[CLIENT DAO] Erreur pendant la recherche par ID.");
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Ajoute un nouveau client.
     */
    public static boolean create(Client client) {

        String sql = """
                INSERT INTO client
                (nom, email, telephone, adresse)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection conn = Database.getConnection()) {

            if (conn == null) {
                return false;
            }

            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, client.getNom());
                ps.setString(2, client.getEmail().trim().toLowerCase());
                ps.setString(3, emptyToNull(client.getTelephone()));
                ps.setString(4, emptyToNull(client.getAdresse()));

                return ps.executeUpdate() > 0;
            }

        } catch (Exception e) {
            System.out.println("[CLIENT DAO] Erreur pendant la création du client.");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Met à jour un client existant.
     */
    public static boolean update(Client client) {

        String sql = """
                UPDATE client
                SET nom = ?,
                    email = ?,
                    telephone = ?,
                    adresse = ?
                WHERE id_client = ?
                """;

        try (Connection conn = Database.getConnection()) {

            if (conn == null) {
                return false;
            }

            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, client.getNom());
                ps.setString(2, client.getEmail().trim().toLowerCase());
                ps.setString(3, emptyToNull(client.getTelephone()));
                ps.setString(4, emptyToNull(client.getAdresse()));
                ps.setInt(5, client.getIdClient());

                return ps.executeUpdate() > 0;
            }

        } catch (Exception e) {
            System.out.println("[CLIENT DAO] Erreur pendant la modification du client.");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Supprime un client à partir de son identifiant.
     */
    public static boolean delete(int idClient) {

        String sql = """
                DELETE FROM client
                WHERE id_client = ?
                """;

        try (Connection conn = Database.getConnection()) {

            if (conn == null) {
                return false;
            }

            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setInt(1, idClient);

                return ps.executeUpdate() > 0;
            }

        } catch (Exception e) {
            System.out.println("[CLIENT DAO] Erreur pendant la suppression du client.");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Vérifie si une adresse email est déjà utilisée.
     */
    public static boolean emailExists(String email) {

        String sql = """
                SELECT COUNT(*) AS total
                FROM client
                WHERE LOWER(email) = ?
                """;

        try (Connection conn = Database.getConnection()) {

            if (conn == null) {
                return false;
            }

            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, email.trim().toLowerCase());

                try (ResultSet rs = ps.executeQuery()) {

                    if (rs.next()) {
                        return rs.getInt("total") > 0;
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("[CLIENT DAO] Erreur pendant la vérification de l'email.");
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Compte le nombre total de clients.
     */
    public static int countAll() {

        String sql = """
                SELECT COUNT(*) AS total
                FROM client
                """;

        try (Connection conn = Database.getConnection()) {

            if (conn == null) {
                return 0;
            }

            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return rs.getInt("total");
                }
            }

        } catch (Exception e) {
            System.out.println("[CLIENT DAO] Erreur pendant le comptage des clients.");
            e.printStackTrace();
        }

        return 0;
    }

    private static String emptyToNull(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        return value.trim();
    }
}