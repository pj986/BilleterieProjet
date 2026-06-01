package dao;

import database.Database;
import models.Billet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class BilletDAO {

    // Charger tous les billets
    public static List<Billet> getAll() {
        List<Billet> billets = new ArrayList<>();
        String sql = "SELECT * FROM billet";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Billet b = new Billet();
                b.setId(rs.getInt("id_billet"));
                b.setNumeroUnique(rs.getString("numero_unique"));
                b.setIdClient(rs.getInt("id_client"));
                b.setIdSeance(rs.getInt("id_seance"));
                b.setStatut(rs.getString("statut"));
                b.setPrixFinal(rs.getDouble("prix_final"));
                b.setCategorieTarif(rs.getString("categorie_tarif"));
                b.setPlaceNumero(rs.getString("place_numero"));
                b.setQrCode(rs.getString("qr_code"));
                b.setDateAchat(rs.getTimestamp("date_achat"));
                billets.add(b);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return billets;
    }

    // Rechercher des billets par mot-clé (client, numéro, statut)
    public static List<Billet> search(String keyword) {
        List<Billet> billets = new ArrayList<>();
        String sql = "SELECT b.* FROM billet b " +
                "JOIN client c ON b.id_client = c.id_client " +
                "WHERE b.numero_unique LIKE ? OR c.nom LIKE ? OR b.statut LIKE ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String kw = "%" + keyword + "%";
            ps.setString(1, kw);
            ps.setString(2, kw);
            ps.setString(3, kw);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Billet b = new Billet();
                    b.setId(rs.getInt("id_billet"));
                    b.setNumeroUnique(rs.getString("numero_unique"));
                    b.setIdClient(rs.getInt("id_client"));
                    b.setIdSeance(rs.getInt("id_seance"));
                    b.setStatut(rs.getString("statut"));
                    b.setPrixFinal(rs.getDouble("prix_final"));
                    b.setCategorieTarif(rs.getString("categorie_tarif"));
                    b.setPlaceNumero(rs.getString("place_numero"));
                    b.setQrCode(rs.getString("qr_code"));
                    b.setDateAchat(rs.getTimestamp("date_achat"));
                    billets.add(b);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return billets;
    }

    // Créer un billet complet
    public static boolean create(Billet b) {
        String sql = "INSERT INTO billet (numero_unique, id_client, id_seance, prix_final, categorie_tarif, place_numero, statut, qr_code) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, b.getNumeroUnique());
            ps.setInt(2, b.getIdClient());
            ps.setInt(3, b.getIdSeance());
            ps.setDouble(4, b.getPrixFinal());
            ps.setString(5, b.getCategorieTarif());
            ps.setString(6, b.getPlaceNumero());
            ps.setString(7, b.getStatut());
            ps.setString(8, b.getQrCode());

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Mettre à jour le statut (valide, annulé, remboursé)
    public static boolean updateStatus(int billetId, String statut) {
        String sql = "UPDATE billet SET statut = ? WHERE id_billet = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, statut);
            ps.setInt(2, billetId);
            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}