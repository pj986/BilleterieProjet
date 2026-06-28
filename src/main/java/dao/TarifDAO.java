package dao;

import database.Database;
import models.Tarif;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TarifDAO {

    public static List<Tarif> getAll() {

        List<Tarif> tarifs = new ArrayList<>();

        String sql = """
                SELECT
                    id_tarif,
                    libelle,
                    type_reduction,
                    valeur,
                    actif,
                    date_debut,
                    date_fin,
                    description
                FROM tarif
                ORDER BY actif DESC, libelle ASC
                """;

        try (Connection conn = Database.getConnection()) {

            if (conn == null) {
                return tarifs;
            }

            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    tarifs.add(mapResultSet(rs));
                }
            }

        } catch (Exception e) {
            System.err.println("[TARIF DAO] Erreur récupération.");
            e.printStackTrace();
        }

        return tarifs;
    }

    public static List<Tarif> search(String keyword) {

        List<Tarif> tarifs = new ArrayList<>();

        String sql = """
                SELECT
                    id_tarif,
                    libelle,
                    type_reduction,
                    valeur,
                    actif,
                    date_debut,
                    date_fin,
                    description
                FROM tarif
                WHERE LOWER(libelle) LIKE ?
                   OR LOWER(description) LIKE ?
                   OR LOWER(type_reduction) LIKE ?
                ORDER BY libelle ASC
                """;

        String value = "%" +
                (keyword == null ? "" : keyword.trim().toLowerCase())
                + "%";

        try (Connection conn = Database.getConnection()) {

            if (conn == null) {
                return tarifs;
            }

            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, value);
                ps.setString(2, value);
                ps.setString(3, value);

                try (ResultSet rs = ps.executeQuery()) {

                    while (rs.next()) {
                        tarifs.add(mapResultSet(rs));
                    }
                }
            }

        } catch (Exception e) {
            System.err.println("[TARIF DAO] Erreur recherche.");
            e.printStackTrace();
        }

        return tarifs;
    }

    public static boolean create(Tarif tarif) {

        String sql = """
                INSERT INTO tarif
                (
                    libelle,
                    type_reduction,
                    valeur,
                    actif,
                    date_debut,
                    date_fin,
                    description
                )
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = Database.getConnection()) {

            if (conn == null) {
                return false;
            }

            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                fillStatement(ps, tarif);

                return ps.executeUpdate() > 0;
            }

        } catch (Exception e) {
            System.err.println("[TARIF DAO] Erreur création.");
            e.printStackTrace();
            return false;
        }
    }

    public static boolean update(Tarif tarif) {

        String sql = """
                UPDATE tarif
                SET libelle = ?,
                    type_reduction = ?,
                    valeur = ?,
                    actif = ?,
                    date_debut = ?,
                    date_fin = ?,
                    description = ?
                WHERE id_tarif = ?
                """;

        try (Connection conn = Database.getConnection()) {

            if (conn == null) {
                return false;
            }

            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                fillStatement(ps, tarif);
                ps.setInt(8, tarif.getIdTarif());

                return ps.executeUpdate() > 0;
            }

        } catch (Exception e) {
            System.err.println("[TARIF DAO] Erreur modification.");
            e.printStackTrace();
            return false;
        }
    }

    public static boolean delete(int idTarif) {

        String sql = """
                DELETE FROM tarif
                WHERE id_tarif = ?
                """;

        try (Connection conn = Database.getConnection()) {

            if (conn == null) {
                return false;
            }

            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setInt(1, idTarif);

                return ps.executeUpdate() > 0;
            }

        } catch (Exception e) {
            System.err.println("[TARIF DAO] Erreur suppression.");
            e.printStackTrace();
            return false;
        }
    }

    public static boolean toggleActive(
            int idTarif,
            boolean newStatus
    ) {

        String sql = """
                UPDATE tarif
                SET actif = ?
                WHERE id_tarif = ?
                """;

        try (Connection conn = Database.getConnection()) {

            if (conn == null) {
                return false;
            }

            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setBoolean(1, newStatus);
                ps.setInt(2, idTarif);

                return ps.executeUpdate() > 0;
            }

        } catch (Exception e) {
            System.err.println("[TARIF DAO] Erreur changement statut.");
            e.printStackTrace();
            return false;
        }
    }

    public static int countActive() {

        String sql = """
                SELECT COUNT(*) AS total
                FROM tarif
                WHERE actif = TRUE
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
            e.printStackTrace();
            return 0;
        }
    }

    private static Tarif mapResultSet(ResultSet rs)
            throws Exception {

        Date dateDebut = rs.getDate("date_debut");
        Date dateFin = rs.getDate("date_fin");

        return new Tarif(
                rs.getInt("id_tarif"),
                rs.getString("libelle"),
                rs.getString("type_reduction"),
                rs.getDouble("valeur"),
                rs.getBoolean("actif"),
                dateDebut == null ? null : dateDebut.toLocalDate(),
                dateFin == null ? null : dateFin.toLocalDate(),
                rs.getString("description")
        );
    }

    private static void fillStatement(
            PreparedStatement ps,
            Tarif tarif
    ) throws Exception {

        ps.setString(1, tarif.getLibelle());
        ps.setString(2, tarif.getTypeReduction());
        ps.setDouble(3, tarif.getValeur());
        ps.setBoolean(4, tarif.isActif());

        if (tarif.getDateDebut() == null) {
            ps.setNull(5, java.sql.Types.DATE);
        } else {
            ps.setDate(
                    5,
                    Date.valueOf(tarif.getDateDebut())
            );
        }

        if (tarif.getDateFin() == null) {
            ps.setNull(6, java.sql.Types.DATE);
        } else {
            ps.setDate(
                    6,
                    Date.valueOf(tarif.getDateFin())
            );
        }

        ps.setString(7, tarif.getDescription());
    }
}