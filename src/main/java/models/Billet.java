package models;

import java.sql.Timestamp;

public class Billet {

    private int id;
    private String numeroUnique;
    private int idClient;
    private int idSeance;
    private String statut;
    private double prixFinal;
    private Timestamp dateAchat;
    private String categorieTarif;
    private String placeNumero;
    private String qrCode;

    public Billet() {
    }

    public Billet(int id, String numeroUnique, int idClient, int idSeance, String statut,
                  double prixFinal, Timestamp dateAchat, String categorieTarif,
                  String placeNumero, String qrCode) {
        this.id = id;
        this.numeroUnique = numeroUnique;
        this.idClient = idClient;
        this.idSeance = idSeance;
        this.statut = statut;
        this.prixFinal = prixFinal;
        this.dateAchat = dateAchat;
        this.categorieTarif = categorieTarif;
        this.placeNumero = placeNumero;
        this.qrCode = qrCode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getNumeroUnique() {
        return numeroUnique;
    }

    public void setNumeroUnique(String numeroUnique) {
        this.numeroUnique = numeroUnique;
    }


    public int getIdClient() {
        return idClient;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }


    public int getIdSeance() {
        return idSeance;
    }

    public void setIdSeance(int idSeance) {
        this.idSeance = idSeance;
    }


    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }


    public double getPrixFinal() {
        return prixFinal;
    }

    public void setPrixFinal(double prixFinal) {
        this.prixFinal = prixFinal;
    }


    public Timestamp getDateAchat() {
        return dateAchat;
    }

    public void setDateAchat(Timestamp dateAchat) {
        this.dateAchat = dateAchat;
    }


    public String getCategorieTarif() {
        return categorieTarif;
    }

    public void setCategorieTarif(String categorieTarif) {
        this.categorieTarif = categorieTarif;
    }


    public String getPlaceNumero() {
        return placeNumero;
    }

    public void setPlaceNumero(String placeNumero) {
        this.placeNumero = placeNumero;
    }


    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }
}