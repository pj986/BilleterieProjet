package models;

import java.time.LocalDate;

public class Tarif {

    private int idTarif;
    private String libelle;
    private String typeReduction;
    private double valeur;
    private boolean actif;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private String description;

    public Tarif(
            int idTarif,
            String libelle,
            String typeReduction,
            double valeur,
            boolean actif,
            LocalDate dateDebut,
            LocalDate dateFin,
            String description
    ) {
        this.idTarif = idTarif;
        this.libelle = libelle;
        this.typeReduction = typeReduction;
        this.valeur = valeur;
        this.actif = actif;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.description = description;
    }

    public Tarif(
            String libelle,
            String typeReduction,
            double valeur,
            boolean actif,
            LocalDate dateDebut,
            LocalDate dateFin,
            String description
    ) {
        this.libelle = libelle;
        this.typeReduction = typeReduction;
        this.valeur = valeur;
        this.actif = actif;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.description = description;
    }

    public int getIdTarif() {
        return idTarif;
    }

    public void setIdTarif(int idTarif) {
        this.idTarif = idTarif;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getTypeReduction() {
        return typeReduction;
    }

    public void setTypeReduction(String typeReduction) {
        this.typeReduction = typeReduction;
    }

    public double getValeur() {
        return valeur;
    }

    public void setValeur(double valeur) {
        this.valeur = valeur;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getValeurFormatee() {
        if ("pourcentage".equalsIgnoreCase(typeReduction)) {
            return valeur + " %";
        }

        return valeur + " €";
    }

    public String getStatutFormate() {
        return actif ? "Actif" : "Inactif";
    }

    public String getDateDebutFormatee() {
        return dateDebut == null ? "-" : dateDebut.toString();
    }

    public String getDateFinFormatee() {
        return dateFin == null ? "-" : dateFin.toString();
    }
}