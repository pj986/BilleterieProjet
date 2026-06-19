package models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Client {

    private int idClient;
    private String nom;
    private String email;
    private String telephone;
    private String adresse;
    private LocalDateTime dateInscription;

    /**
     * Constructeur complet utilisé lors de la lecture depuis MySQL.
     */
    public Client(
            int idClient,
            String nom,
            String email,
            String telephone,
            String adresse,
            LocalDateTime dateInscription
    ) {
        this.idClient = idClient;
        this.nom = nom;
        this.email = email;
        this.telephone = telephone;
        this.adresse = adresse;
        this.dateInscription = dateInscription;
    }

    /**
     * Constructeur utilisé pour créer un nouveau client.
     * L'identifiant et la date seront générés par MySQL.
     */
    public Client(
            String nom,
            String email,
            String telephone,
            String adresse
    ) {
        this.nom = nom;
        this.email = email;
        this.telephone = telephone;
        this.adresse = adresse;
    }

    public int getIdClient() {
        return idClient;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public LocalDateTime getDateInscription() {
        return dateInscription;
    }

    public void setDateInscription(LocalDateTime dateInscription) {
        this.dateInscription = dateInscription;
    }

    /**
     * Retourne une date formatée pour l'affichage dans la TableView.
     */
    public String getDateInscriptionFormatee() {
        if (dateInscription == null) {
            return "";
        }

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        return dateInscription.format(formatter);
    }

    @Override
    public String toString() {
        return nom + " - " + email;
    }
}